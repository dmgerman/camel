begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
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
name|List
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|MDNField
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|AS2DispositionModifier
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|AS2DispositionType
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|AS2MessageDispositionNotificationEntity
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|DispositionMode
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
name|as2
operator|.
name|api
operator|.
name|util
operator|.
name|DispositionNotificationContentUtils
operator|.
name|Field
operator|.
name|Element
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
name|as2
operator|.
name|api
operator|.
name|util
operator|.
name|MicUtils
operator|.
name|ReceivedContentMic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|ParserCursor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|TokenParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|Args
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|CharArrayBuffer
import|;
end_import

begin_class
DECL|class|DispositionNotificationContentUtils
specifier|public
specifier|final
class|class
name|DispositionNotificationContentUtils
block|{
DECL|field|REPORTING_UA
specifier|private
specifier|static
specifier|final
name|String
name|REPORTING_UA
init|=
literal|"reporting-ua"
decl_stmt|;
DECL|field|MDN_GATEWAY
specifier|private
specifier|static
specifier|final
name|String
name|MDN_GATEWAY
init|=
literal|"mdn-gateway"
decl_stmt|;
DECL|field|FINAL_RECIPIENT
specifier|private
specifier|static
specifier|final
name|String
name|FINAL_RECIPIENT
init|=
literal|"final-recipient"
decl_stmt|;
DECL|field|ORIGINAL_MESSAGE_ID
specifier|private
specifier|static
specifier|final
name|String
name|ORIGINAL_MESSAGE_ID
init|=
literal|"original-message-id"
decl_stmt|;
DECL|field|DISPOSITION
specifier|private
specifier|static
specifier|final
name|String
name|DISPOSITION
init|=
literal|"disposition"
decl_stmt|;
DECL|field|FAILURE
specifier|private
specifier|static
specifier|final
name|String
name|FAILURE
init|=
literal|"failure"
decl_stmt|;
DECL|field|ERROR
specifier|private
specifier|static
specifier|final
name|String
name|ERROR
init|=
literal|"error"
decl_stmt|;
DECL|field|WARNING
specifier|private
specifier|static
specifier|final
name|String
name|WARNING
init|=
literal|"warning"
decl_stmt|;
DECL|field|RECEIVED_CONTENT_MIC
specifier|private
specifier|static
specifier|final
name|String
name|RECEIVED_CONTENT_MIC
init|=
literal|"received-content-mic"
decl_stmt|;
DECL|class|Field
specifier|public
specifier|static
class|class
name|Field
block|{
DECL|class|Element
specifier|public
specifier|static
class|class
name|Element
block|{
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|field|parameters
specifier|private
specifier|final
name|String
index|[]
name|parameters
decl_stmt|;
DECL|method|Element (String value, String[] parameters)
specifier|public
name|Element
parameter_list|(
name|String
name|value
parameter_list|,
name|String
index|[]
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
operator|(
name|parameters
operator|==
literal|null
operator|)
condition|?
operator|new
name|String
index|[]
block|{}
else|:
name|parameters
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|getParameters ()
specifier|public
name|String
index|[]
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
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
return|return
name|value
operator|+
operator|(
operator|(
name|parameters
operator|.
name|length
operator|>
literal|0
operator|)
condition|?
literal|", "
operator|+
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|parameters
argument_list|)
else|:
literal|""
operator|)
return|;
block|}
block|}
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|elements
specifier|private
name|Element
index|[]
name|elements
decl_stmt|;
DECL|method|Field (String name, Element[] elements)
specifier|public
name|Field
parameter_list|(
name|String
name|name
parameter_list|,
name|Element
index|[]
name|elements
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|Args
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|this
operator|.
name|elements
operator|=
operator|(
name|elements
operator|==
literal|null
operator|)
condition|?
operator|new
name|Element
index|[]
block|{}
else|:
name|elements
expr_stmt|;
block|}
DECL|method|Field (String name, String value)
specifier|public
name|Field
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|Args
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|this
operator|.
name|elements
operator|=
operator|new
name|Element
index|[]
block|{
operator|new
name|Element
argument_list|(
name|value
argument_list|,
literal|null
argument_list|)
block|}
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getElements ()
specifier|public
name|Element
index|[]
name|getElements
parameter_list|()
block|{
return|return
name|elements
return|;
block|}
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|element
init|=
name|elements
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"; "
operator|+
name|element
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|toString
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|name
operator|+
literal|": "
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|element
init|=
name|elements
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"; "
operator|+
name|element
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|field|TOKEN_PARSER
specifier|private
specifier|static
specifier|final
name|TokenParser
name|TOKEN_PARSER
init|=
name|TokenParser
operator|.
name|INSTANCE
decl_stmt|;
DECL|field|PARAM_DELIMITER
specifier|private
specifier|static
specifier|final
name|char
name|PARAM_DELIMITER
init|=
literal|','
decl_stmt|;
DECL|field|ELEM_DELIMITER
specifier|private
specifier|static
specifier|final
name|char
name|ELEM_DELIMITER
init|=
literal|';'
decl_stmt|;
DECL|field|TOKEN_DELIMS
specifier|private
specifier|static
specifier|final
name|BitSet
name|TOKEN_DELIMS
init|=
name|TokenParser
operator|.
name|INIT_BITSET
argument_list|(
name|PARAM_DELIMITER
argument_list|,
name|ELEM_DELIMITER
argument_list|)
decl_stmt|;
DECL|method|DispositionNotificationContentUtils ()
specifier|private
name|DispositionNotificationContentUtils
parameter_list|()
block|{     }
DECL|method|parseDispositionNotification (List<CharArrayBuffer> dispositionNotificationFields)
specifier|public
specifier|static
name|AS2MessageDispositionNotificationEntity
name|parseDispositionNotification
parameter_list|(
name|List
argument_list|<
name|CharArrayBuffer
argument_list|>
name|dispositionNotificationFields
parameter_list|)
throws|throws
name|ParseException
block|{
name|String
name|reportingUA
init|=
literal|null
decl_stmt|;
name|String
name|mtaName
init|=
literal|null
decl_stmt|;
name|String
name|finalRecipient
init|=
literal|null
decl_stmt|;
name|String
name|originalMessageId
init|=
literal|null
decl_stmt|;
name|DispositionMode
name|dispositionMode
init|=
literal|null
decl_stmt|;
name|AS2DispositionType
name|dispositionType
init|=
literal|null
decl_stmt|;
name|AS2DispositionModifier
name|dispositionModifier
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|failures
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|errors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|warnings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extensionFields
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ReceivedContentMic
name|receivedContentMic
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|dispositionNotificationFields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|CharArrayBuffer
name|fieldLine
init|=
name|dispositionNotificationFields
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|Field
name|field
init|=
name|parseDispositionField
argument_list|(
name|fieldLine
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|field
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
name|REPORTING_UA
case|:
block|{
if|if
condition|(
name|field
operator|.
name|getElements
argument_list|()
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid '"
operator|+
name|MDNField
operator|.
name|REPORTING_UA
operator|+
literal|"' field: UA name is missing"
argument_list|)
throw|;
block|}
name|reportingUA
operator|=
name|field
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
name|MDN_GATEWAY
case|:
block|{
name|Element
index|[]
name|elements
init|=
name|field
operator|.
name|getElements
argument_list|()
decl_stmt|;
if|if
condition|(
name|elements
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid '"
operator|+
name|MDNField
operator|.
name|MDN_GATEWAY
operator|+
literal|"' field: MTA name is missing"
argument_list|)
throw|;
block|}
name|mtaName
operator|=
name|elements
index|[
literal|1
index|]
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
name|FINAL_RECIPIENT
case|:
block|{
name|Element
index|[]
name|elements
init|=
name|field
operator|.
name|getElements
argument_list|()
decl_stmt|;
if|if
condition|(
name|elements
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid '"
operator|+
name|MDNField
operator|.
name|FINAL_RECIPIENT
operator|+
literal|"' field: recipient address is missing"
argument_list|)
throw|;
block|}
name|finalRecipient
operator|=
name|elements
index|[
literal|1
index|]
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
name|ORIGINAL_MESSAGE_ID
case|:
block|{
name|originalMessageId
operator|=
name|field
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
name|DISPOSITION
case|:
block|{
name|Element
index|[]
name|elements
init|=
name|field
operator|.
name|getElements
argument_list|()
decl_stmt|;
if|if
condition|(
name|elements
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid '"
operator|+
name|MDNField
operator|.
name|DISPOSITION
operator|+
literal|"' field: "
operator|+
name|field
operator|.
name|getValue
argument_list|()
argument_list|)
throw|;
block|}
name|dispositionMode
operator|=
name|DispositionMode
operator|.
name|parseDispositionMode
argument_list|(
name|elements
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|dispositionMode
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid '"
operator|+
name|MDNField
operator|.
name|DISPOSITION
operator|+
literal|"' field: invalid disposition mode '"
operator|+
name|elements
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|String
name|dispositionTypeString
init|=
name|elements
index|[
literal|1
index|]
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|int
name|slash
init|=
name|dispositionTypeString
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|slash
operator|==
operator|-
literal|1
condition|)
block|{
name|dispositionType
operator|=
name|AS2DispositionType
operator|.
name|parseDispositionType
argument_list|(
name|dispositionTypeString
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dispositionType
operator|=
name|AS2DispositionType
operator|.
name|parseDispositionType
argument_list|(
name|dispositionTypeString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|slash
argument_list|)
argument_list|)
expr_stmt|;
name|dispositionModifier
operator|=
name|AS2DispositionModifier
operator|.
name|parseDispositionType
argument_list|(
name|dispositionTypeString
operator|.
name|substring
argument_list|(
name|slash
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
name|FAILURE
case|:
name|failures
operator|.
name|add
argument_list|(
name|field
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|ERROR
case|:
name|errors
operator|.
name|add
argument_list|(
name|field
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|WARNING
case|:
name|warnings
operator|.
name|add
argument_list|(
name|field
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|RECEIVED_CONTENT_MIC
case|:
block|{
name|Element
index|[]
name|elements
init|=
name|field
operator|.
name|getElements
argument_list|()
decl_stmt|;
if|if
condition|(
name|elements
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid '"
operator|+
name|MDNField
operator|.
name|RECEIVED_CONTENT_MIC
operator|+
literal|"' field: MIC is missing"
argument_list|)
throw|;
block|}
name|Element
name|element
init|=
name|elements
index|[
literal|0
index|]
decl_stmt|;
name|String
index|[]
name|parameters
init|=
name|element
operator|.
name|getParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid '"
operator|+
name|MDNField
operator|.
name|RECEIVED_CONTENT_MIC
operator|+
literal|"' field: digest algorithm ID is missing"
argument_list|)
throw|;
block|}
name|String
name|digestAlgorithmId
init|=
name|parameters
index|[
literal|0
index|]
decl_stmt|;
name|String
name|encodedMessageDigest
init|=
name|element
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|receivedContentMic
operator|=
operator|new
name|ReceivedContentMic
argument_list|(
name|digestAlgorithmId
argument_list|,
name|encodedMessageDigest
argument_list|)
expr_stmt|;
break|break;
block|}
default|default:
comment|// Extension Field
name|extensionFields
operator|.
name|put
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|field
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|AS2MessageDispositionNotificationEntity
argument_list|(
name|reportingUA
argument_list|,
name|mtaName
argument_list|,
name|finalRecipient
argument_list|,
name|originalMessageId
argument_list|,
name|dispositionMode
argument_list|,
name|dispositionType
argument_list|,
name|dispositionModifier
argument_list|,
name|failures
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|failures
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|errors
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|errors
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|warnings
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|warnings
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|extensionFields
argument_list|,
name|receivedContentMic
argument_list|)
return|;
block|}
DECL|method|parseDispositionField (CharArrayBuffer fieldLine)
specifier|public
specifier|static
name|Field
name|parseDispositionField
parameter_list|(
name|CharArrayBuffer
name|fieldLine
parameter_list|)
block|{
specifier|final
name|int
name|colon
init|=
name|fieldLine
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|colon
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid field: "
operator|+
name|fieldLine
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|String
name|fieldName
init|=
name|fieldLine
operator|.
name|substringTrimmed
argument_list|(
literal|0
argument_list|,
name|colon
argument_list|)
decl_stmt|;
name|ParserCursor
name|cursor
init|=
operator|new
name|ParserCursor
argument_list|(
name|colon
operator|+
literal|1
argument_list|,
name|fieldLine
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Element
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|cursor
operator|.
name|atEnd
argument_list|()
condition|)
block|{
specifier|final
name|Element
name|element
init|=
name|parseDispositionFieldElement
argument_list|(
name|fieldLine
argument_list|,
name|cursor
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|elements
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|Field
argument_list|(
name|fieldName
argument_list|,
name|elements
operator|.
name|toArray
argument_list|(
operator|new
name|Element
index|[
name|elements
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
DECL|method|parseDispositionFieldElement (CharArrayBuffer fieldLine, ParserCursor cursor)
specifier|public
specifier|static
name|Element
name|parseDispositionFieldElement
parameter_list|(
name|CharArrayBuffer
name|fieldLine
parameter_list|,
name|ParserCursor
name|cursor
parameter_list|)
block|{
specifier|final
name|String
name|value
init|=
name|TOKEN_PARSER
operator|.
name|parseToken
argument_list|(
name|fieldLine
argument_list|,
name|cursor
argument_list|,
name|TOKEN_DELIMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|cursor
operator|.
name|atEnd
argument_list|()
condition|)
block|{
return|return
operator|new
name|Element
argument_list|(
name|value
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|final
name|char
name|delim
init|=
name|fieldLine
operator|.
name|charAt
argument_list|(
name|cursor
operator|.
name|getPos
argument_list|()
argument_list|)
decl_stmt|;
name|cursor
operator|.
name|updatePos
argument_list|(
name|cursor
operator|.
name|getPos
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|delim
operator|==
name|ELEM_DELIMITER
condition|)
block|{
return|return
operator|new
name|Element
argument_list|(
name|value
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|parameters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|cursor
operator|.
name|atEnd
argument_list|()
condition|)
block|{
specifier|final
name|String
name|parameter
init|=
name|TOKEN_PARSER
operator|.
name|parseToken
argument_list|(
name|fieldLine
argument_list|,
name|cursor
argument_list|,
name|TOKEN_DELIMS
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|add
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
if|if
condition|(
name|cursor
operator|.
name|atEnd
argument_list|()
condition|)
block|{
break|break;
block|}
specifier|final
name|char
name|ch
init|=
name|fieldLine
operator|.
name|charAt
argument_list|(
name|cursor
operator|.
name|getPos
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cursor
operator|.
name|atEnd
argument_list|()
condition|)
block|{
name|cursor
operator|.
name|updatePos
argument_list|(
name|cursor
operator|.
name|getPos
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ch
operator|==
name|ELEM_DELIMITER
condition|)
block|{
break|break;
block|}
block|}
return|return
operator|new
name|Element
argument_list|(
name|value
argument_list|,
name|parameters
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|parameters
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

