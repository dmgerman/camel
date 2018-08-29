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
name|Test
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
name|Map
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
name|TestSupport
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|LRUSoftCacheTest
specifier|public
class|class
name|LRUSoftCacheTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testLRUSoftCacheGetAndPut ()
specifier|public
name|void
name|testLRUSoftCacheGetAndPut
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCacheHitsAndMisses ()
specifier|public
name|void
name|testLRUSoftCacheHitsAndMisses
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|get
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCachePutOverride ()
specifier|public
name|void
name|testLRUSoftCachePutOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|Object
name|old
init|=
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|old
argument_list|)
expr_stmt|;
name|old
operator|=
name|cache
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|old
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|old
operator|=
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"changed"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|old
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"changed"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCachePutAll ()
specifier|public
name|void
name|testLRUSoftCachePutAll
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCachePutAllAnotherLRUSoftCache ()
specifier|public
name|void
name|testLRUSoftCachePutAllAnotherLRUSoftCache
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache2
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache2
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|cache2
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|cache2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCacheRemove ()
specifier|public
name|void
name|testLRUSoftCacheRemove
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCacheValues ()
specifier|public
name|void
name|testLRUSoftCacheValues
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|col
init|=
name|cache
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|col
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|col
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCacheEmpty ()
specifier|public
name|void
name|testLRUSoftCacheEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCacheContainsKey ()
specifier|public
name|void
name|testLRUSoftCacheContainsKey
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLRUSoftCacheKeySet ()
specifier|public
name|void
name|testLRUSoftCacheKeySet
parameter_list|()
throws|throws
name|Exception
block|{
name|LRUSoftCache
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|cache
init|=
operator|new
name|LRUSoftCache
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|keys
init|=
name|cache
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|it
init|=
name|keys
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

