begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.value
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|value
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
name|Map
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
name|io
operator|.
name|atomix
operator|.
name|variables
operator|.
name|DistributedValue
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
name|Component
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
name|RoutesBuilder
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
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
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
name|atomix
operator|.
name|client
operator|.
name|AtomixClientTestSupport
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|AtomixValueConsumerTest
specifier|public
class|class
name|AtomixValueConsumerTest
extends|extends
name|AtomixClientTestSupport
block|{
DECL|field|VALUE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|VALUE_NAME
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
DECL|field|value
specifier|private
name|DistributedValue
argument_list|<
name|Object
argument_list|>
name|value
decl_stmt|;
comment|// ************************************
comment|// Setup
comment|// ************************************
annotation|@
name|Override
DECL|method|createComponents ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Component
argument_list|>
name|createComponents
parameter_list|()
block|{
name|AtomixValueComponent
name|component
init|=
operator|new
name|AtomixValueComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setNodes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|getReplicaAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"atomix-value"
argument_list|,
name|component
argument_list|)
return|;
block|}
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
name|value
operator|=
name|getClient
argument_list|()
operator|.
name|getValue
argument_list|(
name|VALUE_NAME
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|value
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
comment|// ************************************
comment|// Test
comment|// ************************************
annotation|@
name|Test
DECL|method|testEvents ()
specifier|public
name|void
name|testEvents
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|val1
init|=
name|context
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
name|String
name|val2
init|=
name|context
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|val1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|AtomixClientConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|DistributedValue
operator|.
name|Events
operator|.
name|CHANGE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|val2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|header
argument_list|(
name|AtomixClientConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|DistributedValue
operator|.
name|Events
operator|.
name|CHANGE
argument_list|)
expr_stmt|;
name|value
operator|.
name|set
argument_list|(
name|val1
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
name|value
operator|.
name|compareAndSet
argument_list|(
name|val1
argument_list|,
name|val2
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|// ************************************
comment|// Routes
comment|// ************************************
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"atomix-value:%s"
argument_list|,
name|VALUE_NAME
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

