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
comment|/**  * This annotation represents the root class of the model. When a CSV,  * fixed-length record must be described in the model we will use this  * annotation and the separator (for csv record) to know how to split the data  * during the unmarshal process The separator (mandatory) The name is optional  * and could be used in the future to bind a property which a different name The  * skipfirstline (optional) allows to skip the first line of the file/content  * received The generateHeaderColumnNames (optional) allow to add in the CSV  * generated the header containing names of the columns The crlf (optional) is  * used to add a new line after a record. By default, the value is WINDOWS The  * isOrdered (optional) boolean is used to ordered the message generated in  * output  */
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
DECL|annotation|CsvRecord
specifier|public
annotation_defn|@interface
name|CsvRecord
block|{
comment|/**      * Name describing the record (optional)      */
DECL|method|name ()
name|String
name|name
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Separator used to split a record in tokens (mandatory)      */
DECL|method|separator ()
name|String
name|separator
parameter_list|()
function_decl|;
comment|/**      * The skipFirstLine parameter will allow to skip or not the first line of a      * CSV file. This line often contains columns definition      */
DECL|method|skipFirstLine ()
DECL|field|false
name|boolean
name|skipFirstLine
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Character to be used to add a carriage return after each record      * (optional) Three values can be used : WINDOWS, UNIX or MAC.      */
DECL|method|crlf ()
name|String
name|crlf
parameter_list|()
default|default
literal|"WINDOWS"
function_decl|;
comment|/**      * The generateHeaderColumns parameter allow to add in the CSV generated the      * header containing names of the columns      */
DECL|method|generateHeaderColumns ()
DECL|field|false
name|boolean
name|generateHeaderColumns
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Indicates if the message must be ordered in output      */
DECL|method|isOrdered ()
DECL|field|false
name|boolean
name|isOrdered
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Whether to marshal columns with the given quote character (optional)      */
DECL|method|quote ()
name|String
name|quote
parameter_list|()
default|default
literal|"\""
function_decl|;
comment|/**      * Indicate if the values must be quoted when marshaling (optional)      */
DECL|method|quoting ()
DECL|field|false
name|boolean
name|quoting
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

