begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_comment
comment|/**  * Generator for Globally unique Strings.  */
end_comment

begin_class
DECL|class|UuidGenerator
specifier|public
class|class
name|UuidGenerator
block|{
DECL|field|instance
specifier|private
specifier|static
name|UuidGenerator
name|instance
init|=
operator|new
name|UuidGenerator
argument_list|()
decl_stmt|;
DECL|method|UuidGenerator ()
name|UuidGenerator
parameter_list|()
block|{     }
comment|/**      * Returns a UUID generator. The instance returned by this method makes use      * of {@link java.util.UUID#randomUUID()} for generating UUIDs. Other      * generation strategies are currently not supported (but maybe added in      * future versions).      *       * @return a UUID generator singleton.      */
DECL|method|get ()
specifier|public
specifier|static
name|UuidGenerator
name|get
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
comment|/**      * Generates a UUID string representation.        *       * @return a UUID string.      */
DECL|method|generateUuid ()
specifier|public
name|String
name|generateUuid
parameter_list|()
block|{
return|return
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

