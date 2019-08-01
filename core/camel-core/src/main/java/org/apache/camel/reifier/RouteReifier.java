begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|ExtendedCamelContext
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
name|FailedToCreateRouteException
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
name|NoSuchEndpointException
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
name|Route
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
name|RuntimeCamelException
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
name|AdviceWithRouteBuilder
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
name|AdviceWithTask
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
name|EndpointConsumerBuilder
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
name|model
operator|.
name|Model
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
name|ModelHelper
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
name|model
operator|.
name|ProcessorDefinitionHelper
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
name|PropertyDefinition
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
name|RouteDefinition
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
name|RoutesDefinition
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
name|processor
operator|.
name|ContractAdvice
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
name|reifier
operator|.
name|rest
operator|.
name|RestBindingReifier
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
name|Contract
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
name|LifecycleStrategy
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
name|RouteContext
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
name|RoutePolicy
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
name|RoutePolicyFactory
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
name|support
operator|.
name|CamelContextHelper
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
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|RouteReifier
specifier|public
class|class
name|RouteReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|RouteDefinition
argument_list|>
block|{
DECL|method|RouteReifier (ProcessorDefinition<?> definition)
specifier|public
name|RouteReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|RouteDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
comment|/**      * Advices this route with the route builder.      *<p/>      *<b>Important:</b> It is recommended to only advice a given route once (you can of course advice multiple routes).      * If you do it multiple times, then it may not work as expected, especially when any kind of error handling is involved.      * The Camel team plan for Camel 3.0 to support this as internal refactorings in the routing engine is needed to support this properly.      *<p/>      * You can use a regular {@link RouteBuilder} but the specialized {@link AdviceWithRouteBuilder}      * has additional features when using the<a href="http://camel.apache.org/advicewith.html">advice with</a> feature.      * We therefore suggest you to use the {@link AdviceWithRouteBuilder}.      *<p/>      * The advice process will add the interceptors, on exceptions, on completions etc. configured      * from the route builder to this route.      *<p/>      * This is mostly used for testing purpose to add interceptors and the likes to an existing route.      *<p/>      * Will stop and remove the old route from camel context and add and start this new advised route.      *      * @param definition   the model definition      * @param camelContext the camel context      * @param builder      the route builder      * @return a new route which is this route merged with the route builder      * @throws Exception can be thrown from the route builder      * @see AdviceWithRouteBuilder      */
DECL|method|adviceWith (RouteDefinition definition, CamelContext camelContext, RouteBuilder builder)
specifier|public
specifier|static
name|RouteDefinition
name|adviceWith
parameter_list|(
name|RouteDefinition
name|definition
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|RouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteReifier
argument_list|(
name|definition
argument_list|)
operator|.
name|adviceWith
argument_list|(
name|camelContext
argument_list|,
name|builder
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for RouteDefinition"
argument_list|)
throw|;
block|}
DECL|method|createRoute (CamelContext camelContext, RouteContext routeContext)
specifier|public
name|Route
name|createRoute
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|)
block|{
try|try
block|{
return|return
name|doCreateRoute
argument_list|(
name|camelContext
argument_list|,
name|routeContext
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FailedToCreateRouteException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// wrap in exception which provide more details about which route was failing
throw|throw
operator|new
name|FailedToCreateRouteException
argument_list|(
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|definition
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|resolveEndpoint (CamelContext camelContext, String uri)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
return|return
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|camelContext
argument_list|,
name|uri
argument_list|)
return|;
block|}
comment|/**      * Advices this route with the route builder.      *<p/>      *<b>Important:</b> It is recommended to only advice a given route once (you can of course advice multiple routes).      * If you do it multiple times, then it may not work as expected, especially when any kind of error handling is involved.      * The Camel team plan for Camel 3.0 to support this as internal refactorings in the routing engine is needed to support this properly.      *<p/>      * You can use a regular {@link RouteBuilder} but the specialized {@link org.apache.camel.builder.AdviceWithRouteBuilder}      * has additional features when using the<a href="http://camel.apache.org/advicewith.html">advice with</a> feature.      * We therefore suggest you to use the {@link org.apache.camel.builder.AdviceWithRouteBuilder}.      *<p/>      * The advice process will add the interceptors, on exceptions, on completions etc. configured      * from the route builder to this route.      *<p/>      * This is mostly used for testing purpose to add interceptors and the likes to an existing route.      *<p/>      * Will stop and remove the old route from camel context and add and start this new advised route.      *      * @param camelContext the camel context      * @param builder      the route builder      * @return a new route which is this route merged with the route builder      * @throws Exception can be thrown from the route builder      * @see AdviceWithRouteBuilder      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|adviceWith (CamelContext camelContext, RouteBuilder builder)
specifier|public
name|RouteDefinition
name|adviceWith
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|builder
argument_list|,
literal|"RouteBuilder"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"AdviceWith route before: {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// inject this route into the advice route builder so it can access this route
comment|// and offer features to manipulate the route directly
name|boolean
name|logRoutesAsXml
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|builder
operator|instanceof
name|AdviceWithRouteBuilder
condition|)
block|{
name|AdviceWithRouteBuilder
name|arb
init|=
operator|(
name|AdviceWithRouteBuilder
operator|)
name|builder
decl_stmt|;
name|arb
operator|.
name|setOriginalRoute
argument_list|(
name|definition
argument_list|)
expr_stmt|;
name|logRoutesAsXml
operator|=
name|arb
operator|.
name|isLogRouteAsXml
argument_list|()
expr_stmt|;
block|}
comment|// configure and prepare the routes from the builder
name|RoutesDefinition
name|routes
init|=
name|builder
operator|.
name|configureRoutes
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"AdviceWith routes: {}"
argument_list|,
name|routes
argument_list|)
expr_stmt|;
comment|// we can only advice with a route builder without any routes
if|if
condition|(
operator|!
name|builder
operator|.
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You can only advice from a RouteBuilder which has no existing routes."
operator|+
literal|" Remove all routes from the route builder."
argument_list|)
throw|;
block|}
comment|// we can not advice with error handlers (if you added a new error handler in the route builder)
comment|// we must check the error handler on builder is not the same as on camel context, as that would be the default
comment|// context scoped error handler, in case no error handlers was configured
if|if
condition|(
name|builder
operator|.
name|getRouteCollection
argument_list|()
operator|.
name|getErrorHandlerFactory
argument_list|()
operator|!=
literal|null
operator|&&
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getErrorHandlerFactory
argument_list|()
operator|!=
name|builder
operator|.
name|getRouteCollection
argument_list|()
operator|.
name|getErrorHandlerFactory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You can not advice with error handlers. Remove the error handlers from the route builder."
argument_list|)
throw|;
block|}
name|String
name|beforeAsXml
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|logRoutesAsXml
operator|&&
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|beforeAsXml
operator|=
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|camelContext
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
comment|// stop and remove this existing route
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|removeRouteDefinition
argument_list|(
name|definition
argument_list|)
expr_stmt|;
comment|// any advice with tasks we should execute first?
if|if
condition|(
name|builder
operator|instanceof
name|AdviceWithRouteBuilder
condition|)
block|{
name|List
argument_list|<
name|AdviceWithTask
argument_list|>
name|tasks
init|=
operator|(
operator|(
name|AdviceWithRouteBuilder
operator|)
name|builder
operator|)
operator|.
name|getAdviceWithTasks
argument_list|()
decl_stmt|;
for|for
control|(
name|AdviceWithTask
name|task
range|:
name|tasks
control|)
block|{
name|task
operator|.
name|task
argument_list|()
expr_stmt|;
block|}
block|}
comment|// now merge which also ensures that interceptors and the likes get mixed in correctly as well
name|RouteDefinition
name|merged
init|=
name|routes
operator|.
name|route
argument_list|(
name|definition
argument_list|)
decl_stmt|;
comment|// add the new merged route
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|merged
argument_list|)
expr_stmt|;
comment|// log the merged route at info level to make it easier to end users to spot any mistakes they may have made
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"AdviceWith route after: {}"
argument_list|,
name|merged
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|logRoutesAsXml
operator|&&
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|String
name|afterAsXml
init|=
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|camelContext
argument_list|,
name|merged
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Adviced route before/after as XML:\n{}\n{}"
argument_list|,
name|beforeAsXml
argument_list|,
name|afterAsXml
argument_list|)
expr_stmt|;
block|}
comment|// If the camel context is started then we start the route
if|if
condition|(
name|camelContext
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|addRouteDefinition
argument_list|(
name|merged
argument_list|)
expr_stmt|;
block|}
return|return
name|merged
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|doCreateRoute (CamelContext camelContext, RouteContext routeContext)
specifier|protected
name|Route
name|doCreateRoute
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// configure error handler
name|routeContext
operator|.
name|setErrorHandlerFactory
argument_list|(
name|definition
operator|.
name|getErrorHandlerFactory
argument_list|()
argument_list|)
expr_stmt|;
comment|// configure tracing
if|if
condition|(
name|definition
operator|.
name|getTrace
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Boolean
name|isTrace
init|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getTrace
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isTrace
operator|!=
literal|null
condition|)
block|{
name|routeContext
operator|.
name|setTracing
argument_list|(
name|isTrace
argument_list|)
expr_stmt|;
if|if
condition|(
name|isTrace
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Tracing is enabled on route: {}"
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// tracing is added in the DefaultChannel so we can enable it on the fly
block|}
block|}
block|}
comment|// configure message history
if|if
condition|(
name|definition
operator|.
name|getMessageHistory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Boolean
name|isMessageHistory
init|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getMessageHistory
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isMessageHistory
operator|!=
literal|null
condition|)
block|{
name|routeContext
operator|.
name|setMessageHistory
argument_list|(
name|isMessageHistory
argument_list|)
expr_stmt|;
if|if
condition|(
name|isMessageHistory
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Message history is enabled on route: {}"
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// configure Log EIP mask
if|if
condition|(
name|definition
operator|.
name|getLogMask
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Boolean
name|isLogMask
init|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getLogMask
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isLogMask
operator|!=
literal|null
condition|)
block|{
name|routeContext
operator|.
name|setLogMask
argument_list|(
name|isLogMask
argument_list|)
expr_stmt|;
if|if
condition|(
name|isLogMask
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Security mask for Logging is enabled on route: {}"
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// configure stream caching
if|if
condition|(
name|definition
operator|.
name|getStreamCache
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Boolean
name|isStreamCache
init|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getStreamCache
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isStreamCache
operator|!=
literal|null
condition|)
block|{
name|routeContext
operator|.
name|setStreamCaching
argument_list|(
name|isStreamCache
argument_list|)
expr_stmt|;
if|if
condition|(
name|isStreamCache
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"StreamCaching is enabled on route: {}"
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// configure delayer
if|if
condition|(
name|definition
operator|.
name|getDelayer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Long
name|delayer
init|=
name|CamelContextHelper
operator|.
name|parseLong
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getDelayer
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|delayer
operator|!=
literal|null
condition|)
block|{
name|routeContext
operator|.
name|setDelayer
argument_list|(
name|delayer
argument_list|)
expr_stmt|;
if|if
condition|(
name|delayer
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Delayer is enabled with: {} ms. on route: {}"
argument_list|,
name|delayer
argument_list|,
name|definition
operator|.
name|getId
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
literal|"Delayer is disabled on route: {}"
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// configure route policy
if|if
condition|(
name|definition
operator|.
name|getRoutePolicies
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|definition
operator|.
name|getRoutePolicies
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|RoutePolicy
name|policy
range|:
name|definition
operator|.
name|getRoutePolicies
argument_list|()
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"RoutePolicy is enabled: {} on route: {}"
argument_list|,
name|policy
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|getRoutePolicyList
argument_list|()
operator|.
name|add
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|definition
operator|.
name|getRoutePolicyRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|StringTokenizer
name|policyTokens
init|=
operator|new
name|StringTokenizer
argument_list|(
name|definition
operator|.
name|getRoutePolicyRef
argument_list|()
argument_list|,
literal|","
argument_list|)
decl_stmt|;
while|while
condition|(
name|policyTokens
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|ref
init|=
name|policyTokens
operator|.
name|nextToken
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|RoutePolicy
name|policy
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|ref
argument_list|,
name|RoutePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"RoutePolicy is enabled: {} on route: {}"
argument_list|,
name|policy
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|getRoutePolicyList
argument_list|()
operator|.
name|add
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|camelContext
operator|.
name|getRoutePolicyFactories
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RoutePolicyFactory
name|factory
range|:
name|camelContext
operator|.
name|getRoutePolicyFactories
argument_list|()
control|)
block|{
name|RoutePolicy
name|policy
init|=
name|factory
operator|.
name|createRoutePolicy
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|definition
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"RoutePolicy is enabled: {} on route: {}"
argument_list|,
name|policy
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|getRoutePolicyList
argument_list|()
operator|.
name|add
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// configure auto startup
name|Boolean
name|isAutoStartup
init|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getAutoStartup
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isAutoStartup
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using AutoStartup {} on route: {}"
argument_list|,
name|isAutoStartup
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|setAutoStartup
argument_list|(
name|isAutoStartup
argument_list|)
expr_stmt|;
block|}
comment|// configure startup order
if|if
condition|(
name|definition
operator|.
name|getStartupOrder
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|routeContext
operator|.
name|setStartupOrder
argument_list|(
name|definition
operator|.
name|getStartupOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// configure shutdown
if|if
condition|(
name|definition
operator|.
name|getShutdownRoute
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using ShutdownRoute {} on route: {}"
argument_list|,
name|definition
operator|.
name|getShutdownRoute
argument_list|()
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|setShutdownRoute
argument_list|(
name|definition
operator|.
name|getShutdownRoute
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getShutdownRunningTask
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using ShutdownRunningTask {} on route: {}"
argument_list|,
name|definition
operator|.
name|getShutdownRunningTask
argument_list|()
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|setShutdownRunningTask
argument_list|(
name|definition
operator|.
name|getShutdownRunningTask
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// should inherit the intercept strategies we have defined
name|routeContext
operator|.
name|setInterceptStrategies
argument_list|(
name|definition
operator|.
name|getInterceptStrategies
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve endpoint
name|Endpoint
name|endpoint
init|=
name|definition
operator|.
name|getInput
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
name|EndpointConsumerBuilder
name|def
init|=
name|definition
operator|.
name|getInput
argument_list|()
operator|.
name|getEndpointConsumerBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|def
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|=
name|def
operator|.
name|resolve
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
name|routeContext
operator|.
name|resolveEndpoint
argument_list|(
name|definition
operator|.
name|getInput
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|routeContext
operator|.
name|setEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
comment|// notify route context created
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onRouteContextCreate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
comment|// validate route has output processors
if|if
condition|(
operator|!
name|ProcessorDefinitionHelper
operator|.
name|hasOutputs
argument_list|(
name|definition
operator|.
name|getOutputs
argument_list|()
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|String
name|at
init|=
name|definition
operator|.
name|getInput
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Exception
name|cause
init|=
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Route "
operator|+
name|definition
operator|.
name|getId
argument_list|()
operator|+
literal|" has no output processors."
operator|+
literal|" You need to add outputs to the route such as to(\"log:foo\")."
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|FailedToCreateRouteException
argument_list|(
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|definition
operator|.
name|toString
argument_list|()
argument_list|,
name|at
argument_list|,
name|cause
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|definition
operator|.
name|getOutputs
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|list
control|)
block|{
try|try
block|{
name|ProcessorReifier
operator|.
name|reifier
argument_list|(
name|output
argument_list|)
operator|.
name|addRoutes
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateRouteException
argument_list|(
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|definition
operator|.
name|toString
argument_list|()
argument_list|,
name|output
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|definition
operator|.
name|getRestBindingDefinition
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|routeContext
operator|.
name|addAdvice
argument_list|(
operator|new
name|RestBindingReifier
argument_list|(
name|definition
operator|.
name|getRestBindingDefinition
argument_list|()
argument_list|)
operator|.
name|createRestBindingAdvice
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|// wrap in contract
if|if
condition|(
name|definition
operator|.
name|getInputType
argument_list|()
operator|!=
literal|null
operator|||
name|definition
operator|.
name|getOutputType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Contract
name|contract
init|=
operator|new
name|Contract
argument_list|()
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getInputType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|contract
operator|.
name|setInputType
argument_list|(
name|definition
operator|.
name|getInputType
argument_list|()
operator|.
name|getUrn
argument_list|()
argument_list|)
expr_stmt|;
name|contract
operator|.
name|setValidateInput
argument_list|(
name|definition
operator|.
name|getInputType
argument_list|()
operator|.
name|isValidate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getOutputType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|contract
operator|.
name|setOutputType
argument_list|(
name|definition
operator|.
name|getOutputType
argument_list|()
operator|.
name|getUrn
argument_list|()
argument_list|)
expr_stmt|;
name|contract
operator|.
name|setValidateOutput
argument_list|(
name|definition
operator|.
name|getOutputType
argument_list|()
operator|.
name|isValidate
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|routeContext
operator|.
name|addAdvice
argument_list|(
operator|new
name|ContractAdvice
argument_list|(
name|contract
argument_list|)
argument_list|)
expr_stmt|;
comment|// make sure to enable data type as its in use when using input/output types on routes
name|camelContext
operator|.
name|setUseDataType
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// Set route properties
name|routeContext
operator|.
name|addProperty
argument_list|(
name|Route
operator|.
name|ID_PROPERTY
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|addProperty
argument_list|(
name|Route
operator|.
name|CUSTOM_ID_PROPERTY
argument_list|,
name|definition
operator|.
name|hasCustomIdAssigned
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|addProperty
argument_list|(
name|Route
operator|.
name|PARENT_PROPERTY
argument_list|,
name|Integer
operator|.
name|toHexString
argument_list|(
name|definition
operator|.
name|hashCode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|addProperty
argument_list|(
name|Route
operator|.
name|DESCRIPTION_PROPERTY
argument_list|,
name|definition
operator|.
name|getDescriptionText
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getGroup
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|routeContext
operator|.
name|addProperty
argument_list|(
name|Route
operator|.
name|GROUP_PROPERTY
argument_list|,
name|definition
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|rest
init|=
name|Boolean
operator|.
name|toString
argument_list|(
name|definition
operator|.
name|isRest
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|isRest
argument_list|()
argument_list|)
decl_stmt|;
name|routeContext
operator|.
name|addProperty
argument_list|(
name|Route
operator|.
name|REST_PROPERTY
argument_list|,
name|rest
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PropertyDefinition
argument_list|>
name|properties
init|=
name|definition
operator|.
name|getRouteProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
index|[]
name|reservedProperties
init|=
operator|new
name|String
index|[]
block|{
name|Route
operator|.
name|ID_PROPERTY
block|,
name|Route
operator|.
name|CUSTOM_ID_PROPERTY
block|,
name|Route
operator|.
name|PARENT_PROPERTY
block|,
name|Route
operator|.
name|DESCRIPTION_PROPERTY
block|,
name|Route
operator|.
name|GROUP_PROPERTY
block|,
name|Route
operator|.
name|REST_PROPERTY
block|}
decl_stmt|;
for|for
control|(
name|PropertyDefinition
name|prop
range|:
name|properties
control|)
block|{
try|try
block|{
specifier|final
name|String
name|key
init|=
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|camelContext
argument_list|,
name|prop
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|camelContext
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|property
range|:
name|reservedProperties
control|)
block|{
if|if
condition|(
name|property
operator|.
name|equalsIgnoreCase
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot set route property "
operator|+
name|property
operator|+
literal|" as it is a reserved property"
argument_list|)
throw|;
block|}
block|}
name|routeContext
operator|.
name|addProperty
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|routeContext
operator|.
name|commit
argument_list|()
return|;
block|}
block|}
end_class

end_unit

