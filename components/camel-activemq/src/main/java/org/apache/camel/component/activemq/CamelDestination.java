begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|QueueReceiver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|QueueSender
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicPublisher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicSubscriber
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
name|ActiveMQConnection
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
name|ActiveMQSession
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
name|CustomDestination
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
name|CamelContextAware
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
name|Endpoint
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
name|JmsBinding
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
name|JmsEndpoint
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|CamelDestination
specifier|public
class|class
name|CamelDestination
implements|implements
name|CustomDestination
implements|,
name|CamelContextAware
block|{
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
comment|// add in dummy endpoint pending camel release with
comment|// https://issues.apache.org/activemq/browse/CAMEL-1982
DECL|field|binding
specifier|private
name|JmsBinding
name|binding
init|=
operator|new
name|JmsBinding
argument_list|(
operator|new
name|JmsEndpoint
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|CamelDestination ()
specifier|public
name|CamelDestination
parameter_list|()
block|{     }
DECL|method|CamelDestination (String uri)
specifier|public
name|CamelDestination
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|uri
operator|.
name|toString
argument_list|()
return|;
block|}
comment|// CustomDestination interface
comment|// -----------------------------------------------------------------------
DECL|method|createConsumer (ActiveMQSession session, String messageSelector)
specifier|public
name|MessageConsumer
name|createConsumer
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|,
name|String
name|messageSelector
parameter_list|)
block|{
return|return
name|createConsumer
argument_list|(
name|session
argument_list|,
name|messageSelector
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|createConsumer (ActiveMQSession session, String messageSelector, boolean noLocal)
specifier|public
name|MessageConsumer
name|createConsumer
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|,
name|String
name|messageSelector
parameter_list|,
name|boolean
name|noLocal
parameter_list|)
block|{
return|return
operator|new
name|CamelMessageConsumer
argument_list|(
name|this
argument_list|,
name|resolveEndpoint
argument_list|(
name|session
argument_list|)
argument_list|,
name|session
argument_list|,
name|messageSelector
argument_list|,
name|noLocal
argument_list|)
return|;
block|}
DECL|method|createSubscriber (ActiveMQSession session, String messageSelector, boolean noLocal)
specifier|public
name|TopicSubscriber
name|createSubscriber
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|,
name|String
name|messageSelector
parameter_list|,
name|boolean
name|noLocal
parameter_list|)
block|{
return|return
name|createDurableSubscriber
argument_list|(
name|session
argument_list|,
literal|null
argument_list|,
name|messageSelector
argument_list|,
name|noLocal
argument_list|)
return|;
block|}
DECL|method|createDurableSubscriber (ActiveMQSession session, String name, String messageSelector, boolean noLocal)
specifier|public
name|TopicSubscriber
name|createDurableSubscriber
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|messageSelector
parameter_list|,
name|boolean
name|noLocal
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This destination is not a Topic: "
operator|+
name|this
argument_list|)
throw|;
block|}
DECL|method|createReceiver (ActiveMQSession session, String messageSelector)
specifier|public
name|QueueReceiver
name|createReceiver
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|,
name|String
name|messageSelector
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This destination is not a Queue: "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// Producers
comment|// -----------------------------------------------------------------------
DECL|method|createProducer (ActiveMQSession session)
specifier|public
name|MessageProducer
name|createProducer
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
operator|new
name|CamelMessageProducer
argument_list|(
name|this
argument_list|,
name|resolveEndpoint
argument_list|(
name|session
argument_list|)
argument_list|,
name|session
argument_list|)
return|;
block|}
DECL|method|createPublisher (ActiveMQSession session)
specifier|public
name|TopicPublisher
name|createPublisher
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This destination is not a Topic: "
operator|+
name|this
argument_list|)
throw|;
block|}
DECL|method|createSender (ActiveMQSession session)
specifier|public
name|QueueSender
name|createSender
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This destination is not a Queue: "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (Endpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getBinding ()
specifier|public
name|JmsBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
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
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
comment|/**      * Resolves the Camel Endpoint for this destination      *      * @return      */
DECL|method|resolveEndpoint (ActiveMQSession session)
specifier|protected
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|)
block|{
name|Endpoint
name|answer
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|resolveCamelContext
argument_list|(
name|session
argument_list|)
operator|.
name|getEndpoint
argument_list|(
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No endpoint could be found for URI: "
operator|+
name|getUri
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|resolveCamelContext (ActiveMQSession session)
specifier|protected
name|CamelContext
name|resolveCamelContext
parameter_list|(
name|ActiveMQSession
name|session
parameter_list|)
block|{
name|CamelContext
name|answer
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|ActiveMQConnection
name|connection
init|=
name|session
operator|.
name|getConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|connection
operator|instanceof
name|CamelConnection
condition|)
block|{
name|CamelConnection
name|camelConnection
init|=
operator|(
name|CamelConnection
operator|)
name|connection
decl_stmt|;
name|answer
operator|=
name|camelConnection
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No CamelContext has been configured"
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

