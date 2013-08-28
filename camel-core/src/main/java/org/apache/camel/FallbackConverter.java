begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|ElementType
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * An annotation used to mark methods to indicate code capable of being a  * fallback converter which are then auto-discovered using  * the<a href="http://camel.apache.org/type-converter.html">Type  * Conversion Support</a>.  *<p/>  * The difference between a regular<tt>@Converter</tt> and a<tt>@FallbackConverter</tt>  * is that the fallback is resolved at last if no regular converter could be found.  * Also the method signature is scoped to be generic to allow handling a broader range  * of types trying to be converted. The fallback converter can just return<tt>null</tt>  * if it can not handle the types to convert from/to.  *  * @see org.apache.camel.component.file.GenericFileConverter GenericFileConverter for an example.  *  * @version   */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Documented
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|,
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
DECL|annotation|FallbackConverter
specifier|public
annotation_defn|@interface
name|FallbackConverter
block|{
comment|/**      * Whether or not returning<tt>null</tt> is a valid response.      */
DECL|method|allowNull ()
DECL|field|false
name|boolean
name|allowNull
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Whether or not this fallback converter can be promoted to a first class type converter.      */
DECL|method|canPromote ()
DECL|field|false
name|boolean
name|canPromote
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

