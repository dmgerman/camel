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
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|model
operator|.
name|v24
operator|.
name|message
operator|.
name|QRY_A19
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|model
operator|.
name|v24
operator|.
name|segment
operator|.
name|MSH
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
name|PaxExam
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
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
annotation|@
name|Ignore
argument_list|(
literal|"TODO need to find a way fix the hl7codec setting issue"
argument_list|)
DECL|class|HL7MLLPCodec2Test
specifier|public
class|class
name|HL7MLLPCodec2Test
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
literal|"org/apache/camel/itest/osgi/hl7/CamelContext2.xml"
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
literal|"mina:tcp://127.0.0.1:8889?sync=true&codec=#hl7codec"
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
name|QRY_A19
name|a19
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|QRY_A19
operator|.
name|class
argument_list|)
decl_stmt|;
name|MSH
name|msh
init|=
name|a19
operator|.
name|getMSH
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MYSENDER"
argument_list|,
name|msh
operator|.
name|getSendingApplication
argument_list|()
operator|.
name|getHd1_NamespaceID
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
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
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
comment|// using the features to install the other camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-hl7"
argument_list|,
literal|"camel-mina"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

