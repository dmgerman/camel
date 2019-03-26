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
name|util
operator|.
name|Map
import|;
end_import

begin_interface
DECL|interface|LRUCache
specifier|public
interface|interface
name|LRUCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|cleanUp ()
name|void
name|cleanUp
parameter_list|()
function_decl|;
DECL|method|resetStatistics ()
name|void
name|resetStatistics
parameter_list|()
function_decl|;
DECL|method|getEvicted ()
name|long
name|getEvicted
parameter_list|()
function_decl|;
DECL|method|getMisses ()
name|long
name|getMisses
parameter_list|()
function_decl|;
DECL|method|getHits ()
name|long
name|getHits
parameter_list|()
function_decl|;
DECL|method|getMaxCacheSize ()
name|int
name|getMaxCacheSize
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

