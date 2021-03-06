begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
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
name|salesforce
operator|.
name|api
operator|.
name|SalesforceException
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
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
DECL|class|SalesforceComponentVerifierExtensionIntegrationTest
specifier|public
class|class
name|SalesforceComponentVerifierExtensionIntegrationTest
extends|extends
name|CamelTestSupport
block|{
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
comment|// *********************************
comment|// Helpers
comment|// *********************************
DECL|method|getParameters ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
name|SalesforceLoginConfig
name|loginConfig
init|=
name|LoginConfigHelper
operator|.
name|getLoginConfig
argument_list|()
decl_stmt|;
name|HashMap
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
literal|"clientId"
argument_list|,
name|loginConfig
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"clientSecret"
argument_list|,
name|loginConfig
operator|.
name|getClientSecret
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"userName"
argument_list|,
name|loginConfig
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
name|loginConfig
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|parameters
return|;
block|}
DECL|method|getSystemPropertyOrEnvVar (String systemProperty)
specifier|public
specifier|static
name|String
name|getSystemPropertyOrEnvVar
parameter_list|(
name|String
name|systemProperty
parameter_list|)
block|{
name|String
name|answer
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|systemProperty
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|answer
argument_list|)
condition|)
block|{
name|String
name|envProperty
init|=
name|systemProperty
operator|.
name|toUpperCase
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"[.-]"
argument_list|,
literal|"_"
argument_list|)
decl_stmt|;
name|answer
operator|=
name|System
operator|.
name|getenv
argument_list|(
name|envProperty
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
literal|"salesforce"
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
DECL|method|testConnectivityWithWrongUserName ()
specifier|public
name|void
name|testConnectivityWithWrongUserName
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
literal|"not-a-salesforce-user"
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
literal|2
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
comment|// Exception
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
name|SalesforceException
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
comment|// Salesforce Error
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"invalid_grant"
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getDetail
argument_list|(
literal|"salesforce_code"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectivityWithWrongSecrets ()
specifier|public
name|void
name|testConnectivityWithWrongSecrets
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
literal|"clientId"
argument_list|,
literal|"wrong-client-id"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"clientSecret"
argument_list|,
literal|"wrong-client-secret"
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
literal|2
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
comment|// Exception
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
name|SalesforceException
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
comment|// Salesforce Error
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"invalid_client_id"
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getDetail
argument_list|(
literal|"salesforce_code"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

