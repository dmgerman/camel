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
name|IOException
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
name|LoggingLevel
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|FileConsumerBridgeRouteExceptionHandlerTest
specifier|public
class|class
name|FileConsumerBridgeRouteExceptionHandlerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myReadLockStrategy
specifier|private
name|MyReadLockStrategy
name|myReadLockStrategy
init|=
operator|new
name|MyReadLockStrategy
argument_list|()
decl_stmt|;
DECL|method|testCustomExceptionHandler ()
specifier|public
name|void
name|testCustomExceptionHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Error Forced to simulate no space on device"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/nospace"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/nospace"
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should pickup bye.txt file 2 times"
argument_list|,
literal|2
argument_list|,
name|myReadLockStrategy
operator|.
name|getCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myReadLockStrategy"
argument_list|,
name|myReadLockStrategy
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
comment|// START SNIPPET: e2
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
comment|// to handle any IOException being thrown
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|log
argument_list|(
literal|"IOException occurred due: ${exception.message}"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"Error ${exception.message}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
expr_stmt|;
comment|// this is the file route that pickup files, notice how we bridge the consumer to use the Camel routing error handler
comment|// the exclusiveReadLockStrategy is only configured because this is from an unit test, so we use that to simulate exceptions
name|from
argument_list|(
literal|"file:target/nospace?exclusiveReadLockStrategy=#myReadLockStrategy&consumer.bridgeErrorHandler=true"
argument_list|)
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
comment|// END SNIPPET: e2
comment|// used for simulating exception during acquiring a lock on the file
DECL|class|MyReadLockStrategy
specifier|private
specifier|static
class|class
name|MyReadLockStrategy
implements|implements
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|File
argument_list|>
block|{
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
annotation|@
name|Override
DECL|method|prepareOnStartup (GenericFileOperations<File> operations, GenericFileEndpoint<File> endpoint)
specifier|public
name|void
name|prepareOnStartup
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|acquireExclusiveReadLock (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|boolean
name|acquireExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|file
operator|.
name|getFileNameOnly
argument_list|()
operator|.
name|equals
argument_list|(
literal|"bye.txt"
argument_list|)
condition|)
block|{
if|if
condition|(
name|counter
operator|++
operator|==
literal|0
condition|)
block|{
comment|// force an exception on acquire attempt for the bye.txt file, on the first attempt
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Forced to simulate no space on device"
argument_list|)
throw|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnAbort (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnAbort
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnRollback (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnRollback
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnCommit (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnCommit
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|setCheckInterval (long checkInterval)
specifier|public
name|void
name|setCheckInterval
parameter_list|(
name|long
name|checkInterval
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|setReadLockLoggingLevel (LoggingLevel readLockLoggingLevel)
specifier|public
name|void
name|setReadLockLoggingLevel
parameter_list|(
name|LoggingLevel
name|readLockLoggingLevel
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|setMarkerFiler (boolean markerFile)
specifier|public
name|void
name|setMarkerFiler
parameter_list|(
name|boolean
name|markerFile
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|setDeleteOrphanLockFiles (boolean deleteOrphanLockFiles)
specifier|public
name|void
name|setDeleteOrphanLockFiles
parameter_list|(
name|boolean
name|deleteOrphanLockFiles
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|getCounter ()
specifier|public
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
return|;
block|}
block|}
block|}
end_class

end_unit

