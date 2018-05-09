begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.paho
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|paho
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|DefaultComponentVerifierExtension
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|ResultBuilder
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|ResultErrorBuilder
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|ResultErrorHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|paho
operator|.
name|client
operator|.
name|mqttv3
operator|.
name|MqttClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|paho
operator|.
name|client
operator|.
name|mqttv3
operator|.
name|MqttConnectOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|paho
operator|.
name|client
operator|.
name|mqttv3
operator|.
name|MqttException
import|;
end_import

begin_class
DECL|class|PahoComponentVerifierExtension
specifier|public
class|class
name|PahoComponentVerifierExtension
extends|extends
name|DefaultComponentVerifierExtension
block|{
DECL|method|PahoComponentVerifierExtension ()
specifier|public
name|PahoComponentVerifierExtension
parameter_list|()
block|{
name|this
argument_list|(
literal|"paho"
argument_list|)
expr_stmt|;
block|}
DECL|method|PahoComponentVerifierExtension (String scheme)
specifier|public
name|PahoComponentVerifierExtension
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|super
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
comment|// *********************************
comment|// Parameters validation
comment|// *********************************
annotation|@
name|Override
DECL|method|verifyParameters (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|PARAMETERS
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresOption
argument_list|(
literal|"brokerUrl"
argument_list|,
name|parameters
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|// *********************************
comment|// Connectivity validation
comment|// *********************************
annotation|@
name|Override
DECL|method|verifyConnectivity (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyConnectivity
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|CONNECTIVITY
argument_list|)
operator|.
name|error
argument_list|(
name|parameters
argument_list|,
name|this
operator|::
name|verifyCredentials
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|verifyCredentials (ResultBuilder builder, Map<String, Object> parameters)
specifier|private
name|void
name|verifyCredentials
parameter_list|(
name|ResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|String
name|brokerUrl
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"brokerUrl"
argument_list|)
decl_stmt|;
name|String
name|username
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"userName"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|brokerUrl
argument_list|)
condition|)
block|{
try|try
block|{
comment|// Create MQTT client
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|username
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|password
argument_list|)
condition|)
block|{
name|MqttClient
name|client
init|=
operator|new
name|MqttClient
argument_list|(
name|brokerUrl
argument_list|,
name|MqttClient
operator|.
name|generateClientId
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|connect
argument_list|()
expr_stmt|;
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|MqttClient
name|client
init|=
operator|new
name|MqttClient
argument_list|(
name|brokerUrl
argument_list|,
name|MqttClient
operator|.
name|generateClientId
argument_list|()
argument_list|)
decl_stmt|;
name|MqttConnectOptions
name|connOpts
init|=
operator|new
name|MqttConnectOptions
argument_list|()
decl_stmt|;
name|connOpts
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|connOpts
operator|.
name|setPassword
argument_list|(
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|connect
argument_list|(
name|connOpts
argument_list|)
expr_stmt|;
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MqttException
name|e
parameter_list|)
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withCodeAndDescription
argument_list|(
name|VerificationError
operator|.
name|StandardCode
operator|.
name|ILLEGAL_PARAMETER_VALUE
argument_list|,
literal|"Unable to connect to MQTT broker"
argument_list|)
operator|.
name|parameterKey
argument_list|(
literal|"brokerUrl"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withCodeAndDescription
argument_list|(
name|VerificationError
operator|.
name|StandardCode
operator|.
name|ILLEGAL_PARAMETER_VALUE
argument_list|,
literal|"Invalid blank MQTT brokerUrl "
argument_list|)
operator|.
name|parameterKey
argument_list|(
literal|"brokerUrl"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

