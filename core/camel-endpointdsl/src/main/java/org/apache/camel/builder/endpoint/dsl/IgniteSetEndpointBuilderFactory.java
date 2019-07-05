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
comment|/**  * The Ignite Sets endpoint is one of camel-ignite endpoints which allows you to  * interact with Ignite Set data structures.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|IgniteSetEndpointBuilderFactory
specifier|public
interface|interface
name|IgniteSetEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Ignite Sets component.      */
DECL|interface|IgniteSetEndpointBuilder
specifier|public
interface|interface
name|IgniteSetEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedIgniteSetEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedIgniteSetEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Sets whether to propagate the incoming body if the return type of the          * underlying Ignite operation is void.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|propagateIncomingBodyIfNoReturnValue ( boolean propagateIncomingBodyIfNoReturnValue)
specifier|default
name|IgniteSetEndpointBuilder
name|propagateIncomingBodyIfNoReturnValue
parameter_list|(
name|boolean
name|propagateIncomingBodyIfNoReturnValue
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"propagateIncomingBodyIfNoReturnValue"
argument_list|,
name|propagateIncomingBodyIfNoReturnValue
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to propagate the incoming body if the return type of the          * underlying Ignite operation is void.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|propagateIncomingBodyIfNoReturnValue ( String propagateIncomingBodyIfNoReturnValue)
specifier|default
name|IgniteSetEndpointBuilder
name|propagateIncomingBodyIfNoReturnValue
parameter_list|(
name|String
name|propagateIncomingBodyIfNoReturnValue
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"propagateIncomingBodyIfNoReturnValue"
argument_list|,
name|propagateIncomingBodyIfNoReturnValue
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to treat Collections as cache objects or as Collections          * of items to insert/update/compute, etc.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|treatCollectionsAsCacheObjects ( boolean treatCollectionsAsCacheObjects)
specifier|default
name|IgniteSetEndpointBuilder
name|treatCollectionsAsCacheObjects
parameter_list|(
name|boolean
name|treatCollectionsAsCacheObjects
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"treatCollectionsAsCacheObjects"
argument_list|,
name|treatCollectionsAsCacheObjects
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to treat Collections as cache objects or as Collections          * of items to insert/update/compute, etc.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|treatCollectionsAsCacheObjects ( String treatCollectionsAsCacheObjects)
specifier|default
name|IgniteSetEndpointBuilder
name|treatCollectionsAsCacheObjects
parameter_list|(
name|String
name|treatCollectionsAsCacheObjects
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"treatCollectionsAsCacheObjects"
argument_list|,
name|treatCollectionsAsCacheObjects
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Ignite Sets component.      */
DECL|interface|AdvancedIgniteSetEndpointBuilder
specifier|public
interface|interface
name|AdvancedIgniteSetEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|IgniteSetEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|IgniteSetEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedIgniteSetEndpointBuilder
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
name|AdvancedIgniteSetEndpointBuilder
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
name|AdvancedIgniteSetEndpointBuilder
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
name|AdvancedIgniteSetEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.ignite.set.IgniteSetOperation</code>      * enum.      */
DECL|enum|IgniteSetOperation
enum|enum
name|IgniteSetOperation
block|{
DECL|enumConstant|CONTAINS
name|CONTAINS
block|,
DECL|enumConstant|ADD
name|ADD
block|,
DECL|enumConstant|SIZE
name|SIZE
block|,
DECL|enumConstant|REMOVE
name|REMOVE
block|,
DECL|enumConstant|ITERATOR
name|ITERATOR
block|,
DECL|enumConstant|CLEAR
name|CLEAR
block|,
DECL|enumConstant|RETAIN_ALL
name|RETAIN_ALL
block|,
DECL|enumConstant|ARRAY
name|ARRAY
block|;     }
comment|/**      * Ignite Sets (camel-ignite)      * The Ignite Sets endpoint is one of camel-ignite endpoints which allows      * you to interact with Ignite Set data structures.      *       * Category: nosql,cache      * Available as of version: 2.17      * Maven coordinates: org.apache.camel:camel-ignite      *       * Syntax:<code>ignite-set:name</code>      *       * Path parameter: name (required)      * The set name.      */
DECL|method|igniteSet (String path)
specifier|default
name|IgniteSetEndpointBuilder
name|igniteSet
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|IgniteSetEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|IgniteSetEndpointBuilder
implements|,
name|AdvancedIgniteSetEndpointBuilder
block|{
specifier|public
name|IgniteSetEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"ignite-set"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|IgniteSetEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

