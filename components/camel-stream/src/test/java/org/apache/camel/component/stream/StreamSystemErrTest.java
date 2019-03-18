begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stream
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for System.err  */
end_comment

begin_class
DECL|class|StreamSystemErrTest
specifier|public
class|class
name|StreamSystemErrTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testStringContent ()
specifier|public
name|void
name|testStringContent
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Hello Text World\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBinaryContent ()
specifier|public
name|void
name|testBinaryContent
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Hello Bytes World\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"stream:err"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

