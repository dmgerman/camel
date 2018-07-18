begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest.swagger.springboot
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
name|swagger
operator|.
name|springboot
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
comment|/**  * An awesome REST endpoint backed by Swagger specifications.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.rest-swagger"
argument_list|)
DECL|class|RestSwaggerComponentConfiguration
specifier|public
class|class
name|RestSwaggerComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the rest-swagger component. This      * is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * API basePath, for example /v2. Default is unset, if set overrides the      * value present in Swagger specification.      */
DECL|field|basePath
specifier|private
name|String
name|basePath
decl_stmt|;
comment|/**      * Name of the Camel component that will perform the requests. The compnent      * must be present in Camel registry and it must implement      * RestProducerFactory service provider interface. If not set CLASSPATH is      * searched for single component that implements RestProducerFactory SPI.      * Can be overriden in endpoint configuration.      */
DECL|field|componentName
specifier|private
name|String
name|componentName
decl_stmt|;
comment|/**      * What payload type this component capable of consuming. Could be one type,      * like application/json or multiple types as application/json,      * application/xml; q=0.5 according to the RFC7231. This equates to the      * value of Accept HTTP header. If set overrides any value found in the      * Swagger specification. Can be overriden in endpoint configuration      */
DECL|field|consumes
specifier|private
name|String
name|consumes
decl_stmt|;
comment|/**      * Scheme hostname and port to direct the HTTP requests to in the form of      * https://hostname:port. Can be configured at the endpoint, component or in      * the correspoding REST configuration in the Camel Context. If you give      * this component a name (e.g. petstore) that REST configuration is      * consulted first, rest-swagger next, and global configuration last. If set      * overrides any value found in the Swagger specification,      * RestConfiguration. Can be overriden in endpoint configuration.      */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * What payload type this component is producing. For example      * application/json according to the RFC7231. This equates to the value of      * Content-Type HTTP header. If set overrides any value present in the      * Swagger specification. Can be overriden in endpoint configuration.      */
DECL|field|produces
specifier|private
name|String
name|produces
decl_stmt|;
comment|/**      * Path to the Swagger specification file. The scheme, host base path are      * taken from this specification, but these can be overriden with properties      * on the component or endpoint level. If not given the component tries to      * load swagger.json resource. Note that the host defined on the component      * and endpoint of this Component should contain the scheme, hostname and      * optionally the port in the URI syntax (i.e.      * https://api.example.com:8080). Can be overriden in endpoint      * configuration.      */
DECL|field|specificationUri
specifier|private
name|URI
name|specificationUri
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
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
DECL|method|setBasePath (String basePath)
specifier|public
name|void
name|setBasePath
parameter_list|(
name|String
name|basePath
parameter_list|)
block|{
name|this
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
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
DECL|method|setComponentName (String componentName)
specifier|public
name|void
name|setComponentName
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
name|this
operator|.
name|componentName
operator|=
name|componentName
expr_stmt|;
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
DECL|method|setConsumes (String consumes)
specifier|public
name|void
name|setConsumes
parameter_list|(
name|String
name|consumes
parameter_list|)
block|{
name|this
operator|.
name|consumes
operator|=
name|consumes
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
DECL|method|setProduces (String produces)
specifier|public
name|void
name|setProduces
parameter_list|(
name|String
name|produces
parameter_list|)
block|{
name|this
operator|.
name|produces
operator|=
name|produces
expr_stmt|;
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
DECL|method|setSpecificationUri (URI specificationUri)
specifier|public
name|void
name|setSpecificationUri
parameter_list|(
name|URI
name|specificationUri
parameter_list|)
block|{
name|this
operator|.
name|specificationUri
operator|=
name|specificationUri
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

