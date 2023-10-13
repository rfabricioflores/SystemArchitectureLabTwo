package se.fabricioflores.systemarchitecturelabtwo.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Log
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {
    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @AroundInvoke
    public Object logMethodEntry(InvocationContext ctx) throws Exception {
        String methodName = ctx.getMethod().getName();
        String className = ctx.getMethod().getDeclaringClass().getName();
        logger.info("Method called: " + methodName + " in class " + className);
        return ctx.proceed();
    }
}
