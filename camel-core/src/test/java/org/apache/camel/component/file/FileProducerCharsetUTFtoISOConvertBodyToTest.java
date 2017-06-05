begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|ContextTestSupport
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
name|Exchange
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
name|builder
operator|.
name|RouteBuilder
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
name|IOHelper
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|FileProducerCharsetUTFtoISOConvertBodyToTest
specifier|public
class|class
name|FileProducerCharsetUTFtoISOConvertBodyToTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|utf
specifier|private
name|byte
index|[]
name|utf
decl_stmt|;
DECL|field|iso
specifier|private
name|byte
index|[]
name|iso
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
comment|// use utf-8 as original payload with 00e6 which is a danish ae letter
name|utf
operator|=
literal|"ABC\u00e6"
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
expr_stmt|;
name|iso
operator|=
literal|"ABC\u00e6"
operator|.
name|getBytes
argument_list|(
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/charset"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/charset/input"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"utf: {}"
argument_list|,
operator|new
name|String
argument_list|(
name|utf
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"utf-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"iso: {}"
argument_list|,
operator|new
name|String
argument_list|(
name|iso
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"iso-8859-1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|utf
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"utf byte: {}"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|byte
name|b
range|:
name|iso
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"iso byte: {}"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
comment|// write the byte array to a file using plain API
name|OutputStream
name|fos
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/charset/input/input.txt"
argument_list|)
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|utf
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testFileProducerCharsetUTFtoISOConvertBodyTo ()
specifier|public
name|void
name|testFileProducerCharsetUTFtoISOConvertBodyTo
parameter_list|()
throws|throws
name|Exception
block|{
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/charset/output.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|fis
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|100
index|]
decl_stmt|;
name|int
name|len
init|=
name|fis
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should read data: "
operator|+
name|len
argument_list|,
name|len
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
name|len
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|data
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|data
control|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"loaded byte: {}"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
comment|// data should be in iso, where the danish ae is -26
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|65
argument_list|,
name|data
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|66
argument_list|,
name|data
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|67
argument_list|,
name|data
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|26
argument_list|,
name|data
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the input file is in utf-8
name|from
argument_list|(
literal|"file:target/charset/input?noop=true&charset=utf-8"
argument_list|)
comment|// now convert the input file from utf-8 to iso-8859-1
operator|.
name|convertBodyTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
literal|"iso-8859-1"
argument_list|)
comment|// and write the file using that encoding
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|header
argument_list|(
literal|"someCharsetHeader"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/charset/?fileName=output.txt"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

