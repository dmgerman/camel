begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RoutesBuilder
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|TwoService2Test
specifier|public
class|class
name|TwoService2Test
extends|extends
name|CamelAwsXRayTestSupport
block|{
DECL|method|TwoService2Test ()
specifier|public
name|TwoService2Test
parameter_list|()
block|{
name|super
argument_list|(
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|inRandomOrder
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"route1"
argument_list|)
operator|.
name|withSubsegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSubsegment
argument_list|(
literal|"direct-ServiceB"
argument_list|)
operator|.
name|withSubsegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSubsegment
argument_list|(
literal|"route2"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
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
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:ServiceA"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"direct:ServiceA"
argument_list|)
operator|.
name|log
argument_list|(
literal|"ServiceA has been called"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(1000,2000)}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:ServiceB"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:ServiceB"
argument_list|)
operator|.
name|log
argument_list|(
literal|"ServiceB has been called"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(0,500)}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

