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
comment|/**  * Transforms the message using a Velocity template.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|VelocityEndpointBuilderFactory
specifier|public
interface|interface
name|VelocityEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Velocity component.      */
DECL|interface|VelocityEndpointBuilder
specifier|public
specifier|static
interface|interface
name|VelocityEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedVelocityEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedVelocityEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Path to the resource. You can prefix with: classpath, file, http,          * ref, or bean. classpath, file and http loads the resource using these          * protocols (classpath is default). ref will lookup the resource in the          * registry. bean will call a method on a bean to be used as the          * resource. For bean you can specify the method name after dot, eg          * bean:myBean.myMethod.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|resourceUri (String resourceUri)
specifier|default
name|VelocityEndpointBuilder
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
comment|/**          * Sets whether to use resource content cache or not.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|contentCache (boolean contentCache)
specifier|default
name|VelocityEndpointBuilder
name|contentCache
parameter_list|(
name|boolean
name|contentCache
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Sets whether to use resource content cache or not.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|contentCache (String contentCache)
specifier|default
name|VelocityEndpointBuilder
name|contentCache
parameter_list|(
name|String
name|contentCache
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Character encoding of the resource content.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|encoding (String encoding)
specifier|default
name|VelocityEndpointBuilder
name|encoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Enables / disables the velocity resource loader cache which is          * enabled by default.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|loaderCache (boolean loaderCache)
specifier|default
name|VelocityEndpointBuilder
name|loaderCache
parameter_list|(
name|boolean
name|loaderCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"loaderCache"
argument_list|,
name|loaderCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Enables / disables the velocity resource loader cache which is          * enabled by default.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|loaderCache (String loaderCache)
specifier|default
name|VelocityEndpointBuilder
name|loaderCache
parameter_list|(
name|String
name|loaderCache
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"loaderCache"
argument_list|,
name|loaderCache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The URI of the properties file which is used for VelocityEngine          * initialization.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|propertiesFile (String propertiesFile)
specifier|default
name|VelocityEndpointBuilder
name|propertiesFile
parameter_list|(
name|String
name|propertiesFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"propertiesFile"
argument_list|,
name|propertiesFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Velocity component.      */
DECL|interface|AdvancedVelocityEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedVelocityEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|VelocityEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|VelocityEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedVelocityEndpointBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedVelocityEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedVelocityEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedVelocityEndpointBuilder
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
block|}
comment|/**      * Transforms the message using a Velocity template. Creates a builder to      * build endpoints for the Velocity component.      */
DECL|method|velocity (String path)
specifier|default
name|VelocityEndpointBuilder
name|velocity
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|VelocityEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|VelocityEndpointBuilder
implements|,
name|AdvancedVelocityEndpointBuilder
block|{
specifier|public
name|VelocityEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"velocity"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|VelocityEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

