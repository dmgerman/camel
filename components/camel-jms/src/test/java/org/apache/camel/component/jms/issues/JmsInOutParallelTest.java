begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|issues
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|jms
operator|.
name|CamelJmsTestHelper
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
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_class
DECL|class|JmsInOutParallelTest
specifier|public
class|class
name|JmsInOutParallelTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testInOutParallel ()
specifier|public
name|void
name|testInOutParallel
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:received"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setAssertPeriod
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|String
name|outPayload
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:test"
argument_list|,
literal|"test"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fully done"
argument_list|,
name|outPayload
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"1,2,3,4,5"
argument_list|)
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"activemq:queue:test1?requestTimeout=2000"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|inOut
argument_list|(
literal|"activemq:queue:test2?requestTimeout=2000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:received"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Fully done"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"Finished"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:test1"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Received on queue test1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:test2"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Received on queue test2"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Some reply"
argument_list|)
argument_list|)
operator|.
name|delay
argument_list|(
name|constant
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

