package be.fluid_it.µs.bundle.showcase.app;

import be.fluid_it.µs.bundle.dropwizard.µService;
import be.fluid_it.µs.bundle.dropwizard.µsBundle;
import be.fluid_it.µs.bundle.dropwizard.µsEnvironment;
import be.fluid_it.µs.bundle.showcase.config.HelloConfiguration;
import be.fluid_it.µs.bundle.showcase.modules.HelloModule;
import be.fluid_it.µs.bundle.showcase.resource.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Arrays;

public class HelloService extends µService<HelloConfiguration> {
  static {
    µService.µServiceClass = HelloService.class;
    µService.relativePathToYmlInIDE = "../microservice-bundle-showcase/src/main/config/service/hello-config.yml";
  }

  @Override
  public Class<HelloConfiguration> configurationClass() {
    return HelloConfiguration.class;
  }

  @Override
  public void initialize(µsBundle.Builder<HelloConfiguration> guice) {
    guice.addModule(new HelloModule());
  }

  @Override
  public void run(HelloConfiguration configuration, µsEnvironment µsEnvironment) throws Exception {
    µsEnvironment.jersey().register(HelloResource.class);
  }
}
