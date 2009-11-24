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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
DECL|class|CaseInsensitiveMapTest
specifier|public
class|class
name|CaseInsensitiveMapTest
extends|extends
name|TestCase
block|{
DECL|method|testLookupCaseAgnostic ()
specifier|public
name|void
name|testLookupCaseAgnostic
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
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLookupCaseAgnosticAddHeader ()
specifier|public
name|void
name|testLookupCaseAgnosticAddHeader
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
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLookupCaseAgnosticAddHeader2 ()
specifier|public
name|void
name|testLookupCaseAgnosticAddHeader2
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
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLookupCaseAgnosticAddHeaderRemoveHeader ()
specifier|public
name|void
name|testLookupCaseAgnosticAddHeaderRemoveHeader
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
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"beer"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|remove
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSetWithDifferentCase ()
specifier|public
name|void
name|testSetWithDifferentCase
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
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"Foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoveWithDifferentCase ()
specifier|public
name|void
name|testRemoveWithDifferentCase
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
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"Foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|remove
argument_list|(
literal|"FOO"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPutAll ()
specifier|public
name|void
name|testPutAll
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
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|other
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|other
operator|.
name|put
argument_list|(
literal|"Foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|other
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"BaR"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstructFromOther ()
specifier|public
name|void
name|testConstructFromOther
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|other
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|other
operator|.
name|put
argument_list|(
literal|"Foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|other
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
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
argument_list|(
name|other
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"BaR"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeySet ()
specifier|public
name|void
name|testKeySet
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
literal|"Foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"BAR"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"baZ"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|Set
name|keys
init|=
name|map
operator|.
name|keySet
argument_list|()
decl_stmt|;
comment|// we should be able to lookup no matter what case
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"baZ"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"Baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"BAZ"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRetainKeysCopyToAnotherMap ()
specifier|public
name|void
name|testRetainKeysCopyToAnotherMap
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
literal|"Foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"BAR"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"baZ"
argument_list|,
literal|"beer"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|other
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|map
argument_list|)
decl_stmt|;
comment|// we should retain the cases of the original keys
comment|// when its copied to another map
name|assertTrue
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"BAR"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"baZ"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"Baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|other
operator|.
name|containsKey
argument_list|(
literal|"BAZ"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testValues ()
specifier|public
name|void
name|testValues
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
literal|"Foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"BAR"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"baZ"
argument_list|,
literal|"Beer"
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// should be String values
name|assertEquals
argument_list|(
literal|"String"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
name|values
init|=
name|map
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|"Beer"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRomeks ()
specifier|public
name|void
name|testRomeks
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
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"fOo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"FOO"
argument_list|,
literal|"cake"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cake"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"fOo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRomeksUsingRegularHashMap ()
specifier|public
name|void
name|testRomeksUsingRegularHashMap
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
name|HashMap
argument_list|<
name|String
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
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"fOo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"FOO"
argument_list|,
literal|"cake"
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
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"fOo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cake"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRomeksTransferedToHashMapAfterwards ()
specifier|public
name|void
name|testRomeksTransferedToHashMapAfterwards
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
literal|"Foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"FOO"
argument_list|,
literal|"cake"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|map
operator|.
name|containsKey
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|other
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|other
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|other
operator|.
name|containsKey
argument_list|(
literal|"FOO"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|other
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
throws|throws
name|Exception
block|{
name|CaseInsensitiveMap
name|testMap
init|=
operator|new
name|CaseInsensitiveMap
argument_list|()
decl_stmt|;
name|testMap
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
comment|// force entry set to be created which could cause the map to be non serializable
name|testMap
operator|.
name|entrySet
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|bStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ObjectOutputStream
name|objStream
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bStream
argument_list|)
decl_stmt|;
name|objStream
operator|.
name|writeObject
argument_list|(
name|testMap
argument_list|)
expr_stmt|;
name|ObjectInputStream
name|inStream
init|=
operator|new
name|ObjectInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bStream
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|CaseInsensitiveMap
name|testMapCopy
init|=
operator|(
name|CaseInsensitiveMap
operator|)
name|inStream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|testMapCopy
operator|.
name|containsKey
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

