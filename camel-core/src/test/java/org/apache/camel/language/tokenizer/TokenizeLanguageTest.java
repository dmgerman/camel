begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|TokenizeLanguageTest
specifier|public
class|class
name|TokenizeLanguageTest
extends|extends
name|ContextTestSupport
block|{
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
literal|"<child some_attr='a' anotherAttr='a'></child>"
argument_list|,
literal|"<child some_attr='b' anotherAttr='b'></child>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><parent><child some_attr='a' anotherAttr='a'></child><child some_attr='b' anotherAttr='b'></child></parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
literal|"<child some_attr='a' anotherAttr='a'>\n</child>"
argument_list|,
literal|"<child some_attr='b' anotherAttr='b'>\n</child>"
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
literal|"<parent>\n"
operator|+
literal|"<child some_attr='a' anotherAttr='a'>\n"
operator|+
literal|"</child>\n"
operator|+
literal|"<child some_attr='b' anotherAttr='b'>\n"
operator|+
literal|"</child>\n"
operator|+
literal|"</parent>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|xxxTestSendSelfClosingTagMessageToTokenize ()
specifier|public
name|void
name|xxxTestSendSelfClosingTagMessageToTokenize
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: ignored test as it does not work, there is a CAMEL ticket about this
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<child some_attr='a' anotherAttr='a' />"
argument_list|,
literal|"<child some_attr='b' anotherAttr='b' />"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<?xml version='1.0' encoding='UTF-8'?><parent><child some_attr='a' anotherAttr='a' /><child some_attr='b' anotherAttr='b' /></parent>"
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
name|tokenizeXML
argument_list|(
literal|"child"
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

