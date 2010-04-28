begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.produce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|produce
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
name|Produce
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
name|junit38
operator|.
name|AbstractJUnit38SpringContextTests
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|ProduceTest
specifier|public
class|class
name|ProduceTest
extends|extends
name|AbstractJUnit38SpringContextTests
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:myService"
argument_list|)
DECL|field|producer
specifier|protected
name|MyListener
name|producer
decl_stmt|;
DECL|method|testInvokeService ()
specifier|public
name|void
name|testInvokeService
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets send a message
name|String
name|actual
init|=
name|producer
operator|.
name|sayHello
argument_list|(
literal|"James"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"response"
argument_list|,
literal|"Hello James"
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

