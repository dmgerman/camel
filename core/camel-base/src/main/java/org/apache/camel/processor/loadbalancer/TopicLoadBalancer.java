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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * A {@link LoadBalancer} implementations which sends to all destinations  * (rather like JMS Topics).  *<p/>  * The {@link org.apache.camel.processor.MulticastProcessor} is more powerful as it offers  * option to run in parallel and decide whether or not to stop on failure etc.  */
end_comment

begin_class
DECL|class|TopicLoadBalancer
specifier|public
class|class
name|TopicLoadBalancer
extends|extends
name|LoadBalancerSupport
block|{
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|AsyncProcessor
index|[]
name|processors
init|=
name|doGetProcessors
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getReactiveExecutor
argument_list|()
operator|.
name|schedule
argument_list|(
operator|new
name|State
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|processors
argument_list|)
operator|::
name|run
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
DECL|class|State
specifier|protected
class|class
name|State
block|{
DECL|field|exchange
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|processors
specifier|final
name|AsyncProcessor
index|[]
name|processors
decl_stmt|;
DECL|field|index
name|int
name|index
decl_stmt|;
DECL|method|State (Exchange exchange, AsyncCallback callback, AsyncProcessor[] processors)
specifier|public
name|State
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|AsyncProcessor
index|[]
name|processors
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|processors
operator|=
name|processors
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|index
operator|<
name|processors
operator|.
name|length
condition|)
block|{
name|AsyncProcessor
name|processor
init|=
name|processors
index|[
name|index
operator|++
index|]
decl_stmt|;
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
argument_list|,
name|doneSync
lambda|->
name|done
argument_list|(
name|copy
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|done (Exchange current)
specifier|public
name|void
name|done
parameter_list|(
name|Exchange
name|current
parameter_list|)
block|{
if|if
condition|(
name|current
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|current
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getReactiveExecutor
argument_list|()
operator|.
name|schedule
argument_list|(
name|this
operator|::
name|run
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Strategy method to copy the exchange before sending to another endpoint.      * Derived classes such as the {@link org.apache.camel.processor.Pipeline Pipeline}      * will not clone the exchange      *      * @param processor the processor that will send the exchange      * @param exchange  the exchange      * @return the current exchange if no copying is required such as for a      *         pipeline otherwise a new copy of the exchange is returned.      */
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

