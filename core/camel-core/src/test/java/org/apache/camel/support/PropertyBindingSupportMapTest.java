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
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|PropertyBindingException
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
DECL|class|PropertyBindingSupportMapTest
specifier|public
class|class
name|PropertyBindingSupportMapTest
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
name|Company
name|work1
init|=
operator|new
name|Company
argument_list|()
decl_stmt|;
name|work1
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|work1
operator|.
name|setName
argument_list|(
literal|"Acme"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"company1"
argument_list|,
name|work1
argument_list|)
expr_stmt|;
name|Company
name|work2
init|=
operator|new
name|Company
argument_list|()
decl_stmt|;
name|work2
operator|.
name|setId
argument_list|(
literal|456
argument_list|)
expr_stmt|;
name|work2
operator|.
name|setName
argument_list|(
literal|"Acme 2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"company2"
argument_list|,
name|work2
argument_list|)
expr_stmt|;
name|Properties
name|placeholders
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|placeholders
operator|.
name|put
argument_list|(
literal|"companyName"
argument_list|,
literal|"Acme"
argument_list|)
expr_stmt|;
name|placeholders
operator|.
name|put
argument_list|(
literal|"committer"
argument_list|,
literal|"rider"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setInitialProperties
argument_list|(
name|placeholders
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesMap ()
specifier|public
name|void
name|testPropertiesMap
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|prop
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.works[acme]"
argument_list|,
literal|"#bean:company1"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.works[burger]"
argument_list|,
literal|"#bean:company2"
argument_list|)
expr_stmt|;
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
name|prop
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"acme"
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acme"
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"acme"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"burger"
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acme 2"
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"burger"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesMapNested ()
specifier|public
name|void
name|testPropertiesMapNested
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|prop
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.works[acme]"
argument_list|,
literal|"#bean:company1"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.works[acme].id"
argument_list|,
literal|"666"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.works[burger]"
argument_list|,
literal|"#bean:company2"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.works[burger].name"
argument_list|,
literal|"I changed this"
argument_list|)
expr_stmt|;
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
name|prop
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|666
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"acme"
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acme"
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"acme"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"burger"
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"I changed this"
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"burger"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesMapFirst ()
specifier|public
name|void
name|testPropertiesMapFirst
parameter_list|()
throws|throws
name|Exception
block|{
name|Bar
name|bar
init|=
operator|new
name|Bar
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|prop
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"works[acme]"
argument_list|,
literal|"#bean:company1"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"works[acme].id"
argument_list|,
literal|"666"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"works[burger]"
argument_list|,
literal|"#bean:company2"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"works[burger].name"
argument_list|,
literal|"I changed this"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|build
argument_list|()
operator|.
name|bind
argument_list|(
name|context
argument_list|,
name|bar
argument_list|,
name|prop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bar
operator|.
name|getWorks
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|666
argument_list|,
name|bar
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"acme"
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acme"
argument_list|,
name|bar
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"acme"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|bar
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"burger"
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"I changed this"
argument_list|,
name|bar
operator|.
name|getWorks
argument_list|()
operator|.
name|get
argument_list|(
literal|"burger"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesNotMap ()
specifier|public
name|void
name|testPropertiesNotMap
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|prop
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.gold-customer[foo]"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
try|try
block|{
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
name|prop
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PropertyBindingException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"bar.gold-customer[foo]"
argument_list|,
name|e
operator|.
name|getPropertyName
argument_list|()
argument_list|)
expr_stmt|;
name|IllegalArgumentException
name|iae
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|iae
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Cannot set property: gold-customer[foo] as either a Map/List because target bean is not a Map or List type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
init|=
operator|new
name|Bar
argument_list|()
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
DECL|field|works
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Company
argument_list|>
name|works
decl_stmt|;
comment|// should auto-create this via the setter
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
DECL|method|getWorks ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Company
argument_list|>
name|getWorks
parameter_list|()
block|{
return|return
name|works
return|;
block|}
DECL|method|setWorks (Map<String, Company> works)
specifier|public
name|void
name|setWorks
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Company
argument_list|>
name|works
parameter_list|)
block|{
name|this
operator|.
name|works
operator|=
name|works
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

