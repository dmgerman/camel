begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|CamelContextAware
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
name|Service
import|;
end_import

begin_comment
comment|// TODO: Add javadoc
end_comment

begin_interface
annotation|@
name|Experimental
DECL|interface|RouteController
specifier|public
interface|interface
name|RouteController
extends|extends
name|CamelContextAware
extends|,
name|Service
block|{
comment|/**      * Return the list of routes controlled by this controller.      *      * @return the list of controlled routes      */
DECL|method|getControlledRoutes ()
name|Collection
argument_list|<
name|Route
argument_list|>
name|getControlledRoutes
parameter_list|()
function_decl|;
DECL|method|startRoute (String routeId)
name|void
name|startRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|stopRoute (String routeId)
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit)
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
function_decl|;
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit, boolean abortAfterTimeout)
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
function_decl|;
DECL|method|suspendRoute (String routeId)
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|suspendRoute (String routeId, long timeout, TimeUnit timeUnit)
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
function_decl|;
DECL|method|resumeRoute (String routeId)
name|void
name|resumeRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Access the underlying concrete RouteController implementation.      *      * @param clazz the proprietary class or interface of the underlying concrete RouteController.      * @return an instance of the underlying concrete RouteController as the required type.      */
DECL|method|unwrap (Class<T> clazz)
specifier|default
parameter_list|<
name|T
extends|extends
name|RouteController
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|RouteController
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|this
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to unwrap this RouteController type ("
operator|+
name|getClass
argument_list|()
operator|+
literal|") to the required type ("
operator|+
name|clazz
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
end_interface

end_unit

