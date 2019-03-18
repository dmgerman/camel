begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|tx
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
name|itest
operator|.
name|ITestSupport
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
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_comment
comment|/**  * Unit test will look for the spring .xml file with the same class name  * but postfixed with -config.xml as filename.  *<p/>  * We use Spring Testing for unit test, eg we extend AbstractJUnit4SpringContextTests  * that is a Spring class.  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
annotation|@
name|DirtiesContext
argument_list|(
name|classMode
operator|=
name|DirtiesContext
operator|.
name|ClassMode
operator|.
name|AFTER_EACH_TEST_METHOD
argument_list|)
DECL|class|Jms2RequiresNewTest
specifier|public
class|class
name|Jms2RequiresNewTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|PORT3
specifier|private
specifier|static
specifier|final
name|int
name|PORT3
init|=
name|ITestSupport
operator|.
name|getPort3
argument_list|()
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result1"
argument_list|)
DECL|field|result1
specifier|private
name|MockEndpoint
name|result1
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result2"
argument_list|)
DECL|field|result2
specifier|private
name|MockEndpoint
name|result2
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:dlq"
argument_list|)
DECL|field|dlq
specifier|private
name|MockEndpoint
name|dlq
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|start
specifier|private
name|ProducerTemplate
name|start
decl_stmt|;
annotation|@
name|Before
DECL|method|setUpRoute ()
specifier|public
name|void
name|setUpRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|addRoutes
argument_list|(
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
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|markRollbackOnly
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRES_NEW"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:start"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:result1"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRES_NEW"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:result2"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRES_NEW"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:ActiveMQ.DLQ"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRES_NEW"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dlq"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:start"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRES_NEW"
argument_list|)
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:result1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:route2"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Neverland"
argument_list|)
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|RuntimeException
argument_list|(
literal|"Expected!"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:route2"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRES_NEW"
argument_list|)
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:result2"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendThrowingException ()
specifier|public
name|void
name|testSendThrowingException
parameter_list|()
throws|throws
name|Exception
block|{
name|result1
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|result2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|dlq
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|start
operator|.
name|sendBody
argument_list|(
literal|"Single ticket to Neverland please!"
argument_list|)
expr_stmt|;
name|result2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|dlq
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|result1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSend ()
specifier|public
name|void
name|testSend
parameter_list|()
throws|throws
name|Exception
block|{
name|result1
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|dlq
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|start
operator|.
name|sendBody
argument_list|(
literal|"Piotr Klimczak"
argument_list|)
expr_stmt|;
name|result1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|result2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|dlq
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

