package be.fluid_it.µs.bundle.dropwizard;

import org.junit.rules.ExternalResource;

import java.lang.reflect.Method;

public class µServiceRule extends ExternalResource {
  private final Class<? extends µService> µServiceClass;

  public µServiceRule(Class<? extends µService> µServiceClass) {
    this.µServiceClass = µServiceClass;
  }

  @Override
  protected void before() throws Throwable {
    µServiceClass.newInstance();
    Method method = µServiceClass.getMethod("main", String[].class);
    String[] params = null;
    method.invoke(null, (Object) params);
  }

  @Override
  protected void after() {
  }
}
