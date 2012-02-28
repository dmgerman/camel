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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
DECL|class|WeightedRandomLoadBalancer
specifier|public
class|class
name|WeightedRandomLoadBalancer
extends|extends
name|WeightedLoadBalancer
block|{
DECL|field|rnd
specifier|private
specifier|final
name|Random
name|rnd
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
DECL|field|distributionRatioSum
specifier|private
specifier|final
name|int
name|distributionRatioSum
decl_stmt|;
DECL|field|runtimeRatioSum
specifier|private
name|int
name|runtimeRatioSum
decl_stmt|;
DECL|method|WeightedRandomLoadBalancer (List<Integer> distributionRatioList)
specifier|public
name|WeightedRandomLoadBalancer
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|distributionRatioList
parameter_list|)
block|{
name|super
argument_list|(
name|distributionRatioList
argument_list|)
expr_stmt|;
name|int
name|sum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Integer
name|distributionRatio
range|:
name|distributionRatioList
control|)
block|{
name|sum
operator|+=
name|distributionRatio
expr_stmt|;
block|}
name|distributionRatioSum
operator|=
name|sum
expr_stmt|;
name|runtimeRatioSum
operator|=
name|distributionRatioSum
expr_stmt|;
block|}
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
name|int
name|selectedProcessorIndex
init|=
name|selectProcessIndex
argument_list|()
decl_stmt|;
return|return
name|processors
operator|.
name|get
argument_list|(
name|selectedProcessorIndex
argument_list|)
return|;
block|}
DECL|method|selectProcessIndex ()
specifier|public
name|int
name|selectProcessIndex
parameter_list|()
block|{
if|if
condition|(
name|runtimeRatioSum
operator|==
literal|0
condition|)
block|{
comment|// every processor is exhausted, reload for a new distribution round
for|for
control|(
name|DistributionRatio
name|distributionRatio
range|:
name|getRuntimeRatios
argument_list|()
control|)
block|{
name|int
name|weight
init|=
name|distributionRatio
operator|.
name|getDistributionWeight
argument_list|()
decl_stmt|;
name|distributionRatio
operator|.
name|setRuntimeWeight
argument_list|(
name|weight
argument_list|)
expr_stmt|;
block|}
name|runtimeRatioSum
operator|=
name|distributionRatioSum
expr_stmt|;
block|}
name|DistributionRatio
name|selected
init|=
literal|null
decl_stmt|;
name|int
name|randomWeight
init|=
name|rnd
operator|.
name|nextInt
argument_list|(
name|runtimeRatioSum
argument_list|)
decl_stmt|;
name|int
name|choiceWeight
init|=
literal|0
decl_stmt|;
for|for
control|(
name|DistributionRatio
name|distributionRatio
range|:
name|getRuntimeRatios
argument_list|()
control|)
block|{
name|choiceWeight
operator|+=
name|distributionRatio
operator|.
name|getRuntimeWeight
argument_list|()
expr_stmt|;
if|if
condition|(
name|randomWeight
operator|<
name|choiceWeight
condition|)
block|{
name|selected
operator|=
name|distributionRatio
expr_stmt|;
break|break;
block|}
block|}
name|selected
operator|.
name|setRuntimeWeight
argument_list|(
name|selected
operator|.
name|getRuntimeWeight
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|runtimeRatioSum
operator|--
expr_stmt|;
return|return
name|selected
operator|.
name|getProcessorPosition
argument_list|()
return|;
block|}
block|}
end_class

end_unit

