package be.fluid_it.µs.bundle.showcase.app;

import be.fluid_it.µs.bundle.dropwizard.µsBundle;
import be.fluid_it.µs.bundle.showcase.config.HelloConfiguration;
import be.fluid_it.µs.bundle.showcase.modules.HelloModule;
import be.fluid_it.µs.bundle.showcase.resource.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Arrays;

public class HelloApplication extends Application<HelloConfiguration> {

  public static void main(String[] args) throws Exception {
    if (args != null && args.length > 0) {
      new HelloApplication().run(concat(new String[] {"server"}, args));
    } else {
      new HelloApplication().run("server", "../microservices-bundle-showcase/target/classes/hello-config.yml");
    }
  }

  public static <T> T[] concat(T[] first, T[] second) {
    T[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  @Override
  public void run(HelloConfiguration configuration, Environment environment) throws Exception {
    environment.jersey().register(new HelloResource(configuration.getMessage(),
        environment.jersey().getClass()));
  }
}
