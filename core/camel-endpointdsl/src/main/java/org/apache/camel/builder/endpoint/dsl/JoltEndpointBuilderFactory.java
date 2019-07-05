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
comment|/**  * The jolt component allows you to process a JSON messages using an JOLT  * specification (such as JSON-JSON transformation).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|JoltEndpointBuilderFactory
specifier|public
interface|interface
name|JoltEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the JOLT component.      */
DECL|interface|JoltEndpointBuilder
specifier|public
interface|interface
name|JoltEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJoltEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJoltEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Path to the resource. You can prefix with: classpath, file, http,          * ref, or bean. classpath, file and http loads the resource using these          * protocols (classpath is default). ref will lookup the resource in the          * registry. bean will call a method on a bean to be used as the          * resource. For bean you can specify the method name after dot, eg          * bean:myBean.myMethod.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|resourceUri (String resourceUri)
specifier|default
name|JoltEndpointBuilder
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
comment|/**          * Sets whether to use resource content cache or not.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|contentCache (boolean contentCache)
specifier|default
name|JoltEndpointBuilder
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
name|JoltEndpointBuilder
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
comment|/**          * Specifies if the input is hydrated JSON or a JSON String.          *           * The option is a:          *<code>org.apache.camel.component.jolt.JoltInputOutputType</code>          * type.          *           * Group: producer          */
DECL|method|inputType (JoltInputOutputType inputType)
specifier|default
name|JoltEndpointBuilder
name|inputType
parameter_list|(
name|JoltInputOutputType
name|inputType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inputType"
argument_list|,
name|inputType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies if the input is hydrated JSON or a JSON String.          *           * The option will be converted to a          *<code>org.apache.camel.component.jolt.JoltInputOutputType</code>          * type.          *           * Group: producer          */
DECL|method|inputType (String inputType)
specifier|default
name|JoltEndpointBuilder
name|inputType
parameter_list|(
name|String
name|inputType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"inputType"
argument_list|,
name|inputType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies if the output should be hydrated JSON or a JSON String.          *           * The option is a:          *<code>org.apache.camel.component.jolt.JoltInputOutputType</code>          * type.          *           * Group: producer          */
DECL|method|outputType (JoltInputOutputType outputType)
specifier|default
name|JoltEndpointBuilder
name|outputType
parameter_list|(
name|JoltInputOutputType
name|outputType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"outputType"
argument_list|,
name|outputType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies if the output should be hydrated JSON or a JSON String.          *           * The option will be converted to a          *<code>org.apache.camel.component.jolt.JoltInputOutputType</code>          * type.          *           * Group: producer          */
DECL|method|outputType (String outputType)
specifier|default
name|JoltEndpointBuilder
name|outputType
parameter_list|(
name|String
name|outputType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"outputType"
argument_list|,
name|outputType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the Transform DSL of the endpoint resource. If none is          * specified Chainr will be used.          *           * The option is a:          *<code>org.apache.camel.component.jolt.JoltTransformType</code> type.          *           * Group: producer          */
DECL|method|transformDsl (JoltTransformType transformDsl)
specifier|default
name|JoltEndpointBuilder
name|transformDsl
parameter_list|(
name|JoltTransformType
name|transformDsl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transformDsl"
argument_list|,
name|transformDsl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the Transform DSL of the endpoint resource. If none is          * specified Chainr will be used.          *           * The option will be converted to a          *<code>org.apache.camel.component.jolt.JoltTransformType</code> type.          *           * Group: producer          */
DECL|method|transformDsl (String transformDsl)
specifier|default
name|JoltEndpointBuilder
name|transformDsl
parameter_list|(
name|String
name|transformDsl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transformDsl"
argument_list|,
name|transformDsl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the JOLT component.      */
DECL|interface|AdvancedJoltEndpointBuilder
specifier|public
interface|interface
name|AdvancedJoltEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JoltEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JoltEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJoltEndpointBuilder
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
name|AdvancedJoltEndpointBuilder
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
name|AdvancedJoltEndpointBuilder
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
name|AdvancedJoltEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.jolt.JoltInputOutputType</code> enum.      */
DECL|enum|JoltInputOutputType
enum|enum
name|JoltInputOutputType
block|{
DECL|enumConstant|Hydrated
name|Hydrated
block|,
DECL|enumConstant|JsonString
name|JsonString
block|;     }
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.jolt.JoltTransformType</code> enum.      */
DECL|enum|JoltTransformType
enum|enum
name|JoltTransformType
block|{
DECL|enumConstant|Chainr
name|Chainr
block|,
DECL|enumConstant|Shiftr
name|Shiftr
block|,
DECL|enumConstant|Defaultr
name|Defaultr
block|,
DECL|enumConstant|Removr
name|Removr
block|,
DECL|enumConstant|Sortr
name|Sortr
block|;     }
comment|/**      * JOLT (camel-jolt)      * The jolt component allows you to process a JSON messages using an JOLT      * specification (such as JSON-JSON transformation).      *       * Syntax:<code>jolt:resourceUri</code>      * Category: transformation      * Available as of version: 2.16      * Maven coordinates: org.apache.camel:camel-jolt      */
DECL|method|jolt (String path)
specifier|default
name|JoltEndpointBuilder
name|jolt
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|JoltEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|JoltEndpointBuilder
implements|,
name|AdvancedJoltEndpointBuilder
block|{
specifier|public
name|JoltEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"jolt"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|JoltEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

