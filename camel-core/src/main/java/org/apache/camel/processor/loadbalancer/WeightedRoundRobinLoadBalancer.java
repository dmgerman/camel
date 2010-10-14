begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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
name|Processor
import|;
end_import

begin_class
DECL|class|WeightedRoundRobinLoadBalancer
specifier|public
class|class
name|WeightedRoundRobinLoadBalancer
extends|extends
name|WeightedLoadBalancer
block|{
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
DECL|method|WeightedRoundRobinLoadBalancer (ArrayList<Integer> distributionRatios)
specifier|public
name|WeightedRoundRobinLoadBalancer
parameter_list|(
name|ArrayList
argument_list|<
name|Integer
argument_list|>
name|distributionRatios
parameter_list|)
block|{
name|super
argument_list|(
name|distributionRatios
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.processor.loadbalancer.QueueLoadBalancer#chooseProcessor(java.util.List, org.apache.camel.Exchange)      */
annotation|@
name|Override
DECL|method|chooseProcessor (List<Processor> processors, Exchange exchange)
specifier|protected
name|Processor
name|chooseProcessor
parameter_list|(
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|normalizeDistributionListAgainstProcessors
argument_list|(
name|processors
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRuntimeRatiosZeroed
argument_list|()
condition|)
block|{
name|resetRuntimeRatios
argument_list|()
expr_stmt|;
name|counter
operator|=
literal|0
expr_stmt|;
block|}
name|boolean
name|found
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|found
condition|)
block|{
if|if
condition|(
name|counter
operator|>=
name|getRuntimeRatios
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
name|counter
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|getRuntimeRatios
argument_list|()
operator|.
name|get
argument_list|(
name|counter
argument_list|)
operator|.
name|getRuntimeWeight
argument_list|()
operator|>
literal|0
condition|)
block|{
name|getRuntimeRatios
argument_list|()
operator|.
name|get
argument_list|(
name|counter
argument_list|)
operator|.
name|setRuntimeWeight
argument_list|(
operator|(
name|getRuntimeRatios
argument_list|()
operator|.
name|get
argument_list|(
name|counter
argument_list|)
operator|.
name|getRuntimeWeight
argument_list|()
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
else|else
block|{
name|counter
operator|++
expr_stmt|;
block|}
block|}
return|return
name|processors
operator|.
name|get
argument_list|(
name|counter
operator|++
argument_list|)
return|;
block|}
block|}
end_class

end_unit

