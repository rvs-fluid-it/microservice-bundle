package be.fluid_it.µs.bundle.showcase.app;

import be.fluid_it.µs.bundle.dropwizard.µsBundle;
import be.fluid_it.µs.bundle.showcase.config.HelloConfiguration;
import be.fluid_it.µs.bundle.showcase.modules.HelloModule;
import be.fluid_it.µs.bundle.showcase.resource.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Arrays;

public class WiredHelloApplication extends Application<HelloConfiguration> {

  public static void main(String[] args) throws Exception {
    if (args != null && args.length > 0) {
      new WiredHelloApplication().run(concat(new String[] {"server"}, args));
    } else {
      new WiredHelloApplication().run("server", "../microservice-bundle-showcase/src/main/config/wired-application/hello-config.yml");
    }
  }

  public static <T> T[] concat(T[] first, T[] second) {
    T[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  @Override
  public void initialize(Bootstrap<HelloConfiguration> bootstrap) {
     bootstrap.addBundle(µsBundle.<HelloConfiguration>newBuilder().addModule(new HelloModule()).setConfigClass(HelloConfiguration.class).build());
  }

  @Override
  public void run(HelloConfiguration configuration, Environment environment) throws Exception {
    environment.jersey().register(HelloResource.class);
  }
}
