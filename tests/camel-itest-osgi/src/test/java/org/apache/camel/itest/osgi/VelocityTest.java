begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi
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
package|;
end_package

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
name|InvalidPayloadException
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
name|Message
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
name|CoreOptions
operator|.
name|mavenBundle
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
name|options
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
name|logProfile
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
name|profile
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|VelocityTest
specifier|public
class|class
name|VelocityTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
annotation|@
name|Test
DECL|method|testReceivesFooResponse ()
specifier|public
name|void
name|testReceivesFooResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"foo"
argument_list|,
literal|"<hello>foo</hello>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceivesBarResponse ()
specifier|public
name|void
name|testReceivesBarResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"bar"
argument_list|,
literal|"<hello>bar</hello>"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRespondsWith (final String value, String expectedBody)
specifier|protected
name|void
name|assertRespondsWith
parameter_list|(
specifier|final
name|String
name|value
parameter_list|,
name|String
name|expectedBody
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Exchange
name|response
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:a"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"answer"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"cheese"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertOutMessageBodyEquals
argument_list|(
name|response
argument_list|,
name|expectedBody
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
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"velocity:org/apache/camel/itest/osgi/example.vm"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
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
block|{
name|Option
index|[]
name|options
init|=
name|options
argument_list|(
comment|// install log service using pax runners profile abstraction (there are more profiles, like DS)
name|logProfile
argument_list|()
operator|.
name|version
argument_list|(
literal|"1.3.0"
argument_list|)
argument_list|,
comment|// install the spring dm profile
name|profile
argument_list|(
literal|"spring.dm"
argument_list|)
operator|.
name|version
argument_list|(
literal|"1.2.0"
argument_list|)
argument_list|,
comment|// this is how you set the default log level when using pax logging (logProfile)
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
name|systemProperty
argument_list|(
literal|"org.ops4j.pax.logging.DefaultServiceLog.level"
argument_list|)
operator|.
name|value
argument_list|(
literal|"INFO"
argument_list|)
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.camel.karaf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"features"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
literal|"xml/features"
argument_list|)
argument_list|,
literal|"camel-core"
argument_list|,
literal|"camel-osgi"
argument_list|,
literal|"camel-spring"
argument_list|,
literal|"camel-test"
argument_list|,
literal|"camel-velocity"
argument_list|)
argument_list|,
name|felix
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

