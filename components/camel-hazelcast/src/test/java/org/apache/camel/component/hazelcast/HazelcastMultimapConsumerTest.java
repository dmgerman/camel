begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
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
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|Hazelcast
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|MultiMap
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
name|Test
import|;
end_import

begin_class
DECL|class|HazelcastMultimapConsumerTest
specifier|public
class|class
name|HazelcastMultimapConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|map
specifier|private
name|MultiMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|Hazelcast
operator|.
name|getMultiMap
argument_list|(
literal|"mm"
argument_list|)
expr_stmt|;
name|this
operator|.
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|this
operator|.
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAdd ()
specifier|public
name|void
name|testAdd
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|out
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:added"
argument_list|)
decl_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|this
operator|.
name|checkHeaders
argument_list|(
name|out
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
name|getHeaders
argument_list|()
argument_list|,
name|HazelcastConstants
operator|.
name|ADDED
argument_list|)
expr_stmt|;
block|}
comment|/*      * mail from talip (hazelcast) on 21.02.2011: MultiMap doesn't support eviction yet. We can and should add this feature.      *       * we leave the test in our code an set the result to asserted by default.      */
annotation|@
name|Test
DECL|method|testEnvict ()
specifier|public
name|void
name|testEnvict
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|out
init|=
name|super
operator|.
name|getMockEndpoint
argument_list|(
literal|"mock:envicted"
argument_list|)
decl_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
literal|"my-foo-1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"2"
argument_list|,
literal|"my-foo-2"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"3"
argument_list|,
literal|"my-foo-3"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"4"
argument_list|,
literal|"my-foo-4"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"5"
argument_list|,
literal|"my-foo-5"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"6"
argument_list|,
literal|"my-foo-6"
argument_list|)
expr_stmt|;
comment|// assertMockEndpointsSatisfied(30000, TimeUnit.MILLISECONDS);
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|out
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:removed"
argument_list|)
decl_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|remove
argument_list|(
literal|"4711"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|this
operator|.
name|checkHeaders
argument_list|(
name|out
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
name|getHeaders
argument_list|()
argument_list|,
name|HazelcastConstants
operator|.
name|REMOVED
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
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%smm"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"object..."
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_ACTION
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HazelcastConstants
operator|.
name|ADDED
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"...added"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:added"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_ACTION
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HazelcastConstants
operator|.
name|ENVICTED
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"...envicted"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:envicted"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_ACTION
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HazelcastConstants
operator|.
name|REMOVED
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"...removed"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:removed"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|log
argument_list|(
literal|"fail!"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
return|;
block|}
DECL|method|checkHeaders (Map<String, Object> headers, String action)
specifier|private
name|void
name|checkHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|action
argument_list|,
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_ACTION
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HazelcastConstants
operator|.
name|CACHE_LISTENER
argument_list|,
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"4711"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|LISTENER_TIME
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

