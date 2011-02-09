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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|component
operator|.
name|cxf
operator|.
name|CxfPayload
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
name|ExchangeTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|CxfPayloadConverterTest
specifier|public
class|class
name|CxfPayloadConverterTest
extends|extends
name|ExchangeTestSupport
block|{
DECL|field|document
specifier|private
name|Document
name|document
decl_stmt|;
DECL|field|payload
specifier|private
name|CxfPayload
argument_list|<
name|String
index|[]
argument_list|>
name|payload
decl_stmt|;
DECL|field|inputStream
specifier|private
name|FileInputStream
name|inputStream
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
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
name|DocumentBuilder
name|documentBuilder
init|=
name|documentBuilderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|document
operator|=
name|documentBuilder
operator|.
name|parse
argument_list|(
name|file
argument_list|)
expr_stmt|;
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
name|body
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
name|document
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|payload
operator|=
operator|new
name|CxfPayload
argument_list|<
name|String
index|[]
argument_list|>
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|String
index|[]
argument_list|>
argument_list|()
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|inputStream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDocumentToCxfPayload ()
specifier|public
name|void
name|testDocumentToCxfPayload
parameter_list|()
block|{
name|CxfPayload
argument_list|<
name|String
index|[]
argument_list|>
name|payload
init|=
name|CxfPayloadConverter
operator|.
name|documentToCxfPayload
argument_list|(
name|document
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of body"
argument_list|,
literal|1
argument_list|,
name|payload
operator|.
name|getBody
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNodeListToCxfPayload ()
specifier|public
name|void
name|testNodeListToCxfPayload
parameter_list|()
block|{
name|NodeList
name|nodeList
init|=
name|document
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|CxfPayload
argument_list|<
name|String
index|[]
argument_list|>
name|payload
init|=
name|CxfPayloadConverter
operator|.
name|nodeListToCxfPayload
argument_list|(
name|nodeList
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of body"
argument_list|,
literal|1
argument_list|,
name|payload
operator|.
name|getBody
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfPayloadToNodeList ()
specifier|public
name|void
name|testCxfPayloadToNodeList
parameter_list|()
block|{
name|NodeList
name|nodeList
init|=
name|CxfPayloadConverter
operator|.
name|cxfPayloadToNodeList
argument_list|(
name|payload
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nodeList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a worng size of nodeList"
argument_list|,
literal|1
argument_list|,
name|nodeList
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToCxfPayload ()
specifier|public
name|void
name|testToCxfPayload
parameter_list|()
block|{
comment|// use default type converter
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
name|CxfPayload
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|instanceof
name|CxfPayload
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of body"
argument_list|,
literal|1
argument_list|,
name|payload
operator|.
name|getBody
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFromCxfPayload ()
specifier|public
name|void
name|testFromCxfPayload
parameter_list|()
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|InputStream
name|inputStream
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|inputStream
operator|instanceof
name|InputStream
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

