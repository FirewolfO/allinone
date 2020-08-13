package com.firewolf.nbdoc.core;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/22 8:02 上午
 */

import com.firewolf.nbdoc.controller.NBDocController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数解析器
 */
@ConditionalOnBean(NBDocController.class)
@Component("nbdocParamParser")
public class ParamParser {
    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser parser = new SpelExpressionParser();


    public String parseSpELValue(String spELString, EvaluationContext context) {
        Expression expression = parser.parseExpression(spELString);
        Object value = expression.getValue(context);
        return value == null ? "null" : value.toString();
    }


    /**
     * 解析带有SpEL表达式的字符串，如：
     * my dog name is #{user.dog?.name} , again #{user.dog?.name} ， but my name is #{user.name} , my info is #{user}
     *
     * @param str
     * @param context
     * @return
     */
    public String parseStrWithSpEL(String str, EvaluationContext context) {
        Map<String, String> spELMaps = getSpEL(str);
        //解析spel对应的数据
        Map<String, String> parsedValues = new HashMap<>();
        for (Map.Entry<String, String> entry : spELMaps.entrySet()) {
            String value = parseSpELValue(entry.getValue(), context);
            parsedValues.put(entry.getKey(), value);
        }
        // 转换最终的结果
        String result = str;

        for (String s : spELMaps.keySet()) {
            result = result.replace(s, parsedValues.get(s));
        }
        return result;
    }


    /**
     * 获取原始SpEL字符串和对应的SpEL的对应关系。如：
     * #{user.name} --> #user.name
     *
     * @param str
     * @return
     */
    private Map<String, String> getSpEL(String str) {
        Map<String, String> spElMap = new HashMap<>();
        String regex = "#\\{([^}])*\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            String spel = "#" + group.substring(2, group.length() - 1);
            spElMap.put(group, spel);
        }
        return spElMap;
    }


}
