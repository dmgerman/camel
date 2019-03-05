begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|LoadPropertiesException
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
name|TypeConverter
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
name|utils
operator|.
name|BundleContextUtils
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
name|utils
operator|.
name|BundleDelegatingClassLoader
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
name|spi
operator|.
name|BeanRepository
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
name|FactoryFinder
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
name|Registry
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
name|DefaultRegistry
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

begin_class
DECL|class|OsgiDefaultCamelContext
specifier|public
class|class
name|OsgiDefaultCamelContext
extends|extends
name|DefaultCamelContext
block|{
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|OsgiDefaultCamelContext (BundleContext bundleContext)
specifier|public
name|OsgiDefaultCamelContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
comment|// inject common osgi
name|OsgiCamelContextHelper
operator|.
name|osgiUpdate
argument_list|(
name|this
argument_list|,
name|bundleContext
argument_list|)
expr_stmt|;
comment|// and these are blueprint specific
name|OsgiBeanRepository
name|repo1
init|=
operator|new
name|OsgiBeanRepository
argument_list|(
name|bundleContext
argument_list|)
decl_stmt|;
name|setRegistry
argument_list|(
operator|new
name|DefaultRegistry
argument_list|(
name|repo1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Need to clean up the OSGi service when camel context is closed.
name|addLifecycleStrategy
argument_list|(
name|repo1
argument_list|)
expr_stmt|;
comment|// setup the application context classloader with the bundle classloader
name|setApplicationContextClassLoader
argument_list|(
operator|new
name|BundleDelegatingClassLoader
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|findComponents ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findComponents
parameter_list|()
throws|throws
name|LoadPropertiesException
throws|,
name|IOException
block|{
return|return
name|BundleContextUtils
operator|.
name|findComponents
argument_list|(
name|bundleContext
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createTypeConverter ()
specifier|protected
name|TypeConverter
name|createTypeConverter
parameter_list|()
block|{
comment|// CAMEL-3614: make sure we use a bundle context which imports org.apache.camel.impl.converter package
name|BundleContext
name|ctx
init|=
name|BundleContextUtils
operator|.
name|getBundleContext
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
name|ctx
operator|=
name|bundleContext
expr_stmt|;
block|}
name|FactoryFinder
name|finder
init|=
operator|new
name|OsgiFactoryFinderResolver
argument_list|(
name|bundleContext
argument_list|)
operator|.
name|resolveDefaultFactoryFinder
argument_list|(
name|getClassResolver
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|OsgiTypeConverter
argument_list|(
name|ctx
argument_list|,
name|this
argument_list|,
name|getInjector
argument_list|()
argument_list|,
name|finder
argument_list|)
return|;
block|}
block|}
end_class

end_unit
