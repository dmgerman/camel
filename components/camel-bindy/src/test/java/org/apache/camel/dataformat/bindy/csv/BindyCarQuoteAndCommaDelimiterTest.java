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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|car
operator|.
name|Car
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|BindyCarQuoteAndCommaDelimiterTest
specifier|public
class|class
name|BindyCarQuoteAndCommaDelimiterTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testBindyQuoteAndCommaDelimiter ()
specifier|public
name|void
name|testBindyQuoteAndCommaDelimiter
parameter_list|()
throws|throws
name|Exception
block|{
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
name|String
name|header
init|=
literal|"\"stockid\";\"make\";\"model\";\"deriv\";\"series\";\"registration\";\"chassis\";\"engine\";\"year\""
operator|+
literal|";\"klms\";\"body\";\"colour\";\"enginesize\";\"trans\";\"fuel\";\"options\";\"desc\";\"status\";\"Reserve_price\";\"nvic\""
decl_stmt|;
name|String
name|row
init|=
literal|"\"SS552\";\"TOYOTA\";\"KLUGER\";\"CV 4X4\";\"MCU28R UPGRADE\";\"TBA\";\"\";\"\";\"2005\";\"155000\";\"4D WAGON\""
operator|+
literal|";\"BLACK\";\"3.3 LTR\";\"5 Sp Auto\";\"MULTI POINT FINJ\";\"POWER MIRRORS, POWER STEERING, POWER WINDOWS, CRUISE CONTROL,"
operator|+
literal|" ENGINE IMMOBILISER, BRAKE ASSIST, DUAL AIRBAG PACKAGE, ANTI-LOCK BRAKING, CENTRAL LOCKING REMOTE CONTROL, ALARM SYSTEM/REMOTE"
operator|+
literal|" ANTI THEFT, AUTOMATIC AIR CON / CLIMATE CONTROL, ELECTRONIC BRAKE FORCE DISTRIBUTION, CLOTH TRIM, LIMITED SLIP DIFFERENTIAL,"
operator|+
literal|" RADIO CD WITH 6 SPEAKERS\";\"Dual Airbag Package, Anti-lock Braking, Automatic Air Con / Climate Control, Alarm System/Remote"
operator|+
literal|" Anti Theft, Brake Assist, Cruise Control, Central Locking Remote Control, Cloth Trim, Electronic Brake Force Distribution,"
operator|+
literal|" Engine Immobiliser, Limited Slip Differential, Power Mirrors, Power Steering, Power Windows, Radio CD with 6 Speakers"
operator|+
literal|" CV GOOD KLMS AUTO POWER OPTIONS GOOD KLMS   \";\"Used\";\"\";\"EZR05I\" "
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|header
operator|+
literal|"\n"
operator|+
name|row
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map1
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|mock
operator|.
name|getReceivedExchanges
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
name|List
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Car
name|rec1
init|=
operator|(
name|Car
operator|)
name|map1
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SS552"
argument_list|,
name|rec1
operator|.
name|getStockid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TOYOTA"
argument_list|,
name|rec1
operator|.
name|getMake
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"KLUGER"
argument_list|,
name|rec1
operator|.
name|getModel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2005
argument_list|,
name|rec1
operator|.
name|getYear
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|"155000.0"
argument_list|)
argument_list|,
name|rec1
operator|.
name|getKlms
argument_list|()
argument_list|,
literal|0.0001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EZR05I"
argument_list|,
name|rec1
operator|.
name|getNvic
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Used"
argument_list|,
name|rec1
operator|.
name|getStatus
argument_list|()
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|bindy
argument_list|(
name|BindyType
operator|.
name|Csv
argument_list|,
literal|"org.apache.camel.dataformat.bindy.model.car"
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

