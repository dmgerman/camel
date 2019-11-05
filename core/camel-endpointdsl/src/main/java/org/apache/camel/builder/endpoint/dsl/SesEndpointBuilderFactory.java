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
name|List
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
comment|/**  * The aws-ses component is used for sending emails with Amazon's SES service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SesEndpointBuilderFactory
specifier|public
interface|interface
name|SesEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the AWS Simple Email Service component.      */
DECL|interface|SesEndpointBuilder
specifier|public
interface|interface
name|SesEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSesEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSesEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To use the AmazonSimpleEmailService as the client.          *           * The option is a:          *<code>com.amazonaws.services.simpleemail.AmazonSimpleEmailService</code> type.          *           * Group: producer          */
DECL|method|amazonSESClient (Object amazonSESClient)
specifier|default
name|SesEndpointBuilder
name|amazonSESClient
parameter_list|(
name|Object
name|amazonSESClient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"amazonSESClient"
argument_list|,
name|amazonSESClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use the AmazonSimpleEmailService as the client.          *           * The option will be converted to a          *<code>com.amazonaws.services.simpleemail.AmazonSimpleEmailService</code> type.          *           * Group: producer          */
DECL|method|amazonSESClient (String amazonSESClient)
specifier|default
name|SesEndpointBuilder
name|amazonSESClient
parameter_list|(
name|String
name|amazonSESClient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"amazonSESClient"
argument_list|,
name|amazonSESClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (boolean lazyStartProducer)
specifier|default
name|SesEndpointBuilder
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
name|SesEndpointBuilder
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
comment|/**          * To define a proxy host when instantiating the SES client.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|proxyHost (String proxyHost)
specifier|default
name|SesEndpointBuilder
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
comment|/**          * To define a proxy port when instantiating the SES client.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|proxyPort (Integer proxyPort)
specifier|default
name|SesEndpointBuilder
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
comment|/**          * To define a proxy port when instantiating the SES client.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|proxyPort (String proxyPort)
specifier|default
name|SesEndpointBuilder
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
comment|/**          * To define a proxy protocol when instantiating the SES client.          *           * The option is a:<code>com.amazonaws.Protocol</code> type.          *           * Group: producer          */
DECL|method|proxyProtocol (Protocol proxyProtocol)
specifier|default
name|SesEndpointBuilder
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
comment|/**          * To define a proxy protocol when instantiating the SES client.          *           * The option will be converted to a<code>com.amazonaws.Protocol</code>          * type.          *           * Group: producer          */
DECL|method|proxyProtocol (String proxyProtocol)
specifier|default
name|SesEndpointBuilder
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
comment|/**          * The region in which SES client needs to work. When using this          * parameter, the configuration will expect the capitalized name of the          * region (for example AP_EAST_1) You'll need to use the name          * Regions.EU_WEST_1.name().          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|region (String region)
specifier|default
name|SesEndpointBuilder
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
comment|/**          * List of reply-to email address(es) for the message, override it using          * 'CamelAwsSesReplyToAddresses' header.          *           * The option is a:<code>java.util.List&lt;java.lang.String&gt;</code>          * type.          *           * Group: producer          */
DECL|method|replyToAddresses ( List<String> replyToAddresses)
specifier|default
name|SesEndpointBuilder
name|replyToAddresses
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|replyToAddresses
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"replyToAddresses"
argument_list|,
name|replyToAddresses
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * List of reply-to email address(es) for the message, override it using          * 'CamelAwsSesReplyToAddresses' header.          *           * The option will be converted to a          *<code>java.util.List&lt;java.lang.String&gt;</code> type.          *           * Group: producer          */
DECL|method|replyToAddresses (String replyToAddresses)
specifier|default
name|SesEndpointBuilder
name|replyToAddresses
parameter_list|(
name|String
name|replyToAddresses
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"replyToAddresses"
argument_list|,
name|replyToAddresses
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The email address to which bounce notifications are to be forwarded,          * override it using 'CamelAwsSesReturnPath' header.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|returnPath (String returnPath)
specifier|default
name|SesEndpointBuilder
name|returnPath
parameter_list|(
name|String
name|returnPath
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"returnPath"
argument_list|,
name|returnPath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The subject which is used if the message header 'CamelAwsSesSubject'          * is not present.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|subject (String subject)
specifier|default
name|SesEndpointBuilder
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
comment|/**          * List of destination email address. Can be overriden with          * 'CamelAwsSesTo' header.          *           * The option is a:<code>java.util.List&lt;java.lang.String&gt;</code>          * type.          *           * Group: producer          */
DECL|method|to (List<String> to)
specifier|default
name|SesEndpointBuilder
name|to
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|to
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"to"
argument_list|,
name|to
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * List of destination email address. Can be overriden with          * 'CamelAwsSesTo' header.          *           * The option will be converted to a          *<code>java.util.List&lt;java.lang.String&gt;</code> type.          *           * Group: producer          */
DECL|method|to (String to)
specifier|default
name|SesEndpointBuilder
name|to
parameter_list|(
name|String
name|to
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"to"
argument_list|,
name|to
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Amazon AWS Access Key.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|accessKey (String accessKey)
specifier|default
name|SesEndpointBuilder
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
name|SesEndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the AWS Simple Email Service component.      */
DECL|interface|AdvancedSesEndpointBuilder
specifier|public
interface|interface
name|AdvancedSesEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SesEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SesEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSesEndpointBuilder
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
name|AdvancedSesEndpointBuilder
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
name|AdvancedSesEndpointBuilder
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
name|AdvancedSesEndpointBuilder
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
comment|/**      * AWS Simple Email Service (camel-aws-ses)      * The aws-ses component is used for sending emails with Amazon's SES      * service.      *       * Category: cloud,mail      * Available as of version: 2.9      * Maven coordinates: org.apache.camel:camel-aws-ses      *       * Syntax:<code>aws-ses:from</code>      *       * Path parameter: from (required)      * The sender's email address.      */
DECL|method|awsSes (String path)
specifier|default
name|SesEndpointBuilder
name|awsSes
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SesEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SesEndpointBuilder
implements|,
name|AdvancedSesEndpointBuilder
block|{
specifier|public
name|SesEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"aws-ses"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SesEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

