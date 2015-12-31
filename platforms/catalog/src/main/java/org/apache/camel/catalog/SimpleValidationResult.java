begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
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
DECL|class|SimpleValidationResult
specifier|public
class|class
name|SimpleValidationResult
implements|implements
name|Serializable
block|{
DECL|field|simple
specifier|private
specifier|final
name|String
name|simple
decl_stmt|;
DECL|field|error
specifier|private
name|String
name|error
decl_stmt|;
DECL|method|SimpleValidationResult (String simple)
specifier|public
name|SimpleValidationResult
parameter_list|(
name|String
name|simple
parameter_list|)
block|{
name|this
operator|.
name|simple
operator|=
name|simple
expr_stmt|;
block|}
DECL|method|getSimple ()
specifier|public
name|String
name|getSimple
parameter_list|()
block|{
return|return
name|simple
return|;
block|}
DECL|method|isSuccess ()
specifier|public
name|boolean
name|isSuccess
parameter_list|()
block|{
return|return
name|error
operator|==
literal|null
return|;
block|}
DECL|method|setError (String error)
specifier|public
name|void
name|setError
parameter_list|(
name|String
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
DECL|method|getError ()
specifier|public
name|String
name|getError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
block|}
end_class

end_unit

