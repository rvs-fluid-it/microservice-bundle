package be.fluid_it.µs.bundle.showcase.resource;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class HelloResource {
  private final String message;
  private final Class jerseyClass;

  @Inject
  public HelloResource(@Named("message") String message,
                       @Named("jersey") Class jerseyClass) {
    this.message = message;
    this.jerseyClass = jerseyClass;
  }

  @GET
  public String hello() {
    return message + " (" + jerseyClass + ")";
  }
}