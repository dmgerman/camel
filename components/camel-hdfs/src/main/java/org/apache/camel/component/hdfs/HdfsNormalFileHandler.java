begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
package|;
end_package

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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|RuntimeCamelException
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
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|FSDataOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|FileSystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|FileUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|Path
import|;
end_import

begin_class
DECL|class|HdfsNormalFileHandler
class|class
name|HdfsNormalFileHandler
extends|extends
name|DefaultHdfsFile
argument_list|<
name|OutputStream
argument_list|,
name|InputStream
argument_list|>
block|{
DECL|field|consumed
specifier|private
name|boolean
name|consumed
decl_stmt|;
annotation|@
name|Override
DECL|method|createOutputStream (String hdfsPath, HdfsInfoFactory hdfsInfoFactory)
specifier|public
name|OutputStream
name|createOutputStream
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsInfoFactory
name|hdfsInfoFactory
parameter_list|)
block|{
try|try
block|{
name|OutputStream
name|outputStream
decl_stmt|;
name|HdfsInfo
name|hdfsInfo
init|=
name|hdfsInfoFactory
operator|.
name|newHdfsInfo
argument_list|(
name|hdfsPath
argument_list|)
decl_stmt|;
name|HdfsConfiguration
name|endpointConfig
init|=
name|hdfsInfoFactory
operator|.
name|getEndpointConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpointConfig
operator|.
name|isAppend
argument_list|()
condition|)
block|{
name|outputStream
operator|=
name|hdfsInfo
operator|.
name|getFileSystem
argument_list|()
operator|.
name|append
argument_list|(
name|hdfsInfo
operator|.
name|getPath
argument_list|()
argument_list|,
name|endpointConfig
operator|.
name|getBufferSize
argument_list|()
argument_list|,
parameter_list|()
lambda|->
block|{ }
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputStream
operator|=
name|hdfsInfo
operator|.
name|getFileSystem
argument_list|()
operator|.
name|create
argument_list|(
name|hdfsInfo
operator|.
name|getPath
argument_list|()
argument_list|,
name|endpointConfig
operator|.
name|isOverwrite
argument_list|()
argument_list|,
name|endpointConfig
operator|.
name|getBufferSize
argument_list|()
argument_list|,
name|endpointConfig
operator|.
name|getReplication
argument_list|()
argument_list|,
name|endpointConfig
operator|.
name|getBlockSize
argument_list|()
argument_list|,
parameter_list|()
lambda|->
block|{ }
argument_list|)
expr_stmt|;
block|}
return|return
name|outputStream
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|append (HdfsOutputStream hdfsOutputStream, Object key, Object value, Exchange exchange)
specifier|public
name|long
name|append
parameter_list|(
name|HdfsOutputStream
name|hdfsOutputStream
parameter_list|,
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|InputStream
name|inputStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|inputStream
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
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|copyBytes
argument_list|(
name|inputStream
argument_list|,
operator|(
name|FSDataOutputStream
operator|)
name|hdfsOutputStream
operator|.
name|getOut
argument_list|()
argument_list|,
name|HdfsConstants
operator|.
name|DEFAULT_BUFFERSIZE
argument_list|,
literal|false
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createInputStream (String hdfsPath, HdfsInfoFactory hdfsInfoFactory)
specifier|public
name|InputStream
name|createInputStream
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsInfoFactory
name|hdfsInfoFactory
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|inputStream
decl_stmt|;
name|HdfsConfiguration
name|endpointConfig
init|=
name|hdfsInfoFactory
operator|.
name|getEndpointConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpointConfig
operator|.
name|getFileSystemType
argument_list|()
operator|.
name|equals
argument_list|(
name|HdfsFileSystemType
operator|.
name|LOCAL
argument_list|)
condition|)
block|{
name|HdfsInfo
name|hdfsInfo
init|=
name|hdfsInfoFactory
operator|.
name|newHdfsInfo
argument_list|(
name|hdfsPath
argument_list|)
decl_stmt|;
name|inputStream
operator|=
name|hdfsInfo
operator|.
name|getFileSystem
argument_list|()
operator|.
name|open
argument_list|(
name|hdfsInfo
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inputStream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|getHdfsFileToTmpFile
argument_list|(
name|hdfsPath
argument_list|,
name|endpointConfig
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|inputStream
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|next (HdfsInputStream hdfsInputStream, Holder<Object> key, Holder<Object> value)
specifier|public
name|long
name|next
parameter_list|(
name|HdfsInputStream
name|hdfsInputStream
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|key
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|value
parameter_list|)
block|{
if|if
condition|(
name|hdfsInputStream
operator|.
name|isStreamDownload
argument_list|()
condition|)
block|{
return|return
name|nextAsWrappedStream
argument_list|(
name|hdfsInputStream
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|nextAsOutputStream
argument_list|(
name|hdfsInputStream
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
DECL|method|nextAsWrappedStream (HdfsInputStream hdfsInputStream, Holder<Object> key, Holder<Object> value)
specifier|private
name|long
name|nextAsWrappedStream
parameter_list|(
name|HdfsInputStream
name|hdfsInputStream
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|key
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|value
parameter_list|)
block|{
name|InputStream
name|inputStream
init|=
operator|(
name|InputStream
operator|)
name|hdfsInputStream
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|value
operator|.
name|setValue
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumed
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
name|consumed
operator|=
literal|true
expr_stmt|;
return|return
literal|1
return|;
block|}
block|}
DECL|method|nextAsOutputStream (HdfsInputStream hdfsInputStream, Holder<Object> key, Holder<Object> value)
specifier|private
name|long
name|nextAsOutputStream
parameter_list|(
name|HdfsInputStream
name|hdfsInputStream
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|key
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|value
parameter_list|)
block|{
try|try
block|{
name|ByteArrayOutputStream
name|outputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|hdfsInputStream
operator|.
name|getChunkSize
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|hdfsInputStream
operator|.
name|getChunkSize
argument_list|()
index|]
decl_stmt|;
name|int
name|bytesRead
init|=
operator|(
operator|(
name|InputStream
operator|)
name|hdfsInputStream
operator|.
name|getIn
argument_list|()
operator|)
operator|.
name|read
argument_list|(
name|buf
argument_list|)
decl_stmt|;
if|if
condition|(
name|bytesRead
operator|>=
literal|0
condition|)
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|bytesRead
argument_list|)
expr_stmt|;
name|value
operator|.
name|setValue
argument_list|(
name|outputStream
argument_list|)
expr_stmt|;
return|return
name|bytesRead
return|;
block|}
else|else
block|{
comment|// indication that we may have read from empty file
name|value
operator|.
name|setValue
argument_list|(
name|outputStream
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
DECL|method|getHdfsFileToTmpFile (String hdfsPath, HdfsConfiguration configuration)
specifier|private
name|File
name|getHdfsFileToTmpFile
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsConfiguration
name|configuration
parameter_list|)
block|{
try|try
block|{
name|String
name|fileName
init|=
name|hdfsPath
operator|.
name|substring
argument_list|(
name|hdfsPath
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
decl_stmt|;
comment|// [CAMEL-13711] Files.createTempFile not equivalent to File.createTempFile
name|File
name|outputDest
decl_stmt|;
try|try
block|{
comment|// First trying: Files.createTempFile
name|outputDest
operator|=
name|Files
operator|.
name|createTempFile
argument_list|(
name|fileName
argument_list|,
literal|".hdfs"
argument_list|)
operator|.
name|toFile
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// Now trying: File.createTempFile
name|outputDest
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
name|fileName
argument_list|,
literal|".hdfs"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outputDest
operator|.
name|exists
argument_list|()
condition|)
block|{
name|outputDest
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|HdfsInfoFactory
name|hdfsInfoFactory
init|=
operator|new
name|HdfsInfoFactory
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|HdfsInfo
name|hdfsInfo
init|=
name|hdfsInfoFactory
operator|.
name|newHdfsInfo
argument_list|(
name|hdfsPath
argument_list|)
decl_stmt|;
name|FileSystem
name|fileSystem
init|=
name|hdfsInfo
operator|.
name|getFileSystem
argument_list|()
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|fileSystem
argument_list|,
operator|new
name|Path
argument_list|(
name|hdfsPath
argument_list|)
argument_list|,
name|outputDest
argument_list|,
literal|false
argument_list|,
name|fileSystem
operator|.
name|getConf
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|FileUtil
operator|.
name|copyMerge
argument_list|(
name|fileSystem
argument_list|,
comment|// src
operator|new
name|Path
argument_list|(
name|hdfsPath
argument_list|)
argument_list|,
name|FileSystem
operator|.
name|getLocal
argument_list|(
operator|new
name|Configuration
argument_list|()
argument_list|)
argument_list|,
comment|// dest
operator|new
name|Path
argument_list|(
name|outputDest
operator|.
name|toURI
argument_list|()
argument_list|)
argument_list|,
literal|false
argument_list|,
name|fileSystem
operator|.
name|getConf
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
name|outputDest
return|;
block|}
return|return
operator|new
name|File
argument_list|(
name|outputDest
argument_list|,
name|fileName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

