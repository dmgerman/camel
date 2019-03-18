begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|decorators
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

begin_class
DECL|class|RabbitmqSpanDecoratorTest
specifier|public
class|class
name|RabbitmqSpanDecoratorTest
block|{
annotation|@
name|Test
DECL|method|testGetDestinationHeaderTopic ()
specifier|public
name|void
name|testGetDestinationHeaderTopic
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|RabbitmqSpanDecorator
operator|.
name|EXCHANGE_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|RabbitmqSpanDecorator
name|decorator
init|=
operator|new
name|RabbitmqSpanDecorator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|decorator
operator|.
name|getDestination
argument_list|(
name|exchange
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

