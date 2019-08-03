begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

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
name|TimeoutMap
import|;
end_import

begin_comment
comment|/**  * Represents an entry in a {@link TimeoutMap}  */
end_comment

begin_class
DECL|class|TimeoutMapEntry
specifier|public
class|class
name|TimeoutMapEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Comparable
argument_list|<
name|Object
argument_list|>
implements|,
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|key
specifier|private
name|K
name|key
decl_stmt|;
DECL|field|value
specifier|private
name|V
name|value
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
DECL|field|expireTime
specifier|private
name|long
name|expireTime
decl_stmt|;
DECL|method|TimeoutMapEntry (K id, V handler, long timeout)
specifier|public
name|TimeoutMapEntry
parameter_list|(
name|K
name|id
parameter_list|,
name|V
name|handler
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|handler
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getKey ()
specifier|public
name|K
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getExpireTime ()
specifier|public
name|long
name|getExpireTime
parameter_list|()
block|{
return|return
name|expireTime
return|;
block|}
DECL|method|setExpireTime (long expireTime)
specifier|public
name|void
name|setExpireTime
parameter_list|(
name|long
name|expireTime
parameter_list|)
block|{
name|this
operator|.
name|expireTime
operator|=
name|expireTime
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getValue ()
specifier|public
name|V
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|setValue (V value)
specifier|public
name|V
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
name|V
name|oldValue
init|=
name|this
operator|.
name|value
decl_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
return|return
name|oldValue
return|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|compareTo (Object that)
specifier|public
name|int
name|compareTo
parameter_list|(
name|Object
name|that
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|that
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|that
operator|instanceof
name|TimeoutMapEntry
condition|)
block|{
return|return
name|compareTo
argument_list|(
operator|(
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|that
argument_list|)
return|;
block|}
return|return
literal|1
return|;
block|}
DECL|method|compareTo (TimeoutMapEntry<K, V> that)
specifier|public
name|int
name|compareTo
parameter_list|(
name|TimeoutMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|that
parameter_list|)
block|{
name|long
name|diff
init|=
name|this
operator|.
name|expireTime
operator|-
name|that
operator|.
name|expireTime
decl_stmt|;
if|if
condition|(
name|diff
operator|>
literal|0
condition|)
block|{
return|return
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|diff
operator|<
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|this
operator|.
name|key
operator|.
name|hashCode
argument_list|()
operator|-
name|that
operator|.
name|key
operator|.
name|hashCode
argument_list|()
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
name|key
operator|+
literal|" (times out after "
operator|+
name|timeout
operator|+
literal|" millis)"
return|;
block|}
block|}
end_class

end_unit

