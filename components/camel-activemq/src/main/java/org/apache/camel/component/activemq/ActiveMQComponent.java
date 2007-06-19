begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.activemq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|component
operator|.
name|jms
operator|.
name|JmsComponent
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
name|jms
operator|.
name|JmsConfiguration
import|;
end_import

begin_comment
comment|/**  * The<a href="http://activemq.apache.org/camel/activemq.html">ActiveMQ Component</a>  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ActiveMQComponent
specifier|public
class|class
name|ActiveMQComponent
extends|extends
name|JmsComponent
block|{
comment|/**      * Creates an<a href="http://activemq.apache.org/camel/activemq.html">ActiveMQ Component</a>      *      * @return the created component      */
DECL|method|activeMQComponent ()
specifier|public
specifier|static
name|ActiveMQComponent
name|activeMQComponent
parameter_list|()
block|{
return|return
operator|new
name|ActiveMQComponent
argument_list|()
return|;
block|}
comment|/**      * Creates an<a href="http://activemq.apache.org/camel/activemq.html">ActiveMQ Component</a>      * connecting to the given<a href="http://activemq.apache.org/configuring-transports.html">broker URL</a>      *      * @param brokerURL the URL to connect to      * @return the created component      */
DECL|method|activeMQComponent (String brokerURL)
specifier|public
specifier|static
name|ActiveMQComponent
name|activeMQComponent
parameter_list|(
name|String
name|brokerURL
parameter_list|)
block|{
name|ActiveMQComponent
name|answer
init|=
operator|new
name|ActiveMQComponent
argument_list|()
decl_stmt|;
name|answer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBrokerURL
argument_list|(
name|brokerURL
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|ActiveMQComponent ()
specifier|public
name|ActiveMQComponent
parameter_list|()
block|{     }
DECL|method|ActiveMQComponent (CamelContext context)
specifier|public
name|ActiveMQComponent
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
DECL|method|ActiveMQComponent (ActiveMQConfiguration configuration)
specifier|public
name|ActiveMQComponent
parameter_list|(
name|ActiveMQConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|ActiveMQConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
operator|(
name|ActiveMQConfiguration
operator|)
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|setBrokerURL (String brokerURL)
specifier|public
name|void
name|setBrokerURL
parameter_list|(
name|String
name|brokerURL
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setBrokerURL
argument_list|(
name|brokerURL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConfiguration ()
specifier|protected
name|JmsConfiguration
name|createConfiguration
parameter_list|()
block|{
return|return
operator|new
name|ActiveMQConfiguration
argument_list|()
return|;
block|}
block|}
end_class

end_unit

