begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

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
name|Endpoint
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
name|StaticService
import|;
end_import

begin_comment
comment|/**  * Registry to cache endpoints in memory.  *<p/>  * The registry contains two caches:  *<ul>  *<li>static - which keeps all the endpoints in the cache for the entire lifecycle</li>  *<li>dynamic - which keeps the endpoints in a {@link org.apache.camel.support.LRUCache} and may evict endpoints which hasn't been requested recently</li>  *</ul>  * The static cache stores all the endpoints that are created as part of setting up and starting routes.  * The static cache has no upper limit.  *<p/>  * The dynamic cache stores the endpoints that are created and used ad-hoc, such as from custom Java code that creates new endpoints etc.  * The dynamic cache has an upper limit, that by default is 1000 entries.  *  * @param<K> endpoint key  */
end_comment

begin_interface
DECL|interface|EndpointRegistry
specifier|public
interface|interface
name|EndpointRegistry
parameter_list|<
name|K
parameter_list|>
extends|extends
name|Map
argument_list|<
name|K
argument_list|,
name|Endpoint
argument_list|>
extends|,
name|StaticService
block|{
comment|/**      * Number of endpoints in the static registry.      */
DECL|method|staticSize ()
name|int
name|staticSize
parameter_list|()
function_decl|;
comment|/**      * Number of endpoints in the dynamic registry      */
DECL|method|dynamicSize ()
name|int
name|dynamicSize
parameter_list|()
function_decl|;
comment|/**      * Maximum number of entries to store in the dynamic registry      */
DECL|method|getMaximumCacheSize ()
name|int
name|getMaximumCacheSize
parameter_list|()
function_decl|;
comment|/**      * Purges the cache (removes endpoints from the dynamic cache)      */
DECL|method|purge ()
name|void
name|purge
parameter_list|()
function_decl|;
comment|/**      * Whether the given endpoint is stored in the static cache      *      * @param key  the endpoint key      * @return<tt>true</tt> if in static cache,<tt>false</tt> if not      */
DECL|method|isStatic (String key)
name|boolean
name|isStatic
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Whether the given endpoint is stored in the dynamic cache      *      * @param key  the endpoint key      * @return<tt>true</tt> if in dynamic cache,<tt>false</tt> if not      */
DECL|method|isDynamic (String key)
name|boolean
name|isDynamic
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Cleanup the cache (purging stale entries)      */
DECL|method|cleanUp ()
name|void
name|cleanUp
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

