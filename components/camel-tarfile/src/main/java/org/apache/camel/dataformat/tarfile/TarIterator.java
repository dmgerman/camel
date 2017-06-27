begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.tarfile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
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
name|BufferedInputStream
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
name|Closeable
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
name|util
operator|.
name|Iterator
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
name|Message
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
name|impl
operator|.
name|DefaultMessage
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
name|ArchiveInputStream
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
comment|/**  * The Iterator which can go through the TarArchiveInputStream according to TarArchiveEntry  * Based on ZipIterator from camel-zipfile component  */
end_comment

begin_class
DECL|class|TarIterator
specifier|public
class|class
name|TarIterator
implements|implements
name|Iterator
argument_list|<
name|Message
argument_list|>
implements|,
name|Closeable
block|{
comment|/**      * Header where this {@link TarIterator} will insert the current entry's file name.      */
DECL|field|TARFILE_ENTRY_NAME_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|TARFILE_ENTRY_NAME_HEADER
init|=
literal|"CamelTarFileEntryName"
decl_stmt|;
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TarIterator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|tarInputStream
specifier|private
specifier|volatile
name|TarArchiveInputStream
name|tarInputStream
decl_stmt|;
DECL|field|parent
specifier|private
specifier|volatile
name|Message
name|parent
decl_stmt|;
DECL|field|allowEmptyDirectory
specifier|private
name|boolean
name|allowEmptyDirectory
decl_stmt|;
DECL|method|TarIterator (Exchange exchange, InputStream inputStream)
specifier|public
name|TarIterator
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|allowEmptyDirectory
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|inputStream
operator|instanceof
name|TarArchiveInputStream
condition|)
block|{
name|tarInputStream
operator|=
operator|(
name|TarArchiveInputStream
operator|)
name|inputStream
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|ArchiveInputStream
name|input
init|=
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
operator|new
name|BufferedInputStream
argument_list|(
name|inputStream
argument_list|)
argument_list|)
decl_stmt|;
name|tarInputStream
operator|=
operator|(
name|TarArchiveInputStream
operator|)
name|input
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchiveException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
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
name|parent
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|tarInputStream
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|availableDataInCurrentEntry
init|=
name|tarInputStream
operator|.
name|available
argument_list|()
operator|>
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|availableDataInCurrentEntry
condition|)
block|{
comment|// advance to the next entry.
name|parent
operator|=
name|getNextElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|tarInputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|availableDataInCurrentEntry
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|availableDataInCurrentEntry
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|availableDataInCurrentEntry
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
comment|//Just wrap the IOException as CamelRuntimeException
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|Message
name|next
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|parent
operator|=
name|getNextElement
argument_list|()
expr_stmt|;
block|}
name|Message
name|answer
init|=
name|parent
decl_stmt|;
name|parent
operator|=
literal|null
expr_stmt|;
name|checkNullAnswer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getNextElement ()
specifier|private
name|Message
name|getNextElement
parameter_list|()
block|{
if|if
condition|(
name|tarInputStream
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
name|TarArchiveEntry
name|current
init|=
name|getNextEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Reading tarEntry {}"
argument_list|,
name|current
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|answer
init|=
operator|new
name|DefaultMessage
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|TARFILE_ENTRY_NAME_HEADER
argument_list|,
name|current
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|current
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|current
operator|.
name|getSize
argument_list|()
operator|>
literal|0
condition|)
block|{
name|answer
operator|.
name|setBody
argument_list|(
operator|new
name|TarElementInputStreamWrapper
argument_list|(
name|tarInputStream
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Workaround for the case when the entry is zero bytes big
name|answer
operator|.
name|setBody
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Closed tarInputStream"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
comment|//Just wrap the IOException as CamelRuntimeException
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
block|}
DECL|method|checkNullAnswer (Message answer)
specifier|public
name|void
name|checkNullAnswer
parameter_list|(
name|Message
name|answer
parameter_list|)
block|{
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|tarInputStream
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|tarInputStream
argument_list|)
expr_stmt|;
name|tarInputStream
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getNextEntry ()
specifier|private
name|TarArchiveEntry
name|getNextEntry
parameter_list|()
throws|throws
name|IOException
block|{
name|TarArchiveEntry
name|entry
decl_stmt|;
while|while
condition|(
operator|(
name|entry
operator|=
name|tarInputStream
operator|.
name|getNextTarEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|entry
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
name|entry
return|;
block|}
else|else
block|{
if|if
condition|(
name|allowEmptyDirectory
condition|)
block|{
return|return
name|entry
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
name|IOHelper
operator|.
name|close
argument_list|(
name|tarInputStream
argument_list|)
expr_stmt|;
name|tarInputStream
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|isAllowEmptyDirectory ()
specifier|public
name|boolean
name|isAllowEmptyDirectory
parameter_list|()
block|{
return|return
name|allowEmptyDirectory
return|;
block|}
DECL|method|setAllowEmptyDirectory (boolean allowEmptyDirectory)
specifier|public
name|void
name|setAllowEmptyDirectory
parameter_list|(
name|boolean
name|allowEmptyDirectory
parameter_list|)
block|{
name|this
operator|.
name|allowEmptyDirectory
operator|=
name|allowEmptyDirectory
expr_stmt|;
block|}
block|}
end_class

end_unit

