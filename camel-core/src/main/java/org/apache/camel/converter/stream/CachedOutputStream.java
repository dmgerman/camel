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
name|BufferedOutputStream
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
name|OutputStream
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
name|impl
operator|.
name|SynchronizationAdapter
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
name|FileUtil
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
comment|/**  * This output stream will store the content into a File if the stream context size is exceed the  * THRESHOLD which's default value is 64K. The temp file will store in the temp directory, you   * can configure it by setting the TEMP_DIR property. If you don't set the TEMP_DIR property,  * it will choose the directory which is set by the system property of "java.io.tmpdir".  * You can get a cached input stream of this stream. The temp file which is created with this   * output stream will be deleted when you close this output stream or the all cached   * fileInputStream is closed after the exchange is completed.  */
end_comment

begin_class
DECL|class|CachedOutputStream
specifier|public
class|class
name|CachedOutputStream
extends|extends
name|OutputStream
block|{
DECL|field|THRESHOLD
specifier|public
specifier|static
specifier|final
name|String
name|THRESHOLD
init|=
literal|"CamelCachedOutputStreamThreshold"
decl_stmt|;
DECL|field|TEMP_DIR
specifier|public
specifier|static
specifier|final
name|String
name|TEMP_DIR
init|=
literal|"CamelCachedOutputStreamOutputDirectory"
decl_stmt|;
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
name|CachedOutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|currentStream
specifier|private
name|OutputStream
name|currentStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|2048
argument_list|)
decl_stmt|;
DECL|field|inMemory
specifier|private
name|boolean
name|inMemory
init|=
literal|true
decl_stmt|;
DECL|field|totalLength
specifier|private
name|int
name|totalLength
decl_stmt|;
DECL|field|tempFile
specifier|private
name|File
name|tempFile
decl_stmt|;
DECL|field|fileInputStreamCache
specifier|private
name|FileInputStreamCache
name|fileInputStreamCache
decl_stmt|;
DECL|field|threshold
specifier|private
name|long
name|threshold
init|=
literal|64
operator|*
literal|1024
decl_stmt|;
DECL|field|outputDir
specifier|private
name|File
name|outputDir
decl_stmt|;
DECL|method|CachedOutputStream (Exchange exchange)
specifier|public
name|CachedOutputStream
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|CachedOutputStream (Exchange exchange, boolean closedOnCompletion)
specifier|public
name|CachedOutputStream
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|closedOnCompletion
parameter_list|)
block|{
name|String
name|hold
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|THRESHOLD
argument_list|)
decl_stmt|;
name|String
name|dir
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|TEMP_DIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|hold
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|threshold
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|hold
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dir
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|outputDir
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|File
operator|.
name|class
argument_list|,
name|dir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|closedOnCompletion
condition|)
block|{
comment|// add on completion so we can cleanup after the exchange is done such as deleting temporary files
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|fileInputStreamCache
operator|!=
literal|null
condition|)
block|{
name|fileInputStreamCache
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error deleting temporary cache file: "
operator|+
name|tempFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"OnCompletion[CachedOutputStream]"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
name|currentStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|currentStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|cleanUpTempFile
argument_list|()
expr_stmt|;
block|}
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|currentStream
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|currentStream
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"CachedOutputStream[size: "
operator|+
name|totalLength
operator|+
literal|"]"
return|;
block|}
DECL|method|write (byte[] b, int off, int len)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|totalLength
operator|+=
name|len
expr_stmt|;
if|if
condition|(
name|threshold
operator|>
literal|0
operator|&&
name|inMemory
operator|&&
name|totalLength
operator|>
name|threshold
operator|&&
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|pageToFileStream
argument_list|()
expr_stmt|;
block|}
name|currentStream
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
DECL|method|write (byte[] b)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|totalLength
operator|+=
name|b
operator|.
name|length
expr_stmt|;
if|if
condition|(
name|threshold
operator|>
literal|0
operator|&&
name|inMemory
operator|&&
name|totalLength
operator|>
name|threshold
operator|&&
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|pageToFileStream
argument_list|()
expr_stmt|;
block|}
name|currentStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
DECL|method|write (int b)
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|totalLength
operator|++
expr_stmt|;
if|if
condition|(
name|threshold
operator|>
literal|0
operator|&&
name|inMemory
operator|&&
name|totalLength
operator|>
name|threshold
operator|&&
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|pageToFileStream
argument_list|()
expr_stmt|;
block|}
name|currentStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
DECL|method|getInputStream ()
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|inMemory
condition|)
block|{
if|if
condition|(
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CurrentStream should be an instance of ByteArrayOutputStream but is: "
operator|+
name|currentStream
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
try|try
block|{
if|if
condition|(
name|fileInputStreamCache
operator|==
literal|null
condition|)
block|{
name|fileInputStreamCache
operator|=
operator|new
name|FileInputStreamCache
argument_list|(
name|tempFile
argument_list|)
expr_stmt|;
block|}
return|return
name|fileInputStreamCache
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cached file "
operator|+
name|tempFile
operator|+
literal|" not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|getWrappedInputStream ()
specifier|public
name|InputStream
name|getWrappedInputStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|// The WrappedInputStream will close the CachedOuputStream when it is closed
return|return
operator|new
name|WrappedInputStream
argument_list|(
name|this
argument_list|,
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getStreamCache ()
specifier|public
name|StreamCache
name|getStreamCache
parameter_list|()
throws|throws
name|IOException
block|{
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|inMemory
condition|)
block|{
if|if
condition|(
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
return|return
operator|new
name|InputStreamCache
argument_list|(
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CurrentStream should be an instance of ByteArrayOutputStream but is: "
operator|+
name|currentStream
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
try|try
block|{
if|if
condition|(
name|fileInputStreamCache
operator|==
literal|null
condition|)
block|{
name|fileInputStreamCache
operator|=
operator|new
name|FileInputStreamCache
argument_list|(
name|tempFile
argument_list|)
expr_stmt|;
block|}
return|return
name|fileInputStreamCache
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cached file "
operator|+
name|tempFile
operator|+
literal|" not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|cleanUpTempFile ()
specifier|private
name|void
name|cleanUpTempFile
parameter_list|()
block|{
comment|// cleanup temporary file
if|if
condition|(
name|tempFile
operator|!=
literal|null
condition|)
block|{
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|tempFile
argument_list|)
expr_stmt|;
name|tempFile
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|pageToFileStream ()
specifier|private
name|void
name|pageToFileStream
parameter_list|()
throws|throws
name|IOException
block|{
name|flush
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|bout
init|=
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
decl_stmt|;
if|if
condition|(
name|outputDir
operator|==
literal|null
condition|)
block|{
name|tempFile
operator|=
name|FileUtil
operator|.
name|createTempFile
argument_list|(
literal|"cos"
argument_list|,
literal|".tmp"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tempFile
operator|=
name|FileUtil
operator|.
name|createTempFile
argument_list|(
literal|"cos"
argument_list|,
literal|".tmp"
argument_list|,
name|outputDir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating temporary stream cache file: "
operator|+
name|tempFile
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|currentStream
operator|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|tempFile
argument_list|)
argument_list|)
expr_stmt|;
name|bout
operator|.
name|writeTo
argument_list|(
name|currentStream
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// ensure flag is flipped to file based
name|inMemory
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|// This class will close the CachedOutputStream when it is closed
DECL|class|WrappedInputStream
specifier|private
class|class
name|WrappedInputStream
extends|extends
name|InputStream
block|{
DECL|field|cachedOutputStream
specifier|private
name|CachedOutputStream
name|cachedOutputStream
decl_stmt|;
DECL|field|inputStream
specifier|private
name|InputStream
name|inputStream
decl_stmt|;
DECL|method|WrappedInputStream (CachedOutputStream cos, InputStream is)
name|WrappedInputStream
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
name|cachedOutputStream
operator|=
name|cos
expr_stmt|;
name|inputStream
operator|=
name|is
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
return|return
name|inputStream
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|available ()
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|inputStream
operator|.
name|available
argument_list|()
return|;
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
name|inputStream
operator|.
name|reset
argument_list|()
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
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|cachedOutputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

