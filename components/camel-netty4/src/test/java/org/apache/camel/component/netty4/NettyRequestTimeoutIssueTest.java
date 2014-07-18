begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
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
name|Assert
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
literal|"This test can be run manually"
argument_list|)
DECL|class|NettyRequestTimeoutIssueTest
specifier|public
class|class
name|NettyRequestTimeoutIssueTest
extends|extends
name|CamelTestSupport
block|{
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
literal|"direct:out"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty:tcp://localhost:8080?requestTimeout=5000"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty:tcp://localhost:8080"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:nettyCase?showAll=true&multiline=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:out"
argument_list|,
literal|"hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Sleeping for 20 seconds, and no Netty exception should occur"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

