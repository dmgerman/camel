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
name|JndiRegistry
import|;
end_import

begin_class
DECL|class|LdapRouteTest
specifier|public
class|class
name|LdapRouteTest
extends|extends
name|LdapTestSupport
block|{
DECL|method|testLdapRoute ()
specifier|public
name|void
name|testLdapRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: invoke
name|Endpoint
name|endpoint
init|=
name|context
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
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: register
name|JndiRegistry
name|reg
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"localhost:"
operator|+
name|port
argument_list|,
name|getWiredContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|reg
return|;
comment|// END SNIPPET: register
block|}
annotation|@
name|Override
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

