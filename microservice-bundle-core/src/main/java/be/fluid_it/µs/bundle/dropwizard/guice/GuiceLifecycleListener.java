package be.fluid_it.µs.bundle.dropwizard.guice;

public interface GuiceLifecycleListener {
    void beforeGuiceStart();
    void guiceStarted();
}
