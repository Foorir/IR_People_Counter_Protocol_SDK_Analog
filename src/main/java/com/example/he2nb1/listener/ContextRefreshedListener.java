package com.example.he2nb1.listener;

import com.example.he2nb1.annotation.RequestHandler;
import com.example.he2nb1.dispatcher.RequestDispatcher;
import com.example.he2nb1.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TRH
 * @description:
 * @Package com.example.he2nb1.listener
 * @date 2023/3/27 16:24
 */
@Slf4j
@Component
public class ContextRefreshedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        HashMap<Byte, BaseHandler> map = new HashMap<>();
        Map<String, Object> handlerMap = event.getApplicationContext().getBeansWithAnnotation(RequestHandler.class);
        log.info("---------------------load request handler----------------------");

        for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {
            BaseHandler object = (BaseHandler) entry.getValue();
            Class c = object.getClass();
            Annotation[] annotations = c.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(RequestHandler.class)){
                    RequestHandler requestHandler = (RequestHandler) annotation;
                    map.put(requestHandler.type().getTypeCode(), object);
                    log.info("{}：{}", requestHandler.type().getDescs(),object.getClass().getName());
                }
            }
        }
        log.info("---------------------------------------------------------------");
        RequestDispatcher.setRequestHandlerMap(map);
    }
}
