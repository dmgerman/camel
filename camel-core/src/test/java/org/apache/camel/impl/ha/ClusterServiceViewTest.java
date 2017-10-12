begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ha
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|UUID
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
name|CountDownLatch
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
name|IntStream
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
name|ServiceStatus
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
name|ha
operator|.
name|CamelClusterEventListener
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
name|ha
operator|.
name|CamelClusterMember
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
name|ha
operator|.
name|CamelClusterService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
DECL|class|ClusterServiceViewTest
specifier|public
class|class
name|ClusterServiceViewTest
block|{
annotation|@
name|Test
DECL|method|testViewEquality ()
specifier|public
name|void
name|testViewEquality
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClusterService
name|service
init|=
operator|new
name|TestClusterService
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|TestClusterView
name|view1
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestClusterView
name|view2
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestClusterView
name|view3
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns2"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|view1
argument_list|,
name|view2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotEquals
argument_list|(
name|view1
argument_list|,
name|view3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testViewReferences ()
specifier|public
name|void
name|testViewReferences
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClusterService
name|service
init|=
operator|new
name|TestClusterService
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
name|TestClusterView
name|view1
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestClusterView
name|view2
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestClusterView
name|view3
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns2"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view1
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view3
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|releaseView
argument_list|(
name|view1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view1
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view3
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|releaseView
argument_list|(
name|view2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view1
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view3
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|releaseView
argument_list|(
name|view3
argument_list|)
expr_stmt|;
name|TestClusterView
name|newView1
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestClusterView
name|newView2
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|newView1
argument_list|,
name|newView2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|view1
argument_list|,
name|newView1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|view1
argument_list|,
name|newView2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|newView1
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|newView2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view3
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view1
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view3
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|newView1
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|newView2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testViewForceOperations ()
specifier|public
name|void
name|testViewForceOperations
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClusterService
name|service
init|=
operator|new
name|TestClusterService
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|TestClusterView
name|view
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// This should not start the view as the service has not yet started.
name|service
operator|.
name|startView
argument_list|(
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// This should start the view.
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|stopView
argument_list|(
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|startView
argument_list|(
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|view
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|releaseView
argument_list|(
name|view
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|view
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipleViewListeners ()
specifier|public
name|void
name|testMultipleViewListeners
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|TestClusterService
name|service
init|=
operator|new
name|TestClusterService
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|TestClusterView
name|view
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|int
name|events
init|=
literal|1
operator|+
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|(
literal|10
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|Integer
argument_list|>
name|results
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|events
argument_list|)
decl_stmt|;
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|events
argument_list|)
operator|.
name|forEach
argument_list|(
name|i
lambda|->
name|view
operator|.
name|addEventListener
argument_list|(
call|(
name|CamelClusterEventListener
operator|.
name|Leadership
call|)
argument_list|(
name|v
argument_list|,
name|l
argument_list|)
operator|->
block|{
name|results
operator|.
name|add
argument_list|(
name|i
argument_list|)
argument_list|;
name|latch
operator|.
name|countDown
argument_list|()
argument_list|;
block|}
block|)
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_expr_stmt
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|view
operator|.
name|setLeader
argument_list|(
literal|true
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|latch
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|events
argument_list|)
operator|.
name|forEach
argument_list|(
name|i
lambda|->
name|Assert
operator|.
name|assertTrue
argument_list|(
name|results
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_function
unit|}      @
name|Test
DECL|method|testLateViewListeners ()
specifier|public
name|void
name|testLateViewListeners
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|TestClusterService
name|service
init|=
operator|new
name|TestClusterService
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|TestClusterView
name|view
init|=
name|service
operator|.
name|getView
argument_list|(
literal|"ns1"
argument_list|)
operator|.
name|unwrap
argument_list|(
name|TestClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|int
name|events
init|=
literal|1
operator|+
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|(
literal|10
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|Integer
argument_list|>
name|results
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|events
operator|*
literal|2
argument_list|)
decl_stmt|;
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|events
argument_list|)
operator|.
name|forEach
argument_list|(
name|i
lambda|->
name|view
operator|.
name|addEventListener
argument_list|(
call|(
name|CamelClusterEventListener
operator|.
name|Leadership
call|)
argument_list|(
name|v
argument_list|,
name|l
argument_list|)
operator|->
block|{
name|results
operator|.
name|add
argument_list|(
name|i
argument_list|)
argument_list|;
name|latch
operator|.
name|countDown
argument_list|()
argument_list|;
block|}
end_function

begin_empty_stmt
unit|)         )
empty_stmt|;
end_empty_stmt

begin_expr_stmt
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|view
operator|.
name|setLeader
argument_list|(
literal|true
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|IntStream
operator|.
name|range
argument_list|(
name|events
argument_list|,
name|events
operator|*
literal|2
argument_list|)
operator|.
name|forEach
argument_list|(
name|i
lambda|->
name|view
operator|.
name|addEventListener
argument_list|(
call|(
name|CamelClusterEventListener
operator|.
name|Leadership
call|)
argument_list|(
name|v
argument_list|,
name|l
argument_list|)
operator|->
block|{
name|results
operator|.
name|add
argument_list|(
name|i
argument_list|)
argument_list|;
name|latch
operator|.
name|countDown
argument_list|()
argument_list|;
end_expr_stmt

begin_empty_stmt
unit|})         )
empty_stmt|;
end_empty_stmt

begin_expr_stmt
name|latch
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|events
operator|*
literal|2
argument_list|)
operator|.
name|forEach
argument_list|(
name|i
lambda|->
name|Assert
operator|.
name|assertTrue
argument_list|(
name|results
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_comment
unit|}
comment|// *********************************
end_comment

begin_comment
comment|// Helpers
end_comment

begin_comment
comment|// *********************************
end_comment

begin_class
DECL|class|TestClusterView
unit|private
specifier|static
class|class
name|TestClusterView
extends|extends
name|AbstractCamelClusterView
block|{
DECL|field|leader
specifier|private
name|boolean
name|leader
decl_stmt|;
DECL|method|TestClusterView (CamelClusterService cluster, String namespace)
specifier|public
name|TestClusterView
parameter_list|(
name|CamelClusterService
name|cluster
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|super
argument_list|(
name|cluster
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLeader ()
specifier|public
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|getLeader
parameter_list|()
block|{
return|return
name|leader
condition|?
name|Optional
operator|.
name|of
argument_list|(
name|getLocalMember
argument_list|()
argument_list|)
else|:
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getLocalMember ()
specifier|public
name|CamelClusterMember
name|getLocalMember
parameter_list|()
block|{
return|return
operator|new
name|CamelClusterMember
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
return|return
name|leader
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|getClusterService
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|getMembers ()
specifier|public
name|List
argument_list|<
name|CamelClusterMember
argument_list|>
name|getMembers
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{         }
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|isLeader ()
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
return|return
name|leader
return|;
block|}
DECL|method|setLeader (boolean leader)
specifier|public
name|void
name|setLeader
parameter_list|(
name|boolean
name|leader
parameter_list|)
block|{
name|this
operator|.
name|leader
operator|=
name|leader
expr_stmt|;
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|fireLeadershipChangedEvent
argument_list|(
name|getLeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_class
DECL|class|TestClusterService
specifier|private
specifier|static
class|class
name|TestClusterService
extends|extends
name|AbstractCamelClusterService
argument_list|<
name|TestClusterView
argument_list|>
block|{
DECL|method|TestClusterService (String id)
specifier|public
name|TestClusterService
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createView (String namespace)
specifier|protected
name|TestClusterView
name|createView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|TestClusterView
argument_list|(
name|this
argument_list|,
name|namespace
argument_list|)
return|;
block|}
block|}
end_class

unit|}
end_unit

