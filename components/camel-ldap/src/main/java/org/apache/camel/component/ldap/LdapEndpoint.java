begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ldap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ldap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|RuntimeCamelException
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The ldap component allows you to perform searches in LDAP servers using filters as the message payload.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.5.0"
argument_list|,
name|scheme
operator|=
literal|"ldap"
argument_list|,
name|title
operator|=
literal|"LDAP"
argument_list|,
name|syntax
operator|=
literal|"ldap:dirContextName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"ldap"
argument_list|)
DECL|class|LdapEndpoint
specifier|public
class|class
name|LdapEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|SYSTEM_DN
specifier|public
specifier|static
specifier|final
name|String
name|SYSTEM_DN
init|=
literal|"ou=system"
decl_stmt|;
DECL|field|OBJECT_SCOPE
specifier|public
specifier|static
specifier|final
name|String
name|OBJECT_SCOPE
init|=
literal|"object"
decl_stmt|;
DECL|field|ONELEVEL_SCOPE
specifier|public
specifier|static
specifier|final
name|String
name|ONELEVEL_SCOPE
init|=
literal|"onelevel"
decl_stmt|;
DECL|field|SUBTREE_SCOPE
specifier|public
specifier|static
specifier|final
name|String
name|SUBTREE_SCOPE
init|=
literal|"subtree"
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|dirContextName
specifier|private
name|String
name|dirContextName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|SYSTEM_DN
argument_list|)
DECL|field|base
specifier|private
name|String
name|base
init|=
name|SYSTEM_DN
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|SUBTREE_SCOPE
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
name|SUBTREE_SCOPE
decl_stmt|;
annotation|@
name|UriParam
DECL|field|pageSize
specifier|private
name|Integer
name|pageSize
decl_stmt|;
annotation|@
name|UriParam
DECL|field|returnedAttributes
specifier|private
name|String
name|returnedAttributes
decl_stmt|;
DECL|method|LdapEndpoint (String endpointUri, String remaining, LdapComponent component)
specifier|protected
name|LdapEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|LdapComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|dirContextName
operator|=
name|remaining
expr_stmt|;
block|}
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
name|RuntimeCamelException
argument_list|(
literal|"An LDAP Consumer would be the LDAP server itself! No such support here"
argument_list|)
throw|;
block|}
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
name|LdapProducer
argument_list|(
name|this
argument_list|,
name|dirContextName
argument_list|,
name|base
argument_list|,
name|toSearchControlScope
argument_list|(
name|scope
argument_list|)
argument_list|,
name|pageSize
argument_list|,
name|returnedAttributes
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getDirContextName ()
specifier|public
name|String
name|getDirContextName
parameter_list|()
block|{
return|return
name|dirContextName
return|;
block|}
comment|/**      * Name of either a {@link javax.naming.directory.DirContext}, or {@link java.util.Hashtable}, or {@link Map} bean to lookup in the registry.      * If the bean is either a Hashtable or Map then a new {@link javax.naming.directory.DirContext} instance is created for each use. If the bean      * is a {@link javax.naming.directory.DirContext} then the bean is used as given. The latter may not be possible in all situations where      * the {@link javax.naming.directory.DirContext} must not be shared, and in those situations it can be better to use {@link java.util.Hashtable} or {@link Map} instead.      */
DECL|method|setDirContextName (String dirContextName)
specifier|public
name|void
name|setDirContextName
parameter_list|(
name|String
name|dirContextName
parameter_list|)
block|{
name|this
operator|.
name|dirContextName
operator|=
name|dirContextName
expr_stmt|;
block|}
comment|/**      * When specified the ldap module uses paging to retrieve all results (most LDAP Servers throw an exception when trying to retrieve more than 1000 entries in one query).      * To be able to use this a LdapContext (subclass of DirContext) has to be passed in as ldapServerBean (otherwise an exception is thrown)      */
DECL|method|setPageSize (Integer pageSize)
specifier|public
name|void
name|setPageSize
parameter_list|(
name|Integer
name|pageSize
parameter_list|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
block|}
DECL|method|getPageSize ()
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
DECL|method|getBase ()
specifier|public
name|String
name|getBase
parameter_list|()
block|{
return|return
name|base
return|;
block|}
comment|/**      * The base DN for searches.      */
DECL|method|setBase (String base)
specifier|public
name|void
name|setBase
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
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
comment|/**      * Specifies how deeply to search the tree of entries, starting at the base DN.      */
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
DECL|method|getReturnedAttributes ()
specifier|public
name|String
name|getReturnedAttributes
parameter_list|()
block|{
return|return
name|returnedAttributes
return|;
block|}
comment|/**      * Comma-separated list of attributes that should be set in each entry of the result      */
DECL|method|setReturnedAttributes (String returnedAttributes)
specifier|public
name|void
name|setReturnedAttributes
parameter_list|(
name|String
name|returnedAttributes
parameter_list|)
block|{
name|this
operator|.
name|returnedAttributes
operator|=
name|returnedAttributes
expr_stmt|;
block|}
DECL|method|toSearchControlScope (String scope)
specifier|private
name|int
name|toSearchControlScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
if|if
condition|(
name|scope
operator|.
name|equalsIgnoreCase
argument_list|(
name|OBJECT_SCOPE
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
name|equalsIgnoreCase
argument_list|(
name|ONELEVEL_SCOPE
argument_list|)
condition|)
block|{
return|return
name|SearchControls
operator|.
name|ONELEVEL_SCOPE
return|;
block|}
elseif|else
if|if
condition|(
name|scope
operator|.
name|equalsIgnoreCase
argument_list|(
name|SUBTREE_SCOPE
argument_list|)
condition|)
block|{
return|return
name|SearchControls
operator|.
name|SUBTREE_SCOPE
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid search scope \""
operator|+
name|scope
operator|+
literal|"\" for LdapEndpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

