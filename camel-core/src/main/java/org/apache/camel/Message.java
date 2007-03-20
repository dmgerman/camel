begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Represents an inbound or outbound message as part of an {@link Exchange}  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Message
specifier|public
interface|interface
name|Message
block|{
comment|/**      * Access the headers on the message      */
DECL|method|getHeaders ()
specifier|public
name|Headers
name|getHeaders
parameter_list|()
function_decl|;
comment|/**      * Returns the body of the message as a POJO      *      * @returns the body of the message      */
DECL|method|getBody ()
specifier|public
name|Object
name|getBody
parameter_list|()
function_decl|;
comment|/**      * Returns the body as the specified type      *      * @param type the type that the body i      * @return the body of the message as the specified type      */
DECL|method|getBody (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets the body of the message      */
DECL|method|setBody (Object body)
specifier|public
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sets the body of the message as a specific type      */
DECL|method|setBody (Object body, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Creates a copy of this message so that it can be used and possibly modified further in another exchange      *       * @return a new message instance copied from this message      */
DECL|method|copy ()
specifier|public
name|Message
name|copy
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

