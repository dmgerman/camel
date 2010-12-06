begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

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
name|Locale
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
name|util
operator|.
name|jndi
operator|.
name|ExampleBean
import|;
end_import

begin_comment
comment|/**  * Unit test for IntrospectionSupport  */
end_comment

begin_class
DECL|class|IntrospectionSupportTest
specifier|public
class|class
name|IntrospectionSupportTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testOverloadSetterChooseStringSetter ()
specifier|public
name|void
name|testOverloadSetterChooseStringSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyOverloadedBean
name|overloadedBean
init|=
operator|new
name|MyOverloadedBean
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|overloadedBean
argument_list|,
literal|"bean"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|overloadedBean
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOverloadSetterChooseBeanSetter ()
specifier|public
name|void
name|testOverloadSetterChooseBeanSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyOverloadedBean
name|overloadedBean
init|=
operator|new
name|MyOverloadedBean
argument_list|()
decl_stmt|;
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|overloadedBean
argument_list|,
literal|"bean"
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|overloadedBean
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOverloadSetterChooseUsingTypeConverter ()
specifier|public
name|void
name|testOverloadSetterChooseUsingTypeConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyOverloadedBean
name|overloadedBean
init|=
operator|new
name|MyOverloadedBean
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
literal|"Willem"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
comment|// should use byte[] -> String type converter and call the setBean(String) setter method
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|overloadedBean
argument_list|,
literal|"bean"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Willem"
argument_list|,
name|overloadedBean
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyOverloadedBean
specifier|public
class|class
name|MyOverloadedBean
block|{
DECL|field|bean
specifier|private
name|ExampleBean
name|bean
decl_stmt|;
DECL|method|setBean (ExampleBean bean)
specifier|public
name|void
name|setBean
parameter_list|(
name|ExampleBean
name|bean
parameter_list|)
block|{
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
block|}
DECL|method|setBean (String name)
specifier|public
name|void
name|setBean
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|bean
operator|=
operator|new
name|ExampleBean
argument_list|()
expr_stmt|;
name|bean
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|bean
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
DECL|method|testHasProperties ()
specifier|public
name|void
name|testHasProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|empty
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|empty
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|empty
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|empty
argument_list|,
literal|"foo."
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|"foo."
argument_list|)
argument_list|)
expr_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Claus"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|"foo."
argument_list|)
argument_list|)
expr_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"foo.name"
argument_list|,
literal|"Hadrian"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|param
argument_list|,
literal|"foo."
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetProperties ()
specifier|public
name|void
name|testGetProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|bean
argument_list|,
name|map
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|price
init|=
name|map
operator|.
name|get
argument_list|(
literal|"price"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|price
operator|.
name|startsWith
argument_list|(
literal|"10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAnotherGetProperties ()
specifier|public
name|void
name|testAnotherGetProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|AnotherExampleBean
name|bean
init|=
operator|new
name|AnotherExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setId
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|Date
name|date
init|=
operator|new
name|Date
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setDate
argument_list|(
name|date
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setGoldCustomer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setLittle
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Collection
name|children
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setChildren
argument_list|(
name|children
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|bean
argument_list|,
name|map
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|price
init|=
name|map
operator|.
name|get
argument_list|(
literal|"price"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|price
operator|.
name|startsWith
argument_list|(
literal|"10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|date
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"date"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|children
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"children"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"goldCustomer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"little"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetPropertiesOptionPrefix ()
specifier|public
name|void
name|testGetPropertiesOptionPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setId
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|bean
argument_list|,
name|map
argument_list|,
literal|"bean."
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"bean.name"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|price
init|=
name|map
operator|.
name|get
argument_list|(
literal|"bean.price"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|price
operator|.
name|startsWith
argument_list|(
literal|"10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"bean.id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetProperty ()
specifier|public
name|void
name|testGetProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setId
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|Object
name|name
init|=
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|testSetProperty ()
specifier|public
name|void
name|testSetProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setId
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|bean
argument_list|,
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|bean
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAnotherGetProperty ()
specifier|public
name|void
name|testAnotherGetProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|AnotherExampleBean
name|bean
init|=
operator|new
name|AnotherExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|Date
name|date
init|=
operator|new
name|Date
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setDate
argument_list|(
name|date
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setGoldCustomer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setLittle
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Collection
name|children
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setChildren
argument_list|(
name|children
argument_list|)
expr_stmt|;
name|Object
name|name
init|=
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|date
argument_list|,
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"date"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|children
argument_list|,
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"children"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"goldCustomer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"little"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetPropertyLocaleIndependent ()
specifier|public
name|void
name|testGetPropertyLocaleIndependent
parameter_list|()
throws|throws
name|Exception
block|{
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|Locale
operator|.
name|setDefault
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"tr"
argument_list|,
literal|"TR"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|Object
name|name
init|=
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|Object
name|id
init|=
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"id"
argument_list|)
decl_stmt|;
name|Object
name|price
init|=
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
literal|"price"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10.0
argument_list|,
name|price
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetPropertyGetter ()
specifier|public
name|void
name|testGetPropertyGetter
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|Method
name|name
init|=
name|IntrospectionSupport
operator|.
name|getPropertyGetter
argument_list|(
name|ExampleBean
operator|.
name|class
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName"
argument_list|,
name|name
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|IntrospectionSupport
operator|.
name|getPropertyGetter
argument_list|(
name|ExampleBean
operator|.
name|class
argument_list|,
literal|"xxx"
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
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"org.apache.camel.util.jndi.ExampleBean.getXxx()"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetPropertySetter ()
specifier|public
name|void
name|testGetPropertySetter
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|Method
name|name
init|=
name|IntrospectionSupport
operator|.
name|getPropertySetter
argument_list|(
name|ExampleBean
operator|.
name|class
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"setName"
argument_list|,
name|name
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|IntrospectionSupport
operator|.
name|getPropertySetter
argument_list|(
name|ExampleBean
operator|.
name|class
argument_list|,
literal|"xxx"
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
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"org.apache.camel.util.jndi.ExampleBean.setXxx"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testIsGetter ()
specifier|public
name|void
name|testIsGetter
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|Method
name|name
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getName"
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|price
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getPrice"
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|price
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|price
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsSetter ()
specifier|public
name|void
name|testIsSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
name|Method
name|name
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setName"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|price
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setPrice"
argument_list|,
name|double
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|price
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|price
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOtherIsGetter ()
specifier|public
name|void
name|testOtherIsGetter
parameter_list|()
throws|throws
name|Exception
block|{
name|OtherExampleBean
name|bean
init|=
operator|new
name|OtherExampleBean
argument_list|()
decl_stmt|;
name|Method
name|customerId
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getCustomerId"
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|customerId
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|customerId
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|goldCustomer
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"isGoldCustomer"
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|goldCustomer
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|goldCustomer
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|silverCustomer
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"isSilverCustomer"
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|silverCustomer
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|silverCustomer
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|company
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getCompany"
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|company
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|company
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|setupSomething
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setupSomething"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|setupSomething
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|setupSomething
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOtherIsSetter ()
specifier|public
name|void
name|testOtherIsSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|OtherExampleBean
name|bean
init|=
operator|new
name|OtherExampleBean
argument_list|()
decl_stmt|;
name|Method
name|customerId
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setCustomerId"
argument_list|,
name|int
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|customerId
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|customerId
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|goldCustomer
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setGoldCustomer"
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|goldCustomer
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|goldCustomer
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|silverCustomer
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setSilverCustomer"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|silverCustomer
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|silverCustomer
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|company
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setCompany"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|company
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|company
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|setupSomething
init|=
name|bean
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setupSomething"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isGetter
argument_list|(
name|setupSomething
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|IntrospectionSupport
operator|.
name|isSetter
argument_list|(
name|setupSomething
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

