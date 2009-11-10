begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

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
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|util
operator|.
name|jndi
operator|.
name|JndiTest
import|;
end_import

begin_comment
comment|/**  * A useful base class which creates a {@link CamelContext} with some routes  * along with a {@link ProducerTemplate} for use in the test case  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ContextTestSupport
specifier|public
specifier|abstract
class|class
name|ContextTestSupport
extends|extends
name|TestSupport
block|{
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|consumer
specifier|protected
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
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
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
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"isUseRouteBuilder() is false"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"tearDown test: "
operator|+
name|getName
argument_list|()
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
return|return
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
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
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|JndiTest
operator|.
name|createInitialContext
argument_list|()
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
comment|/**      * Factory method which derived classes can use to create an array of      * {@link RouteBuilder}s to define the routes for testing      *      * @see #createRouteBuilder()      */
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
name|in
operator|.
name|setHeader
argument_list|(
literal|"testCase"
argument_list|,
name|getName
argument_list|()
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
name|in
operator|.
name|setHeader
argument_list|(
literal|"testCase"
argument_list|,
name|getName
argument_list|()
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
block|}
end_class

end_unit

