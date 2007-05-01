begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|local
operator|.
name|LocalConduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|local
operator|.
name|LocalTransportFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_comment
comment|/**  * Sends messages from Camel into the CXF endpoint  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfProducer
specifier|public
class|class
name|CxfProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
name|CxfEndpoint
name|endpoint
decl_stmt|;
DECL|field|transportFactory
specifier|private
specifier|final
name|LocalTransportFactory
name|transportFactory
decl_stmt|;
DECL|field|destination
specifier|private
name|Destination
name|destination
decl_stmt|;
DECL|field|conduit
specifier|private
name|Conduit
name|conduit
decl_stmt|;
DECL|field|future
specifier|private
name|ResultFuture
name|future
init|=
operator|new
name|ResultFuture
argument_list|()
decl_stmt|;
DECL|method|CxfProducer (CxfEndpoint endpoint, LocalTransportFactory transportFactory)
specifier|public
name|CxfProducer
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|,
name|LocalTransportFactory
name|transportFactory
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
name|this
operator|.
name|transportFactory
operator|=
name|transportFactory
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|CxfExchange
name|cxfExchange
init|=
name|endpoint
operator|.
name|toExchangeType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|process
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (CxfExchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|CxfBinding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|MessageImpl
name|m
init|=
name|binding
operator|.
name|createCxfMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|ExchangeImpl
name|e
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|e
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|LocalConduit
operator|.
name|DIRECT_DISPATCH
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|m
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|conduit
init|)
block|{
name|conduit
operator|.
name|prepare
argument_list|(
name|m
argument_list|)
expr_stmt|;
comment|// now lets wait for the response
if|if
condition|(
name|endpoint
operator|.
name|isInOut
argument_list|()
condition|)
block|{
name|Message
name|response
init|=
name|future
operator|.
name|getResponse
argument_list|()
decl_stmt|;
comment|// TODO - why do we need to ignore the returned message and get the out message from the exchange!
name|response
operator|=
name|e
operator|.
name|getOutMessage
argument_list|()
expr_stmt|;
name|binding
operator|.
name|storeCxfResponse
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|EndpointInfo
name|endpointInfo
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|destination
operator|=
name|transportFactory
operator|.
name|getDestination
argument_list|(
name|endpointInfo
argument_list|)
expr_stmt|;
comment|// Set up a listener for the response
name|conduit
operator|=
name|transportFactory
operator|.
name|getConduit
argument_list|(
name|endpointInfo
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|setMessageObserver
argument_list|(
name|future
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|conduit
operator|!=
literal|null
condition|)
block|{
name|conduit
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|ResultFuture
specifier|protected
class|class
name|ResultFuture
implements|implements
name|MessageObserver
block|{
DECL|field|response
name|Message
name|response
decl_stmt|;
DECL|field|latch
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|method|getResponse ()
specifier|public
name|Message
name|getResponse
parameter_list|()
block|{
while|while
condition|(
name|response
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|response
return|;
block|}
DECL|method|onMessage (Message message)
specifier|public
specifier|synchronized
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|message
operator|.
name|remove
argument_list|(
name|LocalConduit
operator|.
name|DIRECT_DISPATCH
argument_list|)
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|message
expr_stmt|;
block|}
finally|finally
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

