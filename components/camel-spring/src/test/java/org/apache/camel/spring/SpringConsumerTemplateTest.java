begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|ConsumerTemplate
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
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_comment
comment|/**  * Unit test using the ConsumerTemplate  *  * @version   */
end_comment

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|SpringConsumerTemplateTest
specifier|public
class|class
name|SpringConsumerTemplateTest
extends|extends
name|SpringRunWithTestSupport
block|{
annotation|@
name|Autowired
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|Autowired
DECL|field|consumer
specifier|private
name|ConsumerTemplate
name|consumer
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|ref
operator|=
literal|"result"
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Test
DECL|method|testConsumeTemplate ()
specifier|public
name|void
name|testConsumeTemplate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we expect Hello World received in our mock endpoint
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// we use the producer template to send a message to the seda:start endpoint
name|producer
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// we consume the body from seda:start
name|String
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:start"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// and then we send the body again to seda:foo so it will be routed to the mock
comment|// endpoint so our unit test can complete
name|producer
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// assert mock received the body
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

