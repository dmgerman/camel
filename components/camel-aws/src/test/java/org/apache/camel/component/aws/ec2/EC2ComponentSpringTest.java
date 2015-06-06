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
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|List
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
name|simpledb
operator|.
name|model
operator|.
name|Attribute
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
name|simpledb
operator|.
name|model
operator|.
name|DeletableItem
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
name|simpledb
operator|.
name|model
operator|.
name|Item
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
name|simpledb
operator|.
name|model
operator|.
name|ReplaceableAttribute
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
name|simpledb
operator|.
name|model
operator|.
name|ReplaceableItem
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
name|simpledb
operator|.
name|model
operator|.
name|UpdateCondition
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
name|impl
operator|.
name|DefaultProducerTemplate
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
name|spring
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|EC2ComponentSpringTest
specifier|public
class|class
name|EC2ComponentSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|amazonEc2Client
specifier|private
name|AmazonEC2ClientMock
name|amazonEc2Client
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|amazonEc2Client
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"amazonEC2Client"
argument_list|,
name|AmazonEC2ClientMock
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAndRunInstances ()
specifier|public
name|void
name|createAndRunInstances
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:createAndRun"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|EC2Constants
operator|.
name|OPERATION
argument_list|,
name|EC2Operations
operator|.
name|createAndRunInstances
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|IMAGE_ID
argument_list|,
literal|"test-1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_TYPE
argument_list|,
name|InstanceType
operator|.
name|T2Micro
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_MIN_COUNT
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCE_MAX_COUNT
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|RunInstancesResult
name|resultGet
init|=
operator|(
name|RunInstancesResult
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
name|resultGet
operator|.
name|getReservation
argument_list|()
operator|.
name|getInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getImageId
argument_list|()
argument_list|,
literal|"test-1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getReservation
argument_list|()
operator|.
name|getInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInstanceType
argument_list|()
argument_list|,
name|InstanceType
operator|.
name|T2Micro
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getReservation
argument_list|()
operator|.
name|getInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInstanceId
argument_list|()
argument_list|,
literal|"instance-1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|startInstances ()
specifier|public
name|void
name|startInstances
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
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
name|Collection
name|l
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
literal|"test-1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|StartInstancesResult
name|resultGet
init|=
operator|(
name|StartInstancesResult
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
name|resultGet
operator|.
name|getStartingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInstanceId
argument_list|()
argument_list|,
literal|"test-1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getStartingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPreviousState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|InstanceStateName
operator|.
name|Stopped
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getStartingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCurrentState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|InstanceStateName
operator|.
name|Running
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|stopInstances ()
specifier|public
name|void
name|stopInstances
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:stop"
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
name|Collection
name|l
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
literal|"test-1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|StopInstancesResult
name|resultGet
init|=
operator|(
name|StopInstancesResult
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
name|resultGet
operator|.
name|getStoppingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInstanceId
argument_list|()
argument_list|,
literal|"test-1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getStoppingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPreviousState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|InstanceStateName
operator|.
name|Running
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getStoppingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCurrentState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|InstanceStateName
operator|.
name|Stopped
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|terminateInstances ()
specifier|public
name|void
name|terminateInstances
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:terminate"
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
name|Collection
name|l
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
literal|"test-1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|TerminateInstancesResult
name|resultGet
init|=
operator|(
name|TerminateInstancesResult
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
name|resultGet
operator|.
name|getTerminatingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInstanceId
argument_list|()
argument_list|,
literal|"test-1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getTerminatingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPreviousState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|InstanceStateName
operator|.
name|Running
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getTerminatingInstances
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCurrentState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|InstanceStateName
operator|.
name|Terminated
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ec2DescribeSpecificInstancesTest ()
specifier|public
name|void
name|ec2DescribeSpecificInstancesTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:describe"
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
name|Collection
name|l
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
literal|"instance-1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_IDS
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|DescribeInstancesResult
name|resultGet
init|=
operator|(
name|DescribeInstancesResult
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
name|resultGet
operator|.
name|getReservations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultGet
operator|.
name|getReservations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInstances
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/aws/ec2/EC2ComponentSpringTest-context.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

