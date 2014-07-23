begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
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
name|CamelContext
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
name|CamelException
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
name|UriEndpointComponent
import|;
end_import

begin_comment
comment|/**  * Component that creates {@link ZooKeeperEndpoint}s for interacting with a ZooKeeper cluster.  */
end_comment

begin_class
DECL|class|ZooKeeperComponent
specifier|public
class|class
name|ZooKeeperComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|configuration
specifier|private
name|ZooKeeperConfiguration
name|configuration
decl_stmt|;
DECL|method|ZooKeeperComponent ()
specifier|public
name|ZooKeeperComponent
parameter_list|()
block|{
name|super
argument_list|(
name|ZooKeeperEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|ZooKeeperComponent (CamelContext context)
specifier|public
name|ZooKeeperComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|ZooKeeperEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|ZooKeeperComponent (ZooKeeperConfiguration configuration)
specifier|public
name|ZooKeeperComponent
parameter_list|(
name|ZooKeeperConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|ZooKeeperEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
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
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"No Camel context has been provided to this zookeeper component"
argument_list|)
throw|;
block|}
name|ZooKeeperConfiguration
name|config
init|=
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|extractConfigFromUri
argument_list|(
name|uri
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|ZooKeeperEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|extractConfigFromUri (String remaining, ZooKeeperConfiguration config)
specifier|private
name|void
name|extractConfigFromUri
parameter_list|(
name|String
name|remaining
parameter_list|,
name|ZooKeeperConfiguration
name|config
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|URI
name|fullUri
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|String
index|[]
name|hosts
init|=
name|fullUri
operator|.
name|getAuthority
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|host
range|:
name|hosts
control|)
block|{
name|config
operator|.
name|addZookeeperServer
argument_list|(
name|host
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|config
operator|.
name|setPath
argument_list|(
name|fullUri
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|ZooKeeperConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|ZooKeeperConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (ZooKeeperConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ZooKeeperConfiguration
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
block|}
end_class

end_unit

