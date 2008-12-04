begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|PersonService
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
name|junit38
operator|.
name|AbstractJUnit38SpringContextTests
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"/org/apache/camel/component/cxf/context-camel-1145.xml"
block|}
argument_list|)
DECL|class|TestCamel1145Route
specifier|public
class|class
name|TestCamel1145Route
extends|extends
name|AbstractJUnit38SpringContextTests
block|{
DECL|method|testCamel1145Route ()
specifier|public
name|void
name|testCamel1145Route
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdlURL
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:9000/PersonService/?wsdl"
argument_list|)
decl_stmt|;
name|PersonService
name|ss
init|=
operator|new
name|PersonService
argument_list|(
name|wsdlURL
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/non-wrapper"
argument_list|,
literal|"PersonService"
argument_list|)
argument_list|)
decl_stmt|;
name|Person
name|client
init|=
name|ss
operator|.
name|getSoap
argument_list|()
decl_stmt|;
name|GetPerson
name|request
init|=
operator|new
name|GetPerson
argument_list|()
decl_stmt|;
name|request
operator|.
name|setPersonId
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|GetPersonResponse
name|response
init|=
name|client
operator|.
name|getPerson
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
literal|"Bill"
argument_list|,
name|response
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
literal|"Test"
argument_list|,
name|response
operator|.
name|getSsn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
literal|"hello world!"
argument_list|,
name|response
operator|.
name|getPersonId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

