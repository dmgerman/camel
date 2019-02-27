begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|ManagementStatisticsLevel
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
name|Service
import|;
end_import

begin_comment
comment|/**  * Camel JMX service agent  */
end_comment

begin_interface
DECL|interface|ManagementAgent
specifier|public
interface|interface
name|ManagementAgent
extends|extends
name|Service
block|{
comment|/**      * Registers object with management infrastructure with a specific name. Object must be annotated or       * implement standard MBean interface.      *      * @param obj  the object to register      * @param name the name      * @throws JMException is thrown if the registration failed      */
DECL|method|register (Object obj, ObjectName name)
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Registers object with management infrastructure with a specific name. Object must be annotated or       * implement standard MBean interface.      *      * @param obj  the object to register      * @param name the name      * @param forceRegistration if set to<tt>true</tt>, then object will be registered despite      * existing object is already registered with the name.      * @throws JMException is thrown if the registration failed      */
DECL|method|register (Object obj, ObjectName name, boolean forceRegistration)
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Unregisters object based upon registered name      *      * @param name the name      * @throws JMException is thrown if the unregistration failed      */
DECL|method|unregister (ObjectName name)
name|void
name|unregister
parameter_list|(
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Is the given object registered      *      * @param name the name      * @return<tt>true</tt> if registered      */
DECL|method|isRegistered (ObjectName name)
name|boolean
name|isRegistered
parameter_list|(
name|ObjectName
name|name
parameter_list|)
function_decl|;
comment|/**      * Creates a new proxy client      *      * @param name   the mbean name      * @param mbean  the client interface, such as from the {@link org.apache.camel.api.management.mbean} package.      * @return the client or<tt>null</tt> if mbean does not exists      */
DECL|method|newProxyClient (ObjectName name, Class<T> mbean)
parameter_list|<
name|T
parameter_list|>
name|T
name|newProxyClient
parameter_list|(
name|ObjectName
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|mbean
parameter_list|)
function_decl|;
comment|/**      * Get the MBeanServer which hosts managed objects.      *<p/>      *<b>Notice:</b> If the JMXEnabled configuration is not set to<tt>true</tt>,      * this method will return<tt>null</tt>.      *       * @return the MBeanServer      */
DECL|method|getMBeanServer ()
name|MBeanServer
name|getMBeanServer
parameter_list|()
function_decl|;
comment|/**      * Sets a custom mbean server to use      *      * @param mbeanServer the custom mbean server      */
DECL|method|setMBeanServer (MBeanServer mbeanServer)
name|void
name|setMBeanServer
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
function_decl|;
comment|/**      * Get domain name for Camel MBeans.      *<p/>      *<b>Notice:</b> That this can be different that the default domain name of the MBean Server.      *       * @return domain name      */
DECL|method|getMBeanObjectDomainName ()
name|String
name|getMBeanObjectDomainName
parameter_list|()
function_decl|;
comment|/**      * Sets the port used by {@link java.rmi.registry.LocateRegistry}.      *      * @param port the port      */
DECL|method|setRegistryPort (Integer port)
name|void
name|setRegistryPort
parameter_list|(
name|Integer
name|port
parameter_list|)
function_decl|;
comment|/**      * Gets the port used by {@link java.rmi.registry.LocateRegistry}.      *      * @return the port      */
DECL|method|getRegistryPort ()
name|Integer
name|getRegistryPort
parameter_list|()
function_decl|;
comment|/**      * Sets the port clients must use to connect      *      * @param port the port      */
DECL|method|setConnectorPort (Integer port)
name|void
name|setConnectorPort
parameter_list|(
name|Integer
name|port
parameter_list|)
function_decl|;
comment|/**      * Gets the port clients must use to connect      *      * @return the port      */
DECL|method|getConnectorPort ()
name|Integer
name|getConnectorPort
parameter_list|()
function_decl|;
comment|/**      * Sets the default domain on the MBean server      *      * @param domain the domain      */
DECL|method|setMBeanServerDefaultDomain (String domain)
name|void
name|setMBeanServerDefaultDomain
parameter_list|(
name|String
name|domain
parameter_list|)
function_decl|;
comment|/**      * Gets the default domain on the MBean server      *      * @return the domain      */
DECL|method|getMBeanServerDefaultDomain ()
name|String
name|getMBeanServerDefaultDomain
parameter_list|()
function_decl|;
comment|/**      * Sets the object domain name      *      * @param domainName the object domain name      */
DECL|method|setMBeanObjectDomainName (String domainName)
name|void
name|setMBeanObjectDomainName
parameter_list|(
name|String
name|domainName
parameter_list|)
function_decl|;
comment|/**      * Sets the service url      *      * @param url the service url      */
DECL|method|setServiceUrlPath (String url)
name|void
name|setServiceUrlPath
parameter_list|(
name|String
name|url
parameter_list|)
function_decl|;
comment|/**      * Gets the service url      *      * @return the url      */
DECL|method|getServiceUrlPath ()
name|String
name|getServiceUrlPath
parameter_list|()
function_decl|;
comment|/**      * Whether connector should be created, allowing clients to connect remotely      *      * @param createConnector<tt>true</tt> to create connector      */
DECL|method|setCreateConnector (Boolean createConnector)
name|void
name|setCreateConnector
parameter_list|(
name|Boolean
name|createConnector
parameter_list|)
function_decl|;
comment|/**      * Whether connector is created, allowing clients to connect remotely      *      * @return<tt>true</tt> if connector is created      */
DECL|method|getCreateConnector ()
name|Boolean
name|getCreateConnector
parameter_list|()
function_decl|;
comment|/**      * Whether to use the platform MBean Server.      *      * @param usePlatformMBeanServer<tt>true</tt> to use platform MBean server      */
DECL|method|setUsePlatformMBeanServer (Boolean usePlatformMBeanServer)
name|void
name|setUsePlatformMBeanServer
parameter_list|(
name|Boolean
name|usePlatformMBeanServer
parameter_list|)
function_decl|;
comment|/**      * Whether to use the platform MBean Server.      *      * @return<tt>true</tt> if platform MBean server is to be used      */
DECL|method|getUsePlatformMBeanServer ()
name|Boolean
name|getUsePlatformMBeanServer
parameter_list|()
function_decl|;
comment|/**      * Whether to only register processors which has a custom id assigned.      *<p/>      * This allows you to filter unwanted processors.      *      * @return<tt>true</tt> if only processors with custom id is registered      */
DECL|method|getOnlyRegisterProcessorWithCustomId ()
name|Boolean
name|getOnlyRegisterProcessorWithCustomId
parameter_list|()
function_decl|;
comment|/**      * Whether to only register processors which has a custom id assigned.      *<p/>      * This allows you to filter unwanted processors.      *      * @param onlyRegisterProcessorWithCustomId<tt>true</tt> to only register if custom id has been assigned      */
DECL|method|setOnlyRegisterProcessorWithCustomId (Boolean onlyRegisterProcessorWithCustomId)
name|void
name|setOnlyRegisterProcessorWithCustomId
parameter_list|(
name|Boolean
name|onlyRegisterProcessorWithCustomId
parameter_list|)
function_decl|;
comment|/**      * Whether to always register mbeans.      *<p/>      * This option is default<tt>false</tt>.      *<p/>      *<b>Important:</b> If this option is enabled then any service is registered as mbean. When using      * dynamic EIP patterns using unique endpoint urls, you may create excessive mbeans in the registry.      * This could lead to degraded performance as memory consumption will rise due the rising number      * of mbeans.      *      * @return<tt>true</tt> if always registering      */
DECL|method|getRegisterAlways ()
name|Boolean
name|getRegisterAlways
parameter_list|()
function_decl|;
comment|/**      * Whether to always register mbeans.      *<p/>      * This option is default<tt>false</tt>.      *<p/>      *<b>Important:</b> If this option is enabled then any service is registered as mbean. When using      * dynamic EIP patterns using unique endpoint urls, you may create excessive mbeans in the registry.      * This could lead to degraded performance as memory consumption will rise due the rising number      * of mbeans.      *      * @param registerAlways<tt>true</tt> to always register      */
DECL|method|setRegisterAlways (Boolean registerAlways)
name|void
name|setRegisterAlways
parameter_list|(
name|Boolean
name|registerAlways
parameter_list|)
function_decl|;
comment|/**      * Whether to register mbeans when starting a new route      *<p/>      * This option is default<tt>true</tt>.      *      * @return<tt>true</tt> to register when starting a new route      */
DECL|method|getRegisterNewRoutes ()
name|Boolean
name|getRegisterNewRoutes
parameter_list|()
function_decl|;
comment|/**      * Whether to register mbeans when starting a new route      *<p/>      * This option is default<tt>true</tt>.      *      * @param registerNewRoutes<tt>true</tt> to register when starting a new route      */
DECL|method|setRegisterNewRoutes (Boolean registerNewRoutes)
name|void
name|setRegisterNewRoutes
parameter_list|(
name|Boolean
name|registerNewRoutes
parameter_list|)
function_decl|;
comment|/**      * Whether to remove detected sensitive information (such as passwords) from MBean names and attributes.      *<p/>      * This option is default<tt>false</tt>.      */
DECL|method|getMask ()
name|Boolean
name|getMask
parameter_list|()
function_decl|;
comment|/**      * Whether to remove detected sensitive information (such as passwords) from MBean names and attributes.      *<p/>      * This option is default<tt>false</tt>.      */
DECL|method|setMask (Boolean sanitize)
name|void
name|setMask
parameter_list|(
name|Boolean
name|sanitize
parameter_list|)
function_decl|;
comment|/**      * Gets whether host name is included in MBean names.      *      * @return<tt>true</tt> if included      */
DECL|method|getIncludeHostName ()
name|Boolean
name|getIncludeHostName
parameter_list|()
function_decl|;
comment|/**      * Sets whether to include host name in the {@link ManagementObjectNameStrategy}.      *<p/>      * By default this is turned off from Camel 2.13 onwards, but this option      * can be set to<tt>true</tt> to include the hostname as Camel 2.12 or      * older releases does.      *      * @param includeHostName<tt>true</tt> to include host name in the MBean names.      */
DECL|method|setIncludeHostName (Boolean includeHostName)
name|void
name|setIncludeHostName
parameter_list|(
name|Boolean
name|includeHostName
parameter_list|)
function_decl|;
comment|/**      * The naming pattern for creating the CamelContext management name.      *<p/>      * The default pattern is<tt>#name#</tt>      */
DECL|method|getManagementNamePattern ()
name|String
name|getManagementNamePattern
parameter_list|()
function_decl|;
comment|/**      * The naming pattern for creating the CamelContext management name.      *<p/>      * The default pattern is<tt>#name#</tt>      */
DECL|method|setManagementNamePattern (String managementNamePattern)
name|void
name|setManagementNamePattern
parameter_list|(
name|String
name|managementNamePattern
parameter_list|)
function_decl|;
comment|/**      * Sets whether load statistics is enabled (gathers load statistics using a background thread per CamelContext).      *<p/>      * The default value is<tt>false</tt>      *      * @param flag<tt>true</tt> to enable load statistics      */
DECL|method|setLoadStatisticsEnabled (Boolean flag)
name|void
name|setLoadStatisticsEnabled
parameter_list|(
name|Boolean
name|flag
parameter_list|)
function_decl|;
comment|/**      * Gets whether load statistics is enabled      *      * @return<tt>true</tt> if enabled      */
DECL|method|getLoadStatisticsEnabled ()
name|Boolean
name|getLoadStatisticsEnabled
parameter_list|()
function_decl|;
comment|/**      * Sets whether endpoint runtime statistics is enabled (gathers runtime usage of each incoming and outgoing endpoints).      *<p/>      * The default value is<tt>false</tt>      *      * @param flag<tt>true</tt> to enable endpoint runtime statistics      */
DECL|method|setEndpointRuntimeStatisticsEnabled (Boolean flag)
name|void
name|setEndpointRuntimeStatisticsEnabled
parameter_list|(
name|Boolean
name|flag
parameter_list|)
function_decl|;
comment|/**      * Gets whether endpoint runtime statistics is enabled      *      * @return<tt>true</tt> if enabled      */
DECL|method|getEndpointRuntimeStatisticsEnabled ()
name|Boolean
name|getEndpointRuntimeStatisticsEnabled
parameter_list|()
function_decl|;
comment|/**      * Sets the statistics level      *<p/>      * Default is {@link org.apache.camel.ManagementStatisticsLevel#Default}      *<p/>      * The level can be set to<tt>Extended</tt> to gather additional information      *      * @param level the new level      */
DECL|method|setStatisticsLevel (ManagementStatisticsLevel level)
name|void
name|setStatisticsLevel
parameter_list|(
name|ManagementStatisticsLevel
name|level
parameter_list|)
function_decl|;
comment|/**                   Â§      * Gets the statistics level      *      * @return the level      */
DECL|method|getStatisticsLevel ()
name|ManagementStatisticsLevel
name|getStatisticsLevel
parameter_list|()
function_decl|;
comment|/**      * Gets whether host IP Address to be used instead of host name.      *      * @return<tt>true</tt> if included      */
DECL|method|getUseHostIPAddress ()
name|Boolean
name|getUseHostIPAddress
parameter_list|()
function_decl|;
comment|/**      * Sets whether to use host IP Address       * @param useHostIPAddress<tt>true</tt> to use IP Address.      */
DECL|method|setUseHostIPAddress (Boolean useHostIPAddress)
name|void
name|setUseHostIPAddress
parameter_list|(
name|Boolean
name|useHostIPAddress
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

