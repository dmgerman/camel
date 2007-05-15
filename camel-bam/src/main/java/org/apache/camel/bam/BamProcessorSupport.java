begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_comment
comment|/**  * A {@link Processor} for working on  *<a href="http://activemq.apache.org/camel/bam.html">BAM</a>  *  * @version $Revision: $  */
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
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
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
argument_list|<
name|Exchange
argument_list|>
name|correlationKeyExpression
decl_stmt|;
DECL|method|BamProcessorSupport (Expression<Exchange> correlationKeyExpression)
specifier|protected
name|BamProcessorSupport
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlationKeyExpression
parameter_list|)
block|{
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
DECL|method|BamProcessorSupport (Class<T> entitytype, Expression<Exchange> correlationKeyExpression)
specifier|protected
name|BamProcessorSupport
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|entitytype
parameter_list|,
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlationKeyExpression
parameter_list|)
block|{
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
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
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
name|log
operator|.
name|info
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
comment|//storeProcessInExchange(exchange, entity);
name|processEntity
argument_list|(
name|exchange
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getCorrelationKeyExpression ()
specifier|public
name|Expression
argument_list|<
name|Exchange
argument_list|>
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
comment|//-----------------------------------------------------------------------
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
init|=
name|correlationKeyExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
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
block|}
end_class

end_unit

