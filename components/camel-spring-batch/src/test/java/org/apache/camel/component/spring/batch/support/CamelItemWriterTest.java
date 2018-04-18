begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.batch.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|batch
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
DECL|class|CamelItemWriterTest
specifier|public
class|class
name|CamelItemWriterTest
extends|extends
name|CamelTestSupport
block|{
comment|// Fixtures
DECL|field|camelItemWriter
name|CamelItemWriter
argument_list|<
name|String
argument_list|>
name|camelItemWriter
decl_stmt|;
DECL|field|message
name|String
name|message
init|=
literal|"message"
decl_stmt|;
comment|// Camel fixtures
annotation|@
name|Override
DECL|method|doPostSetup ()
specifier|protected
name|void
name|doPostSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|camelItemWriter
operator|=
operator|new
name|CamelItemWriter
argument_list|<>
argument_list|(
name|template
argument_list|()
argument_list|,
literal|"seda:queue"
argument_list|)
expr_stmt|;
block|}
comment|// Tests
annotation|@
name|Test
DECL|method|shouldReadMessage ()
specifier|public
name|void
name|shouldReadMessage
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|camelItemWriter
operator|.
name|write
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|message
argument_list|,
name|consumer
argument_list|()
operator|.
name|receiveBody
argument_list|(
literal|"seda:queue"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

