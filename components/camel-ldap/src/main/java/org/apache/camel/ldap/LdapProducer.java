begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.ldap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|List
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
name|DefaultExchange
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
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
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|DefaultProducer
argument_list|<
name|DefaultExchange
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|LdapProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ldapContext
specifier|private
name|DirContext
name|ldapContext
decl_stmt|;
DECL|field|controls
specifier|private
name|SearchControls
name|controls
decl_stmt|;
DECL|field|searchBase
specifier|private
name|String
name|searchBase
decl_stmt|;
DECL|method|LdapProducer (LdapEndpoint endpoint, String remaining, String base, int scope)
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
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|ldapContext
operator|=
operator|(
name|DirContext
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
name|lookup
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|searchBase
operator|=
name|base
expr_stmt|;
name|controls
operator|=
operator|new
name|SearchControls
argument_list|()
expr_stmt|;
name|controls
operator|.
name|setSearchScope
argument_list|(
name|scope
argument_list|)
expr_stmt|;
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
comment|// could throw NamingException
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
name|filter
argument_list|,
name|getControls
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
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
block|}
DECL|method|getDirContext ()
specifier|public
name|DirContext
name|getDirContext
parameter_list|()
block|{
return|return
name|ldapContext
return|;
block|}
DECL|method|getControls ()
specifier|protected
name|SearchControls
name|getControls
parameter_list|()
block|{
return|return
name|controls
return|;
block|}
block|}
end_class

end_unit

