begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.cluster
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
name|cluster
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
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|Address
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
name|cluster
operator|.
name|CamelClusterService
import|;
end_import

begin_class
DECL|class|AtomixEphemeralClientRoutePolicyFactoryTest
specifier|public
specifier|final
class|class
name|AtomixEphemeralClientRoutePolicyFactoryTest
extends|extends
name|AtomixClientRoutePolicyFactoryTestSupport
block|{
annotation|@
name|Override
DECL|method|createClusterService (String id, Address bootstrapNode)
specifier|protected
name|CamelClusterService
name|createClusterService
parameter_list|(
name|String
name|id
parameter_list|,
name|Address
name|bootstrapNode
parameter_list|)
block|{
name|AtomixClusterClientService
name|service
init|=
operator|new
name|AtomixClusterClientService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setId
argument_list|(
literal|"node-"
operator|+
name|id
argument_list|)
expr_stmt|;
name|service
operator|.
name|setNodes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|bootstrapNode
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|setEphemeral
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
block|}
end_class

end_unit

