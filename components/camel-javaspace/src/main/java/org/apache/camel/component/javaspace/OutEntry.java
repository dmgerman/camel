begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.javaspace
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|javaspace
package|;
end_package

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|entry
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * Specialized JavaSpace Entry for managing the request/reply pattern  *   * @version   */
end_comment

begin_class
DECL|class|OutEntry
specifier|public
class|class
name|OutEntry
implements|implements
name|Entry
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7126199811149934838L
decl_stmt|;
DECL|field|correlationId
specifier|public
name|String
name|correlationId
decl_stmt|;
DECL|field|binary
specifier|public
name|Boolean
name|binary
decl_stmt|;
DECL|field|buffer
specifier|public
name|byte
index|[]
name|buffer
decl_stmt|;
DECL|method|OutEntry ()
specifier|public
name|OutEntry
parameter_list|()
block|{
name|correlationId
operator|=
literal|null
expr_stmt|;
name|binary
operator|=
literal|null
expr_stmt|;
name|buffer
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

