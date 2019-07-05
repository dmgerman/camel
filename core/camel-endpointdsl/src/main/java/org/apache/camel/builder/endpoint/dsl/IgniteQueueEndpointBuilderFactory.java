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
comment|/**  * The Ignite Queue endpoint is one of camel-ignite endpoints which allows you  * to interact with Ignite Queue data structures.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|IgniteQueueEndpointBuilderFactory
specifier|public
interface|interface
name|IgniteQueueEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Ignite Queues component.      */
DECL|interface|IgniteQueueEndpointBuilder
specifier|public
interface|interface
name|IgniteQueueEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedIgniteQueueEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedIgniteQueueEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The queue name.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|name (String name)
specifier|default
name|IgniteQueueEndpointBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to propagate the incoming body if the return type of the          * underlying Ignite operation is void.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|propagateIncomingBodyIfNoReturnValue ( boolean propagateIncomingBodyIfNoReturnValue)
specifier|default
name|IgniteQueueEndpointBuilder
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
name|IgniteQueueEndpointBuilder
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
name|IgniteQueueEndpointBuilder
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
name|IgniteQueueEndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the Ignite Queues component.      */
DECL|interface|AdvancedIgniteQueueEndpointBuilder
specifier|public
interface|interface
name|AdvancedIgniteQueueEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|IgniteQueueEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|IgniteQueueEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedIgniteQueueEndpointBuilder
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
name|AdvancedIgniteQueueEndpointBuilder
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
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedIgniteQueueEndpointBuilder
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
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedIgniteQueueEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.ignite.queue.IgniteQueueOperation</code>      * enum.      */
DECL|enum|IgniteQueueOperation
enum|enum
name|IgniteQueueOperation
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
block|,
DECL|enumConstant|DRAIN
name|DRAIN
block|,
DECL|enumConstant|ELEMENT
name|ELEMENT
block|,
DECL|enumConstant|PEEK
name|PEEK
block|,
DECL|enumConstant|OFFER
name|OFFER
block|,
DECL|enumConstant|POLL
name|POLL
block|,
DECL|enumConstant|TAKE
name|TAKE
block|,
DECL|enumConstant|PUT
name|PUT
block|;     }
comment|/**      * The Ignite Queue endpoint is one of camel-ignite endpoints which allows      * you to interact with Ignite Queue data structures.      * Maven coordinates: org.apache.camel:camel-ignite      */
DECL|method|igniteQueue (String path)
specifier|default
name|IgniteQueueEndpointBuilder
name|igniteQueue
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|IgniteQueueEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|IgniteQueueEndpointBuilder
implements|,
name|AdvancedIgniteQueueEndpointBuilder
block|{
specifier|public
name|IgniteQueueEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"ignite-queue"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|IgniteQueueEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

