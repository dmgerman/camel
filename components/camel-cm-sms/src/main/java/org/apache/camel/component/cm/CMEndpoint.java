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
name|java
operator|.
name|util
operator|.
name|UUID
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
name|UriEndpoint
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
name|UriParam
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * The cm-sms component allows to integrate with<a href="https://www.cmtelecom.com/">CM SMS Gateway</a>.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.18.0"
argument_list|,
name|scheme
operator|=
literal|"cm-sms"
argument_list|,
name|title
operator|=
literal|"CM SMS Gateway"
argument_list|,
name|syntax
operator|=
literal|"cm-sms:host"
argument_list|,
name|label
operator|=
literal|"mobile"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|)
DECL|class|CMEndpoint
specifier|public
class|class
name|CMEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|CMConfiguration
name|configuration
decl_stmt|;
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
block|}
comment|/**      * Provides a channel on which clients can send Messages to a CM Endpoint      */
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|CMProducer
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
comment|// CMConstants.DEFAULT_SCHEME + host is a valid URL. It was previously checked
name|String
name|token
init|=
name|config
operator|.
name|getProductToken
argument_list|()
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|token
argument_list|,
literal|"productToken"
argument_list|)
expr_stmt|;
name|UUID
name|uuid
init|=
name|UUID
operator|.
name|fromString
argument_list|(
name|token
argument_list|)
decl_stmt|;
return|return
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
name|uuid
argument_list|)
argument_list|)
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
name|UnsupportedOperationException
argument_list|(
literal|"Consumer not supported"
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
comment|/**      * SMS Provider HOST with scheme      */
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
block|}
end_class

end_unit

