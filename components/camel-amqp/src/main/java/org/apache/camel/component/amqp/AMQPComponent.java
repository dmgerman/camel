begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|qpid
operator|.
name|client
operator|.
name|AMQConnectionFactory
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
name|url
operator|.
name|URLSyntaxException
import|;
end_import

begin_comment
comment|/**  * This component supports the AMQP protocol using the Client API of the Apache Qpid project.  *   * @version   */
end_comment

begin_class
DECL|class|AMQPComponent
specifier|public
class|class
name|AMQPComponent
extends|extends
name|JmsComponent
block|{
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
DECL|method|AMQPComponent (AMQConnectionFactory connectionFactory)
specifier|public
name|AMQPComponent
parameter_list|(
name|AMQConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|amqpComponent (String uri)
specifier|public
specifier|static
name|Component
name|amqpComponent
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URLSyntaxException
block|{
name|AMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|AMQConnectionFactory
argument_list|(
name|uri
argument_list|)
decl_stmt|;
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

