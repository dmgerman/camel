begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
package|;
end_package

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
name|transform
operator|.
name|TransformerException
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
name|CamelExecutionException
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

begin_class
DECL|class|XsltDTDTest
specifier|public
class|class
name|XsltDTDTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSendEntityMessage ()
specifier|public
name|void
name|testSendEntityMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|message
init|=
literal|"<!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///etc//user//test\">]><task><name>&xxe;</name></task>"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start1"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|xml
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
name|assertTrue
argument_list|(
literal|"Get a wrong transformed message"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
literal|"<transformed subject=\"\">"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start2"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect an exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expect an exception here
name|assertTrue
argument_list|(
literal|"Get a wrong exception"
argument_list|,
name|ex
operator|instanceof
name|CamelExecutionException
argument_list|)
expr_stmt|;
comment|// the file could not be found
name|assertTrue
argument_list|(
literal|"Get a wrong exception cause"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|TransformerException
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:start1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xslt:org/apache/camel/component/xslt/transform_dtd.xsl"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xslt:org/apache/camel/component/xslt/transform_dtd.xsl?allowStAX=false"
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
block|}
end_class

end_unit

