package be.fluid_it.µs.bundle.showcase.config;

import be.fluid_it.µs.bundle.dropwizard.swagger.SwaggerAware;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SwaggerAnnotatedHelloConfiguration extends HelloConfiguration implements SwaggerAware {
  @NotNull
  private SwaggerBundleConfiguration swagger;

  @Override
  public SwaggerBundleConfiguration getSwagger() {
    return swagger;
  }

  public void setSwagger(SwaggerBundleConfiguration swagger) {
    this.swagger = swagger;
  }
}
