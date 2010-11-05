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
name|LdapContext
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
name|Endpoint
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
name|SimpleRegistry
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
name|annotations
operator|.
name|CreateLdapServer
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
name|annotations
operator|.
name|CreateTransport
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
name|core
operator|.
name|annotations
operator|.
name|ApplyLdifFiles
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
name|core
operator|.
name|integ
operator|.
name|AbstractLdapTestUnit
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
name|core
operator|.
name|integ
operator|.
name|FrameworkRunner
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
name|ldap
operator|.
name|LdapServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|integ
operator|.
name|ServerIntegrationUtils
operator|.
name|getWiredContext
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|FrameworkRunner
operator|.
name|class
argument_list|)
annotation|@
name|CreateLdapServer
argument_list|(
name|transports
operator|=
block|{
annotation|@
name|CreateTransport
argument_list|(
name|protocol
operator|=
literal|"LDAP"
argument_list|)
block|}
argument_list|)
DECL|class|LdapRouteTest
specifier|public
class|class
name|LdapRouteTest
extends|extends
name|AbstractLdapTestUnit
block|{
DECL|field|ldapServer
specifier|public
specifier|static
name|LdapServer
name|ldapServer
decl_stmt|;
DECL|field|camel
specifier|private
name|CamelContext
name|camel
decl_stmt|;
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|ApplyLdifFiles
argument_list|(
literal|"org/apache/camel/component/ldap/LdapRouteTest.ldif"
argument_list|)
annotation|@
name|Test
DECL|method|testLdapRoute ()
specifier|public
name|void
name|testLdapRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// you can assign port number in the @CreateTransport annotation
name|port
operator|=
name|ldapServer
operator|.
name|getPort
argument_list|()
expr_stmt|;
name|LdapContext
name|ctx
init|=
name|getWiredContext
argument_list|(
name|ldapServer
argument_list|)
decl_stmt|;
name|SimpleRegistry
name|reg
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|put
argument_list|(
literal|"localhost:"
operator|+
name|port
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|camel
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|reg
argument_list|)
expr_stmt|;
name|template
operator|=
name|camel
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|camel
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// START SNIPPET: invoke
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
literal|"direct:start"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// then we set the LDAP filter on the in body
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"(!(ou=test1))"
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response from Camel
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// assertions of the response
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|SearchResult
argument_list|>
name|data
init|=
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|Collection
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"out body could not be converted to a Collection - was: "
operator|+
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|contains
argument_list|(
literal|"uid=test1,ou=test,ou=system"
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|contains
argument_list|(
literal|"uid=test2,ou=test,ou=system"
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|contains
argument_list|(
literal|"uid=testNoOU,ou=test,ou=system"
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|contains
argument_list|(
literal|"uid=tcruise,ou=actors,ou=system"
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
comment|// START SNIPPET: invoke
name|camel
operator|.
name|stop
argument_list|()
expr_stmt|;
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
comment|// START SNIPPET: route
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ldap:localhost:"
operator|+
name|port
operator|+
literal|"?base=ou=system"
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: route
block|}
return|;
block|}
block|}
end_class

end_unit

