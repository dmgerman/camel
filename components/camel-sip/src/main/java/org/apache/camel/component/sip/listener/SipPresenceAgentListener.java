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
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|ClientTransaction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|Dialog
import|;
end_import

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
name|ServerTransaction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|SipException
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
name|SipProvider
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
name|javax
operator|.
name|sip
operator|.
name|address
operator|.
name|SipURI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|header
operator|.
name|EventHeader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|header
operator|.
name|SubscriptionStateHeader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|header
operator|.
name|ToHeader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|message
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|message
operator|.
name|Response
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
name|SipPresenceAgent
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
DECL|class|SipPresenceAgentListener
specifier|public
class|class
name|SipPresenceAgentListener
implements|implements
name|SipListener
implements|,
name|SipMessageCodes
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
name|SipPresenceAgentListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dialog
specifier|protected
name|Dialog
name|dialog
decl_stmt|;
DECL|field|notifyCount
specifier|protected
name|int
name|notifyCount
decl_stmt|;
DECL|field|sipPresenceAgent
specifier|private
name|SipPresenceAgent
name|sipPresenceAgent
decl_stmt|;
DECL|method|SipPresenceAgentListener (SipPresenceAgent sipPresenceAgent)
specifier|public
name|SipPresenceAgentListener
parameter_list|(
name|SipPresenceAgent
name|sipPresenceAgent
parameter_list|)
block|{
name|this
operator|.
name|sipPresenceAgent
operator|=
name|sipPresenceAgent
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
name|Request
name|request
init|=
name|requestEvent
operator|.
name|getRequest
argument_list|()
decl_stmt|;
name|ServerTransaction
name|serverTransactionId
init|=
name|requestEvent
operator|.
name|getServerTransaction
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Request: {}"
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Server Transaction Id: {}"
argument_list|,
name|serverTransactionId
argument_list|)
expr_stmt|;
if|if
condition|(
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|equals
argument_list|(
name|Request
operator|.
name|SUBSCRIBE
argument_list|)
condition|)
block|{
name|processSubscribe
argument_list|(
name|requestEvent
argument_list|,
name|serverTransactionId
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|equals
argument_list|(
name|Request
operator|.
name|PUBLISH
argument_list|)
condition|)
block|{
name|processPublish
argument_list|(
name|requestEvent
argument_list|,
name|serverTransactionId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received expected request with method: {}. No further processing done"
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendNotification (EventHeader eventHeader, boolean isInitial, Object body)
specifier|private
name|void
name|sendNotification
parameter_list|(
name|EventHeader
name|eventHeader
parameter_list|,
name|boolean
name|isInitial
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|SipException
throws|,
name|ParseException
block|{
comment|/*          * NOTIFY requests MUST contain a "Subscription-State" header with a          * value of "active", "pending", or "terminated". The "active" value          * indicates that the subscription has been accepted and has been          * authorized (in most cases; see section 5.2.). The "pending" value          * indicates that the subscription has been received, but that          * policy information is insufficient to accept or deny the          * subscription at this time. The "terminated" value indicates that          * the subscription is not active.          */
name|Request
name|notifyRequest
init|=
name|dialog
operator|.
name|createRequest
argument_list|(
literal|"NOTIFY"
argument_list|)
decl_stmt|;
comment|// Mark the contact header, to check that the remote contact is updated
operator|(
operator|(
name|SipURI
operator|)
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getContactHeader
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getURI
argument_list|()
operator|)
operator|.
name|setParameter
argument_list|(
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFromUser
argument_list|()
argument_list|,
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFromHost
argument_list|()
argument_list|)
expr_stmt|;
name|SubscriptionStateHeader
name|sstate
decl_stmt|;
if|if
condition|(
name|isInitial
condition|)
block|{
comment|// Initial state is pending, second time we assume terminated (Expires==0)
name|sstate
operator|=
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getHeaderFactory
argument_list|()
operator|.
name|createSubscriptionStateHeader
argument_list|(
name|isInitial
condition|?
name|SubscriptionStateHeader
operator|.
name|PENDING
else|:
name|SubscriptionStateHeader
operator|.
name|TERMINATED
argument_list|)
expr_stmt|;
comment|// Need a reason for terminated
if|if
condition|(
name|sstate
operator|.
name|getState
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"terminated"
argument_list|)
condition|)
block|{
name|sstate
operator|.
name|setReasonCode
argument_list|(
literal|"deactivated"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sstate
operator|=
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getHeaderFactory
argument_list|()
operator|.
name|createSubscriptionStateHeader
argument_list|(
name|SubscriptionStateHeader
operator|.
name|ACTIVE
argument_list|)
expr_stmt|;
block|}
name|notifyRequest
operator|.
name|addHeader
argument_list|(
name|sstate
argument_list|)
expr_stmt|;
name|notifyRequest
operator|.
name|setHeader
argument_list|(
name|eventHeader
argument_list|)
expr_stmt|;
name|notifyRequest
operator|.
name|setHeader
argument_list|(
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getContactHeader
argument_list|()
argument_list|)
expr_stmt|;
name|notifyRequest
operator|.
name|setContent
argument_list|(
name|body
argument_list|,
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getContentTypeHeader
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending the following NOTIFY request to Subscriber: {}"
argument_list|,
name|notifyRequest
argument_list|)
expr_stmt|;
name|ClientTransaction
name|clientTransactionId
init|=
name|sipPresenceAgent
operator|.
name|getProvider
argument_list|()
operator|.
name|getNewClientTransaction
argument_list|(
name|notifyRequest
argument_list|)
decl_stmt|;
name|dialog
operator|.
name|sendRequest
argument_list|(
name|clientTransactionId
argument_list|)
expr_stmt|;
block|}
DECL|method|processPublish (RequestEvent requestEvent, ServerTransaction serverTransactionId)
specifier|private
name|void
name|processPublish
parameter_list|(
name|RequestEvent
name|requestEvent
parameter_list|,
name|ServerTransaction
name|serverTransactionId
parameter_list|)
block|{
try|try
block|{
name|Request
name|request
init|=
name|requestEvent
operator|.
name|getRequest
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"SipPresenceAgentListener: Received a Publish request, sending OK"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"SipPresenceAgentListener request: {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|EventHeader
name|eventHeader
init|=
operator|(
name|EventHeader
operator|)
name|requestEvent
operator|.
name|getRequest
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EventHeader
operator|.
name|NAME
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageFactory
argument_list|()
operator|.
name|createResponse
argument_list|(
literal|202
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|sipPresenceAgent
operator|.
name|getProvider
argument_list|()
operator|.
name|sendResponse
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// Send notification to subscriber
name|sendNotification
argument_list|(
name|eventHeader
argument_list|,
literal|false
argument_list|,
name|request
operator|.
name|getContent
argument_list|()
argument_list|)
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
name|error
argument_list|(
literal|"Exception thrown during publish/notify processing in the Sip Presence Agent Listener"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processSubscribe (RequestEvent requestEvent, ServerTransaction serverTransaction)
specifier|public
name|void
name|processSubscribe
parameter_list|(
name|RequestEvent
name|requestEvent
parameter_list|,
name|ServerTransaction
name|serverTransaction
parameter_list|)
block|{
name|SipProvider
name|sipProvider
init|=
operator|(
name|SipProvider
operator|)
name|requestEvent
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|Request
name|request
init|=
name|requestEvent
operator|.
name|getRequest
argument_list|()
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"SipPresenceAgentListener: Received a Subscribe request, sending OK"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"SipPresenceAgentListener request: {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|EventHeader
name|eventHeader
init|=
operator|(
name|EventHeader
operator|)
name|request
operator|.
name|getHeader
argument_list|(
name|EventHeader
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventHeader
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot find event header.... dropping request."
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Always create a ServerTransaction, best as early as possible in the code
name|Response
name|response
init|=
literal|null
decl_stmt|;
name|ServerTransaction
name|st
init|=
name|requestEvent
operator|.
name|getServerTransaction
argument_list|()
decl_stmt|;
if|if
condition|(
name|st
operator|==
literal|null
condition|)
block|{
name|st
operator|=
name|sipProvider
operator|.
name|getNewServerTransaction
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
comment|// Check if it is an initial SUBSCRIBE or a refresh / unsubscribe
name|boolean
name|isInitial
init|=
name|requestEvent
operator|.
name|getDialog
argument_list|()
operator|==
literal|null
decl_stmt|;
if|if
condition|(
name|isInitial
condition|)
block|{
name|String
name|toTag
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|response
operator|=
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageFactory
argument_list|()
operator|.
name|createResponse
argument_list|(
literal|202
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|ToHeader
name|toHeader
init|=
operator|(
name|ToHeader
operator|)
name|response
operator|.
name|getHeader
argument_list|(
name|ToHeader
operator|.
name|NAME
argument_list|)
decl_stmt|;
name|toHeader
operator|.
name|setTag
argument_list|(
name|toTag
argument_list|)
expr_stmt|;
comment|// Application is supposed to set.
name|this
operator|.
name|dialog
operator|=
name|st
operator|.
name|getDialog
argument_list|()
expr_stmt|;
comment|// subscribe dialogs do not terminate on bye.
name|this
operator|.
name|dialog
operator|.
name|terminateOnBye
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageFactory
argument_list|()
operator|.
name|createResponse
argument_list|(
literal|200
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
comment|// Both 2xx response to SUBSCRIBE and NOTIFY need a Contact
name|response
operator|.
name|addHeader
argument_list|(
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getContactHeader
argument_list|()
argument_list|)
expr_stmt|;
comment|// Expires header is mandatory in 2xx responses to SUBSCRIBE
name|response
operator|.
name|addHeader
argument_list|(
name|sipPresenceAgent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getExpiresHeader
argument_list|()
argument_list|)
expr_stmt|;
name|st
operator|.
name|sendResponse
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"SipPresenceAgentListener: Sent OK Message"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"SipPresenceAgentListener response: {}"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|sendNotification
argument_list|(
name|eventHeader
argument_list|,
name|isInitial
argument_list|,
name|request
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Exception thrown during Notify processing in the SipPresenceAgentListener."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processResponse (ResponseEvent responseReceivedEvent)
specifier|public
specifier|synchronized
name|void
name|processResponse
parameter_list|(
name|ResponseEvent
name|responseReceivedEvent
parameter_list|)
block|{
name|Response
name|response
init|=
name|responseReceivedEvent
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|Integer
name|statusCode
init|=
name|response
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|SIP_MESSAGE_CODES
operator|.
name|containsKey
argument_list|(
name|statusCode
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|SIP_MESSAGE_CODES
operator|.
name|get
argument_list|(
name|statusCode
argument_list|)
operator|+
literal|" received from Subscriber"
argument_list|)
expr_stmt|;
block|}
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
literal|"TimeoutEvent received at Sip Subscription Listener"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processIOException (IOExceptionEvent exceptionEvent)
specifier|public
name|void
name|processIOException
parameter_list|(
name|IOExceptionEvent
name|exceptionEvent
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
literal|"IOExceptionEvent received at SipPresenceAgentListener"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processTransactionTerminated ( TransactionTerminatedEvent transactionTerminatedEvent)
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
literal|"TransactionTerminatedEvent received at SipPresenceAgentListener"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processDialogTerminated ( DialogTerminatedEvent dialogTerminatedEvent)
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
literal|"DialogTerminatedEvent received at SipPresenceAgentListener"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

