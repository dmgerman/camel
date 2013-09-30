begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz2
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
name|Exchange
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
name|Predicate
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
comment|/**  * Tests a Quartz based cluster setup of two Camel Apps being triggered through {@link QuartzConsumer}.  *   * @version  */
end_comment

begin_class
DECL|class|SpringQuartzConsumerTwoAppsClusteredFailoverTest
specifier|public
class|class
name|SpringQuartzConsumerTwoAppsClusteredFailoverTest
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
literal|"org/apache/camel/component/quartz2/SpringQuartzConsumerClusteredAppDatabase.xml"
argument_list|)
decl_stmt|;
name|db
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// now launch the first clustered app
name|AbstractXmlApplicationContext
name|app
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/quartz2/SpringQuartzConsumerClusteredAppOne.xml"
argument_list|)
decl_stmt|;
name|app
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// as well as the second one
name|AbstractXmlApplicationContext
name|app2
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/quartz2/SpringQuartzConsumerClusteredAppTwo.xml"
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
name|expectedMinimumMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|ClusteringPredicate
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// let the route run a bit...
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now let's simulate a crash of the first app
name|log
operator|.
name|warn
argument_list|(
literal|"The first app is going to crash NOW!"
argument_list|)
expr_stmt|;
name|app
operator|.
name|close
argument_list|()
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
comment|// inside the logs one can then clearly see how the route of the second CamelContext gets started:
comment|// 2013-09-30 11:22:20,349 [main           ] WARN  erTwoAppsClusteredFailoverTest - Crashed...
comment|// 2013-09-30 11:22:20,349 [main           ] WARN  erTwoAppsClusteredFailoverTest - Crashed...
comment|// 2013-09-30 11:22:20,349 [main           ] WARN  erTwoAppsClusteredFailoverTest - Crashed...
comment|// 2013-09-30 11:22:35,340 [_ClusterManager] INFO  LocalDataSourceJobStore        - ClusterManager: detected 1 failed or restarted instances.
comment|// 2013-09-30 11:22:35,340 [_ClusterManager] INFO  LocalDataSourceJobStore        - ClusterManager: Scanning for instance "app-one"'s failed in-progress jobs.
comment|// 2013-09-30 11:22:35,369 [eduler_Worker-1] INFO  triggered                      - Exchange[ExchangePattern: InOnly, BodyType: String, Body: clustering PONGS!]
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
name|expectedMinimumMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|ClusteringPredicate
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// stop the second app as we're already done
name|app2
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// and as the last step shutdown the database...
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|class|ClusteringPredicate
specifier|private
specifier|static
class|class
name|ClusteringPredicate
implements|implements
name|Predicate
block|{
DECL|field|expectedPayload
specifier|private
specifier|final
name|String
name|expectedPayload
decl_stmt|;
DECL|method|ClusteringPredicate (boolean pings)
name|ClusteringPredicate
parameter_list|(
name|boolean
name|pings
parameter_list|)
block|{
name|expectedPayload
operator|=
name|pings
condition|?
literal|"clustering PINGS!"
else|:
literal|"clustering PONGS!"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|equals
argument_list|(
name|expectedPayload
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

