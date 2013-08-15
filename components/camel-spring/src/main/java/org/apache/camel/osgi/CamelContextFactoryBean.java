begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
package|;
end_package

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
name|XmlRootElement
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
name|XmlTransient
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
name|OsgiCamelContextPublisher
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
name|OsgiEventAdminNotifier
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
name|SpringCamelContext
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
name|osgi
operator|.
name|context
operator|.
name|BundleContextAware
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"camelContext"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelContextFactoryBean
specifier|public
class|class
name|CamelContextFactoryBean
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|CamelContextFactoryBean
implements|implements
name|BundleContextAware
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
name|CamelContextFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|getBundleContext ()
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
DECL|method|setBundleContext (BundleContext bundleContext)
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using BundleContext: {}"
argument_list|,
name|bundleContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
DECL|method|createContext ()
specifier|protected
name|SpringCamelContext
name|createContext
parameter_list|()
block|{
name|SpringCamelContext
name|ctx
init|=
name|newCamelContext
argument_list|()
decl_stmt|;
comment|// only set the name if its explicit (Camel will auto assign name if none explicit set)
if|if
condition|(
operator|!
name|isImplicitId
argument_list|()
condition|)
block|{
name|ctx
operator|.
name|setName
argument_list|(
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ctx
return|;
block|}
DECL|method|newCamelContext ()
specifier|protected
name|SpringCamelContext
name|newCamelContext
parameter_list|()
block|{
return|return
operator|new
name|OsgiSpringCamelContext
argument_list|(
name|getApplicationContext
argument_list|()
argument_list|,
name|getBundleContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
operator|new
name|OsgiCamelContextPublisher
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
literal|"org.osgi.service.event.EventAdmin"
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
operator|new
name|OsgiEventAdminNotifier
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore, if the EventAdmin package is not available, just don't use it
name|LOG
operator|.
name|debug
argument_list|(
literal|"EventAdmin package is not available, just don't use it"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

