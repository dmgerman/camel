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
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
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
block|{
comment|/** The key of the IoSession which is stored in the message header*/
DECL|field|HEADER_MINA_IOSESSION
specifier|public
specifier|static
specifier|final
specifier|transient
name|String
name|HEADER_MINA_IOSESSION
init|=
literal|"CamelMinaIoSession"
decl_stmt|;
comment|/** The socket address of local machine that received the message. */
DECL|field|HEADER_LOCAL_ADDRESS
specifier|public
specifier|static
specifier|final
specifier|transient
name|String
name|HEADER_LOCAL_ADDRESS
init|=
literal|"CamelMinaLocalAddress"
decl_stmt|;
comment|/** The socket address of the remote machine that send the message. */
DECL|field|HEADER_REMOTE_ADDRESS
specifier|public
specifier|static
specifier|final
specifier|transient
name|String
name|HEADER_REMOTE_ADDRESS
init|=
literal|"CamelMinaRemoteAddress"
decl_stmt|;
DECL|field|address
specifier|private
name|SocketAddress
name|address
decl_stmt|;
DECL|field|acceptor
specifier|private
name|IoAcceptor
name|acceptor
decl_stmt|;
DECL|field|connector
specifier|private
name|IoConnector
name|connector
decl_stmt|;
DECL|field|acceptorConfig
specifier|private
name|IoAcceptorConfig
name|acceptorConfig
decl_stmt|;
DECL|field|connectorConfig
specifier|private
name|IoConnectorConfig
name|connectorConfig
decl_stmt|;
DECL|field|configuration
specifier|private
name|MinaConfiguration
name|configuration
decl_stmt|;
DECL|method|MinaEndpoint ()
specifier|public
name|MinaEndpoint
parameter_list|()
block|{     }
DECL|method|MinaEndpoint (String endpointUri, MinaComponent component)
specifier|public
name|MinaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|MinaComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|address
argument_list|,
literal|"address"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|connector
argument_list|,
literal|"connector"
argument_list|)
expr_stmt|;
comment|// wm protocol does not have config
if|if
condition|(
operator|!
name|configuration
operator|.
name|getProtocol
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"vm"
argument_list|)
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|connectorConfig
argument_list|,
literal|"connectorConfig"
argument_list|)
expr_stmt|;
block|}
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
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|address
argument_list|,
literal|"address"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|acceptor
argument_list|,
literal|"acceptor"
argument_list|)
expr_stmt|;
comment|// wm protocol does not have config
if|if
condition|(
operator|!
name|configuration
operator|.
name|getProtocol
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"vm"
argument_list|)
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|acceptorConfig
argument_list|,
literal|"acceptorConfig"
argument_list|)
expr_stmt|;
block|}
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
DECL|method|createExchange (IoSession session, Object payload)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|payload
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_MINA_IOSESSION
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_LOCAL_ADDRESS
argument_list|,
name|session
operator|.
name|getLocalAddress
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_REMOTE_ADDRESS
argument_list|,
name|session
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getConfiguration ()
specifier|public
name|MinaConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (MinaConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MinaConfiguration
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
DECL|method|setAddress (SocketAddress address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|SocketAddress
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
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
DECL|method|setAcceptor (IoAcceptor acceptor)
specifier|public
name|void
name|setAcceptor
parameter_list|(
name|IoAcceptor
name|acceptor
parameter_list|)
block|{
name|this
operator|.
name|acceptor
operator|=
name|acceptor
expr_stmt|;
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
DECL|method|setConnector (IoConnector connector)
specifier|public
name|void
name|setConnector
parameter_list|(
name|IoConnector
name|connector
parameter_list|)
block|{
name|this
operator|.
name|connector
operator|=
name|connector
expr_stmt|;
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
DECL|method|setAcceptorConfig (IoAcceptorConfig acceptorConfig)
specifier|public
name|void
name|setAcceptorConfig
parameter_list|(
name|IoAcceptorConfig
name|acceptorConfig
parameter_list|)
block|{
name|this
operator|.
name|acceptorConfig
operator|=
name|acceptorConfig
expr_stmt|;
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
DECL|method|setConnectorConfig (IoConnectorConfig connectorConfig)
specifier|public
name|void
name|setConnectorConfig
parameter_list|(
name|IoConnectorConfig
name|connectorConfig
parameter_list|)
block|{
name|this
operator|.
name|connectorConfig
operator|=
name|connectorConfig
expr_stmt|;
block|}
block|}
end_class

end_unit

