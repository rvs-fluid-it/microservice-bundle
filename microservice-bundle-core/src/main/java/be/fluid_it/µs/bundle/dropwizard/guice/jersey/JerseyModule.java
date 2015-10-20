package be.fluid_it.µs.bundle.dropwizard.guice.jersey;

import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.BootstrapModule;
import com.squarespace.jersey2.guice.BootstrapUtils;
import org.glassfish.hk2.api.ServiceLocator;

//Inspired by gwizard-jersey - https://github.com/stickfigure/gwizard
public class JerseyModule extends ServletModule {
  @Override
  protected void configureServlets() {
    // The order these operations (including the steps in the linker) are important
    ServiceLocator locator = BootstrapUtils.newServiceLocator();
    install(new BootstrapModule(locator));

    bind(HK2Linker.class).asEagerSingleton();
  }
}