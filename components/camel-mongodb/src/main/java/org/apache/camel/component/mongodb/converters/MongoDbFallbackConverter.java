begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb.converters
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
operator|.
name|converters
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|BasicDBList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|BasicDBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|DBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|util
operator|.
name|JSON
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
name|InvalidPayloadException
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
name|bson
operator|.
name|Document
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
DECL|class|MongoDbFallbackConverter
specifier|public
specifier|final
class|class
name|MongoDbFallbackConverter
block|{
comment|// Jackson's ObjectMapper is thread-safe, so no need to create a pool nor synchronize access to it
DECL|field|OBJECT_MAPPER
specifier|private
specifier|static
specifier|final
name|ObjectMapper
name|OBJECT_MAPPER
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
DECL|method|MongoDbFallbackConverter ()
specifier|private
name|MongoDbFallbackConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
argument_list|(
name|fallback
operator|=
literal|true
argument_list|)
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
throws|throws
name|InvalidPayloadException
block|{
comment|// if the source is a string and we attempt to convert to one of the known mongodb json classes then try that
if|if
condition|(
name|String
operator|.
name|class
operator|==
name|value
operator|.
name|getClass
argument_list|()
condition|)
block|{
if|if
condition|(
name|type
operator|==
name|DBObject
operator|.
name|class
condition|)
block|{
name|Object
name|out
init|=
name|JSON
operator|.
name|parse
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|instanceof
name|DBObject
condition|)
block|{
return|return
name|out
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|BasicDBList
operator|.
name|class
condition|)
block|{
name|Object
name|out
init|=
name|JSON
operator|.
name|parse
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|instanceof
name|BasicDBList
condition|)
block|{
return|return
name|out
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|BasicDBObject
operator|.
name|class
condition|)
block|{
name|Object
name|out
init|=
name|JSON
operator|.
name|parse
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|instanceof
name|BasicDBObject
condition|)
block|{
return|return
name|out
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
throw|;
block|}
block|}
block|}
comment|// okay then fallback and use jackson
if|if
condition|(
name|type
operator|==
name|DBObject
operator|.
name|class
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|m
init|=
name|OBJECT_MAPPER
operator|.
name|convertValue
argument_list|(
name|value
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// workaround problem with mongodb for BigDecimal should be Double
name|mapMongoDBBigDecimalIssue
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
operator|new
name|BasicDBObject
argument_list|(
name|m
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|Document
operator|.
name|class
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
name|OBJECT_MAPPER
operator|.
name|convertValue
argument_list|(
name|value
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// workaround problem with mongodb for BigDecimal should be Double
name|mapMongoDBBigDecimalIssue
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
operator|new
name|Document
argument_list|(
name|m
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|mapMongoDBBigDecimalIssue (Map<?, ?> m)
specifier|private
specifier|static
name|void
name|mapMongoDBBigDecimalIssue
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|m
parameter_list|)
block|{
comment|// workaround problem with mongodb for BigDecimal should be Double
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|m
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|v
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|instanceof
name|BigDecimal
condition|)
block|{
name|v
operator|=
name|Double
operator|.
name|valueOf
argument_list|(
name|v
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setValue
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

