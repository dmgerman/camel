begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|type
operator|.
name|EndpointMappingKey
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
name|spring
operator|.
name|ws
operator|.
name|type
operator|.
name|EndpointMappingType
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|context
operator|.
name|MessageContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|EndpointInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|EndpointInvocationChain
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|EndpointMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|endpoint
operator|.
name|MessageEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|endpoint
operator|.
name|mapping
operator|.
name|AbstractEndpointMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|endpoint
operator|.
name|support
operator|.
name|PayloadRootUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|server
operator|.
name|SoapEndpointInvocationChain
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|server
operator|.
name|SoapEndpointMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|WebServiceConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|context
operator|.
name|TransportContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|context
operator|.
name|TransportContextHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpression
import|;
end_import

begin_comment
comment|/**  * Spring {@link EndpointMapping} for mapping messages to corresponding Camel  * endpoints. This class needs to be registered in the Spring  *<tt>ApplicationContext</tt> when consuming messages using any of the  * following URI schemes:  *<p/>  *<ul>  *<li><tt>spring-ws:rootqname:</tt><br/>  * Equivalent to endpoint mappings specified through  * {@link org.springframework.ws.server.endpoint.mapping.PayloadRootQNameEndpointMapping}  *<p/>  *<li><tt>spring-ws:soapaction:</tt><br/>  * Equivalent to endpoint mappings specified through  * {@link org.springframework.ws.soap.server.endpoint.mapping.SoapActionEndpointMapping}  *<p/>  *<li><tt>spring-ws:uri:</tt><br/>  * Equivalent to endpoint mappings specified through  * {@link org.springframework.ws.server.endpoint.mapping.UriEndpointMapping}  *<p/>  *<li><tt>spring-ws:xpathresult:</tt><br/>  * Equivalent to endpoint mappings specified through  * {@link org.springframework.ws.server.endpoint.mapping.XPathPayloadEndpointMapping}  *</ul>  *   * @see org.springframework.ws.server.endpoint.mapping.AbstractEndpointMapping  * @see org.springframework.ws.server.endpoint.mapping.PayloadRootQNameEndpointMapping  * @see org.springframework.ws.server.endpoint.mapping.UriEndpointMapping  * @see org.springframework.ws.server.endpoint.mapping.XPathPayloadEndpointMapping  * @see org.springframework.ws.soap.server.endpoint.mapping.SoapActionEndpointMapping  */
end_comment

begin_class
DECL|class|CamelEndpointMapping
specifier|public
class|class
name|CamelEndpointMapping
extends|extends
name|AbstractEndpointMapping
implements|implements
name|InitializingBean
implements|,
name|CamelSpringWSEndpointMapping
implements|,
name|SoapEndpointMapping
block|{
DECL|field|DOUBLE_QUOTE
specifier|private
specifier|static
specifier|final
name|String
name|DOUBLE_QUOTE
init|=
literal|"\""
decl_stmt|;
DECL|field|URI_PATH_WILDCARD
specifier|private
specifier|static
specifier|final
name|String
name|URI_PATH_WILDCARD
init|=
literal|"*"
decl_stmt|;
DECL|field|endpoints
specifier|private
name|Map
argument_list|<
name|EndpointMappingKey
argument_list|,
name|MessageEndpoint
argument_list|>
name|endpoints
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|transformerFactory
specifier|private
name|TransformerFactory
name|transformerFactory
decl_stmt|;
DECL|field|xmlConverter
specifier|private
name|XmlConverter
name|xmlConverter
decl_stmt|;
DECL|field|actorsOrRoles
specifier|private
name|String
index|[]
name|actorsOrRoles
decl_stmt|;
DECL|field|isUltimateReceiver
specifier|private
name|boolean
name|isUltimateReceiver
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
DECL|method|getEndpointInternal (MessageContext messageContext)
specifier|protected
name|Object
name|getEndpointInternal
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|EndpointMappingKey
name|key
range|:
name|endpoints
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|messageKey
decl_stmt|;
switch|switch
condition|(
name|key
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|ROOT_QNAME
case|:
name|messageKey
operator|=
name|getRootQName
argument_list|(
name|messageContext
argument_list|)
expr_stmt|;
break|break;
case|case
name|SOAP_ACTION
case|:
name|messageKey
operator|=
name|getSoapAction
argument_list|(
name|messageContext
argument_list|)
expr_stmt|;
break|break;
case|case
name|XPATHRESULT
case|:
name|messageKey
operator|=
name|getXPathResult
argument_list|(
name|messageContext
argument_list|,
name|key
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|URI
case|:
name|messageKey
operator|=
name|getUri
argument_list|()
expr_stmt|;
break|break;
case|case
name|URI_PATH
case|:
name|messageKey
operator|=
name|getUriPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|messageKey
operator|!=
literal|null
operator|&&
name|key
operator|.
name|getLookupKey
argument_list|()
operator|.
name|endsWith
argument_list|(
name|URI_PATH_WILDCARD
argument_list|)
condition|)
block|{
name|String
name|lookupKey
init|=
name|key
operator|.
name|getLookupKey
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|key
operator|.
name|getLookupKey
argument_list|()
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageKey
operator|.
name|startsWith
argument_list|(
name|lookupKey
argument_list|)
condition|)
block|{
return|return
name|endpoints
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
break|break;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Invalid mapping type specified. Supported types are: root QName, SOAP action, XPath expression and URI"
argument_list|)
throw|;
block|}
if|if
condition|(
name|messageKey
operator|!=
literal|null
operator|&&
name|key
operator|.
name|getLookupKey
argument_list|()
operator|.
name|equals
argument_list|(
name|messageKey
argument_list|)
condition|)
block|{
return|return
name|endpoints
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointInvocationChain (MessageContext messageContext, Object endpoint, EndpointInterceptor[] interceptors)
specifier|protected
specifier|final
name|EndpointInvocationChain
name|createEndpointInvocationChain
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|,
name|Object
name|endpoint
parameter_list|,
name|EndpointInterceptor
index|[]
name|interceptors
parameter_list|)
block|{
for|for
control|(
name|EndpointMappingKey
name|key
range|:
name|endpoints
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|EndpointMappingType
operator|.
name|SOAP_ACTION
operator|.
name|equals
argument_list|(
name|key
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|Object
name|messageKey
init|=
name|getSoapAction
argument_list|(
name|messageContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageKey
operator|!=
literal|null
operator|&&
name|key
operator|.
name|getLookupKey
argument_list|()
operator|.
name|equals
argument_list|(
name|messageKey
argument_list|)
condition|)
block|{
return|return
operator|new
name|SoapEndpointInvocationChain
argument_list|(
name|endpoint
argument_list|,
name|interceptors
argument_list|,
name|actorsOrRoles
argument_list|,
name|isUltimateReceiver
argument_list|)
return|;
block|}
block|}
block|}
return|return
name|super
operator|.
name|createEndpointInvocationChain
argument_list|(
name|messageContext
argument_list|,
name|endpoint
argument_list|,
name|interceptors
argument_list|)
return|;
block|}
DECL|method|getSoapAction (MessageContext messageContext)
specifier|private
name|String
name|getSoapAction
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|)
block|{
if|if
condition|(
name|messageContext
operator|.
name|getRequest
argument_list|()
operator|instanceof
name|SoapMessage
condition|)
block|{
name|SoapMessage
name|request
init|=
operator|(
name|SoapMessage
operator|)
name|messageContext
operator|.
name|getRequest
argument_list|()
decl_stmt|;
name|String
name|soapAction
init|=
name|request
operator|.
name|getSoapAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|hasLength
argument_list|(
name|soapAction
argument_list|)
operator|&&
name|soapAction
operator|.
name|startsWith
argument_list|(
name|DOUBLE_QUOTE
argument_list|)
operator|&&
name|soapAction
operator|.
name|endsWith
argument_list|(
name|DOUBLE_QUOTE
argument_list|)
condition|)
block|{
return|return
name|soapAction
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|soapAction
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
return|return
name|soapAction
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getUri ()
specifier|private
name|String
name|getUri
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|WebServiceConnection
name|webServiceConnection
init|=
name|getWeServiceConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|webServiceConnection
operator|!=
literal|null
condition|)
block|{
return|return
name|webServiceConnection
operator|.
name|getUri
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getUriPath ()
specifier|private
name|String
name|getUriPath
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|WebServiceConnection
name|webServiceConnection
init|=
name|getWeServiceConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|webServiceConnection
operator|!=
literal|null
condition|)
block|{
return|return
name|webServiceConnection
operator|.
name|getUri
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getWeServiceConnection ()
specifier|private
name|WebServiceConnection
name|getWeServiceConnection
parameter_list|()
block|{
name|TransportContext
name|transportContext
init|=
name|TransportContextHolder
operator|.
name|getTransportContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|transportContext
operator|!=
literal|null
condition|)
block|{
name|WebServiceConnection
name|webServiceConnection
init|=
name|transportContext
operator|.
name|getConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|webServiceConnection
operator|!=
literal|null
condition|)
block|{
return|return
name|webServiceConnection
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getRootQName (MessageContext messageContext)
specifier|private
name|String
name|getRootQName
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|XMLStreamException
block|{
name|QName
name|qName
init|=
name|PayloadRootUtils
operator|.
name|getPayloadRootQName
argument_list|(
name|messageContext
operator|.
name|getRequest
argument_list|()
operator|.
name|getPayloadSource
argument_list|()
argument_list|,
name|transformerFactory
argument_list|)
decl_stmt|;
return|return
name|qName
operator|!=
literal|null
condition|?
name|qName
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|getXPathResult (MessageContext messageContext, XPathExpression expression)
specifier|private
name|String
name|getXPathResult
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|,
name|XPathExpression
name|expression
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|XMLStreamException
throws|,
name|ParserConfigurationException
throws|,
name|IOException
throws|,
name|SAXException
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|Node
name|domNode
init|=
name|xmlConverter
operator|.
name|toDOMNode
argument_list|(
name|messageContext
operator|.
name|getRequest
argument_list|()
operator|.
name|getPayloadSource
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|domNode
operator|!=
literal|null
condition|)
block|{
return|return
name|expression
operator|.
name|evaluateAsString
argument_list|(
name|domNode
operator|.
name|getFirstChild
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Used by Camel Spring Web Services endpoint to register consumers      *       * @param key unique consumer key      * @param endpoint consumer      */
DECL|method|addConsumer (EndpointMappingKey key, MessageEndpoint endpoint)
specifier|public
name|void
name|addConsumer
parameter_list|(
name|EndpointMappingKey
name|key
parameter_list|,
name|MessageEndpoint
name|endpoint
parameter_list|)
block|{
name|endpoints
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|/**      * Used by Camel Spring Web Services endpoint to unregister consumers      *       * @param key unique consumer key      */
DECL|method|removeConsumer (Object key)
specifier|public
name|void
name|removeConsumer
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|endpoints
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the configured TransformerFactory      *       * @return instance of TransformerFactory      */
DECL|method|getTransformerFactory ()
specifier|public
name|TransformerFactory
name|getTransformerFactory
parameter_list|()
block|{
return|return
name|transformerFactory
return|;
block|}
comment|/**      * Optional setter to override default TransformerFactory      *       * @param transformerFactory non-default TransformerFactory      */
DECL|method|setTransformerFactory (TransformerFactory transformerFactory)
specifier|public
name|void
name|setTransformerFactory
parameter_list|(
name|TransformerFactory
name|transformerFactory
parameter_list|)
block|{
name|this
operator|.
name|transformerFactory
operator|=
name|transformerFactory
expr_stmt|;
block|}
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|xmlConverter
operator|=
operator|new
name|XmlConverter
argument_list|()
expr_stmt|;
if|if
condition|(
name|transformerFactory
operator|!=
literal|null
condition|)
block|{
name|xmlConverter
operator|.
name|setTransformerFactory
argument_list|(
name|transformerFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|transformerFactory
operator|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @see {@link AbstractAddressingEndpointMapping}      * @param actorOrRole      */
DECL|method|setActorOrRole (String actorOrRole)
specifier|public
specifier|final
name|void
name|setActorOrRole
parameter_list|(
name|String
name|actorOrRole
parameter_list|)
block|{
name|Assert
operator|.
name|notNull
argument_list|(
name|actorOrRole
argument_list|,
literal|"actorOrRole must not be null"
argument_list|)
expr_stmt|;
name|actorsOrRoles
operator|=
operator|new
name|String
index|[]
block|{
name|actorOrRole
block|}
expr_stmt|;
block|}
comment|/**      * @see {@link AbstractAddressingEndpointMapping}      * @param actorsOrRoles      */
DECL|method|setActorsOrRoles (String[] actorsOrRoles)
specifier|public
specifier|final
name|void
name|setActorsOrRoles
parameter_list|(
name|String
index|[]
name|actorsOrRoles
parameter_list|)
block|{
name|Assert
operator|.
name|notEmpty
argument_list|(
name|actorsOrRoles
argument_list|,
literal|"actorsOrRoles must not be empty"
argument_list|)
expr_stmt|;
name|this
operator|.
name|actorsOrRoles
operator|=
name|actorsOrRoles
expr_stmt|;
block|}
comment|/**      * @see {@link AbstractAddressingEndpointMapping}      * @param ultimateReceiver      */
DECL|method|setUltimateReceiver (boolean ultimateReceiver)
specifier|public
specifier|final
name|void
name|setUltimateReceiver
parameter_list|(
name|boolean
name|ultimateReceiver
parameter_list|)
block|{
name|this
operator|.
name|isUltimateReceiver
operator|=
name|ultimateReceiver
expr_stmt|;
block|}
block|}
end_class

end_unit

