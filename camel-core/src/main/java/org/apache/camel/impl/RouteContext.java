begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Collection
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
name|AsyncProcessor
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
name|impl
operator|.
name|converter
operator|.
name|AsyncProcessorTypeConverter
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
name|FromType
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
name|ProcessorType
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
name|RouteType
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
name|Interceptor
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
name|Pipeline
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
name|ProceedProcessor
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
name|UnitOfWorkProcessor
import|;
end_import

begin_comment
comment|/**  * The context used to activate new routing rules  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|RouteContext
specifier|public
class|class
name|RouteContext
block|{
DECL|field|route
specifier|private
name|RouteType
name|route
decl_stmt|;
DECL|field|from
specifier|private
name|FromType
name|from
decl_stmt|;
DECL|field|routes
specifier|private
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|eventDrivenProcessors
specifier|private
name|List
argument_list|<
name|Processor
argument_list|>
name|eventDrivenProcessors
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|lastInterceptor
specifier|private
name|Interceptor
name|lastInterceptor
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|RouteContext (RouteType route, FromType from, Collection<Route> routes)
specifier|public
name|RouteContext
parameter_list|(
name|RouteType
name|route
parameter_list|,
name|FromType
name|from
parameter_list|,
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
comment|/**      * Only used for lazy construction from inside ExpressionType      */
DECL|method|RouteContext (CamelContext camelContext)
specifier|public
name|RouteContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|routes
operator|=
operator|new
name|ArrayList
argument_list|<
name|Route
argument_list|>
argument_list|()
expr_stmt|;
name|route
operator|=
operator|new
name|RouteType
argument_list|(
literal|"temporary"
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|=
name|from
operator|.
name|resolveEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getFrom ()
specifier|public
name|FromType
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|getRoute ()
specifier|public
name|RouteType
name|getRoute
parameter_list|()
block|{
return|return
name|route
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
name|camelContext
operator|=
name|getRoute
argument_list|()
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
block|}
return|return
name|camelContext
return|;
block|}
DECL|method|createProcessor (ProcessorType node)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|ProcessorType
name|node
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|node
operator|.
name|createOutputsProcessor
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|resolveEndpoint (String uri)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|route
operator|.
name|resolveEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|/**      * Resolves an endpoint from either a URI or a named reference      */
DECL|method|resolveEndpoint (String uri, String ref)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|=
name|resolveEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
name|uri
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|=
name|lookup
argument_list|(
name|ref
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
literal|"ref:"
operator|+
name|ref
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either 'uri' or 'ref' must be specified on: "
operator|+
name|this
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|endpoint
return|;
block|}
block|}
comment|/**      * lookup an object by name and type      */
DECL|method|lookup (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Lets complete the route creation, creating a single event driven route      * for the current from endpoint with any processors required      */
DECL|method|commit ()
specifier|public
name|void
name|commit
parameter_list|()
block|{
comment|// now lets turn all of the event driven consumer processors into a
comment|// single route
if|if
condition|(
operator|!
name|eventDrivenProcessors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Processor
name|processor
init|=
name|Pipeline
operator|.
name|newInstance
argument_list|(
name|eventDrivenProcessors
argument_list|)
decl_stmt|;
comment|// lets create the async processor
specifier|final
name|AsyncProcessor
name|asyncProcessor
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|Processor
name|unitOfWorkProcessor
init|=
operator|new
name|UnitOfWorkProcessor
argument_list|(
name|asyncProcessor
argument_list|)
decl_stmt|;
name|routes
operator|.
name|add
argument_list|(
operator|new
name|EventDrivenConsumerRoute
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|unitOfWorkProcessor
argument_list|)
argument_list|)
expr_stmt|;
comment|//routes.add(new EventDrivenConsumerRoute(getEndpoint(), processor));
block|}
block|}
DECL|method|addEventDrivenProcessor (Processor processor)
specifier|public
name|void
name|addEventDrivenProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|eventDrivenProcessors
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|intercept (Interceptor interceptor)
specifier|public
name|void
name|intercept
parameter_list|(
name|Interceptor
name|interceptor
parameter_list|)
block|{
name|getRoute
argument_list|()
operator|.
name|intercept
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
name|lastInterceptor
operator|=
name|interceptor
expr_stmt|;
block|}
DECL|method|createProceedProcessor ()
specifier|public
name|Processor
name|createProceedProcessor
parameter_list|()
block|{
if|if
condition|(
name|lastInterceptor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot proceed() from outside of an interceptor!"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
operator|new
name|ProceedProcessor
argument_list|(
name|lastInterceptor
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

