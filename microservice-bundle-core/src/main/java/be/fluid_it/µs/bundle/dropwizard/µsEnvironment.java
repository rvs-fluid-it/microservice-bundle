package be.fluid_it.µs.bundle.dropwizard;

import be.fluid_it.µs.bundle.dropwizard.guice.GuiceEnvironment;
import com.google.inject.Injector;
import org.apache.camel.CamelContext;

public class µsEnvironment {
  private final CamelContext camelContext;
  private final GuiceEnvironment guice;

  public µsEnvironment(Injector bootstrapInjector,
                       Injector applicationInjector,
                       CamelContext camelContext) {
    this.camelContext = camelContext;
    this.guice = new GuiceEnvironment(bootstrapInjector, applicationInjector);
  }

  public CamelContext camel() {
    return camelContext;
  }

  public GuiceEnvironment guice() {
    return guice;
  }
}
