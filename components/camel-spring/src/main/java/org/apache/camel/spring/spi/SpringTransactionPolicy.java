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
name|spi
operator|.
name|Policy
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
comment|/**  * Wraps the processor in a Spring transaction  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SpringTransactionPolicy
specifier|public
class|class
name|SpringTransactionPolicy
implements|implements
name|Policy
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
specifier|final
name|TransactionTemplate
name|transactionTemplate
init|=
name|getTransactionTemplate
argument_list|()
decl_stmt|;
comment|// TODO: Maybe we can auto create a template if non configured
if|if
condition|(
name|transactionTemplate
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No TransactionTemplate available so transactions will not be enabled!"
argument_list|)
expr_stmt|;
return|return
name|processor
return|;
block|}
name|TransactionErrorHandler
name|answer
init|=
operator|new
name|TransactionErrorHandler
argument_list|(
name|transactionTemplate
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setOutput
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|ErrorHandlerBuilder
name|builder
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|builder
operator|instanceof
name|ErrorHandlerBuilderRef
condition|)
block|{
comment|// its a reference to a error handler so lookup the reference
name|ErrorHandlerBuilderRef
name|ref
init|=
operator|(
name|ErrorHandlerBuilderRef
operator|)
name|builder
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
literal|"Looking up errorHandlerRef: "
operator|+
name|ref
operator|.
name|getRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|=
name|ref
operator|.
name|lookupErrorHandlerBuilder
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|builder
operator|instanceof
name|TransactionErrorHandlerBuilder
condition|)
block|{
name|TransactionErrorHandlerBuilder
name|txBuilder
init|=
operator|(
name|TransactionErrorHandlerBuilder
operator|)
name|builder
decl_stmt|;
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
name|answer
operator|.
name|setDelayPolicy
argument_list|(
name|txBuilder
operator|.
name|getDelayPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|txBuilder
operator|.
name|configure
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No TransactionErrorHandler defined so exception policies will not be enabled!"
argument_list|)
expr_stmt|;
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

