begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|component
package|;
end_package

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
name|Set
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
name|DefaultAsyncProducer
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
comment|/**  * Base class for API based Producers  */
end_comment

begin_class
DECL|class|AbstractApiProducer
specifier|public
specifier|abstract
class|class
name|AbstractApiProducer
parameter_list|<
name|E
extends|extends
name|Enum
parameter_list|<
name|E
parameter_list|>
operator|&
name|ApiName
parameter_list|,
name|T
parameter_list|>
extends|extends
name|DefaultAsyncProducer
implements|implements
name|PropertiesInterceptor
implements|,
name|ResultInterceptor
block|{
comment|// API Endpoint
DECL|field|endpoint
specifier|protected
specifier|final
name|AbstractApiEndpoint
argument_list|<
name|E
argument_list|,
name|T
argument_list|>
name|endpoint
decl_stmt|;
comment|// properties helper
DECL|field|propertiesHelper
specifier|protected
specifier|final
name|ApiMethodPropertiesHelper
argument_list|<
name|T
argument_list|>
name|propertiesHelper
decl_stmt|;
comment|// method helper
DECL|field|methodHelper
specifier|protected
specifier|final
name|ApiMethodHelper
argument_list|<
name|?
argument_list|>
name|methodHelper
decl_stmt|;
comment|// logger
DECL|field|log
specifier|private
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|AbstractApiProducer (AbstractApiEndpoint<E, T> endpoint, ApiMethodPropertiesHelper<T> propertiesHelper)
specifier|public
name|AbstractApiProducer
parameter_list|(
name|AbstractApiEndpoint
argument_list|<
name|E
argument_list|,
name|T
argument_list|>
name|endpoint
parameter_list|,
name|ApiMethodPropertiesHelper
argument_list|<
name|T
argument_list|>
name|propertiesHelper
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|propertiesHelper
operator|=
name|propertiesHelper
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|methodHelper
operator|=
name|endpoint
operator|.
name|getMethodHelper
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// properties for method arguments
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|putAll
argument_list|(
name|endpoint
operator|.
name|getEndpointProperties
argument_list|()
argument_list|)
expr_stmt|;
name|propertiesHelper
operator|.
name|getExchangeProperties
argument_list|(
name|exchange
argument_list|,
name|properties
argument_list|)
expr_stmt|;
comment|// let the endpoint and the Producer intercept properties
name|endpoint
operator|.
name|interceptProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|interceptProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
comment|// decide which method to invoke
specifier|final
name|ApiMethod
name|method
init|=
name|findMethod
argument_list|(
name|exchange
argument_list|,
name|properties
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
comment|// synchronous failure
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// create a runnable invocation task to be submitted on a background thread pool
comment|// this way we avoid blocking the current thread for long running methods
name|Runnable
name|invocation
init|=
operator|new
name|Runnable
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
literal|"Invoking operation {} with {}"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|properties
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Object
name|result
init|=
name|doInvokeMethod
argument_list|(
name|method
argument_list|,
name|properties
argument_list|)
decl_stmt|;
comment|// producer returns a single response, even for methods with List return types
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|// copy headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|interceptResult
argument_list|(
name|result
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|endpoint
operator|.
name|getExecutorService
argument_list|()
operator|.
name|submit
argument_list|(
name|invocation
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|interceptProperties (Map<String, Object> properties)
specifier|public
name|void
name|interceptProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
comment|// do nothing by default
block|}
comment|/**      * Invoke the API method. Derived classes can override, but MUST call super.doInvokeMethod().      * @param method API method to invoke.      * @param properties method arguments from endpoint properties and exchange In headers.      * @return API method invocation result.      * @throws RuntimeCamelException on error. Exceptions thrown by API method are wrapped.      */
DECL|method|doInvokeMethod (ApiMethod method, Map<String, Object> properties)
specifier|protected
name|Object
name|doInvokeMethod
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
return|return
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|endpoint
operator|.
name|getApiProxy
argument_list|(
name|method
argument_list|,
name|properties
argument_list|)
argument_list|,
name|method
argument_list|,
name|properties
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|splitResult (Object result)
specifier|public
specifier|final
name|Object
name|splitResult
parameter_list|(
name|Object
name|result
parameter_list|)
block|{
comment|// producer never splits results
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|interceptResult (Object methodResult, Exchange resultExchange)
specifier|public
name|void
name|interceptResult
parameter_list|(
name|Object
name|methodResult
parameter_list|,
name|Exchange
name|resultExchange
parameter_list|)
block|{
comment|// do nothing by default
block|}
DECL|method|findMethod (Exchange exchange, Map<String, Object> properties)
specifier|protected
name|ApiMethod
name|findMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|ApiMethod
name|method
init|=
literal|null
decl_stmt|;
specifier|final
name|List
argument_list|<
name|ApiMethod
argument_list|>
name|candidates
init|=
name|endpoint
operator|.
name|getCandidates
argument_list|()
decl_stmt|;
if|if
condition|(
name|processInBody
argument_list|(
name|exchange
argument_list|,
name|properties
argument_list|)
condition|)
block|{
comment|// filter candidates based on endpoint and exchange properties
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|argNames
init|=
name|properties
operator|.
name|keySet
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|ApiMethod
argument_list|>
name|filteredMethods
init|=
name|methodHelper
operator|.
name|filterMethods
argument_list|(
name|candidates
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|SUPER_SET
argument_list|,
name|argNames
argument_list|)
decl_stmt|;
comment|// get the method to call
if|if
condition|(
name|filteredMethods
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Missing properties for %s, need one or more from %s"
argument_list|,
name|endpoint
operator|.
name|getMethodName
argument_list|()
argument_list|,
name|methodHelper
operator|.
name|getMissingProperties
argument_list|(
name|endpoint
operator|.
name|getMethodName
argument_list|()
argument_list|,
name|argNames
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|filteredMethods
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// found an exact match
name|method
operator|=
name|filteredMethods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|method
operator|=
name|ApiMethodHelper
operator|.
name|getHighestPriorityMethod
argument_list|(
name|filteredMethods
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Calling highest priority operation {} from operations {}"
argument_list|,
name|method
argument_list|,
name|filteredMethods
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|method
return|;
block|}
comment|// returns false on exception, which is set in exchange
DECL|method|processInBody (Exchange exchange, Map<String, Object> properties)
specifier|private
name|boolean
name|processInBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
specifier|final
name|String
name|inBodyProperty
init|=
name|endpoint
operator|.
name|getInBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|inBodyProperty
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|value
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
name|inBodyProperty
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error converting value %s to property %s: %s"
argument_list|,
name|value
argument_list|,
name|inBodyProperty
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
comment|// allow null values for inBody only if its a nullable option
if|if
condition|(
operator|!
name|methodHelper
operator|.
name|getNullableArguments
argument_list|()
operator|.
name|contains
argument_list|(
name|inBodyProperty
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|NullPointerException
argument_list|(
name|inBodyProperty
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Property [{}] has message body value {}"
argument_list|,
name|inBodyProperty
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|inBodyProperty
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit
