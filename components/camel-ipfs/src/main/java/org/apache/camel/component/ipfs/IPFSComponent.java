begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ipfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ipfs
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
name|util
operator|.
name|Map
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
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nessus
operator|.
name|ipfs
operator|.
name|IPFSClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nessus
operator|.
name|ipfs
operator|.
name|impl
operator|.
name|DefaultIPFSClient
import|;
end_import

begin_class
DECL|class|IPFSComponent
specifier|public
class|class
name|IPFSComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|client
specifier|private
name|IPFSClient
name|client
decl_stmt|;
annotation|@
name|Override
DECL|method|createEndpoint (String urispec, String remaining, Map<String, Object> params)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|urispec
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Init the configuration
name|IPFSConfiguration
name|config
init|=
operator|new
name|IPFSConfiguration
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|params
argument_list|)
expr_stmt|;
comment|// Derive host:port and cmd from the give uri
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|urispec
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|uri
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|String
name|cmd
init|=
name|remaining
decl_stmt|;
if|if
condition|(
operator|!
name|cmd
operator|.
name|equals
argument_list|(
name|host
argument_list|)
condition|)
block|{
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
name|config
operator|.
name|setIpfsHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
if|if
condition|(
name|port
operator|>
literal|0
condition|)
name|config
operator|.
name|setIpfsPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|int
name|idx
init|=
name|cmd
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|cmd
operator|=
name|cmd
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|config
operator|.
name|setIpfsCmd
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|client
operator|=
name|createClient
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
operator|new
name|IPFSEndpoint
argument_list|(
name|urispec
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|getIPFSClient ()
specifier|public
name|IPFSClient
name|getIPFSClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|createClient (IPFSConfiguration config)
specifier|private
specifier|synchronized
name|IPFSClient
name|createClient
parameter_list|(
name|IPFSConfiguration
name|config
parameter_list|)
block|{
return|return
operator|new
name|DefaultIPFSClient
argument_list|(
name|config
operator|.
name|getIpfsHost
argument_list|()
argument_list|,
name|config
operator|.
name|getIpfsPort
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

