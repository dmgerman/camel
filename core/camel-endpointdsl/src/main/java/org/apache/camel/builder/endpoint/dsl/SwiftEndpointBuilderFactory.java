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
comment|/**  * The openstack-swift component allows messages to be sent to an OpenStack  * object storage services.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SwiftEndpointBuilderFactory
specifier|public
interface|interface
name|SwiftEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the OpenStack Swift component.      */
DECL|interface|SwiftEndpointBuilder
specifier|public
interface|interface
name|SwiftEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSwiftEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSwiftEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * OpenStack API version.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|apiVersion (String apiVersion)
specifier|default
name|SwiftEndpointBuilder
name|apiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"apiVersion"
argument_list|,
name|apiVersion
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * OpenStack configuration.          *           * The option is a:<code>org.openstack4j.core.transport.Config</code>          * type.          *           * Group: producer          */
DECL|method|config (Object config)
specifier|default
name|SwiftEndpointBuilder
name|config
parameter_list|(
name|Object
name|config
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"config"
argument_list|,
name|config
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * OpenStack configuration.          *           * The option will be converted to a          *<code>org.openstack4j.core.transport.Config</code> type.          *           * Group: producer          */
DECL|method|config (String config)
specifier|default
name|SwiftEndpointBuilder
name|config
parameter_list|(
name|String
name|config
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"config"
argument_list|,
name|config
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Authentication domain.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|domain (String domain)
specifier|default
name|SwiftEndpointBuilder
name|domain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"domain"
argument_list|,
name|domain
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The operation to do.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|operation (String operation)
specifier|default
name|SwiftEndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * OpenStack password.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: producer          */
DECL|method|password (String password)
specifier|default
name|SwiftEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The project ID.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: producer          */
DECL|method|project (String project)
specifier|default
name|SwiftEndpointBuilder
name|project
parameter_list|(
name|String
name|project
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"project"
argument_list|,
name|project
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * OpenStack Swift subsystem.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: producer          */
DECL|method|subsystem (String subsystem)
specifier|default
name|SwiftEndpointBuilder
name|subsystem
parameter_list|(
name|String
name|subsystem
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"subsystem"
argument_list|,
name|subsystem
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * OpenStack username.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: producer          */
DECL|method|username (String username)
specifier|default
name|SwiftEndpointBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the OpenStack Swift component.      */
DECL|interface|AdvancedSwiftEndpointBuilder
specifier|public
interface|interface
name|AdvancedSwiftEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SwiftEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SwiftEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSwiftEndpointBuilder
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
name|AdvancedSwiftEndpointBuilder
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
name|AdvancedSwiftEndpointBuilder
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
name|AdvancedSwiftEndpointBuilder
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
comment|/**      * OpenStack Swift (camel-openstack)      * The openstack-swift component allows messages to be sent to an OpenStack      * object storage services.      *       * Category: cloud,paas      * Available as of version: 2.19      * Maven coordinates: org.apache.camel:camel-openstack      *       * Syntax:<code>openstack-swift:host</code>      *       * Path parameter: host (required)      * OpenStack host url      */
DECL|method|openstackSwift (String path)
specifier|default
name|SwiftEndpointBuilder
name|openstackSwift
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SwiftEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SwiftEndpointBuilder
implements|,
name|AdvancedSwiftEndpointBuilder
block|{
specifier|public
name|SwiftEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"openstack-swift"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SwiftEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

