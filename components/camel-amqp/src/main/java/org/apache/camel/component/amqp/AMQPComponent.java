begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.amqp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|amqp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|qpid
operator|.
name|jms
operator|.
name|JmsConnectionFactory
import|;
end_import

begin_comment
comment|/**  * Messaging with AMQP protocol using Apache QPid Client.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"amqp"
argument_list|)
DECL|class|AMQPComponent
specifier|public
class|class
name|AMQPComponent
extends|extends
name|JmsComponent
block|{
comment|// Constructors
DECL|method|AMQPComponent ()
specifier|public
name|AMQPComponent
parameter_list|()
block|{     }
DECL|method|AMQPComponent (JmsConfiguration configuration)
specifier|public
name|AMQPComponent
parameter_list|(
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
DECL|method|AMQPComponent (CamelContext context)
specifier|public
name|AMQPComponent
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
DECL|method|AMQPComponent (ConnectionFactory connectionFactory)
specifier|public
name|AMQPComponent
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
comment|// Life-cycle
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|AMQPConnectionDetails
argument_list|>
name|connectionDetails
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|AMQPConnectionDetails
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionDetails
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|AMQPConnectionDetails
name|details
init|=
name|connectionDetails
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|JmsConnectionFactory
name|connectionFactory
init|=
operator|new
name|JmsConnectionFactory
argument_list|(
name|details
operator|.
name|username
argument_list|()
argument_list|,
name|details
operator|.
name|password
argument_list|()
argument_list|,
name|details
operator|.
name|uri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|details
operator|.
name|setTopicPrefix
argument_list|()
condition|)
block|{
name|connectionFactory
operator|.
name|setTopicPrefix
argument_list|(
literal|"topic://"
argument_list|)
expr_stmt|;
block|}
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
comment|// Factory methods
DECL|method|amqpComponent (String uri)
specifier|public
specifier|static
name|AMQPComponent
name|amqpComponent
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|JmsConnectionFactory
name|connectionFactory
init|=
operator|new
name|JmsConnectionFactory
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|connectionFactory
operator|.
name|setTopicPrefix
argument_list|(
literal|"topic://"
argument_list|)
expr_stmt|;
return|return
operator|new
name|AMQPComponent
argument_list|(
name|connectionFactory
argument_list|)
return|;
block|}
DECL|method|amqpComponent (String uri, String username, String password)
specifier|public
specifier|static
name|AMQPComponent
name|amqpComponent
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|JmsConnectionFactory
name|connectionFactory
init|=
operator|new
name|JmsConnectionFactory
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
name|uri
argument_list|)
decl_stmt|;
name|connectionFactory
operator|.
name|setTopicPrefix
argument_list|(
literal|"topic://"
argument_list|)
expr_stmt|;
return|return
operator|new
name|AMQPComponent
argument_list|(
name|connectionFactory
argument_list|)
return|;
block|}
block|}
end_class

end_unit

