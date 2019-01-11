begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|HashMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Multimap
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
name|spi
operator|.
name|annotations
operator|.
name|Component
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
name|DefaultComponent
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"mila-client"
argument_list|)
DECL|class|MiloClientComponent
specifier|public
class|class
name|MiloClientComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|cache
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|MiloClientConnection
argument_list|>
name|cache
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|connectionMap
specifier|private
specifier|final
name|Multimap
argument_list|<
name|String
argument_list|,
name|MiloClientEndpoint
argument_list|>
name|connectionMap
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|field|defaultConfiguration
specifier|private
name|MiloClientConfiguration
name|defaultConfiguration
init|=
operator|new
name|MiloClientConfiguration
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
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
specifier|final
name|MiloClientConfiguration
name|configuration
init|=
operator|new
name|MiloClientConfiguration
argument_list|(
name|this
operator|.
name|defaultConfiguration
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setEndpointUri
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|)
return|;
block|}
DECL|method|createEndpoint (final String uri, final MiloClientConfiguration configuration, final Map<String, Object> parameters)
specifier|private
specifier|synchronized
name|MiloClientEndpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|MiloClientConfiguration
name|configuration
parameter_list|,
specifier|final
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
specifier|final
name|String
name|cacheId
init|=
name|configuration
operator|.
name|toCacheId
argument_list|()
decl_stmt|;
name|MiloClientConnection
name|connection
init|=
name|this
operator|.
name|cache
operator|.
name|get
argument_list|(
name|cacheId
argument_list|)
decl_stmt|;
if|if
condition|(
name|connection
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Cache miss - creating new connection instance: {}"
argument_list|,
name|cacheId
argument_list|)
expr_stmt|;
name|connection
operator|=
operator|new
name|MiloClientConnection
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|put
argument_list|(
name|cacheId
argument_list|,
name|connection
argument_list|)
expr_stmt|;
block|}
specifier|final
name|MiloClientEndpoint
name|endpoint
init|=
operator|new
name|MiloClientEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|connection
argument_list|,
name|configuration
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// register connection with endpoint
name|this
operator|.
name|connectionMap
operator|.
name|put
argument_list|(
name|cacheId
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * All default options for client      */
DECL|method|setDefaultConfiguration (final MiloClientConfiguration defaultConfiguration)
specifier|public
name|void
name|setDefaultConfiguration
parameter_list|(
specifier|final
name|MiloClientConfiguration
name|defaultConfiguration
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|=
name|defaultConfiguration
expr_stmt|;
block|}
comment|/**      * Default application name      */
DECL|method|setApplicationName (final String applicationName)
specifier|public
name|void
name|setApplicationName
parameter_list|(
specifier|final
name|String
name|applicationName
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|.
name|setApplicationName
argument_list|(
name|applicationName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Default application URI      */
DECL|method|setApplicationUri (final String applicationUri)
specifier|public
name|void
name|setApplicationUri
parameter_list|(
specifier|final
name|String
name|applicationUri
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|.
name|setApplicationUri
argument_list|(
name|applicationUri
argument_list|)
expr_stmt|;
block|}
comment|/**      * Default product URI      */
DECL|method|setProductUri (final String productUri)
specifier|public
name|void
name|setProductUri
parameter_list|(
specifier|final
name|String
name|productUri
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|.
name|setProductUri
argument_list|(
name|productUri
argument_list|)
expr_stmt|;
block|}
comment|/**      * Default reconnect timeout      */
DECL|method|setReconnectTimeout (final Long reconnectTimeout)
specifier|public
name|void
name|setReconnectTimeout
parameter_list|(
specifier|final
name|Long
name|reconnectTimeout
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|.
name|setRequestTimeout
argument_list|(
name|reconnectTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|disposed (final MiloClientEndpoint endpoint)
specifier|public
specifier|synchronized
name|void
name|disposed
parameter_list|(
specifier|final
name|MiloClientEndpoint
name|endpoint
parameter_list|)
block|{
specifier|final
name|MiloClientConnection
name|connection
init|=
name|endpoint
operator|.
name|getConnection
argument_list|()
decl_stmt|;
comment|// unregister usage of connection
name|this
operator|.
name|connectionMap
operator|.
name|remove
argument_list|(
name|connection
operator|.
name|getConnectionId
argument_list|()
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|// test if this was the last endpoint using this connection
if|if
condition|(
operator|!
name|this
operator|.
name|connectionMap
operator|.
name|containsKey
argument_list|(
name|connection
operator|.
name|getConnectionId
argument_list|()
argument_list|)
condition|)
block|{
comment|// this was the last endpoint using the connection ...
comment|// ... remove from the cache
name|this
operator|.
name|cache
operator|.
name|remove
argument_list|(
name|connection
operator|.
name|getConnectionId
argument_list|()
argument_list|)
expr_stmt|;
comment|// ... and close
try|try
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to close connection"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

