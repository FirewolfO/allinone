package com.firewolf.rule.engine.utils.sql;

import com.firewolf.rule.engine.entity.EntityMetaInfo;
import com.firewolf.rule.engine.enums.LikeType;
import com.firewolf.rule.engine.enums.OrderType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SQL构建工具
 */
public class SqlBuilder {


    /**
     * 删除语句前缀
     */
    private static final String DELETE_PREFIX = "delete from ";

    /**
     * 插入语句前缀
     */
    private static final String INSERT_PREFIX = "insert into ";

    /**
     * 查询语句前缀
     */
    private static final String QUERY_PREFIX = "select * from ";

    /**
     * 更新语句前缀
     */
    private static final String UPDATE_PREFIX = "update ";

    /**
     * 连接符 and
     */
    private static final String SEPARATOR_AND = " and ";

    /**
     * 连接符 or
     */
    private static final String SEPARATOR_OR = " or ";

    /**
     * 连接符 ,
     */
    private static final String SEPARATOR_COMMA = " , ";


    /**
     * order by
     */
    private static final String ORDERBY = " order by ";

    /**
     * like 连接符
     */
    private static final String JOIN_LIKE = " '%' ";


    /**
     * 主表无用数据删除SQL：delete from event_rule where id not in (select distinct rule_id from event_rule_item)
     *
     * @param mainMetaInfo
     * @param subMetaInfo
     * @return
     */
    public static String buildUnusedDeleteSql(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo) {
        String sql = "delete from " + mainMetaInfo.getTable() + " where " + mainMetaInfo.getIdColumn() + " not in ( select distinct "
                + subMetaInfo.getForeignKeyColumn() + " from " + subMetaInfo.getTable() + " )";
        return sql;
    }


    /**
     * 构建count语句 ： select count(1),concat(device_id,event_level_id,event_type) from event_rule_item where device_id in (1) and event_type in (1,2,3) and event_level_id in (1) group by device_id,event_level_id,event_type
     *
     * @param subMetaInfo
     * @param uniqueColumns
     * @return
     */
    public static String buildUniqueCountSql(EntityMetaInfo subMetaInfo, List<String> uniqueColumns) {
        String sql = "SELECT COUNT(1) c, CONCAT(";
        for (String column : uniqueColumns) {
            sql += column + ",\",\",";
        }
        sql = StringUtils.removeEnd(sql, ",");
        sql += ") unikey FROM " + subMetaInfo.getTable() + " where ";
        for (String column : uniqueColumns) {
            sql += column + " in (:" + column + ") and ";
        }
        sql = StringUtils.removeEnd(sql, " and ");
        sql += " GROUP BY " + StringUtils.join(uniqueColumns, ",");
        return sql;
    }

    /**
     * 构建唯一性查询语句 SELECT name,CONCAT( a,",",b,",",c) unikey FROM event_rule_item where (name=:name0 or (a=:a0 and b=:b0 and c=:c0)) or (name=:name1 or (a=:a1 and b=:b1 and c=:c1))
     *
     * @param table            表名
     * @param uniqueKeyColumns 独立主键列
     * @param unionKeyColumns  联合主键列
     * @param size             参数数量
     * @return
     */
    public static String buildUniqueQuerySql(String table, List<String> uniqueKeyColumns, List<String> unionKeyColumns, int size) {
        if (CollectionUtils.isEmpty(uniqueKeyColumns) && CollectionUtils.isEmpty(unionKeyColumns)) {
            throw new RuntimeException("there is no unique key ! ");
        }
        String sql = "SELECT ";
        if (CollectionUtils.isNotEmpty(uniqueKeyColumns)) {
            sql += uniqueKeyColumns.stream().collect(Collectors.joining(","));
        }
        if (CollectionUtils.isNotEmpty(unionKeyColumns)) {
            if (CollectionUtils.isNotEmpty(uniqueKeyColumns)) {
                sql += ",";
            }
            sql += "CONCAT( ";
            for (String column : unionKeyColumns) {
                sql += column + ",\",\",";
            }
            sql = StringUtils.removeEnd(sql, ",\",\",");
            sql += ") unikey ";
        }
        sql = sql + "FROM " + table;


        if (size > 0) {
            sql += " where ";
            for (int i = 0; i < size; i++) {
                final int index = i;
                String oneItem = "(";
                if (CollectionUtils.isNotEmpty(uniqueKeyColumns)) {
                    oneItem = oneItem + uniqueKeyColumns.stream().map(column -> column + "=:" + (column + index)).collect(Collectors.joining(SEPARATOR_OR));
                }
                if (CollectionUtils.isNotEmpty(unionKeyColumns)) {
                    if (CollectionUtils.isNotEmpty(uniqueKeyColumns)) {
                        oneItem = oneItem + SEPARATOR_OR;
                    }
                    oneItem = oneItem + unionKeyColumns.stream().map(column -> column + "=:" + (column + index)).collect(Collectors.joining(SEPARATOR_AND, "(", ")"));
                }
                sql = sql + oneItem + ")" + SEPARATOR_OR;
            }
            sql = StringUtils.removeEnd(sql, SEPARATOR_OR);
        }
        return sql;
    }


    /**
     * 构建查询语句: select * from event_rule where id in (:id)
     *
     * @param sqlBuildParam 构建参数
     * @return
     */
    public static String buildQuerySql(SqlBuildParam sqlBuildParam) {
        return QUERY_PREFIX + sqlBuildParam.getTable() + buildWhere(sqlBuildParam.getParams(), sqlBuildParam.getLike()) + buildOrderBy(sqlBuildParam.getOrderBy()) + buildLimit(sqlBuildParam.getLimit());
    }

    /**
     * 构建删除SQL： delete from event_rule where device_id in (:device_id) and id =:id
     *
     * @param table  表名
     * @param params 参数
     * @return
     */
    public static String buildDeleteSql(String table, Map<String, Object> params) {
        return DELETE_PREFIX + table + buildWhere(params, new HashMap<>());
    }


    /**
     * 构建删除冲突的SQL：delete from event_rule_item where rule_id=:rule_id or (event_type=:event_type and device_id=:device_id)
     *
     * @param table            表名
     * @param uniqueKeyColumns 独立冲突字段
     * @param unionKeyColumns  联合冲突字段
     * @param size             数据条数
     * @return
     */
    public static String buildDeleteConflictSql(String table, List<String> uniqueKeyColumns, List<String> unionKeyColumns, int size) {
        if (CollectionUtils.isEmpty(uniqueKeyColumns) && CollectionUtils.isEmpty(unionKeyColumns)) {
            throw new RuntimeException("no unique columns !");
        }
        String sql = DELETE_PREFIX + table + " where ";
        for (int i = 0; i < size; i++) {
            sql += "(";
            final int index = i;
            if (CollectionUtils.isNotEmpty(uniqueKeyColumns)) {
                sql += uniqueKeyColumns.stream().map(column -> column + "= :" + (column + index)).collect(Collectors.joining(SEPARATOR_OR));
            }
            if (CollectionUtils.isNotEmpty(unionKeyColumns)) {
                if (CollectionUtils.isNotEmpty(uniqueKeyColumns)) {
                    sql += SEPARATOR_OR;
                }
                sql += unionKeyColumns.stream().map(column -> column + "= :" + (column + index)).collect(Collectors.joining(SEPARATOR_AND, "(", ")"));
            }
            sql = sql + ")" + SEPARATOR_OR;
        }
        sql = StringUtils.removeEnd(sql, SEPARATOR_OR);

        return sql;
    }


    /**
     * 构建更新语句: update event_rule set is_enable =:is_enable , time_plan =:time_plan , name =:name , event_linkage_types =:event_linkage_types where id =:id
     *
     * @param table        表名
     * @param updateParams 更新参数
     * @param whereParams  条件参数
     * @return
     */
    public static String buildUpdateSql(String table, Map<String, Object> updateParams, Map<String, Object> whereParams) {
        String sql = UPDATE_PREFIX + table + " set " + buildCondition(updateParams, new HashMap<>(), SEPARATOR_COMMA) + buildWhere(whereParams, new HashMap<>());
        return sql;
    }


    /**
     * 构建插入语句
     *
     * @param table
     * @param params
     */
    public static String buildInsertSql(String table, Map<String, Object> params) {
        String insertSql = INSERT_PREFIX + table + " set " + buildCondition(params, new HashMap<>(), SEPARATOR_COMMA);
        return insertSql;
    }


    /**
     * 构建where条件 ： where device_id in (:device_id) and id =:id
     *
     * @param params
     * @return
     */
    private static String buildWhere(Map<String, Object> params, Map<String, LikeType> likeParams) {
        params.remove(null);
        if (MapUtils.isNotEmpty(params))
            return " where " + buildCondition(params, likeParams, SEPARATOR_AND);
        return "";
    }

    /**
     * 构建order by语句
     *
     * @param orderByMap
     * @return
     */
    private static String buildOrderBy(Map<String, OrderType> orderByMap) {
        if (MapUtils.isEmpty(orderByMap))
            return "";
        String sql = orderByMap.entrySet().stream().map(entry -> entry.getKey() + " " + entry.getValue().getDescription()).collect(Collectors.joining(SEPARATOR_COMMA));
        return ORDERBY + sql;
    }

    /**
     * 构建limit语句
     *
     * @param limit
     * @return
     */
    private static String buildLimit(Limit limit) {
        String limitSql = limit.getStart() < 0 ? "" : " limit " + limit.getStart() + "," + limit.getSize();
        return limitSql;
    }

    /**
     * 构建条件 ： name=:name and event_type=:event_type
     *
     * @param params     参数
     * @param separator  分隔符
     * @param likeParams 模糊参数
     * @return
     */
    private static String buildCondition(final Map<String, Object> params, final Map<String, LikeType> likeParams, final String separator) {
        if (MapUtils.isNotEmpty(params)) {
            String result = params.entrySet().stream().map(entry -> {
                Object value = entry.getValue();
                String key = entry.getKey();
                if (value == null) {
                    return null;
                }
                if ((value.getClass().isArray() || value instanceof Collection)) {//数组或者集合
                    return key + " in (:" + key + ")";
                } else {
                    // 判断是不是Like参数
                    if (likeParams.containsKey(key)) {
                        LikeType likeType = likeParams.get(key);
                        return key + " like " + JOIN_LIKE + ":" + key + JOIN_LIKE;
                    }
                }
                return key + " =:" + key;
            }).filter(StringUtils::isNotEmpty).collect(Collectors.joining(separator));
            return result;
        }
        return "";
    }
}
