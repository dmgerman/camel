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
comment|/**  * Registry to store endpoints in a map like cache.  *  * @param<K> endpoint key  */
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
comment|/**      * Number of static endpoints in the registry.      */
DECL|method|staticSize ()
name|int
name|staticSize
parameter_list|()
function_decl|;
comment|/**      * Number of dynamic endpoints in the registry      */
DECL|method|dynamicSize ()
name|int
name|dynamicSize
parameter_list|()
function_decl|;
comment|/**      * Maximum number of entries to store in the dynamic cache      */
DECL|method|getMaximumCacheSize ()
specifier|public
name|int
name|getMaximumCacheSize
parameter_list|()
function_decl|;
comment|/**      * Purges the cache (removes dynamic endpoints)      */
DECL|method|purge ()
name|void
name|purge
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

