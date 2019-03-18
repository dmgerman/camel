begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_enum
DECL|enum|HazelcastOperation
specifier|public
enum|enum
name|HazelcastOperation
block|{
comment|// actions
DECL|enumConstant|PUT
name|PUT
argument_list|(
literal|"put"
argument_list|)
block|,
DECL|enumConstant|DELETE
name|DELETE
argument_list|(
literal|"delete"
argument_list|)
block|,
DECL|enumConstant|GET
name|GET
argument_list|(
literal|"get"
argument_list|)
block|,
DECL|enumConstant|UPDATE
name|UPDATE
argument_list|(
literal|"update"
argument_list|)
block|,
DECL|enumConstant|QUERY
name|QUERY
argument_list|(
literal|"query"
argument_list|)
block|,
DECL|enumConstant|GET_ALL
name|GET_ALL
argument_list|(
literal|"getAll"
argument_list|)
block|,
DECL|enumConstant|CLEAR
name|CLEAR
argument_list|(
literal|"clear"
argument_list|)
block|,
DECL|enumConstant|PUT_IF_ABSENT
name|PUT_IF_ABSENT
argument_list|(
literal|"putIfAbsent"
argument_list|)
block|,
DECL|enumConstant|ADD_ALL
name|ADD_ALL
argument_list|(
literal|"allAll"
argument_list|)
block|,
DECL|enumConstant|REMOVE_ALL
name|REMOVE_ALL
argument_list|(
literal|"removeAll"
argument_list|)
block|,
DECL|enumConstant|RETAIN_ALL
name|RETAIN_ALL
argument_list|(
literal|"retainAll"
argument_list|)
block|,
DECL|enumConstant|EVICT
name|EVICT
argument_list|(
literal|"evict"
argument_list|)
block|,
DECL|enumConstant|EVICT_ALL
name|EVICT_ALL
argument_list|(
literal|"evictAll"
argument_list|)
block|,
DECL|enumConstant|VALUE_COUNT
name|VALUE_COUNT
argument_list|(
literal|"valueCount"
argument_list|)
block|,
DECL|enumConstant|CONTAINS_KEY
name|CONTAINS_KEY
argument_list|(
literal|"containsKey"
argument_list|)
block|,
DECL|enumConstant|CONTAINS_VALUE
name|CONTAINS_VALUE
argument_list|(
literal|"containsValue"
argument_list|)
block|,
DECL|enumConstant|GET_KEYS
name|GET_KEYS
argument_list|(
literal|"keySet"
argument_list|)
block|,
comment|// multimap
DECL|enumConstant|REMOVE_VALUE
name|REMOVE_VALUE
argument_list|(
literal|"removevalue"
argument_list|)
block|,
comment|// atomic numbers
DECL|enumConstant|INCREMENT
name|INCREMENT
argument_list|(
literal|"increment"
argument_list|)
block|,
DECL|enumConstant|DECREMENT
name|DECREMENT
argument_list|(
literal|"decrement"
argument_list|)
block|,
DECL|enumConstant|SET_VALUE
name|SET_VALUE
argument_list|(
literal|"setvalue"
argument_list|)
block|,
DECL|enumConstant|DESTROY
name|DESTROY
argument_list|(
literal|"destroy"
argument_list|)
block|,
DECL|enumConstant|COMPARE_AND_SET
name|COMPARE_AND_SET
argument_list|(
literal|"compareAndSet"
argument_list|)
block|,
DECL|enumConstant|GET_AND_ADD
name|GET_AND_ADD
argument_list|(
literal|"getAndAdd"
argument_list|)
block|,
comment|// queue
DECL|enumConstant|ADD
name|ADD
argument_list|(
literal|"add"
argument_list|)
block|,
DECL|enumConstant|OFFER
name|OFFER
argument_list|(
literal|"offer"
argument_list|)
block|,
DECL|enumConstant|PEEK
name|PEEK
argument_list|(
literal|"peek"
argument_list|)
block|,
DECL|enumConstant|POLL
name|POLL
argument_list|(
literal|"poll"
argument_list|)
block|,
DECL|enumConstant|REMAINING_CAPACITY
name|REMAINING_CAPACITY
argument_list|(
literal|"remainingCapacity"
argument_list|)
block|,
DECL|enumConstant|DRAIN_TO
name|DRAIN_TO
argument_list|(
literal|"drainTo"
argument_list|)
block|,
DECL|enumConstant|REMOVE_IF
name|REMOVE_IF
argument_list|(
literal|"removeIf"
argument_list|)
block|,
DECL|enumConstant|TAKE
name|TAKE
argument_list|(
literal|"take"
argument_list|)
block|,
comment|// topic
DECL|enumConstant|PUBLISH
name|PUBLISH
argument_list|(
literal|"publish"
argument_list|)
block|,
comment|// ring_buffer
DECL|enumConstant|READ_ONCE_HEAD
name|READ_ONCE_HEAD
argument_list|(
literal|"readOnceHeal"
argument_list|)
block|,
DECL|enumConstant|READ_ONCE_TAIL
name|READ_ONCE_TAIL
argument_list|(
literal|"readOnceTail"
argument_list|)
block|,
DECL|enumConstant|CAPACITY
name|CAPACITY
argument_list|(
literal|"capacity"
argument_list|)
block|;
DECL|field|values
specifier|private
specifier|static
name|HazelcastOperation
index|[]
name|values
init|=
name|values
argument_list|()
decl_stmt|;
DECL|field|operation
specifier|private
specifier|final
name|String
name|operation
decl_stmt|;
DECL|method|HazelcastOperation (String operation)
name|HazelcastOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getHazelcastOperation (String name)
specifier|public
specifier|static
name|HazelcastOperation
name|getHazelcastOperation
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|HazelcastOperation
name|hazelcastOperation
range|:
name|values
control|)
block|{
if|if
condition|(
name|hazelcastOperation
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
operator|||
name|hazelcastOperation
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|hazelcastOperation
return|;
block|}
block|}
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
name|name
argument_list|)
argument_list|)
throw|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
block|}
end_enum

end_unit

