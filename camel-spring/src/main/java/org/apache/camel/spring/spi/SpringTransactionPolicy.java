begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|processor
operator|.
name|DelegateProcessor
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
name|TransactionDefinition
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
name|TransactionCallbackWithoutResult
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
comment|/**  * Wraps the processor in a Spring transaction  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|SpringTransactionPolicy
specifier|public
class|class
name|SpringTransactionPolicy
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Policy
argument_list|<
name|E
argument_list|>
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
DECL|method|wrap (Processor processor)
specifier|public
name|Processor
name|wrap
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
specifier|final
name|TransactionTemplate
name|transactionTemplate
init|=
name|getTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|transactionTemplate
operator|==
literal|null
condition|)
block|{
name|log
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
return|return
operator|new
name|DelegateProcessor
argument_list|(
name|processor
argument_list|)
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallbackWithoutResult
argument_list|()
block|{
specifier|protected
name|void
name|doInTransactionWithoutResult
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
try|try
block|{
name|processNext
argument_list|(
name|exchange
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
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SpringTransactionPolicy:"
operator|+
name|propagationBehaviorToString
argument_list|(
name|transactionTemplate
operator|.
name|getPropagationBehavior
argument_list|()
argument_list|)
operator|+
literal|"["
operator|+
name|getNext
argument_list|()
operator|+
literal|"]"
return|;
block|}
specifier|private
name|String
name|propagationBehaviorToString
parameter_list|(
name|int
name|propagationBehavior
parameter_list|)
block|{
switch|switch
condition|(
name|propagationBehavior
condition|)
block|{
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_MANDATORY
case|:
return|return
literal|"PROPAGATION_MANDATORY"
return|;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NESTED
case|:
return|return
literal|"PROPAGATION_NESTED"
return|;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NEVER
case|:
return|return
literal|"PROPAGATION_NEVER"
return|;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NOT_SUPPORTED
case|:
return|return
literal|"PROPAGATION_NOT_SUPPORTED"
return|;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRED
case|:
return|return
literal|"PROPAGATION_REQUIRED"
return|;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRES_NEW
case|:
return|return
literal|"PROPAGATION_REQUIRES_NEW"
return|;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_SUPPORTS
case|:
return|return
literal|"PROPAGATION_SUPPORTS"
return|;
block|}
return|return
literal|"UNKOWN"
return|;
block|}
block|}
return|;
block|}
DECL|method|getTemplate ()
specifier|public
name|TransactionTemplate
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|setTemplate (TransactionTemplate template)
specifier|public
name|void
name|setTemplate
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
block|}
end_class

end_unit

