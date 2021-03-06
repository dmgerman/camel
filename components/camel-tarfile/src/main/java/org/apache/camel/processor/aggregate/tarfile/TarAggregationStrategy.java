begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.tarfile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|tarfile
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
name|AggregationStrategy
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
name|WrappedFile
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
name|component
operator|.
name|file
operator|.
name|FileConsumer
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
name|component
operator|.
name|file
operator|.
name|GenericFile
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
name|component
operator|.
name|file
operator|.
name|GenericFileMessage
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
name|component
operator|.
name|file
operator|.
name|GenericFileOperationFailedException
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
name|spi
operator|.
name|Synchronization
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
name|commons
operator|.
name|compress
operator|.
name|archivers
operator|.
name|ArchiveEntry
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
name|compress
operator|.
name|archivers
operator|.
name|ArchiveException
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
name|compress
operator|.
name|archivers
operator|.
name|ArchiveStreamFactory
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
name|compress
operator|.
name|archivers
operator|.
name|tar
operator|.
name|TarArchiveEntry
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
name|compress
operator|.
name|archivers
operator|.
name|tar
operator|.
name|TarArchiveInputStream
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
name|compress
operator|.
name|archivers
operator|.
name|tar
operator|.
name|TarArchiveOutputStream
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
name|compress
operator|.
name|utils
operator|.
name|IOUtils
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
comment|/**  * This aggregation strategy will aggregate all incoming messages into a TAR file.  *<p>If the incoming exchanges contain {@link GenericFileMessage} file name will   * be taken from the body otherwise the body content will be treated as a byte   * array and the TAR entry will be named using the message id (unless the flag  * useFilenameHeader is set to true.</p>  *<p><b>NOTE 1:</b> Please note that this aggregation strategy requires eager  * completion check to work properly.</p>  *  *<p><b>NOTE 2:</b> This implementation is very inefficient especially on big files since the tar  * file is completely rewritten for each file that is added to it. Investigate if the  * files can be collected and at completion stored to tar file.</p>  */
end_comment

begin_class
DECL|class|TarAggregationStrategy
specifier|public
class|class
name|TarAggregationStrategy
implements|implements
name|AggregationStrategy
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
name|TarAggregationStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|filePrefix
specifier|private
name|String
name|filePrefix
decl_stmt|;
DECL|field|fileSuffix
specifier|private
name|String
name|fileSuffix
init|=
literal|".tar"
decl_stmt|;
DECL|field|preserveFolderStructure
specifier|private
name|boolean
name|preserveFolderStructure
decl_stmt|;
DECL|field|useFilenameHeader
specifier|private
name|boolean
name|useFilenameHeader
decl_stmt|;
DECL|field|parentDir
specifier|private
name|File
name|parentDir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
decl_stmt|;
DECL|method|TarAggregationStrategy ()
specifier|public
name|TarAggregationStrategy
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param preserveFolderStructure if true, the folder structure is preserved when the source is      * a type of {@link GenericFileMessage}.  If used with a file, use recursive=true.      */
DECL|method|TarAggregationStrategy (boolean preserveFolderStructure)
specifier|public
name|TarAggregationStrategy
parameter_list|(
name|boolean
name|preserveFolderStructure
parameter_list|)
block|{
name|this
argument_list|(
name|preserveFolderStructure
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param preserveFolderStructure if true, the folder structure is preserved when the source is      * a type of {@link GenericFileMessage}.  If used with a file, use recursive=true.      * @param useFilenameHeader if true, the filename header will be used to name aggregated byte arrays      * within the TAR file.      */
DECL|method|TarAggregationStrategy (boolean preserveFolderStructure, boolean useFilenameHeader)
specifier|public
name|TarAggregationStrategy
parameter_list|(
name|boolean
name|preserveFolderStructure
parameter_list|,
name|boolean
name|useFilenameHeader
parameter_list|)
block|{
name|this
operator|.
name|preserveFolderStructure
operator|=
name|preserveFolderStructure
expr_stmt|;
name|this
operator|.
name|useFilenameHeader
operator|=
name|useFilenameHeader
expr_stmt|;
block|}
DECL|method|getFilePrefix ()
specifier|public
name|String
name|getFilePrefix
parameter_list|()
block|{
return|return
name|filePrefix
return|;
block|}
comment|/**      * Sets the prefix that will be used when creating the TAR filename.      */
DECL|method|setFilePrefix (String filePrefix)
specifier|public
name|void
name|setFilePrefix
parameter_list|(
name|String
name|filePrefix
parameter_list|)
block|{
name|this
operator|.
name|filePrefix
operator|=
name|filePrefix
expr_stmt|;
block|}
DECL|method|getFileSuffix ()
specifier|public
name|String
name|getFileSuffix
parameter_list|()
block|{
return|return
name|fileSuffix
return|;
block|}
comment|/**      * Sets the suffix that will be used when creating the ZIP filename.      */
DECL|method|setFileSuffix (String fileSuffix)
specifier|public
name|void
name|setFileSuffix
parameter_list|(
name|String
name|fileSuffix
parameter_list|)
block|{
name|this
operator|.
name|fileSuffix
operator|=
name|fileSuffix
expr_stmt|;
block|}
DECL|method|getParentDir ()
specifier|public
name|File
name|getParentDir
parameter_list|()
block|{
return|return
name|parentDir
return|;
block|}
comment|/**      * Sets the parent directory to use for writing temporary files.      */
DECL|method|setParentDir (File parentDir)
specifier|public
name|void
name|setParentDir
parameter_list|(
name|File
name|parentDir
parameter_list|)
block|{
name|this
operator|.
name|parentDir
operator|=
name|parentDir
expr_stmt|;
block|}
comment|/**      * Sets the parent directory to use for writing temporary files.      */
DECL|method|setParentDir (String parentDir)
specifier|public
name|void
name|setParentDir
parameter_list|(
name|String
name|parentDir
parameter_list|)
block|{
name|this
operator|.
name|parentDir
operator|=
operator|new
name|File
argument_list|(
name|parentDir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
name|File
name|tarFile
decl_stmt|;
name|Exchange
name|answer
init|=
name|oldExchange
decl_stmt|;
comment|// Guard against empty new exchanges
if|if
condition|(
name|newExchange
operator|==
literal|null
condition|)
block|{
return|return
name|oldExchange
return|;
block|}
comment|// First time for this aggregation
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|tarFile
operator|=
name|FileUtil
operator|.
name|createTempFile
argument_list|(
name|this
operator|.
name|filePrefix
argument_list|,
name|this
operator|.
name|fileSuffix
argument_list|,
name|this
operator|.
name|parentDir
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Created temporary file: {}"
argument_list|,
name|tarFile
argument_list|)
expr_stmt|;
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
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|answer
operator|=
name|newExchange
expr_stmt|;
name|answer
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|DeleteTarFileOnCompletion
argument_list|(
name|tarFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tarFile
operator|=
name|oldExchange
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
name|Object
name|body
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|WrappedFile
condition|)
block|{
name|body
operator|=
operator|(
operator|(
name|WrappedFile
operator|)
name|body
operator|)
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|File
condition|)
block|{
try|try
block|{
name|File
name|appendFile
init|=
operator|(
name|File
operator|)
name|body
decl_stmt|;
comment|// do not try to append empty files
if|if
condition|(
name|appendFile
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|entryName
init|=
name|preserveFolderStructure
condition|?
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
else|:
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
decl_stmt|;
name|addFileToTar
argument_list|(
name|tarFile
argument_list|,
name|appendFile
argument_list|,
name|this
operator|.
name|preserveFolderStructure
condition|?
name|entryName
else|:
literal|null
argument_list|)
expr_stmt|;
name|GenericFile
argument_list|<
name|File
argument_list|>
name|genericFile
init|=
name|FileConsumer
operator|.
name|asGenericFile
argument_list|(
name|tarFile
operator|.
name|getParent
argument_list|()
argument_list|,
name|tarFile
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|genericFile
operator|.
name|bindToExchange
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// Handle all other messages
try|try
block|{
name|byte
index|[]
name|buffer
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
comment|// do not try to append empty data
if|if
condition|(
name|buffer
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|String
name|entryName
init|=
name|useFilenameHeader
condition|?
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
else|:
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
decl_stmt|;
name|addEntryToTar
argument_list|(
name|tarFile
argument_list|,
name|entryName
argument_list|,
name|buffer
argument_list|,
name|buffer
operator|.
name|length
argument_list|)
expr_stmt|;
name|GenericFile
argument_list|<
name|File
argument_list|>
name|genericFile
init|=
name|FileConsumer
operator|.
name|asGenericFile
argument_list|(
name|tarFile
operator|.
name|getParent
argument_list|()
argument_list|,
name|tarFile
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|genericFile
operator|.
name|bindToExchange
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|addFileToTar (File source, File file, String fileName)
specifier|private
name|void
name|addFileToTar
parameter_list|(
name|File
name|source
parameter_list|,
name|File
name|file
parameter_list|,
name|String
name|fileName
parameter_list|)
throws|throws
name|IOException
throws|,
name|ArchiveException
block|{
name|File
name|tmpTar
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
name|parentDir
operator|.
name|toPath
argument_list|()
argument_list|,
name|source
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
name|tmpTar
operator|.
name|delete
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|source
operator|.
name|renameTo
argument_list|(
name|tmpTar
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not make temp file ("
operator|+
name|source
operator|.
name|getName
argument_list|()
operator|+
literal|")"
argument_list|)
throw|;
block|}
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|tmpTar
argument_list|)
decl_stmt|;
name|TarArchiveInputStream
name|tin
init|=
operator|(
name|TarArchiveInputStream
operator|)
operator|new
name|ArchiveStreamFactory
argument_list|()
operator|.
name|createArchiveInputStream
argument_list|(
name|ArchiveStreamFactory
operator|.
name|TAR
argument_list|,
name|fis
argument_list|)
decl_stmt|;
name|TarArchiveOutputStream
name|tos
init|=
operator|new
name|TarArchiveOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|source
argument_list|)
argument_list|)
decl_stmt|;
name|tos
operator|.
name|setLongFileMode
argument_list|(
name|TarArchiveOutputStream
operator|.
name|LONGFILE_POSIX
argument_list|)
expr_stmt|;
name|tos
operator|.
name|setBigNumberMode
argument_list|(
name|TarArchiveOutputStream
operator|.
name|BIGNUMBER_POSIX
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
comment|// copy the existing entries
name|ArchiveEntry
name|nextEntry
decl_stmt|;
while|while
condition|(
operator|(
name|nextEntry
operator|=
name|tin
operator|.
name|getNextEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|tos
operator|.
name|putArchiveEntry
argument_list|(
name|nextEntry
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|tin
argument_list|,
name|tos
argument_list|)
expr_stmt|;
name|tos
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
block|}
comment|// Add the new entry
name|TarArchiveEntry
name|entry
init|=
operator|new
name|TarArchiveEntry
argument_list|(
name|fileName
operator|==
literal|null
condition|?
name|file
operator|.
name|getName
argument_list|()
else|:
name|fileName
argument_list|)
decl_stmt|;
name|entry
operator|.
name|setSize
argument_list|(
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|tos
operator|.
name|putArchiveEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|tos
argument_list|)
expr_stmt|;
name|tos
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|fis
argument_list|,
name|in
argument_list|,
name|tin
argument_list|,
name|tos
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting temporary file: {}"
argument_list|,
name|tmpTar
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|tmpTar
argument_list|)
expr_stmt|;
block|}
DECL|method|addEntryToTar (File source, String entryName, byte[] buffer, int length)
specifier|private
name|void
name|addEntryToTar
parameter_list|(
name|File
name|source
parameter_list|,
name|String
name|entryName
parameter_list|,
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
throws|,
name|ArchiveException
block|{
name|File
name|tmpTar
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
name|parentDir
operator|.
name|toPath
argument_list|()
argument_list|,
name|source
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
name|tmpTar
operator|.
name|delete
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|source
operator|.
name|renameTo
argument_list|(
name|tmpTar
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot create temp file: "
operator|+
name|source
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|tmpTar
argument_list|)
decl_stmt|;
name|TarArchiveInputStream
name|tin
init|=
operator|(
name|TarArchiveInputStream
operator|)
operator|new
name|ArchiveStreamFactory
argument_list|()
operator|.
name|createArchiveInputStream
argument_list|(
name|ArchiveStreamFactory
operator|.
name|TAR
argument_list|,
name|fis
argument_list|)
decl_stmt|;
name|TarArchiveOutputStream
name|tos
init|=
operator|new
name|TarArchiveOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|source
argument_list|)
argument_list|)
decl_stmt|;
name|tos
operator|.
name|setLongFileMode
argument_list|(
name|TarArchiveOutputStream
operator|.
name|LONGFILE_POSIX
argument_list|)
expr_stmt|;
name|tos
operator|.
name|setBigNumberMode
argument_list|(
name|TarArchiveOutputStream
operator|.
name|BIGNUMBER_POSIX
argument_list|)
expr_stmt|;
comment|// copy the existing entries
name|ArchiveEntry
name|nextEntry
decl_stmt|;
while|while
condition|(
operator|(
name|nextEntry
operator|=
name|tin
operator|.
name|getNextEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|tos
operator|.
name|putArchiveEntry
argument_list|(
name|nextEntry
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|tin
argument_list|,
name|tos
argument_list|)
expr_stmt|;
name|tos
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
block|}
comment|// Create new entry
name|TarArchiveEntry
name|entry
init|=
operator|new
name|TarArchiveEntry
argument_list|(
name|entryName
argument_list|)
decl_stmt|;
name|entry
operator|.
name|setSize
argument_list|(
name|length
argument_list|)
expr_stmt|;
name|tos
operator|.
name|putArchiveEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|tos
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|tos
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|fis
argument_list|,
name|tin
argument_list|,
name|tos
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting temporary file: {}"
argument_list|,
name|tmpTar
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|tmpTar
argument_list|)
expr_stmt|;
block|}
comment|/**      * This callback class is used to clean up the temporary TAR file once the exchange has completed.      */
DECL|class|DeleteTarFileOnCompletion
specifier|private
class|class
name|DeleteTarFileOnCompletion
implements|implements
name|Synchronization
block|{
DECL|field|fileToDelete
specifier|private
specifier|final
name|File
name|fileToDelete
decl_stmt|;
DECL|method|DeleteTarFileOnCompletion (File fileToDelete)
name|DeleteTarFileOnCompletion
parameter_list|(
name|File
name|fileToDelete
parameter_list|)
block|{
name|this
operator|.
name|fileToDelete
operator|=
name|fileToDelete
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// Keep the file if something gone a miss.
block|}
annotation|@
name|Override
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleting tar file on completion: {}"
argument_list|,
name|this
operator|.
name|fileToDelete
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|this
operator|.
name|fileToDelete
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

