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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|DefaultProducer
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
name|MqttMessage
import|;
end_import

begin_class
DECL|class|PahoProducer
specifier|public
class|class
name|PahoProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|PahoProducer (PahoEndpoint endpoint)
specifier|public
name|PahoProducer
parameter_list|(
name|PahoEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|retrieveQos (Exchange exchange)
specifier|private
name|int
name|retrieveQos
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|PahoConstants
operator|.
name|CAMEL_PAHO_MSG_QOS
argument_list|)
condition|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PahoConstants
operator|.
name|CAMEL_PAHO_MSG_QOS
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getQos
argument_list|()
return|;
block|}
block|}
DECL|method|retrieveRetained (Exchange exchange)
specifier|private
name|boolean
name|retrieveRetained
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|PahoConstants
operator|.
name|CAMEL_PAHO_MSG_RETAINED
argument_list|)
condition|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PahoConstants
operator|.
name|CAMEL_PAHO_MSG_RETAINED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isRetained
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|MqttClient
name|client
init|=
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|String
name|topic
init|=
name|getEndpoint
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
name|int
name|qos
init|=
name|retrieveQos
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|boolean
name|retained
init|=
name|retrieveRetained
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|byte
index|[]
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|MqttMessage
name|message
init|=
operator|new
name|MqttMessage
argument_list|(
name|payload
argument_list|)
decl_stmt|;
name|message
operator|.
name|setQos
argument_list|(
name|qos
argument_list|)
expr_stmt|;
name|message
operator|.
name|setRetained
argument_list|(
name|retained
argument_list|)
expr_stmt|;
name|client
operator|.
name|publish
argument_list|(
name|topic
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|PahoEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|PahoEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

