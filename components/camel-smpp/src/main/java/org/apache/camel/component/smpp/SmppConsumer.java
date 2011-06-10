begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
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
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|ReentrantLock
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
name|jsmpp
operator|.
name|DefaultPDUReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|DefaultPDUSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|SynchronizedPDUSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|AlertNotification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|BindType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|DataSm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|DeliverSm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|NumberingPlanIndicator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|TypeOfNumber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|extra
operator|.
name|ProcessRequestException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|extra
operator|.
name|SessionState
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|BindParameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|DataSmResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|MessageReceiverListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|SMPPSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|SessionStateListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|util
operator|.
name|DefaultComposer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|util
operator|.
name|MessageIDGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|util
operator|.
name|MessageId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|util
operator|.
name|RandomMessageIDGenerator
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

begin_comment
comment|/**  * An implementation of @{link Consumer} which use the SMPP protocol  *   * @version   */
end_comment

begin_class
DECL|class|SmppConsumer
specifier|public
class|class
name|SmppConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SmppConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
DECL|field|session
specifier|private
name|SMPPSession
name|session
decl_stmt|;
DECL|field|messageReceiverListener
specifier|private
name|MessageReceiverListener
name|messageReceiverListener
decl_stmt|;
DECL|field|sessionStateListener
specifier|private
name|SessionStateListener
name|sessionStateListener
decl_stmt|;
DECL|field|reconnectLock
specifier|private
specifier|final
name|ReentrantLock
name|reconnectLock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
comment|/**      * The constructor which gets a smpp endpoint, a smpp configuration and a      * processor      */
DECL|method|SmppConsumer (SmppEndpoint endpoint, SmppConfiguration config, Processor processor)
specifier|public
name|SmppConsumer
parameter_list|(
name|SmppEndpoint
name|endpoint
parameter_list|,
name|SmppConfiguration
name|config
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
name|configuration
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|sessionStateListener
operator|=
operator|new
name|SessionStateListener
argument_list|()
block|{
specifier|public
name|void
name|onStateChange
parameter_list|(
name|SessionState
name|newState
parameter_list|,
name|SessionState
name|oldState
parameter_list|,
name|Object
name|source
parameter_list|)
block|{
if|if
condition|(
name|newState
operator|.
name|equals
argument_list|(
name|SessionState
operator|.
name|CLOSED
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Lost connection to: "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
operator|+
literal|" - trying to reconnect..."
argument_list|)
expr_stmt|;
name|closeSession
argument_list|()
expr_stmt|;
name|reconnect
argument_list|(
name|configuration
operator|.
name|getInitialReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
name|this
operator|.
name|messageReceiverListener
operator|=
operator|new
name|MessageReceiverListener
argument_list|()
block|{
specifier|private
specifier|final
name|MessageIDGenerator
name|messageIDGenerator
init|=
operator|new
name|RandomMessageIDGenerator
argument_list|()
decl_stmt|;
specifier|public
name|void
name|onAcceptAlertNotification
parameter_list|(
name|AlertNotification
name|alertNotification
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received an alertNotification {}"
argument_list|,
name|alertNotification
argument_list|)
expr_stmt|;
try|try
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createOnAcceptAlertNotificationExchange
argument_list|(
name|alertNotification
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing the new smpp exchange..."
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processed the new smpp exchange"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onAcceptDeliverSm
parameter_list|(
name|DeliverSm
name|deliverSm
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received a deliverSm {}"
argument_list|,
name|deliverSm
argument_list|)
expr_stmt|;
try|try
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createOnAcceptDeliverSmExchange
argument_list|(
name|deliverSm
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"processing the new smpp exchange..."
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"processed the new smpp exchange"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|DataSmResult
name|onAcceptDataSm
parameter_list|(
name|DataSm
name|dataSm
parameter_list|,
name|Session
name|session
parameter_list|)
throws|throws
name|ProcessRequestException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received a dataSm {}"
argument_list|,
name|dataSm
argument_list|)
expr_stmt|;
name|MessageId
name|newMessageId
init|=
name|messageIDGenerator
operator|.
name|newMessageId
argument_list|()
decl_stmt|;
try|try
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createOnAcceptDataSm
argument_list|(
name|dataSm
argument_list|,
name|newMessageId
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"processing the new smpp exchange..."
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"processed the new smpp exchange"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ProcessRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|255
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|DataSmResult
argument_list|(
name|newMessageId
argument_list|,
name|dataSm
operator|.
name|getOptionalParametes
argument_list|()
argument_list|)
return|;
block|}
block|}
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connecting to: "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
operator|+
literal|"..."
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|session
operator|=
name|createSession
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Connected to: "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createSession ()
specifier|private
name|SMPPSession
name|createSession
parameter_list|()
throws|throws
name|IOException
block|{
name|SMPPSession
name|session
init|=
name|createSMPPSession
argument_list|()
decl_stmt|;
name|session
operator|.
name|setEnquireLinkTimer
argument_list|(
name|configuration
operator|.
name|getEnquireLinkTimer
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|setTransactionTimer
argument_list|(
name|configuration
operator|.
name|getTransactionTimer
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|addSessionStateListener
argument_list|(
name|sessionStateListener
argument_list|)
expr_stmt|;
name|session
operator|.
name|setMessageReceiverListener
argument_list|(
name|messageReceiverListener
argument_list|)
expr_stmt|;
name|session
operator|.
name|connectAndBind
argument_list|(
name|this
operator|.
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|this
operator|.
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|,
operator|new
name|BindParameter
argument_list|(
name|BindType
operator|.
name|BIND_RX
argument_list|,
name|this
operator|.
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|,
name|this
operator|.
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|,
name|this
operator|.
name|configuration
operator|.
name|getSystemType
argument_list|()
argument_list|,
name|TypeOfNumber
operator|.
name|UNKNOWN
argument_list|,
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
comment|/**      * Factory method to easily instantiate a mock SMPPSession      *       * @return the SMPPSession      */
DECL|method|createSMPPSession ()
name|SMPPSession
name|createSMPPSession
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|.
name|getUsingSSL
argument_list|()
condition|)
block|{
return|return
operator|new
name|SMPPSession
argument_list|(
operator|new
name|SynchronizedPDUSender
argument_list|(
operator|new
name|DefaultPDUSender
argument_list|(
operator|new
name|DefaultComposer
argument_list|()
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DefaultPDUReader
argument_list|()
argument_list|,
name|SmppSSLConnectionFactory
operator|.
name|getInstance
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|SMPPSession
argument_list|()
return|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Disconnecting from: "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
operator|+
literal|"..."
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|closeSession
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Disconnected from: "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|closeSession ()
specifier|private
name|void
name|closeSession
parameter_list|()
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
name|removeSessionStateListener
argument_list|(
name|this
operator|.
name|sessionStateListener
argument_list|)
expr_stmt|;
comment|// remove this hack after http://code.google.com/p/jsmpp/issues/detail?id=93 is fixed
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|session
operator|.
name|unbindAndClose
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not close session "
operator|+
name|session
argument_list|)
expr_stmt|;
block|}
name|session
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|reconnect (final long initialReconnectDelay)
specifier|private
name|void
name|reconnect
parameter_list|(
specifier|final
name|long
name|initialReconnectDelay
parameter_list|)
block|{
if|if
condition|(
name|reconnectLock
operator|.
name|tryLock
argument_list|()
condition|)
block|{
try|try
block|{
name|Runnable
name|r
init|=
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|boolean
name|reconnected
init|=
literal|false
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Schedule reconnect after "
operator|+
name|initialReconnectDelay
operator|+
literal|" millis"
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|initialReconnectDelay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{                         }
name|int
name|attempt
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
operator|(
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
operator|)
operator|&&
operator|(
name|session
operator|==
literal|null
operator|||
name|session
operator|.
name|getSessionState
argument_list|()
operator|.
name|equals
argument_list|(
name|SessionState
operator|.
name|CLOSED
argument_list|)
operator|)
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Trying to reconnect to "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
operator|+
literal|" - attempt #"
operator|+
operator|(
operator|++
name|attempt
operator|)
operator|+
literal|"..."
argument_list|)
expr_stmt|;
name|session
operator|=
name|createSession
argument_list|()
expr_stmt|;
name|reconnected
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Failed to reconnect to "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
argument_list|)
expr_stmt|;
name|closeSession
argument_list|()
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|configuration
operator|.
name|getReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ee
parameter_list|)
block|{                                 }
block|}
block|}
if|if
condition|(
name|reconnected
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Reconnected to "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
name|t
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// noop
block|}
finally|finally
block|{
name|reconnectLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SmppConsumer["
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SmppEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SmppEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Returns the smpp configuration      *       * @return the configuration      */
DECL|method|getConfiguration ()
specifier|public
name|SmppConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

