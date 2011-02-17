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
name|Date
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
name|camel
operator|.
name|bam
operator|.
name|rules
operator|.
name|TemporalRule
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
name|equal
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|TimeExpression
specifier|public
specifier|abstract
class|class
name|TimeExpression
block|{
DECL|field|activityRules
specifier|private
name|ActivityRules
name|activityRules
decl_stmt|;
DECL|field|builder
specifier|private
name|ActivityBuilder
name|builder
decl_stmt|;
DECL|field|lifecycle
specifier|private
name|ActivityLifecycle
name|lifecycle
decl_stmt|;
DECL|method|TimeExpression (ActivityBuilder builder, ActivityLifecycle lifecycle)
specifier|public
name|TimeExpression
parameter_list|(
name|ActivityBuilder
name|builder
parameter_list|,
name|ActivityLifecycle
name|lifecycle
parameter_list|)
block|{
name|this
operator|.
name|lifecycle
operator|=
name|lifecycle
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|builder
expr_stmt|;
name|this
operator|.
name|activityRules
operator|=
name|builder
operator|.
name|getActivityRules
argument_list|()
expr_stmt|;
block|}
DECL|method|isActivityLifecycle (ActivityRules activityRules, ActivityLifecycle lifecycle)
specifier|public
name|boolean
name|isActivityLifecycle
parameter_list|(
name|ActivityRules
name|activityRules
parameter_list|,
name|ActivityLifecycle
name|lifecycle
parameter_list|)
block|{
return|return
name|equal
argument_list|(
name|activityRules
argument_list|,
name|this
operator|.
name|activityRules
argument_list|)
operator|&&
name|equal
argument_list|(
name|lifecycle
argument_list|,
name|this
operator|.
name|lifecycle
argument_list|)
return|;
block|}
comment|/**      * Creates a new temporal rule on this expression and the other expression      */
DECL|method|after (TimeExpression expression)
specifier|public
name|TemporalRule
name|after
parameter_list|(
name|TimeExpression
name|expression
parameter_list|)
block|{
name|TemporalRule
name|rule
init|=
operator|new
name|TemporalRule
argument_list|(
name|expression
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|rule
operator|.
name|getSecond
argument_list|()
operator|.
name|getActivityRules
argument_list|()
operator|.
name|addRule
argument_list|(
name|rule
argument_list|)
expr_stmt|;
return|return
name|rule
return|;
block|}
DECL|method|evaluate (ProcessInstance processInstance)
specifier|public
name|Date
name|evaluate
parameter_list|(
name|ProcessInstance
name|processInstance
parameter_list|)
block|{
name|ActivityState
name|state
init|=
name|processInstance
operator|.
name|getActivityState
argument_list|(
name|activityRules
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
return|return
name|evaluate
argument_list|(
name|processInstance
argument_list|,
name|state
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|evaluate (ProcessInstance instance, ActivityState state)
specifier|public
specifier|abstract
name|Date
name|evaluate
parameter_list|(
name|ProcessInstance
name|instance
parameter_list|,
name|ActivityState
name|state
parameter_list|)
function_decl|;
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getBuilder ()
specifier|public
name|ActivityBuilder
name|getBuilder
parameter_list|()
block|{
return|return
name|builder
return|;
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
DECL|method|getLifecycle ()
specifier|public
name|ActivityLifecycle
name|getLifecycle
parameter_list|()
block|{
return|return
name|lifecycle
return|;
block|}
DECL|method|getActivityState (ProcessInstance instance)
specifier|public
name|ActivityState
name|getActivityState
parameter_list|(
name|ProcessInstance
name|instance
parameter_list|)
block|{
return|return
name|instance
operator|.
name|getActivityState
argument_list|(
name|activityRules
argument_list|)
return|;
block|}
DECL|method|getOrCreateActivityState (ProcessInstance instance)
specifier|public
name|ActivityState
name|getOrCreateActivityState
parameter_list|(
name|ProcessInstance
name|instance
parameter_list|)
block|{
return|return
name|instance
operator|.
name|getOrCreateActivityState
argument_list|(
name|activityRules
argument_list|)
return|;
block|}
block|}
end_class

end_unit

