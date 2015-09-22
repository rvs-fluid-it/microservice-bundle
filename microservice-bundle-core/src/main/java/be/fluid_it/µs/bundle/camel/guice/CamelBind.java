package be.fluid_it.µs.bundle.camel.guice;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/*
 * annotates named camel beans in the registry 
 *  
 * @author chelfim@google.com (Mourad Chelfi)
 */
@Target({METHOD, CONSTRUCTOR, FIELD, TYPE, PARAMETER})
@Retention(RUNTIME)
@Documented
@BindingAnnotation
public @interface CamelBind {
  String value();
}
