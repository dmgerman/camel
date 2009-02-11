begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|NoTypeConversionAvailableException
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
name|util
operator|.
name|UuidGenerator
import|;
end_import

begin_comment
comment|/**  * A base class for implementation inheritence providing the core  * {@link Message} body handling features but letting the derived class deal  * with headers.  *  * Unless a specific provider wishes to do something particularly clever with  * headers you probably want to just derive from {@link DefaultMessage}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MessageSupport
specifier|public
specifier|abstract
class|class
name|MessageSupport
implements|implements
name|Message
block|{
DECL|field|DEFALT_ID_GENERATOR
specifier|private
specifier|static
specifier|final
name|UuidGenerator
name|DEFALT_ID_GENERATOR
init|=
operator|new
name|UuidGenerator
argument_list|()
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
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
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
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
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
name|CamelContext
name|camelContext
init|=
name|e
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|TypeConverter
name|converter
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
try|try
block|{
comment|// lets first try converting the message itself first
comment|// as for some types like InputStream v Reader its more efficient to do the transformation
comment|// from the Message itself as its got efficient implementations of them, before trying the
comment|// payload
return|return
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
return|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|converter
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
return|return
operator|(
name|T
operator|)
name|getBody
argument_list|()
return|;
block|}
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
block|}
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
DECL|method|copyFrom (Message that)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|Message
name|that
parameter_list|)
block|{
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
name|that
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
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
name|getAttachments
argument_list|()
operator|.
name|putAll
argument_list|(
name|that
operator|.
name|getAttachments
argument_list|()
argument_list|)
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
comment|/**      * Lets allow implementations to auto-create a messageId      */
DECL|method|createMessageId ()
specifier|protected
name|String
name|createMessageId
parameter_list|()
block|{
return|return
name|DEFALT_ID_GENERATOR
operator|.
name|generateId
argument_list|()
return|;
block|}
block|}
end_class

end_unit

