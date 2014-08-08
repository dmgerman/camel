begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
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
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|util
operator|.
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;restConfiguration/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"restConfiguration"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RestConfigurationDefinition
specifier|public
class|class
name|RestConfigurationDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|component
specifier|private
name|String
name|component
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|port
specifier|private
name|String
name|port
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|contextPath
specifier|private
name|String
name|contextPath
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|hostNameResolver
specifier|private
name|RestHostNameResolver
name|hostNameResolver
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|bindingMode
specifier|private
name|RestBindingMode
name|bindingMode
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|jsonDataFormat
specifier|private
name|String
name|jsonDataFormat
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|xmlDataFormat
specifier|private
name|String
name|xmlDataFormat
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"componentProperty"
argument_list|)
DECL|field|componentProperties
specifier|private
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|componentProperties
init|=
operator|new
name|ArrayList
argument_list|<
name|RestPropertyDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"endpointProperty"
argument_list|)
DECL|field|endpointProperties
specifier|private
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|endpointProperties
init|=
operator|new
name|ArrayList
argument_list|<
name|RestPropertyDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"consumerProperty"
argument_list|)
DECL|field|consumerProperties
specifier|private
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|consumerProperties
init|=
operator|new
name|ArrayList
argument_list|<
name|RestPropertyDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"dataFormatProperty"
argument_list|)
DECL|field|dataFormatProperties
specifier|private
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|dataFormatProperties
init|=
operator|new
name|ArrayList
argument_list|<
name|RestPropertyDefinition
argument_list|>
argument_list|()
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
DECL|method|getComponentProperties ()
specifier|public
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|getComponentProperties
parameter_list|()
block|{
return|return
name|componentProperties
return|;
block|}
DECL|method|setComponentProperties (List<RestPropertyDefinition> componentProperties)
specifier|public
name|void
name|setComponentProperties
parameter_list|(
name|List
argument_list|<
name|RestPropertyDefinition
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
DECL|method|getEndpointProperties ()
specifier|public
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|getEndpointProperties
parameter_list|()
block|{
return|return
name|endpointProperties
return|;
block|}
DECL|method|setEndpointProperties (List<RestPropertyDefinition> endpointProperties)
specifier|public
name|void
name|setEndpointProperties
parameter_list|(
name|List
argument_list|<
name|RestPropertyDefinition
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
DECL|method|getConsumerProperties ()
specifier|public
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|getConsumerProperties
parameter_list|()
block|{
return|return
name|consumerProperties
return|;
block|}
DECL|method|setConsumerProperties (List<RestPropertyDefinition> consumerProperties)
specifier|public
name|void
name|setConsumerProperties
parameter_list|(
name|List
argument_list|<
name|RestPropertyDefinition
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
DECL|method|getDataFormatProperties ()
specifier|public
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|getDataFormatProperties
parameter_list|()
block|{
return|return
name|dataFormatProperties
return|;
block|}
DECL|method|setDataFormatProperties (List<RestPropertyDefinition> dataFormatProperties)
specifier|public
name|void
name|setDataFormatProperties
parameter_list|(
name|List
argument_list|<
name|RestPropertyDefinition
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
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * To use a specific Camel rest component      */
DECL|method|component (String componentId)
specifier|public
name|RestConfigurationDefinition
name|component
parameter_list|(
name|String
name|componentId
parameter_list|)
block|{
name|setComponent
argument_list|(
name|componentId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * To use a specific scheme such as http/https      */
DECL|method|scheme (String scheme)
specifier|public
name|RestConfigurationDefinition
name|scheme
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
comment|/**      * To define the host to use, such as 0.0.0.0 or localhost      */
DECL|method|host (String host)
specifier|public
name|RestConfigurationDefinition
name|host
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
comment|/**      * To specify the port number to use for the REST service      */
DECL|method|port (int port)
specifier|public
name|RestConfigurationDefinition
name|port
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|setPort
argument_list|(
literal|""
operator|+
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * To specify the port number to use for the REST service      */
DECL|method|port (String port)
specifier|public
name|RestConfigurationDefinition
name|port
parameter_list|(
name|String
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
comment|/**      * Sets a leading context-path the REST services will be using.      *<p/>      * This can be used when using components such as<tt>camel-servlet</tt> where the deployed web application      * is deployed using a context-path.      */
DECL|method|contextPath (String contextPath)
specifier|public
name|RestConfigurationDefinition
name|contextPath
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
comment|/**      * To specify the hostname resolver      */
DECL|method|hostNameResolver (RestHostNameResolver hostNameResolver)
specifier|public
name|RestConfigurationDefinition
name|hostNameResolver
parameter_list|(
name|RestHostNameResolver
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
comment|/**      * To specify the binding mode      */
DECL|method|bindingMode (RestBindingMode bindingMode)
specifier|public
name|RestConfigurationDefinition
name|bindingMode
parameter_list|(
name|RestBindingMode
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
comment|/**      * To use a specific json data format      *      * @param name  name of the data format to {@link org.apache.camel.CamelContext#resolveDataFormat(java.lang.String) resolve}      */
DECL|method|jsonDataFormat (String name)
specifier|public
name|RestConfigurationDefinition
name|jsonDataFormat
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setJsonDataFormat
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * To use a specific XML data format      *      * @param name  name of the data format to {@link org.apache.camel.CamelContext#resolveDataFormat(java.lang.String) resolve}      */
DECL|method|xmlDataFormat (String name)
specifier|public
name|RestConfigurationDefinition
name|xmlDataFormat
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setXmlDataFormat
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * For additional configuration options on component level      */
DECL|method|componentProperty (String key, String value)
specifier|public
name|RestConfigurationDefinition
name|componentProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|RestPropertyDefinition
name|prop
init|=
operator|new
name|RestPropertyDefinition
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getComponentProperties
argument_list|()
operator|.
name|add
argument_list|(
name|prop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * For additional configuration options on endpoint level      */
DECL|method|endpointProperty (String key, String value)
specifier|public
name|RestConfigurationDefinition
name|endpointProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|RestPropertyDefinition
name|prop
init|=
operator|new
name|RestPropertyDefinition
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getEndpointProperties
argument_list|()
operator|.
name|add
argument_list|(
name|prop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * For additional configuration options on consumer level      */
DECL|method|consumerProperty (String key, String value)
specifier|public
name|RestConfigurationDefinition
name|consumerProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|RestPropertyDefinition
name|prop
init|=
operator|new
name|RestPropertyDefinition
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getConsumerProperties
argument_list|()
operator|.
name|add
argument_list|(
name|prop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * For additional configuration options on data format level      */
DECL|method|dataFormatProperty (String key, String value)
specifier|public
name|RestConfigurationDefinition
name|dataFormatProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|RestPropertyDefinition
name|prop
init|=
operator|new
name|RestPropertyDefinition
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getDataFormatProperties
argument_list|()
operator|.
name|add
argument_list|(
name|prop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Implementation
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a {@link org.apache.camel.spi.RestConfiguration} instance based on the definition      *      * @param context     the camel context      * @return the configuration      * @throws Exception is thrown if error creating the configuration      */
DECL|method|asRestConfiguration (CamelContext context)
specifier|public
name|RestConfiguration
name|asRestConfiguration
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|RestConfiguration
name|answer
init|=
operator|new
name|RestConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setComponent
argument_list|(
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|component
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scheme
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setScheme
argument_list|(
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|scheme
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setHost
argument_list|(
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|host
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setPort
argument_list|(
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|context
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contextPath
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setContextPath
argument_list|(
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|contextPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hostNameResolver
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRestHostNameResolver
argument_list|(
name|hostNameResolver
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bindingMode
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setBindingMode
argument_list|(
name|bindingMode
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jsonDataFormat
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setJsonDataFormat
argument_list|(
name|jsonDataFormat
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xmlDataFormat
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setXmlDataFormat
argument_list|(
name|xmlDataFormat
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|componentProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RestPropertyDefinition
name|prop
range|:
name|componentProperties
control|)
block|{
name|String
name|key
init|=
name|prop
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setComponentProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|endpointProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RestPropertyDefinition
name|prop
range|:
name|endpointProperties
control|)
block|{
name|String
name|key
init|=
name|prop
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setEndpointProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|consumerProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RestPropertyDefinition
name|prop
range|:
name|consumerProperties
control|)
block|{
name|String
name|key
init|=
name|prop
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setConsumerProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|dataFormatProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RestPropertyDefinition
name|prop
range|:
name|dataFormatProperties
control|)
block|{
name|String
name|key
init|=
name|prop
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setDataFormatProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

