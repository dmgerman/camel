begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kura
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kura
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|ConsumerTemplate
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
name|builder
operator|.
name|RouteBuilder
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
name|core
operator|.
name|osgi
operator|.
name|OsgiDefaultCamelContext
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
name|RoutesDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
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

begin_class
DECL|class|KuraRouter
specifier|public
specifier|abstract
class|class
name|KuraRouter
extends|extends
name|RouteBuilder
implements|implements
name|BundleActivator
block|{
comment|// Member collaborators
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|bundleContext
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|producerTemplate
specifier|protected
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|field|consumerTemplate
specifier|protected
name|ConsumerTemplate
name|consumerTemplate
decl_stmt|;
comment|// Lifecycle
annotation|@
name|Override
DECL|method|start (BundleContext bundleContext)
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Initializing bundle {}."
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|=
name|createCamelContext
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|ConfigurationAdmin
name|configurationAdmin
init|=
name|requiredService
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
decl_stmt|;
name|Configuration
name|camelKuraConfig
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
literal|"kura.camel"
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelKuraConfig
operator|!=
literal|null
operator|&&
name|camelKuraConfig
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|routePropertyValue
init|=
name|camelKuraConfig
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|camelXmlRoutesProperty
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|routePropertyValue
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|routesXml
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|routePropertyValue
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|RoutesDefinition
name|loadedRoutes
init|=
name|camelContext
operator|.
name|loadRoutesDefinition
argument_list|(
name|routesXml
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addRouteDefinitions
argument_list|(
name|loadedRoutes
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|beforeStart
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"About to start Camel Kura router: {}"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|producerTemplate
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|consumerTemplate
operator|=
name|camelContext
operator|.
name|createConsumerTemplate
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Bundle {} started."
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|String
name|errorMessage
init|=
literal|"Problem when starting Kura module "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|":"
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|errorMessage
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// Print error to the Kura console.
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop (BundleContext bundleContext)
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Stopping bundle {}."
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Bundle {} stopped."
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Callbacks
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
block|{
return|return
operator|new
name|OsgiDefaultCamelContext
argument_list|(
name|bundleContext
argument_list|)
return|;
block|}
DECL|method|beforeStart (CamelContext camelContext)
specifier|protected
name|void
name|beforeStart
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Empty KuraRouter CamelContext before start configuration - skipping."
argument_list|)
expr_stmt|;
block|}
comment|// API Helpers
DECL|method|service (Class<T> serviceType)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|service
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|serviceType
parameter_list|)
block|{
name|ServiceReference
name|reference
init|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|serviceType
argument_list|)
decl_stmt|;
return|return
name|reference
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|T
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
return|;
block|}
DECL|method|requiredService (Class<T> serviceType)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requiredService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|serviceType
parameter_list|)
block|{
name|ServiceReference
name|reference
init|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|serviceType
argument_list|)
decl_stmt|;
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find service: "
operator|+
name|serviceType
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|(
name|T
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
return|;
block|}
comment|// Private helpers
DECL|method|camelXmlRoutesProperty ()
specifier|private
name|String
name|camelXmlRoutesProperty
parameter_list|()
block|{
return|return
literal|"kura.camel."
operator|+
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|".route"
return|;
block|}
block|}
end_class

end_unit

