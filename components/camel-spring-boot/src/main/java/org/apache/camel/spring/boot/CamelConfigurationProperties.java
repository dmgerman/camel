begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|LoggingLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ManagementStatisticsLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
operator|.
name|DefaultConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.springboot"
argument_list|)
DECL|class|CamelConfigurationProperties
specifier|public
class|class
name|CamelConfigurationProperties
extends|extends
name|DefaultConfigurationProperties
argument_list|<
name|CamelConfigurationProperties
argument_list|>
block|{
comment|// Spring Boot only Properties
comment|// ---------------------------
comment|/**      * Whether to use the main run controller to ensure the Spring-Boot application      * keeps running until being stopped or the JVM terminated.      * You typically only need this if you run Spring-Boot standalone.      * If you run Spring-Boot with spring-boot-starter-web then the web container keeps the JVM running.      */
DECL|field|mainRunController
specifier|private
name|boolean
name|mainRunController
decl_stmt|;
comment|/**      * Whether to include non-singleton beans (prototypes) when scanning for RouteBuilder instances.      * By default only singleton beans is included in the context scan.      */
DECL|field|includeNonSingletons
specifier|private
name|boolean
name|includeNonSingletons
decl_stmt|;
comment|/**      * Whether to log a WARN if Camel on Spring Boot was immediately shutdown after starting which      * very likely is because there is no JVM thread to keep the application running.      */
DECL|field|warnOnEarlyShutdown
specifier|private
name|boolean
name|warnOnEarlyShutdown
init|=
literal|true
decl_stmt|;
comment|// Default Properties via camel-main
comment|// ---------------------------------
comment|// IMPORTANT: Must include the options from DefaultConfigurationProperties as spring boot apt compiler
comment|//            needs to grab the documentation from the javadoc on the field.
comment|/**      * Sets the name of the CamelContext.      */
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
comment|/**      * Timeout in seconds to graceful shutdown Camel.      */
DECL|field|shutdownTimeout
specifier|private
name|int
name|shutdownTimeout
init|=
literal|300
decl_stmt|;
comment|/**      * Whether Camel should try to suppress logging during shutdown and timeout was triggered,      * meaning forced shutdown is happening. And during forced shutdown we want to avoid logging      * errors/warnings et all in the logs as a side-effect of the forced timeout.      * Notice the suppress is a best effort as there may still be some logs coming      * from 3rd party libraries and whatnot, which Camel cannot control.      * This option is default false.      */
DECL|field|shutdownSuppressLoggingOnTimeout
specifier|private
name|boolean
name|shutdownSuppressLoggingOnTimeout
decl_stmt|;
comment|/**      * Sets whether to force shutdown of all consumers when a timeout occurred and thus      * not all consumers was shutdown within that period.      *      * You should have good reasons to set this option to false as it means that the routes      * keep running and is halted abruptly when CamelContext has been shutdown.      */
DECL|field|shutdownNowOnTimeout
specifier|private
name|boolean
name|shutdownNowOnTimeout
init|=
literal|true
decl_stmt|;
comment|/**      * Sets whether routes should be shutdown in reverse or the same order as they where started.      */
DECL|field|shutdownRoutesInReverseOrder
specifier|private
name|boolean
name|shutdownRoutesInReverseOrder
init|=
literal|true
decl_stmt|;
comment|/**      * Sets whether to log information about the inflight Exchanges which are still running      * during a shutdown which didn't complete without the given timeout.      */
DECL|field|shutdownLogInflightExchangesOnTimeout
specifier|private
name|boolean
name|shutdownLogInflightExchangesOnTimeout
init|=
literal|true
decl_stmt|;
comment|/**      * Enable JMX in your Camel application.      */
DECL|field|jmxEnabled
specifier|private
name|boolean
name|jmxEnabled
init|=
literal|true
decl_stmt|;
comment|/**      * Producer template endpoints cache size.      */
DECL|field|producerTemplateCacheSize
specifier|private
name|int
name|producerTemplateCacheSize
init|=
literal|1000
decl_stmt|;
comment|/**      * Consumer template endpoints cache size.      */
DECL|field|consumerTemplateCacheSize
specifier|private
name|int
name|consumerTemplateCacheSize
init|=
literal|1000
decl_stmt|;
comment|/**      * Whether to load custom type converters by scanning classpath.      * This is used for backwards compatibility with Camel 2.x.      * Its recommended to migrate to use fast type converter loading      * by setting<tt>@Converter(generateLoader = true)</tt> on your custom      * type converter classes.      */
DECL|field|loadTypeConverters
specifier|private
name|boolean
name|loadTypeConverters
init|=
literal|true
decl_stmt|;
comment|/**      * Directory to load additional configuration files that contains      * configuration values that takes precedence over any other configuration.      * This can be used to refer to files that may have secret configuration that      * has been mounted on the file system for containers.      *      * You must use either file: or classpath: as prefix to load      * from file system or classpath. Then you can specify a pattern to load      * from sub directories and a name pattern such as file:/var/app/secret/*.properties      */
DECL|field|fileConfigurations
specifier|private
name|String
name|fileConfigurations
decl_stmt|;
comment|/**      * Used for filtering routes routes matching the given pattern, which follows the following rules:      *      * - Match by route id      * - Match by route input endpoint uri      *      * The matching is using exact match, by wildcard and regular expression.      *      * For example to only include routes which starts with foo in their route id's, use: include=foo&#42;      * And to exclude routes which starts from JMS endpoints, use: exclude=jms:&#42;      *      * Multiple patterns can be separated by comma, for example to exclude both foo and bar routes, use: exclude=foo&#42;,bar&#42;      *      * Exclude takes precedence over include.      */
DECL|field|routeFilterIncludePattern
specifier|private
name|String
name|routeFilterIncludePattern
decl_stmt|;
comment|/**      * Used for filtering routes routes matching the given pattern, which follows the following rules:      *      * - Match by route id      * - Match by route input endpoint uri      *      * The matching is using exact match, by wildcard and regular expression.      *      * For example to only include routes which starts with foo in their route id's, use: include=foo&#42;      * And to exclude routes which starts from JMS endpoints, use: exclude=jms:&#42;      *      * Multiple patterns can be separated by comma, for example to exclude both foo and bar routes, use: exclude=foo&#42;,bar&#42;      *      * Exclude takes precedence over include.      */
DECL|field|routeFilterExcludePattern
specifier|private
name|String
name|routeFilterExcludePattern
decl_stmt|;
comment|/**      * To specify for how long time in seconds to keep running the JVM before automatic terminating the JVM.      * You can use this to run Spring Boot for a short while.      */
DECL|field|durationMaxSeconds
specifier|private
name|int
name|durationMaxSeconds
decl_stmt|;
comment|/**      * To specify for how long time in seconds Camel can be idle before automatic terminating the JVM.      * You can use this to run Spring Boot for a short while.      */
DECL|field|durationMaxIdleSeconds
specifier|private
name|int
name|durationMaxIdleSeconds
decl_stmt|;
comment|/**      * To specify how many messages to process by Camel before automatic terminating the JVM.      * You can use this to run Spring Boot for a short while.      */
DECL|field|durationMaxMessages
specifier|private
name|int
name|durationMaxMessages
decl_stmt|;
comment|/**      * Is used to limit the maximum length of the logging Camel message bodies. If the message body      * is longer than the limit, the log message is clipped. Use -1 to have unlimited length.      * Use for example 1000 to log at most 1000 characters.      */
DECL|field|logDebugMaxChars
specifier|private
name|int
name|logDebugMaxChars
decl_stmt|;
comment|/**      * Sets whether stream caching is enabled or not.      *      * Default is false.      */
DECL|field|streamCachingEnabled
specifier|private
name|boolean
name|streamCachingEnabled
decl_stmt|;
comment|/**      * Sets the stream caching spool (temporary) directory to use for overflow and spooling to disk.      *      * If no spool directory has been explicit configured, then a temporary directory      * is created in the java.io.tmpdir directory.      */
DECL|field|streamCachingSpoolDirectory
specifier|private
name|String
name|streamCachingSpoolDirectory
decl_stmt|;
comment|/**      * Sets a stream caching cipher name to use when spooling to disk to write with encryption.      * By default the data is not encrypted.      */
DECL|field|streamCachingSpoolCipher
specifier|private
name|String
name|streamCachingSpoolCipher
decl_stmt|;
comment|/**      * Stream caching threshold in bytes when overflow to disk is activated.      * The default threshold is 128kb.      * Use -1 to disable overflow to disk.      */
DECL|field|streamCachingSpoolThreshold
specifier|private
name|long
name|streamCachingSpoolThreshold
decl_stmt|;
comment|/**      * Sets a percentage (1-99) of used heap memory threshold to activate stream caching spooling to disk.      */
DECL|field|streamCachingSpoolUsedHeapMemoryThreshold
specifier|private
name|int
name|streamCachingSpoolUsedHeapMemoryThreshold
decl_stmt|;
comment|/**      * Sets what the upper bounds should be when streamCachingSpoolUsedHeapMemoryThreshold is in use.      */
DECL|field|streamCachingSpoolUsedHeapMemoryLimit
specifier|private
name|String
name|streamCachingSpoolUsedHeapMemoryLimit
decl_stmt|;
comment|/**      * Sets whether if just any of the org.apache.camel.spi.StreamCachingStrategy.SpoolRule rules      * returns true then shouldSpoolCache(long) returns true, to allow spooling to disk.      * If this option is false, then all the org.apache.camel.spi.StreamCachingStrategy.SpoolRule must      * return true.      *      * The default value is false which means that all the rules must return true.      */
DECL|field|streamCachingAnySpoolRules
specifier|private
name|boolean
name|streamCachingAnySpoolRules
decl_stmt|;
comment|/**      * Sets the stream caching buffer size to use when allocating in-memory buffers used for in-memory stream caches.      *      * The default size is 4096.      */
DECL|field|streamCachingBufferSize
specifier|private
name|int
name|streamCachingBufferSize
decl_stmt|;
comment|/**      * Whether to remove stream caching temporary directory when stopping.      * This option is default true.      */
DECL|field|streamCachingRemoveSpoolDirectoryWhenStopping
specifier|private
name|boolean
name|streamCachingRemoveSpoolDirectoryWhenStopping
init|=
literal|true
decl_stmt|;
comment|/**      * Sets whether stream caching statistics is enabled.      */
DECL|field|streamCachingStatisticsEnabled
specifier|private
name|boolean
name|streamCachingStatisticsEnabled
decl_stmt|;
comment|/**      * Sets whether backlog tracing is enabled or not.      *      * Default is false.      */
DECL|field|backlogTracing
specifier|private
name|boolean
name|backlogTracing
decl_stmt|;
comment|/**      * Sets whether tracing is enabled or not.      *      * Default is false.      */
DECL|field|tracing
specifier|private
name|boolean
name|tracing
decl_stmt|;
comment|/**      * Tracing pattern to match which node EIPs to trace.      * For example to match all To EIP nodes, use to*.      * The pattern matches by node and route id's      * Multiple patterns can be separated by comma.      */
DECL|field|tracingPattern
specifier|private
name|String
name|tracingPattern
decl_stmt|;
comment|/**      * Sets whether message history is enabled or not.      *      * Default is true.      */
DECL|field|messageHistory
specifier|private
name|boolean
name|messageHistory
init|=
literal|true
decl_stmt|;
comment|/**      * Sets whether log mask is enabled or not.      *      * Default is false.      */
DECL|field|logMask
specifier|private
name|boolean
name|logMask
decl_stmt|;
comment|/**      * Sets whether to log exhausted message body with message history.      *      * Default is false.      */
DECL|field|logExhaustedMessageBody
specifier|private
name|boolean
name|logExhaustedMessageBody
decl_stmt|;
comment|/**      * Sets whether the object should automatically start when Camel starts.      * Important: Currently only routes can be disabled, as CamelContext's are always started.      * Note: When setting auto startup false on CamelContext then that takes precedence      * and no routes is started. You would need to start CamelContext explicit using      * the org.apache.camel.CamelContext.start() method, to start the context, and then      * you would need to start the routes manually using Camelcontext.getRouteController().startRoute(String).      *      * Default is true to always start up.      */
DECL|field|autoStartup
specifier|private
name|boolean
name|autoStartup
init|=
literal|true
decl_stmt|;
comment|/**      * Sets whether to allow access to the original message from Camel's error handler,      * or from org.apache.camel.spi.UnitOfWork.getOriginalInMessage().      * Turning this off can optimize performance, as defensive copy of the original message is not needed.      *      * Default is false.      */
DECL|field|allowUseOriginalMessage
specifier|private
name|boolean
name|allowUseOriginalMessage
decl_stmt|;
comment|/**      * Sets whether endpoint runtime statistics is enabled (gathers runtime usage of each incoming and outgoing endpoints).      *      * The default value is false.      */
DECL|field|endpointRuntimeStatisticsEnabled
specifier|private
name|boolean
name|endpointRuntimeStatisticsEnabled
decl_stmt|;
comment|/**      * Whether the producer should be started lazy (on the first message). By starting lazy you can use this to allow CamelContext and routes to startup      * in situations where a producer may otherwise fail during starting and cause the route to fail being started. By deferring this startup to be lazy then      * the startup failure can be handled during routing messages via Camel's routing error handlers. Beware that when the first message is processed      * then creating and starting the producer may take a little time and prolong the total processing time of the processing.      *      * The default value is false.      */
DECL|field|endpointLazyStartProducer
specifier|private
name|boolean
name|endpointLazyStartProducer
decl_stmt|;
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler, which mean any exceptions occurred while      * the consumer is trying to pickup incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler.      *<p/>      * By default the consumer will use the org.apache.camel.spi.ExceptionHandler to deal with exceptions,      * that will be logged at WARN/ERROR level and ignored.      *      * The default value is false.      */
DECL|field|endpointBridgeErrorHandler
specifier|public
name|boolean
name|endpointBridgeErrorHandler
decl_stmt|;
comment|/**      * Whether the endpoint should use basic property binding (Camel 2.x) or the newer property binding with additional capabilities.      *      * The default value is false.      */
DECL|field|endpointBasicPropertyBinding
specifier|public
name|boolean
name|endpointBasicPropertyBinding
decl_stmt|;
comment|/**      * Whether to enable using data type on Camel messages.      *      * Data type are automatic turned on if one ore more routes has been explicit configured with input and output types.      * Otherwise data type is default off.      */
DECL|field|useDataType
specifier|private
name|boolean
name|useDataType
decl_stmt|;
comment|/**      * Set whether breadcrumb is enabled.      * The default value is false.      */
DECL|field|useBreadcrumb
specifier|private
name|boolean
name|useBreadcrumb
decl_stmt|;
comment|/**      * Sets the JMX statistics level      * The level can be set to Extended to gather additional information      *      * The default value is Default.      */
DECL|field|jmxManagementStatisticsLevel
specifier|private
name|ManagementStatisticsLevel
name|jmxManagementStatisticsLevel
init|=
name|ManagementStatisticsLevel
operator|.
name|Default
decl_stmt|;
comment|/**      * The naming pattern for creating the CamelContext JMX management name.      *      * The default pattern is #name#      */
DECL|field|jmxManagementNamePattern
specifier|private
name|String
name|jmxManagementNamePattern
init|=
literal|"#name#"
decl_stmt|;
comment|/**      * Whether JMX connector is created, allowing clients to connect remotely      *      * The default value is false.      */
DECL|field|jmxCreateConnector
specifier|private
name|boolean
name|jmxCreateConnector
decl_stmt|;
comment|/**      * To turn on MDC logging      */
DECL|field|useMdcLogging
specifier|private
name|boolean
name|useMdcLogging
decl_stmt|;
comment|/**      * Sets the pattern used for determine which custom MDC keys to propagate during message routing when      * the routing engine continues routing asynchronously for the given message. Setting this pattern to * will      * propagate all custom keys. Or setting the pattern to foo*,bar* will propagate any keys starting with      * either foo or bar.      * Notice that a set of standard Camel MDC keys are always propagated which starts with camel. as key name.      *      * The match rules are applied in this order (case insensitive):      *      * 1. exact match, returns true      * 2. wildcard match (pattern ends with a * and the name starts with the pattern), returns true      * 3. regular expression match, returns true      * 4. otherwise returns false      */
DECL|field|mdcLoggingKeysPattern
specifier|private
name|String
name|mdcLoggingKeysPattern
decl_stmt|;
comment|/**      * Sets the thread name pattern used for creating the full thread name.      *      * The default pattern is: Camel (#camelId#) thread ##counter# - #name#      *      * Where #camelId# is the name of the CamelContext.      * and #counter# is a unique incrementing counter.      * and #name# is the regular thread name.      *      * You can also use #longName# which is the long thread name which can includes endpoint parameters etc.      */
DECL|field|threadNamePattern
specifier|private
name|String
name|threadNamePattern
decl_stmt|;
comment|/**      * Sets whether bean introspection uses extended statistics.      * The default is false.      */
DECL|field|beanIntrospectionExtendedStatistics
specifier|private
name|boolean
name|beanIntrospectionExtendedStatistics
decl_stmt|;
comment|/**      * Sets the logging level used by bean introspection, logging activity of its usage.      * The default is TRACE.      */
DECL|field|beanIntrospectionLoggingLevel
specifier|private
name|LoggingLevel
name|beanIntrospectionLoggingLevel
decl_stmt|;
comment|/**      * Whether the routes collector is enabled or not.      *      * When enabled Camel will auto-discover routes (RouteBuilder instances from the registry and      * also load additional XML routes from the file system.      *      * The routes collector is default enabled.      */
DECL|field|routesCollectorEnabled
specifier|private
name|boolean
name|routesCollectorEnabled
init|=
literal|true
decl_stmt|;
comment|/**      * Used for inclusive filtering component scanning of RouteBuilder classes with @Component annotation.      * The exclusive filtering takes precedence over inclusive filtering.      * The pattern is using Ant-path style pattern.      *      * Multiple patterns can be specified separated by comma.      * For example to include all classes starting with Foo use:&#42;&#42;/Foo*      * To include all routes form a specific package use: com/mycompany/foo/&#42;      * To include all routes form a specific package and its sub-packages use double wildcards: com/mycompany/foo/&#42;&#42;      * And to include all routes from two specific packages use: com/mycompany/foo/&#42;,com/mycompany/stuff/&#42;      */
DECL|field|javaRoutesIncludePattern
specifier|private
name|String
name|javaRoutesIncludePattern
decl_stmt|;
comment|/**      * Used for exclusive filtering component scanning of RouteBuilder classes with @Component annotation.      * The exclusive filtering takes precedence over inclusive filtering.      * The pattern is using Ant-path style pattern.      * Multiple patterns can be specified separated by comma.      *      * For example to exclude all classes starting with Bar use:&#42;&#42;/Bar&#42;      * To exclude all routes form a specific package use: com/mycompany/bar/&#42;      * To exclude all routes form a specific package and its sub-packages use double wildcards: com/mycompany/bar/&#42;&#42;      * And to exclude all routes from two specific packages use: com/mycompany/bar/&#42;,com/mycompany/stuff/&#42;      */
DECL|field|javaRoutesExcludePattern
specifier|private
name|String
name|javaRoutesExcludePattern
decl_stmt|;
comment|/**      * Directory to scan for adding additional XML routes.      * You can turn this off by setting the value to false.      *      * Files can be loaded from either classpath or file by prefixing with classpath: or file:      * Wildcards is supported using a ANT pattern style paths, such as classpath:&#42;&#42;/&#42;camel&#42;.xml      *      * Multiple directories can be specified and separated by comma, such as:      * file:/myapp/mycamel/&#42;.xml,file:/myapp/myothercamel/&#42;.xml      */
DECL|field|xmlRoutes
specifier|private
name|String
name|xmlRoutes
init|=
literal|"classpath:camel/*.xml"
decl_stmt|;
comment|/**      * Directory to scan for adding additional XML rests.      * You can turn this off by setting the value to false.      *      * Files can be loaded from either classpath or file by prefixing with classpath: or file:      * Wildcards is supported using a ANT pattern style paths, such as classpath:&#42;&#42;/&#42;camel&#42;.xml      *      * Multiple directories can be specified and separated by comma, such as:      * file:/myapp/mycamel/&#42;.xml,file:/myapp/myothercamel/&#42;.xml      */
DECL|field|xmlRests
specifier|private
name|String
name|xmlRests
init|=
literal|"classpath:camel-rest/*.xml"
decl_stmt|;
comment|// Getters& setters
comment|// -----------------
DECL|method|isMainRunController ()
specifier|public
name|boolean
name|isMainRunController
parameter_list|()
block|{
return|return
name|mainRunController
return|;
block|}
DECL|method|setMainRunController (boolean mainRunController)
specifier|public
name|void
name|setMainRunController
parameter_list|(
name|boolean
name|mainRunController
parameter_list|)
block|{
name|this
operator|.
name|mainRunController
operator|=
name|mainRunController
expr_stmt|;
block|}
DECL|method|isIncludeNonSingletons ()
specifier|public
name|boolean
name|isIncludeNonSingletons
parameter_list|()
block|{
return|return
name|includeNonSingletons
return|;
block|}
DECL|method|setIncludeNonSingletons (boolean includeNonSingletons)
specifier|public
name|void
name|setIncludeNonSingletons
parameter_list|(
name|boolean
name|includeNonSingletons
parameter_list|)
block|{
name|this
operator|.
name|includeNonSingletons
operator|=
name|includeNonSingletons
expr_stmt|;
block|}
DECL|method|isWarnOnEarlyShutdown ()
specifier|public
name|boolean
name|isWarnOnEarlyShutdown
parameter_list|()
block|{
return|return
name|warnOnEarlyShutdown
return|;
block|}
DECL|method|setWarnOnEarlyShutdown (boolean warnOnEarlyShutdown)
specifier|public
name|void
name|setWarnOnEarlyShutdown
parameter_list|(
name|boolean
name|warnOnEarlyShutdown
parameter_list|)
block|{
name|this
operator|.
name|warnOnEarlyShutdown
operator|=
name|warnOnEarlyShutdown
expr_stmt|;
block|}
block|}
end_class

end_unit

