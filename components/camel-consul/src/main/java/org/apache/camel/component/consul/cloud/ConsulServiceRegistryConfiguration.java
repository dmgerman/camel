begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
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
name|consul
operator|.
name|ConsulClientConfiguration
import|;
end_import

begin_class
DECL|class|ConsulServiceRegistryConfiguration
specifier|public
class|class
name|ConsulServiceRegistryConfiguration
extends|extends
name|ConsulClientConfiguration
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
comment|/**      * The time (in seconds) to live for TTL checks. Default is 1 minute.      */
DECL|field|checkTtl
specifier|private
name|int
name|checkTtl
init|=
literal|60
decl_stmt|;
comment|/**      * How often (in seconds) a service has to be marked as healthy if its check      * is TTL or how often the check should run. Default is 5 seconds.      */
DECL|field|checkInterval
specifier|private
name|int
name|checkInterval
init|=
literal|5
decl_stmt|;
comment|/**      * How long (in seconds) to wait to deregister a service in case of unclean      * shutdown. Default is 1 hour.      */
DECL|field|deregisterAfter
specifier|private
name|int
name|deregisterAfter
init|=
literal|60
operator|*
literal|60
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
DECL|method|getCheckTtl ()
specifier|public
name|int
name|getCheckTtl
parameter_list|()
block|{
return|return
name|checkTtl
return|;
block|}
DECL|method|setCheckTtl (int checkTtl)
specifier|public
name|void
name|setCheckTtl
parameter_list|(
name|int
name|checkTtl
parameter_list|)
block|{
name|this
operator|.
name|checkTtl
operator|=
name|checkTtl
expr_stmt|;
block|}
DECL|method|getCheckInterval ()
specifier|public
name|int
name|getCheckInterval
parameter_list|()
block|{
return|return
name|checkInterval
return|;
block|}
DECL|method|setCheckInterval (int checkInterval)
specifier|public
name|void
name|setCheckInterval
parameter_list|(
name|int
name|checkInterval
parameter_list|)
block|{
name|this
operator|.
name|checkInterval
operator|=
name|checkInterval
expr_stmt|;
block|}
DECL|method|getDeregisterAfter ()
specifier|public
name|int
name|getDeregisterAfter
parameter_list|()
block|{
return|return
name|deregisterAfter
return|;
block|}
DECL|method|setDeregisterAfter (int deregisterAfter)
specifier|public
name|void
name|setDeregisterAfter
parameter_list|(
name|int
name|deregisterAfter
parameter_list|)
block|{
name|this
operator|.
name|deregisterAfter
operator|=
name|deregisterAfter
expr_stmt|;
block|}
comment|// ***********************************************
comment|//
comment|// ***********************************************
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|ConsulServiceRegistryConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|ConsulServiceRegistryConfiguration
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

