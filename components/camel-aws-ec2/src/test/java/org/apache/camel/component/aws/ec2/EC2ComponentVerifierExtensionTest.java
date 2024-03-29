begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ec2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|ec2
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
DECL|class|EC2ComponentVerifierExtensionTest
specifier|public
class|class
name|EC2ComponentVerifierExtensionTest
extends|extends
name|CamelTestSupport
block|{
comment|// *************************************************
comment|// Tests (parameters)
comment|// *************************************************
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
literal|"aws-ec2"
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
literal|"secretKey"
argument_list|,
literal|"l"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"accessKey"
argument_list|,
literal|"k"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"region"
argument_list|,
literal|"l"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"label"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"operation"
argument_list|,
name|EC2Operations
operator|.
name|describeInstances
operator|.
name|toString
argument_list|()
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
literal|"aws-ec2"
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
literal|"secretKey"
argument_list|,
literal|"l"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"accessKey"
argument_list|,
literal|"k"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"region"
argument_list|,
literal|"US_EAST_1"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"label"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"operation"
argument_list|,
name|EC2Operations
operator|.
name|describeInstances
operator|.
name|toString
argument_list|()
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
block|}
block|}
end_class

end_unit

