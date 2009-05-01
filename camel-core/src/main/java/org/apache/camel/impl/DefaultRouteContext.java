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
name|java
operator|.
name|util
operator|.
name|Map
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
name|Intercept
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
name|model
operator|.
name|FromDefinition
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
name|model
operator|.
name|dataformat
operator|.
name|DataFormatDefinition
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
name|InterceptStrategy
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

begin_comment
comment|/**  * The context used to activate new routing rules  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultRouteContext
specifier|public
class|class
name|DefaultRouteContext
implements|implements
name|RouteContext
block|{
DECL|field|route
specifier|private
specifier|final
name|RouteDefinition
name|route
decl_stmt|;
DECL|field|from
specifier|private
name|FromDefinition
name|from
decl_stmt|;
DECL|field|routes
specifier|private
specifier|final
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
specifier|final
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
DECL|field|interceptStrategies
specifier|private
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptStrategies
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptStrategy
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|routeAdded
specifier|private
name|boolean
name|routeAdded
decl_stmt|;
DECL|method|DefaultRouteContext (RouteDefinition route, FromDefinition from, Collection<Route> routes)
specifier|public
name|DefaultRouteContext
parameter_list|(
name|RouteDefinition
name|route
parameter_list|,
name|FromDefinition
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
DECL|method|DefaultRouteContext (CamelContext camelContext)
specifier|public
name|DefaultRouteContext
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
name|RouteDefinition
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
name|FromDefinition
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|getRoute ()
specifier|public
name|RouteDefinition
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
DECL|method|createProcessor (ProcessorDefinition node)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|ProcessorDefinition
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
DECL|method|lookupByType (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|lookupByType
parameter_list|(
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
name|lookupByType
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|commit ()
specifier|public
name|void
name|commit
parameter_list|()
block|{
comment|// now lets turn all of the event driven consumer processors into a single route
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
comment|// and wrap it in a unit of work so the UoW is on the top, so the entire route will be in the same UoW
name|Processor
name|unitOfWorkProcessor
init|=
operator|new
name|UnitOfWorkProcessor
argument_list|(
name|processor
argument_list|)
decl_stmt|;
comment|// and create the route that wraps the UoW
name|Route
name|edcr
init|=
operator|new
name|EventDrivenConsumerRoute
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|unitOfWorkProcessor
argument_list|)
decl_stmt|;
name|edcr
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|Route
operator|.
name|ID_PROPERTY
argument_list|,
name|route
operator|.
name|idOrCreate
argument_list|()
argument_list|)
expr_stmt|;
name|edcr
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|Route
operator|.
name|PARENT_PROPERTY
argument_list|,
name|Integer
operator|.
name|toHexString
argument_list|(
name|route
operator|.
name|hashCode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|route
operator|.
name|getGroup
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|edcr
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|Route
operator|.
name|GROUP_PROPERTY
argument_list|,
name|route
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|routes
operator|.
name|add
argument_list|(
name|edcr
argument_list|)
expr_stmt|;
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
DECL|method|getInterceptStrategies ()
specifier|public
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|getInterceptStrategies
parameter_list|()
block|{
return|return
name|interceptStrategies
return|;
block|}
DECL|method|setInterceptStrategies (List<InterceptStrategy> interceptStrategies)
specifier|public
name|void
name|setInterceptStrategies
parameter_list|(
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptStrategies
parameter_list|)
block|{
name|this
operator|.
name|interceptStrategies
operator|=
name|interceptStrategies
expr_stmt|;
block|}
DECL|method|addInterceptStrategy (InterceptStrategy interceptStrategy)
specifier|public
name|void
name|addInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|interceptStrategy
parameter_list|)
block|{
name|getInterceptStrategies
argument_list|()
operator|.
name|add
argument_list|(
name|interceptStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|isRouteAdded ()
specifier|public
name|boolean
name|isRouteAdded
parameter_list|()
block|{
return|return
name|routeAdded
return|;
block|}
DECL|method|setIsRouteAdded (boolean routeAdded)
specifier|public
name|void
name|setIsRouteAdded
parameter_list|(
name|boolean
name|routeAdded
parameter_list|)
block|{
name|this
operator|.
name|routeAdded
operator|=
name|routeAdded
expr_stmt|;
block|}
DECL|method|getDataFormat (String ref)
specifier|public
name|DataFormatDefinition
name|getDataFormat
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|DataFormatDefinition
argument_list|>
name|dataFormats
init|=
name|getCamelContext
argument_list|()
operator|.
name|getDataFormats
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataFormats
operator|!=
literal|null
condition|)
block|{
return|return
name|dataFormats
operator|.
name|get
argument_list|(
name|ref
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

