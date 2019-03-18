begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.map
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
name|map
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
name|collections
operator|.
name|DistributedMap
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
name|EndpointInject
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
name|FluentProducerTemplate
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
name|Message
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
DECL|class|AtomixMapNodesProducerTest
specifier|public
class|class
name|AtomixMapNodesProducerTest
extends|extends
name|AtomixClientTestSupport
block|{
DECL|field|MAP_NAME
specifier|private
specifier|static
specifier|final
name|String
name|MAP_NAME
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
DECL|field|map
specifier|private
name|DistributedMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|fluent
specifier|private
name|FluentProducerTemplate
name|fluent
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
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
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
name|map
operator|=
name|getClient
argument_list|()
operator|.
name|getMap
argument_list|(
name|MAP_NAME
argument_list|)
operator|.
name|join
argument_list|()
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
name|map
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
DECL|method|testPut ()
specifier|public
name|void
name|testPut
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
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
specifier|final
name|String
name|val
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
name|Message
name|result
decl_stmt|;
name|result
operator|=
name|fluent
operator|.
name|clearAll
argument_list|()
operator|.
name|withHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|RESOURCE_ACTION
argument_list|,
name|AtomixMap
operator|.
name|Action
operator|.
name|PUT
argument_list|)
operator|.
name|withHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|RESOURCE_KEY
argument_list|,
name|key
argument_list|)
operator|.
name|withBody
argument_list|(
name|val
argument_list|)
operator|.
name|request
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|getHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|RESOURCE_ACTION_HAS_RESULT
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|val
argument_list|,
name|result
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|val
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|join
argument_list|()
argument_list|)
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"atomix-map:%s?nodes=%s:%d"
argument_list|,
name|MAP_NAME
argument_list|,
name|replicaAddress
operator|.
name|host
argument_list|()
argument_list|,
name|replicaAddress
operator|.
name|port
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

