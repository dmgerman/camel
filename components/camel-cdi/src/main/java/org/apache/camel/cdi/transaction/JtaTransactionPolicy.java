begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.transaction
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|transaction
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|TransactionManager
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
name|RuntimeCamelException
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
name|ProcessorDefinition
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

begin_comment
comment|/**  * Sets a proper error handler. This class is based on  * {@link org.apache.camel.spring.spi.SpringTransactionPolicy}.  *<p>  * This class requires the resource {@link TransactionManager} to be available  * through JNDI url&quot;java:/TransactionManager&quot;  */
end_comment

begin_class
DECL|class|JtaTransactionPolicy
specifier|public
specifier|abstract
class|class
name|JtaTransactionPolicy
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
name|JtaTransactionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Runnable
specifier|public
interface|interface
name|Runnable
block|{
DECL|method|run ()
name|void
name|run
parameter_list|()
throws|throws
name|Throwable
function_decl|;
block|}
annotation|@
name|Resource
argument_list|(
name|lookup
operator|=
literal|"java:/TransactionManager"
argument_list|)
DECL|field|transactionManager
specifier|protected
name|TransactionManager
name|transactionManager
decl_stmt|;
annotation|@
name|Override
DECL|method|beforeWrap (RouteContext routeContext, ProcessorDefinition<?> definition)
specifier|public
name|void
name|beforeWrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
comment|// do not inherit since we create our own
comment|// (otherwise the default error handler would be used two times
comment|// because we inherit it on our own but only in case of a
comment|// non-transactional
comment|// error handler)
name|definition
operator|.
name|setInheritErrorHandler
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|run (final Runnable runnable)
specifier|public
specifier|abstract
name|void
name|run
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
throws|throws
name|Throwable
function_decl|;
annotation|@
name|Override
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
name|JtaTransactionErrorHandler
name|answer
decl_stmt|;
comment|// the goal is to configure the error handler builder on the route as a
comment|// transacted error handler,
comment|// either its already a transacted or if not we replace it with a
comment|// transacted one that we configure here
comment|// and wrap the processor in the transacted error handler as we can have
comment|// transacted routes that change
comment|// propagation behavior, eg: from A required -> B -> requiresNew C
comment|// (advanced use-case)
comment|// if we should not support this we do not need to wrap the processor as
comment|// we only need one transacted error handler
comment|// find the existing error handler builder
name|ErrorHandlerBuilder
name|builder
init|=
operator|(
name|ErrorHandlerBuilder
operator|)
name|routeContext
operator|.
name|getRoute
argument_list|()
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
comment|// only lookup if there was explicit an error handler builder
comment|// configured
comment|// otherwise its just the "default" that has not explicit been
comment|// configured
comment|// and if so then we can safely replace that with our transacted
comment|// error handler
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
name|JtaTransactionErrorHandlerBuilder
name|txBuilder
decl_stmt|;
if|if
condition|(
operator|(
name|builder
operator|!=
literal|null
operator|)
operator|&&
name|builder
operator|.
name|supportTransacted
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|builder
operator|instanceof
name|JtaTransactionErrorHandlerBuilder
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"The given transactional error handler builder '"
operator|+
name|builder
operator|+
literal|"' is not of type '"
operator|+
name|JtaTransactionErrorHandlerBuilder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"' which is required in this environment!"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"The ErrorHandlerBuilder configured is a JtaTransactionErrorHandlerBuilder: {}"
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|txBuilder
operator|=
operator|(
name|JtaTransactionErrorHandlerBuilder
operator|)
name|builder
operator|.
name|cloneBuilder
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No or no transactional ErrorHandlerBuilder configured, will use default JtaTransactionErrorHandlerBuilder settings"
argument_list|)
expr_stmt|;
name|txBuilder
operator|=
operator|new
name|JtaTransactionErrorHandlerBuilder
argument_list|()
expr_stmt|;
block|}
name|txBuilder
operator|.
name|setTransactionPolicy
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// use error handlers from the configured builder
if|if
condition|(
name|builder
operator|!=
literal|null
condition|)
block|{
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
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|txBuilder
argument_list|)
expr_stmt|;
comment|// return with wrapped transacted error handler
return|return
name|answer
return|;
block|}
DECL|method|createTransactionErrorHandler (RouteContext routeContext, Processor processor, ErrorHandlerBuilder builder)
specifier|protected
name|JtaTransactionErrorHandler
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
name|JtaTransactionErrorHandler
name|answer
decl_stmt|;
try|try
block|{
name|answer
operator|=
operator|(
name|JtaTransactionErrorHandler
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

