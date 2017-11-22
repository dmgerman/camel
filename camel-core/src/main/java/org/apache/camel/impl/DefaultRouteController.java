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
name|Collections
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
name|TimeUnit
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
name|Experimental
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
name|spi
operator|.
name|RouteController
import|;
end_import

begin_class
annotation|@
name|Experimental
DECL|class|DefaultRouteController
specifier|public
class|class
name|DefaultRouteController
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
implements|implements
name|RouteController
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|DefaultRouteController ()
specifier|public
name|DefaultRouteController
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultRouteController (CamelContext camelContext)
specifier|public
name|DefaultRouteController
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
block|}
comment|// ***************************************************
comment|// Properties
comment|// ***************************************************
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
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
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
comment|// ***************************************************
comment|// Life cycle
comment|// ***************************************************
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
comment|// ***************************************************
comment|// Route management
comment|// ***************************************************
annotation|@
name|Override
DECL|method|startRoute (String routeId)
specifier|public
name|void
name|startRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|startRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stopRoute (String routeId)
specifier|public
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|stopRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit)
specifier|public
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|stopRoute
argument_list|(
name|routeId
argument_list|,
name|timeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit, boolean abortAfterTimeout)
specifier|public
name|boolean
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|,
name|boolean
name|abortAfterTimeout
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|camelContext
operator|.
name|stopRoute
argument_list|(
name|routeId
argument_list|,
name|timeout
argument_list|,
name|timeUnit
argument_list|,
name|abortAfterTimeout
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|suspendRoute (String routeId)
specifier|public
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|suspendRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|suspendRoute (String routeId, long timeout, TimeUnit timeUnit)
specifier|public
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|suspendRoute
argument_list|(
name|routeId
argument_list|,
name|timeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resumeRoute (String routeId)
specifier|public
name|void
name|resumeRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|resumeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
comment|// ***************************************************
comment|//
comment|// ***************************************************
annotation|@
name|Override
DECL|method|getControlledRoutes ()
specifier|public
name|Collection
argument_list|<
name|Route
argument_list|>
name|getControlledRoutes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

