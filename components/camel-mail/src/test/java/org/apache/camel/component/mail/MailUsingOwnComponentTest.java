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
name|javax
operator|.
name|mail
operator|.
name|Message
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
name|CamelContext
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
name|jvnet
operator|.
name|mock_javamail
operator|.
name|Mailbox
import|;
end_import

begin_comment
comment|/**  * Unit test for CAMEL-1249  */
end_comment

begin_class
DECL|class|MailUsingOwnComponentTest
specifier|public
class|class
name|MailUsingOwnComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|MailConfiguration
name|config
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setProtocol
argument_list|(
literal|"smtp"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUsername
argument_list|(
literal|"james"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPort
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPassword
argument_list|(
literal|"admin"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setIgnoreUriScheme
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|MailComponent
name|myMailbox
init|=
operator|new
name|MailComponent
argument_list|()
decl_stmt|;
name|myMailbox
operator|.
name|setConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"mailbox"
argument_list|,
name|myMailbox
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testUsingOwnMailComponent ()
specifier|public
name|void
name|testUsingOwnMailComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"mailbox:foo"
argument_list|,
literal|"Hello Mailbox"
argument_list|,
literal|"to"
argument_list|,
literal|"davsclaus@apache.org"
argument_list|)
expr_stmt|;
name|Mailbox
name|box
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"davsclaus@apache.org"
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|box
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"davsclaus@apache.org"
argument_list|,
name|msg
operator|.
name|getRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|)
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

