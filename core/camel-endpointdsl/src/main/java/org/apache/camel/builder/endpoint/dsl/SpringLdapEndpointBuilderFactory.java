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
comment|/**  * The spring-ldap component allows you to perform searches in LDAP servers  * using filters as the message payload.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SpringLdapEndpointBuilderFactory
specifier|public
interface|interface
name|SpringLdapEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Spring LDAP component.      */
DECL|interface|SpringLdapEndpointBuilder
specifier|public
interface|interface
name|SpringLdapEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSpringLdapEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSpringLdapEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Name of the Spring LDAP Template bean.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|templateName (String templateName)
specifier|default
name|SpringLdapEndpointBuilder
name|templateName
parameter_list|(
name|String
name|templateName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"templateName"
argument_list|,
name|templateName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The LDAP operation to be performed.          *           * The option is a:          *<code>org.apache.camel.component.springldap.LdapOperation</code>          * type.          *           * Required: true          * Group: producer          */
DECL|method|operation (LdapOperation operation)
specifier|default
name|SpringLdapEndpointBuilder
name|operation
parameter_list|(
name|LdapOperation
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * The LDAP operation to be performed.          *           * The option will be converted to a          *<code>org.apache.camel.component.springldap.LdapOperation</code>          * type.          *           * Required: true          * Group: producer          */
DECL|method|operation (String operation)
specifier|default
name|SpringLdapEndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|setProperty
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
comment|/**          * The scope of the search operation.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|scope (String scope)
specifier|default
name|SpringLdapEndpointBuilder
name|scope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"scope"
argument_list|,
name|scope
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Spring LDAP component.      */
DECL|interface|AdvancedSpringLdapEndpointBuilder
specifier|public
interface|interface
name|AdvancedSpringLdapEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SpringLdapEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SpringLdapEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSpringLdapEndpointBuilder
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
name|AdvancedSpringLdapEndpointBuilder
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
name|AdvancedSpringLdapEndpointBuilder
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
name|AdvancedSpringLdapEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.springldap.LdapOperation</code> enum.      */
DECL|enum|LdapOperation
enum|enum
name|LdapOperation
block|{
DECL|enumConstant|SEARCH
name|SEARCH
block|,
DECL|enumConstant|BIND
name|BIND
block|,
DECL|enumConstant|UNBIND
name|UNBIND
block|,
DECL|enumConstant|AUTHENTICATE
name|AUTHENTICATE
block|,
DECL|enumConstant|MODIFY_ATTRIBUTES
name|MODIFY_ATTRIBUTES
block|,
DECL|enumConstant|FUNCTION_DRIVEN
name|FUNCTION_DRIVEN
block|;     }
comment|/**      * The spring-ldap component allows you to perform searches in LDAP servers      * using filters as the message payload.      * Maven coordinates: org.apache.camel:camel-spring-ldap      */
DECL|method|springLdap (String path)
specifier|default
name|SpringLdapEndpointBuilder
name|springLdap
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SpringLdapEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SpringLdapEndpointBuilder
implements|,
name|AdvancedSpringLdapEndpointBuilder
block|{
specifier|public
name|SpringLdapEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"spring-ldap"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SpringLdapEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

