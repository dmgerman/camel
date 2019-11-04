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
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|hadoop
operator|.
name|fs
operator|.
name|Path
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
name|io
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

begin_class
DECL|class|HdfsInputStream
specifier|public
class|class
name|HdfsInputStream
implements|implements
name|Closeable
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
name|HdfsInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|fileType
specifier|private
name|HdfsFileType
name|fileType
decl_stmt|;
DECL|field|info
specifier|private
name|HdfsInfo
name|info
decl_stmt|;
DECL|field|actualPath
specifier|private
name|String
name|actualPath
decl_stmt|;
DECL|field|suffixedPath
specifier|private
name|String
name|suffixedPath
decl_stmt|;
DECL|field|suffixedReadPath
specifier|private
name|String
name|suffixedReadPath
decl_stmt|;
DECL|field|in
specifier|private
name|Closeable
name|in
decl_stmt|;
DECL|field|opened
specifier|private
name|boolean
name|opened
decl_stmt|;
DECL|field|chunkSize
specifier|private
name|int
name|chunkSize
decl_stmt|;
DECL|field|numOfReadBytes
specifier|private
specifier|final
name|AtomicLong
name|numOfReadBytes
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0L
argument_list|)
decl_stmt|;
DECL|field|numOfReadMessages
specifier|private
specifier|final
name|AtomicLong
name|numOfReadMessages
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0L
argument_list|)
decl_stmt|;
DECL|field|streamDownload
specifier|private
name|boolean
name|streamDownload
decl_stmt|;
DECL|method|HdfsInputStream ()
specifier|protected
name|HdfsInputStream
parameter_list|()
block|{     }
comment|/**      *      * @param hdfsPath      * @param hdfsInfoFactory      * @return      */
DECL|method|createInputStream (String hdfsPath, HdfsInfoFactory hdfsInfoFactory)
specifier|public
specifier|static
name|HdfsInputStream
name|createInputStream
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsInfoFactory
name|hdfsInfoFactory
parameter_list|)
block|{
name|HdfsConfiguration
name|endpointConfig
init|=
name|hdfsInfoFactory
operator|.
name|getEndpointConfig
argument_list|()
decl_stmt|;
name|HdfsInputStream
name|iStream
init|=
operator|new
name|HdfsInputStream
argument_list|()
decl_stmt|;
name|iStream
operator|.
name|fileType
operator|=
name|endpointConfig
operator|.
name|getFileType
argument_list|()
expr_stmt|;
name|iStream
operator|.
name|actualPath
operator|=
name|hdfsPath
expr_stmt|;
name|iStream
operator|.
name|suffixedPath
operator|=
name|iStream
operator|.
name|actualPath
operator|+
literal|'.'
operator|+
name|endpointConfig
operator|.
name|getOpenedSuffix
argument_list|()
expr_stmt|;
name|iStream
operator|.
name|suffixedReadPath
operator|=
name|iStream
operator|.
name|actualPath
operator|+
literal|'.'
operator|+
name|endpointConfig
operator|.
name|getReadSuffix
argument_list|()
expr_stmt|;
name|iStream
operator|.
name|chunkSize
operator|=
name|endpointConfig
operator|.
name|getChunkSize
argument_list|()
expr_stmt|;
name|iStream
operator|.
name|streamDownload
operator|=
name|endpointConfig
operator|.
name|isStreamDownload
argument_list|()
expr_stmt|;
try|try
block|{
name|iStream
operator|.
name|info
operator|=
name|hdfsInfoFactory
operator|.
name|newHdfsInfo
argument_list|(
name|iStream
operator|.
name|actualPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|iStream
operator|.
name|info
operator|.
name|getFileSystem
argument_list|()
operator|.
name|rename
argument_list|(
operator|new
name|Path
argument_list|(
name|iStream
operator|.
name|actualPath
argument_list|)
argument_list|,
operator|new
name|Path
argument_list|(
name|iStream
operator|.
name|suffixedPath
argument_list|)
argument_list|)
condition|)
block|{
name|iStream
operator|.
name|in
operator|=
name|iStream
operator|.
name|fileType
operator|.
name|createInputStream
argument_list|(
name|iStream
operator|.
name|suffixedPath
argument_list|,
name|hdfsInfoFactory
argument_list|)
expr_stmt|;
name|iStream
operator|.
name|opened
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Failed to open file [{}] because it doesn't exist"
argument_list|,
name|hdfsPath
argument_list|)
expr_stmt|;
name|iStream
operator|=
literal|null
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|iStream
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
specifier|final
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|opened
condition|)
block|{
name|IOUtils
operator|.
name|closeStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|info
operator|.
name|getFileSystem
argument_list|()
operator|.
name|rename
argument_list|(
operator|new
name|Path
argument_list|(
name|suffixedPath
argument_list|)
argument_list|,
operator|new
name|Path
argument_list|(
name|suffixedReadPath
argument_list|)
argument_list|)
expr_stmt|;
name|opened
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|/**      * Reads next record/chunk specific to give file type.      * @param key      * @param value      * @return number of bytes read. 0 is correct number of bytes (empty file), -1 indicates no record was read      */
DECL|method|next (Holder<Object> key, Holder<Object> value)
specifier|public
specifier|final
name|long
name|next
parameter_list|(
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
name|long
name|nb
init|=
name|fileType
operator|.
name|next
argument_list|(
name|this
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
comment|// when zero bytes was read from given type of file, we may still have a record (e.g., empty file)
comment|// null value.value is the only indication that no (new) record/chunk was read
if|if
condition|(
name|nb
operator|==
literal|0
operator|&&
name|numOfReadMessages
operator|.
name|get
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// we've read all chunks from file, which size is exact multiple the chunk size
return|return
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|value
operator|.
name|value
operator|!=
literal|null
condition|)
block|{
name|numOfReadBytes
operator|.
name|addAndGet
argument_list|(
name|nb
argument_list|)
expr_stmt|;
name|numOfReadMessages
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|nb
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
DECL|method|getNumOfReadBytes ()
specifier|public
specifier|final
name|long
name|getNumOfReadBytes
parameter_list|()
block|{
return|return
name|numOfReadBytes
operator|.
name|longValue
argument_list|()
return|;
block|}
DECL|method|getNumOfReadMessages ()
specifier|public
specifier|final
name|long
name|getNumOfReadMessages
parameter_list|()
block|{
return|return
name|numOfReadMessages
operator|.
name|longValue
argument_list|()
return|;
block|}
DECL|method|getActualPath ()
specifier|public
specifier|final
name|String
name|getActualPath
parameter_list|()
block|{
return|return
name|actualPath
return|;
block|}
DECL|method|getChunkSize ()
specifier|public
specifier|final
name|int
name|getChunkSize
parameter_list|()
block|{
return|return
name|chunkSize
return|;
block|}
DECL|method|getIn ()
specifier|public
specifier|final
name|Closeable
name|getIn
parameter_list|()
block|{
return|return
name|in
return|;
block|}
DECL|method|isOpened ()
specifier|public
name|boolean
name|isOpened
parameter_list|()
block|{
return|return
name|opened
return|;
block|}
DECL|method|isStreamDownload ()
specifier|public
name|boolean
name|isStreamDownload
parameter_list|()
block|{
return|return
name|streamDownload
return|;
block|}
block|}
end_class

end_unit

