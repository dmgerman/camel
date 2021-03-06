begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_class
DECL|class|StreamUtils
specifier|public
specifier|final
class|class
name|StreamUtils
block|{
DECL|method|StreamUtils ()
specifier|private
name|StreamUtils
parameter_list|()
block|{     }
comment|/**      * Creates a stream on the given collection if it is not null      *      * @param value  the collection      * @return A stream of elements or an empty stream if the collection is null      */
DECL|method|stream (Collection<C> value)
specifier|public
specifier|static
parameter_list|<
name|C
parameter_list|>
name|Stream
argument_list|<
name|C
argument_list|>
name|stream
parameter_list|(
name|Collection
argument_list|<
name|C
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|stream
argument_list|()
else|:
name|Stream
operator|.
name|empty
argument_list|()
return|;
block|}
comment|/**      * Creates a stream of entries on the given Map if it is not null      *      * @param value  the map      * @return A stream of entries or an empty stream if the collection is null      */
DECL|method|stream (Map<K, V> value)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Stream
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|stream
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
else|:
name|Stream
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

