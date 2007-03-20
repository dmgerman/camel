begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jms
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
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsOperations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|MessageCreator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|AbstractMessageListenerContainer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsEndpoint
specifier|public
class|class
name|JmsEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|JmsExchange
argument_list|>
implements|implements
name|MessageListener
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JmsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|template
specifier|private
name|JmsOperations
name|template
decl_stmt|;
DECL|field|listenerContainer
specifier|private
name|AbstractMessageListenerContainer
name|listenerContainer
decl_stmt|;
DECL|field|destination
specifier|private
name|String
name|destination
decl_stmt|;
DECL|method|JmsEndpoint (String endpointUri, CamelContext container, String destination, JmsOperations template, AbstractMessageListenerContainer listenerContainer)
specifier|public
name|JmsEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|container
parameter_list|,
name|String
name|destination
parameter_list|,
name|JmsOperations
name|template
parameter_list|,
name|AbstractMessageListenerContainer
name|listenerContainer
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|container
argument_list|)
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
name|this
operator|.
name|listenerContainer
operator|=
name|listenerContainer
expr_stmt|;
name|this
operator|.
name|listenerContainer
operator|.
name|setMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|JmsEndpoint
operator|.
name|this
operator|+
literal|" receiving JMS message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
name|JmsExchange
name|exchange
init|=
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|getInboundProcessor
argument_list|()
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|send (Exchange exchange)
specifier|public
name|void
name|send
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// lets convert to the type of an exchange
name|JmsExchange
name|jmsExchange
init|=
name|convertTo
argument_list|(
name|JmsExchange
operator|.
name|class
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|onExchange
argument_list|(
name|jmsExchange
argument_list|)
expr_stmt|;
block|}
DECL|method|onExchange (final JmsExchange exchange)
specifier|public
name|void
name|onExchange
parameter_list|(
specifier|final
name|JmsExchange
name|exchange
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
name|destination
argument_list|,
operator|new
name|MessageCreator
argument_list|()
block|{
specifier|public
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|createMessage
argument_list|(
name|session
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|JmsEndpoint
operator|.
name|this
operator|+
literal|" sending JMS message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|message
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getTemplate ()
specifier|public
name|JmsOperations
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|JmsExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|DefaultJmsExchange
argument_list|(
name|getContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (Message message)
specifier|public
name|JmsExchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|DefaultJmsExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|message
argument_list|)
return|;
block|}
DECL|method|doActivate ()
specifier|protected
name|void
name|doActivate
parameter_list|()
block|{
name|super
operator|.
name|doActivate
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|doDeactivate ()
specifier|protected
name|void
name|doDeactivate
parameter_list|()
block|{
name|listenerContainer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|super
operator|.
name|doDeactivate
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

