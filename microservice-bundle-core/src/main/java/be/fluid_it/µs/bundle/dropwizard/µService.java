package be.fluid_it.µs.bundle.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Arrays;

public abstract class µService<C extends Configuration> extends Application<C> {
  public static Class<? extends µService> µServiceClass;
  public static String relativePathToYmlInIDE;

  public static void main(String[] args) throws Exception {
    if (args != null && args.length > 0) {
      µServiceClass.newInstance().run(concat(new String[]{"server"}, args));
    } else {
      µServiceClass.newInstance().run("server", relativePathToYmlInIDE);
    }
  }

  public static <T> T[] concat(T[] first, T[] second) {
    T[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  private µsBundle<C> µsBundleInstance;

  @Override
  public void initialize(Bootstrap<C> bootstrap) {
    µsBundle.Builder<C> µsBundleBuilder = µsBundle.<C>newBuilder();
    initialize(µsBundleBuilder);
    µsBundleInstance = µsBundleBuilder.setConfigClass(configurationClass()).build();
    bootstrap.addBundle(µsBundleInstance);
  }

  public void initialize(µsBundle.Builder<C> µsBundleBuilder) {
  }

  public abstract Class<C> configurationClass();

  @Override
  public void run(C configuration, Environment environment) throws Exception {
    run(configuration, environment, µsBundleInstance.µsEnvironment());
  }

  protected abstract void run(C configuration, Environment environment, µsEnvironment µsEnvironment) throws Exception;
}
