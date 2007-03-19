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
name|EndpointResolver
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
name|impl
operator|.
name|DefaultEndpointResolver
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_comment
comment|/**  * A builder of destinationBuilders using a typesafe Java DLS.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteBuilder
specifier|public
specifier|abstract
class|class
name|RouteBuilder
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
DECL|field|endpointResolver
specifier|private
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|endpointResolver
decl_stmt|;
DECL|field|destinationBuilders
specifier|private
name|List
argument_list|<
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
argument_list|>
name|destinationBuilders
init|=
operator|new
name|ArrayList
argument_list|<
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|initalized
specifier|private
name|AtomicBoolean
name|initalized
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|routeMap
specifier|private
name|Map
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|>
name|routeMap
init|=
operator|new
name|HashMap
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Called on initialisation to to build the required destinationBuilders      */
DECL|method|configure ()
specifier|public
specifier|abstract
name|void
name|configure
parameter_list|()
function_decl|;
comment|/**      * Resolves the given URI to an endpoint      */
DECL|method|endpoint (String uri)
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|getEndpointResolver
argument_list|()
operator|.
name|resolve
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|from (String uri)
specifier|public
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
name|from
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|from
argument_list|(
name|endpoint
argument_list|(
name|uri
argument_list|)
argument_list|)
return|;
block|}
DECL|method|from (Endpoint<E> endpoint)
specifier|public
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
name|from
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|)
block|{
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|destinationBuilders
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// Helper methods
comment|//-----------------------------------------------------------------------
DECL|method|headerEquals (final String header, final Object value)
specifier|public
name|Predicate
argument_list|<
name|E
argument_list|>
name|headerEquals
parameter_list|(
specifier|final
name|String
name|header
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
block|{
return|return
operator|new
name|Predicate
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|equals
argument_list|(
name|value
argument_list|,
name|exchange
operator|.
name|getHeader
argument_list|(
name|header
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"header["
operator|+
name|header
operator|+
literal|"] == "
operator|+
name|value
return|;
block|}
block|}
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
comment|/**      * Returns the routing map from inbound endpoints to processors      */
DECL|method|getRouteMap ()
specifier|public
name|Map
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|>
name|getRouteMap
parameter_list|()
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
return|return
name|routeMap
return|;
block|}
comment|/**      * Returns the destinationBuilders which have been created      */
DECL|method|getDestinationBuilders ()
specifier|public
name|List
argument_list|<
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
argument_list|>
name|getDestinationBuilders
parameter_list|()
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
return|return
name|destinationBuilders
return|;
block|}
DECL|method|getEndpointResolver ()
specifier|public
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|getEndpointResolver
parameter_list|()
block|{
if|if
condition|(
name|endpointResolver
operator|==
literal|null
condition|)
block|{
name|endpointResolver
operator|=
name|createEndpointResolver
argument_list|()
expr_stmt|;
block|}
return|return
name|endpointResolver
return|;
block|}
DECL|method|setEndpointResolver (EndpointResolver<E> endpointResolver)
specifier|public
name|void
name|setEndpointResolver
parameter_list|(
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|endpointResolver
parameter_list|)
block|{
name|this
operator|.
name|endpointResolver
operator|=
name|endpointResolver
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-----------------------------------------------------------------------
DECL|method|createEndpointResolver ()
specifier|protected
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|createEndpointResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultEndpointResolver
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
DECL|method|checkInitialized ()
specifier|protected
name|void
name|checkInitialized
parameter_list|()
block|{
if|if
condition|(
name|initalized
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|configure
argument_list|()
expr_stmt|;
name|populateRouteMap
argument_list|(
name|routeMap
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populateRouteMap (Map<Endpoint<E>, List<Processor<E>>> routeMap)
specifier|protected
name|void
name|populateRouteMap
parameter_list|(
name|Map
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|,
name|List
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|>
name|routeMap
parameter_list|)
block|{
for|for
control|(
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
name|destinationBuilder
range|:
name|destinationBuilders
control|)
block|{
name|Endpoint
argument_list|<
name|E
argument_list|>
name|from
init|=
name|destinationBuilder
operator|.
name|getFrom
argument_list|()
decl_stmt|;
name|destinationBuilder
operator|.
name|createProcessors
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|processors
init|=
name|destinationBuilder
operator|.
name|getProcessors
argument_list|()
decl_stmt|;
name|routeMap
operator|.
name|put
argument_list|(
name|from
argument_list|,
name|processors
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

