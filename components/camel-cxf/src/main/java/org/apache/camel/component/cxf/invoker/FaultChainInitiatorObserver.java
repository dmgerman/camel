begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.invoker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|invoker
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
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
name|component
operator|.
name|cxf
operator|.
name|interceptors
operator|.
name|FaultOutInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|AbstractFaultChainInitiatorObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|phase
operator|.
name|Phase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_class
DECL|class|FaultChainInitiatorObserver
specifier|public
class|class
name|FaultChainInitiatorObserver
extends|extends
name|AbstractFaultChainInitiatorObserver
block|{
DECL|field|phases
specifier|private
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
decl_stmt|;
DECL|field|isOutbound
specifier|private
name|boolean
name|isOutbound
decl_stmt|;
DECL|method|FaultChainInitiatorObserver (Bus bus, SortedSet<Phase> phases, boolean isOutbound)
specifier|public
name|FaultChainInitiatorObserver
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
parameter_list|,
name|boolean
name|isOutbound
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|this
operator|.
name|phases
operator|=
name|phases
expr_stmt|;
name|this
operator|.
name|isOutbound
operator|=
name|isOutbound
expr_stmt|;
block|}
DECL|method|initializeInterceptors (Exchange ex, PhaseInterceptorChain chain)
specifier|protected
name|void
name|initializeInterceptors
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|PhaseInterceptorChain
name|chain
parameter_list|)
block|{
name|Endpoint
name|e
init|=
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|isOutboundObserver
argument_list|()
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|e
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|e
operator|.
name|getBinding
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|e
operator|.
name|getService
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|getBus
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
operator|new
name|FaultOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|chain
operator|.
name|add
argument_list|(
name|e
operator|.
name|getBinding
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|e
operator|.
name|getService
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|getBus
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getPhases ()
specifier|protected
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|getPhases
parameter_list|()
block|{
return|return
name|phases
return|;
block|}
annotation|@
name|Override
DECL|method|isOutboundObserver ()
specifier|protected
name|boolean
name|isOutboundObserver
parameter_list|()
block|{
return|return
name|isOutbound
return|;
block|}
block|}
end_class

end_unit

