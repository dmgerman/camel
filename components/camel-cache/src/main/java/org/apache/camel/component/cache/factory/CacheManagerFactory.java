begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache.factory
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
operator|.
name|factory
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
name|CacheManager
import|;
end_import

begin_class
DECL|class|CacheManagerFactory
specifier|public
class|class
name|CacheManagerFactory
block|{
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
DECL|method|instantiateCacheManager ()
specifier|public
specifier|synchronized
name|CacheManager
name|instantiateCacheManager
parameter_list|()
block|{
if|if
condition|(
name|cacheManager
operator|==
literal|null
condition|)
block|{
name|cacheManager
operator|=
name|CacheManager
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|cacheManager
return|;
block|}
block|}
end_class

end_unit

