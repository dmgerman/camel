begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
operator|.
name|springboot
package|;
end_package

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
name|annotation
operator|.
name|Generated
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
name|rest
operator|.
name|RestBindingMode
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
name|rest
operator|.
name|RestHostNameResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * To configure rest  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.rest"
argument_list|)
DECL|class|RestConfigurationDefinitionProperties
specifier|public
class|class
name|RestConfigurationDefinitionProperties
block|{
comment|/**      * The Camel Rest component to use for the REST transport (consumer), such      * as restlet, spark-rest. If no component has been explicit configured,      * then Camel will lookup if there is a Camel component that integrates with      * the Rest DSL, or if a org.apache.camel.spi.RestConsumerFactory is      * registered in the registry. If either one is found, then that is being      * used.      */
DECL|field|component
specifier|private
name|String
name|component
decl_stmt|;
comment|/**      * The name of the Camel component to use as the REST API (such as swagger)      */
DECL|field|apiComponent
specifier|private
name|String
name|apiComponent
init|=
literal|"swagger"
decl_stmt|;
comment|/**      * Sets the name of the Camel component to use as the REST producer      */
DECL|field|producerComponent
specifier|private
name|String
name|producerComponent
decl_stmt|;
comment|/**      * The scheme to use for exposing the REST service. Usually http or https is      * supported. The default value is http      */
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
comment|/**      * The hostname to use for exposing the REST service.      */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * To use an specific hostname for the API documentation (eg swagger) This      * can be used to override the generated host with this configured hostname      */
DECL|field|apiHost
specifier|private
name|String
name|apiHost
decl_stmt|;
comment|/**      * The port number to use for exposing the REST service. Notice if you use      * servlet component then the port number configured here does not apply, as      * the port number in use is the actual port number the servlet component is      * using. eg if using Apache Tomcat its the tomcat http port, if using      * Apache Karaf its the HTTP service in Karaf that uses port 8181 by default      * etc. Though in those situations setting the port number here, allows      * tooling and JMX to know the port number, so its recommended to set the      * port number to the number that the servlet engine uses.      */
DECL|field|port
specifier|private
name|String
name|port
decl_stmt|;
comment|/**      * Sets the location of the api document (swagger api) the REST producer      * will use to validate the REST uri and query parameters are valid      * accordingly to the api document. This requires adding camel-swagger-java      * to the classpath, and any miss configuration will let Camel fail on      * startup and report the error(s). The location of the api document is      * loaded from classpath by default, but you can use file: or http: to refer      * to resources to load from file or http url.      */
DECL|field|producerApiDoc
specifier|private
name|String
name|producerApiDoc
decl_stmt|;
comment|/**      * Sets a leading context-path the REST services will be using. This can be      * used when using components such as camel-servlet where the deployed web      * application is deployed using a context-path. Or for components such as      * camel-jetty or camel-netty4-http that includes a HTTP server.      */
DECL|field|contextPath
specifier|private
name|String
name|contextPath
decl_stmt|;
comment|/**      * Sets a leading API context-path the REST API services will be using. This      * can be used when using components such as camel-servlet where the      * deployed web application is deployed using a context-path.      */
DECL|field|apiContextPath
specifier|private
name|String
name|apiContextPath
decl_stmt|;
comment|/**      * Sets the route id to use for the route that services the REST API. The      * route will by default use an auto assigned route id.      */
DECL|field|apiContextRouteId
specifier|private
name|String
name|apiContextRouteId
decl_stmt|;
comment|/**      * Sets an CamelContext id pattern to only allow Rest APIs from rest      * services within CamelContext's which name matches the pattern. The      * pattern #name# refers to the CamelContext name, to match on the current      * CamelContext only. For any other value, the pattern uses the rules from      * {link EndpointHelper#matchPattern(String, String)}      */
DECL|field|apiContextIdPattern
specifier|private
name|String
name|apiContextIdPattern
decl_stmt|;
comment|/**      * Sets whether listing of all available CamelContext's with REST services      * in the JVM is enabled. If enabled it allows to discover these contexts,      * if false then only the current CamelContext is in use.      */
DECL|field|apiContextListing
specifier|private
name|Boolean
name|apiContextListing
init|=
literal|false
decl_stmt|;
comment|/**      * Whether vendor extension is enabled in the Rest APIs. If enabled then      * Camel will include additional information as vendor extension (eg keys      * starting with x-) such as route ids, class names etc. Not all 3rd party      * API gateways and tools supports vendor-extensions when importing your API      * docs.      */
DECL|field|apiVendorExtension
specifier|private
name|Boolean
name|apiVendorExtension
init|=
literal|false
decl_stmt|;
comment|/**      * If no hostname has been explicit configured, then this resolver is used      * to compute the hostname the REST service will be using.      */
DECL|field|hostNameResolver
specifier|private
name|RestHostNameResolver
name|hostNameResolver
decl_stmt|;
comment|/**      * Sets the binding mode to use. The default value is off      */
DECL|field|bindingMode
specifier|private
name|RestBindingMode
name|bindingMode
decl_stmt|;
comment|/**      * Whether to skip binding on output if there is a custom HTTP error code      * header. This allows to build custom error messages that do not bind to      * json / xml etc, as success messages otherwise will do.      */
DECL|field|skipBindingOnErrorCode
specifier|private
name|Boolean
name|skipBindingOnErrorCode
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to enable validation of the client request to check whether the      * Content-Type and Accept headers from the client is supported by the      * Rest-DSL configuration of its consumes/produces settings. This can be      * turned on, to enable this check. In case of validation error, then HTTP      * Status codes 415 or 406 is returned. The default value is false.      */
DECL|field|clientRequestValidation
specifier|private
name|Boolean
name|clientRequestValidation
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to enable CORS headers in the HTTP response. The default value is      * false.      */
DECL|field|enableCors
specifier|private
name|Boolean
name|enableCors
init|=
literal|false
decl_stmt|;
comment|/**      * Name of specific json data format to use. By default json-jackson will be      * used. Important: This option is only for setting a custom name of the      * data format, not to refer to an existing data format instance.      */
DECL|field|jsonDataFormat
specifier|private
name|String
name|jsonDataFormat
decl_stmt|;
comment|/**      * Name of specific XML data format to use. By default jaxb will be used.      * Important: This option is only for setting a custom name of the data      * format, not to refer to an existing data format instance.      */
DECL|field|xmlDataFormat
specifier|private
name|String
name|xmlDataFormat
decl_stmt|;
comment|/**      * Allows to configure as many additional properties for the rest component      * in use.      */
DECL|field|componentProperty
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|componentProperty
decl_stmt|;
comment|/**      * Allows to configure as many additional properties for the rest endpoint      * in use.      */
DECL|field|endpointProperty
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|endpointProperty
decl_stmt|;
comment|/**      * Allows to configure as many additional properties for the rest consumer      * in use.      */
DECL|field|consumerProperty
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|consumerProperty
decl_stmt|;
comment|/**      * Allows to configure as many additional properties for the data formats in      * use. For example set property prettyPrint to true to have json outputted      * in pretty mode. The properties can be prefixed to denote the option is      * only for either JSON or XML and for either the IN or the OUT. The      * prefixes are: json.in. json.out. xml.in. xml.out. For example a key with      * value xml.out.mustBeJAXBElement is only for the XML data format for the      * outgoing. A key without a prefix is a common key for all situations.      */
DECL|field|dataFormatProperty
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dataFormatProperty
decl_stmt|;
comment|/**      * Allows to configure as many additional properties for the api      * documentation (swagger). For example set property api.title to my cool      * stuff      */
DECL|field|apiProperty
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|apiProperty
decl_stmt|;
comment|/**      * Allows to configure custom CORS headers.      */
DECL|field|corsHeaders
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|corsHeaders
decl_stmt|;
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
DECL|method|setComponent (String component)
specifier|public
name|void
name|setComponent
parameter_list|(
name|String
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
DECL|method|getApiComponent ()
specifier|public
name|String
name|getApiComponent
parameter_list|()
block|{
return|return
name|apiComponent
return|;
block|}
DECL|method|setApiComponent (String apiComponent)
specifier|public
name|void
name|setApiComponent
parameter_list|(
name|String
name|apiComponent
parameter_list|)
block|{
name|this
operator|.
name|apiComponent
operator|=
name|apiComponent
expr_stmt|;
block|}
DECL|method|getProducerComponent ()
specifier|public
name|String
name|getProducerComponent
parameter_list|()
block|{
return|return
name|producerComponent
return|;
block|}
DECL|method|setProducerComponent (String producerComponent)
specifier|public
name|void
name|setProducerComponent
parameter_list|(
name|String
name|producerComponent
parameter_list|)
block|{
name|this
operator|.
name|producerComponent
operator|=
name|producerComponent
expr_stmt|;
block|}
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
DECL|method|setScheme (String scheme)
specifier|public
name|void
name|setScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|this
operator|.
name|scheme
operator|=
name|scheme
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getApiHost ()
specifier|public
name|String
name|getApiHost
parameter_list|()
block|{
return|return
name|apiHost
return|;
block|}
DECL|method|setApiHost (String apiHost)
specifier|public
name|void
name|setApiHost
parameter_list|(
name|String
name|apiHost
parameter_list|)
block|{
name|this
operator|.
name|apiHost
operator|=
name|apiHost
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|String
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (String port)
specifier|public
name|void
name|setPort
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getProducerApiDoc ()
specifier|public
name|String
name|getProducerApiDoc
parameter_list|()
block|{
return|return
name|producerApiDoc
return|;
block|}
DECL|method|setProducerApiDoc (String producerApiDoc)
specifier|public
name|void
name|setProducerApiDoc
parameter_list|(
name|String
name|producerApiDoc
parameter_list|)
block|{
name|this
operator|.
name|producerApiDoc
operator|=
name|producerApiDoc
expr_stmt|;
block|}
DECL|method|getContextPath ()
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|contextPath
return|;
block|}
DECL|method|setContextPath (String contextPath)
specifier|public
name|void
name|setContextPath
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|this
operator|.
name|contextPath
operator|=
name|contextPath
expr_stmt|;
block|}
DECL|method|getApiContextPath ()
specifier|public
name|String
name|getApiContextPath
parameter_list|()
block|{
return|return
name|apiContextPath
return|;
block|}
DECL|method|setApiContextPath (String apiContextPath)
specifier|public
name|void
name|setApiContextPath
parameter_list|(
name|String
name|apiContextPath
parameter_list|)
block|{
name|this
operator|.
name|apiContextPath
operator|=
name|apiContextPath
expr_stmt|;
block|}
DECL|method|getApiContextRouteId ()
specifier|public
name|String
name|getApiContextRouteId
parameter_list|()
block|{
return|return
name|apiContextRouteId
return|;
block|}
DECL|method|setApiContextRouteId (String apiContextRouteId)
specifier|public
name|void
name|setApiContextRouteId
parameter_list|(
name|String
name|apiContextRouteId
parameter_list|)
block|{
name|this
operator|.
name|apiContextRouteId
operator|=
name|apiContextRouteId
expr_stmt|;
block|}
DECL|method|getApiContextIdPattern ()
specifier|public
name|String
name|getApiContextIdPattern
parameter_list|()
block|{
return|return
name|apiContextIdPattern
return|;
block|}
DECL|method|setApiContextIdPattern (String apiContextIdPattern)
specifier|public
name|void
name|setApiContextIdPattern
parameter_list|(
name|String
name|apiContextIdPattern
parameter_list|)
block|{
name|this
operator|.
name|apiContextIdPattern
operator|=
name|apiContextIdPattern
expr_stmt|;
block|}
DECL|method|getApiContextListing ()
specifier|public
name|Boolean
name|getApiContextListing
parameter_list|()
block|{
return|return
name|apiContextListing
return|;
block|}
DECL|method|setApiContextListing (Boolean apiContextListing)
specifier|public
name|void
name|setApiContextListing
parameter_list|(
name|Boolean
name|apiContextListing
parameter_list|)
block|{
name|this
operator|.
name|apiContextListing
operator|=
name|apiContextListing
expr_stmt|;
block|}
DECL|method|getApiVendorExtension ()
specifier|public
name|Boolean
name|getApiVendorExtension
parameter_list|()
block|{
return|return
name|apiVendorExtension
return|;
block|}
DECL|method|setApiVendorExtension (Boolean apiVendorExtension)
specifier|public
name|void
name|setApiVendorExtension
parameter_list|(
name|Boolean
name|apiVendorExtension
parameter_list|)
block|{
name|this
operator|.
name|apiVendorExtension
operator|=
name|apiVendorExtension
expr_stmt|;
block|}
DECL|method|getHostNameResolver ()
specifier|public
name|RestHostNameResolver
name|getHostNameResolver
parameter_list|()
block|{
return|return
name|hostNameResolver
return|;
block|}
DECL|method|setHostNameResolver (RestHostNameResolver hostNameResolver)
specifier|public
name|void
name|setHostNameResolver
parameter_list|(
name|RestHostNameResolver
name|hostNameResolver
parameter_list|)
block|{
name|this
operator|.
name|hostNameResolver
operator|=
name|hostNameResolver
expr_stmt|;
block|}
DECL|method|getBindingMode ()
specifier|public
name|RestBindingMode
name|getBindingMode
parameter_list|()
block|{
return|return
name|bindingMode
return|;
block|}
DECL|method|setBindingMode (RestBindingMode bindingMode)
specifier|public
name|void
name|setBindingMode
parameter_list|(
name|RestBindingMode
name|bindingMode
parameter_list|)
block|{
name|this
operator|.
name|bindingMode
operator|=
name|bindingMode
expr_stmt|;
block|}
DECL|method|getSkipBindingOnErrorCode ()
specifier|public
name|Boolean
name|getSkipBindingOnErrorCode
parameter_list|()
block|{
return|return
name|skipBindingOnErrorCode
return|;
block|}
DECL|method|setSkipBindingOnErrorCode (Boolean skipBindingOnErrorCode)
specifier|public
name|void
name|setSkipBindingOnErrorCode
parameter_list|(
name|Boolean
name|skipBindingOnErrorCode
parameter_list|)
block|{
name|this
operator|.
name|skipBindingOnErrorCode
operator|=
name|skipBindingOnErrorCode
expr_stmt|;
block|}
DECL|method|getClientRequestValidation ()
specifier|public
name|Boolean
name|getClientRequestValidation
parameter_list|()
block|{
return|return
name|clientRequestValidation
return|;
block|}
DECL|method|setClientRequestValidation (Boolean clientRequestValidation)
specifier|public
name|void
name|setClientRequestValidation
parameter_list|(
name|Boolean
name|clientRequestValidation
parameter_list|)
block|{
name|this
operator|.
name|clientRequestValidation
operator|=
name|clientRequestValidation
expr_stmt|;
block|}
DECL|method|getEnableCors ()
specifier|public
name|Boolean
name|getEnableCors
parameter_list|()
block|{
return|return
name|enableCors
return|;
block|}
DECL|method|setEnableCors (Boolean enableCors)
specifier|public
name|void
name|setEnableCors
parameter_list|(
name|Boolean
name|enableCors
parameter_list|)
block|{
name|this
operator|.
name|enableCors
operator|=
name|enableCors
expr_stmt|;
block|}
DECL|method|getJsonDataFormat ()
specifier|public
name|String
name|getJsonDataFormat
parameter_list|()
block|{
return|return
name|jsonDataFormat
return|;
block|}
DECL|method|setJsonDataFormat (String jsonDataFormat)
specifier|public
name|void
name|setJsonDataFormat
parameter_list|(
name|String
name|jsonDataFormat
parameter_list|)
block|{
name|this
operator|.
name|jsonDataFormat
operator|=
name|jsonDataFormat
expr_stmt|;
block|}
DECL|method|getXmlDataFormat ()
specifier|public
name|String
name|getXmlDataFormat
parameter_list|()
block|{
return|return
name|xmlDataFormat
return|;
block|}
DECL|method|setXmlDataFormat (String xmlDataFormat)
specifier|public
name|void
name|setXmlDataFormat
parameter_list|(
name|String
name|xmlDataFormat
parameter_list|)
block|{
name|this
operator|.
name|xmlDataFormat
operator|=
name|xmlDataFormat
expr_stmt|;
block|}
DECL|method|getComponentProperty ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getComponentProperty
parameter_list|()
block|{
return|return
name|componentProperty
return|;
block|}
DECL|method|setComponentProperty (Map<String, Object> componentProperty)
specifier|public
name|void
name|setComponentProperty
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|componentProperty
parameter_list|)
block|{
name|this
operator|.
name|componentProperty
operator|=
name|componentProperty
expr_stmt|;
block|}
DECL|method|getEndpointProperty ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getEndpointProperty
parameter_list|()
block|{
return|return
name|endpointProperty
return|;
block|}
DECL|method|setEndpointProperty (Map<String, Object> endpointProperty)
specifier|public
name|void
name|setEndpointProperty
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|endpointProperty
parameter_list|)
block|{
name|this
operator|.
name|endpointProperty
operator|=
name|endpointProperty
expr_stmt|;
block|}
DECL|method|getConsumerProperty ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getConsumerProperty
parameter_list|()
block|{
return|return
name|consumerProperty
return|;
block|}
DECL|method|setConsumerProperty (Map<String, Object> consumerProperty)
specifier|public
name|void
name|setConsumerProperty
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|consumerProperty
parameter_list|)
block|{
name|this
operator|.
name|consumerProperty
operator|=
name|consumerProperty
expr_stmt|;
block|}
DECL|method|getDataFormatProperty ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getDataFormatProperty
parameter_list|()
block|{
return|return
name|dataFormatProperty
return|;
block|}
DECL|method|setDataFormatProperty (Map<String, Object> dataFormatProperty)
specifier|public
name|void
name|setDataFormatProperty
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dataFormatProperty
parameter_list|)
block|{
name|this
operator|.
name|dataFormatProperty
operator|=
name|dataFormatProperty
expr_stmt|;
block|}
DECL|method|getApiProperty ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getApiProperty
parameter_list|()
block|{
return|return
name|apiProperty
return|;
block|}
DECL|method|setApiProperty (Map<String, Object> apiProperty)
specifier|public
name|void
name|setApiProperty
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|apiProperty
parameter_list|)
block|{
name|this
operator|.
name|apiProperty
operator|=
name|apiProperty
expr_stmt|;
block|}
DECL|method|getCorsHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getCorsHeaders
parameter_list|()
block|{
return|return
name|corsHeaders
return|;
block|}
DECL|method|setCorsHeaders (Map<String, Object> corsHeaders)
specifier|public
name|void
name|setCorsHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|corsHeaders
parameter_list|)
block|{
name|this
operator|.
name|corsHeaders
operator|=
name|corsHeaders
expr_stmt|;
block|}
block|}
end_class

end_unit

