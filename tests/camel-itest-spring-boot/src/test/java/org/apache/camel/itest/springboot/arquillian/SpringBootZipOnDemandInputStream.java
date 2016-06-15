begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.arquillian
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|arquillian
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
name|ByteArrayOutputStream
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
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|CRC32
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
name|Deflater
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
name|ZipOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|ArchivePath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|exporter
operator|.
name|ArchiveExportException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|impl
operator|.
name|base
operator|.
name|io
operator|.
name|IOUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|impl
operator|.
name|base
operator|.
name|path
operator|.
name|PathUtil
import|;
end_import

begin_comment
comment|/**  * A spring-boot compatible on-demand input stream.  * It does not compress jar entries for spring-boot nested jar structure compatibility.  */
end_comment

begin_class
DECL|class|SpringBootZipOnDemandInputStream
specifier|public
class|class
name|SpringBootZipOnDemandInputStream
extends|extends
name|InputStream
block|{
comment|/**      * Created by abstract method.      */
DECL|field|outputStream
specifier|protected
name|ZipOutputStream
name|outputStream
decl_stmt|;
comment|/**      * Iterator over nodes contained in base archive.      */
DECL|field|nodesIterator
specifier|private
specifier|final
name|Iterator
argument_list|<
name|Node
argument_list|>
name|nodesIterator
decl_stmt|;
comment|/**      * Base for outputStream.      */
DECL|field|bufferedOutputStream
specifier|private
specifier|final
name|ByteArrayOutputStream
name|bufferedOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
comment|/**      * Stream of currently processed Node.      */
DECL|field|currentNodeStream
specifier|private
name|InputStream
name|currentNodeStream
decl_stmt|;
comment|/**      * Stream to the buffer.      */
DECL|field|bufferInputStream
specifier|private
name|ByteArrayInputStream
name|bufferInputStream
decl_stmt|;
comment|/**      * If output stream was closed - we should finish.      */
DECL|field|outputStreamClosed
specifier|private
name|boolean
name|outputStreamClosed
decl_stmt|;
comment|/**      * Currently processed archive path - for displaying exception.      */
DECL|field|currentPath
specifier|private
name|ArchivePath
name|currentPath
decl_stmt|;
comment|/**      * Creates stream directly from archive.      *      * @param archive      */
DECL|method|SpringBootZipOnDemandInputStream (final Archive<?> archive)
specifier|public
name|SpringBootZipOnDemandInputStream
parameter_list|(
specifier|final
name|Archive
argument_list|<
name|?
argument_list|>
name|archive
parameter_list|)
block|{
specifier|final
name|Collection
argument_list|<
name|Node
argument_list|>
name|nodes
init|=
name|archive
operator|.
name|getContent
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
name|this
operator|.
name|nodesIterator
operator|=
name|nodes
operator|.
name|iterator
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
name|outputStream
operator|==
literal|null
operator|&&
operator|!
name|outputStreamClosed
condition|)
block|{
comment|// first run
name|outputStream
operator|=
name|createOutputStream
argument_list|(
name|bufferedOutputStream
argument_list|)
expr_stmt|;
block|}
name|int
name|value
init|=
name|bufferInputStream
operator|!=
literal|null
condition|?
name|bufferInputStream
operator|.
name|read
argument_list|()
else|:
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|value
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|currentNodeStream
operator|!=
literal|null
condition|)
block|{
comment|// current node was not processed completely
try|try
block|{
name|doCopy
argument_list|()
expr_stmt|;
name|bufferInputStream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bufferedOutputStream
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|bufferedOutputStream
operator|.
name|reset
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|read
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|ArchiveExportException
argument_list|(
literal|"Failed to write asset to output: "
operator|+
name|currentPath
operator|.
name|get
argument_list|()
argument_list|,
name|t
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|nodesIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// current node was processed completely, process next one
specifier|final
name|Node
name|currentNode
init|=
name|nodesIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|currentPath
operator|=
name|currentNode
operator|.
name|getPath
argument_list|()
expr_stmt|;
specifier|final
name|String
name|pathName
init|=
name|PathUtil
operator|.
name|optionallyRemovePrecedingSlash
argument_list|(
name|currentPath
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|isDirectory
init|=
name|currentNode
operator|.
name|getAsset
argument_list|()
operator|==
literal|null
decl_stmt|;
name|String
name|resolvedPath
init|=
name|pathName
decl_stmt|;
if|if
condition|(
name|isDirectory
condition|)
block|{
name|resolvedPath
operator|=
name|PathUtil
operator|.
name|optionallyAppendSlash
argument_list|(
name|resolvedPath
argument_list|)
expr_stmt|;
name|startAsset
argument_list|(
name|resolvedPath
argument_list|,
literal|0L
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
name|endAsset
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|byte
index|[]
name|content
init|=
name|IOUtil
operator|.
name|asByteArray
argument_list|(
name|currentNode
operator|.
name|getAsset
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|long
name|size
init|=
name|content
operator|.
name|length
decl_stmt|;
name|CRC32
name|crc
init|=
operator|new
name|CRC32
argument_list|()
decl_stmt|;
name|crc
operator|.
name|update
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|long
name|crc32Value
init|=
name|crc
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|startAsset
argument_list|(
name|resolvedPath
argument_list|,
name|size
argument_list|,
name|crc32Value
argument_list|)
expr_stmt|;
name|currentNodeStream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|doCopy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|ArchiveExportException
argument_list|(
literal|"Failed to write asset to output: "
operator|+
name|currentPath
operator|.
name|get
argument_list|()
argument_list|,
name|t
argument_list|)
throw|;
block|}
name|bufferInputStream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bufferedOutputStream
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|bufferedOutputStream
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// each node was processed
if|if
condition|(
operator|!
name|outputStreamClosed
condition|)
block|{
name|outputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|outputStreamClosed
operator|=
literal|true
expr_stmt|;
comment|// output closed, now process what was saved on close
name|bufferInputStream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bufferedOutputStream
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|bufferedOutputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|currentNodeStream
operator|=
literal|null
expr_stmt|;
name|outputStream
operator|=
literal|null
expr_stmt|;
return|return
name|this
operator|.
name|read
argument_list|()
return|;
block|}
comment|// everything was read, end
return|return
operator|-
literal|1
return|;
block|}
comment|// chosen new node or new data in buffer - read again
return|return
name|this
operator|.
name|read
argument_list|()
return|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Performs copy operation between currentNodeStream and outputStream using buffer length.      *      * @throws IOException      */
DECL|method|doCopy ()
specifier|private
name|void
name|doCopy
parameter_list|()
throws|throws
name|IOException
block|{
name|IOUtil
operator|.
name|copy
argument_list|(
name|currentNodeStream
argument_list|,
name|outputStream
argument_list|)
expr_stmt|;
name|currentNodeStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|currentNodeStream
operator|=
literal|null
expr_stmt|;
name|endAsset
argument_list|()
expr_stmt|;
block|}
comment|/**      * Start entry in stream.      *      * @param path      * @throws IOException      */
DECL|method|startAsset (final String path, long size, long crc32)
specifier|private
name|void
name|startAsset
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
name|long
name|size
parameter_list|,
name|long
name|crc32
parameter_list|)
throws|throws
name|IOException
block|{
name|putNextEntry
argument_list|(
name|outputStream
argument_list|,
name|path
argument_list|,
name|size
argument_list|,
name|crc32
argument_list|)
expr_stmt|;
block|}
comment|/**      * Close entry in stream.      *      * @throws IOException      */
DECL|method|endAsset ()
specifier|private
name|void
name|endAsset
parameter_list|()
throws|throws
name|IOException
block|{
name|closeEntry
argument_list|(
name|outputStream
argument_list|)
expr_stmt|;
block|}
DECL|method|createOutputStream (final OutputStream outputStream)
specifier|protected
name|ZipOutputStream
name|createOutputStream
parameter_list|(
specifier|final
name|OutputStream
name|outputStream
parameter_list|)
block|{
name|ZipOutputStream
name|stream
init|=
operator|new
name|ZipOutputStream
argument_list|(
name|outputStream
argument_list|)
decl_stmt|;
name|stream
operator|.
name|setMethod
argument_list|(
name|ZipEntry
operator|.
name|STORED
argument_list|)
expr_stmt|;
name|stream
operator|.
name|setLevel
argument_list|(
name|Deflater
operator|.
name|NO_COMPRESSION
argument_list|)
expr_stmt|;
return|return
name|stream
return|;
block|}
DECL|method|closeEntry (final ZipOutputStream outputStream)
specifier|protected
name|void
name|closeEntry
parameter_list|(
specifier|final
name|ZipOutputStream
name|outputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
block|}
DECL|method|putNextEntry (final ZipOutputStream outputStream, final String context, long size, long crc32)
specifier|protected
name|void
name|putNextEntry
parameter_list|(
specifier|final
name|ZipOutputStream
name|outputStream
parameter_list|,
specifier|final
name|String
name|context
parameter_list|,
name|long
name|size
parameter_list|,
name|long
name|crc32
parameter_list|)
throws|throws
name|IOException
block|{
name|ZipEntry
name|entry
init|=
operator|new
name|ZipEntry
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|entry
operator|.
name|setMethod
argument_list|(
name|ZipEntry
operator|.
name|STORED
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setSize
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setCrc
argument_list|(
name|crc32
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|putNextEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

