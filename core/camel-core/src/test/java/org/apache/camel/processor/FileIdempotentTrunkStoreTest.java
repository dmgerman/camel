begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|Endpoint
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
name|Message
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
name|camel
operator|.
name|support
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|FileIdempotentRepository
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
name|spi
operator|.
name|IdempotentRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|collection
operator|.
name|IsIterableContainingInOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|FileIdempotentTrunkStoreTest
specifier|public
class|class
name|FileIdempotentTrunkStoreTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|startEndpoint
specifier|protected
name|Endpoint
name|startEndpoint
decl_stmt|;
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|field|store
specifier|private
name|File
name|store
init|=
operator|new
name|File
argument_list|(
literal|"target/data/idempotentfilestore.dat"
argument_list|)
decl_stmt|;
DECL|field|repo
specifier|private
name|IdempotentRepository
name|repo
decl_stmt|;
annotation|@
name|Test
DECL|method|testTrunkFileStore ()
specifier|public
name|void
name|testTrunkFileStore
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|,
literal|"D"
argument_list|,
literal|"E"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"AAAAAAAAAA"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"BBBBBBBBBB"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"CCCCCCCCCC"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"AAAAAAAAAA"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"DDDDDDDDDD"
argument_list|,
literal|"D"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"BBBBBBBBBB"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"EEEEEEEEEE"
argument_list|,
literal|"E"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Z"
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
comment|// should trunk the file store
name|sendMessage
argument_list|(
literal|"ZZZZZZZZZZ"
argument_list|,
literal|"Z"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"XXXXXXXXXX"
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"XXXXXXXXXX"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check the file should only have the last 2 entries as it was trunked
name|Stream
argument_list|<
name|String
argument_list|>
name|fileContent
init|=
name|Files
operator|.
name|lines
argument_list|(
name|store
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|fileEntries
init|=
name|fileContent
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|fileContent
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//expected order
name|Assert
operator|.
name|assertThat
argument_list|(
name|fileEntries
argument_list|,
name|IsIterableContainingInOrder
operator|.
name|contains
argument_list|(
literal|"ZZZZZZZZZZ"
argument_list|,
literal|"XXXXXXXXXX"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (final Object messageId, final Object body)
specifier|protected
name|void
name|sendMessage
parameter_list|(
specifier|final
name|Object
name|messageId
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
name|startEndpoint
argument_list|,
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
block|{
comment|// now lets fire in a message
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"messageId"
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
comment|// delete file store before testing
if|if
condition|(
name|store
operator|.
name|exists
argument_list|()
condition|)
block|{
name|store
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
comment|// 5 elements in cache, and 50 bytes as max size limit for when trunking should start
name|repo
operator|=
name|FileIdempotentRepository
operator|.
name|fileIdempotentRepository
argument_list|(
name|store
argument_list|,
literal|5
argument_list|,
literal|50
argument_list|)
expr_stmt|;
name|repo
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|startEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"direct:start"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:start"
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|header
argument_list|(
literal|"messageId"
argument_list|)
argument_list|,
name|repo
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
