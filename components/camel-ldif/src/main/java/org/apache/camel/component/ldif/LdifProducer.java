begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ldif
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ldif
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|List
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
name|CamelException
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
name|InvalidPayloadException
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
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|exception
operator|.
name|LdapException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|ldif
operator|.
name|LdifEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|ldif
operator|.
name|LdifReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|name
operator|.
name|Dn
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|name
operator|.
name|Rdn
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|ldap
operator|.
name|client
operator|.
name|api
operator|.
name|LdapConnection
import|;
end_import

begin_class
DECL|class|LdifProducer
specifier|public
class|class
name|LdifProducer
extends|extends
name|DefaultProducer
block|{
comment|// Constants
DECL|field|LDIF_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|LDIF_HEADER
init|=
literal|"version: 1"
decl_stmt|;
comment|// properties
DECL|field|ldapConnectionName
specifier|private
name|String
name|ldapConnectionName
decl_stmt|;
DECL|method|LdifProducer (LdifEndpoint endpoint, String ldapConnectionName)
specifier|public
name|LdifProducer
parameter_list|(
name|LdifEndpoint
name|endpoint
parameter_list|,
name|String
name|ldapConnectionName
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
name|ldapConnectionName
operator|=
name|ldapConnectionName
expr_stmt|;
block|}
comment|/**      * Process the body. There are two options:      *<ol>      *<li>A String body that is the LDIF content. This needs to start with      * "version: 1".</li>      *<li>A String body that is a URL to ready the LDIF content from</li>      *</ol>      */
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
name|body
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
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
literal|null
decl_stmt|;
comment|// Pass through everything
name|exchange
operator|.
name|setOut
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
comment|// If nothing to do, then return an empty body
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|body
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|.
name|startsWith
argument_list|(
name|LDIF_HEADER
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Reading from LDIF body"
argument_list|)
expr_stmt|;
name|result
operator|=
name|processLdif
argument_list|(
operator|new
name|StringReader
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|URL
name|loc
decl_stmt|;
try|try
block|{
name|loc
operator|=
operator|new
name|URL
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Reading from URL: {}"
argument_list|,
name|loc
argument_list|)
expr_stmt|;
name|result
operator|=
name|processLdif
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|loc
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
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
literal|"Failed to parse body as URL and LDIF"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
throw|;
block|}
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the LdapConnection. Since the object is a factory, we'll just call      * that. A future enhancement is to use the ApacheDS LdapConnectionPool      * object to keep a pool of working connections that avoids the connection      * pause.      *      * @return The created LDAP connection.      */
DECL|method|getLdapConnection ()
specifier|protected
name|LdapConnection
name|getLdapConnection
parameter_list|()
throws|throws
name|CamelException
block|{
return|return
operator|(
name|LdapConnection
operator|)
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
name|ldapConnectionName
argument_list|)
return|;
block|}
comment|/**      * Process an LDIF file from a reader.      */
DECL|method|processLdif (Reader reader)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|processLdif
parameter_list|(
name|Reader
name|reader
parameter_list|)
throws|throws
name|CamelException
block|{
name|LdapConnection
name|conn
init|=
name|getLdapConnection
argument_list|()
decl_stmt|;
name|LdifReader
name|ldifReader
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Create the reader
try|try
block|{
name|ldifReader
operator|=
operator|new
name|LdifReader
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LdapException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Unable to create LDIF reader"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// Process each entry
for|for
control|(
name|LdifEntry
name|e
range|:
name|ldifReader
control|)
block|{
name|results
operator|.
name|add
argument_list|(
name|processLdifEntry
argument_list|(
name|conn
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|IOHelper
operator|.
name|close
argument_list|(
name|conn
argument_list|,
name|ldifReader
argument_list|,
name|reader
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
comment|/**      * Figure out the change is and what to do about it.      *      * @return A success/failure message      */
DECL|method|processLdifEntry (LdapConnection conn, LdifEntry ldifEntry)
specifier|private
name|String
name|processLdifEntry
parameter_list|(
name|LdapConnection
name|conn
parameter_list|,
name|LdifEntry
name|ldifEntry
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|ldifEntry
operator|.
name|isChangeAdd
argument_list|()
operator|||
name|ldifEntry
operator|.
name|isLdifContent
argument_list|()
condition|)
block|{
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
literal|"attempting add of "
operator|+
name|ldifEntry
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|conn
operator|.
name|add
argument_list|(
name|ldifEntry
operator|.
name|getEntry
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ldifEntry
operator|.
name|isChangeModify
argument_list|()
condition|)
block|{
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
literal|"attempting modify of "
operator|+
name|ldifEntry
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|conn
operator|.
name|modify
argument_list|(
name|ldifEntry
operator|.
name|getDn
argument_list|()
argument_list|,
name|ldifEntry
operator|.
name|getModificationArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ldifEntry
operator|.
name|isChangeDelete
argument_list|()
condition|)
block|{
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
literal|"attempting delete of "
operator|+
name|ldifEntry
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|conn
operator|.
name|delete
argument_list|(
name|ldifEntry
operator|.
name|getDn
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ldifEntry
operator|.
name|isChangeModDn
argument_list|()
condition|)
block|{
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
literal|"attempting DN move of "
operator|+
name|ldifEntry
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|conn
operator|.
name|moveAndRename
argument_list|(
name|ldifEntry
operator|.
name|getDn
argument_list|()
argument_list|,
operator|new
name|Dn
argument_list|(
name|ldifEntry
operator|.
name|getNewRdn
argument_list|()
argument_list|,
name|ldifEntry
operator|.
name|getNewSuperior
argument_list|()
argument_list|)
argument_list|,
name|ldifEntry
operator|.
name|isDeleteOldRdn
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ldifEntry
operator|.
name|isChangeModRdn
argument_list|()
condition|)
block|{
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
literal|"attempting RDN move of "
operator|+
name|ldifEntry
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|conn
operator|.
name|rename
argument_list|(
name|ldifEntry
operator|.
name|getDn
argument_list|()
argument_list|,
operator|new
name|Rdn
argument_list|(
name|ldifEntry
operator|.
name|getNewRdn
argument_list|()
argument_list|)
argument_list|,
name|ldifEntry
operator|.
name|isDeleteOldRdn
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"ldif success"
argument_list|)
expr_stmt|;
return|return
literal|"success"
return|;
block|}
catch|catch
parameter_list|(
name|LdapException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"failed to apply ldif"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|getRootCause
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
comment|/**      * Get the root cause of an exception      */
DECL|method|getRootCause (LdapException e)
specifier|private
name|String
name|getRootCause
parameter_list|(
name|LdapException
name|e
parameter_list|)
block|{
name|Throwable
name|oldt
decl_stmt|;
name|Throwable
name|thist
decl_stmt|;
name|oldt
operator|=
name|thist
operator|=
name|e
expr_stmt|;
while|while
condition|(
name|thist
operator|!=
literal|null
condition|)
block|{
name|oldt
operator|=
name|thist
expr_stmt|;
name|thist
operator|=
name|thist
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
return|return
name|oldt
operator|.
name|getMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

