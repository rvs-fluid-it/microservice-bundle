package be.fluid_it.µs.bundle.camel.guice;

import javax.inject.Singleton;

import org.apache.camel.impl.CompositeRegistry;
import org.apache.camel.spi.Registry;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;

public class RegistryModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Registry.class).to(CompositeRegistry.class);
  }

  @Provides
  @Singleton
  public CompositeRegistry getRegistry(Injector injector) {
    CompositeRegistry compositeRegistry = new CompositeRegistry();

    compositeRegistry.addRegistry(new GuiceRegistry(injector));

    Injector parentInjector = injector.getParent();
    if (parentInjector != null) {
      GuiceRegistry parentRegistry = parentInjector.getInstance(GuiceRegistry.class);
      compositeRegistry.addRegistry(parentRegistry);
    }

    return compositeRegistry;
  }
}
