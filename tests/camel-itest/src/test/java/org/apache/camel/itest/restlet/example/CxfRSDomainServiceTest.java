begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.restlet.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|restlet
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
name|junit
operator|.
name|Assert
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
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CxfRSDomainServiceTest
specifier|public
class|class
name|CxfRSDomainServiceTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Autowired
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testAddDomainString ()
specifier|public
name|void
name|testAddDomainString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|input
init|=
literal|"<checkDomainRequest><id>123</id><name>www.google.com</name><username>test</username><password>test</password></checkDomainRequest>"
decl_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"cxfrs:http://localhost:9000/domainservice/domains"
argument_list|,
name|input
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Should contains response"
argument_list|,
name|response
operator|.
name|endsWith
argument_list|(
literal|"<CheckDomainResponse><requestId>123</requestId><responseBody>OK</responseBody></CheckDomainResponse>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddDomainStringObject ()
specifier|public
name|void
name|testAddDomainStringObject
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|input
init|=
literal|"<checkDomainRequest><id>123</id><name>www.google.com</name><username>test</username><password>test</password></checkDomainRequest>"
decl_stmt|;
name|CheckDomainAvailabilityRestResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"cxfrs:http://localhost:9000/domainservice/domains"
argument_list|,
name|input
argument_list|,
name|CheckDomainAvailabilityRestResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|response
operator|.
name|getRequestId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"OK"
argument_list|,
name|response
operator|.
name|getResponseBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddDomainBothObjects ()
specifier|public
name|void
name|testAddDomainBothObjects
parameter_list|()
throws|throws
name|Exception
block|{
name|CheckDomainRequest
name|input
init|=
operator|new
name|CheckDomainRequest
argument_list|()
decl_stmt|;
name|input
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|input
operator|.
name|setName
argument_list|(
literal|"www.google.com"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setUsername
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|input
operator|.
name|setPassword
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|CheckDomainAvailabilityRestResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"cxfrs:http://localhost:9000/domainservice/domains"
argument_list|,
name|input
argument_list|,
name|CheckDomainAvailabilityRestResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|response
operator|.
name|getRequestId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"OK"
argument_list|,
name|response
operator|.
name|getResponseBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testGetDomain ()
specifier|public
name|void
name|testGetDomain
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: Must make CXF-RS easier to use
name|Object
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"cxfrs:http://localhost:9000/domainservice/domains/123"
argument_list|,
literal|null
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"{www.google.com}"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

