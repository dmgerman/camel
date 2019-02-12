begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|model
operator|.
name|ModelHelper
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
DECL|class|DumpModelAsXmlNamespaceTest
specifier|public
class|class
name|DumpModelAsXmlNamespaceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|URL_FOO
specifier|private
specifier|static
specifier|final
name|String
name|URL_FOO
init|=
literal|"http://foo.com"
decl_stmt|;
DECL|field|URL_BAR
specifier|private
specifier|static
specifier|final
name|String
name|URL_BAR
init|=
literal|"http://bar.com"
decl_stmt|;
annotation|@
name|Test
DECL|method|testDumpModelAsXml ()
specifier|public
name|void
name|testDumpModelAsXml
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|context
argument_list|,
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"myRoute"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|Document
name|dom
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
name|Element
name|rootNode
init|=
name|dom
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rootNode
argument_list|)
expr_stmt|;
name|String
name|attributeFoo
init|=
name|rootNode
operator|.
name|getAttribute
argument_list|(
literal|"xmlns:foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attributeFoo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URL_FOO
argument_list|,
name|attributeFoo
argument_list|)
expr_stmt|;
name|String
name|attributeBar
init|=
name|rootNode
operator|.
name|getAttribute
argument_list|(
literal|"xmlns:bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attributeBar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URL_BAR
argument_list|,
name|attributeBar
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
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"/foo:customer"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"foo"
argument_list|,
name|URL_FOO
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"/bar:customer"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"bar"
argument_list|,
name|URL_BAR
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
