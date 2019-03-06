begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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
name|BeanInject
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
name|BindToRegistry
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
name|CamelContext
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
name|PropertyInject
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
name|Assert
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
DECL|class|MainIoCNewRouteBuilderTest
specifier|public
class|class
name|MainIoCNewRouteBuilderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testMainIoC ()
specifier|public
name|void
name|testMainIoC
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addConfiguration
argument_list|(
operator|new
name|MyConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|MyRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|main
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|main
operator|.
name|getCamelTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<message>1</message>"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|MainIoCNewRouteBuilderTest
operator|.
name|MyConfiguration
operator|.
name|MyCoolBean
name|mcb
init|=
operator|(
name|MainIoCNewRouteBuilderTest
operator|.
name|MyConfiguration
operator|.
name|MyCoolBean
operator|)
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|"MyCoolBean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mcb
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Tiger"
argument_list|,
name|mcb
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|class|MyConfiguration
specifier|public
specifier|static
class|class
name|MyConfiguration
block|{
annotation|@
name|BeanInject
DECL|field|camel
specifier|private
name|CamelContext
name|camel
decl_stmt|;
annotation|@
name|BindToRegistry
DECL|class|MyCoolBean
specifier|public
specifier|static
class|class
name|MyCoolBean
block|{
DECL|field|name
specifier|private
name|String
name|name
init|=
literal|"Tiger"
decl_stmt|;
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|camel
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyBar
specifier|public
specifier|static
class|class
name|MyBar
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|MyBar (String name)
specifier|public
name|MyBar
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
DECL|class|MyRouteBuilder
specifier|public
specifier|static
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|BindToRegistry
argument_list|(
name|name
operator|=
literal|"bar"
argument_list|)
DECL|method|createBar (@ropertyInjectvalue = R) String hello)
specifier|public
name|MyBar
name|createBar
parameter_list|(
annotation|@
name|PropertyInject
argument_list|(
name|value
operator|=
literal|"hello"
argument_list|)
name|String
name|hello
parameter_list|)
block|{
return|return
operator|new
name|MyBar
argument_list|(
name|hello
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|configure ()
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
name|bean
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

