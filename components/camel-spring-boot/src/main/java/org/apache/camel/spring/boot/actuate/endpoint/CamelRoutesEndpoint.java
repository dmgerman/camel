begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.actuate.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
package|;
end_package

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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonInclude
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonPropertyOrder
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
name|StatefulService
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
name|spring
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|CamelRoutesEndpoint
operator|.
name|RouteEndpointInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|AbstractEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|Endpoint
import|;
end_import

begin_comment
comment|/**  * {@link Endpoint} to expose {@link org.apache.camel.Route} information.  */
end_comment

begin_class
DECL|class|CamelRoutesEndpoint
specifier|public
class|class
name|CamelRoutesEndpoint
extends|extends
name|AbstractEndpoint
argument_list|<
name|List
argument_list|<
name|RouteEndpointInfo
argument_list|>
argument_list|>
block|{
DECL|field|ENDPOINT_ID
specifier|private
specifier|static
specifier|final
name|String
name|ENDPOINT_ID
init|=
literal|"camelroutes"
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|CamelRoutesEndpoint (CamelContext camelContext)
specifier|public
name|CamelRoutesEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|ENDPOINT_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|invoke ()
specifier|public
name|List
argument_list|<
name|RouteEndpointInfo
argument_list|>
name|invoke
parameter_list|()
block|{
comment|// @formatter:off
return|return
name|camelContext
operator|.
name|getRoutes
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|RouteEndpointInfo
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
comment|// @formatter:on
block|}
comment|/**      * Container for exposing {@link org.apache.camel.Route} information as JSON.      */
annotation|@
name|JsonPropertyOrder
argument_list|(
block|{
literal|"id"
block|,
literal|"description"
block|,
literal|"uptime"
block|,
literal|"uptimeMillis"
block|}
argument_list|)
annotation|@
name|JsonInclude
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_EMPTY
argument_list|)
DECL|class|RouteEndpointInfo
specifier|public
specifier|static
class|class
name|RouteEndpointInfo
block|{
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|field|uptime
specifier|private
specifier|final
name|String
name|uptime
decl_stmt|;
DECL|field|uptimeMillis
specifier|private
specifier|final
name|long
name|uptimeMillis
decl_stmt|;
DECL|field|status
specifier|private
name|String
name|status
decl_stmt|;
DECL|method|RouteEndpointInfo (Route route)
specifier|public
name|RouteEndpointInfo
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|route
operator|.
name|getId
argument_list|()
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|route
operator|.
name|getDescription
argument_list|()
expr_stmt|;
name|this
operator|.
name|uptime
operator|=
name|route
operator|.
name|getUptime
argument_list|()
expr_stmt|;
name|this
operator|.
name|uptimeMillis
operator|=
name|route
operator|.
name|getUptimeMillis
argument_list|()
expr_stmt|;
if|if
condition|(
name|route
operator|instanceof
name|StatefulService
condition|)
block|{
name|this
operator|.
name|status
operator|=
operator|(
operator|(
name|StatefulService
operator|)
name|route
operator|)
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|getUptime ()
specifier|public
name|String
name|getUptime
parameter_list|()
block|{
return|return
name|uptime
return|;
block|}
DECL|method|getUptimeMillis ()
specifier|public
name|long
name|getUptimeMillis
parameter_list|()
block|{
return|return
name|uptimeMillis
return|;
block|}
DECL|method|getStatus ()
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
block|}
block|}
end_class

end_unit

