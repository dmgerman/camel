begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.saxon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|saxon
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
name|XPathFactory
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
name|XPathFactoryImpl
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
name|XPathBuilder
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
name|Ignore
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|XPathTest
specifier|public
class|class
name|XPathTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testXPathUsingSaxon ()
specifier|public
name|void
name|testXPathUsingSaxon
parameter_list|()
throws|throws
name|Exception
block|{
name|XPathFactory
name|fac
init|=
operator|new
name|XPathFactoryImpl
argument_list|()
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"foo/bar"
argument_list|)
operator|.
name|factory
argument_list|(
name|fac
argument_list|)
decl_stmt|;
comment|// will evaluate as XPathConstants.NODESET and have Camel convert that to String
comment|// this should return the String incl. xml tags
name|String
name|name
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
literal|"<foo><bar id=\"1\">cheese</bar></foo>"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<bar id=\"1\">cheese</bar>"
argument_list|,
name|name
argument_list|)
expr_stmt|;
comment|// will evaluate using XPathConstants.STRING which just return the text content (eg like text())
name|name
operator|=
name|builder
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
literal|"<foo><bar id=\"1\">cheese</bar></foo>"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXPathFunctionSubstringUsingSaxon ()
specifier|public
name|void
name|testXPathFunctionSubstringUsingSaxon
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
literal|"<foo><bar>Hello World</bar></foo>"
decl_stmt|;
name|XPathFactory
name|fac
init|=
operator|new
name|XPathFactoryImpl
argument_list|()
decl_stmt|;
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"substring(/foo/bar, 7)"
argument_list|)
operator|.
name|factory
argument_list|(
name|fac
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|builder
operator|.
name|resultType
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
name|xml
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|builder
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXPathFunctionTokenizeUsingSaxonXPathFactory ()
specifier|public
name|void
name|testXPathFunctionTokenizeUsingSaxonXPathFactory
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// create a Saxon factory
name|XPathFactory
name|fac
init|=
operator|new
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|xpath
operator|.
name|XPathFactoryImpl
argument_list|()
decl_stmt|;
comment|// create a builder to evaluate the xpath using the saxon factory
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"tokenize(/foo/bar, '_')[2]"
argument_list|)
operator|.
name|factory
argument_list|(
name|fac
argument_list|)
decl_stmt|;
comment|// evaluate as a String result
name|String
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
literal|"<foo><bar>abc_def_ghi</bar></foo>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"def"
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
annotation|@
name|Ignore
argument_list|(
literal|"See http://www.saxonica.com/documentation/index.html#!xpath-api/jaxp-xpath/factory"
argument_list|)
annotation|@
name|Test
DECL|method|testXPathFunctionTokenizeUsingObjectModel ()
specifier|public
name|void
name|testXPathFunctionTokenizeUsingObjectModel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e2
comment|// create a builder to evaluate the xpath using saxon based on its object model uri
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"tokenize(/foo/bar, '_')[2]"
argument_list|)
operator|.
name|objectModel
argument_list|(
literal|"http://saxon.sf.net/jaxp/xpath/om"
argument_list|)
decl_stmt|;
comment|// evaluate as a String result
name|String
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
literal|"<foo><bar>abc_def_ghi</bar></foo>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"def"
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
block|}
annotation|@
name|Test
DECL|method|testXPathFunctionTokenizeUsingSaxon ()
specifier|public
name|void
name|testXPathFunctionTokenizeUsingSaxon
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e3
comment|// create a builder to evaluate the xpath using saxon
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"tokenize(/foo/bar, '_')[2]"
argument_list|)
operator|.
name|saxon
argument_list|()
decl_stmt|;
comment|// evaluate as a String result
name|String
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
literal|"<foo><bar>abc_def_ghi</bar></foo>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"def"
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e3
block|}
annotation|@
name|Test
DECL|method|testXPathFunctionTokenizeUsingSystemProperty ()
specifier|public
name|void
name|testXPathFunctionTokenizeUsingSystemProperty
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e4
comment|// set system property with the XPath factory to use which is Saxon
name|System
operator|.
name|setProperty
argument_list|(
name|XPathFactory
operator|.
name|DEFAULT_PROPERTY_NAME
operator|+
literal|":"
operator|+
literal|"http://saxon.sf.net/jaxp/xpath/om"
argument_list|,
literal|"net.sf.saxon.xpath.XPathFactoryImpl"
argument_list|)
expr_stmt|;
comment|// create a builder to evaluate the xpath using saxon
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"tokenize(/foo/bar, '_')[2]"
argument_list|)
decl_stmt|;
comment|// evaluate as a String result
name|String
name|result
init|=
name|builder
operator|.
name|evaluate
argument_list|(
name|context
argument_list|,
literal|"<foo><bar>abc_def_ghi</bar></foo>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"def"
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e4
block|}
block|}
end_class

end_unit

