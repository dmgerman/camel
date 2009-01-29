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
comment|/**  * Test that verifies JMX connector server can be connected by  * a client.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmxInstrumentationWithConnectorTest
specifier|public
class|class
name|JmxInstrumentationWithConnectorTest
extends|extends
name|JmxInstrumentationUsingDefaultsTest
block|{
DECL|field|JMXSERVICEURL
specifier|protected
specifier|static
specifier|final
name|String
name|JMXSERVICEURL
init|=
literal|"service:jmx:rmi:///jndi/rmi://localhost:2123/jmxrmi/camel"
decl_stmt|;
DECL|field|clientConnector
specifier|protected
name|JMXConnector
name|clientConnector
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|sleepForConnection
operator|=
literal|3000
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
name|REGISTRY_PORT
argument_list|,
literal|"2123"
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
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTRY_PORT
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
name|USE_PLATFORM_MBS
argument_list|)
expr_stmt|;
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
name|JMXSERVICEURL
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
block|}
end_class

end_unit

