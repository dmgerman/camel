begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|internal
package|;
end_package

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
name|net
operator|.
name|Socket
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketTimeoutException
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
name|Route
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
name|mllp
operator|.
name|MllpInvalidMessageException
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
name|mllp
operator|.
name|MllpSocketException
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
name|mllp
operator|.
name|MllpTcpServerConsumer
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
name|MDCUnitOfWork
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|MDC
import|;
end_import

begin_comment
comment|/**  * Runnable to read the Socket  */
end_comment

begin_class
DECL|class|TcpSocketConsumerRunnable
specifier|public
class|class
name|TcpSocketConsumerRunnable
implements|implements
name|Runnable
block|{
DECL|field|clientSocket
specifier|final
name|Socket
name|clientSocket
decl_stmt|;
DECL|field|mllpBuffer
specifier|final
name|MllpSocketBuffer
name|mllpBuffer
decl_stmt|;
DECL|field|log
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|consumer
name|MllpTcpServerConsumer
name|consumer
decl_stmt|;
DECL|field|running
name|boolean
name|running
decl_stmt|;
DECL|field|localAddress
specifier|private
specifier|final
name|String
name|localAddress
decl_stmt|;
DECL|field|remoteAddress
specifier|private
specifier|final
name|String
name|remoteAddress
decl_stmt|;
DECL|field|combinedAddress
specifier|private
specifier|final
name|String
name|combinedAddress
decl_stmt|;
DECL|method|TcpSocketConsumerRunnable (MllpTcpServerConsumer consumer, Socket clientSocket, MllpSocketBuffer mllpBuffer)
specifier|public
name|TcpSocketConsumerRunnable
parameter_list|(
name|MllpTcpServerConsumer
name|consumer
parameter_list|,
name|Socket
name|clientSocket
parameter_list|,
name|MllpSocketBuffer
name|mllpBuffer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
comment|// this.setName(createThreadName(clientSocket));
name|this
operator|.
name|clientSocket
operator|=
name|clientSocket
expr_stmt|;
name|SocketAddress
name|localSocketAddress
init|=
name|clientSocket
operator|.
name|getLocalSocketAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|localSocketAddress
operator|!=
literal|null
condition|)
block|{
name|localAddress
operator|=
name|localSocketAddress
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|localAddress
operator|=
literal|null
expr_stmt|;
block|}
name|SocketAddress
name|remoteSocketAddress
init|=
name|clientSocket
operator|.
name|getRemoteSocketAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|remoteSocketAddress
operator|!=
literal|null
condition|)
block|{
name|remoteAddress
operator|=
name|remoteSocketAddress
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|remoteAddress
operator|=
literal|null
expr_stmt|;
block|}
name|combinedAddress
operator|=
name|MllpSocketBuffer
operator|.
name|formatAddressString
argument_list|(
name|remoteSocketAddress
argument_list|,
name|localSocketAddress
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|hasKeepAlive
argument_list|()
condition|)
block|{
name|this
operator|.
name|clientSocket
operator|.
name|setKeepAlive
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKeepAlive
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|hasTcpNoDelay
argument_list|()
condition|)
block|{
name|this
operator|.
name|clientSocket
operator|.
name|setTcpNoDelay
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTcpNoDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|hasReceiveBufferSize
argument_list|()
condition|)
block|{
name|this
operator|.
name|clientSocket
operator|.
name|setReceiveBufferSize
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getReceiveBufferSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|hasSendBufferSize
argument_list|()
condition|)
block|{
name|this
operator|.
name|clientSocket
operator|.
name|setSendBufferSize
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSendBufferSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|clientSocket
operator|.
name|setSoLinger
argument_list|(
literal|false
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// Initial Read Timeout
name|this
operator|.
name|clientSocket
operator|.
name|setSoTimeout
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|initializationException
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Failed to initialize "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|initializationException
argument_list|)
throw|;
block|}
if|if
condition|(
name|mllpBuffer
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|mllpBuffer
operator|=
operator|new
name|MllpSocketBuffer
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|mllpBuffer
operator|=
name|mllpBuffer
expr_stmt|;
block|}
block|}
comment|/**      * derive a thread name from the class name, the component URI and the connection information      *<p/>      * The String will in the format<class name>[endpoint key] - [local socket address] -> [remote socket address]      *      * @return the thread name      */
DECL|method|createThreadName (Socket socket)
name|String
name|createThreadName
parameter_list|(
name|Socket
name|socket
parameter_list|)
block|{
comment|// Get the URI without options
name|String
name|fullEndpointKey
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
name|String
name|endpointKey
decl_stmt|;
if|if
condition|(
name|fullEndpointKey
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|endpointKey
operator|=
name|fullEndpointKey
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fullEndpointKey
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpointKey
operator|=
name|fullEndpointKey
expr_stmt|;
block|}
comment|// Now put it all together
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s[%s] - %s"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|endpointKey
argument_list|,
name|combinedAddress
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|running
operator|=
literal|true
expr_stmt|;
name|String
name|originalThreadName
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setName
argument_list|(
name|createThreadName
argument_list|(
name|clientSocket
argument_list|)
argument_list|)
expr_stmt|;
name|MDC
operator|.
name|put
argument_list|(
name|MDCUnitOfWork
operator|.
name|MDC_CAMEL_CONTEXT_ID
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Route
name|route
init|=
name|consumer
operator|.
name|getRoute
argument_list|()
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
name|String
name|routeId
init|=
name|route
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|routeId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDCUnitOfWork
operator|.
name|MDC_ROUTE_ID
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Starting {} for {}"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|combinedAddress
argument_list|)
expr_stmt|;
try|try
block|{
name|byte
index|[]
name|hl7MessageBytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mllpBuffer
operator|.
name|hasCompleteEnvelope
argument_list|()
condition|)
block|{
comment|// If we got a complete message on the validation read, process it
name|hl7MessageBytes
operator|=
name|mllpBuffer
operator|.
name|toMllpPayload
argument_list|()
expr_stmt|;
name|mllpBuffer
operator|.
name|reset
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|processMessage
argument_list|(
name|hl7MessageBytes
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|running
operator|&&
literal|null
operator|!=
name|clientSocket
operator|&&
name|clientSocket
operator|.
name|isConnected
argument_list|()
operator|&&
operator|!
name|clientSocket
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Checking for data ...."
argument_list|)
expr_stmt|;
try|try
block|{
name|mllpBuffer
operator|.
name|readFrom
argument_list|(
name|clientSocket
argument_list|)
expr_stmt|;
if|if
condition|(
name|mllpBuffer
operator|.
name|hasCompleteEnvelope
argument_list|()
condition|)
block|{
name|hl7MessageBytes
operator|=
name|mllpBuffer
operator|.
name|toMllpPayload
argument_list|()
expr_stmt|;
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
literal|"Received {} byte message {}"
argument_list|,
name|hl7MessageBytes
operator|.
name|length
argument_list|,
name|Hl7Util
operator|.
name|convertToPrintFriendlyString
argument_list|(
name|hl7MessageBytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mllpBuffer
operator|.
name|hasLeadingOutOfBandData
argument_list|()
condition|)
block|{
comment|// TODO:  Move the conversion utilities to the MllpSocketBuffer to avoid a byte[] copy
name|log
operator|.
name|warn
argument_list|(
literal|"Ignoring leading out-of-band data: {}"
argument_list|,
name|Hl7Util
operator|.
name|convertToPrintFriendlyString
argument_list|(
name|mllpBuffer
operator|.
name|getLeadingOutOfBandData
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mllpBuffer
operator|.
name|hasTrailingOutOfBandData
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Ignoring trailing out-of-band data: {}"
argument_list|,
name|Hl7Util
operator|.
name|convertToPrintFriendlyString
argument_list|(
name|mllpBuffer
operator|.
name|getTrailingOutOfBandData
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|mllpBuffer
operator|.
name|reset
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|processMessage
argument_list|(
name|hl7MessageBytes
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|mllpBuffer
operator|.
name|hasStartOfBlock
argument_list|()
condition|)
block|{
name|byte
index|[]
name|payload
init|=
name|mllpBuffer
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Ignoring {} byte un-enveloped payload {}"
argument_list|,
name|payload
operator|.
name|length
argument_list|,
name|Hl7Util
operator|.
name|convertToPrintFriendlyString
argument_list|(
name|payload
argument_list|)
argument_list|)
expr_stmt|;
name|mllpBuffer
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|mllpBuffer
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|byte
index|[]
name|payload
init|=
name|mllpBuffer
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Partial {} byte payload received {}"
argument_list|,
name|payload
operator|.
name|length
argument_list|,
name|Hl7Util
operator|.
name|convertToPrintFriendlyString
argument_list|(
name|payload
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SocketTimeoutException
name|timeoutEx
parameter_list|)
block|{
if|if
condition|(
name|mllpBuffer
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|hasIdleTimeout
argument_list|()
condition|)
block|{
name|long
name|currentTicks
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|long
name|lastReceivedMessageTicks
init|=
name|consumer
operator|.
name|getConsumerRunnables
argument_list|()
operator|.
name|get
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|long
name|idleTime
init|=
name|currentTicks
operator|-
name|lastReceivedMessageTicks
decl_stmt|;
if|if
condition|(
name|idleTime
operator|>=
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIdleTimeout
argument_list|()
condition|)
block|{
name|String
name|resetMessage
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Connection idle time %d exceeded idleTimeout %d"
argument_list|,
name|idleTime
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIdleTimeout
argument_list|()
argument_list|)
decl_stmt|;
name|mllpBuffer
operator|.
name|resetSocket
argument_list|(
name|clientSocket
argument_list|,
name|resetMessage
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"No data received - ignoring timeout"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mllpBuffer
operator|.
name|resetSocket
argument_list|(
name|clientSocket
argument_list|)
expr_stmt|;
operator|new
name|MllpInvalidMessageException
argument_list|(
literal|"Timeout receiving complete message payload"
argument_list|,
name|mllpBuffer
operator|.
name|toByteArrayAndReset
argument_list|()
argument_list|,
name|timeoutEx
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|handleMessageTimeout
argument_list|(
literal|"Timeout receiving complete message payload"
argument_list|,
name|mllpBuffer
operator|.
name|toByteArrayAndReset
argument_list|()
argument_list|,
name|timeoutEx
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MllpSocketException
name|mllpSocketEx
parameter_list|)
block|{
name|mllpBuffer
operator|.
name|resetSocket
argument_list|(
name|clientSocket
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|mllpBuffer
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|consumer
operator|.
name|handleMessageException
argument_list|(
literal|"Exception encountered reading payload"
argument_list|,
name|mllpBuffer
operator|.
name|toByteArrayAndReset
argument_list|()
argument_list|,
name|mllpSocketEx
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Ignoring exception encountered checking for data"
argument_list|,
name|mllpSocketEx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|unexpectedEx
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unexpected exception encountered receiving messages"
argument_list|,
name|unexpectedEx
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|consumer
operator|.
name|getConsumerRunnables
argument_list|()
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"{} for {} completed"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|combinedAddress
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setName
argument_list|(
name|originalThreadName
argument_list|)
expr_stmt|;
name|MDC
operator|.
name|remove
argument_list|(
name|MDCUnitOfWork
operator|.
name|MDC_ROUTE_ID
argument_list|)
expr_stmt|;
name|MDC
operator|.
name|remove
argument_list|(
name|MDCUnitOfWork
operator|.
name|MDC_CAMEL_CONTEXT_ID
argument_list|)
expr_stmt|;
name|mllpBuffer
operator|.
name|resetSocket
argument_list|(
name|clientSocket
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSocket ()
specifier|public
name|Socket
name|getSocket
parameter_list|()
block|{
return|return
name|clientSocket
return|;
block|}
DECL|method|getMllpBuffer ()
specifier|public
name|MllpSocketBuffer
name|getMllpBuffer
parameter_list|()
block|{
return|return
name|mllpBuffer
return|;
block|}
DECL|method|closeSocket ()
specifier|public
name|void
name|closeSocket
parameter_list|()
block|{
name|mllpBuffer
operator|.
name|closeSocket
argument_list|(
name|clientSocket
argument_list|)
expr_stmt|;
block|}
DECL|method|closeSocket (String logMessage)
specifier|public
name|void
name|closeSocket
parameter_list|(
name|String
name|logMessage
parameter_list|)
block|{
name|mllpBuffer
operator|.
name|closeSocket
argument_list|(
name|clientSocket
argument_list|,
name|logMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|resetSocket ()
specifier|public
name|void
name|resetSocket
parameter_list|()
block|{
name|mllpBuffer
operator|.
name|resetSocket
argument_list|(
name|clientSocket
argument_list|)
expr_stmt|;
block|}
DECL|method|resetSocket (String logMessage)
specifier|public
name|void
name|resetSocket
parameter_list|(
name|String
name|logMessage
parameter_list|)
block|{
name|mllpBuffer
operator|.
name|resetSocket
argument_list|(
name|clientSocket
argument_list|,
name|logMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|running
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|hasLocalAddress ()
specifier|public
name|boolean
name|hasLocalAddress
parameter_list|()
block|{
return|return
name|localAddress
operator|!=
literal|null
operator|&&
operator|!
name|localAddress
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|getLocalAddress ()
specifier|public
name|String
name|getLocalAddress
parameter_list|()
block|{
return|return
name|localAddress
return|;
block|}
DECL|method|hasRemoteAddress ()
specifier|public
name|boolean
name|hasRemoteAddress
parameter_list|()
block|{
return|return
name|remoteAddress
operator|!=
literal|null
operator|&&
operator|!
name|remoteAddress
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|getRemoteAddress ()
specifier|public
name|String
name|getRemoteAddress
parameter_list|()
block|{
return|return
name|remoteAddress
return|;
block|}
DECL|method|hasCombinedAddress ()
specifier|public
name|boolean
name|hasCombinedAddress
parameter_list|()
block|{
return|return
name|combinedAddress
operator|!=
literal|null
operator|&&
name|combinedAddress
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|getCombinedAddress ()
specifier|public
name|String
name|getCombinedAddress
parameter_list|()
block|{
return|return
name|combinedAddress
return|;
block|}
block|}
end_class

end_unit

