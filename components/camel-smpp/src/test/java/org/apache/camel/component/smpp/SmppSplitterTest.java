begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
package|;
end_package

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
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
DECL|class|SmppSplitterTest
specifier|public
class|class
name|SmppSplitterTest
block|{
annotation|@
name|Test
DECL|method|splitShortMessageWith160Character ()
specifier|public
name|void
name|splitShortMessageWith160Character
parameter_list|()
block|{
name|String
name|message
init|=
literal|"12345678901234567890123456789012345678901234567890123456789012345678901234567890"
operator|+
literal|"12345678901234567890123456789012345678901234567890123456789012345678901234567890"
decl_stmt|;
name|int
name|messageLength
init|=
name|SmppSplitter
operator|.
name|MAX_MSG_BYTE_LENGTH
operator|*
literal|8
operator|/
literal|7
decl_stmt|;
comment|// 160
name|int
name|segmentLength
init|=
operator|(
name|SmppSplitter
operator|.
name|MAX_MSG_BYTE_LENGTH
operator|-
name|SmppSplitter
operator|.
name|UDHIE_HEADER_REAL_LENGTH
operator|)
operator|*
literal|8
operator|/
literal|7
decl_stmt|;
comment|// 153
name|int
name|currentLength
init|=
name|message
operator|.
name|length
argument_list|()
decl_stmt|;
name|SmppSplitter
name|splitter
init|=
operator|new
name|SmppSplitter
argument_list|(
name|messageLength
argument_list|,
name|segmentLength
argument_list|,
name|currentLength
argument_list|)
decl_stmt|;
name|SmppSplitter
operator|.
name|resetCurrentReferenceNumber
argument_list|()
expr_stmt|;
name|byte
index|[]
index|[]
name|result
init|=
name|splitter
operator|.
name|split
argument_list|(
name|message
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
argument_list|,
operator|new
name|String
argument_list|(
name|result
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|splitShortMessageWith161Character ()
specifier|public
name|void
name|splitShortMessageWith161Character
parameter_list|()
block|{
name|String
name|message
init|=
literal|"12345678901234567890123456789012345678901234567890123456789012345678901234567890"
operator|+
literal|"1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"
decl_stmt|;
name|int
name|messageLength
init|=
name|SmppSplitter
operator|.
name|MAX_MSG_BYTE_LENGTH
operator|*
literal|8
operator|/
literal|7
decl_stmt|;
comment|// 160
name|int
name|segmentLength
init|=
operator|(
name|SmppSplitter
operator|.
name|MAX_MSG_BYTE_LENGTH
operator|-
name|SmppSplitter
operator|.
name|UDHIE_HEADER_REAL_LENGTH
operator|)
operator|*
literal|8
operator|/
literal|7
decl_stmt|;
comment|// 153
name|int
name|currentLength
init|=
name|message
operator|.
name|length
argument_list|()
decl_stmt|;
name|SmppSplitter
name|splitter
init|=
operator|new
name|SmppSplitter
argument_list|(
name|messageLength
argument_list|,
name|segmentLength
argument_list|,
name|currentLength
argument_list|)
decl_stmt|;
name|SmppSplitter
operator|.
name|resetCurrentReferenceNumber
argument_list|()
expr_stmt|;
name|byte
index|[]
index|[]
name|result
init|=
name|splitter
operator|.
name|split
argument_list|(
name|message
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
name|SmppSplitter
operator|.
name|UDHIE_HEADER_LENGTH
block|,
name|SmppSplitter
operator|.
name|UDHIE_IDENTIFIER_SAR
block|,
name|SmppSplitter
operator|.
name|UDHIE_SAR_LENGTH
block|,
literal|1
block|,
literal|2
block|,
literal|1
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
name|SmppSplitter
operator|.
name|UDHIE_HEADER_LENGTH
block|,
name|SmppSplitter
operator|.
name|UDHIE_IDENTIFIER_SAR
block|,
name|SmppSplitter
operator|.
name|UDHIE_SAR_LENGTH
block|,
literal|1
block|,
literal|2
block|,
literal|2
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|String
name|firstShortMessage
init|=
operator|new
name|String
argument_list|(
name|result
index|[
literal|0
index|]
argument_list|,
name|SmppSplitter
operator|.
name|UDHIE_HEADER_REAL_LENGTH
argument_list|,
name|result
index|[
literal|0
index|]
operator|.
name|length
operator|-
name|SmppSplitter
operator|.
name|UDHIE_HEADER_REAL_LENGTH
argument_list|)
decl_stmt|;
name|String
name|secondShortMessage
init|=
operator|new
name|String
argument_list|(
name|result
index|[
literal|1
index|]
argument_list|,
name|SmppSplitter
operator|.
name|UDHIE_HEADER_REAL_LENGTH
argument_list|,
name|result
index|[
literal|1
index|]
operator|.
name|length
operator|-
name|SmppSplitter
operator|.
name|UDHIE_HEADER_REAL_LENGTH
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|message
argument_list|,
name|firstShortMessage
operator|+
name|secondShortMessage
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

