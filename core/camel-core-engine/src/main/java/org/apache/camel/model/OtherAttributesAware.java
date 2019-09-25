begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * Models can support being configured with any other attributes to shadow  * existing options to be used for property placeholders.  *<p/>  * For example to override attributes that are configured as a boolean or  * integer type. Then the any attributes can be used to override those existing  * attributes and supporting property placeholders.  */
end_comment

begin_interface
DECL|interface|OtherAttributesAware
specifier|public
interface|interface
name|OtherAttributesAware
block|{
comment|/**      * Adds optional attribute to use as property placeholder      */
DECL|method|getOtherAttributes ()
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|getOtherAttributes
parameter_list|()
function_decl|;
comment|/**      * Adds optional attribute to use as property placeholder      */
DECL|method|setOtherAttributes (Map<QName, Object> otherAttributes)
name|void
name|setOtherAttributes
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|otherAttributes
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

