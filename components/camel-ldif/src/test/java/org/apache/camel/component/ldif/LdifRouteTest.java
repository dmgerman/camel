begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|Attribute
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
name|support
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|getWiredConnection
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|not
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|notNullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
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
DECL|class|LdifRouteTest
specifier|public
class|class
name|LdifRouteTest
extends|extends
name|AbstractLdapTestUnit
block|{
comment|// Constants
DECL|field|LDAP_CONN_NAME
specifier|private
specifier|static
specifier|final
name|String
name|LDAP_CONN_NAME
init|=
literal|"conn"
decl_stmt|;
DECL|field|ENDPOINT_LDIF
specifier|private
specifier|static
specifier|final
name|String
name|ENDPOINT_LDIF
init|=
literal|"ldif:"
operator|+
name|LDAP_CONN_NAME
decl_stmt|;
DECL|field|ENDPOINT_START
specifier|private
specifier|static
specifier|final
name|String
name|ENDPOINT_START
init|=
literal|"direct:start"
decl_stmt|;
DECL|field|SEARCH_CONTROLS
specifier|private
specifier|static
specifier|final
name|SearchControls
name|SEARCH_CONTROLS
init|=
operator|new
name|SearchControls
argument_list|(
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Properties
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
DECL|field|ldapContext
specifier|private
name|LdapContext
name|ldapContext
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the LDAPConnection
name|ldapContext
operator|=
name|getWiredContext
argument_list|(
name|ldapServer
argument_list|)
expr_stmt|;
name|SimpleRegistry
name|reg
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|bind
argument_list|(
name|LDAP_CONN_NAME
argument_list|,
name|getWiredConnection
argument_list|(
name|ldapServer
argument_list|)
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
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|camel
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addOne ()
specifier|public
name|void
name|addOne
parameter_list|()
throws|throws
name|Exception
block|{
name|camel
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|(
name|ENDPOINT_LDIF
argument_list|)
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
name|ENDPOINT_START
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
name|URL
name|loc
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/camel/component/ldif/AddOne.ldif"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|loc
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response
comment|// from Camel
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
comment|// Check the results
name|List
argument_list|<
name|String
argument_list|>
name|ldifResults
init|=
name|defaultLdapModuleOutAssertions
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ldifResults
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Container and user
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check LDAP
name|SearchResult
name|sr
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|searchResults
init|=
name|ldapContext
operator|.
name|search
argument_list|(
literal|""
argument_list|,
literal|"(uid=test*)"
argument_list|,
name|SEARCH_CONTROLS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|searchResults
argument_list|)
expr_stmt|;
name|sr
operator|=
name|searchResults
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"uid=test1,ou=test,ou=system"
argument_list|,
name|equalTo
argument_list|(
name|sr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|false
argument_list|,
name|equalTo
argument_list|(
name|searchResults
operator|.
name|hasMore
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addOneInline ()
specifier|public
name|void
name|addOneInline
parameter_list|()
throws|throws
name|Exception
block|{
name|camel
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|(
name|ENDPOINT_LDIF
argument_list|)
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
name|ENDPOINT_START
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
name|URL
name|loc
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/camel/component/ldif/AddOne.ldif"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|readUrl
argument_list|(
name|loc
argument_list|)
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response
comment|// from Camel
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
comment|// Check the results
name|List
argument_list|<
name|String
argument_list|>
name|ldifResults
init|=
name|defaultLdapModuleOutAssertions
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ldifResults
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Container and user
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check LDAP
name|SearchResult
name|sr
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|searchResults
init|=
name|ldapContext
operator|.
name|search
argument_list|(
literal|""
argument_list|,
literal|"(uid=test*)"
argument_list|,
name|SEARCH_CONTROLS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|searchResults
argument_list|)
expr_stmt|;
name|sr
operator|=
name|searchResults
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"uid=test1,ou=test,ou=system"
argument_list|,
name|equalTo
argument_list|(
name|sr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|false
argument_list|,
name|equalTo
argument_list|(
name|searchResults
operator|.
name|hasMore
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|ApplyLdifFiles
argument_list|(
block|{
literal|"org/apache/camel/component/ldif/DeleteOneSetup.ldif"
block|}
argument_list|)
DECL|method|deleteOne ()
specifier|public
name|void
name|deleteOne
parameter_list|()
throws|throws
name|Exception
block|{
name|camel
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|(
name|ENDPOINT_LDIF
argument_list|)
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
name|ENDPOINT_START
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
name|URL
name|loc
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/camel/component/ldif/DeleteOne.ldif"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|loc
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response
comment|// from Camel
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
comment|// Check the results
name|List
argument_list|<
name|String
argument_list|>
name|ldifResults
init|=
name|defaultLdapModuleOutAssertions
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ldifResults
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check LDAP
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|searchResults
init|=
name|ldapContext
operator|.
name|search
argument_list|(
literal|""
argument_list|,
literal|"(uid=test*)"
argument_list|,
name|SEARCH_CONTROLS
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
literal|false
argument_list|,
name|equalTo
argument_list|(
name|searchResults
operator|.
name|hasMore
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|ApplyLdifFiles
argument_list|(
block|{
literal|"org/apache/camel/component/ldif/AddDuplicateSetup.ldif"
block|}
argument_list|)
DECL|method|addDuplicate ()
specifier|public
name|void
name|addDuplicate
parameter_list|()
throws|throws
name|Exception
block|{
name|camel
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|(
name|ENDPOINT_LDIF
argument_list|)
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
name|ENDPOINT_START
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
name|URL
name|loc
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/camel/component/ldif/AddDuplicate.ldif"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|loc
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response
comment|// from Camel
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
comment|// Check the results
name|List
argument_list|<
name|String
argument_list|>
name|ldifResults
init|=
name|defaultLdapModuleOutAssertions
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ldifResults
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|not
argument_list|(
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|ApplyLdifFiles
argument_list|(
block|{
literal|"org/apache/camel/component/ldif/ModifySetup.ldif"
block|}
argument_list|)
DECL|method|modify ()
specifier|public
name|void
name|modify
parameter_list|()
throws|throws
name|Exception
block|{
name|camel
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|(
name|ENDPOINT_LDIF
argument_list|)
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
name|ENDPOINT_START
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
name|URL
name|loc
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/camel/component/ldif/Modify.ldif"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|loc
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response
comment|// from Camel
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
comment|// Check the results
name|List
argument_list|<
name|String
argument_list|>
name|ldifResults
init|=
name|defaultLdapModuleOutAssertions
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ldifResults
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check LDAP
name|SearchResult
name|sr
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|searchResults
init|=
name|ldapContext
operator|.
name|search
argument_list|(
literal|""
argument_list|,
literal|"(uid=test*)"
argument_list|,
name|SEARCH_CONTROLS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|searchResults
argument_list|)
expr_stmt|;
name|sr
operator|=
name|searchResults
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"uid=test4,ou=test,ou=system"
argument_list|,
name|equalTo
argument_list|(
name|sr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check the attributes of the search result
name|Attributes
name|attribs
init|=
name|sr
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|attribs
argument_list|)
expr_stmt|;
name|Attribute
name|attrib
init|=
name|attribs
operator|.
name|get
argument_list|(
literal|"sn"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attribs
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|1
argument_list|,
name|equalTo
argument_list|(
name|attrib
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"5"
argument_list|,
name|equalTo
argument_list|(
name|attrib
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check no more results
name|assertThat
argument_list|(
literal|false
argument_list|,
name|equalTo
argument_list|(
name|searchResults
operator|.
name|hasMore
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|ApplyLdifFiles
argument_list|(
block|{
literal|"org/apache/camel/component/ldif/ModRdnSetup.ldif"
block|}
argument_list|)
DECL|method|modRdn ()
specifier|public
name|void
name|modRdn
parameter_list|()
throws|throws
name|Exception
block|{
name|camel
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|(
name|ENDPOINT_LDIF
argument_list|)
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
name|ENDPOINT_START
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
name|URL
name|loc
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/camel/component/ldif/ModRdn.ldif"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|loc
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response
comment|// from Camel
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
comment|// Check the results
name|List
argument_list|<
name|String
argument_list|>
name|ldifResults
init|=
name|defaultLdapModuleOutAssertions
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ldifResults
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check LDAP
name|SearchResult
name|sr
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|searchResults
init|=
name|ldapContext
operator|.
name|search
argument_list|(
literal|""
argument_list|,
literal|"(uid=test*)"
argument_list|,
name|SEARCH_CONTROLS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|searchResults
argument_list|)
expr_stmt|;
name|sr
operator|=
name|searchResults
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|sr
argument_list|)
expr_stmt|;
comment|// Check the DN
name|assertThat
argument_list|(
literal|"uid=test6,ou=test,ou=system"
argument_list|,
name|equalTo
argument_list|(
name|sr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check no more results
name|assertThat
argument_list|(
literal|false
argument_list|,
name|equalTo
argument_list|(
name|searchResults
operator|.
name|hasMore
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|ApplyLdifFiles
argument_list|(
block|{
literal|"org/apache/camel/component/ldif/ModDnSetup.ldif"
block|}
argument_list|)
DECL|method|modDn ()
specifier|public
name|void
name|modDn
parameter_list|()
throws|throws
name|Exception
block|{
name|camel
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|(
name|ENDPOINT_LDIF
argument_list|)
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
name|ENDPOINT_START
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
name|URL
name|loc
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/camel/component/ldif/ModDn.ldif"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|loc
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// now we send the exchange to the endpoint, and receives the response
comment|// from Camel
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
comment|// Check the results
name|List
argument_list|<
name|String
argument_list|>
name|ldifResults
init|=
name|defaultLdapModuleOutAssertions
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ldifResults
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ldifResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"success"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check LDAP
name|SearchResult
name|sr
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|searchResults
init|=
name|ldapContext
operator|.
name|search
argument_list|(
literal|""
argument_list|,
literal|"(uid=test*)"
argument_list|,
name|SEARCH_CONTROLS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|searchResults
argument_list|)
expr_stmt|;
name|sr
operator|=
name|searchResults
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|sr
argument_list|)
expr_stmt|;
comment|// Check the DN
name|assertThat
argument_list|(
literal|"uid=test7,ou=testnew,ou=system"
argument_list|,
name|equalTo
argument_list|(
name|sr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check no more results
name|assertThat
argument_list|(
literal|false
argument_list|,
name|equalTo
argument_list|(
name|searchResults
operator|.
name|hasMore
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|defaultLdapModuleOutAssertions (Exchange out)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|defaultLdapModuleOutAssertions
parameter_list|(
name|Exchange
name|out
parameter_list|)
block|{
comment|// assertions of the response
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
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
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"out body could not be converted to a List - was: "
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
return|return
name|data
return|;
block|}
DECL|method|createRouteBuilder (final String ldapEndpointUrl)
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|(
specifier|final
name|String
name|ldapEndpointUrl
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
comment|// START SNIPPET: route
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|ENDPOINT_START
argument_list|)
operator|.
name|to
argument_list|(
name|ldapEndpointUrl
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: route
block|}
return|;
block|}
comment|/**      * Read the contents of a URL into a String      * @param in      * @return      * @throws IOException      */
DECL|method|readUrl (URL in)
specifier|private
name|String
name|readUrl
parameter_list|(
name|URL
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|s
decl_stmt|;
while|while
condition|(
literal|null
operator|!=
operator|(
name|s
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

