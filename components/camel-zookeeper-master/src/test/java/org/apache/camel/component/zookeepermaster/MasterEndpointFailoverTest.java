begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
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
name|atomic
operator|.
name|AtomicInteger
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
name|SimpleRegistry
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|framework
operator|.
name|CuratorFramework
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

begin_class
DECL|class|MasterEndpointFailoverTest
specifier|public
class|class
name|MasterEndpointFailoverTest
block|{
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
name|MasterEndpointFailoverTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|producerContext
specifier|protected
name|CamelContext
name|producerContext
decl_stmt|;
DECL|field|consumerContext1
specifier|protected
name|CamelContext
name|consumerContext1
decl_stmt|;
DECL|field|consumerContext2
specifier|protected
name|CamelContext
name|consumerContext2
decl_stmt|;
DECL|field|result1Endpoint
specifier|protected
name|MockEndpoint
name|result1Endpoint
decl_stmt|;
DECL|field|result2Endpoint
specifier|protected
name|MockEndpoint
name|result2Endpoint
decl_stmt|;
DECL|field|messageCounter
specifier|protected
name|AtomicInteger
name|messageCounter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|serverFactoryBean
specifier|protected
name|ZKServerFactoryBean
name|serverFactoryBean
init|=
operator|new
name|ZKServerFactoryBean
argument_list|()
decl_stmt|;
DECL|field|zkClientBean
specifier|protected
name|CuratorFactoryBean
name|zkClientBean
init|=
operator|new
name|CuratorFactoryBean
argument_list|()
decl_stmt|;
annotation|@
name|Before
DECL|method|beforeRun ()
specifier|public
name|void
name|beforeRun
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting ZK server!"
argument_list|)
expr_stmt|;
name|serverFactoryBean
operator|.
name|setPort
argument_list|(
literal|9004
argument_list|)
expr_stmt|;
name|serverFactoryBean
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
comment|// Create the zkClientBean
name|zkClientBean
operator|.
name|setConnectString
argument_list|(
literal|"localhost:9004"
argument_list|)
expr_stmt|;
name|CuratorFramework
name|client
init|=
name|zkClientBean
operator|.
name|getObject
argument_list|()
decl_stmt|;
comment|// Need to bind the zookeeper client with the name "curator"
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"curator"
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|producerContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
comment|// Add the vm:start endpoint to avoid the NPE before starting the consumerContext1
name|producerContext
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"vm:start"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|template
operator|=
name|producerContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|consumerContext1
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|consumerContext1
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
literal|"zookeeper-master:MasterEndpointFailoverTest:vm:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|consumerContext2
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|consumerContext2
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
literal|"zookeeper-master:MasterEndpointFailoverTest:vm:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Need to start at less one consumerContext to enable the vm queue for producerContext
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|consumerContext1
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|producerContext
argument_list|)
expr_stmt|;
name|result1Endpoint
operator|=
name|consumerContext1
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result1"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|result2Endpoint
operator|=
name|consumerContext2
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result2"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|afterRun ()
specifier|public
name|void
name|afterRun
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|consumerContext1
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|consumerContext2
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|producerContext
argument_list|)
expr_stmt|;
name|zkClientBean
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|serverFactoryBean
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpoint ()
specifier|public
name|void
name|testEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting consumerContext1"
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|consumerContext1
argument_list|)
expr_stmt|;
name|assertMessageReceived
argument_list|(
name|result1Endpoint
argument_list|,
name|result2Endpoint
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting consumerContext2"
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|consumerContext2
argument_list|)
expr_stmt|;
name|assertMessageReceivedLoop
argument_list|(
name|result1Endpoint
argument_list|,
name|result2Endpoint
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Stopping consumerContext1"
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|consumerContext1
argument_list|)
expr_stmt|;
name|assertMessageReceivedLoop
argument_list|(
name|result2Endpoint
argument_list|,
name|result1Endpoint
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMessageReceivedLoop (MockEndpoint masterEndpoint, MockEndpoint standbyEndpoint, int count)
specifier|protected
name|void
name|assertMessageReceivedLoop
parameter_list|(
name|MockEndpoint
name|masterEndpoint
parameter_list|,
name|MockEndpoint
name|standbyEndpoint
parameter_list|,
name|int
name|count
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertMessageReceived
argument_list|(
name|masterEndpoint
argument_list|,
name|standbyEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertMessageReceived (MockEndpoint masterEndpoint, MockEndpoint standbyEndpoint)
specifier|protected
name|void
name|assertMessageReceived
parameter_list|(
name|MockEndpoint
name|masterEndpoint
parameter_list|,
name|MockEndpoint
name|standbyEndpoint
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|masterEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|standbyEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|String
name|expectedBody
init|=
name|createNextExpectedBody
argument_list|()
decl_stmt|;
name|masterEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|standbyEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Expecting master: "
operator|+
name|masterEndpoint
operator|+
literal|" and standby: "
operator|+
name|standbyEndpoint
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|masterEndpoint
argument_list|,
name|standbyEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|createNextExpectedBody ()
specifier|protected
name|String
name|createNextExpectedBody
parameter_list|()
block|{
return|return
literal|"body:"
operator|+
name|messageCounter
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

