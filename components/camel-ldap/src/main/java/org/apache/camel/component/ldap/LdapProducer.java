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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|NamingEnumeration
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
name|DirContext
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
name|InitialDirContext
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
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|ldap
operator|.
name|Control
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|ldap
operator|.
name|LdapContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|ldap
operator|.
name|PagedResultsControl
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|ldap
operator|.
name|PagedResultsResponseControl
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
name|NoSuchBeanException
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

begin_comment
comment|/**  * @version $  */
end_comment

begin_class
DECL|class|LdapProducer
specifier|public
class|class
name|LdapProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|remaining
specifier|private
name|String
name|remaining
decl_stmt|;
DECL|field|searchControls
specifier|private
name|SearchControls
name|searchControls
decl_stmt|;
DECL|field|searchBase
specifier|private
name|String
name|searchBase
decl_stmt|;
DECL|field|pageSize
specifier|private
name|Integer
name|pageSize
decl_stmt|;
DECL|method|LdapProducer (LdapEndpoint endpoint, String remaining, String base, int scope, Integer pageSize, String returnedAttributes)
specifier|public
name|LdapProducer
parameter_list|(
name|LdapEndpoint
name|endpoint
parameter_list|,
name|String
name|remaining
parameter_list|,
name|String
name|base
parameter_list|,
name|int
name|scope
parameter_list|,
name|Integer
name|pageSize
parameter_list|,
name|String
name|returnedAttributes
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|remaining
operator|=
name|remaining
expr_stmt|;
name|this
operator|.
name|searchBase
operator|=
name|base
expr_stmt|;
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
name|this
operator|.
name|searchControls
operator|=
operator|new
name|SearchControls
argument_list|()
expr_stmt|;
name|this
operator|.
name|searchControls
operator|.
name|setSearchScope
argument_list|(
name|scope
argument_list|)
expr_stmt|;
if|if
condition|(
name|returnedAttributes
operator|!=
literal|null
condition|)
block|{
name|String
name|returnedAtts
index|[]
init|=
name|returnedAttributes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Setting returning Attributes to searchControls: {}"
argument_list|,
name|Arrays
operator|.
name|toString
argument_list|(
name|returnedAtts
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|searchControls
operator|.
name|setReturningAttributes
argument_list|(
name|returnedAtts
argument_list|)
expr_stmt|;
block|}
block|}
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
name|String
name|filter
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|DirContext
name|dirContext
init|=
name|getDirContext
argument_list|()
decl_stmt|;
try|try
block|{
comment|// could throw NamingException
name|List
argument_list|<
name|SearchResult
argument_list|>
name|data
decl_stmt|;
if|if
condition|(
name|pageSize
operator|==
literal|null
condition|)
block|{
name|data
operator|=
name|simpleSearch
argument_list|(
name|dirContext
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
operator|(
name|dirContext
operator|instanceof
name|LdapContext
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"When using attribute 'pageSize' for a ldap endpoint, you must provide a LdapContext (subclass of DirContext)"
argument_list|)
throw|;
block|}
name|data
operator|=
name|pagedSearch
argument_list|(
operator|(
name|LdapContext
operator|)
name|dirContext
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setAttachments
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|dirContext
operator|!=
literal|null
condition|)
block|{
name|dirContext
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getDirContext ()
specifier|protected
name|DirContext
name|getDirContext
parameter_list|()
throws|throws
name|NamingException
block|{
comment|// Obtain our ldap context. We do this by looking up the context in our registry.
comment|// Note though that a new context is expected each time. Therefore if spring is
comment|// being used then use prototype="scope". If you do not then you might experience
comment|// concurrency issues as InitialContext is not required to support concurrency.
comment|// On the other hand if you have a DirContext that is able to support concurrency
comment|// then using the default singleton scope is entirely sufficient. Most DirContext
comment|// classes will require prototype scope though.
comment|// if its a Map/Hashtable then we create a new context per time
name|DirContext
name|answer
init|=
literal|null
decl_stmt|;
name|Object
name|context
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|instanceof
name|Hashtable
condition|)
block|{
name|answer
operator|=
operator|new
name|InitialDirContext
argument_list|(
operator|(
name|Hashtable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|context
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|context
operator|instanceof
name|Map
condition|)
block|{
name|Hashtable
name|hash
init|=
operator|new
name|Hashtable
argument_list|(
operator|(
name|Map
operator|)
name|context
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|InitialDirContext
argument_list|(
name|hash
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|context
operator|instanceof
name|DirContext
condition|)
block|{
name|answer
operator|=
operator|(
name|DirContext
operator|)
name|context
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|String
name|msg
init|=
literal|"Found bean: "
operator|+
name|remaining
operator|+
literal|" in Registry of type: "
operator|+
name|answer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" expected type was: "
operator|+
name|DirContext
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|simpleSearch (DirContext ldapContext, String searchFilter)
specifier|private
name|List
argument_list|<
name|SearchResult
argument_list|>
name|simpleSearch
parameter_list|(
name|DirContext
name|ldapContext
parameter_list|,
name|String
name|searchFilter
parameter_list|)
throws|throws
name|NamingException
block|{
name|List
argument_list|<
name|SearchResult
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<
name|SearchResult
argument_list|>
argument_list|()
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|namingEnumeration
init|=
name|ldapContext
operator|.
name|search
argument_list|(
name|searchBase
argument_list|,
name|searchFilter
argument_list|,
name|searchControls
argument_list|)
decl_stmt|;
while|while
condition|(
name|namingEnumeration
operator|!=
literal|null
operator|&&
name|namingEnumeration
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|data
operator|.
name|add
argument_list|(
name|namingEnumeration
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
DECL|method|pagedSearch (LdapContext ldapContext, String searchFilter)
specifier|private
name|List
argument_list|<
name|SearchResult
argument_list|>
name|pagedSearch
parameter_list|(
name|LdapContext
name|ldapContext
parameter_list|,
name|String
name|searchFilter
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|SearchResult
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<
name|SearchResult
argument_list|>
argument_list|()
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Using paged ldap search, pageSize={}"
argument_list|,
name|pageSize
argument_list|)
expr_stmt|;
name|Control
index|[]
name|requestControls
init|=
operator|new
name|Control
index|[]
block|{
operator|new
name|PagedResultsControl
argument_list|(
name|pageSize
argument_list|,
name|Control
operator|.
name|CRITICAL
argument_list|)
block|}
decl_stmt|;
name|ldapContext
operator|.
name|setRequestControls
argument_list|(
name|requestControls
argument_list|)
expr_stmt|;
do|do
block|{
name|List
argument_list|<
name|SearchResult
argument_list|>
name|pageResult
init|=
name|simpleSearch
argument_list|(
name|ldapContext
argument_list|,
name|searchFilter
argument_list|)
decl_stmt|;
name|data
operator|.
name|addAll
argument_list|(
name|pageResult
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Page returned {} entries"
argument_list|,
name|pageResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|prepareNextPage
argument_list|(
name|ldapContext
argument_list|)
condition|)
do|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found a total of {} entries for ldap filter {}"
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|,
name|searchFilter
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
DECL|method|prepareNextPage (LdapContext ldapContext)
specifier|private
name|boolean
name|prepareNextPage
parameter_list|(
name|LdapContext
name|ldapContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Control
index|[]
name|responseControls
init|=
name|ldapContext
operator|.
name|getResponseControls
argument_list|()
decl_stmt|;
name|byte
index|[]
name|cookie
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|responseControls
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Control
name|responseControl
range|:
name|responseControls
control|)
block|{
if|if
condition|(
name|responseControl
operator|instanceof
name|PagedResultsResponseControl
condition|)
block|{
name|PagedResultsResponseControl
name|prrc
init|=
operator|(
name|PagedResultsResponseControl
operator|)
name|responseControl
decl_stmt|;
name|cookie
operator|=
name|prrc
operator|.
name|getCookie
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|cookie
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|ldapContext
operator|.
name|setRequestControls
argument_list|(
operator|new
name|Control
index|[]
block|{
operator|new
name|PagedResultsControl
argument_list|(
name|pageSize
argument_list|,
name|cookie
argument_list|,
name|Control
operator|.
name|CRITICAL
argument_list|)
block|}
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

