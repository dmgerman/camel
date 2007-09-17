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
name|util
operator|.
name|Collection
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
name|spi
operator|.
name|LifecycleStrategy
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
name|Route
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
name|model
operator|.
name|RouteType
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

begin_class
DECL|class|InstrumentationLifecycleStrategy
specifier|public
class|class
name|InstrumentationLifecycleStrategy
implements|implements
name|LifecycleStrategy
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
name|InstrumentationProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|agent
specifier|private
name|InstrumentationAgent
name|agent
decl_stmt|;
DECL|field|namingStrategy
specifier|private
name|CamelNamingStrategy
name|namingStrategy
decl_stmt|;
DECL|method|InstrumentationLifecycleStrategy (InstrumentationAgent agent)
specifier|public
name|InstrumentationLifecycleStrategy
parameter_list|(
name|InstrumentationAgent
name|agent
parameter_list|)
block|{
name|this
operator|.
name|agent
operator|=
name|agent
expr_stmt|;
name|setNamingStrategy
argument_list|(
name|agent
operator|.
name|getNamingStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|onContextCreate (CamelContext context)
specifier|public
name|void
name|onContextCreate
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
try|try
block|{
name|DefaultCamelContext
name|dc
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
name|ManagedService
name|ms
init|=
operator|new
name|ManagedService
argument_list|(
name|dc
argument_list|)
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|ms
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|dc
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not register CamelContext MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|onEndpointAdd (Endpoint endpoint)
specifier|public
name|void
name|onEndpointAdd
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
try|try
block|{
name|ManagedEndpoint
name|me
init|=
operator|new
name|ManagedEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|me
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|me
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not register Endpoint MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onRoutesAdd (Collection<Route> routes)
specifier|public
name|void
name|onRoutesAdd
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
try|try
block|{
name|ManagedRoute
name|mr
init|=
operator|new
name|ManagedRoute
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|mr
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|mr
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not register Route MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|onServiceAdd (CamelContext context, Service service)
specifier|public
name|void
name|onServiceAdd
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|ServiceSupport
condition|)
block|{
try|try
block|{
name|ManagedService
name|ms
init|=
operator|new
name|ManagedService
argument_list|(
operator|(
name|ServiceSupport
operator|)
name|service
argument_list|)
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|ms
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|context
argument_list|,
name|ms
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not register Service MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|beforeStartRouteType (CamelContext context, RouteType routeType)
specifier|public
name|void
name|beforeStartRouteType
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RouteType
name|routeType
parameter_list|)
block|{
name|PerformanceCounter
name|mc
init|=
operator|new
name|PerformanceCounter
argument_list|()
decl_stmt|;
name|routeType
operator|.
name|intercept
argument_list|(
operator|new
name|InstrumentationProcessor
argument_list|(
name|mc
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|agent
operator|.
name|register
argument_list|(
name|mc
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|context
argument_list|,
name|mc
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not register Counter MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

