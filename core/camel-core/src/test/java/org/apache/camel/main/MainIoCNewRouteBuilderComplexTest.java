begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|MyFoo
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
DECL|class|MainIoCNewRouteBuilderComplexTest
specifier|public
class|class
name|MainIoCNewRouteBuilderComplexTest
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
literal|"Hi dude"
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
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
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
DECL|class|MyDude
specifier|public
specifier|static
class|class
name|MyDude
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|MyDude (String name)
specifier|public
name|MyDude
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
literal|"foo"
argument_list|)
DECL|method|createFoo (MyBar bar)
specifier|public
name|MyFoo
name|createFoo
parameter_list|(
name|MyBar
name|bar
parameter_list|)
block|{
comment|// should be invoked #3
return|return
operator|new
name|MyFoo
argument_list|(
name|bar
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|BindToRegistry
DECL|method|myDude ()
specifier|public
name|MyDude
name|myDude
parameter_list|()
block|{
comment|// should be invoked #1
return|return
operator|new
name|MyDude
argument_list|(
literal|"dude"
argument_list|)
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"bar"
argument_list|)
DECL|method|createBar (@ropertyInjectvalue = R, defaultValue = R) String hello, MyDude dude)
specifier|public
name|MyBar
name|createBar
parameter_list|(
annotation|@
name|PropertyInject
argument_list|(
name|value
operator|=
literal|"bye"
argument_list|,
name|defaultValue
operator|=
literal|"Hi"
argument_list|)
name|String
name|hello
parameter_list|,
name|MyDude
name|dude
parameter_list|)
block|{
comment|// should be invoked #2
return|return
operator|new
name|MyBar
argument_list|(
name|hello
operator|+
literal|" "
operator|+
name|dude
operator|.
name|name
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
literal|"foo"
argument_list|,
literal|"getName"
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

