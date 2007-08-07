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
name|Message
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
name|PollingConsumer
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
name|DefaultEndpoint
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

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/jms.html">JMS Endpoint</a>  *   * @version $Revision:520964 $  */
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
block|{
DECL|field|binding
specifier|private
name|JmsBinding
name|binding
decl_stmt|;
DECL|field|destination
specifier|private
name|String
name|destination
decl_stmt|;
DECL|field|pubSubDomain
specifier|private
specifier|final
name|boolean
name|pubSubDomain
decl_stmt|;
DECL|field|selector
specifier|private
name|String
name|selector
decl_stmt|;
DECL|field|configuration
specifier|private
name|JmsConfiguration
name|configuration
decl_stmt|;
DECL|method|JmsEndpoint (String uri, JmsComponent component, String destination, boolean pubSubDomain, JmsConfiguration configuration)
specifier|public
name|JmsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JmsComponent
name|component
parameter_list|,
name|String
name|destination
parameter_list|,
name|boolean
name|pubSubDomain
parameter_list|,
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
name|this
operator|.
name|pubSubDomain
operator|=
name|pubSubDomain
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|JmsProducer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|JmsOperations
name|template
init|=
name|createJmsOperations
argument_list|()
decl_stmt|;
return|return
name|createProducer
argument_list|(
name|template
argument_list|)
return|;
block|}
comment|/**      * Creates a producer using the given template      */
DECL|method|createProducer (JmsOperations template)
specifier|public
name|JmsProducer
name|createProducer
parameter_list|(
name|JmsOperations
name|template
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|template
operator|instanceof
name|JmsTemplate
condition|)
block|{
name|JmsTemplate
name|jmsTemplate
init|=
operator|(
name|JmsTemplate
operator|)
name|template
decl_stmt|;
name|jmsTemplate
operator|.
name|setPubSubDomain
argument_list|(
name|pubSubDomain
argument_list|)
expr_stmt|;
name|jmsTemplate
operator|.
name|setDefaultDestinationName
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|JmsProducer
argument_list|(
name|this
argument_list|,
name|template
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|JmsConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|AbstractMessageListenerContainer
name|listenerContainer
init|=
name|configuration
operator|.
name|createMessageListenerContainer
argument_list|()
decl_stmt|;
return|return
name|createConsumer
argument_list|(
name|processor
argument_list|,
name|listenerContainer
argument_list|)
return|;
block|}
comment|/**      * Creates a consumer using the given processor and listener container      *       * @param processor the processor to use to process the messages      * @param listenerContainer the listener container      * @return a newly created consumer      * @throws Exception if the consumer cannot be created      */
DECL|method|createConsumer (Processor processor, AbstractMessageListenerContainer listenerContainer)
specifier|public
name|JmsConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|AbstractMessageListenerContainer
name|listenerContainer
parameter_list|)
throws|throws
name|Exception
block|{
name|listenerContainer
operator|.
name|setDestinationName
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|listenerContainer
operator|.
name|setPubSubDomain
argument_list|(
name|pubSubDomain
argument_list|)
expr_stmt|;
if|if
condition|(
name|selector
operator|!=
literal|null
condition|)
block|{
name|listenerContainer
operator|.
name|setMessageSelector
argument_list|(
name|selector
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|JmsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|listenerContainer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
argument_list|<
name|JmsExchange
argument_list|>
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|JmsOperations
name|template
init|=
name|createJmsOperations
argument_list|()
decl_stmt|;
return|return
operator|new
name|JmsPollingConsumer
argument_list|(
name|this
argument_list|,
name|template
argument_list|)
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
name|JmsExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
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
name|JmsExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|message
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getBinding ()
specifier|public
name|JmsBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|JmsBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * Sets the binding used to convert from a Camel message to and from a JMS      * message      *       * @param binding the binding to use      */
DECL|method|setBinding (JmsBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|JmsBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getDestination ()
specifier|public
name|String
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|JmsConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getSelector ()
specifier|public
name|String
name|getSelector
parameter_list|()
block|{
return|return
name|selector
return|;
block|}
comment|/**      * Sets the JMS selector to use      */
DECL|method|setSelector (String selector)
specifier|public
name|void
name|setSelector
parameter_list|(
name|String
name|selector
parameter_list|)
block|{
name|this
operator|.
name|selector
operator|=
name|selector
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|createJmsOperations ()
specifier|protected
name|JmsOperations
name|createJmsOperations
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|createJmsOperations
argument_list|(
name|pubSubDomain
argument_list|,
name|destination
argument_list|)
return|;
block|}
block|}
end_class

end_unit

