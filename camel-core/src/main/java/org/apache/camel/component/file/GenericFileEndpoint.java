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
DECL|field|operations
specifier|protected
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
decl_stmt|;
DECL|field|configuration
specifier|protected
name|GenericFileConfiguration
name|configuration
decl_stmt|;
DECL|field|directory
specifier|protected
name|boolean
name|directory
init|=
literal|true
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
DECL|field|append
specifier|protected
name|boolean
name|append
init|=
literal|true
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
DECL|field|tempPrefix
specifier|protected
name|String
name|tempPrefix
decl_stmt|;
DECL|field|moveNamePrefix
specifier|protected
name|String
name|moveNamePrefix
decl_stmt|;
DECL|field|moveNamePostfix
specifier|protected
name|String
name|moveNamePostfix
decl_stmt|;
DECL|field|preMoveNamePrefix
specifier|protected
name|String
name|preMoveNamePrefix
decl_stmt|;
DECL|field|preMoveNamePostfix
specifier|protected
name|String
name|preMoveNamePostfix
decl_stmt|;
DECL|field|excludeNamePrefix
specifier|protected
name|String
name|excludeNamePrefix
decl_stmt|;
DECL|field|excludeNamePostfix
specifier|protected
name|String
name|excludeNamePostfix
decl_stmt|;
DECL|field|includeNamePrefix
specifier|protected
name|String
name|includeNamePrefix
decl_stmt|;
DECL|field|includeNamePostfix
specifier|protected
name|String
name|includeNamePostfix
decl_stmt|;
DECL|field|regexPattern
specifier|protected
name|String
name|regexPattern
decl_stmt|;
DECL|field|expression
specifier|protected
name|Expression
name|expression
decl_stmt|;
DECL|field|preMoveExpression
specifier|protected
name|Expression
name|preMoveExpression
decl_stmt|;
DECL|field|idempotent
specifier|protected
name|boolean
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
name|GenericFileExchange
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
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
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
name|UuidGenerator
operator|.
name|generateSanitizedId
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
name|createFactoryFinder
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
literal|".createGenericFileProcessStrategy(GenericFileEndpoint endpoint) method not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|setGenericFileProcessStrategy (GenericFileProcessStrategy<T> genericFileProcessStrategy)
specifier|public
name|void
name|setGenericFileProcessStrategy
parameter_list|(
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|genericFileProcessStrategy
parameter_list|)
block|{
name|this
operator|.
name|processStrategy
operator|=
name|genericFileProcessStrategy
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
DECL|method|getExcludeNamePrefix ()
specifier|public
name|String
name|getExcludeNamePrefix
parameter_list|()
block|{
return|return
name|excludeNamePrefix
return|;
block|}
DECL|method|setExcludeNamePrefix (String excludeNamePrefix)
specifier|public
name|void
name|setExcludeNamePrefix
parameter_list|(
name|String
name|excludeNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|excludeNamePrefix
operator|=
name|excludeNamePrefix
expr_stmt|;
block|}
DECL|method|getExcludeNamePostfix ()
specifier|public
name|String
name|getExcludeNamePostfix
parameter_list|()
block|{
return|return
name|excludeNamePostfix
return|;
block|}
DECL|method|setExcludeNamePostfix (String excludeNamePostfix)
specifier|public
name|void
name|setExcludeNamePostfix
parameter_list|(
name|String
name|excludeNamePostfix
parameter_list|)
block|{
name|this
operator|.
name|excludeNamePostfix
operator|=
name|excludeNamePostfix
expr_stmt|;
block|}
DECL|method|getIncludeNamePrefix ()
specifier|public
name|String
name|getIncludeNamePrefix
parameter_list|()
block|{
return|return
name|includeNamePrefix
return|;
block|}
DECL|method|setIncludeNamePrefix (String includeNamePrefix)
specifier|public
name|void
name|setIncludeNamePrefix
parameter_list|(
name|String
name|includeNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|includeNamePrefix
operator|=
name|includeNamePrefix
expr_stmt|;
block|}
DECL|method|getIncludeNamePostfix ()
specifier|public
name|String
name|getIncludeNamePostfix
parameter_list|()
block|{
return|return
name|includeNamePostfix
return|;
block|}
DECL|method|setIncludeNamePostfix (String includeNamePostfix)
specifier|public
name|void
name|setIncludeNamePostfix
parameter_list|(
name|String
name|includeNamePostfix
parameter_list|)
block|{
name|this
operator|.
name|includeNamePostfix
operator|=
name|includeNamePostfix
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
DECL|method|getRegexPattern ()
specifier|public
name|String
name|getRegexPattern
parameter_list|()
block|{
return|return
name|regexPattern
return|;
block|}
DECL|method|setRegexPattern (String regexPattern)
specifier|public
name|void
name|setRegexPattern
parameter_list|(
name|String
name|regexPattern
parameter_list|)
block|{
name|this
operator|.
name|regexPattern
operator|=
name|regexPattern
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
comment|/**      * Sets the expression based on      * {@link org.apache.camel.language.simple.FileLanguage}      */
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
DECL|method|setPreMoveExpression (Expression preMoveExpression)
specifier|public
name|void
name|setPreMoveExpression
parameter_list|(
name|Expression
name|preMoveExpression
parameter_list|)
block|{
name|this
operator|.
name|preMoveExpression
operator|=
name|preMoveExpression
expr_stmt|;
block|}
comment|/**      * Sets the pre move expression based on      * {@link org.apache.camel.language.simple.FileLanguage}      */
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
name|GenericFileExchange
argument_list|>
name|getSortBy
parameter_list|()
block|{
return|return
name|sortBy
return|;
block|}
DECL|method|setSortBy (Comparator<GenericFileExchange> sortBy)
specifier|public
name|void
name|setSortBy
parameter_list|(
name|Comparator
argument_list|<
name|GenericFileExchange
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
DECL|method|isDirectory ()
specifier|public
name|boolean
name|isDirectory
parameter_list|()
block|{
return|return
name|directory
return|;
block|}
DECL|method|setDirectory (boolean directory)
specifier|public
name|void
name|setDirectory
parameter_list|(
name|boolean
name|directory
parameter_list|)
block|{
name|this
operator|.
name|directory
operator|=
name|directory
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
DECL|method|getOperations ()
specifier|public
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|getOperations
parameter_list|()
block|{
return|return
name|operations
return|;
block|}
DECL|method|setOperations (GenericFileOperations<T> operations)
specifier|public
name|void
name|setOperations
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|)
block|{
name|this
operator|.
name|operations
operator|=
name|operations
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
comment|/**      * Should the file be moved after consuming?      */
DECL|method|isMoveFile ()
specifier|public
name|boolean
name|isMoveFile
parameter_list|()
block|{
return|return
name|moveNamePostfix
operator|!=
literal|null
operator|||
name|moveNamePrefix
operator|!=
literal|null
operator|||
name|preMoveNamePostfix
operator|!=
literal|null
operator|||
name|preMoveNamePrefix
operator|!=
literal|null
operator|||
name|expression
operator|!=
literal|null
return|;
block|}
comment|/**      * Configures the given message with the file which sets the body to the      * file object and sets the {@link FileComponent#HEADER_FILE_NAME} header.      */
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
comment|// compute the name that was written, it should be relative to the endpoint configuraion
name|String
name|name
init|=
name|file
operator|.
name|getRelativeFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|isDirectory
argument_list|()
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
argument_list|)
condition|)
block|{
comment|// remove the file path configured on the endpoint for directory=true
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isDirectory
argument_list|()
condition|)
block|{
comment|// use the filename for directory=false
name|name
operator|=
name|file
operator|.
name|getFileName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|// skip trailing /
name|name
operator|=
name|name
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
name|NewFileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
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
name|readLockTimeout
argument_list|)
expr_stmt|;
block|}
return|return
name|params
return|;
block|}
block|}
end_class

end_unit

