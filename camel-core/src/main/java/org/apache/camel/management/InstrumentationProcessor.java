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
name|AsyncProcessor
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
name|util
operator|.
name|AsyncProcessorHelper
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

begin_comment
comment|/**  * JMX enabled processor that uses the {@link Counter} for instrumenting  * processing of exchanges.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|InstrumentationProcessor
specifier|public
class|class
name|InstrumentationProcessor
extends|extends
name|DelegateProcessor
implements|implements
name|AsyncProcessor
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
DECL|method|InstrumentationProcessor ()
specifier|public
name|InstrumentationProcessor
parameter_list|()
block|{     }
DECL|method|setCounter (PerformanceCounter counter)
specifier|public
name|void
name|setCounter
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
specifier|final
name|long
name|startTime
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|processor
operator|instanceof
name|AsyncProcessor
condition|)
block|{
return|return
operator|(
operator|(
name|AsyncProcessor
operator|)
name|processor
operator|)
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
name|doneSynchronously
parameter_list|)
block|{
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
comment|// convert nanoseconds to milliseconds
name|recordTime
argument_list|(
name|exchange
argument_list|,
operator|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|startTime
operator|)
operator|/
literal|1000000.0
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSynchronously
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
try|try
block|{
name|processor
operator|.
name|process
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
comment|// convert nanoseconds to milliseconds
name|recordTime
argument_list|(
name|exchange
argument_list|,
operator|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|startTime
operator|)
operator|/
literal|1000000.0
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|recordTime (Exchange exchange, double duration)
specifier|protected
name|void
name|recordTime
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|double
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
literal|"Recording duration: "
operator|+
name|duration
operator|+
literal|" millis for exchange: "
operator|+
name|exchange
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
name|duration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|counter
operator|.
name|failedExchange
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

