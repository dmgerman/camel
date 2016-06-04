begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.redis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|redis
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
name|java
operator|.
name|util
operator|.
name|Map
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

begin_class
DECL|class|ExchangeConverter
class|class
name|ExchangeConverter
block|{
DECL|method|getKey (Exchange exchange)
name|String
name|getKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getStringValue (Exchange exchange)
name|String
name|getStringValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getLongValue (Exchange exchange)
name|Long
name|getLongValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getDestination (Exchange exchange)
name|String
name|getDestination
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|DESTINATION
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getChannel (Exchange exchange)
name|String
name|getChannel
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|CHANNEL
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getMessage (Exchange exchange)
name|Object
name|getMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|MESSAGE
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getIndex (Exchange exchange)
name|Long
name|getIndex
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|INDEX
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getPivot (Exchange exchange)
name|String
name|getPivot
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|PIVOT
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getPosition (Exchange exchange)
name|String
name|getPosition
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|POSITION
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getCount (Exchange exchange)
name|Long
name|getCount
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|COUNT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getStart (Exchange exchange)
name|Long
name|getStart
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|START
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getEnd (Exchange exchange)
name|Long
name|getEnd
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|END
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getTimeout (Exchange exchange)
name|Long
name|getTimeout
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|TIMEOUT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getOffset (Exchange exchange)
name|Long
name|getOffset
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|OFFSET
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getValueAsLong (Exchange exchange)
name|Long
name|getValueAsLong
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getFields (Exchange exchange)
name|Collection
argument_list|<
name|String
argument_list|>
name|getFields
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|FIELDS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getValuesAsMap (Exchange exchange)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getValuesAsMap
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|VALUES
argument_list|,
name|Map
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getKeys (Exchange exchange)
name|Collection
argument_list|<
name|String
argument_list|>
name|getKeys
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|KEYS
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getValue (Exchange exchange)
name|Object
name|getValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getBooleanValue (Exchange exchange)
name|Boolean
name|getBooleanValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getField (Exchange exchange)
name|String
name|getField
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|FIELD
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getTimestamp (Exchange exchange)
name|Long
name|getTimestamp
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|TIMESTAMP
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getPattern (Exchange exchange)
name|String
name|getPattern
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|PATTERN
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getDb (Exchange exchange)
name|Integer
name|getDb
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|DB
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getScore (Exchange exchange)
name|Double
name|getScore
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|SCORE
argument_list|,
name|Double
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getMin (Exchange exchange)
name|Double
name|getMin
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|MIN
argument_list|,
name|Double
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getMax (Exchange exchange)
name|Double
name|getMax
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|MAX
argument_list|,
name|Double
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getIncrement (Exchange exchange)
name|Double
name|getIncrement
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|INCREMENT
argument_list|,
name|Double
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getWithScore (Exchange exchange)
name|Boolean
name|getWithScore
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getInHeaderValue
argument_list|(
name|exchange
argument_list|,
name|RedisConstants
operator|.
name|WITHSCORE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getInHeaderValue (Exchange exchange, String key, Class<T> aClass)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getInHeaderValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|key
argument_list|,
name|aClass
argument_list|)
return|;
block|}
block|}
end_class

end_unit

