begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
package|;
end_package

begin_class
DECL|class|MyErrorDto
specifier|public
class|class
name|MyErrorDto
block|{
DECL|field|record
specifier|private
name|String
name|record
decl_stmt|;
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
DECL|method|MyErrorDto (String record, String message)
specifier|public
name|MyErrorDto
parameter_list|(
name|String
name|record
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|record
operator|=
name|record
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|getRecord ()
specifier|public
name|String
name|getRecord
parameter_list|()
block|{
return|return
name|record
return|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

