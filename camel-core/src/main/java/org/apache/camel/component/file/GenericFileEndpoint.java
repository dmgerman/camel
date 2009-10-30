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
name|CamelContext
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
name|Component
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
name|spi
operator|.
name|Language
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
name|StringHelper
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
comment|/**  * Generic FileEndpoint  */
end_comment

begin_class
DECL|class|GenericFileEndpoint
specifier|public
specifier|abstract
class|class
name|GenericFileEndpoint
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ScheduledPollEndpoint
block|{
DECL|field|DEFAULT_STRATEGYFACTORY_CLASS
specifier|protected
specifier|static
specifier|final
specifier|transient
name|String
name|DEFAULT_STRATEGYFACTORY_CLASS
init|=
literal|"org.apache.camel.component.file.strategy.GenericFileProcessStrategyFactory"
decl_stmt|;
DECL|field|DEFAULT_IDEMPOTENT_CACHE_SIZE
specifier|protected
specifier|static
specifier|final
specifier|transient
name|int
name|DEFAULT_IDEMPOTENT_CACHE_SIZE
init|=
literal|1000
decl_stmt|;
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|processStrategy
specifier|protected
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
decl_stmt|;
DECL|field|configuration
specifier|protected
name|GenericFileConfiguration
name|configuration
decl_stmt|;
DECL|field|inProgressRepository
specifier|protected
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|inProgressRepository
init|=
operator|new
name|MemoryIdempotentRepository
argument_list|()
decl_stmt|;
DECL|field|localWorkDirectory
specifier|protected
name|String
name|localWorkDirectory
decl_stmt|;
DECL|field|autoCreate
specifier|protected
name|boolean
name|autoCreate
init|=
literal|true
decl_stmt|;
DECL|field|bufferSize
specifier|protected
name|int
name|bufferSize
init|=
literal|128
operator|*
literal|1024
decl_stmt|;
DECL|field|fileExist
specifier|protected
name|GenericFileExist
name|fileExist
init|=
name|GenericFileExist
operator|.
name|Override
decl_stmt|;
DECL|field|noop
specifier|protected
name|boolean
name|noop
decl_stmt|;
DECL|field|recursive
specifier|protected
name|boolean
name|recursive
decl_stmt|;
DECL|field|delete
specifier|protected
name|boolean
name|delete
decl_stmt|;
DECL|field|flatten
specifier|protected
name|boolean
name|flatten
decl_stmt|;
DECL|field|maxMessagesPerPoll
specifier|protected
name|int
name|maxMessagesPerPoll
decl_stmt|;
DECL|field|tempPrefix
specifier|protected
name|String
name|tempPrefix
decl_stmt|;
DECL|field|tempFileName
specifier|protected
name|Expression
name|tempFileName
decl_stmt|;
DECL|field|include
specifier|protected
name|String
name|include
decl_stmt|;
DECL|field|exclude
specifier|protected
name|String
name|exclude
decl_stmt|;
DECL|field|fileName
specifier|protected
name|Expression
name|fileName
decl_stmt|;
DECL|field|move
specifier|protected
name|Expression
name|move
decl_stmt|;
DECL|field|moveFailed
specifier|protected
name|Expression
name|moveFailed
decl_stmt|;
DECL|field|preMove
specifier|protected
name|Expression
name|preMove
decl_stmt|;
DECL|field|idempotent
specifier|protected
name|Boolean
name|idempotent
decl_stmt|;
DECL|field|idempotentRepository
specifier|protected
name|IdempotentRepository
name|idempotentRepository
decl_stmt|;
DECL|field|filter
specifier|protected
name|GenericFileFilter
argument_list|<
name|T
argument_list|>
name|filter
decl_stmt|;
DECL|field|sorter
specifier|protected
name|Comparator
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|sorter
decl_stmt|;
DECL|field|sortBy
specifier|protected
name|Comparator
argument_list|<
name|Exchange
argument_list|>
name|sortBy
decl_stmt|;
DECL|field|readLock
specifier|protected
name|String
name|readLock
init|=
literal|"none"
decl_stmt|;
DECL|field|readLockTimeout
specifier|protected
name|long
name|readLockTimeout
init|=
literal|10000
decl_stmt|;
DECL|field|exclusiveReadLockStrategy
specifier|protected
name|GenericFileExclusiveReadLockStrategy
name|exclusiveReadLockStrategy
decl_stmt|;
DECL|method|GenericFileEndpoint ()
specifier|public
name|GenericFileEndpoint
parameter_list|()
block|{     }
DECL|method|GenericFileEndpoint (String endpointUri, Component component)
specifier|public
name|GenericFileEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
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
DECL|method|createConsumer (Processor processor)
specifier|public
specifier|abstract
name|GenericFileConsumer
argument_list|<
name|T
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|createProducer ()
specifier|public
specifier|abstract
name|GenericFileProducer
argument_list|<
name|T
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|createExchange (GenericFile<T> file)
specifier|public
specifier|abstract
name|Exchange
name|createExchange
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
function_decl|;
DECL|method|getScheme ()
specifier|public
specifier|abstract
name|String
name|getScheme
parameter_list|()
function_decl|;
DECL|method|getFileSeparator ()
specifier|public
specifier|abstract
name|char
name|getFileSeparator
parameter_list|()
function_decl|;
DECL|method|isAbsolute (String name)
specifier|public
specifier|abstract
name|boolean
name|isAbsolute
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Return the file name that will be auto-generated for the given message if      * none is provided      */
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
name|StringHelper
operator|.
name|sanitize
argument_list|(
name|message
operator|.
name|getMessageId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getGenericFileProcessStrategy ()
specifier|public
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|getGenericFileProcessStrategy
parameter_list|()
block|{
if|if
condition|(
name|processStrategy
operator|==
literal|null
condition|)
block|{
name|processStrategy
operator|=
name|createGenericFileStrategy
argument_list|()
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using Generic file process strategy: "
operator|+
name|processStrategy
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|processStrategy
return|;
block|}
comment|/**      * A strategy method to lazily create the file strategy      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createGenericFileStrategy ()
specifier|protected
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|createGenericFileStrategy
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
name|getCamelContext
argument_list|()
operator|.
name|getFactoryFinder
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
name|getScheme
argument_list|()
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
name|log
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
name|log
operator|.
name|debug
argument_list|(
literal|"No strategy factory defined in 'META-INF/services/org/apache/camel/component/'"
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
name|this
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
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
name|DEFAULT_STRATEGYFACTORY_CLASS
operator|+
literal|" class not found"
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
literal|"createGenericFileProcessStrategy"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|factoryMethod
argument_list|,
literal|null
argument_list|,
name|getCamelContext
argument_list|()
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
literal|".createGenericFileProcessStrategy method not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
DECL|method|isRecursive ()
specifier|public
name|boolean
name|isRecursive
parameter_list|()
block|{
return|return
name|recursive
return|;
block|}
DECL|method|setRecursive (boolean recursive)
specifier|public
name|void
name|setRecursive
parameter_list|(
name|boolean
name|recursive
parameter_list|)
block|{
name|this
operator|.
name|recursive
operator|=
name|recursive
expr_stmt|;
block|}
DECL|method|getInclude ()
specifier|public
name|String
name|getInclude
parameter_list|()
block|{
return|return
name|include
return|;
block|}
DECL|method|setInclude (String include)
specifier|public
name|void
name|setInclude
parameter_list|(
name|String
name|include
parameter_list|)
block|{
name|this
operator|.
name|include
operator|=
name|include
expr_stmt|;
block|}
DECL|method|getExclude ()
specifier|public
name|String
name|getExclude
parameter_list|()
block|{
return|return
name|exclude
return|;
block|}
DECL|method|setExclude (String exclude)
specifier|public
name|void
name|setExclude
parameter_list|(
name|String
name|exclude
parameter_list|)
block|{
name|this
operator|.
name|exclude
operator|=
name|exclude
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
DECL|method|isFlatten ()
specifier|public
name|boolean
name|isFlatten
parameter_list|()
block|{
return|return
name|flatten
return|;
block|}
DECL|method|setFlatten (boolean flatten)
specifier|public
name|void
name|setFlatten
parameter_list|(
name|boolean
name|flatten
parameter_list|)
block|{
name|this
operator|.
name|flatten
operator|=
name|flatten
expr_stmt|;
block|}
DECL|method|getMove ()
specifier|public
name|Expression
name|getMove
parameter_list|()
block|{
return|return
name|move
return|;
block|}
DECL|method|setMove (Expression move)
specifier|public
name|void
name|setMove
parameter_list|(
name|Expression
name|move
parameter_list|)
block|{
name|this
operator|.
name|move
operator|=
name|move
expr_stmt|;
block|}
comment|/**      * Sets the move failure expression based on      * {@link org.apache.camel.language.simple.FileLanguage}      */
DECL|method|setMoveFailed (String fileLanguageExpression)
specifier|public
name|void
name|setMoveFailed
parameter_list|(
name|String
name|fileLanguageExpression
parameter_list|)
block|{
name|String
name|expression
init|=
name|configureMoveOrPreMoveExpression
argument_list|(
name|fileLanguageExpression
argument_list|)
decl_stmt|;
name|this
operator|.
name|moveFailed
operator|=
name|createFileLangugeExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|getMoveFailed ()
specifier|public
name|Expression
name|getMoveFailed
parameter_list|()
block|{
return|return
name|moveFailed
return|;
block|}
DECL|method|setMoveFailed (Expression moveFailed)
specifier|public
name|void
name|setMoveFailed
parameter_list|(
name|Expression
name|moveFailed
parameter_list|)
block|{
name|this
operator|.
name|moveFailed
operator|=
name|moveFailed
expr_stmt|;
block|}
comment|/**      * Sets the move expression based on      * {@link org.apache.camel.language.simple.FileLanguage}      */
DECL|method|setMove (String fileLanguageExpression)
specifier|public
name|void
name|setMove
parameter_list|(
name|String
name|fileLanguageExpression
parameter_list|)
block|{
name|String
name|expression
init|=
name|configureMoveOrPreMoveExpression
argument_list|(
name|fileLanguageExpression
argument_list|)
decl_stmt|;
name|this
operator|.
name|move
operator|=
name|createFileLangugeExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|getPreMove ()
specifier|public
name|Expression
name|getPreMove
parameter_list|()
block|{
return|return
name|preMove
return|;
block|}
DECL|method|setPreMove (Expression preMove)
specifier|public
name|void
name|setPreMove
parameter_list|(
name|Expression
name|preMove
parameter_list|)
block|{
name|this
operator|.
name|preMove
operator|=
name|preMove
expr_stmt|;
block|}
comment|/**      * Sets the pre move expression based on      * {@link org.apache.camel.language.simple.FileLanguage}      */
DECL|method|setPreMove (String fileLanguageExpression)
specifier|public
name|void
name|setPreMove
parameter_list|(
name|String
name|fileLanguageExpression
parameter_list|)
block|{
name|String
name|expression
init|=
name|configureMoveOrPreMoveExpression
argument_list|(
name|fileLanguageExpression
argument_list|)
decl_stmt|;
name|this
operator|.
name|preMove
operator|=
name|createFileLangugeExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|getFileName ()
specifier|public
name|Expression
name|getFileName
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
DECL|method|setFileName (Expression fileName)
specifier|public
name|void
name|setFileName
parameter_list|(
name|Expression
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
block|}
comment|/**      * Sets the file expression based on      * {@link org.apache.camel.language.simple.FileLanguage}      */
DECL|method|setFileName (String fileLanguageExpression)
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|fileLanguageExpression
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|createFileLangugeExpression
argument_list|(
name|fileLanguageExpression
argument_list|)
expr_stmt|;
block|}
DECL|method|isIdempotent ()
specifier|public
name|Boolean
name|isIdempotent
parameter_list|()
block|{
return|return
name|idempotent
operator|!=
literal|null
condition|?
name|idempotent
else|:
literal|false
return|;
block|}
DECL|method|isIdempotentSet ()
name|boolean
name|isIdempotentSet
parameter_list|()
block|{
return|return
name|idempotent
operator|!=
literal|null
return|;
block|}
DECL|method|setIdempotent (Boolean idempotent)
specifier|public
name|void
name|setIdempotent
parameter_list|(
name|Boolean
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
name|GenericFileFilter
argument_list|<
name|T
argument_list|>
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
DECL|method|setFilter (GenericFileFilter<T> filter)
specifier|public
name|void
name|setFilter
parameter_list|(
name|GenericFileFilter
argument_list|<
name|T
argument_list|>
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
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|getSorter
parameter_list|()
block|{
return|return
name|sorter
return|;
block|}
DECL|method|setSorter (Comparator<GenericFile<T>> sorter)
specifier|public
name|void
name|setSorter
parameter_list|(
name|Comparator
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
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
name|Exchange
argument_list|>
name|getSortBy
parameter_list|()
block|{
return|return
name|sortBy
return|;
block|}
DECL|method|setSortBy (Comparator<Exchange> sortBy)
specifier|public
name|void
name|setSortBy
parameter_list|(
name|Comparator
argument_list|<
name|Exchange
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
name|GenericFileDefaultSorter
operator|.
name|sortByFileLanguage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|expression
argument_list|,
name|reverse
argument_list|)
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
comment|/**      * Enables and uses temporary prefix when writing files, after write it will      * be renamed to the correct name.      */
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
name|setTempFileName
argument_list|(
name|tempPrefix
operator|+
literal|"${file:name}"
argument_list|)
expr_stmt|;
block|}
DECL|method|getTempFileName ()
specifier|public
name|Expression
name|getTempFileName
parameter_list|()
block|{
return|return
name|tempFileName
return|;
block|}
DECL|method|setTempFileName (Expression tempFileName)
specifier|public
name|void
name|setTempFileName
parameter_list|(
name|Expression
name|tempFileName
parameter_list|)
block|{
name|this
operator|.
name|tempFileName
operator|=
name|tempFileName
expr_stmt|;
block|}
DECL|method|setTempFileName (String tempFileNameExpression)
specifier|public
name|void
name|setTempFileName
parameter_list|(
name|String
name|tempFileNameExpression
parameter_list|)
block|{
name|this
operator|.
name|tempFileName
operator|=
name|createFileLangugeExpression
argument_list|(
name|tempFileNameExpression
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|GenericFileConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|GenericFileConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (GenericFileConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GenericFileConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getExclusiveReadLockStrategy ()
specifier|public
name|GenericFileExclusiveReadLockStrategy
name|getExclusiveReadLockStrategy
parameter_list|()
block|{
return|return
name|exclusiveReadLockStrategy
return|;
block|}
DECL|method|setExclusiveReadLockStrategy (GenericFileExclusiveReadLockStrategy exclusiveReadLockStrategy)
specifier|public
name|void
name|setExclusiveReadLockStrategy
parameter_list|(
name|GenericFileExclusiveReadLockStrategy
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
DECL|method|getFileExist ()
specifier|public
name|GenericFileExist
name|getFileExist
parameter_list|()
block|{
return|return
name|fileExist
return|;
block|}
DECL|method|setFileExist (GenericFileExist fileExist)
specifier|public
name|void
name|setFileExist
parameter_list|(
name|GenericFileExist
name|fileExist
parameter_list|)
block|{
name|this
operator|.
name|fileExist
operator|=
name|fileExist
expr_stmt|;
block|}
DECL|method|isAutoCreate ()
specifier|public
name|boolean
name|isAutoCreate
parameter_list|()
block|{
return|return
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
DECL|method|getProcessStrategy ()
specifier|public
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|getProcessStrategy
parameter_list|()
block|{
return|return
name|processStrategy
return|;
block|}
DECL|method|setProcessStrategy (GenericFileProcessStrategy<T> processStrategy)
specifier|public
name|void
name|setProcessStrategy
parameter_list|(
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
parameter_list|)
block|{
name|this
operator|.
name|processStrategy
operator|=
name|processStrategy
expr_stmt|;
block|}
DECL|method|getLocalWorkDirectory ()
specifier|public
name|String
name|getLocalWorkDirectory
parameter_list|()
block|{
return|return
name|localWorkDirectory
return|;
block|}
DECL|method|setLocalWorkDirectory (String localWorkDirectory)
specifier|public
name|void
name|setLocalWorkDirectory
parameter_list|(
name|String
name|localWorkDirectory
parameter_list|)
block|{
name|this
operator|.
name|localWorkDirectory
operator|=
name|localWorkDirectory
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|getInProgressRepository ()
specifier|public
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|getInProgressRepository
parameter_list|()
block|{
return|return
name|inProgressRepository
return|;
block|}
DECL|method|setInProgressRepository (IdempotentRepository<String> inProgressRepository)
specifier|public
name|void
name|setInProgressRepository
parameter_list|(
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|inProgressRepository
parameter_list|)
block|{
name|this
operator|.
name|inProgressRepository
operator|=
name|inProgressRepository
expr_stmt|;
block|}
comment|/**      * Configures the given message with the file which sets the body to the      * file object.      */
DECL|method|configureMessage (GenericFile<T> file, Message message)
specifier|public
name|void
name|configureMessage
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
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
if|if
condition|(
name|flatten
condition|)
block|{
comment|// when flatten the file name should not contain any paths
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|file
operator|.
name|getFileNameOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// compute name to set on header that should be relative to starting directory
name|String
name|name
init|=
name|file
operator|.
name|isAbsolute
argument_list|()
condition|?
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
else|:
name|file
operator|.
name|getRelativeFilePath
argument_list|()
decl_stmt|;
comment|// skip leading endpoint configured directory
name|String
name|endpointPath
init|=
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpointPath
argument_list|)
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|endpointPath
argument_list|)
condition|)
block|{
name|name
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|name
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
operator|+
name|File
operator|.
name|separator
argument_list|)
expr_stmt|;
block|}
comment|// adjust filename
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy to configure the move or premove option based on a String input.      *<p/>      * @param expression the original string input      * @return configured string or the original if no modifications is needed      */
DECL|method|configureMoveOrPreMoveExpression (String expression)
specifier|protected
name|String
name|configureMoveOrPreMoveExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
comment|// if the expression already have ${ } placeholders then pass it unmodified
if|if
condition|(
name|expression
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|expression
return|;
block|}
comment|// remove trailing slash
name|expression
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// if relative then insert start with the parent folder
if|if
condition|(
operator|!
name|isAbsolute
argument_list|(
name|expression
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"${file:parent}"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getFileSeparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// insert the directory the end user provided
name|sb
operator|.
name|append
argument_list|(
name|expression
argument_list|)
expr_stmt|;
comment|// append only the filename (file:name can contain a relative path, so we must use onlyname)
name|sb
operator|.
name|append
argument_list|(
name|getFileSeparator
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"${file:onlyname}"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
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
name|move
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"move"
argument_list|,
name|move
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|moveFailed
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"moveFailed"
argument_list|,
name|moveFailed
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|preMove
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"preMove"
argument_list|,
name|preMove
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
name|readLockTimeout
argument_list|)
expr_stmt|;
block|}
return|return
name|params
return|;
block|}
DECL|method|createFileLangugeExpression (String expression)
specifier|private
name|Expression
name|createFileLangugeExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|Language
name|language
init|=
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"file"
argument_list|)
decl_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
block|}
end_class

end_unit

