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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|HeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  * The aws-sns component is used for sending messages to an Amazon Simple  * Notification Topic.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SnsEndpointBuilderFactory
specifier|public
interface|interface
name|SnsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the AWS Simple Notification System component.      */
DECL|interface|SnsEndpointBuilder
specifier|public
interface|interface
name|SnsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSnsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSnsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To use the AmazonSNS as the client.          *           * The option is a:<code>com.amazonaws.services.sns.AmazonSNS</code>          * type.          *           * Group: producer          */
DECL|method|amazonSNSClient (Object amazonSNSClient)
specifier|default
name|SnsEndpointBuilder
name|amazonSNSClient
parameter_list|(
name|Object
name|amazonSNSClient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|amazonSNSClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use the AmazonSNS as the client.          *           * The option will be converted to a          *<code>com.amazonaws.services.sns.AmazonSNS</code> type.          *           * Group: producer          */
DECL|method|amazonSNSClient (String amazonSNSClient)
specifier|default
name|SnsEndpointBuilder
name|amazonSNSClient
parameter_list|(
name|String
name|amazonSNSClient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|amazonSNSClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An SQS Client to use as bridge between SNS and SQS.          *           * The option is a:<code>com.amazonaws.services.sqs.AmazonSQS</code>          * type.          *           * Group: producer          */
DECL|method|amazonSQSClient (Object amazonSQSClient)
specifier|default
name|SnsEndpointBuilder
name|amazonSQSClient
parameter_list|(
name|Object
name|amazonSQSClient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|amazonSQSClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An SQS Client to use as bridge between SNS and SQS.          *           * The option will be converted to a          *<code>com.amazonaws.services.sqs.AmazonSQS</code> type.          *           * Group: producer          */
DECL|method|amazonSQSClient (String amazonSQSClient)
specifier|default
name|SnsEndpointBuilder
name|amazonSQSClient
parameter_list|(
name|String
name|amazonSQSClient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|amazonSQSClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Setting the autocreation of the topic.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|autoCreateTopic (boolean autoCreateTopic)
specifier|default
name|SnsEndpointBuilder
name|autoCreateTopic
parameter_list|(
name|boolean
name|autoCreateTopic
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"autoCreateTopic"
argument_list|,
name|autoCreateTopic
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Setting the autocreation of the topic.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|autoCreateTopic (String autoCreateTopic)
specifier|default
name|SnsEndpointBuilder
name|autoCreateTopic
parameter_list|(
name|String
name|autoCreateTopic
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"autoCreateTopic"
argument_list|,
name|autoCreateTopic
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HeaderFilterStrategy to map headers to/from Camel.          *           * The option is a:          *<code>org.apache.camel.spi.HeaderFilterStrategy</code> type.          *           * Group: producer          */
DECL|method|headerFilterStrategy ( HeaderFilterStrategy headerFilterStrategy)
specifier|default
name|SnsEndpointBuilder
name|headerFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"headerFilterStrategy"
argument_list|,
name|headerFilterStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom HeaderFilterStrategy to map headers to/from Camel.          *           * The option will be converted to a          *<code>org.apache.camel.spi.HeaderFilterStrategy</code> type.          *           * Group: producer          */
DECL|method|headerFilterStrategy ( String headerFilterStrategy)
specifier|default
name|SnsEndpointBuilder
name|headerFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"headerFilterStrategy"
argument_list|,
name|headerFilterStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The ID of an AWS-managed customer master key (CMK) for Amazon SNS or          * a custom CMK.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|kmsMasterKeyId (String kmsMasterKeyId)
specifier|default
name|SnsEndpointBuilder
name|kmsMasterKeyId
parameter_list|(
name|String
name|kmsMasterKeyId
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"kmsMasterKeyId"
argument_list|,
name|kmsMasterKeyId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (boolean lazyStartProducer)
specifier|default
name|SnsEndpointBuilder
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
name|SnsEndpointBuilder
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
comment|/**          * The message structure to use such as json.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|messageStructure (String messageStructure)
specifier|default
name|SnsEndpointBuilder
name|messageStructure
parameter_list|(
name|String
name|messageStructure
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"messageStructure"
argument_list|,
name|messageStructure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The policy for this queue.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|policy (String policy)
specifier|default
name|SnsEndpointBuilder
name|policy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"policy"
argument_list|,
name|policy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy host when instantiating the SNS client.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|proxyHost (String proxyHost)
specifier|default
name|SnsEndpointBuilder
name|proxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * To define a proxy port when instantiating the SNS client.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|proxyPort (Integer proxyPort)
specifier|default
name|SnsEndpointBuilder
name|proxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * To define a proxy port when instantiating the SNS client.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|proxyPort (String proxyPort)
specifier|default
name|SnsEndpointBuilder
name|proxyPort
parameter_list|(
name|String
name|proxyPort
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * To define a proxy protocol when instantiating the SNS client.          *           * The option is a:<code>com.amazonaws.Protocol</code> type.          *           * Group: producer          */
DECL|method|proxyProtocol (Protocol proxyProtocol)
specifier|default
name|SnsEndpointBuilder
name|proxyProtocol
parameter_list|(
name|Protocol
name|proxyProtocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"proxyProtocol"
argument_list|,
name|proxyProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy protocol when instantiating the SNS client.          *           * The option will be converted to a<code>com.amazonaws.Protocol</code>          * type.          *           * Group: producer          */
DECL|method|proxyProtocol (String proxyProtocol)
specifier|default
name|SnsEndpointBuilder
name|proxyProtocol
parameter_list|(
name|String
name|proxyProtocol
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"proxyProtocol"
argument_list|,
name|proxyProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The queueUrl to subscribe to.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|queueUrl (String queueUrl)
specifier|default
name|SnsEndpointBuilder
name|queueUrl
parameter_list|(
name|String
name|queueUrl
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"queueUrl"
argument_list|,
name|queueUrl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The region in which SNS client needs to work. When using this          * parameter, the configuration will expect the capitalized name of the          * region (for example AP_EAST_1) You'll need to use the name          * Regions.EU_WEST_1.name().          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|region (String region)
specifier|default
name|SnsEndpointBuilder
name|region
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Define if Server Side Encryption is enabled or not on the topic.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|serverSideEncryptionEnabled ( boolean serverSideEncryptionEnabled)
specifier|default
name|SnsEndpointBuilder
name|serverSideEncryptionEnabled
parameter_list|(
name|boolean
name|serverSideEncryptionEnabled
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"serverSideEncryptionEnabled"
argument_list|,
name|serverSideEncryptionEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if Server Side Encryption is enabled or not on the topic.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|serverSideEncryptionEnabled ( String serverSideEncryptionEnabled)
specifier|default
name|SnsEndpointBuilder
name|serverSideEncryptionEnabled
parameter_list|(
name|String
name|serverSideEncryptionEnabled
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"serverSideEncryptionEnabled"
argument_list|,
name|serverSideEncryptionEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The subject which is used if the message header 'CamelAwsSnsSubject'          * is not present.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|subject (String subject)
specifier|default
name|SnsEndpointBuilder
name|subject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"subject"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if the subscription between SNS Topic and SQS must be done or          * not.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|subscribeSNStoSQS (boolean subscribeSNStoSQS)
specifier|default
name|SnsEndpointBuilder
name|subscribeSNStoSQS
parameter_list|(
name|boolean
name|subscribeSNStoSQS
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"subscribeSNStoSQS"
argument_list|,
name|subscribeSNStoSQS
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if the subscription between SNS Topic and SQS must be done or          * not.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|subscribeSNStoSQS (String subscribeSNStoSQS)
specifier|default
name|SnsEndpointBuilder
name|subscribeSNStoSQS
parameter_list|(
name|String
name|subscribeSNStoSQS
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"subscribeSNStoSQS"
argument_list|,
name|subscribeSNStoSQS
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon AWS Access Key.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|accessKey (String accessKey)
specifier|default
name|SnsEndpointBuilder
name|accessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Amazon AWS Secret Key.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|secretKey (String secretKey)
specifier|default
name|SnsEndpointBuilder
name|secretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Advanced builder for endpoint for the AWS Simple Notification System      * component.      */
DECL|interface|AdvancedSnsEndpointBuilder
specifier|public
interface|interface
name|AdvancedSnsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SnsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SnsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSnsEndpointBuilder
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
name|AdvancedSnsEndpointBuilder
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
name|AdvancedSnsEndpointBuilder
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
name|AdvancedSnsEndpointBuilder
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
comment|/**      * Proxy enum for<code>com.amazonaws.Protocol</code> enum.      */
DECL|enum|Protocol
enum|enum
name|Protocol
block|{
DECL|enumConstant|http
name|http
block|,
DECL|enumConstant|https
name|https
block|;     }
comment|/**      * AWS Simple Notification System (camel-aws-sns)      * The aws-sns component is used for sending messages to an Amazon Simple      * Notification Topic.      *       * Category: cloud,mobile,messaging      * Since: 2.8      * Maven coordinates: org.apache.camel:camel-aws-sns      *       * Syntax:<code>aws-sns:topicNameOrArn</code>      *       * Path parameter: topicNameOrArn (required)      * Topic name or ARN      */
DECL|method|awsSns (String path)
specifier|default
name|SnsEndpointBuilder
name|awsSns
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SnsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SnsEndpointBuilder
implements|,
name|AdvancedSnsEndpointBuilder
block|{
specifier|public
name|SnsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"aws-sns"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SnsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

