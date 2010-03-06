begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|ParameterizedType
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
name|ScheduledExecutorService
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
name|CamelContextAware
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
name|Component
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
name|PollingConsumer
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

begin_comment
comment|/**  * A default endpoint useful for implementation inheritance  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultEndpoint
specifier|public
specifier|abstract
class|class
name|DefaultEndpoint
implements|implements
name|Endpoint
implements|,
name|CamelContextAware
block|{
DECL|field|DEFAULT_THREADPOOL_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_THREADPOOL_SIZE
init|=
literal|10
decl_stmt|;
DECL|field|endpointUri
specifier|private
name|String
name|endpointUri
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|component
specifier|private
name|Component
name|component
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|exchangePattern
specifier|private
name|ExchangePattern
name|exchangePattern
init|=
name|ExchangePattern
operator|.
name|InOnly
decl_stmt|;
DECL|method|DefaultEndpoint (String endpointUri, Component component)
specifier|protected
name|DefaultEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
name|component
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
DECL|method|DefaultEndpoint (String endpointUri, CamelContext camelContext)
specifier|protected
name|DefaultEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|DefaultEndpoint (String endpointUri)
specifier|protected
name|DefaultEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|this
operator|.
name|setEndpointUri
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultEndpoint ()
specifier|protected
name|DefaultEndpoint
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getEndpointUri
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
operator|+
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|DefaultEndpoint
condition|)
block|{
name|DefaultEndpoint
name|that
init|=
operator|(
name|DefaultEndpoint
operator|)
name|object
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|that
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
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
literal|"Endpoint["
operator|+
name|getEndpointUri
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
if|if
condition|(
name|endpointUri
operator|==
literal|null
condition|)
block|{
name|endpointUri
operator|=
name|createEndpointUri
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpointUri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"endpointUri is not specified and "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" does not implement createEndpointUri() to create a default value"
argument_list|)
throw|;
block|}
block|}
return|return
name|endpointUri
return|;
block|}
DECL|method|getEndpointKey ()
specifier|public
name|String
name|getEndpointKey
parameter_list|()
block|{
if|if
condition|(
name|isLenientProperties
argument_list|()
condition|)
block|{
comment|// only use the endpoint uri without parameters as the properties is lenient
name|String
name|uri
init|=
name|getEndpointUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|ObjectHelper
operator|.
name|before
argument_list|(
name|uri
argument_list|,
literal|"?"
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|uri
return|;
block|}
block|}
else|else
block|{
comment|// use the full endpoint uri
return|return
name|getEndpointUri
argument_list|()
return|;
block|}
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getComponent ()
specifier|public
name|Component
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
specifier|synchronized
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|Component
name|c
init|=
name|getComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|DefaultComponent
condition|)
block|{
name|DefaultComponent
name|dc
init|=
operator|(
name|DefaultComponent
operator|)
name|c
decl_stmt|;
name|executorService
operator|=
name|dc
operator|.
name|getExecutorService
argument_list|()
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
name|createScheduledExecutorService
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|executorService
return|;
block|}
DECL|method|getScheduledExecutorService ()
specifier|public
specifier|synchronized
name|ScheduledExecutorService
name|getScheduledExecutorService
parameter_list|()
block|{
name|ExecutorService
name|executor
init|=
name|getExecutorService
argument_list|()
decl_stmt|;
if|if
condition|(
name|executor
operator|instanceof
name|ScheduledExecutorService
condition|)
block|{
return|return
operator|(
name|ScheduledExecutorService
operator|)
name|executor
return|;
block|}
else|else
block|{
return|return
name|createScheduledExecutorService
argument_list|()
return|;
block|}
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
specifier|synchronized
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|EventDrivenPollingConsumer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createExchange (Exchange exchange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Class
argument_list|<
name|Exchange
argument_list|>
name|exchangeType
init|=
name|getExchangeType
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchangeType
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchangeType
operator|.
name|isInstance
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
return|return
name|exchangeType
operator|.
name|cast
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
return|return
name|exchange
operator|.
name|copy
argument_list|()
return|;
block|}
comment|/**      * Returns the type of the exchange which is generated by this component      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getExchangeType ()
specifier|public
name|Class
argument_list|<
name|Exchange
argument_list|>
name|getExchangeType
parameter_list|()
block|{
name|Type
name|type
init|=
name|getClass
argument_list|()
operator|.
name|getGenericSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|parameterizedType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
name|Type
index|[]
name|arguments
init|=
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
if|if
condition|(
name|arguments
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Type
name|argumentType
init|=
name|arguments
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|argumentType
operator|instanceof
name|Class
condition|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|Exchange
argument_list|>
operator|)
name|argumentType
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|)
return|;
block|}
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|exchangePattern
return|;
block|}
DECL|method|setExchangePattern (ExchangePattern exchangePattern)
specifier|public
name|void
name|setExchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|this
operator|.
name|exchangePattern
operator|=
name|exchangePattern
expr_stmt|;
block|}
DECL|method|createScheduledExecutorService ()
specifier|protected
name|ScheduledExecutorService
name|createScheduledExecutorService
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|,
name|DEFAULT_THREADPOOL_SIZE
argument_list|)
return|;
block|}
DECL|method|configureProperties (Map<String, Object> options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
comment|// do nothing by default
block|}
comment|/**      * A factory method to lazily create the endpointUri if none is specified       */
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Sets the endpointUri if it has not been specified yet via some kind of dependency injection mechanism.      * This allows dependency injection frameworks such as Spring or Guice to set the default endpoint URI in cases      * where it has not been explicitly configured using the name/context in which an Endpoint is created.      */
DECL|method|setEndpointUriIfNotSpecified (String value)
specifier|public
name|void
name|setEndpointUriIfNotSpecified
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|endpointUri
operator|==
literal|null
condition|)
block|{
name|setEndpointUri
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setEndpointUri (String endpointUri)
specifier|protected
name|void
name|setEndpointUri
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|this
operator|.
name|endpointUri
operator|=
name|endpointUri
expr_stmt|;
block|}
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// default should be false for most components
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

