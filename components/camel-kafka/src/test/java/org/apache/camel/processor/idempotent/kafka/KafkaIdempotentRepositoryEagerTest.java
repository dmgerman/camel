begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|kafka
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
name|CamelExecutionException
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
name|EndpointInject
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
name|RoutesBuilder
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
name|kafka
operator|.
name|embedded
operator|.
name|EmbeddedKafkaBroker
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
name|kafka
operator|.
name|embedded
operator|.
name|EmbeddedZookeeper
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
name|DefaultCamelContext
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
name|SimpleRegistry
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
name|Rule
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

begin_comment
comment|/**  * @author jkorab  */
end_comment

begin_class
DECL|class|KafkaIdempotentRepositoryEagerTest
specifier|public
class|class
name|KafkaIdempotentRepositoryEagerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Rule
DECL|field|zookeeper
specifier|public
name|EmbeddedZookeeper
name|zookeeper
init|=
operator|new
name|EmbeddedZookeeper
argument_list|()
decl_stmt|;
annotation|@
name|Rule
DECL|field|kafkaBroker
specifier|public
name|EmbeddedKafkaBroker
name|kafkaBroker
init|=
operator|new
name|EmbeddedKafkaBroker
argument_list|(
literal|0
argument_list|,
name|zookeeper
operator|.
name|getConnection
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|kafkaIdempotentRepository
specifier|private
name|KafkaIdempotentRepository
name|kafkaIdempotentRepository
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:out"
argument_list|)
DECL|field|mockOut
specifier|private
name|MockEndpoint
name|mockOut
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:before"
argument_list|)
DECL|field|mockBefore
specifier|private
name|MockEndpoint
name|mockBefore
decl_stmt|;
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
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|kafkaIdempotentRepository
operator|=
operator|new
name|KafkaIdempotentRepository
argument_list|(
literal|"TEST_IDEM"
argument_list|,
name|kafkaBroker
operator|.
name|getBrokerList
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"kafkaIdempotentRepository"
argument_list|,
name|kafkaIdempotentRepository
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|header
argument_list|(
literal|"id"
argument_list|)
argument_list|)
operator|.
name|messageIdRepositoryRef
argument_list|(
literal|"kafkaIdempotentRepository"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testRemovesDuplicates ()
specifier|public
name|void
name|testRemovesDuplicates
parameter_list|()
throws|throws
name|InterruptedException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Test message"
argument_list|,
literal|"id"
argument_list|,
name|i
operator|%
literal|5
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|kafkaIdempotentRepository
operator|.
name|getDuplicateCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|mockOut
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|mockBefore
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRollsBackOnException ()
specifier|public
name|void
name|testRollsBackOnException
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|mockOut
operator|.
name|whenAnyExchangeReceived
argument_list|(
name|exchange
lambda|->
block|{
name|int
name|id
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Boom!"
argument_list|)
throw|;
block|}
block|}
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Test message"
argument_list|,
literal|"id"
argument_list|,
name|i
operator|%
literal|5
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|cex
parameter_list|)
block|{
comment|// no-op; expected
block|}
block|}
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|kafkaIdempotentRepository
operator|.
name|getDuplicateCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// id{0} is not a duplicate
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|mockOut
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
comment|// id{0} goes through the idempotency check twice
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|mockBefore
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClear ()
specifier|public
name|void
name|testClear
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|mockOut
operator|.
name|setExpectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Test message"
argument_list|,
literal|"id"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|kafkaIdempotentRepository
operator|.
name|clear
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Test message"
argument_list|,
literal|"id"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

