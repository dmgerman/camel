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
name|IoServiceConfig
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
comment|/**  * @version $Revision$  */
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
DECL|field|config
specifier|private
specifier|final
name|IoServiceConfig
name|config
decl_stmt|;
DECL|method|MinaEndpoint (String endpointUri, MinaComponent component, SocketAddress address, IoAcceptor acceptor, IoConnector connector, IoServiceConfig config)
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
name|IoConnector
name|connector
parameter_list|,
name|IoServiceConfig
name|config
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
name|config
operator|=
name|config
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
name|connector
operator|=
name|connector
expr_stmt|;
block|}
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
DECL|method|createExchange ()
specifier|public
name|MinaExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|MinaExchange
argument_list|(
name|getContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (IoSession session, Object object)
specifier|public
name|MinaExchange
name|createExchange
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|MinaExchange
name|exchange
init|=
operator|new
name|MinaExchange
argument_list|(
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|object
argument_list|)
expr_stmt|;
comment|// TODO store session in exchange?
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
DECL|method|getConfig ()
specifier|public
name|IoServiceConfig
name|getConfig
parameter_list|()
block|{
return|return
name|config
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
block|}
end_class

end_unit

