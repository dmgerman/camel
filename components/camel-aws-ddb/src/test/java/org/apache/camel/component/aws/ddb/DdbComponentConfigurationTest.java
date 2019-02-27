begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|ddb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|regions
operator|.
name|Regions
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
DECL|class|DdbComponentConfigurationTest
specifier|public
class|class
name|DdbComponentConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|createEndpointWithComponentElements ()
specifier|public
name|void
name|createEndpointWithComponentElements
parameter_list|()
throws|throws
name|Exception
block|{
name|DdbComponent
name|component
init|=
operator|new
name|DdbComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setAccessKey
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSecretKey
argument_list|(
literal|"YYY"
argument_list|)
expr_stmt|;
name|DdbEndpoint
name|endpoint
init|=
operator|(
name|DdbEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-ddb://myTable"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myTable"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTableName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXX"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"YYY"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithComponentAndEndpointElements ()
specifier|public
name|void
name|createEndpointWithComponentAndEndpointElements
parameter_list|()
throws|throws
name|Exception
block|{
name|DdbComponent
name|component
init|=
operator|new
name|DdbComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setAccessKey
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSecretKey
argument_list|(
literal|"YYY"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setRegion
argument_list|(
name|Regions
operator|.
name|US_WEST_1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|DdbEndpoint
name|endpoint
init|=
operator|(
name|DdbEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-ddb://myTable?accessKey=xxxxxx&secretKey=yyyyy&region=US_EAST_1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myTable"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTableName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxxxxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyyyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"US_EAST_1"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

