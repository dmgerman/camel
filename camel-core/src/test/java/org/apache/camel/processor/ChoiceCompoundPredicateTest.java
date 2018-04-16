begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|Predicate
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|PredicateBuilder
operator|.
name|and
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|PredicateBuilder
operator|.
name|or
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ChoiceCompoundPredicateTest
specifier|public
class|class
name|ChoiceCompoundPredicateTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testGuest ()
specifier|public
name|void
name|testGuest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:guest"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testUser ()
specifier|public
name|void
name|testUser
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:user"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"username"
argument_list|,
literal|"goofy"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testAdmin ()
specifier|public
name|void
name|testAdmin
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:admin"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
literal|"donald"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"admin"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testGod ()
specifier|public
name|void
name|testGod
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:god"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
literal|"pluto"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"admin"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"god"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRiderGod ()
specifier|public
name|void
name|testRiderGod
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:god"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"admin"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel Rider"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
comment|// START SNIPPET: e1
comment|// We define 3 predicates based on some user roles
comment|// we have static imported and/or from org.apache.camel.builder.PredicateBuilder
comment|// First we have a regular user that is just identified having a username header
name|Predicate
name|user
init|=
name|header
argument_list|(
literal|"username"
argument_list|)
operator|.
name|isNotNull
argument_list|()
decl_stmt|;
comment|// The admin user must be a user AND have a admin header as true
name|Predicate
name|admin
init|=
name|and
argument_list|(
name|user
argument_list|,
name|header
argument_list|(
literal|"admin"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"true"
argument_list|)
argument_list|)
decl_stmt|;
comment|// And God must be an admin and (either have type god or a special message containing Camel Rider)
name|Predicate
name|god
init|=
name|and
argument_list|(
name|admin
argument_list|,
name|or
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel Rider"
argument_list|)
argument_list|,
name|header
argument_list|(
literal|"type"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"god"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// As you can see with the predicates above we can stack them to build compound predicates
comment|// In our route below we can create a nice content based router based on the predicates we
comment|// have defined. Then the route is easy to read and understand.
comment|// We encourage you to define complex predicates outside the fluent router builder as
comment|// it will just get a bit complex for humans to read
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|god
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:god"
argument_list|)
operator|.
name|when
argument_list|(
name|admin
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:admin"
argument_list|)
operator|.
name|when
argument_list|(
name|user
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:user"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:guest"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

