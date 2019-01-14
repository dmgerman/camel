begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Consumer
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
name|Producer
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
name|DefaultEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Web3jService
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
name|methods
operator|.
name|request
operator|.
name|EthFilter
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
name|request
operator|.
name|Filter
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
name|request
operator|.
name|ShhFilter
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
name|http
operator|.
name|HttpService
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
name|ipc
operator|.
name|UnixIpcService
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
name|ipc
operator|.
name|WindowsIpcService
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

begin_comment
comment|/**  * The web3j component uses the Web3j client API and allows you to add/read nodes to/from a web3j compliant content repositories.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.22.0"
argument_list|,
name|scheme
operator|=
literal|"web3j"
argument_list|,
name|title
operator|=
literal|"Web3j Ethereum Blockchain"
argument_list|,
name|syntax
operator|=
literal|"web3j:nodeAddress"
argument_list|,
name|label
operator|=
literal|"bitcoin,blockchain"
argument_list|)
DECL|class|Web3jEndpoint
specifier|public
class|class
name|Web3jEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Web3jEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|web3j
specifier|private
specifier|final
name|Web3j
name|web3j
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|nodeAddress
specifier|private
name|String
name|nodeAddress
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|Web3jConfiguration
name|configuration
decl_stmt|;
DECL|method|Web3jEndpoint (String uri, String remaining, Web3jComponent component, Web3jConfiguration configuration)
specifier|public
name|Web3jEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Web3jComponent
name|component
parameter_list|,
name|Web3jConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|nodeAddress
operator|=
name|remaining
expr_stmt|;
name|this
operator|.
name|web3j
operator|=
name|buildService
argument_list|(
name|remaining
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|Web3jProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Web3jConsumer
name|consumer
init|=
operator|new
name|Web3jConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
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
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|buildService (String clientAddress, Web3jConfiguration configuration)
specifier|private
name|Web3j
name|buildService
parameter_list|(
name|String
name|clientAddress
parameter_list|,
name|Web3jConfiguration
name|configuration
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Building service for endpoint: {}"
argument_list|,
name|clientAddress
operator|+
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getWeb3j
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|configuration
operator|.
name|getWeb3j
argument_list|()
return|;
block|}
name|Web3jService
name|web3jService
decl_stmt|;
if|if
condition|(
name|clientAddress
operator|==
literal|null
operator|||
name|clientAddress
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|web3jService
operator|=
operator|new
name|HttpService
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clientAddress
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
name|web3jService
operator|=
operator|new
name|HttpService
argument_list|(
name|clientAddress
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"win"
argument_list|)
condition|)
block|{
name|web3jService
operator|=
operator|new
name|WindowsIpcService
argument_list|(
name|clientAddress
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|web3jService
operator|=
operator|new
name|UnixIpcService
argument_list|(
name|clientAddress
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|isQuorumAPI
argument_list|()
condition|)
block|{
return|return
name|Quorum
operator|.
name|build
argument_list|(
name|web3jService
argument_list|)
return|;
block|}
return|return
name|Web3j
operator|.
name|build
argument_list|(
name|web3jService
argument_list|)
return|;
block|}
DECL|method|getNodeAddress ()
specifier|public
name|String
name|getNodeAddress
parameter_list|()
block|{
return|return
name|nodeAddress
return|;
block|}
comment|/**      * Sets the node address used to communicate      */
DECL|method|setNodeAddress (String nodeAddress)
specifier|public
name|void
name|setNodeAddress
parameter_list|(
name|String
name|nodeAddress
parameter_list|)
block|{
name|this
operator|.
name|nodeAddress
operator|=
name|nodeAddress
expr_stmt|;
block|}
DECL|method|buildEthFilter (DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock, List<String> addresses, List<String> topics)
specifier|public
specifier|static
name|EthFilter
name|buildEthFilter
parameter_list|(
name|DefaultBlockParameter
name|fromBlock
parameter_list|,
name|DefaultBlockParameter
name|toBlock
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|addresses
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|topics
parameter_list|)
block|{
name|EthFilter
name|filter
init|=
operator|new
name|EthFilter
argument_list|(
name|fromBlock
argument_list|,
name|toBlock
argument_list|,
name|addresses
argument_list|)
decl_stmt|;
name|addTopics
argument_list|(
name|filter
argument_list|,
name|topics
argument_list|)
expr_stmt|;
return|return
name|filter
return|;
block|}
DECL|method|buildShhFilter (String data, List<String> topics)
specifier|public
specifier|static
name|ShhFilter
name|buildShhFilter
parameter_list|(
name|String
name|data
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|topics
parameter_list|)
block|{
name|ShhFilter
name|filter
init|=
operator|new
name|ShhFilter
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|addTopics
argument_list|(
name|filter
argument_list|,
name|topics
argument_list|)
expr_stmt|;
return|return
name|filter
return|;
block|}
DECL|method|addTopics (Filter filter, List<String> topics)
specifier|private
specifier|static
name|void
name|addTopics
parameter_list|(
name|Filter
name|filter
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|topics
parameter_list|)
block|{
if|if
condition|(
name|topics
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|topic
range|:
name|topics
control|)
block|{
if|if
condition|(
name|topic
operator|!=
literal|null
operator|&&
name|topic
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|filter
operator|.
name|addSingleTopic
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|filter
operator|.
name|addNullTopic
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

