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
name|builder
operator|.
name|RouteBuilder
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

begin_comment
comment|/**  * A builder of a process definition  *  * @version $Revision: $  */
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
name|entityType
init|=
name|Process
operator|.
name|class
decl_stmt|;
DECL|field|jpaTemplate
specifier|private
name|JpaTemplate
name|jpaTemplate
decl_stmt|;
DECL|field|process
specifier|private
name|ProcessDefinition
name|process
init|=
operator|new
name|ProcessDefinition
argument_list|()
decl_stmt|;
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
DECL|method|entityType (Class entityType)
specifier|public
name|ProcessBuilder
name|entityType
parameter_list|(
name|Class
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
return|return
operator|new
name|JpaBamProcessor
argument_list|(
name|getEntityType
argument_list|()
argument_list|,
name|activityBuilder
operator|.
name|getCorrelationExpression
argument_list|()
argument_list|,
name|activityBuilder
operator|.
name|getActivity
argument_list|()
argument_list|,
name|getJpaTemplate
argument_list|()
argument_list|)
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
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
DECL|method|getProcess ()
specifier|public
name|ProcessDefinition
name|getProcess
parameter_list|()
block|{
return|return
name|process
return|;
block|}
block|}
end_class

end_unit

