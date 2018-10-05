begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.iam
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
name|iam
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|identitymanagement
operator|.
name|model
operator|.
name|CreateAccessKeyResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|identitymanagement
operator|.
name|model
operator|.
name|CreateUserResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|identitymanagement
operator|.
name|model
operator|.
name|DeleteAccessKeyResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|identitymanagement
operator|.
name|model
operator|.
name|DeleteUserResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|identitymanagement
operator|.
name|model
operator|.
name|GetUserResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|identitymanagement
operator|.
name|model
operator|.
name|ListAccessKeysResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|identitymanagement
operator|.
name|model
operator|.
name|ListUsersResult
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
name|Test
import|;
end_import

begin_class
DECL|class|IAMProducerTest
specifier|public
class|class
name|IAMProducerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Test
DECL|method|iamListKeysTest ()
specifier|public
name|void
name|iamListKeysTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:listKeys"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|OPERATION
argument_list|,
name|IAMOperations
operator|.
name|listAccessKeys
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|ListAccessKeysResult
name|resultGet
init|=
operator|(
name|ListAccessKeysResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resultGet
operator|.
name|getAccessKeyMetadata
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|resultGet
operator|.
name|getAccessKeyMetadata
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAccessKeyId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|iamCreateUserTest ()
specifier|public
name|void
name|iamCreateUserTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:createUser"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|OPERATION
argument_list|,
name|IAMOperations
operator|.
name|createUser
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|USERNAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|CreateUserResult
name|resultGet
init|=
operator|(
name|CreateUserResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|resultGet
operator|.
name|getUser
argument_list|()
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|iamDeleteUserTest ()
specifier|public
name|void
name|iamDeleteUserTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:deleteUser"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|OPERATION
argument_list|,
name|IAMOperations
operator|.
name|deleteUser
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|USERNAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|DeleteUserResult
name|resultGet
init|=
operator|(
name|DeleteUserResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultGet
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|iamGetUserTest ()
specifier|public
name|void
name|iamGetUserTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:getUser"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|OPERATION
argument_list|,
name|IAMOperations
operator|.
name|getUser
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|USERNAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|GetUserResult
name|resultGet
init|=
operator|(
name|GetUserResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|resultGet
operator|.
name|getUser
argument_list|()
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|iamListUsersTest ()
specifier|public
name|void
name|iamListUsersTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:listUsers"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|OPERATION
argument_list|,
name|IAMOperations
operator|.
name|listUsers
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|ListUsersResult
name|resultGet
init|=
operator|(
name|ListUsersResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resultGet
operator|.
name|getUsers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|resultGet
operator|.
name|getUsers
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|iamCreateAccessKeyTest ()
specifier|public
name|void
name|iamCreateAccessKeyTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:createAccessKey"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|OPERATION
argument_list|,
name|IAMOperations
operator|.
name|createAccessKey
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|USERNAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|CreateAccessKeyResult
name|resultGet
init|=
operator|(
name|CreateAccessKeyResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|resultGet
operator|.
name|getAccessKey
argument_list|()
operator|.
name|getAccessKeyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testSecret"
argument_list|,
name|resultGet
operator|.
name|getAccessKey
argument_list|()
operator|.
name|getSecretAccessKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|iamDeleteAccessKeyTest ()
specifier|public
name|void
name|iamDeleteAccessKeyTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:deleteAccessKey"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|OPERATION
argument_list|,
name|IAMOperations
operator|.
name|deleteAccessKey
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|IAMConstants
operator|.
name|USERNAME
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|DeleteAccessKeyResult
name|resultGet
init|=
operator|(
name|DeleteAccessKeyResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultGet
argument_list|)
expr_stmt|;
block|}
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
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|AmazonIAMClientMock
name|clientMock
init|=
operator|new
name|AmazonIAMClientMock
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"amazonIAMClient"
argument_list|,
name|clientMock
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
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
literal|"direct:listKeys"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-iam://test?iamClient=#amazonIAMClient&operation=listAccessKeys"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:createUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-iam://test?iamClient=#amazonIAMClient&operation=createUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deleteUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-iam://test?iamClient=#amazonIAMClient&operation=deleteUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:listUsers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-iam://test?iamClient=#amazonIAMClient&operation=listUsers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-iam://test?iamClient=#amazonIAMClient&operation=getUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:createAccessKey"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-iam://test?iamClient=#amazonIAMClient&operation=createAccessKey"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deleteAccessKey"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-iam://test?iamClient=#amazonIAMClient&operation=deleteAccessKey"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

