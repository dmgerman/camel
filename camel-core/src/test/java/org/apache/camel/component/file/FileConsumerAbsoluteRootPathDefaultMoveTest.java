begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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

begin_comment
comment|/**  * Unit test for consuming from an absolute path at root  */
end_comment

begin_class
DECL|class|FileConsumerAbsoluteRootPathDefaultMoveTest
specifier|public
class|class
name|FileConsumerAbsoluteRootPathDefaultMoveTest
extends|extends
name|ContextTestSupport
block|{
comment|// run this test manually
DECL|field|base
specifier|private
name|String
name|base
init|=
literal|"/tmp/mytemp"
decl_stmt|;
DECL|method|testDummy ()
specifier|public
name|void
name|testDummy
parameter_list|()
block|{
comment|// noop
block|}
DECL|method|xxxTestConsumeFromAbsolutePath ()
specifier|public
name|void
name|xxxTestConsumeFromAbsolutePath
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
name|base
argument_list|)
expr_stmt|;
name|context
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:report"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Paris"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|base
operator|+
literal|"/.camel/paris.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:"
operator|+
name|base
argument_list|,
literal|"Hello Paris"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"paris.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
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
literal|"file:"
operator|+
name|base
operator|+
literal|"?initialDelay=0&delay=10"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:report"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

