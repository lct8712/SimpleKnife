package com.chentian.inject;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chentian
 */
public class InjectKnife {

    private InjectKnife() {}

    public static void inject(Object target, Activity source) {
        injectFields(target, source);
        injectMethods(target, source);
    }

    private static void injectFields(Object target, Activity source) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(InjectView.class)) {
                continue;
            }

            int id = field.getAnnotation(InjectView.class).value();
            if (id <= 0) {
                continue;
            }

            try {
                field.setAccessible(true);
                field.set(target, source.findViewById(id));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectMethods(Object target, final Activity source) {
        Method[] methods = target.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            if (!method.isAnnotationPresent(InjectClick.class)) {
                continue;
            }

            int id = method.getAnnotation(InjectClick.class).value();
            if (id <= 0) {
                continue;
            }

            source.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        method.setAccessible(true);
                        method.invoke(source);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
