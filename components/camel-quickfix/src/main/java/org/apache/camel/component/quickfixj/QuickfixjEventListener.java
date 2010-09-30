begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfixj
package|;
end_package

begin_import
import|import
name|quickfix
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionID
import|;
end_import

begin_comment
comment|/**  * Listens for FIX messages forwarded from the QuickfixjEngine  * @see QuickfixjEngine  */
end_comment

begin_interface
DECL|interface|QuickfixjEventListener
specifier|public
interface|interface
name|QuickfixjEventListener
block|{
DECL|method|onEvent (QuickfixjEventCategory eventCategory, SessionID sessionID, Message message)
name|void
name|onEvent
parameter_list|(
name|QuickfixjEventCategory
name|eventCategory
parameter_list|,
name|SessionID
name|sessionID
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

