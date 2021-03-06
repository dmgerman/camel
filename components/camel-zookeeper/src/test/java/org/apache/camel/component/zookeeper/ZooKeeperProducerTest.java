begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ExchangePattern
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
name|operations
operator|.
name|GetChildrenOperation
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
name|CreateMode
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

begin_import
import|import static
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
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_CREATE_MODE
import|;
end_import

begin_import
import|import static
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
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_NODE
import|;
end_import

begin_import
import|import static
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
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_OPERATION
import|;
end_import

begin_class
DECL|class|ZooKeeperProducerTest
specifier|public
class|class
name|ZooKeeperProducerTest
extends|extends
name|ZooKeeperTestSupport
block|{
DECL|field|zookeeperUri
specifier|private
name|String
name|zookeeperUri
decl_stmt|;
DECL|field|testPayload
specifier|private
name|String
name|testPayload
init|=
literal|"TestPayload"
decl_stmt|;
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
name|zookeeperUri
operator|=
literal|"zookeeper://localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
literal|"/node?create=true"
expr_stmt|;
name|from
argument_list|(
literal|"direct:roundtrip"
argument_list|)
operator|.
name|to
argument_list|(
name|zookeeperUri
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:producer-out"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|zookeeperUri
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumed-from-node"
argument_list|)
expr_stmt|;
block|}
block|}
operator|,
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
literal|"direct:no-create-fails-set"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zookeeper://localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
literal|"/doesnotexist"
argument_list|)
expr_stmt|;
block|}
block|}
operator|,
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
literal|"direct:node-from-header"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zookeeper://localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
literal|"/notset?create=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"zookeeper://localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
literal|"/set?create=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:consumed-from-set-node"
argument_list|)
expr_stmt|;
block|}
block|}
operator|,
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
literal|"direct:create-mode"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zookeeper://localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
literal|"/persistent?create=true&createMode=PERSISTENT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:create-mode"
argument_list|)
expr_stmt|;
block|}
block|}
operator|,
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
literal|"direct:delete"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zookeeper://localhost:39913/to-be-deleted"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:delete"
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
DECL|method|testRoundtripOfDataToAndFromZnode ()
specifier|public
name|void
name|testRoundtripOfDataToAndFromZnode
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumed-from-node"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|pipeline
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:producer-out"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|pipeline
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|createExchangeWithBody
argument_list|(
name|testPayload
argument_list|)
decl_stmt|;
name|e
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:roundtrip"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testAsyncRoundtripOfDataToAndFromZnode ()
specifier|public
name|void
name|testAsyncRoundtripOfDataToAndFromZnode
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumed-from-node"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|createExchangeWithBody
argument_list|(
name|testPayload
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:roundtrip"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|setUsingCreateModeFromHeader ()
specifier|public
name|void
name|setUsingCreateModeFromHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|createPersistent
argument_list|(
literal|"/modes-test"
argument_list|,
literal|"parent for modes"
argument_list|)
expr_stmt|;
for|for
control|(
name|CreateMode
name|mode
range|:
name|CreateMode
operator|.
name|values
argument_list|()
control|)
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
name|testPayload
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZOOKEEPER_CREATE_MODE
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZOOKEEPER_NODE
argument_list|,
literal|"/modes-test/"
operator|+
name|mode
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:node-from-header"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|GetChildrenOperation
name|listing
init|=
operator|new
name|GetChildrenOperation
argument_list|(
name|getConnection
argument_list|()
argument_list|,
literal|"/modes-test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CreateMode
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|,
name|listing
operator|.
name|get
argument_list|()
operator|.
name|getResult
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|createWithOtherCreateMode ()
specifier|public
name|void
name|createWithOtherCreateMode
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:create-mode"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|createExchangeWithBody
argument_list|(
name|testPayload
argument_list|)
decl_stmt|;
name|e
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:create-mode"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Stat
name|s
init|=
name|mock
operator|.
name|getReceivedExchanges
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
name|getHeader
argument_list|(
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_STATISTICS
argument_list|,
name|Stat
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|s
operator|.
name|getEphemeralOwner
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|deleteNode ()
specifier|public
name|void
name|deleteNode
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:delete"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|client
operator|.
name|createPersistent
argument_list|(
literal|"/to-be-deleted"
argument_list|,
literal|"to be deleted"
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|createExchangeWithBody
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|e
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZOOKEEPER_OPERATION
argument_list|,
literal|"DELETE"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:delete"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|client
operator|.
name|getConnection
argument_list|()
operator|.
name|exists
argument_list|(
literal|"/to-be-deleted"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|setAndGetListing ()
specifier|public
name|void
name|setAndGetListing
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|createPersistent
argument_list|(
literal|"/set-listing"
argument_list|,
literal|"parent for set and list test"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
name|testPayload
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZOOKEEPER_NODE
argument_list|,
literal|"/set-listing/firstborn"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"zookeeper://localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
literal|"/set-listing?create=true&listChildren=true"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|children
init|=
name|exchange
operator|.
name|getOut
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
literal|1
argument_list|,
name|children
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"firstborn"
argument_list|,
name|children
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testZookeeperMessage ()
specifier|public
name|void
name|testZookeeperMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:consumed-from-node"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
name|testPayload
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:roundtrip"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Message
name|received
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/node"
argument_list|,
name|ZooKeeperMessage
operator|.
name|getPath
argument_list|(
name|received
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ZooKeeperMessage
operator|.
name|getStatistics
argument_list|(
name|received
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

