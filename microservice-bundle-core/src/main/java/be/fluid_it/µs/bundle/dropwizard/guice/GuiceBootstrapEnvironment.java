package be.fluid_it.µs.bundle.dropwizard.guice;

import com.google.inject.Injector;

public class GuiceBootstrapEnvironment {
  private final Injector injector;

  public GuiceBootstrapEnvironment(Injector injector) {
    this.injector = injector;
  }

  public Injector injector() {
    return injector;
  }
}
