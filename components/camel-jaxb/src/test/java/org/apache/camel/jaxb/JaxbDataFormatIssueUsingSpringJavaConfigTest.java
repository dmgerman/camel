begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|EndpointInject
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
name|converter
operator|.
name|jaxb
operator|.
name|JaxbDataFormat
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
name|javaconfig
operator|.
name|SingleRouteCamelConfiguration
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
name|javaconfig
operator|.
name|test
operator|.
name|JavaConfigContextLoader
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
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
literal|"org.apache.camel.jaxb.JaxbDataFormatIssueUsingSpringJavaConfigTest$ContextConfig"
argument_list|,
name|loader
operator|=
name|JavaConfigContextLoader
operator|.
name|class
argument_list|)
DECL|class|JaxbDataFormatIssueUsingSpringJavaConfigTest
specifier|public
class|class
name|JaxbDataFormatIssueUsingSpringJavaConfigTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mock
specifier|protected
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testJaxbMarshalling ()
specifier|public
name|void
name|testJaxbMarshalling
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"<foo><bar>Hello Bar</bar></foo>"
argument_list|)
expr_stmt|;
name|Foo
name|foo
init|=
operator|new
name|Foo
argument_list|()
decl_stmt|;
name|foo
operator|.
name|setBar
argument_list|(
literal|"Hello Bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Configuration
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|SingleRouteCamelConfiguration
block|{
annotation|@
name|Bean
DECL|method|route ()
specifier|public
name|RouteBuilder
name|route
parameter_list|()
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
name|JaxbDataFormat
name|jaxb
init|=
operator|new
name|JaxbDataFormat
argument_list|(
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|jaxb
operator|.
name|setPrettyPrint
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|jaxb
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:xml"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
annotation|@
name|XmlRootElement
DECL|class|Foo
specifier|public
specifier|static
class|class
name|Foo
block|{
DECL|field|bar
specifier|private
name|String
name|bar
decl_stmt|;
DECL|method|getBar ()
specifier|public
name|String
name|getBar
parameter_list|()
block|{
return|return
name|bar
return|;
block|}
DECL|method|setBar (String bar)
specifier|public
name|void
name|setBar
parameter_list|(
name|String
name|bar
parameter_list|)
block|{
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

