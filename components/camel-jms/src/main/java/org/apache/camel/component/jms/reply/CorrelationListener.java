begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.reply
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|reply
package|;
end_package

begin_comment
comment|/**  * Listener for events when correlation id's changes.  */
end_comment

begin_interface
DECL|interface|CorrelationListener
specifier|public
interface|interface
name|CorrelationListener
block|{
comment|/**      * Callback when a new correlation id is added      *      * @param key the correlation id      */
DECL|method|onPut (String key)
name|void
name|onPut
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Callback when a correlation id is removed      *      * @param key the correlation id      */
DECL|method|onRemove (String key)
name|void
name|onRemove
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Callback when a correlation id is evicted due timeout      *      * @param key the correlation id      */
DECL|method|onEviction (String key)
name|void
name|onEviction
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

