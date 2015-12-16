package be.fluid_it.µs.bundle.dropwizard;

import be.fluid_it.µs.bundle.dropwizard.guice.GuiceEnvironment;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Environment;
import org.apache.camel.CamelContext;

import javax.servlet.Servlet;
import javax.validation.Validator;
import java.util.concurrent.ExecutorService;

public class µsEnvironment  {
  private final Environment environment;
  private final CamelContext camelContext;
  private final GuiceEnvironment guice;

  public µsEnvironment(Environment environment,
                       Injector bootstrapInjector,
                       Injector applicationInjector,
                       CamelContext camelContext) {
    this.environment = environment;
    this.camelContext = camelContext;
    this.guice = new GuiceEnvironment(bootstrapInjector, applicationInjector);
  }

  public CamelContext camel() {
    return camelContext;
  }

  public GuiceEnvironment guice() {
    return guice;
  }

  public JerseyEnvironment jersey() {
    return this.environment.jersey();
  }

  public ExecutorService getHealthCheckExecutorService() {
    return this.environment.getHealthCheckExecutorService();
  }

  public AdminEnvironment admin() {
    return this.environment.admin();
  }

  public LifecycleEnvironment lifecycle() {
    return this.environment.lifecycle();
  }

  public ServletEnvironment servlets() {
    return this.environment.servlets();
  }

  public ObjectMapper getObjectMapper() {
    return this.environment.getObjectMapper();
  }

  public String getName() {
    return this.environment.getName();
  }

  public Validator getValidator() {
    return this.environment.getValidator();
  }

  public MetricRegistry metrics() {
    return this.environment.metrics();
  }

  public HealthCheckRegistry healthChecks() {
    return this.environment.healthChecks();
  }

  public MutableServletContextHandler getApplicationContext() {
    return this.environment.getApplicationContext();
  }

  public Servlet getJerseyServletContainer() {
    return this.environment.getJerseyServletContainer();
  }

  public MutableServletContextHandler getAdminContext() {
    return this.environment.getAdminContext();
  }
}
