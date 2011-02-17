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
name|AsyncCallback
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

begin_comment
comment|/**  * Holder which contains the {@link Exchange} and {@link org.apache.camel.AsyncCallback} to be used  * when the reply arrives, so we can set the reply on the {@link Exchange} and continue routing using the callback.  *  * @version   */
end_comment

begin_class
DECL|class|ReplyHolder
specifier|public
class|class
name|ReplyHolder
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|message
specifier|private
specifier|final
name|Message
name|message
decl_stmt|;
DECL|field|originalCorrelationId
specifier|private
specifier|final
name|String
name|originalCorrelationId
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
comment|/**      * Constructor to use when a reply message was received      */
DECL|method|ReplyHolder (Exchange exchange, AsyncCallback callback, String originalCorrelationId, Message message)
specifier|public
name|ReplyHolder
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|String
name|originalCorrelationId
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|originalCorrelationId
operator|=
name|originalCorrelationId
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
comment|/**      * Constructor to use when a timeout occurred      */
DECL|method|ReplyHolder (Exchange exchange, AsyncCallback callback, String originalCorrelationId, long timeout)
specifier|public
name|ReplyHolder
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|String
name|originalCorrelationId
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|originalCorrelationId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
DECL|method|getCallback ()
specifier|public
name|AsyncCallback
name|getCallback
parameter_list|()
block|{
return|return
name|callback
return|;
block|}
comment|/**      * Gets the original correlation id, if one was set when sending the message.      *<p/>      * Some JMS brokers will mess with the correlation id and send back a different/empty correlation id.      * So we need to remember it so we can restore the correlation id.      */
DECL|method|getOriginalCorrelationId ()
specifier|public
name|String
name|getOriginalCorrelationId
parameter_list|()
block|{
return|return
name|originalCorrelationId
return|;
block|}
comment|/**      * Gets the received message      *      * @return  the received message, or<tt>null</tt> if timeout occurred and no message has been received      * @see #isTimeout()      */
DECL|method|getMessage ()
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
comment|/**      * Whether timeout triggered or not.      *<p/>      * A timeout is triggered if<tt>requestTimeout</tt> option has been configured, and a reply message has<b>not</b> been      * received within that time frame.      */
DECL|method|isTimeout ()
specifier|public
name|boolean
name|isTimeout
parameter_list|()
block|{
return|return
name|message
operator|==
literal|null
return|;
block|}
comment|/**      * The timeout value      */
DECL|method|getRequestTimeout ()
specifier|public
name|long
name|getRequestTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
block|}
end_class

end_unit

