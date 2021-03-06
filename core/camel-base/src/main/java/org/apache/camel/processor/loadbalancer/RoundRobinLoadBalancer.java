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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
comment|/**  * Implements the round robin load balancing policy  */
end_comment

begin_class
DECL|class|RoundRobinLoadBalancer
specifier|public
class|class
name|RoundRobinLoadBalancer
extends|extends
name|QueueLoadBalancer
block|{
DECL|field|counter
specifier|private
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
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
name|int
name|size
init|=
name|processors
operator|.
name|length
decl_stmt|;
name|int
name|c
init|=
name|counter
operator|.
name|updateAndGet
argument_list|(
name|x
lambda|->
operator|++
name|x
operator|<
name|size
condition|?
name|x
else|:
literal|0
argument_list|)
decl_stmt|;
return|return
name|processors
index|[
name|c
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
name|counter
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

