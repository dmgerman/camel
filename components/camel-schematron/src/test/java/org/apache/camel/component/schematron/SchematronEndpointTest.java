begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.schematron
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|schematron
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
name|schematron
operator|.
name|constant
operator|.
name|Constants
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
name|DefaultExchange
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for SchematronEndpoint.  *  */
end_comment

begin_class
DECL|class|SchematronEndpointTest
specifier|public
class|class
name|SchematronEndpointTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSchematronFileReadFromClassPath ()
specifier|public
name|void
name|testSchematronFileReadFromClassPath
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|payload
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
literal|"xml/article-1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"schematron://sch/schematron-1.sch"
argument_list|)
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
comment|// invoke the component.
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|String
name|report
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|VALIDATION_REPORT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSchematronFileReadFromFileSystem ()
specifier|public
name|void
name|testSchematronFileReadFromFileSystem
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|payload
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
literal|"xml/article-2.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|ClassLoader
operator|.
name|getSystemResource
argument_list|(
literal|"sch/schematron-1.sch"
argument_list|)
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"schematron://"
operator|+
name|path
argument_list|)
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
comment|// invoke the component.
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|String
name|report
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|VALIDATION_REPORT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

