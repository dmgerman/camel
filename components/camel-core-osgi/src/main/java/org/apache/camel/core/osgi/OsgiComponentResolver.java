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
name|spi
operator|.
name|ComponentResolver
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
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
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

begin_class
DECL|class|OsgiComponentResolver
specifier|public
class|class
name|OsgiComponentResolver
implements|implements
name|ComponentResolver
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
name|OsgiComponentResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|OsgiComponentResolver (BundleContext bundleContext)
specifier|public
name|OsgiComponentResolver
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
DECL|method|resolveComponent (String name, CamelContext context)
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|bean
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bean
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
operator|&&
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
literal|"Found component: "
operator|+
name|name
operator|+
literal|" in registry: "
operator|+
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignored error looking up bean: "
operator|+
name|name
operator|+
literal|". Error: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bean
operator|instanceof
name|Component
condition|)
block|{
return|return
operator|(
name|Component
operator|)
name|bean
return|;
block|}
comment|// Check in OSGi bundles
return|return
name|getComponent
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
block|}
DECL|method|getComponent (String name, CamelContext context)
specifier|protected
name|Component
name|getComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Finding Component: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|ServiceReference
index|[]
name|refs
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|ComponentResolver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"(component="
operator|+
name|name
operator|+
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|!=
literal|null
operator|&&
name|refs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|ComponentResolver
name|resolver
init|=
operator|(
name|ComponentResolver
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|refs
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
name|resolver
operator|.
name|resolveComponent
argument_list|(
name|name
argument_list|,
name|context
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

