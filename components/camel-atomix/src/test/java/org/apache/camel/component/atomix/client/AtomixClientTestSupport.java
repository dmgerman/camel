begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client
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
package|;
end_package

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
name|io
operator|.
name|atomix
operator|.
name|AtomixClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|AtomixReplica
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
name|spi
operator|.
name|Registry
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
name|support
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|AtomixClientTestSupport
specifier|public
specifier|abstract
class|class
name|AtomixClientTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|replicaAddress
specifier|protected
name|Address
name|replicaAddress
decl_stmt|;
DECL|field|replica
specifier|protected
name|AtomixReplica
name|replica
decl_stmt|;
DECL|field|client
specifier|protected
name|AtomixClient
name|client
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelRegistry ()
specifier|protected
name|Registry
name|createCamelRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|createComponents
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|forEach
argument_list|(
name|e
lambda|->
name|registry
operator|.
name|bind
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|registry
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
name|replicaAddress
operator|=
name|AtomixFactory
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
expr_stmt|;
name|replica
operator|=
name|AtomixFactory
operator|.
name|replica
argument_list|(
name|replicaAddress
argument_list|)
expr_stmt|;
name|client
operator|=
name|AtomixFactory
operator|.
name|client
argument_list|(
name|replicaAddress
argument_list|)
expr_stmt|;
name|super
operator|.
name|doPreSetup
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
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
operator|.
name|join
argument_list|()
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|replica
operator|!=
literal|null
condition|)
block|{
name|replica
operator|.
name|shutdown
argument_list|()
operator|.
name|join
argument_list|()
expr_stmt|;
name|replica
operator|.
name|leave
argument_list|()
operator|.
name|join
argument_list|()
expr_stmt|;
name|replica
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|createComponents ()
specifier|protected
specifier|abstract
name|Map
argument_list|<
name|String
argument_list|,
name|Component
argument_list|>
name|createComponents
parameter_list|()
function_decl|;
comment|// *************************************
comment|// properties
comment|// *************************************
DECL|method|getReplicaAddress ()
specifier|protected
name|Address
name|getReplicaAddress
parameter_list|()
block|{
return|return
name|replicaAddress
return|;
block|}
DECL|method|getReplica ()
specifier|protected
name|AtomixReplica
name|getReplica
parameter_list|()
block|{
return|return
name|replica
return|;
block|}
DECL|method|getClient ()
specifier|protected
name|AtomixClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

