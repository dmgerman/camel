begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.testng
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|testng
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
name|Message
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|annotations
operator|.
name|BeforeTest
import|;
end_import

begin_comment
comment|/**  * A base class for a test which requires a {@link org.apache.camel.CamelContext} and  * a populated {@link Exchange}  */
end_comment

begin_class
DECL|class|ExchangeTestSupport
specifier|public
specifier|abstract
class|class
name|ExchangeTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|exchange
specifier|protected
name|Exchange
name|exchange
decl_stmt|;
comment|/**      * A factory method to create an Exchange implementation      */
DECL|method|createExchange ()
specifier|protected
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
return|;
block|}
comment|/**      * A strategy method to populate an exchange with some example values for use      * by language plugins      */
DECL|method|populateExchange (Exchange exchange)
specifier|protected
name|void
name|populateExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|BeforeTest
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|exchange
operator|=
name|createExchange
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|,
literal|"No exchange created!"
argument_list|)
expr_stmt|;
name|populateExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

