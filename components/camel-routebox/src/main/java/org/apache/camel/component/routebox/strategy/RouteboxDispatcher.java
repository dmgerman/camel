begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.strategy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
operator|.
name|strategy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

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
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|CamelExchangeException
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
name|CamelExecutionException
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
name|Endpoint
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
name|ExchangePattern
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
name|Message
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
name|ProducerTemplate
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
name|routebox
operator|.
name|RouteboxEndpoint
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
name|model
operator|.
name|FromDefinition
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
name|model
operator|.
name|RouteDefinition
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
name|SynchronizationAdapter
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

begin_class
DECL|class|RouteboxDispatcher
specifier|public
class|class
name|RouteboxDispatcher
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RouteboxDispatcher
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
DECL|method|RouteboxDispatcher (ProducerTemplate producer)
specifier|public
name|RouteboxDispatcher
parameter_list|(
name|ProducerTemplate
name|producer
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
DECL|method|dispatchSync (RouteboxEndpoint endpoint, Exchange exchange)
specifier|public
name|Exchange
name|dispatchSync
parameter_list|(
name|RouteboxEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|dispatchUri
decl_stmt|;
name|Exchange
name|reply
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Dispatching exchange {} to endpoint {}"
argument_list|,
name|exchange
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|dispatchUri
operator|=
name|selectDispatchUri
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|==
name|ExchangePattern
operator|.
name|InOnly
condition|)
block|{
name|reply
operator|=
name|producer
operator|.
name|send
argument_list|(
name|dispatchUri
operator|.
name|toASCIIString
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reply
operator|=
name|issueRequest
argument_list|(
name|endpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|reply
return|;
block|}
DECL|method|dispatchAsync (RouteboxEndpoint endpoint, Exchange exchange)
specifier|public
name|Exchange
name|dispatchAsync
parameter_list|(
name|RouteboxEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|dispatchUri
decl_stmt|;
name|Exchange
name|reply
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Dispatching exchange {} to endpoint {}"
argument_list|,
name|exchange
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|dispatchUri
operator|=
name|selectDispatchUri
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|==
name|ExchangePattern
operator|.
name|InOnly
condition|)
block|{
name|producer
operator|.
name|asyncSend
argument_list|(
name|dispatchUri
operator|.
name|toASCIIString
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|reply
operator|=
name|exchange
expr_stmt|;
block|}
else|else
block|{
name|Future
argument_list|<
name|Exchange
argument_list|>
name|future
init|=
name|producer
operator|.
name|asyncCallback
argument_list|(
name|dispatchUri
operator|.
name|toASCIIString
argument_list|()
argument_list|,
name|exchange
argument_list|,
operator|new
name|SynchronizationAdapter
argument_list|()
argument_list|)
decl_stmt|;
name|reply
operator|=
name|future
operator|.
name|get
argument_list|(
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
return|return
name|reply
return|;
block|}
DECL|method|selectDispatchUri (RouteboxEndpoint endpoint, Exchange exchange)
specifier|protected
name|URI
name|selectDispatchUri
parameter_list|(
name|RouteboxEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|dispatchUri
decl_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|consumerUris
init|=
name|getInnerContextConsumerList
argument_list|(
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getInnerContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumerUris
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No routes found to dispatch in Routebox at "
operator|+
name|endpoint
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|consumerUris
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|dispatchUri
operator|=
name|consumerUris
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getDispatchMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// apply URI string found in dispatch Map
name|String
name|key
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"ROUTE_DISPATCH_KEY"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getDispatchMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|dispatchUri
operator|=
operator|new
name|URI
argument_list|(
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getDispatchMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No matching entry found in Dispatch Map for ROUTE_DISPATCH_KEY: "
operator|+
name|key
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// apply dispatch strategy
name|dispatchUri
operator|=
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getDispatchStrategy
argument_list|()
operator|.
name|selectDestinationUri
argument_list|(
name|consumerUris
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|dispatchUri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No matching inner routes found for Operation"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Dispatch URI set to: "
operator|+
name|dispatchUri
operator|.
name|toASCIIString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|dispatchUri
return|;
block|}
DECL|method|getInnerContextConsumerList (CamelContext context)
specifier|protected
name|List
argument_list|<
name|URI
argument_list|>
name|getInnerContextConsumerList
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|List
argument_list|<
name|URI
argument_list|>
name|consumerList
init|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routeDefinitions
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteDefinition
name|routeDefinition
range|:
name|routeDefinitions
control|)
block|{
name|List
argument_list|<
name|FromDefinition
argument_list|>
name|inputs
init|=
name|routeDefinition
operator|.
name|getInputs
argument_list|()
decl_stmt|;
for|for
control|(
name|FromDefinition
name|input
range|:
name|inputs
control|)
block|{
name|consumerList
operator|.
name|add
argument_list|(
operator|new
name|URI
argument_list|(
name|input
operator|.
name|getUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|consumerList
return|;
block|}
DECL|method|issueRequest (Endpoint endpoint, ExchangePattern pattern, final Object body, final Map<String, Object> headers)
specifier|public
name|Exchange
name|issueRequest
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

