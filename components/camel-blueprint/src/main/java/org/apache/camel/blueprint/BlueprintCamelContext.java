begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
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
name|OsgiCamelContextHelper
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
name|OsgiCamelContextNameStrategy
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
name|OsgiClassResolver
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
name|OsgiFactoryFinderResolver
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
name|OsgiPackageScanClassResolver
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
name|OsgiTypeConverter
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
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
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
DECL|class|BlueprintCamelContext
specifier|public
class|class
name|BlueprintCamelContext
extends|extends
name|DefaultCamelContext
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BlueprintCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|blueprintContainer
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
DECL|method|BlueprintCamelContext ()
specifier|public
name|BlueprintCamelContext
parameter_list|()
block|{     }
DECL|method|BlueprintCamelContext (BundleContext bundleContext, BlueprintContainer blueprintContainer)
specifier|public
name|BlueprintCamelContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
name|setNameStrategy
argument_list|(
operator|new
name|OsgiCamelContextNameStrategy
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setClassResolver
argument_list|(
operator|new
name|OsgiClassResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setFactoryFinderResolver
argument_list|(
operator|new
name|OsgiFactoryFinderResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setPackageScanClassResolver
argument_list|(
operator|new
name|OsgiPackageScanClassResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setComponentResolver
argument_list|(
operator|new
name|BlueprintComponentResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setLanguageResolver
argument_list|(
operator|new
name|BlueprintLanguageResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
name|setDataFormatResolver
argument_list|(
operator|new
name|BlueprintDataFormatResolver
argument_list|(
name|bundleContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
DECL|method|getBlueprintContainer ()
specifier|public
name|BlueprintContainer
name|getBlueprintContainer
parameter_list|()
block|{
return|return
name|blueprintContainer
return|;
block|}
DECL|method|setBlueprintContainer (BlueprintContainer blueprintContainer)
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|maybeStart
argument_list|()
expr_stmt|;
block|}
DECL|method|maybeStart ()
specifier|private
name|void
name|maybeStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|isStarted
argument_list|()
operator|&&
operator|!
name|isStarting
argument_list|()
condition|)
block|{
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// ignore as Camel is already started
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring maybeStart() as Apache Camel is already started"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|stop
argument_list|()
expr_stmt|;
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
name|getInjector
argument_list|()
argument_list|,
name|finder
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|Registry
name|createRegistry
parameter_list|()
block|{
name|Registry
name|reg
init|=
operator|new
name|BlueprintContainerRegistry
argument_list|(
name|getBlueprintContainer
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|OsgiCamelContextHelper
operator|.
name|wrapRegistry
argument_list|(
name|this
argument_list|,
name|reg
argument_list|,
name|bundleContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

