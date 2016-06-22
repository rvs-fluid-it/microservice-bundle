package be.fluid_it.µs.bundle.dropwizard.swagger;

import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public interface SwaggerAware {
  SwaggerBundleConfiguration getSwagger();
}
