begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|ahc
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|testing
operator|.
name|Helper
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
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|JUnit4TestRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|equinox
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|felix
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|scanFeatures
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|workingDirectory
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|AhcTest
specifier|public
class|class
name|AhcTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
annotation|@
name|Test
DECL|method|testAhcGet ()
specifier|public
name|void
name|testAhcGet
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"ahc:http://localhost:9081/foo"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Error doing POST in OSGi"
argument_list|)
DECL|method|testAhcPost ()
specifier|public
name|void
name|testAhcPost
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"ahc:http://localhost:9081/foo"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Requires online internet for testing"
argument_list|)
DECL|method|testAhcGoogle ()
specifier|public
name|void
name|testAhcGoogle
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"ahc:http://www.google.se"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|reply
argument_list|)
expr_stmt|;
block|}
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
literal|"jetty:http://0.0.0.0:9081/foo"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
comment|// Default karaf environment
name|Helper
operator|.
name|getDefaultOptions
argument_list|(
comment|// this is how you set the default log level when using pax logging (logProfile)
name|Helper
operator|.
name|setLogLevel
argument_list|(
literal|"WARN"
argument_list|)
argument_list|)
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-core"
argument_list|,
literal|"camel-test"
argument_list|,
literal|"camel-jetty"
argument_list|,
literal|"camel-ahc"
argument_list|)
argument_list|,
name|workingDirectory
argument_list|(
literal|"target/paxrunner/"
argument_list|)
argument_list|,
name|felix
argument_list|()
argument_list|,
name|equinox
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

