begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sip.listener
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
operator|.
name|listener
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|DialogTerminatedEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|IOExceptionEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|RequestEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|ResponseEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|SipListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|TransactionTerminatedEvent
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
name|SipPublisher
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

begin_class
DECL|class|SipPublishListener
specifier|public
class|class
name|SipPublishListener
implements|implements
name|SipListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SipPublishListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sipPublisher
specifier|private
name|SipPublisher
name|sipPublisher
decl_stmt|;
DECL|method|SipPublishListener (SipPublisher sipPublisher)
specifier|public
name|SipPublishListener
parameter_list|(
name|SipPublisher
name|sipPublisher
parameter_list|)
block|{
name|this
operator|.
name|setSipPublisher
argument_list|(
name|sipPublisher
argument_list|)
expr_stmt|;
block|}
DECL|method|processRequest (RequestEvent requestEvent)
specifier|public
name|void
name|processRequest
parameter_list|(
name|RequestEvent
name|requestEvent
parameter_list|)
block|{
comment|// The SipPublishListener associated with the SipPublisher
comment|// may not accept incoming requests
block|}
DECL|method|processResponse (ResponseEvent responseReceivedEvent)
specifier|public
name|void
name|processResponse
parameter_list|(
name|ResponseEvent
name|responseReceivedEvent
parameter_list|)
block|{
comment|// The SipPublishListener sends InOnly requests to the Presence Agent
comment|// and only receives ACKs from the Presence Agent to satisfy the
comment|// Sip handshakeand. Hence any responses are not further processed.
block|}
DECL|method|processTimeout (javax.sip.TimeoutEvent timeoutEvent)
specifier|public
name|void
name|processTimeout
parameter_list|(
name|javax
operator|.
name|sip
operator|.
name|TimeoutEvent
name|timeoutEvent
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"processTimeout received at Sip Publish Listener"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processDialogTerminated (DialogTerminatedEvent dialogTerminatedEvent)
specifier|public
name|void
name|processDialogTerminated
parameter_list|(
name|DialogTerminatedEvent
name|dialogTerminatedEvent
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"processDialogTerminated received at Sip Publish Listener"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processIOException (IOExceptionEvent ioExceptionEvent)
specifier|public
name|void
name|processIOException
parameter_list|(
name|IOExceptionEvent
name|ioExceptionEvent
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"IOExceptionEvent received at Sip Publish Listener"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processTransactionTerminated (TransactionTerminatedEvent transactionTerminatedEvent)
specifier|public
name|void
name|processTransactionTerminated
parameter_list|(
name|TransactionTerminatedEvent
name|transactionTerminatedEvent
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"processTransactionTerminated received at Sip Publish Listener"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setSipPublisher (SipPublisher sipPublisher)
specifier|public
name|void
name|setSipPublisher
parameter_list|(
name|SipPublisher
name|sipPublisher
parameter_list|)
block|{
name|this
operator|.
name|sipPublisher
operator|=
name|sipPublisher
expr_stmt|;
block|}
DECL|method|getSipPublisher ()
specifier|public
name|SipPublisher
name|getSipPublisher
parameter_list|()
block|{
return|return
name|sipPublisher
return|;
block|}
block|}
end_class

end_unit

