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
name|StringSource
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
name|TypeConverter
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
name|DefaultClassResolver
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
name|DefaultFactoryFinderResolver
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
name|DefaultPackageScanClassResolver
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
name|converter
operator|.
name|DefaultTypeConverter
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
name|ReflectionInjector
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|StringSourceTest
specifier|public
class|class
name|StringSourceTest
extends|extends
name|TestCase
block|{
DECL|field|converter
specifier|protected
name|TypeConverter
name|converter
init|=
operator|new
name|DefaultTypeConverter
argument_list|(
operator|new
name|DefaultPackageScanClassResolver
argument_list|()
argument_list|,
operator|new
name|ReflectionInjector
argument_list|()
argument_list|,
operator|new
name|DefaultFactoryFinderResolver
argument_list|()
operator|.
name|resolveDefaultFactoryFinder
argument_list|(
operator|new
name|DefaultClassResolver
argument_list|()
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|String
name|expectedBody
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|converter
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
name|StringSource
name|expected
init|=
operator|new
name|StringSource
argument_list|(
name|expectedBody
argument_list|,
literal|"mySystemID"
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|expected
operator|.
name|setPublicId
argument_list|(
literal|"myPublicId"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ObjectOutputStream
name|output
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|output
operator|.
name|writeObject
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
name|ObjectInputStream
name|in
init|=
operator|new
name|ObjectInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|in
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"is a StringSource"
argument_list|,
name|object
operator|instanceof
name|StringSource
argument_list|)
expr_stmt|;
name|StringSource
name|actual
init|=
operator|(
name|StringSource
operator|)
name|object
decl_stmt|;
name|assertEquals
argument_list|(
literal|"source.text"
argument_list|,
name|expected
operator|.
name|getPublicId
argument_list|()
argument_list|,
name|actual
operator|.
name|getPublicId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"source.text"
argument_list|,
name|expected
operator|.
name|getSystemId
argument_list|()
argument_list|,
name|actual
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"source.text"
argument_list|,
name|expected
operator|.
name|getEncoding
argument_list|()
argument_list|,
name|actual
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"source.text"
argument_list|,
name|expected
operator|.
name|getText
argument_list|()
argument_list|,
name|actual
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|actual
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"text value of StringSource"
argument_list|,
name|expectedBody
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

