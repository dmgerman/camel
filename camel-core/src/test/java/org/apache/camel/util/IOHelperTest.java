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
name|io
operator|.
name|PrintWriter
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
name|impl
operator|.
name|DefaultCamelContext
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
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|IOHelperTest
specifier|public
class|class
name|IOHelperTest
extends|extends
name|TestCase
block|{
DECL|method|testIOException ()
specifier|public
name|void
name|testIOException
parameter_list|()
block|{
name|IOException
name|io
init|=
operator|new
name|IOException
argument_list|(
literal|"Damn"
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Damn"
argument_list|,
name|io
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|io
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
DECL|method|testIOExceptionWithMessage ()
specifier|public
name|void
name|testIOExceptionWithMessage
parameter_list|()
block|{
name|IOException
name|io
init|=
operator|new
name|IOException
argument_list|(
literal|"Not again"
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Not again"
argument_list|,
name|io
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|io
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewStringFromBytes ()
specifier|public
name|void
name|testNewStringFromBytes
parameter_list|()
block|{
name|String
name|s
init|=
name|IOHelper
operator|.
name|newStringFromBytes
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewStringFromBytesWithStart ()
specifier|public
name|void
name|testNewStringFromBytesWithStart
parameter_list|()
block|{
name|String
name|s
init|=
name|IOHelper
operator|.
name|newStringFromBytes
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"llo"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyAndCloseInput ()
specifier|public
name|void
name|testCopyAndCloseInput
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
literal|256
argument_list|)
expr_stmt|;
block|}
DECL|method|testCharsetNormalize ()
specifier|public
name|void
name|testCharsetNormalize
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|IOHelper
operator|.
name|normalizeCharset
argument_list|(
literal|"'UTF-8'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|IOHelper
operator|.
name|normalizeCharset
argument_list|(
literal|"\"UTF-8\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|IOHelper
operator|.
name|normalizeCharset
argument_list|(
literal|"\"UTF-8 \""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|IOHelper
operator|.
name|normalizeCharset
argument_list|(
literal|"\' UTF-8\'"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLine1 ()
specifier|public
name|void
name|testLine1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertReadAsWritten
argument_list|(
literal|"line1"
argument_list|,
literal|"line1"
argument_list|,
literal|"line1\n"
argument_list|)
expr_stmt|;
block|}
DECL|method|testLine1LF ()
specifier|public
name|void
name|testLine1LF
parameter_list|()
throws|throws
name|Exception
block|{
name|assertReadAsWritten
argument_list|(
literal|"line1LF"
argument_list|,
literal|"line1\n"
argument_list|,
literal|"line1\n"
argument_list|)
expr_stmt|;
block|}
DECL|method|testLine2 ()
specifier|public
name|void
name|testLine2
parameter_list|()
throws|throws
name|Exception
block|{
name|assertReadAsWritten
argument_list|(
literal|"line2"
argument_list|,
literal|"line1\nline2"
argument_list|,
literal|"line1\nline2\n"
argument_list|)
expr_stmt|;
block|}
DECL|method|testLine2LF ()
specifier|public
name|void
name|testLine2LF
parameter_list|()
throws|throws
name|Exception
block|{
name|assertReadAsWritten
argument_list|(
literal|"line2LF"
argument_list|,
literal|"line1\nline2\n"
argument_list|,
literal|"line1\nline2\n"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertReadAsWritten (String testname, String text, String compareText)
specifier|private
name|void
name|assertReadAsWritten
parameter_list|(
name|String
name|testname
parameter_list|,
name|String
name|text
parameter_list|,
name|String
name|compareText
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|file
init|=
name|tempFile
argument_list|(
name|testname
argument_list|)
decl_stmt|;
name|write
argument_list|(
name|file
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|String
name|loadText
init|=
name|IOHelper
operator|.
name|loadText
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|compareText
argument_list|,
name|loadText
argument_list|)
expr_stmt|;
block|}
DECL|method|tempFile (String testname)
specifier|private
name|File
name|tempFile
parameter_list|(
name|String
name|testname
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|File
operator|.
name|createTempFile
argument_list|(
name|testname
argument_list|,
literal|""
argument_list|)
return|;
block|}
DECL|method|write (File file, String text)
specifier|private
name|void
name|write
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|text
parameter_list|)
throws|throws
name|Exception
block|{
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|out
operator|.
name|print
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|testCharsetName ()
specifier|public
name|void
name|testCharsetName
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"iso-8859-1"
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"iso-8859-1"
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetCharsetNameFromContentType ()
specifier|public
name|void
name|testGetCharsetNameFromContentType
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|charsetName
init|=
name|IOHelper
operator|.
name|getCharsetNameFromContentType
argument_list|(
literal|"text/html; charset=iso-8859-1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"iso-8859-1"
argument_list|,
name|charsetName
argument_list|)
expr_stmt|;
name|charsetName
operator|=
name|IOHelper
operator|.
name|getCharsetNameFromContentType
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|charsetName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

