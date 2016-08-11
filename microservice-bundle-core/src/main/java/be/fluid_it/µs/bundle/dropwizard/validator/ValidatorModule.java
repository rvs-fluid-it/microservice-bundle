package be.fluid_it.µs.bundle.dropwizard.validator;

import com.github.valdr.Options;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.dropwizard.Configuration;

public class ValidatorModule extends AbstractModule {
  @Override
  protected void configure() {
  }

  @Provides
  public Options provideValidator(Configuration configuration) {
    return configuration instanceof ValidatorAware ? ((ValidatorAware)configuration).getValidator() : null;
  }
}
