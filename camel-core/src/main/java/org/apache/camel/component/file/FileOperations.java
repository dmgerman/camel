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
name|Date
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
name|FileUtil
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
comment|/**  * File operations for {@link java.io.File}.  */
end_comment

begin_class
DECL|class|FileOperations
specifier|public
class|class
name|FileOperations
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FileOperations
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|FileEndpoint
name|endpoint
decl_stmt|;
DECL|method|FileOperations ()
specifier|public
name|FileOperations
parameter_list|()
block|{     }
DECL|method|FileOperations (FileEndpoint endpoint)
specifier|public
name|FileOperations
parameter_list|(
name|FileEndpoint
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
DECL|method|setEndpoint (GenericFileEndpoint<File> endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
operator|(
name|FileEndpoint
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
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|file
argument_list|)
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
name|FileUtil
operator|.
name|renameFile
argument_list|(
name|file
argument_list|,
name|target
argument_list|)
return|;
block|}
DECL|method|existsFile (String name)
specifier|public
name|boolean
name|existsFile
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Building starting directory: {}"
argument_list|,
name|endpoint
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|directory
argument_list|)
condition|)
block|{
comment|// no directory to build so return true to indicate ok
return|return
literal|true
return|;
block|}
name|File
name|endpointPath
init|=
name|endpoint
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|directory
argument_list|)
decl_stmt|;
name|File
name|path
decl_stmt|;
if|if
condition|(
name|absolute
condition|)
block|{
comment|// absolute path
name|path
operator|=
name|target
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpointPath
operator|.
name|equals
argument_list|(
name|target
argument_list|)
condition|)
block|{
comment|// its just the root of the endpoint path
name|path
operator|=
name|endpointPath
expr_stmt|;
block|}
else|else
block|{
comment|// relative after the endpoint path
name|String
name|afterRoot
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|directory
argument_list|,
name|endpointPath
operator|.
name|getPath
argument_list|()
operator|+
name|File
operator|.
name|separator
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|afterRoot
argument_list|)
condition|)
block|{
comment|// dir is under the root path
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
name|afterRoot
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// dir is relative to the root path
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Building directory: {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
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
DECL|method|changeToParentDirectory ()
specifier|public
name|void
name|changeToParentDirectory
parameter_list|()
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
DECL|method|retrieveFile (String name, Exchange exchange)
specifier|public
name|boolean
name|retrieveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
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
DECL|method|storeFile (String fileName, Exchange exchange)
specifier|public
name|boolean
name|storeFile
parameter_list|(
name|String
name|fileName
parameter_list|,
name|Exchange
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
comment|// if an existing file already exists what should we do?
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Ignore
condition|)
block|{
comment|// ignore but indicate that the file was written
name|LOG
operator|.
name|trace
argument_list|(
literal|"An existing file already exists: {}. Ignore and do not override it."
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Fail
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"File already exist: "
operator|+
name|file
operator|+
literal|". Cannot write new file."
argument_list|)
throw|;
block|}
block|}
comment|// we can write the file by 3 different techniques
comment|// 1. write file to file
comment|// 2. rename a file from a local work path
comment|// 3. write stream to file
try|try
block|{
comment|// is the body file based
name|File
name|source
init|=
literal|null
decl_stmt|;
comment|// get the File Object from in message
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
if|if
condition|(
name|source
operator|!=
literal|null
condition|)
block|{
comment|// okay we know the body is a file type
comment|// so try to see if we can optimize by renaming the local work path file instead of doing
comment|// a full file to file copy, as the local work copy is to be deleted afterwards anyway
comment|// local work path
name|File
name|local
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LOCAL_WORK_PATH
argument_list|,
name|File
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|local
operator|!=
literal|null
operator|&&
name|local
operator|.
name|exists
argument_list|()
condition|)
block|{
name|boolean
name|renamed
init|=
name|writeFileByLocalWorkPath
argument_list|(
name|local
argument_list|,
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|renamed
condition|)
block|{
comment|// try to keep last modified timestamp if configured to do so
name|keepLastModified
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// clear header as we have renamed the file
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LOCAL_WORK_PATH
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// return as the operation is complete, we just renamed the local work file
comment|// to the target.
return|return
literal|true
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|source
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// no there is no local work file so use file to file copy if the source exists
name|writeFileByFile
argument_list|(
name|source
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// try to keep last modified timestamp if configured to do so
name|keepLastModified
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|// fallback and use stream based
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
comment|// try to keep last modified timestamp if configured to do so
name|keepLastModified
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
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
block|}
DECL|method|keepLastModified (Exchange exchange, File file)
specifier|private
name|void
name|keepLastModified
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isKeepLastModified
argument_list|()
condition|)
block|{
name|Long
name|last
decl_stmt|;
name|Date
name|date
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LAST_MODIFIED
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|date
operator|!=
literal|null
condition|)
block|{
name|last
operator|=
name|date
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// fallback and try a long
name|last
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LAST_MODIFIED
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|last
operator|!=
literal|null
condition|)
block|{
name|boolean
name|result
init|=
name|file
operator|.
name|setLastModified
argument_list|(
name|last
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Keeping last modified timestamp: {} on file: {} with result: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|last
block|,
name|file
block|,
name|result
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|writeFileByLocalWorkPath (File source, File file)
specifier|private
name|boolean
name|writeFileByLocalWorkPath
parameter_list|(
name|File
name|source
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using local work file being renamed from: {} to: {}"
argument_list|,
name|source
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
name|FileUtil
operator|.
name|renameFile
argument_list|(
name|source
argument_list|,
name|file
argument_list|)
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using FileChannel to transfer from: {} to: {}"
argument_list|,
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|long
name|size
init|=
name|in
operator|.
name|size
argument_list|()
decl_stmt|;
name|long
name|position
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|position
operator|<
name|size
condition|)
block|{
name|position
operator|+=
name|in
operator|.
name|transferTo
argument_list|(
name|position
argument_list|,
name|endpoint
operator|.
name|getBufferSize
argument_list|()
argument_list|,
name|out
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
name|IOHelper
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using InputStream to transfer from: {} to: {}"
argument_list|,
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
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
name|int
name|bytesRead
decl_stmt|;
while|while
condition|(
operator|(
name|bytesRead
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|bytesRead
operator|<
name|size
condition|)
block|{
name|byteBuffer
operator|.
name|limit
argument_list|(
name|bytesRead
argument_list|)
expr_stmt|;
block|}
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
finally|finally
block|{
name|IOHelper
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
name|IOHelper
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
comment|/**      * Creates and prepares the output file channel. Will position itself in correct position if the file is writable      *  eg. it should append or override any existing content.      */
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
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Append
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
comment|// will override
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

