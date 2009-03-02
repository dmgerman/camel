begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|stream
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|StreamCache
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link StreamCacheConverter}  */
end_comment

begin_class
DECL|class|StreamCacheConverterTest
specifier|public
class|class
name|StreamCacheConverterTest
extends|extends
name|TestCase
block|{
DECL|field|TEST_FILE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_FILE
init|=
literal|"org/apache/camel/converter/stream/test.xml"
decl_stmt|;
DECL|field|converter
specifier|private
name|StreamCacheConverter
name|converter
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
name|this
operator|.
name|converter
operator|=
operator|new
name|StreamCacheConverter
argument_list|()
expr_stmt|;
block|}
DECL|method|testConvertToStreamCacheStreamSource ()
specifier|public
name|void
name|testConvertToStreamCacheStreamSource
parameter_list|()
throws|throws
name|IOException
throws|,
name|FileNotFoundException
throws|,
name|TransformerException
block|{
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
name|getTestFileStream
argument_list|()
argument_list|)
decl_stmt|;
name|StreamCache
name|cache
init|=
name|converter
operator|.
name|convertToStreamCache
argument_list|(
name|source
argument_list|)
decl_stmt|;
comment|//assert re-readability of the cached StreamSource
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|converter
operator|.
name|toString
argument_list|(
operator|(
name|Source
operator|)
name|cache
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|converter
operator|.
name|toString
argument_list|(
operator|(
name|Source
operator|)
name|cache
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertToStreamCacheInputStream ()
specifier|public
name|void
name|testConvertToStreamCacheInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|getTestFileStream
argument_list|()
decl_stmt|;
name|InputStream
name|cache
init|=
operator|(
name|InputStream
operator|)
name|converter
operator|.
name|convertToStreamCache
argument_list|(
name|is
argument_list|)
decl_stmt|;
comment|//assert re-readability of the cached InputStream
name|assertNotNull
argument_list|(
name|IOConverter
operator|.
name|toString
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|IOConverter
operator|.
name|toString
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getTestFileStream ()
specifier|protected
name|InputStream
name|getTestFileStream
parameter_list|()
block|{
name|InputStream
name|answer
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_FILE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found the file: "
operator|+
name|TEST_FILE
operator|+
literal|" on the classpath"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

