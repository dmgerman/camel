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
name|ServiceStatus
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  *<code>ZooKeeperEndpoint</code>  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"ZooKeeper Endpoint"
argument_list|)
DECL|class|ZooKeeperEndpoint
specifier|public
class|class
name|ZooKeeperEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|configuration
specifier|private
name|ZooKeeperConfiguration
name|configuration
decl_stmt|;
DECL|field|connectionManager
specifier|private
name|ZooKeeperConnectionManager
name|connectionManager
decl_stmt|;
DECL|method|ZooKeeperEndpoint (String uri, ZooKeeperComponent component, ZooKeeperConfiguration configuration)
specifier|public
name|ZooKeeperEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|ZooKeeperComponent
name|component
parameter_list|,
name|ZooKeeperConfiguration
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
name|connectionManager
operator|=
operator|new
name|ZooKeeperConnectionManager
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
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
name|ZookeeperProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
name|ZooKeeperConsumer
name|answer
init|=
operator|new
name|ZooKeeperConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
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
DECL|method|getConfiguration ()
specifier|public
name|ZooKeeperConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getConnectionManager ()
name|ZooKeeperConnectionManager
name|getConnectionManager
parameter_list|()
block|{
return|return
name|connectionManager
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel ID"
argument_list|)
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel ManagementName"
argument_list|)
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Endpoint Uri"
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|super
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Service State"
argument_list|)
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
name|ServiceStatus
name|status
init|=
name|this
operator|.
name|getStatus
argument_list|()
decl_stmt|;
comment|// if no status exists then its stopped
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
name|status
operator|=
name|ServiceStatus
operator|.
name|Stopped
expr_stmt|;
block|}
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getTimeout
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getRepeat ()
specifier|public
name|boolean
name|getRepeat
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|shouldRepeat
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setRepeat (boolean shouldRepeat)
specifier|public
name|void
name|setRepeat
parameter_list|(
name|boolean
name|shouldRepeat
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setRepeat
argument_list|(
name|shouldRepeat
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getServers ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getServers
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getServers
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setServers (List<String> servers)
specifier|public
name|void
name|setServers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|servers
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setServers
argument_list|(
name|servers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isListChildren ()
specifier|public
name|boolean
name|isListChildren
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|isListChildren
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setListChildren (boolean listChildren)
specifier|public
name|void
name|setListChildren
parameter_list|(
name|boolean
name|listChildren
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setListChildren
argument_list|(
name|listChildren
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getCreate ()
specifier|public
name|boolean
name|getCreate
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|shouldCreate
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setCreate (boolean shouldCreate)
specifier|public
name|void
name|setCreate
parameter_list|(
name|boolean
name|shouldCreate
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setCreate
argument_list|(
name|shouldCreate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getBackoff ()
specifier|public
name|long
name|getBackoff
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getBackoff
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setBackoff (long backoff)
specifier|public
name|void
name|setBackoff
parameter_list|(
name|long
name|backoff
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setBackoff
argument_list|(
name|backoff
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated The usage of this property has no effect at all.      */
annotation|@
name|Deprecated
annotation|@
name|ManagedAttribute
DECL|method|getAwaitExistence ()
specifier|public
name|boolean
name|getAwaitExistence
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|shouldAwaitExistence
argument_list|()
return|;
block|}
comment|/**      * @deprecated The usage of this property has no effect at all.      */
annotation|@
name|Deprecated
annotation|@
name|ManagedAttribute
DECL|method|setAwaitExistence (boolean awaitExistence)
specifier|public
name|void
name|setAwaitExistence
parameter_list|(
name|boolean
name|awaitExistence
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAwaitExistence
argument_list|(
name|awaitExistence
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
DECL|method|addServer (String server)
specifier|public
name|void
name|addServer
parameter_list|(
name|String
name|server
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|addZookeeperServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
DECL|method|clearServers ()
specifier|public
name|void
name|clearServers
parameter_list|()
block|{
name|getConfiguration
argument_list|()
operator|.
name|getServers
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getManagedObject (ZooKeeperEndpoint arg0)
specifier|public
name|Object
name|getManagedObject
parameter_list|(
name|ZooKeeperEndpoint
name|arg0
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isSendEmptyMessageOnDelete ()
specifier|public
name|boolean
name|isSendEmptyMessageOnDelete
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|isSendEmptyMessageOnDelete
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setSendEmptyMessageOnDelete (boolean sendEmptyMessageOnDelete)
specifier|public
name|void
name|setSendEmptyMessageOnDelete
parameter_list|(
name|boolean
name|sendEmptyMessageOnDelete
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setSendEmptyMessageOnDelete
argument_list|(
name|sendEmptyMessageOnDelete
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

