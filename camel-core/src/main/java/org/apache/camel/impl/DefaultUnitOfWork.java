begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|List
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
name|Service
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
name|model
operator|.
name|ProcessorDefinition
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
name|Synchronization
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
name|TraceableUnitOfWork
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
name|util
operator|.
name|UuidGenerator
import|;
end_import

begin_comment
comment|/**  * The default implementation of {@link org.apache.camel.spi.UnitOfWork}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultUnitOfWork
specifier|public
class|class
name|DefaultUnitOfWork
implements|implements
name|TraceableUnitOfWork
implements|,
name|Service
block|{
DECL|field|DEFAULT_ID_GENERATOR
specifier|private
specifier|static
specifier|final
name|UuidGenerator
name|DEFAULT_ID_GENERATOR
init|=
operator|new
name|UuidGenerator
argument_list|()
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|synchronizations
specifier|private
name|List
argument_list|<
name|Synchronization
argument_list|>
name|synchronizations
decl_stmt|;
DECL|field|routeList
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|routeList
decl_stmt|;
DECL|field|originalInBody
specifier|private
name|Object
name|originalInBody
decl_stmt|;
DECL|method|DefaultUnitOfWork (Exchange exchange)
specifier|public
name|DefaultUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|originalInBody
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|id
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// need to clean up when we are stopping to not leak memory
if|if
condition|(
name|synchronizations
operator|!=
literal|null
condition|)
block|{
name|synchronizations
operator|.
name|clear
argument_list|()
expr_stmt|;
name|synchronizations
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|routeList
operator|!=
literal|null
condition|)
block|{
name|routeList
operator|.
name|clear
argument_list|()
expr_stmt|;
name|routeList
operator|=
literal|null
expr_stmt|;
block|}
name|originalInBody
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|addSynchronization (Synchronization synchronization)
specifier|public
specifier|synchronized
name|void
name|addSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|==
literal|null
condition|)
block|{
name|synchronizations
operator|=
operator|new
name|ArrayList
argument_list|<
name|Synchronization
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|synchronizations
operator|.
name|add
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
DECL|method|removeSynchronization (Synchronization synchronization)
specifier|public
specifier|synchronized
name|void
name|removeSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|!=
literal|null
condition|)
block|{
name|synchronizations
operator|.
name|remove
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handoverSynchronization (Exchange target)
specifier|public
name|void
name|handoverSynchronization
parameter_list|(
name|Exchange
name|target
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|==
literal|null
operator|||
name|synchronizations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Synchronization
name|synchronization
range|:
name|synchronizations
control|)
block|{
name|target
operator|.
name|addOnCompletion
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
comment|// clear this list as its handed over to the other exchange
name|this
operator|.
name|synchronizations
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|done (Exchange exchange)
specifier|public
name|void
name|done
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|!=
literal|null
operator|&&
operator|!
name|synchronizations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
for|for
control|(
name|Synchronization
name|synchronization
range|:
name|synchronizations
control|)
block|{
if|if
condition|(
name|failed
condition|)
block|{
name|synchronization
operator|.
name|onFailure
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|synchronization
operator|.
name|onComplete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
name|DEFAULT_ID_GENERATOR
operator|.
name|generateId
argument_list|()
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
DECL|method|addInterceptedNode (ProcessorDefinition node)
specifier|public
specifier|synchronized
name|void
name|addInterceptedNode
parameter_list|(
name|ProcessorDefinition
name|node
parameter_list|)
block|{
if|if
condition|(
name|routeList
operator|==
literal|null
condition|)
block|{
name|routeList
operator|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|routeList
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
DECL|method|getLastInterceptedNode ()
specifier|public
specifier|synchronized
name|ProcessorDefinition
name|getLastInterceptedNode
parameter_list|()
block|{
if|if
condition|(
name|routeList
operator|==
literal|null
operator|||
name|routeList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|routeList
operator|.
name|get
argument_list|(
name|routeList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|getInterceptedNodes ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getInterceptedNodes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|routeList
argument_list|)
return|;
block|}
DECL|method|getOriginalInBody ()
specifier|public
name|Object
name|getOriginalInBody
parameter_list|()
block|{
return|return
name|originalInBody
return|;
block|}
block|}
end_class

end_unit

