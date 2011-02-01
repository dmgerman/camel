begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.routepolicy.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|routepolicy
operator|.
name|quartz
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|ServiceStatus
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
name|component
operator|.
name|quartz
operator|.
name|QuartzComponent
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 882486 $  */
end_comment

begin_class
DECL|class|SimpleScheduledCombinedRoutePolicyTest
specifier|public
class|class
name|SimpleScheduledCombinedRoutePolicyTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SimpleScheduledCombinedRoutePolicyTest
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/* (non-Javadoc)      * @see org.apache.camel.test.junit4.CamelTestSupport#s;etUp()      */
annotation|@
name|Override
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
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.test.junit4.CamelTestSupport#isUseRouteBuilder()      */
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
DECL|method|testScheduledStartAndStopRoutePolicy ()
specifier|public
name|void
name|testScheduledStartAndStopRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|success
init|=
operator|(
name|MockEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:success"
argument_list|)
decl_stmt|;
name|success
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz/myquartz.properties"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|SimpleScheduledRoutePolicy
name|policy
init|=
operator|new
name|SimpleScheduledRoutePolicy
argument_list|()
decl_stmt|;
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|3000L
decl_stmt|;
name|long
name|stopTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|8000L
decl_stmt|;
name|policy
operator|.
name|setRouteStartDate
argument_list|(
operator|new
name|Date
argument_list|(
name|startTime
argument_list|)
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStartRepeatCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStartRepeatInterval
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStopDate
argument_list|(
operator|new
name|Date
argument_list|(
name|stopTime
argument_list|)
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStopRepeatCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStopRepeatInterval
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"test"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Started
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Ready or not, Here, I come"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"test"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Stopped
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
name|success
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

