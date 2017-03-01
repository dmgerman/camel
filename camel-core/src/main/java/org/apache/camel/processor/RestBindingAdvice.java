begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HashMap
import|;
end_import

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
name|processor
operator|.
name|binding
operator|.
name|BindingException
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
name|Contract
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
name|RestConfiguration
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
name|Transformer
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
name|ServiceHelper
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

begin_comment
comment|/**  * A {@link org.apache.camel.processor.CamelInternalProcessorAdvice} that binds the REST DSL incoming  * and outgoing messages from sources of json or xml to Java Objects.  *<p/>  * The binding uses {@link org.apache.camel.spi.DataFormat} for the actual work to transform  * from xml/json to Java Objects and reverse again.  *   * @see CamelInternalProcessor, CamelInternalProcessorAdvice  */
end_comment

begin_class
DECL|class|RestBindingAdvice
specifier|public
class|class
name|RestBindingAdvice
implements|implements
name|CamelInternalProcessorAdvice
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
block|{
DECL|field|STATE_KEY_DO_MARSHAL
specifier|private
specifier|static
specifier|final
name|String
name|STATE_KEY_DO_MARSHAL
init|=
literal|"doMarshal"
decl_stmt|;
DECL|field|STATE_KEY_ACCEPT
specifier|private
specifier|static
specifier|final
name|String
name|STATE_KEY_ACCEPT
init|=
literal|"accept"
decl_stmt|;
DECL|field|STATE_JSON
specifier|private
specifier|static
specifier|final
name|String
name|STATE_JSON
init|=
literal|"json"
decl_stmt|;
DECL|field|STATE_XML
specifier|private
specifier|static
specifier|final
name|String
name|STATE_XML
init|=
literal|"xml"
decl_stmt|;
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
DECL|field|jsonMarshal
specifier|private
specifier|final
name|AsyncProcessor
name|jsonMarshal
decl_stmt|;
DECL|field|xmlMarshal
specifier|private
specifier|final
name|AsyncProcessor
name|xmlMarshal
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
DECL|field|skipBindingOnErrorCode
specifier|private
specifier|final
name|boolean
name|skipBindingOnErrorCode
decl_stmt|;
DECL|field|enableCORS
specifier|private
specifier|final
name|boolean
name|enableCORS
decl_stmt|;
DECL|field|corsHeaders
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|corsHeaders
decl_stmt|;
DECL|field|queryDefaultValues
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryDefaultValues
decl_stmt|;
DECL|method|RestBindingAdvice (CamelContext camelContext, DataFormat jsonDataFormat, DataFormat xmlDataFormat, DataFormat outJsonDataFormat, DataFormat outXmlDataFormat, String consumes, String produces, String bindingMode, boolean skipBindingOnErrorCode, boolean enableCORS, Map<String, String> corsHeaders, Map<String, String> queryDefaultValues)
specifier|public
name|RestBindingAdvice
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
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
parameter_list|,
name|boolean
name|skipBindingOnErrorCode
parameter_list|,
name|boolean
name|enableCORS
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|corsHeaders
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryDefaultValues
parameter_list|)
throws|throws
name|Exception
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
name|jsonMarshal
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
name|jsonMarshal
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
name|jsonMarshal
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
name|xmlMarshal
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
name|xmlMarshal
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
name|xmlMarshal
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|jsonMarshal
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|jsonMarshal
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jsonUnmarshal
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|jsonUnmarshal
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xmlMarshal
operator|instanceof
name|CamelContextAware
condition|)
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|xmlMarshal
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xmlUnmarshal
operator|instanceof
name|CamelContextAware
condition|)
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|xmlUnmarshal
argument_list|,
literal|true
argument_list|)
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
name|this
operator|.
name|skipBindingOnErrorCode
operator|=
name|skipBindingOnErrorCode
expr_stmt|;
name|this
operator|.
name|enableCORS
operator|=
name|enableCORS
expr_stmt|;
name|this
operator|.
name|corsHeaders
operator|=
name|corsHeaders
expr_stmt|;
name|this
operator|.
name|queryDefaultValues
operator|=
name|queryDefaultValues
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|before (Exchange exchange)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|before
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|state
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|isOptionsMethod
argument_list|(
name|exchange
argument_list|,
name|state
argument_list|)
condition|)
block|{
return|return
name|state
return|;
block|}
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|state
argument_list|)
expr_stmt|;
return|return
name|state
return|;
block|}
annotation|@
name|Override
DECL|method|after (Exchange exchange, Map<String, Object> state)
specifier|public
name|void
name|after
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|state
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|enableCORS
condition|)
block|{
name|setCORSHeaders
argument_list|(
name|exchange
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|.
name|get
argument_list|(
name|STATE_KEY_DO_MARSHAL
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|marshal
argument_list|(
name|exchange
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isOptionsMethod (Exchange exchange, Map<String, Object> state)
specifier|private
name|boolean
name|isOptionsMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|state
parameter_list|)
block|{
name|String
name|method
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"OPTIONS"
operator|.
name|equalsIgnoreCase
argument_list|(
name|method
argument_list|)
condition|)
block|{
comment|// for OPTIONS methods then we should not route at all as its part of CORS
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ROUTE_STOP
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|unmarshal (Exchange exchange, Map<String, Object> state)
specifier|private
name|void
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|state
parameter_list|)
throws|throws
name|Exception
block|{
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
name|ENGLISH
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
name|ENGLISH
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
name|ENGLISH
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
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|instanceof
name|DataTypeAware
operator|&&
operator|(
name|isJson
operator|||
name|isXml
operator|)
condition|)
block|{
operator|(
operator|(
name|DataTypeAware
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|)
operator|.
name|setDataType
argument_list|(
operator|new
name|DataType
argument_list|(
name|isJson
condition|?
literal|"json"
else|:
literal|"xml"
argument_list|)
argument_list|)
expr_stmt|;
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
comment|// if we do not yet know if its xml or json, then use the binding mode to know the mode
if|if
condition|(
operator|!
name|isJson
operator|&&
operator|!
name|isXml
condition|)
block|{
name|isXml
operator|=
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
operator|=
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
block|}
name|state
operator|.
name|put
argument_list|(
name|STATE_KEY_ACCEPT
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Accept"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// okay we have a binding mode, so need to check for empty body as that can cause the marshaller to fail
comment|// as they assume a non-empty body
if|if
condition|(
name|isXml
operator|||
name|isJson
condition|)
block|{
comment|// we have binding enabled, so we need to know if there body is empty or not
comment|// so force reading the body as a String which we can work with
name|body
operator|=
name|MessageHelper
operator|.
name|extractBodyAsString
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|instanceof
name|DataTypeAware
condition|)
block|{
operator|(
operator|(
name|DataTypeAware
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|)
operator|.
name|setBody
argument_list|(
name|body
argument_list|,
operator|new
name|DataType
argument_list|(
name|isJson
condition|?
literal|"json"
else|:
literal|"xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isXml
operator|&&
name|isJson
condition|)
block|{
comment|// we have still not determined between xml or json, so check the body if its xml based or not
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
block|}
block|}
comment|// add missing default values which are mapped as headers
if|if
condition|(
name|queryDefaultValues
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|queryDefaultValues
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// favor json over xml
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
name|state
operator|.
name|put
argument_list|(
name|STATE_KEY_DO_MARSHAL
argument_list|,
name|STATE_JSON
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|body
argument_list|)
condition|)
block|{
name|jsonUnmarshal
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
elseif|else
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
name|state
operator|.
name|put
argument_list|(
name|STATE_KEY_DO_MARSHAL
argument_list|,
name|STATE_XML
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|body
argument_list|)
condition|)
block|{
name|xmlUnmarshal
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ExchangeHelper
operator|.
name|prepareOutToIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
comment|// we could not bind
if|if
condition|(
literal|"off"
operator|.
name|equals
argument_list|(
name|bindingMode
argument_list|)
operator|||
name|bindingMode
operator|.
name|equals
argument_list|(
literal|"auto"
argument_list|)
condition|)
block|{
comment|// okay for auto we do not mind if we could not bind
name|state
operator|.
name|put
argument_list|(
name|STATE_KEY_DO_MARSHAL
argument_list|,
name|STATE_JSON
argument_list|)
expr_stmt|;
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
block|}
DECL|method|marshal (Exchange exchange, Map<String, Object> state)
specifier|private
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|state
parameter_list|)
block|{
comment|// only marshal if there was no exception
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
return|return;
block|}
if|if
condition|(
name|skipBindingOnErrorCode
condition|)
block|{
name|Integer
name|code
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
else|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// if there is a custom http error code then skip binding
if|if
condition|(
name|code
operator|!=
literal|null
operator|&&
name|code
operator|>=
literal|300
condition|)
block|{
return|return;
block|}
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
comment|// accept takes precedence
name|String
name|accept
init|=
operator|(
name|String
operator|)
name|state
operator|.
name|get
argument_list|(
name|STATE_KEY_ACCEPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|isXml
operator|=
name|accept
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
expr_stmt|;
name|isJson
operator|=
name|accept
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
comment|// fallback to content type if still undecided
if|if
condition|(
operator|!
name|isXml
operator|&&
operator|!
name|isJson
condition|)
block|{
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
name|ENGLISH
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
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
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
name|ENGLISH
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
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
block|}
comment|// only allow xml/json if the binding mode allows that (when off we still want to know if its xml or json)
if|if
condition|(
name|bindingMode
operator|!=
literal|null
condition|)
block|{
name|isXml
operator|&=
name|bindingMode
operator|.
name|equals
argument_list|(
literal|"off"
argument_list|)
operator|||
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
literal|"off"
argument_list|)
operator|||
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
comment|// if we do not yet know if its xml or json, then use the binding mode to know the mode
if|if
condition|(
operator|!
name|isJson
operator|&&
operator|!
name|isXml
condition|)
block|{
name|isXml
operator|=
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
operator|=
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
block|}
block|}
comment|// in case we have not yet been able to determine if xml or json, then use the same as in the unmarshaller
if|if
condition|(
name|isXml
operator|&&
name|isJson
condition|)
block|{
name|isXml
operator|=
name|state
operator|.
name|get
argument_list|(
name|STATE_KEY_DO_MARSHAL
argument_list|)
operator|.
name|equals
argument_list|(
name|STATE_XML
argument_list|)
expr_stmt|;
name|isJson
operator|=
operator|!
name|isXml
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
comment|// ensure there is a content type header (even if binding is off)
name|ensureHeaderContentType
argument_list|(
name|produces
argument_list|,
name|isXml
argument_list|,
name|isJson
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
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
comment|// binding is off, so no message body binding
return|return;
block|}
comment|// is there any marshaller at all
if|if
condition|(
name|jsonMarshal
operator|==
literal|null
operator|&&
name|xmlMarshal
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// is the body empty
if|if
condition|(
operator|(
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
operator|)
operator|||
operator|(
operator|!
name|exchange
operator|.
name|hasOut
argument_list|()
operator|&&
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
return|return;
block|}
name|String
name|contentType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// need to lower-case so the contains check below can match if using upper case
name|contentType
operator|=
name|contentType
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
try|try
block|{
comment|// favor json over xml
if|if
condition|(
name|isJson
operator|&&
name|jsonMarshal
operator|!=
literal|null
condition|)
block|{
comment|// only marshal if its json content type
if|if
condition|(
name|contentType
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
condition|)
block|{
name|jsonMarshal
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|setOutputDataType
argument_list|(
name|exchange
argument_list|,
operator|new
name|DataType
argument_list|(
literal|"json"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isXml
operator|&&
name|xmlMarshal
operator|!=
literal|null
condition|)
block|{
comment|// only marshal if its xml content type
if|if
condition|(
name|contentType
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
condition|)
block|{
name|xmlMarshal
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|setOutputDataType
argument_list|(
name|exchange
argument_list|,
operator|new
name|DataType
argument_list|(
literal|"xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|method|setOutputDataType (Exchange exchange, DataType type)
specifier|private
name|void
name|setOutputDataType
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DataType
name|type
parameter_list|)
block|{
name|Message
name|target
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|instanceof
name|DataTypeAware
condition|)
block|{
operator|(
operator|(
name|DataTypeAware
operator|)
name|target
operator|)
operator|.
name|setDataType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|ensureHeaderContentType (String contentType, boolean isXml, boolean isJson, Exchange exchange)
specifier|private
name|void
name|ensureHeaderContentType
parameter_list|(
name|String
name|contentType
parameter_list|,
name|boolean
name|isXml
parameter_list|,
name|boolean
name|isJson
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// favor given content type
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|String
name|type
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
name|type
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
block|}
block|}
comment|// favor json over xml
if|if
condition|(
name|isJson
condition|)
block|{
comment|// make sure there is a content-type with json
name|String
name|type
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
name|type
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isXml
condition|)
block|{
comment|// make sure there is a content-type with xml
name|String
name|type
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
name|type
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|setCORSHeaders (Exchange exchange, Map<String, Object> state)
specifier|private
name|void
name|setCORSHeaders
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|state
parameter_list|)
block|{
comment|// add the CORS headers after routing, but before the consumer writes the response
name|Message
name|msg
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// use default value if none has been configured
name|String
name|allowOrigin
init|=
name|corsHeaders
operator|!=
literal|null
condition|?
name|corsHeaders
operator|.
name|get
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|allowOrigin
operator|==
literal|null
condition|)
block|{
name|allowOrigin
operator|=
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_ORIGIN
expr_stmt|;
block|}
name|String
name|allowMethods
init|=
name|corsHeaders
operator|!=
literal|null
condition|?
name|corsHeaders
operator|.
name|get
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|allowMethods
operator|==
literal|null
condition|)
block|{
name|allowMethods
operator|=
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_METHODS
expr_stmt|;
block|}
name|String
name|allowHeaders
init|=
name|corsHeaders
operator|!=
literal|null
condition|?
name|corsHeaders
operator|.
name|get
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|allowHeaders
operator|==
literal|null
condition|)
block|{
name|allowHeaders
operator|=
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_HEADERS
expr_stmt|;
block|}
name|String
name|maxAge
init|=
name|corsHeaders
operator|!=
literal|null
condition|?
name|corsHeaders
operator|.
name|get
argument_list|(
literal|"Access-Control-Max-Age"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|maxAge
operator|==
literal|null
condition|)
block|{
name|maxAge
operator|=
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_MAX_AGE
expr_stmt|;
block|}
name|msg
operator|.
name|setHeader
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|,
name|allowOrigin
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|,
name|allowMethods
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|,
name|allowHeaders
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"Access-Control-Max-Age"
argument_list|,
name|maxAge
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

