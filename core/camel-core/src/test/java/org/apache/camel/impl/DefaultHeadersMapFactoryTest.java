begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|Map
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
name|impl
operator|.
name|engine
operator|.
name|DefaultHeadersMapFactory
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
DECL|class|DefaultHeadersMapFactoryTest
specifier|public
class|class
name|DefaultHeadersMapFactoryTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
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
name|DefaultHeadersMapFactory
argument_list|()
operator|.
name|newMap
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
annotation|@
name|Test
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
name|DefaultHeadersMapFactory
argument_list|()
operator|.
name|newMap
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
name|DefaultHeadersMapFactory
argument_list|()
operator|.
name|newMap
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
annotation|@
name|Test
DECL|method|testIsInstance ()
specifier|public
name|void
name|testIsInstance
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
name|DefaultHeadersMapFactory
argument_list|()
operator|.
name|newMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|other
init|=
operator|new
name|DefaultHeadersMapFactory
argument_list|()
operator|.
name|newMap
argument_list|(
name|map
argument_list|)
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
name|assertTrue
argument_list|(
operator|new
name|DefaultHeadersMapFactory
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|DefaultHeadersMapFactory
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|other
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|DefaultHeadersMapFactory
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

