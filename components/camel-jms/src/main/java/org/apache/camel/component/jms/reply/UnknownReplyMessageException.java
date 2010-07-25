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
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * A reply message which cannot be correlated to a match request message.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|UnknownReplyMessageException
specifier|public
class|class
name|UnknownReplyMessageException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|replyMessage
specifier|private
specifier|final
name|Message
name|replyMessage
decl_stmt|;
DECL|field|correlationId
specifier|private
specifier|final
name|String
name|correlationId
decl_stmt|;
DECL|method|UnknownReplyMessageException (String text, Message replyMessage, String correlationId)
specifier|public
name|UnknownReplyMessageException
parameter_list|(
name|String
name|text
parameter_list|,
name|Message
name|replyMessage
parameter_list|,
name|String
name|correlationId
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|this
operator|.
name|replyMessage
operator|=
name|replyMessage
expr_stmt|;
name|this
operator|.
name|correlationId
operator|=
name|correlationId
expr_stmt|;
block|}
comment|/**      * The unknown reply message      */
DECL|method|getReplyMessage ()
specifier|public
name|Message
name|getReplyMessage
parameter_list|()
block|{
return|return
name|replyMessage
return|;
block|}
comment|/**      * The correlation id of the reply message      */
DECL|method|getCorrelationId ()
specifier|public
name|String
name|getCorrelationId
parameter_list|()
block|{
return|return
name|correlationId
return|;
block|}
block|}
end_class

end_unit

