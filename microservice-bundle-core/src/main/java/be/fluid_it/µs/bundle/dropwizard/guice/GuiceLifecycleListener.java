package be.fluid_it.µs.bundle.dropwizard.guice;

import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

public interface GuiceLifecycleListener<C extends Configuration> {
  void beforeGuiceStart(C configuration, Environment environment);
  void guiceStarted(C configuration, Environment environment);
}
