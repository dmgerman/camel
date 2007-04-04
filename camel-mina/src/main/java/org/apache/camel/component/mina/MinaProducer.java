begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|ConnectFuture
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
name|IoHandler
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
name|IoHandlerAdapter
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_comment
comment|/**  * A {@link Producer} implementation for MINA  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaProducer
specifier|public
class|class
name|MinaProducer
extends|extends
name|DefaultProducer
argument_list|<
name|MinaExchange
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|MinaProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|session
specifier|private
name|IoSession
name|session
decl_stmt|;
DECL|field|endpoint
specifier|private
name|MinaEndpoint
name|endpoint
decl_stmt|;
DECL|method|MinaProducer (MinaEndpoint endpoint)
specifier|public
name|MinaProducer
parameter_list|(
name|MinaEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|process (MinaExchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|MinaExchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started yet!"
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
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No payload for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|.
name|write
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
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
name|SocketAddress
name|address
init|=
name|endpoint
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|IoConnector
name|connector
init|=
name|endpoint
operator|.
name|getConnector
argument_list|()
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Creating connector to address: "
operator|+
name|address
operator|+
literal|" using connector: "
operator|+
name|connector
argument_list|)
expr_stmt|;
block|}
name|IoHandler
name|ioHandler
init|=
operator|new
name|IoHandlerAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|ioSession
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|messageReceived
argument_list|(
name|ioSession
argument_list|,
name|object
argument_list|)
expr_stmt|;
comment|/** TODO */
block|}
block|}
decl_stmt|;
name|ConnectFuture
name|future
init|=
name|connector
operator|.
name|connect
argument_list|(
name|address
argument_list|,
name|ioHandler
argument_list|,
name|endpoint
operator|.
name|getConfig
argument_list|()
argument_list|)
decl_stmt|;
name|future
operator|.
name|join
argument_list|()
expr_stmt|;
name|session
operator|=
name|future
operator|.
name|getSession
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|close
argument_list|()
operator|.
name|join
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

