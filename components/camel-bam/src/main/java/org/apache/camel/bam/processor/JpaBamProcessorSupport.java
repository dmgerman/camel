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
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
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
name|camel
operator|.
name|bam
operator|.
name|model
operator|.
name|ProcessDefinition
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
name|bam
operator|.
name|rules
operator|.
name|ActivityRules
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
name|IntrospectionSupport
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
name|orm
operator|.
name|jpa
operator|.
name|JpaTemplate
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

begin_comment
comment|/**  * A base class for JPA based BAM which can use any entity to store the process  * instance information which allows derived classes to specialise the process  * instance entity.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JpaBamProcessorSupport
specifier|public
class|class
name|JpaBamProcessorSupport
parameter_list|<
name|T
parameter_list|>
extends|extends
name|BamProcessorSupport
argument_list|<
name|T
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
name|JpaBamProcessorSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LOCK
specifier|private
specifier|static
specifier|final
name|Lock
name|LOCK
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
comment|// lock used for concurrency issues
DECL|field|activityRules
specifier|private
name|ActivityRules
name|activityRules
decl_stmt|;
DECL|field|template
specifier|private
name|JpaTemplate
name|template
decl_stmt|;
DECL|field|findByKeyQuery
specifier|private
name|String
name|findByKeyQuery
decl_stmt|;
DECL|field|keyPropertyName
specifier|private
name|String
name|keyPropertyName
init|=
literal|"correlationKey"
decl_stmt|;
DECL|field|correlationKeyIsPrimary
specifier|private
name|boolean
name|correlationKeyIsPrimary
init|=
literal|true
decl_stmt|;
DECL|method|JpaBamProcessorSupport (TransactionTemplate transactionTemplate, JpaTemplate template, Expression correlationKeyExpression, ActivityRules activityRules, Class<T> entitytype)
specifier|public
name|JpaBamProcessorSupport
parameter_list|(
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|JpaTemplate
name|template
parameter_list|,
name|Expression
name|correlationKeyExpression
parameter_list|,
name|ActivityRules
name|activityRules
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|entitytype
parameter_list|)
block|{
name|super
argument_list|(
name|transactionTemplate
argument_list|,
name|correlationKeyExpression
argument_list|,
name|entitytype
argument_list|)
expr_stmt|;
name|this
operator|.
name|activityRules
operator|=
name|activityRules
expr_stmt|;
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
DECL|method|JpaBamProcessorSupport (TransactionTemplate transactionTemplate, JpaTemplate template, Expression correlationKeyExpression, ActivityRules activityRules)
specifier|public
name|JpaBamProcessorSupport
parameter_list|(
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|JpaTemplate
name|template
parameter_list|,
name|Expression
name|correlationKeyExpression
parameter_list|,
name|ActivityRules
name|activityRules
parameter_list|)
block|{
name|super
argument_list|(
name|transactionTemplate
argument_list|,
name|correlationKeyExpression
argument_list|)
expr_stmt|;
name|this
operator|.
name|activityRules
operator|=
name|activityRules
expr_stmt|;
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
DECL|method|getFindByKeyQuery ()
specifier|public
name|String
name|getFindByKeyQuery
parameter_list|()
block|{
if|if
condition|(
name|findByKeyQuery
operator|==
literal|null
condition|)
block|{
name|findByKeyQuery
operator|=
name|createFindByKeyQuery
argument_list|()
expr_stmt|;
block|}
return|return
name|findByKeyQuery
return|;
block|}
DECL|method|setFindByKeyQuery (String findByKeyQuery)
specifier|public
name|void
name|setFindByKeyQuery
parameter_list|(
name|String
name|findByKeyQuery
parameter_list|)
block|{
name|this
operator|.
name|findByKeyQuery
operator|=
name|findByKeyQuery
expr_stmt|;
block|}
DECL|method|getActivityRules ()
specifier|public
name|ActivityRules
name|getActivityRules
parameter_list|()
block|{
return|return
name|activityRules
return|;
block|}
DECL|method|setActivityRules (ActivityRules activityRules)
specifier|public
name|void
name|setActivityRules
parameter_list|(
name|ActivityRules
name|activityRules
parameter_list|)
block|{
name|this
operator|.
name|activityRules
operator|=
name|activityRules
expr_stmt|;
block|}
DECL|method|getKeyPropertyName ()
specifier|public
name|String
name|getKeyPropertyName
parameter_list|()
block|{
return|return
name|keyPropertyName
return|;
block|}
DECL|method|setKeyPropertyName (String keyPropertyName)
specifier|public
name|void
name|setKeyPropertyName
parameter_list|(
name|String
name|keyPropertyName
parameter_list|)
block|{
name|this
operator|.
name|keyPropertyName
operator|=
name|keyPropertyName
expr_stmt|;
block|}
DECL|method|getTemplate ()
specifier|public
name|JpaTemplate
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|setTemplate (JpaTemplate template)
specifier|public
name|void
name|setTemplate
parameter_list|(
name|JpaTemplate
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
DECL|method|isCorrelationKeyIsPrimary ()
specifier|public
name|boolean
name|isCorrelationKeyIsPrimary
parameter_list|()
block|{
return|return
name|correlationKeyIsPrimary
return|;
block|}
DECL|method|setCorrelationKeyIsPrimary (boolean correlationKeyIsPrimary)
specifier|public
name|void
name|setCorrelationKeyIsPrimary
parameter_list|(
name|boolean
name|correlationKeyIsPrimary
parameter_list|)
block|{
name|this
operator|.
name|correlationKeyIsPrimary
operator|=
name|correlationKeyIsPrimary
expr_stmt|;
block|}
comment|// Implementatiom methods
comment|// -----------------------------------------------------------------------
DECL|method|loadEntity (Exchange exchange, Object key)
specifier|protected
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
block|{
name|LOCK
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|T
name|entity
init|=
name|findEntityByCorrelationKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
name|entity
operator|=
name|createEntity
argument_list|(
name|exchange
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|setKeyProperty
argument_list|(
name|entity
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|ProcessDefinition
name|definition
init|=
name|ProcessDefinition
operator|.
name|getRefreshedProcessDefinition
argument_list|(
name|template
argument_list|,
name|getActivityRules
argument_list|()
operator|.
name|getProcessRules
argument_list|()
operator|.
name|getProcessDefinition
argument_list|()
argument_list|)
decl_stmt|;
name|setProcessDefinitionProperty
argument_list|(
name|entity
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|template
operator|.
name|persist
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// Now we must flush to avoid concurrent updates clashing trying to
comment|// insert the same row
name|LOG
operator|.
name|debug
argument_list|(
literal|"About to flush on entity: "
operator|+
name|entity
operator|+
literal|" with key: "
operator|+
name|key
argument_list|)
expr_stmt|;
name|template
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
return|return
name|entity
return|;
block|}
finally|finally
block|{
name|LOCK
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|findEntityByCorrelationKey (Object key)
specifier|protected
name|T
name|findEntityByCorrelationKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
name|isCorrelationKeyIsPrimary
argument_list|()
condition|)
block|{
return|return
name|template
operator|.
name|find
argument_list|(
name|getEntityType
argument_list|()
argument_list|,
name|key
argument_list|)
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
name|template
operator|.
name|find
argument_list|(
name|getFindByKeyQuery
argument_list|()
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
block|}
DECL|method|getKeyType ()
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getKeyType
parameter_list|()
block|{
try|try
block|{
name|Method
name|getter
init|=
name|IntrospectionSupport
operator|.
name|getPropertyGetter
argument_list|(
name|getEntityType
argument_list|()
argument_list|,
name|getKeyPropertyName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|getter
operator|.
name|getReturnType
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"no such getter for: "
operator|+
name|getKeyPropertyName
argument_list|()
operator|+
literal|" on "
operator|+
name|getEntityType
argument_list|()
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Sets the key property on the new entity      */
DECL|method|setKeyProperty (T entity, Object key)
specifier|protected
name|void
name|setKeyProperty
parameter_list|(
name|T
name|entity
parameter_list|,
name|Object
name|key
parameter_list|)
throws|throws
name|Exception
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|entity
argument_list|,
name|getKeyPropertyName
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|setProcessDefinitionProperty (T entity, ProcessDefinition processDefinition)
specifier|protected
name|void
name|setProcessDefinitionProperty
parameter_list|(
name|T
name|entity
parameter_list|,
name|ProcessDefinition
name|processDefinition
parameter_list|)
throws|throws
name|Exception
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|entity
argument_list|,
literal|"processDefinition"
argument_list|,
name|processDefinition
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a new instance of the entity for the given key      */
DECL|method|createEntity (Exchange exchange, Object key)
specifier|protected
name|T
name|createEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|key
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|getEntityType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|processEntity (Exchange exchange, T entity)
specifier|protected
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
block|{
if|if
condition|(
name|entity
operator|instanceof
name|Processor
condition|)
block|{
name|Processor
name|processor
init|=
operator|(
name|Processor
operator|)
name|entity
decl_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO add other extension points - eg. passing in Activity
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No processor defined for this route"
argument_list|)
throw|;
block|}
block|}
DECL|method|createFindByKeyQuery ()
specifier|protected
name|String
name|createFindByKeyQuery
parameter_list|()
block|{
return|return
literal|"select x from "
operator|+
name|getEntityType
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" x where x."
operator|+
name|getKeyPropertyName
argument_list|()
operator|+
literal|" = ?1"
return|;
block|}
block|}
end_class

end_unit

