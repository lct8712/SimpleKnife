package com.chentian.bind_compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author chentian
 */
public class ActivityClassCreator {

    private static final String CLASS_NAME_POSTFIX = "$$ViewBinder";
    private static final ClassName CLASS_NAME_VIEW_BINDER = ClassName.get("com.chentian.bind", "ViewBinder");
    private static final ClassName CLASS_NAME_VIEW = ClassName.get("android.view", "View");
    private static final ClassName CLASS_NAME_ON_CLICK_LISTENER = ClassName.get("android.view", "View.OnClickListener");

    private final TypeElement typeElement;
    private String packageName;
    private String className;

    private Map<String, Integer> bindViewMap;
    private Map<String, Integer> bindClickMap;

    public ActivityClassCreator(Elements elementUtils, Element element) {
        typeElement = (TypeElement) element.getEnclosingElement();
        packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        className = typeElement.getSimpleName().toString();

        bindViewMap = new HashMap<>();
        bindClickMap = new HashMap<>();
    }

    public void addBindView(String fieldName, int id) {
        bindViewMap.put(fieldName, id);
    }

    public void addBindClick(String fieldName, int id) {
        bindClickMap.put(fieldName, id);
    }

    public JavaFile build() {
        ClassName targetClassname = ClassName.get(packageName, this.className);
        MethodSpec.Builder bindMethodBuilder = MethodSpec.methodBuilder("bind")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(ParameterSpec.builder(targetClassname, "target", Modifier.FINAL).build())
            .returns(TypeName.VOID);

        // BindView
        for (Map.Entry<String, Integer> entry : bindViewMap.entrySet()) {
            String fieldName = entry.getKey();
            int id = entry.getValue();
            bindMethodBuilder.addStatement("target.$L = target.findViewById($L)", fieldName, id);
        }

        // BindClick
        for (Map.Entry<String, Integer> entry : bindClickMap.entrySet()) {
            MethodSpec onClickMethod = MethodSpec.methodBuilder("onClick")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(CLASS_NAME_VIEW, "v")
                .returns(TypeName.VOID)
                .addStatement("target.$L()", entry.getKey())
                .build();
            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(CLASS_NAME_ON_CLICK_LISTENER)
                .addMethod(onClickMethod).build();
            bindMethodBuilder.addStatement("target.findViewById($L).setOnClickListener($L)", entry.getValue(), listener);
        }

        TypeSpec typeSpec = TypeSpec.classBuilder(this.className + CLASS_NAME_POSTFIX)
            .superclass(TypeName.get(typeElement.asType()))
            .addSuperinterface(ParameterizedTypeName.get(CLASS_NAME_VIEW_BINDER, targetClassname))
            .addModifiers(Modifier.PUBLIC)
            .addMethod(bindMethodBuilder.build())
            .build();

        return JavaFile.builder(packageName, typeSpec).build();
    }
}
