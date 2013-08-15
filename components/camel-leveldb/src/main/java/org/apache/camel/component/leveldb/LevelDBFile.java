begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.leveldb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|leveldb
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
name|IOException
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
name|Service
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
name|iq80
operator|.
name|leveldb
operator|.
name|CompressionType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|iq80
operator|.
name|leveldb
operator|.
name|DB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|iq80
operator|.
name|leveldb
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|org
operator|.
name|iq80
operator|.
name|leveldb
operator|.
name|WriteOptions
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

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|leveldbjni
operator|.
name|JniDBFactory
operator|.
name|factory
import|;
end_import

begin_comment
comment|/**  * Manages access to a shared<a href="https://github.com/fusesource/leveldbjni/">LevelDB</a> file.  *<p/>  * Will by default not sync writes which allows it to be faster.  * You can force syncing by setting the sync option to<tt>true</tt>.  */
end_comment

begin_class
DECL|class|LevelDBFile
specifier|public
class|class
name|LevelDBFile
implements|implements
name|Service
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
name|LevelDBFile
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|db
specifier|private
name|DB
name|db
decl_stmt|;
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
DECL|field|writeBufferSize
specifier|private
name|int
name|writeBufferSize
init|=
literal|4
operator|<<
literal|20
decl_stmt|;
DECL|field|maxOpenFiles
specifier|private
name|int
name|maxOpenFiles
init|=
literal|1000
decl_stmt|;
DECL|field|blockRestartInterval
specifier|private
name|int
name|blockRestartInterval
init|=
literal|16
decl_stmt|;
DECL|field|blockSize
specifier|private
name|int
name|blockSize
init|=
literal|4
operator|*
literal|1024
decl_stmt|;
DECL|field|compressionType
specifier|private
name|String
name|compressionType
decl_stmt|;
DECL|field|verifyChecksums
specifier|private
name|boolean
name|verifyChecksums
init|=
literal|true
decl_stmt|;
DECL|field|paranoidChecks
specifier|private
name|boolean
name|paranoidChecks
decl_stmt|;
DECL|field|cacheSize
specifier|private
name|long
name|cacheSize
init|=
literal|32
operator|<<
literal|20
decl_stmt|;
DECL|field|sync
specifier|private
name|boolean
name|sync
decl_stmt|;
DECL|method|getDb ()
specifier|public
name|DB
name|getDb
parameter_list|()
block|{
return|return
name|db
return|;
block|}
DECL|method|setFile (File file)
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
DECL|method|getFile ()
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
DECL|method|setFileName (String fileName)
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
block|}
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|file
operator|.
name|getCanonicalPath
argument_list|()
return|;
block|}
DECL|method|getWriteBufferSize ()
specifier|public
name|int
name|getWriteBufferSize
parameter_list|()
block|{
return|return
name|writeBufferSize
return|;
block|}
DECL|method|setWriteBufferSize (int writeBufferSize)
specifier|public
name|void
name|setWriteBufferSize
parameter_list|(
name|int
name|writeBufferSize
parameter_list|)
block|{
name|this
operator|.
name|writeBufferSize
operator|=
name|writeBufferSize
expr_stmt|;
block|}
DECL|method|getMaxOpenFiles ()
specifier|public
name|int
name|getMaxOpenFiles
parameter_list|()
block|{
return|return
name|maxOpenFiles
return|;
block|}
DECL|method|setMaxOpenFiles (int maxOpenFiles)
specifier|public
name|void
name|setMaxOpenFiles
parameter_list|(
name|int
name|maxOpenFiles
parameter_list|)
block|{
name|this
operator|.
name|maxOpenFiles
operator|=
name|maxOpenFiles
expr_stmt|;
block|}
DECL|method|getBlockRestartInterval ()
specifier|public
name|int
name|getBlockRestartInterval
parameter_list|()
block|{
return|return
name|blockRestartInterval
return|;
block|}
DECL|method|setBlockRestartInterval (int blockRestartInterval)
specifier|public
name|void
name|setBlockRestartInterval
parameter_list|(
name|int
name|blockRestartInterval
parameter_list|)
block|{
name|this
operator|.
name|blockRestartInterval
operator|=
name|blockRestartInterval
expr_stmt|;
block|}
DECL|method|getBlockSize ()
specifier|public
name|int
name|getBlockSize
parameter_list|()
block|{
return|return
name|blockSize
return|;
block|}
DECL|method|setBlockSize (int blockSize)
specifier|public
name|void
name|setBlockSize
parameter_list|(
name|int
name|blockSize
parameter_list|)
block|{
name|this
operator|.
name|blockSize
operator|=
name|blockSize
expr_stmt|;
block|}
DECL|method|getCompressionType ()
specifier|public
name|String
name|getCompressionType
parameter_list|()
block|{
return|return
name|compressionType
return|;
block|}
DECL|method|setCompressionType (String compressionType)
specifier|public
name|void
name|setCompressionType
parameter_list|(
name|String
name|compressionType
parameter_list|)
block|{
name|this
operator|.
name|compressionType
operator|=
name|compressionType
expr_stmt|;
block|}
DECL|method|isVerifyChecksums ()
specifier|public
name|boolean
name|isVerifyChecksums
parameter_list|()
block|{
return|return
name|verifyChecksums
return|;
block|}
DECL|method|setVerifyChecksums (boolean verifyChecksums)
specifier|public
name|void
name|setVerifyChecksums
parameter_list|(
name|boolean
name|verifyChecksums
parameter_list|)
block|{
name|this
operator|.
name|verifyChecksums
operator|=
name|verifyChecksums
expr_stmt|;
block|}
DECL|method|isParanoidChecks ()
specifier|public
name|boolean
name|isParanoidChecks
parameter_list|()
block|{
return|return
name|paranoidChecks
return|;
block|}
DECL|method|setParanoidChecks (boolean paranoidChecks)
specifier|public
name|void
name|setParanoidChecks
parameter_list|(
name|boolean
name|paranoidChecks
parameter_list|)
block|{
name|this
operator|.
name|paranoidChecks
operator|=
name|paranoidChecks
expr_stmt|;
block|}
DECL|method|getCacheSize ()
specifier|public
name|long
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (long cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|long
name|cacheSize
parameter_list|)
block|{
name|this
operator|.
name|cacheSize
operator|=
name|cacheSize
expr_stmt|;
block|}
DECL|method|isSync ()
specifier|public
name|boolean
name|isSync
parameter_list|()
block|{
return|return
name|sync
return|;
block|}
DECL|method|setSync (boolean sync)
specifier|public
name|void
name|setSync
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
block|}
DECL|method|getWriteOptions ()
specifier|public
name|WriteOptions
name|getWriteOptions
parameter_list|()
block|{
name|WriteOptions
name|options
init|=
operator|new
name|WriteOptions
argument_list|()
decl_stmt|;
name|options
operator|.
name|sync
argument_list|(
name|sync
argument_list|)
expr_stmt|;
return|return
name|options
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A file must be configured"
argument_list|)
throw|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting LevelDB using file: {}"
argument_list|,
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Options
name|options
init|=
operator|new
name|Options
argument_list|()
operator|.
name|writeBufferSize
argument_list|(
name|writeBufferSize
argument_list|)
operator|.
name|maxOpenFiles
argument_list|(
name|maxOpenFiles
argument_list|)
operator|.
name|blockRestartInterval
argument_list|(
name|blockRestartInterval
argument_list|)
operator|.
name|blockSize
argument_list|(
name|blockSize
argument_list|)
operator|.
name|verifyChecksums
argument_list|(
name|verifyChecksums
argument_list|)
operator|.
name|paranoidChecks
argument_list|(
name|paranoidChecks
argument_list|)
operator|.
name|cacheSize
argument_list|(
name|cacheSize
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"snappy"
operator|.
name|equals
argument_list|(
name|compressionType
argument_list|)
condition|)
block|{
name|options
operator|.
name|compressionType
argument_list|(
name|CompressionType
operator|.
name|SNAPPY
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|options
operator|.
name|compressionType
argument_list|(
name|CompressionType
operator|.
name|NONE
argument_list|)
expr_stmt|;
block|}
name|options
operator|.
name|createIfMissing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|getFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|db
operator|=
name|factory
operator|.
name|open
argument_list|(
name|getFile
argument_list|()
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error opening LevelDB with file "
operator|+
name|getFile
argument_list|()
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|File
name|file
init|=
name|getFile
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping LevelDB using file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|db
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

