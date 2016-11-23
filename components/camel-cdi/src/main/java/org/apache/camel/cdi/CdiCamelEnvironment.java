begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Annotated
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionTarget
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Producer
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
name|core
operator|.
name|osgi
operator|.
name|utils
operator|.
name|BundleContextUtils
import|;
end_import

begin_class
annotation|@
name|Vetoed
DECL|class|CdiCamelEnvironment
specifier|final
class|class
name|CdiCamelEnvironment
block|{
DECL|field|hasBundleContext
specifier|private
specifier|final
name|boolean
name|hasBundleContext
decl_stmt|;
DECL|method|CdiCamelEnvironment ()
name|CdiCamelEnvironment
parameter_list|()
block|{
name|hasBundleContext
operator|=
name|isCamelCoreOsgiPresent
argument_list|()
operator|&&
name|hasBundleContext
argument_list|(
name|CdiCamelExtension
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|camelContextProducer (Producer<T> delegate, Annotated annotated, BeanManager manager, CdiCamelExtension extension)
parameter_list|<
name|T
extends|extends
name|CamelContext
parameter_list|>
name|Producer
argument_list|<
name|T
argument_list|>
name|camelContextProducer
parameter_list|(
name|Producer
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|Annotated
name|annotated
parameter_list|,
name|BeanManager
name|manager
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
name|CamelContextProducer
argument_list|<
name|T
argument_list|>
name|producer
init|=
operator|new
name|CamelContextProducer
argument_list|<>
argument_list|(
name|delegate
argument_list|,
name|annotated
argument_list|,
name|manager
argument_list|,
name|extension
argument_list|)
decl_stmt|;
return|return
name|hasBundleContext
condition|?
operator|new
name|CamelContextOsgiProducer
argument_list|<>
argument_list|(
name|producer
argument_list|)
else|:
name|producer
return|;
block|}
DECL|method|camelContextInjectionTarget (InjectionTarget<T> delegate, Annotated annotated, BeanManager manager, CdiCamelExtension extension)
parameter_list|<
name|T
extends|extends
name|CamelContext
parameter_list|>
name|InjectionTarget
argument_list|<
name|T
argument_list|>
name|camelContextInjectionTarget
parameter_list|(
name|InjectionTarget
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|Annotated
name|annotated
parameter_list|,
name|BeanManager
name|manager
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
name|CamelContextProducer
argument_list|<
name|T
argument_list|>
name|producer
init|=
operator|new
name|CamelContextProducer
argument_list|<>
argument_list|(
name|delegate
argument_list|,
name|annotated
argument_list|,
name|manager
argument_list|,
name|extension
argument_list|)
decl_stmt|;
return|return
operator|new
name|CamelContextInjectionTarget
argument_list|<>
argument_list|(
name|delegate
argument_list|,
name|hasBundleContext
condition|?
operator|new
name|CamelContextOsgiProducer
argument_list|<>
argument_list|(
name|producer
argument_list|)
else|:
name|producer
argument_list|)
return|;
block|}
DECL|method|isCamelCoreOsgiPresent ()
specifier|private
specifier|static
name|boolean
name|isCamelCoreOsgiPresent
parameter_list|()
block|{
try|try
block|{
name|getClassLoader
argument_list|(
name|CdiCamelExtension
operator|.
name|class
argument_list|)
operator|.
name|loadClass
argument_list|(
literal|"org.apache.camel.core.osgi.OsgiCamelContextHelper"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
decl||
name|NoClassDefFoundError
name|cause
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|hasBundleContext (Class clazz)
specifier|private
specifier|static
name|boolean
name|hasBundleContext
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
return|return
name|BundleContextUtils
operator|.
name|getBundleContext
argument_list|(
name|clazz
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|getClassLoader (Class<?> clazz)
specifier|private
specifier|static
name|ClassLoader
name|getClassLoader
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|clazz
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

