begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CamelContext
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
name|ContextTestSupport
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
name|Exchange
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
DECL|class|EndpointRegistryKeepRouteEndpointsTest
specifier|public
class|class
name|EndpointRegistryKeepRouteEndpointsTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|MAXIMUM_ENDPOINT_CACHE_SIZE
argument_list|,
literal|"20"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testEndpointRegistryKeepRouteEndpoints ()
specifier|public
name|void
name|testEndpointRegistryKeepRouteEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"direct://start"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
comment|// we dont have this endpoint yet
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://unknown0"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:unknown"
operator|+
name|i
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
comment|// the eviction is async so force cleanup
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
comment|// endpoints from routes is always kept in the cache
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"direct://start"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
comment|// and the dynamic cache only keeps 20 dynamic endpoints
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50
condition|;
name|i
operator|++
control|)
block|{
name|String
name|uri
init|=
literal|"mock://unknown"
operator|+
name|i
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|hasEndpoint
argument_list|(
name|uri
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|count
operator|++
expr_stmt|;
comment|// and it should be dynamic
name|assertTrue
argument_list|(
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|isDynamic
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"Should only be 20 dynamic endpoints in the cache"
argument_list|,
literal|20
argument_list|,
name|count
argument_list|)
expr_stmt|;
comment|// we should have 4 static, 20 dynamic and 24 in total
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|staticSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|isStatic
argument_list|(
literal|"direct://start"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|dynamicSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|24
argument_list|,
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// and we can browse all 24
name|assertEquals
argument_list|(
literal|24
argument_list|,
name|context
operator|.
name|getEndpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// and if we purge only the dynamic is removed
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|purge
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|staticSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|dynamicSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|context
operator|.
name|getEndpointRegistry
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// endpoints from routes is always kept in the cache
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"direct://start"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
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

