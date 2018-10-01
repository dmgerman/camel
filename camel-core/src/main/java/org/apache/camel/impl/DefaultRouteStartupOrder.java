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
name|Consumer
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
name|RouteStartupOrder
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link org.apache.camel.spi.RouteStartupOrder}.  */
end_comment

begin_class
DECL|class|DefaultRouteStartupOrder
specifier|public
class|class
name|DefaultRouteStartupOrder
implements|implements
name|RouteStartupOrder
block|{
DECL|field|startupOrder
specifier|private
specifier|final
name|int
name|startupOrder
decl_stmt|;
DECL|field|route
specifier|private
specifier|final
name|Route
name|route
decl_stmt|;
DECL|field|routeService
specifier|private
specifier|final
name|RouteService
name|routeService
decl_stmt|;
DECL|method|DefaultRouteStartupOrder (int startupOrder, Route route, RouteService routeService)
specifier|public
name|DefaultRouteStartupOrder
parameter_list|(
name|int
name|startupOrder
parameter_list|,
name|Route
name|route
parameter_list|,
name|RouteService
name|routeService
parameter_list|)
block|{
name|this
operator|.
name|startupOrder
operator|=
name|startupOrder
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|routeService
operator|=
name|routeService
expr_stmt|;
block|}
DECL|method|getStartupOrder ()
specifier|public
name|int
name|getStartupOrder
parameter_list|()
block|{
return|return
name|startupOrder
return|;
block|}
DECL|method|getRoute ()
specifier|public
name|Route
name|getRoute
parameter_list|()
block|{
return|return
name|route
return|;
block|}
DECL|method|getInputs ()
specifier|public
name|List
argument_list|<
name|Consumer
argument_list|>
name|getInputs
parameter_list|()
block|{
name|Map
argument_list|<
name|Route
argument_list|,
name|Consumer
argument_list|>
name|inputs
init|=
name|routeService
operator|.
name|getInputs
argument_list|()
decl_stmt|;
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|inputs
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getServices ()
specifier|public
name|List
argument_list|<
name|Service
argument_list|>
name|getServices
parameter_list|()
block|{
name|List
argument_list|<
name|Service
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|routeService
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|route
operator|.
name|getServices
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getRouteService ()
specifier|public
name|RouteService
name|getRouteService
parameter_list|()
block|{
return|return
name|routeService
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Route "
operator|+
name|route
operator|.
name|getId
argument_list|()
operator|+
literal|" starts in order "
operator|+
name|startupOrder
return|;
block|}
block|}
end_class

end_unit

