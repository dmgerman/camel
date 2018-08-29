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
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerConnection
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
name|JMXConnector
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
name|JMXConnectorFactory
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

begin_comment
comment|/**  * Test that verifies JMX connector server can be connected by  * a client.  *  * @version   */
end_comment

begin_class
DECL|class|JmxInstrumentationWithConnectorTest
specifier|public
class|class
name|JmxInstrumentationWithConnectorTest
extends|extends
name|JmxInstrumentationUsingDefaultsTest
block|{
DECL|field|url
specifier|protected
name|String
name|url
decl_stmt|;
DECL|field|clientConnector
specifier|protected
name|JMXConnector
name|clientConnector
decl_stmt|;
DECL|field|registryPort
specifier|protected
name|int
name|registryPort
decl_stmt|;
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|canRunOnThisPlatform ()
specifier|protected
name|boolean
name|canRunOnThisPlatform
parameter_list|()
block|{
name|String
name|os
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
name|boolean
name|aix
init|=
name|os
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"aix"
argument_list|)
decl_stmt|;
name|boolean
name|windows
init|=
name|os
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"windows"
argument_list|)
decl_stmt|;
name|boolean
name|solaris
init|=
name|os
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"sunos"
argument_list|)
decl_stmt|;
comment|// Does not work on AIX / solaris and the problem is hard to identify, could be issues not allowing to use a custom port
comment|// java.io.IOException: Failed to retrieve RMIServer stub: javax.naming.NameNotFoundException: jmxrmi/camel
comment|// windows CI servers is often slow/tricky so skip as well
return|return
operator|!
name|aix
operator|&&
operator|!
name|solaris
operator|&&
operator|!
name|windows
return|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|registryPort
operator|=
literal|30000
operator|+
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using port "
operator|+
name|registryPort
argument_list|)
expr_stmt|;
name|url
operator|=
literal|"service:jmx:rmi:///jndi/rmi://localhost:"
operator|+
name|registryPort
operator|+
literal|"/jmxrmi/camel"
expr_stmt|;
comment|// need to explicit set it to false to use non-platform mbs
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|CREATE_CONNECTOR
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTRY_PORT
argument_list|,
literal|""
operator|+
name|registryPort
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|clientConnector
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|clientConnector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|clientConnector
operator|=
literal|null
expr_stmt|;
block|}
comment|// restore environment to original state
comment|// the following properties may have been set by specialization of this test class
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DOMAIN
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|MBEAN_DOMAIN
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|CREATE_CONNECTOR
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTRY_PORT
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMBeanConnection ()
specifier|protected
name|MBeanServerConnection
name|getMBeanConnection
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|mbsc
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|clientConnector
operator|==
literal|null
condition|)
block|{
name|clientConnector
operator|=
name|JMXConnectorFactory
operator|.
name|connect
argument_list|(
operator|new
name|JMXServiceURL
argument_list|(
name|url
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|mbsc
operator|=
name|clientConnector
operator|.
name|getMBeanServerConnection
argument_list|()
expr_stmt|;
block|}
return|return
name|mbsc
return|;
block|}
annotation|@
name|Test
DECL|method|testRmiRegistryUnexported ()
specifier|public
name|void
name|testRmiRegistryUnexported
parameter_list|()
throws|throws
name|Exception
block|{
name|Registry
name|registry
init|=
name|LocateRegistry
operator|.
name|getRegistry
argument_list|(
name|registryPort
argument_list|)
decl_stmt|;
comment|// before we stop the context the registry is still exported, so list() should work
name|Exception
name|e
decl_stmt|;
try|try
block|{
name|registry
operator|.
name|list
argument_list|()
expr_stmt|;
name|e
operator|=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchObjectException
name|nsoe
parameter_list|)
block|{
name|e
operator|=
name|nsoe
expr_stmt|;
block|}
name|assertNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// stop the Camel context
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// stopping the Camel context unexported the registry, so list() should fail
name|Exception
name|e2
decl_stmt|;
try|try
block|{
name|registry
operator|.
name|list
argument_list|()
expr_stmt|;
name|e2
operator|=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchObjectException
name|nsoe
parameter_list|)
block|{
name|e2
operator|=
name|nsoe
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|e2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

