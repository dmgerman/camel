begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|support
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
name|Annotation
import|;
end_import

begin_comment
comment|/**  * There's no simple way to associate a Scope with an annotation which often  * leads Scoping to return null. This interface allows Scope implementations to  * expose its scope annotation  *   * @version  */
end_comment

begin_interface
DECL|interface|HasScopeAnnotation
specifier|public
interface|interface
name|HasScopeAnnotation
block|{
comment|/**      * Returns the scope annotation associated with this object      */
DECL|method|getScopeAnnotation ()
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|getScopeAnnotation
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

