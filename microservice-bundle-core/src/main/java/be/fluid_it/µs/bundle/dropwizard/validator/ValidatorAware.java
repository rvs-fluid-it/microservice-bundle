package be.fluid_it.µs.bundle.dropwizard.validator;

import be.fluid_it.µs.bundle.dropwizard.jackson.JsonPrettyPrintable;
import com.github.valdr.Options;

public interface ValidatorAware extends JsonPrettyPrintable {
  Options getValidator();
}
