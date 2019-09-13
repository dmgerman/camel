begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet.springboot.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
operator|.
name|springboot
operator|.
name|test
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|web
operator|.
name|client
operator|.
name|TestRestTemplate
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
name|SpringRunner
import|;
end_import

begin_comment
comment|/**  * Testing HttpMessage used with Servlet component.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootApplication
annotation|@
name|DirtiesContext
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
name|ServletHttpMessageTest
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootTest
argument_list|(
name|webEnvironment
operator|=
name|SpringBootTest
operator|.
name|WebEnvironment
operator|.
name|RANDOM_PORT
argument_list|)
DECL|class|ServletHttpMessageTest
specifier|public
class|class
name|ServletHttpMessageTest
block|{
annotation|@
name|Autowired
DECL|field|restTemplate
specifier|private
name|TestRestTemplate
name|restTemplate
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|rest
argument_list|()
operator|.
name|get
argument_list|(
literal|"/outMessageNullBody"
argument_list|)
operator|.
name|produces
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|route
argument_list|()
comment|// read body at least once
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
comment|// simulate endpoints that may put null to out message body
operator|.
name|process
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
argument_list|)
comment|// ensure reading body again does not cause an exception
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOutMessageNullBody ()
specifier|public
name|void
name|testOutMessageNullBody
parameter_list|()
block|{
name|Assert
operator|.
name|assertNull
argument_list|(
name|restTemplate
operator|.
name|getForEntity
argument_list|(
literal|"/camel/outMessageNullBody"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

