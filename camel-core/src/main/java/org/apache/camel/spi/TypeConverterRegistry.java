begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|List
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
name|TypeConverterExists
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
name|TypeConverters
import|;
end_import

begin_comment
comment|/**  * Registry for type converters.  *<p/>  * The utilization {@link Statistics} is by default disabled, as it has a slight performance impact under very high  * concurrent load. The statistics can be enabled using {@link Statistics#setStatisticsEnabled(boolean)} method.  *  * @version   */
end_comment

begin_interface
DECL|interface|TypeConverterRegistry
specifier|public
interface|interface
name|TypeConverterRegistry
extends|extends
name|StaticService
block|{
comment|/**      * Utilization statistics of the this registry.      */
DECL|interface|Statistics
interface|interface
name|Statistics
block|{
comment|/**          * Number of noop attempts (no type conversion was needed)          */
DECL|method|getNoopCounter ()
name|long
name|getNoopCounter
parameter_list|()
function_decl|;
comment|/**          * Number of type conversion attempts          */
DECL|method|getAttemptCounter ()
name|long
name|getAttemptCounter
parameter_list|()
function_decl|;
comment|/**          * Number of successful conversions          */
DECL|method|getHitCounter ()
name|long
name|getHitCounter
parameter_list|()
function_decl|;
comment|/**          * Number of successful conversions by optimised core converters          */
DECL|method|getBaseHitCounter ()
name|long
name|getBaseHitCounter
parameter_list|()
function_decl|;
comment|/**          * Number of attempts which cannot be converted as no suitable type converter exists          */
DECL|method|getMissCounter ()
name|long
name|getMissCounter
parameter_list|()
function_decl|;
comment|/**          * Number of failed attempts during type conversion          */
DECL|method|getFailedCounter ()
name|long
name|getFailedCounter
parameter_list|()
function_decl|;
comment|/**          * Reset the counters          */
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**          * Whether statistics is enabled.          */
DECL|method|isStatisticsEnabled ()
name|boolean
name|isStatisticsEnabled
parameter_list|()
function_decl|;
comment|/**          * Sets whether statistics is enabled.          *          * @param statisticsEnabled<tt>true</tt> to enable          */
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
function_decl|;
block|}
comment|/**      * Registers a new type converter.      *<p/>      * This method may throw {@link org.apache.camel.TypeConverterExistsException} if configured to fail if an existing      * type converter already exists      *      * @param toType        the type to convert to      * @param fromType      the type to convert from      * @param typeConverter the type converter to use      */
DECL|method|addTypeConverter (Class<?> toType, Class<?> fromType, TypeConverter typeConverter)
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
function_decl|;
comment|/**      * Removes the type converter      *      * @param toType        the type to convert to      * @param fromType      the type to convert from      * @return<tt>true</tt> if removed,<tt>false</tt> if the type converter didn't exist      */
DECL|method|removeTypeConverter (Class<?> toType, Class<?> fromType)
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
function_decl|;
comment|/**      * Registers all the type converters from the class, each converter must be implemented as a method and annotated with {@link org.apache.camel.Converter}.      *      * @param typeConverters class which implements the type converters      */
DECL|method|addTypeConverters (TypeConverters typeConverters)
name|void
name|addTypeConverters
parameter_list|(
name|TypeConverters
name|typeConverters
parameter_list|)
function_decl|;
comment|/**      * Registers a new fallback type converter      *      * @param typeConverter the type converter to use      * @param canPromote  whether or not the fallback type converter can be promoted to a first class type converter      */
DECL|method|addFallbackTypeConverter (TypeConverter typeConverter, boolean canPromote)
name|void
name|addFallbackTypeConverter
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|boolean
name|canPromote
parameter_list|)
function_decl|;
comment|/**      * Performs a lookup for a given type converter.      *      * @param toType        the type to convert to      * @param fromType      the type to convert from      * @return the type converter or<tt>null</tt> if not found.      */
DECL|method|lookup (Class<?> toType, Class<?> fromType)
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
function_decl|;
comment|/**      * Gets a read-only list of the type converter from / to classes      *      * @return a list containing fromType/toType class names      */
DECL|method|listAllTypeConvertersFromTo ()
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
function_decl|;
comment|/**      * Sets the injector to be used for creating new instances during type conversions.      *      * @param injector the injector      */
DECL|method|setInjector (Injector injector)
name|void
name|setInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
function_decl|;
comment|/**      * Gets the injector      *      * @return the injector      */
DECL|method|getInjector ()
name|Injector
name|getInjector
parameter_list|()
function_decl|;
comment|/**      * Gets the utilization statistics of this type converter registry      *      * @return the utilization statistics      */
DECL|method|getStatistics ()
name|Statistics
name|getStatistics
parameter_list|()
function_decl|;
comment|/**      * Number of type converters in the registry.      *      * @return number of type converters in the registry.      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * The logging level to use when logging that a type converter already exists when attempting to add a duplicate type converter.      *<p/>      * The default logging level is<tt>WARN</tt>      */
DECL|method|getTypeConverterExistsLoggingLevel ()
name|LoggingLevel
name|getTypeConverterExistsLoggingLevel
parameter_list|()
function_decl|;
comment|/**      * The logging level to use when logging that a type converter already exists when attempting to add a duplicate type converter.      *<p/>      * The default logging level is<tt>WARN</tt>      */
DECL|method|setTypeConverterExistsLoggingLevel (LoggingLevel typeConverterExistsLoggingLevel)
name|void
name|setTypeConverterExistsLoggingLevel
parameter_list|(
name|LoggingLevel
name|typeConverterExistsLoggingLevel
parameter_list|)
function_decl|;
comment|/**      * What should happen when attempting to add a duplicate type converter.      *<p/>      * The default behavior is to override the existing.      */
DECL|method|getTypeConverterExists ()
name|TypeConverterExists
name|getTypeConverterExists
parameter_list|()
function_decl|;
comment|/**      * What should happen when attempting to add a duplicate type converter.      *<p/>      * The default behavior is to override the existing.      */
DECL|method|setTypeConverterExists (TypeConverterExists typeConverterExists)
name|void
name|setTypeConverterExists
parameter_list|(
name|TypeConverterExists
name|typeConverterExists
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

