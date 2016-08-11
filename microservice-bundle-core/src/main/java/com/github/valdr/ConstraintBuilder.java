package com.github.valdr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.valdr.serializer.MinimalMapSerializer;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConstraintBuilder {
  private final Logger logger = LoggerFactory.getLogger(ConstraintBuilder.class);
  private final ClasspathScanner classpathScanner;
  private final Iterable<Class<? extends Annotation>> allRelevantAnnotationClasses;
  private final Options options;

  public ConstraintBuilder(Options options) {
    this.options = options;
    this.classpathScanner = new ClasspathScanner(options);
    this.allRelevantAnnotationClasses = Iterables.concat(BuiltInConstraint.getAllBeanValidationAnnotations(), this.getConfiguredCustomAnnotations());
  }

  public HashMap build() {
    HashMap classNameToValidationRulesMap = new HashMap();
    Iterator var2 = this.classpathScanner.findClassesToParse().iterator();

    while(var2.hasNext()) {
      Class clazz = (Class)var2.next();
      if(clazz != null) {
        ClassConstraints classValidationRules = new AnnotatedClass(clazz, this.options.getExcludedFields(), this.allRelevantAnnotationClasses).extractValidationRules();
        if(classValidationRules.size() > 0) {
          String name = this.options.getOutputFullTypeName().booleanValue()?clazz.getName():clazz.getSimpleName();
          classNameToValidationRulesMap.put(name, classValidationRules);
        }
      }
    }
    return classNameToValidationRulesMap;
  }

  private String toJson(Map<String, ClassConstraints> classNameToValidationRulesMap) {
    try {
      ObjectMapper $ex = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addSerializer(MinimalMap.class, new MinimalMapSerializer());
      $ex.registerModule(module);
      ObjectWriter ow = $ex.writer().withDefaultPrettyPrinter();
      return ow.writeValueAsString(classNameToValidationRulesMap);
    } catch (IOException var5) {
      throw new RuntimeException(var5);
    }
  }

  private Iterable<? extends Class<? extends Annotation>> getConfiguredCustomAnnotations() {
    return Iterables.transform(this.options.getCustomAnnotationClasses(), new Function() {
      @Nullable
      @Override
      public Class<? extends Annotation> apply(@Nullable Object className) {
        Class validatorClass = ReflectionUtils.forName(className.toString(), new ClassLoader[0]);
        if(validatorClass.isAnnotation()) {
          return validatorClass;
        } else {
          ConstraintBuilder.this.logger.warn("The configured custom annotation class \'{}\' is not an annotation. It will be ignored.", validatorClass);
          return null;
        }
      }
    });
  }
}