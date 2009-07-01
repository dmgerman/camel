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
name|impl
operator|.
name|LoggingExceptionHandler
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
name|ExceptionHandler
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
comment|/**  * On completion strategy that performs the nessasary work after the {@link Exchange} has been processed.  *<p/>  * The work is for example to move the processed file into a backup folder, delete the file or  * in case of processing failure do a rollback.   *  * @version $Revision$  */
end_comment

begin_class
DECL|class|GenericFileOnCompletion
specifier|public
class|class
name|GenericFileOnCompletion
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Synchronization
block|{
DECL|field|log
specifier|private
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|GenericFileOnCompletion
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|operations
specifier|private
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|method|GenericFileOnCompletion (GenericFileEndpoint<T> endpoint, GenericFileOperations<T> operations)
specifier|public
name|GenericFileOnCompletion
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|operations
operator|=
name|operations
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|onCompletion
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|onCompletion
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getExceptionHandler ()
specifier|public
name|ExceptionHandler
name|getExceptionHandler
parameter_list|()
block|{
if|if
condition|(
name|exceptionHandler
operator|==
literal|null
condition|)
block|{
name|exceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exceptionHandler
return|;
block|}
DECL|method|setExceptionHandler (ExceptionHandler exceptionHandler)
specifier|public
name|void
name|setExceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|this
operator|.
name|exceptionHandler
operator|=
name|exceptionHandler
expr_stmt|;
block|}
DECL|method|onCompletion (Exchange exchange)
specifier|protected
name|void
name|onCompletion
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
init|=
name|endpoint
operator|.
name|getGenericFileProcessStrategy
argument_list|()
decl_stmt|;
comment|// after processing
specifier|final
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
init|=
operator|(
name|GenericFile
argument_list|<
name|T
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
decl_stmt|;
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
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
literal|"Done processing file: "
operator|+
name|file
operator|+
literal|" using exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// commit or rollback
name|boolean
name|committed
init|=
literal|false
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|failed
condition|)
block|{
comment|// commit the file strategy if there was no failure or already handled by the DeadLetterChannel
name|processStrategyCommit
argument_list|(
name|processStrategy
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|committed
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// if the failure was an exception then handle it
name|handleException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|committed
condition|)
block|{
name|processStrategyRollback
argument_list|(
name|processStrategy
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|// remove file from the in progress list as its no longer in progress
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
operator|.
name|remove
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy when the file was processed and a commit should be executed.      *      * @param processStrategy the strategy to perform the commit      * @param exchange        the exchange      * @param file            the file processed      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|processStrategyCommit (GenericFileProcessStrategy<T> processStrategy, Exchange exchange, GenericFile<T> file)
specifier|protected
name|void
name|processStrategyCommit
parameter_list|(
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isIdempotent
argument_list|()
condition|)
block|{
comment|// only add to idempotent repository if we could process the file
comment|// only use the filename as the key as the file could be moved into a done folder
name|endpoint
operator|.
name|getIdempotentRepository
argument_list|()
operator|.
name|add
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Committing remote file strategy: "
operator|+
name|processStrategy
operator|+
literal|" for file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
name|processStrategy
operator|.
name|commit
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy when the file was not processed and a rollback should be executed.      *      * @param processStrategy the strategy to perform the commit      * @param exchange        the exchange      * @param file            the file processed      */
DECL|method|processStrategyRollback (GenericFileProcessStrategy<T> processStrategy, Exchange exchange, GenericFile<T> file)
specifier|protected
name|void
name|processStrategyRollback
parameter_list|(
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Rolling back remote file strategy: "
operator|+
name|processStrategy
operator|+
literal|" for file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|processStrategy
operator|.
name|rollback
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleException (Throwable t)
specifier|protected
name|void
name|handleException
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|Throwable
name|newt
init|=
operator|(
name|t
operator|==
literal|null
operator|)
condition|?
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Handling [null] exception"
argument_list|)
else|:
name|t
decl_stmt|;
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|newt
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"GenericFileOnCompletion"
return|;
block|}
block|}
end_class

end_unit

