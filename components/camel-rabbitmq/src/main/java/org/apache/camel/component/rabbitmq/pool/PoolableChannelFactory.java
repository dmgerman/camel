begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq.pool
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
operator|.
name|pool
package|;
end_package

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|PoolableObjectFactory
import|;
end_import

begin_comment
comment|/**  * Channel lifecycle manager: create, check and close channel  */
end_comment

begin_class
DECL|class|PoolableChannelFactory
specifier|public
class|class
name|PoolableChannelFactory
implements|implements
name|PoolableObjectFactory
argument_list|<
name|Channel
argument_list|>
block|{
comment|/**      * Parent connection      */
DECL|field|connection
specifier|private
specifier|final
name|Connection
name|connection
decl_stmt|;
DECL|method|PoolableChannelFactory (Connection connection)
specifier|public
name|PoolableChannelFactory
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|makeObject ()
specifier|public
name|Channel
name|makeObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|connection
operator|.
name|createChannel
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|destroyObject (Channel t)
specifier|public
name|void
name|destroyObject
parameter_list|(
name|Channel
name|t
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|t
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//no-op
block|}
block|}
annotation|@
name|Override
DECL|method|validateObject (Channel t)
specifier|public
name|boolean
name|validateObject
parameter_list|(
name|Channel
name|t
parameter_list|)
block|{
return|return
name|t
operator|.
name|isOpen
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|activateObject (Channel t)
specifier|public
name|void
name|activateObject
parameter_list|(
name|Channel
name|t
parameter_list|)
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
DECL|method|passivateObject (Channel t)
specifier|public
name|void
name|passivateObject
parameter_list|(
name|Channel
name|t
parameter_list|)
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

