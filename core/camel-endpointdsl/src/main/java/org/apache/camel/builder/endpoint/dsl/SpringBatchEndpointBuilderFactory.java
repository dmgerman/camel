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
comment|/**  * The spring-batch component allows to send messages to Spring Batch for  * further processing.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SpringBatchEndpointBuilderFactory
specifier|public
interface|interface
name|SpringBatchEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Spring Batch component.      */
DECL|interface|SpringBatchEndpointBuilder
specifier|public
interface|interface
name|SpringBatchEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSpringBatchEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSpringBatchEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of the Spring Batch job located in the registry.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|jobName (String jobName)
specifier|default
name|SpringBatchEndpointBuilder
name|jobName
parameter_list|(
name|String
name|jobName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"jobName"
argument_list|,
name|jobName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Explicitly defines if the jobName should be taken from the headers          * instead of the URI.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|jobFromHeader (boolean jobFromHeader)
specifier|default
name|SpringBatchEndpointBuilder
name|jobFromHeader
parameter_list|(
name|boolean
name|jobFromHeader
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"jobFromHeader"
argument_list|,
name|jobFromHeader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Explicitly defines if the jobName should be taken from the headers          * instead of the URI.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|jobFromHeader (String jobFromHeader)
specifier|default
name|SpringBatchEndpointBuilder
name|jobFromHeader
parameter_list|(
name|String
name|jobFromHeader
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"jobFromHeader"
argument_list|,
name|jobFromHeader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Explicitly specifies a JobLauncher to be used.          *           * The option is a:          *<code>org.springframework.batch.core.launch.JobLauncher</code> type.          *           * Group: producer          */
DECL|method|jobLauncher (Object jobLauncher)
specifier|default
name|SpringBatchEndpointBuilder
name|jobLauncher
parameter_list|(
name|Object
name|jobLauncher
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"jobLauncher"
argument_list|,
name|jobLauncher
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Explicitly specifies a JobLauncher to be used.          *           * The option will be converted to a          *<code>org.springframework.batch.core.launch.JobLauncher</code> type.          *           * Group: producer          */
DECL|method|jobLauncher (String jobLauncher)
specifier|default
name|SpringBatchEndpointBuilder
name|jobLauncher
parameter_list|(
name|String
name|jobLauncher
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"jobLauncher"
argument_list|,
name|jobLauncher
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Explicitly specifies a JobRegistry to be used.          *           * The option is a:          *<code>org.springframework.batch.core.configuration.JobRegistry</code>          * type.          *           * Group: producer          */
DECL|method|jobRegistry (Object jobRegistry)
specifier|default
name|SpringBatchEndpointBuilder
name|jobRegistry
parameter_list|(
name|Object
name|jobRegistry
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"jobRegistry"
argument_list|,
name|jobRegistry
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Explicitly specifies a JobRegistry to be used.          *           * The option will be converted to a          *<code>org.springframework.batch.core.configuration.JobRegistry</code>          * type.          *           * Group: producer          */
DECL|method|jobRegistry (String jobRegistry)
specifier|default
name|SpringBatchEndpointBuilder
name|jobRegistry
parameter_list|(
name|String
name|jobRegistry
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"jobRegistry"
argument_list|,
name|jobRegistry
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Spring Batch component.      */
DECL|interface|AdvancedSpringBatchEndpointBuilder
specifier|public
interface|interface
name|AdvancedSpringBatchEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SpringBatchEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SpringBatchEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSpringBatchEndpointBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedSpringBatchEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedSpringBatchEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedSpringBatchEndpointBuilder
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
comment|/**      * The spring-batch component allows to send messages to Spring Batch for      * further processing.      * Maven coordinates: org.apache.camel:camel-spring-batch      */
DECL|method|springBatch (String path)
specifier|default
name|SpringBatchEndpointBuilder
name|springBatch
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SpringBatchEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SpringBatchEndpointBuilder
implements|,
name|AdvancedSpringBatchEndpointBuilder
block|{
specifier|public
name|SpringBatchEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"spring-batch"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SpringBatchEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

