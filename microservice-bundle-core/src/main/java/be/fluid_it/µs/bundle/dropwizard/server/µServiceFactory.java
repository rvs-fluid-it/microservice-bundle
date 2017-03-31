package be.fluid_it.µs.bundle.dropwizard.server;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jersey.errors.EarlyEofExceptionMapper;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.Servlet;
import javax.validation.Validator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@JsonTypeName("micro")
public class µServiceFactory extends DefaultServerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(µServiceFactory.class);

    @Override
    public Server build(Environment environment) {
        setRegisterDefaultExceptionMappers(Boolean.FALSE);
        return super.build(environment);
    }

    @Override
    protected Handler createAppServlet(Server server,
                                       JerseyEnvironment jersey,
                                       ObjectMapper objectMapper,
                                       Validator validator,
                                       MutableServletContextHandler handler,
                                       @Nullable Servlet jerseyContainer,
                                       MetricRegistry metricRegistry) {
        if (jerseyContainer != null) {
            jersey.register(new LoggingExceptionMapper<Throwable>() {
                @Override
                public Response toResponse(Throwable exception) {
                    final int status;
                    final StatusEntity statusEntity;

                    if (exception instanceof WebApplicationException) {
                        final Response response = ((WebApplicationException) exception).getResponse();
                        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SERVER_ERROR)) {
                            logException(exception);
                        }
                        status = response.getStatus();
                        statusEntity = new StatusEntity(status, new StatusMessage(StatusMessage.Type.ERROR, exception.getLocalizedMessage()));
                    } else {
                        final long id = logException(exception);
                        status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
                        StatusMessage message = new StatusMessage(StatusMessage.Type.ERROR, String.format("%016x", id), formatErrorMessage(id, exception));
                        statusEntity = new StatusEntity(status, message);
                    }

                    return Response.status(status)
                            .type(MediaType.APPLICATION_JSON_TYPE)
                            .entity(statusEntity)
                            .build();
                }
            });
            jersey.register(new ConstraintViolationExceptionMapper());
            jersey.register(new JsonProcessingExceptionMapper());
            jersey.register(new EarlyEofExceptionMapper());
        }
        Handler appServlet = super.createAppServlet(server, jersey, objectMapper, validator, handler, jerseyContainer, metricRegistry);
        return appServlet;
    }
}
