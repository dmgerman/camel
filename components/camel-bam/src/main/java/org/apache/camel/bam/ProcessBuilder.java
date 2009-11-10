begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Endpoint
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
name|ActivityDefinition
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
name|processor
operator|.
name|ActivityMonitorEngine
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
name|processor
operator|.
name|JpaBamProcessor
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
name|ProcessRules
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
name|RouteBuilder
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
name|impl
operator|.
name|DefaultCamelContext
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
name|CastUtils
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
name|notNull
import|;
end_import

begin_comment
comment|/**  * A builder of a process definition  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|ProcessBuilder
specifier|public
specifier|abstract
class|class
name|ProcessBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|processCounter
specifier|private
specifier|static
name|int
name|processCounter
decl_stmt|;
DECL|field|jpaTemplate
specifier|private
name|JpaTemplate
name|jpaTemplate
decl_stmt|;
DECL|field|transactionTemplate
specifier|private
name|TransactionTemplate
name|transactionTemplate
decl_stmt|;
DECL|field|processName
specifier|private
name|String
name|processName
decl_stmt|;
DECL|field|activityBuilders
specifier|private
name|List
argument_list|<
name|ActivityBuilder
argument_list|>
name|activityBuilders
init|=
operator|new
name|ArrayList
argument_list|<
name|ActivityBuilder
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|entityType
specifier|private
name|Class
argument_list|<
name|ProcessInstance
argument_list|>
name|entityType
init|=
name|ProcessInstance
operator|.
name|class
decl_stmt|;
DECL|field|processRules
specifier|private
name|ProcessRules
name|processRules
init|=
operator|new
name|ProcessRules
argument_list|()
decl_stmt|;
DECL|field|processDefinition
specifier|private
name|ProcessDefinition
name|processDefinition
decl_stmt|;
DECL|field|engine
specifier|private
name|ActivityMonitorEngine
name|engine
decl_stmt|;
DECL|method|ProcessBuilder ()
specifier|protected
name|ProcessBuilder
parameter_list|()
block|{     }
DECL|method|ProcessBuilder (JpaTemplate jpaTemplate, TransactionTemplate transactionTemplate)
specifier|protected
name|ProcessBuilder
parameter_list|(
name|JpaTemplate
name|jpaTemplate
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|)
block|{
name|this
argument_list|(
name|jpaTemplate
argument_list|,
name|transactionTemplate
argument_list|,
name|createProcessName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ProcessBuilder (JpaTemplate jpaTemplate, TransactionTemplate transactionTemplate, String processName)
specifier|protected
name|ProcessBuilder
parameter_list|(
name|JpaTemplate
name|jpaTemplate
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|,
name|String
name|processName
parameter_list|)
block|{
name|this
operator|.
name|jpaTemplate
operator|=
name|jpaTemplate
expr_stmt|;
name|this
operator|.
name|transactionTemplate
operator|=
name|transactionTemplate
expr_stmt|;
name|this
operator|.
name|processName
operator|=
name|processName
expr_stmt|;
block|}
DECL|method|createProcessName ()
specifier|protected
specifier|static
specifier|synchronized
name|String
name|createProcessName
parameter_list|()
block|{
return|return
literal|"Process-"
operator|+
operator|(
operator|++
name|processCounter
operator|)
return|;
block|}
DECL|method|activity (String endpointUri)
specifier|public
name|ActivityBuilder
name|activity
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
return|return
name|activity
argument_list|(
name|endpoint
argument_list|(
name|endpointUri
argument_list|)
argument_list|)
return|;
block|}
DECL|method|activity (Endpoint endpoint)
specifier|public
name|ActivityBuilder
name|activity
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|ActivityBuilder
name|answer
init|=
operator|new
name|ActivityBuilder
argument_list|(
name|this
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|activityBuilders
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Sets the process entity type used to perform state management      */
DECL|method|entityType (Class<ProcessInstance> entityType)
specifier|public
name|ProcessBuilder
name|entityType
parameter_list|(
name|Class
argument_list|<
name|ProcessInstance
argument_list|>
name|entityType
parameter_list|)
block|{
name|this
operator|.
name|entityType
operator|=
name|entityType
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|createActivityProcessor (ActivityBuilder activityBuilder)
specifier|public
name|Processor
name|createActivityProcessor
parameter_list|(
name|ActivityBuilder
name|activityBuilder
parameter_list|)
block|{
name|notNull
argument_list|(
name|jpaTemplate
argument_list|,
literal|"jpaTemplate"
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
name|processRules
operator|.
name|setProcessDefinition
argument_list|(
name|getProcessDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
operator|new
name|JpaBamProcessor
argument_list|(
name|getTransactionTemplate
argument_list|()
argument_list|,
name|getJpaTemplate
argument_list|()
argument_list|,
name|activityBuilder
operator|.
name|getCorrelationExpression
argument_list|()
argument_list|,
name|activityBuilder
operator|.
name|getActivityRules
argument_list|()
argument_list|,
name|getEntityType
argument_list|()
argument_list|)
return|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getActivityBuilders ()
specifier|public
name|List
argument_list|<
name|ActivityBuilder
argument_list|>
name|getActivityBuilders
parameter_list|()
block|{
return|return
name|activityBuilders
return|;
block|}
DECL|method|getEntityType ()
specifier|public
name|Class
argument_list|<
name|ProcessInstance
argument_list|>
name|getEntityType
parameter_list|()
block|{
return|return
name|entityType
return|;
block|}
DECL|method|getJpaTemplate ()
specifier|public
name|JpaTemplate
name|getJpaTemplate
parameter_list|()
block|{
return|return
name|jpaTemplate
return|;
block|}
DECL|method|setJpaTemplate (JpaTemplate jpaTemplate)
specifier|public
name|void
name|setJpaTemplate
parameter_list|(
name|JpaTemplate
name|jpaTemplate
parameter_list|)
block|{
name|this
operator|.
name|jpaTemplate
operator|=
name|jpaTemplate
expr_stmt|;
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
DECL|method|getProcessRules ()
specifier|public
name|ProcessRules
name|getProcessRules
parameter_list|()
block|{
return|return
name|processRules
return|;
block|}
DECL|method|getProcessName ()
specifier|public
name|String
name|getProcessName
parameter_list|()
block|{
return|return
name|processName
return|;
block|}
DECL|method|getProcessDefinition ()
specifier|public
name|ProcessDefinition
name|getProcessDefinition
parameter_list|()
block|{
if|if
condition|(
name|processDefinition
operator|==
literal|null
condition|)
block|{
name|processDefinition
operator|=
name|findOrCreateProcessDefinition
argument_list|()
expr_stmt|;
block|}
return|return
name|processDefinition
return|;
block|}
DECL|method|setProcessDefinition (ProcessDefinition processDefinition)
specifier|public
name|void
name|setProcessDefinition
parameter_list|(
name|ProcessDefinition
name|processDefinition
parameter_list|)
block|{
name|this
operator|.
name|processDefinition
operator|=
name|processDefinition
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|populateRoutes ()
specifier|protected
name|void
name|populateRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getJpaTemplate
argument_list|()
argument_list|,
literal|"jpaTemplate"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getTransactionTemplate
argument_list|()
argument_list|,
literal|"transactionTemplate"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// lets add the monitoring service - should there be an easier way??
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
name|engine
operator|=
operator|new
name|ActivityMonitorEngine
argument_list|(
name|getJpaTemplate
argument_list|()
argument_list|,
name|getTransactionTemplate
argument_list|()
argument_list|,
name|getProcessRules
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|CamelContext
name|camelContext
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
name|DefaultCamelContext
name|defaultCamelContext
init|=
operator|(
name|DefaultCamelContext
operator|)
name|camelContext
decl_stmt|;
name|defaultCamelContext
operator|.
name|addService
argument_list|(
name|engine
argument_list|)
expr_stmt|;
block|}
comment|// lets create the routes for the activites
for|for
control|(
name|ActivityBuilder
name|builder
range|:
name|activityBuilders
control|)
block|{
name|from
argument_list|(
name|builder
operator|.
name|getEndpoint
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
name|builder
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|populateRoutes
argument_list|()
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|findOrCreateActivityDefinition (String activityName)
specifier|public
name|ActivityDefinition
name|findOrCreateActivityDefinition
parameter_list|(
name|String
name|activityName
parameter_list|)
block|{
name|ProcessDefinition
name|definition
init|=
name|getProcessDefinition
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ActivityDefinition
argument_list|>
name|list
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|jpaTemplate
operator|.
name|find
argument_list|(
literal|"select x from "
operator|+
name|ActivityDefinition
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x where x.processDefinition = ?1 and x.name = ?2"
argument_list|,
name|definition
argument_list|,
name|activityName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
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
else|else
block|{
name|ActivityDefinition
name|answer
init|=
operator|new
name|ActivityDefinition
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setName
argument_list|(
name|activityName
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setProcessDefinition
argument_list|(
name|ProcessDefinition
operator|.
name|getRefreshedProcessDefinition
argument_list|(
name|jpaTemplate
argument_list|,
name|definition
argument_list|)
argument_list|)
expr_stmt|;
name|jpaTemplate
operator|.
name|persist
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
DECL|method|findOrCreateProcessDefinition ()
specifier|protected
name|ProcessDefinition
name|findOrCreateProcessDefinition
parameter_list|()
block|{
name|List
argument_list|<
name|ProcessDefinition
argument_list|>
name|list
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|jpaTemplate
operator|.
name|find
argument_list|(
literal|"select x from "
operator|+
name|ProcessDefinition
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x where x.name = ?1"
argument_list|,
name|processName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
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
else|else
block|{
name|ProcessDefinition
name|answer
init|=
operator|new
name|ProcessDefinition
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setName
argument_list|(
name|processName
argument_list|)
expr_stmt|;
name|jpaTemplate
operator|.
name|persist
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
block|}
end_class

end_unit

