begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
package|;
end_package

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
name|PatternHelper
import|;
end_import

begin_comment
comment|/**  * Global configuration for Rest DSL.  */
end_comment

begin_class
DECL|class|RestConfigurationProperties
specifier|public
class|class
name|RestConfigurationProperties
block|{
DECL|field|parent
specifier|private
specifier|final
name|MainConfigurationProperties
name|parent
decl_stmt|;
DECL|field|component
specifier|private
name|String
name|component
decl_stmt|;
DECL|field|apiComponent
specifier|private
name|String
name|apiComponent
decl_stmt|;
DECL|field|producerComponent
specifier|private
name|String
name|producerComponent
decl_stmt|;
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|apiHost
specifier|private
name|String
name|apiHost
decl_stmt|;
DECL|field|useXForwardHeaders
specifier|private
name|Boolean
name|useXForwardHeaders
decl_stmt|;
DECL|field|port
specifier|private
name|String
name|port
decl_stmt|;
DECL|field|producerApiDoc
specifier|private
name|String
name|producerApiDoc
decl_stmt|;
DECL|field|contextPath
specifier|private
name|String
name|contextPath
decl_stmt|;
DECL|field|apiContextPath
specifier|private
name|String
name|apiContextPath
decl_stmt|;
DECL|field|apiContextRouteId
specifier|private
name|String
name|apiContextRouteId
decl_stmt|;
DECL|field|apiContextIdPattern
specifier|private
name|String
name|apiContextIdPattern
decl_stmt|;
DECL|field|apiContextListing
specifier|private
name|Boolean
name|apiContextListing
decl_stmt|;
DECL|field|apiVendorExtension
specifier|private
name|Boolean
name|apiVendorExtension
decl_stmt|;
DECL|field|hostNameResolver
specifier|private
name|String
name|hostNameResolver
decl_stmt|;
DECL|field|bindingMode
specifier|private
name|String
name|bindingMode
decl_stmt|;
DECL|field|skipBindingOnErrorCode
specifier|private
name|Boolean
name|skipBindingOnErrorCode
decl_stmt|;
DECL|field|clientRequestValidation
specifier|private
name|Boolean
name|clientRequestValidation
decl_stmt|;
DECL|field|enableCORS
specifier|private
name|Boolean
name|enableCORS
decl_stmt|;
DECL|field|jsonDataFormat
specifier|private
name|String
name|jsonDataFormat
decl_stmt|;
DECL|field|xmlDataFormat
specifier|private
name|String
name|xmlDataFormat
decl_stmt|;
comment|// TODO: Do these later
comment|//    private List<RestPropertyDefinition> componentProperties = new ArrayList<>();
comment|//    private List<RestPropertyDefinition> endpointProperties = new ArrayList<>();
comment|//    private List<RestPropertyDefinition> consumerProperties = new ArrayList<>();
comment|//    private List<RestPropertyDefinition> dataFormatProperties = new ArrayList<>();
comment|//    private List<RestPropertyDefinition> apiProperties = new ArrayList<>();
comment|//    private List<RestPropertyDefinition> corsHeaders = new ArrayList<>();
DECL|method|RestConfigurationProperties (MainConfigurationProperties parent)
specifier|public
name|RestConfigurationProperties
parameter_list|(
name|MainConfigurationProperties
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
DECL|method|end ()
specifier|public
name|MainConfigurationProperties
name|end
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
comment|// getter and setters
comment|// --------------------------------------------------------------
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
comment|/**      * The Camel Rest component to use for the REST transport (consumer), such as restlet, spark-rest.      * If no component has been explicit configured, then Camel will lookup if there is a Camel component      * that integrates with the Rest DSL, or if a org.apache.camel.spi.RestConsumerFactory is registered in the registry.      * If either one is found, then that is being used.      */
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
comment|/**      * The name of the Camel component to use as the REST API (such as swagger)      */
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
comment|/**      * Sets the name of the Camel component to use as the REST producer      */
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
comment|/**      * The scheme to use for exposing the REST service. Usually http or https is supported.      *<p/>      * The default value is http      */
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
comment|/**      * The hostname to use for exposing the REST service.      */
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
comment|/**      * To use an specific hostname for the API documentation (eg swagger)      *<p/>      * This can be used to override the generated host with this configured hostname      */
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
DECL|method|getUseXForwardHeaders ()
specifier|public
name|Boolean
name|getUseXForwardHeaders
parameter_list|()
block|{
return|return
name|useXForwardHeaders
return|;
block|}
comment|/**      * Whether to use X-Forward headers for Host and related setting.      *<p/>      * The default value is true.      */
DECL|method|setUseXForwardHeaders (Boolean useXForwardHeaders)
specifier|public
name|void
name|setUseXForwardHeaders
parameter_list|(
name|Boolean
name|useXForwardHeaders
parameter_list|)
block|{
name|this
operator|.
name|useXForwardHeaders
operator|=
name|useXForwardHeaders
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
comment|/**      * The port number to use for exposing the REST service.      * Notice if you use servlet component then the port number configured here does not apply,      * as the port number in use is the actual port number the servlet component is using.      * eg if using Apache Tomcat its the tomcat http port, if using Apache Karaf its the HTTP service in Karaf      * that uses port 8181 by default etc. Though in those situations setting the port number here,      * allows tooling and JMX to know the port number, so its recommended to set the port number      * to the number that the servlet engine uses.      */
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
comment|/**      * Sets the location of the api document (swagger api) the REST producer will use      * to validate the REST uri and query parameters are valid accordingly to the api document.      * This requires adding camel-swagger-java to the classpath, and any miss configuration      * will let Camel fail on startup and report the error(s).      *<p/>      * The location of the api document is loaded from classpath by default, but you can use      *<tt>file:</tt> or<tt>http:</tt> to refer to resources to load from file or http url.      */
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
comment|/**      * Sets a leading context-path the REST services will be using.      *<p/>      * This can be used when using components such as<tt>camel-servlet</tt> where the deployed web application      * is deployed using a context-path. Or for components such as<tt>camel-jetty</tt> or<tt>camel-netty4-http</tt>      * that includes a HTTP server.      */
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
comment|/**      * Sets a leading API context-path the REST API services will be using.      *<p/>      * This can be used when using components such as<tt>camel-servlet</tt> where the deployed web application      * is deployed using a context-path.      */
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
comment|/**      * Sets the route id to use for the route that services the REST API.      *<p/>      * The route will by default use an auto assigned route id.      */
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
comment|/**      * Sets an CamelContext id pattern to only allow Rest APIs from rest services within CamelContext's which name matches the pattern.      *<p/>      * The pattern<tt>#name#</tt> refers to the CamelContext name, to match on the current CamelContext only.      * For any other value, the pattern uses the rules from {@link PatternHelper#matchPattern(String, String)}      */
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
comment|/**      * Sets whether listing of all available CamelContext's with REST services in the JVM is enabled. If enabled it allows to discover      * these contexts, if<tt>false</tt> then only the current CamelContext is in use.      */
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
comment|/**      * Whether vendor extension is enabled in the Rest APIs. If enabled then Camel will include additional information      * as vendor extension (eg keys starting with x-) such as route ids, class names etc.      * Not all 3rd party API gateways and tools supports vendor-extensions when importing your API docs.      */
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
name|String
name|getHostNameResolver
parameter_list|()
block|{
return|return
name|hostNameResolver
return|;
block|}
comment|/**      * If no hostname has been explicit configured, then this resolver is used to compute the hostname the REST service will be using.      * The possible values are: allLocalIp, localIp, localHostName      */
DECL|method|setHostNameResolver (String hostNameResolver)
specifier|public
name|void
name|setHostNameResolver
parameter_list|(
name|String
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
name|String
name|getBindingMode
parameter_list|()
block|{
return|return
name|bindingMode
return|;
block|}
comment|/**      * Sets the binding mode to use.      *<p/>      * The possible values are: auto, off, json, xml, json_xml      * The default value is off      */
DECL|method|setBindingMode (String bindingMode)
specifier|public
name|void
name|setBindingMode
parameter_list|(
name|String
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
comment|/**      * Whether to skip binding on output if there is a custom HTTP error code header.      * This allows to build custom error messages that do not bind to json / xml etc, as success messages otherwise will do.      */
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
comment|/**      * Whether to enable validation of the client request to check whether the Content-Type and Accept headers from      * the client is supported by the Rest-DSL configuration of its consumes/produces settings.      *<p/>      * This can be turned on, to enable this check. In case of validation error, then HTTP Status codes 415 or 406 is returned.      *<p/>      * The default value is false.      */
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
DECL|method|getEnableCORS ()
specifier|public
name|Boolean
name|getEnableCORS
parameter_list|()
block|{
return|return
name|enableCORS
return|;
block|}
comment|/**      * Whether to enable CORS headers in the HTTP response.      *<p/>      * The default value is false.      */
DECL|method|setEnableCORS (Boolean enableCORS)
specifier|public
name|void
name|setEnableCORS
parameter_list|(
name|Boolean
name|enableCORS
parameter_list|)
block|{
name|this
operator|.
name|enableCORS
operator|=
name|enableCORS
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
comment|/**      * Name of specific json data format to use.      * By default json-jackson will be used.      * Important: This option is only for setting a custom name of the data format, not to refer to an existing data format instance.      */
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
comment|/**      * Name of specific XML data format to use.      * By default jaxb will be used.      * Important: This option is only for setting a custom name of the data format, not to refer to an existing data format instance.      */
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
comment|// fluent builders
comment|// --------------------------------------------------------------
comment|/**      * The Camel Rest component to use for the REST transport (consumer), such as restlet, spark-rest.      * If no component has been explicit configured, then Camel will lookup if there is a Camel component      * that integrates with the Rest DSL, or if a org.apache.camel.spi.RestConsumerFactory is registered in the registry.      * If either one is found, then that is being used.      */
DECL|method|withComponent (String component)
specifier|public
name|RestConfigurationProperties
name|withComponent
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
return|return
name|this
return|;
block|}
comment|/**      * The name of the Camel component to use as the REST API (such as swagger)      */
DECL|method|withApiComponent (String apiComponent)
specifier|public
name|RestConfigurationProperties
name|withApiComponent
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
return|return
name|this
return|;
block|}
comment|/**      * Sets the name of the Camel component to use as the REST producer      */
DECL|method|withProducerComponent (String producerComponent)
specifier|public
name|RestConfigurationProperties
name|withProducerComponent
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
return|return
name|this
return|;
block|}
comment|/**      * The scheme to use for exposing the REST service. Usually http or https is supported.      *<p/>      * The default value is http      */
DECL|method|withScheme (String scheme)
specifier|public
name|RestConfigurationProperties
name|withScheme
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
return|return
name|this
return|;
block|}
comment|/**      * The hostname to use for exposing the REST service.      */
DECL|method|withHost (String host)
specifier|public
name|RestConfigurationProperties
name|withHost
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
return|return
name|this
return|;
block|}
comment|/**      * To use an specific hostname for the API documentation (eg swagger)      *<p/>      * This can be used to override the generated host with this configured hostname      */
DECL|method|withApiHost (String apiHost)
specifier|public
name|RestConfigurationProperties
name|withApiHost
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
return|return
name|this
return|;
block|}
comment|/**      * Whether to use X-Forward headers for Host and related setting.      *<p/>      * The default value is true.      */
DECL|method|withUseXForwardHeaders (Boolean useXForwardHeaders)
specifier|public
name|RestConfigurationProperties
name|withUseXForwardHeaders
parameter_list|(
name|Boolean
name|useXForwardHeaders
parameter_list|)
block|{
name|this
operator|.
name|useXForwardHeaders
operator|=
name|useXForwardHeaders
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The port number to use for exposing the REST service.      * Notice if you use servlet component then the port number configured here does not apply,      * as the port number in use is the actual port number the servlet component is using.      * eg if using Apache Tomcat its the tomcat http port, if using Apache Karaf its the HTTP service in Karaf      * that uses port 8181 by default etc. Though in those situations setting the port number here,      * allows tooling and JMX to know the port number, so its recommended to set the port number      * to the number that the servlet engine uses.      */
DECL|method|withPort (String port)
specifier|public
name|RestConfigurationProperties
name|withPort
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
return|return
name|this
return|;
block|}
comment|/**      * Sets the location of the api document (swagger api) the REST producer will use      * to validate the REST uri and query parameters are valid accordingly to the api document.      * This requires adding camel-swagger-java to the classpath, and any miss configuration      * will let Camel fail on startup and report the error(s).      *<p/>      * The location of the api document is loaded from classpath by default, but you can use      *<tt>file:</tt> or<tt>http:</tt> to refer to resources to load from file or http url.      */
DECL|method|withProducerApiDoc (String producerApiDoc)
specifier|public
name|RestConfigurationProperties
name|withProducerApiDoc
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
return|return
name|this
return|;
block|}
comment|/**      * Sets a leading context-path the REST services will be using.      *<p/>      * This can be used when using components such as<tt>camel-servlet</tt> where the deployed web application      * is deployed using a context-path. Or for components such as<tt>camel-jetty</tt> or<tt>camel-netty4-http</tt>      * that includes a HTTP server.      */
DECL|method|withContextPath (String contextPath)
specifier|public
name|RestConfigurationProperties
name|withContextPath
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
return|return
name|this
return|;
block|}
comment|/**      * Sets a leading API context-path the REST API services will be using.      *<p/>      * This can be used when using components such as<tt>camel-servlet</tt> where the deployed web application      * is deployed using a context-path.      */
DECL|method|withApiContextPath (String apiContextPath)
specifier|public
name|RestConfigurationProperties
name|withApiContextPath
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
return|return
name|this
return|;
block|}
comment|/**      * Sets the route id to use for the route that services the REST API.      *<p/>      * The route will by default use an auto assigned route id.      */
DECL|method|withApiContextRouteId (String apiContextRouteId)
specifier|public
name|RestConfigurationProperties
name|withApiContextRouteId
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
return|return
name|this
return|;
block|}
comment|/**      * Sets an CamelContext id pattern to only allow Rest APIs from rest services within CamelContext's which name matches the pattern.      *<p/>      * The pattern<tt>#name#</tt> refers to the CamelContext name, to match on the current CamelContext only.      * For any other value, the pattern uses the rules from {@link PatternHelper#matchPattern(String, String)}      */
DECL|method|withApiContextIdPattern (String apiContextIdPattern)
specifier|public
name|RestConfigurationProperties
name|withApiContextIdPattern
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
return|return
name|this
return|;
block|}
comment|/**      * Sets whether listing of all available CamelContext's with REST services in the JVM is enabled. If enabled it allows to discover      * these contexts, if<tt>false</tt> then only the current CamelContext is in use.      */
DECL|method|withApiContextListing (Boolean apiContextListing)
specifier|public
name|RestConfigurationProperties
name|withApiContextListing
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
return|return
name|this
return|;
block|}
comment|/**      * Whether vendor extension is enabled in the Rest APIs. If enabled then Camel will include additional information      * as vendor extension (eg keys starting with x-) such as route ids, class names etc.      * Not all 3rd party API gateways and tools supports vendor-extensions when importing your API docs.      */
DECL|method|withApiVendorExtension (Boolean apiVendorExtension)
specifier|public
name|RestConfigurationProperties
name|withApiVendorExtension
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
return|return
name|this
return|;
block|}
comment|/**      * If no hostname has been explicit configured, then this resolver is used to compute the hostname the REST service will be using.      * The possible values are: allLocalIp, localIp, localHostName      */
DECL|method|withHostNameResolver (String hostNameResolver)
specifier|public
name|RestConfigurationProperties
name|withHostNameResolver
parameter_list|(
name|String
name|hostNameResolver
parameter_list|)
block|{
name|this
operator|.
name|hostNameResolver
operator|=
name|hostNameResolver
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the binding mode to use.      *<p/>      * The possible values are: auto, off, json, xml, json_xml      * The default value is off      */
DECL|method|withBindingMode (String bindingMode)
specifier|public
name|RestConfigurationProperties
name|withBindingMode
parameter_list|(
name|String
name|bindingMode
parameter_list|)
block|{
name|this
operator|.
name|bindingMode
operator|=
name|bindingMode
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to skip binding on output if there is a custom HTTP error code header.      * This allows to build custom error messages that do not bind to json / xml etc, as success messages otherwise will do.      */
DECL|method|withSkipBindingOnErrorCode (Boolean skipBindingOnErrorCode)
specifier|public
name|RestConfigurationProperties
name|withSkipBindingOnErrorCode
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
return|return
name|this
return|;
block|}
comment|/**      * Whether to enable validation of the client request to check whether the Content-Type and Accept headers from      * the client is supported by the Rest-DSL configuration of its consumes/produces settings.      *<p/>      * This can be turned on, to enable this check. In case of validation error, then HTTP Status codes 415 or 406 is returned.      *<p/>      * The default value is false.      */
DECL|method|withClientRequestValidation (Boolean clientRequestValidation)
specifier|public
name|RestConfigurationProperties
name|withClientRequestValidation
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
return|return
name|this
return|;
block|}
comment|/**      * Whether to enable CORS headers in the HTTP response.      *<p/>      * The default value is false.      */
DECL|method|withEnableCORS (Boolean enableCORS)
specifier|public
name|RestConfigurationProperties
name|withEnableCORS
parameter_list|(
name|Boolean
name|enableCORS
parameter_list|)
block|{
name|this
operator|.
name|enableCORS
operator|=
name|enableCORS
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Name of specific json data format to use.      * By default json-jackson will be used.      * Important: This option is only for setting a custom name of the data format, not to refer to an existing data format instance.      */
DECL|method|withJsonDataFormat (String jsonDataFormat)
specifier|public
name|RestConfigurationProperties
name|withJsonDataFormat
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
return|return
name|this
return|;
block|}
comment|/**      * Name of specific XML data format to use.      * By default jaxb will be used.      * Important: This option is only for setting a custom name of the data format, not to refer to an existing data format instance.      */
DECL|method|withXmlDataFormat (String xmlDataFormat)
specifier|public
name|RestConfigurationProperties
name|withXmlDataFormat
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
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

