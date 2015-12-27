begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.timer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|timer
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
name|java
operator|.
name|util
operator|.
name|Timer
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
name|Component
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
name|Consumer
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
name|MultipleConsumersSupport
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
name|Producer
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|ManagedResource
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
name|impl
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The timer component is used for generating message exchanges when a timer fires.  *  * This component is similar to the scheduler component, but has much less functionality.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed TimerEndpoint"
argument_list|)
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"timer"
argument_list|,
name|title
operator|=
literal|"Timer"
argument_list|,
name|syntax
operator|=
literal|"timer:timerName"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|consumerClass
operator|=
name|TimerConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"core,scheduling"
argument_list|)
DECL|class|TimerEndpoint
specifier|public
class|class
name|TimerEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|MultipleConsumersSupport
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|timerName
specifier|private
name|String
name|timerName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|period
specifier|private
name|long
name|period
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|delay
specifier|private
name|long
name|delay
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|repeatCount
specifier|private
name|long
name|repeatCount
decl_stmt|;
annotation|@
name|UriParam
DECL|field|fixedRate
specifier|private
name|boolean
name|fixedRate
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|daemon
specifier|private
name|boolean
name|daemon
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|time
specifier|private
name|Date
name|time
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|timer
specifier|private
name|Timer
name|timer
decl_stmt|;
DECL|method|TimerEndpoint ()
specifier|public
name|TimerEndpoint
parameter_list|()
block|{     }
DECL|method|TimerEndpoint (String uri, Component component, String timerName)
specifier|public
name|TimerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|timerName
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|timerName
operator|=
name|timerName
expr_stmt|;
block|}
DECL|method|TimerEndpoint (String endpointUri, Component component)
specifier|protected
name|TimerEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|TimerComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|TimerComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot produce to a TimerEndpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
name|answer
init|=
operator|new
name|TimerConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// do nothing, the timer will be set when the first consumer will request it
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|setTimer
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer Name"
argument_list|)
DECL|method|getTimerName ()
specifier|public
name|String
name|getTimerName
parameter_list|()
block|{
if|if
condition|(
name|timerName
operator|==
literal|null
condition|)
block|{
name|timerName
operator|=
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
return|return
name|timerName
return|;
block|}
comment|/**      * The name of the timer      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer Name"
argument_list|)
DECL|method|setTimerName (String timerName)
specifier|public
name|void
name|setTimerName
parameter_list|(
name|String
name|timerName
parameter_list|)
block|{
name|this
operator|.
name|timerName
operator|=
name|timerName
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer Daemon"
argument_list|)
DECL|method|isDaemon ()
specifier|public
name|boolean
name|isDaemon
parameter_list|()
block|{
return|return
name|daemon
return|;
block|}
comment|/**      * Specifies whether or not the thread associated with the timer endpoint runs as a daemon.      *<p/>      * The default value is true.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer Daemon"
argument_list|)
DECL|method|setDaemon (boolean daemon)
specifier|public
name|void
name|setDaemon
parameter_list|(
name|boolean
name|daemon
parameter_list|)
block|{
name|this
operator|.
name|daemon
operator|=
name|daemon
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer Delay"
argument_list|)
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
comment|/**      * The number of milliseconds to wait before the first event is generated. Should not be used in conjunction with the time option.      *<p/>      * The default value is 1000.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer Delay"
argument_list|)
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer FixedRate"
argument_list|)
DECL|method|isFixedRate ()
specifier|public
name|boolean
name|isFixedRate
parameter_list|()
block|{
return|return
name|fixedRate
return|;
block|}
comment|/**      * Events take place at approximately regular intervals, separated by the specified period.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer FixedRate"
argument_list|)
DECL|method|setFixedRate (boolean fixedRate)
specifier|public
name|void
name|setFixedRate
parameter_list|(
name|boolean
name|fixedRate
parameter_list|)
block|{
name|this
operator|.
name|fixedRate
operator|=
name|fixedRate
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer Period"
argument_list|)
DECL|method|getPeriod ()
specifier|public
name|long
name|getPeriod
parameter_list|()
block|{
return|return
name|period
return|;
block|}
comment|/**      * If greater than 0, generate periodic events every period milliseconds.      *<p/>      * The default value is 1000.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timer Period"
argument_list|)
DECL|method|setPeriod (long period)
specifier|public
name|void
name|setPeriod
parameter_list|(
name|long
name|period
parameter_list|)
block|{
name|this
operator|.
name|period
operator|=
name|period
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Repeat Count"
argument_list|)
DECL|method|getRepeatCount ()
specifier|public
name|long
name|getRepeatCount
parameter_list|()
block|{
return|return
name|repeatCount
return|;
block|}
comment|/**      * Specifies a maximum limit of number of fires.      * So if you set it to 1, the timer will only fire once.      * If you set it to 5, it will only fire five times.      * A value of zero or negative means fire forever.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Repeat Count"
argument_list|)
DECL|method|setRepeatCount (long repeatCount)
specifier|public
name|void
name|setRepeatCount
parameter_list|(
name|long
name|repeatCount
parameter_list|)
block|{
name|this
operator|.
name|repeatCount
operator|=
name|repeatCount
expr_stmt|;
block|}
DECL|method|getTime ()
specifier|public
name|Date
name|getTime
parameter_list|()
block|{
return|return
name|time
return|;
block|}
comment|/**      * A java.util.Date the first event should be generated. If using the URI, the pattern expected is: yyyy-MM-dd HH:mm:ss or yyyy-MM-dd'T'HH:mm:ss.      */
DECL|method|setTime (Date time)
specifier|public
name|void
name|setTime
parameter_list|(
name|Date
name|time
parameter_list|)
block|{
name|this
operator|.
name|time
operator|=
name|time
expr_stmt|;
block|}
DECL|method|getTimer (TimerConsumer consumer)
specifier|public
name|Timer
name|getTimer
parameter_list|(
name|TimerConsumer
name|consumer
parameter_list|)
block|{
if|if
condition|(
name|timer
operator|!=
literal|null
condition|)
block|{
comment|// use custom timer
return|return
name|timer
return|;
block|}
return|return
name|getComponent
argument_list|()
operator|.
name|getTimer
argument_list|(
name|consumer
argument_list|)
return|;
block|}
comment|/**      * To use a custom {@link Timer}      */
DECL|method|setTimer (Timer timer)
specifier|public
name|void
name|setTimer
parameter_list|(
name|Timer
name|timer
parameter_list|)
block|{
name|this
operator|.
name|timer
operator|=
name|timer
expr_stmt|;
block|}
DECL|method|removeTimer (TimerConsumer consumer)
specifier|public
name|void
name|removeTimer
parameter_list|(
name|TimerConsumer
name|consumer
parameter_list|)
block|{
if|if
condition|(
name|timer
operator|==
literal|null
condition|)
block|{
comment|// only remove timer if we are not using a custom timer
name|getComponent
argument_list|()
operator|.
name|removeTimer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

