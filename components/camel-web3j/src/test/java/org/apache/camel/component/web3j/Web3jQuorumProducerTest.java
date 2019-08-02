begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.web3j
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|web3j
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
name|BindToRegistry
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
name|Produce
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
name|ProducerTemplate
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|protocol
operator|.
name|core
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|protocol
operator|.
name|core
operator|.
name|methods
operator|.
name|response
operator|.
name|EthSendTransaction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|protocol
operator|.
name|core
operator|.
name|methods
operator|.
name|response
operator|.
name|VoidResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|quorum
operator|.
name|Quorum
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|quorum
operator|.
name|methods
operator|.
name|response
operator|.
name|BlockMaker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|quorum
operator|.
name|methods
operator|.
name|response
operator|.
name|CanonicalHash
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|quorum
operator|.
name|methods
operator|.
name|response
operator|.
name|MakeBlock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|quorum
operator|.
name|methods
operator|.
name|response
operator|.
name|PrivatePayload
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|quorum
operator|.
name|methods
operator|.
name|response
operator|.
name|QuorumNodeInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|quorum
operator|.
name|methods
operator|.
name|response
operator|.
name|Vote
import|;
end_import

begin_import
import|import
name|org
operator|.
name|web3j
operator|.
name|quorum
operator|.
name|methods
operator|.
name|response
operator|.
name|Voter
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|web3j
operator|.
name|Web3jConstants
operator|.
name|OPERATION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_class
DECL|class|Web3jQuorumProducerTest
specifier|public
class|class
name|Web3jQuorumProducerTest
extends|extends
name|Web3jMockTestSupport
block|{
annotation|@
name|Mock
annotation|@
name|BindToRegistry
argument_list|(
literal|"mockQuorum"
argument_list|)
DECL|field|mockQuorum
specifier|protected
name|Quorum
name|mockQuorum
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Mock
DECL|field|request
specifier|protected
name|Request
name|request
decl_stmt|;
DECL|method|getUrl ()
specifier|protected
name|String
name|getUrl
parameter_list|()
block|{
return|return
literal|"web3j://http://127.0.0.1:8545?web3j=#mockQuorum&quorumAPI=true&"
return|;
block|}
annotation|@
name|Override
DECL|method|isUseAdviceWith ()
specifier|public
name|boolean
name|isUseAdviceWith
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|//Quorum API tests
annotation|@
name|Test
DECL|method|quorumNodeInfoTest ()
specifier|public
name|void
name|quorumNodeInfoTest
parameter_list|()
throws|throws
name|Exception
block|{
name|QuorumNodeInfo
operator|.
name|NodeInfo
name|nodeInfo
init|=
operator|new
name|QuorumNodeInfo
operator|.
name|NodeInfo
argument_list|()
decl_stmt|;
name|QuorumNodeInfo
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|QuorumNodeInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumNodeInfo
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|getNodeInfo
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|nodeInfo
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_NODE_INFO
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|QuorumNodeInfo
operator|.
name|NodeInfo
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|QuorumNodeInfo
operator|.
name|NodeInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumCanonicalHashTest ()
specifier|public
name|void
name|quorumCanonicalHashTest
parameter_list|()
throws|throws
name|Exception
block|{
name|CanonicalHash
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|CanonicalHash
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumCanonicalHash
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|getCanonicalHash
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"4444"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|"1234567890"
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_CANONICAL_HASH
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"4444"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumVoteTest ()
specifier|public
name|void
name|quorumVoteTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Vote
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Vote
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumVote
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|getTransactionHash
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_VOTE
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumMakeBlockTest ()
specifier|public
name|void
name|quorumMakeBlockTest
parameter_list|()
throws|throws
name|Exception
block|{
name|MakeBlock
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MakeBlock
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumMakeBlock
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|getBlockHash
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_MAKE_BLOCK
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumPauseBlockMakerTest ()
specifier|public
name|void
name|quorumPauseBlockMakerTest
parameter_list|()
throws|throws
name|Exception
block|{
name|VoidResponse
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|VoidResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumPauseBlockMaker
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|isValid
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_PAUSE_BLOCK_MAKER
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Boolean
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumResumeBlockMakerTest ()
specifier|public
name|void
name|quorumResumeBlockMakerTest
parameter_list|()
throws|throws
name|Exception
block|{
name|VoidResponse
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|VoidResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumResumeBlockMaker
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|isValid
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_RESUME_BLOCK_MAKER
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Boolean
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumIsBlockMakerTest ()
specifier|public
name|void
name|quorumIsBlockMakerTest
parameter_list|()
throws|throws
name|Exception
block|{
name|BlockMaker
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|BlockMaker
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumIsBlockMaker
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|isBlockMaker
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_IS_BLOCK_MAKER
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Boolean
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumIsVoterTest ()
specifier|public
name|void
name|quorumIsVoterTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Voter
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Voter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumIsVoter
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|isVoter
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_IS_VOTER
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Boolean
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumGetPrivatePayloadTest ()
specifier|public
name|void
name|quorumGetPrivatePayloadTest
parameter_list|()
throws|throws
name|Exception
block|{
name|PrivatePayload
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|PrivatePayload
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|quorumGetPrivatePayload
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|getPrivatePayload
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|"foo"
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_GET_PRIVATE_PAYLOAD
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|quorumEthSendTransactionTest ()
specifier|public
name|void
name|quorumEthSendTransactionTest
parameter_list|()
throws|throws
name|Exception
block|{
name|EthSendTransaction
name|response
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|EthSendTransaction
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockQuorum
operator|.
name|ethSendTransaction
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|request
operator|.
name|send
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|response
operator|.
name|getTransactionHash
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|OPERATION
argument_list|,
name|Web3jConstants
operator|.
name|QUORUM_ETH_SEND_TRANSACTION
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|body
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|getUrl
argument_list|()
operator|+
name|OPERATION
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|"="
operator|+
name|Web3jConstants
operator|.
name|WEB3_CLIENT_VERSION
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

