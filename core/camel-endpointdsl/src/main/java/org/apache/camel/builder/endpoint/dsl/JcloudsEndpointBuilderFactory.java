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
name|ExchangePattern
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
name|ExceptionHandler
import|;
end_import

begin_comment
comment|/**  * For interacting with cloud compute& blobstore service via jclouds.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|JcloudsEndpointBuilderFactory
specifier|public
interface|interface
name|JcloudsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the JClouds component.      */
DECL|interface|JcloudsEndpointConsumerBuilder
specifier|public
interface|interface
name|JcloudsEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJcloudsEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJcloudsEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What command to execute such as blobstore or compute.          * The option is a          *<code>org.apache.camel.component.jclouds.JcloudsCommand</code> type.          * @group common          */
DECL|method|command (JcloudsCommand command)
specifier|default
name|JcloudsEndpointConsumerBuilder
name|command
parameter_list|(
name|JcloudsCommand
name|command
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"command"
argument_list|,
name|command
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What command to execute such as blobstore or compute.          * The option will be converted to a          *<code>org.apache.camel.component.jclouds.JcloudsCommand</code> type.          * @group common          */
DECL|method|command (String command)
specifier|default
name|JcloudsEndpointConsumerBuilder
name|command
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"command"
argument_list|,
name|command
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name of the cloud provider that provides the target service (e.g.          * aws-s3 or aws_ec2).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|providerId (String providerId)
specifier|default
name|JcloudsEndpointConsumerBuilder
name|providerId
parameter_list|(
name|String
name|providerId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"providerId"
argument_list|,
name|providerId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|JcloudsEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( String bridgeErrorHandler)
specifier|default
name|JcloudsEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|String
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name of the blob container.          * The option is a<code>java.lang.String</code> type.          * @group blobstore          */
DECL|method|container (String container)
specifier|default
name|JcloudsEndpointConsumerBuilder
name|container
parameter_list|(
name|String
name|container
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"container"
argument_list|,
name|container
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An optional directory name to use.          * The option is a<code>java.lang.String</code> type.          * @group blobstore          */
DECL|method|directory (String directory)
specifier|default
name|JcloudsEndpointConsumerBuilder
name|directory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"directory"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the JClouds component.      */
DECL|interface|AdvancedJcloudsEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedJcloudsEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JcloudsEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JcloudsEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedJcloudsEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option will be converted to a          *<code>org.apache.camel.spi.ExceptionHandler</code> type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( String exceptionHandler)
specifier|default
name|AdvancedJcloudsEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|String
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          * The option is a<code>org.apache.camel.ExchangePattern</code> type.          * @group consumer (advanced)          */
DECL|method|exchangePattern ( ExchangePattern exchangePattern)
specifier|default
name|AdvancedJcloudsEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          * The option will be converted to a          *<code>org.apache.camel.ExchangePattern</code> type.          * @group consumer (advanced)          */
DECL|method|exchangePattern ( String exchangePattern)
specifier|default
name|AdvancedJcloudsEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJcloudsEndpointConsumerBuilder
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
name|AdvancedJcloudsEndpointConsumerBuilder
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
name|AdvancedJcloudsEndpointConsumerBuilder
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
name|AdvancedJcloudsEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the JClouds component.      */
DECL|interface|JcloudsEndpointProducerBuilder
specifier|public
interface|interface
name|JcloudsEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJcloudsEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJcloudsEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What command to execute such as blobstore or compute.          * The option is a          *<code>org.apache.camel.component.jclouds.JcloudsCommand</code> type.          * @group common          */
DECL|method|command (JcloudsCommand command)
specifier|default
name|JcloudsEndpointProducerBuilder
name|command
parameter_list|(
name|JcloudsCommand
name|command
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"command"
argument_list|,
name|command
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What command to execute such as blobstore or compute.          * The option will be converted to a          *<code>org.apache.camel.component.jclouds.JcloudsCommand</code> type.          * @group common          */
DECL|method|command (String command)
specifier|default
name|JcloudsEndpointProducerBuilder
name|command
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"command"
argument_list|,
name|command
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name of the cloud provider that provides the target service (e.g.          * aws-s3 or aws_ec2).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|providerId (String providerId)
specifier|default
name|JcloudsEndpointProducerBuilder
name|providerId
parameter_list|(
name|String
name|providerId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"providerId"
argument_list|,
name|providerId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|JcloudsEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|default
name|JcloudsEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
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
comment|/**          * The name of the blob.          * The option is a<code>java.lang.String</code> type.          * @group blobstore          */
DECL|method|blobName (String blobName)
specifier|default
name|JcloudsEndpointProducerBuilder
name|blobName
parameter_list|(
name|String
name|blobName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"blobName"
argument_list|,
name|blobName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name of the blob container.          * The option is a<code>java.lang.String</code> type.          * @group blobstore          */
DECL|method|container (String container)
specifier|default
name|JcloudsEndpointProducerBuilder
name|container
parameter_list|(
name|String
name|container
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"container"
argument_list|,
name|container
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The group that will be assigned to the newly created node. Values          * depend on the actual cloud provider.          * The option is a<code>java.lang.String</code> type.          * @group compute          */
DECL|method|group (String group)
specifier|default
name|JcloudsEndpointProducerBuilder
name|group
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"group"
argument_list|,
name|group
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hardware that will be used for creating a node. Values depend on          * the actual cloud provider.          * The option is a<code>java.lang.String</code> type.          * @group compute          */
DECL|method|hardwareId (String hardwareId)
specifier|default
name|JcloudsEndpointProducerBuilder
name|hardwareId
parameter_list|(
name|String
name|hardwareId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hardwareId"
argument_list|,
name|hardwareId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The imageId that will be used for creating a node. Values depend on          * the actual cloud provider.          * The option is a<code>java.lang.String</code> type.          * @group compute          */
DECL|method|imageId (String imageId)
specifier|default
name|JcloudsEndpointProducerBuilder
name|imageId
parameter_list|(
name|String
name|imageId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"imageId"
argument_list|,
name|imageId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The location that will be used for creating a node. Values depend on          * the actual cloud provider.          * The option is a<code>java.lang.String</code> type.          * @group compute          */
DECL|method|locationId (String locationId)
specifier|default
name|JcloudsEndpointProducerBuilder
name|locationId
parameter_list|(
name|String
name|locationId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"locationId"
argument_list|,
name|locationId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The id of the node that will run the script or destroyed.          * The option is a<code>java.lang.String</code> type.          * @group compute          */
DECL|method|nodeId (String nodeId)
specifier|default
name|JcloudsEndpointProducerBuilder
name|nodeId
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"nodeId"
argument_list|,
name|nodeId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To filter by node status to only select running nodes etc.          * The option is a<code>java.lang.String</code> type.          * @group compute          */
DECL|method|nodeState (String nodeState)
specifier|default
name|JcloudsEndpointProducerBuilder
name|nodeState
parameter_list|(
name|String
name|nodeState
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"nodeState"
argument_list|,
name|nodeState
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the type of operation that will be performed to the          * blobstore.          * The option is a<code>java.lang.String</code> type.          * @group compute          */
DECL|method|operation (String operation)
specifier|default
name|JcloudsEndpointProducerBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * The user on the target node that will run the script.          * The option is a<code>java.lang.String</code> type.          * @group compute          */
DECL|method|user (String user)
specifier|default
name|JcloudsEndpointProducerBuilder
name|user
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"user"
argument_list|,
name|user
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the JClouds component.      */
DECL|interface|AdvancedJcloudsEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedJcloudsEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JcloudsEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JcloudsEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJcloudsEndpointProducerBuilder
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
name|AdvancedJcloudsEndpointProducerBuilder
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
name|AdvancedJcloudsEndpointProducerBuilder
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
name|AdvancedJcloudsEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the JClouds component.      */
DECL|interface|JcloudsEndpointBuilder
specifier|public
interface|interface
name|JcloudsEndpointBuilder
extends|extends
name|JcloudsEndpointConsumerBuilder
extends|,
name|JcloudsEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJcloudsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJcloudsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * What command to execute such as blobstore or compute.          * The option is a          *<code>org.apache.camel.component.jclouds.JcloudsCommand</code> type.          * @group common          */
DECL|method|command (JcloudsCommand command)
specifier|default
name|JcloudsEndpointBuilder
name|command
parameter_list|(
name|JcloudsCommand
name|command
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"command"
argument_list|,
name|command
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What command to execute such as blobstore or compute.          * The option will be converted to a          *<code>org.apache.camel.component.jclouds.JcloudsCommand</code> type.          * @group common          */
DECL|method|command (String command)
specifier|default
name|JcloudsEndpointBuilder
name|command
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"command"
argument_list|,
name|command
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name of the cloud provider that provides the target service (e.g.          * aws-s3 or aws_ec2).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|providerId (String providerId)
specifier|default
name|JcloudsEndpointBuilder
name|providerId
parameter_list|(
name|String
name|providerId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"providerId"
argument_list|,
name|providerId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name of the blob container.          * The option is a<code>java.lang.String</code> type.          * @group blobstore          */
DECL|method|container (String container)
specifier|default
name|JcloudsEndpointBuilder
name|container
parameter_list|(
name|String
name|container
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"container"
argument_list|,
name|container
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the JClouds component.      */
DECL|interface|AdvancedJcloudsEndpointBuilder
specifier|public
interface|interface
name|AdvancedJcloudsEndpointBuilder
extends|extends
name|AdvancedJcloudsEndpointConsumerBuilder
extends|,
name|AdvancedJcloudsEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JcloudsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JcloudsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJcloudsEndpointBuilder
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
name|AdvancedJcloudsEndpointBuilder
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
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedJcloudsEndpointBuilder
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
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedJcloudsEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.jclouds.JcloudsCommand</code> enum.      */
DECL|enum|JcloudsCommand
specifier|static
enum|enum
name|JcloudsCommand
block|{
DECL|enumConstant|blobstore
DECL|enumConstant|compute
name|blobstore
block|,
name|compute
block|;     }
comment|/**      * For interacting with cloud compute& blobstore service via jclouds.      * Creates a builder to build endpoints for the JClouds component.      */
DECL|method|jclouds (String path)
specifier|default
name|JcloudsEndpointBuilder
name|jclouds
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|JcloudsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|JcloudsEndpointBuilder
implements|,
name|AdvancedJcloudsEndpointBuilder
block|{
specifier|public
name|JcloudsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"jclouds"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|JcloudsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

