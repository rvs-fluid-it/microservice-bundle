package be.fluid_it.µs.bundle.dropwizard.camel;
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

import io.dropwizard.lifecycle.Managed;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spi.Synchronization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ManagedProducerTemplate implements ProducerTemplate, Managed {

  private static final Logger LOGGER = LoggerFactory.getLogger(ManagedProducerTemplate.class);

  private final ProducerTemplate template;

  private ManagedProducerTemplate(ProducerTemplate template) {
    this.template = template;
  }

  public static ManagedProducerTemplate of(ProducerTemplate template) {
    return new ManagedProducerTemplate(template);
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting");
    template.start();
    LOGGER.info("Started");
  }

  @Override
  public void stop() throws Exception {
    LOGGER.info("Stopping");
    template.stop();
    LOGGER.info("Stopped");
  }

  @Override
  public CamelContext getCamelContext() {
    return template.getCamelContext();
  }

  @Override
  public int getMaximumCacheSize() {
    return template.getMaximumCacheSize();
  }

  @Override
  public void setMaximumCacheSize(int maximumCacheSize) {
    template.setMaximumCacheSize(maximumCacheSize);
  }

  @Override
  public int getCurrentCacheSize() {
    return template.getCurrentCacheSize();
  }

  @Override
  public Endpoint getDefaultEndpoint() {
    return template.getDefaultEndpoint();
  }

  @Override
  public void setDefaultEndpoint(Endpoint defaultEndpoint) {
    template.setDefaultEndpoint(defaultEndpoint);
  }

  @Override
  public void setDefaultEndpointUri(String endpointUri) {
    template.setDefaultEndpointUri(endpointUri);
  }

  @Override
  public void setEventNotifierEnabled(boolean enabled) {
    template.setEventNotifierEnabled(enabled);
  }

  @Override
  public boolean isEventNotifierEnabled() {
    return template.isEventNotifierEnabled();
  }

  @Override
  public Exchange send(Exchange exchange) {
    return template.send(exchange);
  }

  @Override
  public Exchange send(Processor processor) {
    return template.send(processor);
  }

  @Override
  public void sendBody(Object body) throws CamelExecutionException {
    template.sendBody(body);
  }

  @Override
  public void sendBodyAndHeader(Object body, String header, Object headerValue) throws CamelExecutionException {
    template.sendBodyAndHeader(body, header, headerValue);
  }

  @Override
  public void sendBodyAndProperty(Object body, String property, Object propertyValue) throws CamelExecutionException {
    template.sendBodyAndProperty(body, property, propertyValue);
  }

  @Override
  public void sendBodyAndHeaders(Object body, Map<String, Object> headers) throws CamelExecutionException {
    template.sendBodyAndHeaders(body, headers);
  }

  @Override
  public Exchange send(String endpointUri, Exchange exchange) {
    return template.send(endpointUri, exchange);
  }

  @Override
  public Exchange send(String endpointUri, Processor processor) {
    return template.send(endpointUri, processor);
  }

  @Override
  public Exchange send(String endpointUri, ExchangePattern pattern, Processor processor) {
    return template.send(endpointUri, pattern, processor);
  }

  @Override
  public Exchange send(Endpoint endpoint, Exchange exchange) {
    return template.send(endpoint, exchange);
  }

  @Override
  public Exchange send(Endpoint endpoint, Processor processor) {
    return template.send(endpoint, processor);
  }

  @Override
  public Exchange send(Endpoint endpoint, ExchangePattern pattern, Processor processor) {
    return template.send(endpoint, pattern, processor);
  }

  @Override
  public void sendBody(Endpoint endpoint, Object body) throws CamelExecutionException {
    template.sendBody(endpoint, body);
  }

  @Override
  public void sendBody(String endpointUri, Object body) throws CamelExecutionException {
    template.sendBody(endpointUri, body);
  }

  @Override
  public Object sendBody(Endpoint endpoint, ExchangePattern pattern, Object body) throws CamelExecutionException {
    return template.sendBody(endpoint, pattern, body);
  }

  @Override
  public Object sendBody(String endpointUri, ExchangePattern pattern, Object body) throws CamelExecutionException {
    return template.sendBody(endpointUri, pattern, body);
  }

  @Override
  public void sendBodyAndHeader(String endpointUri, Object body, String header, Object headerValue) throws CamelExecutionException {
    template.sendBodyAndHeader(endpointUri, body, header, headerValue);
  }

  @Override
  public void sendBodyAndHeader(Endpoint endpoint, Object body, String header, Object headerValue) throws CamelExecutionException {
    template.sendBodyAndHeader(endpoint, body, header, headerValue);
  }

  @Override
  public Object sendBodyAndHeader(Endpoint endpoint, ExchangePattern pattern, Object body, String header, Object headerValue) throws CamelExecutionException {
    return template.sendBodyAndHeader(endpoint, pattern, body, header, headerValue);
  }

  @Override
  public Object sendBodyAndHeader(String endpoint, ExchangePattern pattern, Object body, String header, Object headerValue) throws CamelExecutionException {
    return template.sendBodyAndHeader(endpoint, pattern, body, header, headerValue);
  }

  @Override
  public void sendBodyAndProperty(String endpointUri, Object body, String property, Object propertyValue) throws CamelExecutionException {
    template.sendBodyAndProperty(endpointUri, body, property, propertyValue);
  }

  @Override
  public void sendBodyAndProperty(Endpoint endpoint, Object body, String property, Object propertyValue) throws CamelExecutionException {
    template.sendBodyAndProperty(endpoint, body, property, propertyValue);
  }

  @Override
  public Object sendBodyAndProperty(Endpoint endpoint, ExchangePattern pattern, Object body, String property, Object propertyValue) throws CamelExecutionException {
    return template.sendBodyAndProperty(endpoint, pattern, body, property, propertyValue);
  }

  @Override
  public Object sendBodyAndProperty(String endpoint, ExchangePattern pattern, Object body, String property, Object propertyValue) throws CamelExecutionException {
    return template.sendBodyAndProperty(endpoint, pattern, body, property, propertyValue);
  }

  @Override
  public void sendBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers) throws CamelExecutionException {
    template.sendBodyAndHeaders(endpointUri, body, headers);
  }

  @Override
  public void sendBodyAndHeaders(Endpoint endpoint, Object body, Map<String, Object> headers) throws CamelExecutionException {
    template.sendBodyAndHeaders(endpoint, body, headers);
  }

  @Override
  public Object sendBodyAndHeaders(String endpointUri, ExchangePattern pattern, Object body, Map<String, Object> headers) throws CamelExecutionException {
    return template.sendBodyAndHeaders(endpointUri, pattern, body, headers);
  }

  @Override
  public Object sendBodyAndHeaders(Endpoint endpoint, ExchangePattern pattern, Object body, Map<String, Object> headers) throws CamelExecutionException {
    return template.sendBodyAndHeaders(endpoint, pattern, body, headers);
  }

  @Override
  public Exchange request(Endpoint endpoint, Processor processor) {
    return template.request(endpoint, processor);
  }

  @Override
  public Exchange request(String endpointUri, Processor processor) {
    return template.request(endpointUri, processor);
  }

  @Override
  public Object requestBody(Object body) throws CamelExecutionException {
    return template.requestBody(body);
  }

  @Override
  public <T> T requestBody(Object body, Class<T> type) throws CamelExecutionException {
    return template.requestBody(body, type);
  }

  @Override
  public Object requestBody(Endpoint endpoint, Object body) throws CamelExecutionException {
    return template.requestBody(endpoint, body);
  }

  @Override
  public <T> T requestBody(Endpoint endpoint, Object body, Class<T> type) throws CamelExecutionException {
    return template.requestBody(endpoint, body, type);
  }

  @Override
  public Object requestBody(String endpointUri, Object body) throws CamelExecutionException {
    return template.requestBody(endpointUri, body);
  }

  @Override
  public <T> T requestBody(String endpointUri, Object body, Class<T> type) throws CamelExecutionException {
    return template.requestBody(endpointUri, body, type);
  }

  @Override
  public Object requestBodyAndHeader(Object body, String header, Object headerValue) throws CamelExecutionException {
    return template.requestBodyAndHeader(body, header, headerValue);
  }

  @Override
  public Object requestBodyAndHeader(Endpoint endpoint, Object body, String header, Object headerValue) throws CamelExecutionException {
    return template.requestBodyAndHeader(endpoint, body, header, headerValue);
  }

  @Override
  public <T> T requestBodyAndHeader(Endpoint endpoint, Object body, String header, Object headerValue, Class<T> type) throws CamelExecutionException {
    return template.requestBodyAndHeader(endpoint, body, header, headerValue, type);
  }

  @Override
  public Object requestBodyAndHeader(String endpointUri, Object body, String header, Object headerValue) throws CamelExecutionException {
    return template.requestBodyAndHeader(endpointUri, body, header, headerValue);
  }

  @Override
  public <T> T requestBodyAndHeader(String endpointUri, Object body, String header, Object headerValue, Class<T> type) throws CamelExecutionException {
    return template.requestBodyAndHeader(endpointUri, body, header, headerValue, type);
  }

  @Override
  public Object requestBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers) throws CamelExecutionException {
    return template.requestBodyAndHeaders(endpointUri, body, headers);
  }

  @Override
  public <T> T requestBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers, Class<T> type) throws CamelExecutionException {
    return template.requestBodyAndHeaders(endpointUri, body, headers, type);
  }

  @Override
  public Object requestBodyAndHeaders(Endpoint endpoint, Object body, Map<String, Object> headers) throws CamelExecutionException {
    return template.requestBodyAndHeaders(endpoint, body, headers);
  }

  @Override
  public Object requestBodyAndHeaders(Object body, Map<String, Object> headers) throws CamelExecutionException {
    return template.requestBodyAndHeaders(body, headers);
  }

  @Override
  public <T> T requestBodyAndHeaders(Endpoint endpoint, Object body, Map<String, Object> headers, Class<T> type) throws CamelExecutionException {
    return template.requestBodyAndHeaders(endpoint, body, headers, type);
  }

  @Override
  public void setExecutorService(ExecutorService executorService) {
    template.setExecutorService(executorService);
  }

  @Override
  public Future<Exchange> asyncSend(String endpointUri, Exchange exchange) {
    return template.asyncSend(endpointUri, exchange);
  }

  @Override
  public Future<Exchange> asyncSend(String endpointUri, Processor processor) {
    return template.asyncSend(endpointUri, processor);
  }

  @Override
  public Future<Object> asyncSendBody(String endpointUri, Object body) {
    return template.asyncSendBody(endpointUri, body);
  }

  @Override
  public Future<Object> asyncRequestBody(String endpointUri, Object body) {
    return template.asyncRequestBody(endpointUri, body);
  }

  @Override
  public Future<Object> asyncRequestBodyAndHeader(String endpointUri, Object body, String header, Object headerValue) {
    return template.asyncRequestBodyAndHeader(endpointUri, body, header, headerValue);
  }

  @Override
  public Future<Object> asyncRequestBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers) {
    return template.asyncRequestBodyAndHeaders(endpointUri, body, headers);
  }

  @Override
  public <T> Future<T> asyncRequestBody(String endpointUri, Object body, Class<T> type) {
    return template.asyncRequestBody(endpointUri, body, type);
  }

  @Override
  public <T> Future<T> asyncRequestBodyAndHeader(String endpointUri, Object body, String header, Object headerValue, Class<T> type) {
    return template.asyncRequestBodyAndHeader(endpointUri, body, header, headerValue, type);
  }

  @Override
  public <T> Future<T> asyncRequestBodyAndHeaders(String endpointUri, Object body, Map<String, Object> headers, Class<T> type) {
    return template.asyncRequestBodyAndHeaders(endpointUri, body, headers, type);
  }

  @Override
  public Future<Exchange> asyncSend(Endpoint endpoint, Exchange exchange) {
    return template.asyncSend(endpoint, exchange);
  }

  @Override
  public Future<Exchange> asyncSend(Endpoint endpoint, Processor processor) {
    return template.asyncSend(endpoint, processor);
  }

  @Override
  public Future<Object> asyncSendBody(Endpoint endpoint, Object body) {
    return template.asyncSendBody(endpoint, body);
  }

  @Override
  public Future<Object> asyncRequestBody(Endpoint endpoint, Object body) {
    return template.asyncRequestBody(endpoint, body);
  }

  @Override
  public Future<Object> asyncRequestBodyAndHeader(Endpoint endpoint, Object body, String header, Object headerValue) {
    return template.asyncRequestBodyAndHeader(endpoint, body, header, headerValue);
  }

  @Override
  public Future<Object> asyncRequestBodyAndHeaders(Endpoint endpoint, Object body, Map<String, Object> headers) {
    return template.asyncRequestBodyAndHeaders(endpoint, body, headers);
  }

  @Override
  public <T> Future<T> asyncRequestBody(Endpoint endpoint, Object body, Class<T> type) {
    return template.asyncRequestBody(endpoint, body, type);
  }

  @Override
  public <T> Future<T> asyncRequestBodyAndHeader(Endpoint endpoint, Object body, String header, Object headerValue, Class<T> type) {
    return template.asyncRequestBodyAndHeader(endpoint, body, header, headerValue, type);
  }

  @Override
  public <T> Future<T> asyncRequestBodyAndHeaders(Endpoint endpoint, Object body, Map<String, Object> headers, Class<T> type) {
    return template.asyncRequestBodyAndHeaders(endpoint, body, headers, type);
  }

  @Override
  public <T> T extractFutureBody(Future<Object> future, Class<T> type) throws CamelExecutionException {
    return template.extractFutureBody(future, type);
  }

  @Override
  public <T> T extractFutureBody(Future<Object> future, long timeout, TimeUnit unit, Class<T> type) throws TimeoutException, CamelExecutionException {
    return template.extractFutureBody(future, timeout, unit, type);
  }

  @Override
  public Future<Exchange> asyncCallback(String endpointUri, Exchange exchange, Synchronization onCompletion) {
    return template.asyncCallback(endpointUri, exchange, onCompletion);
  }

  @Override
  public Future<Exchange> asyncCallback(Endpoint endpoint, Exchange exchange, Synchronization onCompletion) {
    return template.asyncCallback(endpoint, exchange, onCompletion);
  }

  @Override
  public Future<Exchange> asyncCallback(String endpointUri, Processor processor, Synchronization onCompletion) {
    return template.asyncCallback(endpointUri, processor, onCompletion);
  }

  @Override
  public Future<Exchange> asyncCallback(Endpoint endpoint, Processor processor, Synchronization onCompletion) {
    return template.asyncCallback(endpoint, processor, onCompletion);
  }

  @Override
  public Future<Object> asyncCallbackSendBody(String endpointUri, Object body, Synchronization onCompletion) {
    return template.asyncCallbackSendBody(endpointUri, body, onCompletion);
  }

  @Override
  public Future<Object> asyncCallbackSendBody(Endpoint endpoint, Object body, Synchronization onCompletion) {
    return template.asyncCallbackSendBody(endpoint, body, onCompletion);
  }

  @Override
  public Future<Object> asyncCallbackRequestBody(String endpointUri, Object body, Synchronization onCompletion) {
    return template.asyncCallbackRequestBody(endpointUri, body, onCompletion);
  }

  @Override
  public Future<Object> asyncCallbackRequestBody(Endpoint endpoint, Object body, Synchronization onCompletion) {
    return template.asyncCallbackRequestBody(endpoint, body, onCompletion);
  }

}