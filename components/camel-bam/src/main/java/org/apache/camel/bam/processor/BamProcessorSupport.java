begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
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
name|Type
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
name|Processor
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * A base {@link Processor} for working on<a  * href="http://camel.apache.org/bam.html">BAM</a> which a derived  * class would do the actual persistence such as the {@link JpaBamProcessor}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|BamProcessorSupport
specifier|public
specifier|abstract
class|class
name|BamProcessorSupport
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Processor
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
name|BamProcessorSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|entityType
specifier|private
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
decl_stmt|;
DECL|field|correlationKeyExpression
specifier|private
name|Expression
name|correlationKeyExpression
decl_stmt|;
DECL|field|transactionTemplate
specifier|private
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|field|retryCount
specifier|private
name|int
name|retryCount
init|=
literal|20
decl_stmt|;
DECL|field|retrySleep
specifier|private
name|long
name|retrySleep
init|=
literal|1000L
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|BamProcessorSupport (TransactionTemplate transactionTemplate, Expression correlationKeyExpression)
specifier|protected
name|BamProcessorSupport
parameter_list|(
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|Expression
name|correlationKeyExpression
parameter_list|)
block|{
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
name|this
operator|.
name|correlationKeyExpression
operator|=
name|correlationKeyExpression
expr_stmt|;
name|Type
name|type
init|=
name|getClass
argument_list|()
operator|.
name|getGenericSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|parameterizedType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
name|Type
index|[]
name|arguments
init|=
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
if|if
condition|(
name|arguments
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Type
name|argumentType
init|=
name|arguments
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|argumentType
operator|instanceof
name|Class
condition|)
block|{
name|this
operator|.
name|entityType
operator|=
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|argumentType
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|entityType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not infer the entity type!"
argument_list|)
throw|;
block|}
block|}
DECL|method|BamProcessorSupport (TransactionTemplate transactionTemplate, Expression correlationKeyExpression, Class<T> entitytype)
specifier|protected
name|BamProcessorSupport
parameter_list|(
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|Expression
name|correlationKeyExpression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|entitytype
parameter_list|)
block|{
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
name|this
operator|.
name|entityType
operator|=
name|entitytype
expr_stmt|;
name|this
operator|.
name|correlationKeyExpression
operator|=
name|correlationKeyExpression
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|retryCount
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|1
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Retrying attempt: "
operator|+
name|i
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|retrySleep
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|()
block|{
specifier|public
name|Object
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
try|try
block|{
name|Object
name|key
init|=
name|getCorrelationKey
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|T
name|entity
init|=
name|loadEntity
argument_list|(
name|exchange
argument_list|,
name|key
argument_list|)
decl_stmt|;
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
literal|"Correlation key: "
operator|+
name|key
operator|+
literal|" with entity: "
operator|+
name|entity
argument_list|)
expr_stmt|;
block|}
name|processEntity
argument_list|(
name|exchange
argument_list|,
name|entity
argument_list|)
expr_stmt|;
return|return
name|entity
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|onError
argument_list|(
name|status
argument_list|,
name|e
argument_list|)
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|>
literal|1
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Attempt "
operator|+
name|i
operator|+
literal|" worked!"
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to complete transaction: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getCorrelationKeyExpression ()
specifier|public
name|Expression
name|getCorrelationKeyExpression
parameter_list|()
block|{
return|return
name|correlationKeyExpression
return|;
block|}
DECL|method|getEntityType ()
specifier|public
name|Class
argument_list|<
name|T
argument_list|>
name|getEntityType
parameter_list|()
block|{
return|return
name|entityType
return|;
block|}
comment|// Implemenation methods
comment|// -----------------------------------------------------------------------
DECL|method|processEntity (Exchange exchange, T entity)
specifier|protected
specifier|abstract
name|void
name|processEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|T
name|entity
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|loadEntity (Exchange exchange, Object key)
specifier|protected
specifier|abstract
name|T
name|loadEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|key
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|getKeyType ()
specifier|protected
specifier|abstract
name|Class
argument_list|<
name|?
argument_list|>
name|getKeyType
parameter_list|()
function_decl|;
DECL|method|getCorrelationKey (Exchange exchange)
specifier|protected
name|Object
name|getCorrelationKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|NoCorrelationKeyException
block|{
name|Object
name|value
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|keyType
init|=
name|getKeyType
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyType
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|correlationKeyExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|keyType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|correlationKeyExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoCorrelationKeyException
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
DECL|method|onError (TransactionStatus status, Exception e)
specifier|protected
name|Object
name|onError
parameter_list|(
name|TransactionStatus
name|status
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
name|status
operator|.
name|setRollbackOnly
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Caught: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

