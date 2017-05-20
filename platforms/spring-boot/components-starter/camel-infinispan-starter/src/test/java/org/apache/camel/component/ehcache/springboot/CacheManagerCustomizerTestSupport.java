begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ehcache
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|RemoteCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|manager
operator|.
name|DefaultCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|manager
operator|.
name|EmbeddedCacheManager
import|;
end_import

begin_class
DECL|class|CacheManagerCustomizerTestSupport
specifier|public
specifier|final
class|class
name|CacheManagerCustomizerTestSupport
block|{
DECL|method|CacheManagerCustomizerTestSupport ()
specifier|private
name|CacheManagerCustomizerTestSupport
parameter_list|()
block|{     }
DECL|method|newEmbeddedCacheManagerInstance ()
specifier|public
specifier|static
name|EmbeddedCacheManager
name|newEmbeddedCacheManagerInstance
parameter_list|()
block|{
return|return
operator|new
name|DefaultCacheManager
argument_list|(
operator|new
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|global
operator|.
name|GlobalConfigurationBuilder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|,
operator|new
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|cache
operator|.
name|ConfigurationBuilder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|newRemoteCacheManagerInstance ()
specifier|public
specifier|static
name|RemoteCacheManager
name|newRemoteCacheManagerInstance
parameter_list|()
block|{
return|return
operator|new
name|RemoteCacheManager
argument_list|(
operator|new
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|configuration
operator|.
name|ConfigurationBuilder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

