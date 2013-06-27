begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.annotation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Documented
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_comment
comment|/**  * This annotation represents the root class of the model. When a   * fixed-length record must be described in the model we will use this  * annotation to split the data during the unmarshal process.  */
end_comment

begin_annotation_defn
annotation|@
name|Documented
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
DECL|annotation|FixedLengthRecord
specifier|public
annotation_defn|@interface
name|FixedLengthRecord
block|{
comment|/**      * Name describing the record (optional)      *       * @return String      */
DECL|method|name ()
name|String
name|name
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Character to be used to add a carriage return after each record      * (optional) Three values can be used : WINDOWS, UNIX or MAC      *       * @return String      */
DECL|method|crlf ()
name|String
name|crlf
parameter_list|()
default|default
literal|"WINDOWS"
function_decl|;
comment|/**      * The char to pad with.      * @return the char to pad with if the record is set to a fixed length;      */
DECL|method|paddingChar ()
name|char
name|paddingChar
parameter_list|()
default|default
literal|' '
function_decl|;
comment|/**      * The fixed length of the record. It means that the record will always be that long padded with {#paddingChar()}'s      * @return the length of the record.      */
DECL|method|length ()
name|int
name|length
parameter_list|()
default|default
literal|0
function_decl|;
comment|/**      * Indicates that the record(s) of this type may be preceded by a single header record at the beginning of in the file      */
DECL|method|hasHeader ()
DECL|field|false
name|boolean
name|hasHeader
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Indicates that the record(s) of this type may be followed by a single footer record at the end of the file      */
DECL|method|hasFooter ()
DECL|field|false
name|boolean
name|hasFooter
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Configures the data format to skip marshalling / unmarshalling of the header record      */
DECL|method|skipHeader ()
DECL|field|false
name|boolean
name|skipHeader
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Configures the data format to skip marshalling / unmarshalling of the footer record      */
DECL|method|skipFooter ()
DECL|field|false
name|boolean
name|skipFooter
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Identifies this FixedLengthRecord as a header record, which may precede all other records in the file      */
DECL|method|isHeader ()
DECL|field|false
name|boolean
name|isHeader
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Identifies this FixedLengthRecord as a footer record, which may be used as the last record in the file      */
DECL|method|isFooter ()
DECL|field|false
name|boolean
name|isFooter
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Indicates whether trailing characters beyond the last mapped field may be ignored      */
DECL|method|ignoreTrailingChars ()
DECL|field|false
name|boolean
name|ignoreTrailingChars
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

