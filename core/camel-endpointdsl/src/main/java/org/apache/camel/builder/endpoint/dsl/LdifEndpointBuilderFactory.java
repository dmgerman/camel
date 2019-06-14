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
comment|/**  * The ldif component allows you to do updates on an LDAP server from a LDIF  * body content.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|LdifEndpointBuilderFactory
specifier|public
interface|interface
name|LdifEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the LDIF component.      */
DECL|interface|LdifEndpointBuilder
specifier|public
specifier|static
interface|interface
name|LdifEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedLdifEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedLdifEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of the LdapConnection bean to pull from the registry. Note          * that this must be of scope prototype to avoid it being shared among          * threads or using a connection that has timed out.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|ldapConnectionName ( String ldapConnectionName)
specifier|public
specifier|default
name|LdifEndpointBuilder
name|ldapConnectionName
parameter_list|(
name|String
name|ldapConnectionName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ldapConnectionName"
argument_list|,
name|ldapConnectionName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the LDIF component.      */
DECL|interface|AdvancedLdifEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedLdifEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|LdifEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|LdifEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedLdifEndpointBuilder
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
name|AdvancedLdifEndpointBuilder
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
name|AdvancedLdifEndpointBuilder
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
name|AdvancedLdifEndpointBuilder
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
comment|/**      * The ldif component allows you to do updates on an LDAP server from a LDIF      * body content. Creates a builder to build endpoints for the LDIF      * component.      */
DECL|method|ldif (String path)
specifier|public
specifier|default
name|LdifEndpointBuilder
name|ldif
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|LdifEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|LdifEndpointBuilder
implements|,
name|AdvancedLdifEndpointBuilder
block|{
specifier|public
name|LdifEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"ldif"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|LdifEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

