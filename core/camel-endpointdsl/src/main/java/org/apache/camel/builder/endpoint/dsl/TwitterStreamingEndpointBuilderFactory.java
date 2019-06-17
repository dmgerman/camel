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
comment|/**  * The Twitter Streaming component consumes twitter statuses using Streaming  * API.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|TwitterStreamingEndpointBuilderFactory
specifier|public
interface|interface
name|TwitterStreamingEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Twitter Streaming component.      */
DECL|interface|TwitterStreamingEndpointBuilder
specifier|public
interface|interface
name|TwitterStreamingEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedTwitterStreamingEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedTwitterStreamingEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The streaming type to consume.          * The option is a          *<code>org.apache.camel.component.twitter.data.StreamingType</code>          * type.          * @group consumer          */
DECL|method|streamingType ( StreamingType streamingType)
specifier|default
name|TwitterStreamingEndpointBuilder
name|streamingType
parameter_list|(
name|StreamingType
name|streamingType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"streamingType"
argument_list|,
name|streamingType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The streaming type to consume.          * The option will be converted to a          *<code>org.apache.camel.component.twitter.data.StreamingType</code>          * type.          * @group consumer          */
DECL|method|streamingType ( String streamingType)
specifier|default
name|TwitterStreamingEndpointBuilder
name|streamingType
parameter_list|(
name|String
name|streamingType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"streamingType"
argument_list|,
name|streamingType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The http proxy host which can be used for the camel-twitter. Can also          * be configured on the TwitterComponent level instead.          * The option is a<code>java.lang.String</code> type.          * @group proxy          */
DECL|method|httpProxyHost ( String httpProxyHost)
specifier|default
name|TwitterStreamingEndpointBuilder
name|httpProxyHost
parameter_list|(
name|String
name|httpProxyHost
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpProxyHost"
argument_list|,
name|httpProxyHost
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The http proxy password which can be used for the camel-twitter. Can          * also be configured on the TwitterComponent level instead.          * The option is a<code>java.lang.String</code> type.          * @group proxy          */
DECL|method|httpProxyPassword ( String httpProxyPassword)
specifier|default
name|TwitterStreamingEndpointBuilder
name|httpProxyPassword
parameter_list|(
name|String
name|httpProxyPassword
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpProxyPassword"
argument_list|,
name|httpProxyPassword
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The http proxy port which can be used for the camel-twitter. Can also          * be configured on the TwitterComponent level instead.          * The option is a<code>java.lang.Integer</code> type.          * @group proxy          */
DECL|method|httpProxyPort ( Integer httpProxyPort)
specifier|default
name|TwitterStreamingEndpointBuilder
name|httpProxyPort
parameter_list|(
name|Integer
name|httpProxyPort
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpProxyPort"
argument_list|,
name|httpProxyPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The http proxy port which can be used for the camel-twitter. Can also          * be configured on the TwitterComponent level instead.          * The option will be converted to a<code>java.lang.Integer</code>          * type.          * @group proxy          */
DECL|method|httpProxyPort ( String httpProxyPort)
specifier|default
name|TwitterStreamingEndpointBuilder
name|httpProxyPort
parameter_list|(
name|String
name|httpProxyPort
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpProxyPort"
argument_list|,
name|httpProxyPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The http proxy user which can be used for the camel-twitter. Can also          * be configured on the TwitterComponent level instead.          * The option is a<code>java.lang.String</code> type.          * @group proxy          */
DECL|method|httpProxyUser ( String httpProxyUser)
specifier|default
name|TwitterStreamingEndpointBuilder
name|httpProxyUser
parameter_list|(
name|String
name|httpProxyUser
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"httpProxyUser"
argument_list|,
name|httpProxyUser
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The access token. Can also be configured on the TwitterComponent          * level instead.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|accessToken (String accessToken)
specifier|default
name|TwitterStreamingEndpointBuilder
name|accessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accessToken"
argument_list|,
name|accessToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The access secret. Can also be configured on the TwitterComponent          * level instead.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|accessTokenSecret ( String accessTokenSecret)
specifier|default
name|TwitterStreamingEndpointBuilder
name|accessTokenSecret
parameter_list|(
name|String
name|accessTokenSecret
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accessTokenSecret"
argument_list|,
name|accessTokenSecret
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The consumer key. Can also be configured on the TwitterComponent          * level instead.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|consumerKey (String consumerKey)
specifier|default
name|TwitterStreamingEndpointBuilder
name|consumerKey
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consumerKey"
argument_list|,
name|consumerKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The consumer secret. Can also be configured on the TwitterComponent          * level instead.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|consumerSecret ( String consumerSecret)
specifier|default
name|TwitterStreamingEndpointBuilder
name|consumerSecret
parameter_list|(
name|String
name|consumerSecret
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consumerSecret"
argument_list|,
name|consumerSecret
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Twitter Streaming component.      */
DECL|interface|AdvancedTwitterStreamingEndpointBuilder
specifier|public
interface|interface
name|AdvancedTwitterStreamingEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|TwitterStreamingEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|TwitterStreamingEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedTwitterStreamingEndpointBuilder
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
name|AdvancedTwitterStreamingEndpointBuilder
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
name|AdvancedTwitterStreamingEndpointBuilder
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
name|AdvancedTwitterStreamingEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.twitter.data.StreamingType</code> enum.      */
DECL|enum|StreamingType
specifier|public
specifier|static
enum|enum
name|StreamingType
block|{
DECL|enumConstant|SAMPLE
DECL|enumConstant|FILTER
DECL|enumConstant|USER
DECL|enumConstant|UNKNOWN
name|SAMPLE
block|,
name|FILTER
block|,
name|USER
block|,
name|UNKNOWN
block|;     }
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.twitter.data.EndpointType</code> enum.      */
DECL|enum|EndpointType
specifier|public
specifier|static
enum|enum
name|EndpointType
block|{
DECL|enumConstant|POLLING
DECL|enumConstant|DIRECT
DECL|enumConstant|EVENT
name|POLLING
block|,
name|DIRECT
block|,
name|EVENT
block|;     }
comment|/**      * The Twitter Streaming component consumes twitter statuses using Streaming      * API. Creates a builder to build endpoints for the Twitter Streaming      * component.      */
DECL|method|twitterStreaming (String path)
specifier|default
name|TwitterStreamingEndpointBuilder
name|twitterStreaming
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|TwitterStreamingEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|TwitterStreamingEndpointBuilder
implements|,
name|AdvancedTwitterStreamingEndpointBuilder
block|{
specifier|public
name|TwitterStreamingEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"twitter-streaming"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|TwitterStreamingEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

