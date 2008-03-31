begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FutureTask
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
name|TimeUnit
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|ExchangeTimedOutException
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
name|RuntimeExchangeException
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
name|jms
operator|.
name|requestor
operator|.
name|Requestor
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
name|camel
operator|.
name|util
operator|.
name|UuidGenerator
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
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsOperations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|MessageCreator
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsProducer
specifier|public
class|class
name|JmsProducer
extends|extends
name|DefaultProducer
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
name|JmsProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|JmsEndpoint
name|endpoint
decl_stmt|;
DECL|field|inOnlyTemplate
specifier|private
name|JmsOperations
name|inOnlyTemplate
decl_stmt|;
DECL|field|inOutTemplate
specifier|private
name|JmsOperations
name|inOutTemplate
decl_stmt|;
DECL|field|uuidGenerator
specifier|private
name|UuidGenerator
name|uuidGenerator
decl_stmt|;
DECL|method|JmsProducer (JmsEndpoint endpoint)
specifier|public
name|JmsProducer
parameter_list|(
name|JmsEndpoint
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
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
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
comment|// create a temporary queue and consumer for responses...
comment|// note due to JMS transaction semantics we cannot use a single transaction
comment|// for sending the request and receiving the response
name|Requestor
name|requestor
decl_stmt|;
try|try
block|{
name|requestor
operator|=
name|endpoint
operator|.
name|getRequestor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
name|e
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
specifier|final
name|Destination
name|replyTo
init|=
name|requestor
operator|.
name|getReplyTo
argument_list|()
decl_stmt|;
name|String
name|correlationId
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|correlationId
operator|==
literal|null
condition|)
block|{
name|correlationId
operator|=
name|getUuidGenerator
argument_list|()
operator|.
name|generateId
argument_list|()
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|correlationId
argument_list|)
expr_stmt|;
block|}
comment|// lets register the future object before we try send just in case
name|long
name|requestTimeout
init|=
name|endpoint
operator|.
name|getRequestTimeout
argument_list|()
decl_stmt|;
name|FutureTask
name|future
init|=
name|requestor
operator|.
name|getReceiveFuture
argument_list|(
name|correlationId
argument_list|,
name|requestTimeout
argument_list|)
decl_stmt|;
name|getInOutTemplate
argument_list|()
operator|.
name|send
argument_list|(
name|endpoint
operator|.
name|getDestination
argument_list|()
argument_list|,
operator|new
name|MessageCreator
argument_list|()
block|{
specifier|public
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
name|Message
name|message
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|makeJmsMessage
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|session
argument_list|)
decl_stmt|;
name|message
operator|.
name|setJMSReplyTo
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
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
name|endpoint
operator|+
literal|" sending JMS message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|message
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// lets wait and return the response
try|try
block|{
name|Message
name|message
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|requestTimeout
operator|<
literal|0
condition|)
block|{
name|message
operator|=
operator|(
name|Message
operator|)
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
operator|(
name|Message
operator|)
name|future
operator|.
name|get
argument_list|(
name|requestTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
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
literal|"Future interupted: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
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
literal|"Future timed out: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setOut
argument_list|(
operator|new
name|JmsMessage
argument_list|(
name|message
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// lets set a timed out exception
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|requestTimeout
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|getInOnlyTemplate
argument_list|()
operator|.
name|send
argument_list|(
name|endpoint
operator|.
name|getDestination
argument_list|()
argument_list|,
operator|new
name|MessageCreator
argument_list|()
block|{
specifier|public
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
name|Message
name|message
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|makeJmsMessage
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|session
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
name|endpoint
operator|+
literal|" sending JMS message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|message
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Preserved for backwards compatibility.      *      * @deprecated      * @see #getInOnlyTemplate()      */
DECL|method|getTemplate ()
specifier|public
name|JmsOperations
name|getTemplate
parameter_list|()
block|{
return|return
name|getInOnlyTemplate
argument_list|()
return|;
block|}
DECL|method|getInOnlyTemplate ()
specifier|public
name|JmsOperations
name|getInOnlyTemplate
parameter_list|()
block|{
if|if
condition|(
name|inOnlyTemplate
operator|==
literal|null
condition|)
block|{
name|inOnlyTemplate
operator|=
name|endpoint
operator|.
name|createInOnlyTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|inOnlyTemplate
return|;
block|}
DECL|method|setInOnlyTemplate (JmsOperations inOnlyTemplate)
specifier|public
name|void
name|setInOnlyTemplate
parameter_list|(
name|JmsOperations
name|inOnlyTemplate
parameter_list|)
block|{
name|this
operator|.
name|inOnlyTemplate
operator|=
name|inOnlyTemplate
expr_stmt|;
block|}
DECL|method|getInOutTemplate ()
specifier|public
name|JmsOperations
name|getInOutTemplate
parameter_list|()
block|{
if|if
condition|(
name|inOutTemplate
operator|==
literal|null
condition|)
block|{
name|inOutTemplate
operator|=
name|endpoint
operator|.
name|createInOutTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|inOutTemplate
return|;
block|}
DECL|method|setInOutTemplate (JmsOperations inOutTemplate)
specifier|public
name|void
name|setInOutTemplate
parameter_list|(
name|JmsOperations
name|inOutTemplate
parameter_list|)
block|{
name|this
operator|.
name|inOutTemplate
operator|=
name|inOutTemplate
expr_stmt|;
block|}
DECL|method|getUuidGenerator ()
specifier|public
name|UuidGenerator
name|getUuidGenerator
parameter_list|()
block|{
if|if
condition|(
name|uuidGenerator
operator|==
literal|null
condition|)
block|{
name|uuidGenerator
operator|=
operator|new
name|UuidGenerator
argument_list|()
expr_stmt|;
block|}
return|return
name|uuidGenerator
return|;
block|}
DECL|method|setUuidGenerator (UuidGenerator uuidGenerator)
specifier|public
name|void
name|setUuidGenerator
parameter_list|(
name|UuidGenerator
name|uuidGenerator
parameter_list|)
block|{
name|this
operator|.
name|uuidGenerator
operator|=
name|uuidGenerator
expr_stmt|;
block|}
block|}
end_class

end_unit

