begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.corda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|client
operator|.
name|rpc
operator|.
name|CordaRPCClient
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|client
operator|.
name|rpc
operator|.
name|CordaRPCConnection
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|messaging
operator|.
name|CordaRPCOps
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|utilities
operator|.
name|NetworkHostAndPort
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
name|UriPath
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

begin_comment
comment|/**  * The corda component uses the corda-rpc to interact with corda nodes.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.23.0"
argument_list|,
name|scheme
operator|=
literal|"corda"
argument_list|,
name|title
operator|=
literal|"corda"
argument_list|,
name|syntax
operator|=
literal|"corda:url"
argument_list|,
name|consumerClass
operator|=
name|CordaConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"corda,blockchain"
argument_list|)
DECL|class|CordaEndpoint
specifier|public
class|class
name|CordaEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"URL to the corda node"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|configuration
specifier|private
name|CordaConfiguration
name|configuration
decl_stmt|;
DECL|field|rpcConnection
specifier|private
name|CordaRPCConnection
name|rpcConnection
decl_stmt|;
DECL|field|proxy
specifier|private
name|CordaRPCOps
name|proxy
decl_stmt|;
DECL|method|CordaEndpoint (String uri, String remaining, CordaComponent component, CordaConfiguration configuration)
specifier|public
name|CordaEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|CordaComponent
name|component
parameter_list|,
name|CordaConfiguration
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
try|try
block|{
name|URI
name|nodeURI
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setHost
argument_list|(
name|nodeURI
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setPort
argument_list|(
name|nodeURI
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|nodeURI
operator|.
name|getUserInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|creds
init|=
name|nodeURI
operator|.
name|getUserInfo
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getUsername
argument_list|()
operator|==
literal|null
condition|)
block|{
name|configuration
operator|.
name|setUsername
argument_list|(
name|creds
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
condition|)
block|{
name|configuration
operator|.
name|setPassword
argument_list|(
name|creds
operator|.
name|length
operator|>
literal|1
condition|?
name|creds
index|[
literal|1
index|]
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI: "
operator|+
name|remaining
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
name|CordaProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|,
name|proxy
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
name|CordaConsumer
name|consumer
init|=
operator|new
name|CordaConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|,
name|proxy
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
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|NetworkHostAndPort
name|rpcAddress
init|=
operator|new
name|NetworkHostAndPort
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|CordaRPCClient
name|rpcClient
init|=
operator|new
name|CordaRPCClient
argument_list|(
name|rpcAddress
argument_list|)
decl_stmt|;
name|rpcConnection
operator|=
name|rpcClient
operator|.
name|start
argument_list|(
name|this
operator|.
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|,
name|this
operator|.
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|=
name|rpcConnection
operator|.
name|getProxy
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|rpcConnection
operator|!=
literal|null
condition|)
block|{
name|rpcConnection
operator|.
name|notifyServerAndClose
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
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
block|}
end_class

end_unit

