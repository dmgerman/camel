begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sqs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sqs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|AmazonSQS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|MessageAttributeValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|SendMessageRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|SendMessageResult
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
name|NoFactoryAvailableException
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
name|DefaultProducer
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|URISupport
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
name|aws
operator|.
name|common
operator|.
name|AwsExchangeUtil
operator|.
name|getMessageForResponse
import|;
end_import

begin_comment
comment|/**  * A Producer which sends messages to the Amazon Web Service Simple Queue Service  *<a href="http://aws.amazon.com/sqs/">AWS SQS</a>  *   */
end_comment

begin_class
DECL|class|SqsProducer
specifier|public
class|class
name|SqsProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|sqsProducerToString
specifier|private
specifier|transient
name|String
name|sqsProducerToString
decl_stmt|;
DECL|method|SqsProducer (SqsEndpoint endpoint)
specifier|public
name|SqsProducer
parameter_list|(
name|SqsEndpoint
name|endpoint
parameter_list|)
throws|throws
name|NoFactoryAvailableException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|SendMessageRequest
name|request
init|=
operator|new
name|SendMessageRequest
argument_list|(
name|getQueueUrl
argument_list|()
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|request
operator|.
name|setMessageAttributes
argument_list|(
name|translateAttributes
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|addDelay
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|configureFifoAttributes
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Sending request [{}] from exchange [{}]..."
argument_list|,
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|SendMessageResult
name|result
init|=
name|getClient
argument_list|()
operator|.
name|sendMessage
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Received result [{}]"
argument_list|,
name|result
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
name|SqsConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|result
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SqsConstants
operator|.
name|MD5_OF_BODY
argument_list|,
name|result
operator|.
name|getMD5OfMessageBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|configureFifoAttributes (SendMessageRequest request, Exchange exchange)
specifier|private
name|void
name|configureFifoAttributes
parameter_list|(
name|SendMessageRequest
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isFifoQueue
argument_list|()
condition|)
block|{
comment|// use strategies
name|MessageGroupIdStrategy
name|messageGroupIdStrategy
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageGroupIdStrategy
argument_list|()
decl_stmt|;
name|String
name|messageGroupId
init|=
name|messageGroupIdStrategy
operator|.
name|getMessageGroupId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|request
operator|.
name|setMessageGroupId
argument_list|(
name|messageGroupId
argument_list|)
expr_stmt|;
name|MessageDeduplicationIdStrategy
name|messageDeduplicationIdStrategy
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageDeduplicationIdStrategy
argument_list|()
decl_stmt|;
name|String
name|messageDeduplicationId
init|=
name|messageDeduplicationIdStrategy
operator|.
name|getMessageDeduplicationId
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|request
operator|.
name|setMessageDeduplicationId
argument_list|(
name|messageDeduplicationId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addDelay (SendMessageRequest request, Exchange exchange)
specifier|private
name|void
name|addDelay
parameter_list|(
name|SendMessageRequest
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Integer
name|headerValue
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|DELAY_HEADER
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Integer
name|delayValue
decl_stmt|;
if|if
condition|(
name|headerValue
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Using the config delay"
argument_list|)
expr_stmt|;
name|delayValue
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDelaySeconds
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Using the header delay"
argument_list|)
expr_stmt|;
name|delayValue
operator|=
name|headerValue
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"found delay: {}"
argument_list|,
name|delayValue
argument_list|)
expr_stmt|;
name|request
operator|.
name|setDelaySeconds
argument_list|(
name|delayValue
operator|==
literal|null
condition|?
name|Integer
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
else|:
name|delayValue
argument_list|)
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|protected
name|AmazonSQS
name|getClient
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
return|;
block|}
DECL|method|getQueueUrl ()
specifier|protected
name|String
name|getQueueUrl
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getQueueUrl
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SqsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SqsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|sqsProducerToString
operator|==
literal|null
condition|)
block|{
name|sqsProducerToString
operator|=
literal|"SqsProducer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|sqsProducerToString
return|;
block|}
DECL|method|translateAttributes (Map<String, Object> headers, Exchange exchange)
name|Map
argument_list|<
name|String
argument_list|,
name|MessageAttributeValue
argument_list|>
name|translateAttributes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|MessageAttributeValue
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHeaderFilterStrategy
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// only put the message header which is not filtered into the message attribute
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
operator|&&
operator|!
operator|(
operator|(
name|String
operator|)
name|value
operator|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|MessageAttributeValue
name|mav
init|=
operator|new
name|MessageAttributeValue
argument_list|()
decl_stmt|;
name|mav
operator|.
name|setDataType
argument_list|(
literal|"String"
argument_list|)
expr_stmt|;
name|mav
operator|.
name|withStringValue
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|mav
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|ByteBuffer
condition|)
block|{
name|MessageAttributeValue
name|mav
init|=
operator|new
name|MessageAttributeValue
argument_list|()
decl_stmt|;
name|mav
operator|.
name|setDataType
argument_list|(
literal|"Binary"
argument_list|)
expr_stmt|;
name|mav
operator|.
name|withBinaryValue
argument_list|(
operator|(
name|ByteBuffer
operator|)
name|value
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|mav
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Boolean
condition|)
block|{
name|MessageAttributeValue
name|mav
init|=
operator|new
name|MessageAttributeValue
argument_list|()
decl_stmt|;
name|mav
operator|.
name|setDataType
argument_list|(
literal|"Number.Boolean"
argument_list|)
expr_stmt|;
name|mav
operator|.
name|withStringValue
argument_list|(
operator|(
operator|(
name|Boolean
operator|)
name|value
operator|)
condition|?
literal|"1"
else|:
literal|"0"
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|mav
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|MessageAttributeValue
name|mav
init|=
operator|new
name|MessageAttributeValue
argument_list|()
decl_stmt|;
specifier|final
name|String
name|dataType
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Integer
condition|)
block|{
name|dataType
operator|=
literal|"Number.int"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Byte
condition|)
block|{
name|dataType
operator|=
literal|"Number.byte"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Double
condition|)
block|{
name|dataType
operator|=
literal|"Number.double"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Float
condition|)
block|{
name|dataType
operator|=
literal|"Number.float"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Long
condition|)
block|{
name|dataType
operator|=
literal|"Number.long"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Short
condition|)
block|{
name|dataType
operator|=
literal|"Number.short"
expr_stmt|;
block|}
else|else
block|{
name|dataType
operator|=
literal|"Number"
expr_stmt|;
block|}
name|mav
operator|.
name|setDataType
argument_list|(
name|dataType
argument_list|)
expr_stmt|;
name|mav
operator|.
name|withStringValue
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|value
operator|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|mav
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Date
condition|)
block|{
name|MessageAttributeValue
name|mav
init|=
operator|new
name|MessageAttributeValue
argument_list|()
decl_stmt|;
name|mav
operator|.
name|setDataType
argument_list|(
literal|"String"
argument_list|)
expr_stmt|;
name|mav
operator|.
name|withStringValue
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|mav
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// cannot translate the message header to message attribute value
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot put the message header key={}, value={} into Sqs MessageAttribute"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

