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
comment|/**  * An annotation used to identify in a POJO which property is link to a field of  * a record (csv, ...). The pos (mandatory) identifies the position of the data  * in the record The name is optional and could be used in the future to bind a  * property which a different name The columnName (optional) represents the name  * of the column who will appear in the header The pattern (optional) allows to  * define the pattern of the data (useful for Date, ...) The length (optional)  * allows to define for fixed length message the size of the data's block The  * precision(optional) reflects the precision to be used with BigDecimal number  * The position (optional) identify the position of the field in the CSV  * generated The required (optional) property identifies a field which is  * mandatory. The lengthPos (optional) identifies a field in this record that   * defines the fixed length for this field.  The delimiter (optional) defines a  * character that is used to demarcate the field, if it has a variable length.  */
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
DECL|annotation|DataField
specifier|public
annotation_defn|@interface
name|DataField
block|{
comment|/**      * Position of the data in the input record, must start from 1 (mandatory).      */
DECL|method|pos ()
name|int
name|pos
parameter_list|()
function_decl|;
comment|/**      * Name of the field (optional)      */
DECL|method|name ()
name|String
name|name
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Name of the header column (optional)      */
DECL|method|columnName ()
name|String
name|columnName
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Pattern that the formatter will use to transform the data (optional)      */
DECL|method|pattern ()
name|String
name|pattern
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * @return String timezone ID      */
DECL|method|timezone ()
name|String
name|timezone
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Length of the data block if the record is set to a fixed length      */
DECL|method|length ()
name|int
name|length
parameter_list|()
default|default
literal|0
function_decl|;
comment|/**      * Identifies a data field in the record that defines the expected fixed length for this field      */
DECL|method|lengthPos ()
name|int
name|lengthPos
parameter_list|()
default|default
literal|0
function_decl|;
comment|/**      * Align the text to the right or left. Use values<tt>R</tt> or<tt>L</tt>.      */
DECL|method|align ()
name|String
name|align
parameter_list|()
default|default
literal|"R"
function_decl|;
comment|/**      * The char to pad with if the record is set to a fixed length      */
DECL|method|paddingChar ()
name|char
name|paddingChar
parameter_list|()
default|default
literal|' '
function_decl|;
comment|/**      * precision of the {@link java.math.BigDecimal} number to be created      */
DECL|method|precision ()
name|int
name|precision
parameter_list|()
default|default
literal|0
function_decl|;
comment|/**      * Position of the field in the output message generated (should start from 1)      *      * @see #pos()      */
DECL|method|position ()
name|int
name|position
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
comment|/**      * Indicates if the value should be trimmed      */
DECL|method|trim ()
DECL|field|false
name|boolean
name|trim
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Indicates to clip data in the field if it exceeds the allowed length when using fixed length.      */
DECL|method|clip ()
DECL|field|false
name|boolean
name|clip
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Optional delimiter to be used if the field has a variable length      */
DECL|method|delimiter ()
name|String
name|delimiter
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Field's default value in case no value is set       */
DECL|method|defaultValue ()
name|String
name|defaultValue
parameter_list|()
default|default
literal|""
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
comment|/**      * Decimal Separator to be used with BigDecimal number      */
DECL|method|decimalSeparator ()
name|String
name|decimalSeparator
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Grouping Separator to be used with BigDecimal number      * when we would like to format/parse to number with grouping      * e.g. 123,456.789      */
DECL|method|groupingSeparator ()
name|String
name|groupingSeparator
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Round mode to be used to round/scale a BigDecimal      * Values : UP, DOWN, CEILING, FLOOR, HALF_UP, HALF_DOWN,HALF_EVEN, UNNECESSARY      * e.g : Number = 123456.789, Precision = 2, Rounding =  CEILING      * Result : 123456.79      */
DECL|method|rounding ()
name|String
name|rounding
parameter_list|()
default|default
literal|"CEILING"
function_decl|;
comment|/**       * Method name to call to apply such customization       * on DataField. This must be the method on the datafield       * itself or you must provide static fully qualified name of       * the class's method e.g: see unit test        * org.apache.camel.dataformat.bindy.csv.BindySimpleCsvFunctionWithExternalMethodTest.replaceToBar       */
DECL|method|method ()
name|String
name|method
parameter_list|()
default|default
literal|""
function_decl|;
block|}
end_annotation_defn

end_unit

