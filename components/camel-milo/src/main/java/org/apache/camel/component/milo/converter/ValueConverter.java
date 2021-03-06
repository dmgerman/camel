begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|converter
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
name|Converter
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|DataValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|Variant
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|ValueConverter
specifier|public
specifier|final
class|class
name|ValueConverter
implements|implements
name|TypeConverters
block|{
DECL|method|ValueConverter ()
specifier|private
name|ValueConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toVariant (final DataValue value)
specifier|public
specifier|static
name|Variant
name|toVariant
parameter_list|(
specifier|final
name|DataValue
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|getValue
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toDataValue (final Variant value)
specifier|public
specifier|static
name|DataValue
name|toDataValue
parameter_list|(
specifier|final
name|Variant
name|value
parameter_list|)
block|{
return|return
operator|new
name|DataValue
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

