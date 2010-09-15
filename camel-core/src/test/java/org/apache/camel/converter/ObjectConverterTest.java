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
DECL|class|ObjectConverterTest
specifier|public
class|class
name|ObjectConverterTest
extends|extends
name|TestCase
block|{
DECL|method|testIsCollection ()
specifier|public
name|void
name|testIsCollection
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ObjectConverter
operator|.
name|isCollection
argument_list|(
literal|"String"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ObjectConverter
operator|.
name|isCollection
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ObjectConverter
operator|.
name|isCollection
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ObjectConverter
operator|.
name|isCollection
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"foo"
block|,
literal|"bar"
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIterator ()
specifier|public
name|void
name|testIterator
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|ObjectConverter
operator|.
name|iterator
argument_list|(
literal|"Claus,Jonathan"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Jonathan"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByte ()
specifier|public
name|void
name|testToByte
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Byte
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toByte
argument_list|(
name|Byte
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Byte
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toByte
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Byte
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toByte
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectConverter
operator|.
name|toByte
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToShort ()
specifier|public
name|void
name|testToShort
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Short
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toShort
argument_list|(
name|Short
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Short
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toShort
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Short
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toShort
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectConverter
operator|.
name|toShort
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToInteger ()
specifier|public
name|void
name|testToInteger
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toInteger
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toInteger
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toInteger
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectConverter
operator|.
name|toInteger
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToLong ()
specifier|public
name|void
name|testToLong
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toLong
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toLong
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toLong
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectConverter
operator|.
name|toLong
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToFloat ()
specifier|public
name|void
name|testToFloat
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toFloat
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toFloat
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toFloat
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectConverter
operator|.
name|toFloat
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDouble ()
specifier|public
name|void
name|testToDouble
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toDouble
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toDouble
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|"4"
argument_list|)
argument_list|,
name|ObjectConverter
operator|.
name|toDouble
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ObjectConverter
operator|.
name|toDouble
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"ABC"
argument_list|,
name|ObjectConverter
operator|.
name|toString
argument_list|(
operator|new
name|StringBuffer
argument_list|(
literal|"ABC"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABC"
argument_list|,
name|ObjectConverter
operator|.
name|toString
argument_list|(
operator|new
name|StringBuilder
argument_list|(
literal|"ABC"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|ObjectConverter
operator|.
name|toString
argument_list|(
operator|new
name|StringBuffer
argument_list|(
literal|""
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|ObjectConverter
operator|.
name|toString
argument_list|(
operator|new
name|StringBuilder
argument_list|(
literal|""
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

