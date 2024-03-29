begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sip
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sip
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|ListeningPoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|SipProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|SipStack
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|message
operator|.
name|Request
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
name|CamelExchangeException
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
name|Exchange
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
name|component
operator|.
name|sip
operator|.
name|listener
operator|.
name|SipPublishListener
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_class
DECL|class|SipPublisher
specifier|public
class|class
name|SipPublisher
extends|extends
name|DefaultProducer
block|{
DECL|field|configuration
specifier|private
name|SipConfiguration
name|configuration
decl_stmt|;
DECL|field|sequenceNumber
specifier|private
name|long
name|sequenceNumber
init|=
literal|1
decl_stmt|;
DECL|field|sipPublishListener
specifier|private
name|SipPublishListener
name|sipPublishListener
decl_stmt|;
DECL|field|provider
specifier|private
name|SipProvider
name|provider
decl_stmt|;
DECL|field|sipStack
specifier|private
name|SipStack
name|sipStack
decl_stmt|;
DECL|method|SipPublisher (SipEndpoint sipEndpoint, SipConfiguration configuration)
specifier|public
name|SipPublisher
parameter_list|(
name|SipEndpoint
name|sipEndpoint
parameter_list|,
name|SipConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|sipEndpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|Properties
name|properties
init|=
name|configuration
operator|.
name|createInitialProperties
argument_list|()
decl_stmt|;
name|setSipStack
argument_list|(
name|configuration
operator|.
name|getSipFactory
argument_list|()
operator|.
name|createSipStack
argument_list|(
name|properties
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|parseURI
argument_list|()
expr_stmt|;
if|if
condition|(
name|sipPublishListener
operator|==
literal|null
condition|)
block|{
name|sipPublishListener
operator|=
operator|new
name|SipPublishListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|.
name|setListeningPoint
argument_list|(
name|sipStack
operator|.
name|createListeningPoint
argument_list|(
name|configuration
operator|.
name|getFromHost
argument_list|()
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|configuration
operator|.
name|getFromPort
argument_list|()
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|,
name|configuration
operator|.
name|getTransport
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ListeningPoint
name|listeningPoint
range|:
name|provider
operator|.
name|getListeningPoints
argument_list|()
control|)
block|{
if|if
condition|(
name|listeningPoint
operator|.
name|getIPAddress
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|configuration
operator|.
name|getListeningPoint
argument_list|()
operator|.
name|getIPAddress
argument_list|()
argument_list|)
operator|&&
operator|(
name|listeningPoint
operator|.
name|getPort
argument_list|()
operator|==
name|configuration
operator|.
name|getListeningPoint
argument_list|()
operator|.
name|getPort
argument_list|()
operator|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|provider
operator|=
name|getSipStack
argument_list|()
operator|.
name|createSipProvider
argument_list|(
name|configuration
operator|.
name|getListeningPoint
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|addSipListener
argument_list|(
name|sipPublishListener
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCallIdHeader
argument_list|(
name|provider
operator|.
name|getNewCallId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|getSipStack
argument_list|()
operator|.
name|deleteListeningPoint
argument_list|(
name|configuration
operator|.
name|getListeningPoint
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|removeSipListener
argument_list|(
name|sipPublishListener
argument_list|)
expr_stmt|;
name|getSipStack
argument_list|()
operator|.
name|deleteSipProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|getSipStack
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|requestMethod
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"REQUEST_METHOD"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Missing mandatory Header: REQUEST_HEADER"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Request
name|request
init|=
name|configuration
operator|.
name|createSipRequest
argument_list|(
name|sequenceNumber
argument_list|,
name|requestMethod
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|provider
operator|.
name|sendRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
DECL|method|setConfiguration (SipConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SipConfiguration
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
DECL|method|getConfiguration ()
specifier|public
name|SipConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setSipStack (SipStack sipStack)
specifier|public
name|void
name|setSipStack
parameter_list|(
name|SipStack
name|sipStack
parameter_list|)
block|{
name|this
operator|.
name|sipStack
operator|=
name|sipStack
expr_stmt|;
block|}
DECL|method|getSipStack ()
specifier|public
name|SipStack
name|getSipStack
parameter_list|()
block|{
return|return
name|sipStack
return|;
block|}
block|}
end_class

end_unit

