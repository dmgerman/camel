begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

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
name|TypeConverter
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
name|DataType
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
name|DataTypeAware
import|;
end_import

begin_comment
comment|/**  * A base class for implementation inheritance providing the core  * {@link Message} body handling features but letting the derived class deal  * with headers.  *  * Unless a specific provider wishes to do something particularly clever with  * headers you probably want to just derive from {@link DefaultMessage}  */
end_comment

begin_class
DECL|class|MessageSupport
specifier|public
specifier|abstract
class|class
name|MessageSupport
implements|implements
name|Message
implements|,
name|CamelContextAware
implements|,
name|DataTypeAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|body
specifier|private
name|Object
name|body
decl_stmt|;
DECL|field|messageId
specifier|private
name|String
name|messageId
decl_stmt|;
DECL|field|dataType
specifier|private
name|DataType
name|dataType
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// do not output information about the message as it may contain sensitive information
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Message[%s]"
argument_list|,
name|messageId
operator|==
literal|null
condition|?
literal|""
else|:
name|messageId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getBody ()
specifier|public
name|Object
name|getBody
parameter_list|()
block|{
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|body
operator|=
name|createBody
argument_list|()
expr_stmt|;
block|}
return|return
name|body
return|;
block|}
annotation|@
name|Override
DECL|method|getBody (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getBody
argument_list|(
name|type
argument_list|,
name|getBody
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getMandatoryBody ()
specifier|public
name|Object
name|getMandatoryBody
parameter_list|()
throws|throws
name|InvalidPayloadException
block|{
name|Object
name|answer
init|=
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|Object
operator|.
name|class
argument_list|,
name|this
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getBody (Class<T> type, Object body)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|body
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|body
argument_list|)
return|;
block|}
name|Exchange
name|e
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|TypeConverter
name|converter
init|=
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
comment|// lets first try converting the body itself first
comment|// as for some types like InputStream v Reader its more efficient to do the transformation
comment|// from the body itself as its got efficient implementations of them, before trying the message
name|T
name|answer
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|e
argument_list|,
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
comment|// fallback and try the message itself (e.g. used in camel-http)
name|answer
operator|=
name|converter
operator|.
name|tryConvertTo
argument_list|(
name|type
argument_list|,
name|e
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
comment|// not possible to convert
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getMandatoryBody (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|body
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|body
argument_list|)
return|;
block|}
name|Exchange
name|e
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|TypeConverter
name|converter
init|=
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|converter
operator|.
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
name|e
argument_list|,
name|getBody
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|e
argument_list|,
name|type
argument_list|,
name|this
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|e
argument_list|,
name|type
argument_list|,
name|this
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|setBody (Object body)
specifier|public
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
comment|// set data type if in use
if|if
condition|(
name|body
operator|!=
literal|null
operator|&&
name|camelContext
operator|!=
literal|null
operator|&&
name|camelContext
operator|.
name|isUseDataType
argument_list|()
condition|)
block|{
name|this
operator|.
name|dataType
operator|=
operator|new
name|DataType
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setBody (Object value, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setBody
parameter_list|(
name|Object
name|value
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Exchange
name|e
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|T
name|v
init|=
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|e
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|v
expr_stmt|;
block|}
block|}
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setBody (Object body, DataType type)
specifier|public
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|,
name|DataType
name|type
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
name|this
operator|.
name|dataType
operator|=
name|type
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataType ()
specifier|public
name|DataType
name|getDataType
parameter_list|()
block|{
return|return
name|this
operator|.
name|dataType
return|;
block|}
annotation|@
name|Override
DECL|method|setDataType (DataType type)
specifier|public
name|void
name|setDataType
parameter_list|(
name|DataType
name|type
parameter_list|)
block|{
name|this
operator|.
name|dataType
operator|=
name|type
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasDataType ()
specifier|public
name|boolean
name|hasDataType
parameter_list|()
block|{
return|return
name|dataType
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|Message
name|copy
parameter_list|()
block|{
name|Message
name|answer
init|=
name|newInstance
argument_list|()
decl_stmt|;
comment|// must copy over CamelContext
if|if
condition|(
name|answer
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|answer
operator|)
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|copyFrom
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|copyFrom (Message that)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|Message
name|that
parameter_list|)
block|{
if|if
condition|(
name|that
operator|==
name|this
condition|)
block|{
comment|// the same instance so do not need to copy
return|return;
block|}
comment|// must copy over CamelContext
if|if
condition|(
name|that
operator|instanceof
name|CamelContextAware
condition|)
block|{
name|setCamelContext
argument_list|(
operator|(
operator|(
name|CamelContextAware
operator|)
name|that
operator|)
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|that
operator|instanceof
name|DataTypeAware
operator|&&
operator|(
operator|(
name|DataTypeAware
operator|)
name|that
operator|)
operator|.
name|hasDataType
argument_list|()
condition|)
block|{
name|setDataType
argument_list|(
operator|(
operator|(
name|DataTypeAware
operator|)
name|that
operator|)
operator|.
name|getDataType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// cover over exchange if none has been assigned
if|if
condition|(
name|getExchange
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setExchange
argument_list|(
name|that
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|copyFromWithNewBody
argument_list|(
name|that
argument_list|,
name|that
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|copyFromWithNewBody (Message that, Object newBody)
specifier|public
name|void
name|copyFromWithNewBody
parameter_list|(
name|Message
name|that
parameter_list|,
name|Object
name|newBody
parameter_list|)
block|{
if|if
condition|(
name|that
operator|==
name|this
condition|)
block|{
comment|// the same instance so do not need to copy
return|return;
block|}
comment|// must copy over CamelContext
if|if
condition|(
name|that
operator|instanceof
name|CamelContextAware
condition|)
block|{
name|setCamelContext
argument_list|(
operator|(
operator|(
name|CamelContextAware
operator|)
name|that
operator|)
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// cover over exchange if none has been assigned
if|if
condition|(
name|getExchange
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setExchange
argument_list|(
name|that
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// should likely not set DataType as the new body may be a different type than the original body
name|setMessageId
argument_list|(
name|that
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|setBody
argument_list|(
name|newBody
argument_list|)
expr_stmt|;
comment|// the headers may be the same instance if the end user has made some mistake
comment|// and set the OUT message with the same header instance of the IN message etc
name|boolean
name|sameHeadersInstance
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|hasHeaders
argument_list|()
operator|&&
name|that
operator|.
name|hasHeaders
argument_list|()
operator|&&
name|getHeaders
argument_list|()
operator|==
name|that
operator|.
name|getHeaders
argument_list|()
condition|)
block|{
name|sameHeadersInstance
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|sameHeadersInstance
condition|)
block|{
if|if
condition|(
name|hasHeaders
argument_list|()
condition|)
block|{
comment|// okay its safe to clear the headers
name|getHeaders
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|that
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|that
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
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
DECL|method|setExchange (Exchange exchange)
specifier|public
name|void
name|setExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
comment|/**      * Returns a new instance      */
DECL|method|newInstance ()
specifier|public
specifier|abstract
name|Message
name|newInstance
parameter_list|()
function_decl|;
comment|/**      * A factory method to allow a provider to lazily create the message body      * for inbound messages from other sources      *      * @return the value of the message body or null if there is no value      *         available      */
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getMessageId ()
specifier|public
name|String
name|getMessageId
parameter_list|()
block|{
if|if
condition|(
name|messageId
operator|==
literal|null
condition|)
block|{
name|messageId
operator|=
name|createMessageId
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|messageId
return|;
block|}
annotation|@
name|Override
DECL|method|setMessageId (String messageId)
specifier|public
name|void
name|setMessageId
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
name|this
operator|.
name|messageId
operator|=
name|messageId
expr_stmt|;
block|}
comment|/**      * Allow implementations to auto-create a messageId      */
DECL|method|createMessageId ()
specifier|protected
name|String
name|createMessageId
parameter_list|()
block|{
name|String
name|uuid
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|uuid
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
expr_stmt|;
block|}
comment|// fall back to the simple UUID generator
if|if
condition|(
name|uuid
operator|==
literal|null
condition|)
block|{
name|uuid
operator|=
operator|new
name|SimpleUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
expr_stmt|;
block|}
return|return
name|uuid
return|;
block|}
block|}
end_class

end_unit

