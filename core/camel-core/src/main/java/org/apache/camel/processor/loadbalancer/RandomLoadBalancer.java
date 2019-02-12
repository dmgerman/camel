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

begin_comment
comment|/**  * Implements the random load balancing policy  */
end_comment

begin_class
DECL|class|RandomLoadBalancer
specifier|public
class|class
name|RandomLoadBalancer
extends|extends
name|QueueLoadBalancer
block|{
DECL|field|index
specifier|private
specifier|transient
name|int
name|index
decl_stmt|;
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
name|int
name|size
init|=
name|processors
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|size
operator|==
literal|1
condition|)
block|{
comment|// there is only 1
return|return
name|processors
index|[
literal|0
index|]
return|;
block|}
comment|// pick a random
name|index
operator|=
name|ThreadLocalRandom
operator|.
name|current
argument_list|()
operator|.
name|nextInt
argument_list|(
name|size
argument_list|)
expr_stmt|;
return|return
name|processors
index|[
name|index
index|]
return|;
block|}
DECL|method|getLastChosenProcessorIndex ()
specifier|public
name|int
name|getLastChosenProcessorIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
block|}
end_class

end_unit
