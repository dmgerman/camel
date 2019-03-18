begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|azure
operator|.
name|common
package|;
end_package

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|OperationContext
import|;
end_import

begin_class
DECL|class|AbstractServiceRequestOptions
specifier|public
specifier|abstract
class|class
name|AbstractServiceRequestOptions
block|{
DECL|field|opContext
specifier|private
name|OperationContext
name|opContext
decl_stmt|;
DECL|method|getOpContext ()
specifier|public
name|OperationContext
name|getOpContext
parameter_list|()
block|{
return|return
name|opContext
return|;
block|}
DECL|method|setOpContext (OperationContext opContext)
specifier|public
name|void
name|setOpContext
parameter_list|(
name|OperationContext
name|opContext
parameter_list|)
block|{
name|this
operator|.
name|opContext
operator|=
name|opContext
expr_stmt|;
block|}
block|}
end_class

end_unit

