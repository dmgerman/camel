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
name|apache
operator|.
name|camel
operator|.
name|support
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
DECL|class|SplitterWithXqureyTest
specifier|public
class|class
name|SplitterWithXqureyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|xmlData
specifier|private
specifier|static
name|String
name|xmlData
init|=
literal|"<workflow id=\"12345\" xmlns=\"http://camel.apache.org/schema/one\" "
operator|+
literal|"xmlns:two=\"http://camel.apache.org/schema/two\">"
operator|+
literal|"<person><name>Willem</name></person> "
operator|+
literal|"<other><two:test>One</two:test></other>"
operator|+
literal|"<other><two:test>Two</two:test></other>"
operator|+
literal|"<other><test>Three</test></other>"
operator|+
literal|"<other><test>Foure</test></other></workflow>"
decl_stmt|;
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
comment|// split the message with namespaces defined
name|Namespaces
name|namespaces
init|=
operator|new
name|Namespaces
argument_list|(
literal|"one"
argument_list|,
literal|"http://camel.apache.org/schema/one"
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:endpoint"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"//one:other"
argument_list|,
name|namespaces
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testSenderXmlData ()
specifier|public
name|void
name|testSenderXmlData
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|reset
argument_list|()
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:endpoint"
argument_list|,
name|xmlData
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|result
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|String
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"The message is "
operator|+
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The splitted message should start with<other"
argument_list|,
name|message
operator|.
name|indexOf
argument_list|(
literal|"<other"
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

