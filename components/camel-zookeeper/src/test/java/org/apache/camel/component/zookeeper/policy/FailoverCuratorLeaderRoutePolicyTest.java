begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|ExchangePattern
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
name|ProducerTemplate
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
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperTestSupport
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
name|junit
operator|.
name|Test
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_class
DECL|class|FailoverCuratorLeaderRoutePolicyTest
specifier|public
class|class
name|FailoverCuratorLeaderRoutePolicyTest
extends|extends
name|ZooKeeperTestSupport
block|{
DECL|field|ZNODE
specifier|public
specifier|static
specifier|final
name|String
name|ZNODE
init|=
literal|"/curatorleader"
decl_stmt|;
DECL|field|BASE_ZNODE
specifier|public
specifier|static
specifier|final
name|String
name|BASE_ZNODE
init|=
literal|"/someapp"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FailoverCuratorLeaderRoutePolicyTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|disableJMX
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|createCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|masterSlaveScenarioContolledByPolicy ()
specifier|public
name|void
name|masterSlaveScenarioContolledByPolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|ZookeeperPolicyEnforcedContext
name|master
init|=
name|createEnforcedContext
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|ZookeeperPolicyEnforcedContext
name|slave
init|=
name|createEnforcedContext
argument_list|(
literal|"slave"
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// Send messages to the master and the slave.
comment|// The route is enabled in the master and gets through, but that sent to
comment|// the slave context is rejected.
name|master
operator|.
name|sendMessageToEnforcedRoute
argument_list|(
literal|"message for master"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|slave
operator|.
name|sendMessageToEnforcedRoute
argument_list|(
literal|"message for slave"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// trigger failover by killing the master... then assert that the slave
comment|// can now receive messages.
name|master
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|slave
operator|.
name|sendMessageToEnforcedRoute
argument_list|(
literal|"second message for slave"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|slave
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ensureRoutesDoNotStartBeforeElection ()
specifier|public
name|void
name|ensureRoutesDoNotStartBeforeElection
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
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
name|CuratorLeaderRoutePolicy
name|policy
init|=
operator|new
name|CuratorLeaderRoutePolicy
argument_list|(
literal|"zookeeper:localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
name|BASE_ZNODE
operator|+
name|ZNODE
operator|+
literal|2
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"timer://foo?fixedRate=true&period=5"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|id
argument_list|(
literal|"single_route"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:controlled"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// this check verifies that a route marked as autostartable is not started automatically. It will be the policy responsibility to eventually start it.
name|assertThat
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"single_route"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"single_route"
argument_list|)
operator|.
name|isStarting
argument_list|()
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|class|ZookeeperPolicyEnforcedContext
specifier|private
specifier|static
class|class
name|ZookeeperPolicyEnforcedContext
block|{
DECL|field|controlledContext
specifier|private
name|CamelContext
name|controlledContext
decl_stmt|;
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
DECL|field|routename
specifier|private
name|String
name|routename
decl_stmt|;
DECL|method|ZookeeperPolicyEnforcedContext (String name)
name|ZookeeperPolicyEnforcedContext
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|controlledContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|routename
operator|=
name|name
expr_stmt|;
name|template
operator|=
name|controlledContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|mock
operator|=
name|controlledContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:controlled"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|controlledContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|FailoverRoute
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|controlledContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessageToEnforcedRoute (String message, int expected)
specifier|public
name|void
name|sendMessageToEnforcedRoute
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|expected
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
name|expected
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"vm:"
operator|+
name|routename
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|expected
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected messages..."
argument_list|)
expr_stmt|;
block|}
block|}
name|mock
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
operator|.
name|debug
argument_list|(
literal|"stopping"
argument_list|)
expr_stmt|;
name|controlledContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
operator|.
name|debug
argument_list|(
literal|"stopped"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createEnforcedContext (String name)
specifier|private
name|ZookeeperPolicyEnforcedContext
name|createEnforcedContext
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
throws|,
name|InterruptedException
block|{
name|ZookeeperPolicyEnforcedContext
name|context
init|=
operator|new
name|ZookeeperPolicyEnforcedContext
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|delay
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|class|FailoverRoute
specifier|public
specifier|static
class|class
name|FailoverRoute
extends|extends
name|RouteBuilder
block|{
DECL|field|routename
specifier|private
name|String
name|routename
decl_stmt|;
DECL|method|FailoverRoute (String routename)
specifier|public
name|FailoverRoute
parameter_list|(
name|String
name|routename
parameter_list|)
block|{
comment|// need names as if we use the same direct ep name in two contexts
comment|// in the same vm shutting down one context shuts the endpoint for
comment|// both.
name|this
operator|.
name|routename
operator|=
name|routename
expr_stmt|;
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|CuratorLeaderRoutePolicy
name|policy
init|=
operator|new
name|CuratorLeaderRoutePolicy
argument_list|(
literal|"zookeeper:localhost:"
operator|+
name|getServerPort
argument_list|()
operator|+
name|BASE_ZNODE
operator|+
name|ZNODE
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"vm:"
operator|+
name|routename
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|id
argument_list|(
name|routename
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:controlled"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

