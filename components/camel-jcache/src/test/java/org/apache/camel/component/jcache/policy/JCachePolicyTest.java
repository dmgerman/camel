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
name|concurrent
operator|.
name|TimeUnit
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
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|CompleteConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|MutableConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|expiry
operator|.
name|CreatedExpiryPolicy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|expiry
operator|.
name|Duration
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JCachePolicyTest
specifier|public
class|class
name|JCachePolicyTest
extends|extends
name|JCachePolicyTestBase
block|{
comment|//Set cache - this use cases is also covered by tests in JCachePolicyProcessorTest
annotation|@
name|Test
DECL|method|testSetCache ()
specifier|public
name|void
name|testSetCache
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
comment|//Send exchange
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:policy-cache"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//Verify the set cache was used
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|lookupCache
argument_list|(
literal|"setCache"
argument_list|)
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Set cacheManager, cacheName, cacheConfiguration
annotation|@
name|Test
DECL|method|testSetManagerNameConfiguration ()
specifier|public
name|void
name|testSetManagerNameConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
comment|//Send exchange
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:policy-manager-name-configuration"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//Verify cache was created with the set configuration and used in route
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"setManagerNameConfiguration"
argument_list|)
decl_stmt|;
name|CompleteConfiguration
name|completeConfiguration
init|=
operator|(
name|CompleteConfiguration
operator|)
name|cache
operator|.
name|getConfiguration
argument_list|(
name|CompleteConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|completeConfiguration
operator|.
name|getKeyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|completeConfiguration
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CreatedExpiryPolicy
operator|.
name|factoryOf
argument_list|(
operator|new
name|Duration
argument_list|(
name|TimeUnit
operator|.
name|MINUTES
argument_list|,
literal|60
argument_list|)
argument_list|)
argument_list|,
name|completeConfiguration
operator|.
name|getExpiryPolicyFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Set cacheManager, cacheName
annotation|@
name|Test
DECL|method|testSetManagerName ()
specifier|public
name|void
name|testSetManagerName
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
comment|//Send exchange
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:policy-manager-name"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//Verify the cache was created with the name and used
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|lookupCache
argument_list|(
literal|"setManagerName"
argument_list|)
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Set cacheManager, cacheName - cache already exists
annotation|@
name|Test
DECL|method|testSetManagerNameExists ()
specifier|public
name|void
name|testSetManagerNameExists
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
comment|//Send exchange
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:policy-manager-name-exists"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//Verify the existing cache with name was used
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|lookupCache
argument_list|(
literal|"setManagerNameExists"
argument_list|)
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dummy"
argument_list|,
name|lookupCache
argument_list|(
literal|"setManagerNameExists"
argument_list|)
operator|.
name|get
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Set cacheManager, cacheConfiguration
annotation|@
name|Test
DECL|method|testSetManagerConfiguration ()
specifier|public
name|void
name|testSetManagerConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
comment|//Send exchange
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:policy-manager-configuration"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//Verify the cache was created with routeId and configuration
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"direct-policy-manager-configuration"
argument_list|)
decl_stmt|;
name|CompleteConfiguration
name|completeConfiguration
init|=
operator|(
name|CompleteConfiguration
operator|)
name|cache
operator|.
name|getConfiguration
argument_list|(
name|CompleteConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|completeConfiguration
operator|.
name|getKeyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|completeConfiguration
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CreatedExpiryPolicy
operator|.
name|factoryOf
argument_list|(
operator|new
name|Duration
argument_list|(
name|TimeUnit
operator|.
name|MINUTES
argument_list|,
literal|61
argument_list|)
argument_list|)
argument_list|,
name|completeConfiguration
operator|.
name|getExpiryPolicyFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Set cacheName - use CachingProvider to lookup CacheManager
annotation|@
name|Test
DECL|method|testDefaultCacheManager ()
specifier|public
name|void
name|testDefaultCacheManager
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
comment|//Send exchange
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:policy-default-manager"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//Verify the default cacheManager was used, despite it was not set
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"contextCacheManager"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Not enabled
annotation|@
name|Test
DECL|method|testNotEnabled ()
specifier|public
name|void
name|testNotEnabled
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
comment|//Send exchange
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:policy-not-enabled"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//Verify the default cacheManager was used, despite it was not set
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"notEnabled"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
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
name|MutableConfiguration
name|configuration
decl_stmt|;
comment|//Set cache
name|Cache
name|cache
init|=
name|cacheManager
operator|.
name|createCache
argument_list|(
literal|"setCache"
argument_list|,
operator|new
name|MutableConfiguration
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
name|JCachePolicy
name|jcachePolicy
init|=
operator|new
name|JCachePolicy
argument_list|()
decl_stmt|;
name|jcachePolicy
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:policy-cache"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Set cacheManager, cacheName, cacheConfiguration
name|configuration
operator|=
operator|new
name|MutableConfiguration
argument_list|<>
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setTypes
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setExpiryPolicyFactory
argument_list|(
name|CreatedExpiryPolicy
operator|.
name|factoryOf
argument_list|(
operator|new
name|Duration
argument_list|(
name|TimeUnit
operator|.
name|MINUTES
argument_list|,
literal|60
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheManager
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheName
argument_list|(
literal|"setManagerNameConfiguration"
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:policy-manager-name-configuration"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Set cacheManager, cacheName
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheManager
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheName
argument_list|(
literal|"setManagerName"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:policy-manager-name"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Set cacheManager, cacheName - cache already exists
name|cache
operator|=
name|cacheManager
operator|.
name|createCache
argument_list|(
literal|"setManagerNameExists"
argument_list|,
operator|new
name|MutableConfiguration
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"test"
argument_list|,
literal|"dummy"
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheManager
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheName
argument_list|(
literal|"setManagerNameExists"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:policy-manager-name-exists"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Set cacheManager, cacheConfiguration
name|configuration
operator|=
operator|new
name|MutableConfiguration
argument_list|<>
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setTypes
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setExpiryPolicyFactory
argument_list|(
name|CreatedExpiryPolicy
operator|.
name|factoryOf
argument_list|(
operator|new
name|Duration
argument_list|(
name|TimeUnit
operator|.
name|MINUTES
argument_list|,
literal|61
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheManager
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:policy-manager-configuration"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"direct-policy-manager-configuration"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Set cacheName - use CachingProvider to lookup CacheManager
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|.
name|setCacheName
argument_list|(
literal|"contextCacheManager"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:policy-default-manager"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Not enabled
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|cache
operator|=
name|cacheManager
operator|.
name|createCache
argument_list|(
literal|"notEnabled"
argument_list|,
operator|new
name|MutableConfiguration
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:policy-not-enabled"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

