begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

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
name|lang
operator|.
name|reflect
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|ExecutionException
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
name|ExecutorService
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
name|Future
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
name|FutureTask
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
name|Body
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
name|CamelExchangeException
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
name|ExchangeProperty
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
name|Header
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
name|Headers
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
name|InvalidPayloadException
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
name|support
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
name|StringHelper
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
DECL|class|AbstractCamelInvocationHandler
specifier|public
specifier|abstract
class|class
name|AbstractCamelInvocationHandler
implements|implements
name|InvocationHandler
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
name|CamelInvocationHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|EXCLUDED_METHODS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|EXCLUDED_METHODS
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|executorService
specifier|private
specifier|static
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|producer
specifier|protected
specifier|final
name|Producer
name|producer
decl_stmt|;
static|static
block|{
comment|// exclude all java.lang.Object methods as we dont want to invoke them
name|EXCLUDED_METHODS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Object
operator|.
name|class
operator|.
name|getMethods
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractCamelInvocationHandler (Endpoint endpoint, Producer producer)
specifier|public
name|AbstractCamelInvocationHandler
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
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
name|producer
operator|=
name|producer
expr_stmt|;
block|}
DECL|method|getBody (Exchange exchange, Class<?> type)
specifier|private
specifier|static
name|Object
name|getBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
comment|// get the body from the Exchange from either OUT or IN
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|type
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|type
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|invoke (final Object proxy, final Method method, final Object[] args)
specifier|public
specifier|final
name|Object
name|invoke
parameter_list|(
specifier|final
name|Object
name|proxy
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|isValidMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
name|doInvokeProxy
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
return|;
block|}
else|else
block|{
comment|// invalid method then invoke methods on this instead
if|if
condition|(
literal|"toString"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|this
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
literal|"hashCode"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|this
operator|.
name|hashCode
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
literal|"equals"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
DECL|method|doInvokeProxy (Object proxy, Method method, Object[] args)
specifier|public
specifier|abstract
name|Object
name|doInvokeProxy
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|invokeProxy (final Method method, final ExchangePattern pattern, Object[] args, boolean binding)
specifier|protected
name|Object
name|invokeProxy
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
index|[]
name|args
parameter_list|,
name|boolean
name|binding
parameter_list|)
throws|throws
name|Throwable
block|{
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
comment|//Need to check if there are mutiple arguments and the parameters have no annotations for binding,
comment|//then use the original bean invocation.
name|boolean
name|canUseBinding
init|=
name|method
operator|.
name|getParameterCount
argument_list|()
operator|==
literal|1
decl_stmt|;
if|if
condition|(
operator|!
name|canUseBinding
condition|)
block|{
for|for
control|(
name|Parameter
name|parameter
range|:
name|method
operator|.
name|getParameters
argument_list|()
control|)
block|{
if|if
condition|(
name|parameter
operator|.
name|isAnnotationPresent
argument_list|(
name|Header
operator|.
name|class
argument_list|)
operator|||
name|parameter
operator|.
name|isAnnotationPresent
argument_list|(
name|Headers
operator|.
name|class
argument_list|)
operator|||
name|parameter
operator|.
name|isAnnotationPresent
argument_list|(
name|ExchangeProperty
operator|.
name|class
argument_list|)
operator|||
name|parameter
operator|.
name|isAnnotationPresent
argument_list|(
name|Body
operator|.
name|class
argument_list|)
condition|)
block|{
name|canUseBinding
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|binding
operator|&&
name|canUseBinding
condition|)
block|{
comment|// in binding mode we bind the passed in arguments (args) to the created exchange
comment|// using the existing Camel @Body, @Header, @Headers, @ExchangeProperty annotations
comment|// if no annotation then its bound as the message body
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Annotation
index|[]
name|row
range|:
name|method
operator|.
name|getParameterAnnotations
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|args
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|row
operator|==
literal|null
operator|||
name|row
operator|.
name|length
operator|==
literal|0
condition|)
block|{
comment|// assume its message body when there is no annotations
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|Annotation
name|ann
range|:
name|row
control|)
block|{
if|if
condition|(
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|Header
operator|.
name|class
argument_list|)
condition|)
block|{
name|Header
name|header
init|=
operator|(
name|Header
operator|)
name|ann
decl_stmt|;
name|String
name|name
init|=
name|header
operator|.
name|value
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|Headers
operator|.
name|class
argument_list|)
condition|)
block|{
name|Map
name|map
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|ExchangeProperty
operator|.
name|class
argument_list|)
condition|)
block|{
name|ExchangeProperty
name|ep
init|=
operator|(
name|ExchangeProperty
operator|)
name|ann
decl_stmt|;
name|String
name|name
init|=
name|ep
operator|.
name|value
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|Body
operator|.
name|class
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// assume its message body when there is no annotations
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|index
operator|++
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|binding
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Binding to service interface as @Body,@Header,@ExchangeProperty detected when calling proxy method: {}"
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No binding to service interface as @Body,@Header,@ExchangeProperty not detected when calling proxy method: {}"
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
return|return
name|doInvoke
argument_list|(
name|method
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|invokeWithBody (final Method method, Object body, final ExchangePattern pattern)
specifier|protected
name|Object
name|invokeWithBody
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|,
name|Object
name|body
parameter_list|,
specifier|final
name|ExchangePattern
name|pattern
parameter_list|)
throws|throws
name|Throwable
block|{
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
return|return
name|doInvoke
argument_list|(
name|method
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|doInvoke (final Method method, final Exchange exchange)
specifier|protected
name|Object
name|doInvoke
parameter_list|(
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Throwable
block|{
comment|// is the return type a future
specifier|final
name|boolean
name|isFuture
init|=
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
name|Future
operator|.
name|class
decl_stmt|;
comment|// create task to execute the proxy and gather the reply
name|FutureTask
argument_list|<
name|Object
argument_list|>
name|task
init|=
operator|new
name|FutureTask
argument_list|<>
argument_list|(
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
comment|// process the exchange
name|LOG
operator|.
name|trace
argument_list|(
literal|"Proxied method call {} invoking producer: {}"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Object
name|answer
init|=
name|afterInvoke
argument_list|(
name|method
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|,
name|isFuture
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Proxied method call {} returning: {}"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFuture
condition|)
block|{
comment|// submit task and return future
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Submitting task for exchange id {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getExecutorService
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
operator|.
name|submit
argument_list|(
name|task
argument_list|)
expr_stmt|;
return|return
name|task
return|;
block|}
else|else
block|{
comment|// execute task now
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
return|return
name|task
operator|.
name|get
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
comment|// we don't want the wrapped exception from JDK
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
block|}
DECL|method|afterInvoke (Method method, Exchange exchange, ExchangePattern pattern, boolean isFuture)
specifier|protected
name|Object
name|afterInvoke
parameter_list|(
name|Method
name|method
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|boolean
name|isFuture
parameter_list|)
throws|throws
name|Exception
block|{
comment|// check if we had an exception
name|Throwable
name|cause
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
name|Throwable
name|found
init|=
name|findSuitableException
argument_list|(
name|cause
argument_list|,
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|found
operator|instanceof
name|Exception
condition|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|found
throw|;
block|}
else|else
block|{
comment|// wrap as exception
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
comment|// special for runtime camel exceptions as they can be nested
if|if
condition|(
name|cause
operator|instanceof
name|RuntimeCamelException
condition|)
block|{
comment|// if the inner cause is a runtime exception we can throw it
comment|// directly
if|if
condition|(
name|cause
operator|.
name|getCause
argument_list|()
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
call|(
name|RuntimeException
call|)
argument_list|(
operator|(
name|RuntimeCamelException
operator|)
name|cause
argument_list|)
operator|.
name|getCause
argument_list|()
throw|;
block|}
throw|throw
operator|(
name|RuntimeCamelException
operator|)
name|cause
throw|;
block|}
comment|// okay just throw the exception as is
if|if
condition|(
name|cause
operator|instanceof
name|Exception
condition|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|cause
throw|;
block|}
else|else
block|{
comment|// wrap as exception
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|to
init|=
name|isFuture
condition|?
name|getGenericType
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|method
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
else|:
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
comment|// do not return a reply if the method is VOID
if|if
condition|(
name|to
operator|==
name|Void
operator|.
name|TYPE
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getBody
argument_list|(
name|exchange
argument_list|,
name|to
argument_list|)
return|;
block|}
DECL|method|getGenericType (CamelContext context, Type type)
specifier|protected
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getGenericType
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Type
name|type
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
comment|// fallback and use object
return|return
name|Object
operator|.
name|class
return|;
block|}
comment|// unfortunately java dont provide a nice api for getting the generic
comment|// type of the return type
comment|// due type erasure, so we have to gather it based on a String
comment|// representation
name|String
name|name
init|=
name|StringHelper
operator|.
name|between
argument_list|(
name|type
operator|.
name|toString
argument_list|()
argument_list|,
literal|"<"
argument_list|,
literal|">"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"<"
argument_list|)
condition|)
block|{
comment|// we only need the outer type
name|name
operator|=
name|StringHelper
operator|.
name|before
argument_list|(
name|name
argument_list|,
literal|"<"
argument_list|)
expr_stmt|;
block|}
return|return
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
comment|// fallback and use object
return|return
name|Object
operator|.
name|class
return|;
block|}
block|}
DECL|method|getExecutorService (CamelContext context)
specifier|protected
specifier|static
specifier|synchronized
name|ExecutorService
name|getExecutorService
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
comment|// CamelContext will shutdown thread pool when it shutdown so we can
comment|// lazy create it on demand
comment|// but in case of hot-deploy or the likes we need to be able to
comment|// re-create it (its a shared static instance)
if|if
condition|(
name|executorService
operator|==
literal|null
operator|||
name|executorService
operator|.
name|isTerminated
argument_list|()
operator|||
name|executorService
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
comment|// try to lookup a pool first based on id/profile
name|executorService
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"CamelInvocationHandler"
argument_list|,
name|ExecutorService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|CamelInvocationHandler
operator|.
name|class
argument_list|,
literal|"CamelInvocationHandler"
argument_list|,
literal|"CamelInvocationHandler"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|CamelInvocationHandler
operator|.
name|class
argument_list|,
literal|"CamelInvocationHandler"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|executorService
return|;
block|}
comment|/**      * Tries to find the best suited exception to throw.      *<p/>      * It looks in the exception hierarchy from the caused exception and matches      * this against the declared exceptions being thrown on the method.      *      * @param cause the caused exception      * @param method the method      * @return the exception to throw, or<tt>null</tt> if not possible to find      *         a suitable exception      */
DECL|method|findSuitableException (Throwable cause, Method method)
specifier|protected
name|Throwable
name|findSuitableException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
if|if
condition|(
name|method
operator|.
name|getExceptionTypes
argument_list|()
operator|==
literal|null
operator|||
name|method
operator|.
name|getExceptionTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// see if there is any exception which matches the declared exception on
comment|// the method
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|method
operator|.
name|getExceptionTypes
argument_list|()
control|)
block|{
name|Object
name|fault
init|=
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|type
argument_list|,
name|cause
argument_list|)
decl_stmt|;
if|if
condition|(
name|fault
operator|!=
literal|null
condition|)
block|{
return|return
name|Throwable
operator|.
name|class
operator|.
name|cast
argument_list|(
name|fault
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|isValidMethod (Method method)
specifier|protected
name|boolean
name|isValidMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
comment|// must not be in the excluded list
for|for
control|(
name|Method
name|excluded
range|:
name|EXCLUDED_METHODS
control|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isOverridingMethod
argument_list|(
name|excluded
argument_list|,
name|method
argument_list|)
condition|)
block|{
comment|// the method is overriding an excluded method so its not valid
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit
