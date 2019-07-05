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
comment|/**  * Used for communicating with a remote Bonita BPM process engine.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|BonitaEndpointBuilderFactory
specifier|public
interface|interface
name|BonitaEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Bonita component.      */
DECL|interface|BonitaEndpointBuilder
specifier|public
interface|interface
name|BonitaEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedBonitaEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedBonitaEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Hostname where Bonita engine runs.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|hostname (String hostname)
specifier|default
name|BonitaEndpointBuilder
name|hostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hostname"
argument_list|,
name|hostname
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Port of the server hosting Bonita engine.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|port (String port)
specifier|default
name|BonitaEndpointBuilder
name|port
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of the process involved in the operation.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|processName (String processName)
specifier|default
name|BonitaEndpointBuilder
name|processName
parameter_list|(
name|String
name|processName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"processName"
argument_list|,
name|processName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Password to authenticate to Bonita engine.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|password (String password)
specifier|default
name|BonitaEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Username to authenticate to Bonita engine.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|username (String username)
specifier|default
name|BonitaEndpointBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setProperty
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
comment|/**      * Advanced builder for endpoint for the Bonita component.      */
DECL|interface|AdvancedBonitaEndpointBuilder
specifier|public
interface|interface
name|AdvancedBonitaEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|BonitaEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|BonitaEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedBonitaEndpointBuilder
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
name|AdvancedBonitaEndpointBuilder
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
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedBonitaEndpointBuilder
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
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedBonitaEndpointBuilder
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
comment|/**      * Bonita (camel-bonita)      * Used for communicating with a remote Bonita BPM process engine.      *       * Category: process      * Available as of version: 2.19      * Maven coordinates: org.apache.camel:camel-bonita      *       * Syntax:<code>bonita:operation</code>      *       * Path parameter: operation (required)      * Operation to use      * The value can be one of: startCase      */
DECL|method|bonita (String path)
specifier|default
name|BonitaEndpointBuilder
name|bonita
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|BonitaEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|BonitaEndpointBuilder
implements|,
name|AdvancedBonitaEndpointBuilder
block|{
specifier|public
name|BonitaEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"bonita"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|BonitaEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

