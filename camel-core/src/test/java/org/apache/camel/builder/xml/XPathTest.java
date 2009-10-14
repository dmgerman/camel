begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
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
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFunctionResolver
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|Expression
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
name|Predicate
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
DECL|class|XPathTest
specifier|public
class|class
name|XPathTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|testXPathExpressions ()
specifier|public
name|void
name|testXPathExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"/foo/bar/@xyz"
argument_list|,
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"$name"
argument_list|,
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"foo/bar"
argument_list|,
literal|"<foo><bar>cheese</bar></foo>"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"foo/bar/text()"
argument_list|,
literal|"<foo><bar>cheese</bar></foo>"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"/foo/@id"
argument_list|,
literal|"<foo id='cheese'>hey</foo>"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathPredicates ()
specifier|public
name|void
name|testXPathPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPredicate
argument_list|(
literal|"/foo/bar/@xyz"
argument_list|,
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"$name = 'James'"
argument_list|,
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"$name = 'Hiram'"
argument_list|,
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"/foo/notExist"
argument_list|,
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithCustomVariable ()
specifier|public
name|void
name|testXPathWithCustomVariable
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
name|xpath
argument_list|(
literal|"$name"
argument_list|)
operator|.
name|stringResult
argument_list|()
operator|.
name|variable
argument_list|(
literal|"name"
argument_list|,
literal|"Hiram"
argument_list|)
argument_list|,
literal|"<foo/>"
argument_list|,
literal|"Hiram"
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidXPath ()
specifier|public
name|void
name|testInvalidXPath
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|assertPredicate
argument_list|(
literal|"/foo/"
argument_list|,
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidXPathExpression
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"/foo/"
argument_list|,
name|e
operator|.
name|getXpath
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|XPathExpressionException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testXPathBooleanResult ()
specifier|public
name|void
name|testXPathBooleanResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|xpath
argument_list|(
literal|"/foo/bar/@xyz"
argument_list|)
operator|.
name|booleanResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|)
argument_list|)
decl_stmt|;
name|Boolean
name|bool
init|=
name|assertIsInstanceOf
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|bool
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathNodeResult ()
specifier|public
name|void
name|testXPathNodeResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|xpath
argument_list|(
literal|"/foo/bar"
argument_list|)
operator|.
name|nodeResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<foo><bar xyz='cheese'/></foo>"
argument_list|)
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|assertIsInstanceOf
argument_list|(
name|Node
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|String
name|s
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
name|node
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<bar xyz=\"cheese\"/>"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathNodeSetResult ()
specifier|public
name|void
name|testXPathNodeSetResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
operator|.
name|nodeSetResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<foo>bar</foo>"
argument_list|)
argument_list|)
decl_stmt|;
name|NodeList
name|node
init|=
name|assertIsInstanceOf
argument_list|(
name|NodeList
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|String
name|s
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
name|node
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathNumberResult ()
specifier|public
name|void
name|testXPathNumberResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|xpath
argument_list|(
literal|"/foo/bar/@xyz"
argument_list|)
operator|.
name|numberResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<foo><bar xyz='123'/></foo>"
argument_list|)
argument_list|)
decl_stmt|;
name|Double
name|num
init|=
name|assertIsInstanceOf
argument_list|(
name|Double
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123.0"
argument_list|,
name|num
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathStringResult ()
specifier|public
name|void
name|testXPathStringResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|xpath
argument_list|(
literal|"/foo/bar/@xyz"
argument_list|)
operator|.
name|stringResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<foo><bar xyz='123'/></foo>"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|num
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|num
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathCustomResult ()
specifier|public
name|void
name|testXPathCustomResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|xpath
argument_list|(
literal|"/foo/bar/@xyz"
argument_list|)
operator|.
name|resultType
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<foo><bar xyz='123'/></foo>"
argument_list|)
argument_list|)
decl_stmt|;
name|Integer
name|num
init|=
name|assertIsInstanceOf
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|num
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathBuilder ()
specifier|public
name|void
name|testXPathBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/foo/bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|builder
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XPathConstants
operator|.
name|NODESET
argument_list|,
name|builder
operator|.
name|getResultQName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|builder
operator|.
name|getResultType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithDocument ()
specifier|public
name|void
name|testXPathWithDocument
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|doc
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|s
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
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithDocumentTypeDOMSource ()
specifier|public
name|void
name|testXPathWithDocumentTypeDOMSource
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDocumentType
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|doc
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|s
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
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithDocumentTypeInputSource ()
specifier|public
name|void
name|testXPathWithDocumentTypeInputSource
parameter_list|()
throws|throws
name|Exception
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|InputSource
name|doc
init|=
operator|new
name|InputSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDocumentType
argument_list|(
name|InputSource
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|doc
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|s
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
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithDocumentTypeInputSourceFluentBuilder ()
specifier|public
name|void
name|testXPathWithDocumentTypeInputSourceFluentBuilder
parameter_list|()
throws|throws
name|Exception
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|InputSource
name|doc
init|=
operator|new
name|InputSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
operator|.
name|documentType
argument_list|(
name|InputSource
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|doc
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|s
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
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithDocumentTypeInputSourceNoResultQName ()
specifier|public
name|void
name|testXPathWithDocumentTypeInputSourceNoResultQName
parameter_list|()
throws|throws
name|Exception
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|InputSource
name|doc
init|=
operator|new
name|InputSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDocumentType
argument_list|(
name|InputSource
operator|.
name|class
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setResultQName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|doc
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|s
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
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithDocumentTypeDOMSourceNoResultQName ()
specifier|public
name|void
name|testXPathWithDocumentTypeDOMSourceNoResultQName
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDocumentType
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setResultQName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|doc
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|s
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
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithStringTypeDOMSourceNoResultQName ()
specifier|public
name|void
name|testXPathWithStringTypeDOMSourceNoResultQName
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setResultQName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<foo>bar</foo>"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|s
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
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithNamespaceBooleanResult ()
specifier|public
name|void
name|testXPathWithNamespaceBooleanResult
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/c:person[@name='James']"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"c"
argument_list|,
literal|"http://acme.com/cheese"
argument_list|)
operator|.
name|booleanResult
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<person xmlns=\"http://acme.com/cheese\" name='James' city='London'/>"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithNamespaceBooleanResultType ()
specifier|public
name|void
name|testXPathWithNamespaceBooleanResultType
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/c:person[@name='James']"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"c"
argument_list|,
literal|"http://acme.com/cheese"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setResultType
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<person xmlns=\"http://acme.com/cheese\" name='James' city='London'/>"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithNamespaceStringResult ()
specifier|public
name|void
name|testXPathWithNamespaceStringResult
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/c:person/@name"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"c"
argument_list|,
literal|"http://acme.com/cheese"
argument_list|)
operator|.
name|stringResult
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<person xmlns=\"http://acme.com/cheese\" name='James' city='London'/>"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithNamespacesBooleanResult ()
specifier|public
name|void
name|testXPathWithNamespacesBooleanResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Namespaces
name|ns
init|=
operator|new
name|Namespaces
argument_list|(
literal|"c"
argument_list|,
literal|"http://acme.com/cheese"
argument_list|)
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/c:person[@name='James']"
argument_list|)
operator|.
name|namespaces
argument_list|(
name|ns
argument_list|)
operator|.
name|booleanResult
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<person xmlns=\"http://acme.com/cheese\" name='James' city='London'/>"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithNamespacesStringResult ()
specifier|public
name|void
name|testXPathWithNamespacesStringResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Namespaces
name|ns
init|=
operator|new
name|Namespaces
argument_list|(
literal|"c"
argument_list|,
literal|"http://acme.com/cheese"
argument_list|)
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/c:person/@name"
argument_list|)
operator|.
name|namespaces
argument_list|(
name|ns
argument_list|)
operator|.
name|stringResult
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<person xmlns=\"http://acme.com/cheese\" name='James' city='London'/>"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testXPathWithNamespacesNodeResult ()
specifier|public
name|void
name|testXPathWithNamespacesNodeResult
parameter_list|()
throws|throws
name|Exception
block|{
name|Namespaces
name|ns
init|=
operator|new
name|Namespaces
argument_list|(
literal|"c"
argument_list|,
literal|"http://acme.com/cheese"
argument_list|)
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"/c:person/@name"
argument_list|)
operator|.
name|namespaces
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setResultType
argument_list|(
name|Node
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
literal|"<person xmlns=\"http://acme.com/cheese\" name='James' city='London'/>"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"James"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUsingJavaExtensions ()
specifier|public
name|void
name|testUsingJavaExtensions
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|instance
decl_stmt|;
comment|// we may not have Xalan on the classpath
try|try
block|{
name|instance
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.xalan.extensions.XPathFunctionResolverImpl"
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
if|if
condition|(
name|instance
operator|instanceof
name|XPathFunctionResolver
condition|)
block|{
name|XPathFunctionResolver
name|functionResolver
init|=
operator|(
name|XPathFunctionResolver
operator|)
name|instance
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|xpath
argument_list|(
literal|"java:"
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|".func(string(/header/value))"
argument_list|)
operator|.
name|namespace
argument_list|(
literal|"java"
argument_list|,
literal|"http://xml.apache.org/xalan/java"
argument_list|)
operator|.
name|functionResolver
argument_list|(
name|functionResolver
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
literal|"<header><value>12</value></header>"
decl_stmt|;
name|Object
name|value
init|=
name|assertExpression
argument_list|(
name|builder
argument_list|,
name|xml
argument_list|,
literal|"modified12"
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Evaluated xpath: "
operator|+
name|builder
operator|.
name|getText
argument_list|()
operator|+
literal|" on XML: "
operator|+
name|xml
operator|+
literal|" result: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Could not find Xalan on the classpath so ignoring this test case: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertExpression (String xpath, String xml, String expected)
specifier|protected
name|Object
name|assertExpression
parameter_list|(
name|String
name|xpath
parameter_list|,
name|String
name|xml
parameter_list|,
name|String
name|expected
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
name|xpath
argument_list|)
operator|.
name|stringResult
argument_list|()
decl_stmt|;
return|return
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|xml
argument_list|,
name|expected
argument_list|)
return|;
block|}
DECL|method|assertExpression (Expression expression, String xml, String expected)
specifier|protected
name|Object
name|assertExpression
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|String
name|xml
parameter_list|,
name|String
name|expected
parameter_list|)
block|{
return|return
name|assertExpression
argument_list|(
name|expression
argument_list|,
name|createExchange
argument_list|(
name|xml
argument_list|)
argument_list|,
name|expected
argument_list|)
return|;
block|}
DECL|method|assertPredicate (String xpath, String xml, boolean expected)
specifier|protected
name|void
name|assertPredicate
parameter_list|(
name|String
name|xpath
parameter_list|,
name|String
name|xml
parameter_list|,
name|boolean
name|expected
parameter_list|)
block|{
name|Predicate
name|predicate
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
name|xpath
argument_list|)
decl_stmt|;
name|assertPredicate
argument_list|(
name|predicate
argument_list|,
name|createExchange
argument_list|(
name|xml
argument_list|)
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|createExchange (Object xml)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|xml
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
name|context
argument_list|,
name|xml
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

