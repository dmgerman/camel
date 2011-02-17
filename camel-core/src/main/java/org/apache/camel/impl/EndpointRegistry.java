begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|util
operator|.
name|LRUCache
import|;
end_import

begin_comment
comment|/**  * Endpoint registry which is a based on a {@link org.apache.camel.util.LRUCache}  * to keep the last 1000 in an internal cache.  *  * @version   */
end_comment

begin_class
DECL|class|EndpointRegistry
specifier|public
class|class
name|EndpointRegistry
extends|extends
name|LRUCache
argument_list|<
name|EndpointKey
argument_list|,
name|Endpoint
argument_list|>
block|{
DECL|method|EndpointRegistry ()
specifier|public
name|EndpointRegistry
parameter_list|()
block|{
comment|// use a cache size of 1000
name|super
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

