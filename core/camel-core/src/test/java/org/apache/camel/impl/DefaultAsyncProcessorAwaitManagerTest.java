begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|CountDownLatch
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
name|MessageHistory
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
name|NamedNode
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
name|spi
operator|.
name|AsyncProcessorAwaitManager
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
name|spi
operator|.
name|MessageHistoryFactory
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
name|support
operator|.
name|DefaultExchange
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
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsNull
operator|.
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|DefaultAsyncProcessorAwaitManagerTest
specifier|public
class|class
name|DefaultAsyncProcessorAwaitManagerTest
block|{
DECL|field|MESSAGE_HISTORY_FACTORY
specifier|private
specifier|static
specifier|final
name|MessageHistoryFactory
name|MESSAGE_HISTORY_FACTORY
init|=
operator|new
name|DefaultMessageHistoryFactory
argument_list|()
decl_stmt|;
DECL|field|defaultAsyncProcessorAwaitManager
specifier|private
name|DefaultAsyncProcessorAwaitManager
name|defaultAsyncProcessorAwaitManager
decl_stmt|;
DECL|field|exchange
specifier|private
name|DefaultExchange
name|exchange
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
decl_stmt|;
DECL|field|thread
specifier|private
name|Thread
name|thread
decl_stmt|;
annotation|@
name|Test
DECL|method|testNoMessageHistory ()
specifier|public
name|void
name|testNoMessageHistory
parameter_list|()
throws|throws
name|Exception
block|{
name|startAsyncProcess
argument_list|()
expr_stmt|;
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
name|awaitThread
init|=
name|defaultAsyncProcessorAwaitManager
operator|.
name|browse
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getRouteId
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getNodeId
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|waitForEndOfAsyncProcess
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageHistoryWithEmptyList ()
specifier|public
name|void
name|testMessageHistoryWithEmptyList
parameter_list|()
throws|throws
name|Exception
block|{
name|startAsyncProcess
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
operator|new
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
name|awaitThread
init|=
name|defaultAsyncProcessorAwaitManager
operator|.
name|browse
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getRouteId
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getNodeId
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|waitForEndOfAsyncProcess
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageHistoryWithNullMessageHistory ()
specifier|public
name|void
name|testMessageHistoryWithNullMessageHistory
parameter_list|()
throws|throws
name|Exception
block|{
name|startAsyncProcess
argument_list|()
expr_stmt|;
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
name|messageHistories
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|messageHistories
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
name|messageHistories
argument_list|)
expr_stmt|;
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
name|awaitThread
init|=
name|defaultAsyncProcessorAwaitManager
operator|.
name|browse
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getRouteId
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getNodeId
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|waitForEndOfAsyncProcess
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageHistoryWithNullElements ()
specifier|public
name|void
name|testMessageHistoryWithNullElements
parameter_list|()
throws|throws
name|Exception
block|{
name|startAsyncProcess
argument_list|()
expr_stmt|;
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
name|messageHistories
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|messageHistories
operator|.
name|add
argument_list|(
name|MESSAGE_HISTORY_FACTORY
operator|.
name|newMessageHistory
argument_list|(
literal|null
argument_list|,
operator|new
name|MockNamedNode
argument_list|()
operator|.
name|withId
argument_list|(
literal|null
argument_list|)
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
name|messageHistories
argument_list|)
expr_stmt|;
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
name|awaitThread
init|=
name|defaultAsyncProcessorAwaitManager
operator|.
name|browse
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getRouteId
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getNodeId
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|waitForEndOfAsyncProcess
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageHistoryWithNotNullElements ()
specifier|public
name|void
name|testMessageHistoryWithNotNullElements
parameter_list|()
throws|throws
name|Exception
block|{
name|startAsyncProcess
argument_list|()
expr_stmt|;
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
name|messageHistories
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|messageHistories
operator|.
name|add
argument_list|(
name|MESSAGE_HISTORY_FACTORY
operator|.
name|newMessageHistory
argument_list|(
literal|"routeId"
argument_list|,
operator|new
name|MockNamedNode
argument_list|()
operator|.
name|withId
argument_list|(
literal|"nodeId"
argument_list|)
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
name|messageHistories
argument_list|)
expr_stmt|;
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
name|awaitThread
init|=
name|defaultAsyncProcessorAwaitManager
operator|.
name|browse
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getRouteId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"routeId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|awaitThread
operator|.
name|getNodeId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"nodeId"
argument_list|)
argument_list|)
expr_stmt|;
name|waitForEndOfAsyncProcess
argument_list|()
expr_stmt|;
block|}
DECL|method|waitForEndOfAsyncProcess ()
specifier|private
name|void
name|waitForEndOfAsyncProcess
parameter_list|()
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
while|while
condition|(
name|thread
operator|.
name|isAlive
argument_list|()
condition|)
block|{         }
block|}
DECL|method|startAsyncProcess ()
specifier|private
name|void
name|startAsyncProcess
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|defaultAsyncProcessorAwaitManager
operator|=
operator|new
name|DefaultAsyncProcessorAwaitManager
argument_list|()
expr_stmt|;
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|BackgroundAwait
name|backgroundAwait
init|=
operator|new
name|BackgroundAwait
argument_list|()
decl_stmt|;
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|thread
operator|=
operator|new
name|Thread
argument_list|(
name|backgroundAwait
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
DECL|class|BackgroundAwait
specifier|private
class|class
name|BackgroundAwait
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|defaultAsyncProcessorAwaitManager
operator|.
name|await
argument_list|(
name|exchange
argument_list|,
name|latch
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MockNamedNode
specifier|private
specifier|static
class|class
name|MockNamedNode
implements|implements
name|NamedNode
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getDescriptionText ()
specifier|public
name|String
name|getDescriptionText
parameter_list|()
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
return|;
block|}
DECL|method|withId (String id)
specifier|public
name|MockNamedNode
name|withId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit
