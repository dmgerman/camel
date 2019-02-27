begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ses
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|ses
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|SendEmailRequest
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
name|Processor
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
name|builder
operator|.
name|RouteBuilder
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

begin_class
DECL|class|SesComponentTest
specifier|public
class|class
name|SesComponentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|sesClient
specifier|private
name|AmazonSESClientMock
name|sesClient
decl_stmt|;
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
name|sesClient
operator|=
operator|new
name|AmazonSESClientMock
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendInOnlyMessageUsingUrlOptions ()
specifier|public
name|void
name|sendInOnlyMessageUsingUrlOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my message text."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|SendEmailRequest
name|sendEmailRequest
init|=
name|sesClient
operator|.
name|getSendEmailRequest
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"from@example.com"
argument_list|,
name|sendEmailRequest
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|getTo
argument_list|(
name|sendEmailRequest
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getTo
argument_list|(
name|sendEmailRequest
argument_list|)
operator|.
name|contains
argument_list|(
literal|"to1@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getTo
argument_list|(
name|sendEmailRequest
argument_list|)
operator|.
name|contains
argument_list|(
literal|"to2@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bounce@example.com"
argument_list|,
name|sendEmailRequest
operator|.
name|getReturnPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sendEmailRequest
operator|.
name|getReplyToAddresses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sendEmailRequest
operator|.
name|getReplyToAddresses
argument_list|()
operator|.
name|contains
argument_list|(
literal|"replyTo1@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sendEmailRequest
operator|.
name|getReplyToAddresses
argument_list|()
operator|.
name|contains
argument_list|(
literal|"replyTo2@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Subject"
argument_list|,
name|getSubject
argument_list|(
name|sendEmailRequest
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is my message text."
argument_list|,
name|getBody
argument_list|(
name|sendEmailRequest
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendInOutMessageUsingUrlOptions ()
specifier|public
name|void
name|sendInOutMessageUsingUrlOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my message text."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendMessageUsingMessageHeaders ()
specifier|public
name|void
name|sendMessageUsingMessageHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my message text."
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SesConstants
operator|.
name|FROM
argument_list|,
literal|"anotherFrom@example.com"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SesConstants
operator|.
name|TO
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"anotherTo1@example.com"
argument_list|,
literal|"anotherTo2@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SesConstants
operator|.
name|RETURN_PATH
argument_list|,
literal|"anotherBounce@example.com"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SesConstants
operator|.
name|REPLY_TO_ADDRESSES
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"anotherReplyTo1@example.com"
argument_list|,
literal|"anotherReplyTo2@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SesConstants
operator|.
name|SUBJECT
argument_list|,
literal|"anotherSubject"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|SendEmailRequest
name|sendEmailRequest
init|=
name|sesClient
operator|.
name|getSendEmailRequest
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"anotherFrom@example.com"
argument_list|,
name|sendEmailRequest
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|getTo
argument_list|(
name|sendEmailRequest
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getTo
argument_list|(
name|sendEmailRequest
argument_list|)
operator|.
name|contains
argument_list|(
literal|"anotherTo1@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getTo
argument_list|(
name|sendEmailRequest
argument_list|)
operator|.
name|contains
argument_list|(
literal|"anotherTo2@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"anotherBounce@example.com"
argument_list|,
name|sendEmailRequest
operator|.
name|getReturnPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sendEmailRequest
operator|.
name|getReplyToAddresses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sendEmailRequest
operator|.
name|getReplyToAddresses
argument_list|()
operator|.
name|contains
argument_list|(
literal|"anotherReplyTo1@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sendEmailRequest
operator|.
name|getReplyToAddresses
argument_list|()
operator|.
name|contains
argument_list|(
literal|"anotherReplyTo2@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"anotherSubject"
argument_list|,
name|getSubject
argument_list|(
name|sendEmailRequest
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is my message text."
argument_list|,
name|getBody
argument_list|(
name|sendEmailRequest
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"amazonSESClient"
argument_list|,
name|sesClient
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-ses://from@example.com"
operator|+
literal|"?to=to1@example.com,to2@example.com"
operator|+
literal|"&subject=Subject"
operator|+
literal|"&returnPath=bounce@example.com"
operator|+
literal|"&replyToAddresses=replyTo1@example.com,replyTo2@example.com"
operator|+
literal|"&amazonSESClient=#amazonSESClient"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getBody (SendEmailRequest sendEmailRequest)
specifier|private
name|String
name|getBody
parameter_list|(
name|SendEmailRequest
name|sendEmailRequest
parameter_list|)
block|{
return|return
name|sendEmailRequest
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getText
argument_list|()
operator|.
name|getData
argument_list|()
return|;
block|}
DECL|method|getSubject (SendEmailRequest sendEmailRequest)
specifier|private
name|String
name|getSubject
parameter_list|(
name|SendEmailRequest
name|sendEmailRequest
parameter_list|)
block|{
return|return
name|sendEmailRequest
operator|.
name|getMessage
argument_list|()
operator|.
name|getSubject
argument_list|()
operator|.
name|getData
argument_list|()
return|;
block|}
DECL|method|getTo (SendEmailRequest sendEmailRequest)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getTo
parameter_list|(
name|SendEmailRequest
name|sendEmailRequest
parameter_list|)
block|{
return|return
name|sendEmailRequest
operator|.
name|getDestination
argument_list|()
operator|.
name|getToAddresses
argument_list|()
return|;
block|}
block|}
end_class

end_unit
