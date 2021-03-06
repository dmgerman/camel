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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|Processor
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
name|After
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
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_comment
comment|/**  * Unit test to verify that the noop file strategy usage of lock files.  */
end_comment

begin_class
DECL|class|FileNoOpLockFileTest
specifier|public
class|class
name|FileNoOpLockFileTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data/reports"
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLocked ()
specifier|public
name|void
name|testLocked
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"Hello Locked"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/reports/locked"
argument_list|,
literal|"Hello Locked"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// sleep to let file consumer do its unlocking
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
name|existsLockFile
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// should be deleted after processing
name|checkLockFile
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotLocked ()
specifier|public
name|void
name|testNotLocked
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"Hello Not Locked"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/reports/notlocked"
argument_list|,
literal|"Hello Not Locked"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// sleep to let file consumer do its unlocking
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
name|existsLockFile
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// no lock files should exists after processing
name|checkLockFile
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|existsLockFile (boolean expected)
specifier|private
specifier|static
name|boolean
name|existsLockFile
parameter_list|(
name|boolean
name|expected
parameter_list|)
block|{
name|String
name|filename
init|=
literal|"target/data/reports/"
decl_stmt|;
name|filename
operator|+=
name|expected
condition|?
literal|"locked/"
else|:
literal|"notlocked/"
expr_stmt|;
name|filename
operator|+=
literal|"report.txt"
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
return|return
name|expected
operator|==
name|file
operator|.
name|exists
argument_list|()
return|;
block|}
DECL|method|checkLockFile (boolean expected)
specifier|private
specifier|static
name|void
name|checkLockFile
parameter_list|(
name|boolean
name|expected
parameter_list|)
block|{
name|String
name|filename
init|=
literal|"target/data/reports/"
decl_stmt|;
name|filename
operator|+=
name|expected
condition|?
literal|"locked/"
else|:
literal|"notlocked/"
expr_stmt|;
name|filename
operator|+=
literal|"report.txt"
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Lock file should "
operator|+
operator|(
name|expected
condition|?
literal|"exists"
else|:
literal|"not exists"
operator|)
argument_list|,
name|expected
argument_list|,
name|file
operator|.
name|exists
argument_list|()
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// for locks
name|from
argument_list|(
literal|"file://target/data/reports/locked/?initialDelay=0&delay=10&noop=true&readLock=markerFile"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyNoopProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:report"
argument_list|)
expr_stmt|;
comment|// for no locks
name|from
argument_list|(
literal|"file://target/data/reports/notlocked/?initialDelay=0&delay=10&noop=true&readLock=none"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyNoopProcessor
argument_list|()
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
DECL|class|MyNoopProcessor
specifier|private
specifier|static
class|class
name|MyNoopProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|body
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
name|boolean
name|locked
init|=
literal|"Hello Locked"
operator|.
name|equals
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|checkLockFile
argument_list|(
name|locked
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

