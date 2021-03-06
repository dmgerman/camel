begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.search
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|search
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|twitter
operator|.
name|AbstractComponentVerifierExtensionTest
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
DECL|class|TwitterSearchComponentVerifierExtensionTest
specifier|public
class|class
name|TwitterSearchComponentVerifierExtensionTest
extends|extends
name|AbstractComponentVerifierExtensionTest
block|{
annotation|@
name|Override
DECL|method|getComponentScheme ()
specifier|protected
name|String
name|getComponentScheme
parameter_list|()
block|{
return|return
literal|"twitter-search"
return|;
block|}
annotation|@
name|Test
DECL|method|testEmptyConfiguration ()
specifier|public
name|void
name|testEmptyConfiguration
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
name|getComponentScheme
argument_list|()
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
block|{
comment|// Parameters validation
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
name|Collections
operator|.
name|emptyMap
argument_list|()
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
literal|5
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
name|List
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"keywords"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"consumerKey"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"consumerSecret"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"accessToken"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"accessTokenSecret"
argument_list|)
expr_stmt|;
for|for
control|(
name|ComponentVerifierExtension
operator|.
name|VerificationError
name|error
range|:
name|result
operator|.
name|getErrors
argument_list|()
control|)
block|{
name|expected
operator|.
name|removeAll
argument_list|(
name|error
operator|.
name|getParameterKeys
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Missing expected params: "
operator|+
name|expected
operator|.
name|toString
argument_list|()
argument_list|,
name|expected
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|{
comment|// Connectivity validation
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
name|Collections
operator|.
name|emptyMap
argument_list|()
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
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

