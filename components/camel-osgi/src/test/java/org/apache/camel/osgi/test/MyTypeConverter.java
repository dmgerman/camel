begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
operator|.
name|test
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
name|FallbackConverter
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
name|component
operator|.
name|file
operator|.
name|GenericFile
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
name|spi
operator|.
name|TypeConverterRegistry
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|MyTypeConverter
specifier|public
specifier|final
class|class
name|MyTypeConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|MyTypeConverter ()
specifier|private
name|MyTypeConverter
parameter_list|()
block|{     }
comment|/**      * Converts the given value to a boolean, handling strings or Boolean      * objects; otherwise returning false if the value could not be converted to      * a boolean      */
annotation|@
name|Converter
DECL|method|toBool (Object value)
specifier|public
specifier|static
name|boolean
name|toBool
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Boolean
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|answer
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|Boolean
condition|)
block|{
name|answer
operator|=
operator|(
name|Boolean
operator|)
name|value
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
operator|.
name|booleanValue
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|FallbackConverter
DECL|method|convertTo (Class<?> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
name|Object
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
comment|// use a fallback type converter so we can convert the embedded body if the value is GenericFile
if|if
condition|(
name|GenericFile
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|GenericFile
name|file
init|=
operator|(
name|GenericFile
operator|)
name|value
decl_stmt|;
name|Class
name|from
init|=
name|file
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
decl_stmt|;
comment|// maybe from is already the type we want
if|if
condition|(
name|from
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|file
operator|.
name|getBody
argument_list|()
return|;
block|}
comment|// no then try to lookup a type converter
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|from
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|Object
name|body
init|=
name|file
operator|.
name|getBody
argument_list|()
decl_stmt|;
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

