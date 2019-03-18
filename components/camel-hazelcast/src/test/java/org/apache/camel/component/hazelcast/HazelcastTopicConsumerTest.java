begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ITopic
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
name|Message
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
name|MessageListener
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
name|Captor
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
name|ArgumentMatchers
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
DECL|class|HazelcastTopicConsumerTest
specifier|public
class|class
name|HazelcastTopicConsumerTest
extends|extends
name|HazelcastCamelTestSupport
block|{
annotation|@
name|Mock
DECL|field|topic
specifier|private
name|ITopic
argument_list|<
name|String
argument_list|>
name|topic
decl_stmt|;
annotation|@
name|Captor
DECL|field|argument
specifier|private
name|ArgumentCaptor
argument_list|<
name|MessageListener
argument_list|<
name|String
argument_list|>
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
expr|<
name|String
operator|>
name|getTopic
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|topic
operator|.
name|addMessageListener
argument_list|(
name|any
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
name|getTopic
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|topic
argument_list|)
operator|.
name|addMessageListener
argument_list|(
name|any
argument_list|(
name|MessageListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|receive ()
specifier|public
name|void
name|receive
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|out
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:received"
argument_list|)
decl_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|topic
argument_list|)
operator|.
name|addMessageListener
argument_list|(
name|argument
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Message
argument_list|<
name|String
argument_list|>
name|msg
init|=
operator|new
name|Message
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|"foo"
argument_list|,
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|onMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|2000
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
name|RECEIVED
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
literal|"hazelcast-%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|TOPIC_PREFIX
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
name|RECEIVED
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"...received"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:received"
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
literal|null
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

