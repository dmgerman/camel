begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|support
operator|.
name|ObjectHelper
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

begin_comment
comment|/**  * A {@link org.apache.camel.TypeConverter} implementation which invokes a static method  * as a fallback type converter from a type to another type  */
end_comment

begin_class
DECL|class|StaticMethodFallbackTypeConverter
specifier|public
class|class
name|StaticMethodFallbackTypeConverter
extends|extends
name|TypeConverterSupport
block|{
DECL|field|method
specifier|private
specifier|final
name|Method
name|method
decl_stmt|;
DECL|field|useExchange
specifier|private
specifier|final
name|boolean
name|useExchange
decl_stmt|;
DECL|field|registry
specifier|private
specifier|final
name|TypeConverterRegistry
name|registry
decl_stmt|;
DECL|field|allowNull
specifier|private
specifier|final
name|boolean
name|allowNull
decl_stmt|;
DECL|method|StaticMethodFallbackTypeConverter (Method method, TypeConverterRegistry registry, boolean allowNull)
specifier|public
name|StaticMethodFallbackTypeConverter
parameter_list|(
name|Method
name|method
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|,
name|boolean
name|allowNull
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|useExchange
operator|=
name|method
operator|.
name|getParameterCount
argument_list|()
operator|==
literal|4
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|this
operator|.
name|allowNull
operator|=
name|allowNull
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|allowNull ()
specifier|public
name|boolean
name|allowNull
parameter_list|()
block|{
return|return
name|allowNull
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"StaticMethodFallbackTypeConverter: "
operator|+
name|method
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
block|{
return|return
name|useExchange
condition|?
operator|(
name|T
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
literal|null
argument_list|,
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|,
name|registry
argument_list|)
else|:
operator|(
name|T
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
literal|null
argument_list|,
name|type
argument_list|,
name|value
argument_list|,
name|registry
argument_list|)
return|;
block|}
block|}
end_class

end_unit

