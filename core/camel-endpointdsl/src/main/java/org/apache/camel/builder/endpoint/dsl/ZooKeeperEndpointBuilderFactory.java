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
comment|/**  * The zookeeper component allows interaction with a ZooKeeper cluster.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ZooKeeperEndpointBuilderFactory
specifier|public
interface|interface
name|ZooKeeperEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the ZooKeeper component.      */
DECL|interface|ZooKeeperEndpointConsumerBuilder
specifier|public
interface|interface
name|ZooKeeperEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedZooKeeperEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedZooKeeperEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The zookeeper server hosts (multiple servers can be separated by          * comma).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|serverUrls (String serverUrls)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|serverUrls
parameter_list|(
name|String
name|serverUrls
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"serverUrls"
argument_list|,
name|serverUrls
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The node in the ZooKeeper server (aka znode).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|path (String path)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|path
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"path"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Not in use.          * The option is a<code>boolean</code> type.          * @group common          */
annotation|@
name|Deprecated
DECL|method|awaitExistence ( boolean awaitExistence)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|awaitExistence
parameter_list|(
name|boolean
name|awaitExistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"awaitExistence"
argument_list|,
name|awaitExistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Not in use.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
annotation|@
name|Deprecated
DECL|method|awaitExistence ( String awaitExistence)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|awaitExistence
parameter_list|(
name|String
name|awaitExistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"awaitExistence"
argument_list|,
name|awaitExistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the children of the node should be listed.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|listChildren ( boolean listChildren)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|listChildren
parameter_list|(
name|boolean
name|listChildren
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listChildren"
argument_list|,
name|listChildren
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the children of the node should be listed.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|listChildren ( String listChildren)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|listChildren
parameter_list|(
name|String
name|listChildren
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listChildren"
argument_list|,
name|listChildren
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time interval to wait on connection before timing out.          * The option is a<code>int</code> type.          * @group common          */
DECL|method|timeout (int timeout)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|timeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time interval to wait on connection before timing out.          * The option will be converted to a<code>int</code> type.          * @group common          */
DECL|method|timeout (String timeout)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|timeout
parameter_list|(
name|String
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time interval to backoff for after an error before retrying.          * The option is a<code>long</code> type.          * @group consumer          */
DECL|method|backoff (long backoff)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|backoff
parameter_list|(
name|long
name|backoff
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"backoff"
argument_list|,
name|backoff
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time interval to backoff for after an error before retrying.          * The option will be converted to a<code>long</code> type.          * @group consumer          */
DECL|method|backoff (String backoff)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|backoff
parameter_list|(
name|String
name|backoff
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"backoff"
argument_list|,
name|backoff
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
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
name|ZooKeeperEndpointConsumerBuilder
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
comment|/**          * Should changes to the znode be 'watched' and repeatedly processed.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|repeat (boolean repeat)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|repeat
parameter_list|(
name|boolean
name|repeat
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"repeat"
argument_list|,
name|repeat
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Should changes to the znode be 'watched' and repeatedly processed.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|repeat (String repeat)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|repeat
parameter_list|(
name|String
name|repeat
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"repeat"
argument_list|,
name|repeat
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Upon the delete of a znode, should an empty message be send to the          * consumer.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|sendEmptyMessageOnDelete ( boolean sendEmptyMessageOnDelete)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|sendEmptyMessageOnDelete
parameter_list|(
name|boolean
name|sendEmptyMessageOnDelete
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sendEmptyMessageOnDelete"
argument_list|,
name|sendEmptyMessageOnDelete
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Upon the delete of a znode, should an empty message be send to the          * consumer.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|sendEmptyMessageOnDelete ( String sendEmptyMessageOnDelete)
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|sendEmptyMessageOnDelete
parameter_list|(
name|String
name|sendEmptyMessageOnDelete
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sendEmptyMessageOnDelete"
argument_list|,
name|sendEmptyMessageOnDelete
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the ZooKeeper component.      */
DECL|interface|AdvancedZooKeeperEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedZooKeeperEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ZooKeeperEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ZooKeeperEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          * The option is a<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          * @group consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedZooKeeperEndpointConsumerBuilder
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
name|AdvancedZooKeeperEndpointConsumerBuilder
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
name|AdvancedZooKeeperEndpointConsumerBuilder
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
name|AdvancedZooKeeperEndpointConsumerBuilder
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
name|AdvancedZooKeeperEndpointConsumerBuilder
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
name|AdvancedZooKeeperEndpointConsumerBuilder
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
name|AdvancedZooKeeperEndpointConsumerBuilder
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
name|AdvancedZooKeeperEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the ZooKeeper component.      */
DECL|interface|ZooKeeperEndpointProducerBuilder
specifier|public
interface|interface
name|ZooKeeperEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedZooKeeperEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedZooKeeperEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The zookeeper server hosts (multiple servers can be separated by          * comma).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|serverUrls (String serverUrls)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|serverUrls
parameter_list|(
name|String
name|serverUrls
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"serverUrls"
argument_list|,
name|serverUrls
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The node in the ZooKeeper server (aka znode).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|path (String path)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|path
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"path"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Not in use.          * The option is a<code>boolean</code> type.          * @group common          */
annotation|@
name|Deprecated
DECL|method|awaitExistence ( boolean awaitExistence)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|awaitExistence
parameter_list|(
name|boolean
name|awaitExistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"awaitExistence"
argument_list|,
name|awaitExistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Not in use.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
annotation|@
name|Deprecated
DECL|method|awaitExistence ( String awaitExistence)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|awaitExistence
parameter_list|(
name|String
name|awaitExistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"awaitExistence"
argument_list|,
name|awaitExistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the children of the node should be listed.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|listChildren ( boolean listChildren)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|listChildren
parameter_list|(
name|boolean
name|listChildren
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listChildren"
argument_list|,
name|listChildren
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the children of the node should be listed.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|listChildren ( String listChildren)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|listChildren
parameter_list|(
name|String
name|listChildren
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listChildren"
argument_list|,
name|listChildren
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time interval to wait on connection before timing out.          * The option is a<code>int</code> type.          * @group common          */
DECL|method|timeout (int timeout)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|timeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time interval to wait on connection before timing out.          * The option will be converted to a<code>int</code> type.          * @group common          */
DECL|method|timeout (String timeout)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|timeout
parameter_list|(
name|String
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Should the endpoint create the node if it does not currently exist.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|create (boolean create)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|create
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"create"
argument_list|,
name|create
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Should the endpoint create the node if it does not currently exist.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|create (String create)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|create
parameter_list|(
name|String
name|create
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"create"
argument_list|,
name|create
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The create mode that should be used for the newly created node.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|createMode (String createMode)
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|createMode
parameter_list|(
name|String
name|createMode
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"createMode"
argument_list|,
name|createMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|ZooKeeperEndpointProducerBuilder
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
name|ZooKeeperEndpointProducerBuilder
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
block|}
comment|/**      * Advanced builder for endpoint producers for the ZooKeeper component.      */
DECL|interface|AdvancedZooKeeperEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedZooKeeperEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ZooKeeperEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ZooKeeperEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedZooKeeperEndpointProducerBuilder
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
name|AdvancedZooKeeperEndpointProducerBuilder
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
name|AdvancedZooKeeperEndpointProducerBuilder
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
name|AdvancedZooKeeperEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the ZooKeeper component.      */
DECL|interface|ZooKeeperEndpointBuilder
specifier|public
interface|interface
name|ZooKeeperEndpointBuilder
extends|extends
name|ZooKeeperEndpointConsumerBuilder
extends|,
name|ZooKeeperEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedZooKeeperEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedZooKeeperEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The zookeeper server hosts (multiple servers can be separated by          * comma).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|serverUrls (String serverUrls)
specifier|default
name|ZooKeeperEndpointBuilder
name|serverUrls
parameter_list|(
name|String
name|serverUrls
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"serverUrls"
argument_list|,
name|serverUrls
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The node in the ZooKeeper server (aka znode).          * The option is a<code>java.lang.String</code> type.          * @group common          */
DECL|method|path (String path)
specifier|default
name|ZooKeeperEndpointBuilder
name|path
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"path"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Not in use.          * The option is a<code>boolean</code> type.          * @group common          */
annotation|@
name|Deprecated
DECL|method|awaitExistence (boolean awaitExistence)
specifier|default
name|ZooKeeperEndpointBuilder
name|awaitExistence
parameter_list|(
name|boolean
name|awaitExistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"awaitExistence"
argument_list|,
name|awaitExistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Not in use.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
annotation|@
name|Deprecated
DECL|method|awaitExistence (String awaitExistence)
specifier|default
name|ZooKeeperEndpointBuilder
name|awaitExistence
parameter_list|(
name|String
name|awaitExistence
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"awaitExistence"
argument_list|,
name|awaitExistence
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the children of the node should be listed.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|listChildren (boolean listChildren)
specifier|default
name|ZooKeeperEndpointBuilder
name|listChildren
parameter_list|(
name|boolean
name|listChildren
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listChildren"
argument_list|,
name|listChildren
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the children of the node should be listed.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|listChildren (String listChildren)
specifier|default
name|ZooKeeperEndpointBuilder
name|listChildren
parameter_list|(
name|String
name|listChildren
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"listChildren"
argument_list|,
name|listChildren
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time interval to wait on connection before timing out.          * The option is a<code>int</code> type.          * @group common          */
DECL|method|timeout (int timeout)
specifier|default
name|ZooKeeperEndpointBuilder
name|timeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time interval to wait on connection before timing out.          * The option will be converted to a<code>int</code> type.          * @group common          */
DECL|method|timeout (String timeout)
specifier|default
name|ZooKeeperEndpointBuilder
name|timeout
parameter_list|(
name|String
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the ZooKeeper component.      */
DECL|interface|AdvancedZooKeeperEndpointBuilder
specifier|public
interface|interface
name|AdvancedZooKeeperEndpointBuilder
extends|extends
name|AdvancedZooKeeperEndpointConsumerBuilder
extends|,
name|AdvancedZooKeeperEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ZooKeeperEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ZooKeeperEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedZooKeeperEndpointBuilder
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
name|AdvancedZooKeeperEndpointBuilder
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
name|AdvancedZooKeeperEndpointBuilder
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
name|AdvancedZooKeeperEndpointBuilder
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
comment|/**      * The zookeeper component allows interaction with a ZooKeeper cluster.      * Creates a builder to build endpoints for the ZooKeeper component.      */
DECL|method|zooKeeper (String path)
specifier|default
name|ZooKeeperEndpointBuilder
name|zooKeeper
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ZooKeeperEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ZooKeeperEndpointBuilder
implements|,
name|AdvancedZooKeeperEndpointBuilder
block|{
specifier|public
name|ZooKeeperEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"zookeeper"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ZooKeeperEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

