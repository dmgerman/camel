begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.swf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|swf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CancellationException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DataConverter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DataConverterException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DecisionContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DecisionContextProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DecisionContextProviderImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|WorkflowClock
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|WorkflowException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|common
operator|.
name|WorkflowExecutionUtils
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|core
operator|.
name|Promise
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|core
operator|.
name|Settable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|core
operator|.
name|TryCatchFinally
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|generic
operator|.
name|WorkflowDefinition
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

begin_class
DECL|class|CamelWorkflowDefinition
specifier|public
class|class
name|CamelWorkflowDefinition
extends|extends
name|WorkflowDefinition
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelWorkflowDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|swfWorkflowConsumer
specifier|private
name|SWFWorkflowConsumer
name|swfWorkflowConsumer
decl_stmt|;
DECL|field|decisionContext
specifier|private
name|DecisionContext
name|decisionContext
decl_stmt|;
DECL|field|dataConverter
specifier|private
name|DataConverter
name|dataConverter
decl_stmt|;
DECL|field|contextProvider
specifier|private
specifier|final
name|DecisionContextProvider
name|contextProvider
init|=
operator|new
name|DecisionContextProviderImpl
argument_list|()
decl_stmt|;
DECL|field|workflowClock
specifier|private
specifier|final
name|WorkflowClock
name|workflowClock
init|=
name|contextProvider
operator|.
name|getDecisionContext
argument_list|()
operator|.
name|getWorkflowClock
argument_list|()
decl_stmt|;
DECL|method|CamelWorkflowDefinition (SWFWorkflowConsumer swfWorkflowConsumer, DecisionContext decisionContext, DataConverter dataConverter)
specifier|public
name|CamelWorkflowDefinition
parameter_list|(
name|SWFWorkflowConsumer
name|swfWorkflowConsumer
parameter_list|,
name|DecisionContext
name|decisionContext
parameter_list|,
name|DataConverter
name|dataConverter
parameter_list|)
block|{
name|this
operator|.
name|swfWorkflowConsumer
operator|=
name|swfWorkflowConsumer
expr_stmt|;
name|this
operator|.
name|decisionContext
operator|=
name|decisionContext
expr_stmt|;
name|this
operator|.
name|dataConverter
operator|=
name|dataConverter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (final String input)
specifier|public
name|Promise
argument_list|<
name|String
argument_list|>
name|execute
parameter_list|(
specifier|final
name|String
name|input
parameter_list|)
throws|throws
name|WorkflowException
block|{
specifier|final
name|Settable
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|Settable
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|Promise
argument_list|<
name|?
argument_list|>
argument_list|>
name|methodResult
init|=
operator|new
name|AtomicReference
argument_list|<
name|Promise
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
operator|new
name|TryCatchFinally
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doTry
parameter_list|()
throws|throws
name|Throwable
block|{
name|Object
index|[]
name|parameters
init|=
name|dataConverter
operator|.
name|fromData
argument_list|(
name|input
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|long
name|startTime
init|=
name|workflowClock
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|boolean
name|replaying
init|=
name|contextProvider
operator|.
name|getDecisionContext
argument_list|()
operator|.
name|getWorkflowClock
argument_list|()
operator|.
name|isReplaying
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Processing workflow execute"
argument_list|)
expr_stmt|;
name|Object
name|r
init|=
name|swfWorkflowConsumer
operator|.
name|processWorkflow
argument_list|(
name|parameters
argument_list|,
name|startTime
argument_list|,
name|replaying
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|instanceof
name|Promise
condition|)
block|{
name|methodResult
operator|.
name|set
argument_list|(
operator|(
name|Promise
argument_list|<
name|?
argument_list|>
operator|)
name|r
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|methodResult
operator|.
name|set
argument_list|(
operator|new
name|Settable
argument_list|<
name|Object
argument_list|>
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doCatch
parameter_list|(
name|Throwable
name|e
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|instanceof
name|CancellationException
operator|)
operator|||
operator|!
name|decisionContext
operator|.
name|getWorkflowContext
argument_list|()
operator|.
name|isCancelRequested
argument_list|()
condition|)
block|{
name|throwWorkflowException
argument_list|(
name|dataConverter
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doFinally
parameter_list|()
throws|throws
name|Throwable
block|{
name|Promise
argument_list|<
name|?
argument_list|>
name|r
init|=
name|methodResult
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
operator|||
name|r
operator|.
name|isReady
argument_list|()
condition|)
block|{
name|Object
name|workflowResult
init|=
name|r
operator|==
literal|null
condition|?
literal|null
else|:
name|r
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|convertedResult
init|=
name|dataConverter
operator|.
name|toData
argument_list|(
name|workflowResult
argument_list|)
decl_stmt|;
name|result
operator|.
name|set
argument_list|(
name|convertedResult
argument_list|)
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|signalRecieved (String signalName, String input)
specifier|public
name|void
name|signalRecieved
parameter_list|(
name|String
name|signalName
parameter_list|,
name|String
name|input
parameter_list|)
throws|throws
name|WorkflowException
block|{
name|Object
index|[]
name|parameters
init|=
name|dataConverter
operator|.
name|fromData
argument_list|(
name|input
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
try|try
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Processing workflow signalRecieved"
argument_list|)
expr_stmt|;
name|swfWorkflowConsumer
operator|.
name|signalRecieved
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|throwWorkflowException
argument_list|(
name|dataConverter
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unreacheable"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getWorkflowState ()
specifier|public
name|String
name|getWorkflowState
parameter_list|()
throws|throws
name|WorkflowException
block|{
try|try
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Processing workflow getWorkflowState"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|swfWorkflowConsumer
operator|.
name|getWorkflowState
argument_list|(
literal|null
argument_list|)
decl_stmt|;
return|return
name|dataConverter
operator|.
name|toData
argument_list|(
name|result
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|throwWorkflowException
argument_list|(
name|dataConverter
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unreachable"
argument_list|)
throw|;
block|}
block|}
DECL|method|throwWorkflowException (DataConverter c, Throwable exception)
specifier|private
name|void
name|throwWorkflowException
parameter_list|(
name|DataConverter
name|c
parameter_list|,
name|Throwable
name|exception
parameter_list|)
throws|throws
name|WorkflowException
block|{
if|if
condition|(
name|exception
operator|instanceof
name|WorkflowException
condition|)
block|{
throw|throw
operator|(
name|WorkflowException
operator|)
name|exception
throw|;
block|}
name|String
name|reason
init|=
name|WorkflowExecutionUtils
operator|.
name|truncateReason
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|details
init|=
literal|null
decl_stmt|;
try|try
block|{
name|details
operator|=
name|c
operator|.
name|toData
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DataConverterException
name|dataConverterException
parameter_list|)
block|{
if|if
condition|(
name|dataConverterException
operator|.
name|getCause
argument_list|()
operator|==
literal|null
condition|)
block|{
name|dataConverterException
operator|.
name|initCause
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
throw|throw
name|dataConverterException
throw|;
block|}
throw|throw
operator|new
name|WorkflowException
argument_list|(
name|reason
argument_list|,
name|details
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

