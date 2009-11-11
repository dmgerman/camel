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
name|Iterator
import|;
end_import

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
name|Address
import|;
end_import

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
name|InternetAddress
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
name|Endpoint
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
name|Exchange
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|CastUtils
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MailMessageTest
specifier|public
class|class
name|MailMessageTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|mailSession
specifier|private
name|Session
name|mailSession
decl_stmt|;
DECL|field|mimeMessage
specifier|private
name|MimeMessage
name|mimeMessage
decl_stmt|;
DECL|field|endpoint
specifier|private
name|MailEndpoint
name|endpoint
decl_stmt|;
DECL|field|body
specifier|private
name|String
name|body
init|=
literal|"Hello World!"
decl_stmt|;
annotation|@
name|Test
DECL|method|testMailMessageHandlesMultipleHeaders ()
specifier|public
name|void
name|testMailMessageHandlesMultipleHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|mimeMessage
operator|.
name|setRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|,
operator|new
name|Address
index|[]
block|{
operator|new
name|InternetAddress
argument_list|(
literal|"foo@localhost"
argument_list|)
block|,
operator|new
name|InternetAddress
argument_list|(
literal|"bar@localhost"
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|mimeMessage
argument_list|)
decl_stmt|;
name|MailMessage
name|in
init|=
operator|(
name|MailMessage
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mail body"
argument_list|,
name|body
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
comment|// need to use iterator as some mail impl returns String[] and others a single String with comma as separator
comment|// so we let Camel create an iterator so we can use the same code for the test
name|Object
name|to
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"TO"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|to
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|assertEquals
argument_list|(
literal|"foo@localhost"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"bar@localhost"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|i
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMailMessageHandlesSingleHeader ()
specifier|public
name|void
name|testMailMessageHandlesSingleHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|mimeMessage
operator|.
name|setRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|,
operator|new
name|Address
index|[]
block|{
operator|new
name|InternetAddress
argument_list|(
literal|"frank@localhost"
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|mimeMessage
argument_list|)
decl_stmt|;
name|MailMessage
name|in
init|=
operator|(
name|MailMessage
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Object
name|header
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"TO"
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|header
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
literal|"frank@localhost"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"body"
argument_list|,
name|body
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|endpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"pop3://someone@myhost:30/subject"
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail.smtp.host"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|mailSession
operator|=
name|Session
operator|.
name|getInstance
argument_list|(
name|properties
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mimeMessage
operator|=
operator|new
name|MimeMessage
argument_list|(
name|mailSession
argument_list|)
expr_stmt|;
name|mimeMessage
operator|.
name|setText
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveMandatoryEndpoint (String uri)
specifier|protected
name|MailEndpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|super
operator|.
name|resolveMandatoryEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|MailEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
block|}
end_class

end_unit

