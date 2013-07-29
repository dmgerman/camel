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
DECL|method|getSanitize ()
name|Boolean
name|getSanitize
parameter_list|()
function_decl|;
comment|/**      * Whether to remove detected sensitive information (such as passwords) from MBean names and attributes.      *<p/>      * This option is default<tt>false</tt>.      */
DECL|method|setSanitize (Boolean sanitize)
name|void
name|setSanitize
parameter_list|(
name|Boolean
name|sanitize
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

