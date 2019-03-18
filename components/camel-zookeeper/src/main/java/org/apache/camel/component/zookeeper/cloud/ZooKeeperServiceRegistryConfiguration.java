begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.cloud
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
operator|.
name|cloud
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
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperCuratorConfiguration
import|;
end_import

begin_class
DECL|class|ZooKeeperServiceRegistryConfiguration
specifier|public
class|class
name|ZooKeeperServiceRegistryConfiguration
extends|extends
name|ZooKeeperCuratorConfiguration
block|{
comment|/**      * Should we remove all the registered services know by this registry on stop?      */
DECL|field|deregisterServicesOnStop
specifier|private
name|boolean
name|deregisterServicesOnStop
init|=
literal|true
decl_stmt|;
comment|/**      * Should we override the service host if given ?      */
DECL|field|overrideServiceHost
specifier|private
name|boolean
name|overrideServiceHost
init|=
literal|true
decl_stmt|;
comment|/**      * Service host.      */
DECL|field|serviceHost
specifier|private
name|String
name|serviceHost
decl_stmt|;
comment|// ***********************************************
comment|// Properties
comment|// ***********************************************
DECL|method|isDeregisterServicesOnStop ()
specifier|public
name|boolean
name|isDeregisterServicesOnStop
parameter_list|()
block|{
return|return
name|deregisterServicesOnStop
return|;
block|}
DECL|method|setDeregisterServicesOnStop (boolean deregisterServicesOnStop)
specifier|public
name|void
name|setDeregisterServicesOnStop
parameter_list|(
name|boolean
name|deregisterServicesOnStop
parameter_list|)
block|{
name|this
operator|.
name|deregisterServicesOnStop
operator|=
name|deregisterServicesOnStop
expr_stmt|;
block|}
DECL|method|isOverrideServiceHost ()
specifier|public
name|boolean
name|isOverrideServiceHost
parameter_list|()
block|{
return|return
name|overrideServiceHost
return|;
block|}
DECL|method|setOverrideServiceHost (boolean overrideServiceHost)
specifier|public
name|void
name|setOverrideServiceHost
parameter_list|(
name|boolean
name|overrideServiceHost
parameter_list|)
block|{
name|this
operator|.
name|overrideServiceHost
operator|=
name|overrideServiceHost
expr_stmt|;
block|}
DECL|method|getServiceHost ()
specifier|public
name|String
name|getServiceHost
parameter_list|()
block|{
return|return
name|serviceHost
return|;
block|}
DECL|method|setServiceHost (String serviceHost)
specifier|public
name|void
name|setServiceHost
parameter_list|(
name|String
name|serviceHost
parameter_list|)
block|{
name|this
operator|.
name|serviceHost
operator|=
name|serviceHost
expr_stmt|;
block|}
comment|// ***********************************************
comment|//
comment|// ***********************************************
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|ZooKeeperServiceRegistryConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|ZooKeeperServiceRegistryConfiguration
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

