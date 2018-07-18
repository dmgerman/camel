begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.web3j.springboot
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
operator|.
name|springboot
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
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

begin_comment
comment|/**  * The web3j component uses the Web3j client API and allows you to add/read  * nodes to/from a web3j compliant content repositories.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.web3j"
argument_list|)
DECL|class|Web3jComponentConfiguration
specifier|public
class|class
name|Web3jComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the web3j component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Default configuration      */
DECL|field|configuration
specifier|private
name|Web3jConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|Web3jConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( Web3jConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Web3jConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|Web3jConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|Web3jConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
name|Web3jConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * The preconfigured Web3j object.          */
DECL|field|web3j
specifier|private
name|Web3j
name|web3j
decl_stmt|;
comment|/**          * The priority of a whisper message.          */
DECL|field|priority
specifier|private
name|BigInteger
name|priority
decl_stmt|;
comment|/**          * The time to live in seconds of a whisper message.          */
DECL|field|ttl
specifier|private
name|BigInteger
name|ttl
decl_stmt|;
comment|/**          * Gas price used for each paid gas.          */
DECL|field|gasPrice
specifier|private
name|BigInteger
name|gasPrice
decl_stmt|;
comment|/**          * The maximum gas allowed in this block.          */
DECL|field|gasLimit
specifier|private
name|BigInteger
name|gasLimit
decl_stmt|;
comment|/**          * The value sent within a transaction.          */
DECL|field|value
specifier|private
name|BigInteger
name|value
decl_stmt|;
comment|/**          * The compiled code of a contract OR the hash of the invoked method          * signature and encoded parameters.          */
DECL|field|data
specifier|private
name|String
name|data
decl_stmt|;
comment|/**          * The address the transaction is send from          */
DECL|field|fromAddress
specifier|private
name|String
name|fromAddress
decl_stmt|;
comment|/**          * The address the transaction is directed to.          */
DECL|field|toAddress
specifier|private
name|String
name|toAddress
decl_stmt|;
comment|/**          * A random hexadecimal(32 bytes) ID identifying the client.          */
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**          * A hexadecimal string representation (32 bytes) of the hash rate.          */
DECL|field|hashrate
specifier|private
name|String
name|hashrate
decl_stmt|;
comment|/**          * The mix digest (256 bits) used for submitting a proof-of-work          * solution.          */
DECL|field|mixDigest
specifier|private
name|String
name|mixDigest
decl_stmt|;
comment|/**          * The header's pow-hash (256 bits) used for submitting a proof-of-work          * solution.          */
DECL|field|headerPowHash
specifier|private
name|String
name|headerPowHash
decl_stmt|;
comment|/**          * The nonce found (64 bits) used for submitting a proof-of-work          * solution.          */
DECL|field|nonce
specifier|private
name|String
name|nonce
decl_stmt|;
comment|/**          * The source code to compile.          */
DECL|field|sourceCode
specifier|private
name|String
name|sourceCode
decl_stmt|;
comment|/**          * The information about a transaction requested by transaction hash.          */
DECL|field|transactionHash
specifier|private
name|String
name|transactionHash
decl_stmt|;
comment|/**          * The local database name.          */
DECL|field|databaseName
specifier|private
name|String
name|databaseName
decl_stmt|;
comment|/**          * The key name in the database.          */
DECL|field|keyName
specifier|private
name|String
name|keyName
decl_stmt|;
comment|/**          * The filter id to use.          */
DECL|field|filterId
specifier|private
name|BigInteger
name|filterId
decl_stmt|;
comment|/**          * The transactions/uncle index position in the block.          */
DECL|field|index
specifier|private
name|BigInteger
name|index
decl_stmt|;
comment|/**          * The signed transaction data for a new message call transaction or a          * contract creation for signed transactions.          */
DECL|field|signedTransactionData
specifier|private
name|String
name|signedTransactionData
decl_stmt|;
comment|/**          * Hash of the block where this transaction was in.          */
DECL|field|blockHash
specifier|private
name|String
name|blockHash
decl_stmt|;
comment|/**          * Message to sign by calculating an Ethereum specific signature.          */
DECL|field|sha3HashOfDataToSign
specifier|private
name|String
name|sha3HashOfDataToSign
decl_stmt|;
comment|/**          * The transaction index position withing a block.          */
DECL|field|position
specifier|private
name|BigInteger
name|position
decl_stmt|;
comment|/**          * Contract address or a list of addresses.          */
DECL|field|addresses
specifier|private
name|List
name|addresses
decl_stmt|;
comment|/**          * Topics are order-dependent. Each topic can also be a list of topics.          * Specify multiple topics separated by comma.          */
DECL|field|topics
specifier|private
name|List
name|topics
decl_stmt|;
comment|/**          * Contract address.          */
DECL|field|address
specifier|private
name|String
name|address
decl_stmt|;
comment|/**          * If true it returns the full transaction objects, if false only the          * hashes of the transactions.          */
DECL|field|fullTransactionObjects
specifier|private
name|Boolean
name|fullTransactionObjects
init|=
literal|false
decl_stmt|;
comment|/**          * Operation to use.          */
DECL|field|operation
specifier|private
name|String
name|operation
init|=
literal|"transaction"
decl_stmt|;
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
DECL|method|getAddresses ()
specifier|public
name|List
name|getAddresses
parameter_list|()
block|{
return|return
name|addresses
return|;
block|}
DECL|method|setAddresses (List addresses)
specifier|public
name|void
name|setAddresses
parameter_list|(
name|List
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
name|getTopics
parameter_list|()
block|{
return|return
name|topics
return|;
block|}
DECL|method|setTopics (List topics)
specifier|public
name|void
name|setTopics
parameter_list|(
name|List
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
DECL|method|getFullTransactionObjects ()
specifier|public
name|Boolean
name|getFullTransactionObjects
parameter_list|()
block|{
return|return
name|fullTransactionObjects
return|;
block|}
DECL|method|setFullTransactionObjects (Boolean fullTransactionObjects)
specifier|public
name|void
name|setFullTransactionObjects
parameter_list|(
name|Boolean
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
block|}
block|}
end_class

end_unit

