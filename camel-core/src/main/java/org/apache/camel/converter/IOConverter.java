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
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
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
name|util
operator|.
name|Properties
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
name|OutputKeys
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
name|Transformer
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
name|TransformerFactory
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
name|dom
operator|.
name|DOMSource
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
name|StreamResult
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
name|CollectionStringBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Some core java.io based<a  * href="http://activemq.apache.org/camel/type-converter.html">Type Converters</a>  *  * @version $Revision$  */
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
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
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
name|FileNotFoundException
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
name|Converter
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
name|FileNotFoundException
block|{
return|return
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
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
name|Converter
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
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
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
name|FileNotFoundException
block|{
return|return
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
return|;
block|}
annotation|@
name|Converter
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
name|FileNotFoundException
block|{
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|out
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
comment|// TODO could we automatically find this?
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
DECL|method|toInputStream (String text)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|toInputStream
argument_list|(
name|text
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Converter
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
name|toString
argument_list|(
name|buffer
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStrean (DOMSource source)
specifier|public
specifier|static
name|InputStream
name|toInputStrean
parameter_list|(
name|DOMSource
name|source
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|toString
argument_list|(
name|source
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|bais
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
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|data
argument_list|,
name|charsetName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't convert the byte to String with the charset "
operator|+
name|charsetName
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|String
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Converter
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
name|toReader
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
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
name|toInputStream
argument_list|(
name|url
argument_list|)
argument_list|)
return|;
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
try|try
block|{
name|CollectionStringBuffer
name|builder
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|"\n"
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to close stream: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Converter
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
name|toReader
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toString (Source source)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
return|return
name|toString
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|toString (Source source, Properties props)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Source
name|source
parameter_list|,
name|Properties
name|props
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StreamResult
name|sr
init|=
operator|new
name|StreamResult
argument_list|(
name|bos
argument_list|)
decl_stmt|;
name|Transformer
name|trans
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|props
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
block|}
name|trans
operator|.
name|setOutputProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|trans
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|sr
argument_list|)
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|toString
argument_list|()
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
DECL|method|copy (InputStream stream, ByteArrayOutputStream bos)
specifier|protected
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
name|stream
parameter_list|,
name|ByteArrayOutputStream
name|bos
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|read
init|=
name|stream
operator|.
name|read
argument_list|(
name|data
argument_list|)
decl_stmt|;
while|while
condition|(
name|read
operator|!=
operator|-
literal|1
condition|)
block|{
name|bos
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
name|read
operator|=
name|stream
operator|.
name|read
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|bos
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

