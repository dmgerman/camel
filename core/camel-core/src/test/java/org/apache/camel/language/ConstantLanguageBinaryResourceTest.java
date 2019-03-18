begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
DECL|class|ConstantLanguageBinaryResourceTest
specifier|public
class|class
name|ConstantLanguageBinaryResourceTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testConstantBinary ()
specifier|public
name|void
name|testConstantBinary
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|data
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|""
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
comment|// should have X number of bytes
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10249
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// store the logo
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data"
argument_list|,
name|data
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"savedlogo.jpeg"
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"language:constant:resource:classpath:org/apache/camel/logo.jpeg?binary=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

