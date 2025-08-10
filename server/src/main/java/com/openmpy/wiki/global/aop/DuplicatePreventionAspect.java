package com.openmpy.wiki.global.aop;

import com.openmpy.wiki.global.annotaion.PreventDuplicate;
import com.openmpy.wiki.global.exception.CustomException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class DuplicatePreventionAspect {

    private final StringRedisTemplate redisTemplate;

    @Around("@annotation(preventDuplicate)")
    public Object preventDuplicate(
            final ProceedingJoinPoint joinPoint, final PreventDuplicate preventDuplicate
    ) throws Throwable {
        final String key = generateKey(joinPoint, preventDuplicate.keyExpression());
        final Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(
                key, "processing", Duration.ofSeconds(preventDuplicate.timeoutSeconds())
        );

        if (Boolean.FALSE.equals(lockAcquired)) {
            throw new CustomException("중복된 요청입니다.");
        }

        try {
            return joinPoint.proceed();
        } finally {
            redisTemplate.delete(key);
        }
    }

    private String generateKey(final ProceedingJoinPoint joinPoint, final String keyExpression) {
        if (keyExpression.isEmpty()) {
            return joinPoint.getTarget().getClass().getSimpleName() + "::" + joinPoint.getSignature().getName();
        }
        return parseSpelExpression(joinPoint, keyExpression);
    }

    private String parseSpelExpression(final ProceedingJoinPoint joinPoint, final String expression) {
        try {
            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            final String[] parameterNames = signature.getParameterNames();
            final Object[] params = joinPoint.getArgs();

            final EvaluationContext context = new StandardEvaluationContext();

            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], params[i]);
            }

            final SpelExpressionParser parser = new SpelExpressionParser();
            final Expression exp = parser.parseExpression(expression);

            final Object result = exp.getValue(context);
            return result != null ? result.toString() : "null";
        } catch (final Exception e) {
            log.error("SpEL 표현식 파싱 실패: {}", expression, e);
            return "duplicate_prevention_" + System.currentTimeMillis();
        }
    }
}
