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
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnMissingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
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
name|converter
operator|.
name|Converter
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
name|support
operator|.
name|DefaultConversionService
import|;
end_import

begin_class
annotation|@
name|Configuration
DECL|class|SpringConversionServiceConfiguration
specifier|public
class|class
name|SpringConversionServiceConfiguration
block|{
annotation|@
name|ConditionalOnMissingBean
argument_list|(
name|ConversionService
operator|.
name|class
argument_list|)
annotation|@
name|Bean
DECL|method|conversionService (ApplicationContext applicationContext)
name|ConversionService
name|conversionService
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|DefaultConversionService
name|service
init|=
operator|new
name|DefaultConversionService
argument_list|()
decl_stmt|;
for|for
control|(
name|Converter
name|converter
range|:
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|Converter
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
control|)
block|{
name|service
operator|.
name|addConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
return|return
name|service
return|;
block|}
annotation|@
name|Bean
DECL|method|springTypeConverter (ConversionService conversionService)
name|SpringTypeConverter
name|springTypeConverter
parameter_list|(
name|ConversionService
name|conversionService
parameter_list|)
block|{
return|return
operator|new
name|SpringTypeConverter
argument_list|(
name|conversionService
argument_list|)
return|;
block|}
block|}
end_class

end_unit

