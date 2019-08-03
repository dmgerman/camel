begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.loadbalancer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|loadbalancer
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|AsyncProcessor
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
name|Expression
import|;
end_import

begin_comment
comment|/**  * Implements a sticky load balancer using an {@link Expression} to calculate  * a correlation key to perform the sticky load balancing; rather like jsessionid in the web  * or JMSXGroupID in JMS.  */
end_comment

begin_class
DECL|class|StickyLoadBalancer
specifier|public
class|class
name|StickyLoadBalancer
extends|extends
name|QueueLoadBalancer
block|{
DECL|field|correlationExpression
specifier|private
name|Expression
name|correlationExpression
decl_stmt|;
DECL|field|loadBalancer
specifier|private
name|RoundRobinLoadBalancer
name|loadBalancer
decl_stmt|;
DECL|field|numberOfHashGroups
specifier|private
name|int
name|numberOfHashGroups
init|=
literal|64
operator|*
literal|1024
decl_stmt|;
DECL|field|stickyMap
specifier|private
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|AsyncProcessor
argument_list|>
name|stickyMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|StickyLoadBalancer (Expression correlationExpression)
specifier|public
name|StickyLoadBalancer
parameter_list|(
name|Expression
name|correlationExpression
parameter_list|)
block|{
name|this
operator|.
name|correlationExpression
operator|=
name|correlationExpression
expr_stmt|;
name|this
operator|.
name|loadBalancer
operator|=
operator|new
name|RoundRobinLoadBalancer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|chooseProcessor (AsyncProcessor[] processors, Exchange exchange)
specifier|protected
name|AsyncProcessor
name|chooseProcessor
parameter_list|(
name|AsyncProcessor
index|[]
name|processors
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|value
init|=
name|correlationExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|key
init|=
name|getStickyKey
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|AsyncProcessor
name|processor
decl_stmt|;
name|processor
operator|=
name|stickyMap
operator|.
name|computeIfAbsent
argument_list|(
name|key
argument_list|,
name|k
lambda|->
name|loadBalancer
operator|.
name|chooseProcessor
argument_list|(
name|processors
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|processor
return|;
block|}
annotation|@
name|Override
DECL|method|removeProcessor (AsyncProcessor processor)
specifier|public
name|void
name|removeProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
block|{
name|stickyMap
operator|.
name|values
argument_list|()
operator|.
name|remove
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|super
operator|.
name|removeProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getLastChosenProcessorIndex ()
specifier|public
name|int
name|getLastChosenProcessorIndex
parameter_list|()
block|{
return|return
name|loadBalancer
operator|.
name|getLastChosenProcessorIndex
argument_list|()
return|;
block|}
DECL|method|getCorrelationExpression ()
specifier|public
name|Expression
name|getCorrelationExpression
parameter_list|()
block|{
return|return
name|correlationExpression
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getNumberOfHashGroups ()
specifier|public
name|int
name|getNumberOfHashGroups
parameter_list|()
block|{
return|return
name|numberOfHashGroups
return|;
block|}
DECL|method|setNumberOfHashGroups (int numberOfHashGroups)
specifier|public
name|void
name|setNumberOfHashGroups
parameter_list|(
name|int
name|numberOfHashGroups
parameter_list|)
block|{
name|this
operator|.
name|numberOfHashGroups
operator|=
name|numberOfHashGroups
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
comment|/**      * A strategy to create the key for the sticky load balancing map.      * The default implementation uses the hash code of the value      * then modulos by the numberOfHashGroups to avoid the sticky map getting too big      *      * @param value the correlation value      * @return the key to be used in the sticky map      */
DECL|method|getStickyKey (Object value)
specifier|protected
name|Object
name|getStickyKey
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|int
name|hashCode
init|=
literal|37
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|hashCode
operator|=
name|value
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|numberOfHashGroups
operator|>
literal|0
condition|)
block|{
name|hashCode
operator|=
name|hashCode
operator|%
name|numberOfHashGroups
expr_stmt|;
block|}
return|return
name|hashCode
return|;
block|}
block|}
end_class

end_unit

