begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyEditorManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyEditorSupport
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Exchange
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
name|NoTypeConversionAvailableException
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
name|TypeConversionException
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
name|TypeConverter
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|DefaultClassResolver
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
name|impl
operator|.
name|DefaultFactoryFinderResolver
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
name|impl
operator|.
name|DefaultPackageScanClassResolver
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
name|impl
operator|.
name|converter
operator|.
name|DefaultTypeConverter
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
name|support
operator|.
name|DefaultExchange
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
name|support
operator|.
name|IntrospectionSupport
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
name|support
operator|.
name|ServiceHelper
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
name|ReflectionInjector
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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ConverterTest
specifier|public
class|class
name|ConverterTest
extends|extends
name|Assert
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ConverterTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|converter
specifier|protected
name|TypeConverter
name|converter
init|=
operator|new
name|DefaultTypeConverter
argument_list|(
operator|new
name|DefaultPackageScanClassResolver
argument_list|()
argument_list|,
operator|new
name|ReflectionInjector
argument_list|()
argument_list|,
operator|new
name|DefaultFactoryFinderResolver
argument_list|()
operator|.
name|resolveDefaultFactoryFinder
argument_list|(
operator|new
name|DefaultClassResolver
argument_list|()
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
DECL|class|IntegerPropertyEditor
specifier|public
specifier|static
class|class
name|IntegerPropertyEditor
extends|extends
name|PropertyEditorSupport
block|{
DECL|method|setAsText (String text)
specifier|public
name|void
name|setAsText
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|setValue
argument_list|(
operator|new
name|Integer
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getAsText ()
specifier|public
name|String
name|getAsText
parameter_list|()
block|{
name|Integer
name|value
init|=
operator|(
name|Integer
operator|)
name|getValue
argument_list|()
decl_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|""
return|;
block|}
block|}
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
name|PropertyEditorManager
operator|.
name|registerEditor
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|IntegerPropertyEditor
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIntegerPropertyEditorConversion ()
specifier|public
name|void
name|testIntegerPropertyEditorConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|Integer
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"1000"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Converted to Integer"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1000
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Converted to String"
argument_list|,
literal|"1000"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertStringToAndFromByteArray ()
specifier|public
name|void
name|testConvertStringToAndFromByteArray
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|array
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found array of size: "
operator|+
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|array
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Converted to String"
argument_list|,
literal|"foo"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertStringToAndFromCharArray ()
specifier|public
name|void
name|testConvertStringToAndFromCharArray
parameter_list|()
throws|throws
name|Exception
block|{
name|char
index|[]
name|array
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|char
index|[]
operator|.
expr|class
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found array of size: "
operator|+
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|array
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Converted to String"
argument_list|,
literal|"foo"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertStringAndStreams ()
specifier|public
name|void
name|testConvertStringAndStreams
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inputStream
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inputStream
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Converted to String"
argument_list|,
literal|"bar"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testArrayToListAndSetConversion ()
specifier|public
name|void
name|testArrayToListAndSetConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|array
init|=
operator|new
name|String
index|[]
block|{
literal|"one"
block|,
literal|"two"
block|}
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|array
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"List size: "
operator|+
name|list
argument_list|,
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Collection
operator|.
name|class
argument_list|,
name|array
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Collection size: "
operator|+
name|collection
argument_list|,
literal|2
argument_list|,
name|collection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|?
argument_list|>
name|set
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Set
operator|.
name|class
argument_list|,
name|array
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Set size: "
operator|+
name|set
argument_list|,
literal|2
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|set
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Set
operator|.
name|class
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Set size: "
operator|+
name|set
argument_list|,
literal|2
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCollectionToArrayConversion ()
specifier|public
name|void
name|testCollectionToArrayConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|objectArray
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|,
name|list
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Object[] length"
argument_list|,
literal|2
argument_list|,
name|objectArray
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
index|[]
name|stringArray
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
index|[]
operator|.
expr|class
argument_list|,
name|list
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"String[] length"
argument_list|,
literal|2
argument_list|,
name|stringArray
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCollectionToPrimitiveArrayConversion ()
specifier|public
name|void
name|testCollectionToPrimitiveArrayConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|Integer
index|[]
name|integerArray
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Integer
index|[]
operator|.
expr|class
argument_list|,
name|list
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Integer[] length"
argument_list|,
literal|2
argument_list|,
name|integerArray
operator|.
name|length
argument_list|)
expr_stmt|;
name|int
index|[]
name|intArray
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|int
index|[]
operator|.
expr|class
argument_list|,
name|list
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"int[] length"
argument_list|,
literal|2
argument_list|,
name|intArray
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// lets convert the typesafe array to a larger primitive type
name|long
index|[]
name|longArray
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|long
index|[]
operator|.
expr|class
argument_list|,
name|intArray
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"long[] length"
argument_list|,
literal|2
argument_list|,
name|longArray
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// now lets go back to a List again
name|List
argument_list|<
name|?
argument_list|>
name|resultList
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|intArray
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"List size"
argument_list|,
literal|2
argument_list|,
name|resultList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"From primitive type array we've created the list: "
operator|+
name|resultList
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStringToFile ()
specifier|public
name|void
name|testStringToFile
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|File
operator|.
name|class
argument_list|,
literal|"foo.txt"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have converted to a file!"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file name"
argument_list|,
literal|"foo.txt"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFileToString ()
specifier|public
name|void
name|testFileToString
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|resource
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"dummy.txt"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot find resource!"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|resource
operator|.
name|getFile
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have returned a String!"
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|text
operator|=
name|text
operator|.
name|trim
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Text not read correctly: "
operator|+
name|text
argument_list|,
name|text
operator|.
name|endsWith
argument_list|(
literal|"Hello World!"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPrimitiveBooleanConversion ()
specifier|public
name|void
name|testPrimitiveBooleanConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|boolean
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPrimitiveIntConversion ()
specifier|public
name|void
name|testPrimitiveIntConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
literal|4
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPrimitiveIntPropertySetter ()
specifier|public
name|void
name|testPrimitiveIntPropertySetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyBean
name|bean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|converter
argument_list|,
name|bean
argument_list|,
literal|"foo"
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean.foo"
argument_list|,
literal|4
argument_list|,
name|bean
operator|.
name|getFoo
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStringToBoolean ()
specifier|public
name|void
name|testStringToBoolean
parameter_list|()
throws|throws
name|Exception
block|{
name|Boolean
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
literal|"true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"converted boolean value"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|value
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"converted boolean value"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|value
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"converted boolean value"
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStaticMethodConversionWithExchange ()
specifier|public
name|void
name|testStaticMethodConversionWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camel
argument_list|)
decl_stmt|;
name|e
operator|.
name|setProperty
argument_list|(
literal|"prefix"
argument_list|,
literal|"foo-"
argument_list|)
expr_stmt|;
name|MyBean
name|bean
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|MyBean
operator|.
name|class
argument_list|,
name|e
argument_list|,
literal|"5:bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"converted using exchange"
argument_list|,
literal|5
argument_list|,
name|bean
operator|.
name|getFoo
argument_list|()
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"converted using exchange"
argument_list|,
literal|"foo-bar"
argument_list|,
name|bean
operator|.
name|getBar
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInstanceMethodConversionWithExchange ()
specifier|public
name|void
name|testInstanceMethodConversionWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|values
init|=
operator|new
name|String
index|[]
block|{
literal|"5"
block|,
literal|"bar"
block|}
decl_stmt|;
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camel
argument_list|)
decl_stmt|;
name|e
operator|.
name|setProperty
argument_list|(
literal|"prefix"
argument_list|,
literal|"foo-"
argument_list|)
expr_stmt|;
name|MyBean
name|bean
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|MyBean
operator|.
name|class
argument_list|,
name|e
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"converted using exchange"
argument_list|,
literal|5
argument_list|,
name|bean
operator|.
name|getFoo
argument_list|()
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"converted using exchange"
argument_list|,
literal|"foo-bar"
argument_list|,
name|bean
operator|.
name|getBar
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMandatoryConvertTo ()
specifier|public
name|void
name|testMandatoryConvertTo
parameter_list|()
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camel
argument_list|)
decl_stmt|;
try|try
block|{
name|converter
operator|.
name|mandatoryConvertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Expect to get a NoTypeConversionAvailableException here"
argument_list|,
name|ex
operator|instanceof
name|NoTypeConversionAvailableException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testStringToChar ()
specifier|public
name|void
name|testStringToChar
parameter_list|()
throws|throws
name|Exception
block|{
name|char
name|ch
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|char
operator|.
name|class
argument_list|,
literal|"A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|'A'
argument_list|,
name|ch
argument_list|)
expr_stmt|;
name|ch
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|char
operator|.
name|class
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|' '
argument_list|,
name|ch
argument_list|)
expr_stmt|;
try|try
block|{
name|converter
operator|.
name|mandatoryConvertTo
argument_list|(
name|char
operator|.
name|class
argument_list|,
literal|"ABC"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TypeConversionException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"String must have exactly a length of 1: ABC"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testNullToBoolean ()
specifier|public
name|void
name|testNullToBoolean
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|b
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|boolean
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullToInt ()
specifier|public
name|void
name|testNullToInt
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|i
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInt ()
specifier|public
name|void
name|testToInt
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|i
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|"0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

