begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stax
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stax
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
name|component
operator|.
name|stax
operator|.
name|model
operator|.
name|Record
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
name|stax
operator|.
name|model
operator|.
name|RecordsUtil
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
name|BeforeClass
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
name|stax
operator|.
name|StAXBuilder
operator|.
name|stax
import|;
end_import

begin_class
DECL|class|StAXJAXBIteratorExpressionTest
specifier|public
class|class
name|StAXJAXBIteratorExpressionTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:records"
argument_list|)
DECL|field|recordsEndpoint
specifier|private
name|MockEndpoint
name|recordsEndpoint
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initRouteExample ()
specifier|public
specifier|static
name|void
name|initRouteExample
parameter_list|()
block|{
name|RecordsUtil
operator|.
name|createXMLFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|public
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"file:target/in"
argument_list|)
comment|// split the file using StAX (the stax method is from StAXBuilder)
comment|// and use streaming mode in the splitter
operator|.
name|split
argument_list|(
name|stax
argument_list|(
name|Record
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:records"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testStaxExpression ()
specifier|public
name|void
name|testStaxExpression
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|recordsEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|recordsEndpoint
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Record
operator|.
name|class
argument_list|)
expr_stmt|;
name|recordsEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Record
name|five
init|=
name|recordsEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Record
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"4"
argument_list|,
name|five
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#4"
argument_list|,
name|five
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

