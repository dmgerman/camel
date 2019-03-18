begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * This annotation represents the root class of the model. When a message (FIX  * message containing key-value pairs) must be described in the model, we will  * use this annotation. The key pair separator (mandatory) defines the separator  * between the key and the value The pair separator (mandatory) allows to define  * which character separate the pairs from each other The name is optional and  * could be used in the future to bind a property which a different name The  * type (optional) allow to define the type of the message (e.g. FIX, EMX, ...)  * The version (optional) defines the version of the message (e.g. 4.1, ...) The  * crlf (optional) is used to add a new line after a record. By default, the  * value is WINDOWS The isOrdered (optional) boolean is used to ordered the  * message generated in output (line feed and carriage return on windows  */
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
DECL|annotation|Message
specifier|public
annotation_defn|@interface
name|Message
block|{
comment|/**      * Name describing the message (optional)      *       * @return String      */
DECL|method|name ()
name|String
name|name
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Pair separator used to split the key value pairs in tokens (mandatory)      *       * @return String      */
DECL|method|pairSeparator ()
name|String
name|pairSeparator
parameter_list|()
function_decl|;
comment|/**      * Key value pair separator is used to split the values from their keys      * (mandatory)      *       * @return String      */
DECL|method|keyValuePairSeparator ()
name|String
name|keyValuePairSeparator
parameter_list|()
function_decl|;
comment|/**      * type is used to define the type of the message (e.g. FIX, EMX, ...)      * (optional)      */
DECL|method|type ()
name|String
name|type
parameter_list|()
default|default
literal|"FIX"
function_decl|;
comment|/**      * version defines the version of the message (e.g. 4.1, ...) (optional)      */
DECL|method|version ()
name|String
name|version
parameter_list|()
default|default
literal|"4.1"
function_decl|;
comment|/**      * Character to be used to add a carriage return after each record      * (optional) Three values can be used : WINDOWS, UNIX or MAC      *       * @return String      */
DECL|method|crlf ()
name|String
name|crlf
parameter_list|()
default|default
literal|"WINDOWS"
function_decl|;
comment|/**      * Indicates if the message must be ordered in output      *       * @return boolean      */
DECL|method|isOrdered ()
DECL|field|false
name|boolean
name|isOrdered
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

