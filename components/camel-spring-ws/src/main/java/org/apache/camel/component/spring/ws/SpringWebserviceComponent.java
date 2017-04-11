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
name|java
operator|.
name|util
operator|.
name|Map
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
name|SSLContextParametersAware
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
name|filter
operator|.
name|impl
operator|.
name|BasicMessageFilter
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|UriEndpointComponent
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
name|util
operator|.
name|CamelContextHelper
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
name|EndpointHelper
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
name|UnsafeUriCharactersEncoder
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
name|xml
operator|.
name|xpath
operator|.
name|XPathExpression
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
name|XPathExpressionFactory
import|;
end_import

begin_comment
comment|/**  * Apache Camel component for working with Spring Web Services (a.k.a Spring-WS).  */
end_comment

begin_class
DECL|class|SpringWebserviceComponent
specifier|public
class|class
name|SpringWebserviceComponent
extends|extends
name|UriEndpointComponent
implements|implements
name|SSLContextParametersAware
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
name|SpringWebserviceComponent
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useGlobalSslContextParameters
specifier|private
name|boolean
name|useGlobalSslContextParameters
decl_stmt|;
DECL|method|SpringWebserviceComponent ()
specifier|public
name|SpringWebserviceComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SpringWebserviceEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|SpringWebserviceComponent (CamelContext context)
specifier|public
name|SpringWebserviceComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|SpringWebserviceEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|preProcessUri (String uri)
specifier|protected
name|String
name|preProcessUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|String
index|[]
name|u
init|=
name|uri
operator|.
name|split
argument_list|(
literal|"\\?"
argument_list|)
decl_stmt|;
return|return
name|u
index|[
literal|0
index|]
operator|.
name|replaceAll
argument_list|(
literal|"%7B"
argument_list|,
literal|"("
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"%7D"
argument_list|,
literal|")"
argument_list|)
operator|+
operator|(
name|u
operator|.
name|length
operator|>
literal|1
condition|?
literal|"?"
operator|+
name|u
index|[
literal|1
index|]
else|:
literal|""
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|SpringWebserviceConfiguration
name|configuration
init|=
operator|new
name|SpringWebserviceConfiguration
argument_list|()
decl_stmt|;
name|addConsumerConfiguration
argument_list|(
name|remaining
argument_list|,
name|parameters
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|addXmlConverterToConfiguration
argument_list|(
name|parameters
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|configureProducerConfiguration
argument_list|(
name|remaining
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|configureMessageFilter
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|configuration
operator|.
name|setSslContextParameters
argument_list|(
name|retrieveGlobalSslContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|SpringWebserviceEndpoint
argument_list|(
name|this
argument_list|,
name|uri
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|addConsumerConfiguration (String remaining, Map<String, Object> parameters, SpringWebserviceConfiguration configuration)
specifier|private
name|void
name|addConsumerConfiguration
parameter_list|(
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|SpringWebserviceConfiguration
name|configuration
parameter_list|)
block|{
name|EndpointMappingType
name|type
init|=
name|EndpointMappingType
operator|.
name|getTypeFromUriPrefix
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Building Spring Web Services consumer of type "
operator|+
name|type
argument_list|)
expr_stmt|;
name|String
name|lookupKey
init|=
name|getLookupKey
argument_list|(
name|remaining
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|EndpointMappingType
operator|.
name|BEANNAME
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|addEndpointDispatcherToConfiguration
argument_list|(
name|configuration
argument_list|,
name|lookupKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addEndpointMappingToConfiguration
argument_list|(
name|parameters
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
name|XPathExpression
name|xPathExpression
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|EndpointMappingType
operator|.
name|XPATHRESULT
argument_list|)
condition|)
block|{
name|String
name|expression
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"expression"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|xPathExpression
operator|=
name|createXPathExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|.
name|setEndpointMappingKey
argument_list|(
operator|new
name|EndpointMappingKey
argument_list|(
name|type
argument_list|,
name|lookupKey
argument_list|,
name|xPathExpression
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureProducerConfiguration (String remaining, SpringWebserviceConfiguration configuration)
specifier|private
name|void
name|configureProducerConfiguration
parameter_list|(
name|String
name|remaining
parameter_list|,
name|SpringWebserviceConfiguration
name|configuration
parameter_list|)
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|configuration
operator|.
name|getEndpointMapping
argument_list|()
operator|==
literal|null
operator|&&
name|configuration
operator|.
name|getEndpointDispatcher
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Building Spring Web Services producer"
argument_list|)
expr_stmt|;
name|URI
name|webServiceEndpointUri
init|=
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|remaining
argument_list|)
argument_list|)
decl_stmt|;
comment|// Obtain a WebServiceTemplate from the registry when specified by
comment|// an option on the component, else create a new template with
comment|// Spring-WS defaults
name|WebServiceTemplate
name|webServiceTemplate
init|=
name|configuration
operator|.
name|getWebServiceTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|webServiceTemplate
operator|==
literal|null
condition|)
block|{
name|webServiceTemplate
operator|=
operator|new
name|WebServiceTemplate
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setWebServiceTemplate
argument_list|(
name|webServiceTemplate
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|webServiceTemplate
operator|.
name|getDefaultUri
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|uri
init|=
name|webServiceEndpointUri
operator|.
name|toString
argument_list|()
decl_stmt|;
name|webServiceTemplate
operator|.
name|setDefaultUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setWebServiceEndpointUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMessageSender
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|webServiceTemplate
operator|.
name|setMessageSender
argument_list|(
name|configuration
operator|.
name|getMessageSender
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMessageFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|webServiceTemplate
operator|.
name|setMessageFactory
argument_list|(
name|configuration
operator|.
name|getMessageFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getLookupKey (String remaining, EndpointMappingType type)
specifier|private
name|String
name|getLookupKey
parameter_list|(
name|String
name|remaining
parameter_list|,
name|EndpointMappingType
name|type
parameter_list|)
block|{
name|String
name|lookupKey
init|=
name|remaining
operator|.
name|substring
argument_list|(
name|type
operator|.
name|getPrefix
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|lookupKey
operator|=
name|lookupKey
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|?
name|lookupKey
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
else|:
name|lookupKey
expr_stmt|;
return|return
name|SpringWebserviceConfiguration
operator|.
name|decode
argument_list|(
name|lookupKey
argument_list|)
return|;
block|}
DECL|method|createXPathExpression (String xpathExpression)
specifier|private
name|XPathExpression
name|createXPathExpression
parameter_list|(
name|String
name|xpathExpression
parameter_list|)
block|{
if|if
condition|(
name|xpathExpression
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Expression parameter is required when using XPath endpoint mapping"
argument_list|)
throw|;
block|}
name|XPathExpression
name|expression
init|=
name|XPathExpressionFactory
operator|.
name|createXPathExpression
argument_list|(
name|xpathExpression
argument_list|)
decl_stmt|;
return|return
name|expression
return|;
block|}
DECL|method|addEndpointMappingToConfiguration (Map<String, Object> parameters, SpringWebserviceConfiguration configuration)
specifier|private
name|void
name|addEndpointMappingToConfiguration
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|SpringWebserviceConfiguration
name|configuration
parameter_list|)
block|{
comment|// Obtain generic CamelSpringWSEndpointMapping from registry
name|CamelSpringWSEndpointMapping
name|endpointMapping
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"endpointMapping"
argument_list|,
name|CamelSpringWSEndpointMapping
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointMapping
operator|==
literal|null
operator|&&
name|configuration
operator|.
name|getEndpointDispatcher
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No instance of CamelSpringWSEndpointMapping found in Spring ApplicationContext."
operator|+
literal|" This bean is required for Spring-WS consumer support (unless the 'spring-ws:beanname:' URI scheme is used)"
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|setEndpointMapping
argument_list|(
name|endpointMapping
argument_list|)
expr_stmt|;
block|}
DECL|method|addEndpointDispatcherToConfiguration (SpringWebserviceConfiguration configuration, String lookupKey)
specifier|private
name|void
name|addEndpointDispatcherToConfiguration
parameter_list|(
name|SpringWebserviceConfiguration
name|configuration
parameter_list|,
name|String
name|lookupKey
parameter_list|)
block|{
comment|// Obtain CamelEndpointDispatcher with the given name from registry
name|CamelEndpointDispatcher
name|endpoint
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|lookupKey
argument_list|,
name|CamelEndpointDispatcher
operator|.
name|class
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setEndpointDispatcher
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|addXmlConverterToConfiguration (Map<String, Object> parameters, SpringWebserviceConfiguration configuration)
specifier|private
name|void
name|addXmlConverterToConfiguration
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|SpringWebserviceConfiguration
name|configuration
parameter_list|)
block|{
name|XmlConverter
name|xmlConverter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|TransformerFactory
name|transformerFactory
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"transformerFactory"
argument_list|,
name|TransformerFactory
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
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
name|configuration
operator|.
name|setXmlConverter
argument_list|(
name|xmlConverter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Configures the messageFilter's factory. The factory is looked up in the endpoint's URI and then in the Spring's context.      * The bean search mechanism looks for a bean with the name messageFilter.      * The endpoint's URI search mechanism looks for the URI's key parameter name messageFilter, for instance like this:      * spring-ws:http://yourdomain.com?messageFilter=<beanName>      */
DECL|method|configureMessageFilter (SpringWebserviceConfiguration configuration)
specifier|private
name|void
name|configureMessageFilter
parameter_list|(
name|SpringWebserviceConfiguration
name|configuration
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getMessageFilter
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// try to lookup a global filter to use
specifier|final
name|MessageFilter
name|globalMessageFilter
init|=
name|EndpointHelper
operator|.
name|resolveReferenceParameter
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"messageFilter"
argument_list|,
name|MessageFilter
operator|.
name|class
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|globalMessageFilter
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setMessageFilter
argument_list|(
name|globalMessageFilter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use basic as fallback
name|configuration
operator|.
name|setMessageFilter
argument_list|(
operator|new
name|BasicMessageFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|this
operator|.
name|useGlobalSslContextParameters
return|;
block|}
comment|/**      * Enable usage of global SSL context parameters.      */
annotation|@
name|Override
DECL|method|setUseGlobalSslContextParameters (boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
block|}
end_class

end_unit

