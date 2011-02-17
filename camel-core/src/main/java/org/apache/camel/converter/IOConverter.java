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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

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
name|FileNotFoundException
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInput
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
name|ObjectOutput
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|charset
operator|.
name|UnsupportedCharsetException
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
name|Converter
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
name|util
operator|.
name|IOHelper
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
name|ObjectHelper
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

begin_comment
comment|/**  * Some core java.io based<a  * href="http://camel.apache.org/type-converter.html">Type Converters</a>  *  * @version   */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|IOConverter
specifier|public
specifier|final
class|class
name|IOConverter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|IOConverter
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|IOConverter ()
specifier|private
name|IOConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toInputStream (URL url)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|url
operator|.
name|openStream
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (File file)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toReader (File file)
specifier|public
specifier|static
name|BufferedReader
name|toReader
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toReader
argument_list|(
name|file
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toReader (File file, Exchange exchange)
specifier|public
specifier|static
name|BufferedReader
name|toReader
parameter_list|(
name|File
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|BufferedReader
argument_list|(
operator|new
name|EncodingFileReader
argument_list|(
name|file
argument_list|,
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toFile (String name)
specifier|public
specifier|static
name|File
name|toFile
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|File
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toOutputStream (File file)
specifier|public
specifier|static
name|OutputStream
name|toOutputStream
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toWriter (File file)
specifier|public
specifier|static
name|BufferedWriter
name|toWriter
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toWriter
argument_list|(
name|file
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toWriter (File file, Exchange exchange)
specifier|public
specifier|static
name|BufferedWriter
name|toWriter
parameter_list|(
name|File
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|EncodingFileWriter
argument_list|(
name|file
argument_list|,
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toReader (InputStream in)
specifier|public
specifier|static
name|Reader
name|toReader
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toReader
argument_list|(
name|in
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toReader (InputStream in, Exchange exchange)
specifier|public
specifier|static
name|Reader
name|toReader
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toWriter (OutputStream out)
specifier|public
specifier|static
name|Writer
name|toWriter
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toWriter
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
annotation|@
name|Deprecated
DECL|method|toWriter (OutputStream out, Exchange exchange)
specifier|public
specifier|static
name|Writer
name|toWriter
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toReader (String text)
specifier|public
specifier|static
name|StringReader
name|toReader
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|StringReader
argument_list|(
name|text
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toInputStream (String text)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toInputStream
argument_list|(
name|text
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (String text, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|String
name|text
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toInputStream
argument_list|(
name|text
operator|.
name|getBytes
argument_list|(
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toInputStream (BufferedReader buffer)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|BufferedReader
name|buffer
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toInputStream
argument_list|(
name|buffer
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (BufferedReader buffer, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|BufferedReader
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toInputStream
argument_list|(
name|toString
argument_list|(
name|buffer
argument_list|)
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toString (byte[] data)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|data
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (byte[] data, Exchange exchange)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|String
argument_list|(
name|data
argument_list|,
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toString (File file)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|file
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (File file, Exchange exchange)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|File
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|toReader
argument_list|(
name|file
argument_list|,
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteArray (File file)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|toBytes
argument_list|(
name|is
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
literal|"file"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Deprecated
DECL|method|toByteArray (Reader reader)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|Reader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toByteArray
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteArray (Reader reader, Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|Reader
name|reader
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|reader
operator|instanceof
name|BufferedReader
condition|)
block|{
return|return
name|toByteArray
argument_list|(
operator|(
name|BufferedReader
operator|)
name|reader
argument_list|,
name|exchange
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|toByteArray
argument_list|(
operator|new
name|BufferedReader
argument_list|(
name|reader
argument_list|)
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
annotation|@
name|Deprecated
DECL|method|toString (URL url)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|url
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (URL url, Exchange exchange)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|URL
name|url
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|url
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|toString
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
literal|"url"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toString (Reader reader)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Reader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|reader
operator|instanceof
name|BufferedReader
condition|)
block|{
return|return
name|toString
argument_list|(
operator|(
name|BufferedReader
operator|)
name|reader
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|toString
argument_list|(
operator|new
name|BufferedReader
argument_list|(
name|reader
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toString (BufferedReader reader)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|BufferedReader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|1024
argument_list|)
decl_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|1024
index|]
decl_stmt|;
try|try
block|{
name|int
name|len
init|=
literal|0
decl_stmt|;
comment|// read until we reach then end which is the -1 marker
while|while
condition|(
name|len
operator|!=
operator|-
literal|1
condition|)
block|{
name|len
operator|=
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
if|if
condition|(
name|len
operator|!=
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|,
literal|"reader"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toByteArray (BufferedReader reader)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|BufferedReader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toByteArray
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteArray (BufferedReader reader, Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|BufferedReader
name|reader
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toByteArray
argument_list|(
name|toString
argument_list|(
name|reader
argument_list|)
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toByteArray (String value)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toByteArray
argument_list|(
name|value
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteArray (String value, Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|String
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|getBytes
argument_list|(
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toString (InputStream in)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|in
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (InputStream in, Exchange exchange)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|toReader
argument_list|(
name|in
argument_list|,
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (byte[] data)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toObjectOutput (OutputStream stream)
specifier|public
specifier|static
name|ObjectOutput
name|toObjectOutput
parameter_list|(
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|stream
operator|instanceof
name|ObjectOutput
condition|)
block|{
return|return
operator|(
name|ObjectOutput
operator|)
name|stream
return|;
block|}
else|else
block|{
return|return
operator|new
name|ObjectOutputStream
argument_list|(
name|stream
argument_list|)
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toObjectInput (InputStream stream)
specifier|public
specifier|static
name|ObjectInput
name|toObjectInput
parameter_list|(
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|stream
operator|instanceof
name|ObjectInput
condition|)
block|{
return|return
operator|(
name|ObjectInput
operator|)
name|stream
return|;
block|}
else|else
block|{
return|return
operator|new
name|ObjectInputStream
argument_list|(
name|stream
argument_list|)
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toBytes (InputStream stream)
specifier|public
specifier|static
name|byte
index|[]
name|toBytes
parameter_list|(
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|stream
argument_list|,
name|bos
argument_list|)
expr_stmt|;
return|return
name|bos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|bos
argument_list|,
literal|"stream"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toByteArray (ByteArrayOutputStream os)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|ByteArrayOutputStream
name|os
parameter_list|)
block|{
return|return
name|os
operator|.
name|toByteArray
argument_list|()
return|;
block|}
annotation|@
name|Deprecated
DECL|method|toString (ByteArrayOutputStream os)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|ByteArrayOutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|os
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (ByteArrayOutputStream os, Exchange exchange)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|ByteArrayOutputStream
name|os
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|os
operator|.
name|toString
argument_list|(
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (ByteArrayOutputStream os)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|ByteArrayOutputStream
name|os
parameter_list|)
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getCharsetName (Exchange exchange)
specifier|public
specifier|static
name|String
name|getCharsetName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Gets the charset name if set as property {@link Exchange#CHARSET_NAME}.      *      * @param exchange  the exchange      * @param useDefault should we fallback and use JVM default charset if no property existed?      * @return the charset, or<tt>null</tt> if no found      */
DECL|method|getCharsetName (Exchange exchange, boolean useDefault)
specifier|public
specifier|static
name|String
name|getCharsetName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|useDefault
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|String
name|charsetName
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|charsetName
operator|!=
literal|null
condition|)
block|{
return|return
name|IOConverter
operator|.
name|normalizeCharset
argument_list|(
name|charsetName
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|useDefault
condition|)
block|{
return|return
name|getDefaultCharsetName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getDefaultCharsetName ()
specifier|public
specifier|static
name|String
name|getDefaultCharsetName
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|getSystemProperty
argument_list|(
name|Exchange
operator|.
name|DEFAULT_CHARSET_PROPERTY
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
comment|/**      * Encoding-aware file reader.       */
DECL|class|EncodingFileReader
specifier|private
specifier|static
class|class
name|EncodingFileReader
extends|extends
name|InputStreamReader
block|{
comment|/**          * @param file file to read          * @param charset character set to use          */
DECL|method|EncodingFileReader (File file, String charset)
specifier|public
name|EncodingFileReader
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|UnsupportedEncodingException
block|{
name|super
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Encoding-aware file writer.       */
DECL|class|EncodingFileWriter
specifier|private
specifier|static
class|class
name|EncodingFileWriter
extends|extends
name|OutputStreamWriter
block|{
comment|/**          * @param file file to write          * @param charset character set to use          */
DECL|method|EncodingFileWriter (File file, String charset)
specifier|public
name|EncodingFileWriter
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|UnsupportedEncodingException
block|{
name|super
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method will take off the quotes and double quotes of the charset      */
DECL|method|normalizeCharset (String charset)
specifier|public
specifier|static
name|String
name|normalizeCharset
parameter_list|(
name|String
name|charset
parameter_list|)
block|{
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|String
name|answer
init|=
name|charset
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|.
name|startsWith
argument_list|(
literal|"'"
argument_list|)
operator|||
name|answer
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|answer
operator|=
name|answer
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|.
name|endsWith
argument_list|(
literal|"'"
argument_list|)
operator|||
name|answer
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|answer
operator|=
name|answer
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
operator|.
name|trim
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|validateCharset (String charset)
specifier|public
specifier|static
name|void
name|validateCharset
parameter_list|(
name|String
name|charset
parameter_list|)
throws|throws
name|UnsupportedCharsetException
block|{
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Charset
operator|.
name|isSupported
argument_list|(
name|charset
argument_list|)
condition|)
block|{
name|Charset
operator|.
name|forName
argument_list|(
name|charset
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
throw|throw
operator|new
name|UnsupportedCharsetException
argument_list|(
name|charset
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

