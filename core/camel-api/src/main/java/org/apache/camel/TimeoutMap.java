begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Represents a map of values which timeout after a period of inactivity.  */
end_comment

begin_interface
DECL|interface|TimeoutMap
specifier|public
interface|interface
name|TimeoutMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Service
block|{
comment|/**      * Looks up the value in the map by the given key.      *      * @param key the key of the value to search for      * @return the value for the given key or<tt>null</tt> if it is not present (or has timed out)      */
DECL|method|get (K key)
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**      * Returns the size of the map      *      * @return the size      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Adds the key value pair into the map such that some time after the given      * timeout the entry will be evicted      *      * @param key   the key      * @param value the value      * @param timeoutMillis  timeout in millis      * @return the previous value associated with<tt>key</tt>, or      *<tt>null</tt> if there was no mapping for<tt>key</tt>.      */
DECL|method|put (K key, V value, long timeoutMillis)
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|long
name|timeoutMillis
parameter_list|)
function_decl|;
comment|/**      * Adds the key value pair into the map if the specified key is not already associated with a value      * such that some time after the given timeout the entry will be evicted. Expiry time of an existing mapping      * is left unchanged.      *      * @param key   the key      * @param value the value      * @param timeoutMillis  timeout in millis      * @return the value associated with<tt>key</tt>, or      *<tt>null</tt> if there was no mapping for<tt>key</tt>.      */
DECL|method|putIfAbsent (K key, V value, long timeoutMillis)
name|V
name|putIfAbsent
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|long
name|timeoutMillis
parameter_list|)
function_decl|;
comment|/**      * Removes the object with the given key      *      * @param key  key for the object to remove      * @return the value for the given key or<tt>null</tt> if it is not present (or has timed out)      */
DECL|method|remove (K key)
name|V
name|remove
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**      * Assign the (singular) {@link Listener}      *      * @param listener the new listener      */
DECL|method|addListener (Listener<K, V> listener)
name|void
name|addListener
parameter_list|(
name|Listener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|listener
parameter_list|)
function_decl|;
annotation|@
name|FunctionalInterface
DECL|interface|Listener
interface|interface
name|Listener
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
DECL|enum|Type
DECL|enumConstant|Put
DECL|enumConstant|Remove
DECL|enumConstant|Evict
enum|enum
name|Type
block|{
name|Put
block|,
name|Remove
block|,
name|Evict
block|}
comment|/**          * Callback when the map changes content          *          * @param key the item key          * @param value the item value          */
DECL|method|timeoutMapEvent (Type type, K key, V value)
name|void
name|timeoutMapEvent
parameter_list|(
name|Type
name|type
parameter_list|,
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

