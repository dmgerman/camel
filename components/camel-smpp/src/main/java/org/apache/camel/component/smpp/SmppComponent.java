begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|spi
operator|.
name|annotations
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
name|support
operator|.
name|DefaultComponent
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
name|Metadata
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"smpp,smpps"
argument_list|)
DECL|class|SmppComponent
specifier|public
class|class
name|SmppComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
DECL|method|SmppComponent ()
specifier|public
name|SmppComponent
parameter_list|()
block|{     }
DECL|method|SmppComponent (SmppConfiguration configuration)
specifier|public
name|SmppComponent
parameter_list|(
name|SmppConfiguration
name|configuration
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|SmppComponent (CamelContext context)
specifier|public
name|SmppComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|configuration
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|SmppConfiguration
argument_list|()
expr_stmt|;
block|}
comment|// create a copy of the configuration as other endpoints can adjust their copy as well
name|SmppConfiguration
name|config
init|=
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|config
operator|.
name|configureFromURI
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|config
argument_list|)
return|;
block|}
comment|/**      * Create a new smpp endpoint with the provided smpp configuration      */
DECL|method|createEndpoint (SmppConfiguration config)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|SmppConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createEndpoint
argument_list|(
literal|null
argument_list|,
name|config
argument_list|)
return|;
block|}
comment|/**      * Create a new smpp endpoint with the provided uri and smpp configuration      */
DECL|method|createEndpoint (String uri, SmppConfiguration config)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|SmppEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|SmppConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * To use the shared SmppConfiguration as configuration.      */
DECL|method|setConfiguration (SmppConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SmppConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
block|}
end_class

end_unit

