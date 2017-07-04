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
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|concurrent
operator|.
name|ThreadHelper
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
comment|/**  * Factory to create {@link LRUCache} instances.  */
end_comment

begin_class
DECL|class|LRUCacheFactory
specifier|public
specifier|final
class|class
name|LRUCacheFactory
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
name|LRUCacheFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|init
specifier|private
specifier|static
specifier|final
name|AtomicBoolean
name|init
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|method|LRUCacheFactory ()
specifier|private
name|LRUCacheFactory
parameter_list|()
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|warmUp ()
specifier|public
specifier|static
name|void
name|warmUp
parameter_list|()
block|{
comment|// create a dummy map in a separate thread to warm-up the Caffeine cache concurrently
comment|// while Camel is starting up. This allows us to overall startup Camel a bit faster
comment|// as Caffeine takes 150+ millis to initialize.
if|if
condition|(
name|init
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
comment|// only need to init Caffeine once in the JVM/classloader
name|Runnable
name|task
init|=
parameter_list|()
lambda|->
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Warming up LRUCache ..."
argument_list|)
expr_stmt|;
name|newLRUCache
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Warming up LRUCache complete in {} millis"
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
block|}
decl_stmt|;
name|String
name|threadName
init|=
name|ThreadHelper
operator|.
name|resolveThreadName
argument_list|(
literal|null
argument_list|,
literal|"LRUCacheFactory"
argument_list|)
decl_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|task
argument_list|,
name|threadName
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUCache (int maximumCacheSize)
specifier|public
specifier|static
name|LRUCache
name|newLRUCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating LRUCache with maximumCacheSize: {}"
argument_list|,
name|maximumCacheSize
argument_list|)
expr_stmt|;
return|return
operator|new
name|LRUCache
argument_list|(
name|maximumCacheSize
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified initial capacity, maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUCache (int initialCapacity, int maximumCacheSize)
specifier|public
specifier|static
name|LRUCache
name|newLRUCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating LRUCache with initialCapacity: {}, maximumCacheSize: {}"
argument_list|,
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|)
expr_stmt|;
return|return
operator|new
name|LRUCache
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified initial capacity, maximumCacheSize,load factor and ordering mode.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @param stopOnEviction   whether to stop service on eviction.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUCache (int initialCapacity, int maximumCacheSize, boolean stopOnEviction)
specifier|public
specifier|static
name|LRUCache
name|newLRUCache
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating LRUCache with initialCapacity: {}, maximumCacheSize: {}, stopOnEviction: {}"
argument_list|,
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|,
name|stopOnEviction
argument_list|)
expr_stmt|;
return|return
operator|new
name|LRUCache
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|,
name|stopOnEviction
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUSoftCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUSoftCache (int maximumCacheSize)
specifier|public
specifier|static
name|LRUSoftCache
name|newLRUSoftCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating LRUSoftCache with maximumCacheSize: {}"
argument_list|,
name|maximumCacheSize
argument_list|)
expr_stmt|;
return|return
operator|new
name|LRUSoftCache
argument_list|(
name|maximumCacheSize
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUWeakCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUWeakCache (int maximumCacheSize)
specifier|public
specifier|static
name|LRUWeakCache
name|newLRUWeakCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating LRUWeakCache with maximumCacheSize: {}"
argument_list|,
name|maximumCacheSize
argument_list|)
expr_stmt|;
return|return
operator|new
name|LRUWeakCache
argument_list|(
name|maximumCacheSize
argument_list|)
return|;
block|}
block|}
end_class

end_unit

