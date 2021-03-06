begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for PropertyBindingSupport  */
end_comment

begin_class
DECL|class|PropertyBindingSupportAutowireTest
specifier|public
class|class
name|PropertyBindingSupportAutowireTest
extends|extends
name|ContextTestSupport
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
name|Bar
name|bar
init|=
operator|new
name|Bar
argument_list|()
decl_stmt|;
name|bar
operator|.
name|setAge
argument_list|(
literal|33
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setGoldCustomer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setRider
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"myBar"
argument_list|,
name|bar
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testAutowireProperties ()
specifier|public
name|void
name|testAutowireProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Foo
name|foo
init|=
operator|new
name|Foo
argument_list|()
decl_stmt|;
name|PropertyBindingSupport
operator|.
name|build
argument_list|()
operator|.
name|bind
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|autowireSingletonPropertiesFromRegistry
argument_list|(
name|context
argument_list|,
name|foo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|foo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// should be auto wired
name|assertEquals
argument_list|(
literal|33
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getAge
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|isRider
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|isGoldCustomer
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|Foo
specifier|public
specifier|static
class|class
name|Foo
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|bar
specifier|private
name|Bar
name|bar
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
DECL|method|getBar ()
specifier|public
name|Bar
name|getBar
parameter_list|()
block|{
return|return
name|bar
return|;
block|}
DECL|method|setBar (Bar bar)
specifier|public
name|void
name|setBar
parameter_list|(
name|Bar
name|bar
parameter_list|)
block|{
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
block|}
block|}
DECL|class|Bar
specifier|public
specifier|static
class|class
name|Bar
block|{
DECL|field|age
specifier|private
name|int
name|age
decl_stmt|;
DECL|field|rider
specifier|private
name|boolean
name|rider
decl_stmt|;
DECL|field|goldCustomer
specifier|private
name|boolean
name|goldCustomer
decl_stmt|;
DECL|method|getAge ()
specifier|public
name|int
name|getAge
parameter_list|()
block|{
return|return
name|age
return|;
block|}
DECL|method|setAge (int age)
specifier|public
name|void
name|setAge
parameter_list|(
name|int
name|age
parameter_list|)
block|{
name|this
operator|.
name|age
operator|=
name|age
expr_stmt|;
block|}
DECL|method|isRider ()
specifier|public
name|boolean
name|isRider
parameter_list|()
block|{
return|return
name|rider
return|;
block|}
DECL|method|setRider (boolean rider)
specifier|public
name|void
name|setRider
parameter_list|(
name|boolean
name|rider
parameter_list|)
block|{
name|this
operator|.
name|rider
operator|=
name|rider
expr_stmt|;
block|}
DECL|method|isGoldCustomer ()
specifier|public
name|boolean
name|isGoldCustomer
parameter_list|()
block|{
return|return
name|goldCustomer
return|;
block|}
DECL|method|setGoldCustomer (boolean goldCustomer)
specifier|public
name|void
name|setGoldCustomer
parameter_list|(
name|boolean
name|goldCustomer
parameter_list|)
block|{
name|this
operator|.
name|goldCustomer
operator|=
name|goldCustomer
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

