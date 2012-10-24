begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Set
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
name|FailedToStartRouteException
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
name|ErrorHandlerBuilder
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Helper for {@link RouteDefinition}  *<p/>  * Utility methods to help preparing {@link RouteDefinition} before they are added to  * {@link org.apache.camel.CamelContext}.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|,
literal|"deprecation"
block|}
argument_list|)
DECL|class|RouteDefinitionHelper
specifier|public
specifier|final
class|class
name|RouteDefinitionHelper
block|{
DECL|method|RouteDefinitionHelper ()
specifier|private
name|RouteDefinitionHelper
parameter_list|()
block|{     }
comment|/**      * Validates that the target route has no duplicate id's from any of the existing routes.      *      * @param target  the target route      * @param routes  the other routes      * @return<tt>null</tt> if no duplicate id's detected, otherwise the duplicate id is returned.      */
DECL|method|validateUniqueIds (RouteDefinition target, List<RouteDefinition> routes)
specifier|public
specifier|static
name|String
name|validateUniqueIds
parameter_list|(
name|RouteDefinition
name|target
parameter_list|,
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
parameter_list|)
throws|throws
name|FailedToStartRouteException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|routesIds
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// gather all ids for the existing route, but only include custom ids, and no abstract ids
comment|// as abstract nodes is cross-cutting functionality such as interceptors etc
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routes
control|)
block|{
comment|// skip target route as we gather ids in a separate set
if|if
condition|(
name|route
operator|==
name|target
condition|)
block|{
continue|continue;
block|}
name|ProcessorDefinitionHelper
operator|.
name|getAllIDs
argument_list|(
name|route
argument_list|,
name|routesIds
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// gather all ids for the target route, but only include custom ids, and no abstract ids
comment|// as abstract nodes is cross-cutting functionality such as interceptors etc
name|Set
argument_list|<
name|String
argument_list|>
name|targetIds
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ProcessorDefinitionHelper
operator|.
name|getAllIDs
argument_list|(
name|target
argument_list|,
name|targetIds
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// now check for clash with the target route
for|for
control|(
name|String
name|id
range|:
name|targetIds
control|)
block|{
if|if
condition|(
name|routesIds
operator|.
name|contains
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|id
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|initParent (ProcessorDefinition parent)
specifier|public
specifier|static
name|void
name|initParent
parameter_list|(
name|ProcessorDefinition
name|parent
parameter_list|)
block|{
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
name|child
range|:
name|children
control|)
block|{
name|child
operator|.
name|setParent
argument_list|(
name|parent
argument_list|)
expr_stmt|;
if|if
condition|(
name|child
operator|.
name|getOutputs
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|child
operator|.
name|getOutputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// recursive the children
name|initParent
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|initParentAndErrorHandlerBuilder (ProcessorDefinition parent)
specifier|private
specifier|static
name|void
name|initParentAndErrorHandlerBuilder
parameter_list|(
name|ProcessorDefinition
name|parent
parameter_list|)
block|{
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
name|child
range|:
name|children
control|)
block|{
name|child
operator|.
name|setParent
argument_list|(
name|parent
argument_list|)
expr_stmt|;
if|if
condition|(
name|child
operator|.
name|getOutputs
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|child
operator|.
name|getOutputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// recursive the children
name|initParentAndErrorHandlerBuilder
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|prepareRouteForInit (RouteDefinition route, List<ProcessorDefinition<?>> abstracts, List<ProcessorDefinition<?>> lower)
specifier|public
specifier|static
name|void
name|prepareRouteForInit
parameter_list|(
name|RouteDefinition
name|route
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|abstracts
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|lower
parameter_list|)
block|{
comment|// filter the route into abstracts and lower
for|for
control|(
name|ProcessorDefinition
name|output
range|:
name|route
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
name|output
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
name|abstracts
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lower
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Prepares the route.      *<p/>      * This method does<b>not</b> mark the route as prepared afterwards.      *      * @param context the camel context      * @param route   the route      */
DECL|method|prepareRoute (ModelCamelContext context, RouteDefinition route)
specifier|public
specifier|static
name|void
name|prepareRoute
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|,
name|RouteDefinition
name|route
parameter_list|)
block|{
name|prepareRoute
argument_list|(
name|context
argument_list|,
name|route
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prepares the route which supports context scoped features such as onException, interceptors and onCompletions      *<p/>      * This method does<b>not</b> mark the route as prepared afterwards.      *      * @param context                            the camel context      * @param route                              the route      * @param onExceptions                       optional list of onExceptions      * @param intercepts                         optional list of interceptors      * @param interceptFromDefinitions           optional list of interceptFroms      * @param interceptSendToEndpointDefinitions optional list of interceptSendToEndpoints      * @param onCompletions                      optional list onCompletions      */
DECL|method|prepareRoute (ModelCamelContext context, RouteDefinition route, List<OnExceptionDefinition> onExceptions, List<InterceptDefinition> intercepts, List<InterceptFromDefinition> interceptFromDefinitions, List<InterceptSendToEndpointDefinition> interceptSendToEndpointDefinitions, List<OnCompletionDefinition> onCompletions)
specifier|public
specifier|static
name|void
name|prepareRoute
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|,
name|RouteDefinition
name|route
parameter_list|,
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|onExceptions
parameter_list|,
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
parameter_list|,
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|interceptFromDefinitions
parameter_list|,
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|interceptSendToEndpointDefinitions
parameter_list|,
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|onCompletions
parameter_list|)
block|{
comment|// abstracts is the cross cutting concerns
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|abstracts
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// upper is the cross cutting concerns such as interceptors, error handlers etc
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|upper
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// lower is the regular route
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|lower
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|RouteDefinitionHelper
operator|.
name|prepareRouteForInit
argument_list|(
name|route
argument_list|,
name|abstracts
argument_list|,
name|lower
argument_list|)
expr_stmt|;
comment|// parent and error handler builder should be initialized first
name|initParentAndErrorHandlerBuilder
argument_list|(
name|context
argument_list|,
name|route
argument_list|,
name|abstracts
argument_list|,
name|onExceptions
argument_list|)
expr_stmt|;
comment|// then interceptors
name|initInterceptors
argument_list|(
name|context
argument_list|,
name|route
argument_list|,
name|abstracts
argument_list|,
name|upper
argument_list|,
name|intercepts
argument_list|,
name|interceptFromDefinitions
argument_list|,
name|interceptSendToEndpointDefinitions
argument_list|)
expr_stmt|;
comment|// then on completion
name|initOnCompletions
argument_list|(
name|abstracts
argument_list|,
name|upper
argument_list|,
name|onCompletions
argument_list|)
expr_stmt|;
comment|// then transactions
name|initTransacted
argument_list|(
name|abstracts
argument_list|,
name|lower
argument_list|)
expr_stmt|;
comment|// then on exception
name|initOnExceptions
argument_list|(
name|abstracts
argument_list|,
name|upper
argument_list|,
name|onExceptions
argument_list|)
expr_stmt|;
comment|// rebuild route as upper + lower
name|route
operator|.
name|clearOutput
argument_list|()
expr_stmt|;
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
name|lower
argument_list|)
expr_stmt|;
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|upper
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sanity check the route, that it has input(s) and outputs.      *      * @param route the route      * @throws IllegalArgumentException is thrown if the route is invalid      */
DECL|method|sanityCheckRoute (RouteDefinition route)
specifier|public
specifier|static
name|void
name|sanityCheckRoute
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|route
argument_list|,
literal|"route"
argument_list|)
expr_stmt|;
if|if
condition|(
name|route
operator|.
name|getInputs
argument_list|()
operator|==
literal|null
operator|||
name|route
operator|.
name|getInputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
literal|"Route has no inputs: "
operator|+
name|route
decl_stmt|;
if|if
condition|(
name|route
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
literal|"Route "
operator|+
name|route
operator|.
name|getId
argument_list|()
operator|+
literal|" has no inputs: "
operator|+
name|route
expr_stmt|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
name|route
operator|.
name|getOutputs
argument_list|()
operator|==
literal|null
operator|||
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
literal|"Route has no outputs: "
operator|+
name|route
decl_stmt|;
if|if
condition|(
name|route
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
literal|"Route "
operator|+
name|route
operator|.
name|getId
argument_list|()
operator|+
literal|" has no outputs: "
operator|+
name|route
expr_stmt|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
DECL|method|initParentAndErrorHandlerBuilder (ModelCamelContext context, RouteDefinition route, List<ProcessorDefinition<?>> abstracts, List<OnExceptionDefinition> onExceptions)
specifier|private
specifier|static
name|void
name|initParentAndErrorHandlerBuilder
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|,
name|RouteDefinition
name|route
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|abstracts
parameter_list|,
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|onExceptions
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
comment|// let the route inherit the error handler builder from camel context if none already set
comment|// must clone to avoid side effects while building routes using multiple RouteBuilders
name|ErrorHandlerBuilder
name|builder
init|=
name|context
operator|.
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|builder
operator|!=
literal|null
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|cloneBuilder
argument_list|()
expr_stmt|;
name|route
operator|.
name|setErrorHandlerBuilderIfNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
comment|// init parent and error handler builder on the route
name|initParentAndErrorHandlerBuilder
argument_list|(
name|route
argument_list|)
expr_stmt|;
comment|// set the parent and error handler builder on the global on exceptions
if|if
condition|(
name|onExceptions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|OnExceptionDefinition
name|global
range|:
name|onExceptions
control|)
block|{
comment|//global.setErrorHandlerBuilder(context.getErrorHandlerBuilder());
name|initParentAndErrorHandlerBuilder
argument_list|(
name|global
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|initOnExceptions (List<ProcessorDefinition<?>> abstracts, List<ProcessorDefinition<?>> upper, List<OnExceptionDefinition> onExceptions)
specifier|private
specifier|static
name|void
name|initOnExceptions
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|abstracts
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|upper
parameter_list|,
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|onExceptions
parameter_list|)
block|{
comment|// add global on exceptions if any
if|if
condition|(
name|onExceptions
operator|!=
literal|null
operator|&&
operator|!
name|onExceptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|abstracts
operator|.
name|addAll
argument_list|(
name|onExceptions
argument_list|)
expr_stmt|;
block|}
comment|// now add onExceptions to the route
for|for
control|(
name|ProcessorDefinition
name|output
range|:
name|abstracts
control|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|OnExceptionDefinition
condition|)
block|{
comment|// on exceptions must be added at top, so the route flow is correct as
comment|// on exceptions should be the first outputs
comment|// find the index to add the on exception, it should be in the top
comment|// but it should add itself after any existing onException
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|upper
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ProcessorDefinition
name|up
init|=
name|upper
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|up
operator|instanceof
name|OnExceptionDefinition
operator|)
condition|)
block|{
name|index
operator|=
name|i
expr_stmt|;
break|break;
block|}
else|else
block|{
name|index
operator|++
expr_stmt|;
block|}
block|}
name|upper
operator|.
name|add
argument_list|(
name|index
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|initInterceptors (CamelContext context, RouteDefinition route, List<ProcessorDefinition<?>> abstracts, List<ProcessorDefinition<?>> upper, List<InterceptDefinition> intercepts, List<InterceptFromDefinition> interceptFromDefinitions, List<InterceptSendToEndpointDefinition> interceptSendToEndpointDefinitions)
specifier|private
specifier|static
name|void
name|initInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RouteDefinition
name|route
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|abstracts
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|upper
parameter_list|,
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
parameter_list|,
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|interceptFromDefinitions
parameter_list|,
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|interceptSendToEndpointDefinitions
parameter_list|)
block|{
comment|// move the abstracts interceptors into the dedicated list
for|for
control|(
name|ProcessorDefinition
name|processor
range|:
name|abstracts
control|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|InterceptSendToEndpointDefinition
condition|)
block|{
if|if
condition|(
name|interceptSendToEndpointDefinitions
operator|==
literal|null
condition|)
block|{
name|interceptSendToEndpointDefinitions
operator|=
operator|new
name|ArrayList
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|interceptSendToEndpointDefinitions
operator|.
name|add
argument_list|(
operator|(
name|InterceptSendToEndpointDefinition
operator|)
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|InterceptFromDefinition
condition|)
block|{
if|if
condition|(
name|interceptFromDefinitions
operator|==
literal|null
condition|)
block|{
name|interceptFromDefinitions
operator|=
operator|new
name|ArrayList
argument_list|<
name|InterceptFromDefinition
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|interceptFromDefinitions
operator|.
name|add
argument_list|(
operator|(
name|InterceptFromDefinition
operator|)
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|InterceptDefinition
condition|)
block|{
if|if
condition|(
name|intercepts
operator|==
literal|null
condition|)
block|{
name|intercepts
operator|=
operator|new
name|ArrayList
argument_list|<
name|InterceptDefinition
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|intercepts
operator|.
name|add
argument_list|(
operator|(
name|InterceptDefinition
operator|)
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
name|doInitInterceptors
argument_list|(
name|context
argument_list|,
name|route
argument_list|,
name|upper
argument_list|,
name|intercepts
argument_list|,
name|interceptFromDefinitions
argument_list|,
name|interceptSendToEndpointDefinitions
argument_list|)
expr_stmt|;
block|}
DECL|method|doInitInterceptors (CamelContext context, RouteDefinition route, List<ProcessorDefinition<?>> upper, List<InterceptDefinition> intercepts, List<InterceptFromDefinition> interceptFromDefinitions, List<InterceptSendToEndpointDefinition> interceptSendToEndpointDefinitions)
specifier|private
specifier|static
name|void
name|doInitInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RouteDefinition
name|route
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|upper
parameter_list|,
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
parameter_list|,
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|interceptFromDefinitions
parameter_list|,
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|interceptSendToEndpointDefinitions
parameter_list|)
block|{
comment|// configure intercept
if|if
condition|(
name|intercepts
operator|!=
literal|null
operator|&&
operator|!
name|intercepts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|InterceptDefinition
name|intercept
range|:
name|intercepts
control|)
block|{
name|intercept
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
comment|// init the parent
name|initParent
argument_list|(
name|intercept
argument_list|)
expr_stmt|;
comment|// add as first output so intercept is handled before the actual route and that gives
comment|// us the needed head start to init and be able to intercept all the remaining processing steps
name|upper
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|intercept
argument_list|)
expr_stmt|;
block|}
block|}
comment|// configure intercept from
if|if
condition|(
name|interceptFromDefinitions
operator|!=
literal|null
operator|&&
operator|!
name|interceptFromDefinitions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|InterceptFromDefinition
name|intercept
range|:
name|interceptFromDefinitions
control|)
block|{
comment|// should we only apply interceptor for a given endpoint uri
name|boolean
name|match
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|intercept
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
literal|false
expr_stmt|;
for|for
control|(
name|FromDefinition
name|input
range|:
name|route
operator|.
name|getInputs
argument_list|()
control|)
block|{
comment|// a bit more logic to lookup the endpoint as it can be uri/ref based
name|String
name|uri
init|=
name|input
operator|.
name|getUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
name|uri
operator|.
name|startsWith
argument_list|(
literal|"ref:"
argument_list|)
condition|)
block|{
comment|// its a ref: so lookup the endpoint to get its url
name|uri
operator|=
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|context
argument_list|,
name|uri
argument_list|)
operator|.
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|input
operator|.
name|getRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// lookup the endpoint to get its url
name|uri
operator|=
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|context
argument_list|,
literal|"ref:"
operator|+
name|input
operator|.
name|getRef
argument_list|()
argument_list|)
operator|.
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|EndpointHelper
operator|.
name|matchEndpoint
argument_list|(
name|context
argument_list|,
name|uri
argument_list|,
name|intercept
operator|.
name|getUri
argument_list|()
argument_list|)
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|match
condition|)
block|{
name|intercept
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
comment|// init the parent
name|initParent
argument_list|(
name|intercept
argument_list|)
expr_stmt|;
comment|// add as first output so intercept is handled before the actual route and that gives
comment|// us the needed head start to init and be able to intercept all the remaining processing steps
name|upper
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|intercept
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// configure intercept send to endpoint
if|if
condition|(
name|interceptSendToEndpointDefinitions
operator|!=
literal|null
operator|&&
operator|!
name|interceptSendToEndpointDefinitions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|InterceptSendToEndpointDefinition
name|intercept
range|:
name|interceptSendToEndpointDefinitions
control|)
block|{
name|intercept
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
comment|// init the parent
name|initParent
argument_list|(
name|intercept
argument_list|)
expr_stmt|;
comment|// add as first output so intercept is handled before the actual route and that gives
comment|// us the needed head start to init and be able to intercept all the remaining processing steps
name|upper
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|intercept
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|initOnCompletions (List<ProcessorDefinition<?>> abstracts, List<ProcessorDefinition<?>> upper, List<OnCompletionDefinition> onCompletions)
specifier|private
specifier|static
name|void
name|initOnCompletions
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|abstracts
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|upper
parameter_list|,
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|onCompletions
parameter_list|)
block|{
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|completions
init|=
operator|new
name|ArrayList
argument_list|<
name|OnCompletionDefinition
argument_list|>
argument_list|()
decl_stmt|;
comment|// find the route scoped onCompletions
for|for
control|(
name|ProcessorDefinition
name|out
range|:
name|abstracts
control|)
block|{
if|if
condition|(
name|out
operator|instanceof
name|OnCompletionDefinition
condition|)
block|{
name|completions
operator|.
name|add
argument_list|(
operator|(
name|OnCompletionDefinition
operator|)
name|out
argument_list|)
expr_stmt|;
block|}
block|}
comment|// only add global onCompletion if there are no route already
if|if
condition|(
name|completions
operator|.
name|isEmpty
argument_list|()
operator|&&
name|onCompletions
operator|!=
literal|null
condition|)
block|{
name|completions
operator|=
name|onCompletions
expr_stmt|;
comment|// init the parent
for|for
control|(
name|OnCompletionDefinition
name|global
range|:
name|completions
control|)
block|{
name|initParent
argument_list|(
name|global
argument_list|)
expr_stmt|;
block|}
block|}
comment|// are there any completions to init at all?
if|if
condition|(
name|completions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|upper
operator|.
name|addAll
argument_list|(
name|completions
argument_list|)
expr_stmt|;
block|}
DECL|method|initTransacted (List<ProcessorDefinition<?>> abstracts, List<ProcessorDefinition<?>> lower)
specifier|private
specifier|static
name|void
name|initTransacted
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|abstracts
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|lower
parameter_list|)
block|{
name|TransactedDefinition
name|transacted
init|=
literal|null
decl_stmt|;
comment|// add to correct type
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|type
range|:
name|abstracts
control|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|TransactedDefinition
condition|)
block|{
if|if
condition|(
name|transacted
operator|==
literal|null
condition|)
block|{
name|transacted
operator|=
operator|(
name|TransactedDefinition
operator|)
name|type
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The route can only have one transacted defined"
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|transacted
operator|!=
literal|null
condition|)
block|{
comment|// the outputs should be moved to the transacted policy
name|transacted
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
name|lower
argument_list|)
expr_stmt|;
comment|// and add it as the single output
name|lower
operator|.
name|clear
argument_list|()
expr_stmt|;
name|lower
operator|.
name|add
argument_list|(
name|transacted
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Force assigning ids to the give node and all its children (recursively).      *<p/>      * This is needed when doing tracing or the likes, where each node should have its id assigned      * so the tracing can pin point exactly.      *      * @param context the camel context      * @param processor the node      */
DECL|method|forceAssignIds (CamelContext context, ProcessorDefinition processor)
specifier|public
specifier|static
name|void
name|forceAssignIds
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ProcessorDefinition
name|processor
parameter_list|)
block|{
comment|// force id on the child
name|processor
operator|.
name|idOrCreate
argument_list|(
name|context
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|processor
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
if|if
condition|(
name|children
operator|!=
literal|null
operator|&&
operator|!
name|children
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|ProcessorDefinition
name|child
range|:
name|children
control|)
block|{
name|forceAssignIds
argument_list|(
name|context
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

