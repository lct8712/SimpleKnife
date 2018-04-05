package com.chentian.bind;

import android.app.Activity;

/**
 * @author chentian
 */
public class BindKnife {

    private static final String CLASS_NAME_POSTFIX = "$$ViewBinder";

    private BindKnife() {}

    public static void bind(Object target, Activity activity) {
        ViewBinder viewBinder = findViewBinder(target);
        if (viewBinder != null) {
            viewBinder.bind(activity);
        }
    }

    private static ViewBinder findViewBinder(Object target) {
        String binderClassName = target.getClass().getName() + CLASS_NAME_POSTFIX;
        try {
            return (ViewBinder) Class.forName(binderClassName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
