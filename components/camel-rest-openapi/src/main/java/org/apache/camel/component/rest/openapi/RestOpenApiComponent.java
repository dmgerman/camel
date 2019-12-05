begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest.openapi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
operator|.
name|openapi
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
name|spi
operator|.
name|RestProducerFactory
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
operator|.
name|openapi
operator|.
name|RestOpenApiHelper
operator|.
name|isHostParam
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
operator|.
name|openapi
operator|.
name|RestOpenApiHelper
operator|.
name|isMediaRange
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
operator|.
name|notEmpty
import|;
end_import

begin_comment
comment|/**  * An awesome REST component backed by OpenApi specifications. Creates endpoints  * that connect to REST APIs defined by OpenApi specification. This component  * delegates to other {@link RestProducerFactory} components to act as REST  * clients, but it configures them from OpenApi specification. Client needs to  * point to operation that it wants to invoke via REST, provide any additional  * HTTP headers as headers in the Camel message, and any payload as the body of  * the incoming message.  *<p>  * Example usage using Java DSL:  *<p>  *  *<pre>  * from(...).to("rest-openapi:https://petstore3.swagger.io/api/v3/openapi.json#getPetById")  *</pre>  *  * This relies on only one {@link RestProducerFactory} component being available  * to Camel, you can use specific, for instance preconfigured component by using  * the {@code componentName} endpoint property. For example using Undertow  * component in Java DSL:  *<p>  *  *<pre>  * Component undertow = new UndertowComponent();  * undertow.setSslContextParameters(...);  * //...  * camelContext.addComponent("myUndertow", undertow);  *  * from(...).to("rest-openapi:http://petstore.swagger.io/v2/swagger.json#getPetById?componentName=myUndertow")  *</pre>  *  * The most concise way of using this component would be to define it in the  * Camel context under a meaningful name, for example:  *  *<pre>  * Component petstore = new RestOpenApiComponent();  * petstore.setSpecificationUri("https://petstore3.swagger.io/api/v3/openapi.json");  * petstore.setComponentName("undertow");  * //...  * camelContext.addComponent("petstore", petstore);  *  * from(...).to("petstore:getPetById")  *</pre>  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"rest-openapi"
argument_list|)
DECL|class|RestOpenApiComponent
specifier|public
specifier|final
class|class
name|RestOpenApiComponent
extends|extends
name|DefaultComponent
implements|implements
name|SSLContextParametersAware
block|{
DECL|field|DEFAULT_BASE_PATH
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_BASE_PATH
init|=
literal|"/"
decl_stmt|;
DECL|field|DEFAULT_SPECIFICATION_URI
specifier|static
specifier|final
name|URI
name|DEFAULT_SPECIFICATION_URI
init|=
name|URI
operator|.
name|create
argument_list|(
name|RestOpenApiComponent
operator|.
name|DEFAULT_SPECIFICATION_URI_STR
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_SPECIFICATION_URI_STR
specifier|static
specifier|final
name|String
name|DEFAULT_SPECIFICATION_URI_STR
init|=
literal|"openapi.json"
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"API basePath, for example \"`/v2`\". Default is unset, if set overrides the value present in OpenApi specification."
argument_list|,
name|defaultValue
operator|=
literal|""
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|basePath
specifier|private
name|String
name|basePath
init|=
literal|""
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Name of the Camel component that will perform the requests. The component must be present"
operator|+
literal|" in Camel registry and it must implement RestProducerFactory service provider interface. If not set"
operator|+
literal|" CLASSPATH is searched for single component that implements RestProducerFactory SPI. Can be overridden in"
operator|+
literal|" endpoint configuration."
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|componentName
specifier|private
name|String
name|componentName
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"What payload type this component capable of consuming. Could be one type, like `application/json`"
operator|+
literal|" or multiple types as `application/json, application/xml; q=0.5` according to the RFC7231. This equates"
operator|+
literal|" to the value of `Accept` HTTP header. If set overrides any value found in the OpenApi specification."
operator|+
literal|" Can be overridden in endpoint configuration"
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|consumes
specifier|private
name|String
name|consumes
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Scheme hostname and port to direct the HTTP requests to in the form of"
operator|+
literal|" `http[s]://hostname[:port]`. Can be configured at the endpoint, component or in the corresponding"
operator|+
literal|" REST configuration in the Camel Context. If you give this component a name (e.g. `petstore`) that"
operator|+
literal|" REST configuration is consulted first, `rest-openapi` next, and global configuration last. If set"
operator|+
literal|" overrides any value found in the OpenApi specification, RestConfiguration. Can be overridden in endpoint"
operator|+
literal|" configuration."
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"What payload type this component is producing. For example `application/json`"
operator|+
literal|" according to the RFC7231. This equates to the value of `Content-Type` HTTP header. If set overrides"
operator|+
literal|" any value present in the OpenApi specification. Can be overridden in endpoint configuration."
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|produces
specifier|private
name|String
name|produces
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Path to the OpenApi specification file. The scheme, host base path are taken from this"
operator|+
literal|" specification, but these can be overridden with properties on the component or endpoint level. If not"
operator|+
literal|" given the component tries to load `openapi.json` resource. Note that the `host` defined on the"
operator|+
literal|" component and endpoint of this Component should contain the scheme, hostname and optionally the"
operator|+
literal|" port in the URI syntax (i.e. `https://api.example.com:8080`). Can be overridden in endpoint"
operator|+
literal|" configuration."
argument_list|,
name|defaultValue
operator|=
name|DEFAULT_SPECIFICATION_URI_STR
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|specificationUri
specifier|private
name|URI
name|specificationUri
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Customize TLS parameters used by the component. If not set defaults to the TLS parameters"
operator|+
literal|" set in the Camel context "
argument_list|,
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Enable usage of global SSL context parameters."
argument_list|,
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
DECL|method|RestOpenApiComponent ()
specifier|public
name|RestOpenApiComponent
parameter_list|()
block|{     }
DECL|method|RestOpenApiComponent (final CamelContext context)
specifier|public
name|RestOpenApiComponent
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
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
name|Endpoint
name|endpoint
init|=
operator|new
name|RestOpenApiEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getBasePath ()
specifier|public
name|String
name|getBasePath
parameter_list|()
block|{
return|return
name|basePath
return|;
block|}
DECL|method|getComponentName ()
specifier|public
name|String
name|getComponentName
parameter_list|()
block|{
return|return
name|componentName
return|;
block|}
DECL|method|getConsumes ()
specifier|public
name|String
name|getConsumes
parameter_list|()
block|{
return|return
name|consumes
return|;
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
DECL|method|getProduces ()
specifier|public
name|String
name|getProduces
parameter_list|()
block|{
return|return
name|produces
return|;
block|}
DECL|method|getSpecificationUri ()
specifier|public
name|URI
name|getSpecificationUri
parameter_list|()
block|{
return|return
name|specificationUri
return|;
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
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setBasePath (final String basePath)
specifier|public
name|void
name|setBasePath
parameter_list|(
specifier|final
name|String
name|basePath
parameter_list|)
block|{
name|this
operator|.
name|basePath
operator|=
name|notEmpty
argument_list|(
name|basePath
argument_list|,
literal|"basePath"
argument_list|)
expr_stmt|;
block|}
DECL|method|setComponentName (final String componentName)
specifier|public
name|void
name|setComponentName
parameter_list|(
specifier|final
name|String
name|componentName
parameter_list|)
block|{
name|this
operator|.
name|componentName
operator|=
name|notEmpty
argument_list|(
name|componentName
argument_list|,
literal|"componentName"
argument_list|)
expr_stmt|;
block|}
DECL|method|setConsumes (final String consumes)
specifier|public
name|void
name|setConsumes
parameter_list|(
specifier|final
name|String
name|consumes
parameter_list|)
block|{
name|this
operator|.
name|consumes
operator|=
name|isMediaRange
argument_list|(
name|consumes
argument_list|,
literal|"consumes"
argument_list|)
expr_stmt|;
block|}
DECL|method|setHost (final String host)
specifier|public
name|void
name|setHost
parameter_list|(
specifier|final
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|isHostParam
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
DECL|method|setProduces (final String produces)
specifier|public
name|void
name|setProduces
parameter_list|(
specifier|final
name|String
name|produces
parameter_list|)
block|{
name|this
operator|.
name|produces
operator|=
name|isMediaRange
argument_list|(
name|produces
argument_list|,
literal|"produces"
argument_list|)
expr_stmt|;
block|}
DECL|method|setSpecificationUri (final URI specificationUri)
specifier|public
name|void
name|setSpecificationUri
parameter_list|(
specifier|final
name|URI
name|specificationUri
parameter_list|)
block|{
name|this
operator|.
name|specificationUri
operator|=
name|notNull
argument_list|(
name|specificationUri
argument_list|,
literal|"specificationUri"
argument_list|)
expr_stmt|;
block|}
DECL|method|setSslContextParameters (final SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
specifier|final
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
annotation|@
name|Override
DECL|method|setUseGlobalSslContextParameters (final boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
specifier|final
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

