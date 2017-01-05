package ch.p3n.apps.appfront.rest;

import org.aopalliance.aop.Advice;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

/**
 * Advisor which checks intercepted method for rest api requests.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class RequestLoggerAdvisor extends AbstractPointcutAdvisor {

    private static final long serialVersionUID = -6208678371392331661L;

    private static final StaticMethodMatcherPointcut POINTCUT = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(final Method method, final Class<?> aClass) {
            return method.isAnnotationPresent(RequestMapping.class);
        }
    };

    @Autowired
    private transient RequestLogger interceptor;


    @Override
    public Pointcut getPointcut() {
        return POINTCUT;
    }

    @Override
    public Advice getAdvice() {
        return interceptor;
    }

    @Override
    public boolean equals(Object other) {
        return this == other;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(23, 54).append(this).toHashCode();
    }

}
