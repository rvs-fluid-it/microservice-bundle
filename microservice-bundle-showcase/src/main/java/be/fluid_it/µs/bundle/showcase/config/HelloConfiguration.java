package be.fluid_it.µs.bundle.showcase.config;

import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class HelloConfiguration extends Configuration {
  @NotEmpty
  private String message;

  public String getMessage() {
    return message;
  }
}
