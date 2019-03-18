begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|util
operator|.
name|Arrays
import|;
end_import

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
name|Component
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
name|extension
operator|.
name|ComponentVerifierExtension
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
name|http4
operator|.
name|handler
operator|.
name|AuthenticationValidationHandler
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
name|http4
operator|.
name|handler
operator|.
name|BasicValidationHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|bootstrap
operator|.
name|HttpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|bootstrap
operator|.
name|ServerBootstrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|localserver
operator|.
name|RequestBasicAuth
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|localserver
operator|.
name|ResponseBasicUnauthorized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpRequestHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|ImmutableHttpProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|ResponseContent
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
name|Assert
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

begin_class
DECL|class|CamelComponentVerifierExtensionTest
specifier|public
class|class
name|CamelComponentVerifierExtensionTest
extends|extends
name|BaseHttpTest
block|{
DECL|field|AUTH_USERNAME
specifier|private
specifier|static
specifier|final
name|String
name|AUTH_USERNAME
init|=
literal|"camel"
decl_stmt|;
DECL|field|AUTH_PASSWORD
specifier|private
specifier|static
specifier|final
name|String
name|AUTH_PASSWORD
init|=
literal|"password"
decl_stmt|;
DECL|field|localServer
specifier|private
name|HttpServer
name|localServer
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|=
name|ServerBootstrap
operator|.
name|bootstrap
argument_list|()
operator|.
name|setHttpProcessor
argument_list|(
name|getHttpProcessor
argument_list|()
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"/basic"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"/auth"
argument_list|,
operator|new
name|AuthenticationValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|,
name|AUTH_USERNAME
argument_list|,
name|AUTH_PASSWORD
argument_list|)
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"/redirect"
argument_list|,
name|redirectTo
argument_list|(
name|HttpStatus
operator|.
name|SC_MOVED_PERMANENTLY
argument_list|,
literal|"/redirected"
argument_list|)
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"/redirected"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|localServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|localServer
operator|!=
literal|null
condition|)
block|{
name|localServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getHttpProcessor ()
specifier|private
name|HttpProcessor
name|getHttpProcessor
parameter_list|()
block|{
return|return
operator|new
name|ImmutableHttpProcessor
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|RequestBasicAuth
argument_list|()
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ResponseContent
argument_list|()
argument_list|,
operator|new
name|ResponseBasicUnauthorized
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|// *************************************************
comment|// Helpers
comment|// *************************************************
DECL|method|getLocalServerUri (String contextPath)
specifier|protected
name|String
name|getLocalServerUri
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"http://"
argument_list|)
operator|.
name|append
argument_list|(
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
operator|.
name|append
argument_list|(
name|localServer
operator|.
name|getLocalPort
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|contextPath
operator|!=
literal|null
condition|?
name|contextPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|contextPath
else|:
literal|"/"
operator|+
name|contextPath
else|:
literal|""
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|redirectTo (int code, String path)
specifier|private
name|HttpRequestHandler
name|redirectTo
parameter_list|(
name|int
name|code
parameter_list|,
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|HttpRequestHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|HttpResponse
name|response
parameter_list|,
name|HttpContext
name|context
parameter_list|)
throws|throws
name|HttpException
throws|,
name|IOException
block|{
name|response
operator|.
name|setHeader
argument_list|(
literal|"location"
argument_list|,
name|getLocalServerUri
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|response
operator|.
name|setStatusCode
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// *************************************************
comment|// Tests (parameters)
comment|// *************************************************
annotation|@
name|Test
DECL|method|testParameters ()
specifier|public
name|void
name|testParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|)
decl_stmt|;
name|ComponentVerifierExtension
name|verifier
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalStateException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
name|getLocalServerUri
argument_list|(
literal|"/basic"
argument_list|)
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
operator|.
name|PARAMETERS
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMissingMandatoryParameters ()
specifier|public
name|void
name|testMissingMandatoryParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|)
decl_stmt|;
name|ComponentVerifierExtension
name|verifier
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalStateException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
operator|.
name|PARAMETERS
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Result
operator|.
name|Status
operator|.
name|ERROR
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|VerificationError
name|error
init|=
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|StandardCode
operator|.
name|MISSING_PARAMETER
argument_list|,
name|error
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|error
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|contains
argument_list|(
literal|"httpUri"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************
comment|// Tests (connectivity)
comment|// *************************************************
annotation|@
name|Test
DECL|method|testConnectivity ()
specifier|public
name|void
name|testConnectivity
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|)
decl_stmt|;
name|ComponentVerifierExtension
name|verifier
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalStateException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
name|getLocalServerUri
argument_list|(
literal|"/basic"
argument_list|)
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectivityWithWrongUri ()
specifier|public
name|void
name|testConnectivityWithWrongUri
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|)
decl_stmt|;
name|ComponentVerifierExtension
name|verifier
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalStateException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
literal|"http://www.not-existing-uri.unknown"
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Result
operator|.
name|Status
operator|.
name|ERROR
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|VerificationError
name|error
init|=
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|StandardCode
operator|.
name|EXCEPTION
argument_list|,
name|error
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|error
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|contains
argument_list|(
literal|"httpUri"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectivityWithAuthentication ()
specifier|public
name|void
name|testConnectivityWithAuthentication
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|)
decl_stmt|;
name|ComponentVerifierExtension
name|verifier
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalStateException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
name|getLocalServerUri
argument_list|(
literal|"/auth"
argument_list|)
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"authUsername"
argument_list|,
name|AUTH_USERNAME
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"authPassword"
argument_list|,
name|AUTH_PASSWORD
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectivityWithWrongAuthenticationData ()
specifier|public
name|void
name|testConnectivityWithWrongAuthenticationData
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|)
decl_stmt|;
name|ComponentVerifierExtension
name|verifier
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalStateException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
name|getLocalServerUri
argument_list|(
literal|"/auth"
argument_list|)
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"authUsername"
argument_list|,
literal|"unknown"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"authPassword"
argument_list|,
name|AUTH_PASSWORD
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Result
operator|.
name|Status
operator|.
name|ERROR
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|VerificationError
name|error
init|=
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|StandardCode
operator|.
name|AUTHENTICATION
argument_list|,
name|error
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|error
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|error
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|contains
argument_list|(
literal|"authUsername"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|error
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|contains
argument_list|(
literal|"authPassword"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectivityWithRedirect ()
specifier|public
name|void
name|testConnectivityWithRedirect
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|)
decl_stmt|;
name|ComponentVerifierExtension
name|verifier
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalStateException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
name|getLocalServerUri
argument_list|(
literal|"/redirect"
argument_list|)
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectivityWithRedirectDisabled ()
specifier|public
name|void
name|testConnectivityWithRedirectDisabled
parameter_list|()
throws|throws
name|Exception
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|)
decl_stmt|;
name|ComponentVerifierExtension
name|verifier
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|IllegalStateException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
name|getLocalServerUri
argument_list|(
literal|"/redirect"
argument_list|)
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"httpClient.redirectsEnabled"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|Result
operator|.
name|Status
operator|.
name|ERROR
argument_list|,
name|result
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|VerificationError
name|error
init|=
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|StandardCode
operator|.
name|GENERIC
argument_list|,
name|error
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|getLocalServerUri
argument_list|(
literal|"/redirected"
argument_list|)
argument_list|,
name|error
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_REDIRECT
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|error
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|contains
argument_list|(
literal|"httpUri"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

