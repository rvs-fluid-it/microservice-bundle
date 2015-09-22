package be.fluid_it.µs.bundle.showcase.modules;

import be.fluid_it.µs.bundle.guice.ApplicationModule;
import be.fluid_it.µs.bundle.showcase.config.HelloConfiguration;
import com.google.inject.Binder;
import com.google.inject.Provides;
import io.dropwizard.setup.Environment;

import javax.inject.Named;

public class HelloModule implements ApplicationModule {
  @Override
  public void configure(Binder binder) {
  }

  @Provides
  @Named("message")
  public String provideMessage(HelloConfiguration helloConfiguration) {
    return helloConfiguration.getMessage();
  }

  @Provides
  @Named("jersey")
  public Class provideJerseyClass(Environment environment) {
    return environment.jersey().getClass();
  }
}
