begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.script
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
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
name|ScriptTestHelper
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

begin_comment
comment|/**  * Unit test for a Groovy script based on end-user question.  */
end_comment

begin_class
DECL|class|GroovyScriptRouteTest
specifier|public
class|class
name|GroovyScriptRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testGroovyScript ()
specifier|public
name|void
name|testGroovyScript
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|ScriptTestHelper
operator|.
name|canRunTestOnThisPlatform
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// TODO: fails on some JDL1.6 boxes
comment|//        MockEndpoint mock = getMockEndpoint("mock:result");
comment|//        mock.expectedBodiesReceived("Hello World");
comment|//        mock.expectedHeaderReceived("foo", "Hello World");
comment|//
comment|//        template.sendBodyAndHeader("seda:a", "Hello World", "foo", "London");
comment|//
comment|//        mock.assertIsSatisfied();
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|groovy
argument_list|(
literal|"request.body"
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

