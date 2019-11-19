begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|impl
operator|.
name|engine
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
name|model
operator|.
name|ModelCamelContext
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
name|reifier
operator|.
name|RouteReifier
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
name|EndpointHelper
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
name|PatternHelper
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
name|function
operator|.
name|ThrowingConsumer
import|;
end_import

begin_comment
comment|/**  * A {@link RouteBuilder} which has extended capabilities when using the  *<a href="http://camel.apache.org/advicewith.html">advice with</a> feature.  *<p/>  *<b>Important:</b> It is recommended to only advice a given route once (you  * can of course advice multiple routes). If you do it multiple times, then it  * may not work as expected, especially when any kind of error handling is  * involved.  */
end_comment

begin_class
DECL|class|AdviceWithRouteBuilder
specifier|public
specifier|abstract
class|class
name|AdviceWithRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|originalRoute
specifier|private
name|RouteDefinition
name|originalRoute
decl_stmt|;
DECL|field|adviceWithTasks
specifier|private
specifier|final
name|List
argument_list|<
name|AdviceWithTask
argument_list|>
name|adviceWithTasks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|logRouteAsXml
specifier|private
name|boolean
name|logRouteAsXml
init|=
literal|true
decl_stmt|;
DECL|method|AdviceWithRouteBuilder ()
specifier|public
name|AdviceWithRouteBuilder
parameter_list|()
block|{     }
DECL|method|AdviceWithRouteBuilder (CamelContext context)
specifier|public
name|AdviceWithRouteBuilder
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Advices this route with the route builder using a lambda expression. It      * can be used as following:      *       *<pre>      * AdviceWithRouteBuilder.adviceWith(context, "myRoute", a ->      *     a.weaveAddLast().to("mock:result");      *</pre>      *<p/>      *<b>Important:</b> It is recommended to only advice a given route once      * (you can of course advice multiple routes). If you do it multiple times,      * then it may not work as expected, especially when any kind of error      * handling is involved. The Camel team plan for Camel 3.0 to support this      * as internal refactorings in the routing engine is needed to support this      * properly.      *<p/>      * The advice process will add the interceptors, on exceptions, on      * completions etc. configured from the route builder to this route.      *<p/>      * This is mostly used for testing purpose to add interceptors and the likes      * to an existing route.      *<p/>      * Will stop and remove the old route from camel context and add and start      * this new advised route.      *      * @param camelContext the camel context      * @param routeId either the route id as a string value, or<tt>null</tt> to      *            chose the 1st route, or you can specify a number for the n'th      *            route, or provide the route definition instance directly as well.      * @param builder the advice with route builder      * @return a new route which is this route merged with the route builder      * @throws Exception can be thrown from the route builder      */
DECL|method|adviceWith (CamelContext camelContext, Object routeId, ThrowingConsumer<AdviceWithRouteBuilder, Exception> builder)
specifier|public
specifier|static
name|RouteDefinition
name|adviceWith
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|routeId
parameter_list|,
name|ThrowingConsumer
argument_list|<
name|AdviceWithRouteBuilder
argument_list|,
name|Exception
argument_list|>
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
name|ModelCamelContext
name|mcc
init|=
name|camelContext
operator|.
name|adapt
argument_list|(
name|ModelCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mcc
operator|.
name|getRouteDefinitions
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
literal|"Cannot advice route as there are no routes"
argument_list|)
throw|;
block|}
name|RouteDefinition
name|rd
decl_stmt|;
if|if
condition|(
name|routeId
operator|instanceof
name|RouteDefinition
condition|)
block|{
name|rd
operator|=
operator|(
name|RouteDefinition
operator|)
name|routeId
expr_stmt|;
block|}
else|else
block|{
name|String
name|id
init|=
name|mcc
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|rd
operator|=
name|mcc
operator|.
name|getRouteDefinition
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|rd
operator|==
literal|null
condition|)
block|{
comment|// okay it may be a number
name|Integer
name|num
init|=
name|mcc
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|num
operator|!=
literal|null
condition|)
block|{
name|rd
operator|=
name|mcc
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
name|num
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|rd
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot advice route as route with id: "
operator|+
name|routeId
operator|+
literal|" does not exists"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// grab first route
name|rd
operator|=
name|mcc
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|RouteReifier
operator|.
name|adviceWith
argument_list|(
name|rd
argument_list|,
name|camelContext
argument_list|,
operator|new
name|AdviceWithRouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|builder
operator|instanceof
name|AdviceWithRouteBuilder
condition|)
block|{
name|setLogRouteAsXml
argument_list|(
operator|(
operator|(
name|AdviceWithRouteBuilder
operator|)
name|builder
operator|)
operator|.
name|isLogRouteAsXml
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|accept
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Sets the original route to be adviced.      *      * @param originalRoute the original route.      */
DECL|method|setOriginalRoute (RouteDefinition originalRoute)
specifier|public
name|void
name|setOriginalRoute
parameter_list|(
name|RouteDefinition
name|originalRoute
parameter_list|)
block|{
name|this
operator|.
name|originalRoute
operator|=
name|originalRoute
expr_stmt|;
block|}
comment|/**      * Gets the original route to be adviced.      *      * @return the original route.      */
DECL|method|getOriginalRoute ()
specifier|public
name|RouteDefinition
name|getOriginalRoute
parameter_list|()
block|{
return|return
name|originalRoute
return|;
block|}
comment|/**      * Whether to log the adviced routes before/after as XML. This is usable to      * know how the route was adviced and changed. However marshalling the route      * model to XML costs CPU resources and you can then turn this off by not      * logging. This is default enabled.      */
DECL|method|isLogRouteAsXml ()
specifier|public
name|boolean
name|isLogRouteAsXml
parameter_list|()
block|{
return|return
name|logRouteAsXml
return|;
block|}
comment|/**      * Sets whether to log the adviced routes before/after as XML. This is      * usable to know how the route was adviced and changed. However marshalling      * the route model to XML costs CPU resources and you can then turn this off      * by not logging. This is default enabled.      */
DECL|method|setLogRouteAsXml (boolean logRouteAsXml)
specifier|public
name|void
name|setLogRouteAsXml
parameter_list|(
name|boolean
name|logRouteAsXml
parameter_list|)
block|{
name|this
operator|.
name|logRouteAsXml
operator|=
name|logRouteAsXml
expr_stmt|;
block|}
comment|/**      * Gets a list of additional tasks to execute after the {@link #configure()}      * method has been executed during the advice process.      *      * @return a list of additional {@link AdviceWithTask} tasks to be executed      *         during the advice process.      */
DECL|method|getAdviceWithTasks ()
specifier|public
name|List
argument_list|<
name|AdviceWithTask
argument_list|>
name|getAdviceWithTasks
parameter_list|()
block|{
return|return
name|adviceWithTasks
return|;
block|}
comment|/**      * Mock all endpoints.      *      * @throws Exception can be thrown if error occurred      */
DECL|method|mockEndpoints ()
specifier|public
name|void
name|mockEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|getContext
argument_list|()
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|registerEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Mock all endpoints matching the given pattern.      *      * @param pattern the pattern(s).      * @throws Exception can be thrown if error occurred      * @see EndpointHelper#matchEndpoint(org.apache.camel.CamelContext, String,      *      String)      */
DECL|method|mockEndpoints (String... pattern)
specifier|public
name|void
name|mockEndpoints
parameter_list|(
name|String
modifier|...
name|pattern
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|s
range|:
name|pattern
control|)
block|{
name|getContext
argument_list|()
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|registerEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Mock all endpoints matching the given pattern, and<b>skips</b> sending      * to the original endpoint (detour messages).      *      * @param pattern the pattern(s).      * @throws Exception can be thrown if error occurred      * @see EndpointHelper#matchEndpoint(org.apache.camel.CamelContext, String,      *      String)      */
DECL|method|mockEndpointsAndSkip (String... pattern)
specifier|public
name|void
name|mockEndpointsAndSkip
parameter_list|(
name|String
modifier|...
name|pattern
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|s
range|:
name|pattern
control|)
block|{
name|getContext
argument_list|()
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|registerEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
name|s
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Replaces the route from endpoint with a new uri      *      * @param uri uri of the new endpoint      */
DECL|method|replaceFromWith (String uri)
specifier|public
name|void
name|replaceFromWith
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|replaceFromWith
argument_list|(
name|originalRoute
argument_list|,
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Replaces the route from endpoint with a new endpoint      *      * @param endpoint the new endpoint      */
DECL|method|replaceFromWith (Endpoint endpoint)
specifier|public
name|void
name|replaceFromWith
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|replaceFrom
argument_list|(
name|originalRoute
argument_list|,
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Weaves by matching id of the nodes in the route (incl onException etc).      *<p/>      * Uses the {@link PatternHelper#matchPattern(String, String)} matching      * algorithm.      *      * @param pattern the pattern      * @return the builder      * @see PatternHelper#matchPattern(String, String)      */
DECL|method|weaveById (String pattern)
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|weaveById
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<>
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Weaves by matching the to string representation of the nodes in the route      * (incl onException etc).      *<p/>      * Uses the {@link PatternHelper#matchPattern(String, String)} matching      * algorithm.      *      * @param pattern the pattern      * @return the builder      * @see PatternHelper#matchPattern(String, String)      */
DECL|method|weaveByToString (String pattern)
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|weaveByToString
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<>
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Weaves by matching sending to endpoints with the given uri of the nodes      * in the route (incl onException etc).      *<p/>      * Uses the {@link PatternHelper#matchPattern(String, String)} matching      * algorithm.      *      * @param pattern the pattern      * @return the builder      * @see PatternHelper#matchPattern(String, String)      */
DECL|method|weaveByToUri (String pattern)
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|weaveByToUri
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<>
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Weaves by matching type of the nodes in the route (incl onException etc).      *      * @param type the processor type      * @return the builder      */
DECL|method|weaveByType (Class<T> type)
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|weaveByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<>
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Weaves by adding the nodes to the start of the route (excl onException      * etc).      *      * @return the builder      */
DECL|method|weaveAddFirst ()
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|weaveAddFirst
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
literal|"*"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|.
name|selectFirst
argument_list|()
operator|.
name|before
argument_list|()
return|;
block|}
comment|/**      * Weaves by adding the nodes to the end of the route (excl onException      * etc).      *      * @return the builder      */
DECL|method|weaveAddLast ()
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|weaveAddLast
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
literal|"*"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|.
name|maxDeep
argument_list|(
literal|1
argument_list|)
operator|.
name|selectLast
argument_list|()
operator|.
name|after
argument_list|()
return|;
block|}
block|}
end_class

end_unit

