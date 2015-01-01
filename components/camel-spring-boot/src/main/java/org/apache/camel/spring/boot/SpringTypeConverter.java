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
name|ConversionService
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
DECL|field|conversionService
specifier|private
specifier|final
name|ConversionService
name|conversionService
decl_stmt|;
annotation|@
name|Autowired
DECL|method|SpringTypeConverter (ConversionService conversionService)
specifier|public
name|SpringTypeConverter
parameter_list|(
name|ConversionService
name|conversionService
parameter_list|)
block|{
name|this
operator|.
name|conversionService
operator|=
name|conversionService
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
if|if
condition|(
name|conversionService
operator|.
name|canConvert
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|type
argument_list|)
condition|)
block|{
return|return
name|conversionService
operator|.
name|convert
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

