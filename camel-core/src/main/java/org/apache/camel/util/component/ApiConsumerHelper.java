begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
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
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|impl
operator|.
name|DefaultConsumer
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
comment|/**  * Utility class for API Consumers.  */
end_comment

begin_class
DECL|class|ApiConsumerHelper
specifier|public
specifier|final
class|class
name|ApiConsumerHelper
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
name|ApiConsumerHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ApiConsumerHelper ()
specifier|private
name|ApiConsumerHelper
parameter_list|()
block|{     }
comment|/**      * Utility method to find matching API Method for supplied endpoint's configuration properties.      * @param endpoint endpoint for configuration properties.      * @param propertyNamesInterceptor names interceptor for adapting property names, usually the consumer class itself.      * @param<E> ApiName enumeration.      * @param<T> Component configuration class.      * @return matching ApiMethod.      */
DECL|method|findMethod ( AbstractApiEndpoint<E, T> endpoint, PropertyNamesInterceptor propertyNamesInterceptor)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
operator|&
name|ApiName
parameter_list|,
name|T
parameter_list|>
name|ApiMethod
name|findMethod
parameter_list|(
name|AbstractApiEndpoint
argument_list|<
name|E
argument_list|,
name|T
argument_list|>
name|endpoint
parameter_list|,
name|PropertyNamesInterceptor
name|propertyNamesInterceptor
parameter_list|)
block|{
name|ApiMethod
name|result
decl_stmt|;
comment|// find one that takes the largest subset of endpoint parameters
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|argNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|argNames
operator|.
name|addAll
argument_list|(
name|endpoint
operator|.
name|getEndpointPropertyNames
argument_list|()
argument_list|)
expr_stmt|;
name|propertyNamesInterceptor
operator|.
name|interceptPropertyNames
argument_list|(
name|argNames
argument_list|)
expr_stmt|;
specifier|final
name|String
index|[]
name|argNamesArray
init|=
name|argNames
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|argNames
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ApiMethod
argument_list|>
name|filteredMethods
init|=
name|endpoint
operator|.
name|methodHelper
operator|.
name|filterMethods
argument_list|(
name|endpoint
operator|.
name|getCandidates
argument_list|()
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|SUPER_SET
argument_list|,
name|argNamesArray
argument_list|)
decl_stmt|;
if|if
condition|(
name|filteredMethods
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ApiMethodHelper
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|methodHelper
init|=
name|endpoint
operator|.
name|getMethodHelper
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Missing properties for %s/%s, need one or more from %s"
argument_list|,
name|endpoint
operator|.
name|getApiName
argument_list|()
operator|.
name|getName
argument_list|()
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
comment|// single match
name|result
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
name|result
operator|=
name|ApiMethodHelper
operator|.
name|getHighestPriorityMethod
argument_list|(
name|filteredMethods
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Using highest priority operation %s from operations %s for endpoint %s"
argument_list|,
name|result
argument_list|,
name|filteredMethods
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Utility method for Consumers to process API method invocation result.      * @param consumer Consumer that wants to process results.      * @param result result of API method invocation.      * @param splitResult true if the Consumer wants to split result using {@link org.apache.camel.util.component.ResultInterceptor#splitResult(Object)} method.      * @param<T> Consumer class that extends DefaultConsumer and implements {@link org.apache.camel.util.component.ResultInterceptor}.      * @return number of result exchanges processed.      * @throws Exception on error.      */
DECL|method|getResultsProcessed ( T consumer, Object result, boolean splitResult)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|DefaultConsumer
operator|&
name|ResultInterceptor
parameter_list|>
name|int
name|getResultsProcessed
parameter_list|(
name|T
name|consumer
parameter_list|,
name|Object
name|result
parameter_list|,
name|boolean
name|splitResult
parameter_list|)
throws|throws
name|Exception
block|{
comment|// process result according to type
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
name|splitResult
condition|)
block|{
comment|// try to split the result
specifier|final
name|Object
name|results
init|=
name|consumer
operator|.
name|splitResult
argument_list|(
name|result
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|results
operator|instanceof
name|List
condition|)
block|{
comment|// Optimized for lists
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|results
decl_stmt|;
specifier|final
name|int
name|size
init|=
name|list
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// access elements by position rather than with iterator to
comment|// reduce garbage
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|processResult
argument_list|(
name|consumer
argument_list|,
name|result
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
elseif|else
if|if
condition|(
name|results
operator|instanceof
name|Iterable
condition|)
block|{
comment|// Optimized for iterable
name|int
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|singleResult
range|:
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
operator|)
name|results
control|)
block|{
name|processResult
argument_list|(
name|consumer
argument_list|,
name|result
argument_list|,
name|singleResult
argument_list|)
expr_stmt|;
name|size
operator|++
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
elseif|else
if|if
condition|(
name|results
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
comment|// Optimized for array
specifier|final
name|int
name|size
init|=
name|Array
operator|.
name|getLength
argument_list|(
name|results
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|processResult
argument_list|(
name|consumer
argument_list|,
name|result
argument_list|,
name|Array
operator|.
name|get
argument_list|(
name|results
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
block|}
block|}
name|processResult
argument_list|(
name|consumer
argument_list|,
name|result
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
comment|// number of messages polled
block|}
DECL|method|processResult (T consumer, Object methodResult, Object result)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|DefaultConsumer
operator|&
name|ResultInterceptor
parameter_list|>
name|void
name|processResult
parameter_list|(
name|T
name|consumer
parameter_list|,
name|Object
name|methodResult
parameter_list|,
name|Object
name|result
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|interceptResult
argument_list|(
name|methodResult
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
comment|// send message to next processor in the route
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// log exception if an exception occurred and was not handled
specifier|final
name|Exception
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
name|consumer
operator|.
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

