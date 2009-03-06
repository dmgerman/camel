begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|model
operator|.
name|ProcessorDefinition
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
name|TraceableUnitOfWork
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
name|MessageHelper
import|;
end_import

begin_comment
comment|/**  * Default {@link TraceEventMessage}.  */
end_comment

begin_class
DECL|class|DefaultTraceEventMessage
specifier|public
specifier|final
class|class
name|DefaultTraceEventMessage
implements|implements
name|Serializable
implements|,
name|TraceEventMessage
block|{
DECL|field|timestamp
specifier|private
name|Date
name|timestamp
decl_stmt|;
DECL|field|fromEndpointUri
specifier|private
name|String
name|fromEndpointUri
decl_stmt|;
DECL|field|previousNode
specifier|private
name|String
name|previousNode
decl_stmt|;
DECL|field|toNode
specifier|private
name|String
name|toNode
decl_stmt|;
DECL|field|exchangeId
specifier|private
name|String
name|exchangeId
decl_stmt|;
DECL|field|shortExchangeId
specifier|private
name|String
name|shortExchangeId
decl_stmt|;
DECL|field|exchangePattern
specifier|private
name|String
name|exchangePattern
decl_stmt|;
DECL|field|properties
specifier|private
name|String
name|properties
decl_stmt|;
DECL|field|headers
specifier|private
name|String
name|headers
decl_stmt|;
DECL|field|body
specifier|private
name|String
name|body
decl_stmt|;
DECL|field|bodyType
specifier|private
name|String
name|bodyType
decl_stmt|;
DECL|field|outHeaders
specifier|private
name|String
name|outHeaders
decl_stmt|;
DECL|field|outBody
specifier|private
name|String
name|outBody
decl_stmt|;
DECL|field|outBodyType
specifier|private
name|String
name|outBodyType
decl_stmt|;
DECL|field|causedByException
specifier|private
name|String
name|causedByException
decl_stmt|;
comment|/**      * Creates a {@link DefaultTraceEventMessage} based on the given node it was traced while processing      * the current {@link Exchange}      *      * @param toNode the node where this trace is intercepted      * @param exchange the current {@link Exchange}      */
DECL|method|DefaultTraceEventMessage (final Date timestamp, final ProcessorDefinition toNode, final Exchange exchange)
specifier|public
name|DefaultTraceEventMessage
parameter_list|(
specifier|final
name|Date
name|timestamp
parameter_list|,
specifier|final
name|ProcessorDefinition
name|toNode
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// false because we don't want to introduce side effects
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
decl_stmt|;
comment|// need to use defensive copies to avoid Exchange altering after the point of interception
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
name|this
operator|.
name|fromEndpointUri
operator|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|previousNode
operator|=
name|extractPreviousNode
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|toNode
operator|=
name|extractNode
argument_list|(
name|toNode
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchangeId
operator|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
expr_stmt|;
name|this
operator|.
name|shortExchangeId
operator|=
name|extractShortExchangeId
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchangePattern
operator|=
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|this
operator|.
name|headers
operator|=
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|this
operator|.
name|body
operator|=
name|MessageHelper
operator|.
name|extractBodyAsString
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|bodyType
operator|=
name|MessageHelper
operator|.
name|getBodyTypeName
argument_list|(
name|in
argument_list|)
expr_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|outHeaders
operator|=
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|this
operator|.
name|outBody
operator|=
name|MessageHelper
operator|.
name|extractBodyAsString
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|this
operator|.
name|outBodyType
operator|=
name|MessageHelper
operator|.
name|getBodyTypeName
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|causedByException
operator|=
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|toString
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
comment|// Implementation
comment|//---------------------------------------------------------------
DECL|method|extractNode (ProcessorDefinition node)
specifier|private
name|String
name|extractNode
parameter_list|(
name|ProcessorDefinition
name|node
parameter_list|)
block|{
return|return
name|node
operator|.
name|getShortName
argument_list|()
operator|+
literal|"("
operator|+
name|node
operator|.
name|getLabel
argument_list|()
operator|+
literal|")"
return|;
block|}
DECL|method|extractShortExchangeId (Exchange exchange)
specifier|private
name|String
name|extractShortExchangeId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|.
name|substring
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
DECL|method|extractPreviousNode (Exchange exchange)
specifier|private
name|String
name|extractPreviousNode
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|instanceof
name|TraceableUnitOfWork
condition|)
block|{
name|TraceableUnitOfWork
name|tuow
init|=
operator|(
name|TraceableUnitOfWork
operator|)
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
decl_stmt|;
name|ProcessorDefinition
name|last
init|=
name|tuow
operator|.
name|getLastInterceptedNode
argument_list|()
decl_stmt|;
return|return
name|last
operator|!=
literal|null
condition|?
name|extractNode
argument_list|(
name|last
argument_list|)
else|:
literal|null
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// Properties
comment|//---------------------------------------------------------------
DECL|method|getTimestamp ()
specifier|public
name|Date
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
DECL|method|getFromEndpointUri ()
specifier|public
name|String
name|getFromEndpointUri
parameter_list|()
block|{
return|return
name|fromEndpointUri
return|;
block|}
DECL|method|getPreviousNode ()
specifier|public
name|String
name|getPreviousNode
parameter_list|()
block|{
return|return
name|previousNode
return|;
block|}
DECL|method|getToNode ()
specifier|public
name|String
name|getToNode
parameter_list|()
block|{
return|return
name|toNode
return|;
block|}
DECL|method|getExchangeId ()
specifier|public
name|String
name|getExchangeId
parameter_list|()
block|{
return|return
name|exchangeId
return|;
block|}
DECL|method|getShortExchangeId ()
specifier|public
name|String
name|getShortExchangeId
parameter_list|()
block|{
return|return
name|shortExchangeId
return|;
block|}
DECL|method|getExchangePattern ()
specifier|public
name|String
name|getExchangePattern
parameter_list|()
block|{
return|return
name|exchangePattern
return|;
block|}
DECL|method|getProperties ()
specifier|public
name|String
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|getHeaders ()
specifier|public
name|String
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
DECL|method|getBody ()
specifier|public
name|String
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
DECL|method|getBodyType ()
specifier|public
name|String
name|getBodyType
parameter_list|()
block|{
return|return
name|bodyType
return|;
block|}
DECL|method|getOutBody ()
specifier|public
name|String
name|getOutBody
parameter_list|()
block|{
return|return
name|outBody
return|;
block|}
DECL|method|getOutBodyType ()
specifier|public
name|String
name|getOutBodyType
parameter_list|()
block|{
return|return
name|outBodyType
return|;
block|}
DECL|method|getOutHeaders ()
specifier|public
name|String
name|getOutHeaders
parameter_list|()
block|{
return|return
name|outHeaders
return|;
block|}
DECL|method|setOutHeaders (String outHeaders)
specifier|public
name|void
name|setOutHeaders
parameter_list|(
name|String
name|outHeaders
parameter_list|)
block|{
name|this
operator|.
name|outHeaders
operator|=
name|outHeaders
expr_stmt|;
block|}
DECL|method|getCausedByException ()
specifier|public
name|String
name|getCausedByException
parameter_list|()
block|{
return|return
name|causedByException
return|;
block|}
DECL|method|setTimestamp (Date timestamp)
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|Date
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
DECL|method|setFromEndpointUri (String fromEndpointUri)
specifier|public
name|void
name|setFromEndpointUri
parameter_list|(
name|String
name|fromEndpointUri
parameter_list|)
block|{
name|this
operator|.
name|fromEndpointUri
operator|=
name|fromEndpointUri
expr_stmt|;
block|}
DECL|method|setPreviousNode (String previousNode)
specifier|public
name|void
name|setPreviousNode
parameter_list|(
name|String
name|previousNode
parameter_list|)
block|{
name|this
operator|.
name|previousNode
operator|=
name|previousNode
expr_stmt|;
block|}
DECL|method|setToNode (String toNode)
specifier|public
name|void
name|setToNode
parameter_list|(
name|String
name|toNode
parameter_list|)
block|{
name|this
operator|.
name|toNode
operator|=
name|toNode
expr_stmt|;
block|}
DECL|method|setExchangeId (String exchangeId)
specifier|public
name|void
name|setExchangeId
parameter_list|(
name|String
name|exchangeId
parameter_list|)
block|{
name|this
operator|.
name|exchangeId
operator|=
name|exchangeId
expr_stmt|;
block|}
DECL|method|setShortExchangeId (String shortExchangeId)
specifier|public
name|void
name|setShortExchangeId
parameter_list|(
name|String
name|shortExchangeId
parameter_list|)
block|{
name|this
operator|.
name|shortExchangeId
operator|=
name|shortExchangeId
expr_stmt|;
block|}
DECL|method|setExchangePattern (String exchangePattern)
specifier|public
name|void
name|setExchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|this
operator|.
name|exchangePattern
operator|=
name|exchangePattern
expr_stmt|;
block|}
DECL|method|setProperties (String properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|String
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|setHeaders (String headers)
specifier|public
name|void
name|setHeaders
parameter_list|(
name|String
name|headers
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
block|}
DECL|method|setBody (String body)
specifier|public
name|void
name|setBody
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
block|}
DECL|method|setBodyType (String bodyType)
specifier|public
name|void
name|setBodyType
parameter_list|(
name|String
name|bodyType
parameter_list|)
block|{
name|this
operator|.
name|bodyType
operator|=
name|bodyType
expr_stmt|;
block|}
DECL|method|setOutBody (String outBody)
specifier|public
name|void
name|setOutBody
parameter_list|(
name|String
name|outBody
parameter_list|)
block|{
name|this
operator|.
name|outBody
operator|=
name|outBody
expr_stmt|;
block|}
DECL|method|setOutBodyType (String outBodyType)
specifier|public
name|void
name|setOutBodyType
parameter_list|(
name|String
name|outBodyType
parameter_list|)
block|{
name|this
operator|.
name|outBodyType
operator|=
name|outBodyType
expr_stmt|;
block|}
DECL|method|setCausedByException (String causedByException)
specifier|public
name|void
name|setCausedByException
parameter_list|(
name|String
name|causedByException
parameter_list|)
block|{
name|this
operator|.
name|causedByException
operator|=
name|causedByException
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
literal|"TraceEventMessage["
operator|+
name|exchangeId
operator|+
literal|"] on node: "
operator|+
name|toNode
return|;
block|}
block|}
end_class

end_unit

