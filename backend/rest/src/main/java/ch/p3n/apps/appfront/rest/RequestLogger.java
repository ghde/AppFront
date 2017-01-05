package ch.p3n.apps.appfront.rest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Advisor which checks intercepted method for rest api requests.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class RequestLogger implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogger.class);

    private static final String NEW_LINE = "\n";

    private static final String PLUSES = "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";

    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        if (LOGGER.isDebugEnabled()) {
            final Object[] arguments = methodInvocation.getArguments();
            final StringBuilder sb = new StringBuilder();
            sb.append(NEW_LINE);
            sb.append(PLUSES).append(NEW_LINE);
            sb.append("+ Method : ").append(methodInvocation.getMethod().getDeclaringClass().getName()).append("#").append(methodInvocation.getMethod().getName()).append(NEW_LINE);
            if (arguments.length > 0) {
                sb.append("+ Arguments").append(NEW_LINE);
                for (final Object argument : arguments) {
                    sb.append(" ++ ").append(argument).append(NEW_LINE);
                }
            }
            sb.append(PLUSES);
            LOGGER.info(sb.toString());
        }
        return methodInvocation.proceed();
    }

}
