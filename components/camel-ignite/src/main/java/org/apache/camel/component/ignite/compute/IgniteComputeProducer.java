begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.compute
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|compute
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
name|Collection
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
name|AsyncCallback
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
name|Message
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
name|TypeConverter
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
name|component
operator|.
name|ignite
operator|.
name|IgniteConstants
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
name|DefaultAsyncProducer
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
name|MessageHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|IgniteCompute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|compute
operator|.
name|ComputeTask
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteCallable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteClosure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteFuture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteInClosure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteReducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteRunnable
import|;
end_import

begin_comment
comment|/**  * Ignite Compute producer.  */
end_comment

begin_class
DECL|class|IgniteComputeProducer
specifier|public
class|class
name|IgniteComputeProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|endpoint
specifier|private
name|IgniteComputeEndpoint
name|endpoint
decl_stmt|;
DECL|method|IgniteComputeProducer (IgniteComputeEndpoint endpoint)
specifier|public
name|IgniteComputeProducer
parameter_list|(
name|IgniteComputeEndpoint
name|endpoint
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
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|IgniteCompute
name|compute
init|=
name|endpoint
operator|.
name|createIgniteCompute
argument_list|()
operator|.
name|withAsync
argument_list|()
decl_stmt|;
try|try
block|{
switch|switch
condition|(
name|executionTypeFor
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|CALL
case|:
name|doCall
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|compute
argument_list|)
expr_stmt|;
break|break;
case|case
name|BROADCAST
case|:
name|doBroadcast
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|compute
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXECUTE
case|:
name|doExecute
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|compute
argument_list|)
expr_stmt|;
break|break;
case|case
name|RUN
case|:
name|doRun
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|compute
argument_list|)
expr_stmt|;
break|break;
case|case
name|APPLY
case|:
name|doApply
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|compute
argument_list|)
expr_stmt|;
break|break;
case|case
name|AFFINITY_CALL
case|:
name|doAffinityCall
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|compute
argument_list|)
expr_stmt|;
break|break;
case|case
name|AFFINITY_RUN
case|:
name|doAffinityRun
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|compute
argument_list|)
expr_stmt|;
break|break;
default|default:
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Operation not supported by Ignite Compute producer."
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|compute
operator|.
name|future
argument_list|()
operator|.
name|listen
argument_list|(
name|IgniteInCamelClosure
operator|.
name|create
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|doCall (final Exchange exchange, final AsyncCallback callback, IgniteCompute compute)
specifier|private
name|void
name|doCall
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
name|IgniteCompute
name|compute
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|job
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|IgniteReducer
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|reducer
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_REDUCER
argument_list|,
name|IgniteReducer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|job
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|col
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|job
decl_stmt|;
name|TypeConverter
name|tc
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|IgniteCallable
argument_list|<
name|?
argument_list|>
argument_list|>
name|callables
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|col
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|col
control|)
block|{
name|callables
operator|.
name|add
argument_list|(
name|tc
operator|.
name|mandatoryConvertTo
argument_list|(
name|IgniteCallable
operator|.
name|class
argument_list|,
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reducer
operator|!=
literal|null
condition|)
block|{
name|compute
operator|.
name|call
argument_list|(
operator|(
name|Collection
operator|)
name|callables
argument_list|,
name|reducer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|compute
operator|.
name|call
argument_list|(
operator|(
name|Collection
operator|)
name|callables
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|IgniteCallable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|job
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|compute
operator|.
name|call
argument_list|(
operator|(
name|IgniteCallable
argument_list|<
name|Object
argument_list|>
operator|)
name|job
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignite Compute endpoint with CALL executionType is only "
operator|+
literal|"supported for IgniteCallable payloads, or collections of them. The payload type was: %s."
argument_list|,
name|job
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doBroadcast (final Exchange exchange, final AsyncCallback callback, IgniteCompute compute)
specifier|private
name|void
name|doBroadcast
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
name|IgniteCompute
name|compute
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|job
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|IgniteCallable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|job
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|compute
operator|.
name|broadcast
argument_list|(
operator|(
name|IgniteCallable
argument_list|<
name|?
argument_list|>
operator|)
name|job
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|IgniteRunnable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|job
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|compute
operator|.
name|broadcast
argument_list|(
operator|(
name|IgniteRunnable
operator|)
name|job
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|IgniteClosure
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|job
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|compute
operator|.
name|broadcast
argument_list|(
operator|(
name|IgniteClosure
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|job
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_PARAMS
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignite Compute endpoint with BROADCAST executionType is only "
operator|+
literal|"supported for IgniteCallable, IgniteRunnable or IgniteClosure payloads. The payload type was: %s."
argument_list|,
name|job
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doExecute (final Exchange exchange, final AsyncCallback callback, IgniteCompute compute)
specifier|private
name|void
name|doExecute
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
name|IgniteCompute
name|compute
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|job
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Object
name|params
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_PARAMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|job
operator|instanceof
name|Class
operator|&&
name|ComputeTask
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|job
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|ComputeTask
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|task
init|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|ComputeTask
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
operator|)
name|job
decl_stmt|;
name|compute
operator|.
name|execute
argument_list|(
name|task
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ComputeTask
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|job
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|compute
operator|.
name|execute
argument_list|(
operator|(
name|ComputeTask
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|job
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getTaskName
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|params
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
name|compute
operator|.
name|execute
argument_list|(
name|endpoint
operator|.
name|getTaskName
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignite Compute endpoint with EXECUTE executionType is only "
operator|+
literal|"supported for ComputeTask payloads, Class<ComputeTask> or any payload in conjunction with the "
operator|+
literal|"task name option. The payload type was: %s."
argument_list|,
name|job
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|doRun (final Exchange exchange, final AsyncCallback callback, IgniteCompute compute)
specifier|private
name|void
name|doRun
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
name|IgniteCompute
name|compute
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|job
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|job
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|col
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|job
decl_stmt|;
name|TypeConverter
name|tc
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|IgniteRunnable
argument_list|>
name|runnables
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|col
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|col
control|)
block|{
name|runnables
operator|.
name|add
argument_list|(
name|tc
operator|.
name|mandatoryConvertTo
argument_list|(
name|IgniteRunnable
operator|.
name|class
argument_list|,
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|compute
operator|.
name|run
argument_list|(
name|runnables
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|IgniteRunnable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|job
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|compute
operator|.
name|run
argument_list|(
operator|(
name|IgniteRunnable
operator|)
name|job
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignite Compute endpoint with RUN executionType is only "
operator|+
literal|"supported for IgniteRunnable payloads, or collections of them. The payload type was: %s."
argument_list|,
name|job
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doApply (final Exchange exchange, final AsyncCallback callback, IgniteCompute compute)
specifier|private
parameter_list|<
name|T
parameter_list|,
name|R1
parameter_list|,
name|R2
parameter_list|>
name|void
name|doApply
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
name|IgniteCompute
name|compute
parameter_list|)
throws|throws
name|Exception
block|{
name|IgniteClosure
argument_list|<
name|T
argument_list|,
name|R1
argument_list|>
name|job
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|IgniteClosure
operator|.
name|class
argument_list|)
decl_stmt|;
name|T
name|params
init|=
operator|(
name|T
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_PARAMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|job
operator|==
literal|null
operator|||
name|params
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignite Compute endpoint with APPLY executionType is only "
operator|+
literal|"supported for IgniteClosure payloads with parameters. The payload type was: %s."
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|IgniteReducer
argument_list|<
name|R1
argument_list|,
name|R2
argument_list|>
name|reducer
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_REDUCER
argument_list|,
name|IgniteReducer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|params
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|T
argument_list|>
name|colParams
init|=
operator|(
name|Collection
argument_list|<
name|T
argument_list|>
operator|)
name|params
decl_stmt|;
if|if
condition|(
name|reducer
operator|==
literal|null
condition|)
block|{
name|compute
operator|.
name|apply
argument_list|(
name|job
argument_list|,
name|colParams
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|compute
operator|.
name|apply
argument_list|(
name|job
argument_list|,
name|colParams
argument_list|,
name|reducer
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|compute
operator|.
name|apply
argument_list|(
name|job
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doAffinityCall (final Exchange exchange, final AsyncCallback callback, IgniteCompute compute)
specifier|private
name|void
name|doAffinityCall
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
name|IgniteCompute
name|compute
parameter_list|)
throws|throws
name|Exception
block|{
name|IgniteCallable
argument_list|<
name|Object
argument_list|>
name|job
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|IgniteCallable
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|affinityCache
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_AFFINITY_CACHE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|affinityKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_AFFINITY_KEY
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|job
operator|==
literal|null
operator|||
name|affinityCache
operator|==
literal|null
operator|||
name|affinityKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignite Compute endpoint with AFFINITY_CALL executionType is only "
operator|+
literal|"supported for IgniteCallable payloads, along with an affinity cache and key. The payload type was: %s."
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|compute
operator|.
name|affinityCall
argument_list|(
name|affinityCache
argument_list|,
name|affinityKey
argument_list|,
name|job
argument_list|)
expr_stmt|;
block|}
DECL|method|doAffinityRun (final Exchange exchange, final AsyncCallback callback, IgniteCompute compute)
specifier|private
name|void
name|doAffinityRun
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
name|IgniteCompute
name|compute
parameter_list|)
throws|throws
name|Exception
block|{
name|IgniteRunnable
name|job
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|IgniteRunnable
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|affinityCache
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_AFFINITY_CACHE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|affinityKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_AFFINITY_KEY
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|job
operator|==
literal|null
operator|||
name|affinityCache
operator|==
literal|null
operator|||
name|affinityKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignite Compute endpoint with AFFINITY_RUN executionType is only "
operator|+
literal|"supported for IgniteRunnable payloads, along with an affinity cache and key. The payload type was: %s."
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|compute
operator|.
name|affinityRun
argument_list|(
name|affinityCache
argument_list|,
name|affinityKey
argument_list|,
name|job
argument_list|)
expr_stmt|;
block|}
DECL|method|executionTypeFor (Exchange exchange)
specifier|private
name|IgniteComputeExecutionType
name|executionTypeFor
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
name|IgniteConstants
operator|.
name|IGNITE_COMPUTE_EXECUTION_TYPE
argument_list|,
name|endpoint
operator|.
name|getExecutionType
argument_list|()
argument_list|,
name|IgniteComputeExecutionType
operator|.
name|class
argument_list|)
return|;
block|}
DECL|class|IgniteInCamelClosure
specifier|private
specifier|static
class|class
name|IgniteInCamelClosure
implements|implements
name|IgniteInClosure
argument_list|<
name|IgniteFuture
argument_list|<
name|Object
argument_list|>
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7486030906412223384L
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
name|AsyncCallback
name|callback
decl_stmt|;
DECL|method|create (Exchange exchange, AsyncCallback callback)
specifier|private
specifier|static
name|IgniteInCamelClosure
name|create
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|IgniteInCamelClosure
name|answer
init|=
operator|new
name|IgniteInCamelClosure
argument_list|()
decl_stmt|;
name|answer
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|answer
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|apply (IgniteFuture<Object> future)
specifier|public
name|void
name|apply
parameter_list|(
name|IgniteFuture
argument_list|<
name|Object
argument_list|>
name|future
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|result
operator|=
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

