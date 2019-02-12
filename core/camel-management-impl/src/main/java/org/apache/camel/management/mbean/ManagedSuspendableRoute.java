begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedSuspendableRouteMBean
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

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Suspendable Route"
argument_list|)
DECL|class|ManagedSuspendableRoute
specifier|public
class|class
name|ManagedSuspendableRoute
extends|extends
name|ManagedRoute
implements|implements
name|ManagedSuspendableRouteMBean
block|{
DECL|method|ManagedSuspendableRoute (ModelCamelContext context, Route route)
specifier|public
name|ManagedSuspendableRoute
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
DECL|method|suspend ()
specifier|public
name|void
name|suspend
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|suspendRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|suspend (long timeout)
specifier|public
name|void
name|suspend
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|suspendRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|resume ()
specifier|public
name|void
name|resume
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|resumeRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
