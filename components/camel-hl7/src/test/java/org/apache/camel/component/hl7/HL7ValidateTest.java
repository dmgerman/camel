begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|HL7Exception
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
name|CamelExecutionException
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HL7ValidateTest
specifier|public
class|class
name|HL7ValidateTest
extends|extends
name|CamelTestSupport
block|{
comment|// TODO: Need HL7 that can fail HL7 validator
comment|//    @Test
comment|//    public void testUnmarshalFailed() throws Exception {
comment|//        MockEndpoint mock = getMockEndpoint("mock:unmarshal");
comment|//        mock.expectedMessageCount(0);
comment|//
comment|//        String body = createHL7AsString();
comment|//        try {
comment|//            template.sendBody("direct:unmarshalFailed", body);
comment|//            fail("Should have thrown exception");
comment|//        } catch (CamelExecutionException e) {
comment|//            assertIsInstanceOf(HL7Exception.class, e.getCause());
comment|//        }
comment|//
comment|//        assertMockEndpointsSatisfied();
comment|//    }
annotation|@
name|Test
DECL|method|testUnmarshalOk ()
specifier|public
name|void
name|testUnmarshalOk
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|createHL7AsString
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshalOk"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:unmarshalFailed"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|hl7
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalOk"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|hl7
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createHL7AsString ()
specifier|private
specifier|static
name|String
name|createHL7AsString
parameter_list|()
block|{
name|String
name|line1
init|=
literal|"MSH|^~\\&|MYSENDER|MYSENDERAPP|MYCLIENT|MYCLIENTAPP|200612211200||QRY^A19|1234|P|2.4"
decl_stmt|;
name|String
name|line2
init|=
literal|"QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||"
decl_stmt|;
name|StringBuffer
name|body
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line1
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line2
argument_list|)
expr_stmt|;
return|return
name|body
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

