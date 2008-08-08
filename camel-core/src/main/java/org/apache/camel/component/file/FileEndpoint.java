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
name|Properties
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
argument_list|<
name|FileExchange
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
DECL|field|excludedNamePrefixes
specifier|private
name|String
index|[]
name|excludedNamePrefixes
decl_stmt|;
DECL|field|excludedNamePostfixes
specifier|private
name|String
index|[]
name|excludedNamePostfixes
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
argument_list|<
name|FileExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Producer
argument_list|<
name|FileExchange
argument_list|>
name|result
init|=
operator|new
name|FileProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|FileExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
argument_list|<
name|FileExchange
argument_list|>
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
name|FileExchange
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
name|FileExchange
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
DECL|method|getExcludedNamePrefixes ()
specifier|public
name|String
index|[]
name|getExcludedNamePrefixes
parameter_list|()
block|{
return|return
name|excludedNamePrefixes
return|;
block|}
comment|/**      * Sets the excluded file name prefixes, such as<tt>"."</tt> for hidden files which      * are excluded by default      *      * @deprecated use ExcludedNamePrefix. Will be removed in Camel 2.0.      */
DECL|method|setExcludedNamePrefixes (String[] excludedNamePrefixes)
specifier|public
name|void
name|setExcludedNamePrefixes
parameter_list|(
name|String
index|[]
name|excludedNamePrefixes
parameter_list|)
block|{
name|this
operator|.
name|excludedNamePrefixes
operator|=
name|excludedNamePrefixes
expr_stmt|;
block|}
DECL|method|getExcludedNamePostfixes ()
specifier|public
name|String
index|[]
name|getExcludedNamePostfixes
parameter_list|()
block|{
return|return
name|excludedNamePostfixes
return|;
block|}
comment|/**      * Sets the excluded file name postfixes, such as {@link FileEndpoint#DEFAULT_LOCK_FILE_POSTFIX}      * to ignore lock files by default.      *      * @deprecated use ExcludedNamePostfix. Will be removed in Camel 2.0.      */
DECL|method|setExcludedNamePostfixes (String[] excludedNamePostfixes)
specifier|public
name|void
name|setExcludedNamePostfixes
parameter_list|(
name|String
index|[]
name|excludedNamePostfixes
parameter_list|)
block|{
name|this
operator|.
name|excludedNamePostfixes
operator|=
name|excludedNamePostfixes
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
name|Properties
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
name|getParamsAsProperties
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
DECL|method|getParamsAsProperties ()
specifier|protected
name|Properties
name|getParamsAsProperties
parameter_list|()
block|{
name|Properties
name|params
init|=
operator|new
name|Properties
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
name|setProperty
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
name|setProperty
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
name|setProperty
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
name|setProperty
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
name|setProperty
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
name|setProperty
argument_list|(
literal|"moveNamePostfix"
argument_list|,
name|moveNamePostfix
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

