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
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|Callback
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
name|PubnubError
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
name|InvalidPayloadException
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
name|json
operator|.
name|JSONArray
import|;
end_import

begin_import
import|import
name|org
operator|.
name|json
operator|.
name|JSONObject
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
DECL|method|PubNubProducer (PubNubEndpoint endpoint)
specifier|public
name|PubNubProducer
parameter_list|(
name|PubNubEndpoint
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
name|Callback
name|pubnubCallback
init|=
name|pubnubCallback
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
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
name|trace
argument_list|(
literal|"Executing {} operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|PUBLISH
case|:
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
name|channel
operator|=
name|channel
operator|!=
literal|null
condition|?
name|channel
else|:
name|endpoint
operator|.
name|getChannel
argument_list|()
expr_stmt|;
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
return|return
literal|true
return|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Sending message [{}] to channel [{}]"
argument_list|,
name|body
argument_list|,
name|channel
argument_list|)
expr_stmt|;
if|if
condition|(
name|body
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|JSONObject
operator|.
name|class
argument_list|)
condition|)
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|publish
argument_list|(
name|channel
argument_list|,
operator|(
name|JSONObject
operator|)
name|body
argument_list|,
name|pubnubCallback
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|JSONArray
operator|.
name|class
argument_list|)
condition|)
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|publish
argument_list|(
name|channel
argument_list|,
operator|(
name|JSONArray
operator|)
name|body
argument_list|,
name|pubnubCallback
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|publish
argument_list|(
name|channel
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|pubnubCallback
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPayloadException
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
block|}
break|break;
block|}
case|case
name|GET_HISTORY
case|:
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|history
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
literal|false
argument_list|,
name|pubnubCallback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|GET_STATE
case|:
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
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|getState
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
name|uuid
operator|!=
literal|null
condition|?
name|uuid
else|:
name|endpoint
operator|.
name|getUuid
argument_list|()
argument_list|,
name|pubnubCallback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|HERE_NOW
case|:
block|{
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|hereNow
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
name|pubnubCallback
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|SET_STATE
case|:
block|{
try|try
block|{
name|JSONObject
name|state
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|JSONObject
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|setState
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
name|uuid
operator|!=
literal|null
condition|?
name|uuid
else|:
name|endpoint
operator|.
name|getUuid
argument_list|()
argument_list|,
name|state
argument_list|,
name|pubnubCallback
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPayloadException
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
break|break;
block|}
case|case
name|WHERE_NOW
case|:
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
name|endpoint
operator|.
name|getPubnub
argument_list|()
operator|.
name|whereNow
argument_list|(
name|uuid
operator|!=
literal|null
condition|?
name|uuid
else|:
name|endpoint
operator|.
name|getUuid
argument_list|()
argument_list|,
name|pubnubCallback
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
return|return
literal|false
return|;
block|}
DECL|method|pubnubCallback (final Exchange exchange, final AsyncCallback callback)
specifier|private
name|Callback
name|pubnubCallback
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
name|Callback
name|pubnubCallback
init|=
operator|new
name|Callback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|successCallback
parameter_list|(
name|String
name|channel
parameter_list|,
name|Object
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"PubNub response {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PubNubConstants
operator|.
name|CHANNEL
argument_list|,
name|channel
argument_list|)
expr_stmt|;
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
name|message
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|errorCallback
parameter_list|(
name|String
name|channel
parameter_list|,
name|PubnubError
name|error
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
name|error
operator|.
name|toString
argument_list|()
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
block|}
block|}
decl_stmt|;
return|return
name|pubnubCallback
return|;
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
name|endpoint
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
argument_list|)
else|:
name|Operation
operator|.
name|PUBLISH
return|;
block|}
DECL|enum|Operation
specifier|private
enum|enum
name|Operation
block|{
DECL|enumConstant|HERE_NOW
DECL|enumConstant|WHERE_NOW
DECL|enumConstant|GET_STATE
DECL|enumConstant|SET_STATE
DECL|enumConstant|GET_HISTORY
DECL|enumConstant|PUBLISH
name|HERE_NOW
block|,
name|WHERE_NOW
block|,
name|GET_STATE
block|,
name|SET_STATE
block|,
name|GET_HISTORY
block|,
name|PUBLISH
block|;     }
block|}
end_class

end_unit

