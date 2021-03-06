begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.braintree
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|braintree
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|com
operator|.
name|braintreegateway
operator|.
name|TransactionLevelFeeReport
import|;
end_import

begin_import
import|import
name|com
operator|.
name|braintreegateway
operator|.
name|TransactionLevelFeeReportRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|braintreegateway
operator|.
name|TransactionLevelFeeReportRow
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeApiCollection
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
name|braintree
operator|.
name|internal
operator|.
name|ReportGatewayApiMethod
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Test class for {@link com.braintreegateway.ReportGateway} APIs.  */
end_comment

begin_class
DECL|class|ReportGatewayIntegrationTest
specifier|public
class|class
name|ReportGatewayIntegrationTest
extends|extends
name|AbstractBraintreeTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ReportGatewayIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|BraintreeApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|ReportGatewayApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testTransactionLevelFees ()
specifier|public
name|void
name|testTransactionLevelFees
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|merchantAccountId
init|=
name|System
operator|.
name|getenv
argument_list|(
literal|"CAMEL_BRAINTREE_MERCHANT_ACCOUNT_ID"
argument_list|)
decl_stmt|;
name|String
name|reportDateString
init|=
name|System
operator|.
name|getenv
argument_list|(
literal|"CAMEL_BRAINTREE_REPORT_DATE"
argument_list|)
decl_stmt|;
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
decl_stmt|;
name|Calendar
name|reportDate
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|reportDate
operator|.
name|setTime
argument_list|(
name|sdf
operator|.
name|parse
argument_list|(
name|reportDateString
argument_list|)
argument_list|)
expr_stmt|;
name|TransactionLevelFeeReportRequest
name|request
init|=
operator|new
name|TransactionLevelFeeReportRequest
argument_list|()
operator|.
name|date
argument_list|(
name|reportDate
argument_list|)
operator|.
name|merchantAccountId
argument_list|(
name|merchantAccountId
argument_list|)
decl_stmt|;
specifier|final
name|com
operator|.
name|braintreegateway
operator|.
name|Result
argument_list|<
name|TransactionLevelFeeReport
argument_list|>
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://TRANSACTIONLEVELFEES"
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"transactionLevelFees result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"transactionLevelFees success"
argument_list|,
name|result
operator|.
name|isSuccess
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TransactionLevelFeeReportRow
argument_list|>
name|rows
init|=
name|result
operator|.
name|getTarget
argument_list|()
operator|.
name|getRows
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"transactionLevelFeeRows found"
argument_list|,
name|rows
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"transactionLevelFees: "
operator|+
name|result
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for transactionLevelFees
name|from
argument_list|(
literal|"direct://TRANSACTIONLEVELFEES"
argument_list|)
operator|.
name|to
argument_list|(
literal|"braintree://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/transactionLevelFees?inBody=request"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

