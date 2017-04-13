begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pubnub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|PubNubException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|callbacks
operator|.
name|PNCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|PNErrorData
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|PNPublishResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|PNStatus
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|history
operator|.
name|PNHistoryResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|presence
operator|.
name|PNGetStateResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|presence
operator|.
name|PNHereNowResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|presence
operator|.
name|PNSetStateResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|models
operator|.
name|consumer
operator|.
name|presence
operator|.
name|PNWhereNowResult
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
name|CamelException
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
name|impl
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
name|util
operator|.
name|ObjectHelper
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
comment|/**  * The PubNub producer.  */
end_comment

begin_class
DECL|class|PubNubProducer
specifier|public
class|class
name|PubNubProducer
extends|extends
name|DefaultAsyncProducer
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
name|PubNubProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|PubNubEndpoint
name|endpoint
decl_stmt|;
DECL|field|pubnubConfiguration
specifier|private
specifier|final
name|PubNubConfiguration
name|pubnubConfiguration
decl_stmt|;
DECL|method|PubNubProducer (PubNubEndpoint endpoint, PubNubConfiguration pubNubConfiguration)
specifier|public
name|PubNubProducer
parameter_list|(
name|PubNubEndpoint
name|endpoint
parameter_list|,
name|PubNubConfiguration
name|pubNubConfiguration
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
name|pubnubConfiguration
operator|=
name|pubNubConfiguration
expr_stmt|;
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
name|Operation
name|operation
init|=
name|getOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Executing {} operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
try|try
block|{
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|PUBLISH
case|:
block|{
name|doPublish
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|FIRE
case|:
block|{
name|doFire
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|GETHISTORY
case|:
block|{
name|doGetHistory
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|GETSTATE
case|:
block|{
name|doGetState
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|HERENOW
case|:
block|{
name|doHereNow
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|SETSTATE
case|:
block|{
name|doSetState
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|WHERENOW
case|:
block|{
name|doWhereNow
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
block|}
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
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|doPublish (Exchange exchange, AsyncCallback callback)
specifier|private
name|void
name|doPublish
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Object
name|body
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
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|body
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can not publish empty message"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending message [{}] to channel [{}]"
argument_list|,
name|body
argument_list|,
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|publish
argument_list|()
operator|.
name|message
argument_list|(
name|body
argument_list|)
operator|.
name|channel
argument_list|(
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
operator|.
name|usePOST
argument_list|(
literal|true
argument_list|)
operator|.
name|async
argument_list|(
operator|new
name|PNCallback
argument_list|<
name|PNPublishResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|PNPublishResult
name|result
parameter_list|,
name|PNStatus
name|status
parameter_list|)
block|{
if|if
condition|(
operator|!
name|status
operator|.
name|isError
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PubNubConstants
operator|.
name|TIMETOKEN
argument_list|,
name|result
operator|.
name|getTimetoken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|processMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|status
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|doFire (Exchange exchange, AsyncCallback callback)
specifier|private
name|void
name|doFire
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Object
name|body
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
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|body
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"Can not fire empty message"
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending message [{}] to channel [{}]"
argument_list|,
name|body
argument_list|,
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|fire
argument_list|()
operator|.
name|message
argument_list|(
name|body
argument_list|)
operator|.
name|channel
argument_list|(
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
operator|.
name|async
argument_list|(
operator|new
name|PNCallback
argument_list|<
name|PNPublishResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|PNPublishResult
name|result
parameter_list|,
name|PNStatus
name|status
parameter_list|)
block|{
if|if
condition|(
operator|!
name|status
operator|.
name|isError
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PubNubConstants
operator|.
name|TIMETOKEN
argument_list|,
name|result
operator|.
name|getTimetoken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|processMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|status
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|doGetHistory (Exchange exchange, AsyncCallback callback)
specifier|private
name|void
name|doGetHistory
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|history
argument_list|()
operator|.
name|channel
argument_list|(
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
operator|.
name|async
argument_list|(
operator|new
name|PNCallback
argument_list|<
name|PNHistoryResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|PNHistoryResult
name|result
parameter_list|,
name|PNStatus
name|status
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Got history message [{}]"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|processMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|status
argument_list|,
name|result
operator|.
name|getMessages
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|doSetState (Exchange exchange, AsyncCallback callback)
specifier|private
name|void
name|doSetState
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Object
name|body
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
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|body
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"Can not publish empty message"
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending setState [{}] to channel [{}]"
argument_list|,
name|body
argument_list|,
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|setPresenceState
argument_list|()
operator|.
name|channels
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
operator|.
name|state
argument_list|(
name|body
argument_list|)
operator|.
name|uuid
argument_list|(
name|getUUID
argument_list|(
name|exchange
argument_list|)
argument_list|)
operator|.
name|async
argument_list|(
operator|new
name|PNCallback
argument_list|<
name|PNSetStateResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onResponse
parameter_list|(
name|PNSetStateResult
name|result
parameter_list|,
name|PNStatus
name|status
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Got setState responsee [{}]"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|processMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|status
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|doGetState (Exchange exchange, AsyncCallback callback)
specifier|private
name|void
name|doGetState
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|getPresenceState
argument_list|()
operator|.
name|channels
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
operator|.
name|uuid
argument_list|(
name|getUUID
argument_list|(
name|exchange
argument_list|)
argument_list|)
operator|.
name|async
argument_list|(
operator|new
name|PNCallback
argument_list|<
name|PNGetStateResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|PNGetStateResult
name|result
parameter_list|,
name|PNStatus
name|status
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Got state [{}]"
argument_list|,
name|result
operator|.
name|getStateByUUID
argument_list|()
argument_list|)
expr_stmt|;
name|processMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|status
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|doHereNow (Exchange exchange, AsyncCallback callback)
specifier|private
name|void
name|doHereNow
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|hereNow
argument_list|()
operator|.
name|channels
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|getChannel
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
operator|.
name|includeState
argument_list|(
literal|true
argument_list|)
operator|.
name|includeUUIDs
argument_list|(
literal|true
argument_list|)
operator|.
name|async
argument_list|(
operator|new
name|PNCallback
argument_list|<
name|PNHereNowResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|PNHereNowResult
name|result
parameter_list|,
name|PNStatus
name|status
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Got herNow message [{}]"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|processMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|status
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|doWhereNow (Exchange exchange, AsyncCallback callback)
specifier|private
name|void
name|doWhereNow
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|whereNow
argument_list|()
operator|.
name|uuid
argument_list|(
name|getUUID
argument_list|(
name|exchange
argument_list|)
argument_list|)
operator|.
name|async
argument_list|(
operator|new
name|PNCallback
argument_list|<
name|PNWhereNowResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|PNWhereNowResult
name|result
parameter_list|,
name|PNStatus
name|status
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Got whereNow message [{}]"
argument_list|,
name|result
operator|.
name|getChannels
argument_list|()
argument_list|)
expr_stmt|;
name|processMessage
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|status
argument_list|,
name|result
operator|.
name|getChannels
argument_list|()
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|processMessage (Exchange exchange, AsyncCallback callback, PNStatus status, Object body)
specifier|private
name|void
name|processMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|PNStatus
name|status
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
if|if
condition|(
name|status
operator|.
name|isError
argument_list|()
condition|)
block|{
name|PNErrorData
name|errorData
init|=
name|status
operator|.
name|getErrorData
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|errorData
operator|.
name|getThrowable
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|errorData
operator|!=
literal|null
operator|&&
name|errorData
operator|.
name|getThrowable
argument_list|()
operator|instanceof
name|PubNubException
condition|)
block|{
name|PubNubException
name|pubNubException
init|=
operator|(
name|PubNubException
operator|)
name|errorData
operator|.
name|getThrowable
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|pubNubException
operator|.
name|getPubnubError
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
name|errorData
operator|.
name|getThrowable
argument_list|()
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|status
operator|.
name|getErrorData
argument_list|()
operator|.
name|getThrowable
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
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
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
comment|// signal exchange completion
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|PubNubConstants
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
name|pubnubConfiguration
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
operator|.
name|toUpperCase
argument_list|()
argument_list|)
else|:
name|Operation
operator|.
name|PUBLISH
return|;
block|}
DECL|method|getChannel (Exchange exchange)
specifier|private
name|String
name|getChannel
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|channel
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PubNubConstants
operator|.
name|CHANNEL
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|channel
operator|!=
literal|null
condition|?
name|channel
else|:
name|pubnubConfiguration
operator|.
name|getChannel
argument_list|()
return|;
block|}
DECL|method|getUUID (Exchange exchange)
specifier|private
name|String
name|getUUID
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|uuid
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PubNubConstants
operator|.
name|UUID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|uuid
operator|!=
literal|null
condition|?
name|uuid
else|:
name|pubnubConfiguration
operator|.
name|getUuid
argument_list|()
return|;
block|}
DECL|enum|Operation
specifier|private
enum|enum
name|Operation
block|{
DECL|enumConstant|HERENOW
DECL|enumConstant|WHERENOW
DECL|enumConstant|GETSTATE
DECL|enumConstant|SETSTATE
DECL|enumConstant|GETHISTORY
DECL|enumConstant|PUBLISH
DECL|enumConstant|FIRE
name|HERENOW
block|,
name|WHERENOW
block|,
name|GETSTATE
block|,
name|SETSTATE
block|,
name|GETHISTORY
block|,
name|PUBLISH
block|,
name|FIRE
block|;     }
block|}
end_class

end_unit

