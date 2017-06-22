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
name|directory
operator|.
name|BasicAttribute
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
name|BasicAttributes
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
name|ModificationItem
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
name|Message
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
name|DefaultMessage
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|mockito
operator|.
name|Matchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
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
name|query
operator|.
name|LdapQuery
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|isNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|SpringLdapProducerTest
specifier|public
class|class
name|SpringLdapProducerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|ldapEndpoint
specifier|private
name|SpringLdapEndpoint
name|ldapEndpoint
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|SpringLdapEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ldapTemplate
specifier|private
name|LdapTemplate
name|ldapTemplate
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|LdapTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ldapProducer
specifier|private
name|SpringLdapProducer
name|ldapProducer
init|=
operator|new
name|SpringLdapProducer
argument_list|(
name|ldapEndpoint
argument_list|)
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
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
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|getLdapTemplate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ldapTemplate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testEmptyExchange ()
specifier|public
name|void
name|testEmptyExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ldapProducer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testWrongBodyType ()
specifier|public
name|void
name|testWrongBodyType
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|ldapProducer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testNoDN ()
specifier|public
name|void
name|testNoDN
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoDNForFunctionDrivenOperation ()
specifier|public
name|void
name|testNoDNForFunctionDrivenOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|FUNCTION
argument_list|,
name|Mockito
operator|.
name|mock
argument_list|(
name|BiFunction
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|LdapOperation
operator|.
name|FUNCTION_DRIVEN
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|processBody (Exchange exchange, Message message, Map<String, Object> body)
specifier|private
name|void
name|processBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|ldapProducer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testEmptyDN ()
specifier|public
name|void
name|testEmptyDN
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testNullDN ()
specifier|public
name|void
name|testNullDN
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testNullOperation ()
specifier|public
name|void
name|testNullOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSearch ()
specifier|public
name|void
name|testSearch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dn
init|=
literal|"some dn"
decl_stmt|;
name|String
name|filter
init|=
literal|"filter"
decl_stmt|;
name|Integer
name|scope
init|=
name|SearchControls
operator|.
name|SUBTREE_SCOPE
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
name|dn
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|FILTER
argument_list|,
name|filter
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|LdapOperation
operator|.
name|SEARCH
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|scopeValue
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|scope
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|ldapTemplate
argument_list|)
operator|.
name|search
argument_list|(
name|eq
argument_list|(
name|dn
argument_list|)
argument_list|,
name|eq
argument_list|(
name|filter
argument_list|)
argument_list|,
name|eq
argument_list|(
name|scope
argument_list|)
argument_list|,
name|any
argument_list|(
name|AttributesMapper
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBind ()
specifier|public
name|void
name|testBind
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dn
init|=
literal|"some dn"
decl_stmt|;
name|BasicAttributes
name|attributes
init|=
operator|new
name|BasicAttributes
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
name|dn
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|ATTRIBUTES
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|LdapOperation
operator|.
name|BIND
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|ldapTemplate
argument_list|)
operator|.
name|bind
argument_list|(
name|eq
argument_list|(
name|dn
argument_list|)
argument_list|,
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
name|attributes
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnbind ()
specifier|public
name|void
name|testUnbind
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dn
init|=
literal|"some dn"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
name|dn
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|LdapOperation
operator|.
name|UNBIND
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|ldapTemplate
argument_list|)
operator|.
name|unbind
argument_list|(
name|eq
argument_list|(
name|dn
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAuthenticate ()
specifier|public
name|void
name|testAuthenticate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dn
init|=
literal|"cn=dn"
decl_stmt|;
name|String
name|filter
init|=
literal|"filter"
decl_stmt|;
name|String
name|password
init|=
literal|"password"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
name|dn
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|FILTER
argument_list|,
name|filter
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|PASSWORD
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|LdapOperation
operator|.
name|AUTHENTICATE
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|ldapTemplate
argument_list|)
operator|.
name|authenticate
argument_list|(
name|Matchers
operator|.
name|any
argument_list|(
name|LdapQuery
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
name|password
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testModifyAttributes ()
specifier|public
name|void
name|testModifyAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dn
init|=
literal|"cn=dn"
decl_stmt|;
name|ModificationItem
index|[]
name|modificationItems
init|=
operator|new
name|ModificationItem
index|[]
block|{
operator|new
name|ModificationItem
argument_list|(
name|DirContext
operator|.
name|ADD_ATTRIBUTE
argument_list|,
operator|new
name|BasicAttribute
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
argument_list|)
block|}
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
name|dn
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|MODIFICATION_ITEMS
argument_list|,
name|modificationItems
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|LdapOperation
operator|.
name|MODIFY_ATTRIBUTES
argument_list|)
expr_stmt|;
name|processBody
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|ldapTemplate
argument_list|)
operator|.
name|modifyAttributes
argument_list|(
name|eq
argument_list|(
name|dn
argument_list|)
argument_list|,
name|eq
argument_list|(
name|modificationItems
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFunctionDriven ()
specifier|public
name|void
name|testFunctionDriven
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dn
init|=
literal|"cn=dn"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|DN
argument_list|,
name|dn
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|REQUEST
argument_list|,
name|dn
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
name|SpringLdapProducer
operator|.
name|FUNCTION
argument_list|,
call|(
name|BiFunction
argument_list|<
name|LdapOperations
argument_list|,
name|String
argument_list|,
name|Void
argument_list|>
call|)
argument_list|(
name|l
argument_list|,
name|q
argument_list|)
operator|->
block|{
name|l
operator|.
name|lookup
argument_list|(
name|q
argument_list|)
block|;
return|return
literal|null
return|;
block|}
block|)
function|;
name|when
argument_list|(
name|ldapEndpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|LdapOperation
operator|.
name|FUNCTION_DRIVEN
argument_list|)
expr_stmt|;
name|processBody
parameter_list|(
name|exchange
parameter_list|,
name|in
parameter_list|,
name|body
parameter_list|)
constructor_decl|;
name|verify
argument_list|(
name|ldapTemplate
argument_list|)
operator|.
name|lookup
argument_list|(
name|eq
argument_list|(
name|dn
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_class

unit|}
end_unit

