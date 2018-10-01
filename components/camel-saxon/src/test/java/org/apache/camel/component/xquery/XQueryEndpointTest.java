begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
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
name|spring
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
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|XQueryEndpointTest
specifier|public
class|class
name|XQueryEndpointTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendMessageAndHaveItTransformed ()
specifier|public
name|void
name|testSendMessageAndHaveItTransformed
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<mail><subject>Hey</subject><body>Hello world!</body></mail>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
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
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The transformed XML should not be null"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"transformed"
argument_list|,
literal|"<transformed subject=\"Hey\"><mail><subject>Hey</subject>"
operator|+
literal|"<body>Hello world!</body></mail></transformed>"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|TestBean
name|bean
init|=
name|getMandatoryBean
argument_list|(
name|TestBean
operator|.
name|class
argument_list|,
literal|"testBean"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bean.subject"
argument_list|,
literal|"Hey"
argument_list|,
name|bean
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/xquery/camelContext.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

