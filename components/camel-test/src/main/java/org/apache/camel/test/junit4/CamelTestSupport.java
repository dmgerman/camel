begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.junit4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
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
name|CamelContext
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
name|ConsumerTemplate
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
name|Endpoint
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
name|Exchange
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
name|Expression
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
name|Message
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
name|Predicate
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
name|Processor
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
name|ProducerTemplate
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
name|Service
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|impl
operator|.
name|BreakpointSupport
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|DefaultDebugger
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
name|impl
operator|.
name|InterceptSendToMockEndpointStrategy
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
name|impl
operator|.
name|JndiRegistry
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
name|management
operator|.
name|JmxSystemPropertyKeys
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
name|model
operator|.
name|ProcessorDefinition
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
name|spi
operator|.
name|Language
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
name|spring
operator|.
name|CamelBeanPostProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_comment
comment|/**  * A useful base class which creates a {@link org.apache.camel.CamelContext} with some routes  * along with a {@link org.apache.camel.ProducerTemplate} for use in the test case  *  * @version   */
end_comment

begin_class
DECL|class|CamelTestSupport
specifier|public
specifier|abstract
class|class
name|CamelTestSupport
extends|extends
name|TestSupport
block|{
DECL|field|context
specifier|protected
specifier|volatile
name|CamelContext
name|context
decl_stmt|;
DECL|field|template
specifier|protected
specifier|volatile
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|consumer
specifier|protected
specifier|volatile
name|ConsumerTemplate
name|consumer
decl_stmt|;
DECL|field|useRouteBuilder
specifier|private
name|boolean
name|useRouteBuilder
init|=
literal|true
decl_stmt|;
DECL|field|camelContextService
specifier|private
name|Service
name|camelContextService
decl_stmt|;
DECL|field|breakpoint
specifier|private
specifier|final
name|DebugBreakpoint
name|breakpoint
init|=
operator|new
name|DebugBreakpoint
argument_list|()
decl_stmt|;
comment|/**      * Use the RouteBuilder or not      * @return       *  If the return value is true, the camel context will be started in the setup method.      *  If the return value is false, the camel context will not be started in the setup method.      */
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
name|useRouteBuilder
return|;
block|}
comment|/**      * Override to enable auto mocking endpoints based on the pattern.      *<p/>      * Return<tt>*</tt> to mock all endpoints.      *      * @see org.apache.camel.util.EndpointHelper#matchEndpoint(String, String)      */
DECL|method|isMockEndpoints ()
specifier|public
name|String
name|isMockEndpoints
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|setUseRouteBuilder (boolean useRouteBuilder)
specifier|public
name|void
name|setUseRouteBuilder
parameter_list|(
name|boolean
name|useRouteBuilder
parameter_list|)
block|{
name|this
operator|.
name|useRouteBuilder
operator|=
name|useRouteBuilder
expr_stmt|;
block|}
DECL|method|getCamelContextService ()
specifier|public
name|Service
name|getCamelContextService
parameter_list|()
block|{
return|return
name|camelContextService
return|;
block|}
comment|/**      * Allows a service to be registered a separate lifecycle service to start      * and stop the context; such as for Spring when the ApplicationContext is      * started and stopped, rather than directly stopping the CamelContext      */
DECL|method|setCamelContextService (Service camelContextService)
specifier|public
name|void
name|setCamelContextService
parameter_list|(
name|Service
name|camelContextService
parameter_list|)
block|{
name|this
operator|.
name|camelContextService
operator|=
name|camelContextService
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"********************************************************************************"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Testing: "
operator|+
name|getTestMethodName
argument_list|()
operator|+
literal|"("
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"********************************************************************************"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"setUp test"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|useJmx
argument_list|()
condition|)
block|{
name|disableJMX
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|enableJMX
argument_list|()
expr_stmt|;
block|}
name|context
operator|=
name|createCamelContext
argument_list|()
expr_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// reduce default shutdown timeout to avoid waiting for 300 seconds
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
name|getShutdownTimeout
argument_list|()
argument_list|)
expr_stmt|;
comment|// set debugger
name|context
operator|.
name|setDebugger
argument_list|(
operator|new
name|DefaultDebugger
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|getDebugger
argument_list|()
operator|.
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|)
expr_stmt|;
comment|// note: when stopping CamelContext it will automatic remove the breakpoint
name|template
operator|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|template
operator|.
name|start
argument_list|()
expr_stmt|;
name|consumer
operator|=
name|context
operator|.
name|createConsumerTemplate
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// enable auto mocking if enabled
name|String
name|pattern
init|=
name|isMockEndpoints
argument_list|()
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|addRegisterEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|postProcessTest
argument_list|()
expr_stmt|;
if|if
condition|(
name|isUseRouteBuilder
argument_list|()
condition|)
block|{
name|RouteBuilder
index|[]
name|builders
init|=
name|createRouteBuilders
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteBuilder
name|builder
range|:
name|builders
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using created route builder: "
operator|+
name|builder
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
name|startCamelContext
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Routing Rules are: "
operator|+
name|context
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using route builder from the created context: "
operator|+
name|context
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Routing Rules are: "
operator|+
name|context
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Testing done: "
operator|+
name|this
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"tearDown test"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|stopCamelContext
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the timeout to use when shutting down (unit in seconds).      *<p/>      * Will default use 10 seconds.      *      * @return the timeout to use      */
DECL|method|getShutdownTimeout ()
specifier|protected
name|int
name|getShutdownTimeout
parameter_list|()
block|{
return|return
literal|10
return|;
block|}
comment|/**      * Whether or not JMX should be used during testing.      *      * @return<tt>false</tt> by default.      */
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Whether or not type converters should be lazy loaded (notice core converters is always loaded)      *<p/>      * We enabled lazy by default as it would speedup unit testing.      *      * @return<tt>true</tt> by default.      */
DECL|method|isLazyLoadingTypeConverter ()
specifier|protected
name|boolean
name|isLazyLoadingTypeConverter
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Lets post process this test instance to process any Camel annotations.      * Note that using Spring Test or Guice is a more powerful approach.      */
DECL|method|postProcessTest ()
specifier|protected
name|void
name|postProcessTest
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelBeanPostProcessor
name|processor
init|=
operator|new
name|CamelBeanPostProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|processor
operator|.
name|postProcessBeforeInitialization
argument_list|(
name|this
argument_list|,
literal|"this"
argument_list|)
expr_stmt|;
block|}
DECL|method|stopCamelContext ()
specifier|protected
name|void
name|stopCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContextService
operator|!=
literal|null
condition|)
block|{
name|camelContextService
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|startCamelContext ()
specifier|protected
name|void
name|startCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContextService
operator|!=
literal|null
condition|)
block|{
name|camelContextService
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
name|DefaultCamelContext
name|defaultCamelContext
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
if|if
condition|(
operator|!
name|defaultCamelContext
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|defaultCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|setLazyLoadTypeConverters
argument_list|(
name|isLazyLoadingTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|JndiRegistry
argument_list|(
name|createJndiContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|// jndi.properties is optional
name|InputStream
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"jndi.properties"
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using jndi.properties from classpath root"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"java.naming.factory.initial"
argument_list|,
literal|"org.apache.camel.util.jndi.CamelInitialContextFactory"
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|InitialContext
argument_list|(
operator|new
name|Hashtable
argument_list|(
name|properties
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Factory method which derived classes can use to create a {@link RouteBuilder}      * to define the routes for testing      */
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// no routes added by default
block|}
block|}
return|;
block|}
comment|/**      * Factory method which derived classes can use to create an array of      * {@link org.apache.camel.builder.RouteBuilder}s to define the routes for testing      *      * @see #createRouteBuilder()      */
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{
name|createRouteBuilder
argument_list|()
block|}
return|;
block|}
comment|/**      * Resolves a mandatory endpoint for the given URI or an exception is thrown      *      * @param uri the Camel<a href="">URI</a> to use to create or resolve an endpoint      * @return the endpoint      */
DECL|method|resolveMandatoryEndpoint (String uri)
specifier|protected
name|Endpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|resolveMandatoryEndpoint
argument_list|(
name|context
argument_list|,
name|uri
argument_list|)
return|;
block|}
comment|/**      * Resolves a mandatory endpoint for the given URI and expected type or an exception is thrown      *      * @param uri the Camel<a href="">URI</a> to use to create or resolve an endpoint      * @return the endpoint      */
DECL|method|resolveMandatoryEndpoint (String uri, Class<T> endpointType)
specifier|protected
parameter_list|<
name|T
extends|extends
name|Endpoint
parameter_list|>
name|T
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|endpointType
parameter_list|)
block|{
return|return
name|resolveMandatoryEndpoint
argument_list|(
name|context
argument_list|,
name|uri
argument_list|,
name|endpointType
argument_list|)
return|;
block|}
comment|/**      * Resolves the mandatory Mock endpoint using a URI of the form<code>mock:someName</code>      *      * @param uri the URI which typically starts with "mock:" and has some name      * @return the mandatory mock endpoint or an exception is thrown if it could not be resolved      */
DECL|method|getMockEndpoint (String uri)
specifier|protected
name|MockEndpoint
name|getMockEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|resolveMandatoryEndpoint
argument_list|(
name|uri
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Sends a message to the given endpoint URI with the body value      *      * @param endpointUri the URI of the endpoint to send to      * @param body        the body for the message      */
DECL|method|sendBody (String endpointUri, final Object body)
specifier|protected
name|void
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends a message to the given endpoint URI with the body value and specified headers      *      * @param endpointUri the URI of the endpoint to send to      * @param body        the body for the message      * @param headers     any headers to set on the message      */
DECL|method|sendBody (String endpointUri, final Object body, final Map<String, Object> headers)
specifier|protected
name|void
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|in
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends messages to the given endpoint for each of the specified bodies      *      * @param endpointUri the endpoint URI to send to      * @param bodies      the bodies to send, one per message      */
DECL|method|sendBodies (String endpointUri, Object... bodies)
specifier|protected
name|void
name|sendBodies
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
modifier|...
name|bodies
parameter_list|)
block|{
for|for
control|(
name|Object
name|body
range|:
name|bodies
control|)
block|{
name|sendBody
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates an exchange with the given body      */
DECL|method|createExchangeWithBody (Object body)
specifier|protected
name|Exchange
name|createExchangeWithBody
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
return|return
name|createExchangeWithBody
argument_list|(
name|context
argument_list|,
name|body
argument_list|)
return|;
block|}
comment|/**      * Asserts that the given language name and expression evaluates to the      * given value on a specific exchange      */
DECL|method|assertExpression (Exchange exchange, String languageName, String expressionText, Object expectedValue)
specifier|protected
name|void
name|assertExpression
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|languageName
parameter_list|,
name|String
name|expressionText
parameter_list|,
name|Object
name|expectedValue
parameter_list|)
block|{
name|Language
name|language
init|=
name|assertResolveLanguage
argument_list|(
name|languageName
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|language
operator|.
name|createExpression
argument_list|(
name|expressionText
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No Expression could be created for text: "
operator|+
name|expressionText
operator|+
literal|" language: "
operator|+
name|language
argument_list|,
name|expression
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
name|expectedValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the given language name and predicate expression evaluates      * to the expected value on the message exchange      */
DECL|method|assertPredicate (String languageName, String expressionText, Exchange exchange, boolean expected)
specifier|protected
name|void
name|assertPredicate
parameter_list|(
name|String
name|languageName
parameter_list|,
name|String
name|expressionText
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|expected
parameter_list|)
block|{
name|Language
name|language
init|=
name|assertResolveLanguage
argument_list|(
name|languageName
argument_list|)
decl_stmt|;
name|Predicate
name|predicate
init|=
name|language
operator|.
name|createPredicate
argument_list|(
name|expressionText
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No Predicate could be created for text: "
operator|+
name|expressionText
operator|+
literal|" language: "
operator|+
name|language
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the language name can be resolved      */
DECL|method|assertResolveLanguage (String languageName)
specifier|protected
name|Language
name|assertResolveLanguage
parameter_list|(
name|String
name|languageName
parameter_list|)
block|{
name|Language
name|language
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
name|languageName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No language found for name: "
operator|+
name|languageName
argument_list|,
name|language
argument_list|)
expr_stmt|;
return|return
name|language
return|;
block|}
comment|/**      * Asserts that all the expectations of the Mock endpoints are valid      */
DECL|method|assertMockEndpointsSatisfied ()
specifier|protected
name|void
name|assertMockEndpointsSatisfied
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that all the expectations of the Mock endpoints are valid      */
DECL|method|assertMockEndpointsSatisfied (long timeout, TimeUnit unit)
specifier|protected
name|void
name|assertMockEndpointsSatisfied
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|context
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reset all Mock endpoints.      */
DECL|method|resetMocks ()
specifier|protected
name|void
name|resetMocks
parameter_list|()
block|{
name|MockEndpoint
operator|.
name|resetMocks
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|assertValidContext (CamelContext context)
specifier|protected
name|void
name|assertValidContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"No context found!"
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|getMandatoryEndpoint (String uri, Class<T> type)
specifier|protected
parameter_list|<
name|T
extends|extends
name|Endpoint
parameter_list|>
name|T
name|getMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|T
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No endpoint found for uri: "
operator|+
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getMandatoryEndpoint (String uri)
specifier|protected
name|Endpoint
name|getMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No endpoint found for uri: "
operator|+
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Disables the JMX agent. Must be called before the {@link #setUp()} method.      */
DECL|method|disableJMX ()
specifier|protected
name|void
name|disableJMX
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Enables the JMX agent. Must be called before the {@link #setUp()} method.      */
DECL|method|enableJMX ()
specifier|protected
name|void
name|enableJMX
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Single step debugs and Camel invokes this method before entering the given processor      */
DECL|method|debugBefore (Exchange exchange, Processor processor, ProcessorDefinition definition, String id, String label)
specifier|protected
name|void
name|debugBefore
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|label
parameter_list|)
block|{     }
comment|/**      * Single step debugs and Camel invokes this method after processing the given processor      */
DECL|method|debugAfter (Exchange exchange, Processor processor, ProcessorDefinition definition, String id, String label, long timeTaken)
specifier|protected
name|void
name|debugAfter
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|label
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{     }
comment|/**      * To easily debug by overriding the<tt>debugBefore</tt> and<tt>debugAfter</tt> methods.      */
DECL|class|DebugBreakpoint
specifier|private
class|class
name|DebugBreakpoint
extends|extends
name|BreakpointSupport
block|{
annotation|@
name|Override
DECL|method|beforeProcess (Exchange exchange, Processor processor, ProcessorDefinition definition)
specifier|public
name|void
name|beforeProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|)
block|{
name|CamelTestSupport
operator|.
name|this
operator|.
name|debugBefore
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|definition
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterProcess (Exchange exchange, Processor processor, ProcessorDefinition definition, long timeTaken)
specifier|public
name|void
name|afterProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
name|CamelTestSupport
operator|.
name|this
operator|.
name|debugAfter
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|definition
operator|.
name|getLabel
argument_list|()
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

