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
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|KeyValueHolderTest
specifier|public
class|class
name|KeyValueHolderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testKeyValueHolder ()
specifier|public
name|void
name|testKeyValueHolder
parameter_list|()
block|{
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|foo
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|foo
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|foo
operator|.
name|getValue
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEqualsAndHashCodeOnEqualObjects ()
specifier|public
name|void
name|testEqualsAndHashCodeOnEqualObjects
parameter_list|()
block|{
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|foo1
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|foo2
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be equals"
argument_list|,
name|foo1
operator|.
name|equals
argument_list|(
name|foo2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Hash code should be equal"
argument_list|,
name|foo1
operator|.
name|hashCode
argument_list|()
operator|==
name|foo2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEqualsAndHashCodeOnUnequalObjects ()
specifier|public
name|void
name|testEqualsAndHashCodeOnUnequalObjects
parameter_list|()
block|{
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|foo
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|bar
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"bar"
argument_list|,
literal|678
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be equals"
argument_list|,
name|foo
operator|.
name|equals
argument_list|(
name|bar
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Hash code should not be equal"
argument_list|,
name|foo
operator|.
name|hashCode
argument_list|()
operator|==
name|bar
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEqualsAndHashCodeOnUnequalObjectsWithSameKeys ()
specifier|public
name|void
name|testEqualsAndHashCodeOnUnequalObjectsWithSameKeys
parameter_list|()
block|{
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|foo1
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|foo2
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|678
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be equals"
argument_list|,
name|foo1
operator|.
name|equals
argument_list|(
name|foo2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Hash code should not be equal"
argument_list|,
name|foo1
operator|.
name|hashCode
argument_list|()
operator|==
name|foo2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEqualsAndHashCodeOnUnequalObjectsWithSameValues ()
specifier|public
name|void
name|testEqualsAndHashCodeOnUnequalObjectsWithSameValues
parameter_list|()
block|{
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|foo
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|bar
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be equals"
argument_list|,
name|foo
operator|.
name|equals
argument_list|(
name|bar
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Hash code should not be equal"
argument_list|,
name|foo
operator|.
name|hashCode
argument_list|()
operator|==
name|bar
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|KeyValueHolder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|foo
init|=
operator|new
name|KeyValueHolder
argument_list|<>
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo -> 123"
argument_list|,
name|foo
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

