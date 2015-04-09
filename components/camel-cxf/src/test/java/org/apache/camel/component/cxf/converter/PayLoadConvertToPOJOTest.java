begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.converter
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
name|converter
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
name|non_wrapper
operator|.
name|Person
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
name|non_wrapper
operator|.
name|types
operator|.
name|GetPerson
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
name|non_wrapper
operator|.
name|types
operator|.
name|GetPersonResponse
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
name|spring
operator|.
name|SpringCamelContext
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|BeforeClass
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

begin_class
DECL|class|PayLoadConvertToPOJOTest
specifier|public
class|class
name|PayLoadConvertToPOJOTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|applicationContext
specifier|protected
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUpSystemProperty ()
specifier|public
specifier|static
name|void
name|setUpSystemProperty
parameter_list|()
block|{
comment|// just force the CXFTestSupport to load before running the test
name|getPort1
argument_list|()
expr_stmt|;
block|}
DECL|method|getPort1 ()
specifier|public
specifier|static
name|int
name|getPort1
parameter_list|()
block|{
return|return
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
return|;
block|}
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
name|applicationContext
operator|=
name|createApplicationContext
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have created a valid spring context"
argument_list|,
name|applicationContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
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
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testClient ()
specifier|public
name|void
name|testClient
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort1
argument_list|()
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/CamelContext/RouterPort"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Person
operator|.
name|class
argument_list|)
expr_stmt|;
name|Person
name|person
init|=
name|factory
operator|.
name|create
argument_list|(
name|Person
operator|.
name|class
argument_list|)
decl_stmt|;
name|GetPerson
name|payload
init|=
operator|new
name|GetPerson
argument_list|()
decl_stmt|;
name|payload
operator|.
name|setPersonId
argument_list|(
literal|"1234"
argument_list|)
expr_stmt|;
name|GetPersonResponse
name|reply
init|=
name|person
operator|.
name|getPerson
argument_list|(
name|payload
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong person id."
argument_list|,
literal|"1234"
argument_list|,
name|reply
operator|.
name|getPersonId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"cxf:bean:routerEndpoint?dataFormat=PAYLOAD"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|process
argument_list|(
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
comment|// just try to turn the payload to the parameter we want
comment|// to use
name|GetPerson
name|request
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|GetPerson
operator|.
name|class
argument_list|)
decl_stmt|;
name|GetPersonResponse
name|reply
init|=
operator|new
name|GetPersonResponse
argument_list|()
decl_stmt|;
name|reply
operator|.
name|setPersonId
argument_list|(
name|request
operator|.
name|getPersonId
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/converter/PayloadConverterBeans.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

