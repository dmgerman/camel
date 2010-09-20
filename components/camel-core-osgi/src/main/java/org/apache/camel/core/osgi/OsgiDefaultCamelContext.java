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
DECL|field|registry
specifier|private
specifier|final
name|Registry
name|registry
decl_stmt|;
DECL|method|OsgiDefaultCamelContext (BundleContext bundleContext)
specifier|public
name|OsgiDefaultCamelContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
argument_list|(
name|bundleContext
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|OsgiDefaultCamelContext (BundleContext bundleContext, Registry registry)
specifier|public
name|OsgiDefaultCamelContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Registry
name|registry
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
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|OsgiCamelContextHelper
operator|.
name|osgiUpdate
argument_list|(
name|this
argument_list|,
name|bundleContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|Registry
name|createRegistry
parameter_list|()
block|{
if|if
condition|(
name|registry
operator|!=
literal|null
condition|)
block|{
return|return
name|OsgiCamelContextHelper
operator|.
name|wrapRegistry
argument_list|(
name|this
argument_list|,
name|registry
argument_list|,
name|bundleContext
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|OsgiCamelContextHelper
operator|.
name|wrapRegistry
argument_list|(
name|this
argument_list|,
name|super
operator|.
name|createRegistry
argument_list|()
argument_list|,
name|bundleContext
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createTypeConverter ()
specifier|protected
name|TypeConverter
name|createTypeConverter
parameter_list|()
block|{
return|return
operator|new
name|OsgiTypeConverter
argument_list|(
name|bundleContext
argument_list|,
name|getInjector
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

