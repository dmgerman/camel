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
name|concurrent
operator|.
name|ThreadLocalRandom
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

begin_class
DECL|class|WeightedRandomLoadBalancer
specifier|public
class|class
name|WeightedRandomLoadBalancer
extends|extends
name|WeightedLoadBalancer
block|{
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
block|}
annotation|@
name|Override
DECL|method|chooseProcessor (AsyncProcessor[] processors, Exchange exchange)
specifier|protected
specifier|synchronized
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
name|int
name|randomWeight
init|=
name|ThreadLocalRandom
operator|.
name|current
argument_list|()
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
name|int
name|index
init|=
literal|0
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|DistributionRatio
name|ratio
init|=
name|getRatios
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|choiceWeight
operator|+=
name|ratio
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
name|ratio
operator|.
name|decrement
argument_list|()
expr_stmt|;
name|decrementSum
argument_list|()
expr_stmt|;
name|lastIndex
operator|=
name|index
expr_stmt|;
return|return
name|processors
index|[
name|index
index|]
return|;
block|}
name|index
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
