begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jmxconnect.provider.activemq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jmxconnect
operator|.
name|provider
operator|.
name|activemq
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|camel
operator|.
name|component
operator|.
name|ActiveMQComponent
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
name|jmxconnect
operator|.
name|CamelJmxConnectorSupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ActiveMQHelper
specifier|public
specifier|final
class|class
name|ActiveMQHelper
block|{
DECL|method|ActiveMQHelper ()
specifier|private
name|ActiveMQHelper
parameter_list|()
block|{
comment|// helper class
block|}
DECL|method|getDefaultEndpointUri ()
specifier|public
specifier|static
name|String
name|getDefaultEndpointUri
parameter_list|()
block|{
return|return
literal|"activemq:"
operator|+
name|CamelJmxConnectorSupport
operator|.
name|DEFAULT_DESTINATION_PREFIX
return|;
block|}
DECL|method|configureActiveMQComponent (CamelContext camelContext, String brokerUrl)
specifier|public
specifier|static
name|void
name|configureActiveMQComponent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|brokerUrl
parameter_list|)
block|{
name|ActiveMQComponent
name|activemqComponent
init|=
name|camelContext
operator|.
name|getComponent
argument_list|(
literal|"activemq"
argument_list|,
name|ActiveMQComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|activemqComponent
operator|.
name|setBrokerURL
argument_list|(
name|brokerUrl
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

