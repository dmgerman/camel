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
name|processor
operator|.
name|idempotent
operator|.
name|MemoryIdempotentRepository
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|IdempotentConsumerNoMessageIdTest
specifier|public
class|class
name|IdempotentConsumerNoMessageIdTest
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
annotation|@
name|Test
DECL|method|testNoMessageId ()
specifier|public
name|void
name|testNoMessageId
parameter_list|()
throws|throws
name|Exception
block|{
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:dead"
argument_list|)
argument_list|)
expr_stmt|;
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
name|MemoryIdempotentRepository
operator|.
name|memoryIdempotentRepository
argument_list|(
literal|200
argument_list|)
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
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"one"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"2"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
block|}
end_class

end_unit

