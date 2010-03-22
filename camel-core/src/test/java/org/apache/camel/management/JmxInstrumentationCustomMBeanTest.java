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
name|util
operator|.
name|Iterator
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
name|Endpoint
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
name|direct
operator|.
name|DirectEndpoint
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
name|DefaultComponent
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
name|CastUtils
import|;
end_import

begin_comment
comment|/**  * JmxInstrumentationCustomMBeanTest will verify that all endpoints are registered  * with the mbean server.  */
end_comment

begin_class
DECL|class|JmxInstrumentationCustomMBeanTest
specifier|public
class|class
name|JmxInstrumentationCustomMBeanTest
extends|extends
name|JmxInstrumentationUsingDefaultsTest
block|{
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
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"custom"
argument_list|,
operator|new
name|CustomComponent
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultManagementNamingStrategy
name|naming
init|=
operator|(
name|DefaultManagementNamingStrategy
operator|)
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementNamingStrategy
argument_list|()
decl_stmt|;
name|naming
operator|.
name|setHostName
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|naming
operator|.
name|setDomainName
argument_list|(
literal|"org.apache.camel"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testCustomEndpoint ()
specifier|public
name|void
name|testCustomEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRunOnThisPlatform
argument_list|()
condition|)
block|{
return|return;
block|}
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
operator|&&
operator|!
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|domainName
argument_list|,
name|mbsc
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resolveMandatoryEndpoint
argument_list|(
literal|"custom://end"
argument_list|,
name|CustomEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=endpoints,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 endpoints: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// get custom
name|Iterator
argument_list|<
name|ObjectName
argument_list|>
name|it
init|=
name|s
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|ObjectName
name|on1
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ObjectName
name|on2
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|on1
operator|.
name|getCanonicalName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"custom"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|mbsc
operator|.
name|getAttribute
argument_list|(
name|on1
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|mbsc
operator|.
name|getAttribute
argument_list|(
name|on2
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testManagedEndpoint ()
specifier|public
name|void
name|testManagedEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRunOnThisPlatform
argument_list|()
condition|)
block|{
return|return;
block|}
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
operator|&&
operator|!
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|domainName
argument_list|,
name|mbsc
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resolveMandatoryEndpoint
argument_list|(
literal|"direct:start"
argument_list|,
name|DirectEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|ObjectName
name|objName
init|=
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=endpoints,*"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|mbsc
operator|.
name|queryNames
argument_list|(
name|objName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCounters ()
specifier|public
name|void
name|testCounters
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRunOnThisPlatform
argument_list|()
condition|)
block|{
return|return;
block|}
name|CustomEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"custom:end"
argument_list|,
name|CustomEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|verifyCounter
argument_list|(
name|mbsc
argument_list|,
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=routes,*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMBeansRegistered ()
specifier|public
name|void
name|testMBeansRegistered
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRunOnThisPlatform
argument_list|()
condition|)
block|{
return|return;
block|}
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
operator|&&
operator|!
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|domainName
argument_list|,
name|mbsc
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=endpoints,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 endpoints: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=context,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 context: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=processors,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 processors: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=routes,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 route: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// need a little delay for fast computers being able to process
comment|// the exchange in 0 millis and we need to simulate a little computation time
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|delay
argument_list|(
literal|10
argument_list|)
operator|.
name|to
argument_list|(
literal|"custom:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|CustomComponent
specifier|private
class|class
name|CustomComponent
extends|extends
name|DefaultComponent
block|{
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|CustomEndpoint
argument_list|(
literal|"custom"
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

