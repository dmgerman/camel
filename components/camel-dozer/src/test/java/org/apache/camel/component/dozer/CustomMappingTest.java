begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
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
name|Produce
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
name|dozer
operator|.
name|example
operator|.
name|abc
operator|.
name|ABCOrder
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
name|dozer
operator|.
name|example
operator|.
name|abc
operator|.
name|ABCOrder
operator|.
name|Header
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
name|dozer
operator|.
name|example
operator|.
name|xyz
operator|.
name|XYZOrder
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
name|After
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|SpringJUnit4ClassRunner
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
DECL|class|CustomMappingTest
specifier|public
class|class
name|CustomMappingTest
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|startEndpoint
specifier|private
name|ProducerTemplate
name|startEndpoint
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomMapping ()
specifier|public
name|void
name|testCustomMapping
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|ABCOrder
name|abcOrder
init|=
operator|new
name|ABCOrder
argument_list|()
decl_stmt|;
name|abcOrder
operator|.
name|setHeader
argument_list|(
operator|new
name|Header
argument_list|()
argument_list|)
expr_stmt|;
name|abcOrder
operator|.
name|getHeader
argument_list|()
operator|.
name|setStatus
argument_list|(
literal|"GOLD"
argument_list|)
expr_stmt|;
name|abcOrder
operator|.
name|getHeader
argument_list|()
operator|.
name|setCustomerNum
argument_list|(
literal|"ACME"
argument_list|)
expr_stmt|;
name|startEndpoint
operator|.
name|sendBody
argument_list|(
name|abcOrder
argument_list|)
expr_stmt|;
comment|// check results
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|XYZOrder
name|result
init|=
name|resultEndpoint
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
name|XYZOrder
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|result
operator|.
name|getPriority
argument_list|()
argument_list|,
literal|"custom2:GOLD"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|result
operator|.
name|getCustId
argument_list|()
argument_list|,
literal|"mapCustomer:ACME"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

