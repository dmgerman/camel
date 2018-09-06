begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|ExchangePatternTest
specifier|public
class|class
name|ExchangePatternTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testExchangePattern ()
specifier|public
name|void
name|testExchangePattern
parameter_list|()
throws|throws
name|Exception
block|{
name|ExchangePattern
name|mep
init|=
name|ExchangePattern
operator|.
name|InOut
decl_stmt|;
name|assertEquals
argument_list|(
literal|"WSDL Uri"
argument_list|,
literal|"http://www.w3.org/ns/wsdl/in-out"
argument_list|,
name|mep
operator|.
name|getWsdlUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStringToMEP ()
specifier|public
name|void
name|testStringToMEP
parameter_list|()
throws|throws
name|Exception
block|{
name|ExchangePattern
name|mep
init|=
name|ExchangePattern
operator|.
name|fromWsdlUri
argument_list|(
literal|"http://www.w3.org/ns/wsdl/in-only"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MEP"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|mep
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

