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

begin_comment
comment|/**  * JMX enabled processor that uses the {@link org.apache.camel.management.mbean.ManagedCounter} for instrumenting  * processing of exchanges.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|InstrumentationProcessor
specifier|public
class|class
name|InstrumentationProcessor
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
literal|"Instrumention"
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
DECL|method|setCounter (ManagedPerformanceCounter counter)
specifier|public
name|void
name|setCounter
parameter_list|(
name|ManagedPerformanceCounter
name|counter
parameter_list|)
block|{
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
name|counter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|counter
operator|=
name|counter
expr_stmt|;
block|}
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
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
comment|// use nano time as its more accurate
name|long
name|startTime
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
operator|&&
name|counter
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|startTime
operator|=
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
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
name|startTime
operator|!=
operator|-
literal|1
condition|)
block|{
name|long
name|diff
init|=
operator|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|startTime
operator|)
operator|/
literal|1000000
decl_stmt|;
name|recordTime
argument_list|(
name|exchange
argument_list|,
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
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
operator|(
name|type
operator|!=
literal|null
condition|?
name|type
operator|+
literal|": "
else|:
literal|""
operator|)
operator|+
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

