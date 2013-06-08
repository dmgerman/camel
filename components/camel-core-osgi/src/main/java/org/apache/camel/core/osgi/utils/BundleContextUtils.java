begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi.utils
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
operator|.
name|utils
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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|SortedMap
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
name|CamelContextHelper
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
name|LoadPropertiesException
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
name|Bundle
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

begin_comment
comment|/**  * Helper class  */
end_comment

begin_class
DECL|class|BundleContextUtils
specifier|public
specifier|final
class|class
name|BundleContextUtils
block|{
DECL|method|BundleContextUtils ()
specifier|private
name|BundleContextUtils
parameter_list|()
block|{     }
comment|/**      * Retrieve the BundleContext that the given class has been loaded from.      *      * @param clazz the class to find the bundle context from      * @return the bundle context or<code>null</code> if it can't be found      */
DECL|method|getBundleContext (Class<?> clazz)
specifier|public
specifier|static
name|BundleContext
name|getBundleContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
comment|// Ideally we should use FrameworkUtil.getBundle(clazz).getBundleContext()
comment|// but that does not exist in OSGi 4.1, so until we upgrade, we keep that one
try|try
block|{
name|ClassLoader
name|cl
init|=
name|clazz
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clClazz
init|=
name|cl
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Method
name|mth
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|clClazz
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|mth
operator|=
name|clClazz
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getBundle"
argument_list|)
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
name|clClazz
operator|=
name|clClazz
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|mth
operator|!=
literal|null
condition|)
block|{
name|mth
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|Bundle
operator|)
name|mth
operator|.
name|invoke
argument_list|(
name|cl
argument_list|)
operator|)
operator|.
name|getBundleContext
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Finds the components available on the bundle context and camel context      */
DECL|method|findComponents (BundleContext bundleContext, CamelContext camelContext)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findComponents
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|IOException
throws|,
name|LoadPropertiesException
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|answer
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
argument_list|()
decl_stmt|;
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|iter
init|=
name|bundle
operator|.
name|getResources
argument_list|(
name|CamelContextHelper
operator|.
name|COMPONENT_DESCRIPTOR
argument_list|)
decl_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|map
init|=
name|CamelContextHelper
operator|.
name|findComponents
argument_list|(
name|camelContext
argument_list|,
name|iter
argument_list|)
decl_stmt|;
name|answer
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

