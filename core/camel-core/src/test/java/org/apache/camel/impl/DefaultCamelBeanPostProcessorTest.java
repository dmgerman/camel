begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Consume
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
name|Produce
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
name|ProducerTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|DefaultCamelBeanPostProcessorTest
specifier|public
class|class
name|DefaultCamelBeanPostProcessorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|postProcessor
specifier|private
name|DefaultCamelBeanPostProcessor
name|postProcessor
decl_stmt|;
annotation|@
name|Test
DECL|method|testPostProcessor ()
specifier|public
name|void
name|testPostProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|FooService
name|foo
init|=
operator|new
name|FooService
argument_list|()
decl_stmt|;
name|foo
operator|.
name|setFooEndpoint
argument_list|(
literal|"seda:input"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setBarEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|postProcessor
operator|.
name|postProcessBeforeInitialization
argument_list|(
name|foo
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|postProcessor
operator|.
name|postProcessAfterInitialization
argument_list|(
name|foo
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:input"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should register the beans in the registry via @BindRegistry
name|Object
name|bean
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|"myCoolBean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|MySerialBean
operator|.
name|class
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|"FooService"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|FooService
operator|.
name|class
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|"doSomething"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|FooBar
operator|.
name|class
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|postProcessor
operator|=
operator|new
name|DefaultCamelBeanPostProcessor
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BindToRegistry
DECL|class|FooService
specifier|public
class|class
name|FooService
block|{
DECL|field|fooEndpoint
specifier|private
name|String
name|fooEndpoint
decl_stmt|;
DECL|field|barEndpoint
specifier|private
name|String
name|barEndpoint
decl_stmt|;
annotation|@
name|Produce
DECL|field|bar
specifier|private
name|ProducerTemplate
name|bar
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
name|name
operator|=
literal|"myCoolBean"
argument_list|)
DECL|field|myBean
specifier|private
name|MySerialBean
name|myBean
init|=
operator|new
name|MySerialBean
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
DECL|method|doSomething ()
specifier|public
name|FooBar
name|doSomething
parameter_list|()
block|{
return|return
operator|new
name|FooBar
argument_list|()
return|;
block|}
DECL|method|getFooEndpoint ()
specifier|public
name|String
name|getFooEndpoint
parameter_list|()
block|{
return|return
name|fooEndpoint
return|;
block|}
DECL|method|setFooEndpoint (String fooEndpoint)
specifier|public
name|void
name|setFooEndpoint
parameter_list|(
name|String
name|fooEndpoint
parameter_list|)
block|{
name|this
operator|.
name|fooEndpoint
operator|=
name|fooEndpoint
expr_stmt|;
block|}
DECL|method|getBarEndpoint ()
specifier|public
name|String
name|getBarEndpoint
parameter_list|()
block|{
return|return
name|barEndpoint
return|;
block|}
DECL|method|setBarEndpoint (String barEndpoint)
specifier|public
name|void
name|setBarEndpoint
parameter_list|(
name|String
name|barEndpoint
parameter_list|)
block|{
name|this
operator|.
name|barEndpoint
operator|=
name|barEndpoint
expr_stmt|;
block|}
annotation|@
name|Consume
DECL|method|onFoo (String input)
specifier|public
name|void
name|onFoo
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|bar
operator|.
name|sendBody
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

