begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|quartz
operator|.
name|Trigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SimpleTrigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|CronTrigger
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|QuartzEndpointConfigureTest
specifier|public
class|class
name|QuartzEndpointConfigureTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testConfigureGroupAndName ()
specifier|public
name|void
name|testConfigureGroupAndName
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz://myGroup/myName?trigger.repeatCount=3"
argument_list|)
decl_stmt|;
name|Trigger
name|trigger
init|=
name|endpoint
operator|.
name|getTrigger
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myName"
argument_list|,
name|trigger
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"myGroup"
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleTrigger
name|simpleTrigger
init|=
name|assertIsInstanceOf
argument_list|(
name|SimpleTrigger
operator|.
name|class
argument_list|,
name|trigger
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getRepeatCount()"
argument_list|,
literal|3
argument_list|,
name|simpleTrigger
operator|.
name|getRepeatCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConfigureName ()
specifier|public
name|void
name|testConfigureName
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz://myName"
argument_list|)
decl_stmt|;
name|Trigger
name|trigger
init|=
name|endpoint
operator|.
name|getTrigger
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myName"
argument_list|,
name|trigger
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"Camel"
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConfigureCronExpression ()
specifier|public
name|void
name|testConfigureCronExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz://myGroup/myTimerName/0/0/12/*/*/$"
argument_list|)
decl_stmt|;
name|CronTrigger
name|trigger
init|=
name|assertIsInstanceOf
argument_list|(
name|CronTrigger
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getTrigger
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myTimerName"
argument_list|,
name|trigger
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"myGroup"
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cron expression"
argument_list|,
literal|"0 0 12 * * ?"
argument_list|,
name|trigger
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveMandatoryEndpoint (String uri)
specifier|protected
name|QuartzEndpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|super
operator|.
name|resolveMandatoryEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|QuartzEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
block|}
end_class

end_unit

