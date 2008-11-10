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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|CamelContext
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
name|ProducerTemplate
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
name|Service
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
name|RouteBuilder
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
name|DefaultCamelContext
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
name|JndiRegistry
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
name|management
operator|.
name|JmxSystemPropertyKeys
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
name|jndi
operator|.
name|JndiTest
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|constants
operator|.
name|ServerDNConstants
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
name|server
operator|.
name|unit
operator|.
name|AbstractServerTest
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|LdapTestSupport
specifier|public
specifier|abstract
class|class
name|LdapTestSupport
extends|extends
name|AbstractServerTest
block|{
DECL|field|log
specifier|protected
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|useRouteBuilder
specifier|private
name|boolean
name|useRouteBuilder
init|=
literal|true
decl_stmt|;
DECL|field|camelContextService
specifier|private
name|Service
name|camelContextService
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|loadTestLdif
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// disable JMX
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
comment|// create Camel context
name|context
operator|=
name|createCamelContext
argument_list|()
expr_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|template
operator|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
if|if
condition|(
name|useRouteBuilder
condition|)
block|{
name|RouteBuilder
index|[]
name|builders
init|=
name|createRouteBuilders
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteBuilder
name|builder
range|:
name|builders
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using created route builder: "
operator|+
name|builder
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using route builder from the created context: "
operator|+
name|context
argument_list|)
expr_stmt|;
block|}
name|startCamelContext
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Routing Rules are: "
operator|+
name|context
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"tearDown test: "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
name|stopCamelContext
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|getResults (DirContext ctx, String filter)
specifier|protected
name|Set
argument_list|<
name|SearchResult
argument_list|>
name|getResults
parameter_list|(
name|DirContext
name|ctx
parameter_list|,
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|SearchResult
argument_list|>
name|results
init|=
operator|new
name|HashSet
argument_list|<
name|SearchResult
argument_list|>
argument_list|()
decl_stmt|;
name|SearchControls
name|controls
init|=
operator|new
name|SearchControls
argument_list|()
decl_stmt|;
name|controls
operator|.
name|setSearchScope
argument_list|(
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|)
expr_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|namingEnumeration
init|=
name|ctx
operator|.
name|search
argument_list|(
name|ServerDNConstants
operator|.
name|SYSTEM_DN
argument_list|,
name|filter
argument_list|,
name|controls
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
name|results
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
name|results
return|;
block|}
DECL|method|contains (String dn, Collection<SearchResult> results)
specifier|protected
name|boolean
name|contains
parameter_list|(
name|String
name|dn
parameter_list|,
name|Collection
argument_list|<
name|SearchResult
argument_list|>
name|results
parameter_list|)
block|{
for|for
control|(
name|SearchResult
name|result
range|:
name|results
control|)
block|{
if|if
condition|(
name|result
operator|.
name|getNameInNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|dn
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|JndiRegistry
argument_list|(
name|createJndiContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|JndiTest
operator|.
name|createInitialContext
argument_list|()
return|;
block|}
DECL|method|stopCamelContext ()
specifier|protected
name|void
name|stopCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContextService
operator|!=
literal|null
condition|)
block|{
name|camelContextService
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|startCamelContext ()
specifier|protected
name|void
name|startCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContextService
operator|!=
literal|null
condition|)
block|{
name|camelContextService
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
name|DefaultCamelContext
name|defaultCamelContext
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
if|if
condition|(
operator|!
name|defaultCamelContext
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|defaultCamelContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|assertValidContext (CamelContext context)
specifier|protected
name|void
name|assertValidContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"No context found!"
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// no routes added by default
block|}
block|}
return|;
block|}
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{
name|createRouteBuilder
argument_list|()
block|}
return|;
block|}
block|}
end_class

end_unit

