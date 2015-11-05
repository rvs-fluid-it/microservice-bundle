package be.fluid_it.µs.bundle.dropwizard;

import io.dropwizard.Application;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;

public class ApplicationRule extends ExternalResource {
  private final Class<? extends Application> applicationClass;

  public ApplicationRule(Class<? extends Application> applicationClass) {
    this.applicationClass = applicationClass;
  }

  @Override
  protected void before() throws Throwable {
    Application application = applicationClass.newInstance();
    Method method = applicationClass.getMethod("main", String[].class);
    String[] params = null;
    method.invoke(null, (Object) params);
  }

  @Override
  protected void after() {
  }
}
