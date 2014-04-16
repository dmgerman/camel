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
name|net
operator|.
name|InetSocketAddress
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
name|Cluster
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
name|HazelcastInstance
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
name|IList
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
name|Member
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
name|MembershipEvent
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
name|MembershipListener
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

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentCaptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|HazelcastInstanceConsumerTest
specifier|public
class|class
name|HazelcastInstanceConsumerTest
extends|extends
name|HazelcastCamelTestSupport
block|{
annotation|@
name|Mock
DECL|field|list
specifier|private
name|IList
argument_list|<
name|String
argument_list|>
name|list
decl_stmt|;
annotation|@
name|Mock
DECL|field|cluster
specifier|private
name|Cluster
name|cluster
decl_stmt|;
annotation|@
name|Mock
DECL|field|member
specifier|private
name|Member
name|member
decl_stmt|;
DECL|field|argument
specifier|private
name|ArgumentCaptor
argument_list|<
name|MembershipListener
argument_list|>
name|argument
decl_stmt|;
annotation|@
name|Override
DECL|method|trainHazelcastInstance (HazelcastInstance hazelcastInstance)
specifier|protected
name|void
name|trainHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|when
argument_list|(
name|hazelcastInstance
operator|.
name|getCluster
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|cluster
argument_list|)
expr_stmt|;
name|argument
operator|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|MembershipListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|cluster
operator|.
name|addMembershipListener
argument_list|(
name|argument
operator|.
name|capture
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|verifyHazelcastInstance (HazelcastInstance hazelcastInstance)
specifier|protected
name|void
name|verifyHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|verify
argument_list|(
name|hazelcastInstance
argument_list|)
operator|.
name|getCluster
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|cluster
argument_list|)
operator|.
name|addMembershipListener
argument_list|(
name|any
argument_list|(
name|MembershipListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddInstance ()
specifier|public
name|void
name|testAddInstance
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|added
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:added"
argument_list|)
decl_stmt|;
name|added
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|member
operator|.
name|getInetSocketAddress
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
literal|"foo.bar"
argument_list|,
literal|12345
argument_list|)
argument_list|)
expr_stmt|;
name|MembershipEvent
name|event
init|=
operator|new
name|MembershipEvent
argument_list|(
name|cluster
argument_list|,
name|member
argument_list|,
name|MembershipEvent
operator|.
name|MEMBER_ADDED
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|memberAdded
argument_list|(
name|event
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
comment|// check headers
name|Exchange
name|ex
init|=
name|added
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|this
operator|.
name|checkHeaders
argument_list|(
name|headers
argument_list|,
name|HazelcastConstants
operator|.
name|ADDED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveInstance ()
specifier|public
name|void
name|testRemoveInstance
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|removed
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:removed"
argument_list|)
decl_stmt|;
name|removed
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|member
operator|.
name|getInetSocketAddress
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
literal|"foo.bar"
argument_list|,
literal|12345
argument_list|)
argument_list|)
expr_stmt|;
name|MembershipEvent
name|event
init|=
operator|new
name|MembershipEvent
argument_list|(
name|cluster
argument_list|,
name|member
argument_list|,
name|MembershipEvent
operator|.
name|MEMBER_REMOVED
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|memberRemoved
argument_list|(
name|event
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
comment|// check headers
name|Exchange
name|ex
init|=
name|removed
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|this
operator|.
name|checkHeaders
argument_list|(
name|headers
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
literal|"hazelcast:%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|INSTANCE_PREFIX
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"instance..."
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
name|otherwise
argument_list|()
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
expr_stmt|;
block|}
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
name|INSTANCE_LISTENER
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
name|assertNotNull
argument_list|(
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|INSTANCE_HOST
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
name|INSTANCE_PORT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

