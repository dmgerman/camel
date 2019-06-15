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
comment|/**  * The aws-kinesis-firehose component is used for producing Amazon's Kinesis  * Firehose streams.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|KinesisFirehoseEndpointBuilderFactory
specifier|public
interface|interface
name|KinesisFirehoseEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the AWS Kinesis Firehose component.      */
DECL|interface|KinesisFirehoseEndpointBuilder
specifier|public
specifier|static
interface|interface
name|KinesisFirehoseEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedKinesisFirehoseEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedKinesisFirehoseEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Name of the stream.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|streamName (String streamName)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|streamName
parameter_list|(
name|String
name|streamName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"streamName"
argument_list|,
name|streamName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon Kinesis Firehose client to use for all requests for this          * endpoint.          * The option is a          *<code>com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose</code> type.          * @group producer          */
DECL|method|amazonKinesisFirehoseClient ( Object amazonKinesisFirehoseClient)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|amazonKinesisFirehoseClient
parameter_list|(
name|Object
name|amazonKinesisFirehoseClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"amazonKinesisFirehoseClient"
argument_list|,
name|amazonKinesisFirehoseClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon Kinesis Firehose client to use for all requests for this          * endpoint.          * The option will be converted to a          *<code>com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose</code> type.          * @group producer          */
DECL|method|amazonKinesisFirehoseClient ( String amazonKinesisFirehoseClient)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|amazonKinesisFirehoseClient
parameter_list|(
name|String
name|amazonKinesisFirehoseClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"amazonKinesisFirehoseClient"
argument_list|,
name|amazonKinesisFirehoseClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy host when instantiating the DDBStreams client.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|proxyHost (String proxyHost)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|proxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"proxyHost"
argument_list|,
name|proxyHost
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy port when instantiating the DDBStreams client.          * The option is a<code>java.lang.Integer</code> type.          * @group producer          */
DECL|method|proxyPort (Integer proxyPort)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|proxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"proxyPort"
argument_list|,
name|proxyPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy port when instantiating the DDBStreams client.          * The option will be converted to a<code>java.lang.Integer</code>          * type.          * @group producer          */
DECL|method|proxyPort (String proxyPort)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|proxyPort
parameter_list|(
name|String
name|proxyPort
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"proxyPort"
argument_list|,
name|proxyPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The region in which Kinesis client needs to work.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|region (String region)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|region
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"region"
argument_list|,
name|region
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon AWS Access Key.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|accessKey (String accessKey)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|accessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accessKey"
argument_list|,
name|accessKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon AWS Secret Key.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|secretKey (String secretKey)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|secretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secretKey"
argument_list|,
name|secretKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the AWS Kinesis Firehose component.      */
DECL|interface|AdvancedKinesisFirehoseEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedKinesisFirehoseEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|KinesisFirehoseEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|KinesisFirehoseEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedKinesisFirehoseEndpointBuilder
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
name|AdvancedKinesisFirehoseEndpointBuilder
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
name|AdvancedKinesisFirehoseEndpointBuilder
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
name|AdvancedKinesisFirehoseEndpointBuilder
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
comment|/**      * The aws-kinesis-firehose component is used for producing Amazon's Kinesis      * Firehose streams. Creates a builder to build endpoints for the AWS      * Kinesis Firehose component.      */
DECL|method|kinesisFirehose (String path)
specifier|default
name|KinesisFirehoseEndpointBuilder
name|kinesisFirehose
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|KinesisFirehoseEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|KinesisFirehoseEndpointBuilder
implements|,
name|AdvancedKinesisFirehoseEndpointBuilder
block|{
specifier|public
name|KinesisFirehoseEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"aws-kinesis-firehose"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|KinesisFirehoseEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

