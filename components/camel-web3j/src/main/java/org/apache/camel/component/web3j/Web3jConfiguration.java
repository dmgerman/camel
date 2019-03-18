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
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|UriParam
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
name|UriParams
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
name|Web3j
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
name|DefaultBlockParameter
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
name|DefaultBlockParameterName
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|Web3jConfiguration
specifier|public
class|class
name|Web3jConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"transaction"
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
init|=
name|Web3jConstants
operator|.
name|TRANSACTION
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
comment|// TODO: Make this an enum
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"latest"
argument_list|)
DECL|field|fromBlock
specifier|private
name|DefaultBlockParameter
name|fromBlock
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"latest"
argument_list|)
DECL|field|toBlock
specifier|private
name|DefaultBlockParameter
name|toBlock
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"latest"
argument_list|)
DECL|field|atBlock
specifier|private
name|DefaultBlockParameter
name|atBlock
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|addresses
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|addresses
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|address
specifier|private
name|String
name|address
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|javaType
operator|=
literal|"String"
argument_list|)
DECL|field|topics
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|topics
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|position
specifier|private
name|BigInteger
name|position
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|blockHash
specifier|private
name|String
name|blockHash
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|sha3HashOfDataToSign
specifier|private
name|String
name|sha3HashOfDataToSign
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|signedTransactionData
specifier|private
name|String
name|signedTransactionData
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|index
specifier|private
name|BigInteger
name|index
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|filterId
specifier|private
name|BigInteger
name|filterId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|databaseName
specifier|private
name|String
name|databaseName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|keyName
specifier|private
name|String
name|keyName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|transactionHash
specifier|private
name|String
name|transactionHash
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|sourceCode
specifier|private
name|String
name|sourceCode
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|nonce
specifier|private
name|String
name|nonce
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|headerPowHash
specifier|private
name|String
name|headerPowHash
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|mixDigest
specifier|private
name|String
name|mixDigest
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|hashrate
specifier|private
name|String
name|hashrate
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|fromAddress
specifier|private
name|String
name|fromAddress
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|toAddress
specifier|private
name|String
name|toAddress
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|gasPrice
specifier|private
name|BigInteger
name|gasPrice
decl_stmt|;
annotation|@
name|UriParam
DECL|field|gasLimit
specifier|private
name|BigInteger
name|gasLimit
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|value
specifier|private
name|BigInteger
name|value
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|data
specifier|private
name|String
name|data
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|priority
specifier|private
name|BigInteger
name|priority
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|ttl
specifier|private
name|BigInteger
name|ttl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|web3j
specifier|private
name|Web3j
name|web3j
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|fullTransactionObjects
specifier|private
name|boolean
name|fullTransactionObjects
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|quorumAPI
specifier|private
name|boolean
name|quorumAPI
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|privateFor
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|privateFor
decl_stmt|;
DECL|method|getPrivateFor ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPrivateFor
parameter_list|()
block|{
return|return
name|privateFor
return|;
block|}
comment|/**      *  A transaction privateFor nodes with public keys in a Quorum network      */
DECL|method|setPrivateFor (List<String> privateFor)
specifier|public
name|void
name|setPrivateFor
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|privateFor
parameter_list|)
block|{
name|this
operator|.
name|privateFor
operator|=
name|privateFor
expr_stmt|;
block|}
DECL|method|isQuorumAPI ()
specifier|public
name|boolean
name|isQuorumAPI
parameter_list|()
block|{
return|return
name|quorumAPI
return|;
block|}
comment|/**      *  If true, this will support Quorum API.      */
DECL|method|setQuorumAPI (boolean quorumAPI)
specifier|public
name|void
name|setQuorumAPI
parameter_list|(
name|boolean
name|quorumAPI
parameter_list|)
block|{
name|this
operator|.
name|quorumAPI
operator|=
name|quorumAPI
expr_stmt|;
block|}
DECL|method|getWeb3j ()
specifier|public
name|Web3j
name|getWeb3j
parameter_list|()
block|{
return|return
name|web3j
return|;
block|}
comment|/**      * The preconfigured Web3j object.      */
DECL|method|setWeb3j (Web3j web3j)
specifier|public
name|void
name|setWeb3j
parameter_list|(
name|Web3j
name|web3j
parameter_list|)
block|{
name|this
operator|.
name|web3j
operator|=
name|web3j
expr_stmt|;
block|}
DECL|method|getPriority ()
specifier|public
name|BigInteger
name|getPriority
parameter_list|()
block|{
return|return
name|priority
return|;
block|}
comment|/**      * The priority of a whisper message.      */
DECL|method|setPriority (BigInteger priority)
specifier|public
name|void
name|setPriority
parameter_list|(
name|BigInteger
name|priority
parameter_list|)
block|{
name|this
operator|.
name|priority
operator|=
name|priority
expr_stmt|;
block|}
DECL|method|getTtl ()
specifier|public
name|BigInteger
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
comment|/**      * The time to live in seconds of a whisper message.      */
DECL|method|setTtl (BigInteger ttl)
specifier|public
name|void
name|setTtl
parameter_list|(
name|BigInteger
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
expr_stmt|;
block|}
DECL|method|getGasPrice ()
specifier|public
name|BigInteger
name|getGasPrice
parameter_list|()
block|{
return|return
name|gasPrice
return|;
block|}
comment|/**      * Gas price used for each paid gas.      */
DECL|method|setGasPrice (BigInteger gasPrice)
specifier|public
name|void
name|setGasPrice
parameter_list|(
name|BigInteger
name|gasPrice
parameter_list|)
block|{
name|this
operator|.
name|gasPrice
operator|=
name|gasPrice
expr_stmt|;
block|}
DECL|method|getGasLimit ()
specifier|public
name|BigInteger
name|getGasLimit
parameter_list|()
block|{
return|return
name|gasLimit
return|;
block|}
comment|/**      * The maximum gas allowed in this block.      */
DECL|method|setGasLimit (BigInteger gasLimit)
specifier|public
name|void
name|setGasLimit
parameter_list|(
name|BigInteger
name|gasLimit
parameter_list|)
block|{
name|this
operator|.
name|gasLimit
operator|=
name|gasLimit
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|BigInteger
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * The value sent within a transaction.      */
DECL|method|setValue (BigInteger value)
specifier|public
name|void
name|setValue
parameter_list|(
name|BigInteger
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getData ()
specifier|public
name|String
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
comment|/**      * The compiled code of a contract OR the hash of the invoked method signature and encoded parameters.      */
DECL|method|setData (String data)
specifier|public
name|void
name|setData
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
DECL|method|getFromAddress ()
specifier|public
name|String
name|getFromAddress
parameter_list|()
block|{
return|return
name|fromAddress
return|;
block|}
comment|/**      * The address the transaction is send from      */
DECL|method|setFromAddress (String fromAddress)
specifier|public
name|void
name|setFromAddress
parameter_list|(
name|String
name|fromAddress
parameter_list|)
block|{
name|this
operator|.
name|fromAddress
operator|=
name|fromAddress
expr_stmt|;
block|}
DECL|method|getToAddress ()
specifier|public
name|String
name|getToAddress
parameter_list|()
block|{
return|return
name|toAddress
return|;
block|}
comment|/**      * The address the transaction is directed to.      */
DECL|method|setToAddress (String toAddress)
specifier|public
name|void
name|setToAddress
parameter_list|(
name|String
name|toAddress
parameter_list|)
block|{
name|this
operator|.
name|toAddress
operator|=
name|toAddress
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
comment|/**      * A random hexadecimal(32 bytes) ID identifying the client.      */
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getHashrate ()
specifier|public
name|String
name|getHashrate
parameter_list|()
block|{
return|return
name|hashrate
return|;
block|}
comment|/**      * A hexadecimal string representation (32 bytes) of the hash rate.      */
DECL|method|setHashrate (String hashrate)
specifier|public
name|void
name|setHashrate
parameter_list|(
name|String
name|hashrate
parameter_list|)
block|{
name|this
operator|.
name|hashrate
operator|=
name|hashrate
expr_stmt|;
block|}
DECL|method|getMixDigest ()
specifier|public
name|String
name|getMixDigest
parameter_list|()
block|{
return|return
name|mixDigest
return|;
block|}
comment|/**      * The mix digest (256 bits) used for submitting a proof-of-work solution.      */
DECL|method|setMixDigest (String mixDigest)
specifier|public
name|void
name|setMixDigest
parameter_list|(
name|String
name|mixDigest
parameter_list|)
block|{
name|this
operator|.
name|mixDigest
operator|=
name|mixDigest
expr_stmt|;
block|}
DECL|method|getHeaderPowHash ()
specifier|public
name|String
name|getHeaderPowHash
parameter_list|()
block|{
return|return
name|headerPowHash
return|;
block|}
comment|/**      * The header's pow-hash (256 bits) used for submitting a proof-of-work solution.      */
DECL|method|setHeaderPowHash (String headerPowHash)
specifier|public
name|void
name|setHeaderPowHash
parameter_list|(
name|String
name|headerPowHash
parameter_list|)
block|{
name|this
operator|.
name|headerPowHash
operator|=
name|headerPowHash
expr_stmt|;
block|}
DECL|method|getNonce ()
specifier|public
name|String
name|getNonce
parameter_list|()
block|{
return|return
name|nonce
return|;
block|}
comment|/**      * The nonce found (64 bits) used for submitting a proof-of-work solution.      */
DECL|method|setNonce (String nonce)
specifier|public
name|void
name|setNonce
parameter_list|(
name|String
name|nonce
parameter_list|)
block|{
name|this
operator|.
name|nonce
operator|=
name|nonce
expr_stmt|;
block|}
DECL|method|getSourceCode ()
specifier|public
name|String
name|getSourceCode
parameter_list|()
block|{
return|return
name|sourceCode
return|;
block|}
comment|/**      * The source code to compile.      */
DECL|method|setSourceCode (String sourceCode)
specifier|public
name|void
name|setSourceCode
parameter_list|(
name|String
name|sourceCode
parameter_list|)
block|{
name|this
operator|.
name|sourceCode
operator|=
name|sourceCode
expr_stmt|;
block|}
DECL|method|getTransactionHash ()
specifier|public
name|String
name|getTransactionHash
parameter_list|()
block|{
return|return
name|transactionHash
return|;
block|}
comment|/**      * The information about a transaction requested by transaction hash.      */
DECL|method|setTransactionHash (String transactionHash)
specifier|public
name|void
name|setTransactionHash
parameter_list|(
name|String
name|transactionHash
parameter_list|)
block|{
name|this
operator|.
name|transactionHash
operator|=
name|transactionHash
expr_stmt|;
block|}
DECL|method|getDatabaseName ()
specifier|public
name|String
name|getDatabaseName
parameter_list|()
block|{
return|return
name|databaseName
return|;
block|}
comment|/**      * The local database name.      */
DECL|method|setDatabaseName (String databaseName)
specifier|public
name|void
name|setDatabaseName
parameter_list|(
name|String
name|databaseName
parameter_list|)
block|{
name|this
operator|.
name|databaseName
operator|=
name|databaseName
expr_stmt|;
block|}
DECL|method|getKeyName ()
specifier|public
name|String
name|getKeyName
parameter_list|()
block|{
return|return
name|keyName
return|;
block|}
comment|/**      * The key name in the database.      */
DECL|method|setKeyName (String keyName)
specifier|public
name|void
name|setKeyName
parameter_list|(
name|String
name|keyName
parameter_list|)
block|{
name|this
operator|.
name|keyName
operator|=
name|keyName
expr_stmt|;
block|}
DECL|method|getFilterId ()
specifier|public
name|BigInteger
name|getFilterId
parameter_list|()
block|{
return|return
name|filterId
return|;
block|}
comment|/**      * The filter id to use.      */
DECL|method|setFilterId (BigInteger filterId)
specifier|public
name|void
name|setFilterId
parameter_list|(
name|BigInteger
name|filterId
parameter_list|)
block|{
name|this
operator|.
name|filterId
operator|=
name|filterId
expr_stmt|;
block|}
DECL|method|getIndex ()
specifier|public
name|BigInteger
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
comment|/**      * The transactions/uncle index position in the block.      */
DECL|method|setIndex (BigInteger index)
specifier|public
name|void
name|setIndex
parameter_list|(
name|BigInteger
name|index
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
DECL|method|getSignedTransactionData ()
specifier|public
name|String
name|getSignedTransactionData
parameter_list|()
block|{
return|return
name|signedTransactionData
return|;
block|}
comment|/**      * The signed transaction data for a new message call transaction or a contract creation for signed transactions.      */
DECL|method|setSignedTransactionData (String signedTransactionData)
specifier|public
name|void
name|setSignedTransactionData
parameter_list|(
name|String
name|signedTransactionData
parameter_list|)
block|{
name|this
operator|.
name|signedTransactionData
operator|=
name|signedTransactionData
expr_stmt|;
block|}
DECL|method|getBlockHash ()
specifier|public
name|String
name|getBlockHash
parameter_list|()
block|{
return|return
name|blockHash
return|;
block|}
comment|/**      * Hash of the block where this transaction was in.      */
DECL|method|setBlockHash (String blockHash)
specifier|public
name|void
name|setBlockHash
parameter_list|(
name|String
name|blockHash
parameter_list|)
block|{
name|this
operator|.
name|blockHash
operator|=
name|blockHash
expr_stmt|;
block|}
DECL|method|getSha3HashOfDataToSign ()
specifier|public
name|String
name|getSha3HashOfDataToSign
parameter_list|()
block|{
return|return
name|sha3HashOfDataToSign
return|;
block|}
comment|/**      * Message to sign by calculating an Ethereum specific signature.      */
DECL|method|setSha3HashOfDataToSign (String sha3HashOfDataToSign)
specifier|public
name|void
name|setSha3HashOfDataToSign
parameter_list|(
name|String
name|sha3HashOfDataToSign
parameter_list|)
block|{
name|this
operator|.
name|sha3HashOfDataToSign
operator|=
name|sha3HashOfDataToSign
expr_stmt|;
block|}
DECL|method|getPosition ()
specifier|public
name|BigInteger
name|getPosition
parameter_list|()
block|{
return|return
name|position
return|;
block|}
comment|/**      * The transaction index position withing a block.      */
DECL|method|setPosition (BigInteger position)
specifier|public
name|void
name|setPosition
parameter_list|(
name|BigInteger
name|position
parameter_list|)
block|{
name|this
operator|.
name|position
operator|=
name|position
expr_stmt|;
block|}
DECL|method|getFromBlock ()
specifier|public
name|DefaultBlockParameter
name|getFromBlock
parameter_list|()
block|{
return|return
name|fromBlock
return|;
block|}
comment|/**      * The block number, or the string "latest" for the last mined block or "pending", "earliest" for not yet mined transactions.      */
DECL|method|setFromBlock (String block)
specifier|public
name|void
name|setFromBlock
parameter_list|(
name|String
name|block
parameter_list|)
block|{
name|this
operator|.
name|fromBlock
operator|=
name|toDefaultBlockParameter
argument_list|(
name|block
argument_list|)
expr_stmt|;
block|}
comment|/**      * The block number, or the string "latest" for the last mined block or "pending", "earliest" for not yet mined transactions.      */
DECL|method|setToBlock (String block)
specifier|public
name|void
name|setToBlock
parameter_list|(
name|String
name|block
parameter_list|)
block|{
name|this
operator|.
name|toBlock
operator|=
name|toDefaultBlockParameter
argument_list|(
name|block
argument_list|)
expr_stmt|;
block|}
DECL|method|toDefaultBlockParameter (String block)
specifier|private
name|DefaultBlockParameter
name|toDefaultBlockParameter
parameter_list|(
name|String
name|block
parameter_list|)
block|{
name|DefaultBlockParameter
name|defaultBlockParameter
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|block
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DefaultBlockParameterName
name|defaultBlockParameterName
range|:
name|DefaultBlockParameterName
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|block
operator|.
name|equalsIgnoreCase
argument_list|(
name|defaultBlockParameterName
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|defaultBlockParameter
operator|=
name|defaultBlockParameterName
expr_stmt|;
block|}
block|}
if|if
condition|(
name|defaultBlockParameter
operator|==
literal|null
condition|)
block|{
name|defaultBlockParameter
operator|=
name|DefaultBlockParameter
operator|.
name|valueOf
argument_list|(
operator|new
name|BigInteger
argument_list|(
name|block
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|defaultBlockParameter
return|;
block|}
DECL|method|getToBlock ()
specifier|public
name|DefaultBlockParameter
name|getToBlock
parameter_list|()
block|{
return|return
name|toBlock
return|;
block|}
DECL|method|getAtBlock ()
specifier|public
name|DefaultBlockParameter
name|getAtBlock
parameter_list|()
block|{
return|return
name|atBlock
return|;
block|}
comment|/**      * The block number, or the string "latest" for the last mined block or "pending", "earliest" for not yet mined transactions.      */
DECL|method|setAtBlock (String block)
specifier|public
name|void
name|setAtBlock
parameter_list|(
name|String
name|block
parameter_list|)
block|{
name|this
operator|.
name|atBlock
operator|=
name|toDefaultBlockParameter
argument_list|(
name|block
argument_list|)
expr_stmt|;
block|}
DECL|method|getAddresses ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAddresses
parameter_list|()
block|{
return|return
name|addresses
return|;
block|}
comment|/**      * Contract address or a list of addresses.      */
DECL|method|setAddresses (List<String> addresses)
specifier|public
name|void
name|setAddresses
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|addresses
parameter_list|)
block|{
name|this
operator|.
name|addresses
operator|=
name|addresses
expr_stmt|;
block|}
DECL|method|getTopics ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTopics
parameter_list|()
block|{
return|return
name|topics
return|;
block|}
comment|/**      * Topics are order-dependent. Each topic can also be a list of topics.      * Specify multiple topics separated by comma.      */
DECL|method|setTopics (List<String> topics)
specifier|public
name|void
name|setTopics
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|topics
parameter_list|)
block|{
name|this
operator|.
name|topics
operator|=
name|topics
expr_stmt|;
block|}
DECL|method|setTopics (String topics)
specifier|public
name|void
name|setTopics
parameter_list|(
name|String
name|topics
parameter_list|)
block|{
name|String
index|[]
name|arr
init|=
name|topics
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|this
operator|.
name|topics
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|arr
argument_list|)
expr_stmt|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
comment|/**      * Contract address.      */
DECL|method|setAddress (String address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
DECL|method|isFullTransactionObjects ()
specifier|public
name|boolean
name|isFullTransactionObjects
parameter_list|()
block|{
return|return
name|fullTransactionObjects
return|;
block|}
comment|/**      *  If true it returns the full transaction objects, if false only the hashes of the transactions.      */
DECL|method|setFullTransactionObjects (boolean fullTransactionObjects)
specifier|public
name|void
name|setFullTransactionObjects
parameter_list|(
name|boolean
name|fullTransactionObjects
parameter_list|)
block|{
name|this
operator|.
name|fullTransactionObjects
operator|=
name|fullTransactionObjects
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Operation to use.      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getOperationOrDefault ()
specifier|public
name|String
name|getOperationOrDefault
parameter_list|()
block|{
return|return
name|this
operator|.
name|operation
operator|!=
literal|null
condition|?
name|operation
else|:
name|Web3jConstants
operator|.
name|TRANSACTION
return|;
block|}
DECL|method|copy ()
specifier|public
name|Web3jConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|Web3jConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

