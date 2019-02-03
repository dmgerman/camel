begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcache
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Caching
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|JCachePolicyTestBase
specifier|public
class|class
name|JCachePolicyTestBase
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
comment|//reset mock
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|whenAnyExchangeReceived
argument_list|(
parameter_list|(
name|e
parameter_list|)
lambda|->
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|generateValue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|randomString ()
specifier|protected
name|String
name|randomString
parameter_list|()
block|{
return|return
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|lookupCache (String cacheName)
specifier|protected
name|Cache
name|lookupCache
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
comment|//This will also open a closed cache
return|return
name|Caching
operator|.
name|getCachingProvider
argument_list|()
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
return|;
block|}
DECL|method|generateValue (String key)
specifier|public
specifier|static
name|String
name|generateValue
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
literal|"value-"
operator|+
name|key
return|;
block|}
annotation|@
name|After
DECL|method|after ()
specifier|public
name|void
name|after
parameter_list|()
block|{
comment|//The RouteBuilder code is called for every test, so we destroy cache after each test
name|CacheManager
name|cacheManager
init|=
name|Caching
operator|.
name|getCachingProvider
argument_list|()
operator|.
name|getCacheManager
argument_list|()
decl_stmt|;
name|cacheManager
operator|.
name|getCacheNames
argument_list|()
operator|.
name|forEach
argument_list|(
parameter_list|(
name|s
parameter_list|)
lambda|->
name|cacheManager
operator|.
name|destroyCache
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
name|Caching
operator|.
name|getCachingProvider
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

