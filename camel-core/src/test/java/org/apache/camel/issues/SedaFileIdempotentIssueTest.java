begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
package|;
end_package

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
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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
name|impl
operator|.
name|JndiRegistry
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
name|processor
operator|.
name|idempotent
operator|.
name|FileIdempotentRepository
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SedaFileIdempotentIssueTest
specifier|public
class|class
name|SedaFileIdempotentIssueTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|repository
specifier|private
name|FileIdempotentRepository
name|repository
init|=
operator|new
name|FileIdempotentRepository
argument_list|()
decl_stmt|;
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
literal|"target/inbox"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/inbox"
argument_list|)
expr_stmt|;
comment|// create file without using Camel
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/inbox/hello.txt"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
literal|true
argument_list|)
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
name|fos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
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
name|repository
operator|.
name|setFileStore
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/repo.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"repo"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
name|onException
argument_list|(
name|RuntimeException
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ShutDown
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file:target/inbox?idempotent=true&noop=true&idempotentRepository=#repo&initialDelay=0&delay=10"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:begin"
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"seda:process"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:process"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|RuntimeException
argument_list|(
literal|"Testing with exception"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testRepo ()
specifier|public
name|void
name|testRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|done
init|=
name|latch
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should stop Camel"
argument_list|,
name|done
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No file should be reported consumed"
argument_list|,
literal|0
argument_list|,
name|repository
operator|.
name|getCache
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|ShutDown
specifier|protected
class|class
name|ShutDown
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// shutdown route
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// shutdown camel
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Stopping Camel"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Stopped Camel complete"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
comment|// start shutdown in a separate thread
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

