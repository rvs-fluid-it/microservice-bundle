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
import org.apache.camel.Component;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Endpoint;
import org.apache.camel.ErrorHandlerFactory;
import org.apache.camel.NoFactoryAvailableException;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Producer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.ServiceStatus;
import org.apache.camel.ShutdownRoute;
import org.apache.camel.ShutdownRunningTask;
import org.apache.camel.StartupListener;
import org.apache.camel.TypeConverter;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.spi.AsyncProcessorAwaitManager;
import org.apache.camel.spi.CamelContextNameStrategy;
import org.apache.camel.spi.ClassResolver;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatResolver;
import org.apache.camel.spi.Debugger;
import org.apache.camel.spi.EndpointRegistry;
import org.apache.camel.spi.EndpointStrategy;
import org.apache.camel.spi.ExecutorServiceManager;
import org.apache.camel.spi.ExecutorServiceStrategy;
import org.apache.camel.spi.FactoryFinder;
import org.apache.camel.spi.FactoryFinderResolver;
import org.apache.camel.spi.InflightRepository;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.camel.spi.Language;
import org.apache.camel.spi.LifecycleStrategy;
import org.apache.camel.spi.ManagementMBeanAssembler;
import org.apache.camel.spi.ManagementNameStrategy;
import org.apache.camel.spi.ManagementStrategy;
import org.apache.camel.spi.ModelJAXBContextFactory;
import org.apache.camel.spi.NodeIdFactory;
import org.apache.camel.spi.PackageScanClassResolver;
import org.apache.camel.spi.ProcessorFactory;
import org.apache.camel.spi.Registry;
import org.apache.camel.spi.RestConfiguration;
import org.apache.camel.spi.RestRegistry;
import org.apache.camel.spi.RoutePolicyFactory;
import org.apache.camel.spi.RouteStartupOrder;
import org.apache.camel.spi.RuntimeEndpointRegistry;
import org.apache.camel.spi.ServicePool;
import org.apache.camel.spi.ShutdownStrategy;
import org.apache.camel.spi.StreamCachingStrategy;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.spi.UnitOfWorkFactory;
import org.apache.camel.spi.UuidGenerator;
import org.apache.camel.util.LoadPropertiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ManagedCamelContext implements CamelContext, Managed {

  private static final Logger LOGGER = LoggerFactory.getLogger(ManagedCamelContext.class);

  private final CamelContext context;

  private ManagedCamelContext(CamelContext context) {
    this.context = context;
  }

  public static ManagedCamelContext of(CamelContext context) {
    return new ManagedCamelContext(context);
  }

  @Override
  public void setStreamCaching(Boolean cache) {
    context.setStreamCaching(cache);
  }

  @Override
  public Boolean isStreamCaching() {
    return context.isStreamCaching();
  }

  @Override
  public void setTracing(Boolean tracing) {
    context.setTracing(tracing);
  }

  @Override
  public Boolean isTracing() {
    return context.isTracing();
  }

  @Override
  public void setMessageHistory(Boolean messageHistory) {
    context.setMessageHistory(messageHistory);
  }

  @Override
  public Boolean isMessageHistory() {
    return context.isMessageHistory();
  }

  @Override
  public void setHandleFault(Boolean handleFault) {
    context.setHandleFault(handleFault);
  }

  @Override
  public Boolean isHandleFault() {
    return context.isHandleFault();
  }

  @Override
  public void setDelayer(Long delay) {
    context.setDelayer(delay);
  }

  @Override
  public Long getDelayer() {
    return context.getDelayer();
  }

  @Override
  public void setAutoStartup(Boolean autoStartup) {
    context.setAutoStartup(autoStartup);
  }

  @Override
  public Boolean isAutoStartup() {
    return context.isAutoStartup();
  }

  @Override
  public void setShutdownRoute(ShutdownRoute shutdownRoute) {
    context.setShutdownRoute(shutdownRoute);
  }

  @Override
  public ShutdownRoute getShutdownRoute() {
    return context.getShutdownRoute();
  }

  @Override
  public void setShutdownRunningTask(ShutdownRunningTask shutdownRunningTask) {
    context.setShutdownRunningTask(shutdownRunningTask);
  }

  @Override
  public ShutdownRunningTask getShutdownRunningTask() {
    return context.getShutdownRunningTask();
  }

  @Override
  public void setAllowUseOriginalMessage(Boolean allowUseOriginalMessage) {
    context.setAllowUseOriginalMessage(allowUseOriginalMessage);
  }

  @Override
  public Boolean isAllowUseOriginalMessage() {
    return context.isAllowUseOriginalMessage();
  }

  @Override
  public void suspend() throws Exception {
    context.suspend();
  }

  @Override
  public void resume() throws Exception {
    context.resume();
  }

  @Override
  public boolean isSuspended() {
    return context.isSuspended();
  }

  @Override
  public <T extends CamelContext> T adapt(Class<T> type) {
    return context.adapt(type);
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting");
    context.start();
    LOGGER.info("Started");
  }

  @Override
  public void stop() throws Exception {
    LOGGER.info("Stopping");
    context.stop();
    LOGGER.info("Stopped");
  }

  @Override
  public String getName() {
    return context.getName();
  }

  @Override
  public CamelContextNameStrategy getNameStrategy() {
    return context.getNameStrategy();
  }

  @Override
  public void setNameStrategy(CamelContextNameStrategy nameStrategy) {
    context.setNameStrategy(nameStrategy);
  }

  @Override
  public ManagementNameStrategy getManagementNameStrategy() {
    return context.getManagementNameStrategy();
  }

  @Override
  public void setManagementNameStrategy(ManagementNameStrategy nameStrategy) {
    context.setManagementNameStrategy(nameStrategy);
  }

  @Override
  public String getManagementName() {
    return context.getManagementName();
  }

  @Override
  public String getVersion() {
    return context.getVersion();
  }

  @Override
  public ServiceStatus getStatus() {
    return context.getStatus();
  }

  @Override
  public String getUptime() {
    return context.getUptime();
  }

  @Override
  public void addService(Object object) throws Exception {
    context.addService(object);
  }

  @Override
  public void addService(Object object, boolean closeOnShutdown) throws Exception {
    context.addService(object, closeOnShutdown);
  }

  @Override
  public boolean removeService(Object object) throws Exception {
    return context.removeService(object);
  }

  @Override
  public boolean hasService(Object object) {
    return context.hasService(object);
  }

  @Override
  public <T> T hasService(Class<T> type) {
    return context.hasService(type);
  }

  @Override
  public void addStartupListener(StartupListener listener) throws Exception {
    context.addStartupListener(listener);
  }

  @Override
  public void addComponent(String componentName, Component component) {
    context.addComponent(componentName, component);
  }

  @Override
  public Component hasComponent(String componentName) {
    return context.hasComponent(componentName);
  }

  @Override
  public Component getComponent(String componentName) {
    return context.getComponent(componentName);
  }

  @Override
  public Component getComponent(String name, boolean autoCreateComponents) {
    return context.getComponent(name, autoCreateComponents);
  }

  @Override
  public <T extends Component> T getComponent(String name, Class<T> componentType) {
    return context.getComponent(name, componentType);
  }

  @Override
  public List<String> getComponentNames() {
    return context.getComponentNames();
  }

  @Override
  public Component removeComponent(String componentName) {
    return context.removeComponent(componentName);
  }

  @Override
  public EndpointRegistry<String> getEndpointRegistry() {
    return context.getEndpointRegistry();
  }

  @Override
  public Endpoint getEndpoint(String uri) {
    return context.getEndpoint(uri);
  }

  @Override
  public <T extends Endpoint> T getEndpoint(String name, Class<T> endpointType) {
    return context.getEndpoint(name, endpointType);
  }

  @Override
  public Collection<Endpoint> getEndpoints() {
    return context.getEndpoints();
  }

  @Override
  public Map<String, Endpoint> getEndpointMap() {
    return context.getEndpointMap();
  }

  @Override
  public Endpoint hasEndpoint(String uri) {
    return context.hasEndpoint(uri);
  }

  @Override
  public Endpoint addEndpoint(String uri, Endpoint endpoint) throws Exception {
    return context.addEndpoint(uri, endpoint);
  }

  @Override
  public void removeEndpoint(Endpoint endpoint) throws Exception {
    removeEndpoint(endpoint);
  }

  @Override
  public Collection<Endpoint> removeEndpoints(String pattern) throws Exception {
    return context.removeEndpoints(pattern);
  }

  @Override
  public void addRegisterEndpointCallback(EndpointStrategy strategy) {
    context.addRegisterEndpointCallback(strategy);
  }

  @Override
  public void setupRoutes(boolean done) {
    context.setupRoutes(done);
  }

  @Override
  @Deprecated
  public List<RouteDefinition> getRouteDefinitions() {
    return context.getRouteDefinitions();
  }

  @Override
  @Deprecated
  public RouteDefinition getRouteDefinition(String id) {
    return context.getRouteDefinition(id);
  }

  @Override
  @Deprecated
  public List<RestDefinition> getRestDefinitions() {
    return context.getRestDefinitions();
  }

  @Override
  @Deprecated
  public void addRestDefinitions(Collection<RestDefinition> restDefinitions) throws Exception {
    context.addRestDefinitions(restDefinitions);
  }

  @Override
  public void setRestConfiguration(RestConfiguration restConfiguration) {
    context.setRestConfiguration(restConfiguration);
  }

  @Override
  public RestConfiguration getRestConfiguration() {
    return context.getRestConfiguration();
  }

  @Override
  public List<RouteStartupOrder> getRouteStartupOrder() {
    return context.getRouteStartupOrder();
  }

  @Override
  public List<Route> getRoutes() {
    return context.getRoutes();
  }

  @Override
  public Route getRoute(String id) {
    return context.getRoute(id);
  }

  @Override
  public void addRoutes(RoutesBuilder builder) throws Exception {
    context.addRoutes(builder);
  }

  @Override
  @Deprecated
  public RoutesDefinition loadRoutesDefinition(InputStream is) throws Exception {
    return context.loadRoutesDefinition(is);
  }

  @Override
  @Deprecated
  public void addRouteDefinitions(Collection<RouteDefinition> routeDefinitions) throws Exception {
    context.addRouteDefinitions(routeDefinitions);
  }

  @Override
  @Deprecated
  public void addRouteDefinition(RouteDefinition routeDefinition) throws Exception {
    context.addRouteDefinition(routeDefinition);
  }

  @Override
  @Deprecated
  public void removeRouteDefinitions(Collection<RouteDefinition> routeDefinitions) throws Exception {
    context.removeRouteDefinitions(routeDefinitions);
  }

  @Override
  @Deprecated
  public void removeRouteDefinition(RouteDefinition routeDefinition) throws Exception {
    context.removeRouteDefinition(routeDefinition);
  }

  @Override
  @Deprecated
  public void startRoute(RouteDefinition route) throws Exception {
    context.startRoute(route);
  }

  @Override
  public void startAllRoutes() throws Exception {
    context.startAllRoutes();
  }

  @Override
  public void startRoute(String routeId) throws Exception {
    context.startRoute(routeId);
  }

  @Override
  @Deprecated
  public void stopRoute(RouteDefinition route) throws Exception {
    context.stopRoute(route);
  }

  @Override
  public void stopRoute(String routeId) throws Exception {
    context.stopRoute(routeId);
  }

  @Override
  public void stopRoute(String routeId, long timeout, TimeUnit timeUnit) throws Exception {
    context.stopRoute(routeId, timeout, timeUnit);
  }

  @Override
  public boolean stopRoute(String routeId, long timeout, TimeUnit timeUnit, boolean abortAfterTimeout) throws Exception {
    return context.stopRoute(routeId, timeout, timeUnit, abortAfterTimeout);
  }

  @Override
  @Deprecated
  public void shutdownRoute(String routeId) throws Exception {
    context.shutdownRoute(routeId);
  }

  @Override
  @Deprecated
  public void shutdownRoute(String routeId, long timeout, TimeUnit timeUnit) throws Exception {
    context.shutdownRoute(routeId, timeout, timeUnit);
  }

  @Override
  public boolean removeRoute(String routeId) throws Exception {
    return context.removeRoute(routeId);
  }

  @Override
  public void resumeRoute(String routeId) throws Exception {
    context.resumeRoute(routeId);
  }

  @Override
  public void suspendRoute(String routeId) throws Exception {
    context.suspendRoute(routeId);
  }

  @Override
  public void suspendRoute(String routeId, long timeout, TimeUnit timeUnit) throws Exception {
    context.suspendRoute(routeId, timeout, timeUnit);
  }

  @Override
  public ServiceStatus getRouteStatus(String routeId) {
    return context.getRouteStatus(routeId);
  }

  @Override
  public boolean isStartingRoutes() {
    return context.isStartingRoutes();
  }

  @Override
  public boolean isSetupRoutes() {
    return context.isSetupRoutes();
  }

  @Override
  public TypeConverter getTypeConverter() {
    return context.getTypeConverter();
  }

  @Override
  public TypeConverterRegistry getTypeConverterRegistry() {
    return context.getTypeConverterRegistry();
  }

  @Override
  public Registry getRegistry() {
    return context.getRegistry();
  }

  @Override
  public <T> T getRegistry(Class<T> type) {
    return context.getRegistry(type);
  }

  @Override
  public Injector getInjector() {
    return context.getInjector();
  }

  @Override
  public ManagementMBeanAssembler getManagementMBeanAssembler() {
    return context.getManagementMBeanAssembler();
  }

  @Override
  public List<LifecycleStrategy> getLifecycleStrategies() {
    return context.getLifecycleStrategies();
  }

  @Override
  public void addLifecycleStrategy(LifecycleStrategy lifecycleStrategy) {
    context.addLifecycleStrategy(lifecycleStrategy);
  }

  @Override
  public Language resolveLanguage(String language) {
    return context.resolveLanguage(language);
  }

  @Override
  public String resolvePropertyPlaceholders(String text) throws Exception {
    return context.resolvePropertyPlaceholders(text);
  }

  @Override
  public String getPropertyPrefixToken() {
    return context.getPropertyPrefixToken();
  }

  @Override
  public String getPropertySuffixToken() {
    return context.getPropertySuffixToken();
  }

  @Override
  public List<String> getLanguageNames() {
    return context.getLanguageNames();
  }

  @Override
  public ProducerTemplate createProducerTemplate() {
    return context.createProducerTemplate();
  }

  @Override
  public ProducerTemplate createProducerTemplate(int maximumCacheSize) {
    return context.createProducerTemplate(maximumCacheSize);
  }

  @Override
  public ConsumerTemplate createConsumerTemplate() {
    return context.createConsumerTemplate();
  }

  @Override
  public ConsumerTemplate createConsumerTemplate(int maximumCacheSize) {
    return context.createConsumerTemplate(maximumCacheSize);
  }

  @Override
  public void addInterceptStrategy(InterceptStrategy interceptStrategy) {
    context.addInterceptStrategy(interceptStrategy);
  }

  @Override
  public List<InterceptStrategy> getInterceptStrategies() {
    return context.getInterceptStrategies();
  }

  @Override
  @Deprecated
  public ErrorHandlerBuilder getErrorHandlerBuilder() {
    return context.getErrorHandlerBuilder();
  }

  @Override
  public void setErrorHandlerBuilder(ErrorHandlerFactory errorHandlerBuilder) {
    context.setErrorHandlerBuilder(errorHandlerBuilder);
  }

  @Override
  public ScheduledExecutorService getErrorHandlerExecutorService() {
    return context.getErrorHandlerExecutorService();
  }

  @Override
  @Deprecated
  public void setDataFormats(Map<String, DataFormatDefinition> dataFormats) {
    context.setDataFormats(dataFormats);
  }

  @Override
  @Deprecated
  public Map<String, DataFormatDefinition> getDataFormats() {
    return context.getDataFormats();
  }

  @Override
  public DataFormat resolveDataFormat(String name) {
    return context.resolveDataFormat(name);
  }

  @Override
  @Deprecated
  public DataFormatDefinition resolveDataFormatDefinition(String name) {
    return context.resolveDataFormatDefinition(name);
  }

  @Override
  public DataFormatResolver getDataFormatResolver() {
    return context.getDataFormatResolver();
  }

  @Override
  public void setDataFormatResolver(DataFormatResolver dataFormatResolver) {
    context.setDataFormatResolver(dataFormatResolver);
  }

  @Override
  public void setProperties(Map<String, String> properties) {
    context.setProperties(properties);
  }

  @Override
  public Map<String, String> getProperties() {
    return context.getProperties();
  }

  @Override
  public String getProperty(String name) {
    return context.getProperty(name);
  }

  @Override
  public FactoryFinder getDefaultFactoryFinder() {
    return context.getDefaultFactoryFinder();
  }

  @Override
  public void setFactoryFinderResolver(FactoryFinderResolver resolver) {
    context.setFactoryFinderResolver(resolver);
  }

  @Override
  public FactoryFinder getFactoryFinder(String path) throws NoFactoryAvailableException {
    return context.getFactoryFinder(path);
  }

  @Override
  public ClassResolver getClassResolver() {
    return context.getClassResolver();
  }

  @Override
  public PackageScanClassResolver getPackageScanClassResolver() {
    return context.getPackageScanClassResolver();
  }

  @Override
  public void setClassResolver(ClassResolver resolver) {
    context.setClassResolver(resolver);
  }

  @Override
  public void setPackageScanClassResolver(PackageScanClassResolver resolver) {
    context.setPackageScanClassResolver(resolver);
  }

  @Override
  public void setProducerServicePool(ServicePool<Endpoint, Producer> servicePool) {
    context.setProducerServicePool(servicePool);
  }

  @Override
  public ServicePool<Endpoint, Producer> getProducerServicePool() {
    return context.getProducerServicePool();
  }

  @Override
  public void setPollingConsumerServicePool(ServicePool<Endpoint, PollingConsumer> servicePool) {
    context.setPollingConsumerServicePool(servicePool);
  }

  @Override
  public ServicePool<Endpoint, PollingConsumer> getPollingConsumerServicePool() {
    return context.getPollingConsumerServicePool();
  }

  @Override
  public void setNodeIdFactory(NodeIdFactory factory) {
    context.setNodeIdFactory(factory);
  }

  @Override
  public NodeIdFactory getNodeIdFactory() {
    return context.getNodeIdFactory();
  }

  @Override
  public ManagementStrategy getManagementStrategy() {
    return context.getManagementStrategy();
  }

  @Override
  public void setManagementStrategy(ManagementStrategy strategy) {
    context.setManagementStrategy(strategy);
  }

  @Override
  public InterceptStrategy getDefaultTracer() {
    return context.getDefaultTracer();
  }

  @Override
  public void setDefaultTracer(InterceptStrategy tracer) {
    context.setDefaultTracer(tracer);
  }

  @Override
  public InterceptStrategy getDefaultBacklogTracer() {
    return context.getDefaultBacklogTracer();
  }

  @Override
  public void setDefaultBacklogTracer(InterceptStrategy backlogTracer) {
    context.setDefaultBacklogTracer(backlogTracer);
  }

  @Override
  public InterceptStrategy getDefaultBacklogDebugger() {
    return context.getDefaultBacklogDebugger();
  }

  @Override
  public void setDefaultBacklogDebugger(InterceptStrategy backlogDebugger) {
    context.setDefaultBacklogDebugger(backlogDebugger);
  }

  @Override
  public void disableJMX() throws IllegalStateException {
    context.disableJMX();
  }

  @Override
  public InflightRepository getInflightRepository() {
    return context.getInflightRepository();
  }

  @Override
  public void setInflightRepository(InflightRepository repository) {
    context.setInflightRepository(repository);
  }

  @Override
  public AsyncProcessorAwaitManager getAsyncProcessorAwaitManager() {
    return context.getAsyncProcessorAwaitManager();
  }

  @Override
  public void setAsyncProcessorAwaitManager(AsyncProcessorAwaitManager manager) {
    context.setAsyncProcessorAwaitManager(manager);
  }

  @Override
  public ClassLoader getApplicationContextClassLoader() {
    return context.getApplicationContextClassLoader();
  }

  @Override
  public void setApplicationContextClassLoader(ClassLoader classLoader) {
    context.setApplicationContextClassLoader(classLoader);
  }

  @Override
  public ShutdownStrategy getShutdownStrategy() {
    return context.getShutdownStrategy();
  }

  @Override
  public void setShutdownStrategy(ShutdownStrategy shutdownStrategy) {
    context.setShutdownStrategy(shutdownStrategy);
  }

  @Override
  public ExecutorServiceManager getExecutorServiceManager() {
    return context.getExecutorServiceManager();
  }

  @Override
  @Deprecated
  public ExecutorServiceStrategy getExecutorServiceStrategy() {
    return context.getExecutorServiceStrategy();
  }

  @Override
  public void setExecutorServiceManager(ExecutorServiceManager executorServiceManager) {
    context.setExecutorServiceManager(executorServiceManager);
  }

  @Override
  public ProcessorFactory getProcessorFactory() {
    return context.getProcessorFactory();
  }

  @Override
  public void setProcessorFactory(ProcessorFactory processorFactory) {
    context.setProcessorFactory(processorFactory);
  }

  @Override
  public Debugger getDebugger() {
    return context.getDebugger();
  }

  @Override
  public void setDebugger(Debugger debugger) {
    context.setDebugger(debugger);
  }

  @Override
  public UuidGenerator getUuidGenerator() {
    return context.getUuidGenerator();
  }

  @Override
  public void setUuidGenerator(UuidGenerator uuidGenerator) {
    context.setUuidGenerator(uuidGenerator);
  }

  @Override
  @Deprecated
  public Boolean isLazyLoadTypeConverters() {
    return context.isLazyLoadTypeConverters();
  }

  @Override
  @Deprecated
  public void setLazyLoadTypeConverters(Boolean lazyLoadTypeConverters) {
    context.setLazyLoadTypeConverters(lazyLoadTypeConverters);
  }

  @Override
  public Boolean isTypeConverterStatisticsEnabled() {
    return context.isTypeConverterStatisticsEnabled();
  }

  @Override
  public void setTypeConverterStatisticsEnabled(Boolean typeConverterStatisticsEnabled) {
    context.setTypeConverterStatisticsEnabled(typeConverterStatisticsEnabled);
  }

  @Override
  public Boolean isUseMDCLogging() {
    return context.isUseMDCLogging();
  }

  @Override
  public void setUseMDCLogging(Boolean useMDCLogging) {
    context.setUseMDCLogging(useMDCLogging);
  }

  @Override
  public Boolean isUseBreadcrumb() {
    return context.isUseBreadcrumb();
  }

  @Override
  public void setUseBreadcrumb(Boolean useBreadcrumb) {
    context.setUseBreadcrumb(useBreadcrumb);
  }

  @Override
  public String resolveComponentDefaultName(String javaType) {
    return context.resolveComponentDefaultName(javaType);
  }

  @Override
  public Map<String, Properties> findComponents() throws LoadPropertiesException, IOException {
    return context.findComponents();
  }

  @Override
  public Map<String, Properties> findEips() throws LoadPropertiesException, IOException {
    return context.findEips();
  }

  @Override
  public String getComponentDocumentation(String componentName) throws IOException {
    return context.getComponentDocumentation(componentName);
  }

  @Override
  public String getComponentParameterJsonSchema(String componentName) throws IOException {
    return context.getComponentParameterJsonSchema(componentName);
  }

  @Override
  public String getDataFormatParameterJsonSchema(String dataFormatName) throws IOException {
    return context.getDataFormatParameterJsonSchema(dataFormatName);
  }

  @Override
  public String getLanguageParameterJsonSchema(String languageName) throws IOException {
    return context.getLanguageParameterJsonSchema(languageName);
  }

  @Override
  public String getEipParameterJsonSchema(String eipName) throws IOException {
    return context.getEipParameterJsonSchema(eipName);
  }

  @Override
  public String explainEipJson(String nameOrId, boolean includeAllOptions) {
    return context.explainEipJson(nameOrId, includeAllOptions);
  }

  @Override
  public String explainComponentJson(String componentName, boolean includeAllOptions) {
    return context.explainComponentJson(componentName, includeAllOptions);
  }

  @Override
  public String explainEndpointJson(String uri, boolean includeAllOptions) {
    return context.explainEndpointJson(uri, includeAllOptions);
  }

  @Override
  public String createRouteStaticEndpointJson(String routeId) {
    return context.createRouteStaticEndpointJson(routeId);
  }

  @Override
  public String createRouteStaticEndpointJson(String routeId, boolean includeDynamic) {
    return context.createRouteStaticEndpointJson(routeId, includeDynamic);
  }

  @Override
  public StreamCachingStrategy getStreamCachingStrategy() {
    return context.getStreamCachingStrategy();
  }

  @Override
  public void setStreamCachingStrategy(StreamCachingStrategy streamCachingStrategy) {
    context.setStreamCachingStrategy(streamCachingStrategy);
  }

  @Override
  public UnitOfWorkFactory getUnitOfWorkFactory() {
    return context.getUnitOfWorkFactory();
  }

  @Override
  public void setUnitOfWorkFactory(UnitOfWorkFactory unitOfWorkFactory) {
    context.setUnitOfWorkFactory(unitOfWorkFactory);
  }

  @Override
  public RuntimeEndpointRegistry getRuntimeEndpointRegistry() {
    return context.getRuntimeEndpointRegistry();
  }

  @Override
  public void setRuntimeEndpointRegistry(RuntimeEndpointRegistry runtimeEndpointRegistry) {
    context.setRuntimeEndpointRegistry(runtimeEndpointRegistry);
  }

  @Override
  public RestRegistry getRestRegistry() {
    return context.getRestRegistry();
  }

  @Override
  public void setRestRegistry(RestRegistry restRegistry) {
    context.setRestRegistry(restRegistry);
  }

  @Override
  public void addRoutePolicyFactory(RoutePolicyFactory routePolicyFactory) {
    context.addRoutePolicyFactory(routePolicyFactory);
  }

  @Override
  public List<RoutePolicyFactory> getRoutePolicyFactories() {
    return context.getRoutePolicyFactories();
  }

  @Override
  public ModelJAXBContextFactory getModelJAXBContextFactory() {
    return context.getModelJAXBContextFactory();
  }

  @Override
  public void setModelJAXBContextFactory(ModelJAXBContextFactory modelJAXBContextFactory) {
    context.setModelJAXBContextFactory(modelJAXBContextFactory);
  }
}