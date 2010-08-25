begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|store
operator|.
name|MemoryStoreEvictionPolicy
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
name|Converter
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|CacheConverter
specifier|public
specifier|final
class|class
name|CacheConverter
block|{
DECL|method|CacheConverter ()
specifier|private
name|CacheConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toPolicy (String s)
specifier|public
specifier|static
name|MemoryStoreEvictionPolicy
name|toPolicy
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|s
operator|=
name|s
operator|.
name|replace
argument_list|(
literal|"MemoryStoreEvictionPolicy."
argument_list|,
literal|""
argument_list|)
expr_stmt|;
return|return
name|MemoryStoreEvictionPolicy
operator|.
name|fromString
argument_list|(
name|s
argument_list|)
return|;
block|}
block|}
end_class

end_unit

