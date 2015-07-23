begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

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
name|security
operator|.
name|Principal
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|Processor
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
name|RuntimeCamelException
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
name|http
operator|.
name|common
operator|.
name|HttpOperationFailedException
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
name|eclipse
operator|.
name|jetty
operator|.
name|security
operator|.
name|ConstraintMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|security
operator|.
name|ConstraintSecurityHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|security
operator|.
name|HashLoginService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|security
operator|.
name|SecurityHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|security
operator|.
name|authentication
operator|.
name|BasicAuthenticator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|security
operator|.
name|Constraint
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

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|HttpBasicAuthTest
specifier|public
class|class
name|HttpBasicAuthTest
extends|extends
name|BaseJettyTest
block|{
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
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myAuthHandler"
argument_list|,
name|getSecurityHandler
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|getSecurityHandler ()
specifier|private
name|SecurityHandler
name|getSecurityHandler
parameter_list|()
throws|throws
name|IOException
block|{
name|Constraint
name|constraint
init|=
operator|new
name|Constraint
argument_list|(
name|Constraint
operator|.
name|__BASIC_AUTH
argument_list|,
literal|"user"
argument_list|)
decl_stmt|;
name|constraint
operator|.
name|setAuthenticate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ConstraintMapping
name|cm
init|=
operator|new
name|ConstraintMapping
argument_list|()
decl_stmt|;
name|cm
operator|.
name|setPathSpec
argument_list|(
literal|"/*"
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setConstraint
argument_list|(
name|constraint
argument_list|)
expr_stmt|;
name|ConstraintSecurityHandler
name|sh
init|=
operator|new
name|ConstraintSecurityHandler
argument_list|()
decl_stmt|;
name|sh
operator|.
name|setAuthenticator
argument_list|(
operator|new
name|BasicAuthenticator
argument_list|()
argument_list|)
expr_stmt|;
name|sh
operator|.
name|setConstraintMappings
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ConstraintMapping
index|[]
block|{
name|cm
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|HashLoginService
name|loginService
init|=
operator|new
name|HashLoginService
argument_list|(
literal|"MyRealm"
argument_list|,
literal|"src/test/resources/myRealm.properties"
argument_list|)
decl_stmt|;
name|sh
operator|.
name|setLoginService
argument_list|(
name|loginService
argument_list|)
expr_stmt|;
name|sh
operator|.
name|setConstraintMappings
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ConstraintMapping
index|[]
block|{
name|cm
block|}
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sh
return|;
block|}
annotation|@
name|Test
DECL|method|testHttpBaiscAuth ()
specifier|public
name|void
name|testHttpBaiscAuth
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/test?authMethod=Basic&authUsername=donald&authPassword=duck"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpBasicAuthInvalidPassword ()
specifier|public
name|void
name|testHttpBasicAuthInvalidPassword
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/test?authMethod=Basic&authUsername=donald&authPassword=sorry"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|HttpOperationFailedException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
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
literal|"jetty://http://localhost:{{port}}/test?handlers=myAuthHandler"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
name|HttpServletRequest
name|req
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|Principal
name|user
init|=
name|req
operator|.
name|getUserPrincipal
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"donald"
argument_list|,
name|user
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

