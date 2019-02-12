begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|ContextTestSupport
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
name|support
operator|.
name|jndi
operator|.
name|JndiContext
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
DECL|class|NormalizerTest
specifier|public
class|class
name|NormalizerTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendToFirstWhen ()
specifier|public
name|void
name|testSendToFirstWhen
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|employeeBody1
init|=
literal|"<employee><name>Jon</name></employee>"
decl_stmt|;
name|String
name|employeeBody2
init|=
literal|"<employee><name>Hadrian</name></employee>"
decl_stmt|;
name|String
name|employeeBody3
init|=
literal|"<employee><name>Claus</name></employee>"
decl_stmt|;
name|String
name|customerBody
init|=
literal|"<customer name=\"James\"/>"
decl_stmt|;
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
literal|4
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"<person name=\"Jon\"/>"
argument_list|,
literal|"<person name=\"Hadrian\"/>"
argument_list|,
literal|"<person name=\"Claus\"/>"
argument_list|,
literal|"<person name=\"James\"/>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|employeeBody1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|employeeBody2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|employeeBody3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|customerBody
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"normalizer"
argument_list|,
operator|new
name|MyNormalizer
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
comment|// START SNIPPET: example
comment|// we need to normalize two types of incoming messages
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/employee"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:normalizer?method=employeeToPerson"
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/customer"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:normalizer?method=customerToPerson"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
