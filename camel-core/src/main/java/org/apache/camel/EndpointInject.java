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
comment|/**  * Used to indicate an injection point of an {@link Endpoint}, {@link Producer} or  * {@link ProducerTemplate} into a POJO.  *  * A<a href="http://camel.apache.org/uris.html">URI</a> for an endpoint  * can be specified on this annotation, or a name can be specified which is resolved in the  * {@link org.apache.camel.spi.Registry} such as in your Spring ApplicationContext.  *  * If no ref or uri is specified then the ref is defaulted from the field, property or method name.  *  * @version   */
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
name|FIELD
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|CONSTRUCTOR
block|}
argument_list|)
DECL|annotation|EndpointInject
specifier|public
annotation_defn|@interface
name|EndpointInject
block|{
DECL|method|uri ()
name|String
name|uri
parameter_list|()
default|default
literal|""
function_decl|;
DECL|method|ref ()
name|String
name|ref
parameter_list|()
default|default
literal|""
function_decl|;
DECL|method|property ()
name|String
name|property
parameter_list|()
default|default
literal|""
function_decl|;
DECL|method|context ()
name|String
name|context
parameter_list|()
default|default
literal|""
function_decl|;
block|}
end_annotation_defn

end_unit

