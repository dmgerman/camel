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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

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
name|impl
operator|.
name|DefaultEndpoint
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

begin_class
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
DECL|field|operationMap
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|LdapOperation
argument_list|>
name|operationMap
decl_stmt|;
DECL|field|scopeMap
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|scopeMap
decl_stmt|;
DECL|field|ldapTemplate
specifier|private
name|LdapTemplate
name|ldapTemplate
decl_stmt|;
DECL|field|operation
specifier|private
name|LdapOperation
name|operation
decl_stmt|;
DECL|field|scope
specifier|private
name|int
name|scope
init|=
name|SearchControls
operator|.
name|SUBTREE_SCOPE
decl_stmt|;
DECL|field|templateName
specifier|private
name|String
name|templateName
decl_stmt|;
comment|/**      * Initializes the SpringLdapEndpoint using the provided template      * @param templateName name of the LDAP template      * @param ldapTemplate LDAP template, see org.springframework.ldap.core.LdapTemplate      */
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
if|if
condition|(
literal|null
operator|==
name|operationMap
condition|)
block|{
name|initializeOperationMap
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|scopeMap
condition|)
block|{
name|initializeScopeMap
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|initializeScopeMap ()
specifier|private
specifier|static
name|void
name|initializeScopeMap
parameter_list|()
block|{
name|scopeMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|scopeMap
operator|.
name|put
argument_list|(
name|OBJECT_SCOPE_NAME
argument_list|,
name|SearchControls
operator|.
name|OBJECT_SCOPE
argument_list|)
expr_stmt|;
name|scopeMap
operator|.
name|put
argument_list|(
name|ONELEVEL_SCOPE_NAME
argument_list|,
name|SearchControls
operator|.
name|ONELEVEL_SCOPE
argument_list|)
expr_stmt|;
name|scopeMap
operator|.
name|put
argument_list|(
name|SUBTREE_SCOPE_NAME
argument_list|,
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|)
expr_stmt|;
block|}
DECL|method|initializeOperationMap ()
specifier|private
specifier|static
name|void
name|initializeOperationMap
parameter_list|()
block|{
name|operationMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|LdapOperation
argument_list|>
argument_list|()
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
name|LdapOperation
operator|.
name|SEARCH
operator|.
name|name
argument_list|()
argument_list|,
name|LdapOperation
operator|.
name|SEARCH
argument_list|)
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
name|LdapOperation
operator|.
name|BIND
operator|.
name|name
argument_list|()
argument_list|,
name|LdapOperation
operator|.
name|BIND
argument_list|)
expr_stmt|;
name|operationMap
operator|.
name|put
argument_list|(
name|LdapOperation
operator|.
name|UNBIND
operator|.
name|name
argument_list|()
argument_list|,
name|LdapOperation
operator|.
name|UNBIND
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Producer using this SpringLdapEndpoint      */
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
comment|/**      * Consumer endpoints are not supported.      * @throw UnsupportedOperationException      */
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
comment|/**      * returns false (constant)      */
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
DECL|method|getScope ()
name|int
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
comment|/**      * sets the scope of the LDAP operation. The scope string must be one of "object", "onelevel", or "subtree"      * @param scope      */
DECL|method|setScope (String scope)
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|allowedScope
range|:
name|scopeMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|allowedScope
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
name|this
operator|.
name|scope
operator|=
name|allowedScope
operator|.
name|getValue
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Search scope '"
operator|+
name|scope
operator|+
literal|"' is not supported. The supported scopes are 'object', 'onelevel', and 'subtree'."
argument_list|)
throw|;
block|}
DECL|method|getLdapTemplate ()
name|LdapTemplate
name|getLdapTemplate
parameter_list|()
block|{
return|return
name|ldapTemplate
return|;
block|}
comment|/**      * @return URI used to create this endpoint       */
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
name|getScopeName
argument_list|()
return|;
block|}
DECL|method|getScopeName ()
specifier|private
name|String
name|getScopeName
parameter_list|()
block|{
for|for
control|(
name|String
name|key
range|:
name|scopeMap
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|scope
operator|==
name|scopeMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|key
return|;
block|}
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Search scope '"
operator|+
name|scope
operator|+
literal|"' is not supported. The supported scopes are 'object', 'onelevel', and 'subtree'."
argument_list|)
throw|;
block|}
DECL|method|getOperation ()
name|LdapOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Sets the LDAP operation to be performed. The supported operations are defined in org.apache.camel.component.springldap.LdapOperation.      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|LdapOperation
argument_list|>
name|allowedOperation
range|:
name|operationMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|allowedOperation
operator|.
name|getKey
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|this
operator|.
name|operation
operator|=
name|allowedOperation
operator|.
name|getValue
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"LDAP operation '"
operator|+
name|operation
operator|+
literal|"' is not supported. The supported operations are 'search', 'bind', and 'unbind'."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

