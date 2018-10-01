begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.common.header
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|header
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|DefaultHeaderFilterStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|headers
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
comment|/**  * The default CXF header filter strategy.  */
end_comment

begin_class
DECL|class|CxfHeaderFilterStrategy
specifier|public
class|class
name|CxfHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
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
name|CxfHeaderFilterStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|messageHeaderFiltersMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|MessageHeaderFilter
argument_list|>
name|messageHeaderFiltersMap
decl_stmt|;
DECL|field|messageHeaderFilters
specifier|private
name|List
argument_list|<
name|MessageHeaderFilter
argument_list|>
name|messageHeaderFilters
decl_stmt|;
DECL|field|relayHeaders
specifier|private
name|boolean
name|relayHeaders
init|=
literal|true
decl_stmt|;
DECL|field|allowFilterNamespaceClash
specifier|private
name|boolean
name|allowFilterNamespaceClash
decl_stmt|;
DECL|field|relayAllMessageHeaders
specifier|private
name|boolean
name|relayAllMessageHeaders
decl_stmt|;
DECL|method|CxfHeaderFilterStrategy ()
specifier|public
name|CxfHeaderFilterStrategy
parameter_list|()
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
DECL|method|initialize ()
specifier|protected
name|void
name|initialize
parameter_list|()
block|{
comment|//filter the operationName and operationName
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAMESPACE
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Request and response context Maps will be passed to CXF Client APIs
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// protocol headers are stored as a Map.  DefaultCxfBinding
comment|// read the Map and send each entry to the filter.  Therefore,
comment|// we need to filter the header of this name.
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Add the filter for the Generic Message header
comment|// http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.5
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"cache-control"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"connection"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"date"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"pragma"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"trailer"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"transfer-encoding"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"upgrade"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"via"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"warning"
argument_list|)
expr_stmt|;
comment|// Since CXF can take the content-type from the protocol header
comment|// we need to filter this header of this name.
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"Content-Type"
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Filter out Content-Length since it can fool Jetty (HttpGenerator) to
comment|// close response output stream prematurely.  (It occurs when the
comment|// message size (e.g. with attachment) is large and response content length
comment|// is bigger than request content length.)
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"Content-Length"
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Filter Content-Length as it will cause some trouble when the message
comment|// is passed to the other endpoint
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-length"
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|setLowerCase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// initialize message header filter map with default SOAP filter
name|messageHeaderFiltersMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|addToMessageHeaderFilterMap
argument_list|(
operator|new
name|SoapMessageHeaderFilter
argument_list|()
argument_list|)
expr_stmt|;
comment|// filter headers begin with "Camel" or "org.apache.camel"
name|setOutFilterPattern
argument_list|(
literal|"(Camel|org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|extendedFilter (Direction direction, String key, Object value, Exchange exchange)
specifier|protected
name|boolean
name|extendedFilter
parameter_list|(
name|Direction
name|direction
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// Currently only handles Header.HEADER_LIST message header relay/filter
if|if
condition|(
operator|!
name|Header
operator|.
name|HEADER_LIST
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|relayHeaders
condition|)
block|{
comment|// not propagating Header.HEADER_LIST at all
return|return
literal|true
return|;
block|}
if|if
condition|(
name|relayAllMessageHeaders
condition|)
block|{
comment|// all message headers will be relayed (no filtering)
return|return
literal|false
return|;
block|}
comment|// get filter
name|MessageHeaderFilter
name|messageHeaderfilter
init|=
name|getMessageHeaderFilter
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageHeaderfilter
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No CXF Binding namespace can be resolved.  Message headers are intact."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"messageHeaderfilter = {}"
argument_list|,
name|messageHeaderfilter
argument_list|)
expr_stmt|;
try|try
block|{
name|messageHeaderfilter
operator|.
name|filter
argument_list|(
name|direction
argument_list|,
operator|(
name|List
argument_list|<
name|Header
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
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
literal|"Failed to cast value to Header<List> due to {}"
argument_list|,
name|t
operator|.
name|toString
argument_list|()
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
comment|// return false since the header list (which has been filtered) should be propagated
return|return
literal|false
return|;
block|}
DECL|method|addToMessageHeaderFilterMap (MessageHeaderFilter filter)
specifier|private
name|void
name|addToMessageHeaderFilterMap
parameter_list|(
name|MessageHeaderFilter
name|filter
parameter_list|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|filter
operator|.
name|getActivationNamespaces
argument_list|()
control|)
block|{
if|if
condition|(
name|messageHeaderFiltersMap
operator|.
name|containsKey
argument_list|(
name|ns
argument_list|)
operator|&&
name|messageHeaderFiltersMap
operator|.
name|get
argument_list|(
name|ns
argument_list|)
operator|!=
name|messageHeaderFiltersMap
operator|&&
operator|!
name|allowFilterNamespaceClash
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"More then one MessageHeaderRelay activates "
operator|+
literal|"for the same namespace: "
operator|+
name|ns
argument_list|)
throw|;
block|}
name|messageHeaderFiltersMap
operator|.
name|put
argument_list|(
name|ns
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getMessageHeaderFilter (Exchange exchange)
specifier|private
name|MessageHeaderFilter
name|getMessageHeaderFilter
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|BindingOperationInfo
name|boi
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|BindingOperationInfo
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|ns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|boi
operator|!=
literal|null
condition|)
block|{
name|BindingInfo
name|b
init|=
name|boi
operator|.
name|getBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|ns
operator|=
name|b
operator|.
name|getBindingId
argument_list|()
expr_stmt|;
block|}
block|}
name|MessageHeaderFilter
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ns
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|messageHeaderFiltersMap
operator|.
name|get
argument_list|(
name|ns
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * @param messageHeaderFilters the messageHeaderFilters to set      */
DECL|method|setMessageHeaderFilters (List<MessageHeaderFilter> messageHeaderFilters)
specifier|public
name|void
name|setMessageHeaderFilters
parameter_list|(
name|List
argument_list|<
name|MessageHeaderFilter
argument_list|>
name|messageHeaderFilters
parameter_list|)
block|{
name|this
operator|.
name|messageHeaderFilters
operator|=
name|messageHeaderFilters
expr_stmt|;
comment|// clear the amp to allow removal of default filter
name|messageHeaderFiltersMap
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|MessageHeaderFilter
name|filter
range|:
name|messageHeaderFilters
control|)
block|{
name|addToMessageHeaderFilterMap
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return the messageHeaderFilters      */
DECL|method|getMessageHeaderFilters ()
specifier|public
name|List
argument_list|<
name|MessageHeaderFilter
argument_list|>
name|getMessageHeaderFilters
parameter_list|()
block|{
return|return
name|messageHeaderFilters
return|;
block|}
comment|/**      * @return the allowFilterNamespaceClash      */
DECL|method|isAllowFilterNamespaceClash ()
specifier|public
name|boolean
name|isAllowFilterNamespaceClash
parameter_list|()
block|{
return|return
name|allowFilterNamespaceClash
return|;
block|}
comment|/**      * @param allowFilterNamespaceClash the allowFilterNamespaceClash to set      */
DECL|method|setAllowFilterNamespaceClash (boolean allowFilterNamespaceClash)
specifier|public
name|void
name|setAllowFilterNamespaceClash
parameter_list|(
name|boolean
name|allowFilterNamespaceClash
parameter_list|)
block|{
name|this
operator|.
name|allowFilterNamespaceClash
operator|=
name|allowFilterNamespaceClash
expr_stmt|;
block|}
comment|/**      * @return the messageHeaderFiltersMap      */
DECL|method|getMessageHeaderFiltersMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|MessageHeaderFilter
argument_list|>
name|getMessageHeaderFiltersMap
parameter_list|()
block|{
return|return
name|messageHeaderFiltersMap
return|;
block|}
comment|/**      * @param relayHeaders the relayHeaders to set      */
DECL|method|setRelayHeaders (boolean relayHeaders)
specifier|public
name|void
name|setRelayHeaders
parameter_list|(
name|boolean
name|relayHeaders
parameter_list|)
block|{
name|this
operator|.
name|relayHeaders
operator|=
name|relayHeaders
expr_stmt|;
block|}
comment|/**      * @return the relayHeaders      */
DECL|method|isRelayHeaders ()
specifier|public
name|boolean
name|isRelayHeaders
parameter_list|()
block|{
return|return
name|relayHeaders
return|;
block|}
comment|/**      * @return the relayAllMessageHeaders      */
DECL|method|isRelayAllMessageHeaders ()
specifier|public
name|boolean
name|isRelayAllMessageHeaders
parameter_list|()
block|{
return|return
name|relayAllMessageHeaders
return|;
block|}
comment|/**      * @param relayAllMessageHeaders the relayAllMessageHeaders to set      */
DECL|method|setRelayAllMessageHeaders (boolean relayAllMessageHeaders)
specifier|public
name|void
name|setRelayAllMessageHeaders
parameter_list|(
name|boolean
name|relayAllMessageHeaders
parameter_list|)
block|{
name|this
operator|.
name|relayAllMessageHeaders
operator|=
name|relayAllMessageHeaders
expr_stmt|;
block|}
block|}
end_class

end_unit

