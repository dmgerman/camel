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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|CaseInsensitiveMap
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link CollectionConverter}  */
end_comment

begin_class
DECL|class|CollectionConverterTest
specifier|public
class|class
name|CollectionConverterTest
extends|extends
name|TestCase
block|{
DECL|field|SMURFS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|SMURFS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Papa smurf"
argument_list|,
literal|"Smurfette"
argument_list|,
literal|"Hefty smurf"
argument_list|,
literal|"Jokey smurf"
argument_list|)
decl_stmt|;
DECL|method|testIteratorToList ()
specifier|public
name|void
name|testIteratorToList
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSmurfs
argument_list|(
name|CollectionConverter
operator|.
name|toArrayList
argument_list|(
name|SMURFS
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIterableToList ()
specifier|public
name|void
name|testIterableToList
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSmurfs
argument_list|(
name|CollectionConverter
operator|.
name|toList
argument_list|(
operator|new
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|SMURFS
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
comment|// no conversion should occur for the list itself
name|assertSame
argument_list|(
name|SMURFS
argument_list|,
name|CollectionConverter
operator|.
name|toList
argument_list|(
operator|(
name|Iterable
argument_list|<
name|String
argument_list|>
operator|)
name|SMURFS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertSmurfs (Collection<String> result)
specifier|private
name|void
name|assertSmurfs
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|result
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|SMURFS
operator|.
name|size
argument_list|()
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|result
control|)
block|{
name|assertTrue
argument_list|(
name|SMURFS
operator|.
name|contains
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testToArray ()
specifier|public
name|void
name|testToArray
parameter_list|()
block|{
name|Object
index|[]
name|data
init|=
name|CollectionConverter
operator|.
name|toArray
argument_list|(
name|SMURFS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|testToList ()
specifier|public
name|void
name|testToList
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|out
init|=
name|CollectionConverter
operator|.
name|toList
argument_list|(
name|SMURFS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToSet ()
specifier|public
name|void
name|testToSet
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|out
init|=
name|CollectionConverter
operator|.
name|toSet
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToHashMap ()
specifier|public
name|void
name|testToHashMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|CaseInsensitiveMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|out
init|=
name|CollectionConverter
operator|.
name|toHashMap
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToHashtable ()
specifier|public
name|void
name|testToHashtable
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|CaseInsensitiveMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|out
init|=
name|CollectionConverter
operator|.
name|toHashtable
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToProperties ()
specifier|public
name|void
name|testToProperties
parameter_list|()
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|Properties
name|prop
init|=
name|CollectionConverter
operator|.
name|toProperties
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|prop
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
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|prop
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

