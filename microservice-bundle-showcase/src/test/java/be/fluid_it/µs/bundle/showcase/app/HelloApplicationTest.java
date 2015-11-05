package be.fluid_it.µs.bundle.showcase.app;

import be.fluid_it.µs.bundle.dropwizard.ApplicationRule;
import com.jayway.restassured.RestAssured;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class HelloApplicationTest {
  @ClassRule
  public static ApplicationRule application = new ApplicationRule(HelloApplication.class);

  @Test
  public void sanintyCheck() throws URISyntaxException {
    RestAssured.get(new URI("http://localhost:8880/hello")).then().assertThat().body(Matchers.containsString("Hello from configuration! (class " +
        JerseyEnvironment.class.getName() +
        ")"));
  }
}
