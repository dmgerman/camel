begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.artemis.amqp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|artemis
operator|.
name|amqp
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
name|artemis
operator|.
name|jms
operator|.
name|server
operator|.
name|embedded
operator|.
name|EmbeddedJMS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|artemis
operator|.
name|spi
operator|.
name|core
operator|.
name|security
operator|.
name|ActiveMQJAASSecurityManager
import|;
end_import

begin_comment
comment|// import org.apache.activemq.artemis.core.config.impl.SecurityConfiguration;
end_comment

begin_comment
comment|// import org.apache.activemq.artemis.spi.core.security.jaas.InVMLoginModule;
end_comment

begin_comment
comment|//#################################################
end_comment

begin_comment
comment|// Blueprint does not support Bean inheritance (necessary for Artemis EmbeddedJMS)
end_comment

begin_comment
comment|// We need therefore a 'support' class
end_comment

begin_comment
comment|//#################################################
end_comment

begin_class
DECL|class|EmbeddedBrokerSupport
specifier|public
class|class
name|EmbeddedBrokerSupport
extends|extends
name|EmbeddedJMS
block|{
DECL|method|EmbeddedBrokerSupport (ActiveMQJAASSecurityManager securityManager)
specifier|public
name|EmbeddedBrokerSupport
parameter_list|(
name|ActiveMQJAASSecurityManager
name|securityManager
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|setSecurityManager
argument_list|(
name|securityManager
argument_list|)
expr_stmt|;
name|this
operator|.
name|start
argument_list|()
expr_stmt|;
comment|//if you need more twicking use Java to customise as follows:
comment|// SecurityConfiguration securityConfig = new SecurityConfiguration();
comment|// securityConfig.addUser("guest", "guest");
comment|// securityConfig.addRole("guest", "guest");
comment|// securityConfig.setDefaultUser("guest");
comment|// ActiveMQJAASSecurityManager securityManager = new ActiveMQJAASSecurityManager(InVMLoginModule.class.getName(), securityConfig);
comment|// this.setSecurityManager(securityManager);
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

