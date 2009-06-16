begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flatpack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flatpack
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|DelimitedWithUnmarshalTest
specifier|public
class|class
name|DelimitedWithUnmarshalTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FixedLengthTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:results"
argument_list|)
DECL|field|results
specifier|protected
name|MockEndpoint
name|results
decl_stmt|;
DECL|field|expectedItemDesc
specifier|protected
name|String
index|[]
name|expectedItemDesc
init|=
block|{
literal|"SOME VALVE"
block|,
literal|"AN ENGINE"
block|,
literal|"A BELT"
block|,
literal|"A BOLT"
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|testCamel ()
specifier|public
name|void
name|testCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|results
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|results
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|results
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Map
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found body as a Map but was: "
operator|+
name|ObjectHelper
operator|.
name|className
argument_list|(
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ITEM_DESC result("
operator|+
name|counter
operator|+
literal|")"
argument_list|,
name|expectedItemDesc
index|[
name|counter
index|]
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|"ITEM_DESC"
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Result: "
operator|+
name|counter
operator|+
literal|" = "
operator|+
name|body
argument_list|)
expr_stmt|;
name|counter
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

