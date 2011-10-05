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

begin_class
DECL|class|SmppUcs2Splitter
specifier|public
class|class
name|SmppUcs2Splitter
extends|extends
name|SmppSplitter
block|{
comment|/**      * The maximum length in chars of the unicode messages.      *<p/>      * Each letter requires 2 bytes.      */
DECL|field|MAX_MSG_CHAR_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|MAX_MSG_CHAR_SIZE
init|=
name|MAX_MSG_BYTE_LENGTH
operator|/
literal|2
decl_stmt|;
comment|// ( / 2 * 2) is required because UDHIE_HEADER_REAL_LENGTH might be equal to 0x07 so the length of the segment
comment|// is 133 = (70 * 2 - 7)and the last letter in the unicode will be damaged.
DECL|field|MAX_SEG_BYTE_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|MAX_SEG_BYTE_SIZE
init|=
operator|(
name|MAX_MSG_CHAR_SIZE
operator|*
literal|2
operator|-
name|UDHIE_HEADER_REAL_LENGTH
operator|)
operator|/
literal|2
operator|*
literal|2
decl_stmt|;
DECL|method|SmppUcs2Splitter (int segmentLength)
specifier|public
name|SmppUcs2Splitter
parameter_list|(
name|int
name|segmentLength
parameter_list|)
block|{
name|super
argument_list|(
name|MAX_MSG_CHAR_SIZE
argument_list|,
name|MAX_SEG_BYTE_SIZE
argument_list|,
name|segmentLength
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

