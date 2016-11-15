package be.fluid_it.µs.bundle.dropwizard;

import be.fluid_it.µs.bundle.dropwizard.guice.GuiceLifecycleListener;
import be.fluid_it.µs.bundle.dropwizard.jackson.JsonPrettyPrintable;
import be.fluid_it.µs.bundle.dropwizard.swagger.SwaggerAware;
import be.fluid_it.µs.bundle.dropwizard.validator.ValidationResource;
import be.fluid_it.µs.bundle.dropwizard.validator.ValidatorAware;
import be.fluid_it.µs.bundle.dropwizard.zipkin.ZipkinAware;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.kristofa.brave.Brave;
import com.github.valdr.MinimalMap;
import com.github.valdr.serializer.MinimalMapSerializer;
import com.smoketurner.dropwizard.zipkin.ZipkinBundle;
import com.smoketurner.dropwizard.zipkin.ZipkinFactory;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.listing.AcceptHeaderApiListingResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class µService<C extends Configuration> extends Application<C> implements GuiceLifecycleListener<C> {
  public static Class<? extends µService> µServiceClass;
  public static String relativePathToYmlInIDE;

  public static void main(String[] args) throws Exception {
    if (args != null && args.length > 0) {
      newService(args);
    } else {
      newService(relativePathToYmlInIDE);
    }
  }

  public static <T> T[] concat(T[] first, T[] second) {
    T[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  public static µService newService(String... args) throws Exception {
    µService µService = µServiceClass.newInstance();
    µService.run(concat(new String[]{"server"}, args));
    return µService;
  }

  private µsBundle<C> µsBundleInstance;
  private C configuration;
  private Environment environment;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void initialize(Bootstrap<C> bootstrap) {
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(new ResourceConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(false))
    );
    µsBundle.Builder<C> µsBundleBuilder = µsBundle.<C>newBuilder();
    initialize(µsBundleBuilder);
    µsBundleInstance = µsBundleBuilder.setConfigClass(getConfigurationClass()).addGuiceLifecycleListener(this).build();
    bootstrap.addBundle(µsBundleInstance);
    if (ZipkinAware.class.isAssignableFrom(getConfigurationClass())) {
      logger.info("Register " + ZipkinBundle.class.getSimpleName() + " ...");
      bootstrap.addBundle(new ZipkinBundle<C>(getName()) {
        @Override
        public ZipkinFactory getZipkinFactory(C c) {
          return ((ZipkinAware)c).getZipkin();
        }
      });
    }
  }

  private void configureMapper(C configuration, ObjectMapper objectMapper) {
    if (configuration instanceof ValidatorAware) {
      SimpleModule module = new SimpleModule();
      module.addSerializer(MinimalMap.class, new MinimalMapSerializer());
      objectMapper.registerModule(module);
    }
    if (configuration instanceof JsonPrettyPrintable) {
      objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }
  }

  public void initialize(µsBundle.Builder<C> µsBundleBuilder) {
  }

  public C configuration() {
    return this.configuration;
  }

  public void stop() {
    for (org.eclipse.jetty.util.component.LifeCycle lifeCycle : environment.lifecycle().getManagedObjects()) {
      try {
        lifeCycle.stop();
      } catch (Exception e) {
        logger.warn("Exception encountered while stopping (" + lifeCycle + ") : " + e);
      }
    }
  }

  @Override
  public void beforeGuiceStart(C configuration, Environment environment) {
  }

  @Override
  public void guiceStarted(C configuration, Environment environment) {
  }

  @Override
  public void run(C configuration, Environment environment) throws Exception {
    this.configuration = configuration;
    this.environment = environment;
    run(configuration, µsBundleInstance.µsEnvironment(this.environment));
    if (configuration instanceof ZipkinAware) {
      Brave brave = ((ZipkinAware)configuration).getZipkin().build(environment);
    }
    configureMapper(configuration, environment.getObjectMapper());
    if (configuration instanceof ValidatorAware) {
      environment.jersey().register(ValidationResource.class);
    }
    if (configuration instanceof SwaggerAware) {
      environment.jersey().register(SwaggerSerializers.class);
      environment.jersey().register(ApiListingResource.class);
      environment.jersey().register(AcceptHeaderApiListingResource.class);
    }
  }

  protected abstract void run(C configuration, µsEnvironment µsEnvironment) throws Exception;
}
