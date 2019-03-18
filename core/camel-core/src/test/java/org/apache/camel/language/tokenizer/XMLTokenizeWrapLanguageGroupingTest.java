begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.tokenizer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|tokenizer
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
name|builder
operator|.
name|xml
operator|.
name|Namespaces
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
DECL|class|XMLTokenizeWrapLanguageGroupingTest
specifier|public
class|class
name|XMLTokenizeWrapLanguageGroupingTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendClosedTagMessageToTokenize ()
specifier|public
name|void
name|testSendClosedTagMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'></c:child></c:parent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child some_attr='a' anotherAttr='a'></c:child><c:child some_attr='b' anotherAttr='b'></c:child></c:parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendClosedTagWithLineBreaksMessageToTokenize ()
specifier|public
name|void
name|testSendClosedTagWithLineBreaksMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?>\n<c:parent xmlns:c='urn:c'>\n<c:child some_attr='a' anotherAttr='a'>\n</c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'>\n</c:child></c:parent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?>\n"
operator|+
literal|"<c:parent xmlns:c='urn:c'>\n"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'>\n"
operator|+
literal|"</c:child>\n"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'>\n"
operator|+
literal|"</c:child>\n"
operator|+
literal|"</c:parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendSelfClosingTagMessageToTokenize ()
specifier|public
name|void
name|testSendSelfClosingTagMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child some_attr='a' anotherAttr='a' />"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b' /></c:parent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child some_attr='a' anotherAttr='a' /><c:child some_attr='b' anotherAttr='b' /></c:parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMixedClosingTagMessageToTokenize ()
specifier|public
name|void
name|testSendMixedClosingTagMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child some_attr='a' anotherAttr='a'>ha</c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b' /></c:parent>"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child some_attr='c'></c:child></c:parent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child some_attr='a' anotherAttr='a'>ha</c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b' /><c:child some_attr='c'></c:child></c:parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMixedClosingTagInsideMessageToTokenize ()
specifier|public
name|void
name|testSendMixedClosingTagInsideMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<c:parent xmlns:c='urn:c'><c:child name='child1'><grandchild name='grandchild1'/><grandchild name='grandchild2'/></c:child>"
operator|+
literal|"<c:child name='child2'><grandchild name='grandchild1'></grandchild><grandchild name='grandchild2'></grandchild></c:child></c:parent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<c:parent xmlns:c='urn:c'><c:child name='child1'><grandchild name='grandchild1'/><grandchild name='grandchild2'/></c:child>"
operator|+
literal|"<c:child name='child2'><grandchild name='grandchild1'></grandchild><grandchild name='grandchild2'></grandchild></c:child></c:parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendNamespacedChildMessageToTokenize ()
specifier|public
name|void
name|testSendNamespacedChildMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child xmlns:c='urn:c' some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"<c:child xmlns:c='urn:c' some_attr='b' anotherAttr='b' /></c:parent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c'><c:child xmlns:c='urn:c' some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"<c:child xmlns:c='urn:c' some_attr='b' anotherAttr='b' /></c:parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendNamespacedParentMessageToTokenize ()
specifier|public
name|void
name|testSendNamespacedParentMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c' xmlns:d=\"urn:d\"><c:child some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'/></c:parent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><c:parent xmlns:c='urn:c' xmlns:d=\"urn:d\"><c:child some_attr='a' anotherAttr='a'></c:child><c:child some_attr='b' anotherAttr='b'/></c:parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMoreParentsMessageToTokenize ()
specifier|public
name|void
name|testSendMoreParentsMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt><c:parent xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'/></c:parent></grandparent></g:greatgrandparent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt><c:parent xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'></c:child><c:child some_attr='b' anotherAttr='b'/></c:parent></grandparent></g:greatgrandparent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendParentMessagesWithDifferentAttributesToTokenize ()
specifier|public
name|void
name|testSendParentMessagesWithDifferentAttributesToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><g:grandparent xmlns:g='urn:g'><c:parent name='e' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'></c:child></c:parent></g:grandparent>"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:grandparent xmlns:g='urn:g'><c:parent name='f' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'/></c:parent></g:grandparent>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:grandparent xmlns:g='urn:g'><c:parent name='e' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'></c:child></c:parent><c:parent name='f' xmlns:c='urn:c' xmlns:d=\"urn:d\"><c:child some_attr='b' anotherAttr='b'/>"
operator|+
literal|"</c:parent></g:grandparent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
name|Namespaces
name|ns
init|=
operator|new
name|Namespaces
argument_list|(
literal|"C"
argument_list|,
literal|"urn:c"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|xtokenize
argument_list|(
literal|"//C:child"
argument_list|,
literal|'w'
argument_list|,
name|ns
argument_list|,
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

