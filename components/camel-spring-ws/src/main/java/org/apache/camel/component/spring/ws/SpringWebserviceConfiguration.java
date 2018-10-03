begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws
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
operator|.
name|CamelEndpointDispatcher
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
name|bean
operator|.
name|CamelSpringWSEndpointMapping
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
name|filter
operator|.
name|MessageFilter
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
name|UriParams
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
name|jsse
operator|.
name|SSLContextParameters
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
name|WebServiceMessageFactory
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
name|client
operator|.
name|core
operator|.
name|WebServiceTemplate
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
name|addressing
operator|.
name|messageid
operator|.
name|MessageIdStrategy
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
name|addressing
operator|.
name|server
operator|.
name|annotation
operator|.
name|Action
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
name|WebServiceMessageSender
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|SpringWebserviceConfiguration
specifier|public
class|class
name|SpringWebserviceConfiguration
block|{
DECL|field|xmlConverter
specifier|private
name|XmlConverter
name|xmlConverter
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|webServiceEndpointUri
specifier|private
name|String
name|webServiceEndpointUri
decl_stmt|;
comment|/* Common configuration */
annotation|@
name|UriParam
DECL|field|messageFilter
specifier|private
name|MessageFilter
name|messageFilter
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/* Producer configuration */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|webServiceTemplate
specifier|private
name|WebServiceTemplate
name|webServiceTemplate
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|messageSender
specifier|private
name|WebServiceMessageSender
name|messageSender
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|messageFactory
specifier|private
name|WebServiceMessageFactory
name|messageFactory
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|soapAction
specifier|private
name|String
name|soapAction
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|wsAddressingAction
specifier|private
name|URI
name|wsAddressingAction
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|outputAction
specifier|private
name|URI
name|outputAction
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|faultAction
specifier|private
name|URI
name|faultAction
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|faultTo
specifier|private
name|URI
name|faultTo
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|replyTo
specifier|private
name|URI
name|replyTo
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|messageIdStrategy
specifier|private
name|MessageIdStrategy
name|messageIdStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|allowResponseHeaderOverride
specifier|private
name|boolean
name|allowResponseHeaderOverride
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|allowResponseAttachmentOverride
specifier|private
name|boolean
name|allowResponseAttachmentOverride
decl_stmt|;
comment|/* Consumer configuration */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|endpointMappingKey
specifier|private
name|EndpointMappingKey
name|endpointMappingKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|endpointMapping
specifier|private
name|CamelSpringWSEndpointMapping
name|endpointMapping
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|endpointDispatcher
specifier|private
name|CamelEndpointDispatcher
name|endpointDispatcher
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|expression
specifier|private
name|String
name|expression
decl_stmt|;
DECL|method|getWebServiceTemplate ()
specifier|public
name|WebServiceTemplate
name|getWebServiceTemplate
parameter_list|()
block|{
return|return
name|webServiceTemplate
return|;
block|}
comment|/**      * Option to provide a custom WebServiceTemplate.      * This allows for full control over client-side web services handling; like adding a custom interceptor      * or specifying a fault resolver, message sender or message factory.      */
DECL|method|setWebServiceTemplate (WebServiceTemplate webServiceTemplate)
specifier|public
name|void
name|setWebServiceTemplate
parameter_list|(
name|WebServiceTemplate
name|webServiceTemplate
parameter_list|)
block|{
name|this
operator|.
name|webServiceTemplate
operator|=
name|webServiceTemplate
expr_stmt|;
block|}
DECL|method|getMessageFactory ()
specifier|public
name|WebServiceMessageFactory
name|getMessageFactory
parameter_list|()
block|{
return|return
name|messageFactory
return|;
block|}
comment|/**      * Option to provide a custom WebServiceMessageFactory. For example when you want Apache Axiom to handle web service messages instead of SAAJ.      */
DECL|method|setMessageFactory (WebServiceMessageFactory messageFactory)
specifier|public
name|void
name|setMessageFactory
parameter_list|(
name|WebServiceMessageFactory
name|messageFactory
parameter_list|)
block|{
name|this
operator|.
name|messageFactory
operator|=
name|messageFactory
expr_stmt|;
block|}
DECL|method|getWebServiceEndpointUri ()
specifier|public
name|String
name|getWebServiceEndpointUri
parameter_list|()
block|{
return|return
name|webServiceEndpointUri
return|;
block|}
comment|/**      * The default Web Service endpoint uri to use for the producer.      */
DECL|method|setWebServiceEndpointUri (String webServiceEndpointUri)
specifier|public
name|void
name|setWebServiceEndpointUri
parameter_list|(
name|String
name|webServiceEndpointUri
parameter_list|)
block|{
name|this
operator|.
name|webServiceEndpointUri
operator|=
name|webServiceEndpointUri
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
comment|/**      * The XPath expression to use when option type=xpathresult. Then this option is required to be configured.      */
DECL|method|setExpression (String expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getSoapAction ()
specifier|public
name|String
name|getSoapAction
parameter_list|()
block|{
return|return
name|soapAction
return|;
block|}
comment|/**      * SOAP action to include inside a SOAP request when accessing remote web services      */
DECL|method|setSoapAction (String soapAction)
specifier|public
name|void
name|setSoapAction
parameter_list|(
name|String
name|soapAction
parameter_list|)
block|{
name|this
operator|.
name|soapAction
operator|=
name|soapAction
expr_stmt|;
block|}
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
if|if
condition|(
name|endpointMappingKey
operator|!=
literal|null
condition|)
block|{
comment|// only for consumers, use lookup key as endpoint uri/key
return|return
name|encode
argument_list|(
name|endpointMappingKey
operator|.
name|getLookupKey
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|webServiceTemplate
operator|!=
literal|null
condition|)
block|{
return|return
name|webServiceTemplate
operator|.
name|getDefaultUri
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getWsAddressingAction ()
specifier|public
name|URI
name|getWsAddressingAction
parameter_list|()
block|{
return|return
name|wsAddressingAction
return|;
block|}
comment|/**      * WS-Addressing 1.0 action header to include when accessing web services.      * The To header is set to the address of the web service as specified in the endpoint URI (default Spring-WS behavior).      */
DECL|method|setWsAddressingAction (URI wsAddressingAction)
specifier|public
name|void
name|setWsAddressingAction
parameter_list|(
name|URI
name|wsAddressingAction
parameter_list|)
block|{
name|this
operator|.
name|wsAddressingAction
operator|=
name|wsAddressingAction
expr_stmt|;
block|}
DECL|method|setWsAddressingAction (String wsAddressingAction)
specifier|public
name|void
name|setWsAddressingAction
parameter_list|(
name|String
name|wsAddressingAction
parameter_list|)
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|hasText
argument_list|(
name|wsAddressingAction
argument_list|)
condition|)
block|{
name|setWsAddressingAction
argument_list|(
operator|new
name|URI
argument_list|(
name|wsAddressingAction
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Sets the socket read timeout (in milliseconds) while invoking a webservice using the producer,      * see URLConnection.setReadTimeout() and CommonsHttpMessageSender.setReadTimeout().      * This option works when using the built-in message sender implementations:      * CommonsHttpMessageSender and HttpUrlConnectionMessageSender.      * One of these implementations will be used by default for HTTP based services unless you customize the      * Spring WS configuration options supplied to the component.      * If you are using a non-standard sender, it is assumed that you will handle your own timeout configuration.      * The built-in message sender HttpComponentsMessageSender is considered instead of CommonsHttpMessageSender      * which has been deprecated, see HttpComponentsMessageSender.setReadTimeout().      */
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getEndpointMapping ()
specifier|public
name|CamelSpringWSEndpointMapping
name|getEndpointMapping
parameter_list|()
block|{
return|return
name|endpointMapping
return|;
block|}
comment|/**      * Reference to an instance of org.apache.camel.component.spring.ws.bean.CamelEndpointMapping in the Registry/ApplicationContext.      * Only one bean is required in the registry to serve all Camel/Spring-WS endpoints.      * This bean is auto-discovered by the MessageDispatcher and used to map requests to Camel endpoints based      * on characteristics specified on the endpoint (like root QName, SOAP action, etc)      */
DECL|method|setEndpointMapping (CamelSpringWSEndpointMapping endpointMapping)
specifier|public
name|void
name|setEndpointMapping
parameter_list|(
name|CamelSpringWSEndpointMapping
name|endpointMapping
parameter_list|)
block|{
name|this
operator|.
name|endpointMapping
operator|=
name|endpointMapping
expr_stmt|;
block|}
DECL|method|getEndpointMappingKey ()
specifier|public
name|EndpointMappingKey
name|getEndpointMappingKey
parameter_list|()
block|{
return|return
name|endpointMappingKey
return|;
block|}
DECL|method|setEndpointMappingKey (EndpointMappingKey endpointMappingKey)
specifier|public
name|void
name|setEndpointMappingKey
parameter_list|(
name|EndpointMappingKey
name|endpointMappingKey
parameter_list|)
block|{
name|this
operator|.
name|endpointMappingKey
operator|=
name|endpointMappingKey
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * To configure security using SSLContextParameters      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getEndpointDispatcher ()
specifier|public
name|CamelEndpointDispatcher
name|getEndpointDispatcher
parameter_list|()
block|{
return|return
name|endpointDispatcher
return|;
block|}
comment|/**      * Spring {@link org.springframework.ws.server.endpoint.MessageEndpoint} for dispatching messages received by Spring-WS to a Camel endpoint,      * to integrate with existing (legacy) endpoint mappings like PayloadRootQNameEndpointMapping, SoapActionEndpointMapping, etc.      */
DECL|method|setEndpointDispatcher (CamelEndpointDispatcher endpointDispatcher)
specifier|public
name|void
name|setEndpointDispatcher
parameter_list|(
name|CamelEndpointDispatcher
name|endpointDispatcher
parameter_list|)
block|{
name|this
operator|.
name|endpointDispatcher
operator|=
name|endpointDispatcher
expr_stmt|;
block|}
DECL|method|getXmlConverter ()
specifier|public
name|XmlConverter
name|getXmlConverter
parameter_list|()
block|{
return|return
name|xmlConverter
return|;
block|}
DECL|method|setXmlConverter (XmlConverter xmlConverter)
specifier|public
name|void
name|setXmlConverter
parameter_list|(
name|XmlConverter
name|xmlConverter
parameter_list|)
block|{
name|this
operator|.
name|xmlConverter
operator|=
name|xmlConverter
expr_stmt|;
block|}
DECL|method|encode (String uri)
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|int
name|i
init|=
name|uri
operator|.
name|lastIndexOf
argument_list|(
literal|'}'
argument_list|)
decl_stmt|;
return|return
name|i
operator|==
operator|-
literal|1
condition|?
name|uri
else|:
operator|(
name|uri
operator|.
name|subSequence
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
operator|+
literal|")"
operator|+
name|uri
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
operator|)
operator|.
name|replaceFirst
argument_list|(
literal|"\\{"
argument_list|,
literal|"("
argument_list|)
return|;
block|}
DECL|method|decode (String uri)
specifier|public
specifier|static
name|String
name|decode
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|int
name|i
init|=
name|uri
operator|.
name|lastIndexOf
argument_list|(
literal|')'
argument_list|)
decl_stmt|;
return|return
name|i
operator|==
operator|-
literal|1
condition|?
name|uri
else|:
operator|(
name|uri
operator|.
name|subSequence
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
operator|+
literal|"}"
operator|+
name|uri
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
operator|)
operator|.
name|replaceFirst
argument_list|(
literal|"\\("
argument_list|,
literal|"{"
argument_list|)
return|;
block|}
comment|/**      * Option to provide a custom MessageFilter. For example when you want to process your headers or attachments by your own.      */
DECL|method|setMessageFilter (MessageFilter messageFilter)
specifier|public
name|void
name|setMessageFilter
parameter_list|(
name|MessageFilter
name|messageFilter
parameter_list|)
block|{
name|this
operator|.
name|messageFilter
operator|=
name|messageFilter
expr_stmt|;
block|}
DECL|method|getMessageFilter ()
specifier|public
name|MessageFilter
name|getMessageFilter
parameter_list|()
block|{
return|return
name|messageFilter
return|;
block|}
DECL|method|getOutputAction ()
specifier|public
name|URI
name|getOutputAction
parameter_list|()
block|{
return|return
name|outputAction
return|;
block|}
comment|/**      * Signifies the value for the response WS-Addressing<code>Action</code>      * header that is provided by the method.      *      * @see {@link Action}      */
DECL|method|setOutputAction (URI outputAction)
specifier|public
name|void
name|setOutputAction
parameter_list|(
name|URI
name|outputAction
parameter_list|)
block|{
name|this
operator|.
name|outputAction
operator|=
name|outputAction
expr_stmt|;
block|}
DECL|method|setOutputAction (String output)
specifier|public
name|void
name|setOutputAction
parameter_list|(
name|String
name|output
parameter_list|)
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|hasText
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|setOutputAction
argument_list|(
operator|new
name|URI
argument_list|(
name|output
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getFaultAction ()
specifier|public
name|URI
name|getFaultAction
parameter_list|()
block|{
return|return
name|faultAction
return|;
block|}
comment|/**      * Signifies the value for the faultAction response WS-Addressing      *<code>Fault Action</code> header that is provided by the method.      *      * @see {@link Action}      */
DECL|method|setFaultAction (String fault)
specifier|public
name|void
name|setFaultAction
parameter_list|(
name|String
name|fault
parameter_list|)
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|hasText
argument_list|(
name|fault
argument_list|)
condition|)
block|{
name|setFaultAction
argument_list|(
operator|new
name|URI
argument_list|(
name|fault
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Signifies the value for the faultAction response WS-Addressing      *<code>Fault Action</code> header that is provided by the method.      *      * @see {@link Action}      */
DECL|method|setFaultAction (URI fault)
specifier|public
name|void
name|setFaultAction
parameter_list|(
name|URI
name|fault
parameter_list|)
block|{
name|this
operator|.
name|faultAction
operator|=
name|fault
expr_stmt|;
block|}
DECL|method|getFaultTo ()
specifier|public
name|URI
name|getFaultTo
parameter_list|()
block|{
return|return
name|faultTo
return|;
block|}
comment|/**      * Signifies the value for the faultAction response WS-Addressing      *<code>FaultTo</code> header that is provided by the method.      *      * @see {@link Action}      */
DECL|method|setFaultTo (String faultTo)
specifier|public
name|void
name|setFaultTo
parameter_list|(
name|String
name|faultTo
parameter_list|)
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|hasText
argument_list|(
name|faultTo
argument_list|)
condition|)
block|{
name|setFaultTo
argument_list|(
operator|new
name|URI
argument_list|(
name|faultTo
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Signifies the value for the faultAction response WS-Addressing      *<code>FaultTo</code> header that is provided by the method.      *      * @see {@link Action}      */
DECL|method|setFaultTo (URI faultTo)
specifier|public
name|void
name|setFaultTo
parameter_list|(
name|URI
name|faultTo
parameter_list|)
block|{
name|this
operator|.
name|faultTo
operator|=
name|faultTo
expr_stmt|;
block|}
DECL|method|getReplyTo ()
specifier|public
name|URI
name|getReplyTo
parameter_list|()
block|{
return|return
name|replyTo
return|;
block|}
comment|/**      * Signifies the value for the replyTo response WS-Addressing      *<code>ReplyTo</code> header that is provided by the method.      *      * @see {@link Action}      */
DECL|method|setReplyTo (String replyToAction)
specifier|public
name|void
name|setReplyTo
parameter_list|(
name|String
name|replyToAction
parameter_list|)
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|hasText
argument_list|(
name|replyToAction
argument_list|)
condition|)
block|{
name|setReplyTo
argument_list|(
operator|new
name|URI
argument_list|(
name|replyToAction
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Signifies the value for the replyTo response WS-Addressing      *<code>ReplyTo</code> header that is provided by the method.      *      * @see {@link Action}      */
DECL|method|setReplyTo (URI replyToAction)
specifier|public
name|void
name|setReplyTo
parameter_list|(
name|URI
name|replyToAction
parameter_list|)
block|{
name|this
operator|.
name|replyTo
operator|=
name|replyToAction
expr_stmt|;
block|}
DECL|method|getMessageSender ()
specifier|public
name|WebServiceMessageSender
name|getMessageSender
parameter_list|()
block|{
return|return
name|messageSender
return|;
block|}
comment|/**      * Option to provide a custom WebServiceMessageSender. For example to perform authentication or use alternative transports      */
DECL|method|setMessageSender (WebServiceMessageSender messageSender)
specifier|public
name|void
name|setMessageSender
parameter_list|(
name|WebServiceMessageSender
name|messageSender
parameter_list|)
block|{
name|this
operator|.
name|messageSender
operator|=
name|messageSender
expr_stmt|;
block|}
DECL|method|getMessageIdStrategy ()
specifier|public
name|MessageIdStrategy
name|getMessageIdStrategy
parameter_list|()
block|{
return|return
name|messageIdStrategy
return|;
block|}
comment|/**      * Option to provide a custom MessageIdStrategy to control generation of unique message ids.      */
DECL|method|setMessageIdStrategy (MessageIdStrategy messageIdStrategy)
specifier|public
name|void
name|setMessageIdStrategy
parameter_list|(
name|MessageIdStrategy
name|messageIdStrategy
parameter_list|)
block|{
name|this
operator|.
name|messageIdStrategy
operator|=
name|messageIdStrategy
expr_stmt|;
block|}
DECL|method|isAllowResponseHeaderOverride ()
specifier|public
name|boolean
name|isAllowResponseHeaderOverride
parameter_list|()
block|{
return|return
name|allowResponseHeaderOverride
return|;
block|}
comment|/**      * Option to override soap response header in in/out exchange with header info from the actual service layer.      * If the invoked service appends or rewrites the soap header this option when set to true, allows the modified      * soap header to be overwritten in in/out message headers      *       * @param allowResponseHeaderOverride - true, will override header with spring-ws response message header      */
DECL|method|setAllowResponseHeaderOverride (boolean allowResponseHeaderOverride)
specifier|public
name|void
name|setAllowResponseHeaderOverride
parameter_list|(
name|boolean
name|allowResponseHeaderOverride
parameter_list|)
block|{
name|this
operator|.
name|allowResponseHeaderOverride
operator|=
name|allowResponseHeaderOverride
expr_stmt|;
block|}
DECL|method|isAllowResponseAttachmentOverride ()
specifier|public
name|boolean
name|isAllowResponseAttachmentOverride
parameter_list|()
block|{
return|return
name|allowResponseAttachmentOverride
return|;
block|}
comment|/**      * Option to override soap response attachments in in/out exchange with attachments from the actual service layer.      * If the invoked service appends or rewrites the soap attachments this option when set to true, allows the modified      * soap attachments to be overwritten in in/out message attachments      *       * @param allowResponseAttachmentOverride - true, will override attachments with spring-ws response message attachments      */
DECL|method|setAllowResponseAttachmentOverride (boolean allowResponseAttachmentOverride)
specifier|public
name|void
name|setAllowResponseAttachmentOverride
parameter_list|(
name|boolean
name|allowResponseAttachmentOverride
parameter_list|)
block|{
name|this
operator|.
name|allowResponseAttachmentOverride
operator|=
name|allowResponseAttachmentOverride
expr_stmt|;
block|}
block|}
end_class

end_unit

