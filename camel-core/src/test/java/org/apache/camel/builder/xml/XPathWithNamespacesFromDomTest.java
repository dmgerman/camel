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
name|junit
operator|.
name|Test
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|XPathWithNamespacesFromDomTest
specifier|public
class|class
name|XPathWithNamespacesFromDomTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testXPathUsingDomForNamespaces ()
specifier|public
name|void
name|testXPathUsingDomForNamespaces
parameter_list|()
throws|throws
name|Exception
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
literal|"<x:foo xmlns:x='n1' xmlns:y='n2'><bar id='a' xmlns:y='n3'/></x:foo>"
argument_list|)
decl_stmt|;
name|Element
name|element
init|=
operator|(
name|Element
operator|)
name|document
operator|.
name|getElementsByTagName
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find element for id 'a'"
argument_list|,
name|element
argument_list|)
expr_stmt|;
name|XPathBuilder
name|builder
init|=
name|XPathBuilder
operator|.
name|xpath
argument_list|(
literal|"//y:foo[@id='z']"
argument_list|)
decl_stmt|;
name|Namespaces
name|ns
init|=
operator|new
name|Namespaces
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|ns
operator|.
name|configure
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|builder
operator|.
name|start
argument_list|()
expr_stmt|;
name|DefaultNamespaceContext
name|namespaceContext
init|=
name|builder
operator|.
name|getNamespaceContext
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"y namespace"
argument_list|,
literal|"n3"
argument_list|,
name|namespaceContext
operator|.
name|getNamespaceURI
argument_list|(
literal|"y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertPredicateMatches
argument_list|(
name|builder
argument_list|,
name|createExchangeWithBody
argument_list|(
literal|"<blah><foo xmlns='n3' id='z'/></blah>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertPredicateDoesNotMatch
argument_list|(
name|builder
argument_list|,
name|createExchangeWithBody
argument_list|(
literal|"<blah><foo xmlns='n2' id='z'/></blah>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

