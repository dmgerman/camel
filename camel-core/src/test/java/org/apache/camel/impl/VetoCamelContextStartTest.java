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
name|Collection
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
name|ThreadPoolExecutor
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
name|Component
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
name|ContextTestSupport
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
name|ErrorHandlerFactory
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
name|VetoCamelContextStartException
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|VetoCamelContextStartTest
specifier|public
class|class
name|VetoCamelContextStartTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|veto
specifier|private
name|LifecycleStrategy
name|veto
init|=
operator|new
name|MyVeto
argument_list|()
decl_stmt|;
DECL|method|testVetoCamelContextStart ()
specifier|public
name|void
name|testVetoCamelContextStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// context is veto'ed but appears as started
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
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
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addLifecycleStrategy
argument_list|(
name|veto
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|class|MyVeto
specifier|private
class|class
name|MyVeto
implements|implements
name|LifecycleStrategy
block|{
annotation|@
name|Override
DECL|method|onContextStart (CamelContext context)
specifier|public
name|void
name|onContextStart
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|VetoCamelContextStartException
block|{
comment|// we just want camel context to not startup, but do not rethrow exception
throw|throw
operator|new
name|VetoCamelContextStartException
argument_list|(
literal|"Forced"
argument_list|,
name|context
argument_list|,
literal|false
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|onContextStop (CamelContext context)
specifier|public
name|void
name|onContextStop
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onComponentAdd (String name, Component component)
specifier|public
name|void
name|onComponentAdd
parameter_list|(
name|String
name|name
parameter_list|,
name|Component
name|component
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onComponentRemove (String name, Component component)
specifier|public
name|void
name|onComponentRemove
parameter_list|(
name|String
name|name
parameter_list|,
name|Component
name|component
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onEndpointAdd (Endpoint endpoint)
specifier|public
name|void
name|onEndpointAdd
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onEndpointRemove (Endpoint endpoint)
specifier|public
name|void
name|onEndpointRemove
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onServiceAdd (CamelContext context, Service service, Route route)
specifier|public
name|void
name|onServiceAdd
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onServiceRemove (CamelContext context, Service service, Route route)
specifier|public
name|void
name|onServiceRemove
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onRoutesAdd (Collection<Route> routes)
specifier|public
name|void
name|onRoutesAdd
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onRoutesRemove (Collection<Route> routes)
specifier|public
name|void
name|onRoutesRemove
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onRouteContextCreate (RouteContext routeContext)
specifier|public
name|void
name|onRouteContextCreate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onErrorHandlerAdd (RouteContext routeContext, Processor errorHandler, ErrorHandlerFactory errorHandlerBuilder)
specifier|public
name|void
name|onErrorHandlerAdd
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|errorHandler
parameter_list|,
name|ErrorHandlerFactory
name|errorHandlerBuilder
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onThreadPoolAdd (CamelContext camelContext, ThreadPoolExecutor threadPool, String id, String sourceId, String routeId, String threadPoolProfileId)
specifier|public
name|void
name|onThreadPoolAdd
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ThreadPoolExecutor
name|threadPool
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|sourceId
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|threadPoolProfileId
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onThreadPoolRemove (CamelContext camelContext, ThreadPoolExecutor threadPool)
specifier|public
name|void
name|onThreadPoolRemove
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ThreadPoolExecutor
name|threadPool
parameter_list|)
block|{         }
block|}
block|}
end_class

end_unit

