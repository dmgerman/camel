begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.caffeine.loadcache
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
name|loadcache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
DECL|class|CaffeineLoadCacheProducerMultiOperationSameCacheTest
specifier|public
class|class
name|CaffeineLoadCacheProducerMultiOperationSameCacheTest
extends|extends
name|CaffeineLoadCacheTestSupport
block|{
annotation|@
name|Test
DECL|method|testSameCachePutAndGet ()
specifier|public
name|void
name|testSameCachePutAndGet
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
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"1"
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
literal|true
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
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|mock1
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
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
literal|"caffeine-loadcache://%s?cache=#cache&action=PUT&key=1"
argument_list|,
literal|"test"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"caffeine-loadcache://%s?cache=#cache&key=1&action=GET"
argument_list|,
literal|"test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.caffeine?level=INFO&showAll=true&multiline=true"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Test! ${body}"
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

