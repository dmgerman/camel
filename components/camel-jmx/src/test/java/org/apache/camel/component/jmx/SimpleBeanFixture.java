begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|InstanceNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanRegistrationException
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
name|MBeanServerInvocationHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MalformedObjectNameException
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
name|builder
operator|.
name|RouteBuilder
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
name|jmx
operator|.
name|beans
operator|.
name|ISimpleMXBean
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
name|jmx
operator|.
name|beans
operator|.
name|SimpleBean
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
name|mock
operator|.
name|MockEndpoint
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
name|support
operator|.
name|SimpleRegistry
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
name|Before
import|;
end_import

begin_comment
comment|/**  * MBean that is registered for the unit tests. The fixture will register a bean  * and provide access to the mxbean so tests can invoke methods on the mxbean  * to trigger notifications.  */
end_comment

begin_class
DECL|class|SimpleBeanFixture
specifier|public
class|class
name|SimpleBeanFixture
block|{
comment|/**      * domain to use for the mbean      */
DECL|field|DOMAIN
specifier|protected
specifier|static
specifier|final
name|String
name|DOMAIN
init|=
literal|"TestDomain"
decl_stmt|;
comment|/**      * key for the object name      */
DECL|field|NAME
specifier|protected
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
DECL|field|server
specifier|protected
name|MBeanServer
name|server
decl_stmt|;
comment|/**      * camel context to stand up for the test      */
DECL|field|mContext
specifier|private
name|DefaultCamelContext
name|mContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|/**      * registry to store referenced beans (i.e. objectProperties or NotificationFilter)      */
DECL|field|mRegistry
specifier|private
name|SimpleRegistry
name|mRegistry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
comment|/**      * destination for the simple route created.      */
DECL|field|mMockEndpoint
specifier|private
name|MockEndpointFixture
name|mMockEndpoint
decl_stmt|;
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
name|initServer
argument_list|()
expr_stmt|;
name|initBean
argument_list|()
expr_stmt|;
name|initRegistry
argument_list|()
expr_stmt|;
name|initContext
argument_list|()
expr_stmt|;
name|startContext
argument_list|()
expr_stmt|;
block|}
DECL|method|startContext ()
specifier|protected
name|void
name|startContext
parameter_list|()
throws|throws
name|Exception
block|{
name|mContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
operator|!
name|mContext
operator|.
name|isStopped
argument_list|()
condition|)
block|{
name|mContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|unregisterBean
argument_list|(
name|makeObjectName
argument_list|(
literal|"simpleBean"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|initServer ()
specifier|protected
name|void
name|initServer
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
expr_stmt|;
block|}
comment|/**      * Registers the bean on the platform mbean server      */
DECL|method|registerBean (Object aBean, ObjectName aObjectName)
specifier|protected
name|void
name|registerBean
parameter_list|(
name|Object
name|aBean
parameter_list|,
name|ObjectName
name|aObjectName
parameter_list|)
throws|throws
name|Exception
block|{
name|server
operator|.
name|registerMBean
argument_list|(
name|aBean
argument_list|,
name|aObjectName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unregisters the bean      */
DECL|method|unregisterBean (ObjectName aObjectName)
specifier|protected
name|void
name|unregisterBean
parameter_list|(
name|ObjectName
name|aObjectName
parameter_list|)
throws|throws
name|MBeanRegistrationException
throws|,
name|InstanceNotFoundException
block|{
name|server
operator|.
name|unregisterMBean
argument_list|(
name|aObjectName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the mxbean for our remote object using the specified name      */
DECL|method|getMXBean (ObjectName aObjectName)
specifier|protected
name|ISimpleMXBean
name|getMXBean
parameter_list|(
name|ObjectName
name|aObjectName
parameter_list|)
block|{
return|return
name|MBeanServerInvocationHandler
operator|.
name|newProxyInstance
argument_list|(
name|server
argument_list|,
name|aObjectName
argument_list|,
name|ISimpleMXBean
operator|.
name|class
argument_list|,
literal|false
argument_list|)
return|;
comment|// revert the above change to the below when we move to JDK 1.6
comment|//        ISimpleMXBean simpleBean = JMX.newMXBeanProxy(server, aObjectName, ISimpleMXBean.class);
comment|//        return simpleBean;
block|}
comment|/**      * Gets the mxbean for our remote object using the default name "simpleBean"      */
DECL|method|getSimpleMXBean ()
specifier|protected
name|ISimpleMXBean
name|getSimpleMXBean
parameter_list|()
throws|throws
name|MalformedObjectNameException
block|{
return|return
name|getMXBean
argument_list|(
name|makeObjectName
argument_list|(
literal|"simpleBean"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Makes an ObjectName for the given domain using our domain and the name attribute.      */
DECL|method|makeObjectName (String aName)
specifier|protected
name|ObjectName
name|makeObjectName
parameter_list|(
name|String
name|aName
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|ObjectName
name|objectName
init|=
operator|new
name|ObjectName
argument_list|(
name|DOMAIN
argument_list|,
name|NAME
argument_list|,
name|aName
argument_list|)
decl_stmt|;
return|return
name|objectName
return|;
block|}
comment|/**      * Creates the bean and registers it within the mbean server.      * Note that we're using a fixed timestamp here to simplify the assertions in the tests      */
DECL|method|initBean ()
specifier|protected
name|void
name|initBean
parameter_list|()
throws|throws
name|Exception
block|{
name|registerBean
argument_list|(
name|createSimpleBean
argument_list|()
argument_list|,
name|makeObjectName
argument_list|(
literal|"simpleBean"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createSimpleBean ()
specifier|protected
name|SimpleBean
name|createSimpleBean
parameter_list|()
throws|throws
name|ParseException
block|{
name|SimpleBean
name|simpleBean
init|=
operator|new
name|SimpleBean
argument_list|()
decl_stmt|;
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-dd-MM'T'HH:mm:ss"
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|sdf
operator|.
name|parse
argument_list|(
literal|"2010-07-01T10:30:15"
argument_list|)
decl_stmt|;
name|simpleBean
operator|.
name|setTimestamp
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|simpleBean
return|;
block|}
comment|/**      * Initializes the camel context by creating a simple route from our mbean      * to the mock endpoint.      */
DECL|method|initContext ()
specifier|protected
name|void
name|initContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|MockEndpoint
name|mock
init|=
name|mContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:sink"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mMockEndpoint
operator|=
operator|new
name|MockEndpointFixture
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|mContext
operator|.
name|setRegistry
argument_list|(
name|getRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|mContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|buildFromURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Override this to control the properties that make up the endpoint      */
DECL|method|buildFromURI ()
specifier|protected
name|JMXUriBuilder
name|buildFromURI
parameter_list|()
block|{
name|JMXUriBuilder
name|uri
init|=
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withObjectDomain
argument_list|(
name|DOMAIN
argument_list|)
operator|.
name|withObjectName
argument_list|(
literal|"simpleBean"
argument_list|)
decl_stmt|;
return|return
name|uri
return|;
block|}
comment|/**      * Override this to put stuff into the registry so it's available to be      * referenced. (i.e. NotificationFilter or Hashtable<String,String> for ObjectProperties      */
DECL|method|initRegistry ()
specifier|protected
name|void
name|initRegistry
parameter_list|()
block|{     }
DECL|method|getContext ()
specifier|protected
name|DefaultCamelContext
name|getContext
parameter_list|()
block|{
return|return
name|mContext
return|;
block|}
DECL|method|setContext (DefaultCamelContext aContext)
specifier|protected
name|void
name|setContext
parameter_list|(
name|DefaultCamelContext
name|aContext
parameter_list|)
block|{
name|mContext
operator|=
name|aContext
expr_stmt|;
block|}
DECL|method|getRegistry ()
specifier|protected
name|SimpleRegistry
name|getRegistry
parameter_list|()
block|{
return|return
name|mRegistry
return|;
block|}
DECL|method|setRegistry (SimpleRegistry aRegistry)
specifier|protected
name|void
name|setRegistry
parameter_list|(
name|SimpleRegistry
name|aRegistry
parameter_list|)
block|{
name|mRegistry
operator|=
name|aRegistry
expr_stmt|;
block|}
DECL|method|getMockFixture ()
specifier|protected
name|MockEndpointFixture
name|getMockFixture
parameter_list|()
block|{
return|return
name|mMockEndpoint
return|;
block|}
block|}
end_class

end_unit

