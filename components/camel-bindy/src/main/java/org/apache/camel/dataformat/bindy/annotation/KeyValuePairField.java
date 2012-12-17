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
comment|/**  * An annotation used to identify in a POJO which property is link to a key  * value pair field The tag (mandatory) identifies the key of the key value pair  * (e.g. 8 equals the begin string in FIX The name (optional) could be used in  * the future to bind a property which a different name The pattern (optional)  * allows to define the pattern of the data (useful for Date, BigDecimal ...)  * The precision (optional) reflects the precision to be used with BigDecimal  * number The required (optional) field allows to define if the field is  * required or not. This property is not yet used but will be useful in the  * future with the validation The position (optional) field is used to order the  * tags during the creation of the message  */
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
DECL|annotation|KeyValuePairField
specifier|public
annotation_defn|@interface
name|KeyValuePairField
block|{
comment|/**      * tag identifying the field in the message (mandatory)      *       * @return int      */
DECL|method|tag ()
name|int
name|tag
parameter_list|()
function_decl|;
comment|/**      * name of the field (optional)      *       * @return String      */
DECL|method|name ()
name|String
name|name
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * pattern that the formater will use to transform the data (optional)      *       * @return String      */
DECL|method|pattern ()
name|String
name|pattern
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Position of the field in the message generated      *       * @return int      */
DECL|method|position ()
name|int
name|position
parameter_list|()
default|default
literal|0
function_decl|;
comment|/**      * precision of the BigDecimal number to be created      *       * @return int      */
DECL|method|precision ()
name|int
name|precision
parameter_list|()
default|default
literal|0
function_decl|;
comment|/**      * Indicates if the field is mandatory      */
DECL|method|required ()
DECL|field|false
name|boolean
name|required
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Indicates if there is a decimal point implied at a specified location      */
DECL|method|impliedDecimalSeparator ()
DECL|field|false
name|boolean
name|impliedDecimalSeparator
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

