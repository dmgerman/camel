begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.springldap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|springldap
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchControls
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|support
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ldap
operator|.
name|core
operator|.
name|LdapTemplate
import|;
end_import

begin_comment
comment|/**  * The spring-ldap component allows you to perform searches in LDAP servers using filters as the message payload.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.11.0"
argument_list|,
name|scheme
operator|=
literal|"spring-ldap"
argument_list|,
name|title
operator|=
literal|"Spring LDAP"
argument_list|,
name|syntax
operator|=
literal|"spring-ldap:templateName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"spring,ldap"
argument_list|)
DECL|class|SpringLdapEndpoint
specifier|public
class|class
name|SpringLdapEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|OBJECT_SCOPE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|OBJECT_SCOPE_NAME
init|=
literal|"object"
decl_stmt|;
DECL|field|ONELEVEL_SCOPE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|ONELEVEL_SCOPE_NAME
init|=
literal|"onelevel"
decl_stmt|;
DECL|field|SUBTREE_SCOPE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SUBTREE_SCOPE_NAME
init|=
literal|"subtree"
decl_stmt|;
DECL|field|ldapTemplate
specifier|private
name|LdapTemplate
name|ldapTemplate
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|templateName
specifier|private
name|String
name|templateName
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|operation
specifier|private
name|LdapOperation
name|operation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"subtree"
argument_list|,
name|enums
operator|=
literal|"object,onelevel,subtree"
argument_list|)
DECL|field|scope
specifier|private
name|String
name|scope
init|=
name|SUBTREE_SCOPE_NAME
decl_stmt|;
comment|/**      * Initializes the SpringLdapEndpoint using the provided template      *      * @param templateName name of the LDAP template      * @param ldapTemplate LDAP template, see org.springframework.ldap.core.LdapTemplate      */
DECL|method|SpringLdapEndpoint (String templateName, LdapTemplate ldapTemplate)
specifier|public
name|SpringLdapEndpoint
parameter_list|(
name|String
name|templateName
parameter_list|,
name|LdapTemplate
name|ldapTemplate
parameter_list|)
block|{
name|this
operator|.
name|templateName
operator|=
name|templateName
expr_stmt|;
name|this
operator|.
name|ldapTemplate
operator|=
name|ldapTemplate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SpringLdapProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"spring-ldap endpoint supports producer enrpoint only."
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|public
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"spring-ldap://"
operator|+
name|templateName
operator|+
literal|"?operation="
operator|+
name|operation
operator|.
name|name
argument_list|()
operator|+
literal|"&scope="
operator|+
name|getScope
argument_list|()
return|;
block|}
DECL|method|getLdapTemplate ()
specifier|public
name|LdapTemplate
name|getLdapTemplate
parameter_list|()
block|{
return|return
name|ldapTemplate
return|;
block|}
DECL|method|getTemplateName ()
specifier|public
name|String
name|getTemplateName
parameter_list|()
block|{
return|return
name|templateName
return|;
block|}
comment|/**      * Name of the Spring LDAP Template bean      */
DECL|method|setTemplateName (String templateName)
specifier|public
name|void
name|setTemplateName
parameter_list|(
name|String
name|templateName
parameter_list|)
block|{
name|this
operator|.
name|templateName
operator|=
name|templateName
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|LdapOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The LDAP operation to be performed.      */
DECL|method|setOperation (LdapOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|LdapOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getScope ()
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
comment|/**      * The scope of the search operation.      */
DECL|method|setScope (String scope)
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
DECL|method|scopeValue ()
specifier|public
name|int
name|scopeValue
parameter_list|()
block|{
if|if
condition|(
name|scope
operator|.
name|equals
argument_list|(
name|OBJECT_SCOPE_NAME
argument_list|)
condition|)
block|{
return|return
name|SearchControls
operator|.
name|OBJECT_SCOPE
return|;
block|}
elseif|else
if|if
condition|(
name|scope
operator|.
name|equals
argument_list|(
name|ONELEVEL_SCOPE_NAME
argument_list|)
condition|)
block|{
return|return
name|SearchControls
operator|.
name|ONELEVEL_SCOPE
return|;
block|}
else|else
block|{
return|return
name|SearchControls
operator|.
name|SUBTREE_SCOPE
return|;
block|}
block|}
block|}
end_class

end_unit

