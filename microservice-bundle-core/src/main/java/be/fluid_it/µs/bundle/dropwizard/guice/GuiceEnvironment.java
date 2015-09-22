package be.fluid_it.µs.bundle.dropwizard.guice;

import com.google.inject.Injector;

public class GuiceEnvironment extends GuiceBootstrapEnvironment{
  private final GuiceBootstrapEnvironment bootstrap;

  public GuiceEnvironment(Injector bootstrapInjector, Injector applicationInjector) {
    super(applicationInjector);
    this.bootstrap = new GuiceBootstrapEnvironment(bootstrapInjector);
  }

  public GuiceBootstrapEnvironment bootstrap() {
    return bootstrap;
  }
}
