begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|model
operator|.
name|language
operator|.
name|XPathExpression
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

begin_comment
comment|/**  * Partial operations test.  */
end_comment

begin_class
DECL|class|SpringDataFormatPartialTest
specifier|public
class|class
name|SpringDataFormatPartialTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testPartialMarshal ()
specifier|public
name|void
name|testPartialMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|bean
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Beer"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAmount
argument_list|(
literal|23
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|2.5
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:marshal"
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
name|XPathExpression
name|xpath
init|=
operator|new
name|XPathExpression
argument_list|(
literal|"count(//*[namespace-uri() = 'http://example.camel.org/apache' and local-name() = 'po']) = 1"
argument_list|)
decl_stmt|;
name|xpath
operator|.
name|setResultType
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|matches
argument_list|(
name|xpath
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|//To make sure there is no XML declaration.
name|assertFalse
argument_list|(
literal|"There should have no XML declaration."
argument_list|,
name|mock
operator|.
name|getExchanges
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
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"<?xml version="
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPartialUnmarshal ()
specifier|public
name|void
name|testPartialUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:unmarshal"
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
name|Partial
name|partial
init|=
operator|new
name|Partial
argument_list|()
decl_stmt|;
name|partial
operator|.
name|setName
argument_list|(
literal|"mock"
argument_list|)
expr_stmt|;
name|partial
operator|.
name|setLocation
argument_list|(
literal|"org.apache.camel"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodyReceived
argument_list|()
operator|.
name|constant
argument_list|(
name|partial
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
literal|"<Partial name=\"mock\"><location>org.apache.camel</location></Partial>"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|xml
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
literal|"org/apache/camel/example/springDataFormatPartial.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

