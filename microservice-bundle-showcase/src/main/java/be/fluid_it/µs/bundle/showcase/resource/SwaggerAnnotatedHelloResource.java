package be.fluid_it.µs.bundle.showcase.resource;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api("/hello")
@Path("/hello")
@Produces(MediaType.TEXT_PLAIN)
public class SwaggerAnnotatedHelloResource {
  private final String message;
  private final Class jerseyClass;

  @Inject
  public SwaggerAnnotatedHelloResource(@Named("message") String message,
                                       @Named("jersey") Class jerseyClass) {
    this.message = message;
    this.jerseyClass = jerseyClass;
  }

  @GET
  @ApiOperation("Hello endpoint")
  public String hello() {
    return message + " (" + jerseyClass + ")";
  }
}