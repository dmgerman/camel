begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|cdi
operator|.
name|pojo
operator|.
name|TypeConverterInput
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
name|cdi
operator|.
name|pojo
operator|.
name|TypeConverterOutput
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|InjectedTestTypeConverter
specifier|public
specifier|final
class|class
name|InjectedTestTypeConverter
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Inject
DECL|method|InjectedTestTypeConverter (CamelContext context)
name|InjectedTestTypeConverter
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Converter
DECL|method|convert (TypeConverterInput input)
specifier|public
name|TypeConverterOutput
name|convert
parameter_list|(
name|TypeConverterInput
name|input
parameter_list|)
throws|throws
name|Exception
block|{
name|TypeConverterOutput
name|output
init|=
operator|new
name|TypeConverterOutput
argument_list|()
decl_stmt|;
name|output
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|input
operator|.
name|getProperty
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
block|}
end_class

end_unit

