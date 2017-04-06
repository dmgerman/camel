begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4.rest
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
operator|.
name|rest
package|;
end_package

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
name|ComponentVerifier
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
name|BaseHttpTest
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
name|camel
operator|.
name|component
operator|.
name|rest
operator|.
name|RestComponent
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
DECL|class|RestCamelComponentVerifierTest
specifier|public
class|class
name|RestCamelComponentVerifierTest
extends|extends
name|BaseHttpTest
block|{
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
literal|"/verify"
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
comment|// *************************************************
comment|// Tests
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
name|RestComponent
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"rest"
argument_list|,
name|RestComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ComponentVerifier
name|verifier
init|=
name|component
operator|.
name|getVerifier
argument_list|()
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
literal|"componentName"
argument_list|,
literal|"http4"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"http://localhost:"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
literal|"verify"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"method"
argument_list|,
literal|"get"
argument_list|)
expr_stmt|;
name|ComponentVerifier
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifier
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
name|ComponentVerifier
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
DECL|method|testMissingRestParameters ()
specifier|public
name|void
name|testMissingRestParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|RestComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"rest"
argument_list|,
name|RestComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ComponentVerifier
name|verifier
init|=
name|component
operator|.
name|getVerifier
argument_list|()
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
literal|"componentName"
argument_list|,
literal|"http4"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"http://localhost:"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
literal|"verify"
argument_list|)
expr_stmt|;
comment|// This parameter does not belong to the rest component and validation
comment|// is delegated to the transport component
name|parameters
operator|.
name|put
argument_list|(
literal|"copyHeaders"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ComponentVerifier
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifier
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
name|ComponentVerifier
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
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|VerificationError
operator|.
name|StandardCode
operator|.
name|MISSING_PARAMETER
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCode
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
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|contains
argument_list|(
literal|"method"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWrongComponentParameters ()
specifier|public
name|void
name|testWrongComponentParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|RestComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"rest"
argument_list|,
name|RestComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ComponentVerifier
name|verifier
init|=
name|component
operator|.
name|getVerifier
argument_list|()
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
literal|"componentName"
argument_list|,
literal|"http4"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"http://localhost:"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
literal|"verify"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"method"
argument_list|,
literal|"get"
argument_list|)
expr_stmt|;
comment|// This parameter does not belong to the rest component and validation
comment|// is delegated to the transport component
name|parameters
operator|.
name|put
argument_list|(
literal|"nonExistingOption"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ComponentVerifier
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifier
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
name|ComponentVerifier
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
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|VerificationError
operator|.
name|StandardCode
operator|.
name|UNKNOWN_PARAMETER
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCode
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
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParameterKeys
argument_list|()
operator|.
name|contains
argument_list|(
literal|"nonExistingOption"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|RestComponent
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"rest"
argument_list|,
name|RestComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ComponentVerifier
name|verifier
init|=
name|component
operator|.
name|getVerifier
argument_list|()
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
literal|"componentName"
argument_list|,
literal|"http4"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"http://localhost:"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
literal|"verify"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"method"
argument_list|,
literal|"get"
argument_list|)
expr_stmt|;
name|ComponentVerifier
operator|.
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|ComponentVerifier
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
name|ComponentVerifier
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
block|}
end_class

end_unit

