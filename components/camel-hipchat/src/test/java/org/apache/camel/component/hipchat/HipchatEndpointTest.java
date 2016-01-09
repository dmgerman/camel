begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hipchat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hipchat
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
name|CamelContext
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
name|Processor
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_class
DECL|class|HipchatEndpointTest
specifier|public
class|class
name|HipchatEndpointTest
block|{
annotation|@
name|Test
DECL|method|testCreateConsumer ()
specifier|public
name|void
name|testCreateConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|HipchatComponent
name|component
init|=
operator|new
name|HipchatComponent
argument_list|(
name|Mockito
operator|.
name|mock
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|HipchatEndpoint
name|endpoint
init|=
operator|new
name|HipchatEndpoint
argument_list|(
literal|"hipchat:http://api.hipchat.com?authKey=token"
argument_list|,
name|component
argument_list|)
decl_stmt|;
name|HipchatConsumer
name|consumer
init|=
operator|(
name|HipchatConsumer
operator|)
name|endpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{              }
block|}
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|consumer
operator|.
name|getDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

