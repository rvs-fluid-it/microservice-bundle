package be.fluid_it.µs.bundle.dropwizard.camel.health;
/*
 * Copyright (C) 2014 Commerce Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.codahale.metrics.health.HealthCheck;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.StatefulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CamelHealthCheck extends HealthCheck {

  private static final Logger LOGGER = LoggerFactory.getLogger(CamelHealthCheck.class);

  private final CamelContext camelContext;

  public CamelHealthCheck(CamelContext camelContext) {
    this.camelContext = camelContext;
  }

  @Override
  protected Result check() throws Exception {
    Result result = checkCamelContext();
    if (!result.isHealthy()) {
      return result;
    }

    List<Route> routes = camelContext.getRoutes();
    if (routes != null && routes.size() > 0) {
      for (Route route : routes) {
        result = checkRoute(route);
        if (!result.isHealthy()) {
          return result;
        }
      }
    }

    List<String> componentNames = camelContext.getComponentNames();
    if (componentNames != null && componentNames.size() > 0) {
      for (String componentName : componentNames) {
        result = checkComponent(componentName);
        if (!result.isHealthy()) {
          return result;
        }
      }
    }

    Map<String, Endpoint> endpointsByKey = camelContext.getEndpointMap();
    if (endpointsByKey.size() > 0) {
      Set<String> keys = endpointsByKey.keySet();
      if (keys.size() > 0) {
        for (String endpointKey : keys) {
          Endpoint endpoint = endpointsByKey.get(endpointKey);
          result = checkEndpoint(endpointKey, endpoint);
          if (!result.isHealthy()) {
            return result;
          }
        }
      }
    }

    return result;
  }

  private Result checkCamelContext() {
    LOGGER.debug("Checking context [{}] of type [{}]", camelContext.getName(), camelContext.getClass());
    ServiceStatus status = camelContext.getStatus();
    if (!status.isStarted()) {
      return Result.unhealthy(String.format("CamelContext [%s] is not running", camelContext.getName()));
    }
    return Result.healthy();
  }

  private Result checkRoute(Route route) {
    LOGGER.debug("Checking route [{}] of type [{}]", route.getId(), route.getClass());
    if (route instanceof StatefulService) {
      StatefulService statefulRoute = (StatefulService) route;
      if (!statefulRoute.isStarted()) {
        return Result.unhealthy(String.format("Route [%s] is not running", route.getId()));
      }
    }
    Consumer consumer = route.getConsumer();
    if (consumer != null) {
      LOGGER.debug("Checking route [{}]'s consumer of type [{}]", route.getId(), consumer.getClass());
      if (consumer instanceof StatefulService) {
        StatefulService statefulConsumer = (StatefulService) consumer;
        if (!statefulConsumer.isStarted()) {
          return Result.unhealthy(String.format("Route [%s]'s consumer is not running", route.getId()));
        }
      }
    }
    return Result.healthy();
  }

  private Result checkComponent(String componentName) {
    Component component = camelContext.getComponent(componentName);
    LOGGER.debug("Checking component [{}] of type [{}]", componentName, component.getClass());
    if (component instanceof StatefulService) {
      StatefulService statefulComponent = (StatefulService) component;
      if (!statefulComponent.isStarted()) {
        return Result.unhealthy(String.format("Component [%s] is not running", componentName));
      }
    }
    return Result.healthy();
  }

  private Result checkEndpoint(String endpointKey, Endpoint endpoint) {
    LOGGER.debug("Checking endpoint [{}] of type [{}]", endpointKey, endpoint.getClass());
    if (endpoint instanceof StatefulService) {
      StatefulService statefulEndpoint = (StatefulService) endpoint;
      if (!statefulEndpoint.isStarted()) {
        return Result.unhealthy(String.format("Endpoint [%s] is not running", endpointKey));
      }
    }
    return Result.healthy();
  }

}