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
comment|/**  * Transforms the message using a Mustache template.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|MustacheEndpointBuilderFactory
specifier|public
interface|interface
name|MustacheEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Mustache component.      */
DECL|interface|MustacheEndpointBuilder
specifier|public
interface|interface
name|MustacheEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedMustacheEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedMustacheEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Sets whether to use resource content cache or not.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|contentCache (boolean contentCache)
specifier|default
name|MustacheEndpointBuilder
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
comment|/**          * Sets whether to use resource content cache or not.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|contentCache (String contentCache)
specifier|default
name|MustacheEndpointBuilder
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
comment|/**          * Character encoding of the resource content.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|encoding (String encoding)
specifier|default
name|MustacheEndpointBuilder
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
comment|/**          * Characters used to mark template code end.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|endDelimiter (String endDelimiter)
specifier|default
name|MustacheEndpointBuilder
name|endDelimiter
parameter_list|(
name|String
name|endDelimiter
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"endDelimiter"
argument_list|,
name|endDelimiter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Characters used to mark template code beginning.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|startDelimiter (String startDelimiter)
specifier|default
name|MustacheEndpointBuilder
name|startDelimiter
parameter_list|(
name|String
name|startDelimiter
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"startDelimiter"
argument_list|,
name|startDelimiter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Mustache component.      */
DECL|interface|AdvancedMustacheEndpointBuilder
specifier|public
interface|interface
name|AdvancedMustacheEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|MustacheEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|MustacheEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedMustacheEndpointBuilder
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
name|AdvancedMustacheEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedMustacheEndpointBuilder
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
name|AdvancedMustacheEndpointBuilder
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
comment|/**      * Mustache (camel-mustache)      * Transforms the message using a Mustache template.      *       * Syntax:<code>mustache:resourceUri</code>      * Category: transformation      * Available as of version: 2.12      * Maven coordinates: org.apache.camel:camel-mustache      */
DECL|method|mustache (String path)
specifier|default
name|MustacheEndpointBuilder
name|mustache
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|MustacheEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|MustacheEndpointBuilder
implements|,
name|AdvancedMustacheEndpointBuilder
block|{
specifier|public
name|MustacheEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"mustache"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MustacheEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

