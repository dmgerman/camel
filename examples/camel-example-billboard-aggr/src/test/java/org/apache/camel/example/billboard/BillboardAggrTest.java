begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.billboard
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|billboard
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|model
operator|.
name|dataformat
operator|.
name|BindyType
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
DECL|class|BillboardAggrTest
specifier|public
class|class
name|BillboardAggrTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|basePath
specifier|private
specifier|final
specifier|static
name|String
name|basePath
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
operator|+
literal|"/target/test-classes/data"
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
name|CamelContext
name|ctx
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
return|return
name|ctx
return|;
block|}
annotation|@
name|Override
DECL|method|getShutdownTimeout ()
specifier|protected
name|int
name|getShutdownTimeout
parameter_list|()
block|{
return|return
literal|300
return|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|top20
init|=
operator|(
operator|(
name|MyAggregationStrategy
operator|)
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
literal|"myAggregation"
argument_list|)
operator|)
operator|.
name|getTop20Artists
argument_list|()
decl_stmt|;
name|top20
operator|.
name|forEach
argument_list|(
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
name|log
operator|.
name|info
argument_list|(
literal|"{}, {}"
argument_list|,
name|k
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|top20
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|35
argument_list|,
operator|(
name|int
operator|)
name|top20
operator|.
name|get
argument_list|(
literal|"madonna"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|26
argument_list|,
operator|(
name|int
operator|)
name|top20
operator|.
name|get
argument_list|(
literal|"elton john"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|17
argument_list|,
operator|(
name|int
operator|)
name|top20
operator|.
name|get
argument_list|(
literal|"the beatles"
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
literal|"file:"
operator|+
name|basePath
operator|+
literal|"?noop=true"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
comment|// skip first line with headers
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|simple
argument_list|(
literal|"${property.CamelSplitIndex}> 0"
argument_list|)
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|unmarshal
argument_list|()
operator|.
name|bindy
argument_list|(
name|BindyType
operator|.
name|Csv
argument_list|,
name|SongRecord
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:aggregate"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
comment|// malformed record trace
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"${property.CamelSplitIndex}:${body}"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:"
operator|+
name|basePath
operator|+
literal|"?fileName=waste.log&fileExist=append"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"seda:aggregate?concurrentConsumers=10"
argument_list|)
operator|.
name|bean
argument_list|(
name|MyAggregationStrategy
operator|.
name|class
argument_list|,
literal|"setArtistHeader"
argument_list|)
operator|.
name|aggregate
argument_list|(
operator|new
name|MyAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|header
argument_list|(
literal|"artist"
argument_list|)
operator|.
name|completionPredicate
argument_list|(
name|header
argument_list|(
literal|"CamelSplitComplete"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
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
DECL|class|MyAggregationStrategy
specifier|public
specifier|static
class|class
name|MyAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|field|map
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
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
name|Message
name|newIn
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|artist
init|=
operator|(
name|String
operator|)
name|newIn
operator|.
name|getHeader
argument_list|(
literal|"artist"
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
name|artist
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|artist
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|artist
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|artist
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
name|newIn
operator|.
name|setHeader
argument_list|(
literal|"myAggregation"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|newExchange
return|;
block|}
DECL|method|setArtistHeader (Exchange exchange, SongRecord song)
specifier|public
name|void
name|setArtistHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SongRecord
name|song
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"artist"
argument_list|,
name|song
operator|.
name|getArtist
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getTop20Artists ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|getTop20Artists
parameter_list|()
block|{
return|return
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|(
operator|(
name|Map
operator|.
name|Entry
operator|.
expr|<
name|String
operator|,
name|Integer
operator|>
name|comparingByValue
argument_list|()
operator|.
name|reversed
argument_list|()
operator|)
argument_list|)
operator|.
name|limit
argument_list|(
literal|20
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|Map
operator|.
name|Entry
operator|::
name|getKey
argument_list|,
name|Map
operator|.
name|Entry
operator|::
name|getValue
argument_list|,
parameter_list|(
name|e1
parameter_list|,
name|e2
parameter_list|)
lambda|->
name|e1
argument_list|,
name|LinkedHashMap
operator|::
operator|new
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

