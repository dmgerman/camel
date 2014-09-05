begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CamelExecutionException
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
name|EndpointInject
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
name|Produce
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"/org/apache/camel/component/netty4/http/SpringNettyHttpBasicAuthTest.xml"
block|}
argument_list|)
DECL|class|SpringNettyHttpBasicAuthTest
specifier|public
class|class
name|SpringNettyHttpBasicAuthTest
extends|extends
name|TestCase
block|{
annotation|@
name|Produce
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:input"
argument_list|)
DECL|field|mockEndpoint
specifier|private
name|MockEndpoint
name|mockEndpoint
decl_stmt|;
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"dynaPort"
argument_list|)
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
annotation|@
name|BeforeClass
DECL|method|setUpJaas ()
specifier|public
specifier|static
name|void
name|setUpJaas
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.security.auth.login.config"
argument_list|,
literal|"src/test/resources/myjaas.config"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|tearDownJaas ()
specifier|public
specifier|static
name|void
name|tearDownJaas
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"java.security.auth.login.config"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAdminAuth ()
specifier|public
name|void
name|testAdminAuth
parameter_list|()
throws|throws
name|Exception
block|{
name|mockEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Public"
argument_list|,
literal|"Hello Foo"
argument_list|,
literal|"Hello Admin"
argument_list|)
expr_stmt|;
comment|// public do not need authentication
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/foo/public/welcome"
argument_list|,
literal|"Hello Public"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye /foo/public/welcome"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// username:password is scott:secret
name|String
name|auth
init|=
literal|"Basic c2NvdHQ6c2VjcmV0"
decl_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/foo"
argument_list|,
literal|"Hello Foo"
argument_list|,
literal|"Authorization"
argument_list|,
name|auth
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye /foo"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/foo/admin/users"
argument_list|,
literal|"Hello Admin"
argument_list|,
literal|"Authorization"
argument_list|,
name|auth
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye /foo/admin/users"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/foo"
argument_list|,
literal|"Hello Foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should send back 401"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NettyHttpOperationFailedException
name|cause
init|=
operator|(
name|NettyHttpOperationFailedException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testGuestAuth ()
specifier|public
name|void
name|testGuestAuth
parameter_list|()
throws|throws
name|Exception
block|{
comment|// username:password is guest:secret
name|String
name|auth
init|=
literal|"Basic Z3Vlc3Q6c2VjcmV0"
decl_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/foo/guest/hello"
argument_list|,
literal|"Hello Guest"
argument_list|,
literal|"Authorization"
argument_list|,
name|auth
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye /foo/guest/hello"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// but we can access foo as that is any roles
name|out
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/foo"
argument_list|,
literal|"Hello Foo"
argument_list|,
literal|"Authorization"
argument_list|,
name|auth
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye /foo"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// accessing admin is restricted for guest user
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/foo/admin/users"
argument_list|,
literal|"Hello Admin"
argument_list|,
literal|"Authorization"
argument_list|,
name|auth
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should send back 401"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|NettyHttpOperationFailedException
name|cause
init|=
operator|(
name|NettyHttpOperationFailedException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

