begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CamelSpringTestSupport
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
name|support
operator|.
name|AbstractApplicationContext
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

begin_comment
comment|/**  * Unit test for issues CAMEL-1034 and CAMEL-1037  */
end_comment

begin_class
DECL|class|JmsResequencerTest
specifier|public
class|class
name|JmsResequencerTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/jms/issues/JmsResequencerTest-context.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testBatchResequencer ()
specifier|public
name|void
name|testBatchResequencer
parameter_list|()
throws|throws
name|Exception
block|{
name|testResequencer
argument_list|(
literal|"activemq:queue:in1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStreamResequencer ()
specifier|public
name|void
name|testStreamResequencer
parameter_list|()
throws|throws
name|Exception
block|{
name|testResequencer
argument_list|(
literal|"activemq:queue:in2"
argument_list|)
expr_stmt|;
block|}
DECL|method|testResequencer (String endpoint)
specifier|private
name|void
name|testResequencer
parameter_list|(
name|String
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|100
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
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|message
argument_list|(
name|i
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|100
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|endpoint
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
argument_list|,
literal|"num"
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

