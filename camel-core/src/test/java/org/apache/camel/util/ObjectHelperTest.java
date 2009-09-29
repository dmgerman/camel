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
name|Arrays
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ObjectHelperTest
specifier|public
class|class
name|ObjectHelperTest
extends|extends
name|TestCase
block|{
DECL|method|testRemoveInitialCharacters ()
specifier|public
name|void
name|testRemoveInitialCharacters
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|ObjectHelper
operator|.
name|removeStartingCharacters
argument_list|(
literal|"foo"
argument_list|,
literal|'/'
argument_list|)
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectHelper
operator|.
name|removeStartingCharacters
argument_list|(
literal|"/foo"
argument_list|,
literal|'/'
argument_list|)
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectHelper
operator|.
name|removeStartingCharacters
argument_list|(
literal|"//foo"
argument_list|,
literal|'/'
argument_list|)
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetPropertyName ()
specifier|public
name|void
name|testGetPropertyName
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|method
init|=
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setCheese"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"should have found a method!"
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|ObjectHelper
operator|.
name|getPropertyName
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Property name"
argument_list|,
literal|"cheese"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|setCheese (String cheese)
specifier|public
name|void
name|setCheese
parameter_list|(
name|String
name|cheese
parameter_list|)
block|{
comment|// used in the above unit test
block|}
DECL|method|testContains ()
specifier|public
name|void
name|testContains
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|array
init|=
block|{
literal|"foo"
block|,
literal|"bar"
block|}
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|collection
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|contains
argument_list|(
name|array
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|contains
argument_list|(
name|collection
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|contains
argument_list|(
name|array
argument_list|,
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|contains
argument_list|(
name|collection
argument_list|,
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|,
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqual ()
specifier|public
name|void
name|testEqual
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|" "
argument_list|,
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|"Hello"
argument_list|,
literal|"Hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|123
argument_list|,
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|null
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|""
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|" "
argument_list|,
literal|"    "
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|"Hello"
argument_list|,
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|a
init|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
decl_stmt|;
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
expr_stmt|;
name|b
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|,
literal|70
block|}
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqualByteArray ()
specifier|public
name|void
name|testEqualByteArray
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"World"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
literal|"Hello Thai Elephant \u0E08"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"Hello Thai Elephant \u0E08"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|empty
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|empty
argument_list|,
name|empty
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|a
init|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
decl_stmt|;
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
expr_stmt|;
name|b
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|,
literal|70
block|}
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|,
literal|70
block|}
expr_stmt|;
name|b
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
expr_stmt|;
name|b
operator|=
operator|new
name|byte
index|[
literal|0
index|]
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|byte
index|[
literal|0
index|]
expr_stmt|;
name|b
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
expr_stmt|;
name|b
operator|=
literal|null
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
literal|null
expr_stmt|;
name|b
operator|=
operator|new
name|byte
index|[]
block|{
literal|40
block|,
literal|50
block|,
literal|60
block|}
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
literal|null
expr_stmt|;
name|b
operator|=
literal|null
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|equalByteArray
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateIterator ()
specifier|public
name|void
name|testCreateIterator
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
literal|"Should return the same iterator"
argument_list|,
name|iterator
argument_list|,
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|iterator
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateIteratorWithStringAndCommaSeparator ()
specifier|public
name|void
name|testCreateIteratorWithStringAndCommaSeparator
parameter_list|()
block|{
name|String
name|s
init|=
literal|"a,b,c"
decl_stmt|;
name|Iterator
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|s
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateIteratorWithStringAndSemiColonSeparator ()
specifier|public
name|void
name|testCreateIteratorWithStringAndSemiColonSeparator
parameter_list|()
block|{
name|String
name|s
init|=
literal|"a;b;c"
decl_stmt|;
name|Iterator
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|s
argument_list|,
literal|";"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBefore ()
specifier|public
name|void
name|testBefore
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Hello "
argument_list|,
name|ObjectHelper
operator|.
name|before
argument_list|(
literal|"Hello World"
argument_list|,
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
argument_list|,
name|ObjectHelper
operator|.
name|before
argument_list|(
literal|"Hello World Again"
argument_list|,
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectHelper
operator|.
name|before
argument_list|(
literal|"Hello Again"
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAfter ()
specifier|public
name|void
name|testAfter
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|" World"
argument_list|,
name|ObjectHelper
operator|.
name|after
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" World Again"
argument_list|,
name|ObjectHelper
operator|.
name|after
argument_list|(
literal|"Hello World Again"
argument_list|,
literal|"Hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectHelper
operator|.
name|after
argument_list|(
literal|"Hello Again"
argument_list|,
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testBetween ()
specifier|public
name|void
name|testBetween
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo bar"
argument_list|,
name|ObjectHelper
operator|.
name|between
argument_list|(
literal|"Hello 'foo bar' how are you"
argument_list|,
literal|"'"
argument_list|,
literal|"'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo bar"
argument_list|,
name|ObjectHelper
operator|.
name|between
argument_list|(
literal|"Hello ${foo bar} how are you"
argument_list|,
literal|"${"
argument_list|,
literal|"}"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectHelper
operator|.
name|between
argument_list|(
literal|"Hello ${foo bar} how are you"
argument_list|,
literal|"'"
argument_list|,
literal|"'"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsJavaIdentifier ()
specifier|public
name|void
name|testIsJavaIdentifier
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ObjectHelper
operator|.
name|isJavaIdentifier
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ObjectHelper
operator|.
name|isJavaIdentifier
argument_list|(
literal|"foo.bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ObjectHelper
operator|.
name|isJavaIdentifier
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ObjectHelper
operator|.
name|isJavaIdentifier
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetDefaultCharSet ()
specifier|public
name|void
name|testGetDefaultCharSet
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|ObjectHelper
operator|.
name|getDefaultCharacterSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertPrimitiveTypeToWrapper ()
specifier|public
name|void
name|testConvertPrimitiveTypeToWrapper
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"java.lang.Integer"
argument_list|,
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|int
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Long"
argument_list|,
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|long
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Double"
argument_list|,
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|double
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Float"
argument_list|,
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|float
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Short"
argument_list|,
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|short
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Byte"
argument_list|,
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|byte
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.Boolean"
argument_list|,
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|boolean
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// non primitive just fall through
name|assertEquals
argument_list|(
literal|"java.lang.Object"
argument_list|,
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|Object
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsString ()
specifier|public
name|void
name|testAsString
parameter_list|()
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"foo"
block|,
literal|"bar"
block|}
decl_stmt|;
name|String
name|out
init|=
name|ObjectHelper
operator|.
name|asString
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{foo, bar}"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testName ()
specifier|public
name|void
name|testName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"java.lang.Integer"
argument_list|,
name|ObjectHelper
operator|.
name|name
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectHelper
operator|.
name|name
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testClassName ()
specifier|public
name|void
name|testClassName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"java.lang.Integer"
argument_list|,
name|ObjectHelper
operator|.
name|className
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"5"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectHelper
operator|.
name|className
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetSystemPropertyDefault ()
specifier|public
name|void
name|testGetSystemPropertyDefault
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|ObjectHelper
operator|.
name|getSystemProperty
argument_list|(
literal|"CamelFooDoesNotExist"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetSystemPropertyBooleanDefault ()
specifier|public
name|void
name|testGetSystemPropertyBooleanDefault
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ObjectHelper
operator|.
name|getSystemProperty
argument_list|(
literal|"CamelFooDoesNotExist"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMatches ()
specifier|public
name|void
name|testMatches
parameter_list|()
block|{
name|List
name|data
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|data
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ObjectHelper
operator|.
name|matches
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|clear
argument_list|()
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ObjectHelper
operator|.
name|matches
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ObjectHelper
operator|.
name|matches
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToBoolean ()
specifier|public
name|void
name|testToBoolean
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
literal|"true"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

