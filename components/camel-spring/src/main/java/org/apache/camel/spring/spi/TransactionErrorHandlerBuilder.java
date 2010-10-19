begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|spi
package|;
end_package

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
name|LoggingLevel
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
name|builder
operator|.
name|DefaultErrorHandlerBuilder
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
name|Logger
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
name|RouteContext
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
name|TransactedPolicy
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
name|PlatformTransactionManager
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
comment|/**  * A transactional error handler that supports leveraging Spring TransactionManager.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|TransactionErrorHandlerBuilder
specifier|public
class|class
name|TransactionErrorHandlerBuilder
extends|extends
name|DefaultErrorHandlerBuilder
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
name|TransactionErrorHandlerBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PROPAGATION_REQUIRED
specifier|private
specifier|static
specifier|final
name|String
name|PROPAGATION_REQUIRED
init|=
literal|"PROPAGATION_REQUIRED"
decl_stmt|;
DECL|field|transactionTemplate
specifier|private
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|method|TransactionErrorHandlerBuilder ()
specifier|public
name|TransactionErrorHandlerBuilder
parameter_list|()
block|{
comment|// no-arg constructor used by Spring DSL
block|}
DECL|method|getTransactionTemplate ()
specifier|public
name|TransactionTemplate
name|getTransactionTemplate
parameter_list|()
block|{
return|return
name|transactionTemplate
return|;
block|}
DECL|method|supportTransacted ()
specifier|public
name|boolean
name|supportTransacted
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createErrorHandler (RouteContext routeContext, Processor processor)
specifier|public
name|Processor
name|createErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|transactionTemplate
operator|==
literal|null
condition|)
block|{
comment|// lookup in context if no transaction template has been configured
name|LOG
operator|.
name|debug
argument_list|(
literal|"No TransactionTemplate configured on TransactionErrorHandlerBuilder. Will try find it in the registry."
argument_list|)
expr_stmt|;
if|if
condition|(
name|transactionTemplate
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|TransactedPolicy
argument_list|>
name|map
init|=
name|routeContext
operator|.
name|lookupByType
argument_list|(
name|TransactedPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|TransactedPolicy
name|policy
init|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
operator|&&
name|policy
operator|instanceof
name|SpringTransactionPolicy
condition|)
block|{
name|transactionTemplate
operator|=
operator|(
operator|(
name|SpringTransactionPolicy
operator|)
name|policy
operator|)
operator|.
name|getTransactionTemplate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|transactionTemplate
operator|==
literal|null
condition|)
block|{
name|TransactedPolicy
name|policy
init|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|PROPAGATION_REQUIRED
argument_list|,
name|TransactedPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
operator|&&
name|policy
operator|instanceof
name|SpringTransactionPolicy
condition|)
block|{
name|transactionTemplate
operator|=
operator|(
operator|(
name|SpringTransactionPolicy
operator|)
name|policy
operator|)
operator|.
name|getTransactionTemplate
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|transactionTemplate
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|TransactionTemplate
argument_list|>
name|map
init|=
name|routeContext
operator|.
name|lookupByType
argument_list|(
name|TransactionTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|transactionTemplate
operator|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
operator|||
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No TransactionTemplate found in registry."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found "
operator|+
name|map
operator|.
name|size
argument_list|()
operator|+
literal|" TransactionTemplate in registry. "
operator|+
literal|"Cannot determine which one to use. Please configure a TransactionTemplate on the TransactionErrorHandlerBuilder"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|transactionTemplate
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PlatformTransactionManager
argument_list|>
name|map
init|=
name|routeContext
operator|.
name|lookupByType
argument_list|(
name|PlatformTransactionManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|transactionTemplate
operator|=
operator|new
name|TransactionTemplate
argument_list|(
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
operator|||
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No PlatformTransactionManager found in registry."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found "
operator|+
name|map
operator|.
name|size
argument_list|()
operator|+
literal|" PlatformTransactionManager in registry. "
operator|+
literal|"Cannot determine which one to use for TransactionTemplate. Please configure a TransactionTemplate on the TransactionErrorHandlerBuilder"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|transactionTemplate
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found TransactionTemplate in registry to use: "
operator|+
name|transactionTemplate
argument_list|)
expr_stmt|;
block|}
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|transactionTemplate
argument_list|,
literal|"transactionTemplate"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|TransactionErrorHandler
name|answer
init|=
operator|new
name|TransactionErrorHandler
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|processor
argument_list|,
name|getLogger
argument_list|()
argument_list|,
name|getOnRedelivery
argument_list|()
argument_list|,
name|getRedeliveryPolicy
argument_list|()
argument_list|,
name|getHandledPolicy
argument_list|()
argument_list|,
name|getExceptionPolicyStrategy
argument_list|()
argument_list|,
name|transactionTemplate
argument_list|,
name|getRetryWhilePolicy
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// configure error handler before we can use it
name|configure
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|setTransactionTemplate (TransactionTemplate transactionTemplate)
specifier|public
name|void
name|setTransactionTemplate
parameter_list|(
name|TransactionTemplate
name|transactionTemplate
parameter_list|)
block|{
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
block|}
DECL|method|setSpringTransactionPolicy (SpringTransactionPolicy policy)
specifier|public
name|void
name|setSpringTransactionPolicy
parameter_list|(
name|SpringTransactionPolicy
name|policy
parameter_list|)
block|{
name|this
operator|.
name|transactionTemplate
operator|=
name|policy
operator|.
name|getTransactionTemplate
argument_list|()
expr_stmt|;
block|}
DECL|method|setTransactionManager (PlatformTransactionManager transactionManager)
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|PlatformTransactionManager
name|transactionManager
parameter_list|)
block|{
name|this
operator|.
name|transactionTemplate
operator|=
operator|new
name|TransactionTemplate
argument_list|(
name|transactionManager
argument_list|)
expr_stmt|;
block|}
comment|// Builder methods
comment|// -------------------------------------------------------------------------
DECL|method|createLogger ()
specifier|protected
name|Logger
name|createLogger
parameter_list|()
block|{
return|return
operator|new
name|Logger
argument_list|(
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TransactionErrorHandler
operator|.
name|class
argument_list|)
argument_list|,
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
return|;
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
literal|"TransactionErrorHandlerBuilder"
return|;
block|}
block|}
end_class

end_unit

