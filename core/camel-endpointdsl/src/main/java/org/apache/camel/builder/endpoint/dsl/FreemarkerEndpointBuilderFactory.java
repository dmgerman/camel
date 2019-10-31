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
comment|/**  * Transforms the message using a FreeMarker template.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|FreemarkerEndpointBuilderFactory
specifier|public
interface|interface
name|FreemarkerEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Freemarker component.      */
DECL|interface|FreemarkerEndpointBuilder
specifier|public
interface|interface
name|FreemarkerEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedFreemarkerEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedFreemarkerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Sets the Freemarker configuration to use.          *           * The option is a:<code>freemarker.template.Configuration</code> type.          *           * Group: producer          */
DECL|method|configuration (Object configuration)
specifier|default
name|FreemarkerEndpointBuilder
name|configuration
parameter_list|(
name|Object
name|configuration
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"configuration"
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the Freemarker configuration to use.          *           * The option will be converted to a          *<code>freemarker.template.Configuration</code> type.          *           * Group: producer          */
DECL|method|configuration (String configuration)
specifier|default
name|FreemarkerEndpointBuilder
name|configuration
parameter_list|(
name|String
name|configuration
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"configuration"
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use resource content cache or not.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|contentCache (boolean contentCache)
specifier|default
name|FreemarkerEndpointBuilder
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
name|FreemarkerEndpointBuilder
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
comment|/**          * Sets the encoding to be used for loading the template file.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|encoding (String encoding)
specifier|default
name|FreemarkerEndpointBuilder
name|encoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"encoding"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|FreemarkerEndpointBuilder
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
name|FreemarkerEndpointBuilder
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
comment|/**          * Number of seconds the loaded template resource will remain in the          * cache.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|templateUpdateDelay ( int templateUpdateDelay)
specifier|default
name|FreemarkerEndpointBuilder
name|templateUpdateDelay
parameter_list|(
name|int
name|templateUpdateDelay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"templateUpdateDelay"
argument_list|,
name|templateUpdateDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Number of seconds the loaded template resource will remain in the          * cache.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|templateUpdateDelay ( String templateUpdateDelay)
specifier|default
name|FreemarkerEndpointBuilder
name|templateUpdateDelay
parameter_list|(
name|String
name|templateUpdateDelay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"templateUpdateDelay"
argument_list|,
name|templateUpdateDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Freemarker component.      */
DECL|interface|AdvancedFreemarkerEndpointBuilder
specifier|public
interface|interface
name|AdvancedFreemarkerEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|FreemarkerEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|FreemarkerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedFreemarkerEndpointBuilder
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
name|AdvancedFreemarkerEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedFreemarkerEndpointBuilder
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
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedFreemarkerEndpointBuilder
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
comment|/**      * Freemarker (camel-freemarker)      * Transforms the message using a FreeMarker template.      *       * Category: transformation      * Available as of version: 2.10      * Maven coordinates: org.apache.camel:camel-freemarker      *       * Syntax:<code>freemarker:resourceUri</code>      *       * Path parameter: resourceUri (required)      * Path to the resource. You can prefix with: classpath, file, http, ref, or      * bean. classpath, file and http loads the resource using these protocols      * (classpath is default). ref will lookup the resource in the registry.      * bean will call a method on a bean to be used as the resource. For bean      * you can specify the method name after dot, eg bean:myBean.myMethod.      */
DECL|method|freemarker (String path)
specifier|default
name|FreemarkerEndpointBuilder
name|freemarker
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|FreemarkerEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|FreemarkerEndpointBuilder
implements|,
name|AdvancedFreemarkerEndpointBuilder
block|{
specifier|public
name|FreemarkerEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"freemarker"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|FreemarkerEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

