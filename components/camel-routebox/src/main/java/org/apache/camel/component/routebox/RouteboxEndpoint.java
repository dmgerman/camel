begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
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
name|impl
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriParam
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"routebox"
argument_list|,
name|syntax
operator|=
literal|"routebox:routeboxName"
argument_list|,
name|consumerClass
operator|=
name|RouteboxConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"eventbus"
argument_list|)
DECL|class|RouteboxEndpoint
specifier|public
specifier|abstract
class|class
name|RouteboxEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|config
name|RouteboxConfiguration
name|config
decl_stmt|;
DECL|method|RouteboxEndpoint ()
specifier|public
name|RouteboxEndpoint
parameter_list|()
block|{     }
annotation|@
name|Deprecated
DECL|method|RouteboxEndpoint (String endpointUri)
specifier|public
name|RouteboxEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|RouteboxEndpoint (String endpointUri, CamelContext camelContext)
specifier|public
name|RouteboxEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|RouteboxEndpoint (String endpointUri, Component component)
specifier|public
name|RouteboxEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|RouteboxEndpoint (String endpointUri, Component component, RouteboxConfiguration config)
specifier|public
name|RouteboxEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|RouteboxConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|RouteboxConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (RouteboxConfiguration config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|RouteboxConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
block|}
end_class

end_unit

