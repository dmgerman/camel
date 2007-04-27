begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|springframework
operator|.
name|mail
operator|.
name|MailAuthenticationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mail
operator|.
name|MailException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mail
operator|.
name|MailSendException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mail
operator|.
name|javamail
operator|.
name|JavaMailSenderImpl
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|AuthenticationFailedException
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
name|Transport
import|;
end_import

begin_comment
comment|/**  * An extension of Spring's {@link JavaMailSenderImpl} to provide helper methods for listening for new mail  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|JavaMailConnection
specifier|public
class|class
name|JavaMailConnection
extends|extends
name|JavaMailSenderImpl
block|{
comment|/**      * Create a new {@link Transport} which can then be used to consume new messages      *      * @throws MailAuthenticationException in case of authentication failure      * @throws MailSendException           in case of failure when sending a message      */
DECL|method|createTransport ()
specifier|public
name|Transport
name|createTransport
parameter_list|()
throws|throws
name|MailException
block|{
try|try
block|{
name|Transport
name|transport
init|=
name|getTransport
argument_list|(
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|transport
operator|.
name|connect
argument_list|(
name|getHost
argument_list|()
argument_list|,
name|getPort
argument_list|()
argument_list|,
name|getUsername
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|transport
return|;
block|}
catch|catch
parameter_list|(
name|AuthenticationFailedException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|MailAuthenticationException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|MailSendException
argument_list|(
literal|"Mail server connection failed"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

