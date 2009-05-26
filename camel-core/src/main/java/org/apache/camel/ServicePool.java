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
comment|/**  * A service pool is like a connection pool.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ServicePool
specifier|public
interface|interface
name|ServicePool
parameter_list|<
name|Key
parameter_list|,
name|Service
parameter_list|>
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Service
block|{
comment|/**      * Acquires the given service. If absent in pool the service      * is added to the pool.      *      * @param key the key      * @param service the service      * @return the acquired service, is newer<tt>null</tt>      */
DECL|method|acquireIfAbsent (Key key, Service service)
name|Service
name|acquireIfAbsent
parameter_list|(
name|Key
name|key
parameter_list|,
name|Service
name|service
parameter_list|)
function_decl|;
comment|/**      * Tries to acquire the servie with the given key      * @param key the key      * @return the acquired service, or<tt>null</tt> if no free in pool      */
DECL|method|acquire (Key key)
name|Service
name|acquire
parameter_list|(
name|Key
name|key
parameter_list|)
function_decl|;
comment|/**      * Releases the service back to the pool      *      * @param key  the key      * @param service the service      */
DECL|method|release (Key key, Service service)
name|void
name|release
parameter_list|(
name|Key
name|key
parameter_list|,
name|Service
name|service
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

