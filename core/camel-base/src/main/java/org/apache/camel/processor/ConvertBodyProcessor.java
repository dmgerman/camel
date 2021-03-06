begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletableFuture
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
name|AsyncProcessor
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
name|IdAware
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
name|AsyncCallbackToCompletableFutureAdapter
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
name|DefaultMessage
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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
name|IOHelper
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A processor which converts the payload of the input message to be of the given type  *<p/>  * If the conversion fails an {@link org.apache.camel.InvalidPayloadException} is thrown.  */
end_comment

begin_class
DECL|class|ConvertBodyProcessor
specifier|public
class|class
name|ConvertBodyProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|charset
specifier|private
specifier|final
name|String
name|charset
decl_stmt|;
DECL|method|ConvertBodyProcessor (Class<?> type)
specifier|public
name|ConvertBodyProcessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|type
argument_list|,
literal|"type"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|charset
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|ConvertBodyProcessor (Class<?> type, String charset)
specifier|public
name|ConvertBodyProcessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|type
argument_list|,
literal|"type"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|charset
operator|=
name|IOHelper
operator|.
name|normalizeCharset
argument_list|(
name|charset
argument_list|)
expr_stmt|;
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
literal|"convertBodyTo["
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
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
name|Message
name|old
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|old
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// only convert if there is a body
return|return;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// do not convert if an exception has been thrown as if we attempt to convert and it also fails with a new
comment|// exception then it will override the existing exception
return|return;
block|}
name|String
name|originalCharsetName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|originalCharsetName
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// override existing charset with configured charset as that is what the user
comment|// have explicit configured and expects to be used
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
comment|// use mandatory conversion
name|Object
name|value
init|=
name|old
operator|.
name|getMandatoryBody
argument_list|(
name|type
argument_list|)
decl_stmt|;
comment|// create a new message container so we do not drag specialized message objects along
comment|// but that is only needed if the old message is a specialized message
name|boolean
name|copyNeeded
init|=
operator|!
operator|(
name|old
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|DefaultMessage
operator|.
name|class
argument_list|)
operator|)
decl_stmt|;
if|if
condition|(
name|copyNeeded
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|msg
operator|.
name|copyFromWithNewBody
argument_list|(
name|old
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// replace message on exchange
name|ExchangeHelper
operator|.
name|replaceMessage
argument_list|(
name|exchange
argument_list|,
name|msg
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no copy needed so set replace value directly
name|old
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|// remove or restore charset when we are done as we should not propagate that,
comment|// as that can lead to double converting later on
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|originalCharsetName
operator|!=
literal|null
operator|&&
operator|!
name|originalCharsetName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|originalCharsetName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|processAsync (Exchange exchange)
specifier|public
name|CompletableFuture
argument_list|<
name|Exchange
argument_list|>
name|processAsync
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|AsyncCallbackToCompletableFutureAdapter
argument_list|<
name|Exchange
argument_list|>
name|callback
init|=
operator|new
name|AsyncCallbackToCompletableFutureAdapter
argument_list|<>
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
return|return
name|callback
operator|.
name|getFuture
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getCharset ()
specifier|public
name|String
name|getCharset
parameter_list|()
block|{
return|return
name|charset
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

