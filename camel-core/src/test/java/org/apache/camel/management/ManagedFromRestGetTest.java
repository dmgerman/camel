begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|restbinding
operator|.
name|DummyRestBindingCapableComponent
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
name|model
operator|.
name|rest
operator|.
name|PathDefinition
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
name|model
operator|.
name|rest
operator|.
name|RestDefinition
import|;
end_import

begin_class
DECL|class|ManagedFromRestGetTest
specifier|public
class|class
name|ManagedFromRestGetTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// register our dummy rest capable component
name|context
operator|.
name|addComponent
argument_list|(
literal|"dummy-rest"
argument_list|,
operator|new
name|DummyRestBindingCapableComponent
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testFromRestModel ()
specifier|public
name|void
name|testFromRestModel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"dumpRestsAsXml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"rests"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"rest"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<path uri=\"/say\">"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<get method=\"get\" uri=\"/hello\">"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<get method=\"get\" uri=\"/bye\">"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<post method=\"post\">"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|xml2
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"dumpRoutesAsXml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml2
argument_list|)
expr_stmt|;
comment|// and we should have rest in the routes that indicate its from a rest dsl
name|assertTrue
argument_list|(
name|xml2
operator|.
name|contains
argument_list|(
literal|"rest=\"true\""
argument_list|)
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
name|rest
argument_list|()
operator|.
name|path
argument_list|(
literal|"/say"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/bye"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|post
argument_list|()
operator|.
name|to
argument_list|(
literal|"seda:update"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

