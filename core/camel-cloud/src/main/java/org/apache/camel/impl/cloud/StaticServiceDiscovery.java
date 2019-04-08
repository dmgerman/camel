begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|cloud
operator|.
name|ServiceDefinition
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * A static list of known servers Camel Service Call EIP.  */
end_comment

begin_class
DECL|class|StaticServiceDiscovery
specifier|public
class|class
name|StaticServiceDiscovery
extends|extends
name|DefaultServiceDiscovery
block|{
DECL|field|services
specifier|private
specifier|final
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
decl_stmt|;
DECL|method|StaticServiceDiscovery ()
specifier|public
name|StaticServiceDiscovery
parameter_list|()
block|{
name|this
operator|.
name|services
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|StaticServiceDiscovery (List<ServiceDefinition> servers)
specifier|public
name|StaticServiceDiscovery
parameter_list|(
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|servers
parameter_list|)
block|{
name|this
operator|.
name|services
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|servers
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the servers.      *      * @param servers server in the format: [service@]host:port.      */
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
name|this
operator|.
name|services
operator|.
name|clear
argument_list|()
expr_stmt|;
name|servers
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|addServer
argument_list|)
expr_stmt|;
block|}
DECL|method|addServers (String serviceName, List<String> servers)
specifier|public
name|void
name|addServers
parameter_list|(
name|String
name|serviceName
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|servers
parameter_list|)
block|{
for|for
control|(
name|String
name|server
range|:
name|servers
control|)
block|{
name|String
name|host
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|server
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|port
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|server
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|host
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|port
argument_list|)
condition|)
block|{
name|addServer
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
name|serviceName
argument_list|)
operator|.
name|withHost
argument_list|(
name|host
argument_list|)
operator|.
name|withPort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|port
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Set the servers.      *      * @param servers servers separated by comma in the format: [service@]host:port,[service@]host2:port,[service@]host3:port and so on.      */
DECL|method|setServers (String servers)
specifier|public
name|void
name|setServers
parameter_list|(
name|String
name|servers
parameter_list|)
block|{
name|this
operator|.
name|services
operator|.
name|clear
argument_list|()
expr_stmt|;
name|addServer
argument_list|(
name|servers
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a server to the known list of servers.      */
DECL|method|addServer (ServiceDefinition server)
specifier|public
name|void
name|addServer
parameter_list|(
name|ServiceDefinition
name|server
parameter_list|)
block|{
name|services
operator|.
name|add
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a server to the known list of servers.      * @param serverString servers separated by comma in the format: [service@]host:port,[service@]host2:port,[service@]host3:port and so on.      */
DECL|method|addServer (String serverString)
specifier|public
name|void
name|addServer
parameter_list|(
name|String
name|serverString
parameter_list|)
block|{
name|DefaultServiceDefinition
operator|.
name|parse
argument_list|(
name|serverString
argument_list|)
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|addServer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Remove an existing server from the list of known servers.      */
DECL|method|removeServer (Predicate<ServiceDefinition> condition)
specifier|public
name|void
name|removeServer
parameter_list|(
name|Predicate
argument_list|<
name|ServiceDefinition
argument_list|>
name|condition
parameter_list|)
block|{
name|services
operator|.
name|removeIf
argument_list|(
name|condition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getServices (String name)
specifier|public
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|getServices
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|services
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|s
lambda|->
name|Objects
operator|.
name|isNull
argument_list|(
name|s
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|Objects
operator|.
name|equals
argument_list|(
name|name
argument_list|,
name|s
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|// *************************************************************************
comment|// Helpers
comment|// *************************************************************************
DECL|method|forServices (Collection<ServiceDefinition> definitions)
specifier|public
specifier|static
name|StaticServiceDiscovery
name|forServices
parameter_list|(
name|Collection
argument_list|<
name|ServiceDefinition
argument_list|>
name|definitions
parameter_list|)
block|{
name|StaticServiceDiscovery
name|discovery
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
for|for
control|(
name|ServiceDefinition
name|definition
range|:
name|definitions
control|)
block|{
name|discovery
operator|.
name|addServer
argument_list|(
name|definition
argument_list|)
expr_stmt|;
block|}
return|return
name|discovery
return|;
block|}
DECL|method|forServices (ServiceDefinition... definitions)
specifier|public
specifier|static
name|StaticServiceDiscovery
name|forServices
parameter_list|(
name|ServiceDefinition
modifier|...
name|definitions
parameter_list|)
block|{
name|StaticServiceDiscovery
name|discovery
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
for|for
control|(
name|ServiceDefinition
name|definition
range|:
name|definitions
control|)
block|{
name|discovery
operator|.
name|addServer
argument_list|(
name|definition
argument_list|)
expr_stmt|;
block|}
return|return
name|discovery
return|;
block|}
block|}
end_class

end_unit
