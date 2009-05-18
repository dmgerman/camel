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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|converter
operator|.
name|AnnotationTypeConverterLoader
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
name|converter
operator|.
name|DefaultTypeConverter
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
name|converter
operator|.
name|TypeConverterLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|BundleContextAware
import|;
end_import

begin_comment
comment|/**  * This factory just create a DefaultContext in OSGi without   * any spring application context involved.  */
end_comment

begin_class
DECL|class|CamelContextFactory
specifier|public
class|class
name|CamelContextFactory
implements|implements
name|BundleContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CamelContextFactory
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
DECL|method|createContext ()
specifier|public
name|DefaultCamelContext
name|createContext
parameter_list|()
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundleContext
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"The bundle context is not be null, let's setup the Osgi resolvers"
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|setFactoryFinderResolver
argument_list|(
operator|new
name|OsgiFactoryFinderResolver
argument_list|()
argument_list|)
expr_stmt|;
name|context
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
name|context
operator|.
name|setComponentResolver
argument_list|(
operator|new
name|OsgiComponentResolver
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setLanguageResolver
argument_list|(
operator|new
name|OsgiLanguageResolver
argument_list|()
argument_list|)
expr_stmt|;
name|addOsgiAnnotationTypeConverterLoader
argument_list|(
name|context
argument_list|,
name|bundleContext
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
DECL|method|addOsgiAnnotationTypeConverterLoader (DefaultCamelContext context, BundleContext bundleContext)
specifier|protected
name|void
name|addOsgiAnnotationTypeConverterLoader
parameter_list|(
name|DefaultCamelContext
name|context
parameter_list|,
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|DefaultTypeConverter
name|typeConverter
init|=
operator|(
name|DefaultTypeConverter
operator|)
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|TypeConverterLoader
argument_list|>
name|typeConverterLoaders
init|=
name|typeConverter
operator|.
name|getTypeConverterLoaders
argument_list|()
decl_stmt|;
comment|// Remove the AnnotationTypeConverterLoader
name|TypeConverterLoader
name|atLoader
init|=
literal|null
decl_stmt|;
for|for
control|(
name|TypeConverterLoader
name|loader
range|:
name|typeConverterLoaders
control|)
block|{
if|if
condition|(
name|loader
operator|instanceof
name|AnnotationTypeConverterLoader
condition|)
block|{
name|atLoader
operator|=
name|loader
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|atLoader
operator|!=
literal|null
condition|)
block|{
name|typeConverterLoaders
operator|.
name|remove
argument_list|(
name|atLoader
argument_list|)
expr_stmt|;
block|}
name|typeConverterLoaders
operator|.
name|add
argument_list|(
operator|new
name|OsgiAnnotationTypeConverterLoader
argument_list|(
name|context
operator|.
name|getPackageScanClassResolver
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"added the OsgiAnnotationTypeConverterLoader"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

