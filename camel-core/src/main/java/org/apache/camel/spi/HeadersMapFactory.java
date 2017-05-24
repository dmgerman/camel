begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|Message
import|;
end_import

begin_comment
comment|/**  * Factory to create the {@link Map} implementation to use for storing headers and properties  * on {@link Message} and {@link org.apache.camel.Exchange}.  *  * @see org.apache.camel.impl.DefaultHeadersMapFactory  */
end_comment

begin_interface
DECL|interface|HeadersMapFactory
specifier|public
interface|interface
name|HeadersMapFactory
block|{
comment|/**      * Creates a new empty {@link Map}      *      * @return new empty map      */
DECL|method|newMap ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|newMap
parameter_list|()
function_decl|;
comment|/**      * Creates a new {@link Map} and copies over all the content from the existing map.      *<p/>      * The copy of the content should use defensive copy, so the returned map      * can add/remove/change the content without affecting the existing map.      *      * @param map  existing map to copy over (must use defensive copy)      * @return new map with the content from the existing map      */
DECL|method|fromMap (Map<String, Object> map)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|fromMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
function_decl|;
comment|/**      * Whether the given {@link Map} implementation is created by this factory?      *      * @return<tt>true</tt> if created from this factory,<tt>false</tt> if not      */
DECL|method|isInstanceOf (Map<String, Object> map)
name|boolean
name|isInstanceOf
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

