begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stomp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stomp
package|;
end_package

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
name|impl
operator|.
name|UriEndpointComponent
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
DECL|class|StompComponent
specifier|public
class|class
name|StompComponent
extends|extends
name|UriEndpointComponent
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
name|StompConfiguration
name|configuration
init|=
operator|new
name|StompConfiguration
argument_list|()
decl_stmt|;
DECL|field|brokerUrl
specifier|private
name|String
name|brokerUrl
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|login
specifier|private
name|String
name|login
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|passcode
specifier|private
name|String
name|passcode
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|method|StompComponent ()
specifier|public
name|StompComponent
parameter_list|()
block|{
name|super
argument_list|(
name|StompEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|destination
init|=
literal|"/"
operator|+
name|remaining
operator|.
name|replaceAll
argument_list|(
literal|":"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
comment|// must copy config so we do not have side effects
name|StompConfiguration
name|config
init|=
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// allow to configure configuration from uri parameters
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|StompEndpoint
name|endpoint
init|=
operator|new
name|StompEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|,
name|destination
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|StompConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * To use the shared stomp configuration      */
DECL|method|setConfiguration (StompConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|StompConfiguration
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
comment|/**      * The URI of the Stomp broker to connect to      */
DECL|method|setBrokerURL (String brokerURL)
specifier|public
name|void
name|setBrokerURL
parameter_list|(
name|String
name|brokerURL
parameter_list|)
block|{
name|configuration
operator|.
name|setBrokerURL
argument_list|(
name|brokerURL
argument_list|)
expr_stmt|;
block|}
comment|/**      * The username      */
DECL|method|setLogin (String login)
specifier|public
name|void
name|setLogin
parameter_list|(
name|String
name|login
parameter_list|)
block|{
name|configuration
operator|.
name|setLogin
argument_list|(
name|login
argument_list|)
expr_stmt|;
block|}
comment|/**      * The password      */
DECL|method|setPasscode (String passcode)
specifier|public
name|void
name|setPasscode
parameter_list|(
name|String
name|passcode
parameter_list|)
block|{
name|configuration
operator|.
name|setPasscode
argument_list|(
name|passcode
argument_list|)
expr_stmt|;
block|}
comment|/**      * The virtual host      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|configuration
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

