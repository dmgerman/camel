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
comment|/**  * A {@link LoadBalancer} implementations which sends to all destinations  * (rather like JMS Topics).    *   * @version $Revision$  */
end_comment

begin_class
DECL|class|TopicLoadBalancer
specifier|public
class|class
name|TopicLoadBalancer
extends|extends
name|LoadBalancerSupport
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Processor
argument_list|>
name|list
init|=
name|getProcessors
argument_list|()
decl_stmt|;
for|for
control|(
name|Processor
name|processor
range|:
name|list
control|)
block|{
name|Exchange
name|copy
init|=
name|copyExchangeStrategy
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy method to copy the exchange before sending to another endpoint.      * Derived classes such as the {@link org.apache.camel.processor.Pipeline Pipeline}      * will not clone the exchange      *       * @param processor the processor that will send the exchange      * @param exchange  the exchange      * @return the current exchange if no copying is required such as for a      *         pipeline otherwise a new copy of the exchange is returned.      */
DECL|method|copyExchangeStrategy (Processor processor, Exchange exchange)
specifier|protected
name|Exchange
name|copyExchangeStrategy
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|copy
argument_list|()
return|;
block|}
block|}
end_class

end_unit

