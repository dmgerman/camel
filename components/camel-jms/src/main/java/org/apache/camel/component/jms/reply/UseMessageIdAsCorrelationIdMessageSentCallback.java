begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.reply
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
operator|.
name|reply
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
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
name|Session
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
name|MessageSentCallback
import|;
end_import

begin_import
import|import static
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
name|JmsMessageHelper
operator|.
name|getJMSMessageID
import|;
end_import

begin_comment
comment|/**  * Callback to be used when using the option<tt>useMessageIDAsCorrelationID</tt>.  *<p/>  * This callback will keep the correlation registration in {@link ReplyManager} up-to-date with  * the<tt>JMSMessageID</tt> which was assigned and used when the message was sent.  *  * @version   */
end_comment

begin_class
DECL|class|UseMessageIdAsCorrelationIdMessageSentCallback
specifier|public
class|class
name|UseMessageIdAsCorrelationIdMessageSentCallback
implements|implements
name|MessageSentCallback
block|{
DECL|field|replyManager
specifier|private
name|ReplyManager
name|replyManager
decl_stmt|;
DECL|field|correlationId
specifier|private
name|String
name|correlationId
decl_stmt|;
DECL|field|requestTimeout
specifier|private
name|long
name|requestTimeout
decl_stmt|;
DECL|method|UseMessageIdAsCorrelationIdMessageSentCallback (ReplyManager replyManager, String correlationId, long requestTimeout)
specifier|public
name|UseMessageIdAsCorrelationIdMessageSentCallback
parameter_list|(
name|ReplyManager
name|replyManager
parameter_list|,
name|String
name|correlationId
parameter_list|,
name|long
name|requestTimeout
parameter_list|)
block|{
name|this
operator|.
name|replyManager
operator|=
name|replyManager
expr_stmt|;
name|this
operator|.
name|correlationId
operator|=
name|correlationId
expr_stmt|;
name|this
operator|.
name|requestTimeout
operator|=
name|requestTimeout
expr_stmt|;
block|}
DECL|method|sent (Session session, Message message, Destination destination)
specifier|public
name|void
name|sent
parameter_list|(
name|Session
name|session
parameter_list|,
name|Message
name|message
parameter_list|,
name|Destination
name|destination
parameter_list|)
block|{
name|String
name|newCorrelationID
init|=
name|getJMSMessageID
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|newCorrelationID
operator|!=
literal|null
condition|)
block|{
name|replyManager
operator|.
name|updateCorrelationId
argument_list|(
name|correlationId
argument_list|,
name|newCorrelationID
argument_list|,
name|requestTimeout
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

