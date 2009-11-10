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
DECL|class|CollectionHelperTest
specifier|public
class|class
name|CollectionHelperTest
extends|extends
name|TestCase
block|{
DECL|field|names
specifier|private
name|String
index|[]
name|names
init|=
operator|new
name|String
index|[]
block|{
literal|"Claus"
block|,
literal|"Willem"
block|,
literal|"Jonathan"
block|}
decl_stmt|;
DECL|field|list
specifier|private
name|List
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|names
argument_list|)
decl_stmt|;
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
name|names
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus,Willem,Jonathan"
argument_list|,
name|CollectionHelper
operator|.
name|collectionAsCommaDelimitedString
argument_list|(
name|list
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
operator|(
name|String
index|[]
operator|)
literal|null
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
operator|(
name|Collection
operator|)
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
operator|new
name|String
index|[]
block|{
literal|"Claus"
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
block|{
name|Map
name|map
init|=
operator|new
name|HashMap
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
DECL|method|testAppendValue ()
specifier|public
name|void
name|testAppendValue
parameter_list|()
block|{
name|Map
name|map
init|=
operator|new
name|HashMap
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
name|values
init|=
operator|(
name|List
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
block|}
end_class

end_unit

