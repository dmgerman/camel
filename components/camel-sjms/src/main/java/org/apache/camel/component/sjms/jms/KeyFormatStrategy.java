begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|jms
package|;
end_package

begin_comment
comment|/**  * Strategy for applying encoding and decoding of JMS headers so they apply to  * the JMS spec.  *   * @version  */
end_comment

begin_interface
DECL|interface|KeyFormatStrategy
specifier|public
interface|interface
name|KeyFormatStrategy
block|{
comment|/**      * Encodes the key before its sent as a {@link javax.jms.Message} message.      *       * @param key the original key      * @return the encoded key      */
DECL|method|encodeKey (String key)
name|String
name|encodeKey
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Decodes the key after its received from a {@link javax.jms.Message}      * message.      *       * @param key the encoded key      * @return the decoded key as the original key      */
DECL|method|decodeKey (String key)
name|String
name|decodeKey
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

