begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.binding
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|binding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|processor
operator|.
name|MarshalProcessor
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
name|processor
operator|.
name|UnmarshalProcessor
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
name|DataFormat
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
name|support
operator|.
name|SynchronizationAdapter
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
name|AsyncProcessorHelper
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
name|util
operator|.
name|MessageHelper
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.Processor} that binds the REST DSL incoming and outgoing messages  * from sources of json or xml to Java Objects.  *<p/>  * The binding uses {@link org.apache.camel.spi.DataFormat} for the actual work to transform  * from xml/json to Java Objects and reverse again.  */
end_comment

begin_class
DECL|class|RestBindingProcessor
specifier|public
class|class
name|RestBindingProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
block|{
DECL|field|jsonUnmarshal
specifier|private
specifier|final
name|AsyncProcessor
name|jsonUnmarshal
decl_stmt|;
DECL|field|xmlUnmarshal
specifier|private
specifier|final
name|AsyncProcessor
name|xmlUnmarshal
decl_stmt|;
DECL|field|jsonMmarshal
specifier|private
specifier|final
name|AsyncProcessor
name|jsonMmarshal
decl_stmt|;
DECL|field|xmlMmarshal
specifier|private
specifier|final
name|AsyncProcessor
name|xmlMmarshal
decl_stmt|;
DECL|field|consumes
specifier|private
specifier|final
name|String
name|consumes
decl_stmt|;
DECL|field|produces
specifier|private
specifier|final
name|String
name|produces
decl_stmt|;
DECL|field|bindingMode
specifier|private
specifier|final
name|String
name|bindingMode
decl_stmt|;
DECL|method|RestBindingProcessor (DataFormat jsonDataFormat, DataFormat xmlDataFormat, DataFormat outJsonDataFormat, DataFormat outXmlDataFormat, String consumes, String produces, String bindingMode)
specifier|public
name|RestBindingProcessor
parameter_list|(
name|DataFormat
name|jsonDataFormat
parameter_list|,
name|DataFormat
name|xmlDataFormat
parameter_list|,
name|DataFormat
name|outJsonDataFormat
parameter_list|,
name|DataFormat
name|outXmlDataFormat
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|String
name|bindingMode
parameter_list|)
block|{
if|if
condition|(
name|jsonDataFormat
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|jsonUnmarshal
operator|=
operator|new
name|UnmarshalProcessor
argument_list|(
name|jsonDataFormat
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|jsonUnmarshal
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|outJsonDataFormat
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|jsonMmarshal
operator|=
operator|new
name|MarshalProcessor
argument_list|(
name|outJsonDataFormat
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|jsonDataFormat
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|jsonMmarshal
operator|=
operator|new
name|MarshalProcessor
argument_list|(
name|jsonDataFormat
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|jsonMmarshal
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|xmlDataFormat
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|xmlUnmarshal
operator|=
operator|new
name|UnmarshalProcessor
argument_list|(
name|xmlDataFormat
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|xmlUnmarshal
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|outXmlDataFormat
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|xmlMmarshal
operator|=
operator|new
name|MarshalProcessor
argument_list|(
name|outXmlDataFormat
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xmlDataFormat
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|xmlMmarshal
operator|=
operator|new
name|MarshalProcessor
argument_list|(
name|xmlDataFormat
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|xmlMmarshal
operator|=
literal|null
expr_stmt|;
block|}
name|this
operator|.
name|consumes
operator|=
name|consumes
expr_stmt|;
name|this
operator|.
name|produces
operator|=
name|produces
expr_stmt|;
name|this
operator|.
name|bindingMode
operator|=
name|bindingMode
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// TODO: consumes/produces can be a list of media types, and prioritized 1st to last.
comment|// TODO: parsing body should only be done if really needed
annotation|@
name|Override
DECL|method|process (Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|bindingMode
operator|==
literal|null
operator|||
literal|"off"
operator|.
name|equals
argument_list|(
name|bindingMode
argument_list|)
condition|)
block|{
comment|// binding is off
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
comment|// is there any unmarshaller at all
if|if
condition|(
name|jsonUnmarshal
operator|==
literal|null
operator|&&
name|xmlUnmarshal
operator|==
literal|null
condition|)
block|{
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
comment|// is the body empty
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|)
block|{
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
name|boolean
name|isXml
init|=
literal|false
decl_stmt|;
name|boolean
name|isJson
init|=
literal|false
decl_stmt|;
comment|// content type takes precedence, over consumes
name|String
name|contentType
init|=
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|isXml
operator|=
name|contentType
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
expr_stmt|;
name|isJson
operator|=
name|contentType
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
comment|// if content type could not tell us if it was json or xml, then fallback to if the binding was configured with
comment|// that information in the consumes
if|if
condition|(
operator|!
name|isXml
operator|&&
operator|!
name|isJson
condition|)
block|{
name|isXml
operator|=
name|consumes
operator|!=
literal|null
operator|&&
name|consumes
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
expr_stmt|;
name|isJson
operator|=
name|consumes
operator|!=
literal|null
operator|&&
name|consumes
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
comment|// if we do not know explicit if its json or xml, then need to check the message body to be sure what it is
if|if
condition|(
operator|!
name|isXml
operator|&&
operator|!
name|isJson
operator|||
name|isXml
operator|&&
name|isJson
condition|)
block|{
comment|// read the content into memory so we can determine if its xml or json
name|String
name|body
init|=
name|MessageHelper
operator|.
name|extractBodyAsString
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|isXml
operator|=
name|body
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
name|isJson
operator|=
operator|!
name|isXml
expr_stmt|;
block|}
block|}
comment|// only allow xml/json if the binding mode allows that
name|isXml
operator|&=
name|bindingMode
operator|.
name|equals
argument_list|(
literal|"auto"
argument_list|)
operator|||
name|bindingMode
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
expr_stmt|;
name|isJson
operator|&=
name|bindingMode
operator|.
name|equals
argument_list|(
literal|"auto"
argument_list|)
operator|||
name|bindingMode
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isXml
operator|&&
name|xmlUnmarshal
operator|!=
literal|null
condition|)
block|{
comment|// add reverse operation
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|RestBindingMarshalOnCompletion
argument_list|(
name|jsonMmarshal
argument_list|,
name|xmlMmarshal
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|xmlUnmarshal
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|isJson
operator|&&
name|jsonUnmarshal
operator|!=
literal|null
condition|)
block|{
comment|// add reverse operation
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|RestBindingMarshalOnCompletion
argument_list|(
name|jsonMmarshal
argument_list|,
name|xmlMmarshal
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|jsonUnmarshal
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|// we could not bind
if|if
condition|(
name|bindingMode
operator|.
name|equals
argument_list|(
literal|"auto"
argument_list|)
condition|)
block|{
comment|// okay for auto we do not mind if we could not bind
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
else|else
block|{
if|if
condition|(
name|bindingMode
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|BindingException
argument_list|(
literal|"Cannot bind to xml as message body is not xml compatible"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|BindingException
argument_list|(
literal|"Cannot bind to json as message body is not json compatible"
argument_list|,
name|exchange
argument_list|)
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
literal|"RestBindingProcessor"
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
comment|/**      * An {@link org.apache.camel.spi.Synchronization} that does the reverse operation      * of marshalling from POJO to json/xml      */
DECL|class|RestBindingMarshalOnCompletion
specifier|private
specifier|final
class|class
name|RestBindingMarshalOnCompletion
extends|extends
name|SynchronizationAdapter
block|{
DECL|field|jsonMmarshal
specifier|private
specifier|final
name|AsyncProcessor
name|jsonMmarshal
decl_stmt|;
DECL|field|xmlMmarshal
specifier|private
specifier|final
name|AsyncProcessor
name|xmlMmarshal
decl_stmt|;
DECL|method|RestBindingMarshalOnCompletion (AsyncProcessor jsonMmarshal, AsyncProcessor xmlMmarshal)
specifier|private
name|RestBindingMarshalOnCompletion
parameter_list|(
name|AsyncProcessor
name|jsonMmarshal
parameter_list|,
name|AsyncProcessor
name|xmlMmarshal
parameter_list|)
block|{
name|this
operator|.
name|jsonMmarshal
operator|=
name|jsonMmarshal
expr_stmt|;
name|this
operator|.
name|xmlMmarshal
operator|=
name|xmlMmarshal
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// only marshal if we succeeded (= onComplete)
if|if
condition|(
name|bindingMode
operator|==
literal|null
operator|||
literal|"off"
operator|.
name|equals
argument_list|(
name|bindingMode
argument_list|)
condition|)
block|{
comment|// binding is off
return|return;
block|}
comment|// is there any marshaller at all
if|if
condition|(
name|jsonMmarshal
operator|==
literal|null
operator|&&
name|xmlMmarshal
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// is the body empty
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
operator|&&
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
operator|==
literal|null
operator|||
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|boolean
name|isXml
init|=
literal|false
decl_stmt|;
name|boolean
name|isJson
init|=
literal|false
decl_stmt|;
name|String
name|contentType
init|=
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|isXml
operator|=
name|contentType
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
expr_stmt|;
name|isJson
operator|=
name|contentType
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
comment|// if content type could not tell us if it was json or xml, then fallback to if the binding was configured with
comment|// that information in the consumes
if|if
condition|(
operator|!
name|isXml
operator|&&
operator|!
name|isJson
condition|)
block|{
name|isXml
operator|=
name|produces
operator|!=
literal|null
operator|&&
name|produces
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
expr_stmt|;
name|isJson
operator|=
name|produces
operator|!=
literal|null
operator|&&
name|produces
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
comment|// need to prepare exchange first
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// if we do not know explicit if its json or xml, then need to check the message body to be sure what it is
if|if
condition|(
operator|!
name|isXml
operator|&&
operator|!
name|isJson
operator|||
name|isXml
operator|&&
name|isJson
condition|)
block|{
comment|// read the content into memory so we can determine if its xml or json
name|String
name|body
init|=
name|MessageHelper
operator|.
name|extractBodyAsString
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|isXml
operator|=
name|body
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
name|isJson
operator|=
operator|!
name|isXml
expr_stmt|;
block|}
block|}
comment|// only allow xml/json if the binding mode allows that
name|isXml
operator|&=
name|bindingMode
operator|.
name|equals
argument_list|(
literal|"auto"
argument_list|)
operator|||
name|bindingMode
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
expr_stmt|;
name|isJson
operator|&=
name|bindingMode
operator|.
name|equals
argument_list|(
literal|"auto"
argument_list|)
operator|||
name|bindingMode
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|isXml
operator|&&
name|xmlMmarshal
operator|!=
literal|null
condition|)
block|{
name|xmlMmarshal
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isJson
operator|&&
name|jsonMmarshal
operator|!=
literal|null
condition|)
block|{
name|jsonMmarshal
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we could not bind
if|if
condition|(
name|bindingMode
operator|.
name|equals
argument_list|(
literal|"auto"
argument_list|)
condition|)
block|{
comment|// okay for auto we do not mind if we could not bind
return|return;
block|}
else|else
block|{
if|if
condition|(
name|bindingMode
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|BindingException
argument_list|(
literal|"Cannot bind to xml as message body is not xml compatible"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|BindingException
argument_list|(
literal|"Cannot bind to json as message body is not json compatible"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
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
block|}
block|}
block|}
end_class

end_unit

