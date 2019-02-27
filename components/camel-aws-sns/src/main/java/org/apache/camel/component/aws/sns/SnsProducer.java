begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sns
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
name|sns
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
name|sns
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
name|sns
operator|.
name|model
operator|.
name|PublishRequest
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
name|sns
operator|.
name|model
operator|.
name|PublishResult
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
name|support
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * A Producer which sends messages to the Amazon Web Service Simple Notification Service  *<a href="http://aws.amazon.com/sns/">AWS SNS</a>  */
end_comment

begin_class
DECL|class|SnsProducer
specifier|public
class|class
name|SnsProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|snsProducerToString
specifier|private
specifier|transient
name|String
name|snsProducerToString
decl_stmt|;
DECL|method|SnsProducer (Endpoint endpoint)
specifier|public
name|SnsProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
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
name|PublishRequest
name|request
init|=
operator|new
name|PublishRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setTopicArn
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setSubject
argument_list|(
name|determineSubject
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setMessageStructure
argument_list|(
name|determineMessageStructure
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setMessage
argument_list|(
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
argument_list|)
expr_stmt|;
name|request
operator|.
name|setMessageAttributes
argument_list|(
name|this
operator|.
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
name|PublishResult
name|result
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSNSClient
argument_list|()
operator|.
name|publish
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
name|SnsConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|result
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|determineSubject (Exchange exchange)
specifier|private
name|String
name|determineSubject
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|subject
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SnsConstants
operator|.
name|SUBJECT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
block|{
name|subject
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getSubject
argument_list|()
expr_stmt|;
block|}
return|return
name|subject
return|;
block|}
DECL|method|determineMessageStructure (Exchange exchange)
specifier|private
name|String
name|determineMessageStructure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|structure
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SnsConstants
operator|.
name|MESSAGE_STRUCTURE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|structure
operator|==
literal|null
condition|)
block|{
name|structure
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getMessageStructure
argument_list|()
expr_stmt|;
block|}
return|return
name|structure
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
literal|"Cannot put the message header key={}, value={} into Sns MessageAttribute"
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
DECL|method|getConfiguration ()
specifier|protected
name|SnsConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
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
name|snsProducerToString
operator|==
literal|null
condition|)
block|{
name|snsProducerToString
operator|=
literal|"SnsProducer["
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
name|snsProducerToString
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SnsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SnsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getMessageForResponse (final Exchange exchange)
specifier|public
specifier|static
name|Message
name|getMessageForResponse
parameter_list|(
specifier|final
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
block|}
end_class

end_unit

