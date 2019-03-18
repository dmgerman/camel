begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.security
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
name|security
package|;
end_package

begin_import
import|import
name|net
operator|.
name|javacrumbs
operator|.
name|calc
operator|.
name|model
operator|.
name|PlusRequest
import|;
end_import

begin_import
import|import
name|net
operator|.
name|javacrumbs
operator|.
name|calc
operator|.
name|model
operator|.
name|PlusResponse
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
name|Produce
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
name|ProducerTemplate
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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|client
operator|.
name|core
operator|.
name|WebServiceTemplate
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
name|client
operator|.
name|support
operator|.
name|interceptor
operator|.
name|ClientInterceptor
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
name|security
operator|.
name|wss4j2
operator|.
name|Wss4jSecurityInterceptor
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"run manually since it requires running sample"
operator|+
literal|" secured ws on j2ee-compliant application server"
argument_list|)
DECL|class|ProducerWss4JSecurityHeaderTest
specifier|public
class|class
name|ProducerWss4JSecurityHeaderTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Produce
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|webServiceTemplate
specifier|private
name|WebServiceTemplate
name|webServiceTemplate
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|webServiceTemplate
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"webServiceTemplate"
argument_list|,
name|WebServiceTemplate
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResponseUsingWss4jSampleInterceptorWithoutHeadersRemoved ()
specifier|public
name|void
name|testResponseUsingWss4jSampleInterceptorWithoutHeadersRemoved
parameter_list|()
throws|throws
name|Exception
block|{
name|setRemoveHeaders
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|PlusResponse
name|result
init|=
name|createSampleRequestResponsePair
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ProducerWss4JSecurityHeaderTestInterceptor
operator|.
name|isX509DataPresent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResponseUsingWss4jSampleInterceptorWithHeadersRemoved ()
specifier|public
name|void
name|testResponseUsingWss4jSampleInterceptorWithHeadersRemoved
parameter_list|()
throws|throws
name|Exception
block|{
name|setRemoveHeaders
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|PlusResponse
name|result
init|=
name|createSampleRequestResponsePair
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ProducerWss4JSecurityHeaderTestInterceptor
operator|.
name|isX509DataPresent
argument_list|)
expr_stmt|;
block|}
DECL|method|createSampleRequestResponsePair ()
specifier|private
name|PlusResponse
name|createSampleRequestResponsePair
parameter_list|()
block|{
name|PlusRequest
name|request
init|=
operator|new
name|PlusRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setA
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setB
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|PlusResponse
name|result
init|=
operator|(
name|PlusResponse
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:testHeader"
argument_list|,
name|request
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
DECL|method|setRemoveHeaders (boolean isRemoved)
specifier|private
name|void
name|setRemoveHeaders
parameter_list|(
name|boolean
name|isRemoved
parameter_list|)
block|{
name|ClientInterceptor
index|[]
name|clientInterceptors
init|=
name|webServiceTemplate
operator|.
name|getInterceptors
argument_list|()
decl_stmt|;
for|for
control|(
name|ClientInterceptor
name|clientInterceptor
range|:
name|clientInterceptors
control|)
block|{
if|if
condition|(
name|clientInterceptor
operator|instanceof
name|Wss4jSecurityInterceptor
condition|)
block|{
name|Wss4jSecurityInterceptor
name|wss4jSampleInterceptor
init|=
operator|(
name|Wss4jSecurityInterceptor
operator|)
name|clientInterceptor
decl_stmt|;
name|wss4jSampleInterceptor
operator|.
name|setRemoveSecurityHeader
argument_list|(
name|isRemoved
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"org/apache/camel/component/spring/ws/security/ProducerWss4JSecurityHeaderTest-context.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

