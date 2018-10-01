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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|NamedNode
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
name|ErrorHandlerBuilder
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
name|ErrorHandlerBuilderRef
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
name|model
operator|.
name|RouteDefinition
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
comment|/**  * Wraps the processor in a Spring transaction  */
end_comment

begin_class
DECL|class|SpringTransactionPolicy
specifier|public
class|class
name|SpringTransactionPolicy
implements|implements
name|TransactedPolicy
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
name|SpringTransactionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|template
specifier|private
name|TransactionTemplate
name|template
decl_stmt|;
DECL|field|propagationBehaviorName
specifier|private
name|String
name|propagationBehaviorName
decl_stmt|;
DECL|field|transactionManager
specifier|private
name|PlatformTransactionManager
name|transactionManager
decl_stmt|;
comment|/**      * Default constructor for easy spring configuration.      */
DECL|method|SpringTransactionPolicy ()
specifier|public
name|SpringTransactionPolicy
parameter_list|()
block|{     }
DECL|method|SpringTransactionPolicy (TransactionTemplate template)
specifier|public
name|SpringTransactionPolicy
parameter_list|(
name|TransactionTemplate
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
DECL|method|SpringTransactionPolicy (PlatformTransactionManager transactionManager)
specifier|public
name|SpringTransactionPolicy
parameter_list|(
name|PlatformTransactionManager
name|transactionManager
parameter_list|)
block|{
name|this
operator|.
name|transactionManager
operator|=
name|transactionManager
expr_stmt|;
block|}
DECL|method|beforeWrap (RouteContext routeContext, NamedNode definition)
specifier|public
name|void
name|beforeWrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
block|{     }
DECL|method|wrap (RouteContext routeContext, Processor processor)
specifier|public
name|Processor
name|wrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|TransactionErrorHandler
name|answer
decl_stmt|;
comment|// the goal is to configure the error handler builder on the route as a transacted error handler,
comment|// either its already a transacted or if not we replace it with a transacted one that we configure here
comment|// and wrap the processor in the transacted error handler as we can have transacted routes that change
comment|// propagation behavior, eg: from A required -> B -> requiresNew C (advanced use-case)
comment|// if we should not support this we do not need to wrap the processor as we only need one transacted error handler
comment|// find the existing error handler builder
name|RouteDefinition
name|route
init|=
operator|(
name|RouteDefinition
operator|)
name|routeContext
operator|.
name|getRoute
argument_list|()
decl_stmt|;
name|ErrorHandlerBuilder
name|builder
init|=
operator|(
name|ErrorHandlerBuilder
operator|)
name|route
operator|.
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
comment|// check if its a ref if so then do a lookup
if|if
condition|(
name|builder
operator|instanceof
name|ErrorHandlerBuilderRef
condition|)
block|{
comment|// its a reference to a error handler so lookup the reference
name|ErrorHandlerBuilderRef
name|builderRef
init|=
operator|(
name|ErrorHandlerBuilderRef
operator|)
name|builder
decl_stmt|;
name|String
name|ref
init|=
name|builderRef
operator|.
name|getRef
argument_list|()
decl_stmt|;
comment|// only lookup if there was explicit an error handler builder configured
comment|// otherwise its just the "default" that has not explicit been configured
comment|// and if so then we can safely replace that with our transacted error handler
if|if
condition|(
name|ErrorHandlerBuilderRef
operator|.
name|isErrorHandlerBuilderConfigured
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Looking up ErrorHandlerBuilder with ref: {}"
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|builder
operator|=
operator|(
name|ErrorHandlerBuilder
operator|)
name|ErrorHandlerBuilderRef
operator|.
name|lookupErrorHandlerBuilder
argument_list|(
name|routeContext
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|builder
operator|!=
literal|null
operator|&&
name|builder
operator|.
name|supportTransacted
argument_list|()
condition|)
block|{
comment|// already a TX error handler then we are good to go
name|LOG
operator|.
name|debug
argument_list|(
literal|"The ErrorHandlerBuilder configured is already a TransactionErrorHandlerBuilder: {}"
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|answer
operator|=
name|createTransactionErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setExceptionPolicy
argument_list|(
name|builder
operator|.
name|getExceptionPolicyStrategy
argument_list|()
argument_list|)
expr_stmt|;
comment|// configure our answer based on the existing error handler
name|builder
operator|.
name|configure
argument_list|(
name|routeContext
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no transaction error handler builder configure so create a temporary one as we got all
comment|// the needed information form the configured builder anyway this allow us to use transacted
comment|// routes anyway even though the error handler is not transactional, eg ease of configuration
if|if
condition|(
name|builder
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"The ErrorHandlerBuilder configured is not a TransactionErrorHandlerBuilder: {}"
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No ErrorHandlerBuilder configured, will use default TransactionErrorHandlerBuilder settings"
argument_list|)
expr_stmt|;
block|}
name|TransactionErrorHandlerBuilder
name|txBuilder
init|=
operator|new
name|TransactionErrorHandlerBuilder
argument_list|()
decl_stmt|;
name|txBuilder
operator|.
name|setTransactionTemplate
argument_list|(
name|getTransactionTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|txBuilder
operator|.
name|setSpringTransactionPolicy
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|builder
operator|!=
literal|null
condition|)
block|{
comment|// use error handlers from the configured builder
name|txBuilder
operator|.
name|setErrorHandlers
argument_list|(
name|routeContext
argument_list|,
name|builder
operator|.
name|getErrorHandlers
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|createTransactionErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|,
name|txBuilder
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setExceptionPolicy
argument_list|(
name|txBuilder
operator|.
name|getExceptionPolicyStrategy
argument_list|()
argument_list|)
expr_stmt|;
comment|// configure our answer based on the existing error handler
name|txBuilder
operator|.
name|configure
argument_list|(
name|routeContext
argument_list|,
name|answer
argument_list|)
expr_stmt|;
comment|// set the route to use our transacted error handler builder
name|route
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|txBuilder
argument_list|)
expr_stmt|;
block|}
comment|// return with wrapped transacted error handler
return|return
name|answer
return|;
block|}
DECL|method|createTransactionErrorHandler (RouteContext routeContext, Processor processor, ErrorHandlerBuilder builder)
specifier|protected
name|TransactionErrorHandler
name|createTransactionErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ErrorHandlerBuilder
name|builder
parameter_list|)
block|{
name|TransactionErrorHandler
name|answer
decl_stmt|;
try|try
block|{
name|answer
operator|=
operator|(
name|TransactionErrorHandler
operator|)
name|builder
operator|.
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getTransactionTemplate ()
specifier|public
name|TransactionTemplate
name|getTransactionTemplate
parameter_list|()
block|{
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|transactionManager
argument_list|,
literal|"transactionManager"
argument_list|)
expr_stmt|;
name|template
operator|=
operator|new
name|TransactionTemplate
argument_list|(
name|transactionManager
argument_list|)
expr_stmt|;
if|if
condition|(
name|propagationBehaviorName
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|setPropagationBehaviorName
argument_list|(
name|propagationBehaviorName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|template
return|;
block|}
DECL|method|setTransactionTemplate (TransactionTemplate template)
specifier|public
name|void
name|setTransactionTemplate
parameter_list|(
name|TransactionTemplate
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
name|transactionManager
operator|=
name|transactionManager
expr_stmt|;
block|}
DECL|method|getTransactionManager ()
specifier|public
name|PlatformTransactionManager
name|getTransactionManager
parameter_list|()
block|{
return|return
name|transactionManager
return|;
block|}
DECL|method|setPropagationBehaviorName (String propagationBehaviorName)
specifier|public
name|void
name|setPropagationBehaviorName
parameter_list|(
name|String
name|propagationBehaviorName
parameter_list|)
block|{
name|this
operator|.
name|propagationBehaviorName
operator|=
name|propagationBehaviorName
expr_stmt|;
block|}
DECL|method|getPropagationBehaviorName ()
specifier|public
name|String
name|getPropagationBehaviorName
parameter_list|()
block|{
return|return
name|propagationBehaviorName
return|;
block|}
block|}
end_class

end_unit

