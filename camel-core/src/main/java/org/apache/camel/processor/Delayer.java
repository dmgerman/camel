begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|ScheduledExecutorService
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
name|Traceable
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/delayer.html">Delayer</a> which  * delays processing the exchange until the correct amount of time has elapsed  * using an expression to determine the delivery time.  *<p/>  * This implementation will block while waiting.  *  * @version   */
end_comment

begin_class
DECL|class|Delayer
specifier|public
class|class
name|Delayer
extends|extends
name|DelayProcessorSupport
implements|implements
name|Traceable
block|{
DECL|field|delay
specifier|private
name|Expression
name|delay
decl_stmt|;
DECL|field|delayValue
specifier|private
name|long
name|delayValue
decl_stmt|;
DECL|method|Delayer (CamelContext camelContext, Processor processor, Expression delay, ScheduledExecutorService executorService, boolean shutdownExecutorService)
specifier|public
name|Delayer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Expression
name|delay
parameter_list|,
name|ScheduledExecutorService
name|executorService
parameter_list|,
name|boolean
name|shutdownExecutorService
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|executorService
argument_list|,
name|shutdownExecutorService
argument_list|)
expr_stmt|;
name|this
operator|.
name|delay
operator|=
name|delay
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
literal|"Delayer["
operator|+
name|delay
operator|+
literal|" to: "
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"delay["
operator|+
name|delay
operator|+
literal|"]"
return|;
block|}
DECL|method|getDelay ()
specifier|public
name|Expression
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|getDelayValue ()
specifier|public
name|long
name|getDelayValue
parameter_list|()
block|{
return|return
name|delayValue
return|;
block|}
DECL|method|setDelay (Expression delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|Expression
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|calculateDelay (Exchange exchange)
specifier|protected
name|long
name|calculateDelay
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|long
name|time
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|delay
operator|!=
literal|null
condition|)
block|{
name|Long
name|longValue
init|=
name|delay
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|longValue
operator|!=
literal|null
condition|)
block|{
name|delayValue
operator|=
name|longValue
expr_stmt|;
name|time
operator|=
name|longValue
expr_stmt|;
block|}
else|else
block|{
name|delayValue
operator|=
literal|0
expr_stmt|;
block|}
block|}
if|if
condition|(
name|time
operator|<=
literal|0
condition|)
block|{
comment|// no delay
return|return
literal|0
return|;
block|}
return|return
name|time
return|;
block|}
block|}
end_class

end_unit

