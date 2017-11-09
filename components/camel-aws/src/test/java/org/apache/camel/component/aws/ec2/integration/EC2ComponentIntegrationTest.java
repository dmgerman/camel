begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ec2.integration
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
operator|.
name|integration
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
name|aws
operator|.
name|ec2
operator|.
name|EC2Constants
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
name|aws
operator|.
name|ec2
operator|.
name|EC2Operations
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
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"Must be manually tested. Provide your own accessKey and secretKey!"
argument_list|)
DECL|class|EC2ComponentIntegrationTest
specifier|public
class|class
name|EC2ComponentIntegrationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|createAndRunInstancesTest ()
specifier|public
name|void
name|createAndRunInstancesTest
parameter_list|()
block|{
name|template
operator|.
name|send
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
name|IMAGE_ID
argument_list|,
literal|"ami-fd65ba94"
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
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createAndRunInstancesWithSecurityGroupsTest ()
specifier|public
name|void
name|createAndRunInstancesWithSecurityGroupsTest
parameter_list|()
block|{
name|template
operator|.
name|send
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
name|IMAGE_ID
argument_list|,
literal|"ami-fd65ba94"
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
name|Collection
argument_list|<
name|String
argument_list|>
name|secGroups
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|secGroups
operator|.
name|add
argument_list|(
literal|"secgroup-1"
argument_list|)
expr_stmt|;
name|secGroups
operator|.
name|add
argument_list|(
literal|"secgroup-2"
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
name|INSTANCE_SECURITY_GROUPS
argument_list|,
name|secGroups
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ec2CreateAndRunTestWithKeyPair ()
specifier|public
name|void
name|ec2CreateAndRunTestWithKeyPair
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"ami-fd65ba94"
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EC2Constants
operator|.
name|INSTANCES_KEY_PAIR
argument_list|,
literal|"keypair-1"
argument_list|)
expr_stmt|;
block|}
block|}
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
name|template
operator|.
name|send
argument_list|(
literal|"direct:stop"
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
name|Collection
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
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
name|Collection
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|template
operator|.
name|send
argument_list|(
literal|"direct:terminate"
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
name|Collection
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
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
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ec2DescribeInstancesTest ()
specifier|public
name|void
name|ec2DescribeInstancesTest
parameter_list|()
throws|throws
name|Exception
block|{
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
block|{                              }
block|}
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
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
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
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ec2DescribeInstancesStatusTest ()
specifier|public
name|void
name|ec2DescribeInstancesStatusTest
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|request
argument_list|(
literal|"direct:describeStatus"
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
block|{                              }
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ec2DescribeStatusSpecificInstancesTest ()
specifier|public
name|void
name|ec2DescribeStatusSpecificInstancesTest
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|request
argument_list|(
literal|"direct:describeStatus"
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
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
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
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ec2RebootInstancesTest ()
specifier|public
name|void
name|ec2RebootInstancesTest
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|request
argument_list|(
literal|"direct:reboot"
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
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
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
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ec2MonitorInstancesTest ()
specifier|public
name|void
name|ec2MonitorInstancesTest
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|request
argument_list|(
literal|"direct:monitor"
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
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
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
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ec2UnmonitorInstancesTest ()
specifier|public
name|void
name|ec2UnmonitorInstancesTest
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|request
argument_list|(
literal|"direct:unmonitor"
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
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
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
expr_stmt|;
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
literal|"direct:createAndRun"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=createAndRunInstances"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:stop"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=stopInstances"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=startInstances"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:terminate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=terminateInstances"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:describe"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=describeInstances"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:describeStatus"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=describeInstancesStatus"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:reboot"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=rebootInstances"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:monitor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=monitorInstances"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmonitor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ec2://TestDomain?accessKey=xxxx&secretKey=xxxx&operation=unmonitorInstances"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

