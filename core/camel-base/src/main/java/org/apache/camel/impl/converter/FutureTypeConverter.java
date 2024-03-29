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
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|StreamCache
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
name|ExchangeHelper
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Future type converter.  */
end_comment

begin_class
DECL|class|FutureTypeConverter
specifier|public
specifier|final
class|class
name|FutureTypeConverter
extends|extends
name|TypeConverterSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FutureTypeConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|converter
specifier|private
specifier|final
name|TypeConverter
name|converter
decl_stmt|;
DECL|method|FutureTypeConverter (TypeConverter converter)
specifier|public
name|FutureTypeConverter
parameter_list|(
name|TypeConverter
name|converter
parameter_list|)
block|{
name|this
operator|.
name|converter
operator|=
name|converter
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
block|{
try|try
block|{
return|return
name|doConvertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
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
name|TypeConversionException
argument_list|(
name|value
argument_list|,
name|type
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|doConvertTo
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
name|Exception
block|{
comment|// do not convert to stream cache
if|if
condition|(
name|StreamCache
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
return|return
literal|null
return|;
block|}
if|if
condition|(
name|Future
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
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
operator|(
name|Future
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|future
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
comment|// return void to indicate its not possible to convert at this time
return|return
operator|(
name|T
operator|)
name|MISS_VALUE
return|;
block|}
comment|// do some trace logging as the get is blocking until the response is ready
name|LOG
operator|.
name|trace
argument_list|(
literal|"Getting future response"
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|future
operator|.
name|get
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Got future response"
argument_list|)
expr_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
comment|// return void to indicate its not possible to convert at this time
return|return
operator|(
name|T
operator|)
name|MISS_VALUE
return|;
block|}
comment|// maybe from is already the type we want
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|body
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Exchange
condition|)
block|{
name|Exchange
name|result
init|=
operator|(
name|Exchange
operator|)
name|body
decl_stmt|;
name|body
operator|=
name|ExchangeHelper
operator|.
name|extractResultBody
argument_list|(
name|result
argument_list|,
name|result
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// no then convert to the type
return|return
name|converter
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

