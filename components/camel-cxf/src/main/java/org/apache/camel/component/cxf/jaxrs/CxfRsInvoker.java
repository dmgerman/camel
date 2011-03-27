begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
operator|.
name|jaxrs
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
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
name|RuntimeCamelException
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
name|jaxrs
operator|.
name|JAXRSInvoker
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
name|jaxrs
operator|.
name|model
operator|.
name|OperationResourceInfo
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
DECL|class|CxfRsInvoker
specifier|public
class|class
name|CxfRsInvoker
extends|extends
name|JAXRSInvoker
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
name|CxfRsInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SUSPENED
specifier|private
specifier|static
specifier|final
name|String
name|SUSPENED
init|=
literal|"org.apache.camel.component.cxf.jaxrs.suspend"
decl_stmt|;
DECL|field|SERVICE_OBJECT_SCOPE
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_OBJECT_SCOPE
init|=
literal|"org.apache.cxf.service.scope"
decl_stmt|;
DECL|field|REQUEST_SCOPE
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_SCOPE
init|=
literal|"request"
decl_stmt|;
DECL|field|cxfRsConsumer
specifier|private
name|CxfRsConsumer
name|cxfRsConsumer
decl_stmt|;
DECL|field|endpoint
specifier|private
name|CxfRsEndpoint
name|endpoint
decl_stmt|;
DECL|method|CxfRsInvoker (CxfRsEndpoint endpoint, CxfRsConsumer consumer)
specifier|public
name|CxfRsInvoker
parameter_list|(
name|CxfRsEndpoint
name|endpoint
parameter_list|,
name|CxfRsConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|cxfRsConsumer
operator|=
name|consumer
expr_stmt|;
block|}
DECL|method|performInvocation (Exchange cxfExchange, final Object serviceObject, Method method, Object[] paramArray)
specifier|protected
name|Object
name|performInvocation
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
throws|throws
name|Exception
block|{
name|paramArray
operator|=
name|insertExchange
argument_list|(
name|method
argument_list|,
name|paramArray
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|cxfExchange
operator|.
name|get
argument_list|(
name|OperationResourceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ori
operator|.
name|isSubResourceLocator
argument_list|()
condition|)
block|{
comment|// don't delegate the sub resource locator call to camel processor
return|return
name|method
operator|.
name|invoke
argument_list|(
name|serviceObject
argument_list|,
name|paramArray
argument_list|)
return|;
block|}
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
name|LOG
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
name|serviceObject
argument_list|,
name|method
argument_list|,
name|paramArray
argument_list|,
name|continuation
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
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
argument_list|,
name|serviceObject
argument_list|,
name|method
argument_list|,
name|paramArray
argument_list|)
return|;
block|}
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
return|return
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
return|;
block|}
DECL|method|asyncInvoke (Exchange cxfExchange, final Object serviceObject, Method method, Object[] paramArray, final Continuation continuation)
specifier|private
name|Object
name|asyncInvoke
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|,
specifier|final
name|Continuation
name|continuation
parameter_list|)
throws|throws
name|Exception
block|{
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
name|ExchangePattern
name|ep
init|=
name|ExchangePattern
operator|.
name|InOut
decl_stmt|;
if|if
condition|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
name|Void
operator|.
name|class
condition|)
block|{
name|ep
operator|=
name|ExchangePattern
operator|.
name|InOnly
expr_stmt|;
block|}
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
name|endpoint
operator|.
name|createExchange
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|CxfRsBinding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|populateExchangeFromCxfRsRequest
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|,
name|method
argument_list|,
name|paramArray
argument_list|)
expr_stmt|;
comment|// Now we don't set up the timeout value
name|LOG
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
comment|// TODO Support to set the timeout in case the Camel can't send the response back on time.
comment|// The continuation could be called before the suspend is called
name|continuation
operator|.
name|suspend
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|cxfExchange
operator|.
name|put
argument_list|(
name|SUSPENED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|cxfRsConsumer
operator|.
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
name|LOG
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
name|setObject
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
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
return|return
literal|null
return|;
block|}
if|if
condition|(
name|continuation
operator|.
name|isResumed
argument_list|()
condition|)
block|{
name|cxfExchange
operator|.
name|put
argument_list|(
name|SUSPENED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
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
return|return
name|returnResponse
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|syncInvoke (Exchange cxfExchange, final Object serviceObject, Method method, Object[] paramArray)
specifier|private
name|Object
name|syncInvoke
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
throws|throws
name|Exception
block|{
name|ExchangePattern
name|ep
init|=
name|ExchangePattern
operator|.
name|InOut
decl_stmt|;
if|if
condition|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
name|Void
operator|.
name|class
condition|)
block|{
name|ep
operator|=
name|ExchangePattern
operator|.
name|InOnly
expr_stmt|;
block|}
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
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|CxfRsBinding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|populateExchangeFromCxfRsRequest
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|,
name|method
argument_list|,
name|paramArray
argument_list|)
expr_stmt|;
try|try
block|{
name|cxfRsConsumer
operator|.
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
name|exception
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
return|return
name|returnResponse
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|)
return|;
block|}
DECL|method|returnResponse (Exchange cxfExchange, org.apache.camel.Exchange camelExchange)
specifier|private
name|Object
name|returnResponse
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
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Throwable
name|exception
init|=
name|camelExchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exception
operator|instanceof
name|RuntimeCamelException
condition|)
block|{
comment|// Unwrap the RuntimeCamelException
if|if
condition|(
name|exception
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exception
operator|=
name|exception
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|exception
operator|instanceof
name|WebApplicationException
condition|)
block|{
name|result
operator|=
operator|(
operator|(
name|WebApplicationException
operator|)
name|exception
operator|)
operator|.
name|getResponse
argument_list|()
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
else|else
block|{
throw|throw
operator|(
name|WebApplicationException
operator|)
name|exception
throw|;
block|}
block|}
else|else
block|{
comment|// Send the exception message back
name|WebApplicationException
name|webApplicationException
init|=
operator|new
name|WebApplicationException
argument_list|(
name|exception
argument_list|,
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|entity
argument_list|(
name|exception
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
name|webApplicationException
throw|;
block|}
block|}
return|return
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|populateCxfRsResponseFromExchange
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

