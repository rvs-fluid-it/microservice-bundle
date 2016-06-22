package be.fluid_it.µs.bundle.showcase.modules;

import be.fluid_it.µs.bundle.guice.ApplicationModule;
import be.fluid_it.µs.bundle.showcase.config.HelloConfiguration;
import be.fluid_it.µs.bundle.showcase.config.SwaggerAnnotatedHelloConfiguration;
import com.google.inject.Binder;
import com.google.inject.Provides;
import io.dropwizard.setup.Environment;

import javax.inject.Named;

public class SwaggerAnnotatedHelloModule implements ApplicationModule {
  @Override
  public void configure(Binder binder) {
  }

  @Provides
  @Named("message")
  public String provideMessage(SwaggerAnnotatedHelloConfiguration helloConfiguration) {
    return helloConfiguration.getMessage();
  }

  @Provides
  @Named("jersey")
  public Class provideJerseyClass(Environment environment) {
    return environment.jersey().getClass();
  }
}
