begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|api
operator|.
name|management
operator|.
name|PerformanceCounter
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
name|management
operator|.
name|mbean
operator|.
name|ManagedPerformanceCounter
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
name|DelegateAsyncProcessor
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
name|StopWatch
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

begin_comment
comment|/**  * JMX enabled processor that uses the {@link org.apache.camel.management.mbean.ManagedCounter} for instrumenting  * processing of exchanges.  *  * @version   */
end_comment

begin_class
DECL|class|InstrumentationProcessor
specifier|public
class|class
name|InstrumentationProcessor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|InstrumentationProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|counter
specifier|private
name|PerformanceCounter
name|counter
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|method|InstrumentationProcessor ()
specifier|public
name|InstrumentationProcessor
parameter_list|()
block|{     }
DECL|method|InstrumentationProcessor (PerformanceCounter counter)
specifier|public
name|InstrumentationProcessor
parameter_list|(
name|PerformanceCounter
name|counter
parameter_list|)
block|{
name|this
operator|.
name|counter
operator|=
name|counter
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
literal|"Instrumentation"
operator|+
operator|(
name|type
operator|!=
literal|null
condition|?
literal|":"
operator|+
name|type
else|:
literal|""
operator|)
operator|+
literal|"["
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
DECL|method|setCounter (Object counter)
specifier|public
name|void
name|setCounter
parameter_list|(
name|Object
name|counter
parameter_list|)
block|{
name|ManagedPerformanceCounter
name|mpc
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|counter
operator|instanceof
name|ManagedPerformanceCounter
condition|)
block|{
name|mpc
operator|=
operator|(
name|ManagedPerformanceCounter
operator|)
name|counter
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|counter
operator|instanceof
name|DelegatePerformanceCounter
condition|)
block|{
operator|(
operator|(
name|DelegatePerformanceCounter
operator|)
name|this
operator|.
name|counter
operator|)
operator|.
name|setCounter
argument_list|(
name|mpc
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mpc
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|counter
operator|=
name|mpc
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|counter
operator|instanceof
name|PerformanceCounter
condition|)
block|{
name|this
operator|.
name|counter
operator|=
operator|(
name|PerformanceCounter
operator|)
name|counter
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// only record time if stats is enabled
specifier|final
name|StopWatch
name|watch
init|=
operator|(
name|counter
operator|!=
literal|null
operator|&&
name|counter
operator|.
name|isStatisticsEnabled
argument_list|()
operator|)
condition|?
operator|new
name|StopWatch
argument_list|()
else|:
literal|null
decl_stmt|;
comment|// mark beginning to process the exchange
if|if
condition|(
name|watch
operator|!=
literal|null
condition|)
block|{
name|beginTime
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
comment|// record end time
if|if
condition|(
name|watch
operator|!=
literal|null
condition|)
block|{
name|recordTime
argument_list|(
name|exchange
argument_list|,
name|watch
operator|.
name|stop
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// and let the original callback know we are done as well
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|InstrumentationProcessor
operator|.
name|this
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|beginTime (Exchange exchange)
specifier|protected
name|void
name|beginTime
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|counter
operator|.
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|recordTime (Exchange exchange, long duration)
specifier|protected
name|void
name|recordTime
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|duration
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"{}Recording duration: {} millis for exchange: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|type
operator|!=
literal|null
condition|?
name|type
operator|+
literal|": "
else|:
literal|""
block|,
name|duration
block|,
name|exchange
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|exchange
operator|.
name|isFailed
argument_list|()
operator|&&
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|counter
operator|.
name|completedExchange
argument_list|(
name|exchange
argument_list|,
name|duration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|counter
operator|.
name|failedExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
block|}
end_class

end_unit

