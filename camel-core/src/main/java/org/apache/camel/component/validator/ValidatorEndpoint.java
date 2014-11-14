begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
package|;
end_package

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
name|validation
operator|.
name|SchemaFactory
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
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|IOConverter
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
name|DefaultEndpoint
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
name|validation
operator|.
name|DefaultValidationErrorHandler
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
name|validation
operator|.
name|ValidatingProcessor
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
name|validation
operator|.
name|ValidatorErrorHandler
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|ResourceHelper
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"validator"
argument_list|,
name|label
operator|=
literal|"core,validation"
argument_list|)
DECL|class|ValidatorEndpoint
specifier|public
class|class
name|ValidatorEndpoint
extends|extends
name|DefaultEndpoint
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
name|ValidatorEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriPath
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
annotation|@
name|UriParam
DECL|field|schemaLanguage
specifier|private
name|String
name|schemaLanguage
init|=
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
decl_stmt|;
annotation|@
name|UriParam
DECL|field|schemaFactory
specifier|private
name|SchemaFactory
name|schemaFactory
decl_stmt|;
annotation|@
name|UriParam
DECL|field|errorHandler
specifier|private
name|ValidatorErrorHandler
name|errorHandler
init|=
operator|new
name|DefaultValidationErrorHandler
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useDom
specifier|private
name|boolean
name|useDom
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|useSharedSchema
specifier|private
name|boolean
name|useSharedSchema
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|resourceResolver
specifier|private
name|LSResourceResolver
name|resourceResolver
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|failOnNullBody
specifier|private
name|boolean
name|failOnNullBody
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|failOnNullHeader
specifier|private
name|boolean
name|failOnNullHeader
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
DECL|method|ValidatorEndpoint ()
specifier|public
name|ValidatorEndpoint
parameter_list|()
block|{     }
DECL|method|ValidatorEndpoint (String endpointUri, Component component, String resourceUri)
specifier|public
name|ValidatorEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ValidatingProcessor
name|validator
init|=
operator|new
name|ValidatingProcessor
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bytes
operator|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// and make sure to close the input stream after the schema has been loaded
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|validator
operator|.
name|setSchemaAsByteArray
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} using schema resource: {}"
argument_list|,
name|this
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|configureValidator
argument_list|(
name|validator
argument_list|)
expr_stmt|;
comment|// force loading of schema at create time otherwise concurrent
comment|// processing could cause thread safe issues for the javax.xml.validation.SchemaFactory
name|validator
operator|.
name|loadSchema
argument_list|()
expr_stmt|;
return|return
operator|new
name|ValidatorProducer
argument_list|(
name|this
argument_list|,
name|validator
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot consume from validator"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|configureValidator (ValidatingProcessor validator)
specifier|protected
name|void
name|configureValidator
parameter_list|(
name|ValidatingProcessor
name|validator
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|resourceResolver
operator|!=
literal|null
condition|)
block|{
name|validator
operator|.
name|setResourceResolver
argument_list|(
name|resourceResolver
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|validator
operator|.
name|setResourceResolver
argument_list|(
operator|new
name|DefaultLSResourceResolver
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|validator
operator|.
name|setSchemaLanguage
argument_list|(
name|getSchemaLanguage
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setSchemaFactory
argument_list|(
name|getSchemaFactory
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setErrorHandler
argument_list|(
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setUseDom
argument_list|(
name|isUseDom
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setUseSharedSchema
argument_list|(
name|isUseSharedSchema
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setFailOnNullBody
argument_list|(
name|isFailOnNullBody
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setFailOnNullHeader
argument_list|(
name|isFailOnNullHeader
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setHeaderName
argument_list|(
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getResourceUri ()
specifier|public
name|String
name|getResourceUri
parameter_list|()
block|{
return|return
name|resourceUri
return|;
block|}
comment|/**      * URL to a local resource on the classpath or a full URL to a remote resource or resource on the file system which contains the XSD to validate against.      */
DECL|method|setResourceUri (String resourceUri)
specifier|public
name|void
name|setResourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
DECL|method|getSchemaLanguage ()
specifier|public
name|String
name|getSchemaLanguage
parameter_list|()
block|{
return|return
name|schemaLanguage
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
name|this
operator|.
name|schemaLanguage
operator|=
name|schemaLanguage
expr_stmt|;
block|}
DECL|method|getSchemaFactory ()
specifier|public
name|SchemaFactory
name|getSchemaFactory
parameter_list|()
block|{
return|return
name|schemaFactory
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
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
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
name|resourceResolver
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
name|this
operator|.
name|resourceResolver
operator|=
name|resourceResolver
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
block|}
end_class

end_unit

