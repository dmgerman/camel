begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
operator|.
name|remote
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
name|InputStream
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|FtpProducerFileWithCharsetTest
specifier|public
class|class
name|FtpProducerFileWithCharsetTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FtpProducerFileWithCharsetTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|payload
specifier|private
name|String
name|payload
init|=
literal|"\u00e6\u00f8\u00e5 \u00a9"
decl_stmt|;
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/upload?charset=iso-8859-1&password=admin"
return|;
block|}
annotation|@
name|Override
annotation|@
name|BeforeEach
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|utf
init|=
name|payload
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|iso
init|=
name|payload
operator|.
name|getBytes
argument_list|(
literal|"iso-8859-1"
argument_list|)
decl_stmt|;
name|LOG
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
name|LOG
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
name|LOG
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"iso byte: {}"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerWithCharset ()
specifier|public
name|void
name|testProducerWithCharset
parameter_list|()
throws|throws
name|Exception
block|{
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
name|payload
argument_list|,
literal|"charset/iso.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/upload/charset/iso.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|file
operator|.
name|exists
argument_list|()
argument_list|,
literal|"The uploaded file should exists"
argument_list|)
expr_stmt|;
name|String
name|fileContent
init|=
operator|new
name|String
argument_list|(
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|file
argument_list|)
argument_list|,
literal|"iso-8859-1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|fileContent
argument_list|,
name|payload
argument_list|)
expr_stmt|;
comment|// Lets also test byte wise
name|InputStream
name|fis
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
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
name|len
operator|!=
operator|-
literal|1
argument_list|,
literal|"Should read data: "
operator|+
name|len
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
comment|// data should be in iso, where the danish ae is -26, oe is -8 aa is -27
comment|// and copyright is -87
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|26
argument_list|,
name|data
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|8
argument_list|,
name|data
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|27
argument_list|,
name|data
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|32
argument_list|,
name|data
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|87
argument_list|,
name|data
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

