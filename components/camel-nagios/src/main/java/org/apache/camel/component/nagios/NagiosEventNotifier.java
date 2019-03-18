begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nagios
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
package|;
end_package

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|MessagePayload
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|NagiosPassiveCheckSender
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|NagiosSettings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|PassiveCheckSender
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
name|CamelEvent
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
name|CamelEvent
operator|.
name|CamelContextStartupFailureEvent
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
name|CamelEvent
operator|.
name|CamelContextStopFailureEvent
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
name|CamelEvent
operator|.
name|ExchangeFailedEvent
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
name|CamelEvent
operator|.
name|ExchangeFailureHandledEvent
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
name|CamelEvent
operator|.
name|ExchangeRedeliveryEvent
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
name|CamelEvent
operator|.
name|ServiceStartupFailureEvent
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
name|CamelEvent
operator|.
name|ServiceStopFailureEvent
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
name|EventNotifierSupport
import|;
end_import

begin_comment
comment|/**  * An {@link org.apache.camel.spi.EventNotifier} which sends alters to Nagios.  */
end_comment

begin_class
DECL|class|NagiosEventNotifier
specifier|public
class|class
name|NagiosEventNotifier
extends|extends
name|EventNotifierSupport
block|{
DECL|field|nagiosSettings
specifier|private
name|NagiosSettings
name|nagiosSettings
decl_stmt|;
DECL|field|configuration
specifier|private
name|NagiosConfiguration
name|configuration
decl_stmt|;
DECL|field|sender
specifier|private
name|PassiveCheckSender
name|sender
decl_stmt|;
DECL|field|serviceName
specifier|private
name|String
name|serviceName
init|=
literal|"Camel"
decl_stmt|;
DECL|field|hostName
specifier|private
name|String
name|hostName
init|=
literal|"localhost"
decl_stmt|;
DECL|method|NagiosEventNotifier ()
specifier|public
name|NagiosEventNotifier
parameter_list|()
block|{      }
DECL|method|NagiosEventNotifier (PassiveCheckSender sender)
specifier|public
name|NagiosEventNotifier
parameter_list|(
name|PassiveCheckSender
name|sender
parameter_list|)
block|{
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
DECL|method|notify (CamelEvent eventObject)
specifier|public
name|void
name|notify
parameter_list|(
name|CamelEvent
name|eventObject
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create message payload to send
name|String
name|message
init|=
name|eventObject
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Level
name|level
init|=
name|determineLevel
argument_list|(
name|eventObject
argument_list|)
decl_stmt|;
name|MessagePayload
name|payload
init|=
operator|new
name|MessagePayload
argument_list|(
name|getHostName
argument_list|()
argument_list|,
name|level
argument_list|,
name|getServiceName
argument_list|()
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Sending notification to Nagios: {}"
argument_list|,
name|payload
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sender
operator|.
name|send
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Sending notification done"
argument_list|)
expr_stmt|;
block|}
DECL|method|isEnabled (CamelEvent eventObject)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
name|eventObject
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|determineLevel (CamelEvent eventObject)
specifier|protected
name|Level
name|determineLevel
parameter_list|(
name|CamelEvent
name|eventObject
parameter_list|)
block|{
comment|// failures is considered critical
if|if
condition|(
name|eventObject
operator|instanceof
name|ExchangeFailedEvent
operator|||
name|eventObject
operator|instanceof
name|CamelContextStartupFailureEvent
operator|||
name|eventObject
operator|instanceof
name|CamelContextStopFailureEvent
operator|||
name|eventObject
operator|instanceof
name|ServiceStartupFailureEvent
operator|||
name|eventObject
operator|instanceof
name|ServiceStopFailureEvent
condition|)
block|{
return|return
name|Level
operator|.
name|CRITICAL
return|;
block|}
comment|// the failure was handled so its just a warning
comment|// and warn when a redelivery attempt is done
if|if
condition|(
name|eventObject
operator|instanceof
name|ExchangeFailureHandledEvent
operator|||
name|eventObject
operator|instanceof
name|ExchangeRedeliveryEvent
condition|)
block|{
return|return
name|Level
operator|.
name|WARNING
return|;
block|}
comment|// default to OK
return|return
name|Level
operator|.
name|OK
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|NagiosConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|NagiosConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (NagiosConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NagiosConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getNagiosSettings ()
specifier|public
name|NagiosSettings
name|getNagiosSettings
parameter_list|()
block|{
return|return
name|nagiosSettings
return|;
block|}
DECL|method|setNagiosSettings (NagiosSettings nagiosSettings)
specifier|public
name|void
name|setNagiosSettings
parameter_list|(
name|NagiosSettings
name|nagiosSettings
parameter_list|)
block|{
name|this
operator|.
name|nagiosSettings
operator|=
name|nagiosSettings
expr_stmt|;
block|}
DECL|method|getServiceName ()
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
DECL|method|setServiceName (String serviceName)
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
DECL|method|getHostName ()
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
DECL|method|setHostName (String hostName)
specifier|public
name|void
name|setHostName
parameter_list|(
name|String
name|hostName
parameter_list|)
block|{
name|this
operator|.
name|hostName
operator|=
name|hostName
expr_stmt|;
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
if|if
condition|(
name|nagiosSettings
operator|==
literal|null
condition|)
block|{
name|nagiosSettings
operator|=
name|configuration
operator|.
name|getNagiosSettings
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|sender
operator|==
literal|null
condition|)
block|{
name|sender
operator|=
operator|new
name|NagiosPassiveCheckSender
argument_list|(
name|nagiosSettings
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Using {}"
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
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
name|sender
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

