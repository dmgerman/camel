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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|TypeConverter
import|;
end_import

begin_comment
comment|/**  * A {@link TypeConverter} that converts to and from {@link URI}s.  */
end_comment

begin_class
DECL|class|UriTypeConverter
specifier|public
class|class
name|UriTypeConverter
block|{
annotation|@
name|Converter
DECL|method|toCharSequence (final URI value)
specifier|public
specifier|static
name|CharSequence
name|toCharSequence
parameter_list|(
specifier|final
name|URI
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toUri (final CharSequence value)
specifier|public
specifier|static
name|URI
name|toUri
parameter_list|(
specifier|final
name|CharSequence
name|value
parameter_list|)
block|{
specifier|final
name|String
name|stringValue
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|stringValue
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TypeConversionException
argument_list|(
name|value
argument_list|,
name|URI
operator|.
name|class
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

