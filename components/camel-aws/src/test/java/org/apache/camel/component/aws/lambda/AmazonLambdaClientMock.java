begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.lambda
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
name|lambda
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonServiceException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonWebServiceRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|ResponseMetadata
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|BasicAWSCredentials
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
name|lambda
operator|.
name|AWSLambdaClient
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
name|lambda
operator|.
name|model
operator|.
name|AddPermissionRequest
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
name|lambda
operator|.
name|model
operator|.
name|AddPermissionResult
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
name|lambda
operator|.
name|model
operator|.
name|CreateAliasRequest
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
name|lambda
operator|.
name|model
operator|.
name|CreateAliasResult
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
name|lambda
operator|.
name|model
operator|.
name|CreateEventSourceMappingRequest
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
name|lambda
operator|.
name|model
operator|.
name|CreateEventSourceMappingResult
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
name|lambda
operator|.
name|model
operator|.
name|CreateFunctionRequest
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
name|lambda
operator|.
name|model
operator|.
name|CreateFunctionResult
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
name|lambda
operator|.
name|model
operator|.
name|DeleteAliasRequest
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
name|lambda
operator|.
name|model
operator|.
name|DeleteAliasResult
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
name|lambda
operator|.
name|model
operator|.
name|DeleteEventSourceMappingRequest
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
name|lambda
operator|.
name|model
operator|.
name|DeleteEventSourceMappingResult
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
name|lambda
operator|.
name|model
operator|.
name|DeleteFunctionRequest
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
name|lambda
operator|.
name|model
operator|.
name|DeleteFunctionResult
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
name|lambda
operator|.
name|model
operator|.
name|FunctionConfiguration
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
name|lambda
operator|.
name|model
operator|.
name|GetAccountSettingsRequest
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
name|lambda
operator|.
name|model
operator|.
name|GetAccountSettingsResult
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
name|lambda
operator|.
name|model
operator|.
name|GetAliasRequest
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
name|lambda
operator|.
name|model
operator|.
name|GetAliasResult
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
name|lambda
operator|.
name|model
operator|.
name|GetEventSourceMappingRequest
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
name|lambda
operator|.
name|model
operator|.
name|GetEventSourceMappingResult
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
name|lambda
operator|.
name|model
operator|.
name|GetFunctionConfigurationRequest
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
name|lambda
operator|.
name|model
operator|.
name|GetFunctionConfigurationResult
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
name|lambda
operator|.
name|model
operator|.
name|GetFunctionRequest
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
name|lambda
operator|.
name|model
operator|.
name|GetFunctionResult
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
name|lambda
operator|.
name|model
operator|.
name|GetPolicyRequest
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
name|lambda
operator|.
name|model
operator|.
name|GetPolicyResult
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
name|lambda
operator|.
name|model
operator|.
name|InvokeAsyncRequest
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
name|lambda
operator|.
name|model
operator|.
name|InvokeAsyncResult
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
name|lambda
operator|.
name|model
operator|.
name|InvokeRequest
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
name|lambda
operator|.
name|model
operator|.
name|InvokeResult
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
name|lambda
operator|.
name|model
operator|.
name|ListAliasesRequest
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
name|lambda
operator|.
name|model
operator|.
name|ListAliasesResult
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
name|lambda
operator|.
name|model
operator|.
name|ListEventSourceMappingsRequest
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
name|lambda
operator|.
name|model
operator|.
name|ListEventSourceMappingsResult
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
name|lambda
operator|.
name|model
operator|.
name|ListFunctionsRequest
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
name|lambda
operator|.
name|model
operator|.
name|ListFunctionsResult
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
name|lambda
operator|.
name|model
operator|.
name|ListTagsRequest
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
name|lambda
operator|.
name|model
operator|.
name|ListTagsResult
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
name|lambda
operator|.
name|model
operator|.
name|ListVersionsByFunctionRequest
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
name|lambda
operator|.
name|model
operator|.
name|ListVersionsByFunctionResult
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
name|lambda
operator|.
name|model
operator|.
name|PublishVersionRequest
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
name|lambda
operator|.
name|model
operator|.
name|PublishVersionResult
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
name|lambda
operator|.
name|model
operator|.
name|RemovePermissionRequest
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
name|lambda
operator|.
name|model
operator|.
name|RemovePermissionResult
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
name|lambda
operator|.
name|model
operator|.
name|Runtime
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
name|lambda
operator|.
name|model
operator|.
name|TagResourceRequest
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
name|lambda
operator|.
name|model
operator|.
name|TagResourceResult
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
name|lambda
operator|.
name|model
operator|.
name|TracingConfigResponse
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
name|lambda
operator|.
name|model
operator|.
name|TracingMode
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
name|lambda
operator|.
name|model
operator|.
name|UntagResourceRequest
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
name|lambda
operator|.
name|model
operator|.
name|UntagResourceResult
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
name|lambda
operator|.
name|model
operator|.
name|UpdateAliasRequest
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
name|lambda
operator|.
name|model
operator|.
name|UpdateAliasResult
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
name|lambda
operator|.
name|model
operator|.
name|UpdateEventSourceMappingRequest
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
name|lambda
operator|.
name|model
operator|.
name|UpdateEventSourceMappingResult
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
name|lambda
operator|.
name|model
operator|.
name|UpdateFunctionCodeRequest
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
name|lambda
operator|.
name|model
operator|.
name|UpdateFunctionCodeResult
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
name|lambda
operator|.
name|model
operator|.
name|UpdateFunctionConfigurationRequest
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
name|lambda
operator|.
name|model
operator|.
name|UpdateFunctionConfigurationResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|type
operator|.
name|TypeReference
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_class
DECL|class|AmazonLambdaClientMock
specifier|public
class|class
name|AmazonLambdaClientMock
extends|extends
name|AWSLambdaClient
block|{
DECL|method|AmazonLambdaClientMock ()
specifier|public
name|AmazonLambdaClientMock
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|BasicAWSCredentials
argument_list|(
literal|"user"
argument_list|,
literal|"secret"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addPermission (AddPermissionRequest addPermissionRequest)
specifier|public
name|AddPermissionResult
name|addPermission
parameter_list|(
name|AddPermissionRequest
name|addPermissionRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createAlias (CreateAliasRequest createAliasRequest)
specifier|public
name|CreateAliasResult
name|createAlias
parameter_list|(
name|CreateAliasRequest
name|createAliasRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createEventSourceMapping (CreateEventSourceMappingRequest createEventSourceMappingRequest)
specifier|public
name|CreateEventSourceMappingResult
name|createEventSourceMapping
parameter_list|(
name|CreateEventSourceMappingRequest
name|createEventSourceMappingRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createFunction (CreateFunctionRequest createFunctionRequest)
specifier|public
name|CreateFunctionResult
name|createFunction
parameter_list|(
name|CreateFunctionRequest
name|createFunctionRequest
parameter_list|)
block|{
name|CreateFunctionResult
name|result
init|=
operator|new
name|CreateFunctionResult
argument_list|()
decl_stmt|;
name|result
operator|.
name|setFunctionName
argument_list|(
name|createFunctionRequest
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setDeadLetterConfig
argument_list|(
name|createFunctionRequest
operator|.
name|getDeadLetterConfig
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setDescription
argument_list|(
name|createFunctionRequest
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setHandler
argument_list|(
name|createFunctionRequest
operator|.
name|getHandler
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFunctionArn
argument_list|(
literal|"arn:aws:lambda:eu-central-1:643534317684:function:"
operator|+
name|createFunctionRequest
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Runtime
name|runtime
init|=
name|Runtime
operator|.
name|fromValue
argument_list|(
name|createFunctionRequest
operator|.
name|getRuntime
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|setRuntime
argument_list|(
name|runtime
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AmazonServiceException
argument_list|(
literal|"validation error detected: Value '"
operator|+
name|createFunctionRequest
operator|.
name|getRuntime
argument_list|()
operator|+
literal|"' at 'runtime' failed to satisfy constraint: Member must satisfy enum value set: [java8, nodejs, nodejs4.3, nodejs6.10, python2.7, python3.6, dotnetcore1.0]"
argument_list|)
throw|;
block|}
name|result
operator|.
name|setRole
argument_list|(
literal|"arn:aws:iam::643534317684:role/"
operator|+
name|createFunctionRequest
operator|.
name|getRole
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setCodeSize
argument_list|(
literal|340L
argument_list|)
expr_stmt|;
name|result
operator|.
name|setCodeSha256
argument_list|(
literal|"PKt5ygvZ6G8vWJASlWIypsBmKzAdmRrvTO"
argument_list|)
expr_stmt|;
name|result
operator|.
name|setMemorySize
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|result
operator|.
name|setTimeout
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|result
operator|.
name|setLastModified
argument_list|(
name|DateTime
operator|.
name|now
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setVersion
argument_list|(
literal|"$LATEST"
argument_list|)
expr_stmt|;
name|result
operator|.
name|setTracingConfig
argument_list|(
operator|new
name|TracingConfigResponse
argument_list|()
operator|.
name|withMode
argument_list|(
name|TracingMode
operator|.
name|PassThrough
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|deleteAlias (DeleteAliasRequest deleteAliasRequest)
specifier|public
name|DeleteAliasResult
name|deleteAlias
parameter_list|(
name|DeleteAliasRequest
name|deleteAliasRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|deleteEventSourceMapping (DeleteEventSourceMappingRequest deleteEventSourceMappingRequest)
specifier|public
name|DeleteEventSourceMappingResult
name|deleteEventSourceMapping
parameter_list|(
name|DeleteEventSourceMappingRequest
name|deleteEventSourceMappingRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|deleteFunction (DeleteFunctionRequest deleteFunctionRequest)
specifier|public
name|DeleteFunctionResult
name|deleteFunction
parameter_list|(
name|DeleteFunctionRequest
name|deleteFunctionRequest
parameter_list|)
block|{
return|return
operator|new
name|DeleteFunctionResult
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getAccountSettings (GetAccountSettingsRequest getAccountSettingsRequest)
specifier|public
name|GetAccountSettingsResult
name|getAccountSettings
parameter_list|(
name|GetAccountSettingsRequest
name|getAccountSettingsRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getAlias (GetAliasRequest getAliasRequest)
specifier|public
name|GetAliasResult
name|getAlias
parameter_list|(
name|GetAliasRequest
name|getAliasRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getEventSourceMapping (GetEventSourceMappingRequest getEventSourceMappingRequest)
specifier|public
name|GetEventSourceMappingResult
name|getEventSourceMapping
parameter_list|(
name|GetEventSourceMappingRequest
name|getEventSourceMappingRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getFunction (GetFunctionRequest getFunctionRequest)
specifier|public
name|GetFunctionResult
name|getFunction
parameter_list|(
name|GetFunctionRequest
name|getFunctionRequest
parameter_list|)
block|{
name|GetFunctionResult
name|result
init|=
operator|new
name|GetFunctionResult
argument_list|()
decl_stmt|;
name|FunctionConfiguration
name|configuration
init|=
operator|new
name|FunctionConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setFunctionName
argument_list|(
name|getFunctionRequest
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setFunctionArn
argument_list|(
literal|"arn:aws:lambda:eu-central-1:643534317684:function:"
operator|+
name|getFunctionRequest
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRuntime
argument_list|(
literal|"nodejs6.10"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRole
argument_list|(
literal|"arn:aws:iam::643534317684:role/lambda-execution-role"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setHandler
argument_list|(
name|getFunctionRequest
operator|.
name|getFunctionName
argument_list|()
operator|+
literal|".handler"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCodeSize
argument_list|(
literal|640L
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCodeSha256
argument_list|(
literal|"PKt5ygvZ6G8vWJASlWIypsBmKzAdmRrvTO/eBH06mBA="
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setMemorySize
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTimeout
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setLastModified
argument_list|(
name|DateTime
operator|.
name|now
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setVersion
argument_list|(
literal|"$LATEST"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTracingConfig
argument_list|(
operator|new
name|TracingConfigResponse
argument_list|()
operator|.
name|withMode
argument_list|(
name|TracingMode
operator|.
name|PassThrough
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|getFunctionConfiguration (GetFunctionConfigurationRequest getFunctionConfigurationRequest)
specifier|public
name|GetFunctionConfigurationResult
name|getFunctionConfiguration
parameter_list|(
name|GetFunctionConfigurationRequest
name|getFunctionConfigurationRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getPolicy (GetPolicyRequest getPolicyRequest)
specifier|public
name|GetPolicyResult
name|getPolicy
parameter_list|(
name|GetPolicyRequest
name|getPolicyRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|invoke (InvokeRequest invokeRequest)
specifier|public
name|InvokeResult
name|invoke
parameter_list|(
name|InvokeRequest
name|invokeRequest
parameter_list|)
block|{
name|InvokeResult
name|result
init|=
operator|new
name|InvokeResult
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|payload
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|payload
operator|=
name|mapper
operator|.
name|readValue
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|decode
argument_list|(
name|invokeRequest
operator|.
name|getPayload
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|TypeReference
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
block|{             }
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{          }
name|String
name|responsePayload
init|=
literal|"{\"Hello\":\""
operator|+
name|payload
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|+
literal|"\"}"
decl_stmt|;
name|result
operator|.
name|setPayload
argument_list|(
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|responsePayload
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|invokeAsync (InvokeAsyncRequest invokeAsyncRequest)
specifier|public
name|InvokeAsyncResult
name|invokeAsync
parameter_list|(
name|InvokeAsyncRequest
name|invokeAsyncRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listAliases (ListAliasesRequest listAliasesRequest)
specifier|public
name|ListAliasesResult
name|listAliases
parameter_list|(
name|ListAliasesRequest
name|listAliasesRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listEventSourceMappings (ListEventSourceMappingsRequest listEventSourceMappingsRequest)
specifier|public
name|ListEventSourceMappingsResult
name|listEventSourceMappings
parameter_list|(
name|ListEventSourceMappingsRequest
name|listEventSourceMappingsRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listEventSourceMappings ()
specifier|public
name|ListEventSourceMappingsResult
name|listEventSourceMappings
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listFunctions (ListFunctionsRequest listFunctionsRequest)
specifier|public
name|ListFunctionsResult
name|listFunctions
parameter_list|(
name|ListFunctionsRequest
name|listFunctionsRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listFunctions ()
specifier|public
name|ListFunctionsResult
name|listFunctions
parameter_list|()
block|{
name|ListFunctionsResult
name|result
init|=
operator|new
name|ListFunctionsResult
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|FunctionConfiguration
argument_list|>
name|listFunctions
init|=
operator|new
name|ArrayList
argument_list|<
name|FunctionConfiguration
argument_list|>
argument_list|()
decl_stmt|;
name|FunctionConfiguration
name|configuration
init|=
operator|new
name|FunctionConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setFunctionName
argument_list|(
literal|"GetHelloWithName"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setFunctionArn
argument_list|(
literal|"arn:aws:lambda:eu-central-1:643534317684:function:GetHelloWithName"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRuntime
argument_list|(
literal|"nodejs6.10"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRole
argument_list|(
literal|"arn:aws:iam::643534317684:role/lambda-execution-role"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setHandler
argument_list|(
literal|"GetHelloWithName.handler"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCodeSize
argument_list|(
literal|640L
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCodeSha256
argument_list|(
literal|"PKt5ygvZ6G8vWJASlWIypsBmKzAdmRrvTO/eBH06mBA="
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setMemorySize
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTimeout
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setLastModified
argument_list|(
name|DateTime
operator|.
name|now
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setVersion
argument_list|(
literal|"$LATEST"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTracingConfig
argument_list|(
operator|new
name|TracingConfigResponse
argument_list|()
operator|.
name|withMode
argument_list|(
name|TracingMode
operator|.
name|PassThrough
argument_list|)
argument_list|)
expr_stmt|;
name|listFunctions
operator|.
name|add
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFunctions
argument_list|(
name|listFunctions
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|listTags (ListTagsRequest listTagsRequest)
specifier|public
name|ListTagsResult
name|listTags
parameter_list|(
name|ListTagsRequest
name|listTagsRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listVersionsByFunction (ListVersionsByFunctionRequest listVersionsByFunctionRequest)
specifier|public
name|ListVersionsByFunctionResult
name|listVersionsByFunction
parameter_list|(
name|ListVersionsByFunctionRequest
name|listVersionsByFunctionRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|publishVersion (PublishVersionRequest publishVersionRequest)
specifier|public
name|PublishVersionResult
name|publishVersion
parameter_list|(
name|PublishVersionRequest
name|publishVersionRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|removePermission (RemovePermissionRequest removePermissionRequest)
specifier|public
name|RemovePermissionResult
name|removePermission
parameter_list|(
name|RemovePermissionRequest
name|removePermissionRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|tagResource (TagResourceRequest tagResourceRequest)
specifier|public
name|TagResourceResult
name|tagResource
parameter_list|(
name|TagResourceRequest
name|tagResourceRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|untagResource (UntagResourceRequest untagResourceRequest)
specifier|public
name|UntagResourceResult
name|untagResource
parameter_list|(
name|UntagResourceRequest
name|untagResourceRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|updateAlias (UpdateAliasRequest updateAliasRequest)
specifier|public
name|UpdateAliasResult
name|updateAlias
parameter_list|(
name|UpdateAliasRequest
name|updateAliasRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|updateEventSourceMapping (UpdateEventSourceMappingRequest updateEventSourceMappingRequest)
specifier|public
name|UpdateEventSourceMappingResult
name|updateEventSourceMapping
parameter_list|(
name|UpdateEventSourceMappingRequest
name|updateEventSourceMappingRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|updateFunctionCode (UpdateFunctionCodeRequest updateFunctionCodeRequest)
specifier|public
name|UpdateFunctionCodeResult
name|updateFunctionCode
parameter_list|(
name|UpdateFunctionCodeRequest
name|updateFunctionCodeRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|updateFunctionConfiguration (UpdateFunctionConfigurationRequest updateFunctionConfigurationRequest)
specifier|public
name|UpdateFunctionConfigurationResult
name|updateFunctionConfiguration
parameter_list|(
name|UpdateFunctionConfigurationRequest
name|updateFunctionConfigurationRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getCachedResponseMetadata (AmazonWebServiceRequest amazonWebServiceRequest)
specifier|public
name|ResponseMetadata
name|getCachedResponseMetadata
parameter_list|(
name|AmazonWebServiceRequest
name|amazonWebServiceRequest
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

