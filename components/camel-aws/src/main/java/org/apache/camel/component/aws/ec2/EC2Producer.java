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
name|DescribeInstanceStatusRequest
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
name|DescribeInstanceStatusResult
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
name|MonitorInstancesRequest
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
name|MonitorInstancesResult
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
name|Placement
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
name|RebootInstancesRequest
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
name|UnmonitorInstancesRequest
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
name|UnmonitorInstancesResult
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
name|Endpoint
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
name|impl
operator|.
name|DefaultProducer
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A Producer which sends messages to the Amazon EC2 Service  *<a href="http://aws.amazon.com/ec2/">AWS EC2</a>  */
end_comment

begin_class
DECL|class|EC2Producer
specifier|public
class|class
name|EC2Producer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EC2Producer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|EC2Producer (Endpoint endpoint)
specifier|public
name|EC2Producer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
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
switch|switch
condition|(
name|determineOperation
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|createAndRunInstances
case|:
name|createAndRunInstance
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|startInstances
case|:
name|startInstances
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|stopInstances
case|:
name|stopInstances
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|terminateInstances
case|:
name|terminateInstances
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|describeInstances
case|:
name|describeInstances
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|describeInstancesStatus
case|:
name|describeInstancesStatus
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|rebootInstances
case|:
name|rebootInstances
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|monitorInstances
case|:
name|monitorInstances
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|unmonitorInstances
case|:
name|unmonitorInstances
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEc2Client
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operation"
argument_list|)
throw|;
block|}
block|}
DECL|method|determineOperation (Exchange exchange)
specifier|private
name|EC2Operations
name|determineOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|EC2Operations
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|OPERATION
argument_list|,
name|EC2Operations
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
name|operation
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
expr_stmt|;
block|}
return|return
name|operation
return|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|EC2Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"EC2Producer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|EC2Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|EC2Endpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|createAndRunInstance (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|createAndRunInstance
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|ami
decl_stmt|;
name|InstanceType
name|instanceType
decl_stmt|;
name|int
name|minCount
decl_stmt|;
name|int
name|maxCount
decl_stmt|;
name|boolean
name|monitoring
decl_stmt|;
name|String
name|kernelId
decl_stmt|;
name|boolean
name|ebsOptimized
decl_stmt|;
name|Collection
name|securityGroups
decl_stmt|;
name|String
name|keyName
decl_stmt|;
name|String
name|clientToken
decl_stmt|;
name|Placement
name|placement
decl_stmt|;
name|RunInstancesRequest
name|request
init|=
operator|new
name|RunInstancesRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|IMAGE_ID
argument_list|)
argument_list|)
condition|)
block|{
name|ami
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|IMAGE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withImageId
argument_list|(
name|ami
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"AMI must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|instanceType
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_TYPE
argument_list|,
name|InstanceType
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceType
argument_list|(
name|instanceType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instance Type must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_MIN_COUNT
argument_list|)
argument_list|)
condition|)
block|{
name|minCount
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_MIN_COUNT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withMinCount
argument_list|(
name|minCount
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Min instances count must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_MAX_COUNT
argument_list|)
argument_list|)
condition|)
block|{
name|maxCount
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_MAX_COUNT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withMaxCount
argument_list|(
name|maxCount
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Max instances count must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_MONITORING
argument_list|)
argument_list|)
condition|)
block|{
name|monitoring
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_MONITORING
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withMonitoring
argument_list|(
name|monitoring
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_KERNEL_ID
argument_list|)
argument_list|)
condition|)
block|{
name|kernelId
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_KERNEL_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withKernelId
argument_list|(
name|kernelId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_EBS_OPTIMIZED
argument_list|)
argument_list|)
condition|)
block|{
name|ebsOptimized
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_EBS_OPTIMIZED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withEbsOptimized
argument_list|(
name|ebsOptimized
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_SECURITY_GROUPS
argument_list|)
argument_list|)
condition|)
block|{
name|securityGroups
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_SECURITY_GROUPS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withSecurityGroups
argument_list|(
name|securityGroups
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_KEY_PAIR
argument_list|)
argument_list|)
condition|)
block|{
name|keyName
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_KEY_PAIR
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withKeyName
argument_list|(
name|keyName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_CLIENT_TOKEN
argument_list|)
argument_list|)
condition|)
block|{
name|clientToken
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_CLIENT_TOKEN
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withClientToken
argument_list|(
name|clientToken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_PLACEMENT
argument_list|)
argument_list|)
condition|)
block|{
name|placement
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_PLACEMENT
argument_list|,
name|Placement
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withPlacement
argument_list|(
name|placement
argument_list|)
expr_stmt|;
block|}
name|RunInstancesResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ec2Client
operator|.
name|runInstances
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Run Instances command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|startInstances (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|startInstances
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
name|instanceIds
decl_stmt|;
name|StartInstancesRequest
name|request
init|=
operator|new
name|StartInstancesRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|)
argument_list|)
condition|)
block|{
name|instanceIds
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceIds
argument_list|(
name|instanceIds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instances Ids must be specified"
argument_list|)
throw|;
block|}
name|StartInstancesResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ec2Client
operator|.
name|startInstances
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Start Instances command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|stopInstances (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|stopInstances
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
name|instanceIds
decl_stmt|;
name|StopInstancesRequest
name|request
init|=
operator|new
name|StopInstancesRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|)
argument_list|)
condition|)
block|{
name|instanceIds
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceIds
argument_list|(
name|instanceIds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instances Ids must be specified"
argument_list|)
throw|;
block|}
name|StopInstancesResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ec2Client
operator|.
name|stopInstances
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Stop Instances command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|terminateInstances (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|terminateInstances
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
name|instanceIds
decl_stmt|;
name|TerminateInstancesRequest
name|request
init|=
operator|new
name|TerminateInstancesRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|)
argument_list|)
condition|)
block|{
name|instanceIds
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceIds
argument_list|(
name|instanceIds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instances Ids must be specified"
argument_list|)
throw|;
block|}
name|TerminateInstancesResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ec2Client
operator|.
name|terminateInstances
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Terminate Instances command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|describeInstances (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|describeInstances
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
name|instanceIds
decl_stmt|;
name|DescribeInstancesRequest
name|request
init|=
operator|new
name|DescribeInstancesRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|)
argument_list|)
condition|)
block|{
name|instanceIds
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceIds
argument_list|(
name|instanceIds
argument_list|)
expr_stmt|;
block|}
name|DescribeInstancesResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ec2Client
operator|.
name|describeInstances
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Describe Instances command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|describeInstancesStatus (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|describeInstancesStatus
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
name|instanceIds
decl_stmt|;
name|DescribeInstanceStatusRequest
name|request
init|=
operator|new
name|DescribeInstanceStatusRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|)
argument_list|)
condition|)
block|{
name|instanceIds
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceIds
argument_list|(
name|instanceIds
argument_list|)
expr_stmt|;
block|}
name|DescribeInstanceStatusResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ec2Client
operator|.
name|describeInstanceStatus
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Describe Instances Status command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|rebootInstances (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|rebootInstances
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
name|instanceIds
decl_stmt|;
name|RebootInstancesRequest
name|request
init|=
operator|new
name|RebootInstancesRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|)
argument_list|)
condition|)
block|{
name|instanceIds
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceIds
argument_list|(
name|instanceIds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instances Ids must be specified"
argument_list|)
throw|;
block|}
try|try
block|{
name|ec2Client
operator|.
name|rebootInstances
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Reboot Instances command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
block|}
DECL|method|monitorInstances (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|monitorInstances
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
name|instanceIds
decl_stmt|;
name|MonitorInstancesRequest
name|request
init|=
operator|new
name|MonitorInstancesRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|)
argument_list|)
condition|)
block|{
name|instanceIds
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceIds
argument_list|(
name|instanceIds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instances Ids must be specified"
argument_list|)
throw|;
block|}
name|MonitorInstancesResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ec2Client
operator|.
name|monitorInstances
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Monitor Instances command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|unmonitorInstances (AmazonEC2Client ec2Client, Exchange exchange)
specifier|private
name|void
name|unmonitorInstances
parameter_list|(
name|AmazonEC2Client
name|ec2Client
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Collection
name|instanceIds
decl_stmt|;
name|UnmonitorInstancesRequest
name|request
init|=
operator|new
name|UnmonitorInstancesRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|)
argument_list|)
condition|)
block|{
name|instanceIds
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withInstanceIds
argument_list|(
name|instanceIds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instances Ids must be specified"
argument_list|)
throw|;
block|}
name|UnmonitorInstancesResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|ec2Client
operator|.
name|unmonitorInstances
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Unmonitor Instances command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

