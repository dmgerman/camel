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
name|Message
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TestSupport
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
name|impl
operator|.
name|DefaultExchange
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
name|TestSupport
block|{
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
init|=
literal|null
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
DECL|method|func (String s)
specifier|public
specifier|static
name|String
name|func
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"modified"
operator|+
name|s
return|;
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
DECL|method|createExchange (String xml)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|String
name|xml
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
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

