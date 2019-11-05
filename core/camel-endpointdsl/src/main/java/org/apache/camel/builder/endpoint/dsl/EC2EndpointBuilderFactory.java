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
comment|/**  * The aws-ec2 is used for managing Amazon EC2 instances.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|EC2EndpointBuilderFactory
specifier|public
interface|interface
name|EC2EndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the AWS EC2 component.      */
DECL|interface|EC2EndpointBuilder
specifier|public
interface|interface
name|EC2EndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedEC2EndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedEC2EndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Amazon AWS Access Key.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|accessKey (String accessKey)
specifier|default
name|EC2EndpointBuilder
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
comment|/**          * To use a existing configured AmazonEC2Client as client.          *           * The option is a:<code>com.amazonaws.services.ec2.AmazonEC2</code>          * type.          *           * Group: producer          */
DECL|method|amazonEc2Client (Object amazonEc2Client)
specifier|default
name|EC2EndpointBuilder
name|amazonEc2Client
parameter_list|(
name|Object
name|amazonEc2Client
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"amazonEc2Client"
argument_list|,
name|amazonEc2Client
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a existing configured AmazonEC2Client as client.          *           * The option will be converted to a          *<code>com.amazonaws.services.ec2.AmazonEC2</code> type.          *           * Group: producer          */
DECL|method|amazonEc2Client (String amazonEc2Client)
specifier|default
name|EC2EndpointBuilder
name|amazonEc2Client
parameter_list|(
name|String
name|amazonEc2Client
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"amazonEc2Client"
argument_list|,
name|amazonEc2Client
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (boolean lazyStartProducer)
specifier|default
name|EC2EndpointBuilder
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
name|EC2EndpointBuilder
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
comment|/**          * The operation to perform. It can be createAndRunInstances,          * startInstances, stopInstances, terminateInstances, describeInstances,          * describeInstancesStatus, rebootInstances, monitorInstances,          * unmonitorInstances, createTags or deleteTags.          *           * The option is a:          *<code>org.apache.camel.component.aws.ec2.EC2Operations</code> type.          *           * Required: true          * Group: producer          */
DECL|method|operation (EC2Operations operation)
specifier|default
name|EC2EndpointBuilder
name|operation
parameter_list|(
name|EC2Operations
name|operation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The operation to perform. It can be createAndRunInstances,          * startInstances, stopInstances, terminateInstances, describeInstances,          * describeInstancesStatus, rebootInstances, monitorInstances,          * unmonitorInstances, createTags or deleteTags.          *           * The option will be converted to a          *<code>org.apache.camel.component.aws.ec2.EC2Operations</code> type.          *           * Required: true          * Group: producer          */
DECL|method|operation (String operation)
specifier|default
name|EC2EndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To define a proxy host when instantiating the EC2 client.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|proxyHost (String proxyHost)
specifier|default
name|EC2EndpointBuilder
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
comment|/**          * To define a proxy port when instantiating the EC2 client.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|proxyPort (Integer proxyPort)
specifier|default
name|EC2EndpointBuilder
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
comment|/**          * To define a proxy port when instantiating the EC2 client.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|proxyPort (String proxyPort)
specifier|default
name|EC2EndpointBuilder
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
comment|/**          * To define a proxy protocol when instantiating the EC2 client.          *           * The option is a:<code>com.amazonaws.Protocol</code> type.          *           * Group: producer          */
DECL|method|proxyProtocol (Protocol proxyProtocol)
specifier|default
name|EC2EndpointBuilder
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
comment|/**          * To define a proxy protocol when instantiating the EC2 client.          *           * The option will be converted to a<code>com.amazonaws.Protocol</code>          * type.          *           * Group: producer          */
DECL|method|proxyProtocol (String proxyProtocol)
specifier|default
name|EC2EndpointBuilder
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
comment|/**          * The region in which EC2 client needs to work. When using this          * parameter, the configuration will expect the capitalized name of the          * region (for example AP_EAST_1) You'll need to use the name          * Regions.EU_WEST_1.name().          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|region (String region)
specifier|default
name|EC2EndpointBuilder
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
comment|/**          * Amazon AWS Secret Key.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|secretKey (String secretKey)
specifier|default
name|EC2EndpointBuilder
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
comment|/**      * Advanced builder for endpoint for the AWS EC2 component.      */
DECL|interface|AdvancedEC2EndpointBuilder
specifier|public
interface|interface
name|AdvancedEC2EndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|EC2EndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|EC2EndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedEC2EndpointBuilder
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
name|AdvancedEC2EndpointBuilder
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
name|AdvancedEC2EndpointBuilder
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
name|AdvancedEC2EndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.aws.ec2.EC2Operations</code> enum.      */
DECL|enum|EC2Operations
enum|enum
name|EC2Operations
block|{
DECL|enumConstant|createAndRunInstances
name|createAndRunInstances
block|,
DECL|enumConstant|startInstances
name|startInstances
block|,
DECL|enumConstant|stopInstances
name|stopInstances
block|,
DECL|enumConstant|terminateInstances
name|terminateInstances
block|,
DECL|enumConstant|describeInstances
name|describeInstances
block|,
DECL|enumConstant|describeInstancesStatus
name|describeInstancesStatus
block|,
DECL|enumConstant|rebootInstances
name|rebootInstances
block|,
DECL|enumConstant|monitorInstances
name|monitorInstances
block|,
DECL|enumConstant|unmonitorInstances
name|unmonitorInstances
block|,
DECL|enumConstant|createTags
name|createTags
block|,
DECL|enumConstant|deleteTags
name|deleteTags
block|;     }
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
comment|/**      * AWS EC2 (camel-aws-ec2)      * The aws-ec2 is used for managing Amazon EC2 instances.      *       * Category: cloud,management      * Available as of version: 2.16      * Maven coordinates: org.apache.camel:camel-aws-ec2      *       * Syntax:<code>aws-ec2:label</code>      *       * Path parameter: label (required)      * Logical name      */
DECL|method|awsEc2 (String path)
specifier|default
name|EC2EndpointBuilder
name|awsEc2
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|EC2EndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|EC2EndpointBuilder
implements|,
name|AdvancedEC2EndpointBuilder
block|{
specifier|public
name|EC2EndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"aws-ec2"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|EC2EndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

