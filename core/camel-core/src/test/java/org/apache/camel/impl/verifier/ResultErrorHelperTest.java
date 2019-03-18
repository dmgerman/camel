begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|verifier
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
name|List
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|extension
operator|.
name|ComponentVerifierExtension
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
name|component
operator|.
name|extension
operator|.
name|ComponentVerifierExtension
operator|.
name|VerificationError
operator|.
name|StandardCode
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
name|verifier
operator|.
name|OptionsGroup
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
name|verifier
operator|.
name|ResultErrorHelper
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|ResultErrorHelperTest
specifier|public
class|class
name|ResultErrorHelperTest
block|{
DECL|field|groups
name|OptionsGroup
index|[]
name|groups
init|=
operator|new
name|OptionsGroup
index|[]
block|{
name|OptionsGroup
operator|.
name|withName
argument_list|(
literal|"optionA"
argument_list|)
operator|.
name|options
argument_list|(
literal|"param1"
argument_list|,
literal|"param2"
argument_list|,
literal|"!param3"
argument_list|)
block|,
name|OptionsGroup
operator|.
name|withName
argument_list|(
literal|"optionB"
argument_list|)
operator|.
name|options
argument_list|(
literal|"param1"
argument_list|,
literal|"!param2"
argument_list|,
literal|"param3"
argument_list|)
block|,
name|OptionsGroup
operator|.
name|withName
argument_list|(
literal|"optionC"
argument_list|)
operator|.
name|options
argument_list|(
literal|"!param1"
argument_list|,
literal|"!param2"
argument_list|,
literal|"param4"
argument_list|)
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldValidateCorrectParameters ()
specifier|public
name|void
name|shouldValidateCorrectParameters
parameter_list|()
block|{
comment|// just giving param1 and param2 is OK
name|assertTrue
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresAny
argument_list|(
name|map
argument_list|(
literal|"param1"
argument_list|,
literal|"param2"
argument_list|)
argument_list|,
name|groups
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// just giving param1 and param3 is OK
name|assertTrue
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresAny
argument_list|(
name|map
argument_list|(
literal|"param1"
argument_list|,
literal|"param3"
argument_list|)
argument_list|,
name|groups
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// just giving param4 is OK
name|assertTrue
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresAny
argument_list|(
name|map
argument_list|(
literal|"param4"
argument_list|)
argument_list|,
name|groups
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldValidateParameterExclusions ()
specifier|public
name|void
name|shouldValidateParameterExclusions
parameter_list|()
block|{
comment|// combining param2 and param3 is not OK
specifier|final
name|List
argument_list|<
name|ComponentVerifierExtension
operator|.
name|VerificationError
argument_list|>
name|results
init|=
name|ResultErrorHelper
operator|.
name|requiresAny
argument_list|(
name|map
argument_list|(
literal|"param1"
argument_list|,
literal|"param2"
argument_list|,
literal|"param3"
argument_list|)
argument_list|,
name|groups
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|VerificationError
name|param3Error
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|StandardCode
operator|.
name|ILLEGAL_PARAMETER_GROUP_COMBINATION
argument_list|,
name|param3Error
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"optionA"
argument_list|,
name|param3Error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|GroupAttribute
operator|.
name|GROUP_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"param1,param2,param3"
argument_list|,
name|param3Error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|GroupAttribute
operator|.
name|GROUP_OPTIONS
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|VerificationError
name|param2Error
init|=
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|StandardCode
operator|.
name|ILLEGAL_PARAMETER_GROUP_COMBINATION
argument_list|,
name|param2Error
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"optionB"
argument_list|,
name|param2Error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|GroupAttribute
operator|.
name|GROUP_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"param1,param2,param3"
argument_list|,
name|param2Error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|GroupAttribute
operator|.
name|GROUP_OPTIONS
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|VerificationError
name|param4Error
init|=
name|results
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|StandardCode
operator|.
name|ILLEGAL_PARAMETER_GROUP_COMBINATION
argument_list|,
name|param4Error
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"optionC"
argument_list|,
name|param4Error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|GroupAttribute
operator|.
name|GROUP_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"param1,param2,param4"
argument_list|,
name|param4Error
operator|.
name|getDetail
argument_list|(
name|VerificationError
operator|.
name|GroupAttribute
operator|.
name|GROUP_OPTIONS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|map (final String... params)
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|(
specifier|final
name|String
modifier|...
name|params
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|stream
argument_list|(
name|params
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|e
lambda|->
name|e
argument_list|,
name|e
lambda|->
literal|"value"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

