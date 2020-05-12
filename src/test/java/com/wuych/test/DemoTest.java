package com.wuych.test;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * 总结：
 *  一切框架设计必须考虑：拓展性
 *  1. 抽象
 *  2. 设计模式
 *
 * Spring给出了拓展：
 *  1. 在创建对象前可以让你干一点事
 *  2. 容器初始化之前可以干一点事
 *  3. 在不同的阶段发出不同的事件：还可以干一点事
 *  4. 抽象出各个接口——为所欲为
 *  5. 面向接口编程
 *
 * @Author Wu Yanchen
 * @Date 2020-05-11 16:08
 */
public class DemoTest {

    @Test
    public void testField() throws Exception {
        UserController userController = new UserController();
        UserService userService = new UserService();
        Class<? extends UserController> clazz = userController.getClass();
        Field userServiceField = clazz.getDeclaredField("userService");
        userServiceField.setAccessible(true);

        String name = userServiceField.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        String setMethodName = "set" + name;

        Method setMethod = clazz.getMethod(setMethodName, UserService.class);
        setMethod.invoke(userController, userService);

        System.out.println(userController.getUserService());
    }

    @Test
    public void testAutowired() {
        UserController userController = new UserController();

        Class<? extends UserController> clazz = userController.getClass();

        Stream.of(clazz.getDeclaredFields()).forEach(field -> {
            String name = field.getName();
            Autowired annotation = field.getAnnotation(Autowired.class);
            if (annotation != null) {
                field.setAccessible(true);
                // 得到了域的类型
                Class<?> fieldType = field.getType();

                try {
                    /*
                     * 这个对象是直接newInstance出来的
                     * 在Spring中，这个bean是定义在注解或者xml中的，如何找到？
                     */
                    Object object = fieldType.getConstructor().newInstance();
                    field.set(userController, object);
                } catch (InstantiationException
                        | IllegalAccessException
                        | InvocationTargetException
                        | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println(userController);
    }
}
