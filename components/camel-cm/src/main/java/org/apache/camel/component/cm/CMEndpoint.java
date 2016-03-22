begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Processor
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
name|Producer
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
name|RuntimeCamelException
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|impl
operator|.
name|DefaultEndpoint
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
name|Metadata
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
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"CM SMS Endpoint"
argument_list|)
comment|// @UriEndpoint(scheme = "cm", title = "CM DIRECT SMS", syntax = "cm:host",
comment|// label = "sms provider", producerOnly = true)
DECL|class|CMEndpoint
specifier|public
class|class
name|CMEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CMEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"SMS Provider HOST with scheme"
argument_list|,
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * data needed for exchange interaction      */
DECL|field|configuration
specifier|private
name|CMConfiguration
name|configuration
decl_stmt|;
DECL|field|producer
specifier|private
name|CMProducer
name|producer
decl_stmt|;
comment|/**      * Constructs a partially-initialized CMEndpoint instance. Useful when creating endpoints manually (e.g., as beans in Spring).      */
comment|// We are just going to allow fully initialized endpoint instances
comment|// public CMEndpoint() {
comment|// }
comment|/**      * Constructs a fully-initialized CMEndpoint instance. This is the preferred method of constructing an object from Java code (as opposed to Spring beans, etc.).      *      * @param endpointUri the full URI used to create this endpoint      * @param component the component that created this endpoint      */
DECL|method|CMEndpoint (final String uri, final CMComponent component)
specifier|public
name|CMEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|CMComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"CM Endpoint created"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Provides a channel on which clients can send Messages to a CM Endpoint      */
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CMConfiguration
name|config
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
comment|// This is the camel exchange processor. Allows to send messages to CM
comment|// API.
comment|// TODO: Should i provide a CMSender factory? Dynamically choose
comment|// CMSender implementation? Sending strategy?
comment|// Consider:
comment|// 1. single - Single Message strategy.
comment|// 2. Multi - CM Api supports to 1000 messages per call.
comment|// 3. sliding - sliding window? 1000 messages or time thresold?
comment|// 4. mocked - in order to fake cm responses
comment|// CMConstants.DEFAULT_SCHEME + host is a valid URL. It was previously
comment|// checked
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating CM Producer"
argument_list|)
expr_stmt|;
name|producer
operator|=
operator|new
name|CMProducer
argument_list|(
name|this
argument_list|,
operator|new
name|CMSenderOneMessageImpl
argument_list|(
name|getCMUrl
argument_list|()
argument_list|,
name|config
operator|.
name|getProductToken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"CM Producer: OK!"
argument_list|)
expr_stmt|;
return|return
name|producer
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (final Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"So far, cannot consume from CM Endpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|CMConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (final CMConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
specifier|final
name|CMConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|// @Override
comment|// public Exchange createExchange() {
comment|// return super.createExchange();
comment|// }
annotation|@
name|ManagedAttribute
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|getCMUrl ()
specifier|public
name|String
name|getCMUrl
parameter_list|()
block|{
return|return
name|CMConstants
operator|.
name|DEFAULT_SCHEME
operator|+
name|host
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dynamically modify Service HOST"
argument_list|)
DECL|method|setHost (final String host)
specifier|public
name|void
name|setHost
parameter_list|(
specifier|final
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|CMComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|CMComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getProducer ()
specifier|public
name|CMProducer
name|getProducer
parameter_list|()
block|{
return|return
name|producer
return|;
block|}
block|}
end_class

end_unit

