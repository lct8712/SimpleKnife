package com.chentian.bind_compiler;

import com.chentian.bind_annotation.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author chentian
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindView.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "process start: " + getClass().getName());

        ActivityClassCreator creator = null;
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(BindView.class);
        for (Element element : elementsAnnotatedWith) {
            if (creator == null) {
                creator = new ActivityClassCreator(elementUtils, element);
            }


            String fieldName = element.getSimpleName().toString();
            int id = element.getAnnotation(BindView.class).value();
            creator.addField(fieldName, id);
        }

        try {
            if (creator != null) {
                messager.printMessage(Diagnostic.Kind.NOTE, "creator build.");
                creator.build().writeTo(processingEnv.getFiler());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
