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
DECL|class|BindySimpleCsvCAMEL11065Test
specifier|public
class|class
name|BindySimpleCsvCAMEL11065Test
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
DECL|method|testUnMarshallMessage ()
specifier|public
name|void
name|testUnMarshallMessage
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
literal|"'text1',',text2',1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"',',',text2',2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"',','text2,',3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"'',',text2,',4"
argument_list|)
expr_stmt|;
name|mockEndPoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|MyCsvRecord2
name|rc
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
name|MyCsvRecord2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"text1"
argument_list|,
name|rc
operator|.
name|getText1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|",text2"
argument_list|,
name|rc
operator|.
name|getText2
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
name|rc
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|rc
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
name|MyCsvRecord2
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|","
argument_list|,
name|rc
operator|.
name|getText1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|",text2"
argument_list|,
name|rc
operator|.
name|getText2
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
name|rc
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|rc
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
name|MyCsvRecord2
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|","
argument_list|,
name|rc
operator|.
name|getText1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text2,"
argument_list|,
name|rc
operator|.
name|getText2
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
name|rc
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|rc
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
name|MyCsvRecord2
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|rc
operator|.
name|getText1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|",text2,"
argument_list|,
name|rc
operator|.
name|getText2
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
name|rc
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
name|MyCsvRecord2
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
block|}
end_class

end_unit

