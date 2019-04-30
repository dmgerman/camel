begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ironmq.integrationtest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ironmq
operator|.
name|integrationtest
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
name|ProducerTemplate
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
name|ironmq
operator|.
name|IronMQConstants
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
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"Integration test that requires ironmq account."
argument_list|)
DECL|class|IronMQFIFOTest
specifier|public
class|class
name|IronMQFIFOTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|projectId
specifier|private
name|String
name|projectId
init|=
literal|"replace-this"
decl_stmt|;
DECL|field|token
specifier|private
name|String
name|token
init|=
literal|"replace-this"
decl_stmt|;
DECL|field|ironMQEndpoint
specifier|private
specifier|final
name|String
name|ironMQEndpoint
init|=
literal|"ironmq:testqueue?projectId="
operator|+
name|projectId
operator|+
literal|"&token="
operator|+
name|token
operator|+
literal|"&maxMessagesPerPoll=20&ironMQCloud=https://mq-v3-aws-us-east-1.iron.io"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Before
DECL|method|clearQueue ()
specifier|public
name|void
name|clearQueue
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ironMQEndpoint
argument_list|,
literal|"fo"
argument_list|,
name|IronMQConstants
operator|.
name|OPERATION
argument_list|,
name|IronMQConstants
operator|.
name|CLEARQUEUE
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|50
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|ironMQEndpoint
argument_list|,
literal|"<foo>"
operator|+
name|i
operator|+
literal|"</foo>"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testIronMQFifo ()
specifier|public
name|void
name|testIronMQFifo
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|setExpectedMessageCount
argument_list|(
literal|50
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
name|int
name|i
init|=
literal|1
decl_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|result
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchanges
control|)
block|{
name|assertEquals
argument_list|(
literal|"<foo>"
operator|+
name|i
operator|+
literal|"</foo>"
argument_list|,
name|exchange
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
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|ironMQEndpoint
argument_list|)
operator|.
name|log
argument_list|(
literal|"got message ${body}"
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

