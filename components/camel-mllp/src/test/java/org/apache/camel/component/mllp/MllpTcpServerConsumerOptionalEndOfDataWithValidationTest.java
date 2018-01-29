begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|NotifyBuilder
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
name|mllp
operator|.
name|Hl7TestMessageGenerator
import|;
end_import

begin_class
DECL|class|MllpTcpServerConsumerOptionalEndOfDataWithValidationTest
specifier|public
class|class
name|MllpTcpServerConsumerOptionalEndOfDataWithValidationTest
extends|extends
name|TcpServerConsumerEndOfDataAndValidationTestSupport
block|{
annotation|@
name|Override
DECL|method|validatePayload ()
name|boolean
name|validatePayload
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|requireEndOfData ()
name|boolean
name|requireEndOfData
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|testInvalidMessage ()
specifier|public
name|void
name|testInvalidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedInvalidCount
operator|=
literal|1
expr_stmt|;
name|runInvalidMessage
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testNthInvalidMessage ()
specifier|public
name|void
name|testNthInvalidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedInvalidCount
operator|=
literal|1
expr_stmt|;
name|runNthInvalidMessage
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testMessageContainingEmbeddedStartOfBlock ()
specifier|public
name|void
name|testMessageContainingEmbeddedStartOfBlock
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedInvalidCount
operator|=
literal|1
expr_stmt|;
name|runMessageContainingEmbeddedStartOfBlock
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testNthMessageContainingEmbeddedStartOfBlock ()
specifier|public
name|void
name|testNthMessageContainingEmbeddedStartOfBlock
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedInvalidCount
operator|=
literal|1
expr_stmt|;
name|runNthMessageContainingEmbeddedStartOfBlock
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testMessageContainingEmbeddedEndOfBlock ()
specifier|public
name|void
name|testMessageContainingEmbeddedEndOfBlock
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedInvalidCount
operator|=
literal|1
expr_stmt|;
name|setExpectedCounts
argument_list|()
expr_stmt|;
name|NotifyBuilder
name|done
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|()
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|mllpClient
operator|.
name|sendFramedData
argument_list|(
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|()
operator|.
name|replaceFirst
argument_list|(
literal|"EVN"
argument_list|,
literal|"EVN"
operator|+
name|MllpProtocolConstants
operator|.
name|END_OF_BLOCK
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Exchange should have completed"
argument_list|,
name|done
operator|.
name|matches
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testInvalidMessageContainingEmbeddedEndOfBlock ()
specifier|public
name|void
name|testInvalidMessageContainingEmbeddedEndOfBlock
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedInvalidCount
operator|=
literal|1
expr_stmt|;
name|runInvalidMessageContainingEmbeddedEndOfBlock
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testNthMessageContainingEmbeddedEndOfBlock ()
specifier|public
name|void
name|testNthMessageContainingEmbeddedEndOfBlock
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedInvalidCount
operator|=
literal|1
expr_stmt|;
name|runNthMessageContainingEmbeddedEndOfBlock
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testMessageWithoutEndOfDataByte ()
specifier|public
name|void
name|testMessageWithoutEndOfDataByte
parameter_list|()
throws|throws
name|Exception
block|{
name|expectedCompleteCount
operator|=
literal|1
expr_stmt|;
name|runMessageWithoutEndOfDataByte
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

