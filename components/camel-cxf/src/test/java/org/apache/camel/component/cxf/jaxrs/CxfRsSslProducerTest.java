begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

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
name|ExchangePattern
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
name|component
operator|.
name|cxf
operator|.
name|CXFTestSupport
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
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|Customer
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
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_class
DECL|class|CxfRsSslProducerTest
specifier|public
class|class
name|CxfRsSslProducerTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|port1
specifier|private
specifier|static
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getSslPort
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getPort1 ()
specifier|public
name|int
name|getPort1
parameter_list|()
block|{
return|return
name|port1
return|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/jaxrs/CxfRsSpringSslProducer.xml"
argument_list|)
return|;
block|}
DECL|method|setupDestinationURL (Message inMessage)
specifier|protected
name|void
name|setupDestinationURL
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
comment|// do nothing here
block|}
annotation|@
name|Test
DECL|method|testCorrectTrustStore ()
specifier|public
name|void
name|testCorrectTrustStore
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct://trust"
argument_list|,
operator|new
name|MyProcessor
argument_list|()
argument_list|)
decl_stmt|;
comment|// get the response message
name|Customer
name|response
init|=
operator|(
name|Customer
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer id "
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer name"
argument_list|,
name|response
operator|.
name|getName
argument_list|()
argument_list|,
literal|"John"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response code"
argument_list|,
literal|200
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong header value"
argument_list|,
literal|"value"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoTrustStore ()
specifier|public
name|void
name|testNoTrustStore
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct://noTrust"
argument_list|,
operator|new
name|MyProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|Exception
name|e
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"javax.net.ssl.SSLHandshakeException"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWrongTrustStore ()
specifier|public
name|void
name|testWrongTrustStore
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct://wrongTrust"
argument_list|,
operator|new
name|MyProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|Exception
name|e
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"javax.net.ssl.SSLHandshakeException"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|MyProcessor
specifier|private
class|class
name|MyProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|setupDestinationURL
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|// using the http central client API
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// set the Http method
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
comment|// set the relative path
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
literal|"/customerservice/customers/123"
argument_list|)
expr_stmt|;
comment|// Specify the response class , cxfrs will use InputStream as the response object type
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
name|Customer
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// set a customer header
name|inMessage
operator|.
name|setHeader
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
comment|// since we use the Get method, so we don't need to set the message body
name|inMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

