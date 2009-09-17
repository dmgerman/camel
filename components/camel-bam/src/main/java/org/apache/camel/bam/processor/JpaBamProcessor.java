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
name|bam
operator|.
name|model
operator|.
name|ActivityState
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
name|ProcessInstance
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
comment|/**  * A concrete {@link Processor} for working on<a  * href="http://camel.apache.org/bam.html">BAM</a> which uses JPA as  * the persistence and uses the {@link ProcessInstance} entity to store the  * process information.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|JpaBamProcessor
specifier|public
class|class
name|JpaBamProcessor
extends|extends
name|JpaBamProcessorSupport
argument_list|<
name|ProcessInstance
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
name|JpaBamProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|JpaBamProcessor (TransactionTemplate transactionTemplate, JpaTemplate template, Expression correlationKeyExpression, ActivityRules activityRules)
specifier|public
name|JpaBamProcessor
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
name|template
argument_list|,
name|correlationKeyExpression
argument_list|,
name|activityRules
argument_list|)
expr_stmt|;
block|}
DECL|method|JpaBamProcessor (TransactionTemplate transactionTemplate, JpaTemplate template, Expression correlationKeyExpression, ActivityRules activityRules, Class<ProcessInstance> entitytype)
specifier|public
name|JpaBamProcessor
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
name|ProcessInstance
argument_list|>
name|entitytype
parameter_list|)
block|{
name|super
argument_list|(
name|transactionTemplate
argument_list|,
name|template
argument_list|,
name|correlationKeyExpression
argument_list|,
name|activityRules
argument_list|,
name|entitytype
argument_list|)
expr_stmt|;
block|}
DECL|method|processEntity (Exchange exchange, ProcessInstance process)
specifier|protected
name|void
name|processEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ProcessInstance
name|process
parameter_list|)
throws|throws
name|Exception
block|{
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
literal|"Processing process instance: "
operator|+
name|process
argument_list|)
expr_stmt|;
block|}
comment|// lets force the lazy creation of this activity
name|ActivityRules
name|rules
init|=
name|getActivityRules
argument_list|()
decl_stmt|;
name|ActivityState
name|state
init|=
name|process
operator|.
name|getOrCreateActivityState
argument_list|(
name|rules
argument_list|)
decl_stmt|;
name|state
operator|.
name|processExchange
argument_list|(
name|rules
argument_list|,
operator|new
name|ProcessContext
argument_list|(
name|exchange
argument_list|,
name|rules
argument_list|,
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|rules
operator|.
name|getProcessRules
argument_list|()
operator|.
name|processExchange
argument_list|(
name|exchange
argument_list|,
name|process
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

