begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|Metadata
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
name|support
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
name|support
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
name|support
operator|.
name|processor
operator|.
name|validation
operator|.
name|SchemaReader
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
name|support
operator|.
name|processor
operator|.
name|validation
operator|.
name|ValidatorErrorHandler
import|;
end_import

begin_comment
comment|/**  * Validates the payload of a message using XML Schema and JAXP Validation.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed ValidatorEndpoint"
argument_list|)
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.1.0"
argument_list|,
name|scheme
operator|=
literal|"validator"
argument_list|,
name|title
operator|=
literal|"Validator"
argument_list|,
name|syntax
operator|=
literal|"validator:resourceUri"
argument_list|,
name|producerOnly
operator|=
literal|true
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
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"URL to a local resource on the classpath, or a reference to lookup a bean in the Registry,"
operator|+
literal|" or a full URL to a remote resource or resource on the file system which contains the XSD to validate against."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Configures the W3C XML Schema Namespace URI."
argument_list|)
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
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom javax.xml.validation.SchemaFactory"
argument_list|)
DECL|field|schemaFactory
specifier|private
name|SchemaFactory
name|schemaFactory
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom org.apache.camel.processor.validation.ValidatorErrorHandler. The default error handler captures the errors and throws an exception."
argument_list|)
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
literal|"true"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Whether the Schema instance should be shared or not. This option is introduced to work around a JDK 1.6.x bug. Xerces should not have this issue."
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
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom LSResourceResolver.  Do not use together with resourceResolverFactory"
argument_list|)
DECL|field|resourceResolver
specifier|private
name|LSResourceResolver
name|resourceResolver
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom LSResourceResolver which depends on a dynamic endpoint resource URI. "
operator|+
comment|//
literal|"The default resource resolver factory resturns a resource resolver which can read files from the class path and file system. Do not use together with resourceResolver."
argument_list|)
DECL|field|resourceResolverFactory
specifier|private
name|ValidatorResourceResolverFactory
name|resourceResolverFactory
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Whether to fail if no body exists."
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
argument_list|,
name|description
operator|=
literal|"Whether to fail if no header exists when validating against a header."
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
argument_list|(
name|description
operator|=
literal|"To validate against a header instead of the message body."
argument_list|)
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
comment|/**      * We need a one-to-one relation between endpoint and schema reader in order      * to be able to clear the cached schema in the schema reader. See method      * {@link #clearCachedSchema}.      */
DECL|field|schemaReader
specifier|private
specifier|final
name|SchemaReader
name|schemaReader
decl_stmt|;
DECL|field|schemaReaderConfigured
specifier|private
specifier|volatile
name|boolean
name|schemaReaderConfigured
decl_stmt|;
DECL|method|ValidatorEndpoint ()
specifier|public
name|ValidatorEndpoint
parameter_list|()
block|{
name|schemaReader
operator|=
operator|new
name|SchemaReader
argument_list|()
expr_stmt|;
block|}
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
name|schemaReader
operator|=
operator|new
name|SchemaReader
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clears the cached schema, forcing to re-load the schema on next request"
argument_list|)
DECL|method|clearCachedSchema ()
specifier|public
name|void
name|clearCachedSchema
parameter_list|()
block|{
comment|// will cause to reload the schema
name|schemaReader
operator|.
name|setSchema
argument_list|(
literal|null
argument_list|)
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
if|if
condition|(
operator|!
name|schemaReaderConfigured
condition|)
block|{
if|if
condition|(
name|resourceResolver
operator|!=
literal|null
condition|)
block|{
name|schemaReader
operator|.
name|setResourceResolver
argument_list|(
name|resourceResolver
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resourceResolverFactory
operator|!=
literal|null
condition|)
block|{
name|resourceResolver
operator|=
name|resourceResolverFactory
operator|.
name|createResourceResolver
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
comment|// set the created resource resolver to the resourceResolver variable, so that it can
comment|// be accessed by the endpoint
name|schemaReader
operator|.
name|setResourceResolver
argument_list|(
name|resourceResolver
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|schemaReader
operator|.
name|setResourceResolver
argument_list|(
operator|new
name|DefaultValidatorResourceResolverFactory
argument_list|()
operator|.
name|createResourceResolver
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|schemaReader
operator|.
name|setSchemaLanguage
argument_list|(
name|getSchemaLanguage
argument_list|()
argument_list|)
expr_stmt|;
name|schemaReader
operator|.
name|setSchemaFactory
argument_list|(
name|getSchemaFactory
argument_list|()
argument_list|)
expr_stmt|;
comment|// force loading of schema at create time otherwise concurrent
comment|// processing could cause thread safe issues for the
comment|// javax.xml.validation.SchemaFactory
name|schemaReader
operator|.
name|loadSchema
argument_list|()
expr_stmt|;
comment|// configure only once
name|schemaReaderConfigured
operator|=
literal|true
expr_stmt|;
block|}
name|ValidatingProcessor
name|validator
init|=
operator|new
name|ValidatingProcessor
argument_list|(
name|schemaReader
argument_list|)
decl_stmt|;
name|configureValidator
argument_list|(
name|validator
argument_list|)
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
comment|/**      * URL to a local resource on the classpath,or a reference to lookup a bean in the Registry,      * or a full URL to a remote resource or resource on the file system which contains the XSD to validate against.      */
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
comment|/**      * Configures the W3C XML Schema Namespace URI.      */
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
comment|/**      * To use a custom javax.xml.validation.SchemaFactory      */
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
comment|/**      * To use a custom org.apache.camel.processor.validation.ValidatorErrorHandler.      *<p/>      * The default error handler captures the errors and throws an exception.      */
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
comment|/**      * Whether the Schema instance should be shared or not. This option is introduced to work around a JDK 1.6.x bug. Xerces should not have this issue.      */
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
comment|/**      * To use a custom LSResourceResolver. See also {@link #setResourceResolverFactory(ValidatorResourceResolverFactory)}      */
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
DECL|method|getResourceResolverFactory ()
specifier|public
name|ValidatorResourceResolverFactory
name|getResourceResolverFactory
parameter_list|()
block|{
return|return
name|resourceResolverFactory
return|;
block|}
comment|/** For creating a resource resolver which depends on the endpoint resource URI.       * Must not be used in combination with method {@link #setResourceResolver(LSResourceResolver)}.       * If not set then {@link DefaultValidatorResourceResolverFactory} is used       */
DECL|method|setResourceResolverFactory (ValidatorResourceResolverFactory resourceResolverFactory)
specifier|public
name|void
name|setResourceResolverFactory
parameter_list|(
name|ValidatorResourceResolverFactory
name|resourceResolverFactory
parameter_list|)
block|{
name|this
operator|.
name|resourceResolverFactory
operator|=
name|resourceResolverFactory
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
comment|/**      * Whether to fail if no body exists.      */
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
comment|/**      * Whether to fail if no header exists when validating against a header.      */
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
comment|/**      * To validate against a header instead of the message body.      */
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

