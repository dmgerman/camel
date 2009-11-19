begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
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
name|FailedToCreateConsumerException
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
name|Processor
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
name|SuspendableService
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
name|DefaultConsumer
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
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|support
operator|.
name|JmsUtils
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.Consumer} which uses Spring's {@link AbstractMessageListenerContainer} implementations to consume JMS messages  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsConsumer
specifier|public
class|class
name|JmsConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|SuspendableService
block|{
DECL|field|listenerContainer
specifier|private
name|AbstractMessageListenerContainer
name|listenerContainer
decl_stmt|;
DECL|field|messageListener
specifier|private
name|EndpointMessageListener
name|messageListener
decl_stmt|;
DECL|field|initialized
specifier|private
name|boolean
name|initialized
decl_stmt|;
DECL|field|suspended
specifier|private
name|boolean
name|suspended
decl_stmt|;
DECL|method|JmsConsumer (JmsEndpoint endpoint, Processor processor, AbstractMessageListenerContainer listenerContainer)
specifier|public
name|JmsConsumer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AbstractMessageListenerContainer
name|listenerContainer
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
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
name|getEndpointMessageListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|JmsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|JmsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getListenerContainer ()
specifier|public
name|AbstractMessageListenerContainer
name|getListenerContainer
parameter_list|()
block|{
if|if
condition|(
name|listenerContainer
operator|==
literal|null
condition|)
block|{
name|createMessageListenerContainer
argument_list|()
expr_stmt|;
block|}
return|return
name|listenerContainer
return|;
block|}
DECL|method|getEndpointMessageListener ()
specifier|public
name|EndpointMessageListener
name|getEndpointMessageListener
parameter_list|()
block|{
if|if
condition|(
name|messageListener
operator|==
literal|null
condition|)
block|{
name|createMessageListener
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|messageListener
return|;
block|}
DECL|method|createMessageListener (JmsEndpoint endpoint, Processor processor)
specifier|protected
name|void
name|createMessageListener
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|messageListener
operator|=
operator|new
name|EndpointMessageListener
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|messageListener
operator|.
name|setBinding
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createMessageListenerContainer ()
specifier|protected
name|void
name|createMessageListenerContainer
parameter_list|()
block|{
name|listenerContainer
operator|=
name|getEndpoint
argument_list|()
operator|.
name|createMessageListenerContainer
argument_list|()
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|configureListenerContainer
argument_list|(
name|listenerContainer
argument_list|)
expr_stmt|;
name|listenerContainer
operator|.
name|setMessageListener
argument_list|(
name|getEndpointMessageListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Starts the JMS listener container      *<p/>      * Can be used to start this consumer later if it was configured to not auto startup.      */
DECL|method|startListenerContainer ()
specifier|public
name|void
name|startListenerContainer
parameter_list|()
block|{
name|listenerContainer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|/**      * Pre tests the connection before starting the listening.      *<p/>      * In case of connection failure the exception is thrown which prevents Camel from starting.      *      * @throws FailedToCreateConsumerException is thrown if testing the connection failed      */
DECL|method|testConnectionOnStartup ()
specifier|protected
name|void
name|testConnectionOnStartup
parameter_list|()
throws|throws
name|FailedToCreateConsumerException
block|{
try|try
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
literal|"Testing JMS Connection on startup for destination: "
operator|+
name|getDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Connection
name|con
init|=
name|listenerContainer
operator|.
name|getConnectionFactory
argument_list|()
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|JmsUtils
operator|.
name|closeConnection
argument_list|(
name|con
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Successfully tested JMS Connection on startup for destination: "
operator|+
name|getDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Cannot get JMS Connection on startup for destination "
operator|+
name|getDestinationName
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|FailedToCreateConsumerException
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// create listener container
if|if
condition|(
name|listenerContainer
operator|==
literal|null
condition|)
block|{
name|createMessageListenerContainer
argument_list|()
expr_stmt|;
block|}
name|listenerContainer
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
comment|// only start listener if auto start is enabled or we are explicit invoking start later
if|if
condition|(
name|initialized
operator|||
name|getEndpoint
argument_list|()
operator|.
name|isAutoStartup
argument_list|()
condition|)
block|{
comment|// should we pre test connections before starting?
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isTestConnectionOnStartup
argument_list|()
condition|)
block|{
name|testConnectionOnStartup
argument_list|()
expr_stmt|;
block|}
name|startListenerContainer
argument_list|()
expr_stmt|;
block|}
comment|// mark as initialized for the first time
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
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
comment|// null container and listener so they are fully re created if this consumer is restarted
comment|// then we will use updated configuration from jms endpoint that may have been managed using JMX
name|listenerContainer
operator|=
literal|null
expr_stmt|;
name|messageListener
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|suspend ()
specifier|public
name|void
name|suspend
parameter_list|()
block|{
if|if
condition|(
name|listenerContainer
operator|!=
literal|null
condition|)
block|{
name|listenerContainer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|suspended
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|resume ()
specifier|public
name|void
name|resume
parameter_list|()
block|{
if|if
condition|(
name|listenerContainer
operator|!=
literal|null
condition|)
block|{
name|startListenerContainer
argument_list|()
expr_stmt|;
name|suspended
operator|=
literal|false
expr_stmt|;
block|}
block|}
DECL|method|isSuspended ()
specifier|public
name|boolean
name|isSuspended
parameter_list|()
block|{
return|return
name|suspended
return|;
block|}
DECL|method|getDestinationName ()
specifier|private
name|String
name|getDestinationName
parameter_list|()
block|{
if|if
condition|(
name|listenerContainer
operator|.
name|getDestination
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|listenerContainer
operator|.
name|getDestination
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|listenerContainer
operator|.
name|getDestinationName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

