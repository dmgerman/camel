begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

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
name|LoggingLevel
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
name|StaticService
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
name|meta
operator|.
name|Experimental
import|;
end_import

begin_comment
comment|// TODO: Add javadoc for methods
end_comment

begin_comment
comment|// TODO: Consolidate some of the methods
end_comment

begin_comment
comment|/**  * Used for introspecting beans properties via Java reflection; such as extracting current property values,  * or updating one or more properties etc.  *  * End users should favour using org.apache.camel.support.PropertyBindingSupport instead.  *<p/>  * Notice this API is not final yet  */
end_comment

begin_interface
annotation|@
name|Experimental
DECL|interface|BeanIntrospection
specifier|public
interface|interface
name|BeanIntrospection
extends|extends
name|StaticService
block|{
comment|/**      * Structure of an introspected class.      */
DECL|class|ClassInfo
specifier|final
class|class
name|ClassInfo
block|{
DECL|field|clazz
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
DECL|field|methods
specifier|public
name|MethodInfo
index|[]
name|methods
decl_stmt|;
block|}
comment|/**      * Structure of an introspected method.      */
DECL|class|MethodInfo
specifier|final
class|class
name|MethodInfo
block|{
DECL|field|method
specifier|public
name|Method
name|method
decl_stmt|;
DECL|field|isGetter
specifier|public
name|Boolean
name|isGetter
decl_stmt|;
DECL|field|isSetter
specifier|public
name|Boolean
name|isSetter
decl_stmt|;
DECL|field|getterOrSetterShorthandName
specifier|public
name|String
name|getterOrSetterShorthandName
decl_stmt|;
DECL|field|hasGetterAndSetter
specifier|public
name|Boolean
name|hasGetterAndSetter
decl_stmt|;
block|}
comment|// Statistics
comment|// ----------------------------------------------------
comment|/**      * Number of times bean introspection has been invoked      */
DECL|method|getInvokedCounter ()
name|long
name|getInvokedCounter
parameter_list|()
function_decl|;
comment|/**      * Reset the statistics counters.      */
DECL|method|resetCounters ()
name|void
name|resetCounters
parameter_list|()
function_decl|;
comment|/**      * Whether to gather extended statistics for introspection usage.      */
DECL|method|isExtendedStatistics ()
name|boolean
name|isExtendedStatistics
parameter_list|()
function_decl|;
comment|/**      * Whether to gather extended statistics for introspection usage.      */
DECL|method|setExtendedStatistics (boolean extendedStatistics)
name|void
name|setExtendedStatistics
parameter_list|(
name|boolean
name|extendedStatistics
parameter_list|)
function_decl|;
comment|/**      * Logging level used for logging introspection usage. Is using TRACE level as default.      */
DECL|method|getLoggingLevel ()
name|LoggingLevel
name|getLoggingLevel
parameter_list|()
function_decl|;
comment|/**      * Logging level used for logging introspection usage. Is using TRACE level as default.      */
DECL|method|setLoggingLevel (LoggingLevel loggingLevel)
name|void
name|setLoggingLevel
parameter_list|(
name|LoggingLevel
name|loggingLevel
parameter_list|)
function_decl|;
comment|// Introspection
comment|// ----------------------------------------------------
comment|/**      * Will inspect the target for properties.      *<p/>      * Notice a property must have both a getter/setter method to be included.      * Notice all<tt>null</tt> values will be included.      *      * @param target         the target bean      * @param properties     the map to fill in found properties      * @param optionPrefix   an optional prefix to append the property key      * @return<tt>true</tt> if any properties was found,<tt>false</tt> otherwise.      */
DECL|method|getProperties (Object target, Map<String, Object> properties, String optionPrefix)
name|boolean
name|getProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
function_decl|;
comment|/**      * Will inspect the target for properties.      *<p/>      * Notice a property must have both a getter/setter method to be included.      *      * @param target         the target bean      * @param properties     the map to fill in found properties      * @param optionPrefix   an optional prefix to append the property key      * @param includeNull    whether to include<tt>null</tt> values      * @return<tt>true</tt> if any properties was found,<tt>false</tt> otherwise.      */
DECL|method|getProperties (Object target, Map<String, Object> properties, String optionPrefix, boolean includeNull)
name|boolean
name|getProperties
parameter_list|(
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|,
name|boolean
name|includeNull
parameter_list|)
function_decl|;
comment|/**      * Introspects the given class.      *      * @param clazz the class      * @return the introspection result as a {@link ClassInfo} structure.      */
DECL|method|cacheClass (Class<?> clazz)
name|ClassInfo
name|cacheClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Clears the introspection cache.      */
DECL|method|clearCache ()
name|void
name|clearCache
parameter_list|()
function_decl|;
comment|/**      * Number of classes in the introspection cache.      */
DECL|method|getCachedClassesCounter ()
name|long
name|getCachedClassesCounter
parameter_list|()
function_decl|;
comment|/**      * Gets the property or else returning the default value.      *      * @param target         the target bean      * @param propertyName   the property name      * @param defaultValue   the default value      * @param ignoreCase     whether to ignore case for matching the property name      * @return the property value, or the default value if the target does not have a property with the given name      */
DECL|method|getOrElseProperty (Object target, String propertyName, Object defaultValue, boolean ignoreCase)
name|Object
name|getOrElseProperty
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|defaultValue
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
function_decl|;
comment|/**      * Gets the getter method for the property.      *      * @param type            the target class      * @param propertyName    the property name      * @param ignoreCase      whether to ignore case for matching the property name      * @return                the getter method      * @throws NoSuchMethodException  is thrown if there are no getter method for the property      */
DECL|method|getPropertyGetter (Class<?> type, String propertyName, boolean ignoreCase)
name|Method
name|getPropertyGetter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
throws|throws
name|NoSuchMethodException
function_decl|;
comment|/**      * This method supports three modes to set a property:      *      * 1. Setting a Map property where the property name refers to a map via name[aKey] where aKey is the map key to use.      *      * 2. Setting a property that has already been resolved, this is the case when {@code context} and {@code refName} are      * NULL and {@code value} is non-NULL.      *      * 3. Setting a property that has not yet been resolved, the property will be resolved based on the suitable methods      * found matching the property name on the {@code target} bean. For this mode to be triggered the parameters      * {@code context} and {@code refName} must NOT be NULL, and {@code value} MUST be NULL.      */
DECL|method|setProperty (CamelContext context, TypeConverter typeConverter, Object target, String name, Object value, String refName, boolean allowBuilderPattern)
name|boolean
name|setProperty
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|String
name|refName
parameter_list|,
name|boolean
name|allowBuilderPattern
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * This method supports three modes to set a property:      *      * 1. Setting a Map property where the property name refers to a map via name[aKey] where aKey is the map key to use.      *      * 2. Setting a property that has already been resolved, this is the case when {@code context} and {@code refName} are      * NULL and {@code value} is non-NULL.      *      * 3. Setting a property that has not yet been resolved, the property will be resolved based on the suitable methods      * found matching the property name on the {@code target} bean. For this mode to be triggered the parameters      * {@code context} and {@code refName} must NOT be NULL, and {@code value} MUST be NULL.      */
DECL|method|setProperty (CamelContext context, TypeConverter typeConverter, Object target, String name, Object value, String refName, boolean allowBuilderPattern, boolean allowPrivateSetter, boolean ignoreCase)
name|boolean
name|setProperty
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|String
name|refName
parameter_list|,
name|boolean
name|allowBuilderPattern
parameter_list|,
name|boolean
name|allowPrivateSetter
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|setProperty (CamelContext context, Object target, String name, Object value)
name|boolean
name|setProperty
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|setProperty (TypeConverter typeConverter, Object target, String name, Object value)
name|boolean
name|setProperty
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|findSetterMethods (Class<?> clazz, String name, boolean allowBuilderPattern, boolean allowPrivateSetter, boolean ignoreCase)
name|Set
argument_list|<
name|Method
argument_list|>
name|findSetterMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|allowBuilderPattern
parameter_list|,
name|boolean
name|allowPrivateSetter
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

