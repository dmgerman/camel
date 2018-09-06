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
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|ExchangeIdempotentRepository
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ExchangeIdempotentConsumerTest
specifier|public
class|class
name|ExchangeIdempotentConsumerTest
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
DECL|field|repo
specifier|private
name|MyIdempotentRepo
name|repo
init|=
operator|new
name|MyIdempotentRepo
argument_list|()
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
DECL|method|testDuplicateMessagesAreFilteredOut ()
specifier|public
name|void
name|testDuplicateMessagesAreFilteredOut
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repo
operator|.
name|getExchange
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
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
name|sendMessage
argument_list|(
literal|"3"
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// we used 6 different exchanges
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|repo
operator|.
name|getExchange
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|resultEndpoint
operator|.
name|getExchanges
argument_list|()
control|)
block|{
comment|// should be in repo list
name|assertTrue
argument_list|(
literal|"Should contain the exchange"
argument_list|,
name|repo
operator|.
name|getExchange
argument_list|()
operator|.
name|contains
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|class|MyIdempotentRepo
specifier|private
specifier|final
class|class
name|MyIdempotentRepo
implements|implements
name|ExchangeIdempotentRepository
argument_list|<
name|String
argument_list|>
block|{
DECL|field|delegate
specifier|private
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|delegate
decl_stmt|;
DECL|field|exchanges
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|exchanges
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|MyIdempotentRepo ()
specifier|private
name|MyIdempotentRepo
parameter_list|()
block|{
name|delegate
operator|=
name|MemoryIdempotentRepository
operator|.
name|memoryIdempotentRepository
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (Exchange exchange, String key)
specifier|public
name|boolean
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|exchanges
operator|.
name|add
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|add
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Exchange exchange, String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|exchanges
operator|.
name|add
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|contains
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Exchange exchange, String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|exchanges
operator|.
name|add
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|confirm (Exchange exchange, String key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|exchanges
operator|.
name|add
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|confirm
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|delegate
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (String key)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|key
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|confirm (String key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|String
name|key
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
DECL|method|getExchange ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getExchange
parameter_list|()
block|{
return|return
name|exchanges
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
block|}
end_class

end_unit

