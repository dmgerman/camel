begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_class
DECL|class|SerializableRequestDto
specifier|public
class|class
name|SerializableRequestDto
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|customerName
specifier|public
name|String
name|customerName
decl_stmt|;
DECL|method|SerializableRequestDto ()
specifier|public
name|SerializableRequestDto
parameter_list|()
block|{
name|customerName
operator|=
literal|"Default"
expr_stmt|;
block|}
DECL|method|SerializableRequestDto (String customerName)
specifier|public
name|SerializableRequestDto
parameter_list|(
name|String
name|customerName
parameter_list|)
block|{
name|this
operator|.
name|customerName
operator|=
name|customerName
expr_stmt|;
block|}
block|}
end_class

end_unit

