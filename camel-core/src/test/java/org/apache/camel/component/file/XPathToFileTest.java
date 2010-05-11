begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
operator|.
name|xpath
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|XPathToFileTest
specifier|public
class|class
name|XPathToFileTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/xpath"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testXPathToFile ()
specifier|public
name|void
name|testXPathToFile
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
literal|"<foo><person id=\"1\">Claus<country>SE</country></person>"
operator|+
literal|"<person id=\"2\">Jonathan<country>CA</country></person></foo>"
decl_stmt|;
name|Document
name|doc
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|xml
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|File
name|first
init|=
operator|new
name|File
argument_list|(
literal|"target/xpath/xpath-0.xml"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File xpath-0.xml should exists"
argument_list|,
name|first
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"1\">Claus<country>SE</country></person>"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|first
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|second
init|=
operator|new
name|File
argument_list|(
literal|"target/xpath/xpath-1.xml"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File xpath-1.xml should exists"
argument_list|,
name|second
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"2\">Jonathan<country>CA</country></person>"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|second
argument_list|)
argument_list|)
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
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|xpath
argument_list|(
literal|"/foo/person"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"${bodyAs(String)}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://target/xpath?fileName=xpath-${property.CamelSplitIndex}.xml"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

