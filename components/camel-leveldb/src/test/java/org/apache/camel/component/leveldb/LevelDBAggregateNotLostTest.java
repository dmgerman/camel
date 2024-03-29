begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.leveldb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|leveldb
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AggregationStrategy
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
name|fusesource
operator|.
name|hawtbuf
operator|.
name|Buffer
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|leveldb
operator|.
name|LevelDBAggregationRepository
operator|.
name|keyBuilder
import|;
end_import

begin_class
DECL|class|LevelDBAggregateNotLostTest
specifier|public
class|class
name|LevelDBAggregateNotLostTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|repo
specifier|private
name|LevelDBAggregationRepository
name|repo
decl_stmt|;
annotation|@
name|Override
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
name|deleteDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
name|repo
operator|=
operator|new
name|LevelDBAggregationRepository
argument_list|(
literal|"repo1"
argument_list|,
literal|"target/data/leveldb.dat"
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
DECL|method|testLevelDBAggregateNotLost ()
specifier|public
name|void
name|testLevelDBAggregateNotLost
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"ABCDE"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"D"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"E"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|String
name|exchangeId
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
comment|// the exchange should be in the completed repo where we should be able to find it
specifier|final
name|LevelDBFile
name|levelDBFile
init|=
name|repo
operator|.
name|getLevelDBFile
argument_list|()
decl_stmt|;
specifier|final
name|LevelDBCamelCodec
name|codec
init|=
operator|new
name|LevelDBCamelCodec
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bf
init|=
name|levelDBFile
operator|.
name|getDb
argument_list|()
operator|.
name|get
argument_list|(
name|keyBuilder
argument_list|(
literal|"repo1-completed"
argument_list|,
name|exchangeId
argument_list|)
argument_list|)
decl_stmt|;
comment|// assert the exchange was not lost and we got all the information still
name|assertNotNull
argument_list|(
name|bf
argument_list|)
expr_stmt|;
name|Exchange
name|completed
init|=
name|codec
operator|.
name|unmarshallExchange
argument_list|(
name|context
argument_list|,
operator|new
name|Buffer
argument_list|(
name|bf
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|completed
argument_list|)
expr_stmt|;
comment|// should retain the exchange id
name|assertEquals
argument_list|(
name|exchangeId
argument_list|,
name|completed
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABCDE"
argument_list|,
name|completed
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|completed
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"size"
argument_list|,
name|completed
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|completed
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|)
argument_list|)
expr_stmt|;
comment|// will store correlation keys as String
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|completed
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_CORRELATION_KEY
argument_list|)
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
literal|"direct:start"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"id"
argument_list|)
argument_list|,
operator|new
name|MyAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|5
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|repo
argument_list|)
operator|.
name|log
argument_list|(
literal|"aggregated exchange id ${exchangeId} with ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:aggregated"
argument_list|)
comment|// throw an exception to fail, which we then will loose this message
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyAggregationStrategy
specifier|public
specifier|static
class|class
name|MyAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
name|String
name|body1
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|body2
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body1
operator|+
name|body2
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
block|}
block|}
end_class

end_unit

