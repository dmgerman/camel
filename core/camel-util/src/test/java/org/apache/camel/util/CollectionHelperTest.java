begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|LinkedHashMap
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

begin_class
DECL|class|CollectionHelperTest
specifier|public
class|class
name|CollectionHelperTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testCollectionAsCommaDelimitedString ()
specifier|public
name|void
name|testCollectionAsCommaDelimitedString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Claus,Willem,Jonathan"
argument_list|,
name|CollectionHelper
operator|.
name|collectionAsCommaDelimitedString
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Claus"
argument_list|,
literal|"Willem"
argument_list|,
literal|"Jonathan"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|CollectionHelper
operator|.
name|collectionAsCommaDelimitedString
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|CollectionHelper
operator|.
name|collectionAsCommaDelimitedString
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"Claus"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSize ()
specifier|public
name|void
name|testSize
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
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|CollectionHelper
operator|.
name|size
argument_list|(
name|map
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|array
init|=
operator|new
name|String
index|[]
block|{
literal|"Claus"
block|,
literal|"Willem"
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|CollectionHelper
operator|.
name|size
argument_list|(
name|array
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAppendValue ()
specifier|public
name|void
name|testAppendValue
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
argument_list|<>
argument_list|()
decl_stmt|;
name|CollectionHelper
operator|.
name|appendValue
argument_list|(
name|map
argument_list|,
literal|"foo"
argument_list|,
literal|123
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
name|CollectionHelper
operator|.
name|appendValue
argument_list|(
name|map
argument_list|,
literal|"foo"
argument_list|,
literal|456
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
name|CollectionHelper
operator|.
name|appendValue
argument_list|(
name|map
argument_list|,
literal|"bar"
argument_list|,
literal|789
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
name|List
argument_list|<
name|?
argument_list|>
name|values
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Integer
name|value
init|=
operator|(
name|Integer
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|789
argument_list|,
name|value
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateSetContaining ()
specifier|public
name|void
name|testCreateSetContaining
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
name|CollectionHelper
operator|.
name|createSetContaining
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"baz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|contains
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testflattenKeysInMap ()
specifier|public
name|void
name|testflattenKeysInMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|root
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|api
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contact
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|contact
operator|.
name|put
argument_list|(
literal|"organization"
argument_list|,
literal|"Apache Software Foundation"
argument_list|)
expr_stmt|;
name|api
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
literal|"1.0.0"
argument_list|)
expr_stmt|;
name|api
operator|.
name|put
argument_list|(
literal|"title"
argument_list|,
literal|"My cool API"
argument_list|)
expr_stmt|;
name|api
operator|.
name|put
argument_list|(
literal|"contact"
argument_list|,
name|contact
argument_list|)
expr_stmt|;
name|root
operator|.
name|put
argument_list|(
literal|"api"
argument_list|,
name|api
argument_list|)
expr_stmt|;
name|root
operator|.
name|put
argument_list|(
literal|"cors"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|flattern
init|=
name|CollectionHelper
operator|.
name|flattenKeysInMap
argument_list|(
name|root
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|flattern
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|flattern
operator|.
name|get
argument_list|(
literal|"cors"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0.0"
argument_list|,
name|flattern
operator|.
name|get
argument_list|(
literal|"api.version"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"My cool API"
argument_list|,
name|flattern
operator|.
name|get
argument_list|(
literal|"api.title"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Apache Software Foundation"
argument_list|,
name|flattern
operator|.
name|get
argument_list|(
literal|"api.contact.organization"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

