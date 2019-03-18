begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_comment
comment|/**  * Represents method alias  */
end_comment

begin_class
DECL|class|ApiMethodAlias
specifier|public
class|class
name|ApiMethodAlias
block|{
DECL|field|methodPattern
specifier|private
name|String
name|methodPattern
decl_stmt|;
DECL|field|methodAlias
specifier|private
name|String
name|methodAlias
decl_stmt|;
DECL|method|ApiMethodAlias ()
specifier|public
name|ApiMethodAlias
parameter_list|()
block|{     }
DECL|method|ApiMethodAlias (String methodPattern, String methodAlias)
specifier|public
name|ApiMethodAlias
parameter_list|(
name|String
name|methodPattern
parameter_list|,
name|String
name|methodAlias
parameter_list|)
block|{
name|this
operator|.
name|methodPattern
operator|=
name|methodPattern
expr_stmt|;
name|this
operator|.
name|methodAlias
operator|=
name|methodAlias
expr_stmt|;
block|}
DECL|method|getMethodPattern ()
specifier|public
name|String
name|getMethodPattern
parameter_list|()
block|{
return|return
name|methodPattern
return|;
block|}
DECL|method|setMethodPattern (String methodPattern)
specifier|public
name|void
name|setMethodPattern
parameter_list|(
name|String
name|methodPattern
parameter_list|)
block|{
name|this
operator|.
name|methodPattern
operator|=
name|methodPattern
expr_stmt|;
block|}
DECL|method|getMethodAlias ()
specifier|public
name|String
name|getMethodAlias
parameter_list|()
block|{
return|return
name|methodAlias
return|;
block|}
DECL|method|setMethodAlias (String methodAlias)
specifier|public
name|void
name|setMethodAlias
parameter_list|(
name|String
name|methodAlias
parameter_list|)
block|{
name|this
operator|.
name|methodAlias
operator|=
name|methodAlias
expr_stmt|;
block|}
block|}
end_class

end_unit

