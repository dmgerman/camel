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
name|ConcurrentHashMap
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
name|atomic
operator|.
name|AtomicLong
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
name|RuntimeCamelException
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
name|Service
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
name|Constants
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
name|ServiceEvent
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
name|ServiceListener
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
implements|,
name|Service
implements|,
name|ServiceListener
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
name|OsgiServiceRegistry
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
DECL|field|serviceReferenceUsageMap
specifier|private
specifier|final
name|Map
argument_list|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
argument_list|,
name|AtomicLong
argument_list|>
name|serviceReferenceUsageMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
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
name|bundleContext
operator|.
name|addServiceListener
argument_list|(
name|this
argument_list|)
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
name|incrementServiceUsage
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
name|RuntimeCamelException
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
comment|/**      * It's only support to look up the ServiceReference with Class name or service PID      */
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
operator|==
literal|null
condition|)
block|{
comment|// trying to lookup service by PID if not found by name
name|String
name|filterExpression
init|=
literal|"("
operator|+
name|Constants
operator|.
name|SERVICE_PID
operator|+
literal|"="
operator|+
name|name
operator|+
literal|")"
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
operator|(
name|String
operator|)
literal|null
argument_list|,
name|filterExpression
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
block|}
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|ex
parameter_list|)
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
literal|"Invalid OSGi service reference filter, skipped lookup by service.pid. Filter expression: {}"
argument_list|,
name|filterExpression
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|sr
operator|!=
literal|null
condition|)
block|{
name|incrementServiceUsage
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
argument_list|<>
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
name|incrementServiceUsage
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
name|RuntimeCamelException
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
argument_list|<>
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Unget the OSGi service as OSGi uses reference counting
comment|// and we should do this as one of the last actions when stopping Camel
name|this
operator|.
name|serviceReferenceUsageMap
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|drainServiceUsage
argument_list|)
expr_stmt|;
name|this
operator|.
name|serviceReferenceUsageMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|drainServiceUsage (ServiceReference<?> serviceReference, AtomicLong serviceUsageCount)
name|void
name|drainServiceUsage
parameter_list|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|serviceReference
parameter_list|,
name|AtomicLong
name|serviceUsageCount
parameter_list|)
block|{
if|if
condition|(
name|serviceUsageCount
operator|!=
literal|null
operator|&&
name|serviceReference
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|serviceUsageCount
operator|.
name|decrementAndGet
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|this
operator|.
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|serviceReference
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|incrementServiceUsage (ServiceReference<?> sr)
name|void
name|incrementServiceUsage
parameter_list|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|sr
parameter_list|)
block|{
name|AtomicLong
name|serviceUsageCount
init|=
name|this
operator|.
name|serviceReferenceUsageMap
operator|.
name|get
argument_list|(
name|sr
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceUsageCount
operator|!=
literal|null
condition|)
block|{
name|serviceUsageCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|serviceReferenceUsageMap
operator|.
name|merge
argument_list|(
name|sr
argument_list|,
operator|new
name|AtomicLong
argument_list|(
literal|1
argument_list|)
argument_list|,
parameter_list|(
name|existingServiceUsageCount
parameter_list|,
name|newServiceUsageCount
parameter_list|)
lambda|->
block|{
name|existingServiceUsageCount
operator|.
name|getAndAdd
argument_list|(
name|newServiceUsageCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|existingServiceUsageCount
return|;
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|serviceChanged (ServiceEvent event)
specifier|public
name|void
name|serviceChanged
parameter_list|(
name|ServiceEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|ServiceEvent
operator|.
name|UNREGISTERING
condition|)
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|serviceReference
init|=
name|event
operator|.
name|getServiceReference
argument_list|()
decl_stmt|;
name|AtomicLong
name|serviceUsageCount
init|=
name|this
operator|.
name|serviceReferenceUsageMap
operator|.
name|remove
argument_list|(
name|serviceReference
argument_list|)
decl_stmt|;
name|drainServiceUsage
argument_list|(
name|serviceReference
argument_list|,
name|serviceUsageCount
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

