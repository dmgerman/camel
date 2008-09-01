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
name|CamelException
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
name|impl
operator|.
name|DefaultConsumer
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
name|ExchangeHelper
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

begin_comment
comment|/**  * A {@link org.apache.camel.Consumer Consumer} implementation for Apache MINA.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaConsumer
specifier|public
class|class
name|MinaConsumer
extends|extends
name|DefaultConsumer
argument_list|<
name|MinaExchange
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|MinaConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|MinaEndpoint
name|endpoint
decl_stmt|;
DECL|field|address
specifier|private
specifier|final
name|SocketAddress
name|address
decl_stmt|;
DECL|field|acceptor
specifier|private
specifier|final
name|IoAcceptor
name|acceptor
decl_stmt|;
DECL|field|sync
specifier|private
name|boolean
name|sync
decl_stmt|;
DECL|method|MinaConsumer (final MinaEndpoint endpoint, Processor processor)
specifier|public
name|MinaConsumer
parameter_list|(
specifier|final
name|MinaEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|address
operator|=
name|endpoint
operator|.
name|getAddress
argument_list|()
expr_stmt|;
name|this
operator|.
name|acceptor
operator|=
name|endpoint
operator|.
name|getAcceptor
argument_list|()
expr_stmt|;
name|this
operator|.
name|sync
operator|=
name|endpoint
operator|.
name|isSync
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Binding to server address: "
operator|+
name|address
operator|+
literal|" using acceptor: "
operator|+
name|acceptor
argument_list|)
expr_stmt|;
block|}
name|IoHandler
name|handler
init|=
operator|new
name|ReceiveHandler
argument_list|()
decl_stmt|;
name|acceptor
operator|.
name|bind
argument_list|(
name|address
argument_list|,
name|handler
argument_list|,
name|endpoint
operator|.
name|getAcceptorConfig
argument_list|()
argument_list|)
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
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Unbinding from server address: "
operator|+
name|address
operator|+
literal|" using acceptor: "
operator|+
name|acceptor
argument_list|)
expr_stmt|;
block|}
name|acceptor
operator|.
name|unbind
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Handles comsuming messages and replying if the exchange is out capable.      */
DECL|class|ReceiveHandler
specifier|private
specifier|final
class|class
name|ReceiveHandler
extends|extends
name|IoHandlerAdapter
block|{
annotation|@
name|Override
DECL|method|exceptionCaught (IoSession session, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Throwable
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
comment|// close invalid session
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Closing session as an exception was thrown from MINA"
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// must wrap and rethrow since cause can be of Throwable and we must only throw Exception
throw|throw
operator|new
name|CamelException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|messageReceived (IoSession session, Object object)
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|object
argument_list|)
expr_stmt|;
block|}
name|MinaExchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|session
argument_list|,
name|object
argument_list|)
decl_stmt|;
comment|//Set the exchange charset property for converting
if|if
condition|(
name|endpoint
operator|.
name|getCharsetName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|endpoint
operator|.
name|getCharsetName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// if sync then we should return a response
if|if
condition|(
name|sync
condition|)
block|{
name|Object
name|body
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|body
operator|=
name|MinaPayloadHelper
operator|.
name|getOut
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|MinaPayloadHelper
operator|.
name|getIn
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
comment|/*if (failed) {                     // can not write a response since the exchange is failed and we don't know in what state the                     // in/out messages are in so the session is closed                     LOG.warn("Can not write body since the exchange is failed, closing session: " + exchange);                     session.close();                     if (exchange.getException() != null) {                         throw new CamelException(exchange.getException());                     }                     if (exchange.getFault(false) != null) {                         if (exchange.getFault().getBody() instanceof Throwable) {                             System.out.println("throw the exception here");                             throw new CamelException((Throwable)exchange.getFault().getBody());                         }                     }                  } else*/
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
comment|// must close session if no data to write otherwise client will never receive a response
comment|// and wait forever (if not timing out)
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can not write body since its null, closing session: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// we got a response to write
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing body: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
name|MinaHelper
operator|.
name|writeBody
argument_list|(
name|session
argument_list|,
name|body
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

