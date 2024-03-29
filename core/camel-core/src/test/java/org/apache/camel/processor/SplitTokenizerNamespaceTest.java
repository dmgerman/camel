begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SplitTokenizerNamespaceTest
specifier|public
class|class
name|SplitTokenizerNamespaceTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSplitTokenizerWithImplicitNamespaces ()
specifier|public
name|void
name|testSplitTokenizerWithImplicitNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
comment|// we expect to receive results that have namespace definitions on each
comment|// token
comment|// we could receive nodes from multiple namespaces since we did not
comment|// specify a namespace prefix,
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<ns1:person xmlns:ns1=\"urn:org.apache.camel\" xmlns:ns2=\"urn:org.apache.cameltoo\">Claus</ns1:person>"
argument_list|,
literal|"<ns1:person xmlns:ns1=\"urn:org.apache.camel\" xmlns:ns2=\"urn:org.apache.cameltoo\">James</ns1:person>"
argument_list|,
literal|"<ns1:person xmlns:ns1=\"urn:org.apache.camel\" xmlns:ns2=\"urn:org.apache.cameltoo\">Willem</ns1:person>"
argument_list|,
literal|"<ns2:person xmlns:ns1=\"urn:org.apache.camel\" xmlns:ns2=\"urn:org.apache.cameltoo\">Rich</ns2:person>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:noPrefix"
argument_list|,
name|getXmlBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitTokenizerWithExplicitNamespaces ()
specifier|public
name|void
name|testSplitTokenizerWithExplicitNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
comment|// we expect to receive results that have namespace definitions on each
comment|// token
comment|// we provided an explicit namespace prefix value in the route, so we
comment|// will only receive nodes that have a matching prefix value
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<ns1:person xmlns:ns1=\"urn:org.apache.camel\" xmlns:ns2=\"urn:org.apache.cameltoo\">Claus</ns1:person>"
argument_list|,
literal|"<ns1:person xmlns:ns1=\"urn:org.apache.camel\" xmlns:ns2=\"urn:org.apache.cameltoo\">James</ns1:person>"
argument_list|,
literal|"<ns1:person xmlns:ns1=\"urn:org.apache.camel\" xmlns:ns2=\"urn:org.apache.cameltoo\">Willem</ns1:person>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:explicitPrefix"
argument_list|,
name|getXmlBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|getXmlBody ()
specifier|protected
name|String
name|getXmlBody
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"<?xml version=\"1.0\"?>\n"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<ns1:persons xmlns:ns1=\"urn:org.apache.camel\" xmlns:ns2=\"urn:org.apache.cameltoo\">\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<ns1:person>Claus</ns1:person>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<ns1:person>James</ns1:person>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<ns1:person>Willem</ns1:person>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<ns2:person>Rich</ns2:person>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</ns1:persons>"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
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
literal|"direct:noPrefix"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|tokenizeXML
argument_list|(
literal|"person"
argument_list|,
literal|"persons"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:explicitPrefix"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|tokenizeXML
argument_list|(
literal|"ns1:person"
argument_list|,
literal|"ns1:persons"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

