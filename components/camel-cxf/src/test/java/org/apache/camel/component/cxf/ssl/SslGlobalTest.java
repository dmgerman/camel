begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.ssl
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
name|ssl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|SSLContextParametersAware
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
name|camel
operator|.
name|support
operator|.
name|jsse
operator|.
name|SSLContextParameters
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
DECL|class|SslGlobalTest
specifier|public
class|class
name|SslGlobalTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|GREET_ME_OPERATION
specifier|protected
specifier|static
specifier|final
name|String
name|GREET_ME_OPERATION
init|=
literal|"greetMe"
decl_stmt|;
DECL|field|TEST_MESSAGE
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"Hello World!"
decl_stmt|;
DECL|field|JAXWS_SERVER_ADDRESS
specifier|protected
specifier|static
specifier|final
name|String
name|JAXWS_SERVER_ADDRESS
init|=
literal|"https://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfSslTest/SoapContext/SoapPort"
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
name|SSLContextParameters
name|parameters
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"mySslContext"
argument_list|,
name|SSLContextParameters
operator|.
name|class
argument_list|)
decl_stmt|;
operator|(
operator|(
name|SSLContextParametersAware
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"cxf"
argument_list|)
operator|)
operator|.
name|setUseGlobalSslContextParameters
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setSSLContextParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testInvokingTrustRoute ()
specifier|public
name|void
name|testInvokingTrustRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|reply
init|=
name|sendJaxWsMessage
argument_list|(
literal|"direct:trust"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"We expect no exception here"
argument_list|,
name|reply
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokingWrongTrustRoute ()
specifier|public
name|void
name|testInvokingWrongTrustRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|reply
init|=
name|sendJaxWsMessage
argument_list|(
literal|"direct:wrongTrust"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"We expect the exception here"
argument_list|,
name|reply
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|Throwable
name|e
init|=
name|reply
operator|.
name|getException
argument_list|()
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|e
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
DECL|method|sendJaxWsMessage (String endpointUri)
specifier|protected
name|Exchange
name|sendJaxWsMessage
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|GREET_ME_OPERATION
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
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
comment|// we can put the http conduit configuration here
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/CxfGlobalSslContext.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

