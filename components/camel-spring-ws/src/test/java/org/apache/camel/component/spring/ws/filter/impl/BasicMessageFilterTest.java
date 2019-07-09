begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.filter.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|filter
operator|.
name|impl
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
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|attachment
operator|.
name|AttachmentMessage
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
name|spring
operator|.
name|ws
operator|.
name|SpringWebserviceConstants
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
name|ExchangeTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fest
operator|.
name|assertions
operator|.
name|Assertions
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|JUnit4
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|pox
operator|.
name|dom
operator|.
name|DomPoxMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|pox
operator|.
name|dom
operator|.
name|DomPoxMessageFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|saaj
operator|.
name|SaajSoapMessageFactory
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|value
operator|=
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|BasicMessageFilterTest
specifier|public
class|class
name|BasicMessageFilterTest
extends|extends
name|ExchangeTestSupport
block|{
DECL|field|filter
specifier|private
name|BasicMessageFilter
name|filter
decl_stmt|;
DECL|field|message
specifier|private
name|SoapMessage
name|message
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|filter
operator|=
operator|new
name|BasicMessageFilter
argument_list|()
expr_stmt|;
name|SaajSoapMessageFactory
name|saajSoapMessageFactory
init|=
operator|new
name|SaajSoapMessageFactory
argument_list|()
decl_stmt|;
name|saajSoapMessageFactory
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|message
operator|=
name|saajSoapMessageFactory
operator|.
name|createWebServiceMessage
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
throws|throws
name|Exception
block|{
name|filter
operator|.
name|filterConsumer
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterProducer
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullsWithExchange ()
specifier|public
name|void
name|testNullsWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|filter
operator|.
name|filterConsumer
argument_list|(
name|exchange
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterProducer
argument_list|(
name|exchange
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|nonSoapMessageShouldBeSkipped ()
specifier|public
name|void
name|nonSoapMessageShouldBeSkipped
parameter_list|()
throws|throws
name|Exception
block|{
name|DomPoxMessage
name|domPoxMessage
init|=
operator|new
name|DomPoxMessageFactory
argument_list|()
operator|.
name|createWebServiceMessage
argument_list|()
decl_stmt|;
name|filter
operator|.
name|filterConsumer
argument_list|(
name|exchange
argument_list|,
name|domPoxMessage
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterProducer
argument_list|(
name|exchange
argument_list|,
name|domPoxMessage
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withoutHeader ()
specifier|public
name|void
name|withoutHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|hasAttachments
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|getAttachments
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getOut
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|hasAttachments
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|getAttachments
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|filter
operator|.
name|filterProducer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterConsumer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|examineAllHeaderElements
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|getAllAttributes
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeCamelInternalHeaderAttributes ()
specifier|public
name|void
name|removeCamelInternalHeaderAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_SOAP_ACTION
argument_list|,
literal|"mustBeRemoved"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ADDRESSING_ACTION
argument_list|,
literal|"mustBeRemoved"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ADDRESSING_PRODUCER_FAULT_TO
argument_list|,
literal|"mustBeRemoved"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ADDRESSING_PRODUCER_REPLY_TO
argument_list|,
literal|"mustBeRemoved"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ADDRESSING_CONSUMER_FAULT_ACTION
argument_list|,
literal|"mustBeRemoved"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ADDRESSING_CONSUMER_OUTPUT_ACTION
argument_list|,
literal|"mustBeRemoved"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ENDPOINT_URI
argument_list|,
literal|"mustBeRemoved"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
literal|"breadcrumbId"
argument_list|,
literal|"mustBeRemoved"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterConsumer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|examineAllHeaderElements
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|getAllAttributes
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|consumerWithHeader ()
specifier|public
name|void
name|consumerWithHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
literal|"headerAttributeKey"
argument_list|,
literal|"testAttributeValue"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
literal|"headerAttributeElement"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://shouldBeInHeader"
argument_list|,
literal|"<myElement />"
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterConsumer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|examineAllHeaderElements
argument_list|()
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|getAllAttributes
argument_list|()
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producerWithHeader ()
specifier|public
name|void
name|producerWithHeader
parameter_list|()
throws|throws
name|Exception
block|{
comment|// foo is already in the header.in from the parent ExchangeTestSupport
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
literal|"headerAttributeKey"
argument_list|,
literal|"testAttributeValue"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
literal|"headerAttributeElement"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://shouldBeInHeader"
argument_list|,
literal|"<myElement />"
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterProducer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|examineAllHeaderElements
argument_list|()
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|getAllAttributes
argument_list|()
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withoutAttachment ()
specifier|public
name|void
name|withoutAttachment
parameter_list|()
throws|throws
name|Exception
block|{
name|filter
operator|.
name|filterConsumer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterProducer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producerWithAttachment ()
specifier|public
name|void
name|producerWithAttachment
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|addAttachment
argument_list|(
literal|"testAttachment"
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/sampleAttachment.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterProducer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachment
argument_list|(
literal|"testAttachment"
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|consumerWithAttachment ()
specifier|public
name|void
name|consumerWithAttachment
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getMessage
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|addAttachment
argument_list|(
literal|"testAttachment"
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/sampleAttachment.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|.
name|filterConsumer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|message
operator|.
name|getAttachment
argument_list|(
literal|"testAttachment"
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

