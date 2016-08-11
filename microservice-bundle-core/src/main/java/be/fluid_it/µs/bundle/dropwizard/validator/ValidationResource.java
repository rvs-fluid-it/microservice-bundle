package be.fluid_it.µs.bundle.dropwizard.validator;

import com.github.valdr.ConstraintBuilder;
import com.github.valdr.Options;
import com.google.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

@Path("/validation")
public class ValidationResource {
  private final Options options;

  @Inject
  public ValidationResource(Options options) {
    this.options = options;
  }

  @GET
  @Path("rules")
  @Produces(MediaType.APPLICATION_JSON)
  public HashMap rules() {
    return new ConstraintBuilder(options).build();
  }
}