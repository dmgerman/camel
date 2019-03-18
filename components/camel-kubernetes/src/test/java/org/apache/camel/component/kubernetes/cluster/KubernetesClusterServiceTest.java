begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|cluster
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Objects
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|ConfigMapBuilder
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
name|kubernetes
operator|.
name|KubernetesConfiguration
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
name|kubernetes
operator|.
name|cluster
operator|.
name|utils
operator|.
name|ConfigMapLockSimulator
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
name|kubernetes
operator|.
name|cluster
operator|.
name|utils
operator|.
name|LeaderRecorder
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
name|kubernetes
operator|.
name|cluster
operator|.
name|utils
operator|.
name|LockTestServer
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
name|CamelTestSupport
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

begin_comment
comment|/**  * Test leader election scenarios using a mock server.  */
end_comment

begin_class
DECL|class|KubernetesClusterServiceTest
specifier|public
class|class
name|KubernetesClusterServiceTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LEASE_TIME_MILLIS
specifier|private
specifier|static
specifier|final
name|int
name|LEASE_TIME_MILLIS
init|=
literal|2000
decl_stmt|;
DECL|field|RENEW_DEADLINE_MILLIS
specifier|private
specifier|static
specifier|final
name|int
name|RENEW_DEADLINE_MILLIS
init|=
literal|1000
decl_stmt|;
DECL|field|RETRY_PERIOD_MILLIS
specifier|private
specifier|static
specifier|final
name|int
name|RETRY_PERIOD_MILLIS
init|=
literal|200
decl_stmt|;
DECL|field|JITTER_FACTOR
specifier|private
specifier|static
specifier|final
name|double
name|JITTER_FACTOR
init|=
literal|1.1
decl_stmt|;
DECL|field|lockSimulator
specifier|private
name|ConfigMapLockSimulator
name|lockSimulator
decl_stmt|;
DECL|field|lockServers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|LockTestServer
argument_list|>
name|lockServers
decl_stmt|;
annotation|@
name|Before
DECL|method|prepareLock ()
specifier|public
name|void
name|prepareLock
parameter_list|()
block|{
name|this
operator|.
name|lockSimulator
operator|=
operator|new
name|ConfigMapLockSimulator
argument_list|(
literal|"leaders"
argument_list|)
expr_stmt|;
name|this
operator|.
name|lockServers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|shutdownLock ()
specifier|public
name|void
name|shutdownLock
parameter_list|()
block|{
for|for
control|(
name|LockTestServer
name|server
range|:
name|this
operator|.
name|lockServers
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// can happen in case of delay
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSimpleLeaderElection ()
specifier|public
name|void
name|testSimpleLeaderElection
parameter_list|()
throws|throws
name|Exception
block|{
name|LeaderRecorder
name|mypod1
init|=
name|addMember
argument_list|(
literal|"mypod1"
argument_list|)
decl_stmt|;
name|LeaderRecorder
name|mypod2
init|=
name|addMember
argument_list|(
literal|"mypod2"
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|mypod1
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mypod2
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|String
name|leader
init|=
name|mypod1
operator|.
name|getCurrentLeader
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|leader
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|leader
operator|.
name|startsWith
argument_list|(
literal|"mypod"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Leaders should be equals"
argument_list|,
name|mypod2
operator|.
name|getCurrentLeader
argument_list|()
argument_list|,
name|leader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipleMembersLeaderElection ()
specifier|public
name|void
name|testMultipleMembersLeaderElection
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|number
init|=
literal|5
decl_stmt|;
name|List
argument_list|<
name|LeaderRecorder
argument_list|>
name|members
init|=
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|number
argument_list|)
operator|.
name|mapToObj
argument_list|(
name|i
lambda|->
name|addMember
argument_list|(
literal|"mypod"
operator|+
name|i
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
for|for
control|(
name|LeaderRecorder
name|member
range|:
name|members
control|)
block|{
name|member
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|leaders
init|=
name|members
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|LeaderRecorder
operator|::
name|getCurrentLeader
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|leaders
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|leader
init|=
name|leaders
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|leader
operator|.
name|startsWith
argument_list|(
literal|"mypod"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSimpleLeaderElectionWithExistingConfigMap ()
specifier|public
name|void
name|testSimpleLeaderElectionWithExistingConfigMap
parameter_list|()
throws|throws
name|Exception
block|{
name|lockSimulator
operator|.
name|setConfigMap
argument_list|(
operator|new
name|ConfigMapBuilder
argument_list|()
operator|.
name|withNewMetadata
argument_list|()
operator|.
name|withName
argument_list|(
literal|"leaders"
argument_list|)
operator|.
name|and
argument_list|()
operator|.
name|build
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|LeaderRecorder
name|mypod1
init|=
name|addMember
argument_list|(
literal|"mypod1"
argument_list|)
decl_stmt|;
name|LeaderRecorder
name|mypod2
init|=
name|addMember
argument_list|(
literal|"mypod2"
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|mypod1
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mypod2
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|String
name|leader
init|=
name|mypod1
operator|.
name|getCurrentLeader
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|leader
operator|.
name|startsWith
argument_list|(
literal|"mypod"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Leaders should be equals"
argument_list|,
name|mypod2
operator|.
name|getCurrentLeader
argument_list|()
argument_list|,
name|leader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLeadershipLoss ()
specifier|public
name|void
name|testLeadershipLoss
parameter_list|()
throws|throws
name|Exception
block|{
name|LeaderRecorder
name|mypod1
init|=
name|addMember
argument_list|(
literal|"mypod1"
argument_list|)
decl_stmt|;
name|LeaderRecorder
name|mypod2
init|=
name|addMember
argument_list|(
literal|"mypod2"
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|mypod1
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mypod2
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|String
name|firstLeader
init|=
name|mypod1
operator|.
name|getCurrentLeader
argument_list|()
decl_stmt|;
name|LeaderRecorder
name|formerLeaderRecorder
init|=
name|firstLeader
operator|.
name|equals
argument_list|(
literal|"mypod1"
argument_list|)
condition|?
name|mypod1
else|:
name|mypod2
decl_stmt|;
name|LeaderRecorder
name|formerLoserRecorder
init|=
name|firstLeader
operator|.
name|equals
argument_list|(
literal|"mypod1"
argument_list|)
condition|?
name|mypod2
else|:
name|mypod1
decl_stmt|;
name|refuseRequestsFromPod
argument_list|(
name|firstLeader
argument_list|)
expr_stmt|;
name|disconnectPod
argument_list|(
name|firstLeader
argument_list|)
expr_stmt|;
name|formerLeaderRecorder
operator|.
name|waitForALeaderChange
argument_list|(
literal|7
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|formerLoserRecorder
operator|.
name|waitForANewLeader
argument_list|(
name|firstLeader
argument_list|,
literal|7
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|String
name|secondLeader
init|=
name|formerLoserRecorder
operator|.
name|getCurrentLeader
argument_list|()
decl_stmt|;
name|assertNotEquals
argument_list|(
literal|"The firstLeader should be different from the new one"
argument_list|,
name|firstLeader
argument_list|,
name|secondLeader
argument_list|)
expr_stmt|;
name|Long
name|lossTimestamp
init|=
name|formerLeaderRecorder
operator|.
name|getLastTimeOf
argument_list|(
name|l
lambda|->
name|l
operator|==
literal|null
argument_list|)
decl_stmt|;
name|Long
name|gainTimestamp
init|=
name|formerLoserRecorder
operator|.
name|getLastTimeOf
argument_list|(
name|secondLeader
operator|::
name|equals
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"At least half distance must elapse from leadership loss and regain (see renewDeadlineSeconds)"
argument_list|,
name|gainTimestamp
operator|>=
name|lossTimestamp
operator|+
operator|(
name|LEASE_TIME_MILLIS
operator|-
name|RENEW_DEADLINE_MILLIS
operator|)
operator|/
literal|2
argument_list|)
expr_stmt|;
name|checkLeadershipChangeDistance
argument_list|(
operator|(
name|LEASE_TIME_MILLIS
operator|-
name|RENEW_DEADLINE_MILLIS
operator|)
operator|/
literal|2
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|,
name|mypod1
argument_list|,
name|mypod2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSlowLeaderLosingLeadershipOnlyInternally ()
specifier|public
name|void
name|testSlowLeaderLosingLeadershipOnlyInternally
parameter_list|()
throws|throws
name|Exception
block|{
name|LeaderRecorder
name|mypod1
init|=
name|addMember
argument_list|(
literal|"mypod1"
argument_list|)
decl_stmt|;
name|LeaderRecorder
name|mypod2
init|=
name|addMember
argument_list|(
literal|"mypod2"
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|mypod1
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mypod2
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|String
name|firstLeader
init|=
name|mypod1
operator|.
name|getCurrentLeader
argument_list|()
decl_stmt|;
name|LeaderRecorder
name|formerLeaderRecorder
init|=
name|firstLeader
operator|.
name|equals
argument_list|(
literal|"mypod1"
argument_list|)
condition|?
name|mypod1
else|:
name|mypod2
decl_stmt|;
name|LeaderRecorder
name|formerLoserRecorder
init|=
name|firstLeader
operator|.
name|equals
argument_list|(
literal|"mypod1"
argument_list|)
condition|?
name|mypod2
else|:
name|mypod1
decl_stmt|;
name|delayRequestsFromPod
argument_list|(
name|firstLeader
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|LEASE_TIME_MILLIS
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|formerLeaderRecorder
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|firstLeader
argument_list|,
name|formerLoserRecorder
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRecoveryAfterFailure ()
specifier|public
name|void
name|testRecoveryAfterFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|LeaderRecorder
name|mypod1
init|=
name|addMember
argument_list|(
literal|"mypod1"
argument_list|)
decl_stmt|;
name|LeaderRecorder
name|mypod2
init|=
name|addMember
argument_list|(
literal|"mypod2"
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|mypod1
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mypod2
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|String
name|firstLeader
init|=
name|mypod1
operator|.
name|getCurrentLeader
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|refuseRequestsFromPod
argument_list|(
name|firstLeader
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|RENEW_DEADLINE_MILLIS
argument_list|)
expr_stmt|;
name|allowRequestsFromPod
argument_list|(
name|firstLeader
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|LEASE_TIME_MILLIS
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|firstLeader
argument_list|,
name|mypod1
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|firstLeader
argument_list|,
name|mypod2
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSharedConfigMap ()
specifier|public
name|void
name|testSharedConfigMap
parameter_list|()
throws|throws
name|Exception
block|{
name|LeaderRecorder
name|a1
init|=
name|addMember
argument_list|(
literal|"a1"
argument_list|)
decl_stmt|;
name|LeaderRecorder
name|a2
init|=
name|addMember
argument_list|(
literal|"a2"
argument_list|)
decl_stmt|;
name|LeaderRecorder
name|b1
init|=
name|addMember
argument_list|(
literal|"b1"
argument_list|,
literal|"app2"
argument_list|)
decl_stmt|;
name|LeaderRecorder
name|b2
init|=
name|addMember
argument_list|(
literal|"b2"
argument_list|,
literal|"app2"
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|a1
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|a2
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|b1
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|b2
operator|.
name|waitForAnyLeader
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|a1
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a1
operator|.
name|getCurrentLeader
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a1
operator|.
name|getCurrentLeader
argument_list|()
argument_list|,
name|a2
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|b1
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|b1
operator|.
name|getCurrentLeader
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b1
operator|.
name|getCurrentLeader
argument_list|()
argument_list|,
name|b2
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|a1
operator|.
name|getCurrentLeader
argument_list|()
argument_list|,
name|b2
operator|.
name|getCurrentLeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|delayRequestsFromPod (String pod, long delay, TimeUnit unit)
specifier|private
name|void
name|delayRequestsFromPod
parameter_list|(
name|String
name|pod
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|this
operator|.
name|lockServers
operator|.
name|get
argument_list|(
name|pod
argument_list|)
operator|.
name|setDelayRequests
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|delay
argument_list|,
name|unit
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|refuseRequestsFromPod (String pod)
specifier|private
name|void
name|refuseRequestsFromPod
parameter_list|(
name|String
name|pod
parameter_list|)
block|{
name|this
operator|.
name|lockServers
operator|.
name|get
argument_list|(
name|pod
argument_list|)
operator|.
name|setRefuseRequests
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|allowRequestsFromPod (String pod)
specifier|private
name|void
name|allowRequestsFromPod
parameter_list|(
name|String
name|pod
parameter_list|)
block|{
name|this
operator|.
name|lockServers
operator|.
name|get
argument_list|(
name|pod
argument_list|)
operator|.
name|setRefuseRequests
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnectPod (String pod)
specifier|private
name|void
name|disconnectPod
parameter_list|(
name|String
name|pod
parameter_list|)
block|{
for|for
control|(
name|LockTestServer
name|server
range|:
name|this
operator|.
name|lockServers
operator|.
name|values
argument_list|()
control|)
block|{
name|server
operator|.
name|removePod
argument_list|(
name|pod
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|connectPod (String pod)
specifier|private
name|void
name|connectPod
parameter_list|(
name|String
name|pod
parameter_list|)
block|{
for|for
control|(
name|LockTestServer
name|server
range|:
name|this
operator|.
name|lockServers
operator|.
name|values
argument_list|()
control|)
block|{
name|server
operator|.
name|addPod
argument_list|(
name|pod
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkLeadershipChangeDistance (long minimum, TimeUnit unit, LeaderRecorder... recorders)
specifier|private
name|void
name|checkLeadershipChangeDistance
parameter_list|(
name|long
name|minimum
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|LeaderRecorder
modifier|...
name|recorders
parameter_list|)
block|{
name|List
argument_list|<
name|LeaderRecorder
operator|.
name|LeadershipInfo
argument_list|>
name|infos
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|recorders
argument_list|)
operator|.
name|flatMap
argument_list|(
name|lr
lambda|->
name|lr
operator|.
name|getLeadershipInfo
argument_list|()
operator|.
name|stream
argument_list|()
argument_list|)
operator|.
name|sorted
argument_list|(
parameter_list|(
name|li1
parameter_list|,
name|li2
parameter_list|)
lambda|->
name|Long
operator|.
name|compare
argument_list|(
name|li1
operator|.
name|getChangeTimestamp
argument_list|()
argument_list|,
name|li2
operator|.
name|getChangeTimestamp
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|LeaderRecorder
operator|.
name|LeadershipInfo
name|currentLeaderLastSeen
init|=
literal|null
decl_stmt|;
for|for
control|(
name|LeaderRecorder
operator|.
name|LeadershipInfo
name|info
range|:
name|infos
control|)
block|{
if|if
condition|(
name|currentLeaderLastSeen
operator|==
literal|null
operator|||
name|currentLeaderLastSeen
operator|.
name|getLeader
argument_list|()
operator|==
literal|null
condition|)
block|{
name|currentLeaderLastSeen
operator|=
name|info
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|info
operator|.
name|getLeader
argument_list|()
argument_list|,
name|currentLeaderLastSeen
operator|.
name|getLeader
argument_list|()
argument_list|)
condition|)
block|{
name|currentLeaderLastSeen
operator|=
name|info
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|info
operator|.
name|getLeader
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|info
operator|.
name|getLeader
argument_list|()
operator|.
name|equals
argument_list|(
name|currentLeaderLastSeen
operator|.
name|getLeader
argument_list|()
argument_list|)
condition|)
block|{
comment|// switch
name|long
name|delay
init|=
name|info
operator|.
name|getChangeTimestamp
argument_list|()
operator|-
name|currentLeaderLastSeen
operator|.
name|getChangeTimestamp
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Lease time not elapsed between switch, minimum="
operator|+
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|minimum
argument_list|,
name|unit
argument_list|)
operator|+
literal|", found="
operator|+
name|delay
argument_list|,
name|delay
operator|>=
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|minimum
argument_list|,
name|unit
argument_list|)
argument_list|)
expr_stmt|;
name|currentLeaderLastSeen
operator|=
name|info
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|addMember (String name)
specifier|private
name|LeaderRecorder
name|addMember
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|addMember
argument_list|(
name|name
argument_list|,
literal|"app"
argument_list|)
return|;
block|}
DECL|method|addMember (String name, String namespace)
specifier|private
name|LeaderRecorder
name|addMember
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|assertNull
argument_list|(
name|this
operator|.
name|lockServers
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|LockTestServer
name|lockServer
init|=
operator|new
name|LockTestServer
argument_list|(
name|lockSimulator
argument_list|)
decl_stmt|;
name|this
operator|.
name|lockServers
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|lockServer
argument_list|)
expr_stmt|;
name|KubernetesConfiguration
name|configuration
init|=
operator|new
name|KubernetesConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setKubernetesClient
argument_list|(
name|lockServer
operator|.
name|createClient
argument_list|()
argument_list|)
expr_stmt|;
name|KubernetesClusterService
name|member
init|=
operator|new
name|KubernetesClusterService
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|member
operator|.
name|setKubernetesNamespace
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|member
operator|.
name|setPodName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|member
operator|.
name|setLeaseDurationMillis
argument_list|(
name|LEASE_TIME_MILLIS
argument_list|)
expr_stmt|;
name|member
operator|.
name|setRenewDeadlineMillis
argument_list|(
name|RENEW_DEADLINE_MILLIS
argument_list|)
expr_stmt|;
name|member
operator|.
name|setRetryPeriodMillis
argument_list|(
name|RETRY_PERIOD_MILLIS
argument_list|)
expr_stmt|;
name|member
operator|.
name|setJitterFactor
argument_list|(
name|JITTER_FACTOR
argument_list|)
expr_stmt|;
name|LeaderRecorder
name|recorder
init|=
operator|new
name|LeaderRecorder
argument_list|()
decl_stmt|;
try|try
block|{
name|context
argument_list|()
operator|.
name|addService
argument_list|(
name|member
argument_list|)
expr_stmt|;
name|member
operator|.
name|getView
argument_list|(
name|namespace
argument_list|)
operator|.
name|addEventListener
argument_list|(
name|recorder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
for|for
control|(
name|String
name|pod
range|:
name|this
operator|.
name|lockServers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|connectPod
argument_list|(
name|pod
argument_list|)
expr_stmt|;
block|}
return|return
name|recorder
return|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

