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
name|FileFilter
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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Consumer
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
name|ExchangePattern
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
name|Expression
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
name|Processor
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
name|Producer
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
name|ScheduledPollEndpoint
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
name|language
operator|.
name|simple
operator|.
name|FileLanguage
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
name|processor
operator|.
name|idempotent
operator|.
name|MemoryIdempotentRepository
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
name|IdempotentRepository
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
name|FactoryFinder
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
name|camel
operator|.
name|util
operator|.
name|UuidGenerator
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
comment|/**  * A<a href="http://activemq.apache.org/camel/file.html">File Endpoint</a> for  * working with file systems  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FileEndpoint
specifier|public
class|class
name|FileEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
DECL|field|DEFAULT_LOCK_FILE_POSTFIX
specifier|public
specifier|static
specifier|final
specifier|transient
name|String
name|DEFAULT_LOCK_FILE_POSTFIX
init|=
literal|".camelLock"
decl_stmt|;
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
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_STRATEGYFACTORY_CLASS
specifier|private
specifier|static
specifier|final
specifier|transient
name|String
name|DEFAULT_STRATEGYFACTORY_CLASS
init|=
literal|"org.apache.camel.component.file.strategy.FileProcessStrategyFactory"
decl_stmt|;
DECL|field|DEFAULT_IDEMPOTENT_CACHE_SIZE
specifier|private
specifier|static
specifier|final
specifier|transient
name|int
name|DEFAULT_IDEMPOTENT_CACHE_SIZE
init|=
literal|1000
decl_stmt|;
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
DECL|field|fileProcessStrategy
specifier|private
name|FileProcessStrategy
name|fileProcessStrategy
decl_stmt|;
DECL|field|autoCreate
specifier|private
name|boolean
name|autoCreate
init|=
literal|true
decl_stmt|;
DECL|field|lock
specifier|private
name|boolean
name|lock
init|=
literal|true
decl_stmt|;
DECL|field|delete
specifier|private
name|boolean
name|delete
decl_stmt|;
DECL|field|noop
specifier|private
name|boolean
name|noop
decl_stmt|;
DECL|field|append
specifier|private
name|boolean
name|append
init|=
literal|true
decl_stmt|;
DECL|field|moveNamePrefix
specifier|private
name|String
name|moveNamePrefix
decl_stmt|;
DECL|field|moveNamePostfix
specifier|private
name|String
name|moveNamePostfix
decl_stmt|;
DECL|field|preMoveNamePrefix
specifier|private
name|String
name|preMoveNamePrefix
decl_stmt|;
DECL|field|preMoveNamePostfix
specifier|private
name|String
name|preMoveNamePostfix
decl_stmt|;
DECL|field|excludedNamePrefix
specifier|private
name|String
name|excludedNamePrefix
decl_stmt|;
DECL|field|excludedNamePostfix
specifier|private
name|String
name|excludedNamePostfix
decl_stmt|;
DECL|field|bufferSize
specifier|private
name|int
name|bufferSize
init|=
literal|128
operator|*
literal|1024
decl_stmt|;
DECL|field|ignoreFileNameHeader
specifier|private
name|boolean
name|ignoreFileNameHeader
decl_stmt|;
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|field|preMoveExpression
specifier|private
name|Expression
name|preMoveExpression
decl_stmt|;
DECL|field|tempPrefix
specifier|private
name|String
name|tempPrefix
decl_stmt|;
DECL|field|idempotent
specifier|private
name|boolean
name|idempotent
decl_stmt|;
DECL|field|idempotentRepository
specifier|private
name|IdempotentRepository
name|idempotentRepository
decl_stmt|;
DECL|field|filter
specifier|private
name|FileFilter
name|filter
decl_stmt|;
DECL|field|sorter
specifier|private
name|Comparator
argument_list|<
name|File
argument_list|>
name|sorter
decl_stmt|;
DECL|field|sortBy
specifier|private
name|Comparator
argument_list|<
name|FileExchange
argument_list|>
name|sortBy
decl_stmt|;
DECL|field|exclusiveReadLockStrategy
specifier|private
name|ExclusiveReadLockStrategy
name|exclusiveReadLockStrategy
decl_stmt|;
DECL|field|readLock
specifier|private
name|String
name|readLock
init|=
literal|"fileLock"
decl_stmt|;
DECL|field|readLockTimeout
specifier|private
name|long
name|readLockTimeout
decl_stmt|;
DECL|method|FileEndpoint (File file, String endpointUri, FileComponent component)
specifier|protected
name|FileEndpoint
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|FileComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
DECL|method|FileEndpoint (String endpointUri, File file)
specifier|public
name|FileEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
DECL|method|FileEndpoint (File file)
specifier|public
name|FileEndpoint
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
DECL|method|FileEndpoint ()
specifier|public
name|FileEndpoint
parameter_list|()
block|{     }
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|FileProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
name|result
init|=
operator|new
name|FileConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|isDelete
argument_list|()
operator|&&
operator|(
name|getMoveNamePrefix
argument_list|()
operator|!=
literal|null
operator|||
name|getMoveNamePostfix
argument_list|()
operator|!=
literal|null
operator|||
name|getExpression
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You cannot set delet and a moveNamePrefix, moveNamePostfix or expression option"
argument_list|)
throw|;
block|}
comment|// if noop=true then idempotent should also be configured
if|if
condition|(
name|isNoop
argument_list|()
operator|&&
operator|!
name|isIdempotent
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Endpoint is configured with noop=true so forcing endpoint to be idempotent as well"
argument_list|)
expr_stmt|;
name|setIdempotent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// if idempotent and no repository set then create a default one
if|if
condition|(
name|isIdempotent
argument_list|()
operator|&&
name|idempotentRepository
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using default memory based idempotent repository with cache max size: "
operator|+
name|DEFAULT_IDEMPOTENT_CACHE_SIZE
argument_list|)
expr_stmt|;
name|idempotentRepository
operator|=
name|MemoryIdempotentRepository
operator|.
name|memoryIdempotentRepository
argument_list|(
name|DEFAULT_IDEMPOTENT_CACHE_SIZE
argument_list|)
expr_stmt|;
block|}
name|configureConsumer
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Create a new exchange for communicating with this endpoint      *      * @param file  the file      * @return the created exchange      */
DECL|method|createExchange (File file)
specifier|public
name|FileExchange
name|createExchange
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
operator|new
name|FileExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|,
name|file
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|createExchange
argument_list|(
name|getFile
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|FileExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|,
name|file
argument_list|)
return|;
block|}
comment|/**      * Return the file name that will be auto-generated for the given message if none is provided      */
DECL|method|getGeneratedFileName (Message message)
specifier|public
name|String
name|getGeneratedFileName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getFileFriendlyMessageId
argument_list|(
name|message
operator|.
name|getMessageId
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Configures the given message with the file which sets the body to the file object      * and sets the {@link FileComponent#HEADER_FILE_NAME} header.      */
DECL|method|configureMessage (File file, Message message)
specifier|public
name|void
name|configureMessage
parameter_list|(
name|File
name|file
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|String
name|relativePath
init|=
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|substring
argument_list|(
name|getFile
argument_list|()
operator|.
name|getPath
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|relativePath
operator|.
name|startsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|||
name|relativePath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|relativePath
operator|=
name|relativePath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
name|relativePath
argument_list|)
expr_stmt|;
block|}
DECL|method|getFile ()
specifier|public
name|File
name|getFile
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|file
argument_list|,
literal|"file"
argument_list|)
expr_stmt|;
if|if
condition|(
name|autoCreate
operator|&&
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
return|return
name|file
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
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isAutoCreate ()
specifier|public
name|boolean
name|isAutoCreate
parameter_list|()
block|{
return|return
name|this
operator|.
name|autoCreate
return|;
block|}
DECL|method|setAutoCreate (boolean autoCreate)
specifier|public
name|void
name|setAutoCreate
parameter_list|(
name|boolean
name|autoCreate
parameter_list|)
block|{
name|this
operator|.
name|autoCreate
operator|=
name|autoCreate
expr_stmt|;
block|}
DECL|method|getFileStrategy ()
specifier|public
name|FileProcessStrategy
name|getFileStrategy
parameter_list|()
block|{
if|if
condition|(
name|fileProcessStrategy
operator|==
literal|null
condition|)
block|{
name|fileProcessStrategy
operator|=
name|createFileStrategy
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using file process strategy: "
operator|+
name|fileProcessStrategy
argument_list|)
expr_stmt|;
block|}
return|return
name|fileProcessStrategy
return|;
block|}
comment|/**      * Sets the strategy to be used when the file has been processed such as      * deleting or renaming it etc.      *      * @param fileProcessStrategy the new strategy to use      */
DECL|method|setFileStrategy (FileProcessStrategy fileProcessStrategy)
specifier|public
name|void
name|setFileStrategy
parameter_list|(
name|FileProcessStrategy
name|fileProcessStrategy
parameter_list|)
block|{
name|this
operator|.
name|fileProcessStrategy
operator|=
name|fileProcessStrategy
expr_stmt|;
block|}
DECL|method|isDelete ()
specifier|public
name|boolean
name|isDelete
parameter_list|()
block|{
return|return
name|delete
return|;
block|}
DECL|method|setDelete (boolean delete)
specifier|public
name|void
name|setDelete
parameter_list|(
name|boolean
name|delete
parameter_list|)
block|{
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
block|}
DECL|method|isLock ()
specifier|public
name|boolean
name|isLock
parameter_list|()
block|{
return|return
name|lock
return|;
block|}
DECL|method|setLock (boolean lock)
specifier|public
name|void
name|setLock
parameter_list|(
name|boolean
name|lock
parameter_list|)
block|{
name|this
operator|.
name|lock
operator|=
name|lock
expr_stmt|;
block|}
DECL|method|getMoveNamePostfix ()
specifier|public
name|String
name|getMoveNamePostfix
parameter_list|()
block|{
return|return
name|moveNamePostfix
return|;
block|}
comment|/**      * Sets the name postfix appended to moved files. For example to rename all      * the files from<tt>*</tt> to<tt>*.done</tt> set this value to<tt>.done</tt>      */
DECL|method|setMoveNamePostfix (String moveNamePostfix)
specifier|public
name|void
name|setMoveNamePostfix
parameter_list|(
name|String
name|moveNamePostfix
parameter_list|)
block|{
name|this
operator|.
name|moveNamePostfix
operator|=
name|moveNamePostfix
expr_stmt|;
block|}
DECL|method|getMoveNamePrefix ()
specifier|public
name|String
name|getMoveNamePrefix
parameter_list|()
block|{
return|return
name|moveNamePrefix
return|;
block|}
comment|/**      * Sets the name prefix appended to moved files. For example to move      * processed files into a hidden directory called<tt>.camel</tt> set this value to      *<tt>.camel/</tt>      */
DECL|method|setMoveNamePrefix (String moveNamePrefix)
specifier|public
name|void
name|setMoveNamePrefix
parameter_list|(
name|String
name|moveNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|moveNamePrefix
operator|=
name|moveNamePrefix
expr_stmt|;
block|}
DECL|method|getPreMoveNamePrefix ()
specifier|public
name|String
name|getPreMoveNamePrefix
parameter_list|()
block|{
return|return
name|preMoveNamePrefix
return|;
block|}
DECL|method|setPreMoveNamePrefix (String preMoveNamePrefix)
specifier|public
name|void
name|setPreMoveNamePrefix
parameter_list|(
name|String
name|preMoveNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|preMoveNamePrefix
operator|=
name|preMoveNamePrefix
expr_stmt|;
block|}
comment|/**      * Sets the name prefix appended to pre moved files. For example to move      * files before processing into a inprogress directory called<tt>.inprogress</tt> set this value to      *<tt>.inprogress/</tt>      */
DECL|method|getPreMoveNamePostfix ()
specifier|public
name|String
name|getPreMoveNamePostfix
parameter_list|()
block|{
return|return
name|preMoveNamePostfix
return|;
block|}
comment|/**      * Sets the name postfix appended to pre moved files. For example to rename      * files before processing from<tt>*</tt> to<tt>*.inprogress</tt> set this value to<tt>.inprogress</tt>      */
DECL|method|setPreMoveNamePostfix (String preMoveNamePostfix)
specifier|public
name|void
name|setPreMoveNamePostfix
parameter_list|(
name|String
name|preMoveNamePostfix
parameter_list|)
block|{
name|this
operator|.
name|preMoveNamePostfix
operator|=
name|preMoveNamePostfix
expr_stmt|;
block|}
DECL|method|isNoop ()
specifier|public
name|boolean
name|isNoop
parameter_list|()
block|{
return|return
name|noop
return|;
block|}
comment|/**      * If set to true then the default {@link FileProcessStrategy} will be to use the      * {@link org.apache.camel.component.file.strategy.NoOpFileProcessStrategy NoOpFileProcessStrategy}      * to not move or copy processed files      */
DECL|method|setNoop (boolean noop)
specifier|public
name|void
name|setNoop
parameter_list|(
name|boolean
name|noop
parameter_list|)
block|{
name|this
operator|.
name|noop
operator|=
name|noop
expr_stmt|;
block|}
DECL|method|isAppend ()
specifier|public
name|boolean
name|isAppend
parameter_list|()
block|{
return|return
name|append
return|;
block|}
comment|/**      * When writing do we append to the end of the file, or replace it?      * The default is to append      */
DECL|method|setAppend (boolean append)
specifier|public
name|void
name|setAppend
parameter_list|(
name|boolean
name|append
parameter_list|)
block|{
name|this
operator|.
name|append
operator|=
name|append
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
comment|/**      * Sets the buffer size used to read/write files      */
DECL|method|setBufferSize (int bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|isIgnoreFileNameHeader ()
specifier|public
name|boolean
name|isIgnoreFileNameHeader
parameter_list|()
block|{
return|return
name|ignoreFileNameHeader
return|;
block|}
comment|/**      * If this flag is enabled then producers will ignore the {@link FileComponent#HEADER_FILE_NAME}      * header and generate a new dynamic file      */
DECL|method|setIgnoreFileNameHeader (boolean ignoreFileNameHeader)
specifier|public
name|void
name|setIgnoreFileNameHeader
parameter_list|(
name|boolean
name|ignoreFileNameHeader
parameter_list|)
block|{
name|this
operator|.
name|ignoreFileNameHeader
operator|=
name|ignoreFileNameHeader
expr_stmt|;
block|}
DECL|method|getExcludedNamePrefix ()
specifier|public
name|String
name|getExcludedNamePrefix
parameter_list|()
block|{
return|return
name|excludedNamePrefix
return|;
block|}
DECL|method|setExcludedNamePrefix (String excludedNamePrefix)
specifier|public
name|void
name|setExcludedNamePrefix
parameter_list|(
name|String
name|excludedNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|excludedNamePrefix
operator|=
name|excludedNamePrefix
expr_stmt|;
block|}
DECL|method|getExcludedNamePostfix ()
specifier|public
name|String
name|getExcludedNamePostfix
parameter_list|()
block|{
return|return
name|excludedNamePostfix
return|;
block|}
DECL|method|setExcludedNamePostfix (String excludedNamePostfix)
specifier|public
name|void
name|setExcludedNamePostfix
parameter_list|(
name|String
name|excludedNamePostfix
parameter_list|)
block|{
name|this
operator|.
name|excludedNamePostfix
operator|=
name|excludedNamePostfix
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|setExpression (Expression expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
comment|/**      * Sets the expression based on {@link FileLanguage}      */
DECL|method|setExpression (String fileLanguageExpression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|String
name|fileLanguageExpression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|FileLanguage
operator|.
name|file
argument_list|(
name|fileLanguageExpression
argument_list|)
expr_stmt|;
block|}
DECL|method|getPreMoveExpression ()
specifier|public
name|Expression
name|getPreMoveExpression
parameter_list|()
block|{
return|return
name|preMoveExpression
return|;
block|}
DECL|method|setPreMoveExpression (Expression expression)
specifier|public
name|void
name|setPreMoveExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|preMoveExpression
operator|=
name|expression
expr_stmt|;
block|}
comment|/**      * Sets the pre move expression based on {@link FileLanguage}      */
DECL|method|setPreMoveExpression (String fileLanguageExpression)
specifier|public
name|void
name|setPreMoveExpression
parameter_list|(
name|String
name|fileLanguageExpression
parameter_list|)
block|{
name|this
operator|.
name|preMoveExpression
operator|=
name|FileLanguage
operator|.
name|file
argument_list|(
name|fileLanguageExpression
argument_list|)
expr_stmt|;
block|}
DECL|method|getTempPrefix ()
specifier|public
name|String
name|getTempPrefix
parameter_list|()
block|{
return|return
name|tempPrefix
return|;
block|}
comment|/**      * Enables and uses temporary prefix when writing files, after write it will be renamed to the correct name.      */
DECL|method|setTempPrefix (String tempPrefix)
specifier|public
name|void
name|setTempPrefix
parameter_list|(
name|String
name|tempPrefix
parameter_list|)
block|{
name|this
operator|.
name|tempPrefix
operator|=
name|tempPrefix
expr_stmt|;
block|}
DECL|method|isIdempotent ()
specifier|public
name|boolean
name|isIdempotent
parameter_list|()
block|{
return|return
name|idempotent
return|;
block|}
DECL|method|setIdempotent (boolean idempotent)
specifier|public
name|void
name|setIdempotent
parameter_list|(
name|boolean
name|idempotent
parameter_list|)
block|{
name|this
operator|.
name|idempotent
operator|=
name|idempotent
expr_stmt|;
block|}
DECL|method|getIdempotentRepository ()
specifier|public
name|IdempotentRepository
name|getIdempotentRepository
parameter_list|()
block|{
return|return
name|idempotentRepository
return|;
block|}
DECL|method|setIdempotentRepository (IdempotentRepository idempotentRepository)
specifier|public
name|void
name|setIdempotentRepository
parameter_list|(
name|IdempotentRepository
name|idempotentRepository
parameter_list|)
block|{
name|this
operator|.
name|idempotentRepository
operator|=
name|idempotentRepository
expr_stmt|;
block|}
DECL|method|getFilter ()
specifier|public
name|FileFilter
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
DECL|method|setFilter (FileFilter filter)
specifier|public
name|void
name|setFilter
parameter_list|(
name|FileFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
DECL|method|getSorter ()
specifier|public
name|Comparator
argument_list|<
name|File
argument_list|>
name|getSorter
parameter_list|()
block|{
return|return
name|sorter
return|;
block|}
DECL|method|setSorter (Comparator<File> sorter)
specifier|public
name|void
name|setSorter
parameter_list|(
name|Comparator
argument_list|<
name|File
argument_list|>
name|sorter
parameter_list|)
block|{
name|this
operator|.
name|sorter
operator|=
name|sorter
expr_stmt|;
block|}
DECL|method|getSortBy ()
specifier|public
name|Comparator
argument_list|<
name|FileExchange
argument_list|>
name|getSortBy
parameter_list|()
block|{
return|return
name|sortBy
return|;
block|}
DECL|method|setSortBy (Comparator<FileExchange> sortBy)
specifier|public
name|void
name|setSortBy
parameter_list|(
name|Comparator
argument_list|<
name|FileExchange
argument_list|>
name|sortBy
parameter_list|)
block|{
name|this
operator|.
name|sortBy
operator|=
name|sortBy
expr_stmt|;
block|}
DECL|method|setSortBy (String expression)
specifier|public
name|void
name|setSortBy
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|setSortBy
argument_list|(
name|expression
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|setSortBy (String expression, boolean reverse)
specifier|public
name|void
name|setSortBy
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|reverse
parameter_list|)
block|{
name|setSortBy
argument_list|(
name|DefaultFileSorter
operator|.
name|sortByFileLanguage
argument_list|(
name|expression
argument_list|,
name|reverse
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getExclusiveReadLockStrategy ()
specifier|public
name|ExclusiveReadLockStrategy
name|getExclusiveReadLockStrategy
parameter_list|()
block|{
return|return
name|exclusiveReadLockStrategy
return|;
block|}
DECL|method|setExclusiveReadLockStrategy (ExclusiveReadLockStrategy exclusiveReadLockStrategy)
specifier|public
name|void
name|setExclusiveReadLockStrategy
parameter_list|(
name|ExclusiveReadLockStrategy
name|exclusiveReadLockStrategy
parameter_list|)
block|{
name|this
operator|.
name|exclusiveReadLockStrategy
operator|=
name|exclusiveReadLockStrategy
expr_stmt|;
block|}
DECL|method|getReadLock ()
specifier|public
name|String
name|getReadLock
parameter_list|()
block|{
return|return
name|readLock
return|;
block|}
DECL|method|setReadLock (String readLock)
specifier|public
name|void
name|setReadLock
parameter_list|(
name|String
name|readLock
parameter_list|)
block|{
name|this
operator|.
name|readLock
operator|=
name|readLock
expr_stmt|;
block|}
DECL|method|getReadLockTimeout ()
specifier|public
name|long
name|getReadLockTimeout
parameter_list|()
block|{
return|return
name|readLockTimeout
return|;
block|}
DECL|method|setReadLockTimeout (long readLockTimeout)
specifier|public
name|void
name|setReadLockTimeout
parameter_list|(
name|long
name|readLockTimeout
parameter_list|)
block|{
name|this
operator|.
name|readLockTimeout
operator|=
name|readLockTimeout
expr_stmt|;
block|}
comment|/**      * A strategy method to lazily create the file strategy      */
DECL|method|createFileStrategy ()
specifier|protected
name|FileProcessStrategy
name|createFileStrategy
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|factory
init|=
literal|null
decl_stmt|;
try|try
block|{
name|FactoryFinder
name|finder
init|=
operator|new
name|FactoryFinder
argument_list|(
literal|"META-INF/services/org/apache/camel/component/"
argument_list|)
decl_stmt|;
name|factory
operator|=
name|finder
operator|.
name|findClass
argument_list|(
literal|"file"
argument_list|,
literal|"strategy.factory."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"'strategy.factory.class' not found"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No strategy factory defined in 'META-INF/services/org/apache/camel/component/file'"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
comment|// use default
name|factory
operator|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|DEFAULT_STRATEGYFACTORY_CLASS
argument_list|)
expr_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|TypeNotPresentException
argument_list|(
literal|"FileProcessStrategyFactory class not found"
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
try|try
block|{
name|Method
name|factoryMethod
init|=
name|factory
operator|.
name|getMethod
argument_list|(
literal|"createFileProcessStrategy"
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|FileProcessStrategy
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|factoryMethod
argument_list|,
literal|null
argument_list|,
name|getParamsAsMap
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TypeNotPresentException
argument_list|(
name|factory
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|".createFileProcessStrategy(Properties params) method not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getParamsAsMap ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParamsAsMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNoop
argument_list|()
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"noop"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isDelete
argument_list|()
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"delete"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isAppend
argument_list|()
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"append"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isLock
argument_list|()
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"lock"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|moveNamePrefix
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"moveNamePrefix"
argument_list|,
name|moveNamePrefix
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|moveNamePostfix
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"moveNamePostfix"
argument_list|,
name|moveNamePostfix
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|preMoveNamePrefix
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"preMoveNamePrefix"
argument_list|,
name|preMoveNamePrefix
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|preMoveNamePostfix
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"preMoveNamePostfix"
argument_list|,
name|preMoveNamePostfix
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"expression"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|preMoveExpression
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"preMoveExpression"
argument_list|,
name|preMoveExpression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exclusiveReadLockStrategy
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"exclusiveReadLockStrategy"
argument_list|,
name|exclusiveReadLockStrategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|readLock
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"readLock"
argument_list|,
name|readLock
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|readLockTimeout
operator|>
literal|0
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"readLockTimeout"
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
name|readLockTimeout
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|params
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"file://"
operator|+
name|getFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
DECL|method|getFileFriendlyMessageId (String id)
specifier|protected
name|String
name|getFileFriendlyMessageId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|UuidGenerator
operator|.
name|generateSanitizedId
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit

