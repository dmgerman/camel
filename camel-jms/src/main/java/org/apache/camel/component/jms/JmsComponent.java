begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|sun
operator|.
name|jndi
operator|.
name|toolkit
operator|.
name|url
operator|.
name|Uri
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
name|impl
operator|.
name|DefaultComponent
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
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsTemplate
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
name|listener
operator|.
name|DefaultMessageListenerContainer
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
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_comment
comment|/**  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|JmsComponent
specifier|public
class|class
name|JmsComponent
extends|extends
name|DefaultComponent
argument_list|<
name|JmsExchange
argument_list|>
block|{
DECL|field|QUEUE_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|QUEUE_PREFIX
init|=
literal|"queue/"
decl_stmt|;
DECL|field|TOPIC_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_PREFIX
init|=
literal|"topic/"
decl_stmt|;
DECL|field|template
specifier|private
name|JmsTemplate
name|template
decl_stmt|;
comment|/**      * Static builder method      */
DECL|method|jmsComponent ()
specifier|public
specifier|static
name|JmsComponent
name|jmsComponent
parameter_list|()
block|{
return|return
operator|new
name|JmsComponent
argument_list|()
return|;
block|}
comment|/**      * Static builder method      */
DECL|method|jmsComponent (JmsTemplate template)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponent
parameter_list|(
name|JmsTemplate
name|template
parameter_list|)
block|{
return|return
operator|new
name|JmsComponent
argument_list|(
name|template
argument_list|)
return|;
block|}
comment|/**      * Static builder method      */
DECL|method|jmsComponent (ConnectionFactory connectionFactory)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponent
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
return|return
name|jmsComponent
argument_list|(
operator|new
name|JmsTemplate
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Static builder method      */
DECL|method|jmsComponentClientAcknowledge (ConnectionFactory connectionFactory)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponentClientAcknowledge
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|JmsTemplate
name|template
init|=
operator|new
name|JmsTemplate
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|template
operator|.
name|setSessionTransacted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|template
operator|.
name|setSessionAcknowledgeMode
argument_list|(
name|Session
operator|.
name|CLIENT_ACKNOWLEDGE
argument_list|)
expr_stmt|;
return|return
name|jmsComponent
argument_list|(
name|template
argument_list|)
return|;
block|}
DECL|method|JmsComponent ()
specifier|protected
name|JmsComponent
parameter_list|()
block|{
name|this
operator|.
name|template
operator|=
operator|new
name|JmsTemplate
argument_list|()
expr_stmt|;
block|}
DECL|method|JmsComponent (JmsTemplate template)
specifier|protected
name|JmsComponent
parameter_list|(
name|JmsTemplate
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
DECL|method|JmsComponent (CamelContext context)
specifier|public
name|JmsComponent
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
name|this
operator|.
name|template
operator|=
operator|new
name|JmsTemplate
argument_list|()
expr_stmt|;
block|}
DECL|method|createEndpoint (Uri uri)
specifier|public
name|JmsEndpoint
name|createEndpoint
parameter_list|(
name|Uri
name|uri
parameter_list|)
block|{
comment|// lets figure out from the URI whether its a queue, topic etc
name|String
name|path
init|=
name|uri
operator|.
name|getPath
argument_list|()
decl_stmt|;
return|return
name|createEndpoint
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|,
name|path
argument_list|)
return|;
block|}
DECL|method|createEndpoint (String uri, String path)
specifier|public
name|JmsEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getContext
argument_list|()
argument_list|,
literal|"container"
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|QUEUE_PREFIX
argument_list|)
condition|)
block|{
name|template
operator|.
name|setPubSubDomain
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|QUEUE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|TOPIC_PREFIX
argument_list|)
condition|)
block|{
name|template
operator|.
name|setPubSubDomain
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|TOPIC_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|subject
init|=
name|convertPathToActualDestination
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|template
operator|.
name|setDefaultDestinationName
argument_list|(
name|subject
argument_list|)
expr_stmt|;
comment|/*         Destination destination = (Destination) template.execute(new SessionCallback() {             public Object doInJms(Session session) throws JMSException {                 return template.getDestinationResolver().resolveDestinationName(session, subject, template.isPubSubDomain());             }         });         */
name|AbstractMessageListenerContainer
name|listenerContainer
init|=
name|createMessageListenerContainer
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|listenerContainer
operator|.
name|setDestinationName
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|listenerContainer
operator|.
name|setPubSubDomain
argument_list|(
name|template
operator|.
name|isPubSubDomain
argument_list|()
argument_list|)
expr_stmt|;
name|listenerContainer
operator|.
name|setConnectionFactory
argument_list|(
name|template
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO support optional parameters
comment|// selector
comment|// messageConverter
comment|// durableSubscriberName
return|return
operator|new
name|JmsEndpoint
argument_list|(
name|uri
argument_list|,
name|getContext
argument_list|()
argument_list|,
name|subject
argument_list|,
name|template
argument_list|,
name|listenerContainer
argument_list|)
return|;
block|}
DECL|method|getTemplate ()
specifier|public
name|JmsTemplate
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|setTemplate (JmsTemplate template)
specifier|public
name|void
name|setTemplate
parameter_list|(
name|JmsTemplate
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
DECL|method|createMessageListenerContainer (JmsTemplate template)
specifier|protected
name|AbstractMessageListenerContainer
name|createMessageListenerContainer
parameter_list|(
name|JmsTemplate
name|template
parameter_list|)
block|{
comment|// TODO use an enum to auto-switch container types?
comment|//return new SimpleMessageListenerContainer();
return|return
operator|new
name|DefaultMessageListenerContainer
argument_list|()
return|;
block|}
comment|/**      * A strategy method allowing the URI destination to be translated into the actual JMS destination name      * (say by looking up in JNDI or something)      */
DECL|method|convertPathToActualDestination (String path)
specifier|protected
name|String
name|convertPathToActualDestination
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|path
return|;
block|}
DECL|method|activate (JmsEndpoint endpoint, Processor<JmsExchange> processor)
specifier|public
name|void
name|activate
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|Processor
argument_list|<
name|JmsExchange
argument_list|>
name|processor
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
DECL|method|deactivate (JmsEndpoint endpoint)
specifier|public
name|void
name|deactivate
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
end_class

end_unit

