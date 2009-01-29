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
comment|/**  * An annotation used to identify in a POJO which property is link to a field of  * a record (csv, ...) a) position identifies the position of the data in the  * record (mandatory) b) name is optional and could be used in the future to  * bind a property which a different name c) pattern which is optional allows to  * define the pattern of the data (useful for Date, ...) d) length allows to  * define for fixed length message the size of the data's block e) precision  * reflects the precision to be used with BigDecimal number  */
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
comment|/**      * position of the data in the record (mandatory)      *       * @return int      */
DECL|method|pos ()
name|int
name|pos
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
comment|/**      * length of the data block (useful for the fixedlength record) (optional in      * this version)      *       * @return int      */
DECL|method|length ()
name|int
name|length
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
block|}
end_annotation_defn

end_unit

