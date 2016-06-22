package be.fluid_it.µs.bundle.dropwizard.zipkin;

import com.smoketurner.dropwizard.zipkin.ZipkinFactory;

public interface ZipkinAware {
  ZipkinFactory getZipkin();
}
