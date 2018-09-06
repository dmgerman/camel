begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

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
name|atomic
operator|.
name|LongAdder
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

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|Caffeine
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|RemovalCause
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|RemovalListener
import|;
end_import

begin_comment
comment|/**  * A cache that uses a near optional LRU Cache.  *<p/>  * The Cache is implemented by Caffeine which provides an<a href="https://github.com/ben-manes/caffeine/wiki/Efficiency">efficient cache</a>.  *<p/>  * If this cache stores {@link org.apache.camel.Service} then this implementation will on eviction  * invoke the {@link org.apache.camel.Service#stop()} method, to auto-stop the service.  *<p/>  * Use {@link LRUCacheFactory} to create a new instance (do not use the constructor).  *  * @see LRUCacheFactory  * @see LRUSoftCache  * @see LRUWeakCache  */
end_comment

begin_class
DECL|class|LRUCache
specifier|public
class|class
name|LRUCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|Serializable
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
name|LRUCache
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|hits
specifier|protected
specifier|final
name|LongAdder
name|hits
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
DECL|field|misses
specifier|protected
specifier|final
name|LongAdder
name|misses
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
DECL|field|evicted
specifier|protected
specifier|final
name|LongAdder
name|evicted
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
DECL|field|maxCacheSize
specifier|private
name|int
name|maxCacheSize
init|=
literal|10000
decl_stmt|;
DECL|field|stopOnEviction
specifier|private
name|boolean
name|stopOnEviction
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
decl_stmt|;
DECL|field|map
specifier|private
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|LRUCache (int maximumCacheSize)
specifier|public
name|LRUCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
name|this
argument_list|(
literal|16
argument_list|,
name|maximumCacheSize
argument_list|)
expr_stmt|;
comment|// 16 is the default initial capacity in ConcurrentLinkedHashMap
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified initial capacity, maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|LRUCache (int initialCapacity, int maximumCacheSize)
specifier|public
name|LRUCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
block|{
comment|//Do not stop service if ConcurrentLinkedHashMap try to evict entry when its max capacity is zero.
name|this
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|,
name|maximumCacheSize
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified initial capacity, maximumCacheSize,load factor and ordering mode.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @param stopOnEviction   whether to stop service on eviction.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|LRUCache (int initialCapacity, int maximumCacheSize, boolean stopOnEviction)
specifier|public
name|LRUCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|,
name|boolean
name|stopOnEviction
parameter_list|)
block|{
name|this
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|,
name|stopOnEviction
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified initial capacity, maximumCacheSize,load factor and ordering mode.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @param stopOnEviction   whether to stop service on eviction.      * @param soft             whether to use soft values a soft cache  (default is false)      * @param weak             whether to use weak keys/values as a weak cache  (default is false)      * @param syncListener     whether to use synchronous call for the eviction listener (default is false)      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|LRUCache (int initialCapacity, int maximumCacheSize, boolean stopOnEviction, boolean soft, boolean weak, boolean syncListener)
specifier|public
name|LRUCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|,
name|boolean
name|stopOnEviction
parameter_list|,
name|boolean
name|soft
parameter_list|,
name|boolean
name|weak
parameter_list|,
name|boolean
name|syncListener
parameter_list|)
block|{
name|Caffeine
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|caffeine
init|=
name|Caffeine
operator|.
name|newBuilder
argument_list|()
operator|.
name|initialCapacity
argument_list|(
name|initialCapacity
argument_list|)
operator|.
name|maximumSize
argument_list|(
name|maximumCacheSize
argument_list|)
operator|.
name|removalListener
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|soft
condition|)
block|{
name|caffeine
operator|.
name|softValues
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|weak
condition|)
block|{
name|caffeine
operator|.
name|weakKeys
argument_list|()
expr_stmt|;
name|caffeine
operator|.
name|weakValues
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|syncListener
condition|)
block|{
name|caffeine
operator|.
name|executor
argument_list|(
name|Runnable
operator|::
name|run
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|cache
operator|=
name|caffeine
operator|.
name|build
argument_list|()
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|cache
operator|.
name|asMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|maxCacheSize
operator|=
name|maximumCacheSize
expr_stmt|;
name|this
operator|.
name|stopOnEviction
operator|=
name|stopOnEviction
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (Object o)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|V
name|answer
init|=
name|map
operator|.
name|get
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|hits
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|misses
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|map
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|map
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|containsKey (Object o)
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsValue (Object o)
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsValue
argument_list|(
literal|0
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|put (K k, V v)
specifier|public
name|V
name|put
parameter_list|(
name|K
name|k
parameter_list|,
name|V
name|v
parameter_list|)
block|{
return|return
name|map
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Object o)
specifier|public
name|V
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|map
operator|.
name|remove
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resetStatistics
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|map
operator|.
name|keySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|map
operator|.
name|values
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|map
operator|.
name|entrySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onRemoval (K key, V value, RemovalCause cause)
specifier|public
name|void
name|onRemoval
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|RemovalCause
name|cause
parameter_list|)
block|{
if|if
condition|(
name|cause
operator|.
name|wasEvicted
argument_list|()
condition|)
block|{
name|evicted
operator|.
name|increment
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"onRemoval {} -> {}"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|stopOnEviction
condition|)
block|{
try|try
block|{
comment|// stop service as its evicted from cache
name|ServiceHelper
operator|.
name|stopService
argument_list|(
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error stopping service: "
operator|+
name|value
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Gets the number of cache hits      */
DECL|method|getHits ()
specifier|public
name|long
name|getHits
parameter_list|()
block|{
return|return
name|hits
operator|.
name|longValue
argument_list|()
return|;
block|}
comment|/**      * Gets the number of cache misses.      */
DECL|method|getMisses ()
specifier|public
name|long
name|getMisses
parameter_list|()
block|{
return|return
name|misses
operator|.
name|longValue
argument_list|()
return|;
block|}
comment|/**      * Gets the number of evicted entries.      */
DECL|method|getEvicted ()
specifier|public
name|long
name|getEvicted
parameter_list|()
block|{
return|return
name|evicted
operator|.
name|longValue
argument_list|()
return|;
block|}
comment|/**      * Returns the maxCacheSize.      */
DECL|method|getMaxCacheSize ()
specifier|public
name|int
name|getMaxCacheSize
parameter_list|()
block|{
return|return
name|maxCacheSize
return|;
block|}
comment|/**      * Rest the cache statistics such as hits and misses.      */
DECL|method|resetStatistics ()
specifier|public
name|void
name|resetStatistics
parameter_list|()
block|{
name|hits
operator|.
name|reset
argument_list|()
expr_stmt|;
name|misses
operator|.
name|reset
argument_list|()
expr_stmt|;
name|evicted
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{
name|cache
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
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
literal|"LRUCache@"
operator|+
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

