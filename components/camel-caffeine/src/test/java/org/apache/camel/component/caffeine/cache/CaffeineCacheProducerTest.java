begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.caffeine.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|caffeine
operator|.
name|cache
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|Cache
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|caffeine
operator|.
name|CaffeineConstants
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CaffeineCacheProducerTest
specifier|public
class|class
name|CaffeineCacheProducerTest
extends|extends
name|CaffeineCacheTestSupport
block|{
comment|// ****************************
comment|// Clear
comment|// ****************************
annotation|@
name|Test
DECL|method|testCacheClear ()
specifier|public
name|void
name|testCacheClear
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
operator|(
name|Object
operator|)
literal|null
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION_CLEANUP
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|// ****************************
comment|// Put
comment|// ****************************
annotation|@
name|Test
DECL|method|testCachePut ()
specifier|public
name|void
name|testCachePut
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|generateRandomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|generateRandomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION_PUT
argument_list|)
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
operator|.
name|withBody
argument_list|(
name|val
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getTestCache
argument_list|()
operator|.
name|getIfPresent
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|val
argument_list|,
name|getTestCache
argument_list|()
operator|.
name|getIfPresent
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCachePutAll ()
specifier|public
name|void
name|testCachePutAll
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|generateRandomMapOfString
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION_PUT_ALL
argument_list|)
operator|.
name|withBody
argument_list|(
name|map
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock1
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock1
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock1
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mock1
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|elements
init|=
name|getTestCache
argument_list|()
operator|.
name|getAllPresent
argument_list|(
name|keys
argument_list|)
decl_stmt|;
name|keys
operator|.
name|forEach
argument_list|(
name|k
lambda|->
block|{
name|assertTrue
argument_list|(
name|elements
operator|.
name|containsKey
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|,
name|elements
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|// ****************************
comment|// Get
comment|// ****************************
annotation|@
name|Test
DECL|method|testCacheGet ()
specifier|public
name|void
name|testCacheGet
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getTestCache
argument_list|()
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|generateRandomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|generateRandomString
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION_GET
argument_list|)
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
operator|.
name|withBody
argument_list|(
name|val
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCacheGetAll ()
specifier|public
name|void
name|testCacheGetAll
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getTestCache
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|generateRandomMapOfString
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION_GET_ALL
argument_list|)
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|elements
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|keys
operator|.
name|forEach
argument_list|(
name|k
lambda|->
block|{
name|assertTrue
argument_list|(
name|elements
operator|.
name|containsKey
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|,
name|elements
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// ****************************
comment|// INVALIDATE
comment|// ****************************
annotation|@
name|Test
DECL|method|testCacheInvalidate ()
specifier|public
name|void
name|testCacheInvalidate
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getTestCache
argument_list|()
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|generateRandomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|generateRandomString
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION_INVALIDATE
argument_list|)
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|getIfPresent
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCacheInvalidateAll ()
specifier|public
name|void
name|testCacheInvalidateAll
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getTestCache
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|generateRandomMapOfString
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION_INVALIDATE_ALL
argument_list|)
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|elements
init|=
name|getTestCache
argument_list|()
operator|.
name|getAllPresent
argument_list|(
name|keys
argument_list|)
decl_stmt|;
name|keys
operator|.
name|forEach
argument_list|(
name|k
lambda|->
block|{
name|assertFalse
argument_list|(
name|elements
operator|.
name|containsKey
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStats ()
specifier|public
name|void
name|testStats
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|generateRandomMapOfString
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION
argument_list|,
name|CaffeineConstants
operator|.
name|ACTION_PUT_ALL
argument_list|)
operator|.
name|withBody
argument_list|(
name|map
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock1
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock1
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock1
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_HAS_RESULT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mock1
operator|.
name|expectedHeaderReceived
argument_list|(
name|CaffeineConstants
operator|.
name|ACTION_SUCCEEDED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|elements
init|=
name|getTestCache
argument_list|()
operator|.
name|getAllPresent
argument_list|(
name|keys
argument_list|)
decl_stmt|;
name|keys
operator|.
name|forEach
argument_list|(
name|k
lambda|->
block|{
name|assertTrue
argument_list|(
name|elements
operator|.
name|containsKey
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|,
name|elements
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
comment|// ****************************
comment|// Route
comment|// ****************************
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"caffeine-cache://%s?cache=#cache"
argument_list|,
literal|"test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.caffeine?level=INFO&showAll=true&multiline=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

