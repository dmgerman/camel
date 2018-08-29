begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|converter
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|CamelContext
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
name|converter
operator|.
name|IOConverter
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
name|DefaultCamelContext
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
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_class
DECL|class|ResourceConverterTest
specifier|public
class|class
name|ResourceConverterTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testResourceConverterRegistry ()
specifier|public
name|void
name|testResourceConverterRegistry
parameter_list|()
block|{
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|getResourceTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNonNullConversion ()
specifier|public
name|void
name|testNonNullConversion
parameter_list|()
throws|throws
name|IOException
block|{
name|Resource
name|resource
init|=
operator|new
name|ClassPathResource
argument_list|(
literal|"testresource.txt"
argument_list|,
name|ResourceConverterTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|resource
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|inputStream
init|=
name|getResourceTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|byte
index|[]
name|resourceBytes
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|resource
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|inputStreamBytes
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|resourceBytes
argument_list|,
name|inputStreamBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|getResourceTypeConverter ()
specifier|private
name|TypeConverter
name|getResourceTypeConverter
parameter_list|()
block|{
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|TypeConverter
name|typeConverter
init|=
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|Resource
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|typeConverter
return|;
block|}
block|}
end_class

end_unit

