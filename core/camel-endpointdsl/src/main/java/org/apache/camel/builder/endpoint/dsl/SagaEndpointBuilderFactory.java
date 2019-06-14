begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The saga component provides access to advanced options for managing the flow  * in the Saga EIP.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SagaEndpointBuilderFactory
specifier|public
interface|interface
name|SagaEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Saga component.      */
DECL|interface|SagaEndpointBuilder
specifier|public
specifier|static
interface|interface
name|SagaEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedSagaEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSagaEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Action to execute (complete or compensate).          * The option is a          *<code>org.apache.camel.component.saga.SagaEndpoint$SagaEndpointAction</code> type.          * @group producer          */
DECL|method|action (SagaEndpointAction action)
specifier|public
specifier|default
name|SagaEndpointBuilder
name|action
parameter_list|(
name|SagaEndpointAction
name|action
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"action"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Action to execute (complete or compensate).          * The option will be converted to a          *<code>org.apache.camel.component.saga.SagaEndpoint$SagaEndpointAction</code> type.          * @group producer          */
DECL|method|action (String action)
specifier|public
specifier|default
name|SagaEndpointBuilder
name|action
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"action"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Saga component.      */
DECL|interface|AdvancedSagaEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedSagaEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|SagaEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SagaEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedSagaEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedSagaEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|public
specifier|default
name|AdvancedSagaEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( String synchronous)
specifier|public
specifier|default
name|AdvancedSagaEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.saga.SagaEndpoint$SagaEndpointAction</code> enum.      */
DECL|enum|SagaEndpointAction
specifier|public
specifier|static
enum|enum
name|SagaEndpointAction
block|{
DECL|enumConstant|COMPLETE
DECL|enumConstant|COMPENSATE
name|COMPLETE
block|,
name|COMPENSATE
block|;     }
comment|/**      * The saga component provides access to advanced options for managing the      * flow in the Saga EIP. Creates a builder to build endpoints for the Saga      * component.      */
DECL|method|saga (String path)
specifier|public
specifier|default
name|SagaEndpointBuilder
name|saga
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SagaEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SagaEndpointBuilder
implements|,
name|AdvancedSagaEndpointBuilder
block|{
specifier|public
name|SagaEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"saga"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SagaEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

