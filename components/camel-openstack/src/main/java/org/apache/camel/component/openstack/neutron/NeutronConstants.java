begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.neutron
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openstack
operator|.
name|neutron
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
name|component
operator|.
name|openstack
operator|.
name|common
operator|.
name|OpenstackConstants
import|;
end_import

begin_class
DECL|class|NeutronConstants
specifier|public
specifier|final
class|class
name|NeutronConstants
extends|extends
name|OpenstackConstants
block|{
DECL|field|NEUTRON_NETWORK_SUBSYSTEM
specifier|public
specifier|static
specifier|final
name|String
name|NEUTRON_NETWORK_SUBSYSTEM
init|=
literal|"networks"
decl_stmt|;
DECL|field|NEUTRON_SUBNETS_SYSTEM
specifier|public
specifier|static
specifier|final
name|String
name|NEUTRON_SUBNETS_SYSTEM
init|=
literal|"subnets"
decl_stmt|;
DECL|field|NEUTRON_PORT_SYSTEM
specifier|public
specifier|static
specifier|final
name|String
name|NEUTRON_PORT_SYSTEM
init|=
literal|"ports"
decl_stmt|;
DECL|field|NEUTRON_ROUTER_SYSTEM
specifier|public
specifier|static
specifier|final
name|String
name|NEUTRON_ROUTER_SYSTEM
init|=
literal|"routers"
decl_stmt|;
DECL|field|TENANT_ID
specifier|public
specifier|static
specifier|final
name|String
name|TENANT_ID
init|=
literal|"tenantId"
decl_stmt|;
DECL|field|NETWORK_ID
specifier|public
specifier|static
specifier|final
name|String
name|NETWORK_ID
init|=
literal|"networkId"
decl_stmt|;
comment|//network
DECL|field|ADMIN_STATE_UP
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_STATE_UP
init|=
literal|"adminStateUp"
decl_stmt|;
DECL|field|NETWORK_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|NETWORK_TYPE
init|=
literal|"networkType"
decl_stmt|;
DECL|field|PHYSICAL_NETWORK
specifier|public
specifier|static
specifier|final
name|String
name|PHYSICAL_NETWORK
init|=
literal|"physicalNetwork"
decl_stmt|;
DECL|field|SEGMENT_ID
specifier|public
specifier|static
specifier|final
name|String
name|SEGMENT_ID
init|=
literal|"segmentId"
decl_stmt|;
DECL|field|IS_SHARED
specifier|public
specifier|static
specifier|final
name|String
name|IS_SHARED
init|=
literal|"isShared"
decl_stmt|;
DECL|field|IS_ROUTER_EXTERNAL
specifier|public
specifier|static
specifier|final
name|String
name|IS_ROUTER_EXTERNAL
init|=
literal|"isRouterExternal"
decl_stmt|;
comment|//subnet
DECL|field|ENABLE_DHCP
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_DHCP
init|=
literal|"enableDHCP"
decl_stmt|;
DECL|field|GATEWAY
specifier|public
specifier|static
specifier|final
name|String
name|GATEWAY
init|=
literal|"gateway"
decl_stmt|;
comment|//port
DECL|field|DEVICE_ID
specifier|public
specifier|static
specifier|final
name|String
name|DEVICE_ID
init|=
literal|"deviceId"
decl_stmt|;
DECL|field|MAC_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|MAC_ADDRESS
init|=
literal|"macAddress"
decl_stmt|;
comment|//router
DECL|field|ROUTER_ID
specifier|public
specifier|static
specifier|final
name|String
name|ROUTER_ID
init|=
literal|"routerId"
decl_stmt|;
DECL|field|SUBNET_ID
specifier|public
specifier|static
specifier|final
name|String
name|SUBNET_ID
init|=
literal|"subnetId"
decl_stmt|;
DECL|field|PORT_ID
specifier|public
specifier|static
specifier|final
name|String
name|PORT_ID
init|=
literal|"portId"
decl_stmt|;
DECL|field|ITERFACE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|ITERFACE_TYPE
init|=
literal|"interfaceType"
decl_stmt|;
DECL|field|ATTACH_INTERFACE
specifier|public
specifier|static
specifier|final
name|String
name|ATTACH_INTERFACE
init|=
literal|"attachInterface"
decl_stmt|;
DECL|field|DETACH_INTERFACE
specifier|public
specifier|static
specifier|final
name|String
name|DETACH_INTERFACE
init|=
literal|"detachInterface"
decl_stmt|;
block|}
end_class

end_unit

