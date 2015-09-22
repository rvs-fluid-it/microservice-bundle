package be.fluid_it.µs.bundle.camel.guice;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.camel.NoSuchBeanException;
import org.apache.camel.spi.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;

/*
 * Class implementing the Camel spi Registry to work fully with guice without the need
 * for a jndi registry.  just annotate injected parameters/fiels with @CamelBien(beanname)
 * and bind beanname in a guice module. beanRef in the camel dsl will inject the named
 * bean from the guiceregistry.
 *  
 * @author chelfim@google.com (Mourad Chelfi)
 */
public class GuiceRegistry implements Registry {
  private static final Logger logger = LoggerFactory.getLogger(GuiceRegistry.class);
  private final Injector injector;

  @Inject
  public GuiceRegistry(Injector injector) {
    this.injector = injector;
  }

  @Override
  public Object lookupByName(String name) {
    Object answer = null;

    Map<Key<?>, Binding<?>> bindings = injector.getBindings();

    for (Key<?> key : bindings.keySet()) {
      Annotation annotation = key.getAnnotation();
      if (annotation instanceof CamelBind) {
        if (((CamelBind) annotation).value().equals(name)) {
          answer = injector.getInstance(key);
        }
      }
    }

    return answer;
  }

  @Override
  public <T> T lookupByNameAndType(String name, Class<T> type) {
    Object answer = lookupByName(name);

    if (answer != null) {
      try {
        return type.cast(answer);
      } catch (ClassCastException e) {
        String msg = "Found bean: " + name + " in GuiceRegistry: " + this + " of type: " + answer.getClass().getName() + " expected type was: " + type;
        logger.error(msg, e);
        throw new NoSuchBeanException(name, msg, e);
      }
    } else {
      return null;
    }
  }

  @Override
  public <T> Map<String, T> findByTypeWithName(Class<T> type) {
    Map<String, T> result = Maps.newHashMap();

    Map<Key<?>, Binding<?>> bindings = injector.getBindings();

    for (Key<?> key : bindings.keySet()) {
      Annotation annotation = key.getAnnotation();

      if (annotation instanceof CamelBind) {
        if (key.getTypeLiteral().getRawType().isAssignableFrom(type)) {
          String name = ((CamelBind) key.getAnnotation()).value();
          Object inst = injector.getInstance(key);

          logger.debug("Found binding for name: {} with value: {}", name, inst);

          result.put(name, type.cast(inst));
        }
      }
    }

    return result;
  }

  @Override
  public <T> Set<T> findByType(Class<T> type) {
    return Sets.newHashSet(findByTypeWithName(type).values());
  }

  @Override
  public Object lookup(String name) {
    return lookupByName(name);
  }

  @Override
  public <T> T lookup(String name, Class<T> type) {
    return lookupByNameAndType(name, type);
  }

  @Override
  public <T> Map<String, T> lookupByType(Class<T> type) {
    return findByTypeWithName(type);
  }
}
