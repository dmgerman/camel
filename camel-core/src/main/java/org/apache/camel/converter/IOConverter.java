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
name|ObjectStreamClass
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
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|CharBuffer
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
comment|/**  * Some core java.io based<a  * href="http://camel.apache.org/type-converter.html">Type Converters</a>  */
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
DECL|field|defaultCharset
specifier|static
name|Supplier
argument_list|<
name|Charset
argument_list|>
name|defaultCharset
init|=
name|Charset
operator|::
name|defaultCharset
decl_stmt|;
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
name|IOHelper
operator|.
name|buffered
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
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
return|;
block|}
comment|/**      * Converts the given {@link File} with the given charset to {@link InputStream} with the JVM default charset      *      * @param file the file to be converted      * @param charset the charset the file is read with      * @return the input stream with the JVM default charset      */
DECL|method|toInputStream (File file, String charset)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|EncodingInputStream
argument_list|(
name|file
argument_list|,
name|charset
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|toInputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
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
name|toReader
argument_list|(
name|file
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toReader (File file, String charset)
specifier|public
specifier|static
name|BufferedReader
name|toReader
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
name|FileInputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
return|return
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|EncodingFileReader
argument_list|(
name|in
argument_list|,
name|charset
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
name|IOHelper
operator|.
name|buffered
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
name|FileOutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|toWriter
argument_list|(
name|os
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toWriter (File file, boolean append, String charset)
specifier|public
specifier|static
name|BufferedWriter
name|toWriter
parameter_list|(
name|File
name|file
parameter_list|,
name|boolean
name|append
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
name|append
argument_list|)
argument_list|,
name|charset
argument_list|)
return|;
block|}
DECL|method|toWriter (FileOutputStream os, String charset)
specifier|public
specifier|static
name|BufferedWriter
name|toWriter
parameter_list|(
name|FileOutputStream
name|os
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|EncodingFileWriter
argument_list|(
name|os
argument_list|,
name|charset
argument_list|)
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
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
name|IOHelper
operator|.
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
DECL|method|toReader (byte[] data, Exchange exchange)
specifier|public
specifier|static
name|Reader
name|toReader
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
name|toReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
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
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
name|IOHelper
operator|.
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
comment|// no buffering required as the complete string input is already passed
comment|// over as a whole
return|return
operator|new
name|StringReader
argument_list|(
name|text
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
name|IOHelper
operator|.
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
DECL|method|toInputStream (StringBuffer buffer, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|StringBuffer
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
name|buffer
operator|.
name|toString
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (StringBuilder builder, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|StringBuilder
name|builder
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
name|builder
operator|.
name|toString
argument_list|()
argument_list|,
name|exchange
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
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
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
return|return
name|toByteArray
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|reader
argument_list|)
argument_list|,
name|exchange
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
return|return
name|toString
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|reader
argument_list|)
argument_list|)
return|;
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
decl_stmt|;
comment|// read until we reach then end which is the -1 marker
while|while
condition|(
operator|(
name|len
operator|=
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
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
name|String
name|s
init|=
name|toString
argument_list|(
name|reader
argument_list|)
decl_stmt|;
return|return
name|toByteArray
argument_list|(
name|s
argument_list|,
name|exchange
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
operator|.
name|getBytes
argument_list|(
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
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
comment|// no buffering required as the complete byte input is already passed
comment|// over as a whole
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
name|IOHelper
operator|.
name|buffered
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toObjectInput (final InputStream stream, final Exchange exchange)
specifier|public
specifier|static
name|ObjectInput
name|toObjectInput
parameter_list|(
specifier|final
name|InputStream
name|stream
parameter_list|,
specifier|final
name|Exchange
name|exchange
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
name|IOHelper
operator|.
name|buffered
argument_list|(
name|stream
argument_list|)
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|resolveClass
parameter_list|(
name|ObjectStreamClass
name|objectStreamClass
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
comment|// need to let Camel be able to resolve class using ClassResolver SPI, to let class loading
comment|// work in OSGi and other containers
name|Class
argument_list|<
name|?
argument_list|>
name|answer
init|=
literal|null
decl_stmt|;
name|String
name|name
init|=
name|objectStreamClass
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading class {} using Camel ClassResolver"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|answer
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading class {} using JDK default implementation"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|answer
operator|=
name|super
operator|.
name|resolveClass
argument_list|(
name|objectStreamClass
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
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
name|IOHelper
operator|.
name|copy
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
name|stream
argument_list|)
argument_list|,
name|bos
argument_list|)
expr_stmt|;
comment|// no need to close the ByteArrayOutputStream as it's close()
comment|// implementation is noop
return|return
name|bos
operator|.
name|toByteArray
argument_list|()
return|;
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
name|Converter
DECL|method|covertToByteBuffer (InputStream is)
specifier|public
specifier|static
name|ByteBuffer
name|covertToByteBuffer
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
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
argument_list|)
expr_stmt|;
return|return
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
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
name|IOHelper
operator|.
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
comment|// no buffering required as the complete byte array input is already
comment|// passed over as a whole
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
annotation|@
name|Converter
DECL|method|toProperties (File file)
specifier|public
specifier|static
name|Properties
name|toProperties
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toProperties
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
name|Converter
DECL|method|toProperties (InputStream is)
specifier|public
specifier|static
name|Properties
name|toProperties
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|prop
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
return|return
name|prop
return|;
block|}
annotation|@
name|Converter
DECL|method|toProperties (Reader reader)
specifier|public
specifier|static
name|Properties
name|toProperties
parameter_list|(
name|Reader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|prop
operator|.
name|load
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
return|return
name|prop
return|;
block|}
comment|/**      * Encoding-aware input stream.      */
DECL|class|EncodingInputStream
specifier|public
specifier|static
class|class
name|EncodingInputStream
extends|extends
name|InputStream
block|{
DECL|field|file
specifier|private
specifier|final
name|File
name|file
decl_stmt|;
DECL|field|reader
specifier|private
specifier|final
name|BufferedReader
name|reader
decl_stmt|;
DECL|field|defaultStreamCharset
specifier|private
specifier|final
name|Charset
name|defaultStreamCharset
decl_stmt|;
DECL|field|bufferBytes
specifier|private
name|ByteBuffer
name|bufferBytes
decl_stmt|;
DECL|field|bufferedChars
specifier|private
name|CharBuffer
name|bufferedChars
init|=
name|CharBuffer
operator|.
name|allocate
argument_list|(
literal|4096
argument_list|)
decl_stmt|;
DECL|method|EncodingInputStream (File file, String charset)
specifier|public
name|EncodingInputStream
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
name|reader
operator|=
name|toReader
argument_list|(
name|file
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|defaultStreamCharset
operator|=
name|defaultCharset
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|bufferBytes
operator|==
literal|null
operator|||
name|bufferBytes
operator|.
name|remaining
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|bufferedChars
operator|.
name|clear
argument_list|()
expr_stmt|;
name|int
name|len
init|=
name|reader
operator|.
name|read
argument_list|(
name|bufferedChars
argument_list|)
decl_stmt|;
name|bufferedChars
operator|.
name|flip
argument_list|()
expr_stmt|;
if|if
condition|(
name|len
operator|==
operator|-
literal|1
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|bufferBytes
operator|=
name|defaultStreamCharset
operator|.
name|encode
argument_list|(
name|bufferedChars
argument_list|)
expr_stmt|;
block|}
return|return
name|bufferBytes
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
throws|throws
name|IOException
block|{
name|reader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
DECL|method|toOriginalInputStream ()
specifier|public
name|InputStream
name|toOriginalInputStream
parameter_list|()
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
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
DECL|field|in
specifier|private
specifier|final
name|FileInputStream
name|in
decl_stmt|;
comment|/**          * @param in file to read          * @param charset character set to use          */
DECL|method|EncodingFileReader (FileInputStream in, String charset)
name|EncodingFileReader
parameter_list|(
name|FileInputStream
name|in
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
name|in
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
DECL|field|out
specifier|private
specifier|final
name|FileOutputStream
name|out
decl_stmt|;
comment|/**          * @param out file to write          * @param charset character set to use          */
DECL|method|EncodingFileWriter (FileOutputStream out, String charset)
name|EncodingFileWriter
parameter_list|(
name|FileOutputStream
name|out
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
name|out
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

