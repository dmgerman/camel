begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc.aggregationrepository
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
operator|.
name|aggregationrepository
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
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|JdbcAggregateLoadTest
specifier|public
class|class
name|JdbcAggregateLoadTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JdbcAggregateLoadTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SIZE
specifier|private
specifier|static
specifier|final
name|int
name|SIZE
init|=
literal|500
decl_stmt|;
DECL|field|repo
specifier|private
name|JdbcAggregationRepository
name|repo
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|ApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/jdbc/aggregationrepository/JdbcSpringDataSource.xml"
argument_list|)
decl_stmt|;
name|repo
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"repo1"
argument_list|,
name|JdbcAggregationRepository
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
DECL|method|testLoadTestJdbcAggregate ()
specifier|public
name|void
name|testLoadTestJdbcAggregate
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
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|50
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Staring to send "
operator|+
name|SIZE
operator|+
literal|" messages."
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
name|SIZE
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|value
init|=
literal|1
decl_stmt|;
name|char
name|id
init|=
literal|'A'
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending "
operator|+
name|value
operator|+
literal|" with id "
operator|+
name|id
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:start?size="
operator|+
name|SIZE
argument_list|,
name|value
argument_list|,
literal|"id"
argument_list|,
literal|""
operator|+
name|id
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Sending all "
operator|+
name|SIZE
operator|+
literal|" message done. Now waiting for aggregation to complete."
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
literal|"seda:start?size="
operator|+
name|SIZE
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:input?groupSize=500"
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
name|aggregationRepository
argument_list|(
name|repo
argument_list|)
operator|.
name|completionSize
argument_list|(
name|SIZE
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:output?showHeaders=true"
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
name|Integer
name|body1
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Integer
name|body2
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|sum
init|=
name|body1
operator|+
name|body2
decl_stmt|;
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|sum
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

