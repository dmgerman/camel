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
name|util
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
name|util
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

begin_comment
comment|/**  * Default implementation of a type converter registry used for  *<a href="http://activemq.apache.org/camel/type-converter.html">type converters</a> in Camel.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultTypeConverter
specifier|public
class|class
name|DefaultTypeConverter
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
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
DECL|field|typeConverterLoaders
specifier|private
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
DECL|field|loaded
specifier|private
name|boolean
name|loaded
decl_stmt|;
DECL|method|DefaultTypeConverter (Injector injector)
specifier|public
name|DefaultTypeConverter
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|typeConverterLoaders
operator|.
name|add
argument_list|(
operator|new
name|AnnotationTypeConverterLoader
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
name|addFallbackConverter
argument_list|(
operator|new
name|AsyncProcessorTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|addFallbackConverter
argument_list|(
operator|new
name|PropertyEditorTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|addFallbackConverter
argument_list|(
operator|new
name|ToStringTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|addFallbackConverter
argument_list|(
operator|new
name|ArrayTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
name|addFallbackConverter
argument_list|(
operator|new
name|EnumTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// make sure we have loaded the converters
name|checkLoaded
argument_list|()
expr_stmt|;
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
return|return
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
return|;
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
name|T
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
operator|(
name|T
operator|)
name|Boolean
operator|.
name|FALSE
return|;
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
operator|(
name|T
operator|)
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
literal|"Could not find a type converter for converting "
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
return|return
literal|null
return|;
block|}
DECL|method|addTypeConverter (Class toType, Class fromType, TypeConverter typeConverter)
specifier|public
name|void
name|addTypeConverter
parameter_list|(
name|Class
name|toType
parameter_list|,
name|Class
name|fromType
parameter_list|,
name|TypeConverter
name|typeConverter
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
DECL|method|addFallbackConverter (TypeConverter converter)
specifier|public
name|void
name|addFallbackConverter
parameter_list|(
name|TypeConverter
name|converter
parameter_list|)
block|{
name|fallbackConverters
operator|.
name|add
argument_list|(
name|converter
argument_list|)
expr_stmt|;
if|if
condition|(
name|converter
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
name|converter
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
DECL|method|getTypeConverter (Class toType, Class fromType)
specifier|public
name|TypeConverter
name|getTypeConverter
parameter_list|(
name|Class
name|toType
parameter_list|,
name|Class
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
DECL|method|getOrFindTypeConverter (Class toType, Object value)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|TypeConverter
name|getOrFindTypeConverter
parameter_list|(
name|Class
name|toType
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Class
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
name|findTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|,
name|value
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
comment|/**      * Tries to auto-discover any available type converters      */
DECL|method|findTypeConverter (Class toType, Class fromType, Object value)
specifier|protected
name|TypeConverter
name|findTypeConverter
parameter_list|(
name|Class
name|toType
parameter_list|,
name|Class
name|fromType
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// lets try the super classes of the from type
if|if
condition|(
name|fromType
operator|!=
literal|null
condition|)
block|{
name|Class
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
name|TypeConverter
name|converter
init|=
name|getTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromSuperClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|converter
operator|==
literal|null
condition|)
block|{
name|converter
operator|=
name|findTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromSuperClass
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
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
for|for
control|(
name|Class
name|type
range|:
name|fromType
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|TypeConverter
name|converter
init|=
name|getTypeConverter
argument_list|(
name|toType
argument_list|,
name|type
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
comment|// lets test for arrays
if|if
condition|(
name|fromType
operator|.
name|isArray
argument_list|()
operator|&&
operator|!
name|fromType
operator|.
name|getComponentType
argument_list|()
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
comment|// TODO can we try walking the inheritance-tree for the element types?
if|if
condition|(
operator|!
name|fromType
operator|.
name|equals
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
condition|)
block|{
name|fromSuperClass
operator|=
name|Object
index|[]
operator|.
name|class
expr_stmt|;
name|TypeConverter
name|converter
init|=
name|getTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromSuperClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|converter
operator|==
literal|null
condition|)
block|{
name|converter
operator|=
name|findTypeConverter
argument_list|(
name|toType
argument_list|,
name|fromSuperClass
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
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
comment|// lets test for Object based converters
if|if
condition|(
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
comment|// lets try classes derived from this toType
if|if
condition|(
name|fromType
operator|!=
literal|null
condition|)
block|{
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
if|if
condition|(
name|key
operator|.
name|getFromType
argument_list|()
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
block|}
comment|// TODO look at constructors of toType?
return|return
literal|null
return|;
block|}
comment|/**      * Checks if the registry is loaded and if not lazily load it      */
DECL|method|checkLoaded ()
specifier|protected
specifier|synchronized
name|void
name|checkLoaded
parameter_list|()
block|{
if|if
condition|(
operator|!
name|loaded
condition|)
block|{
name|loaded
operator|=
literal|true
expr_stmt|;
try|try
block|{
for|for
control|(
name|TypeConverterLoader
name|typeConverterLoader
range|:
name|typeConverterLoaders
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
name|e
argument_list|)
throw|;
block|}
block|}
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
name|FactoryFinder
name|finder
init|=
operator|new
name|FactoryFinder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|TypeConverter
argument_list|>
name|converters
init|=
name|finder
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
name|addFallbackConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
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
name|toType
decl_stmt|;
DECL|field|fromType
name|Class
name|fromType
decl_stmt|;
DECL|method|TypeMapping (Class toType, Class fromType)
specifier|public
name|TypeMapping
parameter_list|(
name|Class
name|toType
parameter_list|,
name|Class
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
block|}
block|}
end_class

end_unit

