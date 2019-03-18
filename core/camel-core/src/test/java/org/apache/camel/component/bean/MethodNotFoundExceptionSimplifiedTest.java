begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|ExchangeBuilder
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|MethodNotFoundExceptionSimplifiedTest
specifier|public
class|class
name|MethodNotFoundExceptionSimplifiedTest
extends|extends
name|ContextTestSupport
block|{
DECL|interface|InterfaceEmpty
specifier|public
interface|interface
name|InterfaceEmpty
block|{
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
block|}
DECL|class|SuperClazz
specifier|public
specifier|static
class|class
name|SuperClazz
block|{
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
DECL|class|Clazz
specifier|public
specifier|static
class|class
name|Clazz
extends|extends
name|SuperClazz
implements|implements
name|InterfaceEmpty
block|{     }
annotation|@
name|Override
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
literal|"direct:in"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|simple
argument_list|(
literal|"${headers.bean.isEmpty()}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testMethodNotFound ()
specifier|public
name|void
name|testMethodNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|out
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
decl_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|ExchangeBuilder
name|exchangeBuilder
init|=
operator|new
name|ExchangeBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"bean"
argument_list|,
operator|new
name|Clazz
argument_list|()
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:in"
argument_list|,
name|exchangeBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

