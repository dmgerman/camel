begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ironmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ironmq
package|;
end_package

begin_import
import|import
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|Queue
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
name|InvalidPayloadException
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * The IronMQ producer.  */
end_comment

begin_class
DECL|class|IronMQProducer
specifier|public
class|class
name|IronMQProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|ironQueue
specifier|private
specifier|final
name|Queue
name|ironQueue
decl_stmt|;
DECL|method|IronMQProducer (IronMQEndpoint endpoint, Queue ironQueue)
specifier|public
name|IronMQProducer
parameter_list|(
name|IronMQEndpoint
name|endpoint
parameter_list|,
name|Queue
name|ironQueue
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|ironQueue
operator|=
name|ironQueue
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|IronMQConfiguration
name|configuration
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|IronMQConstants
operator|.
name|CLEARQUEUE
operator|.
name|equals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IronMQConstants
operator|.
name|OPERATION
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
condition|)
block|{
name|this
operator|.
name|ironQueue
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Object
name|messageId
init|=
literal|null
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|String
index|[]
condition|)
block|{
name|messageId
operator|=
name|this
operator|.
name|ironQueue
operator|.
name|pushMessages
argument_list|(
operator|(
name|String
index|[]
operator|)
name|body
argument_list|,
name|configuration
operator|.
name|getVisibilityDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isPreserveHeaders
argument_list|()
condition|)
block|{
name|body
operator|=
name|GsonUtil
operator|.
name|getBodyFromMessage
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|messageId
operator|=
name|this
operator|.
name|ironQueue
operator|.
name|push
argument_list|(
operator|(
name|String
operator|)
name|body
argument_list|,
name|configuration
operator|.
name|getVisibilityDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
throw|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Send request [{}] from exchange [{}]..."
argument_list|,
name|body
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Received messageId [{}]"
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|IronMQConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getMessageForResponse (Exchange exchange)
specifier|private
name|Message
name|getMessageForResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|IronMQEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|IronMQEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

