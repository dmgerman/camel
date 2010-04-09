begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|converter
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
name|List
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
name|ExecutionException
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
name|CamelExecutionException
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
name|NoFactoryAvailableException
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
name|PackageScanClassResolver
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
name|TypeConverterAware
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
name|StopWatch
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

begin_comment
comment|/**  * Default implementation of a type converter registry used for  *<a href="http://camel.apache.org/type-converter.html">type converters</a> in Camel.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultTypeConverter
specifier|public
class|class
name|DefaultTypeConverter
extends|extends
name|ServiceSupport
implements|implements
name|TypeConverter
implements|,
name|TypeConverterRegistry
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
name|DefaultTypeConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|typeMappings
specifier|private
specifier|final
name|Map
argument_list|<
name|TypeMapping
argument_list|,
name|TypeConverter
argument_list|>
name|typeMappings
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|TypeMapping
argument_list|,
name|TypeConverter
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|misses
specifier|private
specifier|final
name|Map
argument_list|<
name|TypeMapping
argument_list|,
name|TypeMapping
argument_list|>
name|misses
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|TypeMapping
argument_list|,
name|TypeMapping
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|typeConverterLoaders
specifier|private
specifier|final
name|List
argument_list|<
name|TypeConverterLoader
argument_list|>
name|typeConverterLoaders
init|=
operator|new
name|ArrayList
argument_list|<
name|TypeConverterLoader
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|fallbackConverters
specifier|private
specifier|final
name|List
argument_list|<
name|TypeConverter
argument_list|>
name|fallbackConverters
init|=
operator|new
name|ArrayList
argument_list|<
name|TypeConverter
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
DECL|field|factoryFinder
specifier|private
specifier|final
name|FactoryFinder
name|factoryFinder
decl_stmt|;
DECL|method|DefaultTypeConverter (PackageScanClassResolver resolver, Injector injector, FactoryFinder factoryFinder)
specifier|public
name|DefaultTypeConverter
parameter_list|(
name|PackageScanClassResolver
name|resolver
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
name|typeConverterLoaders
operator|.
name|add
argument_list|(
operator|new
name|AnnotationTypeConverterLoader
argument_list|(
name|resolver
argument_list|)
argument_list|)
expr_stmt|;
comment|// add to string first as it will then be last in the last as to string can nearly
comment|// always convert something to a string so we want it only as the last resort
name|addFallbackTypeConverter
argument_list|(
operator|new
name|ToStringTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|addFallbackTypeConverter
argument_list|(
operator|new
name|EnumTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|addFallbackTypeConverter
argument_list|(
operator|new
name|ArrayTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|addFallbackTypeConverter
argument_list|(
operator|new
name|PropertyEditorTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|addFallbackTypeConverter
argument_list|(
operator|new
name|FutureTypeConverter
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getTypeConverterLoaders ()
specifier|public
name|List
argument_list|<
name|TypeConverterLoader
argument_list|>
name|getTypeConverterLoaders
parameter_list|()
block|{
return|return
name|typeConverterLoaders
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
name|convertTo
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|Object
name|answer
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|doConvertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// if its a ExecutionException then we have rethrow it as its not due to failed conversion
name|boolean
name|execution
init|=
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|ExecutionException
operator|.
name|class
argument_list|,
name|e
argument_list|)
operator|!=
literal|null
operator|||
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|CamelExecutionException
operator|.
name|class
argument_list|,
name|e
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|execution
condition|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapCamelExecutionException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// we cannot convert so return null
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
name|NoTypeConversionAvailableException
operator|.
name|createMessage
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
operator|+
literal|" Caused by: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". Will ignore this and continue."
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|answer
operator|==
name|Void
operator|.
name|TYPE
condition|)
block|{
comment|// Could not find suitable conversion
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
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
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|Object
name|answer
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|doConvertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
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
name|NoTypeConversionAvailableException
argument_list|(
name|value
argument_list|,
name|type
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|answer
operator|==
name|Void
operator|.
name|TYPE
operator|||
name|value
operator|==
literal|null
condition|)
block|{
comment|// Could not find suitable conversion
throw|throw
operator|new
name|NoTypeConversionAvailableException
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
throw|;
block|}
else|else
block|{
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doConvertTo (final Class type, final Exchange exchange, final Object value)
specifier|public
name|Object
name|doConvertTo
parameter_list|(
specifier|final
name|Class
name|type
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
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
literal|"Converting "
operator|+
operator|(
name|value
operator|==
literal|null
condition|?
literal|"null"
else|:
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|)
operator|+
literal|" -> "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" with value: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// lets avoid NullPointerException when converting to boolean for null values
if|if
condition|(
name|boolean
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// same instance type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|// check if we have tried it before and if its a miss
name|TypeMapping
name|key
init|=
operator|new
name|TypeMapping
argument_list|(
name|type
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|misses
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|// we have tried before but we cannot convert this one
return|return
name|Void
operator|.
name|TYPE
return|;
block|}
comment|// try to find a suitable type converter
name|TypeConverter
name|converter
init|=
name|getOrFindTypeConverter
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
name|Object
name|rc
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
return|return
name|rc
return|;
block|}
block|}
comment|// fallback converters
for|for
control|(
name|TypeConverter
name|fallback
range|:
name|fallbackConverters
control|)
block|{
name|Object
name|rc
init|=
name|fallback
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|Void
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|rc
argument_list|)
condition|)
block|{
comment|// it cannot be converted so give up
return|return
name|Void
operator|.
name|TYPE
return|;
block|}
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
comment|// add it as a known type converter since we found a fallback that could do it
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
literal|"Adding fallback type converter as a known type converter to convert from: "
operator|+
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" to: "
operator|+
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addTypeConverter
argument_list|(
name|type
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|fallback
argument_list|)
expr_stmt|;
return|return
name|rc
return|;
block|}
block|}
comment|// primitives
if|if
condition|(
name|type
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|Class
name|primitiveType
init|=
name|ObjectHelper
operator|.
name|convertPrimitiveTypeToWrapperType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|primitiveType
operator|!=
name|type
condition|)
block|{
return|return
name|convertTo
argument_list|(
name|primitiveType
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
comment|// Could not find suitable conversion, so remember it
synchronized|synchronized
init|(
name|misses
init|)
block|{
name|misses
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
comment|// Could not find suitable conversion, so return Void to indicate not found
return|return
name|Void
operator|.
name|TYPE
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
literal|"Adding type converter: "
operator|+
name|typeConverter
argument_list|)
expr_stmt|;
block|}
name|TypeMapping
name|key
init|=
operator|new
name|TypeMapping
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|typeMappings
init|)
block|{
name|TypeConverter
name|converter
init|=
name|typeMappings
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|// only override it if its different
comment|// as race conditions can lead to many threads trying to promote the same fallback converter
if|if
condition|(
name|typeConverter
operator|!=
name|converter
condition|)
block|{
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Overriding type converter from: "
operator|+
name|converter
operator|+
literal|" to: "
operator|+
name|typeConverter
argument_list|)
expr_stmt|;
block|}
name|typeMappings
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|typeConverter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addFallbackTypeConverter (TypeConverter typeConverter)
specifier|public
name|void
name|addFallbackTypeConverter
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|)
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
literal|"Adding fallback type converter: "
operator|+
name|typeConverter
argument_list|)
expr_stmt|;
block|}
comment|// add in top of fallback as the toString() fallback will nearly always be able to convert
name|fallbackConverters
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|typeConverter
argument_list|)
expr_stmt|;
if|if
condition|(
name|typeConverter
operator|instanceof
name|TypeConverterAware
condition|)
block|{
name|TypeConverterAware
name|typeConverterAware
init|=
operator|(
name|TypeConverterAware
operator|)
name|typeConverter
decl_stmt|;
name|typeConverterAware
operator|.
name|setTypeConverter
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getTypeConverter (Class<?> toType, Class<?> fromType)
specifier|public
name|TypeConverter
name|getTypeConverter
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
name|TypeMapping
name|key
init|=
operator|new
name|TypeMapping
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|)
decl_stmt|;
return|return
name|typeMappings
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|getInjector ()
specifier|public
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|injector
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
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
block|}
DECL|method|getFromClassMappings ()
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getFromClassMappings
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|typeMappings
init|)
block|{
for|for
control|(
name|TypeMapping
name|mapping
range|:
name|typeMappings
operator|.
name|keySet
argument_list|()
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|mapping
operator|.
name|getFromType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getToClassMappings (Class<?> fromClass)
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|TypeConverter
argument_list|>
name|getToClassMappings
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|fromClass
parameter_list|)
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|TypeConverter
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|TypeConverter
argument_list|>
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|typeMappings
init|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|TypeMapping
argument_list|,
name|TypeConverter
argument_list|>
name|entry
range|:
name|typeMappings
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|TypeMapping
name|mapping
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|mapping
operator|.
name|isApplicable
argument_list|(
name|fromClass
argument_list|)
condition|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|mapping
operator|.
name|getToType
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getTypeMappings ()
specifier|public
name|Map
argument_list|<
name|TypeMapping
argument_list|,
name|TypeConverter
argument_list|>
name|getTypeMappings
parameter_list|()
block|{
return|return
name|typeMappings
return|;
block|}
DECL|method|getOrFindTypeConverter (Class<?> toType, Object value)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|TypeConverter
name|getOrFindTypeConverter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|fromType
operator|=
name|value
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
name|TypeMapping
name|key
init|=
operator|new
name|TypeMapping
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|)
decl_stmt|;
name|TypeConverter
name|converter
decl_stmt|;
synchronized|synchronized
init|(
name|typeMappings
init|)
block|{
name|converter
operator|=
name|typeMappings
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|converter
operator|==
literal|null
condition|)
block|{
name|converter
operator|=
name|lookup
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|)
expr_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
name|typeMappings
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|converter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|converter
return|;
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
name|doLookup
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|doLookup (Class<?> toType, Class<?> fromType, boolean isSuper)
specifier|private
name|TypeConverter
name|doLookup
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
name|boolean
name|isSuper
parameter_list|)
block|{
if|if
condition|(
name|fromType
operator|!=
literal|null
condition|)
block|{
comment|// lets try if there is a direct match
name|TypeConverter
name|converter
init|=
name|getTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|)
decl_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
return|return
name|converter
return|;
block|}
comment|// try the interfaces
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|fromType
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|converter
operator|=
name|getTypeConverter
argument_list|(
name|toType
argument_list|,
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
return|return
name|converter
return|;
block|}
block|}
comment|// try super then
name|Class
argument_list|<
name|?
argument_list|>
name|fromSuperClass
init|=
name|fromType
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|fromSuperClass
operator|!=
literal|null
operator|&&
operator|!
name|fromSuperClass
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
condition|)
block|{
name|converter
operator|=
name|doLookup
argument_list|(
name|toType
argument_list|,
name|fromSuperClass
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
return|return
name|converter
return|;
block|}
block|}
block|}
comment|// only do these tests as fallback and only on the target type (eg not on its super)
if|if
condition|(
operator|!
name|isSuper
condition|)
block|{
if|if
condition|(
name|fromType
operator|!=
literal|null
operator|&&
operator|!
name|fromType
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// lets try classes derived from this toType
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|TypeMapping
argument_list|,
name|TypeConverter
argument_list|>
argument_list|>
name|entries
init|=
name|typeMappings
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|TypeMapping
argument_list|,
name|TypeConverter
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|TypeMapping
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|aToType
init|=
name|key
operator|.
name|getToType
argument_list|()
decl_stmt|;
if|if
condition|(
name|toType
operator|.
name|isAssignableFrom
argument_list|(
name|aToType
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|aFromType
init|=
name|key
operator|.
name|getFromType
argument_list|()
decl_stmt|;
comment|// skip Object based we do them last
if|if
condition|(
operator|!
name|aFromType
operator|.
name|equals
argument_list|(
name|Object
operator|.
name|class
argument_list|)
operator|&&
name|aFromType
operator|.
name|isAssignableFrom
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
block|}
comment|// lets test for Object based converters as last resort
name|TypeConverter
name|converter
init|=
name|getTypeConverter
argument_list|(
name|toType
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
return|return
name|converter
return|;
block|}
block|}
block|}
comment|// none found
return|return
literal|null
return|;
block|}
comment|/**      * Checks if the registry is loaded and if not lazily load it      */
DECL|method|loadTypeConverters ()
specifier|protected
name|void
name|loadTypeConverters
parameter_list|()
throws|throws
name|Exception
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading type converters ..."
argument_list|)
expr_stmt|;
for|for
control|(
name|TypeConverterLoader
name|typeConverterLoader
range|:
name|getTypeConverterLoaders
argument_list|()
control|)
block|{
name|typeConverterLoader
operator|.
name|load
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|// lets try load any other fallback converters
try|try
block|{
name|loadFallbackTypeConverters
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoFactoryAvailableException
name|e
parameter_list|)
block|{
comment|// ignore its fine to have none
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading type converters done"
argument_list|)
expr_stmt|;
comment|// report how long time it took to load
name|LOG
operator|.
name|info
argument_list|(
literal|"Loaded "
operator|+
name|typeMappings
operator|.
name|size
argument_list|()
operator|+
literal|" type converters in "
operator|+
name|watch
operator|.
name|stop
argument_list|()
operator|+
literal|" millis"
argument_list|)
expr_stmt|;
block|}
DECL|method|loadFallbackTypeConverters ()
specifier|protected
name|void
name|loadFallbackTypeConverters
parameter_list|()
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|List
argument_list|<
name|TypeConverter
argument_list|>
name|converters
init|=
name|factoryFinder
operator|.
name|newInstances
argument_list|(
literal|"FallbackTypeConverter"
argument_list|,
name|getInjector
argument_list|()
argument_list|,
name|TypeConverter
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|TypeConverter
name|converter
range|:
name|converters
control|)
block|{
name|addFallbackTypeConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
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
name|loadTypeConverters
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
name|typeMappings
operator|.
name|clear
argument_list|()
expr_stmt|;
name|misses
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Represents a mapping from one type (which can be null) to another      */
DECL|class|TypeMapping
specifier|protected
specifier|static
class|class
name|TypeMapping
block|{
DECL|field|toType
name|Class
argument_list|<
name|?
argument_list|>
name|toType
decl_stmt|;
DECL|field|fromType
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
decl_stmt|;
DECL|method|TypeMapping (Class<?> toType, Class<?> fromType)
specifier|public
name|TypeMapping
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
name|this
operator|.
name|toType
operator|=
name|toType
expr_stmt|;
name|this
operator|.
name|fromType
operator|=
name|fromType
expr_stmt|;
block|}
DECL|method|getFromType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getFromType
parameter_list|()
block|{
return|return
name|fromType
return|;
block|}
DECL|method|getToType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getToType
parameter_list|()
block|{
return|return
name|toType
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|TypeMapping
condition|)
block|{
name|TypeMapping
name|that
init|=
operator|(
name|TypeMapping
operator|)
name|object
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|fromType
argument_list|,
name|that
operator|.
name|fromType
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|toType
argument_list|,
name|that
operator|.
name|toType
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|answer
init|=
name|toType
operator|.
name|hashCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|fromType
operator|!=
literal|null
condition|)
block|{
name|answer
operator|*=
literal|37
operator|+
name|fromType
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"["
operator|+
name|fromType
operator|+
literal|"=>"
operator|+
name|toType
operator|+
literal|"]"
return|;
block|}
DECL|method|isApplicable (Class<?> fromClass)
specifier|public
name|boolean
name|isApplicable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|fromClass
parameter_list|)
block|{
return|return
name|fromType
operator|.
name|isAssignableFrom
argument_list|(
name|fromClass
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

