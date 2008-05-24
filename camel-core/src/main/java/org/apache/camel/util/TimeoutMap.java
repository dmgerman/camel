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
comment|/**  * Represents a thread safe map of values which timeout after a period of  * inactivity.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|TimeoutMap
specifier|public
interface|interface
name|TimeoutMap
extends|extends
name|Runnable
block|{
comment|/**      * Looks up the value in the map by the given key.      *      * @param key the key of the value to search for      * @return the value for the given key or null if it is not present (or has timed out)      */
DECL|method|get (Object key)
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**      * Returns a copy of the keys in the map      */
DECL|method|getKeys ()
name|Object
index|[]
name|getKeys
parameter_list|()
function_decl|;
comment|/**      * Returns the size of the map      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Adds the key value pair into the map such that some time after the given      * timeout the entry will be evicted      */
DECL|method|put (Object key, Object value, long timeoutMillis)
name|void
name|put
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|long
name|timeoutMillis
parameter_list|)
function_decl|;
comment|/**      * Removes the object with the given key      *      * @param key  key for the object to remove      */
DECL|method|remove (Object key)
name|void
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**      * Purges any old entries from the map      */
DECL|method|purge ()
name|void
name|purge
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

