package com.firewolf.testcase.junit5.s06_condition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

/**
 * 描述：条件测试
 * Author：liuxing
 * Date：2020/6/20
 */
public class ConditionTest {


    @Test
    @EnabledOnOs(OS.WINDOWS) // 在windows环境下执行
    @EnabledOnJre(JRE.JAVA_8)
    @EnabledIfSystemProperty(named = "os.name", matches = ".*10.*") // 要求win10
    public void test1() {
        System.out.println("这是windows10下的java8环境");
    }

    /**
     * 如果环境变量中有special=true的时候，就禁用测试
     */
    @Test
    @DisabledIfEnvironmentVariable(named = "special", matches = "true")
    public void test2() {
        System.out.println("这是特殊测试");
    }

    /**
     * 自定义注解
     */
    @Test
    @EnableOnSaturday
    public void testSelf() {
        System.out.println("xxxx");
    }
}

/**
 * 自定义Condition注解，参考EnabledOnJre进行实现
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(EnableOnSelfCondition.class)
@interface EnableOnSaturday {
}

class EnableOnSelfCondition implements ExecutionCondition {
    private static final ConditionEvaluationResult ENABLED_BY_DEFAULT = enabled("@EnableOnSaturday is not present");

    static final ConditionEvaluationResult ENABLED_ON_SATURDAY = enabled("Enabled on Saturday ");

    static final ConditionEvaluationResult DISABLED_ON_SATURDAY = disabled("Disabled on Saturday ");

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<EnableOnSaturday> optional = findAnnotation(context.getElement(), EnableOnSaturday.class);
        if (optional.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            DayOfWeek dayOfWeek = now.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY) {
                return ENABLED_ON_SATURDAY;
            } else {
                return DISABLED_ON_SATURDAY;
            }
        }
        return ENABLED_BY_DEFAULT;
    }
}