begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|NoSuchBeanException
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
name|Before
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
DECL|class|SimpleRegistryTest
specifier|public
class|class
name|SimpleRegistryTest
extends|extends
name|Assert
block|{
DECL|field|registry
specifier|private
name|SimpleRegistry
name|registry
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|registry
operator|=
operator|new
name|SimpleRegistry
argument_list|()
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupByName ()
specifier|public
name|void
name|testLookupByName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|registry
operator|.
name|lookupByName
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupByWrongName ()
specifier|public
name|void
name|testLookupByWrongName
parameter_list|()
block|{
name|assertNull
argument_list|(
name|registry
operator|.
name|lookupByName
argument_list|(
literal|"x"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupByNameAndType ()
specifier|public
name|void
name|testLookupByNameAndType
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|registry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"a"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupByNameAndWrongType ()
specifier|public
name|void
name|testLookupByNameAndWrongType
parameter_list|()
block|{
try|try
block|{
name|registry
operator|.
name|lookupByNameAndType
argument_list|(
literal|"a"
argument_list|,
name|Float
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
comment|// expected
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"of type: java.lang.String expected type was: class java.lang.Float"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testLookupByType ()
specifier|public
name|void
name|testLookupByType
parameter_list|()
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|registry
operator|.
name|findByTypeWithName
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
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
literal|"b"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|registry
operator|.
name|findByTypeWithName
argument_list|(
name|Object
operator|.
name|class
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
literal|"b"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLookupByWrongType ()
specifier|public
name|void
name|testLookupByWrongType
parameter_list|()
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|registry
operator|.
name|findByTypeWithName
argument_list|(
name|Float
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
