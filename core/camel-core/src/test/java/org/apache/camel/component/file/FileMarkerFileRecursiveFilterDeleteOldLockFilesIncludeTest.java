begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|FileMarkerFileRecursiveFilterDeleteOldLockFilesIncludeTest
specifier|public
class|class
name|FileMarkerFileRecursiveFilterDeleteOldLockFilesIncludeTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data/oldlock"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteOldLockOnStartup ()
specifier|public
name|void
name|testDeleteOldLockOnStartup
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|,
literal|"Hi World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bye.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"gooday.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/oldlock/bar/davs.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/oldlock/bar/davs.txt"
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/oldlock"
argument_list|,
literal|"locked"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/oldlock"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bye.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/oldlock/foo"
argument_list|,
literal|"locked"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"gooday.txt"
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/oldlock/foo"
argument_list|,
literal|"Hi World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"gooday.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/oldlock/bar"
argument_list|,
literal|"locked"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"davs.txt"
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/oldlock/bar"
argument_list|,
literal|"Davs World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"davs.txt"
argument_list|)
expr_stmt|;
comment|// start the route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// the lock files should be gone
name|assertFileNotExists
argument_list|(
literal|"target/data/oldlock/hello.txt."
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
argument_list|)
expr_stmt|;
name|assertFileNotExists
argument_list|(
literal|"target/data/oldlock/foo/hegooddayllo.txt."
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
argument_list|)
expr_stmt|;
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
literal|"file:target/data/oldlock?initialDelay=0&delay=10&recursive=true&sortBy=file:name&include=.*(hello.txt|bye.txt|gooday.txt)$"
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

