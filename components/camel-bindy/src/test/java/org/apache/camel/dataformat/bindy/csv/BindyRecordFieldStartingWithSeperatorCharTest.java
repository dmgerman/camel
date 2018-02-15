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
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|EndpointInject
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
name|RoutesBuilder
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
name|annotation
operator|.
name|DataField
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

begin_class
DECL|class|BindyRecordFieldStartingWithSeperatorCharTest
specifier|public
class|class
name|BindyRecordFieldStartingWithSeperatorCharTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mockEndPoint
specifier|private
name|MockEndpoint
name|mockEndPoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testUnmarshallCsvRecordFieldStartingWithSeparatorChar ()
specifier|public
name|void
name|testUnmarshallCsvRecordFieldStartingWithSeparatorChar
parameter_list|()
throws|throws
name|Exception
block|{
name|mockEndPoint
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"'val1',',val2',1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"',',',val2',2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"',','val2,',3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"'',',val2,',4"
argument_list|)
expr_stmt|;
name|mockEndPoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|BindyCsvRowFormat
name|row
init|=
name|mockEndPoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|BindyCsvRowFormat
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"val1"
argument_list|,
name|row
operator|.
name|getFirstField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|",val2"
argument_list|,
name|row
operator|.
name|getSecondField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|row
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|=
name|mockEndPoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|BindyCsvRowFormat
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|","
argument_list|,
name|row
operator|.
name|getFirstField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|",val2"
argument_list|,
name|row
operator|.
name|getSecondField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|row
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|=
name|mockEndPoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|BindyCsvRowFormat
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|","
argument_list|,
name|row
operator|.
name|getFirstField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"val2,"
argument_list|,
name|row
operator|.
name|getSecondField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|3
argument_list|)
argument_list|,
name|row
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|=
name|mockEndPoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|BindyCsvRowFormat
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|row
operator|.
name|getFirstField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|",val2,"
argument_list|,
name|row
operator|.
name|getSecondField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|4
argument_list|)
argument_list|,
name|row
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|BindyCsvDataFormat
name|camelDataFormat
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
name|BindyCsvRowFormat
operator|.
name|class
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|camelDataFormat
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
comment|//from https://issues.apache.org/jira/browse/CAMEL-11065
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|","
argument_list|,
name|quote
operator|=
literal|"'"
argument_list|)
DECL|class|BindyCsvRowFormat
specifier|public
specifier|static
class|class
name|BindyCsvRowFormat
implements|implements
name|Serializable
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|)
DECL|field|firstField
specifier|private
name|String
name|firstField
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|)
DECL|field|secondField
specifier|private
name|String
name|secondField
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|3
argument_list|,
name|pattern
operator|=
literal|"########.##"
argument_list|)
DECL|field|number
specifier|private
name|BigDecimal
name|number
decl_stmt|;
DECL|method|getFirstField ()
specifier|public
name|String
name|getFirstField
parameter_list|()
block|{
return|return
name|firstField
return|;
block|}
DECL|method|setFirstField (String firstField)
specifier|public
name|void
name|setFirstField
parameter_list|(
name|String
name|firstField
parameter_list|)
block|{
name|this
operator|.
name|firstField
operator|=
name|firstField
expr_stmt|;
block|}
DECL|method|getSecondField ()
specifier|public
name|String
name|getSecondField
parameter_list|()
block|{
return|return
name|secondField
return|;
block|}
DECL|method|setSecondField (String secondField)
specifier|public
name|void
name|setSecondField
parameter_list|(
name|String
name|secondField
parameter_list|)
block|{
name|this
operator|.
name|secondField
operator|=
name|secondField
expr_stmt|;
block|}
DECL|method|getNumber ()
specifier|public
name|BigDecimal
name|getNumber
parameter_list|()
block|{
return|return
name|number
return|;
block|}
DECL|method|setNumber (BigDecimal number)
specifier|public
name|void
name|setNumber
parameter_list|(
name|BigDecimal
name|number
parameter_list|)
block|{
name|this
operator|.
name|number
operator|=
name|number
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

