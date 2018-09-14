begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|spring
operator|.
name|SpringTestSupport
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
name|AbstractXmlApplicationContext
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
comment|/**  * Bean binding unit test.  */
end_comment

begin_class
DECL|class|BeanBindingTest
specifier|public
class|class
name|BeanBindingTest
extends|extends
name|SpringTestSupport
block|{
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/bean/beanBindingTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testBeanBindingUsingBeanExpression ()
specifier|public
name|void
name|testBeanBindingUsingBeanExpression
parameter_list|()
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
literal|2
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Claus"
argument_list|,
literal|"Bye James"
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startBeanExpression"
argument_list|,
literal|"Claus"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startBeanExpression"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanBindingUsingConstantExpression ()
specifier|public
name|void
name|testBeanBindingUsingConstantExpression
parameter_list|()
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
literal|2
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Claus"
argument_list|,
literal|"Bye James"
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startConstantExpression"
argument_list|,
literal|"Claus"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startConstantExpression"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanBindingUsingHeaderExpression ()
specifier|public
name|void
name|testBeanBindingUsingHeaderExpression
parameter_list|()
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
literal|2
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Claus"
argument_list|,
literal|"Bye James"
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:startHeaderExpression"
argument_list|,
literal|"Claus"
argument_list|,
literal|"number"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:startHeaderExpression"
argument_list|,
literal|"James"
argument_list|,
literal|"number"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanBindingUsingManyExpression ()
specifier|public
name|void
name|testBeanBindingUsingManyExpression
parameter_list|()
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
literal|2
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Claus"
argument_list|,
literal|"Bye James"
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|5
operator|*
literal|3
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|5
operator|*
literal|4
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:startMany"
argument_list|,
literal|"Claus"
argument_list|,
literal|"number"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:startMany"
argument_list|,
literal|"James"
argument_list|,
literal|"number"
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

