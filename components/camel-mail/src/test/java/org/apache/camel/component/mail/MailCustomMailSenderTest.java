begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|MailCustomMailSenderTest
specifier|public
class|class
name|MailCustomMailSenderTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|sent
specifier|private
specifier|static
name|boolean
name|sent
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"mySender"
argument_list|)
DECL|field|sender
specifier|private
name|MySender
name|sender
init|=
operator|new
name|MySender
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendWithCustomMailSender ()
specifier|public
name|void
name|testSendWithCustomMailSender
parameter_list|()
throws|throws
name|Exception
block|{
name|sendBody
argument_list|(
literal|"smtp://claus@localhost?javaMailSender=#mySender"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have used custom mail sender"
argument_list|,
name|sent
argument_list|)
expr_stmt|;
block|}
DECL|class|MySender
specifier|private
specifier|static
class|class
name|MySender
implements|implements
name|JavaMailSender
block|{
annotation|@
name|Override
DECL|method|send (MimeMessage mimeMessage)
specifier|public
name|void
name|send
parameter_list|(
name|MimeMessage
name|mimeMessage
parameter_list|)
throws|throws
name|MessagingException
block|{
name|sent
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getJavaMailProperties ()
specifier|public
name|Properties
name|getJavaMailProperties
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setJavaMailProperties (Properties javaMailProperties)
specifier|public
name|void
name|setJavaMailProperties
parameter_list|(
name|Properties
name|javaMailProperties
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setSession (Session session)
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

