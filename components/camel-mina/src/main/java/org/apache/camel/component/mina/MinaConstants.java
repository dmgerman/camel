begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
package|;
end_package

begin_comment
comment|/**  * Mina constants  *  * @version   */
end_comment

begin_class
DECL|class|MinaConstants
specifier|public
specifier|final
class|class
name|MinaConstants
block|{
DECL|field|MINA_CLOSE_SESSION_WHEN_COMPLETE
specifier|public
specifier|static
specifier|final
name|String
name|MINA_CLOSE_SESSION_WHEN_COMPLETE
init|=
literal|"CamelMinaCloseSessionWhenComplete"
decl_stmt|;
comment|/** The key of the IoSession which is stored in the message header*/
DECL|field|MINA_IOSESSION
specifier|public
specifier|static
specifier|final
name|String
name|MINA_IOSESSION
init|=
literal|"CamelMinaIoSession"
decl_stmt|;
comment|/** The socket address of local machine that received the message. */
DECL|field|MINA_LOCAL_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|MINA_LOCAL_ADDRESS
init|=
literal|"CamelMinaLocalAddress"
decl_stmt|;
comment|/** The socket address of the remote machine that send the message. */
DECL|field|MINA_REMOTE_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|MINA_REMOTE_ADDRESS
init|=
literal|"CamelMinaRemoteAddress"
decl_stmt|;
DECL|method|MinaConstants ()
specifier|private
name|MinaConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

