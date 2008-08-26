begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|mina
operator|.
name|common
operator|.
name|IoAcceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoAcceptorConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoConnectorConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoSession
import|;
end_import

begin_comment
comment|/**  * Endpoint for Camel MINA.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaEndpoint
specifier|public
class|class
name|MinaEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|MinaExchange
argument_list|>
block|{
DECL|field|DEFAULT_TIMEOUT
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_TIMEOUT
init|=
literal|30000
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
name|DEFAULT_TIMEOUT
decl_stmt|;
DECL|field|acceptor
specifier|private
specifier|final
name|IoAcceptor
name|acceptor
decl_stmt|;
DECL|field|address
specifier|private
specifier|final
name|SocketAddress
name|address
decl_stmt|;
DECL|field|connector
specifier|private
specifier|final
name|IoConnector
name|connector
decl_stmt|;
DECL|field|acceptorConfig
specifier|private
specifier|final
name|IoAcceptorConfig
name|acceptorConfig
decl_stmt|;
DECL|field|connectorConfig
specifier|private
specifier|final
name|IoConnectorConfig
name|connectorConfig
decl_stmt|;
DECL|field|lazySessionCreation
specifier|private
specifier|final
name|boolean
name|lazySessionCreation
decl_stmt|;
DECL|field|transferExchange
specifier|private
specifier|final
name|boolean
name|transferExchange
decl_stmt|;
DECL|field|sync
specifier|private
specifier|final
name|boolean
name|sync
decl_stmt|;
DECL|method|MinaEndpoint (String endpointUri, MinaComponent component, SocketAddress address, IoAcceptor acceptor, IoAcceptorConfig acceptorConfig, IoConnector connector, IoConnectorConfig connectorConfig, boolean lazySessionCreation, long timeout, boolean transferExchange, boolean sync)
specifier|public
name|MinaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|MinaComponent
name|component
parameter_list|,
name|SocketAddress
name|address
parameter_list|,
name|IoAcceptor
name|acceptor
parameter_list|,
name|IoAcceptorConfig
name|acceptorConfig
parameter_list|,
name|IoConnector
name|connector
parameter_list|,
name|IoConnectorConfig
name|connectorConfig
parameter_list|,
name|boolean
name|lazySessionCreation
parameter_list|,
name|long
name|timeout
parameter_list|,
name|boolean
name|transferExchange
parameter_list|,
name|boolean
name|sync
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
name|this
operator|.
name|acceptor
operator|=
name|acceptor
expr_stmt|;
name|this
operator|.
name|acceptorConfig
operator|=
name|acceptorConfig
expr_stmt|;
name|this
operator|.
name|connectorConfig
operator|=
name|connectorConfig
expr_stmt|;
name|this
operator|.
name|connector
operator|=
name|connector
expr_stmt|;
name|this
operator|.
name|lazySessionCreation
operator|=
name|lazySessionCreation
expr_stmt|;
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
comment|// override default timeout if provided
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
name|this
operator|.
name|transferExchange
operator|=
name|transferExchange
expr_stmt|;
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|MinaExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|MinaProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|MinaExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|MinaConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|MinaExchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|MinaExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createExchange (IoSession session, Object payload)
specifier|public
name|MinaExchange
name|createExchange
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|payload
parameter_list|)
block|{
name|MinaExchange
name|exchange
init|=
operator|new
name|MinaExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|,
name|session
argument_list|)
decl_stmt|;
name|MinaPayloadHelper
operator|.
name|setIn
argument_list|(
name|exchange
argument_list|,
name|payload
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getAcceptor ()
specifier|public
name|IoAcceptor
name|getAcceptor
parameter_list|()
block|{
return|return
name|acceptor
return|;
block|}
DECL|method|getAddress ()
specifier|public
name|SocketAddress
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|getConnector ()
specifier|public
name|IoConnector
name|getConnector
parameter_list|()
block|{
return|return
name|connector
return|;
block|}
DECL|method|isLazySessionCreation ()
specifier|public
name|boolean
name|isLazySessionCreation
parameter_list|()
block|{
return|return
name|lazySessionCreation
return|;
block|}
DECL|method|getAcceptorConfig ()
specifier|public
name|IoAcceptorConfig
name|getAcceptorConfig
parameter_list|()
block|{
return|return
name|acceptorConfig
return|;
block|}
DECL|method|getConnectorConfig ()
specifier|public
name|IoConnectorConfig
name|getConnectorConfig
parameter_list|()
block|{
return|return
name|connectorConfig
return|;
block|}
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
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|isTransferExchange ()
specifier|public
name|boolean
name|isTransferExchange
parameter_list|()
block|{
return|return
name|transferExchange
return|;
block|}
DECL|method|isSync ()
specifier|public
name|boolean
name|isSync
parameter_list|()
block|{
return|return
name|sync
return|;
block|}
block|}
end_class

end_unit

