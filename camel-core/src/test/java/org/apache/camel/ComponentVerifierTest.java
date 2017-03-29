begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

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
name|ComponentVerifier
operator|.
name|VerificationError
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
name|verifier
operator|.
name|ResultErrorBuilder
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

begin_class
DECL|class|ComponentVerifierTest
specifier|public
class|class
name|ComponentVerifierTest
extends|extends
name|TestCase
block|{
DECL|method|testGetErrorDetails ()
specifier|public
name|void
name|testGetErrorDetails
parameter_list|()
block|{
name|VerificationError
name|error
init|=
name|ResultErrorBuilder
operator|.
name|withCodeAndDescription
argument_list|(
name|VerificationError
operator|.
name|asCode
argument_list|(
literal|"test_code"
argument_list|)
argument_list|,
literal|"test error desc"
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|asAttribute
argument_list|(
literal|"test_attr_1"
argument_list|)
argument_list|,
literal|"test_detail_1"
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_CODE
argument_list|,
literal|"test_detail_2"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"test_detail_1"
argument_list|,
name|error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|asAttribute
argument_list|(
literal|"test_attr_1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"test_detail_1"
argument_list|,
name|error
operator|.
name|getDetail
argument_list|(
literal|"test_attr_1"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"test_detail_2"
argument_list|,
name|error
operator|.
name|getDetail
argument_list|(
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
name|assertNull
argument_list|(
name|error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_TEXT
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|asAttribute
argument_list|(
literal|"test_attr_non_existant"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidAttribute ()
specifier|public
name|void
name|testInvalidAttribute
parameter_list|()
block|{
try|try
block|{
name|VerificationError
operator|.
name|asAttribute
argument_list|(
literal|"HTTP_CODE"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|exp
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|exp
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"HTTP_CODE"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testInvalidCode ()
specifier|public
name|void
name|testInvalidCode
parameter_list|()
block|{
try|try
block|{
name|VerificationError
operator|.
name|asCode
argument_list|(
literal|"Authentication"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|exp
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|exp
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Authentication"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNullCode ()
specifier|public
name|void
name|testNullCode
parameter_list|()
block|{
try|try
block|{
name|VerificationError
operator|.
name|asCode
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Code must not be null"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|exp
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|exp
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"null"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNullAttribute ()
specifier|public
name|void
name|testNullAttribute
parameter_list|()
block|{
try|try
block|{
name|VerificationError
operator|.
name|asAttribute
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Attribute must not be null"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|exp
parameter_list|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|exp
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"null"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testScopeFromString ()
specifier|public
name|void
name|testScopeFromString
parameter_list|()
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|PARAMETERS
argument_list|,
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|fromString
argument_list|(
literal|"PaRaMeTeRS"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|fromString
argument_list|(
literal|"unknown"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|exp
parameter_list|)
block|{}
block|}
block|}
end_class

end_unit

