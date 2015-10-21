package be.fluid_it.µs.bundle.dropwizard;

import be.fluid_it.µs.bundle.camel.guice.RegistryModule;
import be.fluid_it.µs.bundle.dropwizard.camel.ManagedCamelContext;
import be.fluid_it.µs.bundle.dropwizard.camel.health.CamelHealthCheck;
import be.fluid_it.µs.bundle.dropwizard.guice.DropwizardEnvironmentModule;
import be.fluid_it.µs.bundle.dropwizard.guice.GuiceLifecycleListener;
import be.fluid_it.µs.bundle.dropwizard.guice.jersey.JerseyModule;
import be.fluid_it.µs.bundle.dropwizard.guice.jersey.JerseyUtil;
import be.fluid_it.µs.bundle.guice.ApplicationModule;
import be.fluid_it.µs.bundle.guice.BootstrapModule;
import be.fluid_it.µs.bundle.guice.Modules;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class µsBundle<C extends Configuration> implements ConfiguredBundle<C> {
  final Logger logger = LoggerFactory.getLogger(µsBundle.class);

  private final List<Module> bootstrapModules;
  private final List<Module> applicationModules;
  private final List<RoutesBuilder> routesBuilders;
  private List<Class<? extends RoutesBuilder>> routesBuilderClasses;
  private List<GuiceLifecycleListener> guiceLifecycleListeners;

  private Injector bootstrapInjector;
  private Injector applicationInjector;

  private DropwizardEnvironmentModule dropwizardEnvironmentModule;
  private Optional<Class<C>> configurationClass;
  private Stage stage;

  private CamelContext camelContext;

  public static class Builder<T extends Configuration> {
    private List<Module> bootstrapModules = Modules.find(BootstrapModule.class);
    private List<Module> applicationModules = Modules.find(ApplicationModule.class);
    private List<RoutesBuilder> routesBuilders = new LinkedList<>();
    private List<Class<? extends RoutesBuilder>> routesBuilderClasses = new LinkedList<>();
    private List<GuiceLifecycleListener> guiceLifecycleListeners = new LinkedList<>();
    private Optional<Class<T>> configurationClass = Optional.absent();

    public Builder<T> addModule(Module module) {
      Preconditions.checkNotNull(module);
      applicationModules.add(module);
      return this;
    }

    public Builder<T> addBootstrapModule(Module module) {
      Preconditions.checkNotNull(module);
      bootstrapModules.add(module);
      return this;
    }

    public Builder<T> addRoutes(RoutesBuilder routes) {
      Preconditions.checkNotNull(routes);
      routesBuilders.add(routes);
      return this;
    }

    public Builder<T> addRoutes(Class<? extends RoutesBuilder> routesBuilderClass) {
      Preconditions.checkNotNull(routesBuilderClass);
      routesBuilderClasses.add(routesBuilderClass);
      return this;
    }

    public Builder<T> addGuiceLifecycleListener(GuiceLifecycleListener guiceLifecycleListener) {
      guiceLifecycleListeners.add(guiceLifecycleListener);
      return this;
    }

    public Builder<T> setConfigClass(Class<T> clazz) {
      configurationClass = Optional.of(clazz);
      return this;
    }

    public µsBundle<T> build() {
      return build(Stage.PRODUCTION);
    }

    public µsBundle<T> build(Stage s) {
      return new µsBundle<>(s, bootstrapModules, applicationModules, configurationClass, routesBuilders, routesBuilderClasses, guiceLifecycleListeners);
    }
  }

  private µsBundle(Stage stage, List<Module> bootstrapModules, List<Module> applicationModules, Optional<Class<C>> configurationClass, List<RoutesBuilder> routesBuilders, List<Class<? extends RoutesBuilder>> routesBuilderClasses, List<GuiceLifecycleListener> guiceLifecycleListeners) {
    Preconditions.checkNotNull(bootstrapModules);
    Preconditions.checkNotNull(applicationModules);
    Preconditions.checkArgument(!applicationModules.isEmpty());
    Preconditions.checkNotNull(stage);
    this.bootstrapModules = bootstrapModules;
    this.applicationModules = applicationModules;
    this.configurationClass = configurationClass;
    this.routesBuilders = routesBuilders;
    this.routesBuilderClasses = routesBuilderClasses;
    this.guiceLifecycleListeners = guiceLifecycleListeners;
    this.stage = stage;
  }

  @Override
  public void run(C configuration, Environment environment) throws Exception {
    for (GuiceLifecycleListener guiceLifecycleListener : guiceLifecycleListeners) {
      guiceLifecycleListener.beforeGuiceStart();
    }
    if (configurationClass.isPresent()) {
      dropwizardEnvironmentModule = new DropwizardEnvironmentModule<>(configurationClass.get());
    } else {
      dropwizardEnvironmentModule = new DropwizardEnvironmentModule<>(Configuration.class);
    }
    setEnvironment(configuration, environment);

    applicationModules.add(new JerseyModule());
    applicationModules.add(new RegistryModule());
    applicationModules.add(dropwizardEnvironmentModule);

    logger.info("Wiring of application:");
    for (Module applicationModule : applicationModules) {
      logger.info("   " + applicationModule.getClass().getSimpleName());
    }
    applicationInjector = bootstrapInjector.createChildInjector(applicationModules);

    for (GuiceLifecycleListener guiceLifecycleListener : guiceLifecycleListeners) {
      guiceLifecycleListener.guiceStarted();
    }

    JerseyUtil.registerGuiceBound(applicationInjector, environment.jersey());
    JerseyUtil.registerGuiceFilter(environment);

    if (routesBuilderClasses != null && !routesBuilderClasses.isEmpty()) {
      for (Class<? extends RoutesBuilder> routesBuilderClass : routesBuilderClasses) {
        RoutesBuilder wiredRoutesBuilder = applicationInjector.getInstance(routesBuilderClass);
        this.routesBuilders.add(wiredRoutesBuilder);
      }
    }

    if (routesBuilders != null && !routesBuilders.isEmpty()) {
      camelContext = new DefaultCamelContext(applicationInjector.getInstance(Registry.class));
      for (RoutesBuilder routesBuilder : routesBuilders) {
        camelContext.addRoutes(routesBuilder);
      }
      environment.lifecycle().manage(ManagedCamelContext.of(camelContext));
      HealthCheck camelHealthCheck = new CamelHealthCheck(camelContext);
      environment.healthChecks().register("camel", camelHealthCheck);
    }
  }

  @Override
  public void initialize(Bootstrap<?> bootstrap) {
    logger.info("Wiring during bootstrap:");
    for (Module bootstrapModule : bootstrapModules) {
      logger.info("   " + bootstrapModule.getClass().getSimpleName());
    }
    this.bootstrapInjector = Guice.createInjector(stage, bootstrapModules);
  }

  @SuppressWarnings("unchecked")
  private void setEnvironment(final C configuration, final Environment environment) {
    dropwizardEnvironmentModule.setEnvironmentData(configuration, environment);
  }

  public Injector bootstrapInjector() {
    return bootstrapInjector;
  }

  public Injector applicationInjector() {
    return applicationInjector;
  }

  public boolean rideTheCamel() {
    return camelContext != null;
  }

  public µsEnvironment µsEnvironment() {
    return new µsEnvironment(bootstrapInjector, applicationInjector, camelContext);
  }

  public static <T extends Configuration> Builder<T> newBuilder() {
    return new Builder<>();
  }

}