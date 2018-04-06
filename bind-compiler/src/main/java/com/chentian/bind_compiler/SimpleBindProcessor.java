package com.chentian.bind_compiler;

import com.chentian.bind_annotation.BindClick;
import com.chentian.bind_annotation.BindString;
import com.chentian.bind_annotation.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
@SuppressWarnings("unused")
public class SimpleBindProcessor extends AbstractProcessor {

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
        List<String> annotations = Arrays.asList(
            BindView.class.getCanonicalName(),
            BindString.class.getCanonicalName(),
            BindClick.class.getCanonicalName()
        );
        return new HashSet<>(annotations);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "process start: " + getClass().getName());

        ActivityClassCreator creator = processBindView(roundEnv.getElementsAnnotatedWith(BindView.class));
        creator = processBindString(creator, roundEnv.getElementsAnnotatedWith(BindString.class));
        creator = processBindClick(creator, roundEnv.getElementsAnnotatedWith(BindClick.class));

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

    private ActivityClassCreator processBindView(Set<? extends Element> elementsAnnotatedWith) {
        ActivityClassCreator creator = null;
        for (Element element : elementsAnnotatedWith) {
            if (creator == null) {
                creator = new ActivityClassCreator(elementUtils, element);
            }

            String fieldName = element.getSimpleName().toString();
            int id = element.getAnnotation(BindView.class).value();
            creator.addBindView(fieldName, id);
        }
        return creator;
    }

    private ActivityClassCreator processBindString(ActivityClassCreator creator,
                                                 Set<? extends Element> elementsAnnotatedWith) {
        for (Element element : elementsAnnotatedWith) {
            if (creator == null) {
                creator = new ActivityClassCreator(elementUtils, element);
            }

            String fieldName = element.getSimpleName().toString();
            int id = element.getAnnotation(BindString.class).value();
            creator.addBindString(fieldName, id);
        }
        return creator;
    }

    private ActivityClassCreator processBindClick(ActivityClassCreator creator,
                                                  Set<? extends Element> elementsAnnotatedWith) {
        for (Element element : elementsAnnotatedWith) {
            if (creator == null) {
                creator = new ActivityClassCreator(elementUtils, element);
            }

            String methodName = element.getSimpleName().toString();
            int id = element.getAnnotation(BindClick.class).value();
            creator.addBindClick(methodName, id);
        }
        return creator;
    }
}
