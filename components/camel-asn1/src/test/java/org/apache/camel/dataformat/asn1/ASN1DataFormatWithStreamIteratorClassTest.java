begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.asn1
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|asn1
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|dataformat
operator|.
name|asn1
operator|.
name|model
operator|.
name|testsmscbercdr
operator|.
name|SmsCdr
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

begin_class
DECL|class|ASN1DataFormatWithStreamIteratorClassTest
specifier|public
class|class
name|ASN1DataFormatWithStreamIteratorClassTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|asn1
specifier|private
name|ASN1DataFormat
name|asn1
decl_stmt|;
DECL|field|fileName
specifier|private
name|String
name|fileName
init|=
literal|"src/test/resources/asn1_data/SMS_SINGLE.tt"
decl_stmt|;
DECL|method|baseUnmarshalReturnClassObjectTest (String mockEnpointName, String directEndpointName)
specifier|private
name|void
name|baseUnmarshalReturnClassObjectTest
parameter_list|(
name|String
name|mockEnpointName
parameter_list|,
name|String
name|directEndpointName
parameter_list|)
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
name|mockEnpointName
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
name|ASN1DataFormatTestHelper
operator|.
name|reteriveByteArrayInputStream
argument_list|(
name|testFile
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|directEndpointName
argument_list|,
name|bais
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|getMockEndpoint
argument_list|(
name|mockEnpointName
argument_list|)
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exchanges
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchanges
control|)
block|{
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|SmsCdr
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalReturnClassObject ()
specifier|public
name|void
name|testUnmarshalReturnClassObject
parameter_list|()
throws|throws
name|Exception
block|{
name|baseUnmarshalReturnClassObjectTest
argument_list|(
literal|"mock:unmarshal"
argument_list|,
literal|"direct:unmarshal"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalReturnClassObjectDsl ()
specifier|public
name|void
name|testUnmarshalReturnClassObjectDsl
parameter_list|()
throws|throws
name|Exception
block|{
name|baseUnmarshalReturnClassObjectTest
argument_list|(
literal|"mock:unmarshaldsl"
argument_list|,
literal|"direct:unmarshaldsl"
argument_list|)
expr_stmt|;
block|}
DECL|method|baseUnmarshalMarshalReturnOutputStreamTest (String mockEnpointName, String directEndpointName)
specifier|private
name|void
name|baseUnmarshalMarshalReturnOutputStreamTest
parameter_list|(
name|String
name|mockEnpointName
parameter_list|,
name|String
name|directEndpointName
parameter_list|)
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
name|mockEnpointName
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
name|ASN1DataFormatTestHelper
operator|.
name|reteriveByteArrayInputStream
argument_list|(
name|testFile
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|directEndpointName
argument_list|,
name|bais
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|getMockEndpoint
argument_list|(
name|mockEnpointName
argument_list|)
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exchanges
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchanges
control|)
block|{
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|byte
index|[]
argument_list|)
expr_stmt|;
comment|// assertTrue(Arrays.equals(FileUtils.readFileToByteArray(testFile),
comment|// exchange.getIn().getBody(byte[].class)));
comment|// FileOutputStream fos = new
comment|// FileOutputStream("src/test/resources/after_unmarshal_marshal_SMS_SINGLE.tt");
comment|// fos.write(ObjectHelper.cast(byte[].class,
comment|// exchange.getIn().getBody()));
comment|// fos.close();
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalMarshalReturnOutputStream ()
specifier|public
name|void
name|testUnmarshalMarshalReturnOutputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|baseUnmarshalMarshalReturnOutputStreamTest
argument_list|(
literal|"mock:marshal"
argument_list|,
literal|"direct:unmarshalthenmarshal"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalMarshalReturnOutputStreamDsl ()
specifier|public
name|void
name|testUnmarshalMarshalReturnOutputStreamDsl
parameter_list|()
throws|throws
name|Exception
block|{
name|baseUnmarshalMarshalReturnOutputStreamTest
argument_list|(
literal|"mock:marshaldsl"
argument_list|,
literal|"direct:unmarshalthenmarshaldsl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testUnmarshalReturnClassObjectAfterUnmarshalMarshalReturnOutputStream ()
specifier|public
name|void
name|testUnmarshalReturnClassObjectAfterUnmarshalMarshalReturnOutputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/after_unmarshal_marshal_SMS_SINGLE.tt"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
name|ASN1DataFormatTestHelper
operator|.
name|reteriveByteArrayInputStream
argument_list|(
name|testFile
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|bais
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|)
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exchanges
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchanges
control|)
block|{
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|SmsCdr
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|baseDoubleUnmarshalTest (String firstMockEnpointName, String secondMockEnpointName, String directEndpointName)
specifier|private
name|void
name|baseDoubleUnmarshalTest
parameter_list|(
name|String
name|firstMockEnpointName
parameter_list|,
name|String
name|secondMockEnpointName
parameter_list|,
name|String
name|directEndpointName
parameter_list|)
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
name|firstMockEnpointName
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
name|secondMockEnpointName
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
name|ASN1DataFormatTestHelper
operator|.
name|reteriveByteArrayInputStream
argument_list|(
name|testFile
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|directEndpointName
argument_list|,
name|bais
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangesFirst
init|=
name|getMockEndpoint
argument_list|(
name|firstMockEnpointName
argument_list|)
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exchangesFirst
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|SmsCdr
name|firstUnmarshalledCdr
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchangesFirst
control|)
block|{
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|SmsCdr
argument_list|)
expr_stmt|;
name|firstUnmarshalledCdr
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SmsCdr
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangesSecond
init|=
name|getMockEndpoint
argument_list|(
name|secondMockEnpointName
argument_list|)
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exchangesSecond
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|SmsCdr
name|secondUnmarshalledCdr
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchangesSecond
control|)
block|{
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|SmsCdr
argument_list|)
expr_stmt|;
name|secondUnmarshalledCdr
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SmsCdr
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|firstUnmarshalledCdr
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|secondUnmarshalledCdr
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoubleUnmarshal ()
specifier|public
name|void
name|testDoubleUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|baseDoubleUnmarshalTest
argument_list|(
literal|"mock:firstunmarshal"
argument_list|,
literal|"mock:secondunmarshal"
argument_list|,
literal|"direct:doubleunmarshal"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoubleUnmarshalDsl ()
specifier|public
name|void
name|testDoubleUnmarshalDsl
parameter_list|()
throws|throws
name|Exception
block|{
name|baseDoubleUnmarshalTest
argument_list|(
literal|"mock:firstunmarshaldsldsl"
argument_list|,
literal|"mock:secondunmarshaldsl"
argument_list|,
literal|"direct:doubleunmarshaldsl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|asn1
operator|=
operator|new
name|ASN1DataFormat
argument_list|(
name|SmsCdr
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|asn1
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalthenmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|asn1
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|marshal
argument_list|(
name|asn1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:marshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:doubleunmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|asn1
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|wireTap
argument_list|(
literal|"direct:secondunmarshal"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:firstunmarshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:secondunmarshal"
argument_list|)
operator|.
name|marshal
argument_list|(
name|asn1
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|asn1
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:secondunmarshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshaldsl"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|asn1
argument_list|(
literal|"org.apache.camel.dataformat.asn1.model.testsmscbercdr.SmsCdr"
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:unmarshaldsl"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalthenmarshaldsl"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|asn1
argument_list|(
literal|"org.apache.camel.dataformat.asn1.model.testsmscbercdr.SmsCdr"
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|marshal
argument_list|()
operator|.
name|asn1
argument_list|(
literal|"org.apache.camel.dataformat.asn1.model.testsmscbercdr.SmsCdr"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:marshaldsl"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:doubleunmarshaldsl"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|asn1
argument_list|(
literal|"org.apache.camel.dataformat.asn1.model.testsmscbercdr.SmsCdr"
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|wireTap
argument_list|(
literal|"direct:secondunmarshaldsl"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:firstunmarshaldsldsl"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:secondunmarshaldsl"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|asn1
argument_list|(
literal|"org.apache.camel.dataformat.asn1.model.testsmscbercdr.SmsCdr"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|asn1
argument_list|(
literal|"org.apache.camel.dataformat.asn1.model.testsmscbercdr.SmsCdr"
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:secondunmarshaldsl"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

