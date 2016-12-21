begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mqtt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mqtt
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

begin_comment
comment|/**  * MQTT Component  */
end_comment

begin_class
DECL|class|MQTTComponent
specifier|public
class|class
name|MQTTComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|host
specifier|private
name|String
name|host
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
DECL|field|userName
specifier|private
name|String
name|userName
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
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|method|MQTTComponent ()
specifier|public
name|MQTTComponent
parameter_list|()
block|{
name|super
argument_list|(
name|MQTTEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|MQTTConfiguration
name|configuration
init|=
operator|new
name|MQTTConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|userName
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
comment|// and then override from parameters
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|MQTTEndpoint
name|endpoint
init|=
operator|new
name|MQTTEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|useRawUri ()
specifier|public
name|boolean
name|useRawUri
parameter_list|()
block|{
return|return
literal|true
return|;
comment|// to prevent MQTT "+" wildcard from being lost
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * The URI of the MQTT broker to connect too - this component also supports SSL - e.g. ssl://127.0.0.1:8883      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
comment|/**      * Username to be used for authentication against the MQTT broker      */
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * Password to be used for authentication against the MQTT broker      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
block|}
end_class

end_unit

