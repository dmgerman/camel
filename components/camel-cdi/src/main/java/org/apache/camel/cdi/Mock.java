begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
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

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|Nonbinding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Qualifier
import|;
end_import

begin_comment
comment|/**  * A qualifier for injecting instances of {@link org.apache.camel.component.mock.MockEndpoint} into a bean.  */
end_comment

begin_annotation_defn
annotation|@
name|Qualifier
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
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
block|,
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|PARAMETER
block|}
argument_list|)
DECL|annotation|Mock
specifier|public
annotation_defn|@interface
name|Mock
block|{
comment|/**      * Returns an optional URI used to create the MockEndpoint      */
annotation|@
name|Nonbinding
DECL|method|value ()
name|String
name|value
parameter_list|()
default|default
literal|""
function_decl|;
comment|/**      * Returns the name of the CamelContext to use to resolve the endpoint for this URI      */
annotation|@
name|Nonbinding
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

