begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
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
name|ExchangePattern
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
name|Message
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
name|spring
operator|.
name|integration
operator|.
name|adapter
operator|.
name|CamelTargetAdapter
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * An {@link Exchange} for working with Spring Integration endpoints which exposes the underlying  * Spring messages via {@link #getIn()} and {@link #getOut()}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SpringIntegrationExchange
specifier|public
class|class
name|SpringIntegrationExchange
extends|extends
name|DefaultExchange
block|{
DECL|method|SpringIntegrationExchange (SpringIntegrationEndpoint endpoint)
specifier|public
name|SpringIntegrationExchange
parameter_list|(
name|SpringIntegrationEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|SpringIntegrationExchange (SpringIntegrationEndpoint endpoint, ExchangePattern pattern)
specifier|public
name|SpringIntegrationExchange
parameter_list|(
name|SpringIntegrationEndpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
DECL|method|SpringIntegrationExchange (CamelTargetAdapter adapter, ExchangePattern pattern)
specifier|public
name|SpringIntegrationExchange
parameter_list|(
name|CamelTargetAdapter
name|adapter
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
DECL|method|getFromSpringIntegrationEndpoint ()
specifier|public
name|SpringIntegrationEndpoint
name|getFromSpringIntegrationEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SpringIntegrationEndpoint
operator|)
name|super
operator|.
name|getFromEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|Exchange
name|newInstance
parameter_list|()
block|{
if|if
condition|(
name|getFromSpringIntegrationEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|SpringIntegrationExchange
argument_list|(
name|getFromSpringIntegrationEndpoint
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|this
operator|.
name|getContext
argument_list|()
argument_list|,
name|this
operator|.
name|getPattern
argument_list|()
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createFaultMessage ()
specifier|protected
name|Message
name|createFaultMessage
parameter_list|()
block|{
return|return
operator|new
name|SpringIntegrationMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createInMessage ()
specifier|protected
name|Message
name|createInMessage
parameter_list|()
block|{
return|return
operator|new
name|SpringIntegrationMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createOutMessage ()
specifier|protected
name|Message
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|SpringIntegrationMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

