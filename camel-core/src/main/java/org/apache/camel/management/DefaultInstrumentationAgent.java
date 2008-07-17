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
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|InstanceAlreadyExistsException
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
name|modelmbean
operator|.
name|InvalidTargetObjectTypeException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBeanInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|RequiredModelMBean
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
name|impl
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
name|spi
operator|.
name|InstrumentationAgent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|AnnotationJmxAttributeSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|assembler
operator|.
name|MetadataMBeanInfoAssembler
import|;
end_import

begin_comment
comment|/**  * Default implementation of the Camel JMX service agent  */
end_comment

begin_class
DECL|class|DefaultInstrumentationAgent
specifier|public
class|class
name|DefaultInstrumentationAgent
extends|extends
name|ServiceSupport
implements|implements
name|InstrumentationAgent
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
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultInstrumentationAgent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|server
specifier|private
name|MBeanServer
name|server
decl_stmt|;
DECL|field|mbeans
specifier|private
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|mbeans
init|=
operator|new
name|HashSet
argument_list|<
name|ObjectName
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|assembler
specifier|private
name|MetadataMBeanInfoAssembler
name|assembler
decl_stmt|;
DECL|field|cs
specifier|private
name|JMXConnectorServer
name|cs
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
decl_stmt|;
DECL|field|createConnector
specifier|private
name|Boolean
name|createConnector
decl_stmt|;
DECL|method|finalizeSettings ()
specifier|protected
name|void
name|finalizeSettings
parameter_list|()
block|{
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
name|usePlatformMBeanServer
operator|==
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
block|}
DECL|method|setRegistryPort (Integer value)
specifier|public
name|void
name|setRegistryPort
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|registryPort
operator|=
name|value
expr_stmt|;
block|}
DECL|method|setConnectorPort (Integer value)
specifier|public
name|void
name|setConnectorPort
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|connectorPort
operator|=
name|value
expr_stmt|;
block|}
DECL|method|setMBeanServerDefaultDomain (String value)
specifier|public
name|void
name|setMBeanServerDefaultDomain
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|mBeanServerDefaultDomain
operator|=
name|value
expr_stmt|;
block|}
DECL|method|setMBeanObjectDomainName (String value)
specifier|public
name|void
name|setMBeanObjectDomainName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|mBeanObjectDomainName
operator|=
name|value
expr_stmt|;
block|}
DECL|method|setServiceUrlPath (String value)
specifier|public
name|void
name|setServiceUrlPath
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|serviceUrlPath
operator|=
name|value
expr_stmt|;
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
comment|// If this is not a "normal" MBean, then try to deploy it using JMX
comment|// annotations
name|ModelMBeanInfo
name|mbi
init|=
literal|null
decl_stmt|;
name|mbi
operator|=
name|assembler
operator|.
name|getMBeanInfo
argument_list|(
name|obj
argument_list|,
name|name
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|RequiredModelMBean
name|mbean
init|=
operator|(
name|RequiredModelMBean
operator|)
name|server
operator|.
name|instantiate
argument_list|(
name|RequiredModelMBean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|mbean
operator|.
name|setModelMBeanInfo
argument_list|(
name|mbi
argument_list|)
expr_stmt|;
try|try
block|{
name|mbean
operator|.
name|setManagedResource
argument_list|(
name|obj
argument_list|,
literal|"ObjectReference"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidTargetObjectTypeException
name|itotex
parameter_list|)
block|{
throw|throw
operator|new
name|JMException
argument_list|(
name|itotex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
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
name|server
operator|.
name|unregisterMBean
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|assembler
operator|=
operator|new
name|MetadataMBeanInfoAssembler
argument_list|()
expr_stmt|;
name|assembler
operator|.
name|setAttributeSource
argument_list|(
operator|new
name|AnnotationJmxAttributeSource
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting JMX agent on server: "
operator|+
name|getMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// close JMX Connector
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
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|cs
operator|=
literal|null
expr_stmt|;
block|}
comment|// Using the array to hold the busMBeans to avoid the
comment|// CurrentModificationException
name|Object
index|[]
name|mBeans
init|=
name|mbeans
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|int
name|caught
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|name
range|:
name|mBeans
control|)
block|{
name|mbeans
operator|.
name|remove
argument_list|(
operator|(
name|ObjectName
operator|)
name|name
argument_list|)
expr_stmt|;
try|try
block|{
name|unregister
argument_list|(
operator|(
name|ObjectName
operator|)
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Exception unregistering MBean"
argument_list|,
name|jmex
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
name|server
operator|.
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
literal|"ForceRegistration enabled, unregistering existing MBean"
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"MBean already registered with objectname: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// register bean if by force or not exsists
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Registering MBean with objectname: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Registered MBean with objectname: "
operator|+
name|registeredName
argument_list|)
expr_stmt|;
block|}
name|mbeans
operator|.
name|add
argument_list|(
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
init|=
name|DEFAULT_HOST
decl_stmt|;
name|boolean
name|canAccessSystemProps
init|=
literal|true
decl_stmt|;
try|try
block|{
comment|// we'll do it this way mostly to determine if we should lookup the
comment|// hostName
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|findOrCreateMBeanServer ()
specifier|protected
name|MBeanServer
name|findOrCreateMBeanServer
parameter_list|()
block|{
comment|// return platform mbean server if the option is specified.
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
operator|||
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
operator|(
name|List
argument_list|<
name|MBeanServer
argument_list|>
operator|)
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found MBeanServer with default domain "
operator|+
name|server
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
try|try
block|{
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
name|registryPort
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created JMXConnector RMI regisry on port "
operator|+
name|registryPort
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RemoteException
name|ex
parameter_list|)
block|{
comment|// The registry may had been created, we could get the registry instead
block|}
comment|// Create an RMI connector and start it
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
name|serviceUrlPath
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
name|serviceUrlPath
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
comment|// Start the connector server asynchronously (in a separate thread).
name|Thread
name|connectorThread
init|=
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|cs
operator|.
name|start
argument_list|()
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
literal|"Could not start JMXConnector thread."
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|connectorThread
operator|.
name|setName
argument_list|(
literal|"Camel JMX Connector Thread ["
operator|+
name|url
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|connectorThread
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"JMX Connector thread started and listening at: "
operator|+
name|url
argument_list|)
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
DECL|method|setServer (MBeanServer value)
specifier|public
name|void
name|setServer
parameter_list|(
name|MBeanServer
name|value
parameter_list|)
block|{
name|server
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

