begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.saxon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|saxon
package|;
end_package

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
name|LinkedList
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
name|namespace
operator|.
name|NamespaceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
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
name|Node
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
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|om
operator|.
name|NodeInfo
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|trans
operator|.
name|XPathException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|xpath
operator|.
name|XPathEvaluator
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
name|StringSource
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
name|xml
operator|.
name|DefaultNamespaceContext
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
name|DefaultExchange
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
DECL|class|SaxonConverterTest
specifier|public
class|class
name|SaxonConverterTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CONTENT
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT
init|=
literal|"<a xmlns=\"http://www.apache.org/test\"><b foo=\"bar\">test</b><c><d>foobar</d></c></a>"
decl_stmt|;
DECL|field|CONTENT_B
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_B
init|=
literal|"<b xmlns=\"http://www.apache.org/test\" foo=\"bar\">test</b>"
decl_stmt|;
DECL|field|NS_CONTEXT
specifier|private
specifier|static
specifier|final
name|NamespaceContext
name|NS_CONTEXT
init|=
operator|(
operator|new
name|DefaultNamespaceContext
argument_list|()
operator|)
operator|.
name|add
argument_list|(
literal|"ns1"
argument_list|,
literal|"http://www.apache.org/test"
argument_list|)
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|evaluator
specifier|private
name|XPathEvaluator
name|evaluator
decl_stmt|;
DECL|field|doc
specifier|private
name|NodeInfo
name|doc
decl_stmt|;
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
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|evaluator
operator|=
operator|new
name|XPathEvaluator
argument_list|()
expr_stmt|;
name|doc
operator|=
name|evaluator
operator|.
name|setSource
argument_list|(
operator|new
name|StringSource
argument_list|(
name|CONTENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToDOMSource ()
specifier|public
name|void
name|convertToDOMSource
parameter_list|()
throws|throws
name|XPathException
block|{
name|DOMSource
name|source
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|source
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToDocument ()
specifier|public
name|void
name|convertToDocument
parameter_list|()
throws|throws
name|XPathException
block|{
name|Document
name|document
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
name|exchange
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|document
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertSubNodeToDocument ()
specifier|public
name|void
name|convertSubNodeToDocument
parameter_list|()
throws|throws
name|XPathException
throws|,
name|XPathExpressionException
block|{
name|evaluator
operator|.
name|setNamespaceContext
argument_list|(
name|NS_CONTEXT
argument_list|)
expr_stmt|;
name|Object
name|nodeObj
init|=
name|evaluator
operator|.
name|evaluate
argument_list|(
literal|"/ns1:a/ns1:b"
argument_list|,
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nodeObj
argument_list|)
expr_stmt|;
name|Document
name|document
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
name|exchange
argument_list|,
name|nodeObj
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|document
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT_B
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertSubNodeSetToDocument ()
specifier|public
name|void
name|convertSubNodeSetToDocument
parameter_list|()
throws|throws
name|XPathException
throws|,
name|XPathExpressionException
block|{
name|evaluator
operator|.
name|setNamespaceContext
argument_list|(
name|NS_CONTEXT
argument_list|)
expr_stmt|;
name|Object
name|nodeObj
init|=
name|evaluator
operator|.
name|evaluate
argument_list|(
literal|"/ns1:a/ns1:b"
argument_list|,
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nodeObj
argument_list|)
expr_stmt|;
name|Document
name|document
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
name|exchange
argument_list|,
name|nodeObj
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|document
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT_B
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToNode ()
specifier|public
name|void
name|convertToNode
parameter_list|()
throws|throws
name|XPathException
block|{
name|Node
name|node
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Node
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToNodeList ()
specifier|public
name|void
name|convertToNodeList
parameter_list|()
throws|throws
name|XPathException
block|{
name|List
argument_list|<
name|NodeInfo
argument_list|>
name|nil
init|=
operator|new
name|LinkedList
argument_list|<
name|NodeInfo
argument_list|>
argument_list|()
decl_stmt|;
name|nil
operator|.
name|add
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|NodeList
name|nodeList
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|NodeList
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|nil
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nodeList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nodeList
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|nodeList
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToInputStream ()
specifier|public
name|void
name|convertToInputStream
parameter_list|()
throws|throws
name|XPathException
block|{
name|InputStream
name|is
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToByteArray ()
specifier|public
name|void
name|convertToByteArray
parameter_list|()
throws|throws
name|XPathException
block|{
name|byte
index|[]
name|ba
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ba
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|ba
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToNodeAndByteArray ()
specifier|public
name|void
name|convertToNodeAndByteArray
parameter_list|()
throws|throws
name|XPathException
block|{
name|Node
name|node
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Node
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|byte
index|[]
name|ba
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ba
argument_list|)
expr_stmt|;
name|String
name|string
init|=
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
name|exchange
argument_list|,
name|ba
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

