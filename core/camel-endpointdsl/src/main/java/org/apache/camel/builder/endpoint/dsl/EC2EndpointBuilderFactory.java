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
comment|/**  * The aws-ec2 is used for managing Amazon EC2 instances.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|EC2EndpointBuilderFactory
specifier|public
interface|interface
name|EC2EndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the AWS EC2 component.      */
DECL|interface|EC2EndpointBuilder
specifier|public
interface|interface
name|EC2EndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedEC2EndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedEC2EndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The region in which EC2 client needs to work. When using this          * parameter, the configuration will expect the capitalized name of the          * region (for example AP_EAST_1) You'll need to use the name          * Regions.EU_WEST_1.name().          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|region (String region)
specifier|default
name|EC2EndpointBuilder
name|region
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Advanced builder for endpoint for the AWS EC2 component.      */
DECL|interface|AdvancedEC2EndpointBuilder
specifier|public
interface|interface
name|AdvancedEC2EndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|EC2EndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|EC2EndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedEC2EndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
name|AdvancedEC2EndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedEC2EndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedEC2EndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.aws.ec2.EC2Operations</code> enum.      */
DECL|enum|EC2Operations
enum|enum
name|EC2Operations
block|{
DECL|enumConstant|createAndRunInstances
name|createAndRunInstances
block|,
DECL|enumConstant|startInstances
name|startInstances
block|,
DECL|enumConstant|stopInstances
name|stopInstances
block|,
DECL|enumConstant|terminateInstances
name|terminateInstances
block|,
DECL|enumConstant|describeInstances
name|describeInstances
block|,
DECL|enumConstant|describeInstancesStatus
name|describeInstancesStatus
block|,
DECL|enumConstant|rebootInstances
name|rebootInstances
block|,
DECL|enumConstant|monitorInstances
name|monitorInstances
block|,
DECL|enumConstant|unmonitorInstances
name|unmonitorInstances
block|,
DECL|enumConstant|createTags
name|createTags
block|,
DECL|enumConstant|deleteTags
name|deleteTags
block|;     }
comment|/**      * AWS EC2 (camel-aws-ec2)      * The aws-ec2 is used for managing Amazon EC2 instances.      *       * Category: cloud,management      * Available as of version: 2.16      * Maven coordinates: org.apache.camel:camel-aws-ec2      *       * Syntax:<code>aws-ec2:label</code>      *       * Path parameter: label (required)      * Logical name      */
DECL|method|eC2 (String path)
specifier|default
name|EC2EndpointBuilder
name|eC2
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|EC2EndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|EC2EndpointBuilder
implements|,
name|AdvancedEC2EndpointBuilder
block|{
specifier|public
name|EC2EndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"aws-ec2"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|EC2EndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

