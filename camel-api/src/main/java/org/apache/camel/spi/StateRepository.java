begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * This {@link StateRepository} holds a set of key/value pairs for defining a particular<em>state</em> of a component. For instance it can be a set of indexes.  *<p/>  * An {@link IdempotentRepository} behaves more or less like a {@code Set} whereas this {@link StateRepository} behaves like a {@code Map}.  *  * @param<K> Key type  * @param<V> Value type  */
end_comment

begin_interface
DECL|interface|StateRepository
specifier|public
interface|interface
name|StateRepository
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Service
block|{
comment|/**      * Sets the state value for the given key.      *      * @param key State key      * @param value State value      */
DECL|method|setState (K key, V value)
name|void
name|setState
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
comment|/**      * Gets the state value for the given key. It returns {@code null} if the key is unknown.      *      * @param key State key      * @return State value or null the key is unknown      */
DECL|method|getState (K key)
name|V
name|getState
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

