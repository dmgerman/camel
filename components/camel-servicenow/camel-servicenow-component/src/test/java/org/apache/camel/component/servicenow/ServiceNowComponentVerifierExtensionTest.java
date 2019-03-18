begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
package|;
end_package

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ProcessingException
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

begin_class
DECL|class|ServiceNowComponentVerifierExtensionTest
specifier|public
class|class
name|ServiceNowComponentVerifierExtensionTest
extends|extends
name|ServiceNowTestSupport
block|{
DECL|method|ServiceNowComponentVerifierExtensionTest ()
specifier|public
name|ServiceNowComponentVerifierExtensionTest
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
DECL|method|getExtension ()
specifier|protected
name|ComponentVerifierExtension
name|getExtension
parameter_list|()
block|{
name|Component
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"servicenow"
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
return|return
name|verifier
return|;
block|}
comment|// *********************************
comment|// Parameters validation
comment|// *********************************
annotation|@
name|Test
DECL|method|testParameter ()
specifier|public
name|void
name|testParameter
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|getExtension
argument_list|()
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
DECL|method|testMissingMandatoryParameter ()
specifier|public
name|void
name|testMissingMandatoryParameter
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"instanceName"
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|getExtension
argument_list|()
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
literal|"instanceName"
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
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMissingMandatoryAuthenticationParameter ()
specifier|public
name|void
name|testMissingMandatoryAuthenticationParameter
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"userName"
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|getExtension
argument_list|()
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
literal|"userName"
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
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// *********************************
comment|// Connectivity validation
comment|// *********************************
annotation|@
name|Test
DECL|method|testConnectivity ()
specifier|public
name|void
name|testConnectivity
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|getExtension
argument_list|()
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
DECL|method|testConnectivityOnCustomTable ()
specifier|public
name|void
name|testConnectivityOnCustomTable
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"table"
argument_list|,
literal|"ticket"
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|getExtension
argument_list|()
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
DECL|method|testConnectivityWithWrongInstance ()
specifier|public
name|void
name|testConnectivityWithWrongInstance
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"instanceName"
argument_list|,
literal|"unknown-instance"
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|getExtension
argument_list|()
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
name|assertNotNull
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
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_INSTANCE
argument_list|)
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
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_INSTANCE
argument_list|)
operator|instanceof
name|ProcessingException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectivityWithWrongTable ()
specifier|public
name|void
name|testConnectivityWithWrongTable
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"table"
argument_list|,
literal|"unknown"
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|getExtension
argument_list|()
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
name|assertNotNull
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
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|400
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
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_INSTANCE
argument_list|)
operator|instanceof
name|ServiceNowException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectivityWithWrongAuthentication ()
specifier|public
name|void
name|testConnectivityWithWrongAuthentication
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|getParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"userName"
argument_list|,
literal|"unknown-user"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"oauthClientId"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"oauthClientSecret"
argument_list|)
expr_stmt|;
name|ComponentVerifierExtension
operator|.
name|Result
name|result
init|=
name|getExtension
argument_list|()
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
name|assertNotNull
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
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|401
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
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_INSTANCE
argument_list|)
operator|instanceof
name|ServiceNowException
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
literal|"userName"
argument_list|)
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
literal|"password"
argument_list|)
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
literal|"oauthClientId"
argument_list|)
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
literal|"oauthClientSecret"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

