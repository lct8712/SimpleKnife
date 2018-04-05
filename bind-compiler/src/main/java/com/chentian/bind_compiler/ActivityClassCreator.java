package com.chentian.bind_compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author chentian
 */
public class ActivityClassCreator {

    private static final String CLASS_NAME_POSTFIX = "$$ViewBinder";
    private static final ClassName CLASS_NAME_ACTIVITY = ClassName.get("android.app", "Activity");
    private static final ClassName CLASS_NAME_VIEW_BINDER = ClassName.get("com.chentian.bind", "ViewBinder");

    private final TypeElement typeElement;
    private String packageName;
    private String className;

    private List<String> statementList;

    public ActivityClassCreator(Elements elementUtils, Element element) {
        typeElement = (TypeElement) element.getEnclosingElement();
        packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        className = typeElement.getSimpleName().toString();

        statementList = new ArrayList<>();
    }

    public void addField(String fieldName, int id) {
        statementList.add(String.format(Locale.getDefault(), "target.%s = target.findViewById(%d)", fieldName, id));
    }

    public JavaFile build() {
        ClassName targetClassname = ClassName.get(packageName, this.className);
        MethodSpec.Builder bindMethodBuilder = MethodSpec.methodBuilder("bind")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(targetClassname, "target")
            .returns(TypeName.VOID);
        for (String statement : statementList) {
            bindMethodBuilder.addStatement(statement);
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
