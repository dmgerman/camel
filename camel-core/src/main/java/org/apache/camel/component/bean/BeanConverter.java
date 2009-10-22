begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|spi
operator|.
name|TypeConverterRegistry
import|;
end_import

begin_comment
comment|/**  * A set of converter methods for working with beans  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|BeanConverter
specifier|public
specifier|final
class|class
name|BeanConverter
block|{
DECL|method|BeanConverter ()
specifier|private
name|BeanConverter
parameter_list|()
block|{
comment|// Helper Class
block|}
annotation|@
name|FallbackConverter
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
comment|// use a fallback type converter so we can convert the embedded body if the value is BeanInvocation
if|if
condition|(
name|BeanInvocation
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
name|BeanInvocation
name|bi
init|=
operator|(
name|BeanInvocation
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|bi
operator|.
name|getArgs
argument_list|()
operator|==
literal|null
operator|||
name|bi
operator|.
name|getArgs
argument_list|()
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
comment|// not possible to convert as we try to convert the data passed in at first argument
return|return
literal|null
return|;
block|}
name|Class
name|from
init|=
name|bi
operator|.
name|getArgs
argument_list|()
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Object
name|body
init|=
name|bi
operator|.
name|getArgs
argument_list|()
index|[
literal|0
index|]
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
name|body
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

