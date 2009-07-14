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
name|ObjectHelper
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
DECL|method|testArrayAsIterator ()
specifier|public
name|void
name|testArrayAsIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|data
init|=
block|{
literal|"a"
block|,
literal|"b"
block|}
decl_stmt|;
name|Iterator
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"should have next"
argument_list|,
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|a
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"should have next"
argument_list|,
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|b
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
literal|"b"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"should not have a next"
argument_list|,
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsEmpty ()
specifier|public
name|void
name|testIsEmpty
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
literal|" A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
literal|" A "
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsNotEmpty ()
specifier|public
name|void
name|testIsNotEmpty
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
literal|" A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
literal|" A "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIteratorWithComma ()
specifier|public
name|void
name|testIteratorWithComma
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
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
DECL|method|testIteratorWithOtherDelimiter ()
specifier|public
name|void
name|testIteratorWithOtherDelimiter
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
literal|"Claus#Jonathan"
argument_list|,
literal|"#"
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
DECL|method|testIteratorEmpty ()
specifier|public
name|void
name|testIteratorEmpty
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
literal|""
argument_list|)
decl_stmt|;
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
name|it
operator|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
literal|"    "
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
name|it
operator|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
literal|null
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
block|}
end_class

end_unit

