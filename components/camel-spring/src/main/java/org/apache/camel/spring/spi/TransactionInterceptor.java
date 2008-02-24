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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|TransactionInterceptor
specifier|public
class|class
name|TransactionInterceptor
extends|extends
name|DelegateProcessor
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
name|TransactionInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|transactionTemplate
specifier|private
specifier|final
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|method|TransactionInterceptor (TransactionTemplate transactionTemplate)
specifier|public
name|TransactionInterceptor
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
name|LOG
operator|.
name|info
argument_list|(
literal|"transaction begin"
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"transaction commit"
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
literal|"TransactionInterceptor:"
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
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|propagationBehaviorToString (int propagationBehavior)
specifier|private
name|String
name|propagationBehaviorToString
parameter_list|(
name|int
name|propagationBehavior
parameter_list|)
block|{
name|String
name|rc
decl_stmt|;
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
name|rc
operator|=
literal|"PROPAGATION_MANDATORY"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NESTED
case|:
name|rc
operator|=
literal|"PROPAGATION_NESTED"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NEVER
case|:
name|rc
operator|=
literal|"PROPAGATION_NEVER"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_NOT_SUPPORTED
case|:
name|rc
operator|=
literal|"PROPAGATION_NOT_SUPPORTED"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRED
case|:
name|rc
operator|=
literal|"PROPAGATION_REQUIRED"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRES_NEW
case|:
name|rc
operator|=
literal|"PROPAGATION_REQUIRES_NEW"
expr_stmt|;
break|break;
case|case
name|TransactionDefinition
operator|.
name|PROPAGATION_SUPPORTS
case|:
name|rc
operator|=
literal|"PROPAGATION_SUPPORTS"
expr_stmt|;
break|break;
default|default:
name|rc
operator|=
literal|"UNKOWN"
expr_stmt|;
block|}
return|return
name|rc
return|;
block|}
block|}
end_class

end_unit

