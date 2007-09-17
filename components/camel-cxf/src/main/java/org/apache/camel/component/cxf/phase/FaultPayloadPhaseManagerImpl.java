begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.phase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|phase
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|SortedArraySet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|phase
operator|.
name|Phase
import|;
end_import

begin_class
DECL|class|FaultPayloadPhaseManagerImpl
specifier|public
class|class
name|FaultPayloadPhaseManagerImpl
extends|extends
name|AbstractPhaseManagerImpl
block|{
DECL|method|createInPhases ()
specifier|protected
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|createInPhases
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|inPhases
init|=
operator|new
name|SortedArraySet
argument_list|<
name|Phase
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|RECEIVE
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|USER_STREAM
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|READ
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|USER_PROTOCOL
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|POST_PROTOCOL
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|inPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|UNMARSHAL
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|inPhases
return|;
block|}
DECL|method|createOutPhases ()
specifier|protected
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|createOutPhases
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|outPhases
init|=
operator|new
name|SortedArraySet
argument_list|<
name|Phase
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|USER_PROTOCOL
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|POST_PROTOCOL
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|WRITE
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|MARSHAL
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|USER_STREAM
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|SEND
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|SEND_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|POST_STREAM_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|USER_STREAM_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|POST_PROTOCOL_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|USER_PROTOCOL_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|WRITE_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|PRE_STREAM_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|outPhases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND_ENDING
argument_list|,
operator|++
name|i
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|outPhases
return|;
block|}
block|}
end_class

end_unit

