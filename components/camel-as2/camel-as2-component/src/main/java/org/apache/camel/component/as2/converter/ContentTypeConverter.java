begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
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
name|CamelException
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
name|http
operator|.
name|entity
operator|.
name|ContentType
import|;
end_import

begin_comment
comment|/**  * Content type related converters.  */
end_comment

begin_enum
annotation|@
name|Converter
DECL|enum|ContentTypeConverter
specifier|public
enum|enum
name|ContentTypeConverter
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Converter
DECL|method|toContentType (String contentTypeString)
specifier|public
specifier|static
name|ContentType
name|toContentType
parameter_list|(
name|String
name|contentTypeString
parameter_list|)
throws|throws
name|CamelException
block|{
try|try
block|{
return|return
name|ContentType
operator|.
name|parse
argument_list|(
name|contentTypeString
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
name|CamelException
argument_list|(
literal|"failed to convert String to ContentType"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_enum

end_unit

