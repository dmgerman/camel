begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
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
name|jgroups
operator|.
name|JChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|ReceiverAdapter
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
name|Test
import|;
end_import

begin_class
DECL|class|JGroupsProducerTest
specifier|public
class|class
name|JGroupsProducerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CLUSTER_NAME
specifier|static
specifier|final
name|String
name|CLUSTER_NAME
init|=
literal|"CLUSTER_NAME"
decl_stmt|;
DECL|field|MESSAGE
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"MESSAGE"
decl_stmt|;
comment|// Fixtures
DECL|field|channel
name|JChannel
name|channel
decl_stmt|;
DECL|field|messageReceived
name|Object
name|messageReceived
decl_stmt|;
comment|// Routes fixture
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
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
literal|"jgroups:"
operator|+
name|CLUSTER_NAME
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// Fixture setup
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
name|channel
operator|=
operator|new
name|JChannel
argument_list|()
expr_stmt|;
name|channel
operator|.
name|setReceiver
argument_list|(
operator|new
name|ReceiverAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|receive
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|messageReceived
operator|=
name|msg
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|channel
operator|.
name|connect
argument_list|(
name|CLUSTER_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|channel
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldReceiveMulticastedBody ()
specifier|public
name|void
name|shouldReceiveMulticastedBody
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|MESSAGE
argument_list|)
expr_stmt|;
comment|// Then
name|waitForMulticastChannel
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MESSAGE
argument_list|,
name|messageReceived
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotSendNullMessage ()
specifier|public
name|void
name|shouldNotSendNullMessage
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Then
name|waitForMulticastChannel
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|messageReceived
argument_list|)
expr_stmt|;
block|}
comment|// Helpers
DECL|method|waitForMulticastChannel (int attempts)
specifier|private
name|void
name|waitForMulticastChannel
parameter_list|(
name|int
name|attempts
parameter_list|)
throws|throws
name|InterruptedException
block|{
while|while
condition|(
name|messageReceived
operator|==
literal|null
operator|&&
name|attempts
operator|>
literal|0
condition|)
block|{
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|sleep
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|attempts
operator|--
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

