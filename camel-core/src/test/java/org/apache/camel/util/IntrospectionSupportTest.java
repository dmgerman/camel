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
name|Collections
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
comment|/**  * Unit test for IntrospectionSupport   */
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
name|assertFalse
argument_list|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
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
name|Collections
operator|.
name|EMPTY_MAP
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
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
literal|"foo."
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|param
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
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
name|map
init|=
operator|new
name|HashMap
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
literal|2
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
name|Map
name|map
init|=
operator|new
name|HashMap
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
literal|2
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
block|}
block|}
end_class

end_unit

