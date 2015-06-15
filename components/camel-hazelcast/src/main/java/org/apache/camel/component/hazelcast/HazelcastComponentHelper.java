begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_class
DECL|class|HazelcastComponentHelper
specifier|public
specifier|final
class|class
name|HazelcastComponentHelper
block|{
DECL|field|mapping
specifier|private
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|mapping
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|HazelcastComponentHelper ()
specifier|public
name|HazelcastComponentHelper
parameter_list|()
block|{
name|this
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|copyHeaders (Exchange ex)
specifier|public
specifier|static
name|void
name|copyHeaders
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
comment|// get in headers
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
comment|// delete item id
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|)
condition|)
block|{
name|headers
operator|.
name|remove
argument_list|(
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
condition|)
block|{
name|headers
operator|.
name|remove
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
expr_stmt|;
block|}
comment|// propagate headers if OUT message created
if|if
condition|(
name|ex
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setListenerHeaders (Exchange ex, String listenerType, String listenerAction, String cacheName)
specifier|public
specifier|static
name|void
name|setListenerHeaders
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|String
name|listenerType
parameter_list|,
name|String
name|listenerAction
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|CACHE_NAME
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
name|HazelcastComponentHelper
operator|.
name|setListenerHeaders
argument_list|(
name|ex
argument_list|,
name|listenerType
argument_list|,
name|listenerAction
argument_list|)
expr_stmt|;
block|}
DECL|method|setListenerHeaders (Exchange ex, String listenerType, String listenerAction)
specifier|public
specifier|static
name|void
name|setListenerHeaders
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|String
name|listenerType
parameter_list|,
name|String
name|listenerAction
parameter_list|)
block|{
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_ACTION
argument_list|,
name|listenerAction
argument_list|)
expr_stmt|;
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_TYPE
argument_list|,
name|listenerType
argument_list|)
expr_stmt|;
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_TIME
argument_list|,
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|lookupOperationNumber (Exchange exchange, int defaultOperation)
specifier|public
name|int
name|lookupOperationNumber
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|defaultOperation
parameter_list|)
block|{
return|return
name|extractOperationNumber
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
argument_list|,
name|defaultOperation
argument_list|)
return|;
block|}
DECL|method|extractOperationNumber (Object value, int defaultOperation)
specifier|public
name|int
name|extractOperationNumber
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|defaultOperation
parameter_list|)
block|{
name|int
name|operation
init|=
name|defaultOperation
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|operation
operator|=
name|mapToOperationNumber
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Integer
condition|)
block|{
name|operation
operator|=
operator|(
name|Integer
operator|)
name|value
expr_stmt|;
block|}
return|return
name|operation
return|;
block|}
comment|/**      * Allows the use of speaking operation names (e.g. for usage in Spring DSL)      */
DECL|method|mapToOperationNumber (String operationName)
specifier|private
name|int
name|mapToOperationNumber
parameter_list|(
name|String
name|operationName
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|mapping
operator|.
name|containsKey
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
return|return
name|this
operator|.
name|mapping
operator|.
name|get
argument_list|(
name|operationName
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Operation '%s' is not supported by this component."
argument_list|,
name|operationName
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|init ()
specifier|private
name|void
name|init
parameter_list|()
block|{
comment|// fill map with values
name|addMapping
argument_list|(
literal|"put"
argument_list|,
name|HazelcastConstants
operator|.
name|PUT_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"delete"
argument_list|,
name|HazelcastConstants
operator|.
name|DELETE_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"get"
argument_list|,
name|HazelcastConstants
operator|.
name|GET_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"update"
argument_list|,
name|HazelcastConstants
operator|.
name|UPDATE_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"query"
argument_list|,
name|HazelcastConstants
operator|.
name|QUERY_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"getAll"
argument_list|,
name|HazelcastConstants
operator|.
name|GET_ALL_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"clear"
argument_list|,
name|HazelcastConstants
operator|.
name|CLEAR_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"evict"
argument_list|,
name|HazelcastConstants
operator|.
name|EVICT_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"evictAll"
argument_list|,
name|HazelcastConstants
operator|.
name|EVICT_ALL_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"putIfAbsent"
argument_list|,
name|HazelcastConstants
operator|.
name|PUT_IF_ABSENT_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"addAll"
argument_list|,
name|HazelcastConstants
operator|.
name|ADD_ALL_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"removeAll"
argument_list|,
name|HazelcastConstants
operator|.
name|REMOVE_ALL_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"retainAll"
argument_list|,
name|HazelcastConstants
operator|.
name|RETAIN_ALL_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"valueCount"
argument_list|,
name|HazelcastConstants
operator|.
name|VALUE_COUNT_OPERATION
argument_list|)
expr_stmt|;
comment|// multimap
name|addMapping
argument_list|(
literal|"removevalue"
argument_list|,
name|HazelcastConstants
operator|.
name|REMOVEVALUE_OPERATION
argument_list|)
expr_stmt|;
comment|// atomic numbers
name|addMapping
argument_list|(
literal|"increment"
argument_list|,
name|HazelcastConstants
operator|.
name|INCREMENT_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"decrement"
argument_list|,
name|HazelcastConstants
operator|.
name|DECREMENT_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"setvalue"
argument_list|,
name|HazelcastConstants
operator|.
name|SETVALUE_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"destroy"
argument_list|,
name|HazelcastConstants
operator|.
name|DESTROY_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"compareAndSet"
argument_list|,
name|HazelcastConstants
operator|.
name|COMPARE_AND_SET_OPERATION
argument_list|)
expr_stmt|;
comment|// queue
name|addMapping
argument_list|(
literal|"add"
argument_list|,
name|HazelcastConstants
operator|.
name|ADD_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"offer"
argument_list|,
name|HazelcastConstants
operator|.
name|OFFER_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"peek"
argument_list|,
name|HazelcastConstants
operator|.
name|PEEK_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"poll"
argument_list|,
name|HazelcastConstants
operator|.
name|POLL_OPERATION
argument_list|)
expr_stmt|;
name|addMapping
argument_list|(
literal|"remainingCapacity"
argument_list|,
name|HazelcastConstants
operator|.
name|REMAINING_CAPACITY_OPERATION
argument_list|)
expr_stmt|;
comment|// topic
name|addMapping
argument_list|(
literal|"publish"
argument_list|,
name|HazelcastConstants
operator|.
name|PUBLISH_OPERATION
argument_list|)
expr_stmt|;
block|}
DECL|method|addMapping (String operationName, int operationNumber)
specifier|private
name|void
name|addMapping
parameter_list|(
name|String
name|operationName
parameter_list|,
name|int
name|operationNumber
parameter_list|)
block|{
name|this
operator|.
name|mapping
operator|.
name|put
argument_list|(
name|operationName
argument_list|,
name|operationNumber
argument_list|)
expr_stmt|;
name|this
operator|.
name|mapping
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|operationNumber
argument_list|)
argument_list|,
name|operationNumber
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

