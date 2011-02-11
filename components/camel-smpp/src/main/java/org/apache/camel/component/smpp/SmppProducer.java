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
name|impl
operator|.
name|DefaultProducer
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
name|Alphabet
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
name|ESMClass
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
name|GeneralDataCoding
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
name|MessageClass
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
name|RegisteredDelivery
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
name|SubmitSm
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
comment|/**  * An implementation of @{link Producer} which use the SMPP protocol  *   * @version $Revision$  * @author muellerc  */
end_comment

begin_class
DECL|class|SmppProducer
specifier|public
class|class
name|SmppProducer
extends|extends
name|DefaultProducer
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
name|SmppProducer
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
DECL|method|SmppProducer (SmppEndpoint endpoint, SmppConfiguration config)
specifier|public
name|SmppProducer
parameter_list|(
name|SmppEndpoint
name|endpoint
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
literal|"Loosing connection to: "
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
argument_list|(
name|session
argument_list|)
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
name|this
operator|.
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
name|this
operator|.
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
name|BIND_TX
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
name|valueOf
argument_list|(
name|configuration
operator|.
name|getTypeOfNumber
argument_list|()
argument_list|)
argument_list|,
name|NumberingPlanIndicator
operator|.
name|valueOf
argument_list|(
name|configuration
operator|.
name|getNumberingPlanIndicator
argument_list|()
argument_list|)
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
literal|"Sending a short message for exchange id '"
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|"'..."
argument_list|)
expr_stmt|;
block|}
comment|// only possible by trying to reconnect
if|if
condition|(
name|this
operator|.
name|session
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Lost connection to "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConnectionString
argument_list|()
operator|+
literal|" and yet not reconnected"
argument_list|)
throw|;
block|}
name|SubmitSm
name|submitSm
init|=
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|createSubmitSm
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|messageId
init|=
name|session
operator|.
name|submitShortMessage
argument_list|(
name|submitSm
operator|.
name|getServiceType
argument_list|()
argument_list|,
name|TypeOfNumber
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
argument_list|,
name|NumberingPlanIndicator
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
argument_list|,
name|submitSm
operator|.
name|getSourceAddr
argument_list|()
argument_list|,
name|TypeOfNumber
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
argument_list|,
name|NumberingPlanIndicator
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
argument_list|,
name|submitSm
operator|.
name|getDestAddress
argument_list|()
argument_list|,
operator|new
name|ESMClass
argument_list|()
argument_list|,
name|submitSm
operator|.
name|getProtocolId
argument_list|()
argument_list|,
name|submitSm
operator|.
name|getPriorityFlag
argument_list|()
argument_list|,
name|submitSm
operator|.
name|getScheduleDeliveryTime
argument_list|()
argument_list|,
name|submitSm
operator|.
name|getValidityPeriod
argument_list|()
argument_list|,
operator|new
name|RegisteredDelivery
argument_list|(
name|submitSm
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
argument_list|,
name|submitSm
operator|.
name|getReplaceIfPresent
argument_list|()
argument_list|,
operator|new
name|GeneralDataCoding
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
name|MessageClass
operator|.
name|CLASS1
argument_list|,
name|Alphabet
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getDataCoding
argument_list|()
argument_list|)
argument_list|)
argument_list|,
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|submitSm
operator|.
name|getShortMessage
argument_list|()
argument_list|)
decl_stmt|;
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
literal|"Sent a short message for exchange id '"
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|"' and received message id '"
operator|+
name|messageId
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
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
literal|"Exchange is out capable, setting headers on out exchange..."
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppBinding
operator|.
name|ID
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
else|else
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
literal|"Exchange is not out capable, setting headers on in exchange..."
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppBinding
operator|.
name|ID
argument_list|,
name|messageId
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
argument_list|(
name|session
argument_list|)
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
DECL|method|closeSession (SMPPSession session)
specifier|private
name|void
name|closeSession
parameter_list|(
name|SMPPSession
name|session
parameter_list|)
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
name|session
operator|.
name|unbindAndClose
argument_list|()
expr_stmt|;
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
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
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
argument_list|(
name|session
argument_list|)
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
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * Returns the smppConfiguration for this producer      *       * @return the configuration      */
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SmppProducer["
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
block|}
end_class

end_unit

