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
comment|/**  * The Ignite Events endpoint is one of camel-ignite endpoints which allows you  * to receive events from the Ignite cluster by creating a local event listener.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|IgniteEventsEndpointBuilderFactory
specifier|public
interface|interface
name|IgniteEventsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Ignite Events component.      */
DECL|interface|IgniteEventsEndpointBuilder
specifier|public
interface|interface
name|IgniteEventsEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedIgniteEventsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedIgniteEventsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The endpoint ID (not used).          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|endpointId (String endpointId)
specifier|default
name|IgniteEventsEndpointBuilder
name|endpointId
parameter_list|(
name|String
name|endpointId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"endpointId"
argument_list|,
name|endpointId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to propagate the incoming body if the return type of the          * underlying Ignite operation is void.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|propagateIncomingBodyIfNoReturnValue ( boolean propagateIncomingBodyIfNoReturnValue)
specifier|default
name|IgniteEventsEndpointBuilder
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
comment|/**          * Sets whether to propagate the incoming body if the return type of the          * underlying Ignite operation is void.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|propagateIncomingBodyIfNoReturnValue ( String propagateIncomingBodyIfNoReturnValue)
specifier|default
name|IgniteEventsEndpointBuilder
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
comment|/**          * Sets whether to treat Collections as cache objects or as Collections          * of items to insert/update/compute, etc.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|treatCollectionsAsCacheObjects ( boolean treatCollectionsAsCacheObjects)
specifier|default
name|IgniteEventsEndpointBuilder
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
comment|/**          * Sets whether to treat Collections as cache objects or as Collections          * of items to insert/update/compute, etc.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|treatCollectionsAsCacheObjects ( String treatCollectionsAsCacheObjects)
specifier|default
name|IgniteEventsEndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the Ignite Events component.      */
DECL|interface|AdvancedIgniteEventsEndpointBuilder
specifier|public
interface|interface
name|AdvancedIgniteEventsEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|IgniteEventsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|IgniteEventsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedIgniteEventsEndpointBuilder
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
name|AdvancedIgniteEventsEndpointBuilder
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
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedIgniteEventsEndpointBuilder
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
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedIgniteEventsEndpointBuilder
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
comment|/**      * The Ignite Events endpoint is one of camel-ignite endpoints which allows      * you to receive events from the Ignite cluster by creating a local event      * listener.      * Maven coordinates: org.apache.camel:camel-ignite      */
DECL|method|igniteEvents (String path)
specifier|default
name|IgniteEventsEndpointBuilder
name|igniteEvents
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|IgniteEventsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|IgniteEventsEndpointBuilder
implements|,
name|AdvancedIgniteEventsEndpointBuilder
block|{
specifier|public
name|IgniteEventsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"ignite-events"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|IgniteEventsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

