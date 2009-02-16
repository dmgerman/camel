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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|FileAsyncRouteTest
specifier|public
class|class
name|FileAsyncRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|expectedBody
specifier|protected
name|Object
name|expectedBody
init|=
literal|"Hello there!"
decl_stmt|;
DECL|field|uri
specifier|protected
name|String
name|uri
init|=
literal|"file:target/test-async-inbox?delete=true&consumer.delay=10000&recursive=true"
decl_stmt|;
DECL|field|receivedLatch
specifier|private
name|CountDownLatch
name|receivedLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|processingLatch
specifier|private
name|CountDownLatch
name|processingLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|file
specifier|private
name|AtomicReference
argument_list|<
name|File
argument_list|>
name|file
init|=
operator|new
name|AtomicReference
argument_list|<
name|File
argument_list|>
argument_list|()
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
literal|"target/test-async-inbox"
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
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|processingLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|receivedLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|testFileRoute ()
specifier|public
name|void
name|testFileRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultWaitTime
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
name|expectedBody
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
comment|// Wait till the exchange is delivered to the processor
name|assertTrue
argument_list|(
literal|"Async processor received exchange"
argument_list|,
name|receivedLatch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|this
operator|.
name|file
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// The file consumer support async processing of the exchange,
comment|// so the file should not get deleted until the exchange
comment|// finishes being asynchronously processed.
name|assertTrue
argument_list|(
literal|"File should exist"
argument_list|,
name|file
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Release the async processing thread so that the exchange completes
comment|// and the file gets deleted.
name|processingLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"File should not exist"
argument_list|,
name|file
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|from
argument_list|(
name|uri
argument_list|)
operator|.
name|thread
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|GenericFile
operator|.
name|class
argument_list|)
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|file
operator|.
name|set
argument_list|(
operator|(
name|File
operator|)
name|body
argument_list|)
expr_stmt|;
comment|// Simulate a processing delay..
name|receivedLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|processingLatch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
block|}
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

