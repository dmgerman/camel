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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
name|Attributes
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
name|ModificationItem
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
name|Exchange
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
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|AttributesMapper
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
name|ContextSource
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
name|LdapOperations
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
name|support
operator|.
name|BaseLdapPathContextSource
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
name|query
operator|.
name|LdapQueryBuilder
import|;
end_import

begin_class
DECL|class|SpringLdapProducer
specifier|public
class|class
name|SpringLdapProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|DN
specifier|public
specifier|static
specifier|final
name|String
name|DN
init|=
literal|"dn"
decl_stmt|;
DECL|field|FILTER
specifier|public
specifier|static
specifier|final
name|String
name|FILTER
init|=
literal|"filter"
decl_stmt|;
DECL|field|ATTRIBUTES
specifier|public
specifier|static
specifier|final
name|String
name|ATTRIBUTES
init|=
literal|"attributes"
decl_stmt|;
DECL|field|PASSWORD
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"password"
decl_stmt|;
DECL|field|MODIFICATION_ITEMS
specifier|public
specifier|static
specifier|final
name|String
name|MODIFICATION_ITEMS
init|=
literal|"modificationItems"
decl_stmt|;
DECL|field|FUNCTION
specifier|public
specifier|static
specifier|final
name|String
name|FUNCTION
init|=
literal|"function"
decl_stmt|;
DECL|field|REQUEST
specifier|public
specifier|static
specifier|final
name|String
name|REQUEST
init|=
literal|"request"
decl_stmt|;
DECL|field|endpoint
name|SpringLdapEndpoint
name|endpoint
decl_stmt|;
DECL|field|mapper
specifier|private
name|AttributesMapper
argument_list|<
name|Object
argument_list|>
name|mapper
init|=
operator|new
name|AttributesMapper
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|mapFromAttributes
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
name|attributes
return|;
block|}
block|}
decl_stmt|;
comment|/**      * Initializes the SpringLdapProducer with the given endpoint      */
DECL|method|SpringLdapProducer (SpringLdapEndpoint endpoint)
specifier|public
name|SpringLdapProducer
parameter_list|(
name|SpringLdapEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
comment|/**      * Performs the LDAP operation defined in SpringLdapEndpoint that created      * this producer. The in-message in the exchange must be a map, containing      * the following entries:      *       *<pre>      * key: "dn" - base DN for the LDAP operation      * key: "filter" - necessary for the search operation only; LDAP filter for the search operation,      * see<a http://en.wikipedia.org/wiki/Lightweight_Directory_Access_Protocol>http://en.wikipedia.org/wiki/Lightweight_Directory_Access_Protocol</a>      * key: "attributes" - necessary for the bind operation only; an instance of javax.naming.directory.Attributes,      * containing the information necessary to create an LDAP node.      * key: "password" - necessary for the authentication operation only;      * key: "modificationItems" - necessary for the modify_attributes operation only;      * key: "function" - necessary for the function_driven operation only; provides a flexible hook into the {@link LdapTemplate} to call any method      * key: "request" - necessary for the function_driven operation only; passed into the "function" to enable the client to bind parameters that need to be passed into the {@link LdapTemplate}      *</pre>      *       * The keys are defined as final fields above.      */
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|LdapOperation
name|operation
init|=
name|endpoint
operator|.
name|getOperation
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|operation
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"LDAP operation must not be empty, but you provided an empty operation"
argument_list|)
throw|;
block|}
name|LdapTemplate
name|ldapTemplate
init|=
name|endpoint
operator|.
name|getLdapTemplate
argument_list|()
decl_stmt|;
name|String
name|dn
init|=
operator|(
name|String
operator|)
name|body
operator|.
name|get
argument_list|(
name|DN
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|dn
argument_list|)
condition|)
block|{
name|ContextSource
name|contextSource
init|=
name|ldapTemplate
operator|.
name|getContextSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|contextSource
operator|instanceof
name|BaseLdapPathContextSource
condition|)
block|{
name|dn
operator|=
operator|(
operator|(
name|BaseLdapPathContextSource
operator|)
name|contextSource
operator|)
operator|.
name|getBaseLdapPathAsString
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|operation
operator|!=
name|LdapOperation
operator|.
name|FUNCTION_DRIVEN
operator|&&
operator|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|dn
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"DN must not be empty, but you provided an empty DN"
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|SEARCH
case|:
name|String
name|filter
init|=
operator|(
name|String
operator|)
name|body
operator|.
name|get
argument_list|(
name|FILTER
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|ldapTemplate
operator|.
name|search
argument_list|(
name|dn
argument_list|,
name|filter
argument_list|,
name|endpoint
operator|.
name|scopeValue
argument_list|()
argument_list|,
name|mapper
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|BIND
case|:
name|Attributes
name|attributes
init|=
operator|(
name|Attributes
operator|)
name|body
operator|.
name|get
argument_list|(
name|ATTRIBUTES
argument_list|)
decl_stmt|;
name|ldapTemplate
operator|.
name|bind
argument_list|(
name|dn
argument_list|,
literal|null
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
break|break;
case|case
name|UNBIND
case|:
name|ldapTemplate
operator|.
name|unbind
argument_list|(
name|dn
argument_list|)
expr_stmt|;
break|break;
case|case
name|AUTHENTICATE
case|:
name|ldapTemplate
operator|.
name|authenticate
argument_list|(
name|LdapQueryBuilder
operator|.
name|query
argument_list|()
operator|.
name|base
argument_list|(
name|dn
argument_list|)
operator|.
name|filter
argument_list|(
operator|(
name|String
operator|)
name|body
operator|.
name|get
argument_list|(
name|FILTER
argument_list|)
argument_list|)
argument_list|,
operator|(
name|String
operator|)
name|body
operator|.
name|get
argument_list|(
name|PASSWORD
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|MODIFY_ATTRIBUTES
case|:
name|ModificationItem
index|[]
name|modificationItems
init|=
operator|(
name|ModificationItem
index|[]
operator|)
name|body
operator|.
name|get
argument_list|(
name|MODIFICATION_ITEMS
argument_list|)
decl_stmt|;
name|ldapTemplate
operator|.
name|modifyAttributes
argument_list|(
name|dn
argument_list|,
name|modificationItems
argument_list|)
expr_stmt|;
break|break;
case|case
name|FUNCTION_DRIVEN
case|:
name|BiFunction
argument_list|<
name|LdapOperations
argument_list|,
name|Object
argument_list|,
name|?
argument_list|>
name|ldapOperationFunction
init|=
operator|(
name|BiFunction
argument_list|<
name|LdapOperations
argument_list|,
name|Object
argument_list|,
name|?
argument_list|>
operator|)
name|body
operator|.
name|get
argument_list|(
name|FUNCTION
argument_list|)
decl_stmt|;
name|Object
name|ldapOperationRequest
init|=
name|body
operator|.
name|get
argument_list|(
name|REQUEST
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|ldapOperationFunction
operator|.
name|apply
argument_list|(
name|ldapTemplate
argument_list|,
name|ldapOperationRequest
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Bug in the Spring-LDAP component. Despite of all assertions, you managed to call an unsupported operation '"
operator|+
name|operation
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

