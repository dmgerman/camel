begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
name|Map
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
name|spi
operator|.
name|ClaimCheckRepository
import|;
end_import

begin_comment
comment|/**  * The default {@link ClaimCheckRepository} implementation that is an in-memory storage.  */
end_comment

begin_class
DECL|class|DefaultClaimCheckRepository
specifier|public
class|class
name|DefaultClaimCheckRepository
implements|implements
name|ClaimCheckRepository
block|{
DECL|field|map
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Exchange
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|stack
specifier|private
specifier|final
name|Deque
argument_list|<
name|Exchange
argument_list|>
name|stack
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|add (String key, Exchange exchange)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|)
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (String key)
specifier|public
name|Exchange
name|get
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getAndRemove (String key)
specifier|public
name|Exchange
name|getAndRemove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|push (Exchange exchange)
specifier|public
name|void
name|push
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|stack
operator|.
name|push
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|pop ()
specifier|public
name|Exchange
name|pop
parameter_list|()
block|{
if|if
condition|(
operator|!
name|stack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|stack
operator|.
name|pop
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
name|stack
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
comment|// noop
block|}
block|}
end_class

end_unit

