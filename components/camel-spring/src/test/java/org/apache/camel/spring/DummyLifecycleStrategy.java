begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
comment|/**  * Dummy LifecycleStrategy for LifecycleStrategy injection test.  *  * @version $Revision$  *  */
end_comment

begin_class
DECL|class|DummyLifecycleStrategy
specifier|public
class|class
name|DummyLifecycleStrategy
implements|implements
name|LifecycleStrategy
block|{
DECL|method|onContextCreate (CamelContext context)
specifier|public
name|void
name|onContextCreate
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
DECL|method|onEndpointAdd (Endpoint<? extends Exchange> endpoint)
specifier|public
name|void
name|onEndpointAdd
parameter_list|(
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|endpoint
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
DECL|method|onRouteContextCreate (RouteContext routeContext)
specifier|public
name|void
name|onRouteContextCreate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
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
block|{
comment|// TODO Auto-generated method stub
block|}
DECL|method|onServiceAdd (CamelContext context, Service service)
specifier|public
name|void
name|onServiceAdd
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
DECL|method|onContextStart (CamelContext arg0)
specifier|public
name|void
name|onContextStart
parameter_list|(
name|CamelContext
name|arg0
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
end_class

end_unit

