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
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileLock
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Unit test to verify exclusive read - that we do not poll files that is in progress of being written.  */
end_comment

begin_class
DECL|class|FileExclusiveReadRenameStrategyTest
specifier|public
class|class
name|FileExclusiveReadRenameStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FileExclusiveReadRenameStrategyTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ON_WINDOWS
specifier|private
specifier|static
specifier|final
name|boolean
name|ON_WINDOWS
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
decl_stmt|;
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/exclusiveread/slowfile?consumer.delay=500&readLock=rename"
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"./target/exclusiveread"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"./target/exclusiveread/slowfile"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|testPoolIn3SecondsButNoFiles ()
specifier|public
name|void
name|testPoolIn3SecondsButNoFiles
parameter_list|()
throws|throws
name|Exception
block|{
comment|// can only be tested on Windows
if|if
condition|(
operator|!
name|ON_WINDOWS
condition|)
block|{
return|return;
block|}
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
literal|0
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setMinimumResultWaitTime
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3
operator|*
literal|1000L
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testPollFileWhileSlowFileIsBeingWritten ()
specifier|public
name|void
name|testPollFileWhileSlowFileIsBeingWritten
parameter_list|()
throws|throws
name|Exception
block|{
comment|// can only be tested on Windows
if|if
condition|(
operator|!
name|ON_WINDOWS
condition|)
block|{
return|return;
block|}
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"seda:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MySlowFileProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|fileUrl
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
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
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello WorldLine #0Line #1Line #2Bye World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setMinimumResultWaitTime
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// send a message to seda:start to trigger the creating of the slowfile to poll
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"Create the slow file"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testPollFileWhileSlowFileIsBeingWrittenWithTimeout ()
specifier|public
name|void
name|testPollFileWhileSlowFileIsBeingWrittenWithTimeout
parameter_list|()
throws|throws
name|Exception
block|{
comment|// can only be tested on Windows
if|if
condition|(
operator|!
name|ON_WINDOWS
condition|)
block|{
return|return;
block|}
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"seda:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MySlowFileProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|fileUrl
operator|+
literal|"&readLockTimeout=1000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
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
literal|0
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setMinimumResultWaitTime
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// send a message to seda:start to trigger the creating of the slowfile to poll
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"Create the slow file"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testPollFileWhileSlowFileIsBeingWrittenWithTimeoutAndNoop ()
specifier|public
name|void
name|testPollFileWhileSlowFileIsBeingWrittenWithTimeoutAndNoop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// can only be tested on Windows
if|if
condition|(
operator|!
name|ON_WINDOWS
condition|)
block|{
return|return;
block|}
comment|// to test that if noop and thus idempotent we will retry to consume the file
comment|// the 2nd. time since the first time we could not get the read lock due timeout
comment|// so the file should only be marked in the idempotent repository if we could process it
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"seda:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MySlowFileProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|fileUrl
operator|+
literal|"&readLockTimeout=1000&noop=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
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
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setMinimumResultWaitTime
argument_list|(
literal|2500
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// send a message to seda:start to trigger the creating of the slowfile to poll
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"Create the slow file"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MySlowFileProcessor
specifier|private
class|class
name|MySlowFileProcessor
implements|implements
name|Processor
block|{
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating a slow file ..."
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./target/exclusiveread/slowfile/hello.txt"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|FileLock
name|lock
init|=
name|fos
operator|.
name|getChannel
argument_list|()
operator|.
name|lock
argument_list|()
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|"Line #"
operator|+
name|i
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Appending to slowfile"
argument_list|)
expr_stmt|;
block|}
name|fos
operator|.
name|write
argument_list|(
literal|"Bye World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"... done creating slowfile"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

