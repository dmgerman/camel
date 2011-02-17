begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
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
name|RuntimeCamelException
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
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|NettyInOutWithForcedNoResponseTest
specifier|public
class|class
name|NettyInOutWithForcedNoResponseTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testResponse ()
specifier|public
name|void
name|testResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:4444"
argument_list|,
literal|"Copenhagen"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Claus"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoResponse ()
specifier|public
name|void
name|testNoResponse
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:4444"
argument_list|,
literal|"London"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"No response"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"netty:tcp://localhost:4444"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Copenhagen"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello Claus"
argument_list|)
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty:tcp://localhost:4445?sync=true&disconnectOnNoReply=false&noReplyLogLevel=INFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Copenhagen"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello Claus"
argument_list|)
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|null
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

