package com.horyu1234.kkutugame;

import com.google.gson.Gson;
import com.horyu1234.kkutugame.request.JSONRequest;
import com.horyu1234.kkutugame.request.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created by horyu on 2017-03-24.
 */
@Component
public class Dispatcher {
    private final String MAIN_PACKAGE = "com.horyu1234.kkutugame";
    private Gson gson;
    private ApplicationContext applicationContext;
    private Logger logger;

    @Autowired
    public Dispatcher(Gson gson, ApplicationContext applicationContext) {
        this.gson = gson;
        this.applicationContext = applicationContext;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void mappingController(WebSocketSession webSocketSession, String jsonStr) {
        try {
            // 처음 오는 JSON 을 Type 와 Value 로 이루어진 객체로 변환
            JSONRequest jsonRequest;
            try {
                jsonRequest = gson.fromJson(jsonStr, JSONRequest.class);
            } catch (Exception e) {
                logger.error("초기 JSON 을 JSONRequest 타입으로 변환할 수 없습니다.", e);
                return;
            }

            // Type 으로 Request 클래스를 알아내, Value 부분 으로 객체 생성
            Class requestClass = getRequestClass(jsonRequest.getType());
            if (requestClass == null) {
                logger.error("Type 에 해당하는 Request 클래스를 찾을 수 없습니다.");
                return;
            }

            // 해당 Request 클래스에 JSON 으로 값 설정
            String json = jsonRequest.getValue().replace("\\\"", "\"");

            Object requestObj;
            try {
                requestObj = gson.fromJson(json, requestClass);
            } catch (Exception e) {
                logger.error("값 JSON 을 확인된 Request 타입으로 변환할 수 없습니다.", e);
                return;
            }

            // 매핑할 메서드 찾기
            Map<Method, Class> annotationMethods = getAnnotationMethods(RequestHandler.class);
            for (Map.Entry<Method, Class> methodEntry : annotationMethods.entrySet()) {
                Class methodClass = methodEntry.getValue();
                Method method = methodEntry.getKey();

                List<Object> resultParameters = new ArrayList<>();
                boolean success = true;

                // 함수 파라미터들
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.getType().equals(WebSocketSession.class)) {
                        resultParameters.add(webSocketSession);
                    } else if (parameter.getType().equals(requestClass)) {
                        resultParameters.add(requestObj);
                    } else {
                        success = false;
                    }
                }

                if (success) {
                    String methodClassSimpleName = methodClass.getSimpleName();
                    String methodClassBeanName = methodClassSimpleName.substring(0, 1).toLowerCase() + methodClassSimpleName.substring(1);
                    Object methodClassBean = applicationContext.getBean(methodClassBeanName);

                    method.invoke(methodClassBean, resultParameters.toArray());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Class getRequestClass(String packetType) throws ClassNotFoundException, IOException, URISyntaxException {
        for (Class clazz : getClasses()) {
            if (clazz.getSimpleName().endsWith("Request") && clazz.getSimpleName().replace("Request", "").equalsIgnoreCase(packetType)) {
                return clazz;
            }
        }
        return null;
    }

    private Map<Method, Class> getAnnotationMethods(Class<? extends Annotation> annotation) throws ClassNotFoundException, IOException, URISyntaxException {
        Map<Method, Class> methods = new HashMap<>();
        for (Class clazz : getClasses()) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.put(method, clazz);
                }
            }
        }
        return methods;
    }

    private Iterable<Class> getClasses() throws URISyntaxException, IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(MAIN_PACKAGE.replace(".", "/"));

        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            URI uri = new URI(resource.toString());
            File file = new File(uri.getPath());

            dirs.add(file);
        }

        List<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, MAIN_PACKAGE));
        }

        return classes;
    }

    private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }

        return classes;
    }
}
