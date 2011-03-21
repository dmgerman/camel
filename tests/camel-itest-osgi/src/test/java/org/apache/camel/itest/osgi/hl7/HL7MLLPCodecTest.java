begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.hl7
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
name|hl7
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationSpringTestSupport
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
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|support
operator|.
name|OsgiBundleXmlApplicationContext
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|HL7MLLPCodecTest
specifier|public
class|class
name|HL7MLLPCodecTest
extends|extends
name|OSGiIntegrationSpringTestSupport
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|OsgiBundleXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|OsgiBundleXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/itest/osgi/hl7/CamelContext.xml"
block|}
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testSendHL7Message ()
specifier|public
name|void
name|testSendHL7Message
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|line1
init|=
literal|"MSH|^~\\&|MYSENDER|MYRECEIVER|MYAPPLICATION||200612211200||QRY^A19|1234|P|2.4"
decl_stmt|;
name|String
name|line2
init|=
literal|"QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||"
decl_stmt|;
name|StringBuilder
name|in
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|in
operator|.
name|append
argument_list|(
name|line1
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
name|line2
argument_list|)
expr_stmt|;
name|String
name|out
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"mina:tcp://127.0.0.1:8888?sync=true&codec=#hl7codec"
argument_list|,
name|in
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|lines
init|=
name|out
operator|.
name|split
argument_list|(
literal|"\r"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MSH|^~\\&|MYSENDER||||200701011539||ADR^A19||||123"
argument_list|,
name|lines
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MSA|AA|123"
argument_list|,
name|lines
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
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
name|String
name|out
init|=
literal|"MSH|^~\\&|MYSENDER||||200701011539||ADR^A19||||123\rMSA|AA|123\n"
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|out
argument_list|)
expr_stmt|;
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
literal|"camel-spring"
argument_list|,
literal|"camel-test"
argument_list|,
literal|"camel-mina"
argument_list|,
literal|"camel-hl7"
argument_list|)
argument_list|,
comment|// add hl7 osgi bundle
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"http://hl7api.sourceforge.net/m2/!ca.uhn.hapi"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"hapi-osgi-base"
argument_list|)
operator|.
name|version
argument_list|(
literal|"1.0.1"
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

