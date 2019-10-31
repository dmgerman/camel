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
comment|/**  * The cm-sms component allows to integrate with CM SMS Gateway.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|CMEndpointBuilderFactory
specifier|public
interface|interface
name|CMEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the CM SMS Gateway component.      */
DECL|interface|CMEndpointBuilder
specifier|public
interface|interface
name|CMEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedCMEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedCMEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * This is the sender name. The maximum length is 11 characters.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: producer          */
DECL|method|defaultFrom (String defaultFrom)
specifier|default
name|CMEndpointBuilder
name|defaultFrom
parameter_list|(
name|String
name|defaultFrom
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"defaultFrom"
argument_list|,
name|defaultFrom
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If it is a multipart message forces the max number. Message can be          * truncated. Technically the gateway will first check if a message is          * larger than 160 characters, if so, the message will be cut into          * multiple 153 characters parts limited by these parameters.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|defaultMaxNumberOfParts ( int defaultMaxNumberOfParts)
specifier|default
name|CMEndpointBuilder
name|defaultMaxNumberOfParts
parameter_list|(
name|int
name|defaultMaxNumberOfParts
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"defaultMaxNumberOfParts"
argument_list|,
name|defaultMaxNumberOfParts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If it is a multipart message forces the max number. Message can be          * truncated. Technically the gateway will first check if a message is          * larger than 160 characters, if so, the message will be cut into          * multiple 153 characters parts limited by these parameters.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|defaultMaxNumberOfParts ( String defaultMaxNumberOfParts)
specifier|default
name|CMEndpointBuilder
name|defaultMaxNumberOfParts
parameter_list|(
name|String
name|defaultMaxNumberOfParts
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"defaultMaxNumberOfParts"
argument_list|,
name|defaultMaxNumberOfParts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (boolean lazyStartProducer)
specifier|default
name|CMEndpointBuilder
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
DECL|method|lazyStartProducer (String lazyStartProducer)
specifier|default
name|CMEndpointBuilder
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
comment|/**          * The unique token to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: producer          */
DECL|method|productToken (String productToken)
specifier|default
name|CMEndpointBuilder
name|productToken
parameter_list|(
name|String
name|productToken
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"productToken"
argument_list|,
name|productToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to test the connection to the SMS Gateway on startup.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|testConnectionOnStartup ( boolean testConnectionOnStartup)
specifier|default
name|CMEndpointBuilder
name|testConnectionOnStartup
parameter_list|(
name|boolean
name|testConnectionOnStartup
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"testConnectionOnStartup"
argument_list|,
name|testConnectionOnStartup
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to test the connection to the SMS Gateway on startup.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|testConnectionOnStartup ( String testConnectionOnStartup)
specifier|default
name|CMEndpointBuilder
name|testConnectionOnStartup
parameter_list|(
name|String
name|testConnectionOnStartup
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"testConnectionOnStartup"
argument_list|,
name|testConnectionOnStartup
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the CM SMS Gateway component.      */
DECL|interface|AdvancedCMEndpointBuilder
specifier|public
interface|interface
name|AdvancedCMEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|CMEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|CMEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedCMEndpointBuilder
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
name|AdvancedCMEndpointBuilder
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
name|AdvancedCMEndpointBuilder
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
name|AdvancedCMEndpointBuilder
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
comment|/**      * CM SMS Gateway (camel-cm-sms)      * The cm-sms component allows to integrate with CM SMS Gateway.      *       * Category: mobile      * Available as of version: 2.18      * Maven coordinates: org.apache.camel:camel-cm-sms      *       * Syntax:<code>cm-sms:host</code>      *       * Path parameter: host (required)      * SMS Provider HOST with scheme      */
DECL|method|cmSms (String path)
specifier|default
name|CMEndpointBuilder
name|cmSms
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|CMEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|CMEndpointBuilder
implements|,
name|AdvancedCMEndpointBuilder
block|{
specifier|public
name|CMEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"cm-sms"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|CMEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

