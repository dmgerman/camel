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
comment|/**  *  */
end_comment

begin_class
DECL|class|NewFileConsumerTest
specifier|public
class|class
name|NewFileConsumerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myFile
specifier|private
name|MyFileEndpoint
name|myFile
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
literal|"target/data/myfile"
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
DECL|method|testNewFileConsumer ()
specifier|public
name|void
name|testNewFileConsumer
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
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/myfile"
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|await
argument_list|(
literal|"postPollCheck invocation"
argument_list|)
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
name|myFile
operator|::
name|isPost
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
name|myFile
operator|=
operator|new
name|MyFileEndpoint
argument_list|()
expr_stmt|;
name|myFile
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|myFile
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/data/myfile"
argument_list|)
argument_list|)
expr_stmt|;
name|myFile
operator|.
name|setDelay
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|myFile
operator|.
name|setInitialDelay
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|myFile
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
DECL|class|MyFileEndpoint
specifier|private
class|class
name|MyFileEndpoint
extends|extends
name|FileEndpoint
block|{
DECL|field|post
specifier|private
specifier|volatile
name|boolean
name|post
decl_stmt|;
DECL|method|newFileConsumer (Processor processor, GenericFileOperations<File> operations)
specifier|protected
name|FileConsumer
name|newFileConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|)
block|{
return|return
operator|new
name|FileConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|operations
argument_list|,
name|createGenericFileStrategy
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|postPollCheck
parameter_list|(
name|int
name|polledMessages
parameter_list|)
block|{
name|post
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|isPost ()
specifier|public
name|boolean
name|isPost
parameter_list|()
block|{
return|return
name|post
return|;
block|}
block|}
block|}
end_class

end_unit

