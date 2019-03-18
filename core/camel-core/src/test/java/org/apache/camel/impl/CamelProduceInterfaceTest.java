begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|builder
operator|.
name|ProxyBuilder
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|CamelProduceInterfaceTest
specifier|public
class|class
name|CamelProduceInterfaceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|echo
specifier|private
name|Echo
name|echo
decl_stmt|;
annotation|@
name|Test
DECL|method|testCamelProduceInterface ()
specifier|public
name|void
name|testCamelProduceInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|reply
init|=
name|echo
operator|.
name|hello
argument_list|(
literal|"Camel"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|reply
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
comment|// we gotta cheat and use proxy builder as ContextTestSupport doesnt do
comment|// all the IoC wiring we need when using @Produce on an interface
name|echo
operator|=
operator|new
name|ProxyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|endpoint
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|build
argument_list|(
name|Echo
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Hello "
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

