begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * Validates the payload of a message using the MSV Library.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|MsvEndpointBuilderFactory
specifier|public
interface|interface
name|MsvEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the MSV component.      */
DECL|interface|MsvEndpointBuilder
specifier|public
interface|interface
name|MsvEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedMsvEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedMsvEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * URL to a local resource on the classpath,or a reference to lookup a          * bean in the Registry, or a full URL to a remote resource or resource          * on the file system which contains the XSD to validate against.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|resourceUri (String resourceUri)
specifier|default
name|MsvEndpointBuilder
name|resourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceUri"
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to fail if no body exists.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failOnNullBody (boolean failOnNullBody)
specifier|default
name|MsvEndpointBuilder
name|failOnNullBody
parameter_list|(
name|boolean
name|failOnNullBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"failOnNullBody"
argument_list|,
name|failOnNullBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to fail if no body exists.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failOnNullBody (String failOnNullBody)
specifier|default
name|MsvEndpointBuilder
name|failOnNullBody
parameter_list|(
name|String
name|failOnNullBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"failOnNullBody"
argument_list|,
name|failOnNullBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to fail if no header exists when validating against a header.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failOnNullHeader (boolean failOnNullHeader)
specifier|default
name|MsvEndpointBuilder
name|failOnNullHeader
parameter_list|(
name|boolean
name|failOnNullHeader
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"failOnNullHeader"
argument_list|,
name|failOnNullHeader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to fail if no header exists when validating against a header.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failOnNullHeader (String failOnNullHeader)
specifier|default
name|MsvEndpointBuilder
name|failOnNullHeader
parameter_list|(
name|String
name|failOnNullHeader
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"failOnNullHeader"
argument_list|,
name|failOnNullHeader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To validate against a header instead of the message body.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|headerName (String headerName)
specifier|default
name|MsvEndpointBuilder
name|headerName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"headerName"
argument_list|,
name|headerName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the MSV component.      */
DECL|interface|AdvancedMsvEndpointBuilder
specifier|public
interface|interface
name|AdvancedMsvEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|MsvEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|MsvEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedMsvEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedMsvEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom          * org.apache.camel.processor.validation.ValidatorErrorHandler. The          * default error handler captures the errors and throws an exception.          *           * The option is a:          *<code>org.apache.camel.support.processor.validation.ValidatorErrorHandler</code> type.          *           * Group: advanced          */
DECL|method|errorHandler (Object errorHandler)
specifier|default
name|AdvancedMsvEndpointBuilder
name|errorHandler
parameter_list|(
name|Object
name|errorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"errorHandler"
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom          * org.apache.camel.processor.validation.ValidatorErrorHandler. The          * default error handler captures the errors and throws an exception.          *           * The option will be converted to a          *<code>org.apache.camel.support.processor.validation.ValidatorErrorHandler</code> type.          *           * Group: advanced          */
DECL|method|errorHandler (String errorHandler)
specifier|default
name|AdvancedMsvEndpointBuilder
name|errorHandler
parameter_list|(
name|String
name|errorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"errorHandler"
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom LSResourceResolver. See also          * setResourceResolverFactory(ValidatorResourceResolverFactory).          *           * The option is a:<code>org.w3c.dom.ls.LSResourceResolver</code> type.          *           * Group: advanced          */
DECL|method|resourceResolver ( Object resourceResolver)
specifier|default
name|AdvancedMsvEndpointBuilder
name|resourceResolver
parameter_list|(
name|Object
name|resourceResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceResolver"
argument_list|,
name|resourceResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom LSResourceResolver. See also          * setResourceResolverFactory(ValidatorResourceResolverFactory).          *           * The option will be converted to a          *<code>org.w3c.dom.ls.LSResourceResolver</code> type.          *           * Group: advanced          */
DECL|method|resourceResolver ( String resourceResolver)
specifier|default
name|AdvancedMsvEndpointBuilder
name|resourceResolver
parameter_list|(
name|String
name|resourceResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceResolver"
argument_list|,
name|resourceResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * For creating a resource resolver which depends on the endpoint          * resource URI. Must not be used in combination with method          * setResourceResolver(LSResourceResolver). If not set then          * DefaultValidatorResourceResolverFactory is used.          *           * The option is a:          *<code>org.apache.camel.component.validator.ValidatorResourceResolverFactory</code> type.          *           * Group: advanced          */
DECL|method|resourceResolverFactory ( Object resourceResolverFactory)
specifier|default
name|AdvancedMsvEndpointBuilder
name|resourceResolverFactory
parameter_list|(
name|Object
name|resourceResolverFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceResolverFactory"
argument_list|,
name|resourceResolverFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * For creating a resource resolver which depends on the endpoint          * resource URI. Must not be used in combination with method          * setResourceResolver(LSResourceResolver). If not set then          * DefaultValidatorResourceResolverFactory is used.          *           * The option will be converted to a          *<code>org.apache.camel.component.validator.ValidatorResourceResolverFactory</code> type.          *           * Group: advanced          */
DECL|method|resourceResolverFactory ( String resourceResolverFactory)
specifier|default
name|AdvancedMsvEndpointBuilder
name|resourceResolverFactory
parameter_list|(
name|String
name|resourceResolverFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceResolverFactory"
argument_list|,
name|resourceResolverFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom javax.xml.validation.SchemaFactory.          *           * The option is a:<code>javax.xml.validation.SchemaFactory</code>          * type.          *           * Group: advanced          */
DECL|method|schemaFactory (Object schemaFactory)
specifier|default
name|AdvancedMsvEndpointBuilder
name|schemaFactory
parameter_list|(
name|Object
name|schemaFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"schemaFactory"
argument_list|,
name|schemaFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom javax.xml.validation.SchemaFactory.          *           * The option will be converted to a          *<code>javax.xml.validation.SchemaFactory</code> type.          *           * Group: advanced          */
DECL|method|schemaFactory (String schemaFactory)
specifier|default
name|AdvancedMsvEndpointBuilder
name|schemaFactory
parameter_list|(
name|String
name|schemaFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"schemaFactory"
argument_list|,
name|schemaFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Configures the W3C XML Schema Namespace URI.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: advanced          */
DECL|method|schemaLanguage (String schemaLanguage)
specifier|default
name|AdvancedMsvEndpointBuilder
name|schemaLanguage
parameter_list|(
name|String
name|schemaLanguage
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"schemaLanguage"
argument_list|,
name|schemaLanguage
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedMsvEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedMsvEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the Schema instance should be shared or not. This option is          * introduced to work around a JDK 1.6.x bug. Xerces should not have          * this issue.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|useSharedSchema ( boolean useSharedSchema)
specifier|default
name|AdvancedMsvEndpointBuilder
name|useSharedSchema
parameter_list|(
name|boolean
name|useSharedSchema
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"useSharedSchema"
argument_list|,
name|useSharedSchema
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the Schema instance should be shared or not. This option is          * introduced to work around a JDK 1.6.x bug. Xerces should not have          * this issue.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|useSharedSchema ( String useSharedSchema)
specifier|default
name|AdvancedMsvEndpointBuilder
name|useSharedSchema
parameter_list|(
name|String
name|useSharedSchema
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"useSharedSchema"
argument_list|,
name|useSharedSchema
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * MSV (camel-msv)      * Validates the payload of a message using the MSV Library.      *       * Syntax:<code>msv:resourceUri</code>      * Category: validation      * Available as of version: 1.1      * Maven coordinates: org.apache.camel:camel-msv      */
DECL|method|msv (String path)
specifier|default
name|MsvEndpointBuilder
name|msv
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|MsvEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|MsvEndpointBuilder
implements|,
name|AdvancedMsvEndpointBuilder
block|{
specifier|public
name|MsvEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"msv"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MsvEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

