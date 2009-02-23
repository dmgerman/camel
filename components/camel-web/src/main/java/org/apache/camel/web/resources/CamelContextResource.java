begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|view
operator|.
name|ImplicitProduces
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|spi
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|spi
operator|.
name|resource
operator|.
name|Singleton
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
name|ProducerTemplate
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
name|DefaultCamelContext
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
name|ServiceSupport
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
name|web
operator|.
name|model
operator|.
name|Camel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * The root Camel resource from which all other resources can be navigated such as for<code>endpoints</code>  * or<code>routes</code>  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
annotation|@
name|ImplicitProduces
argument_list|(
name|Constants
operator|.
name|HTML_MIME_TYPES
argument_list|)
annotation|@
name|Singleton
DECL|class|CamelContextResource
specifier|public
class|class
name|CamelContextResource
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
DECL|method|CamelContextResource (@nject CamelContext camelContext)
specifier|public
name|CamelContextResource
parameter_list|(
annotation|@
name|Inject
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|template
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|template
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getTemplate ()
specifier|public
name|ProducerTemplate
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
if|if
condition|(
name|camelContext
operator|instanceof
name|ServiceSupport
condition|)
block|{
name|ServiceSupport
name|serviceSupport
init|=
operator|(
name|ServiceSupport
operator|)
name|camelContext
decl_stmt|;
return|return
name|serviceSupport
operator|.
name|getVersion
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|PreDestroy
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the system properties      */
DECL|method|getSystemProperties ()
specifier|public
name|Map
name|getSystemProperties
parameter_list|()
block|{
return|return
operator|new
name|TreeMap
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
return|;
block|}
comment|// representations
comment|//-------------------------------------------------------------------------
comment|/**      * Returns the XML or JSON representation of the CamelContext      */
annotation|@
name|GET
comment|// TODO we can replace this long expression with a static constant
comment|// when Jersey supports JAX-RS 1.1
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|TEXT_XML
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
DECL|method|getCamel ()
specifier|public
name|Camel
name|getCamel
parameter_list|()
block|{
return|return
operator|new
name|Camel
argument_list|(
name|camelContext
argument_list|,
name|getVersion
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the active endpoints      */
annotation|@
name|Path
argument_list|(
literal|"endpoints"
argument_list|)
DECL|method|getEndpointsResource ()
specifier|public
name|EndpointsResource
name|getEndpointsResource
parameter_list|()
block|{
return|return
operator|new
name|EndpointsResource
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns the active routes      */
annotation|@
name|Path
argument_list|(
literal|"routes"
argument_list|)
DECL|method|getRoutesResource ()
specifier|public
name|RoutesResource
name|getRoutesResource
parameter_list|()
block|{
return|return
operator|new
name|RoutesResource
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"converters"
argument_list|)
DECL|method|getConvertersResource ()
specifier|public
name|ConvertersResource
name|getConvertersResource
parameter_list|()
block|{
return|return
operator|new
name|ConvertersResource
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/*      public List<EndpointLink> getEndpoints() {         return getEndpointsResource().getDTO().getEndpoints();     } */
block|}
end_class

end_unit

