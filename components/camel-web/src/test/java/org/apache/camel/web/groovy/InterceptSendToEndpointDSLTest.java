begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|groovy
package|;
end_package

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
comment|/**  *   */
end_comment

begin_class
DECL|class|InterceptSendToEndpointDSLTest
specifier|public
class|class
name|InterceptSendToEndpointDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
annotation|@
name|Test
DECL|method|testInterceptSendToEndpoint ()
specifier|public
name|void
name|testInterceptSendToEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptSendToEndpoint(\"mock:foo\").to(\"mock:detour\").transform(constant(\"Bye World\"));"
operator|+
literal|"from(\"direct:first\").to(\"mock:bar\").to(\"mock:foo\").to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInterceptSendToEndpointDynamic ()
specifier|public
name|void
name|testInterceptSendToEndpointDynamic
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptSendToEndpoint(\"file:*\").skipSendToOriginalEndpoint().to(\"mock:detour\");"
operator|+
literal|"from(\"direct:first\").to(\"file://foo\").to(\"file://bar\").to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInterceptSendToEndpointInOnException ()
specifier|public
name|void
name|testInterceptSendToEndpointInOnException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"onException(IOException.class).handled(true).to(\"mock:io\");"
operator|+
literal|"interceptSendToEndpoint(\"mock:io\").skipSendToOriginalEndpoint().to(\"mock:intercepted\");"
operator|+
literal|"from(\"direct:start\").to(\"mock:foo\").to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Need to fix this test"
argument_list|)
annotation|@
name|Test
comment|// TODO: fix this test!
DECL|method|fixmeTestInterceptSendToIssue ()
specifier|public
name|void
name|fixmeTestInterceptSendToIssue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dsl
init|=
literal|"interceptSendToEndpoint(\"direct:foo\").to(\"mock:foo\");"
operator|+
literal|"from(\"direct:start\").setHeader(Exchange.FILE_NAME, constant(\"hello.txt\")).to(\"direct:foo\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|dsl
argument_list|,
name|render
argument_list|(
name|dsl
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

