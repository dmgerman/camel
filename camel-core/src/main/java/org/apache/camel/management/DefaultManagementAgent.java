begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|NoSuchObjectException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|LocateRegistry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|server
operator|.
name|UnicastRemoteObject
import|;
end_import

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
name|List
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
name|MBeanServerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectInstance
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
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorServerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXServiceURL
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
name|CamelContextAware
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
name|ManagementAgent
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
name|ManagementMBeanAssembler
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
name|ServiceSupport
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Default implementation of the Camel JMX service agent  */
end_comment

begin_class
DECL|class|DefaultManagementAgent
specifier|public
class|class
name|DefaultManagementAgent
extends|extends
name|ServiceSupport
implements|implements
name|ManagementAgent
implements|,
name|CamelContextAware
block|{
DECL|field|DEFAULT_DOMAIN
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_DOMAIN
init|=
literal|"org.apache.camel"
decl_stmt|;
DECL|field|DEFAULT_HOST
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_HOST
init|=
literal|"localhost"
decl_stmt|;
DECL|field|DEFAULT_REGISTRY_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_REGISTRY_PORT
init|=
literal|1099
decl_stmt|;
DECL|field|DEFAULT_CONNECTION_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_CONNECTION_PORT
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|DEFAULT_SERVICE_URL_PATH
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_SERVICE_URL_PATH
init|=
literal|"/jmxrmi/camel"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultManagementAgent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|server
specifier|private
name|MBeanServer
name|server
decl_stmt|;
comment|// need a name -> actual name mapping as some servers changes the names (such as WebSphere)
DECL|field|mbeansRegistered
specifier|private
specifier|final
name|Map
argument_list|<
name|ObjectName
argument_list|,
name|ObjectName
argument_list|>
name|mbeansRegistered
init|=
operator|new
name|HashMap
argument_list|<
name|ObjectName
argument_list|,
name|ObjectName
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|cs
specifier|private
name|JMXConnectorServer
name|cs
decl_stmt|;
DECL|field|registry
specifier|private
name|Registry
name|registry
decl_stmt|;
DECL|field|registryPort
specifier|private
name|Integer
name|registryPort
decl_stmt|;
DECL|field|connectorPort
specifier|private
name|Integer
name|connectorPort
decl_stmt|;
DECL|field|mBeanServerDefaultDomain
specifier|private
name|String
name|mBeanServerDefaultDomain
decl_stmt|;
DECL|field|mBeanObjectDomainName
specifier|private
name|String
name|mBeanObjectDomainName
decl_stmt|;
DECL|field|serviceUrlPath
specifier|private
name|String
name|serviceUrlPath
decl_stmt|;
DECL|field|usePlatformMBeanServer
specifier|private
name|Boolean
name|usePlatformMBeanServer
init|=
literal|true
decl_stmt|;
DECL|field|createConnector
specifier|private
name|Boolean
name|createConnector
decl_stmt|;
DECL|field|onlyRegisterProcessorWithCustomId
specifier|private
name|Boolean
name|onlyRegisterProcessorWithCustomId
decl_stmt|;
DECL|field|registerAlways
specifier|private
name|Boolean
name|registerAlways
decl_stmt|;
DECL|field|registerNewRoutes
specifier|private
name|Boolean
name|registerNewRoutes
init|=
literal|true
decl_stmt|;
DECL|method|DefaultManagementAgent ()
specifier|public
name|DefaultManagementAgent
parameter_list|()
block|{     }
DECL|method|DefaultManagementAgent (CamelContext camelContext)
specifier|public
name|DefaultManagementAgent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|finalizeSettings ()
specifier|protected
name|void
name|finalizeSettings
parameter_list|()
block|{
comment|// TODO: System properties ought to take precedence, over configured options
if|if
condition|(
name|registryPort
operator|==
literal|null
condition|)
block|{
name|registryPort
operator|=
name|Integer
operator|.
name|getInteger
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTRY_PORT
argument_list|,
name|DEFAULT_REGISTRY_PORT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|connectorPort
operator|==
literal|null
condition|)
block|{
name|connectorPort
operator|=
name|Integer
operator|.
name|getInteger
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|CONNECTOR_PORT
argument_list|,
name|DEFAULT_CONNECTION_PORT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mBeanServerDefaultDomain
operator|==
literal|null
condition|)
block|{
name|mBeanServerDefaultDomain
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DOMAIN
argument_list|,
name|DEFAULT_DOMAIN
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mBeanObjectDomainName
operator|==
literal|null
condition|)
block|{
name|mBeanObjectDomainName
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|MBEAN_DOMAIN
argument_list|,
name|DEFAULT_DOMAIN
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serviceUrlPath
operator|==
literal|null
condition|)
block|{
name|serviceUrlPath
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|SERVICE_URL_PATH
argument_list|,
name|DEFAULT_SERVICE_URL_PATH
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|createConnector
operator|==
literal|null
condition|)
block|{
name|createConnector
operator|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|CREATE_CONNECTOR
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onlyRegisterProcessorWithCustomId
operator|==
literal|null
condition|)
block|{
name|onlyRegisterProcessorWithCustomId
operator|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|ONLY_REGISTER_PROCESSOR_WITH_CUSTOM_ID
argument_list|)
expr_stmt|;
block|}
comment|// "Use platform mbean server" is true by default
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|usePlatformMBeanServer
operator|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTER_ALWAYS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|registerAlways
operator|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTER_ALWAYS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTER_NEW_ROUTES
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|registerNewRoutes
operator|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTER_NEW_ROUTES
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setRegistryPort (Integer port)
specifier|public
name|void
name|setRegistryPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|registryPort
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getRegistryPort ()
specifier|public
name|Integer
name|getRegistryPort
parameter_list|()
block|{
return|return
name|registryPort
return|;
block|}
DECL|method|setConnectorPort (Integer port)
specifier|public
name|void
name|setConnectorPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|connectorPort
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getConnectorPort ()
specifier|public
name|Integer
name|getConnectorPort
parameter_list|()
block|{
return|return
name|connectorPort
return|;
block|}
DECL|method|setMBeanServerDefaultDomain (String domain)
specifier|public
name|void
name|setMBeanServerDefaultDomain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|mBeanServerDefaultDomain
operator|=
name|domain
expr_stmt|;
block|}
DECL|method|getMBeanServerDefaultDomain ()
specifier|public
name|String
name|getMBeanServerDefaultDomain
parameter_list|()
block|{
return|return
name|mBeanServerDefaultDomain
return|;
block|}
DECL|method|setMBeanObjectDomainName (String domainName)
specifier|public
name|void
name|setMBeanObjectDomainName
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
name|mBeanObjectDomainName
operator|=
name|domainName
expr_stmt|;
block|}
DECL|method|getMBeanObjectDomainName ()
specifier|public
name|String
name|getMBeanObjectDomainName
parameter_list|()
block|{
return|return
name|mBeanObjectDomainName
return|;
block|}
DECL|method|setServiceUrlPath (String url)
specifier|public
name|void
name|setServiceUrlPath
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|serviceUrlPath
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getServiceUrlPath ()
specifier|public
name|String
name|getServiceUrlPath
parameter_list|()
block|{
return|return
name|serviceUrlPath
return|;
block|}
DECL|method|setCreateConnector (Boolean flag)
specifier|public
name|void
name|setCreateConnector
parameter_list|(
name|Boolean
name|flag
parameter_list|)
block|{
name|createConnector
operator|=
name|flag
expr_stmt|;
block|}
DECL|method|getCreateConnector ()
specifier|public
name|Boolean
name|getCreateConnector
parameter_list|()
block|{
return|return
name|createConnector
return|;
block|}
DECL|method|setUsePlatformMBeanServer (Boolean flag)
specifier|public
name|void
name|setUsePlatformMBeanServer
parameter_list|(
name|Boolean
name|flag
parameter_list|)
block|{
name|usePlatformMBeanServer
operator|=
name|flag
expr_stmt|;
block|}
DECL|method|getUsePlatformMBeanServer ()
specifier|public
name|Boolean
name|getUsePlatformMBeanServer
parameter_list|()
block|{
return|return
name|usePlatformMBeanServer
return|;
block|}
DECL|method|getOnlyRegisterProcessorWithCustomId ()
specifier|public
name|Boolean
name|getOnlyRegisterProcessorWithCustomId
parameter_list|()
block|{
return|return
name|onlyRegisterProcessorWithCustomId
return|;
block|}
DECL|method|setOnlyRegisterProcessorWithCustomId (Boolean onlyRegisterProcessorWithCustomId)
specifier|public
name|void
name|setOnlyRegisterProcessorWithCustomId
parameter_list|(
name|Boolean
name|onlyRegisterProcessorWithCustomId
parameter_list|)
block|{
name|this
operator|.
name|onlyRegisterProcessorWithCustomId
operator|=
name|onlyRegisterProcessorWithCustomId
expr_stmt|;
block|}
DECL|method|setMBeanServer (MBeanServer mbeanServer)
specifier|public
name|void
name|setMBeanServer
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
block|{
name|server
operator|=
name|mbeanServer
expr_stmt|;
block|}
DECL|method|getMBeanServer ()
specifier|public
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
DECL|method|getRegisterAlways ()
specifier|public
name|Boolean
name|getRegisterAlways
parameter_list|()
block|{
return|return
name|registerAlways
operator|!=
literal|null
operator|&&
name|registerAlways
return|;
block|}
DECL|method|setRegisterAlways (Boolean registerAlways)
specifier|public
name|void
name|setRegisterAlways
parameter_list|(
name|Boolean
name|registerAlways
parameter_list|)
block|{
name|this
operator|.
name|registerAlways
operator|=
name|registerAlways
expr_stmt|;
block|}
DECL|method|getRegisterNewRoutes ()
specifier|public
name|Boolean
name|getRegisterNewRoutes
parameter_list|()
block|{
return|return
name|registerNewRoutes
operator|!=
literal|null
operator|&&
name|registerNewRoutes
return|;
block|}
DECL|method|setRegisterNewRoutes (Boolean registerNewRoutes)
specifier|public
name|void
name|setRegisterNewRoutes
parameter_list|(
name|Boolean
name|registerNewRoutes
parameter_list|)
block|{
name|this
operator|.
name|registerNewRoutes
operator|=
name|registerNewRoutes
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|register (Object obj, ObjectName name)
specifier|public
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
block|{
name|register
argument_list|(
name|obj
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|register (Object obj, ObjectName name, boolean forceRegistration)
specifier|public
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
block|{
try|try
block|{
name|registerMBeanWithServer
argument_list|(
name|obj
argument_list|,
name|name
argument_list|,
name|forceRegistration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NotCompliantMBeanException
name|e
parameter_list|)
block|{
comment|// If this is not a "normal" MBean, then try to deploy it using JMX annotations
name|ManagementMBeanAssembler
name|assembler
init|=
name|camelContext
operator|.
name|getManagementMBeanAssembler
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|assembler
argument_list|,
literal|"ManagementMBeanAssembler"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|Object
name|mbean
init|=
name|assembler
operator|.
name|assemble
argument_list|(
name|server
argument_list|,
name|obj
argument_list|,
name|name
argument_list|)
decl_stmt|;
comment|// and register the mbean
name|registerMBeanWithServer
argument_list|(
name|mbean
argument_list|,
name|name
argument_list|,
name|forceRegistration
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|unregister (ObjectName name)
specifier|public
name|void
name|unregister
parameter_list|(
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
block|{
if|if
condition|(
name|isRegistered
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|server
operator|.
name|unregisterMBean
argument_list|(
name|mbeansRegistered
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unregistered MBean with ObjectName: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
name|mbeansRegistered
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|isRegistered (ObjectName name)
specifier|public
name|boolean
name|isRegistered
parameter_list|(
name|ObjectName
name|name
parameter_list|)
block|{
return|return
operator|(
name|mbeansRegistered
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
operator|&&
name|server
operator|.
name|isRegistered
argument_list|(
name|mbeansRegistered
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
operator|)
operator|||
name|server
operator|.
name|isRegistered
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
comment|// create mbean server if is has not be injected.
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|finalizeSettings
argument_list|()
expr_stmt|;
name|createMBeanServer
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting JMX agent on server: {}"
argument_list|,
name|getMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// close JMX Connector, if it was created
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|cs
operator|.
name|stop
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopped JMX Connector"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error occurred during stopping JMXConnectorService: "
operator|+
name|cs
operator|+
literal|". This exception will be ignored."
argument_list|)
expr_stmt|;
block|}
name|cs
operator|=
literal|null
expr_stmt|;
block|}
comment|// Unexport JMX RMI registry, if it was created
if|if
condition|(
name|registry
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|UnicastRemoteObject
operator|.
name|unexportObject
argument_list|(
name|registry
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unexported JMX RMI Registry"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchObjectException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error occurred while unexporting JMX RMI registry. This exception will be ignored."
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|mbeansRegistered
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// Using the array to hold the busMBeans to avoid the CurrentModificationException
name|ObjectName
index|[]
name|mBeans
init|=
name|mbeansRegistered
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|ObjectName
index|[
name|mbeansRegistered
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|int
name|caught
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|mBeans
control|)
block|{
try|try
block|{
name|unregister
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Exception unregistering MBean with name "
operator|+
name|name
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|caught
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|caught
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"A number of "
operator|+
name|caught
operator|+
literal|" exceptions caught while unregistering MBeans during stop operation."
operator|+
literal|" See INFO log for details."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|registerMBeanWithServer (Object obj, ObjectName name, boolean forceRegistration)
specifier|private
name|void
name|registerMBeanWithServer
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
block|{
comment|// have we already registered the bean, there can be shared instances in the camel routes
name|boolean
name|exists
init|=
name|isRegistered
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|exists
condition|)
block|{
if|if
condition|(
name|forceRegistration
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"ForceRegistration enabled, unregistering existing MBean with ObjectName: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|server
operator|.
name|unregisterMBean
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// okay ignore we do not want to force it and it could be a shared instance
name|LOG
operator|.
name|debug
argument_list|(
literal|"MBean already registered with ObjectName: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|// register bean if by force or not exists
name|ObjectInstance
name|instance
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|forceRegistration
operator|||
operator|!
name|exists
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Registering MBean with ObjectName: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|instance
operator|=
name|server
operator|.
name|registerMBean
argument_list|(
name|obj
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|// need to use the name returned from the server as some JEE servers may modify the name
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|ObjectName
name|registeredName
init|=
name|instance
operator|.
name|getObjectName
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Registered MBean with ObjectName: {}"
argument_list|,
name|registeredName
argument_list|)
expr_stmt|;
name|mbeansRegistered
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|registeredName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createMBeanServer ()
specifier|protected
name|void
name|createMBeanServer
parameter_list|()
block|{
name|String
name|hostName
decl_stmt|;
name|boolean
name|canAccessSystemProps
init|=
literal|true
decl_stmt|;
try|try
block|{
comment|// we'll do it this way mostly to determine if we should lookup the hostName
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
name|sm
operator|.
name|checkPropertiesAccess
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|se
parameter_list|)
block|{
name|canAccessSystemProps
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|canAccessSystemProps
condition|)
block|{
try|try
block|{
name|hostName
operator|=
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|.
name|getHostName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|uhe
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Cannot determine localhost name. Using default: "
operator|+
name|DEFAULT_REGISTRY_PORT
argument_list|,
name|uhe
argument_list|)
expr_stmt|;
name|hostName
operator|=
name|DEFAULT_HOST
expr_stmt|;
block|}
block|}
else|else
block|{
name|hostName
operator|=
name|DEFAULT_HOST
expr_stmt|;
block|}
name|server
operator|=
name|findOrCreateMBeanServer
argument_list|()
expr_stmt|;
try|try
block|{
comment|// Create the connector if we need
if|if
condition|(
name|createConnector
condition|)
block|{
name|createJmxConnector
argument_list|(
name|hostName
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not create and start JMX connector."
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|findOrCreateMBeanServer ()
specifier|protected
name|MBeanServer
name|findOrCreateMBeanServer
parameter_list|()
block|{
comment|// return platform mbean server if the option is specified.
if|if
condition|(
name|usePlatformMBeanServer
condition|)
block|{
return|return
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
return|;
block|}
comment|// look for the first mbean server that has match default domain name
name|List
argument_list|<
name|MBeanServer
argument_list|>
name|servers
init|=
name|MBeanServerFactory
operator|.
name|findMBeanServer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|MBeanServer
name|server
range|:
name|servers
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found MBeanServer with default domain {}"
argument_list|,
name|server
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|mBeanServerDefaultDomain
operator|.
name|equals
argument_list|(
name|server
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|server
return|;
block|}
block|}
comment|// create a mbean server with the given default domain name
return|return
name|MBeanServerFactory
operator|.
name|createMBeanServer
argument_list|(
name|mBeanServerDefaultDomain
argument_list|)
return|;
block|}
DECL|method|createJmxConnector (String host)
specifier|protected
name|void
name|createJmxConnector
parameter_list|(
name|String
name|host
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|serviceUrlPath
argument_list|,
literal|"serviceUrlPath"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|registryPort
argument_list|,
literal|"registryPort"
argument_list|)
expr_stmt|;
try|try
block|{
name|registry
operator|=
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
name|registryPort
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created JMXConnector RMI registry on port {}"
argument_list|,
name|registryPort
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RemoteException
name|ex
parameter_list|)
block|{
comment|// The registry may had been created, we could get the registry instead
block|}
comment|// must start with leading slash
name|String
name|path
init|=
name|serviceUrlPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|serviceUrlPath
else|:
literal|"/"
operator|+
name|serviceUrlPath
decl_stmt|;
comment|// Create an RMI connector and start it
specifier|final
name|JMXServiceURL
name|url
decl_stmt|;
if|if
condition|(
name|connectorPort
operator|>
literal|0
condition|)
block|{
name|url
operator|=
operator|new
name|JMXServiceURL
argument_list|(
literal|"service:jmx:rmi://"
operator|+
name|host
operator|+
literal|":"
operator|+
name|connectorPort
operator|+
literal|"/jndi/rmi://"
operator|+
name|host
operator|+
literal|":"
operator|+
name|registryPort
operator|+
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
operator|new
name|JMXServiceURL
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://"
operator|+
name|host
operator|+
literal|":"
operator|+
name|registryPort
operator|+
name|path
argument_list|)
expr_stmt|;
block|}
name|cs
operator|=
name|JMXConnectorServerFactory
operator|.
name|newJMXConnectorServer
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
name|server
argument_list|)
expr_stmt|;
comment|// use async thread for starting the JMX Connector
comment|// (no need to use a thread pool or enlist in JMX as this thread is terminated when the JMX connector has been started)
name|String
name|threadName
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|resolveThreadName
argument_list|(
literal|"JMXConnector: "
operator|+
name|url
argument_list|)
decl_stmt|;
name|Thread
name|thread
init|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThread
argument_list|(
name|threadName
argument_list|,
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Staring JMX Connector thread to listen at: {}"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|cs
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"JMX Connector thread started and listening at: {}"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not start JMXConnector thread at: "
operator|+
name|url
operator|+
literal|". JMX Connector not in use."
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

