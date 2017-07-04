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

begin_comment
comment|/**  * A cache that uses a near optional LRU Cache using {@link java.lang.ref.WeakReference}.  *<p/>  * The Cache is implemented by Caffeine which provides an<a href="https://github.com/ben-manes/caffeine/wiki/Efficiency">efficient cache</a>.  *<p/>  * This implementation uses {@link java.lang.ref.WeakReference} for stored values in the cache, to support the JVM  * when it wants to reclaim objects for example during garbage collection. Therefore this implementation does  * not support<b>all</b> the {@link java.util.Map} methods.  *<p/>  * The following methods is<b>only</b> be be used:  *<ul>  *<li>containsKey - To determine if the key is in the cache and refers to a value</li>  *<li>entrySet - To return a set of all the entries (as key/value paris)</li>  *<li>get - To get a value from the cache</li>  *<li>isEmpty - To determine if the cache contains any values</li>  *<li>keySet - To return a set of the current keys which refers to a value</li>  *<li>put - To add a value to the cache</li>  *<li>putAll - To add values to the cache</li>  *<li>remove - To remove a value from the cache by its key</li>  *<li>size - To get the current size</li>  *<li>values - To return a copy of all the value in a list</li>  *</ul>  *<p/>  * The {@link #containsValue(Object)} method should<b>not</b> be used as it's not adjusted to check  * for the existence of a value without catering for the soft references.  *<p/>  * Notice that if the JVM reclaim memory the content of this cache may be garbage collected, without any  * eviction notifications.  *<p/>  * Use {@link LRUCacheFactory} to create a new instance (do not use the constructor).  *  * @see LRUCacheFactory  * @see LRUCache  * @see LRUSoftCache  */
end_comment

begin_class
DECL|class|LRUWeakCache
specifier|public
class|class
name|LRUWeakCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|LRUCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|LRUWeakCache (int maximumCacheSize)
specifier|public
name|LRUWeakCache
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
block|}
DECL|method|LRUWeakCache (int initialCapacity, int maximumCacheSize)
specifier|public
name|LRUWeakCache
parameter_list|(
name|int
name|initialCapacity
parameter_list|,
name|int
name|maximumCacheSize
parameter_list|)
block|{
name|super
argument_list|(
name|initialCapacity
argument_list|,
name|maximumCacheSize
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
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
literal|"LRUWeakCache@"
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

