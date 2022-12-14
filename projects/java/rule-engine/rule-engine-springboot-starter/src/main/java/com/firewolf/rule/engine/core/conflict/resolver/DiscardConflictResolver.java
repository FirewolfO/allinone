package com.firewolf.rule.engine.core.conflict.resolver;

import com.firewolf.rule.engine.entity.EntityMetaInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@ConditionalOnProperty(prefix = "lx.rule", name = "conflict-strategy", havingValue = "discard")
@Component
/**
 * 丢弃策略，丢弃要插入数据中已经存在的，只插入没存在的
 */
public class DiscardConflictResolver extends AbstractConflictResolver {
    @Override
    public List beforeSub(EntityMetaInfo mainEntityMetaInfo, EntityMetaInfo subMetaInfo, List data, List conflictItem, List notConflictItem) throws Exception {
        return notConflictItem;
    }

    @Override
    public void afterSub(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo) throws Exception {
    }
}
