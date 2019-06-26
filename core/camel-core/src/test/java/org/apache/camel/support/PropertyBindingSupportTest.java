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
name|HashMap
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Injector
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
DECL|class|PropertyBindingSupportTest
specifier|public
class|class
name|PropertyBindingSupportTest
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
name|work
init|=
operator|new
name|Company
argument_list|()
decl_stmt|;
name|work
operator|.
name|setId
argument_list|(
literal|456
argument_list|)
expr_stmt|;
name|work
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
literal|"myWork"
argument_list|,
name|work
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
DECL|method|testProperties ()
specifier|public
name|void
name|testProperties
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
name|HashMap
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
literal|"bar.work.id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.work.name"
argument_list|,
literal|"{{companyName}}"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperties
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
literal|123
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should bind all properties"
argument_list|,
name|prop
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesIgnoreCase ()
specifier|public
name|void
name|testPropertiesIgnoreCase
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
name|HashMap
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
literal|"bar.AGE"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"BAR.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.gOLd-Customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bAr.work.ID"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"bar.WORk.naME"
argument_list|,
literal|"{{companyName}}"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperties
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
name|prop
argument_list|,
literal|true
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
literal|123
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should bind all properties"
argument_list|,
name|prop
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBindPropertiesWithOptionPrefix ()
specifier|public
name|void
name|testBindPropertiesWithOptionPrefix
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
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prefix.name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prefix.bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prefix.bar.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prefix.bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prefix.bar.work.id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prefix.bar.work.name"
argument_list|,
literal|"{{companyName}}"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.other.prefix.something"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperties
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
name|prop
argument_list|,
literal|"my.prefix."
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
literal|123
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|prop
operator|.
name|containsKey
argument_list|(
literal|"my.other.prefix.something"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|prop
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBindPropertiesWithOptionPrefixIgnoreCase ()
specifier|public
name|void
name|testBindPropertiesWithOptionPrefixIgnoreCase
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
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prefix.name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.PREFIX.bar.AGE"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prefix.bar.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"My.prefix.bar.Gold-custoMER"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"mY.prefix.bar.work.ID"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.prEFIx.bar.Work.Name"
argument_list|,
literal|"{{companyName}}"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my.other.prefix.something"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperties
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
name|prop
argument_list|,
literal|"my.prefix."
argument_list|,
literal|true
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
literal|123
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|prop
operator|.
name|containsKey
argument_list|(
literal|"my.other.prefix.something"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|prop
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNested ()
specifier|public
name|void
name|testNested
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
name|bindProperty
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
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work.id"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work.name"
argument_list|,
literal|"{{companyName}}"
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
literal|123
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNestedReference ()
specifier|public
name|void
name|testNestedReference
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
name|bindProperty
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
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.rider"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work"
argument_list|,
literal|"#bean:myWork"
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
literal|456
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNestedReferenceId ()
specifier|public
name|void
name|testNestedReferenceId
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
name|bindProperty
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
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.rider"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work"
argument_list|,
literal|"#bean:myWork"
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
literal|456
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNestedType ()
specifier|public
name|void
name|testNestedType
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
name|bindProperty
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
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work"
argument_list|,
literal|"#type:org.apache.camel.support.Company"
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
literal|456
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNestedClass ()
specifier|public
name|void
name|testNestedClass
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
name|bindProperty
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
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work"
argument_list|,
literal|"#class:org.apache.camel.support.Company"
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
comment|// a new class was created so its empty
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAutowired ()
specifier|public
name|void
name|testAutowired
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
name|bindProperty
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
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.age"
argument_list|,
literal|"33"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.{{committer}}"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.gold-customer"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work"
argument_list|,
literal|"#autowired"
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
literal|456
argument_list|,
name|foo
operator|.
name|getBar
argument_list|()
operator|.
name|getWork
argument_list|()
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
name|getWork
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMandatory ()
specifier|public
name|void
name|testMandatory
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
name|bindMandatoryProperty
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
name|boolean
name|bound
init|=
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.myAge"
argument_list|,
literal|"33"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|bound
argument_list|)
expr_stmt|;
try|try
block|{
name|PropertyBindingSupport
operator|.
name|bindMandatoryProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.myAge"
argument_list|,
literal|"33"
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
literal|"bar.myAge"
argument_list|,
name|e
operator|.
name|getPropertyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|foo
argument_list|,
name|e
operator|.
name|getTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testDoesNotExistClass ()
specifier|public
name|void
name|testDoesNotExistClass
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
name|bindProperty
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
try|try
block|{
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work"
argument_list|,
literal|"#class:org.apache.camel.support.DoesNotExist"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PropertyBindingException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|ClassNotFoundException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testNullInjectorClass ()
specifier|public
name|void
name|testNullInjectorClass
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
name|context
operator|.
name|setInjector
argument_list|(
operator|new
name|Injector
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|boolean
name|postProcessBean
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsAutoWiring
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperty
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
try|try
block|{
name|PropertyBindingSupport
operator|.
name|bindProperty
argument_list|(
name|context
argument_list|,
name|foo
argument_list|,
literal|"bar.work"
argument_list|,
literal|"#class:org.apache.camel.support.Company"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PropertyBindingException
name|e
parameter_list|)
block|{
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
DECL|field|work
specifier|private
name|Company
name|work
decl_stmt|;
comment|// has no default value but Camel can automatic create one if there is a setter
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
DECL|method|getWork ()
specifier|public
name|Company
name|getWork
parameter_list|()
block|{
return|return
name|work
return|;
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
comment|// this has no setter but only builders
comment|// and mix the builders with both styles (with as prefix and no prefix at all)
DECL|method|withAge (int age)
specifier|public
name|Bar
name|withAge
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
return|return
name|this
return|;
block|}
DECL|method|withRider (boolean rider)
specifier|public
name|Bar
name|withRider
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
return|return
name|this
return|;
block|}
DECL|method|work (Company work)
specifier|public
name|Bar
name|work
parameter_list|(
name|Company
name|work
parameter_list|)
block|{
name|this
operator|.
name|work
operator|=
name|work
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|goldCustomer (boolean goldCustomer)
specifier|public
name|Bar
name|goldCustomer
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
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

