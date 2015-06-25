begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
package|;
end_package

begin_interface
DECL|interface|InfinispanConstants
specifier|public
interface|interface
name|InfinispanConstants
block|{
DECL|field|EVENT_TYPE
name|String
name|EVENT_TYPE
init|=
literal|"CamelInfinispanEventType"
decl_stmt|;
DECL|field|IS_PRE
name|String
name|IS_PRE
init|=
literal|"CamelInfinispanIsPre"
decl_stmt|;
DECL|field|CACHE_NAME
name|String
name|CACHE_NAME
init|=
literal|"CamelInfinispanCacheName"
decl_stmt|;
DECL|field|KEY
name|String
name|KEY
init|=
literal|"CamelInfinispanKey"
decl_stmt|;
DECL|field|VALUE
name|String
name|VALUE
init|=
literal|"CamelInfinispanValue"
decl_stmt|;
DECL|field|OLD_VALUE
name|String
name|OLD_VALUE
init|=
literal|"CamelInfinispanOldValue"
decl_stmt|;
DECL|field|MAP
name|String
name|MAP
init|=
literal|"CamelInfinispanMap"
decl_stmt|;
DECL|field|OPERATION
name|String
name|OPERATION
init|=
literal|"CamelInfinispanOperation"
decl_stmt|;
DECL|field|PUT
name|String
name|PUT
init|=
literal|"CamelInfinispanOperationPut"
decl_stmt|;
DECL|field|PUT_ASYNC
name|String
name|PUT_ASYNC
init|=
literal|"CamelInfinispanOperationPutAsync"
decl_stmt|;
DECL|field|PUT_IF_ABSENT
name|String
name|PUT_IF_ABSENT
init|=
literal|"CamelInfinispanOperationPutIfAbsent"
decl_stmt|;
DECL|field|PUT_IF_ABSENT_ASYNC
name|String
name|PUT_IF_ABSENT_ASYNC
init|=
literal|"CamelInfinispanOperationPutIfAbsentAsync"
decl_stmt|;
DECL|field|GET
name|String
name|GET
init|=
literal|"CamelInfinispanOperationGet"
decl_stmt|;
DECL|field|CONTAINS_KEY
name|String
name|CONTAINS_KEY
init|=
literal|"CamelInfinispanOperationContainsKey"
decl_stmt|;
DECL|field|CONTAINS_VALUE
name|String
name|CONTAINS_VALUE
init|=
literal|"CamelInfinispanOperationContainsValue"
decl_stmt|;
DECL|field|PUT_ALL
name|String
name|PUT_ALL
init|=
literal|"CamelInfinispanOperationPutAll"
decl_stmt|;
DECL|field|PUT_ALL_ASYNC
name|String
name|PUT_ALL_ASYNC
init|=
literal|"CamelInfinispanOperationPutAllAsync"
decl_stmt|;
DECL|field|REMOVE
name|String
name|REMOVE
init|=
literal|"CamelInfinispanOperationRemove"
decl_stmt|;
DECL|field|REMOVE_ASYNC
name|String
name|REMOVE_ASYNC
init|=
literal|"CamelInfinispanOperationRemoveAsync"
decl_stmt|;
DECL|field|REPLACE
name|String
name|REPLACE
init|=
literal|"CamelInfinispanOperationReplace"
decl_stmt|;
DECL|field|REPLACE_ASYNC
name|String
name|REPLACE_ASYNC
init|=
literal|"CamelInfinispanOperationReplaceAsync"
decl_stmt|;
DECL|field|CLEAR
name|String
name|CLEAR
init|=
literal|"CamelInfinispanOperationClear"
decl_stmt|;
DECL|field|CLEAR_ASYNC
name|String
name|CLEAR_ASYNC
init|=
literal|"CamelInfinispanOperationClearAsync"
decl_stmt|;
DECL|field|SIZE
name|String
name|SIZE
init|=
literal|"CamelInfinispanOperationSize"
decl_stmt|;
DECL|field|RESULT
name|String
name|RESULT
init|=
literal|"CamelInfinispanOperationResult"
decl_stmt|;
DECL|field|LIFESPAN_TIME
name|String
name|LIFESPAN_TIME
init|=
literal|"CamelInfinispanLifespanTime"
decl_stmt|;
DECL|field|LIFESPAN_TIME_UNIT
name|String
name|LIFESPAN_TIME_UNIT
init|=
literal|"CamelInfinispanTimeUnit"
decl_stmt|;
DECL|field|MAX_IDLE_TIME
name|String
name|MAX_IDLE_TIME
init|=
literal|"CamelInfinispanMaxIdleTime"
decl_stmt|;
DECL|field|MAX_IDLE_TIME_UNIT
name|String
name|MAX_IDLE_TIME_UNIT
init|=
literal|"CamelInfinispanMaxIdleTimeUnit"
decl_stmt|;
block|}
end_interface

end_unit

