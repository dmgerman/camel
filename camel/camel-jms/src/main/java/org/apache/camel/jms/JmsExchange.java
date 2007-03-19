begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jms
package|;
end_package

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
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|JmsExchange
specifier|public
interface|interface
name|JmsExchange
extends|extends
name|Exchange
argument_list|<
name|Message
argument_list|>
block|{
comment|/**      * Creates the JMS message for this exchange so that it can be sent to      * a JMS endpoint.      */
DECL|method|createMessage (Session session)
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
function_decl|;
block|}
end_interface

end_unit

