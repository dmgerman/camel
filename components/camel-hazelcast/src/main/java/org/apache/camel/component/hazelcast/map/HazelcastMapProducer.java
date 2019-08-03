begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.map
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
operator|.
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|IMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|query
operator|.
name|SqlPredicate
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
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastComponentHelper
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
name|hazelcast
operator|.
name|HazelcastConstants
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
name|hazelcast
operator|.
name|HazelcastDefaultProducer
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
name|hazelcast
operator|.
name|HazelcastOperation
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

begin_class
DECL|class|HazelcastMapProducer
specifier|public
class|class
name|HazelcastMapProducer
extends|extends
name|HazelcastDefaultProducer
block|{
DECL|field|cache
specifier|private
specifier|final
name|IMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
DECL|method|HazelcastMapProducer (HazelcastInstance hazelcastInstance, HazelcastMapEndpoint endpoint, String cacheName)
specifier|public
name|HazelcastMapProducer
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|HazelcastMapEndpoint
name|endpoint
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|hazelcastInstance
operator|.
name|getMap
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
comment|// GET header parameters
name|Object
name|oid
init|=
literal|null
decl_stmt|;
name|Object
name|ovalue
init|=
literal|null
decl_stmt|;
name|Object
name|ttl
init|=
literal|null
decl_stmt|;
name|Object
name|ttlUnit
init|=
literal|null
decl_stmt|;
name|String
name|query
init|=
literal|null
decl_stmt|;
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
name|oid
operator|=
name|headers
operator|.
name|get
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
name|OBJECT_VALUE
argument_list|)
condition|)
block|{
name|ovalue
operator|=
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|OBJECT_VALUE
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
name|TTL_VALUE
argument_list|)
condition|)
block|{
name|ttl
operator|=
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|TTL_VALUE
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
name|TTL_UNIT
argument_list|)
condition|)
block|{
name|ttlUnit
operator|=
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|TTL_UNIT
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
name|QUERY
argument_list|)
condition|)
block|{
name|query
operator|=
operator|(
name|String
operator|)
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|QUERY
argument_list|)
expr_stmt|;
block|}
specifier|final
name|HazelcastOperation
name|operation
init|=
name|lookupOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|PUT
case|:
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ttl
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ttlUnit
argument_list|)
condition|)
block|{
name|this
operator|.
name|put
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|put
argument_list|(
name|oid
argument_list|,
name|ttl
argument_list|,
name|ttlUnit
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|PUT_IF_ABSENT
case|:
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ttl
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ttlUnit
argument_list|)
condition|)
block|{
name|this
operator|.
name|putIfAbsent
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|putIfAbsent
argument_list|(
name|oid
argument_list|,
name|ttl
argument_list|,
name|ttlUnit
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|GET
case|:
name|this
operator|.
name|get
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_ALL
case|:
name|this
operator|.
name|getAll
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_KEYS
case|:
name|this
operator|.
name|getKeys
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|CONTAINS_KEY
case|:
name|this
operator|.
name|containsKey
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|CONTAINS_VALUE
case|:
name|this
operator|.
name|containsValue
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|DELETE
case|:
name|this
operator|.
name|delete
argument_list|(
name|oid
argument_list|)
expr_stmt|;
break|break;
case|case
name|UPDATE
case|:
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|ovalue
argument_list|)
condition|)
block|{
name|this
operator|.
name|update
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|update
argument_list|(
name|oid
argument_list|,
name|ovalue
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|QUERY
case|:
name|this
operator|.
name|query
argument_list|(
name|query
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|CLEAR
case|:
name|this
operator|.
name|clear
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|EVICT
case|:
name|this
operator|.
name|evict
argument_list|(
name|oid
argument_list|)
expr_stmt|;
break|break;
case|case
name|EVICT_ALL
case|:
name|this
operator|.
name|evictAll
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The value '%s' is not allowed for parameter '%s' on the MAP cache."
argument_list|,
name|operation
argument_list|,
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
argument_list|)
throw|;
block|}
comment|// finally copy headers
name|HazelcastComponentHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * QUERY map with a sql like syntax (see http://www.hazelcast.com/)      */
DECL|method|query (String query, Exchange exchange)
specifier|private
name|void
name|query
parameter_list|(
name|String
name|query
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|result
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
name|result
operator|=
name|this
operator|.
name|cache
operator|.
name|values
argument_list|(
operator|new
name|SqlPredicate
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|this
operator|.
name|cache
operator|.
name|values
argument_list|()
expr_stmt|;
block|}
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
block|}
comment|/**      * UPDATE an object in your cache (the whole object will be replaced)      */
DECL|method|update (Object oid, Exchange exchange)
specifier|private
name|void
name|update
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|cache
operator|.
name|lock
argument_list|(
name|oid
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|replace
argument_list|(
name|oid
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|unlock
argument_list|(
name|oid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Replaces the entry for given id with a specific value in the body, only if currently mapped to a given value      */
DECL|method|update (Object oid, Object ovalue, Exchange exchange)
specifier|private
name|void
name|update
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Object
name|ovalue
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|cache
operator|.
name|lock
argument_list|(
name|oid
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|replace
argument_list|(
name|oid
argument_list|,
name|ovalue
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|unlock
argument_list|(
name|oid
argument_list|)
expr_stmt|;
block|}
comment|/**      * remove an object from the cache      */
DECL|method|delete (Object oid)
specifier|private
name|void
name|delete
parameter_list|(
name|Object
name|oid
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|.
name|remove
argument_list|(
name|oid
argument_list|)
expr_stmt|;
block|}
comment|/**      * find an object by the given id and give it back      */
DECL|method|get (Object oid, Exchange exchange)
specifier|private
name|void
name|get
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|cache
operator|.
name|get
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * GET All objects and give it back      */
DECL|method|getAll (Object oid, Exchange exchange)
specifier|private
name|void
name|getAll
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|cache
operator|.
name|getAll
argument_list|(
operator|(
name|Set
argument_list|<
name|Object
argument_list|>
operator|)
name|oid
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * PUT a new object into the cache      */
DECL|method|put (Object oid, Exchange exchange)
specifier|private
name|void
name|put
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|cache
operator|.
name|put
argument_list|(
name|oid
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * PUT a new object into the cache with a specific time to live      */
DECL|method|put (Object oid, Object ttl, Object ttlUnit, Exchange exchange)
specifier|private
name|void
name|put
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Object
name|ttl
parameter_list|,
name|Object
name|ttlUnit
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|cache
operator|.
name|put
argument_list|(
name|oid
argument_list|,
name|body
argument_list|,
operator|(
name|long
operator|)
name|ttl
argument_list|,
operator|(
name|TimeUnit
operator|)
name|ttlUnit
argument_list|)
expr_stmt|;
block|}
comment|/**      * if the specified key is not already associated with a value, associate it with the given value.      */
DECL|method|putIfAbsent (Object oid, Exchange exchange)
specifier|private
name|void
name|putIfAbsent
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|cache
operator|.
name|putIfAbsent
argument_list|(
name|oid
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * Puts an entry into this map with a given ttl (time to live) value if the specified key is not already associated with a value.      */
DECL|method|putIfAbsent (Object oid, Object ttl, Object ttlUnit, Exchange exchange)
specifier|private
name|void
name|putIfAbsent
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Object
name|ttl
parameter_list|,
name|Object
name|ttlUnit
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|cache
operator|.
name|putIfAbsent
argument_list|(
name|oid
argument_list|,
name|body
argument_list|,
operator|(
name|long
operator|)
name|ttl
argument_list|,
operator|(
name|TimeUnit
operator|)
name|ttlUnit
argument_list|)
expr_stmt|;
block|}
comment|/**      * Clear all the entries      */
DECL|method|clear (Exchange exchange)
specifier|private
name|void
name|clear
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Eviction operation for a specific key      */
DECL|method|evict (Object oid)
specifier|private
name|void
name|evict
parameter_list|(
name|Object
name|oid
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|.
name|evict
argument_list|(
name|oid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Evict All operation       */
DECL|method|evictAll ()
specifier|private
name|void
name|evictAll
parameter_list|()
block|{
name|this
operator|.
name|cache
operator|.
name|evictAll
argument_list|()
expr_stmt|;
block|}
comment|/**      * Check for a specific key in the cache and return true if it exists or false otherwise      */
DECL|method|containsKey (Object oid, Exchange exchange)
specifier|private
name|void
name|containsKey
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|cache
operator|.
name|containsKey
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check for a specific value in the cache and return true if it exists or false otherwise      */
DECL|method|containsValue (Exchange exchange)
specifier|private
name|void
name|containsValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|cache
operator|.
name|containsValue
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**     * GET keys set of objects and give it back     */
DECL|method|getKeys (Exchange exchange)
specifier|private
name|void
name|getKeys
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|cache
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

