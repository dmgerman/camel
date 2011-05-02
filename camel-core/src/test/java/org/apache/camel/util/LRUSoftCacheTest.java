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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
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
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
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
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
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
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
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
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
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
name|cache
operator|.
name|stop
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
DECL|method|testLRUSoftCacheStopEmpty ()
specifier|public
name|void
name|testLRUSoftCacheStopEmpty
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
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
name|stop
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
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
name|stop
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
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
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLRUSoftCacheNotRunOutOfMemory ()
specifier|public
name|void
name|testLRUSoftCacheNotRunOutOfMemory
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we should not run out of memory using the soft cache
comment|// if you run this test with LRUCache then you will run out of memory
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
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|250
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|data
init|=
name|createData
argument_list|()
decl_stmt|;
name|Integer
name|key
init|=
operator|new
name|Integer
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Putting {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
name|int
name|size
init|=
name|cache
operator|.
name|size
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Cache size {}"
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Cache size should not be max, was: "
operator|+
name|size
argument_list|,
name|size
operator|<
name|cache
operator|.
name|getMaxCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// should be the last keys
name|List
argument_list|<
name|Integer
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|cache
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Keys: "
operator|+
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Cache size should not be max, was: "
operator|+
name|list
operator|.
name|size
argument_list|()
argument_list|,
name|list
operator|.
name|size
argument_list|()
operator|<
name|cache
operator|.
name|getMaxCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// first key should not be 0
name|int
name|first
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"First key should not be 0, was: "
operator|+
name|first
argument_list|,
name|first
operator|!=
literal|0
argument_list|)
expr_stmt|;
comment|// last key should be 999
name|assertEquals
argument_list|(
literal|999
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|createData ()
specifier|private
name|Object
name|createData
parameter_list|()
block|{
comment|// 1mb data
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1
operator|*
literal|1024
operator|*
literal|1024
index|]
decl_stmt|;
return|return
name|buf
return|;
block|}
block|}
end_class

end_unit

