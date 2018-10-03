begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|component
operator|.
name|apns
operator|.
name|model
operator|.
name|InactiveDevice
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
name|ScheduledPollConsumer
import|;
end_import

begin_class
DECL|class|ApnsConsumer
specifier|public
class|class
name|ApnsConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|DEFAULT_CONSUME_INITIAL_DELAY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_CONSUME_INITIAL_DELAY
init|=
literal|10
decl_stmt|;
DECL|field|DEFAULT_CONSUME_DELAY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_CONSUME_DELAY
init|=
literal|3600
decl_stmt|;
DECL|field|DEFAULT_APNS_FIXED_DELAY
specifier|private
specifier|static
specifier|final
name|boolean
name|DEFAULT_APNS_FIXED_DELAY
init|=
literal|true
decl_stmt|;
DECL|method|ApnsConsumer (ApnsEndpoint apnsEndpoint, Processor processor)
specifier|public
name|ApnsConsumer
parameter_list|(
name|ApnsEndpoint
name|apnsEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|apnsEndpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|setInitialDelay
argument_list|(
name|DEFAULT_CONSUME_INITIAL_DELAY
argument_list|)
expr_stmt|;
name|setDelay
argument_list|(
name|DEFAULT_CONSUME_DELAY
argument_list|)
expr_stmt|;
name|setUseFixedDelay
argument_list|(
name|DEFAULT_APNS_FIXED_DELAY
argument_list|)
expr_stmt|;
block|}
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|InactiveDevice
argument_list|>
name|inactiveDeviceList
init|=
name|getInactiveDevices
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|InactiveDevice
argument_list|>
name|it
init|=
name|inactiveDeviceList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|InactiveDevice
name|inactiveDevice
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|inactiveDevice
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|inactiveDeviceList
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|getInactiveDevices ()
specifier|private
name|List
argument_list|<
name|InactiveDevice
argument_list|>
name|getInactiveDevices
parameter_list|()
block|{
name|ApnsEndpoint
name|ae
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Date
argument_list|>
name|inactiveDeviceMap
init|=
name|ae
operator|.
name|getApnsService
argument_list|()
operator|.
name|getInactiveDevices
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|InactiveDevice
argument_list|>
name|inactiveDeviceList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Date
argument_list|>
name|inactiveDeviceEntry
range|:
name|inactiveDeviceMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|deviceToken
init|=
name|inactiveDeviceEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Date
name|date
init|=
name|inactiveDeviceEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|InactiveDevice
name|inactiveDevice
init|=
operator|new
name|InactiveDevice
argument_list|(
name|deviceToken
argument_list|,
name|date
argument_list|)
decl_stmt|;
name|inactiveDeviceList
operator|.
name|add
argument_list|(
name|inactiveDevice
argument_list|)
expr_stmt|;
block|}
return|return
name|inactiveDeviceList
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|ApnsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|ApnsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
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
comment|// only add as consumer if not already registered
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|getConsumers
argument_list|()
operator|.
name|contains
argument_list|(
name|this
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|getConsumers
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Endpoint "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|" only allows 1 active consumer but you attempted to start a 2nd consumer."
argument_list|)
throw|;
block|}
name|getEndpoint
argument_list|()
operator|.
name|getConsumers
argument_list|()
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getConsumers
argument_list|()
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

