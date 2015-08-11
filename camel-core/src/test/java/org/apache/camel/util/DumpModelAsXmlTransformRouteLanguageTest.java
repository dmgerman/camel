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
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DumpModelAsXmlTransformRouteLanguageTest
specifier|public
class|class
name|DumpModelAsXmlTransformRouteLanguageTest
extends|extends
name|ContextTestSupport
block|{
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
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
operator|new
name|XmlConverter
argument_list|()
operator|.
name|toDOMDocument
argument_list|(
name|xml
argument_list|)
decl_stmt|;
name|NodeList
name|nodes
init|=
name|doc
operator|.
name|getElementsByTagName
argument_list|(
literal|"language"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nodes
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|node
init|=
operator|(
name|Element
operator|)
name|nodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Node<simple> expected to be instanceof Element"
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"constant"
argument_list|,
name|node
operator|.
name|getAttribute
argument_list|(
literal|"language"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|node
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|nodes
operator|=
name|doc
operator|.
name|getElementsByTagName
argument_list|(
literal|"to"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nodes
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|=
operator|(
name|Element
operator|)
name|nodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Node<to> expected to be instanceof Element"
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock:result"
argument_list|,
name|node
operator|.
name|getAttribute
argument_list|(
literal|"uri"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myMock"
argument_list|,
name|node
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|node
operator|.
name|getAttribute
argument_list|(
literal|"customId"
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
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|transform
argument_list|(
name|language
argument_list|(
literal|"constant"
argument_list|,
literal|"Hello World"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|id
argument_list|(
literal|"myMock"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

