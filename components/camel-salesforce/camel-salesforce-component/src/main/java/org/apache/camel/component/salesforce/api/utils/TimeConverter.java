begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|OffsetTime
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|ConversionException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|MarshallingContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|UnmarshallingContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamWriter
import|;
end_import

begin_comment
comment|/**  * XStream converter for handling {@link OffsetTime} fields.  */
end_comment

begin_class
DECL|class|TimeConverter
specifier|public
class|class
name|TimeConverter
implements|implements
name|Converter
block|{
annotation|@
name|Override
DECL|method|marshal (Object o, HierarchicalStreamWriter writer, MarshallingContext context)
specifier|public
name|void
name|marshal
parameter_list|(
name|Object
name|o
parameter_list|,
name|HierarchicalStreamWriter
name|writer
parameter_list|,
name|MarshallingContext
name|context
parameter_list|)
block|{
name|OffsetTime
name|time
init|=
operator|(
name|OffsetTime
operator|)
name|o
decl_stmt|;
name|writer
operator|.
name|setValue
argument_list|(
name|DateTimeUtils
operator|.
name|formatTime
argument_list|(
name|time
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (HierarchicalStreamReader reader, UnmarshallingContext context)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|HierarchicalStreamReader
name|reader
parameter_list|,
name|UnmarshallingContext
name|context
parameter_list|)
block|{
try|try
block|{
return|return
name|DateTimeUtils
operator|.
name|parseTime
argument_list|(
name|reader
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConversionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error reading OffsetTime from value %s: %s"
argument_list|,
name|reader
operator|.
name|getValue
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|canConvert (Class aClass)
specifier|public
name|boolean
name|canConvert
parameter_list|(
name|Class
name|aClass
parameter_list|)
block|{
return|return
name|OffsetTime
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|aClass
argument_list|)
return|;
block|}
block|}
end_class

end_unit

