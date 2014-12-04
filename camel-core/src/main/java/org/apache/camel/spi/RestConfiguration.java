begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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

begin_comment
comment|/**  * Configuration use by {@link org.apache.camel.spi.RestConsumerFactory} for Camel components to support  * the Camel {@link org.apache.camel.model.rest.RestDefinition rest} DSL.  */
end_comment

begin_class
DECL|class|RestConfiguration
specifier|public
class|class
name|RestConfiguration
block|{
DECL|enum|RestBindingMode
specifier|public
enum|enum
name|RestBindingMode
block|{
DECL|enumConstant|auto
DECL|enumConstant|off
DECL|enumConstant|json
DECL|enumConstant|xml
DECL|enumConstant|json_xml
name|auto
block|,
name|off
block|,
name|json
block|,
name|xml
block|,
name|json_xml
block|}
DECL|enum|RestHostNameResolver
specifier|public
enum|enum
name|RestHostNameResolver
block|{
DECL|enumConstant|localIp
DECL|enumConstant|localHostName
name|localIp
block|,
name|localHostName
block|}
DECL|field|component
specifier|private
name|String
name|component
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
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|contextPath
specifier|private
name|String
name|contextPath
decl_stmt|;
DECL|field|restHostNameResolver
specifier|private
name|RestHostNameResolver
name|restHostNameResolver
init|=
name|RestHostNameResolver
operator|.
name|localHostName
decl_stmt|;
DECL|field|bindingMode
specifier|private
name|RestBindingMode
name|bindingMode
init|=
name|RestBindingMode
operator|.
name|off
decl_stmt|;
DECL|field|skipBindingOnErrorCode
specifier|private
name|boolean
name|skipBindingOnErrorCode
init|=
literal|true
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
DECL|field|componentProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|componentProperties
decl_stmt|;
DECL|field|endpointProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|endpointProperties
decl_stmt|;
DECL|field|consumerProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|consumerProperties
decl_stmt|;
DECL|field|dataFormatProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dataFormatProperties
decl_stmt|;
comment|/**      * Gets the name of the Camel component to use as the REST consumer      *      * @return the component name, or<tt>null</tt> to let Camel search the {@link Registry} to find suitable implementation      */
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
comment|/**      * Sets the name of the Camel component to use as the REST consumer      *      * @param componentName the name of the component (such as restlet, spark-rest, etc.)      */
DECL|method|setComponent (String componentName)
specifier|public
name|void
name|setComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|componentName
expr_stmt|;
block|}
comment|/**      * Gets the hostname to use by the REST consumer      *      * @return the hostname, or<tt>null</tt> to use default hostname      */
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
comment|/**      * Sets the hostname to use by the REST consumer      *      * @param host the hostname      */
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
comment|/**      * Gets the scheme to use by the REST consumer      *      * @return the scheme, or<tt>null</tt> to use default scheme      */
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
comment|/**      * Sets the scheme to use by the REST consumer      *      * @param scheme the scheme      */
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
comment|/**      * Gets the port to use by the REST consumer      *      * @return the port, or<tt>0</tt> or<tt>-1</tt> to use default port      */
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Sets the port to use by the REST consumer      *      * @param port the port number      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
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
comment|/**      * Gets the configured context-path      *      * @return the context path, or<tt>null</tt> if none configured.      */
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
comment|/**      * Sets a leading context-path the REST services will be using.      *<p/>      * This can be used when using components such as<tt>camel-servlet</tt> where the deployed web application      * is deployed using a context-path.      *      * @param contextPath the context path      */
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
comment|/**      * Gets the resolver to use for resolving hostname      *      * @return the resolver      */
DECL|method|getRestHostNameResolver ()
specifier|public
name|RestHostNameResolver
name|getRestHostNameResolver
parameter_list|()
block|{
return|return
name|restHostNameResolver
return|;
block|}
comment|/**      * Sets the resolver to use for resolving hostname      *      * @param restHostNameResolver the resolver      */
DECL|method|setRestHostNameResolver (RestHostNameResolver restHostNameResolver)
specifier|public
name|void
name|setRestHostNameResolver
parameter_list|(
name|RestHostNameResolver
name|restHostNameResolver
parameter_list|)
block|{
name|this
operator|.
name|restHostNameResolver
operator|=
name|restHostNameResolver
expr_stmt|;
block|}
comment|/**      * Sets the resolver to use for resolving hostname      *      * @param restHostNameResolver the resolver      */
DECL|method|setRestHostNameResolver (String restHostNameResolver)
specifier|public
name|void
name|setRestHostNameResolver
parameter_list|(
name|String
name|restHostNameResolver
parameter_list|)
block|{
name|this
operator|.
name|restHostNameResolver
operator|=
name|RestHostNameResolver
operator|.
name|valueOf
argument_list|(
name|restHostNameResolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the binding mode used by the REST consumer      *      * @return the binding mode      */
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
comment|/**      * Sets the binding mode to be used by the REST consumer      *      * @param bindingMode the binding mode      */
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
comment|/**      * Sets the binding mode to be used by the REST consumer      *      * @param bindingMode the binding mode      */
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
name|RestBindingMode
operator|.
name|valueOf
argument_list|(
name|bindingMode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Whether to skip binding output if there is a custom HTTP error code, and instead use the response body as-is.      *<p/>      * This option is default<tt>true</tt>.      */
DECL|method|isSkipBindingOnErrorCode ()
specifier|public
name|boolean
name|isSkipBindingOnErrorCode
parameter_list|()
block|{
return|return
name|skipBindingOnErrorCode
return|;
block|}
comment|/**      * Whether to skip binding output if there is a custom HTTP error code, and instead use the response body as-is.      *<p/>      * This option is default<tt>true</tt>.      */
DECL|method|setSkipBindingOnErrorCode (boolean skipBindingOnErrorCode)
specifier|public
name|void
name|setSkipBindingOnErrorCode
parameter_list|(
name|boolean
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
comment|/**      * Gets the name of the json data format.      *<p/>      *<b>Important:</b> This option is only for setting a custom name of the data format, not to refer to an existing data format instance.      *      * @return the name, or<tt>null</tt> to use default      */
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
comment|/**      * Sets a custom json data format to be used      *<p/>      *<b>Important:</b> This option is only for setting a custom name of the data format, not to refer to an existing data format instance.      *      * @param name name of the data format      */
DECL|method|setJsonDataFormat (String name)
specifier|public
name|void
name|setJsonDataFormat
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|jsonDataFormat
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Gets the name of the xml data format.      *<p/>      *<b>Important:</b> This option is only for setting a custom name of the data format, not to refer to an existing data format instance.      *      * @return the name, or<tt>null</tt> to use default      */
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
comment|/**      * Sets a custom xml data format to be used.      *<p/>      *<b>Important:</b> This option is only for setting a custom name of the data format, not to refer to an existing data format instance.      *      * @param name name of the data format      */
DECL|method|setXmlDataFormat (String name)
specifier|public
name|void
name|setXmlDataFormat
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|xmlDataFormat
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Gets additional options on component level      *      * @return additional options      */
DECL|method|getComponentProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getComponentProperties
parameter_list|()
block|{
return|return
name|componentProperties
return|;
block|}
comment|/**      * Sets additional options on component level      *      * @param componentProperties the options      */
DECL|method|setComponentProperties (Map<String, Object> componentProperties)
specifier|public
name|void
name|setComponentProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|componentProperties
parameter_list|)
block|{
name|this
operator|.
name|componentProperties
operator|=
name|componentProperties
expr_stmt|;
block|}
comment|/**      * Gets additional options on endpoint level      *      * @return additional options      */
DECL|method|getEndpointProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getEndpointProperties
parameter_list|()
block|{
return|return
name|endpointProperties
return|;
block|}
comment|/**      * Sets additional options on endpoint level      *      * @param endpointProperties the options      */
DECL|method|setEndpointProperties (Map<String, Object> endpointProperties)
specifier|public
name|void
name|setEndpointProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|endpointProperties
parameter_list|)
block|{
name|this
operator|.
name|endpointProperties
operator|=
name|endpointProperties
expr_stmt|;
block|}
comment|/**      * Gets additional options on consumer level      *      * @return additional options      */
DECL|method|getConsumerProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getConsumerProperties
parameter_list|()
block|{
return|return
name|consumerProperties
return|;
block|}
comment|/**      * Sets additional options on consumer level      *      * @param consumerProperties the options      */
DECL|method|setConsumerProperties (Map<String, Object> consumerProperties)
specifier|public
name|void
name|setConsumerProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|consumerProperties
parameter_list|)
block|{
name|this
operator|.
name|consumerProperties
operator|=
name|consumerProperties
expr_stmt|;
block|}
comment|/**      * Gets additional options on data format level      *      * @return additional options      */
DECL|method|getDataFormatProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getDataFormatProperties
parameter_list|()
block|{
return|return
name|dataFormatProperties
return|;
block|}
comment|/**      * Sets additional options on data format level      *      * @param dataFormatProperties the options      */
DECL|method|setDataFormatProperties (Map<String, Object> dataFormatProperties)
specifier|public
name|void
name|setDataFormatProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dataFormatProperties
parameter_list|)
block|{
name|this
operator|.
name|dataFormatProperties
operator|=
name|dataFormatProperties
expr_stmt|;
block|}
block|}
end_class

end_unit

