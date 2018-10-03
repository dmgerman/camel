begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|Node
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
name|impl
operator|.
name|DefaultCamelContext
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
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageContentsList
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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|reset
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|ConverterTest
specifier|public
class|class
name|ConverterTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testToArray ()
specifier|public
name|void
name|testToArray
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|testList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|testList
operator|.
name|add
argument_list|(
literal|"string 1"
argument_list|)
expr_stmt|;
name|testList
operator|.
name|add
argument_list|(
literal|"string 2"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|array
init|=
name|CxfConverter
operator|.
name|toArray
argument_list|(
name|testList
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The array should not be null"
argument_list|,
name|array
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The array size should not be wrong"
argument_list|,
literal|2
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInputStream ()
specifier|public
name|void
name|testToInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|mock
argument_list|(
name|Response
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|mock
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|InputStream
name|result
init|=
name|CxfConverter
operator|.
name|toInputStream
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the inputStream here "
argument_list|,
name|is
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|reset
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|result
operator|=
name|CxfConverter
operator|.
name|toInputStream
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the inputStream here "
argument_list|,
name|result
operator|instanceof
name|ByteArrayInputStream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFallbackConverter ()
specifier|public
name|void
name|testFallbackConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MessageContentsList
name|list
init|=
operator|new
name|MessageContentsList
argument_list|()
decl_stmt|;
name|NodeListWrapper
name|nl
init|=
operator|new
name|NodeListWrapper
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|nl
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/component/cxf/converter/test.xml"
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|documentBuilderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|documentBuilderFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|documentBuilder
init|=
name|documentBuilderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|document
init|=
name|documentBuilder
operator|.
name|parse
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|normalize
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
name|document
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|nl
operator|=
operator|new
name|NodeListWrapper
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|list
operator|.
name|clear
argument_list|()
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|nl
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|node
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Node
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

