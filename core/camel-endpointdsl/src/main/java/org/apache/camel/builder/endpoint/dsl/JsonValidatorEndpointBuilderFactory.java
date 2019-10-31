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
comment|/**  * Validates the payload of a message using NetworkNT JSON Schema library.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|JsonValidatorEndpointBuilderFactory
specifier|public
interface|interface
name|JsonValidatorEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the JSON Schema Validator component.      */
DECL|interface|JsonValidatorEndpointBuilder
specifier|public
interface|interface
name|JsonValidatorEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJsonValidatorEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJsonValidatorEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Sets whether to use resource content cache or not.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|contentCache (boolean contentCache)
specifier|default
name|JsonValidatorEndpointBuilder
name|contentCache
parameter_list|(
name|boolean
name|contentCache
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"contentCache"
argument_list|,
name|contentCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use resource content cache or not.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|contentCache (String contentCache)
specifier|default
name|JsonValidatorEndpointBuilder
name|contentCache
parameter_list|(
name|String
name|contentCache
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"contentCache"
argument_list|,
name|contentCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to fail if no body exists.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|failOnNullBody ( boolean failOnNullBody)
specifier|default
name|JsonValidatorEndpointBuilder
name|failOnNullBody
parameter_list|(
name|boolean
name|failOnNullBody
parameter_list|)
block|{
name|doSetProperty
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
DECL|method|failOnNullBody ( String failOnNullBody)
specifier|default
name|JsonValidatorEndpointBuilder
name|failOnNullBody
parameter_list|(
name|String
name|failOnNullBody
parameter_list|)
block|{
name|doSetProperty
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
DECL|method|failOnNullHeader ( boolean failOnNullHeader)
specifier|default
name|JsonValidatorEndpointBuilder
name|failOnNullHeader
parameter_list|(
name|boolean
name|failOnNullHeader
parameter_list|)
block|{
name|doSetProperty
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
DECL|method|failOnNullHeader ( String failOnNullHeader)
specifier|default
name|JsonValidatorEndpointBuilder
name|failOnNullHeader
parameter_list|(
name|String
name|failOnNullHeader
parameter_list|)
block|{
name|doSetProperty
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
name|JsonValidatorEndpointBuilder
name|headerName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|JsonValidatorEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|default
name|JsonValidatorEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the JSON Schema Validator component.      */
DECL|interface|AdvancedJsonValidatorEndpointBuilder
specifier|public
interface|interface
name|AdvancedJsonValidatorEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JsonValidatorEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JsonValidatorEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJsonValidatorEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedJsonValidatorEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * To use a custom ValidatorErrorHandler. The default error handler          * captures the errors and throws an exception.          *           * The option is a:          *<code>org.apache.camel.component.jsonvalidator.JsonValidatorErrorHandler</code> type.          *           * Group: advanced          */
DECL|method|errorHandler ( Object errorHandler)
specifier|default
name|AdvancedJsonValidatorEndpointBuilder
name|errorHandler
parameter_list|(
name|Object
name|errorHandler
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * To use a custom ValidatorErrorHandler. The default error handler          * captures the errors and throws an exception.          *           * The option will be converted to a          *<code>org.apache.camel.component.jsonvalidator.JsonValidatorErrorHandler</code> type.          *           * Group: advanced          */
DECL|method|errorHandler ( String errorHandler)
specifier|default
name|AdvancedJsonValidatorEndpointBuilder
name|errorHandler
parameter_list|(
name|String
name|errorHandler
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * To use a custom schema loader allowing for adding custom format          * validation. The default implementation will create a schema loader          * with draft v4 support.          *           * The option is a:          *<code>org.apache.camel.component.jsonvalidator.JsonSchemaLoader</code> type.          *           * Group: advanced          */
DECL|method|schemaLoader ( Object schemaLoader)
specifier|default
name|AdvancedJsonValidatorEndpointBuilder
name|schemaLoader
parameter_list|(
name|Object
name|schemaLoader
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"schemaLoader"
argument_list|,
name|schemaLoader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom schema loader allowing for adding custom format          * validation. The default implementation will create a schema loader          * with draft v4 support.          *           * The option will be converted to a          *<code>org.apache.camel.component.jsonvalidator.JsonSchemaLoader</code> type.          *           * Group: advanced          */
DECL|method|schemaLoader ( String schemaLoader)
specifier|default
name|AdvancedJsonValidatorEndpointBuilder
name|schemaLoader
parameter_list|(
name|String
name|schemaLoader
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"schemaLoader"
argument_list|,
name|schemaLoader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedJsonValidatorEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedJsonValidatorEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
block|}
comment|/**      * JSON Schema Validator (camel-json-validator)      * Validates the payload of a message using NetworkNT JSON Schema library.      *       * Category: validation      * Available as of version: 2.20      * Maven coordinates: org.apache.camel:camel-json-validator      *       * Syntax:<code>json-validator:resourceUri</code>      *       * Path parameter: resourceUri (required)      * Path to the resource. You can prefix with: classpath, file, http, ref, or      * bean. classpath, file and http loads the resource using these protocols      * (classpath is default). ref will lookup the resource in the registry.      * bean will call a method on a bean to be used as the resource. For bean      * you can specify the method name after dot, eg bean:myBean.myMethod.      */
DECL|method|jsonValidator (String path)
specifier|default
name|JsonValidatorEndpointBuilder
name|jsonValidator
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|JsonValidatorEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|JsonValidatorEndpointBuilder
implements|,
name|AdvancedJsonValidatorEndpointBuilder
block|{
specifier|public
name|JsonValidatorEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"json-validator"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|JsonValidatorEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

