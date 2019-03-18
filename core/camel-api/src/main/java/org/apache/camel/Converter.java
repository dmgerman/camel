begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * An annotation used to mark classes and methods to indicate code capable of  * converting from a type to another type which are then auto-discovered using  * the<a href="http://camel.apache.org/type-converter.html">Type  * Conversion Support</a>  */
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
DECL|annotation|Converter
specifier|public
annotation_defn|@interface
name|Converter
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
comment|/**      * Whether this converter is a regular converter or a fallback converter.      *      * The difference between a regular converter and a fallback-converter      * is that the fallback is resolved at last if no regular converter could be found.      * Also the method signature is scoped to be generic to allow handling a broader range      * of types trying to be converted. The fallback converter can just return<tt>null</tt>      * if it can not handle the types to convert from/to.      */
DECL|method|fallback ()
DECL|field|false
name|boolean
name|fallback
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Whether or not this fallback converter can be promoted to a first class type converter.      */
DECL|method|fallbackCanPromote ()
DECL|field|false
name|boolean
name|fallbackCanPromote
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Whether to ignore the type converter if it cannot be loaded for some reason.      *<p/>      * This can be used if a Camel component provides multiple components      * where the end user can opt-out some of these components by excluding      * dependencies on the classpath, meaning the type converter would not      * be able to load due class not found errors. But in those cases its      * okay as the component is opted-out.      *<p/>      * Important this configuration must be set on the class-level, not on the method.      */
DECL|method|ignoreOnLoadError ()
DECL|field|false
name|boolean
name|ignoreOnLoadError
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * Whether to let the Camel compiler plugin to generate java source code      * for fast loading of the type converters.      *<p/>      * Important this configuration must be set on the class-level, not on the method.      */
DECL|method|loader ()
DECL|field|false
name|boolean
name|loader
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

