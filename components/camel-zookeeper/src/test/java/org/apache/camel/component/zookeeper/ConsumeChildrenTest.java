begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|CamelExchangeException
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
name|Message
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
name|NoSuchHeaderException
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
name|component
operator|.
name|zookeeper
operator|.
name|NaturalSortComparator
operator|.
name|Order
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
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|Watcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|data
operator|.
name|Stat
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
DECL|class|ConsumeChildrenTest
specifier|public
class|class
name|ConsumeChildrenTest
extends|extends
name|ZooKeeperTestSupport
block|{
annotation|@
name|Override
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"zookeeper://localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
literal|"/grimm?repeat=true&listChildren=true"
argument_list|)
operator|.
name|sort
argument_list|(
name|body
argument_list|()
argument_list|,
operator|new
name|NaturalSortComparator
argument_list|(
name|Order
operator|.
name|Descending
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:zookeeper-data"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_function
unit|}      @
name|Test
DECL|method|shouldAwaitCreationAndGetDataNotification ()
specifier|public
name|void
name|shouldAwaitCreationAndGetDataNotification
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:zookeeper-data"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|client
operator|.
name|createPersistent
argument_list|(
literal|"/grimm"
argument_list|,
literal|"parent"
argument_list|)
expr_stmt|;
name|client
operator|.
name|create
argument_list|(
literal|"/grimm/hansel"
argument_list|,
literal|"child"
argument_list|)
expr_stmt|;
name|client
operator|.
name|create
argument_list|(
literal|"/grimm/gretel"
argument_list|,
literal|"child"
argument_list|)
expr_stmt|;
name|client
operator|.
name|delete
argument_list|(
literal|"/grimm/hansel"
argument_list|)
expr_stmt|;
name|client
operator|.
name|delete
argument_list|(
literal|"/grimm/gretel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|validateExchangesContainListings
argument_list|(
name|mock
argument_list|,
name|createChildListing
argument_list|()
argument_list|,
name|createChildListing
argument_list|(
literal|"hansel"
argument_list|)
argument_list|,
name|createChildListing
argument_list|(
literal|"hansel"
argument_list|,
literal|"gretel"
argument_list|)
argument_list|,
name|createChildListing
argument_list|(
literal|"gretel"
argument_list|)
argument_list|,
name|createChildListing
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|validateExchangesContainListings (MockEndpoint mock, List<?>... expected)
specifier|private
name|void
name|validateExchangesContainListings
parameter_list|(
name|MockEndpoint
name|mock
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
modifier|...
name|expected
parameter_list|)
throws|throws
name|CamelExchangeException
throws|,
name|NoSuchHeaderException
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Exchange
name|received
range|:
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
control|)
block|{
name|Watcher
operator|.
name|Event
operator|.
name|EventType
name|expectedEvent
decl_stmt|;
if|if
condition|(
name|index
operator|==
literal|0
condition|)
block|{
name|expectedEvent
operator|=
name|Watcher
operator|.
name|Event
operator|.
name|EventType
operator|.
name|NodeCreated
expr_stmt|;
block|}
else|else
block|{
name|expectedEvent
operator|=
name|Watcher
operator|.
name|Event
operator|.
name|EventType
operator|.
name|NodeChildrenChanged
expr_stmt|;
block|}
name|List
argument_list|<
name|?
argument_list|>
name|actual
init|=
name|received
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
index|[
name|index
operator|++
index|]
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedEvent
argument_list|,
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|received
argument_list|,
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_EVENT_TYPE
argument_list|,
name|Watcher
operator|.
name|Event
operator|.
name|EventType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|validateChildrenCountChangesEachTime
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
DECL|method|validateChildrenCountChangesEachTime (MockEndpoint mock)
specifier|protected
name|void
name|validateChildrenCountChangesEachTime
parameter_list|(
name|MockEndpoint
name|mock
parameter_list|)
block|{
name|int
name|lastChildCount
init|=
operator|-
literal|1
decl_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|received
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|received
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Message
name|zkm
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|int
name|childCount
init|=
operator|(
operator|(
name|Stat
operator|)
name|zkm
operator|.
name|getHeader
argument_list|(
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_STATISTICS
argument_list|)
operator|)
operator|.
name|getNumChildren
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
literal|"Num of children did not change"
argument_list|,
name|lastChildCount
argument_list|,
name|childCount
argument_list|)
expr_stmt|;
name|lastChildCount
operator|=
name|childCount
expr_stmt|;
block|}
block|}
end_function

unit|}
end_unit

