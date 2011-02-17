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
name|io
operator|.
name|InputStream
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
name|impl
operator|.
name|JndiRegistry
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
name|SimpleMailMessage
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
name|JavaMailSender
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
name|MimeMessagePreparator
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

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
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"mySender"
argument_list|,
operator|new
name|MySender
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
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
class|class
name|MySender
implements|implements
name|JavaMailSender
block|{
DECL|method|send (SimpleMailMessage simpleMessage)
specifier|public
name|void
name|send
parameter_list|(
name|SimpleMailMessage
name|simpleMessage
parameter_list|)
throws|throws
name|MailException
block|{
comment|// noop
block|}
DECL|method|send (SimpleMailMessage[] simpleMessages)
specifier|public
name|void
name|send
parameter_list|(
name|SimpleMailMessage
index|[]
name|simpleMessages
parameter_list|)
throws|throws
name|MailException
block|{
comment|// noop
block|}
DECL|method|createMimeMessage ()
specifier|public
name|MimeMessage
name|createMimeMessage
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|createMimeMessage (InputStream contentStream)
specifier|public
name|MimeMessage
name|createMimeMessage
parameter_list|(
name|InputStream
name|contentStream
parameter_list|)
throws|throws
name|MailException
block|{
return|return
literal|null
return|;
block|}
DECL|method|send (MimeMessage mimeMessage)
specifier|public
name|void
name|send
parameter_list|(
name|MimeMessage
name|mimeMessage
parameter_list|)
throws|throws
name|MailException
block|{
comment|// noop
block|}
DECL|method|send (MimeMessage[] mimeMessages)
specifier|public
name|void
name|send
parameter_list|(
name|MimeMessage
index|[]
name|mimeMessages
parameter_list|)
throws|throws
name|MailException
block|{
comment|// noop
block|}
DECL|method|send (MimeMessagePreparator mimeMessagePreparator)
specifier|public
name|void
name|send
parameter_list|(
name|MimeMessagePreparator
name|mimeMessagePreparator
parameter_list|)
throws|throws
name|MailException
block|{
name|sent
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|send (MimeMessagePreparator[] mimeMessagePreparators)
specifier|public
name|void
name|send
parameter_list|(
name|MimeMessagePreparator
index|[]
name|mimeMessagePreparators
parameter_list|)
throws|throws
name|MailException
block|{
comment|// noop
block|}
block|}
block|}
end_class

end_unit

