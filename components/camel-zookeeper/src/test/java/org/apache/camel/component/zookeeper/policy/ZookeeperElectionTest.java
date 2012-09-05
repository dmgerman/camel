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
name|atomic
operator|.
name|AtomicBoolean
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
DECL|class|ZookeeperElectionTest
specifier|public
class|class
name|ZookeeperElectionTest
extends|extends
name|ZooKeeperTestSupport
block|{
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
name|ZookeeperElectionTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NODE_BASE_KEY
specifier|private
specifier|static
specifier|final
name|String
name|NODE_BASE_KEY
init|=
literal|"/someapp"
decl_stmt|;
DECL|field|NODE_PARTICULAR_KEY
specifier|private
specifier|static
specifier|final
name|String
name|NODE_PARTICULAR_KEY
init|=
literal|"/someapp/somepolicy"
decl_stmt|;
DECL|field|ELECTION_URI
specifier|private
specifier|static
specifier|final
name|String
name|ELECTION_URI
init|=
literal|"zookeeper:localhost:39913/someapp/somepolicy"
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
comment|// set up the parent used to control the election
name|client
operator|.
name|createPersistent
argument_list|(
name|NODE_BASE_KEY
argument_list|,
literal|"App node to contain policy election nodes..."
argument_list|)
expr_stmt|;
name|client
operator|.
name|createPersistent
argument_list|(
name|NODE_PARTICULAR_KEY
argument_list|,
literal|"Policy node used by route policy to control routes..."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|after ()
specifier|public
name|void
name|after
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|deleteAll
argument_list|(
name|NODE_PARTICULAR_KEY
argument_list|)
expr_stmt|;
name|client
operator|.
name|delete
argument_list|(
name|NODE_BASE_KEY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|masterCanBeElected ()
specifier|public
name|void
name|masterCanBeElected
parameter_list|()
throws|throws
name|Exception
block|{
name|ZooKeeperElection
name|candidate
init|=
operator|new
name|ZooKeeperElection
argument_list|(
name|template
argument_list|,
name|context
argument_list|,
name|ELECTION_URI
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The only election candidate was not elected as master."
argument_list|,
name|candidate
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|masterAndSlave ()
specifier|public
name|void
name|masterAndSlave
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DefaultCamelContext
name|candidateOneContext
init|=
name|createNewContext
argument_list|()
decl_stmt|;
specifier|final
name|DefaultCamelContext
name|candidateTwoContext
init|=
name|createNewContext
argument_list|()
decl_stmt|;
name|ZooKeeperElection
name|electionCandidate1
init|=
name|createElectionCandidate
argument_list|(
name|candidateOneContext
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The first candidate was not elected."
argument_list|,
name|electionCandidate1
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
name|ZooKeeperElection
name|electionCandidate2
init|=
name|createElectionCandidate
argument_list|(
name|candidateTwoContext
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"The second candidate should not have been elected."
argument_list|,
name|electionCandidate2
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMasterGoesAway ()
specifier|public
name|void
name|testMasterGoesAway
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DefaultCamelContext
name|candidateOneContext
init|=
name|createNewContext
argument_list|()
decl_stmt|;
specifier|final
name|DefaultCamelContext
name|candidateTwoContext
init|=
name|createNewContext
argument_list|()
decl_stmt|;
name|ZooKeeperElection
name|electionCandidate1
init|=
name|createElectionCandidate
argument_list|(
name|candidateOneContext
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The first candidate was not elected."
argument_list|,
name|electionCandidate1
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
name|ZooKeeperElection
name|electionCandidate2
init|=
name|createElectionCandidate
argument_list|(
name|candidateTwoContext
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"The second candidate should not have been elected."
argument_list|,
name|electionCandidate2
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"About to shutdown the first candidate."
argument_list|)
expr_stmt|;
name|candidateOneContext
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// the first candidate was killed.
name|delay
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// more than the timeout on the zeekeeper server.
name|assertTrue
argument_list|(
literal|"The second candidate should have been elected."
argument_list|,
name|electionCandidate2
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDualMaster ()
specifier|public
name|void
name|testDualMaster
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DefaultCamelContext
name|candidateOneContext
init|=
name|createNewContext
argument_list|()
decl_stmt|;
specifier|final
name|DefaultCamelContext
name|candidateTwoContext
init|=
name|createNewContext
argument_list|()
decl_stmt|;
name|ZooKeeperElection
name|electionCandidate1
init|=
name|createElectionCandidate
argument_list|(
name|candidateOneContext
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The first candidate was not elected."
argument_list|,
name|electionCandidate1
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
name|ZooKeeperElection
name|electionCandidate2
init|=
name|createElectionCandidate
argument_list|(
name|candidateTwoContext
argument_list|,
literal|2
argument_list|)
decl_stmt|;
comment|// Need to wait for a while to Candidate2 to be elected.
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The second candidate should also be a master."
argument_list|,
name|electionCandidate2
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWatchersAreNotified ()
specifier|public
name|void
name|testWatchersAreNotified
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DefaultCamelContext
name|candidateOneContext
init|=
name|createNewContext
argument_list|()
decl_stmt|;
specifier|final
name|DefaultCamelContext
name|candidateTwoContext
init|=
name|createNewContext
argument_list|()
decl_stmt|;
specifier|final
name|AtomicBoolean
name|notified
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|ElectionWatcher
name|watcher
init|=
operator|new
name|ElectionWatcher
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|electionResultChanged
parameter_list|()
block|{
name|notified
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|ZooKeeperElection
name|electionCandidate1
init|=
name|createElectionCandidate
argument_list|(
name|candidateOneContext
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The first candidate was not elected."
argument_list|,
name|electionCandidate1
operator|.
name|isMaster
argument_list|()
argument_list|)
expr_stmt|;
name|electionCandidate1
operator|.
name|addElectionWatcher
argument_list|(
name|watcher
argument_list|)
expr_stmt|;
name|ZooKeeperElection
name|electionCandidate2
init|=
name|createElectionCandidate
argument_list|(
name|candidateTwoContext
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|electionCandidate2
operator|.
name|isMaster
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The first candidate should have had it's watcher notified"
argument_list|,
name|notified
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createNewContext ()
specifier|private
name|DefaultCamelContext
name|createNewContext
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|controlledContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|controlledContext
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|controlledContext
return|;
block|}
DECL|method|createElectionCandidate (final DefaultCamelContext context, int masterCount)
specifier|private
name|ZooKeeperElection
name|createElectionCandidate
parameter_list|(
specifier|final
name|DefaultCamelContext
name|context
parameter_list|,
name|int
name|masterCount
parameter_list|)
block|{
return|return
operator|new
name|ZooKeeperElection
argument_list|(
name|context
operator|.
name|createProducerTemplate
argument_list|()
argument_list|,
name|context
argument_list|,
name|ELECTION_URI
argument_list|,
name|masterCount
argument_list|)
return|;
block|}
block|}
end_class

end_unit

