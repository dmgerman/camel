begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.redis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|redis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|impl
operator|.
name|JndiRegistry
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
name|Before
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
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|connection
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|connection
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|listener
operator|.
name|ChannelTopic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|listener
operator|.
name|RedisMessageListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|listener
operator|.
name|Topic
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
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
name|mock
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

begin_class
DECL|class|RedisConsumerTest
specifier|public
class|class
name|RedisConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|listenerContainer
specifier|private
name|RedisMessageListenerContainer
name|listenerContainer
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"listenerContainer"
argument_list|,
name|listenerContainer
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|listenerContainer
operator|=
name|mock
argument_list|(
name|RedisMessageListenerContainer
operator|.
name|class
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|registerConsumerForTwoChannelTopics ()
specifier|public
name|void
name|registerConsumerForTwoChannelTopics
parameter_list|()
throws|throws
name|Exception
block|{
name|ArgumentCaptor
argument_list|<
name|Collection
argument_list|>
name|collectionCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Collection
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|listenerContainer
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
argument_list|,
name|collectionCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ChannelTopic
argument_list|>
name|topics
init|=
name|collectionCaptor
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ChannelTopic
argument_list|>
name|topicIterator
init|=
name|topics
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Topic
name|firstTopic
init|=
name|topicIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Topic
name|twoTopic
init|=
name|topicIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|firstTopic
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|twoTopic
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|consumerReceivesMessages ()
specifier|public
name|void
name|consumerReceivesMessages
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
literal|2
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|MessageListener
argument_list|>
name|messageListenerCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|MessageListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|listenerContainer
argument_list|)
operator|.
name|addMessageListener
argument_list|(
name|messageListenerCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|any
argument_list|(
name|Collection
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|MessageListener
name|messageListener
init|=
name|messageListenerCaptor
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|messageListener
operator|.
name|onMessage
argument_list|(
operator|new
name|DefaultMessage
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|messageListener
operator|.
name|onMessage
argument_list|(
operator|new
name|DefaultMessage
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
literal|"spring-redis://localhost:6379?command=SUBSCRIBE&channels=one,two&listenerContainer=#listenerContainer"
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

