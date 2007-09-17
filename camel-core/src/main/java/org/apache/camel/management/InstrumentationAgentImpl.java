begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|impl
operator|.
name|DefaultCamelContext
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

begin_class
DECL|class|InstrumentationAgentImpl
specifier|public
class|class
name|InstrumentationAgentImpl
extends|extends
name|ServiceSupport
implements|implements
name|InstrumentationAgent
implements|,
name|CamelContextAware
block|{
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
name|InstrumentationAgentImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SYSTEM_PROPERTY_JMX
specifier|public
specifier|static
specifier|final
name|String
name|SYSTEM_PROPERTY_JMX
init|=
literal|"org.apache.camel.jmx"
decl_stmt|;
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
DECL|field|DEFAULT_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|1099
decl_stmt|;
DECL|field|server
specifier|private
name|MBeanServer
name|server
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
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
DECL|field|jmxEnabled
specifier|private
name|boolean
name|jmxEnabled
init|=
literal|false
decl_stmt|;
DECL|field|jmxDomainName
specifier|private
name|String
name|jmxDomainName
init|=
literal|null
decl_stmt|;
DECL|field|jmxConnectorPort
specifier|private
name|int
name|jmxConnectorPort
init|=
literal|0
decl_stmt|;
DECL|field|namingStrategy
specifier|private
name|CamelNamingStrategy
name|namingStrategy
decl_stmt|;
DECL|method|InstrumentationAgentImpl ()
specifier|public
name|InstrumentationAgentImpl
parameter_list|()
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
comment|//naming = new CamelNamingStrategy(agent.getMBeanServer().getDefaultDomain());
name|namingStrategy
operator|=
operator|new
name|CamelNamingStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
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
name|context
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|setMBeanServer (MBeanServer server)
specifier|public
name|void
name|setMBeanServer
parameter_list|(
name|MBeanServer
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
name|jmxEnabled
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|getMBeanServer ()
specifier|public
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|server
operator|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
expr_stmt|;
block|}
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
comment|//If this is not a "normal" MBean, then try to deploy it using JMX annotations
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
DECL|method|getNamingStrategy ()
specifier|public
name|CamelNamingStrategy
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|namingStrategy
return|;
block|}
DECL|method|setNamingStrategy (CamelNamingStrategy namingStrategy)
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|CamelNamingStrategy
name|namingStrategy
parameter_list|)
block|{
name|this
operator|.
name|namingStrategy
operator|=
name|namingStrategy
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
if|if
condition|(
name|getMBeanServer
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// The MBeanServer was not injected
name|createMBeanServer
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|jmxDomainName
operator|==
literal|null
condition|)
block|{
name|jmxDomainName
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|SYSTEM_PROPERTY_JMX
operator|+
literal|".domain"
argument_list|)
expr_stmt|;
if|if
condition|(
name|jmxDomainName
operator|==
literal|null
operator|||
name|jmxDomainName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|jmxDomainName
operator|=
name|DEFAULT_DOMAIN
expr_stmt|;
block|}
block|}
name|configureDomainName
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
name|DefaultCamelContext
name|dc
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
name|InstrumentationLifecycleStrategy
name|ls
init|=
operator|new
name|InstrumentationLifecycleStrategy
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|dc
operator|.
name|setLifecycleStrategy
argument_list|(
name|ls
argument_list|)
expr_stmt|;
name|ls
operator|.
name|onContextCreate
argument_list|(
name|context
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
comment|//Using the array to hold the busMBeans to avoid the CurrentModificationException
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
literal|" exceptions caught while unregistering MBeans during stop operation.  "
operator|+
literal|"See INFO log for details."
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
name|ObjectInstance
name|instance
init|=
literal|null
decl_stmt|;
try|try
block|{
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
catch|catch
parameter_list|(
name|InstanceAlreadyExistsException
name|e
parameter_list|)
block|{
if|if
condition|(
name|forceRegistration
condition|)
block|{
name|server
operator|.
name|unregisterMBean
argument_list|(
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
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|mbeans
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|enableJmx (String domainName, int port)
specifier|public
name|void
name|enableJmx
parameter_list|(
name|String
name|domainName
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|jmxEnabled
operator|=
literal|true
expr_stmt|;
name|jmxDomainName
operator|=
name|domainName
expr_stmt|;
name|configureDomainName
argument_list|()
expr_stmt|;
name|jmxConnectorPort
operator|=
name|port
expr_stmt|;
block|}
DECL|method|configureDomainName ()
specifier|protected
name|void
name|configureDomainName
parameter_list|()
block|{
if|if
condition|(
name|jmxDomainName
operator|!=
literal|null
condition|)
block|{
name|namingStrategy
operator|.
name|setDomainName
argument_list|(
name|jmxDomainName
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
if|if
condition|(
operator|!
name|jmxEnabled
condition|)
block|{
name|jmxEnabled
operator|=
literal|null
operator|!=
name|System
operator|.
name|getProperty
argument_list|(
name|SYSTEM_PROPERTY_JMX
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|jmxEnabled
condition|)
block|{
comment|// we're done here
return|return;
block|}
block|}
if|if
condition|(
name|jmxConnectorPort
operator|<=
literal|0
condition|)
block|{
name|String
name|portKey
init|=
name|SYSTEM_PROPERTY_JMX
operator|+
literal|".port"
decl_stmt|;
name|String
name|portValue
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|portKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|portValue
operator|!=
literal|null
operator|&&
name|portValue
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|jmxConnectorPort
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portValue
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Invalid port number specified via System property ["
operator|+
name|portKey
operator|+
literal|"="
operator|+
name|portValue
operator|+
literal|"].  Using default: "
operator|+
name|DEFAULT_PORT
argument_list|)
expr_stmt|;
name|jmxConnectorPort
operator|=
name|DEFAULT_PORT
expr_stmt|;
block|}
block|}
block|}
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
literal|"Cannot determine host name.  Using default: "
operator|+
name|DEFAULT_PORT
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
name|jmxDomainName
operator|=
name|jmxDomainName
operator|!=
literal|null
condition|?
name|jmxDomainName
else|:
name|DEFAULT_DOMAIN
expr_stmt|;
name|jmxConnectorPort
operator|=
name|jmxConnectorPort
operator|>
literal|0
condition|?
name|jmxConnectorPort
else|:
name|DEFAULT_PORT
expr_stmt|;
name|hostName
operator|=
name|DEFAULT_HOST
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|jmxEnabled
condition|)
block|{
return|return;
block|}
comment|// jmx is enabled but there's no MBeanServer, so create one
name|List
name|servers
init|=
name|MBeanServerFactory
operator|.
name|findMBeanServer
argument_list|(
name|jmxDomainName
argument_list|)
decl_stmt|;
if|if
condition|(
name|servers
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|server
operator|=
name|MBeanServerFactory
operator|.
name|createMBeanServer
argument_list|(
name|jmxDomainName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|server
operator|=
operator|(
name|MBeanServer
operator|)
name|servers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// we need a connector too
try|try
block|{
name|createJmxConnector
argument_list|(
name|hostName
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
literal|"Could not create and start jmx connector."
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|jmxConnectorPort
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
name|jmxConnectorPort
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RemoteException
name|ex
parameter_list|)
block|{
comment|// the registry may had been created
name|LocateRegistry
operator|.
name|getRegistry
argument_list|(
name|jmxConnectorPort
argument_list|)
expr_stmt|;
block|}
comment|// Create an RMI connector and start it
name|JMXServiceURL
name|url
init|=
operator|new
name|JMXServiceURL
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://"
operator|+
name|host
operator|+
literal|":"
operator|+
name|jmxConnectorPort
operator|+
literal|"/jmxrmi"
argument_list|)
decl_stmt|;
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
literal|"Could not start jmx connector thread."
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
literal|"JMX Connector Thread ["
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
literal|"Jmx connector thread started on "
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

