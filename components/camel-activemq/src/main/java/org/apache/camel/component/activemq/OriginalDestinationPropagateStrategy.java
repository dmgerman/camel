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
name|Message
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|command
operator|.
name|ActiveMQDestination
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
name|command
operator|.
name|ActiveMQMessage
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
name|component
operator|.
name|jms
operator|.
name|JmsMessage
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
name|MessageCreatedStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A strategy to enrich JMS message with their original destination if the Camel  * route originates from a JMS destination.  */
end_comment

begin_class
DECL|class|OriginalDestinationPropagateStrategy
specifier|public
class|class
name|OriginalDestinationPropagateStrategy
implements|implements
name|MessageCreatedStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|OriginalDestinationPropagateStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|onMessageCreated (Message message, Session session, Exchange exchange, Throwable cause)
specifier|public
name|void
name|onMessageCreated
parameter_list|(
name|Message
name|message
parameter_list|,
name|Session
name|session
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|instanceof
name|JmsMessage
condition|)
block|{
name|JmsMessage
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|JmsMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|jms
init|=
name|msg
operator|.
name|getJmsMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|jms
operator|!=
literal|null
operator|&&
name|jms
operator|instanceof
name|ActiveMQMessage
operator|&&
name|message
operator|instanceof
name|ActiveMQMessage
condition|)
block|{
name|ActiveMQMessage
name|amq
init|=
operator|(
name|ActiveMQMessage
operator|)
name|jms
decl_stmt|;
if|if
condition|(
name|amq
operator|.
name|getOriginalDestination
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ActiveMQDestination
name|from
init|=
name|amq
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|from
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setting OriginalDestination: {} on {}"
argument_list|,
name|from
argument_list|,
name|message
argument_list|)
expr_stmt|;
operator|(
operator|(
name|ActiveMQMessage
operator|)
name|message
operator|)
operator|.
name|setOriginalDestination
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

