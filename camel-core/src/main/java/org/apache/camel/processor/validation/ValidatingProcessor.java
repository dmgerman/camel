begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.validation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|validation
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|validation
operator|.
name|SchemaReader
operator|.
name|ACCESS_EXTERNAL_DTD
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stax
operator|.
name|StAXSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|SchemaFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Validator
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
name|ExpectedBodyTypeException
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
name|RuntimeTransformException
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|IOHelper
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSResourceResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
import|;
end_import

begin_comment
comment|/**  * A processor which validates the XML version of the inbound message body  * against some schema either in XSD or RelaxNG  */
end_comment

begin_class
DECL|class|ValidatingProcessor
specifier|public
class|class
name|ValidatingProcessor
implements|implements
name|AsyncProcessor
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
name|ValidatingProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|schemaReader
specifier|private
specifier|final
name|SchemaReader
name|schemaReader
decl_stmt|;
DECL|field|errorHandler
specifier|private
name|ValidatorErrorHandler
name|errorHandler
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
DECL|field|converter
specifier|private
specifier|final
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
DECL|field|useDom
specifier|private
name|boolean
name|useDom
decl_stmt|;
DECL|field|useSharedSchema
specifier|private
name|boolean
name|useSharedSchema
init|=
literal|true
decl_stmt|;
DECL|field|failOnNullBody
specifier|private
name|boolean
name|failOnNullBody
init|=
literal|true
decl_stmt|;
DECL|field|failOnNullHeader
specifier|private
name|boolean
name|failOnNullHeader
init|=
literal|true
decl_stmt|;
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
DECL|method|ValidatingProcessor ()
specifier|public
name|ValidatingProcessor
parameter_list|()
block|{
name|schemaReader
operator|=
operator|new
name|SchemaReader
argument_list|()
expr_stmt|;
block|}
DECL|method|ValidatingProcessor (SchemaReader schemaReader)
specifier|public
name|ValidatingProcessor
parameter_list|(
name|SchemaReader
name|schemaReader
parameter_list|)
block|{
comment|// schema reader can be a singelton per schema, therefore make reuse, see ValidatorEndpoint and ValidatorProducer
name|this
operator|.
name|schemaReader
operator|=
name|schemaReader
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
name|doProcess
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
DECL|method|doProcess (Exchange exchange)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Schema
name|schema
decl_stmt|;
if|if
condition|(
name|isUseSharedSchema
argument_list|()
condition|)
block|{
name|schema
operator|=
name|getSchema
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|schema
operator|=
name|createSchema
argument_list|()
expr_stmt|;
block|}
name|Validator
name|validator
init|=
name|schema
operator|.
name|newValidator
argument_list|()
decl_stmt|;
comment|// turn off access to external schema by default
if|if
condition|(
operator|!
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|get
argument_list|(
name|ACCESS_EXTERNAL_DTD
argument_list|)
argument_list|)
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Configuring Validator to not allow access to external DTD/Schema"
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setProperty
argument_list|(
name|XMLConstants
operator|.
name|ACCESS_EXTERNAL_DTD
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setProperty
argument_list|(
name|XMLConstants
operator|.
name|ACCESS_EXTERNAL_SCHEMA
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// the underlying input stream, which we need to close to avoid locking files or other resources
name|Source
name|source
init|=
literal|null
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Result
name|result
init|=
literal|null
decl_stmt|;
comment|// only convert to input stream if really needed
if|if
condition|(
name|isInputStreamNeeded
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|is
operator|=
name|getContentToValidate
argument_list|(
name|exchange
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|source
operator|=
name|getSource
argument_list|(
name|exchange
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Object
name|content
init|=
name|getContentToValidate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
name|source
operator|=
name|getSource
argument_list|(
name|exchange
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|shouldUseHeader
argument_list|()
condition|)
block|{
if|if
condition|(
name|source
operator|==
literal|null
operator|&&
name|isFailOnNullHeader
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoXmlHeaderValidationException
argument_list|(
name|exchange
argument_list|,
name|headerName
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|source
operator|==
literal|null
operator|&&
name|isFailOnNullBody
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoXmlBodyValidationException
argument_list|(
name|exchange
argument_list|)
throw|;
block|}
block|}
comment|//CAMEL-7036 We don't need to set the result if the source is an instance of StreamSource
if|if
condition|(
name|source
operator|instanceof
name|DOMSource
condition|)
block|{
name|result
operator|=
operator|new
name|DOMResult
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|source
operator|instanceof
name|SAXSource
condition|)
block|{
name|result
operator|=
operator|new
name|SAXResult
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|source
operator|instanceof
name|StAXSource
operator|||
name|source
operator|instanceof
name|StreamSource
condition|)
block|{
name|result
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|source
operator|!=
literal|null
condition|)
block|{
comment|// create a new errorHandler and set it on the validator
comment|// must be a local instance to avoid problems with concurrency (to be
comment|// thread safe)
name|ValidatorErrorHandler
name|handler
init|=
name|errorHandler
operator|.
name|getClass
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|validator
operator|.
name|setErrorHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
try|try
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Validating {}"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|validator
operator|.
name|validate
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handleErrors
argument_list|(
name|exchange
argument_list|,
name|schema
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
block|{
comment|// can be thrown for non well formed XML
throw|throw
operator|new
name|SchemaValidationException
argument_list|(
name|exchange
argument_list|,
name|schema
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|e
argument_list|)
argument_list|,
name|Collections
operator|.
expr|<
name|SAXParseException
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|SAXParseException
operator|>
name|emptyList
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getContentToValidate (Exchange exchange)
specifier|private
name|Object
name|getContentToValidate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|shouldUseHeader
argument_list|()
condition|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
DECL|method|getContentToValidate (Exchange exchange, Class<T> clazz)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|getContentToValidate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|shouldUseHeader
argument_list|()
condition|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|,
name|clazz
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|clazz
argument_list|)
return|;
block|}
block|}
DECL|method|shouldUseHeader ()
specifier|private
name|boolean
name|shouldUseHeader
parameter_list|()
block|{
return|return
name|headerName
operator|!=
literal|null
return|;
block|}
DECL|method|loadSchema ()
specifier|public
name|void
name|loadSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|schemaReader
operator|.
name|loadSchema
argument_list|()
expr_stmt|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getSchema ()
specifier|public
name|Schema
name|getSchema
parameter_list|()
throws|throws
name|IOException
throws|,
name|SAXException
block|{
return|return
name|schemaReader
operator|.
name|getSchema
argument_list|()
return|;
block|}
DECL|method|setSchema (Schema schema)
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|schemaReader
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
DECL|method|getSchemaLanguage ()
specifier|public
name|String
name|getSchemaLanguage
parameter_list|()
block|{
return|return
name|schemaReader
operator|.
name|getSchemaLanguage
argument_list|()
return|;
block|}
DECL|method|setSchemaLanguage (String schemaLanguage)
specifier|public
name|void
name|setSchemaLanguage
parameter_list|(
name|String
name|schemaLanguage
parameter_list|)
block|{
name|schemaReader
operator|.
name|setSchemaLanguage
argument_list|(
name|schemaLanguage
argument_list|)
expr_stmt|;
block|}
DECL|method|getSchemaSource ()
specifier|public
name|Source
name|getSchemaSource
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|schemaReader
operator|.
name|getSchemaSource
argument_list|()
return|;
block|}
DECL|method|setSchemaSource (Source schemaSource)
specifier|public
name|void
name|setSchemaSource
parameter_list|(
name|Source
name|schemaSource
parameter_list|)
block|{
name|schemaReader
operator|.
name|setSchemaSource
argument_list|(
name|schemaSource
argument_list|)
expr_stmt|;
block|}
DECL|method|getSchemaUrl ()
specifier|public
name|URL
name|getSchemaUrl
parameter_list|()
block|{
return|return
name|schemaReader
operator|.
name|getSchemaUrl
argument_list|()
return|;
block|}
DECL|method|setSchemaUrl (URL schemaUrl)
specifier|public
name|void
name|setSchemaUrl
parameter_list|(
name|URL
name|schemaUrl
parameter_list|)
block|{
name|schemaReader
operator|.
name|setSchemaUrl
argument_list|(
name|schemaUrl
argument_list|)
expr_stmt|;
block|}
DECL|method|getSchemaFile ()
specifier|public
name|File
name|getSchemaFile
parameter_list|()
block|{
return|return
name|schemaReader
operator|.
name|getSchemaFile
argument_list|()
return|;
block|}
DECL|method|setSchemaFile (File schemaFile)
specifier|public
name|void
name|setSchemaFile
parameter_list|(
name|File
name|schemaFile
parameter_list|)
block|{
name|schemaReader
operator|.
name|setSchemaFile
argument_list|(
name|schemaFile
argument_list|)
expr_stmt|;
block|}
DECL|method|getSchemaAsByteArray ()
specifier|public
name|byte
index|[]
name|getSchemaAsByteArray
parameter_list|()
block|{
return|return
name|schemaReader
operator|.
name|getSchemaAsByteArray
argument_list|()
return|;
block|}
DECL|method|setSchemaAsByteArray (byte[] schemaAsByteArray)
specifier|public
name|void
name|setSchemaAsByteArray
parameter_list|(
name|byte
index|[]
name|schemaAsByteArray
parameter_list|)
block|{
name|schemaReader
operator|.
name|setSchemaAsByteArray
argument_list|(
name|schemaAsByteArray
argument_list|)
expr_stmt|;
block|}
DECL|method|getSchemaFactory ()
specifier|public
name|SchemaFactory
name|getSchemaFactory
parameter_list|()
block|{
return|return
name|schemaReader
operator|.
name|getSchemaFactory
argument_list|()
return|;
block|}
DECL|method|setSchemaFactory (SchemaFactory schemaFactory)
specifier|public
name|void
name|setSchemaFactory
parameter_list|(
name|SchemaFactory
name|schemaFactory
parameter_list|)
block|{
name|schemaReader
operator|.
name|setSchemaFactory
argument_list|(
name|schemaFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|getErrorHandler ()
specifier|public
name|ValidatorErrorHandler
name|getErrorHandler
parameter_list|()
block|{
return|return
name|errorHandler
return|;
block|}
DECL|method|setErrorHandler (ValidatorErrorHandler errorHandler)
specifier|public
name|void
name|setErrorHandler
parameter_list|(
name|ValidatorErrorHandler
name|errorHandler
parameter_list|)
block|{
name|this
operator|.
name|errorHandler
operator|=
name|errorHandler
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|isUseDom ()
specifier|public
name|boolean
name|isUseDom
parameter_list|()
block|{
return|return
name|useDom
return|;
block|}
comment|/**      * Sets whether DOMSource and DOMResult should be used.      *      * @param useDom true to use DOM otherwise      */
annotation|@
name|Deprecated
DECL|method|setUseDom (boolean useDom)
specifier|public
name|void
name|setUseDom
parameter_list|(
name|boolean
name|useDom
parameter_list|)
block|{
name|this
operator|.
name|useDom
operator|=
name|useDom
expr_stmt|;
block|}
DECL|method|isUseSharedSchema ()
specifier|public
name|boolean
name|isUseSharedSchema
parameter_list|()
block|{
return|return
name|useSharedSchema
return|;
block|}
DECL|method|setUseSharedSchema (boolean useSharedSchema)
specifier|public
name|void
name|setUseSharedSchema
parameter_list|(
name|boolean
name|useSharedSchema
parameter_list|)
block|{
name|this
operator|.
name|useSharedSchema
operator|=
name|useSharedSchema
expr_stmt|;
block|}
DECL|method|getResourceResolver ()
specifier|public
name|LSResourceResolver
name|getResourceResolver
parameter_list|()
block|{
return|return
name|schemaReader
operator|.
name|getResourceResolver
argument_list|()
return|;
block|}
DECL|method|setResourceResolver (LSResourceResolver resourceResolver)
specifier|public
name|void
name|setResourceResolver
parameter_list|(
name|LSResourceResolver
name|resourceResolver
parameter_list|)
block|{
name|schemaReader
operator|.
name|setResourceResolver
argument_list|(
name|resourceResolver
argument_list|)
expr_stmt|;
block|}
DECL|method|isFailOnNullBody ()
specifier|public
name|boolean
name|isFailOnNullBody
parameter_list|()
block|{
return|return
name|failOnNullBody
return|;
block|}
DECL|method|setFailOnNullBody (boolean failOnNullBody)
specifier|public
name|void
name|setFailOnNullBody
parameter_list|(
name|boolean
name|failOnNullBody
parameter_list|)
block|{
name|this
operator|.
name|failOnNullBody
operator|=
name|failOnNullBody
expr_stmt|;
block|}
DECL|method|isFailOnNullHeader ()
specifier|public
name|boolean
name|isFailOnNullHeader
parameter_list|()
block|{
return|return
name|failOnNullHeader
return|;
block|}
DECL|method|setFailOnNullHeader (boolean failOnNullHeader)
specifier|public
name|void
name|setFailOnNullHeader
parameter_list|(
name|boolean
name|failOnNullHeader
parameter_list|)
block|{
name|this
operator|.
name|failOnNullHeader
operator|=
name|failOnNullHeader
expr_stmt|;
block|}
DECL|method|getHeaderName ()
specifier|public
name|String
name|getHeaderName
parameter_list|()
block|{
return|return
name|headerName
return|;
block|}
DECL|method|setHeaderName (String headerName)
specifier|public
name|void
name|setHeaderName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
operator|.
name|headerName
operator|=
name|headerName
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
DECL|method|createSchemaFactory ()
specifier|protected
name|SchemaFactory
name|createSchemaFactory
parameter_list|()
block|{
return|return
name|schemaReader
operator|.
name|createSchemaFactory
argument_list|()
return|;
block|}
DECL|method|createSchemaSource ()
specifier|protected
name|Source
name|createSchemaSource
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|schemaReader
operator|.
name|createSchemaSource
argument_list|()
return|;
block|}
DECL|method|createSchema ()
specifier|protected
name|Schema
name|createSchema
parameter_list|()
throws|throws
name|SAXException
throws|,
name|IOException
block|{
return|return
name|schemaReader
operator|.
name|createSchema
argument_list|()
return|;
block|}
comment|/**      * Checks whether we need an {@link InputStream} to access the message body or header.      *<p/>      * Depending on the content in the message body or header, we may not need to convert      * to {@link InputStream}.      *      * @param exchange the current exchange      * @return<tt>true</tt> to convert to {@link InputStream} beforehand converting to {@link Source} afterwards.      */
DECL|method|isInputStreamNeeded (Exchange exchange)
specifier|protected
name|boolean
name|isInputStreamNeeded
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|content
init|=
name|getContentToValidate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|content
operator|instanceof
name|InputStream
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|content
operator|instanceof
name|Source
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|content
operator|instanceof
name|String
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|content
operator|instanceof
name|byte
index|[]
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|content
operator|instanceof
name|Node
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|content
operator|.
name|getClass
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|//there is a direct and hopefully optimized converter to Source
return|return
literal|false
return|;
block|}
comment|// yes an input stream is needed
return|return
literal|true
return|;
block|}
comment|/**      * Converts the inbound body or header to a {@link Source}, if it is<b>not</b> already a {@link Source}.      *<p/>      * This implementation will prefer to source in the following order:      *<ul>      *<li>DOM - DOM if explicit configured to use DOM</li>      *<li>SAX - SAX as 2nd choice</li>      *<li>Stream - Stream as 3rd choice</li>      *<li>DOM - DOM as 4th choice</li>      *</ul>      */
DECL|method|getSource (Exchange exchange, Object content)
specifier|protected
name|Source
name|getSource
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|content
parameter_list|)
block|{
if|if
condition|(
name|isUseDom
argument_list|()
condition|)
block|{
comment|// force DOM
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|content
argument_list|)
return|;
block|}
comment|// body or header may already be a source
if|if
condition|(
name|content
operator|instanceof
name|Source
condition|)
block|{
return|return
operator|(
name|Source
operator|)
name|content
return|;
block|}
name|Source
name|source
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|InputStream
condition|)
block|{
return|return
operator|new
name|StreamSource
argument_list|(
operator|(
name|InputStream
operator|)
name|content
argument_list|)
return|;
block|}
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
name|TypeConverter
name|tc
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|content
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|source
operator|=
name|tc
operator|.
name|convertTo
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
comment|// then try SAX
name|source
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|SAXSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
comment|// then try stream
name|source
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|StreamSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
comment|// and fallback to DOM
name|source
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isFailOnNullBody
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ExpectedBodyTypeException
argument_list|(
name|exchange
argument_list|,
name|Source
operator|.
name|class
argument_list|)
throw|;
block|}
else|else
block|{
try|try
block|{
name|source
operator|=
name|converter
operator|.
name|toDOMSource
argument_list|(
name|converter
operator|.
name|createDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeTransformException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|source
return|;
block|}
block|}
end_class

end_unit

