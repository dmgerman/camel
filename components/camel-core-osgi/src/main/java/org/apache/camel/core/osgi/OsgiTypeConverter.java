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
name|ArrayList
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
name|Collections
import|;
end_import

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
name|Set
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
name|Exchange
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
name|NoTypeConversionAvailableException
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
name|DefaultPackageScanClassResolver
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
name|Injector
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
name|TypeConverterLoader
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
name|TypeConverterRegistry
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
name|ServiceSupport
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
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTrackerCustomizer
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
DECL|class|OsgiTypeConverter
specifier|public
class|class
name|OsgiTypeConverter
extends|extends
name|ServiceSupport
implements|implements
name|TypeConverter
implements|,
name|TypeConverterRegistry
implements|,
name|ServiceTrackerCustomizer
argument_list|<
name|TypeConverterLoader
argument_list|,
name|Object
argument_list|>
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
name|OsgiTypeConverter
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
DECL|field|injector
specifier|private
specifier|final
name|Injector
name|injector
decl_stmt|;
DECL|field|factoryFinder
specifier|private
specifier|final
name|FactoryFinder
name|factoryFinder
decl_stmt|;
DECL|field|tracker
specifier|private
specifier|final
name|ServiceTracker
argument_list|<
name|TypeConverterLoader
argument_list|,
name|Object
argument_list|>
name|tracker
decl_stmt|;
DECL|field|delegate
specifier|private
specifier|volatile
name|DefaultTypeConverter
name|delegate
decl_stmt|;
DECL|method|OsgiTypeConverter (BundleContext bundleContext, Injector injector, FactoryFinder factoryFinder)
specifier|public
name|OsgiTypeConverter
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Injector
name|injector
parameter_list|,
name|FactoryFinder
name|factoryFinder
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
name|injector
operator|=
name|injector
expr_stmt|;
name|this
operator|.
name|factoryFinder
operator|=
name|factoryFinder
expr_stmt|;
name|this
operator|.
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|<
name|TypeConverterLoader
argument_list|,
name|Object
argument_list|>
argument_list|(
name|bundleContext
argument_list|,
name|TypeConverterLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|addingService (ServiceReference<TypeConverterLoader> serviceReference)
specifier|public
name|Object
name|addingService
parameter_list|(
name|ServiceReference
argument_list|<
name|TypeConverterLoader
argument_list|>
name|serviceReference
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"AddingService: {}"
argument_list|,
name|serviceReference
argument_list|)
expr_stmt|;
name|TypeConverterLoader
name|loader
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|serviceReference
argument_list|)
decl_stmt|;
comment|// just make sure we don't load the bundle converter this time
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|this
operator|.
name|delegate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error stopping service due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// It can force camel to reload the type converter again
name|this
operator|.
name|delegate
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|loader
return|;
block|}
DECL|method|modifiedService (ServiceReference<TypeConverterLoader> serviceReference, Object o)
specifier|public
name|void
name|modifiedService
parameter_list|(
name|ServiceReference
argument_list|<
name|TypeConverterLoader
argument_list|>
name|serviceReference
parameter_list|,
name|Object
name|o
parameter_list|)
block|{     }
DECL|method|removedService (ServiceReference<TypeConverterLoader> serviceReference, Object o)
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
argument_list|<
name|TypeConverterLoader
argument_list|>
name|serviceReference
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"RemovedService: {}"
argument_list|,
name|serviceReference
argument_list|)
expr_stmt|;
try|try
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|this
operator|.
name|delegate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error stopping service due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// It can force camel to reload the type converter again
name|this
operator|.
name|delegate
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|this
operator|.
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|allowNull ()
specifier|public
name|boolean
name|allowNull
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|allowNull
argument_list|()
return|;
block|}
DECL|method|convertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|tryConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|tryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|tryConvertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|tryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|addTypeConverter (Class<?> toType, Class<?> fromType, TypeConverter typeConverter)
specifier|public
name|void
name|addTypeConverter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
block|{
name|getDelegate
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|,
name|typeConverter
argument_list|)
expr_stmt|;
block|}
DECL|method|removeTypeConverter (Class<?> toType, Class<?> fromType)
specifier|public
name|boolean
name|removeTypeConverter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|removeTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|)
return|;
block|}
DECL|method|addFallbackTypeConverter (TypeConverter typeConverter, boolean canPromote)
specifier|public
name|void
name|addFallbackTypeConverter
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|boolean
name|canPromote
parameter_list|)
block|{
name|getDelegate
argument_list|()
operator|.
name|addFallbackTypeConverter
argument_list|(
name|typeConverter
argument_list|,
name|canPromote
argument_list|)
expr_stmt|;
block|}
DECL|method|lookup (Class<?> toType, Class<?> fromType)
specifier|public
name|TypeConverter
name|lookup
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|lookup
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|)
return|;
block|}
DECL|method|listAllTypeConvertersFromTo ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
index|[]
argument_list|>
name|listAllTypeConvertersFromTo
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|listAllTypeConvertersFromTo
argument_list|()
return|;
block|}
DECL|method|setInjector (Injector injector)
specifier|public
name|void
name|setInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|getDelegate
argument_list|()
operator|.
name|setInjector
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
DECL|method|getInjector ()
specifier|public
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getInjector
argument_list|()
return|;
block|}
DECL|method|getStatistics ()
specifier|public
name|Statistics
name|getStatistics
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getStatistics
argument_list|()
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|getDelegate ()
specifier|public
specifier|synchronized
name|DefaultTypeConverter
name|getDelegate
parameter_list|()
block|{
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
name|delegate
operator|=
name|createRegistry
argument_list|()
expr_stmt|;
block|}
return|return
name|delegate
return|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|DefaultTypeConverter
name|createRegistry
parameter_list|()
block|{
comment|// base the osgi type converter on the default type converter
name|DefaultTypeConverter
name|answer
init|=
operator|new
name|DefaultTypeConverter
argument_list|(
operator|new
name|DefaultPackageScanClassResolver
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|getClassLoaders
parameter_list|()
block|{
comment|// we don't need any classloaders as we use osgi service tracker instead
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
argument_list|,
name|injector
argument_list|,
name|factoryFinder
argument_list|)
decl_stmt|;
try|try
block|{
comment|// only load the core type converters, as osgi activator will keep track on bundles
comment|// being installed/uninstalled and load type converters as part of that process
name|answer
operator|.
name|loadCoreTypeConverters
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error loading CoreTypeConverter due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// Load the type converters the tracker has been tracking
comment|// Here we need to use the ServiceReference to check the ranking
name|ServiceReference
argument_list|<
name|TypeConverterLoader
argument_list|>
index|[]
name|serviceReferences
init|=
name|this
operator|.
name|tracker
operator|.
name|getServiceReferences
argument_list|()
decl_stmt|;
if|if
condition|(
name|serviceReferences
operator|!=
literal|null
condition|)
block|{
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|<
name|TypeConverterLoader
argument_list|>
argument_list|>
name|servicesList
init|=
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|<
name|TypeConverterLoader
argument_list|>
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|serviceReferences
argument_list|)
argument_list|)
decl_stmt|;
comment|// Just make sure we install the high ranking fallback converter at last
name|Collections
operator|.
name|sort
argument_list|(
name|servicesList
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceReference
argument_list|<
name|TypeConverterLoader
argument_list|>
name|sr
range|:
name|servicesList
control|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"loading the type converter from bundle{} "
argument_list|,
name|sr
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|TypeConverterLoader
operator|)
name|this
operator|.
name|tracker
operator|.
name|getService
argument_list|(
name|sr
argument_list|)
operator|)
operator|.
name|load
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error loading type converters from service: "
operator|+
name|sr
operator|+
literal|" due: "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|t
argument_list|)
throw|;
block|}
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Created TypeConverter: {}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

