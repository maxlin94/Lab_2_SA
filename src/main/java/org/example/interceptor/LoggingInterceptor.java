package org.example.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Log
public class LoggingInterceptor {
    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        logger.info("Entering method: {}", ic.getMethod().getName());
        try {
            return ic.proceed();
        } finally {
            logger.info("Exiting method: {}", ic.getMethod().getName());
        }
    }
}