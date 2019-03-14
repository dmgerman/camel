begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfixj
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|Converter
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
name|ExchangePattern
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
name|quickfixj
operator|.
name|QuickfixjEndpoint
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
name|quickfixj
operator|.
name|QuickfixjEventCategory
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
name|ExchangeHelper
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

begin_import
import|import
name|quickfix
operator|.
name|ConfigError
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|DataDictionary
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FieldNotFound
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|InvalidMessage
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|MsgType
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
name|quickfixj
operator|.
name|QuickfixjEndpoint
operator|.
name|EVENT_CATEGORY_KEY
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
name|quickfixj
operator|.
name|QuickfixjEndpoint
operator|.
name|MESSAGE_TYPE_KEY
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
name|quickfixj
operator|.
name|QuickfixjEndpoint
operator|.
name|SESSION_ID_KEY
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|QuickfixjConverters
specifier|public
specifier|final
class|class
name|QuickfixjConverters
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|QuickfixjConverters
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|QuickfixjConverters ()
specifier|private
name|QuickfixjConverters
parameter_list|()
block|{
comment|//Utility class
block|}
annotation|@
name|Converter
DECL|method|toSessionID (String sessionID)
specifier|public
specifier|static
name|SessionID
name|toSessionID
parameter_list|(
name|String
name|sessionID
parameter_list|)
block|{
return|return
operator|new
name|SessionID
argument_list|(
name|sessionID
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toMessage (String value, Exchange exchange)
specifier|public
specifier|static
name|Message
name|toMessage
parameter_list|(
name|String
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidMessage
throws|,
name|ConfigError
block|{
name|DataDictionary
name|dataDictionary
init|=
name|getDataDictionary
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
operator|new
name|Message
argument_list|(
name|value
argument_list|,
name|dataDictionary
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toMessage (byte[] value, Exchange exchange)
specifier|public
specifier|static
name|Message
name|toMessage
parameter_list|(
name|byte
index|[]
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidMessage
throws|,
name|ConfigError
throws|,
name|UnsupportedEncodingException
block|{
name|DataDictionary
name|dataDictionary
init|=
name|getDataDictionary
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|charsetName
init|=
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|message
decl_stmt|;
if|if
condition|(
name|charsetName
operator|!=
literal|null
condition|)
block|{
name|message
operator|=
operator|new
name|String
argument_list|(
name|value
argument_list|,
name|charsetName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
operator|new
name|String
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|// if message ends with any sort of newline trim it so QuickfixJ's doesn't fail while parsing the string
if|if
condition|(
name|message
operator|.
name|endsWith
argument_list|(
literal|"\r\n"
argument_list|)
condition|)
block|{
name|message
operator|=
name|message
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|message
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|.
name|endsWith
argument_list|(
literal|"\r"
argument_list|)
operator|||
name|message
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|message
operator|=
name|message
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|message
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|dataDictionary
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (Message value, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|Message
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidMessage
throws|,
name|ConfigError
throws|,
name|UnsupportedEncodingException
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|String
name|charsetName
init|=
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|charsetName
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charsetName
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getDataDictionary (Exchange exchange)
specifier|private
specifier|static
name|DataDictionary
name|getDataDictionary
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|ConfigError
block|{
name|Object
name|dictionaryValue
init|=
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|QuickfixjEndpoint
operator|.
name|DATA_DICTIONARY_KEY
argument_list|)
decl_stmt|;
name|DataDictionary
name|dataDictionary
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dictionaryValue
operator|instanceof
name|DataDictionary
condition|)
block|{
name|dataDictionary
operator|=
operator|(
name|DataDictionary
operator|)
name|dictionaryValue
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dictionaryValue
operator|instanceof
name|String
condition|)
block|{
name|dataDictionary
operator|=
operator|new
name|DataDictionary
argument_list|(
operator|(
name|String
operator|)
name|dictionaryValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SessionID
name|sessionID
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|SESSION_ID_KEY
argument_list|,
name|SessionID
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sessionID
operator|!=
literal|null
condition|)
block|{
name|Session
name|session
init|=
name|Session
operator|.
name|lookupSession
argument_list|(
name|sessionID
argument_list|)
decl_stmt|;
name|dataDictionary
operator|=
name|session
operator|!=
literal|null
condition|?
name|session
operator|.
name|getDataDictionary
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
block|}
return|return
name|dataDictionary
return|;
block|}
DECL|method|toExchange (Endpoint endpoint, SessionID sessionID, Message message, QuickfixjEventCategory eventCategory)
specifier|public
specifier|static
name|Exchange
name|toExchange
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|SessionID
name|sessionID
parameter_list|,
name|Message
name|message
parameter_list|,
name|QuickfixjEventCategory
name|eventCategory
parameter_list|)
block|{
return|return
name|toExchange
argument_list|(
name|endpoint
argument_list|,
name|sessionID
argument_list|,
name|message
argument_list|,
name|eventCategory
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
return|;
block|}
DECL|method|toExchange (Endpoint endpoint, SessionID sessionID, Message message, QuickfixjEventCategory eventCategory, ExchangePattern exchangePattern)
specifier|public
specifier|static
name|Exchange
name|toExchange
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|SessionID
name|sessionID
parameter_list|,
name|Message
name|message
parameter_list|,
name|QuickfixjEventCategory
name|eventCategory
parameter_list|,
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|exchangePattern
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|EVENT_CATEGORY_KEY
argument_list|,
name|eventCategory
argument_list|)
expr_stmt|;
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|SESSION_ID_KEY
argument_list|,
name|sessionID
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|MESSAGE_TYPE_KEY
argument_list|,
name|message
operator|.
name|getHeader
argument_list|()
operator|.
name|getString
argument_list|(
name|MsgType
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FieldNotFound
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Message type field not found in QFJ message: {}, continuing..."
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
name|camelMessage
operator|.
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

