begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|support
operator|.
name|DefaultProducer
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
name|support
operator|.
name|ExchangeHelper
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
name|URISupport
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
DECL|class|SWFWorkflowProducer
specifier|public
class|class
name|SWFWorkflowProducer
extends|extends
name|DefaultProducer
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
name|SWFWorkflowProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelSWFClient
specifier|private
specifier|final
name|CamelSWFWorkflowClient
name|camelSWFClient
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SWFEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
name|SWFConfiguration
name|configuration
decl_stmt|;
DECL|field|swfWorkflowProducerToString
specifier|private
specifier|transient
name|String
name|swfWorkflowProducerToString
decl_stmt|;
DECL|method|SWFWorkflowProducer (SWFEndpoint endpoint, CamelSWFWorkflowClient camelSWFClient)
specifier|public
name|SWFWorkflowProducer
parameter_list|(
name|SWFEndpoint
name|endpoint
parameter_list|,
name|CamelSWFWorkflowClient
name|camelSWFClient
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|camelSWFClient
operator|=
name|camelSWFClient
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"processing workflow task "
operator|+
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|Operation
name|operation
init|=
name|getOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|CANCEL
case|:
name|camelSWFClient
operator|.
name|requestCancelWorkflowExecution
argument_list|(
name|getWorkflowId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getRunId
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_STATE
case|:
name|Object
name|state
init|=
name|camelSWFClient
operator|.
name|getWorkflowExecutionState
argument_list|(
name|getWorkflowId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getRunId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getResultType
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setResult
argument_list|(
name|exchange
argument_list|,
name|state
argument_list|)
expr_stmt|;
break|break;
case|case
name|DESCRIBE
case|:
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|workflowInfo
init|=
name|camelSWFClient
operator|.
name|describeWorkflowInstance
argument_list|(
name|getWorkflowId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getRunId
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setResult
argument_list|(
name|exchange
argument_list|,
name|workflowInfo
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_HISTORY
case|:
name|Object
name|history
init|=
name|camelSWFClient
operator|.
name|getWorkflowExecutionHistory
argument_list|(
name|getWorkflowId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getRunId
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setResult
argument_list|(
name|exchange
argument_list|,
name|history
argument_list|)
expr_stmt|;
break|break;
case|case
name|START
case|:
name|String
index|[]
name|ids
init|=
name|camelSWFClient
operator|.
name|startWorkflowExecution
argument_list|(
name|getWorkflowId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getRunId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getEventName
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getVersion
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getArguments
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getTags
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|setHeader
argument_list|(
name|exchange
argument_list|,
name|SWFConstants
operator|.
name|WORKFLOW_ID
argument_list|,
name|ids
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|setHeader
argument_list|(
name|exchange
argument_list|,
name|SWFConstants
operator|.
name|RUN_ID
argument_list|,
name|ids
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|SIGNAL
case|:
name|camelSWFClient
operator|.
name|signalWorkflowExecution
argument_list|(
name|getWorkflowId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getRunId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getSignalName
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getArguments
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|TERMINATE
case|:
name|camelSWFClient
operator|.
name|terminateWorkflowExecution
argument_list|(
name|getWorkflowId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getRunId
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getReason
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getDetails
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|getChildPolicy
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|operation
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|throwable
argument_list|)
throw|;
block|}
block|}
DECL|method|getEventName (Exchange exchange)
specifier|private
name|String
name|getEventName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|eventName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|EVENT_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|eventName
operator|!=
literal|null
condition|?
name|eventName
else|:
name|configuration
operator|.
name|getEventName
argument_list|()
return|;
block|}
DECL|method|getVersion (Exchange exchange)
specifier|private
name|String
name|getVersion
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|version
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|VERSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|version
operator|!=
literal|null
condition|?
name|version
else|:
name|configuration
operator|.
name|getVersion
argument_list|()
return|;
block|}
DECL|method|getTags (Exchange exchange)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getTags
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|TAGS
argument_list|,
name|List
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getSignalName (Exchange exchange)
specifier|private
name|String
name|getSignalName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|signalName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|SIGNAL_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|signalName
operator|!=
literal|null
condition|?
name|signalName
else|:
name|configuration
operator|.
name|getSignalName
argument_list|()
return|;
block|}
DECL|method|getChildPolicy (Exchange exchange)
specifier|private
name|String
name|getChildPolicy
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|childPolicy
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|CHILD_POLICY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|childPolicy
operator|!=
literal|null
condition|?
name|childPolicy
else|:
name|configuration
operator|.
name|getChildPolicy
argument_list|()
return|;
block|}
DECL|method|getDetails (Exchange exchange)
specifier|private
name|String
name|getDetails
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|details
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|DETAILS
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|details
operator|!=
literal|null
condition|?
name|details
else|:
name|configuration
operator|.
name|getTerminationDetails
argument_list|()
return|;
block|}
DECL|method|getReason (Exchange exchange)
specifier|private
name|String
name|getReason
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|reason
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|REASON
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|reason
operator|!=
literal|null
condition|?
name|reason
else|:
name|configuration
operator|.
name|getTerminationReason
argument_list|()
return|;
block|}
DECL|method|getWorkflowId (Exchange exchange)
specifier|private
name|String
name|getWorkflowId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|WORKFLOW_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getRunId (Exchange exchange)
specifier|private
name|String
name|getRunId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|RUN_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getResultType (Exchange exchange)
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|String
name|type
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|STATE_RESULT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|configuration
operator|.
name|getStateResultType
argument_list|()
expr_stmt|;
block|}
return|return
name|type
operator|!=
literal|null
condition|?
name|Class
operator|.
name|forName
argument_list|(
name|type
argument_list|)
else|:
name|Object
operator|.
name|class
return|;
block|}
DECL|method|getOperation (Exchange exchange)
specifier|private
name|Operation
name|getOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SWFConstants
operator|.
name|OPERATION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
name|operation
operator|=
name|configuration
operator|.
name|getOperation
argument_list|()
expr_stmt|;
block|}
return|return
name|operation
operator|!=
literal|null
condition|?
name|Operation
operator|.
name|valueOf
argument_list|(
name|operation
argument_list|)
else|:
name|Operation
operator|.
name|START
return|;
block|}
DECL|method|setHeader (Exchange exchange, String key, Object value)
specifier|private
name|void
name|setHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getArguments (Exchange exchange)
specifier|private
name|Object
name|getArguments
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
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
if|if
condition|(
name|swfWorkflowProducerToString
operator|==
literal|null
condition|)
block|{
name|swfWorkflowProducerToString
operator|=
literal|"SWFWorkflowProducer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|swfWorkflowProducerToString
return|;
block|}
DECL|enum|Operation
specifier|private
enum|enum
name|Operation
block|{
DECL|enumConstant|SIGNAL
DECL|enumConstant|CANCEL
DECL|enumConstant|TERMINATE
DECL|enumConstant|GET_STATE
DECL|enumConstant|START
DECL|enumConstant|DESCRIBE
DECL|enumConstant|GET_HISTORY
name|SIGNAL
block|,
name|CANCEL
block|,
name|TERMINATE
block|,
name|GET_STATE
block|,
name|START
block|,
name|DESCRIBE
block|,
name|GET_HISTORY
block|;     }
block|}
end_class

end_unit

