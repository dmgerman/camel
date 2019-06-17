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
comment|/**  * The aws-kms is used for managing Amazon ECS  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ECSEndpointBuilderFactory
specifier|public
interface|interface
name|ECSEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the AWS ECS component.      */
DECL|interface|ECSEndpointBuilder
specifier|public
interface|interface
name|ECSEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedECSEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedECSEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Logical name.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|label (String label)
specifier|default
name|ECSEndpointBuilder
name|label
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"label"
argument_list|,
name|label
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The region in which ECS client needs to work.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|region (String region)
specifier|default
name|ECSEndpointBuilder
name|region
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"region"
argument_list|,
name|region
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the AWS ECS component.      */
DECL|interface|AdvancedECSEndpointBuilder
specifier|public
interface|interface
name|AdvancedECSEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ECSEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ECSEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedECSEndpointBuilder
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
specifier|default
name|AdvancedECSEndpointBuilder
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
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedECSEndpointBuilder
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
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedECSEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.aws.ecs.ECSOperations</code> enum.      */
DECL|enum|ECSOperations
enum|enum
name|ECSOperations
block|{
DECL|enumConstant|listClusters
DECL|enumConstant|describeCluster
DECL|enumConstant|createCluster
DECL|enumConstant|deleteCluster
name|listClusters
block|,
name|describeCluster
block|,
name|createCluster
block|,
name|deleteCluster
block|;     }
comment|/**      * The aws-kms is used for managing Amazon ECS Creates a builder to build      * endpoints for the AWS ECS component.      */
DECL|method|eCS (String path)
specifier|default
name|ECSEndpointBuilder
name|eCS
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ECSEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ECSEndpointBuilder
implements|,
name|AdvancedECSEndpointBuilder
block|{
specifier|public
name|ECSEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"aws-ecs"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ECSEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

