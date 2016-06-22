package be.fluid_it.µs.bundle.showcase.app;

import be.fluid_it.µs.bundle.dropwizard.µServiceRule;
import com.jayway.restassured.RestAssured;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class SwaggerAnnotatedHelloServiceTest {
  @ClassRule
  public static µServiceRule µService = new µServiceRule(SwaggerAnnotatedHelloService.class);

  @Test
  public void sanintyCheck() throws URISyntaxException {
    RestAssured.get(new URI("http://localhost:8886/hello")).then().assertThat().body(Matchers.containsString("Hello from configuration! (class " +
        JerseyEnvironment.class.getName() +
        ")"));
    RestAssured.get(new URI("http://localhost:8886/swagger#!/hello/hello")).then().assertThat().body(Matchers.containsString("swagger-section"));
  }
}
