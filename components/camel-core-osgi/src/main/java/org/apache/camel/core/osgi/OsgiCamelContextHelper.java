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
name|support
operator|.
name|DefaultRegistry
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
name|ObjectHelper
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

begin_class
DECL|class|OsgiCamelContextHelper
specifier|public
specifier|final
class|class
name|OsgiCamelContextHelper
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
name|OsgiCamelContextHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|OsgiCamelContextHelper ()
specifier|private
name|OsgiCamelContextHelper
parameter_list|()
block|{
comment|// helper class
block|}
DECL|method|osgiUpdate (DefaultCamelContext camelContext, BundleContext bundleContext, OsgiBeanRepository beanRepository)
specifier|public
specifier|static
name|void
name|osgiUpdate
parameter_list|(
name|DefaultCamelContext
name|camelContext
parameter_list|,
name|BundleContext
name|bundleContext
parameter_list|,
name|OsgiBeanRepository
name|beanRepository
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|bundleContext
argument_list|,
literal|"BundleContext"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiBeanRepository"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setRegistry
argument_list|(
operator|new
name|DefaultRegistry
argument_list|(
name|beanRepository
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiCamelContextNameStrategy"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setNameStrategy
argument_list|(
operator|new
name|OsgiCamelContextNameStrategy
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiManagementNameStrategy"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setManagementNameStrategy
argument_list|(
operator|new
name|OsgiManagementNameStrategy
argument_list|(
name|camelContext
argument_list|,
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiClassResolver"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setClassResolver
argument_list|(
operator|new
name|OsgiClassResolver
argument_list|(
name|camelContext
argument_list|,
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiFactoryFinderResolver"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setFactoryFinderResolver
argument_list|(
operator|new
name|OsgiFactoryFinderResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiPackageScanClassResolver"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setPackageScanClassResolver
argument_list|(
operator|new
name|OsgiPackageScanClassResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiComponentResolver"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setComponentResolver
argument_list|(
operator|new
name|OsgiComponentResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiLanguageResolver"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setLanguageResolver
argument_list|(
operator|new
name|OsgiLanguageResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OsgiDataFormatResolver"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setDataFormatResolver
argument_list|(
operator|new
name|OsgiDataFormatResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
comment|// Need to clean up the OSGi service when camel context is closed.
name|camelContext
operator|.
name|addLifecycleStrategy
argument_list|(
name|beanRepository
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

