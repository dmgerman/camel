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
name|spi
operator|.
name|RestConfiguration
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
extends|extends
name|RestConfiguration
block|{
DECL|field|parent
specifier|private
specifier|final
name|MainConfigurationProperties
name|parent
decl_stmt|;
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
comment|// these are inherited from the parent class
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
name|setComponent
argument_list|(
name|component
argument_list|)
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
name|setApiComponent
argument_list|(
name|apiComponent
argument_list|)
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
name|setProducerComponent
argument_list|(
name|producerComponent
argument_list|)
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
name|setScheme
argument_list|(
name|scheme
argument_list|)
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
name|setHost
argument_list|(
name|host
argument_list|)
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
name|setApiHost
argument_list|(
name|apiHost
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to use X-Forward headers for Host and related setting.      *<p/>      * The default value is true.      */
DECL|method|withUseXForwardHeaders (boolean useXForwardHeaders)
specifier|public
name|RestConfigurationProperties
name|withUseXForwardHeaders
parameter_list|(
name|boolean
name|useXForwardHeaders
parameter_list|)
block|{
name|setUseXForwardHeaders
argument_list|(
name|useXForwardHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The port number to use for exposing the REST service.      * Notice if you use servlet component then the port number configured here does not apply,      * as the port number in use is the actual port number the servlet component is using.      * eg if using Apache Tomcat its the tomcat http port, if using Apache Karaf its the HTTP service in Karaf      * that uses port 8181 by default etc. Though in those situations setting the port number here,      * allows tooling and JMX to know the port number, so its recommended to set the port number      * to the number that the servlet engine uses.      */
DECL|method|withPort (int port)
specifier|public
name|RestConfigurationProperties
name|withPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|setPort
argument_list|(
name|port
argument_list|)
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
name|setProducerApiDoc
argument_list|(
name|producerApiDoc
argument_list|)
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
name|setContextPath
argument_list|(
name|contextPath
argument_list|)
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
name|setApiContextPath
argument_list|(
name|apiContextPath
argument_list|)
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
name|setApiContextRouteId
argument_list|(
name|apiContextRouteId
argument_list|)
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
name|setApiContextIdPattern
argument_list|(
name|apiContextIdPattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether listing of all available CamelContext's with REST services in the JVM is enabled. If enabled it allows to discover      * these contexts, if<tt>false</tt> then only the current CamelContext is in use.      */
DECL|method|withApiContextListing (boolean apiContextListing)
specifier|public
name|RestConfigurationProperties
name|withApiContextListing
parameter_list|(
name|boolean
name|apiContextListing
parameter_list|)
block|{
name|setApiContextListing
argument_list|(
name|apiContextListing
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether vendor extension is enabled in the Rest APIs. If enabled then Camel will include additional information      * as vendor extension (eg keys starting with x-) such as route ids, class names etc.      * Not all 3rd party API gateways and tools supports vendor-extensions when importing your API docs.      */
DECL|method|withApiVendorExtension (boolean apiVendorExtension)
specifier|public
name|RestConfigurationProperties
name|withApiVendorExtension
parameter_list|(
name|boolean
name|apiVendorExtension
parameter_list|)
block|{
name|setApiVendorExtension
argument_list|(
name|apiVendorExtension
argument_list|)
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
name|setHostNameResolver
argument_list|(
name|hostNameResolver
argument_list|)
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
name|setBindingMode
argument_list|(
name|bindingMode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to skip binding on output if there is a custom HTTP error code header.      * This allows to build custom error messages that do not bind to json / xml etc, as success messages otherwise will do.      */
DECL|method|withSkipBindingOnErrorCode (boolean skipBindingOnErrorCode)
specifier|public
name|RestConfigurationProperties
name|withSkipBindingOnErrorCode
parameter_list|(
name|boolean
name|skipBindingOnErrorCode
parameter_list|)
block|{
name|setSkipBindingOnErrorCode
argument_list|(
name|skipBindingOnErrorCode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to enable validation of the client request to check whether the Content-Type and Accept headers from      * the client is supported by the Rest-DSL configuration of its consumes/produces settings.      *<p/>      * This can be turned on, to enable this check. In case of validation error, then HTTP Status codes 415 or 406 is returned.      *<p/>      * The default value is false.      */
DECL|method|withClientRequestValidation (boolean clientRequestValidation)
specifier|public
name|RestConfigurationProperties
name|withClientRequestValidation
parameter_list|(
name|boolean
name|clientRequestValidation
parameter_list|)
block|{
name|setClientRequestValidation
argument_list|(
name|clientRequestValidation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to enable CORS headers in the HTTP response.      *<p/>      * The default value is false.      */
DECL|method|withEnableCORS (boolean enableCORS)
specifier|public
name|RestConfigurationProperties
name|withEnableCORS
parameter_list|(
name|boolean
name|enableCORS
parameter_list|)
block|{
name|setEnableCORS
argument_list|(
name|enableCORS
argument_list|)
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
name|setJsonDataFormat
argument_list|(
name|jsonDataFormat
argument_list|)
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
name|setXmlDataFormat
argument_list|(
name|xmlDataFormat
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

