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
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SpringSimpleScheduledRoutePolicyTest
specifier|public
class|class
name|SpringSimpleScheduledRoutePolicyTest
extends|extends
name|SpringScheduledRoutePolicyTest
block|{
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|setApplicationContext
argument_list|(
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/routepolicy/quartz/SimplePolicies.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|setTestType
argument_list|(
name|TestType
operator|.
name|SIMPLE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledStartRoutePolicy ()
specifier|public
name|void
name|testScheduledStartRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|startTest
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledStopRoutePolicy ()
specifier|public
name|void
name|testScheduledStopRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|stopTest
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledSuspendRoutePolicy ()
specifier|public
name|void
name|testScheduledSuspendRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|suspendTest
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledResumeRoutePolicy ()
specifier|public
name|void
name|testScheduledResumeRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|resumeTest
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

