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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
comment|/**  * The class component is for invoking Java classes (Java beans) from Camel.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ClassEndpointBuilderFactory
specifier|public
interface|interface
name|ClassEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Class component.      */
DECL|interface|ClassEndpointBuilder
specifier|public
interface|interface
name|ClassEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedClassEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedClassEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * If enabled, Camel will cache the result of the first Registry          * look-up. Cache can be enabled if the bean in the Registry is defined          * as a singleton scope.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: common          */
DECL|method|cache (Boolean cache)
specifier|default
name|ClassEndpointBuilder
name|cache
parameter_list|(
name|Boolean
name|cache
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"cache"
argument_list|,
name|cache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled, Camel will cache the result of the first Registry          * look-up. Cache can be enabled if the bean in the Registry is defined          * as a singleton scope.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: common          */
DECL|method|cache (String cache)
specifier|default
name|ClassEndpointBuilder
name|cache
parameter_list|(
name|String
name|cache
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"cache"
argument_list|,
name|cache
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the name of the method to invoke on the bean.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|method (String method)
specifier|default
name|ClassEndpointBuilder
name|method
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"method"
argument_list|,
name|method
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Used for configuring additional properties on the bean.          *           * The option is a:<code>java.util.Map&lt;java.lang.String,          * java.lang.Object&gt;</code> type.          *           * Group: common          */
DECL|method|parameters (Map<String, Object> parameters)
specifier|default
name|ClassEndpointBuilder
name|parameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"parameters"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Used for configuring additional properties on the bean.          *           * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String, java.lang.Object&gt;</code>          * type.          *           * Group: common          */
DECL|method|parameters (String parameters)
specifier|default
name|ClassEndpointBuilder
name|parameters
parameter_list|(
name|String
name|parameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"parameters"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Class component.      */
DECL|interface|AdvancedClassEndpointBuilder
specifier|public
interface|interface
name|AdvancedClassEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ClassEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ClassEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedClassEndpointBuilder
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
name|AdvancedClassEndpointBuilder
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
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedClassEndpointBuilder
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
name|AdvancedClassEndpointBuilder
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
comment|/**      * Class (camel-bean)      * The class component is for invoking Java classes (Java beans) from Camel.      *       * Category: core,java      * Available as of version: 2.4      * Maven coordinates: org.apache.camel:camel-bean      *       * Syntax:<code>class:beanName</code>      *       * Path parameter: beanName (required)      * Sets the name of the bean to invoke      */
DECL|method|clas (String path)
specifier|default
name|ClassEndpointBuilder
name|clas
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ClassEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ClassEndpointBuilder
implements|,
name|AdvancedClassEndpointBuilder
block|{
specifier|public
name|ClassEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"class"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ClassEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

