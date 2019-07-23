begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|AsyncCallback
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
name|Suspendable
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
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|cxf
operator|.
name|interceptors
operator|.
name|UnitOfWorkCloserInterceptor
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
name|cxf
operator|.
name|util
operator|.
name|CxfUtils
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
name|support
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
name|ObjectHelper
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
name|continuations
operator|.
name|Continuation
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
name|continuations
operator|.
name|ContinuationProvider
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
name|endpoint
operator|.
name|Server
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|interceptor
operator|.
name|Fault
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
name|Exchange
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
name|FaultMode
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
name|phase
operator|.
name|Phase
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
name|invoker
operator|.
name|Invoker
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
name|BindingOperationInfo
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
name|ws
operator|.
name|addressing
operator|.
name|ContextUtils
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_comment
comment|/**  * A Consumer of exchanges for a service in CXF.  CxfConsumer acts a CXF  * service to receive requests, convert them, and forward them to Camel  * route for processing. It is also responsible for converting and sending  * back responses to CXF client.  */
end_comment

begin_class
DECL|class|CxfConsumer
specifier|public
class|class
name|CxfConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|Suspendable
block|{
DECL|field|server
specifier|private
name|Server
name|server
decl_stmt|;
DECL|field|cxfEndpoint
specifier|private
name|CxfEndpoint
name|cxfEndpoint
decl_stmt|;
DECL|method|CxfConsumer (final CxfEndpoint endpoint, Processor processor)
specifier|public
name|CxfConsumer
parameter_list|(
specifier|final
name|CxfEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|cxfEndpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|createServer ()
specifier|protected
name|Server
name|createServer
parameter_list|()
throws|throws
name|Exception
block|{
name|ServerFactoryBean
name|svrBean
init|=
name|cxfEndpoint
operator|.
name|createServerFactoryBean
argument_list|()
decl_stmt|;
name|svrBean
operator|.
name|setInvoker
argument_list|(
operator|new
name|CxfConsumerInvoker
argument_list|(
name|cxfEndpoint
argument_list|)
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|svrBean
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// Apply the server configurer if it is possible
if|if
condition|(
name|cxfEndpoint
operator|.
name|getCxfEndpointConfigurer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cxfEndpoint
operator|.
name|getCxfEndpointConfigurer
argument_list|()
operator|.
name|configureServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"serviceClass"
argument_list|,
name|cxfEndpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|cxfEndpoint
operator|.
name|getPublishedEndpointUrl
argument_list|()
argument_list|)
condition|)
block|{
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"publishedEndpointUrl"
argument_list|,
name|cxfEndpoint
operator|.
name|getPublishedEndpointUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|MessageObserver
name|originalOutFaultObserver
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutFaultObserver
argument_list|()
decl_stmt|;
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|setOutFaultObserver
argument_list|(
name|message
lambda|->
block|{
name|originalOutFaultObserver
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|CxfUtils
operator|.
name|closeCamelUnitOfWork
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// setup the UnitOfWorkCloserInterceptor for OneWayMessageProcessor
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|UnitOfWorkCloserInterceptor
argument_list|(
name|Phase
operator|.
name|POST_INVOKE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// close the UnitOfWork normally
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|UnitOfWorkCloserInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|server
return|;
block|}
DECL|method|getServer ()
specifier|public
name|Server
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
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
name|server
operator|==
literal|null
condition|)
block|{
name|server
operator|=
name|createServer
argument_list|()
expr_stmt|;
block|}
name|server
operator|.
name|start
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
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getReplyTo (Object o)
specifier|private
name|EndpointReferenceType
name|getReplyTo
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|EndpointReferenceType
operator|)
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getReplyTo"
argument_list|)
operator|.
name|invoke
argument_list|(
name|o
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
DECL|method|isAsyncInvocationSupported (Exchange cxfExchange)
specifier|protected
name|boolean
name|isAsyncInvocationSupported
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|)
block|{
name|Message
name|cxfMessage
init|=
name|cxfExchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|Object
name|addressingProperties
init|=
name|cxfMessage
operator|.
name|get
argument_list|(
name|CxfConstants
operator|.
name|WSA_HEADERS_INBOUND
argument_list|)
decl_stmt|;
if|if
condition|(
name|addressingProperties
operator|!=
literal|null
operator|&&
operator|!
name|ContextUtils
operator|.
name|isGenericAddress
argument_list|(
name|getReplyTo
argument_list|(
name|addressingProperties
argument_list|)
argument_list|)
condition|)
block|{
comment|//it's decoupled endpoint, so already switch thread and
comment|//use executors, which means underlying transport won't
comment|//be block, so we shouldn't rely on continuation in
comment|//this case, as the SuspendedInvocationException can't be
comment|//caught by underlying transport. So we should use the SyncInvocation this time
return|return
literal|false
return|;
block|}
comment|// we assume it should support AsyncInvocation out of box
return|return
literal|true
return|;
block|}
DECL|class|CxfConsumerInvoker
specifier|private
class|class
name|CxfConsumerInvoker
implements|implements
name|Invoker
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|CxfEndpoint
name|endpoint
decl_stmt|;
DECL|method|CxfConsumerInvoker (CxfEndpoint endpoint)
name|CxfConsumerInvoker
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
comment|// we receive a CXF request when this method is called
DECL|method|invoke (Exchange cxfExchange, Object o)
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Received CXF Request: {}"
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
name|Continuation
name|continuation
decl_stmt|;
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSynchronous
argument_list|()
operator|&&
name|isAsyncInvocationSupported
argument_list|(
name|cxfExchange
argument_list|)
operator|&&
operator|(
name|continuation
operator|=
name|getContinuation
argument_list|(
name|cxfExchange
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Calling the Camel async processors."
argument_list|)
expr_stmt|;
return|return
name|asyncInvoke
argument_list|(
name|cxfExchange
argument_list|,
name|continuation
argument_list|)
return|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Calling the Camel sync processors."
argument_list|)
expr_stmt|;
return|return
name|syncInvoke
argument_list|(
name|cxfExchange
argument_list|)
return|;
block|}
block|}
comment|// NOTE this code cannot work with CXF 2.2.x and JMSContinuation
comment|// as it doesn't break out the interceptor chain when we call it
DECL|method|asyncInvoke (Exchange cxfExchange, final Continuation continuation)
specifier|private
name|Object
name|asyncInvoke
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
specifier|final
name|Continuation
name|continuation
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"asyncInvoke continuation: {}"
argument_list|,
name|continuation
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|continuation
init|)
block|{
if|if
condition|(
name|continuation
operator|.
name|isNew
argument_list|()
condition|)
block|{
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
init|=
name|prepareCamelExchange
argument_list|(
name|cxfExchange
argument_list|)
decl_stmt|;
comment|// Now we don't set up the timeout value
name|log
operator|.
name|trace
argument_list|(
literal|"Suspending continuation of exchangeId: {}"
argument_list|,
name|camelExchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// The continuation could be called before the suspend is called
name|continuation
operator|.
name|suspend
argument_list|(
name|cxfEndpoint
operator|.
name|getContinuationTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|continuation
operator|.
name|setObject
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
comment|// use the asynchronous API to process the exchange
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|camelExchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// make sure the continuation resume will not be called before the suspend method in other thread
synchronized|synchronized
init|(
name|continuation
init|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Resuming continuation of exchangeId: {}"
argument_list|,
name|camelExchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// resume processing after both, sync and async callbacks
name|continuation
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|continuation
operator|.
name|isTimeout
argument_list|()
operator|&&
name|continuation
operator|.
name|isResumed
argument_list|()
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|)
name|continuation
operator|.
name|getObject
argument_list|()
decl_stmt|;
try|try
block|{
name|setResponseBack
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|CxfConsumer
operator|.
name|this
operator|.
name|doneUoW
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|continuation
operator|.
name|isTimeout
argument_list|()
operator|||
operator|(
operator|!
name|continuation
operator|.
name|isResumed
argument_list|()
operator|&&
operator|!
name|continuation
operator|.
name|isPending
argument_list|()
operator|)
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|)
name|continuation
operator|.
name|getObject
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|continuation
operator|.
name|isPending
argument_list|()
condition|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
operator|new
name|ExchangeTimedOutException
argument_list|(
name|camelExchange
argument_list|,
name|cxfEndpoint
operator|.
name|getContinuationTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setResponseBack
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|CxfConsumer
operator|.
name|this
operator|.
name|doneUoW
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getContinuation (Exchange cxfExchange)
specifier|private
name|Continuation
name|getContinuation
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|)
block|{
name|ContinuationProvider
name|provider
init|=
operator|(
name|ContinuationProvider
operator|)
name|cxfExchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Continuation
name|continuation
init|=
name|provider
operator|==
literal|null
condition|?
literal|null
else|:
name|provider
operator|.
name|getContinuation
argument_list|()
decl_stmt|;
comment|// Make sure we don't return the JMSContinuation, as it doesn't support the Continuation we wants
comment|// Don't want to introduce the dependency of cxf-rt-transprot-jms here
if|if
condition|(
name|continuation
operator|!=
literal|null
operator|&&
name|continuation
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.cxf.transport.jms.continuations.JMSContinuation"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|continuation
return|;
block|}
block|}
DECL|method|syncInvoke (Exchange cxfExchange)
specifier|private
name|Object
name|syncInvoke
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
init|=
name|prepareCamelExchange
argument_list|(
name|cxfExchange
argument_list|)
decl_stmt|;
try|try
block|{
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing +++ START +++"
argument_list|)
expr_stmt|;
comment|// send Camel exchange to the target processor
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|camelExchange
argument_list|)
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
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Processing +++ END +++"
argument_list|)
expr_stmt|;
name|setResponseBack
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|doneUoW
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
comment|// response should have been set in outMessage's content
return|return
literal|null
return|;
block|}
DECL|method|prepareCamelExchange (Exchange cxfExchange)
specifier|private
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|prepareCamelExchange
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|)
block|{
comment|// get CXF binding
name|CxfEndpoint
name|endpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
name|CxfBinding
name|binding
init|=
name|endpoint
operator|.
name|getCxfBinding
argument_list|()
decl_stmt|;
comment|// create a Camel exchange, the default MEP is InOut
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|//needs access in MessageObserver/Interceptor to close the UnitOfWork
name|cxfExchange
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|.
name|class
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
name|DataFormat
name|dataFormat
init|=
name|endpoint
operator|.
name|getDataFormat
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|cxfExchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
comment|// make sure the "boi" is remained as wrapped in PAYLOAD mode
if|if
condition|(
name|boi
operator|!=
literal|null
operator|&&
name|dataFormat
operator|==
name|DataFormat
operator|.
name|PAYLOAD
operator|&&
name|boi
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|boi
operator|=
name|boi
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
name|cxfExchange
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|boi
operator|!=
literal|null
condition|)
block|{
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|BindingOperationInfo
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Set exchange property: BindingOperationInfo: {}"
argument_list|,
name|boi
argument_list|)
expr_stmt|;
comment|// set the message exchange patter with the boi
if|if
condition|(
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|camelExchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|cxfEndpoint
operator|.
name|getExchangePattern
argument_list|()
operator|.
name|equals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
condition|)
block|{
name|camelExchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
block|}
comment|// set data format mode in Camel exchange
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|DATA_FORMAT_PROPERTY
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Set Exchange property: {}={}"
argument_list|,
name|DataFormat
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|Message
operator|.
name|MTOM_ENABLED
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|endpoint
operator|.
name|isMtomEnabled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getMergeProtocolHeaders
argument_list|()
condition|)
block|{
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_PROTOCOL_HEADERS_MERGED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|// bind the CXF request into a Camel exchange
name|binding
operator|.
name|populateExchangeFromCxfRequest
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
comment|// extract the javax.xml.ws header
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|binding
operator|.
name|extractJaxWsContext
argument_list|(
name|cxfExchange
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// put the context into camelExchange
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|JAXWS_CONTEXT
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// we want to handle the UoW
try|try
block|{
name|CxfConsumer
operator|.
name|this
operator|.
name|createUoW
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error processing request"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|camelExchange
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|setResponseBack (Exchange cxfExchange, org.apache.camel.Exchange camelExchange)
specifier|private
name|void
name|setResponseBack
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|CxfEndpoint
name|endpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
name|CxfBinding
name|binding
init|=
name|endpoint
operator|.
name|getCxfBinding
argument_list|()
decl_stmt|;
name|checkFailure
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
name|binding
operator|.
name|populateCxfResponseFromExchange
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
comment|// check failure again as fault could be discovered by converter
name|checkFailure
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
comment|// copy the headers javax.xml.ws header back
name|binding
operator|.
name|copyJaxWsContext
argument_list|(
name|cxfExchange
argument_list|,
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|camelExchange
operator|.
name|getProperty
argument_list|(
name|CxfConstants
operator|.
name|JAXWS_CONTEXT
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|checkFailure (org.apache.camel.Exchange camelExchange, Exchange cxfExchange)
specifier|private
name|void
name|checkFailure
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|,
name|Exchange
name|cxfExchange
parameter_list|)
throws|throws
name|Fault
block|{
name|Throwable
name|t
init|=
name|camelExchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
comment|// SOAP faults can be stored as exceptions as message body (to be backwards compatible)
name|Object
name|body
init|=
name|camelExchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|Throwable
condition|)
block|{
name|t
operator|=
operator|(
name|Throwable
operator|)
name|body
expr_stmt|;
block|}
block|}
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|cxfExchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|UNCHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|instanceof
name|Fault
condition|)
block|{
name|cxfExchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|CHECKED_APPLICATION_FAULT
argument_list|)
expr_stmt|;
throw|throw
operator|(
name|Fault
operator|)
name|t
throw|;
block|}
else|else
block|{
comment|// This is not a CXF Fault. Build the CXF Fault manually.
name|Fault
name|fault
init|=
operator|new
name|Fault
argument_list|(
name|t
argument_list|)
decl_stmt|;
if|if
condition|(
name|fault
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// The Fault has no Message. This is the case if it has
comment|// no message, for example was a NullPointerException.
name|fault
operator|.
name|setMessage
argument_list|(
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|WebFault
name|faultAnnotation
init|=
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|WebFault
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|faultInfo
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Method
name|method
init|=
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getFaultInfo"
argument_list|)
decl_stmt|;
name|faultInfo
operator|=
name|method
operator|.
name|invoke
argument_list|(
name|t
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing here
block|}
if|if
condition|(
name|faultAnnotation
operator|!=
literal|null
operator|&&
name|faultInfo
operator|==
literal|null
condition|)
block|{
comment|// t has a JAX-WS WebFault annotation, which describes
comment|// in detail the Web Service Fault that should be thrown. Add the
comment|// detail.
name|Element
name|detail
init|=
name|fault
operator|.
name|getOrCreateDetail
argument_list|()
decl_stmt|;
name|Element
name|faultDetails
init|=
name|detail
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElementNS
argument_list|(
name|faultAnnotation
operator|.
name|targetNamespace
argument_list|()
argument_list|,
name|faultAnnotation
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|detail
operator|.
name|appendChild
argument_list|(
name|faultDetails
argument_list|)
expr_stmt|;
block|}
throw|throw
name|fault
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

