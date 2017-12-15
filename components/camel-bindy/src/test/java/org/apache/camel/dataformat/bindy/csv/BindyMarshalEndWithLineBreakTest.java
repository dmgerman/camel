begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|csv
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|CsvRecord
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
name|bindy
operator|.
name|model
operator|.
name|csv
operator|.
name|MyCsvRecord
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
name|bindy
operator|.
name|model
operator|.
name|csv
operator|.
name|MyCsvRecord2
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
name|bindy
operator|.
name|util
operator|.
name|ConverterUtils
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
name|model
operator|.
name|dataformat
operator|.
name|BindyType
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
comment|/**  *  */
end_comment

begin_class
DECL|class|BindyMarshalEndWithLineBreakTest
specifier|public
class|class
name|BindyMarshalEndWithLineBreakTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testCsvWithEndingLineBreak ()
specifier|public
name|void
name|testCsvWithEndingLineBreak
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CsvRecord
name|record
init|=
name|MyCsvRecord
operator|.
name|class
operator|.
name|getAnnotation
argument_list|(
name|CsvRecord
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|MyCsvRecord
name|csvRecord
init|=
operator|new
name|MyCsvRecord
argument_list|()
decl_stmt|;
name|csvRecord
operator|.
name|setAddressLine1
argument_list|(
literal|"221b Baker Street"
argument_list|)
expr_stmt|;
name|csvRecord
operator|.
name|setCity
argument_list|(
literal|"London"
argument_list|)
expr_stmt|;
name|csvRecord
operator|.
name|setCountry
argument_list|(
literal|"England"
argument_list|)
expr_stmt|;
name|csvRecord
operator|.
name|setAttention
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|convertToString
argument_list|()
operator|.
name|endsWith
argument_list|(
name|ConverterUtils
operator|.
name|getStringCarriageReturn
argument_list|(
name|record
operator|.
name|crlf
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:withlb"
argument_list|,
name|csvRecord
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCsvWithoutEndingLineBreak ()
specifier|public
name|void
name|testCsvWithoutEndingLineBreak
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CsvRecord
name|record
init|=
name|MyCsvRecord2
operator|.
name|class
operator|.
name|getAnnotation
argument_list|(
name|CsvRecord
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|MyCsvRecord2
name|csvRecord2
init|=
operator|new
name|MyCsvRecord2
argument_list|()
decl_stmt|;
name|csvRecord2
operator|.
name|setAddressLine1
argument_list|(
literal|"221b Baker Street"
argument_list|)
expr_stmt|;
name|csvRecord2
operator|.
name|setCity
argument_list|(
literal|"London"
argument_list|)
expr_stmt|;
name|csvRecord2
operator|.
name|setCountry
argument_list|(
literal|"England"
argument_list|)
expr_stmt|;
name|csvRecord2
operator|.
name|setAttention
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|convertToString
argument_list|()
operator|.
name|endsWith
argument_list|(
name|record
operator|.
name|separator
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:withoutlb"
argument_list|,
name|csvRecord2
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
name|from
argument_list|(
literal|"direct:withoutlb"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|bindy
argument_list|(
name|BindyType
operator|.
name|Csv
argument_list|,
name|MyCsvRecord2
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:after.unmarshal"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:withlb"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|bindy
argument_list|(
name|BindyType
operator|.
name|Csv
argument_list|,
name|MyCsvRecord
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:after.marshal"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

