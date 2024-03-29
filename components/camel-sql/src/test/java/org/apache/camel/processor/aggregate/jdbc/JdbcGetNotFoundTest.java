begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|jdbc
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
name|support
operator|.
name|DefaultExchange
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
DECL|class|JdbcGetNotFoundTest
specifier|public
class|class
name|JdbcGetNotFoundTest
extends|extends
name|AbstractJdbcAggregationTestSupport
block|{
annotation|@
name|Test
DECL|method|testGetNotFound ()
specifier|public
name|void
name|testGetNotFound
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|repo
operator|.
name|get
argument_list|(
name|context
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should not find exchange"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPutAndGetNotFound ()
specifier|public
name|void
name|testPutAndGetNotFound
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Created "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|context
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|repo
operator|.
name|get
argument_list|(
name|context
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should find exchange"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|Exchange
name|exchange2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Created "
operator|+
name|exchange2
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|out2
init|=
name|repo
operator|.
name|get
argument_list|(
name|context
argument_list|,
name|exchange2
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should not find exchange"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

