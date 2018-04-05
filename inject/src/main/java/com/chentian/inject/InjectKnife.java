package com.chentian.inject;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * @author chentian
 */
public class InjectKnife {

    private InjectKnife() {}

    public static void Inject(Object target, Activity source) {
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
}
