begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.greeter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|greeter
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
name|AvailablePortFinder
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|JmsToCxfInOutTest
specifier|public
class|class
name|JmsToCxfInOutTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|port
specifier|private
specifier|static
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
static|static
block|{
comment|//set them as system properties so Spring can use the property place holder
comment|//things to set them into the URL's in the spring contexts
name|System
operator|.
name|setProperty
argument_list|(
literal|"JmsToCxfInOutTest.port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Autowired
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testJmsToCxfInOut ()
specifier|public
name|void
name|testJmsToCxfInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"jms:queue:bridge.cxf"
argument_list|,
literal|"Willem"
argument_list|,
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"greetMe"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Willem"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// call for the other operation
name|out
operator|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"jms:queue:bridge.cxf"
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"sayHi"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

