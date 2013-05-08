begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
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
name|junit
operator|.
name|Test
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
DECL|class|MainTest
specifier|public
class|class
name|MainTest
block|{
annotation|@
name|Test
DECL|method|testMyMain ()
specifier|public
name|void
name|testMyMain
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|setBundleName
argument_list|(
literal|"MyMainBundle"
argument_list|)
expr_stmt|;
comment|// as we run this test without packing ourselves as bundle, then include ourselves
name|main
operator|.
name|setIncludeSelfAsBundle
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// setup the blueprint file here
name|main
operator|.
name|setDescriptors
argument_list|(
literal|"org/apache/camel/test/blueprint/main-loadfile.xml"
argument_list|)
expr_stmt|;
comment|// set the configAdmin persistent id
name|main
operator|.
name|setConfigAdminPid
argument_list|(
literal|"stuff"
argument_list|)
expr_stmt|;
comment|// set the configAdmin persistent file name
name|main
operator|.
name|setConfigAdminFileName
argument_list|(
literal|"src/test/resources/etc/stuff.cfg"
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|main
operator|.
name|getCamelTemplate
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the template here"
argument_list|,
name|template
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Bye hello"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

