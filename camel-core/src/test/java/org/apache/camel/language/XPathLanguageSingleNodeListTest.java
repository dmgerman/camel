begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
name|CamelExecutionException
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
name|NoTypeConversionAvailableException
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
name|RuntimeCamelException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests new converters added to XmlConverters to make Camel intelligent when needing to convert   * a NodeList of length 1 into a Document or a Node.  *  */
end_comment

begin_class
DECL|class|XPathLanguageSingleNodeListTest
specifier|public
class|class
name|XPathLanguageSingleNodeListTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|XML_INPUT_SINGLE
specifier|private
specifier|static
specifier|final
name|String
name|XML_INPUT_SINGLE
init|=
literal|"<root><name>Raul</name><surname>Kripalani</surname></root>"
decl_stmt|;
DECL|field|XML_INPUT_MULTIPLE
specifier|private
specifier|static
specifier|final
name|String
name|XML_INPUT_MULTIPLE
init|=
literal|"<root><name>Raul</name><name>Raul</name><surname>Kripalani</surname></root>"
decl_stmt|;
comment|/**      * A single node XPath selection that internally returns a DTMNodeList of length 1 can now be automatically      * converted to a Document/Node.      * @throws Exception      */
annotation|@
name|Test
DECL|method|testSingleNodeList ()
specifier|public
name|void
name|testSingleNodeList
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:found"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:found"
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:notfound"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:notfound"
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:doTest"
argument_list|,
name|XML_INPUT_SINGLE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Regression test to ensure that a NodeList of length> 1 is not processed by the new converters.      * @throws Exception      */
annotation|@
name|Test
DECL|method|testMultipleNodeList ()
specifier|public
name|void
name|testMultipleNodeList
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:found"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:found"
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:notfound"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:notfound"
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
literal|500
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:doTest"
argument_list|,
name|XML_INPUT_MULTIPLE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"NoTypeConversionAvailableException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|RuntimeCamelException
operator|.
name|class
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NoTypeConversionAvailableException
operator|.
name|class
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:doTest"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/root/name"
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
literal|"/name"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:found"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:notfound"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

