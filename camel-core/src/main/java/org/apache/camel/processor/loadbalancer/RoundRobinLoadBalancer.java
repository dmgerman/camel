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

begin_comment
comment|/**  * Implements the round robin load balancing policy  *  * @version   */
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
name|int
name|counter
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|chooseProcessor (List<Processor> processors, Exchange exchange)
specifier|protected
specifier|synchronized
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
name|size
init|=
name|processors
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
operator|++
name|counter
operator|>=
name|size
condition|)
block|{
name|counter
operator|=
literal|0
expr_stmt|;
block|}
return|return
name|processors
operator|.
name|get
argument_list|(
name|counter
argument_list|)
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RoundRobinLoadBalancer"
return|;
block|}
block|}
end_class

end_unit

