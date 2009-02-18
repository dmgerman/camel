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

begin_comment
comment|/**  * A<a href="http://camel.apache.org/delayer.html">Delayer</a> which  * delays processing the exchange until the correct amount of time has elapsed  * using an expression to determine the delivery time.<p/> For example if you  * wish to delay JMS messages by 25 seconds from their publish time you could  * create an instance of this class with the expression  *<code>header("JMSTimestamp")</code> and a delay value of 25000L.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|Delayer
specifier|public
class|class
name|Delayer
extends|extends
name|DelayProcessorSupport
block|{
DECL|field|timeExpression
specifier|private
name|Expression
name|timeExpression
decl_stmt|;
DECL|field|delay
specifier|private
name|long
name|delay
decl_stmt|;
DECL|method|Delayer (Processor processor, Expression timeExpression, long delay)
specifier|public
name|Delayer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Expression
name|timeExpression
parameter_list|,
name|long
name|delay
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeExpression
operator|=
name|timeExpression
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
literal|"Delayer[on: "
operator|+
name|timeExpression
operator|+
literal|" delay: "
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
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
comment|/**      * Sets the delay from the publish time; which is typically the time from      * the expression or the current system time if none is available      */
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
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
comment|/**      * Waits for an optional time period before continuing to process the      * exchange      */
DECL|method|delay (Exchange exchange)
specifier|protected
name|void
name|delay
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|time
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|timeExpression
operator|!=
literal|null
condition|)
block|{
name|Long
name|longValue
init|=
name|timeExpression
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
name|time
operator|=
name|longValue
operator|.
name|longValue
argument_list|()
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
name|time
operator|=
name|defaultProcessTime
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
name|time
operator|+=
name|delay
expr_stmt|;
name|waitUntil
argument_list|(
name|time
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * A Strategy Method to allow derived implementations to decide the current      * system time or some other default exchange property      */
DECL|method|defaultProcessTime (Exchange exchange)
specifier|protected
name|long
name|defaultProcessTime
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|currentSystemTime
argument_list|()
return|;
block|}
block|}
end_class

end_unit

