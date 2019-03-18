begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray
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
name|xray
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|NotifyBuilder
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
name|xray
operator|.
name|TestDataBuilder
operator|.
name|TestTrace
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
name|Rule
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_class
DECL|class|SpringAwsXRaySimpleRouteTest
specifier|public
class|class
name|SpringAwsXRaySimpleRouteTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Rule
DECL|field|socketListener
specifier|public
name|FakeAWSDaemon
name|socketListener
init|=
operator|new
name|FakeAWSDaemon
argument_list|()
decl_stmt|;
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
literal|"org/apache/camel/aws/xray/AwsXRaySimpleRouteTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testRoute ()
specifier|public
name|void
name|testRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|5
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:dude"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
literal|"Not all exchanges were fully processed"
argument_list|,
name|notify
operator|.
name|matches
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TestTrace
argument_list|>
name|testData
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"dude"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"car"
argument_list|)
argument_list|)
argument_list|,
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"dude"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"car"
argument_list|)
argument_list|)
argument_list|,
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"dude"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"car"
argument_list|)
argument_list|)
argument_list|,
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"dude"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"car"
argument_list|)
argument_list|)
argument_list|,
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"dude"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"car"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|TestUtils
operator|.
name|checkData
argument_list|(
name|socketListener
operator|.
name|getReceivedData
argument_list|()
argument_list|,
name|testData
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

