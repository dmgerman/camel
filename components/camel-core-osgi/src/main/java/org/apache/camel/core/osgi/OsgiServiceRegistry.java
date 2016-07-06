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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Queue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentLinkedQueue
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
name|LifecycleStrategySupport
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
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_comment
comment|/**  * The OsgiServiceRegistry support to get the service object from the bundle context  */
end_comment

begin_class
DECL|class|OsgiServiceRegistry
specifier|public
class|class
name|OsgiServiceRegistry
extends|extends
name|LifecycleStrategySupport
implements|implements
name|Registry
block|{
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|serviceReferenceQueue
specifier|private
specifier|final
name|Queue
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
name|serviceReferenceQueue
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|OsgiServiceRegistry (BundleContext bc)
specifier|public
name|OsgiServiceRegistry
parameter_list|(
name|BundleContext
name|bc
parameter_list|)
block|{
name|bundleContext
operator|=
name|bc
expr_stmt|;
block|}
comment|/**      * Support to lookup the Object with filter with the (name=NAME) and class type      */
DECL|method|lookupByNameAndType (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookupByNameAndType
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|service
init|=
literal|null
decl_stmt|;
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|sr
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|refs
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|,
literal|"(name="
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
comment|// just return the first one
name|sr
operator|=
name|refs
index|[
literal|0
index|]
expr_stmt|;
name|serviceReferenceQueue
operator|.
name|add
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|service
operator|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|sr
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|service
argument_list|)
return|;
block|}
comment|/**      * It's only support to look up the ServiceReference with Class name      */
DECL|method|lookupByName (String name)
specifier|public
name|Object
name|lookupByName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|service
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|sr
init|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|sr
operator|!=
literal|null
condition|)
block|{
comment|// Need to keep the track of Service
comment|// and call ungetService when the camel context is closed
name|serviceReferenceQueue
operator|.
name|add
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|service
operator|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|sr
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|service
return|;
block|}
DECL|method|findByTypeWithName (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|findByTypeWithName
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
try|try
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|refs
init|=
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|sr
range|:
name|refs
control|)
block|{
if|if
condition|(
name|sr
operator|!=
literal|null
condition|)
block|{
name|Object
name|service
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|sr
argument_list|)
decl_stmt|;
name|serviceReferenceQueue
operator|.
name|add
argument_list|(
name|sr
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|sr
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type
operator|.
name|cast
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// generate a unique name for it
name|result
operator|.
name|put
argument_list|(
name|type
operator|.
name|getSimpleName
argument_list|()
operator|+
name|count
argument_list|,
name|type
operator|.
name|cast
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
DECL|method|findByType (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|findByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|map
init|=
name|findByTypeWithName
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|HashSet
argument_list|<
name|T
argument_list|>
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
DECL|method|lookup (String name)
specifier|public
name|Object
name|lookup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|lookupByName
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|lookup (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|lookupByNameAndType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|lookupByType (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|lookupByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|findByTypeWithName
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|onContextStop (CamelContext context)
specifier|public
name|void
name|onContextStop
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
comment|// Unget the OSGi service
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|sr
init|=
name|serviceReferenceQueue
operator|.
name|poll
argument_list|()
decl_stmt|;
while|while
condition|(
name|sr
operator|!=
literal|null
condition|)
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|sr
operator|=
name|serviceReferenceQueue
operator|.
name|poll
argument_list|()
expr_stmt|;
block|}
comment|// Clean up the OSGi Service Cache
name|serviceReferenceQueue
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

