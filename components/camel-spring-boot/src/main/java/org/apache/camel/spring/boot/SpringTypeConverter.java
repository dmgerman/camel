begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|TypeConversionException
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
name|TypeConverterSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|ConversionFailedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|ConversionService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|ConverterNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|TypeDescriptor
import|;
end_import

begin_class
DECL|class|SpringTypeConverter
specifier|public
class|class
name|SpringTypeConverter
extends|extends
name|TypeConverterSupport
block|{
DECL|field|conversionServices
specifier|private
specifier|final
name|List
argument_list|<
name|ConversionService
argument_list|>
name|conversionServices
decl_stmt|;
DECL|field|types
specifier|private
specifier|final
name|ConcurrentHashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|TypeDescriptor
argument_list|>
name|types
decl_stmt|;
annotation|@
name|Autowired
DECL|method|SpringTypeConverter (List<ConversionService> conversionServices)
specifier|public
name|SpringTypeConverter
parameter_list|(
name|List
argument_list|<
name|ConversionService
argument_list|>
name|conversionServices
parameter_list|)
block|{
name|this
operator|.
name|conversionServices
operator|=
name|conversionServices
expr_stmt|;
name|this
operator|.
name|types
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
throws|throws
name|TypeConversionException
block|{
comment|// do not attempt to convert Camel types
if|if
condition|(
name|type
operator|.
name|getCanonicalName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"org.apache"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// do not attempt to convert List -> Map. Ognl expression may use this converter as a fallback expecting null
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|Map
operator|.
name|class
argument_list|)
operator|&&
operator|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
operator|||
name|value
operator|instanceof
name|Collection
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|TypeDescriptor
name|sourceType
init|=
name|types
operator|.
name|computeIfAbsent
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|TypeDescriptor
operator|::
name|valueOf
argument_list|)
decl_stmt|;
name|TypeDescriptor
name|targetType
init|=
name|types
operator|.
name|computeIfAbsent
argument_list|(
name|type
argument_list|,
name|TypeDescriptor
operator|::
name|valueOf
argument_list|)
decl_stmt|;
for|for
control|(
name|ConversionService
name|conversionService
range|:
name|conversionServices
control|)
block|{
if|if
condition|(
name|conversionService
operator|.
name|canConvert
argument_list|(
name|sourceType
argument_list|,
name|targetType
argument_list|)
condition|)
block|{
try|try
block|{
return|return
operator|(
name|T
operator|)
name|conversionService
operator|.
name|convert
argument_list|(
name|value
argument_list|,
name|sourceType
argument_list|,
name|targetType
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ConversionFailedException
name|e
parameter_list|)
block|{
comment|// if value is a collection or an array the check ConversionService::canConvert
comment|// may return true but then the conversion of specific objects may fail
comment|//
comment|// https://issues.apache.org/jira/browse/CAMEL-10548
comment|// https://jira.spring.io/browse/SPR-14971
comment|//
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ConverterNotFoundException
operator|&&
name|isArrayOrCollection
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|TypeConversionException
argument_list|(
name|value
argument_list|,
name|type
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|isArrayOrCollection (Object value)
specifier|private
name|boolean
name|isArrayOrCollection
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|value
operator|instanceof
name|Collection
operator|||
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

