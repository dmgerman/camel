begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ScheduledPollConsumer
import|;
end_import

begin_comment
comment|/**  * Abstract base class for API Component Consumers.  */
end_comment

begin_class
DECL|class|AbstractApiConsumer
specifier|public
specifier|abstract
class|class
name|AbstractApiConsumer
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
name|ScheduledPollConsumer
implements|implements
name|PropertyNamesInterceptor
implements|,
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
comment|// API method to invoke
DECL|field|method
specifier|protected
specifier|final
name|ApiMethod
name|method
decl_stmt|;
comment|// split Array or Collection API method results into multiple Exchanges
DECL|field|splitResult
specifier|private
name|boolean
name|splitResult
init|=
literal|true
decl_stmt|;
DECL|method|AbstractApiConsumer (AbstractApiEndpoint<E, T> endpoint, Processor processor)
specifier|public
name|AbstractApiConsumer
parameter_list|(
name|AbstractApiEndpoint
argument_list|<
name|E
argument_list|,
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|ApiConsumerHelper
operator|.
name|findMethod
argument_list|(
name|endpoint
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isGreedy ()
specifier|public
name|boolean
name|isGreedy
parameter_list|()
block|{
comment|// make this consumer not greedy to avoid making too many calls
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// invoke the consumer method
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|args
operator|.
name|putAll
argument_list|(
name|endpoint
operator|.
name|getEndpointProperties
argument_list|()
argument_list|)
expr_stmt|;
comment|// let the endpoint and the Consumer intercept properties
name|endpoint
operator|.
name|interceptProperties
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|interceptProperties
argument_list|(
name|args
argument_list|)
expr_stmt|;
try|try
block|{
name|Object
name|result
init|=
name|doInvokeMethod
argument_list|(
name|args
argument_list|)
decl_stmt|;
return|return
name|ApiConsumerHelper
operator|.
name|getResultsProcessed
argument_list|(
name|this
argument_list|,
name|result
argument_list|,
name|isSplitResult
argument_list|()
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
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|interceptPropertyNames (Set<String> propertyNames)
specifier|public
name|void
name|interceptPropertyNames
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|propertyNames
parameter_list|)
block|{
comment|// do nothing by default
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
comment|/**      * Invoke the API method.      * This method can be overridden, for example to synchronize API calls for thread-unsafe proxies.      * Derived class MUST call super.doInvokeMethod() to invoke the API method.      * @param args method arguments from endpoint parameters.      * @return method invocation result.      */
DECL|method|doInvokeMethod (Map<String, Object> args)
specifier|protected
name|Object
name|doInvokeMethod
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
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
name|args
argument_list|)
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|splitResult (Object result)
specifier|public
name|Object
name|splitResult
parameter_list|(
name|Object
name|result
parameter_list|)
block|{
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|interceptResult (Object result, Exchange resultExchange)
specifier|public
name|void
name|interceptResult
parameter_list|(
name|Object
name|result
parameter_list|,
name|Exchange
name|resultExchange
parameter_list|)
block|{
comment|// do nothing by default
block|}
DECL|method|isSplitResult ()
specifier|public
specifier|final
name|boolean
name|isSplitResult
parameter_list|()
block|{
return|return
name|splitResult
return|;
block|}
DECL|method|setSplitResult (boolean splitResult)
specifier|public
specifier|final
name|void
name|setSplitResult
parameter_list|(
name|boolean
name|splitResult
parameter_list|)
block|{
name|this
operator|.
name|splitResult
operator|=
name|splitResult
expr_stmt|;
block|}
block|}
end_class

end_unit

