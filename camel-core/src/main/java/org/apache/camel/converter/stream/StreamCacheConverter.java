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
name|ByteArrayInputStream
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
name|sax
operator|.
name|SAXSource
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
name|BytesSource
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
comment|/**  * A set of {@link Converter} methods for wrapping stream-based messages in a {@link StreamCache}  * implementation to ensure message re-readability (eg multicasting, retrying)  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|StreamCacheConverter
specifier|public
class|class
name|StreamCacheConverter
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
name|StreamCacheConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|converter
specifier|private
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
annotation|@
name|Converter
DECL|method|convertToStreamCache (StreamSource source)
specifier|public
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|StreamSource
name|source
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|StreamSourceCache
argument_list|(
name|source
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (StringSource source)
specifier|public
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|StringSource
name|source
parameter_list|)
block|{
comment|//no need to do stream caching for a StringSource
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (BytesSource source)
specifier|public
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|BytesSource
name|source
parameter_list|)
block|{
comment|//no need to do stream caching for a BytesSource
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (SAXSource source)
specifier|public
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|SAXSource
name|source
parameter_list|)
throws|throws
name|TransformerException
block|{
return|return
operator|new
name|SourceCache
argument_list|(
name|converter
operator|.
name|toString
argument_list|(
name|source
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (InputStream stream)
specifier|public
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|InputStreamCache
argument_list|(
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (Reader reader)
specifier|public
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|Reader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|ReaderCache
argument_list|(
name|IOConverter
operator|.
name|toString
argument_list|(
name|reader
argument_list|)
argument_list|)
return|;
block|}
comment|/*      * {@link StreamCache} implementation for {@link Source}s      */
DECL|class|SourceCache
specifier|private
class|class
name|SourceCache
extends|extends
name|StringSource
implements|implements
name|StreamCache
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4147248494104812945L
decl_stmt|;
DECL|method|SourceCache ()
specifier|public
name|SourceCache
parameter_list|()
block|{         }
DECL|method|SourceCache (String text)
specifier|public
name|SourceCache
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
comment|// do nothing here
block|}
block|}
comment|/*      * {@link StreamCache} implementation for Cache the StreamSource {@link StreamSource}s      */
DECL|class|StreamSourceCache
specifier|private
class|class
name|StreamSourceCache
extends|extends
name|StreamSource
implements|implements
name|StreamCache
block|{
DECL|field|inputStreamCache
name|InputStreamCache
name|inputStreamCache
decl_stmt|;
DECL|field|readCache
name|ReaderCache
name|readCache
decl_stmt|;
DECL|method|StreamSourceCache (StreamSource source)
specifier|public
name|StreamSourceCache
parameter_list|(
name|StreamSource
name|source
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|source
operator|.
name|getInputStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|inputStreamCache
operator|=
operator|new
name|InputStreamCache
argument_list|(
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|source
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setInputStream
argument_list|(
name|inputStreamCache
argument_list|)
expr_stmt|;
name|setSystemId
argument_list|(
name|source
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|source
operator|.
name|getReader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|readCache
operator|=
operator|new
name|ReaderCache
argument_list|(
name|IOConverter
operator|.
name|toString
argument_list|(
name|source
operator|.
name|getReader
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setReader
argument_list|(
name|readCache
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
if|if
condition|(
name|inputStreamCache
operator|!=
literal|null
condition|)
block|{
name|inputStreamCache
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|readCache
operator|!=
literal|null
condition|)
block|{
name|readCache
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|class|InputStreamCache
specifier|private
class|class
name|InputStreamCache
extends|extends
name|ByteArrayInputStream
implements|implements
name|StreamCache
block|{
DECL|method|InputStreamCache (byte[] data)
specifier|public
name|InputStreamCache
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|super
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ReaderCache
specifier|private
class|class
name|ReaderCache
extends|extends
name|StringReader
implements|implements
name|StreamCache
block|{
DECL|method|ReaderCache (String s)
specifier|public
name|ReaderCache
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|super
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
try|try
block|{
name|super
operator|.
name|reset
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
literal|"Exception is thrown when resets the ReaderCache"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// Do not release the string for caching
block|}
block|}
block|}
end_class

end_unit

