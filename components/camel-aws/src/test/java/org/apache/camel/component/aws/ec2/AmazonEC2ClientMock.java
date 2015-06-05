begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|ec2
operator|.
name|AmazonEC2Client
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
name|ec2
operator|.
name|model
operator|.
name|DescribeInstancesRequest
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
name|ec2
operator|.
name|model
operator|.
name|DescribeInstancesResult
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
name|ec2
operator|.
name|model
operator|.
name|Instance
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
name|ec2
operator|.
name|model
operator|.
name|InstanceState
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
name|ec2
operator|.
name|model
operator|.
name|InstanceStateChange
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
name|ec2
operator|.
name|model
operator|.
name|InstanceStateName
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
name|ec2
operator|.
name|model
operator|.
name|InstanceType
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
name|ec2
operator|.
name|model
operator|.
name|Reservation
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
name|ec2
operator|.
name|model
operator|.
name|RunInstancesRequest
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
name|ec2
operator|.
name|model
operator|.
name|RunInstancesResult
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
name|ec2
operator|.
name|model
operator|.
name|StartInstancesRequest
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
name|ec2
operator|.
name|model
operator|.
name|StartInstancesResult
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
name|ec2
operator|.
name|model
operator|.
name|StopInstancesRequest
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
name|ec2
operator|.
name|model
operator|.
name|StopInstancesResult
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
name|ec2
operator|.
name|model
operator|.
name|TerminateInstancesRequest
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
name|ec2
operator|.
name|model
operator|.
name|TerminateInstancesResult
import|;
end_import

begin_class
DECL|class|AmazonEC2ClientMock
specifier|public
class|class
name|AmazonEC2ClientMock
extends|extends
name|AmazonEC2Client
block|{
DECL|method|AmazonEC2ClientMock ()
specifier|public
name|AmazonEC2ClientMock
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
DECL|method|runInstances (RunInstancesRequest runInstancesRequest)
specifier|public
name|RunInstancesResult
name|runInstances
parameter_list|(
name|RunInstancesRequest
name|runInstancesRequest
parameter_list|)
block|{
name|RunInstancesResult
name|result
init|=
operator|new
name|RunInstancesResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|runInstancesRequest
operator|.
name|getImageId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"test-1"
argument_list|)
condition|)
block|{
name|Reservation
name|res
init|=
operator|new
name|Reservation
argument_list|()
decl_stmt|;
name|res
operator|.
name|setOwnerId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setRequesterId
argument_list|(
literal|"user-test"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setReservationId
argument_list|(
literal|"res-1"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Instance
argument_list|>
name|instances
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Instance
name|ins
init|=
operator|new
name|Instance
argument_list|()
decl_stmt|;
name|ins
operator|.
name|setImageId
argument_list|(
name|runInstancesRequest
operator|.
name|getImageId
argument_list|()
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setInstanceType
argument_list|(
name|runInstancesRequest
operator|.
name|getInstanceType
argument_list|()
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setInstanceId
argument_list|(
literal|"instance-1"
argument_list|)
expr_stmt|;
name|instances
operator|.
name|add
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|res
operator|.
name|setInstances
argument_list|(
name|instances
argument_list|)
expr_stmt|;
name|result
operator|.
name|setReservation
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|AmazonServiceException
argument_list|(
literal|"The image-id doesn't exists"
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|startInstances (StartInstancesRequest startInstancesRequest)
specifier|public
name|StartInstancesResult
name|startInstances
parameter_list|(
name|StartInstancesRequest
name|startInstancesRequest
parameter_list|)
block|{
name|StartInstancesResult
name|result
init|=
operator|new
name|StartInstancesResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|startInstancesRequest
operator|.
name|getInstanceIds
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
literal|"test-1"
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|InstanceStateChange
argument_list|>
name|coll
init|=
operator|new
name|ArrayList
argument_list|<
name|InstanceStateChange
argument_list|>
argument_list|()
decl_stmt|;
name|InstanceStateChange
name|sc
init|=
operator|new
name|InstanceStateChange
argument_list|()
decl_stmt|;
name|InstanceState
name|previousState
init|=
operator|new
name|InstanceState
argument_list|()
decl_stmt|;
name|previousState
operator|.
name|setCode
argument_list|(
literal|80
argument_list|)
expr_stmt|;
name|previousState
operator|.
name|setName
argument_list|(
name|InstanceStateName
operator|.
name|Stopped
argument_list|)
expr_stmt|;
name|InstanceState
name|newState
init|=
operator|new
name|InstanceState
argument_list|()
decl_stmt|;
name|newState
operator|.
name|setCode
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|newState
operator|.
name|setName
argument_list|(
name|InstanceStateName
operator|.
name|Running
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setPreviousState
argument_list|(
name|previousState
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setCurrentState
argument_list|(
name|newState
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setInstanceId
argument_list|(
literal|"test-1"
argument_list|)
expr_stmt|;
name|coll
operator|.
name|add
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|result
operator|.
name|setStartingInstances
argument_list|(
name|coll
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|AmazonServiceException
argument_list|(
literal|"The image-id doesn't exists"
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|stopInstances (StopInstancesRequest stopInstancesRequest)
specifier|public
name|StopInstancesResult
name|stopInstances
parameter_list|(
name|StopInstancesRequest
name|stopInstancesRequest
parameter_list|)
block|{
name|StopInstancesResult
name|result
init|=
operator|new
name|StopInstancesResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|stopInstancesRequest
operator|.
name|getInstanceIds
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
literal|"test-1"
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|InstanceStateChange
argument_list|>
name|coll
init|=
operator|new
name|ArrayList
argument_list|<
name|InstanceStateChange
argument_list|>
argument_list|()
decl_stmt|;
name|InstanceStateChange
name|sc
init|=
operator|new
name|InstanceStateChange
argument_list|()
decl_stmt|;
name|InstanceState
name|previousState
init|=
operator|new
name|InstanceState
argument_list|()
decl_stmt|;
name|previousState
operator|.
name|setCode
argument_list|(
literal|80
argument_list|)
expr_stmt|;
name|previousState
operator|.
name|setName
argument_list|(
name|InstanceStateName
operator|.
name|Running
argument_list|)
expr_stmt|;
name|InstanceState
name|newState
init|=
operator|new
name|InstanceState
argument_list|()
decl_stmt|;
name|newState
operator|.
name|setCode
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|newState
operator|.
name|setName
argument_list|(
name|InstanceStateName
operator|.
name|Stopped
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setPreviousState
argument_list|(
name|previousState
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setCurrentState
argument_list|(
name|newState
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setInstanceId
argument_list|(
literal|"test-1"
argument_list|)
expr_stmt|;
name|coll
operator|.
name|add
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|result
operator|.
name|setStoppingInstances
argument_list|(
name|coll
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|AmazonServiceException
argument_list|(
literal|"The image-id doesn't exists"
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|terminateInstances (TerminateInstancesRequest terminateInstancesRequest)
specifier|public
name|TerminateInstancesResult
name|terminateInstances
parameter_list|(
name|TerminateInstancesRequest
name|terminateInstancesRequest
parameter_list|)
block|{
name|TerminateInstancesResult
name|result
init|=
operator|new
name|TerminateInstancesResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|terminateInstancesRequest
operator|.
name|getInstanceIds
argument_list|()
operator|.
name|contains
argument_list|(
literal|"test-1"
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|InstanceStateChange
argument_list|>
name|coll
init|=
operator|new
name|ArrayList
argument_list|<
name|InstanceStateChange
argument_list|>
argument_list|()
decl_stmt|;
name|InstanceStateChange
name|sc
init|=
operator|new
name|InstanceStateChange
argument_list|()
decl_stmt|;
name|InstanceState
name|previousState
init|=
operator|new
name|InstanceState
argument_list|()
decl_stmt|;
name|previousState
operator|.
name|setCode
argument_list|(
literal|80
argument_list|)
expr_stmt|;
name|previousState
operator|.
name|setName
argument_list|(
name|InstanceStateName
operator|.
name|Running
argument_list|)
expr_stmt|;
name|InstanceState
name|newState
init|=
operator|new
name|InstanceState
argument_list|()
decl_stmt|;
name|newState
operator|.
name|setCode
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|newState
operator|.
name|setName
argument_list|(
name|InstanceStateName
operator|.
name|Terminated
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setPreviousState
argument_list|(
name|previousState
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setCurrentState
argument_list|(
name|newState
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setInstanceId
argument_list|(
literal|"test-1"
argument_list|)
expr_stmt|;
name|coll
operator|.
name|add
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|result
operator|.
name|setTerminatingInstances
argument_list|(
name|coll
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|AmazonServiceException
argument_list|(
literal|"The image-id doesn't exists"
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|describeInstances (DescribeInstancesRequest describeInstancesRequest)
specifier|public
name|DescribeInstancesResult
name|describeInstances
parameter_list|(
name|DescribeInstancesRequest
name|describeInstancesRequest
parameter_list|)
block|{
name|DescribeInstancesResult
name|result
init|=
operator|new
name|DescribeInstancesResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|describeInstancesRequest
operator|.
name|getInstanceIds
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Collection
argument_list|<
name|Reservation
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Reservation
argument_list|>
argument_list|()
decl_stmt|;
name|Reservation
name|res
init|=
operator|new
name|Reservation
argument_list|()
decl_stmt|;
name|res
operator|.
name|setOwnerId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setRequesterId
argument_list|(
literal|"user-test"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setReservationId
argument_list|(
literal|"res-1"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Instance
argument_list|>
name|instances
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Instance
name|ins
init|=
operator|new
name|Instance
argument_list|()
decl_stmt|;
name|ins
operator|.
name|setImageId
argument_list|(
literal|"id-1"
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setInstanceType
argument_list|(
name|InstanceType
operator|.
name|T2Micro
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setInstanceId
argument_list|(
literal|"instance-1"
argument_list|)
expr_stmt|;
name|instances
operator|.
name|add
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|Instance
name|ins1
init|=
operator|new
name|Instance
argument_list|()
decl_stmt|;
name|ins1
operator|.
name|setImageId
argument_list|(
literal|"id-2"
argument_list|)
expr_stmt|;
name|ins1
operator|.
name|setInstanceType
argument_list|(
name|InstanceType
operator|.
name|T2Micro
argument_list|)
expr_stmt|;
name|ins1
operator|.
name|setInstanceId
argument_list|(
literal|"instance-2"
argument_list|)
expr_stmt|;
name|instances
operator|.
name|add
argument_list|(
name|ins1
argument_list|)
expr_stmt|;
name|res
operator|.
name|setInstances
argument_list|(
name|instances
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|result
operator|.
name|setReservations
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|describeInstancesRequest
operator|.
name|getInstanceIds
argument_list|()
operator|.
name|contains
argument_list|(
literal|"instance-1"
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|Reservation
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Reservation
argument_list|>
argument_list|()
decl_stmt|;
name|Reservation
name|res
init|=
operator|new
name|Reservation
argument_list|()
decl_stmt|;
name|res
operator|.
name|setOwnerId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setRequesterId
argument_list|(
literal|"user-test"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setReservationId
argument_list|(
literal|"res-1"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Instance
argument_list|>
name|instances
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Instance
name|ins
init|=
operator|new
name|Instance
argument_list|()
decl_stmt|;
name|ins
operator|.
name|setImageId
argument_list|(
literal|"id-1"
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setInstanceType
argument_list|(
name|InstanceType
operator|.
name|T2Micro
argument_list|)
expr_stmt|;
name|ins
operator|.
name|setInstanceId
argument_list|(
literal|"instance-1"
argument_list|)
expr_stmt|;
name|instances
operator|.
name|add
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|res
operator|.
name|setInstances
argument_list|(
name|instances
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|result
operator|.
name|setReservations
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

