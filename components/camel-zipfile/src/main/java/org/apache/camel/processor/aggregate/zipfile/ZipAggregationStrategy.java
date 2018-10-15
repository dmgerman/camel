begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.zipfile
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
name|zipfile
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
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipOutputStream
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

begin_comment
comment|/**  * This aggregation strategy will aggregate all incoming messages into a ZIP file.  *<p>If the incoming exchanges contain {@link GenericFileMessage} file name will   * be taken from the body otherwise the body content will be treated as a byte   * array and the ZIP entry will be named using the message id (unless the flag  * useFilenameHeader is set to true.</p>  *<p><b>Note:</b> Please note that this aggregation strategy requires eager   * completion check to work properly.</p>  */
end_comment

begin_class
DECL|class|ZipAggregationStrategy
specifier|public
class|class
name|ZipAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
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
literal|".zip"
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
DECL|method|ZipAggregationStrategy ()
specifier|public
name|ZipAggregationStrategy
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
DECL|method|ZipAggregationStrategy (boolean preserveFolderStructure)
specifier|public
name|ZipAggregationStrategy
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
comment|/**      * @param preserveFolderStructure if true, the folder structure is preserved when the source is      * a type of {@link GenericFileMessage}.  If used with a file, use recursive=true.      * @param useFilenameHeader if true, the filename header will be used to name aggregated byte arrays      * within the ZIP file.      */
DECL|method|ZipAggregationStrategy (boolean preserveFolderStructure, boolean useFilenameHeader)
specifier|public
name|ZipAggregationStrategy
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
comment|/**      * Gets the prefix used when creating the ZIP file name.      * @return the prefix      */
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
comment|/**      * Sets the prefix that will be used when creating the ZIP filename.      * @param filePrefix prefix to use on ZIP file.      */
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
comment|/**      * Gets the suffix used when creating the ZIP file name.      * @return the suffix      */
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
comment|/**      * Sets the suffix that will be used when creating the ZIP filename.      * @param fileSuffix suffix to use on ZIP file.      */
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
name|zipFile
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
name|zipFile
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
name|DeleteZipFileOnCompletion
argument_list|(
name|zipFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|zipFile
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
name|addFileToZip
argument_list|(
name|zipFile
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
name|zipFile
operator|.
name|getParent
argument_list|()
argument_list|,
name|zipFile
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
name|addEntryToZip
argument_list|(
name|zipFile
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
name|zipFile
operator|.
name|getParent
argument_list|()
argument_list|,
name|zipFile
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
DECL|method|addFileToZip (File source, File file, String fileName)
specifier|private
specifier|static
name|void
name|addFileToZip
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
block|{
name|File
name|tmpZip
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|source
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|tmpZip
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
name|tmpZip
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
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|tmpZip
argument_list|)
decl_stmt|;
name|ZipInputStream
name|zin
init|=
operator|new
name|ZipInputStream
argument_list|(
name|fis
argument_list|)
decl_stmt|;
name|ZipOutputStream
name|out
init|=
operator|new
name|ZipOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|source
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
operator|new
name|ZipEntry
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
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|read
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
init|;
name|read
operator|>
operator|-
literal|1
condition|;
name|read
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
control|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|)
expr_stmt|;
for|for
control|(
name|ZipEntry
name|ze
init|=
name|zin
operator|.
name|getNextEntry
argument_list|()
init|;
name|ze
operator|!=
literal|null
condition|;
name|ze
operator|=
name|zin
operator|.
name|getNextEntry
argument_list|()
control|)
block|{
name|out
operator|.
name|putNextEntry
argument_list|(
name|ze
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|read
init|=
name|zin
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
init|;
name|read
operator|>
operator|-
literal|1
condition|;
name|read
operator|=
name|zin
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
control|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|closeEntry
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
name|fis
argument_list|,
name|zin
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|tmpZip
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
DECL|method|addEntryToZip (File source, String entryName, byte[] buffer, int length)
specifier|private
specifier|static
name|void
name|addEntryToZip
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
block|{
name|File
name|tmpZip
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|source
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|tmpZip
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
name|tmpZip
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
name|tmpZip
argument_list|)
decl_stmt|;
name|ZipInputStream
name|zin
init|=
operator|new
name|ZipInputStream
argument_list|(
name|fis
argument_list|)
decl_stmt|;
name|ZipOutputStream
name|out
init|=
operator|new
name|ZipOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|source
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|putNextEntry
argument_list|(
operator|new
name|ZipEntry
argument_list|(
name|entryName
argument_list|)
argument_list|)
expr_stmt|;
name|out
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
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
for|for
control|(
name|ZipEntry
name|ze
init|=
name|zin
operator|.
name|getNextEntry
argument_list|()
init|;
name|ze
operator|!=
literal|null
condition|;
name|ze
operator|=
name|zin
operator|.
name|getNextEntry
argument_list|()
control|)
block|{
name|out
operator|.
name|putNextEntry
argument_list|(
name|ze
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|read
init|=
name|zin
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
init|;
name|read
operator|>
operator|-
literal|1
condition|;
name|read
operator|=
name|zin
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
control|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|closeEntry
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
name|fis
argument_list|,
name|zin
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|tmpZip
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
comment|/**      * This callback class is used to clean up the temporary ZIP file once the exchange has completed.      */
DECL|class|DeleteZipFileOnCompletion
specifier|private
class|class
name|DeleteZipFileOnCompletion
implements|implements
name|Synchronization
block|{
DECL|field|fileToDelete
specifier|private
specifier|final
name|File
name|fileToDelete
decl_stmt|;
DECL|method|DeleteZipFileOnCompletion (File fileToDelete)
name|DeleteZipFileOnCompletion
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

