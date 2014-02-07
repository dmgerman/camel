begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.routepolicy.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|routepolicy
operator|.
name|quartz
package|;
end_package

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
name|test
operator|.
name|junit4
operator|.
name|TestSupport
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
name|IOHelper
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * Tests a Quartz based cluster setup of two Camel Apps being triggered through {@link CronScheduledRoutePolicy}.  *   * @version  */
end_comment

begin_class
DECL|class|SpringQuartzTwoAppsClusteredFailoverTest
specifier|public
class|class
name|SpringQuartzTwoAppsClusteredFailoverTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testQuartzPersistentStoreClusteredApp ()
specifier|public
name|void
name|testQuartzPersistentStoreClusteredApp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// boot up the database the two apps are going to share inside a clustered quartz setup
name|AbstractXmlApplicationContext
name|db
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/routepolicy/quartz/SpringQuartzClusteredAppDatabase.xml"
argument_list|)
decl_stmt|;
name|db
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// now launch the first clustered app which will acquire the quartz database lock and become the master
name|AbstractXmlApplicationContext
name|app
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/routepolicy/quartz/SpringQuartzClusteredAppOne.xml"
argument_list|)
decl_stmt|;
name|app
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// as well as the second one which will run in slave mode as it will not be able to acquire the same lock
name|AbstractXmlApplicationContext
name|app2
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/routepolicy/quartz/SpringQuartzClusteredAppTwo.xml"
argument_list|)
decl_stmt|;
name|app2
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelContext
name|camel
init|=
name|app
operator|.
name|getBean
argument_list|(
literal|"camelContext"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"clustering PINGS!"
argument_list|)
expr_stmt|;
comment|// wait a bit to make sure the route has already been properly started through the given route policy
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|app
operator|.
name|getBean
argument_list|(
literal|"template"
argument_list|,
name|ProducerTemplate
operator|.
name|class
argument_list|)
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"clustering"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now let's simulate a crash of the first app (the quartz instance 'app-one')
name|log
operator|.
name|warn
argument_list|(
literal|"The first app is going to crash NOW!"
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|app
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Crashed..."
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Crashed..."
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Crashed..."
argument_list|)
expr_stmt|;
comment|// wait long enough until the second app takes it over...
name|Thread
operator|.
name|sleep
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
comment|// inside the logs one can then clearly see how the route of the second app ('app-two') gets started:
comment|// 2013-09-24 22:51:34,215 [main           ] WARN  ersistentStoreClusteredAppTest - Crashed...
comment|// 2013-09-24 22:51:34,215 [main           ] WARN  ersistentStoreClusteredAppTest - Crashed...
comment|// 2013-09-24 22:51:34,215 [main           ] WARN  ersistentStoreClusteredAppTest - Crashed...
comment|// 2013-09-24 22:51:49,188 [_ClusterManager] INFO  LocalDataSourceJobStore        - ClusterManager: detected 1 failed or restarted instances.
comment|// 2013-09-24 22:51:49,188 [_ClusterManager] INFO  LocalDataSourceJobStore        - ClusterManager: Scanning for instance "app-one"'s failed in-progress jobs.
comment|// 2013-09-24 22:51:49,211 [eduler_Worker-1] INFO  SpringCamelContext             - Route: myRoute started and consuming from: Endpoint[direct://start]
name|CamelContext
name|camel2
init|=
name|app2
operator|.
name|getBean
argument_list|(
literal|"camelContext2"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock2
init|=
name|camel2
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"clustering PONGS!"
argument_list|)
expr_stmt|;
name|app2
operator|.
name|getBean
argument_list|(
literal|"template"
argument_list|,
name|ProducerTemplate
operator|.
name|class
argument_list|)
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"clustering"
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// and as the last step shutdown the second app as well as the database
name|IOHelper
operator|.
name|close
argument_list|(
name|app2
argument_list|,
name|db
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

