begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
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
specifier|abstract
class|class
name|LRUCacheFactory
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
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
DECL|field|instance
specifier|private
specifier|static
specifier|volatile
name|LRUCacheFactory
name|instance
decl_stmt|;
DECL|method|getInstance ()
specifier|public
specifier|static
name|LRUCacheFactory
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|LRUCacheFactory
operator|.
name|class
init|)
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
name|createLRUCacheFactory
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|instance
return|;
block|}
DECL|method|createLRUCacheFactory ()
specifier|private
specifier|static
name|LRUCacheFactory
name|createLRUCacheFactory
parameter_list|()
block|{
try|try
block|{
name|ClassLoader
name|classLoader
init|=
name|LRUCacheFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|classLoader
operator|.
name|getResource
argument_list|(
literal|"META-INF/services/org/apache/camel/lru-cache-factory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|String
name|clazzName
init|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"class"
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
name|clazzName
argument_list|)
decl_stmt|;
name|Object
name|factory
init|=
name|clazz
operator|.
name|getDeclaredConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
return|return
operator|(
name|LRUCacheFactory
operator|)
name|factory
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error creating LRUCacheFactory"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|DefaultLRUCacheFactory
argument_list|()
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUCache (int maximumCacheSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUCache
argument_list|(
name|maximumCacheSize
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUCache (int maximumCacheSize, Consumer<V> onEvict)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|,
name|Consumer
argument_list|<
name|V
argument_list|>
name|onEvict
parameter_list|)
block|{
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUCache
argument_list|(
name|maximumCacheSize
argument_list|,
name|onEvict
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified initial capacity, maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUCache (int initialCapacity, int maximumCacheSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
block|{
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUCache
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
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUCache
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
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUSoftCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUSoftCache
argument_list|(
name|maximumCacheSize
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUSoftCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUSoftCache (int initialCapacity, int maximumCacheSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUSoftCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
block|{
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUSoftCache
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUSoftCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @param stopOnEviction   whether to stop service on eviction.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUSoftCache (int initialCapacity, int maximumCacheSize, boolean stopOnEviction)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUSoftCache
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
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUSoftCache
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|,
name|stopOnEviction
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUWeakCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUWeakCache (int maximumCacheSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUWeakCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUWeakCache
argument_list|(
name|maximumCacheSize
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUWeakCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUWeakCache (int initialCapacity, int maximumCacheSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUWeakCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
block|{
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUWeakCache
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUWeakCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @param stopOnEviction   whether to stop service on eviction.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|newLRUWeakCache (int initialCapacity, int maximumCacheSize, boolean stopOnEviction)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLRUWeakCache
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
return|return
name|getInstance
argument_list|()
operator|.
name|createLRUWeakCache
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|,
name|stopOnEviction
argument_list|)
return|;
block|}
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUCache (int maximumCacheSize)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUCache (int maximumCacheSize, Consumer<V> onEvict)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|,
name|Consumer
argument_list|<
name|V
argument_list|>
name|onEvict
parameter_list|)
function_decl|;
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified initial capacity, maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUCache (int initialCapacity, int maximumCacheSize)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Constructs an empty<tt>LRUCache</tt> instance with the      * specified initial capacity, maximumCacheSize,load factor and ordering mode.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @param stopOnEviction   whether to stop service on eviction.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUCache (int initialCapacity, int maximumCacheSize, boolean stopOnEviction)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUCache
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
function_decl|;
comment|/**      * Constructs an empty<tt>LRUSoftCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUSoftCache (int maximumCacheSize)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUSoftCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Constructs an empty<tt>LRUSoftCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUSoftCache (int initialCapacity, int maximumCacheSize)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUSoftCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Constructs an empty<tt>LRUSoftCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @param stopOnEviction   whether to stop service on eviction.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUSoftCache (int initialCapacity, int maximumCacheSize, boolean stopOnEviction)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUSoftCache
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
function_decl|;
comment|/**      * Constructs an empty<tt>LRUWeakCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUWeakCache (int maximumCacheSize)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUWeakCache
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Constructs an empty<tt>LRUWeakCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUWeakCache (int initialCapacity, int maximumCacheSize)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUWeakCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Constructs an empty<tt>LRUWeakCache</tt> instance with the      * specified maximumCacheSize, and will stop on eviction.      *      * @param initialCapacity  the initial capacity.      * @param maximumCacheSize the max capacity.      * @param stopOnEviction   whether to stop service on eviction.      * @throws IllegalArgumentException if the initial capacity is negative      */
DECL|method|createLRUWeakCache (int initialCapacity, int maximumCacheSize, boolean stopOnEviction)
specifier|public
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createLRUWeakCache
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
function_decl|;
block|}
end_class

end_unit

