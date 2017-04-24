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
name|EntryEvent
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
name|EntryEventType
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
name|EntryListener
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
name|Matchers
operator|.
name|eq
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
DECL|class|HazelcastMultimapConsumerTest
specifier|public
class|class
name|HazelcastMultimapConsumerTest
extends|extends
name|HazelcastCamelTestSupport
block|{
annotation|@
name|Mock
DECL|field|map
specifier|private
name|MultiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
decl_stmt|;
DECL|field|argument
specifier|private
name|ArgumentCaptor
argument_list|<
name|EntryListener
argument_list|>
name|argument
decl_stmt|;
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|getMultiMap
argument_list|(
literal|"mm"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|argument
operator|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|EntryListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|map
operator|.
name|addEntryListener
argument_list|(
name|argument
operator|.
name|capture
argument_list|()
argument_list|,
name|eq
argument_list|(
literal|true
argument_list|)
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|getMultiMap
argument_list|(
literal|"mm"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|addEntryListener
argument_list|(
name|any
argument_list|(
name|EntryListener
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
init|=
operator|new
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
name|EntryEventType
operator|.
name|ADDED
operator|.
name|getType
argument_list|()
argument_list|,
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
decl_stmt|;
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|entryAdded
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
comment|/*      * mail from talip (hazelcast) on 21.02.2011: MultiMap doesn't support eviction yet. We can and should add this feature.      */
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testEvict ()
specifier|public
name|void
name|testEvict
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|out
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:evicted"
argument_list|)
decl_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
init|=
operator|new
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
name|EntryEventType
operator|.
name|EVICTED
operator|.
name|getType
argument_list|()
argument_list|,
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
decl_stmt|;
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|entryEvicted
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|30000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
init|=
operator|new
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|,
name|EntryEventType
operator|.
name|REMOVED
operator|.
name|getType
argument_list|()
argument_list|,
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
decl_stmt|;
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|entryRemoved
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
literal|"hazelcast-%smm"
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
name|EVICTED
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"...evicted"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:evicted"
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

