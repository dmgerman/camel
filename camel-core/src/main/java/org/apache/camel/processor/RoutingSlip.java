begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|CamelContext
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
name|Endpoint
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
name|ExchangePattern
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
name|Message
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
name|ProducerCallback
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
name|DefaultExchange
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
name|ProducerCache
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
name|ServiceSupport
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
name|model
operator|.
name|RoutingSlipDefinition
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
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
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
name|ServiceHelper
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Implements a<a href="http://camel.apache.org/routing-slip.html">Routing Slip</a>  * pattern where the list of actual endpoints to send a message exchange to are  * dependent on the value of a message header.  */
end_comment

begin_class
DECL|class|RoutingSlip
specifier|public
class|class
name|RoutingSlip
extends|extends
name|ServiceSupport
implements|implements
name|Processor
implements|,
name|Traceable
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
name|RoutingSlip
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producerCache
specifier|private
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|header
specifier|private
specifier|final
name|String
name|header
decl_stmt|;
DECL|field|uriDelimiter
specifier|private
specifier|final
name|String
name|uriDelimiter
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|RoutingSlip (CamelContext camelContext, String header)
specifier|public
name|RoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|header
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|header
argument_list|,
name|RoutingSlipDefinition
operator|.
name|DEFAULT_DELIMITER
argument_list|)
expr_stmt|;
block|}
DECL|method|RoutingSlip (CamelContext camelContext, String header, String uriDelimiter)
specifier|public
name|RoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|header
parameter_list|,
name|String
name|uriDelimiter
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|header
argument_list|,
literal|"header"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|uriDelimiter
argument_list|,
literal|"uriDelimiter"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
name|this
operator|.
name|uriDelimiter
operator|=
name|uriDelimiter
expr_stmt|;
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
literal|"RoutingSlip[header="
operator|+
name|header
operator|+
literal|" uriDelimiter="
operator|+
name|uriDelimiter
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"routingSlip["
operator|+
name|header
operator|+
literal|"]"
return|;
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
operator|!
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"RoutingSlip has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
index|[]
name|recipients
init|=
name|recipients
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Exchange
name|current
init|=
name|exchange
decl_stmt|;
for|for
control|(
name|String
name|nextRecipient
range|:
name|recipients
control|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|nextRecipient
argument_list|)
decl_stmt|;
name|Exchange
name|copy
init|=
operator|new
name|DefaultExchange
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|updateRoutingSlip
argument_list|(
name|current
argument_list|)
expr_stmt|;
name|copyOutToIn
argument_list|(
name|copy
argument_list|,
name|current
argument_list|)
expr_stmt|;
try|try
block|{
name|producerCache
operator|.
name|doInProducer
argument_list|(
name|endpoint
argument_list|,
name|copy
argument_list|,
literal|null
argument_list|,
operator|new
name|ProducerCallback
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|doInProducer
parameter_list|(
name|Producer
name|producer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|exchangePattern
parameter_list|)
throws|throws
name|Exception
block|{
comment|// set property which endpoint we send to
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// catch exception so we can decide if we want to continue or not
name|copy
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|current
operator|=
name|copy
expr_stmt|;
block|}
comment|// Decide whether to continue with the recipients or not; similar logic to the Pipeline
name|boolean
name|exceptionHandled
init|=
name|hasExceptionBeenHandledByErrorHandler
argument_list|(
name|current
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|.
name|isFailed
argument_list|()
operator|||
name|current
operator|.
name|isRollbackOnly
argument_list|()
operator|||
name|exceptionHandled
condition|)
block|{
comment|// The Exchange.ERRORHANDLED_HANDLED property is only set if satisfactory handling was done
comment|// by the error handler. It's still an exception, the exchange still failed.
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Message exchange has failed so breaking out of the routing slip: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
argument_list|)
expr_stmt|;
if|if
condition|(
name|current
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Marked as rollback only."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Exception: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|hasOut
argument_list|()
operator|&&
name|current
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Fault: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exceptionHandled
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Handled by the error handler."
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|current
argument_list|)
expr_stmt|;
block|}
DECL|method|hasExceptionBeenHandledByErrorHandler (Exchange nextExchange)
specifier|private
specifier|static
name|boolean
name|hasExceptionBeenHandledByErrorHandler
parameter_list|(
name|Exchange
name|nextExchange
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|nextExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|)
argument_list|)
return|;
block|}
DECL|method|resolveEndpoint (Exchange exchange, Object recipient)
specifier|protected
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|recipient
parameter_list|)
block|{
return|return
name|ExchangeHelper
operator|.
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|producerCache
operator|==
literal|null
condition|)
block|{
name|producerCache
operator|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
comment|// add it as a service so we can manage it
name|camelContext
operator|.
name|addService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
DECL|method|updateRoutingSlip (Exchange current)
specifier|private
name|void
name|updateRoutingSlip
parameter_list|(
name|Exchange
name|current
parameter_list|)
block|{
name|Message
name|message
init|=
name|getResultMessage
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|String
name|oldSlip
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|header
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldSlip
operator|!=
literal|null
condition|)
block|{
name|int
name|delimiterIndex
init|=
name|oldSlip
operator|.
name|indexOf
argument_list|(
name|uriDelimiter
argument_list|)
decl_stmt|;
name|String
name|newSlip
init|=
name|delimiterIndex
operator|>
literal|0
condition|?
name|oldSlip
operator|.
name|substring
argument_list|(
name|delimiterIndex
operator|+
literal|1
argument_list|)
else|:
literal|""
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|header
argument_list|,
name|newSlip
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the outbound message if available. Otherwise return the inbound      * message.      */
DECL|method|getResultMessage (Exchange exchange)
specifier|private
name|Message
name|getResultMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
return|return
name|exchange
operator|.
name|getOut
argument_list|()
return|;
block|}
else|else
block|{
comment|// if this endpoint had no out (like a mock endpoint) just take the in
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
comment|/**      * Return the list of recipients defined in the routing slip in the      * specified message.      */
DECL|method|recipients (Message message)
specifier|private
name|String
index|[]
name|recipients
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Object
name|headerValue
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|header
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|headerValue
argument_list|)
condition|)
block|{
return|return
name|headerValue
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
name|uriDelimiter
argument_list|)
return|;
block|}
return|return
operator|new
name|String
index|[]
block|{}
return|;
block|}
comment|/**      * Copy the outbound data in 'source' to the inbound data in 'result'.      */
DECL|method|copyOutToIn (Exchange result, Exchange source)
specifier|private
name|void
name|copyOutToIn
parameter_list|(
name|Exchange
name|result
parameter_list|,
name|Exchange
name|source
parameter_list|)
block|{
name|result
operator|.
name|setException
argument_list|(
name|source
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|source
operator|.
name|hasOut
argument_list|()
operator|&&
name|source
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|source
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|setIn
argument_list|(
name|getResultMessage
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|getProperties
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|result
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

