package be.fluid_it.µs.bundle.showcase.app;

import be.fluid_it.µs.bundle.dropwizard.µService;
import be.fluid_it.µs.bundle.dropwizard.µsBundle;
import be.fluid_it.µs.bundle.dropwizard.µsEnvironment;
import be.fluid_it.µs.bundle.showcase.config.SwaggerAnnotatedHelloConfiguration;
import be.fluid_it.µs.bundle.showcase.modules.SwaggerAnnotatedHelloModule;
import be.fluid_it.µs.bundle.showcase.resource.SwaggerAnnotatedHelloResource;

public class SwaggerAnnotatedHelloService extends µService<SwaggerAnnotatedHelloConfiguration> {
  static {
    µService.µServiceClass = SwaggerAnnotatedHelloService.class;
    µService.relativePathToYmlInIDE = "swagger-annotated-hello-service-config.yml";
  }

  @Override
  public void initialize(µsBundle.Builder<SwaggerAnnotatedHelloConfiguration> guice) {
    guice.addModule(new SwaggerAnnotatedHelloModule());
  }

  @Override
  public void run(SwaggerAnnotatedHelloConfiguration configuration, µsEnvironment µsEnvironment) throws Exception {
    µsEnvironment.jersey().register(SwaggerAnnotatedHelloResource.class);
  }
}
