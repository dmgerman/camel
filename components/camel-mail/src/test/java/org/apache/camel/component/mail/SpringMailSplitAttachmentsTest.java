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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|FileDataSource
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
name|Producer
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|spring
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  *  Spring XML version of {@link MailSplitAttachmentsTest}  */
end_comment

begin_class
DECL|class|SpringMailSplitAttachmentsTest
specifier|public
class|class
name|SpringMailSplitAttachmentsTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/mail/SpringMailSplitAttachmentsTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Before
DECL|method|clearMailBox ()
specifier|public
name|void
name|clearMailBox
parameter_list|()
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
comment|// create the exchange with the mail message that is multipart with a file and a Hello World text/plain message.
name|endpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"smtp://james@mymailserver.com?password=secret"
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|endpoint
operator|.
name|createExchange
argument_list|()
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|in
operator|.
name|addAttachment
argument_list|(
literal|"logo.jpeg"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/test/data/logo.jpeg"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|in
operator|.
name|addAttachment
argument_list|(
literal|"license.txt"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/main/resources/META-INF/LICENSE.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitAttachments ()
specifier|public
name|void
name|testSplitAttachments
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Message
name|first
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|second
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|first
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|second
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|file1
init|=
name|first
operator|.
name|getAttachments
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|file2
init|=
name|second
operator|.
name|getAttachments
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|logo
init|=
name|file1
operator|.
name|equals
argument_list|(
literal|"logo.jpeg"
argument_list|)
operator|||
name|file2
operator|.
name|equals
argument_list|(
literal|"logo.jpeg"
argument_list|)
decl_stmt|;
name|boolean
name|license
init|=
name|file1
operator|.
name|equals
argument_list|(
literal|"license.txt"
argument_list|)
operator|||
name|file2
operator|.
name|equals
argument_list|(
literal|"license.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should have logo.jpeg file attachment"
argument_list|,
name|logo
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have license.txt file attachment"
argument_list|,
name|license
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractAttachments ()
specifier|public
name|void
name|testExtractAttachments
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// set the expression to extract the attachments as byte[]s
name|SplitAttachmentsExpression
name|splitAttachmentsExpression
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|SplitAttachmentsExpression
operator|.
name|class
argument_list|)
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|splitAttachmentsExpression
operator|.
name|setExtractAttachments
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Message
name|first
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|second
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// check it's no longer an attachment, but is the message body
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|first
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|second
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"logo.jpeg"
argument_list|,
name|first
operator|.
name|getHeader
argument_list|(
literal|"CamelSplitAttachmentId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"license.txt"
argument_list|,
name|second
operator|.
name|getHeader
argument_list|(
literal|"CamelSplitAttachmentId"
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|expected1
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/test/data/logo.jpeg"
argument_list|)
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|expected2
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/main/resources/META-INF/LICENSE.txt"
argument_list|)
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|expected1
argument_list|,
name|first
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|expected2
argument_list|,
name|second
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

