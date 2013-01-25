begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|impl
operator|.
name|DefaultCamelContext
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
name|AbstractApplicationContext
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

begin_comment
comment|/**  * Unit test the example.  *  * @version   */
end_comment

begin_class
DECL|class|RmiTest
specifier|public
class|class
name|RmiTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|port
specifier|private
specifier|static
name|int
name|port
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setupFreePort ()
specifier|public
specifier|static
name|void
name|setupFreePort
parameter_list|()
throws|throws
name|Exception
block|{
comment|// find a free port number, and write that in the custom.properties file
comment|// which we will use for the unit tests, to avoid port number in use problems
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|String
name|s
init|=
literal|"port="
operator|+
name|port
decl_stmt|;
name|File
name|custom
init|=
operator|new
name|File
argument_list|(
literal|"target/custom.properties"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|custom
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"META-INF/spring/camelContext.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testRmi ()
specifier|public
name|void
name|testRmi
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create a new camel context to send the request so we can test the service which is deployed into a container
name|CamelContext
name|myContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ProducerTemplate
name|myTemplate
init|=
name|myContext
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|myTemplate
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Calling on port "
operator|+
name|port
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|myTemplate
operator|.
name|requestBody
argument_list|(
literal|"rmi://localhost:"
operator|+
name|port
operator|+
literal|"/helloServiceBean"
argument_list|,
literal|"Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|myTemplate
operator|.
name|stop
argument_list|()
expr_stmt|;
name|myContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

