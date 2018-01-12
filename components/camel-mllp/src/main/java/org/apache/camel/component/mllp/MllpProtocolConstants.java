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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/**  * Constants for the MLLP Protocol and the Camel MLLP component.  */
end_comment

begin_class
DECL|class|MllpProtocolConstants
specifier|public
specifier|final
class|class
name|MllpProtocolConstants
block|{
DECL|field|START_OF_BLOCK
specifier|public
specifier|static
specifier|final
name|char
name|START_OF_BLOCK
init|=
literal|0x0b
decl_stmt|;
comment|// VT (vertical tab)        - decimal 11, octal 013
DECL|field|END_OF_BLOCK
specifier|public
specifier|static
specifier|final
name|char
name|END_OF_BLOCK
init|=
literal|0x1c
decl_stmt|;
comment|// FS (file separator)      - decimal 28, octal 034
DECL|field|END_OF_DATA
specifier|public
specifier|static
specifier|final
name|char
name|END_OF_DATA
init|=
literal|0x0d
decl_stmt|;
comment|// CR (carriage return)     - decimal 13, octal 015
DECL|field|END_OF_STREAM
specifier|public
specifier|static
specifier|final
name|int
name|END_OF_STREAM
init|=
operator|-
literal|1
decl_stmt|;
comment|//
DECL|field|SEGMENT_DELIMITER
specifier|public
specifier|static
specifier|final
name|char
name|SEGMENT_DELIMITER
init|=
literal|0x0d
decl_stmt|;
comment|// CR (carriage return)     - decimal 13, octal 015
DECL|field|MESSAGE_TERMINATOR
specifier|public
specifier|static
specifier|final
name|char
name|MESSAGE_TERMINATOR
init|=
literal|0x0a
decl_stmt|;
comment|// LF (line feed, new line) - decimal 10, octal 012
DECL|field|PAYLOAD_TERMINATOR
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|PAYLOAD_TERMINATOR
decl_stmt|;
DECL|field|DEFAULT_CHARSET
specifier|public
specifier|static
specifier|final
name|Charset
name|DEFAULT_CHARSET
init|=
name|StandardCharsets
operator|.
name|US_ASCII
decl_stmt|;
DECL|field|MSH18_VALUES
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Charset
argument_list|>
name|MSH18_VALUES
decl_stmt|;
static|static
block|{
name|PAYLOAD_TERMINATOR
operator|=
operator|new
name|byte
index|[
literal|2
index|]
expr_stmt|;
name|PAYLOAD_TERMINATOR
index|[
literal|0
index|]
operator|=
name|MllpProtocolConstants
operator|.
name|END_OF_BLOCK
expr_stmt|;
name|PAYLOAD_TERMINATOR
index|[
literal|1
index|]
operator|=
name|MllpProtocolConstants
operator|.
name|END_OF_DATA
expr_stmt|;
name|MSH18_VALUES
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|15
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"ASCII"
argument_list|,
name|StandardCharsets
operator|.
name|US_ASCII
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/1"
argument_list|,
name|StandardCharsets
operator|.
name|ISO_8859_1
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/2"
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-2"
argument_list|)
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/3"
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-3"
argument_list|)
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/4"
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-4"
argument_list|)
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/5"
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-5"
argument_list|)
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/6"
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-6"
argument_list|)
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/7"
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-7"
argument_list|)
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/8"
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-8"
argument_list|)
argument_list|)
expr_stmt|;
name|MSH18_VALUES
operator|.
name|put
argument_list|(
literal|"8859/9"
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-9"
argument_list|)
argument_list|)
expr_stmt|;
comment|/*       // These are defined in the HL7 Spec, but I don't know how to map them to Java charset names        MSH18_VALUES.put("JAS2020", "????");       MSH18_VALUES.put("JIS X 0202", "????");       MSH18_VALUES.put("JIS X 0201-1976", "????");       MSH18_VALUES.put("JIS X 0208-1990", "????");       MSH18_VALUES.put("JIS X 0212-1990", "????");     */
block|}
DECL|method|MllpProtocolConstants ()
specifier|private
name|MllpProtocolConstants
parameter_list|()
block|{
comment|//utility class, never constructed
block|}
block|}
end_class

end_unit

