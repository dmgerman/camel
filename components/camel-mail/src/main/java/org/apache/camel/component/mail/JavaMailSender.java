begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMessage
import|;
end_import

begin_comment
comment|/**  * The JavaMailSender interface contains all the methods of a JavaMailSender  * implementation currently used by the mail component.  */
end_comment

begin_interface
DECL|interface|JavaMailSender
specifier|public
interface|interface
name|JavaMailSender
block|{
comment|/**      * Send the mail      *      * @param mimeMessage the message to send      * @throws javax.mail.MessagingException is thrown if error sending the mail.      */
DECL|method|send (MimeMessage mimeMessage)
name|void
name|send
parameter_list|(
name|MimeMessage
name|mimeMessage
parameter_list|)
throws|throws
name|MessagingException
function_decl|;
DECL|method|getJavaMailProperties ()
name|Properties
name|getJavaMailProperties
parameter_list|()
function_decl|;
DECL|method|setJavaMailProperties (Properties javaMailProperties)
name|void
name|setJavaMailProperties
parameter_list|(
name|Properties
name|javaMailProperties
parameter_list|)
function_decl|;
DECL|method|setHost (String host)
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
function_decl|;
DECL|method|getHost ()
name|String
name|getHost
parameter_list|()
function_decl|;
DECL|method|setPort (int port)
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
function_decl|;
DECL|method|getPort ()
name|int
name|getPort
parameter_list|()
function_decl|;
DECL|method|setUsername (String username)
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
function_decl|;
DECL|method|getUsername ()
name|String
name|getUsername
parameter_list|()
function_decl|;
DECL|method|setPassword (String password)
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
function_decl|;
DECL|method|getPassword ()
name|String
name|getPassword
parameter_list|()
function_decl|;
DECL|method|setProtocol (String protocol)
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
function_decl|;
DECL|method|getProtocol ()
name|String
name|getProtocol
parameter_list|()
function_decl|;
DECL|method|setSession (Session session)
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
function_decl|;
DECL|method|getSession ()
name|Session
name|getSession
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

