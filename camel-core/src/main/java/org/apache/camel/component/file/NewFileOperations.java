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
name|RandomAccessFile
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
name|channels
operator|.
name|FileChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|InvalidPayloadException
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
name|NoTypeConversionAvailableException
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
name|ExchangeHelper
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
comment|/**  * File operations for {@link java.io.File}.  */
end_comment

begin_class
DECL|class|NewFileOperations
specifier|public
class|class
name|NewFileOperations
implements|implements
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
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
name|NewFileOperations
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|NewFileEndpoint
name|endpoint
decl_stmt|;
DECL|method|NewFileOperations ()
specifier|public
name|NewFileOperations
parameter_list|()
block|{     }
DECL|method|NewFileOperations (NewFileEndpoint endpoint)
specifier|public
name|NewFileOperations
parameter_list|(
name|NewFileEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|setEndpoint (GenericFileEndpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|GenericFileEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
operator|(
name|NewFileEndpoint
operator|)
name|endpoint
expr_stmt|;
block|}
DECL|method|deleteFile (String name)
specifier|public
name|boolean
name|deleteFile
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|delete
argument_list|()
return|;
block|}
DECL|method|renameFile (String from, String to)
specifier|public
name|boolean
name|renameFile
parameter_list|(
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|from
argument_list|)
decl_stmt|;
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|to
argument_list|)
decl_stmt|;
return|return
name|file
operator|.
name|renameTo
argument_list|(
name|target
argument_list|)
return|;
block|}
DECL|method|buildDirectory (String directory, boolean absolute)
specifier|public
name|boolean
name|buildDirectory
parameter_list|(
name|String
name|directory
parameter_list|,
name|boolean
name|absolute
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|endpoint
argument_list|,
literal|"endpoint"
argument_list|)
expr_stmt|;
comment|// always create endpoint defined directory
if|if
condition|(
name|endpoint
operator|.
name|isAutoCreate
argument_list|()
operator|&&
name|endpoint
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
name|endpoint
operator|.
name|getFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|getFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|File
name|path
decl_stmt|;
if|if
condition|(
name|absolute
condition|)
block|{
name|path
operator|=
operator|new
name|File
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// skip trailing endpoint configued filename as we always start with the endoint file
comment|// for creating relative directories
if|if
condition|(
name|directory
operator|.
name|startsWith
argument_list|(
name|endpoint
operator|.
name|getFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
condition|)
block|{
name|directory
operator|=
name|directory
operator|.
name|substring
argument_list|(
name|endpoint
operator|.
name|getFile
argument_list|()
operator|.
name|getPath
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|path
operator|=
operator|new
name|File
argument_list|(
name|endpoint
operator|.
name|getFile
argument_list|()
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|.
name|isDirectory
argument_list|()
operator|&&
name|path
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// the directory already exists
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
name|path
operator|.
name|mkdirs
argument_list|()
return|;
block|}
block|}
DECL|method|listFiles ()
specifier|public
name|List
argument_list|<
name|File
argument_list|>
name|listFiles
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// noop
return|return
literal|null
return|;
block|}
DECL|method|listFiles (String path)
specifier|public
name|List
argument_list|<
name|File
argument_list|>
name|listFiles
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// noop
return|return
literal|null
return|;
block|}
DECL|method|changeCurrentDirectory (String path)
specifier|public
name|void
name|changeCurrentDirectory
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// noop
block|}
DECL|method|getCurrentDirectory ()
specifier|public
name|String
name|getCurrentDirectory
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// noop
return|return
literal|null
return|;
block|}
DECL|method|retrieveFile (String name, GenericFileExchange<File> exchange)
specifier|public
name|boolean
name|retrieveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|GenericFileExchange
argument_list|<
name|File
argument_list|>
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// noop as we use type converters to read the body content for java.io.File
return|return
literal|true
return|;
block|}
DECL|method|storeFile (String fileName, GenericFileExchange<File> exchange)
specifier|public
name|boolean
name|storeFile
parameter_list|(
name|String
name|fileName
parameter_list|,
name|GenericFileExchange
argument_list|<
name|File
argument_list|>
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|endpoint
argument_list|,
literal|"endpoint"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
try|try
block|{
name|File
name|source
init|=
literal|null
decl_stmt|;
try|try
block|{
name|source
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|File
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
name|source
operator|!=
literal|null
operator|&&
name|source
operator|.
name|exists
argument_list|()
condition|)
block|{
name|writeFileByFile
argument_list|(
name|source
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|InputStream
name|in
init|=
name|ExchangeHelper
operator|.
name|getMandatoryInBody
argument_list|(
name|exchange
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|writeFileByStream
argument_list|(
name|in
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot store file: "
operator|+
name|file
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvalidPayloadException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot store file: "
operator|+
name|file
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|writeFileByFile (File source, File target)
specifier|private
name|void
name|writeFileByFile
parameter_list|(
name|File
name|source
parameter_list|,
name|File
name|target
parameter_list|)
throws|throws
name|IOException
block|{
name|FileChannel
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|source
argument_list|)
operator|.
name|getChannel
argument_list|()
decl_stmt|;
name|FileChannel
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|out
operator|=
name|prepareOutputFileChannel
argument_list|(
name|target
argument_list|,
name|out
argument_list|)
expr_stmt|;
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
literal|"Using FileChannel to transfer from: "
operator|+
name|in
operator|+
literal|" to: "
operator|+
name|out
argument_list|)
expr_stmt|;
block|}
name|in
operator|.
name|transferTo
argument_list|(
literal|0
argument_list|,
name|in
operator|.
name|size
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|in
argument_list|,
name|source
operator|.
name|getName
argument_list|()
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|close
argument_list|(
name|out
argument_list|,
name|source
operator|.
name|getName
argument_list|()
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|writeFileByStream (InputStream in, File target)
specifier|private
name|void
name|writeFileByStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|File
name|target
parameter_list|)
throws|throws
name|IOException
block|{
name|FileChannel
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|out
operator|=
name|prepareOutputFileChannel
argument_list|(
name|target
argument_list|,
name|out
argument_list|)
expr_stmt|;
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
literal|"Using InputStream to transfer from: "
operator|+
name|in
operator|+
literal|" to: "
operator|+
name|out
argument_list|)
expr_stmt|;
block|}
name|int
name|size
init|=
name|endpoint
operator|.
name|getBufferSize
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|size
index|]
decl_stmt|;
name|ByteBuffer
name|byteBuffer
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|count
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|<=
literal|0
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|count
operator|<
name|size
condition|)
block|{
name|byteBuffer
operator|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|byteBuffer
argument_list|)
expr_stmt|;
break|break;
block|}
else|else
block|{
name|out
operator|.
name|write
argument_list|(
name|byteBuffer
argument_list|)
expr_stmt|;
name|byteBuffer
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|in
argument_list|,
name|target
operator|.
name|getName
argument_list|()
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|close
argument_list|(
name|out
argument_list|,
name|target
operator|.
name|getName
argument_list|()
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates and prepares the output file channel. Will position itself in correct position if eg. it should append      * or override any existing content.      */
DECL|method|prepareOutputFileChannel (File target, FileChannel out)
specifier|private
name|FileChannel
name|prepareOutputFileChannel
parameter_list|(
name|File
name|target
parameter_list|,
name|FileChannel
name|out
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|endpoint
operator|.
name|isAppend
argument_list|()
condition|)
block|{
name|out
operator|=
operator|new
name|RandomAccessFile
argument_list|(
name|target
argument_list|,
literal|"rw"
argument_list|)
operator|.
name|getChannel
argument_list|()
expr_stmt|;
name|out
operator|=
name|out
operator|.
name|position
argument_list|(
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|target
argument_list|)
operator|.
name|getChannel
argument_list|()
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
block|}
end_class

end_unit

