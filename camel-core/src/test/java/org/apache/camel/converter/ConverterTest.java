begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

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
name|InputStream
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ConverterTest
specifier|public
class|class
name|ConverterTest
extends|extends
name|TestCase
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
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
argument_list|()
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
operator|(
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
operator|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
block|}
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
DECL|method|testConvertStringAndBytes ()
specifier|public
name|void
name|testConvertStringAndBytes
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
name|log
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
DECL|method|testCollectionToArrayConversion ()
specifier|public
name|void
name|testCollectionToArrayConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|list
init|=
operator|new
name|ArrayList
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
DECL|method|testCollectionToPrimitiveArrayConversion ()
specifier|public
name|void
name|testCollectionToPrimitiveArrayConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|list
init|=
operator|new
name|ArrayList
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
name|log
operator|.
name|info
argument_list|(
literal|"From primitive type array we've created the list: "
operator|+
name|resultList
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

